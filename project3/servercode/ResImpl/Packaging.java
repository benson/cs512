package ResImpl;

import java.util.*;
import java.rmi.*;

import ResInterface.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Packaging implements ResourceManager
{
    private int managers;
    private boolean[] live;
    private ResourceManager[] servers;

    private boolean awake;

    public void wakeUp()
    {
        awake = true;
    }

    public Packaging(Registry registry, String base, int managers)
    {
        boolean atLeastOne = false;

        this.managers = managers;
        this.servers = new ResourceManager[managers];
        this.live = new boolean[managers];
        this.awake = false;

        for (int i = 0; i < managers; i++) {
            try {
                servers[i] = (ResourceManager) registry.lookup(base + new Integer(i).toString());
                live[i] = true;
                System.out.println("in packaging init, " + i + " is live");
                atLeastOne = true;
            } catch (Exception e) {
                System.out.println("caught exception e, " + i + " set to dead");
                System.out.println(e);
                live[i] = false;
            }
        }
 
        if (!atLeastOne) {
            
        }
    }

    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        boolean ret = false;
        boolean sent = false;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    ret = ret || servers[i].addFlight(id, flightNum, flightSeats, flightPrice);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }
    
    /* Add cars to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addCars(int id, String location, int numCars, int price) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        System.out.println("In packaging addcars");
        if (!awake) return true;

        boolean ret = false;
        boolean sent = false;
        boolean temp;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            System.out.println("In packaging for loop, i = " + i);
            if (live[i]) {
                System.out.println("i is live");
                try {
                    System.out.println("inside try");
                    temp = servers[i].addCars(id, location, numCars, price);
                    ret = ret || temp;
                    System.out.println("after setting ret");
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }
   
    /* Add rooms to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addRooms(int id, String location, int numRooms, int price) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        boolean ret = false;
        boolean sent = false;
        boolean temp;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].addRooms(id, location, numRooms, price);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;

    }
                
    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        int ret = 0;

        //WRITE
        // basic loop
        boolean sent = false;
        int first = -1;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    if(first < 0)
                        first = servers[i].newCustomer(id);
                    else
                        servers[i].newCustomer(id, first);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return first;
    }

    /* new customer with providing id */
    public boolean newCustomer(int id, int cid)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        boolean ret = false;
        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].newCustomer(id, cid);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }

    /**
     *   Delete the entire flight.
     *   deleteflight implies whole deletion of the flight.  
     *   all seats, all reservations.  If there is a reservation on the flight, 
     *   then the flight cannot be deleted
     *
     * @return success.
     */   
    public boolean deleteFlight(int id, int flightNum) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean ret = false;
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].deleteFlight(id, flightNum);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }   

    public boolean deleteCars(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].deleteCars(id, location);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }

    public boolean deleteRooms(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].deleteRooms(id, location);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }

    public boolean deleteCustomer(int id,int customer)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].deleteCustomer(id, customer);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }

    public int queryFlight(int id, int flightNumber)
        throws RemoteException, MissingResourceException, NoSuchElementException 
    {
        if (!awake) return 0;

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    return servers[i].queryFlight(id, flightNumber);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public int queryCars(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return 0;

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    return servers[i].queryCars(id, location);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public int queryRooms(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return 0;

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    return servers[i].queryRooms(id, location);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public String queryCustomerInfo(int id,int customer)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return "";

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    System.out.println("In querycustomerinfo PACKAGING");
                    return servers[i].queryCustomerInfo(id, customer);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public int queryFlightPrice(int id, int flightNumber)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return 0;

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    return servers[i].queryFlightPrice(id, flightNumber);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public int queryCarsPrice(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return 0;

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    return servers[i].queryCarsPrice(id, location);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public int queryRoomsPrice(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return 0;

        //READ
        // basic loop
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    return servers[i].queryRoomsPrice(id, location);
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        throw new RemoteException();
    }

    public boolean reserveFlight(int id, int customer, int flightNumber)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].reserveFlight(id, customer, flightNumber);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }

    public boolean reserveCar(int id, int customer, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].reserveCar(id, customer, location);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }

        return ret;
    }

    public boolean reserveRoom(int id, int customer, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;
        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].reserveRoom(id, customer, location); 
                    ret = ret || temp;                
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return sent;
    }

    public boolean itinerary(int id,int customer,Vector flightNumbers,String location, boolean Car, boolean Room)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].itinerary(id, customer, flightNumbers, location, Car, Room);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }

    public int start() throws RemoteException
    {
        if (!awake) return 0;
        int ret = 0;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    ret = servers[i].start();
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }
    public void start(int id) throws RemoteException
    {
        if (!awake) return;
        System.out.println("In Packaging.start(int)");
        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                System.out.println(i + " is live");
                try {
                    System.out.println("Inside try, about to call servers[" + i + "].start(" + id + ")");
                    System.out.println("servers[i] = " + servers[i]);
                    servers[i].start(id);
                    System.out.println("After calling start, didn't throw an error");
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }

    }
    public boolean commit(int id) throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].commit(id);
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }

        return ret;
    }

    public void abort(int id) throws RemoteException, NoSuchElementException
    {
        if (!awake) return;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].abort(id);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
    }

    public boolean shutdown() throws RemoteException
    {
        if (!awake) return true;
        boolean ret = false;

        //WRITE
        // basic loop
        boolean sent = false;
        boolean temp;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    temp = servers[i].shutdown();
                    ret = ret || temp;
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return ret;
    }
}