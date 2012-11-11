package ResImpl;

import java.util.Hashtable;
import ResInterface.*;

import java.util.*;
import java.rmi.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;



class TransactionManager
{
	Hashtable<Integer, LocalTransaction> ht;
	public TransactionManager()
	{
		this.ht = new Hashtable<Integer, LocalTransaction>();
	}

	private LocalTransaction get(int id)
	{
		return ht.get(new Integer(id));
	}

	public int start(int id)
	{
		ht.put(new Integer(id), new LocalTransaction(id));
		return id;
	}

	public void remove(int id)
	{
		ht.remove(new Integer(id));
	}

	public void addWriteOperation(int id, String objectS, RMItem previous)
	{
		LocalTransaction t = get(id);
		if(!t.hasHistory(objectS))
		{
			t.addToHistory(objectS, previous);
		}
	}

	public Hashtable<String, RMItem> getHistory(int id)
	{
		return get(id).getHistory();
	}
}

class LocalTransaction
{
	private Vector locks;
	private int id;
	private Hashtable<String, RMItem> history;
	public LocalTransaction(int id)
	{
		this.id = id;
		this.locks = new Vector();
		this.history = new Hashtable<String, RMItem>();
	}

	public boolean hasHistory(String objectS)
	{
		return history.contains(objectS);
	}

	public Hashtable<String, RMItem> getHistory()
	{
		return history;
	}

	public void addToHistory(String objectS, RMItem item)
	{
		history.put(objectS, item);
	}
}