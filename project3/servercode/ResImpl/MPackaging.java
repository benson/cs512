public class MPackaging implements ResourceManager
{
    private int number;
    private ResourceManager[] mwares;
    private boolean[] alive;

    public MPackaging(Registry registry, String base; int number)
    {
        this.number = number;
        this.mwares = new ResourceManager[number];
        this.alive = new boolean[number];

        for (int i = 0; i < number; i++) {
            try {
                mwares[i] = registry.lookup(base + new Integer(i).toString());
                alive[i] = true;
            } catch (Exception e) {
                alive[i] = false;
            }
        }
    }



        boolean hasValue = false;
        TYPE result;
        for (int i = 0; i < number; i++) {
            if (alive[i]) {
                try {
                    if (!hasValue) {
                        mwares[i].wakeUp();
                    }
                    result = mwares[i].METHOD(ARGS);
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



    /* Add seats to a flight.  In general this will be used to create a new
     * flight, but it should be possible to add seats to an existing flight.
     * Adding to an existing flight should overwrite the current price of the
     * available seats.
     *
     * @return success.
     */
    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) 
	throws RemoteException, NoSuchElementException, MissingResourceException;
    
    /* Add cars to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addCars(int id, String location, int numCars, int price) 
	throws RemoteException, NoSuchElementException, MissingResourceException;
   
    /* Add rooms to a location.  
     * This should look a lot like addFlight, only keyed on a string location
     * instead of a flight number.
     */
    public boolean addRooms(int id, String location, int numRooms, int price) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
			    
    /* new customer just returns a unique customer identifier */
    public int newCustomer(int id) 
    throws RemoteException, NoSuchElementException, MissingResourceException;    
    /* new customer with providing id */
    public boolean newCustomer(int id, int cid)
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /**
     *   Delete the entire flight.
     *   deleteflight implies whole deletion of the flight.  
     *   all seats, all reservations.  If there is a reservation on the flight, 
     *   then the flight cannot be deleted
     *
     * @return success.
     */   
    public boolean deleteFlight(int id, int flightNum) 
    throws RemoteException, NoSuchElementException, MissingResourceException;    
    /* Delete all Cars from a location.
     * It may not succeed if there are reservations for this location
     *
     * @return success
     */		    
    public boolean deleteCars(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* Delete all Rooms from a location.
     * It may not succeed if there are reservations for this location.
     *
     * @return success
     */
    public boolean deleteRooms(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* deleteCustomer removes the customer and associated reservations */
    public boolean deleteCustomer(int id,int customer) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* queryFlight returns the number of empty seats. */
    public int queryFlight(int id, int flightNumber) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* return the number of cars available at a location */
    public int queryCars(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* return the number of rooms available at a location */
    public int queryRooms(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* return a bill */
    public String queryCustomerInfo(int id,int customer) 
    throws RemoteException, NoSuchElementException, MissingResourceException;    
    /* queryFlightPrice returns the price of a seat on this flight. */
    public int queryFlightPrice(int id, int flightNumber) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* return the price of a car at a location */
    public int queryCarsPrice(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* return the price of a room at a location */
    public int queryRoomsPrice(int id, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* Reserve a seat on this flight*/
    public boolean reserveFlight(int id, int customer, int flightNumber) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* reserve a car at this location */
    public boolean reserveCar(int id, int customer, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;
    /* reserve a room certain at this location */
    public boolean reserveRoom(int id, int customer, String location) 
    throws RemoteException, NoSuchElementException, MissingResourceException;

    /* reserve an itinerary */
    public boolean itinerary(int id,int customer,Vector flightNumbers,String location, boolean Car, boolean Room)
    throws RemoteException, NoSuchElementException, MissingResourceException;

    public int start() throws RemoteException;
    public void start(int id) throws RemoteException;
    public boolean commit(int id) throws RemoteException, NoSuchElementException, MissingResourceException;
    public void abort(int id) throws RemoteException, NoSuchElementException;
    public boolean shutdown() throws RemoteException;
}