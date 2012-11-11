package ResImpl;

import java.util.Hashtable;
import ResInterface.*;
import NewExceptions.*;

import java.util.*;
import java.rmi.*;

import java.rmi.RemoteException;

class TMDispatcher
{
	private Hashtable<Integer, Transaction> table;
	private Vector<Integer> aborted;
	private int newId = 0;

	public TMDispatcher() {
		table = new Hashtable<Integer, Transaction>();
		aborted = new Vector<Integer>();
	}

	public int start()
	{
		int id = newId++;
		table.put(new Integer(id), new Transaction(id));
		return id;
	}

	public Transaction get(int id) throws InvalidTransactionException, TransactionAbortedException
	{
		Transaction t = table.get(new Integer(id));
		if (t == null)
		{
			if (aborted.contains(new Integer(id)))
			{
				throw new TransactionAbortedException();
			}
			else
			{
				throw new InvalidTransactionException();
			}
		}
		return t;
	}

	public boolean commit(int id) throws InvalidTransactionException, TransactionAbortedException
	{
		boolean result = get(id).commit();
		table.remove(new Integer(id));
		return result;
	}

	public void abort(int id) throws InvalidTransactionException
	{
		Transaction t;
		try
		{
			t = get(id);
			t.abort();
			aborted.add(id);
			table.remove(new Integer(id));
		}
		catch (TransactionAbortedException e)
		{
			return;
		}
	}

	// CALL THIS SHIT WHENEVER YOU DISPATCH A READ OR WRITE MOTHERFUCKER
	public void enlist(int id, ResourceManager rm) throws InvalidTransactionException, TransactionAbortedException
	{
		get(id).enlist(rm);
	}
}

class Transaction
{
	private int id;
	private Vector<ResourceManager> enlisted;

	public Transaction(int id)
	{
		this.id = id;
		this.enlisted = new Vector<ResourceManager>();
	}

	public void enlist(ResourceManager rm)
	{
		if (!enlisted.contains(rm))
		{
			enlisted.add(rm);
			try
			{
				rm.start(id);
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
				rm.abort(id);
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
				return rm.commit(id);
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