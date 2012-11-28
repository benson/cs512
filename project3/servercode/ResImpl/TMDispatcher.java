package ResImpl;

import java.util.Hashtable;
import ResInterface.*;

import java.util.*;
import java.rmi.*;

import java.rmi.RemoteException;

class TMDispatcher
{
	private Hashtable<Integer, Transaction> table;
	private Vector<Integer> aborted;
	private int newId = 0;
	private Timeouter to;
    boolean awake;

	public TMDispatcher() {
		table = new Hashtable<Integer, Transaction>();
		aborted = new Vector<Integer>();
		to = new Timeouter(this);
        awake = false;
		to.start();
	}

    public void wakeUp()
    {
        awake = true;
    }

	public int start()
	{
		int id = newId++;
		table.put(new Integer(id), new Transaction(id));
		return id;
	}

	public Transaction get(int id) throws MissingResourceException, NoSuchElementException
	{
		System.out.print("OPEN TXNS: ");
		for (Enumeration e = table.keys(); e.hasMoreElements();)
		{
			System.out.print(e.nextElement() + " ");
		}
		System.out.println();

		Transaction t = table.get(new Integer(id));
		if (t == null)
		{
			if (aborted.contains(new Integer(id)))
			{
				throw new MissingResourceException("transaction " + id + " aborted", "ABORTED", new Integer(id).toString());
			}
			else
			{
				throw new NoSuchElementException();
			}
		}
		return t;
	}

	public boolean commit(int id) throws MissingResourceException, NoSuchElementException
	{
		boolean result = get(id).commit();
		table.remove(new Integer(id));
		return result;
	}

	public void shutdown()
	{
		for (Enumeration e = table.keys(); e.hasMoreElements();)
		{
			abort((Integer) e.nextElement());
		}
		to.interrupt();
		try
		{
			to.join();
		}
		catch(InterruptedException e)
		{
		}
	}

	public void decrementTime()
	{
		synchronized(table)
		{
			Integer key;
			for (Enumeration e = table.keys(); e.hasMoreElements();)
			{
				key = (Integer)e.nextElement();
				if (table.get(key).decrementTime() == 0)
				{
					System.out.println("Transaction " + key + " just timed out.");
					abort(key.intValue());
				}
			}
		}
	}

	public void abort(int id) throws NoSuchElementException
	{
		synchronized(table)
		{
			Transaction t;
			t = get(id);
			t.abort();
			aborted.add(id);
			table.remove(new Integer(id));
		}
	}

	// CALL THIS SHIT WHENEVER YOU DISPATCH A READ OR WRITE MOTHERFUCKER
	public void enlist(int id, ResourceManager rm) throws MissingResourceException, NoSuchElementException
	{
		synchronized (table)
		{
			get(id).enlist(rm);
		}
	}
}

class Transaction
{
	private static int maxTime = 30;
	private int id;
	private Vector<ResourceManager> enlisted;
	private int timeToLive;

	public Transaction(int id)
	{
		this.id = id;
		this.enlisted = new Vector<ResourceManager>();
		resetTime();
	}

	public void resetTime()
	{
		timeToLive = maxTime;
	}

	public int decrementTime()
	{
		return --timeToLive;
	}

	public void enlist(ResourceManager rm)
	{
		resetTime();
		if (!enlisted.contains(rm))
		{
			enlisted.add(rm);
			try
			{
				if (awake) rm.start(id);
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		}
	}

	public void abort() {
		for (ResourceManager rm : enlisted)
		{
			try
			{
				if (awake) rm.abort(id);
			}
			catch (Exception e)
			{
				System.err.println(e);
			}
		}
	}

	public boolean commit() {
		boolean error = false;

		for (ResourceManager rm : enlisted)
		{
			try
			{
				if (awake) return rm.commit(id);
			}
			catch (Exception e)
			{
				System.err.println(e);
				error = true;
			}
		}

		return error;
	}
}

class Timeouter extends Thread
{
	private TMDispatcher tm;

	public Timeouter(TMDispatcher tm)
	{
		this.tm = tm;
	}

	public void run()
	{
		while (true)
		{
			try
			{
				sleep(1000);
			}
			catch(InterruptedException e)
			{
				break;
			}
			tm.decrementTime();
		}
	}
}
