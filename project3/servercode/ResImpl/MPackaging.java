package ResImpl;

import java.util.*;
import java.rmi.*;
import ResInterface.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MPackaging implements ResourceManager
{
    private int number;
    private ResourceManager[] mwares;
    private boolean[] alive;

    public MPackaging(Registry registry, String base, int number)
    {
        this.number = number;
        this.mwares = new ResourceManager[number];
        this.alive = new boolean[number];

        for (int i = 0; i < number; i++) {
            try {
                mwares[i] = (ResourceManager) registry.lookup(base + new Integer(i).toString());
                alive[i] = true;
            } catch (Exception e) {
                alive[i] = false;
            }
        }
    }

    /* Add seats to a flight.  In general this will be used to create a new
     * flight, but it should be possible to add seats to an existing flight.
     * Adding to an existing flight should overwrite the current price of the
     * available seats.
     *
     * @return success.
     */
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) 
	throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].addFlight(id, flightNum, flightSeats, flightPrice);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    
    /* Add cars to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addCars(int id, String location, int numCars, int price) 
	throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                        result = mwares[i].addCars(id, location, numCars, price);
                        hasValue = true;
                    }
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* Add rooms to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addRooms(int id, String location, int numRooms, int price) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].addRooms(id, location, numRooms, price);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (result == -1)   // try to wake up first middleware
                        mwares[i].wakeUp();
                    if(result != -1)    // if result has already been set - use it
                        mwares[i].newCustomer(id, result);
                    else    // result hasn't been set, set it 
                        result = mwares[i].newCustomer(id); 
                } catch (Exception e) {
                    alive[i] = false;
                    abort(id);
                    throw new MissingResourceException("foo", "bar", "vars");
                }
            }
        }
        if (result != -1) {
            return result;
        } else {
            throw new RemoteException();
        }

    } 
    /* new customer with providing id */
    public boolean newCustomer(int id, int cid)
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].newCustomer(id, cid);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                    abort(id);
                    throw new MissingResourceException("foo", "bar", "vars");
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

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
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].deleteFlight(id, flightNum) ;
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    } 
    /* Delete all Cars from a location.
     * It may not succeed if there are reservations for this location
     *
     * @return success
     */		    
    public boolean deleteCars(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].deleteCars(id, location) ;
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* Delete all Rooms from a location.
     * It may not succeed if there are reservations for this location.
     *
     * @return success
     */
    public boolean deleteRooms(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].deleteRooms(id, location) ;
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* deleteCustomer removes the customer and associated reservations */
    public boolean deleteCustomer(int id,int customer) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].deleteCustomer(id, customer) ;
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* queryFlight returns the number of empty seats. */
    public int queryFlight(int id, int flightNumber) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].queryFlight(id, flightNumber);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* return the number of cars available at a location */
    public int queryCars(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].queryCars(id, location);
                    if (!hasValue)
                    {
                        result = temp;
                        hasValue = true;
                    } 
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* return the number of rooms available at a location */
    public int queryRooms(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].queryRooms(id, location);
                    if (!hasValue)
                    {
                        result = temp;
                        hasValue = true;
                    }                    
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* return a bill */
    public String queryCustomerInfo(int id,int customer) 
    throws RemoteException, NoSuchElementException, MissingResourceException 
    {
        boolean hasValue = false;
        String temp = "";
        String result = null;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].queryCustomerInfo(id, customer);
                    if (!hasValue) {
                        result = temp;
                        hasValue = true;
                    }
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }  
    /* queryFlightPrice returns the price of a seat on this flight. */
    public int queryFlightPrice(int id, int flightNumber) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].queryFlightPrice(id, flightNumber);
                    if(!hasValue) {
                        result = temp;
                        hasValue = true;
                    }                    
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* return the price of a car at a location */
    public int queryCarsPrice(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].queryCarsPrice(id, location);
                    if(!hasValue) {
                        result = temp;
                        hasValue = true;
                    } 
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* return the price of a room at a location */
    public int queryRoomsPrice(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].queryRoomsPrice(id, location);
                    if (!hasValue)
                    {
                        result = temp;
                        hasValue = true;
                    } 
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* Reserve a seat on this flight*/
    public boolean reserveFlight(int id, int customer, int flightNumber) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].reserveFlight(id, customer, flightNumber);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* reserve a car at this location */
    public boolean reserveCar(int id, int customer, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].reserveCar(id, customer, location);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    /* reserve a room certain at this location */
    public boolean reserveRoom(int id, int customer, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].reserveRoom(id, customer, location);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }

    /* reserve an itinerary */
    public boolean itinerary(int id,int customer,Vector flightNumbers,String location, boolean Car, boolean Room)
    throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].itinerary(id,customer,flightNumbers,location, Car, Room);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                    abort(id);
                    throw new MissingResourceException("foo", "bar", "vars");
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }

    public int start() throws RemoteException
    {
        boolean hasValue = false;
        int temp = -1, result = -1;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    temp = mwares[i].start();
                    if (!hasValue)
                    {
                        result = temp;
                        hasValue = true;
                    } 
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    public void start(int id) throws RemoteException
    {
        boolean hasValue = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    mwares[i].start(id);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return;
        } else {
            throw new RemoteException();
        }

    }
    public boolean commit(int id) throws RemoteException, NoSuchElementException, MissingResourceException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].commit(id);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }
    public void abort(int id) throws RemoteException, NoSuchElementException
    {
        boolean hasValue = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    mwares[i].abort(id);
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return;
        } else {
            throw new RemoteException();
        }

    }
    public boolean shutdown() throws RemoteException
    {
        boolean hasValue = false;
        boolean result = false;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].shutdown();
                    hasValue = true;
                } catch (Exception e) {
                    alive[i] = false;
                }
            }
        }
        if (hasValue) {
            return result;
        } else {
            throw new RemoteException();
        }

    }

    public void wakeUp()
    {
        return;
    }
}