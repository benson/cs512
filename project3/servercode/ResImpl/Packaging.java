package ResImpl;

import ResInterface.*;

import java.util.*;
import java.rmi.*;

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
                atLeastOne = true;
            } catch (Exception e) {
                live[i] = false;
            }
        }
 
        if (!atLeastOne) {
            
        }
    }

    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean sent = false;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].addFlight(id, flightNum, flightSeats, flightPrice);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;

    }
    
    /* Add cars to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addCars(int id, String location, int numCars, int price) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean sent = false;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].addCars(id, location, numCars, price);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;
    }
   
    /* Add rooms to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addRooms(int id, String location, int numRooms, int price) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean sent = false;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].addRooms(id, location, numRooms, price);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;

    }
                
    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        int first = 0;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    if(first == 0)
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
        return 1;

    }  
    /* new customer with providing id */
    public boolean newCustomer(int id, int cid)
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].newCustomer(id, cid);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;


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
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].deleteFlight(id, flightNum);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;

    }   

    public boolean deleteCars(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].deleteCars(id, location);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;
    }

    public boolean deleteRooms(int id, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].deleteRooms(id, location);
                    sent = true;
                } catch (RemoteException e) {
                    live[i] = false;
                }
            }
        }
        if (!sent) {
            throw new RemoteException();
        }
        return true;
    }

    public boolean deleteCustomer(int id,int customer)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].deleteCustomer(id, customer);
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

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].reserveFlight(id, customer, flightNumber);
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

    public boolean reserveCar(int id, int customer, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].reserveCar(id, customer, location);
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

    public boolean reserveRoom(int id, int customer, String location)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].reserveRoom(id, customer, location);
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

    public boolean itinerary(int id,int customer,Vector flightNumbers,String location, boolean Car, boolean Room)
        throws RemoteException, NoSuchElementException, MissingResourceException
    {
        if (!awake) return true;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].itinerary(id, customer, flightNumbers, location, Car, Room);
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

    public int start() throws RemoteException
    {
        if (!awake) return 0;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].start();
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
    public void start(int id) throws RemoteException
    {
        if (!awake) return;

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].start(id);
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

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].commit(id);
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

        //WRITE
        // basic loop
        boolean sent = false;
        for (int i = 0; i < managers; i++) {
            if (live[i]) {
                try {
                    servers[i].shutdown();
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
}