package ResImpl;

import java.net.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class ClientHandler extends Thread {
    private RMHashtable m_itemHT;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public ClientHandler(Socket connection, RMHashtable table) {
        this.connection = connection;
        this.m_itemHT = table;
        try {
            input = new ObjectInputStream(connection.getInputStream());
            output = new ObjectOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            System.err.println("Couldn't get in/out streams for handler.");
        }
    }

    public void run() {
        MethodCall call;
        while (true) {
            try {
                call = (MethodCall) input.readObject();
            } catch (Exception e) {
                System.err.println ("failed to read object from client: " + e);
                return;
            }
            
            try {
                output.writeObject(handleCall(call));
            } catch (IOException e) {
                System.err.println ("failed to write object to client: " + e);
                return;
            }
        }
    }

    private Serializable handleCall(MethodCall call) {
        Vector args = call.arguments;

        try {
            switch (call.method) {
            case ADD_FLIGHT:
                return new Boolean(addFlight(((Integer) args.get(0)).intValue(),
                                             ((Integer) args.get(1)).intValue(),
                                             ((Integer) args.get(2)).intValue(),
                                             ((Integer) args.get(3)).intValue()));
                // break;
            case ADD_CARS:
                return new Boolean(addCars(((Integer) args.get(0)).intValue(),
                                           (String) args.get(1),
                                           ((Integer) args.get(2)).intValue(),
                                           ((Integer) args.get(3)).intValue()));
                // break;
            case ADD_ROOMS:
                return new Boolean(addRooms(((Integer) args.get(0)).intValue(),
                                            (String) args.get(1),
                                            ((Integer) args.get(2)).intValue(),
                                            ((Integer) args.get(3)).intValue()));
                // break;
            case NEW_CUSTOMER1:
                return new Integer(newCustomer(((Integer) args.get(0)).intValue()));
                // break;
            case NEW_CUSTOMER2:
                return new Boolean(newCustomer(((Integer) args.get(0)).intValue(),
                                               ((Integer) args.get(1)).intValue()));
                // break;
            case DELETE_FLIGHT:
                return new Boolean(deleteFlight(((Integer) args.get(0)).intValue(),
                                                ((Integer) args.get(1)).intValue()));
                // break;
            case DELETE_CARS:
                return new Boolean(deleteCars(((Integer) args.get(0)).intValue(),
                                              (String) args.get(1)));
                // break;
            case DELETE_ROOMS:
                return new Boolean(deleteRooms(((Integer) args.get(0)).intValue(),
                                               (String) args.get(1)));
                // break;
            case DELETE_CUSTOMER:
                return new Boolean(deleteCustomer(((Integer) args.get(0)).intValue(),
                                                  ((Integer) args.get(1)).intValue()));
                // break;
            case QUERY_FLIGHT:
                return new Integer(queryFlight(((Integer) args.get(0)).intValue(),
                                               ((Integer) args.get(1)).intValue()));
                // break;
            case QUERY_CARS:
                return new Integer(queryCars(((Integer) args.get(0)).intValue(),
                                             (String) args.get(1)));
                // break;
            case QUERY_ROOMS:
                return new Integer(queryRooms(((Integer) args.get(0)).intValue(),
                                              (String) args.get(1)));
                // break;
            case QUERY_CUSTOMER_INFO:
                return new String(queryCustomerInfo(((Integer) args.get(0)).intValue(),
                                                    ((Integer) args.get(1)).intValue()));
                // break;
            case QUERY_FLIGHT_PRICE:
                return new Integer(queryFlightPrice(((Integer) args.get(0)).intValue(),
                                                    ((Integer) args.get(1)).intValue()));
                // break;
            case QUERY_CARS_PRICE:
                return new Integer(queryCarsPrice(((Integer) args.get(0)).intValue(),
                                                  (String) args.get(1)));
                // break;
            case QUERY_ROOMS_PRICE:
                return new Integer(queryRoomsPrice(((Integer) args.get(0)).intValue(),
                                                   (String) args.get(1)));
                // break;
            case RESERVE_FLIGHT:
                return new Boolean(reserveFlight(((Integer) args.get(0)).intValue(),
                                                 ((Integer) args.get(1)).intValue(),
                                                 ((Integer) args.get(2)).intValue()));
                // break;
            case RESERVE_CAR:
                return new Boolean(reserveCar(((Integer) args.get(0)).intValue(),
                                              ((Integer) args.get(1)).intValue(),
                                              (String) args.get(2)));
                // break;
            case RESERVE_ROOM:
                return new Boolean(reserveRoom(((Integer) args.get(0)).intValue(),
                                               ((Integer) args.get(1)).intValue(),
                                               (String) args.get(2)));
                // break;
                // case ITINERARY:
                //     return new Boolean(itinerary(((Integer) args.get(0)).intValue(),
                //                                  ((Integer) args.get(1)).intValue(),
                //                                  (Vector) args.get(2),
                //                                  (String) args.get(3),
                //                                  (Boolean) args.get(4),
                //                                  (Boolean) args.get(5)));
                //     break;
            }
        } catch (Exception e) {
            System.err.println("welp.");
        }
        return new Boolean(false);
    }     
	// Reads a data item
	private RMItem readData( int id, String key )
	{
		synchronized(m_itemHT){
			return (RMItem) m_itemHT.get(key);
		}
	}
    
	// Writes a data item
	private void writeData( int id, String key, RMItem value )
	{
		synchronized(m_itemHT){
			m_itemHT.put(key, value);
		}
	}
	
	// Remove the item out of storage
	protected RMItem removeData(int id, String key){
		synchronized(m_itemHT){
			return (RMItem)m_itemHT.remove(key);
		}
	}
	
	
	// deletes the entire item
	protected boolean deleteItem(int id, String key)
	{
		ReservableItem curObj = (ReservableItem) readData( id, key );
		// Check if there is such an item in the storage
		if( curObj == null ) {
			return false;
		} else {
			if(curObj.getReserved()==0){
				removeData(id, curObj.getKey());
				return true;
			}
			else{
				return false;
			}
		} 
	}
	

	// query the number of available seats/rooms/cars
	protected int queryNum(int id, String key) {
		ReservableItem curObj = (ReservableItem) readData( id, key);
		int value = 0;  
		if( curObj != null ) {
			value = curObj.getCount();
		} 
		return value;
	}	
	
	// query the price of an item
	protected int queryPrice(int id, String key){
		ReservableItem curObj = (ReservableItem) readData( id, key);
		int value = 0; 
		if( curObj != null ) {
			value = curObj.getPrice();
		} 
		return value;		
	}
	
	// reserve an item
	protected boolean reserveItem(int id, int customerID, String key, String location){
		// Read customer object if it exists (and read lock it)
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );		
		if( cust == null ) {
			return false;
		} 
		
		// check if the item is available
		ReservableItem item = (ReservableItem)readData(id, key);
		if(item==null){
			return false;
		}else if(item.getCount()==0){
			return false;
		}else{			
			cust.reserve( key, location, item.getPrice());		
			writeData( id, cust.getKey(), cust );
			
			// decrease the number of available items in the storage
			item.setCount(item.getCount() - 1);
			item.setReserved(item.getReserved()+1);
			
			return true;
		}		
	}
	
	// Create a new flight, or add seats to existing flight
	//  NOTE: if flightPrice <= 0 and the flight already exists, it maintains its current price
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice)
		throws RemoteException
	{
		Flight curObj = (Flight) readData( id, Flight.getKey(flightNum) );
		if( curObj == null ) {
			// doesn't exist...add it
			Flight newObj = new Flight( flightNum, flightSeats, flightPrice );
			writeData( id, newObj.getKey(), newObj );
		} else {
			// add seats to existing flight and update the price...
			curObj.setCount( curObj.getCount() + flightSeats );
			if( flightPrice > 0 ) {
				curObj.setPrice( flightPrice );
			} 
			writeData( id, curObj.getKey(), curObj );
		} // else
		return(true);
	}


	
	public boolean deleteFlight(int id, int flightNum)
		throws RemoteException
	{
		return deleteItem(id, Flight.getKey(flightNum));
	}



	// Create a new room location or add rooms to an existing location
	//  NOTE: if price <= 0 and the room location already exists, it maintains its current price
	public boolean addRooms(int id, String location, int count, int price)
		throws RemoteException
	{
		Hotel curObj = (Hotel) readData( id, Hotel.getKey(location) );
		if( curObj == null ) {
			Hotel newObj = new Hotel( location, count, price );
			writeData( id, newObj.getKey(), newObj );
		} else {
			// add count to existing object and update price...
			curObj.setCount( curObj.getCount() + count );
			if( price > 0 ) {
				curObj.setPrice( price );
			} 
			writeData( id, curObj.getKey(), curObj );
    		} 
		return(true);
	}

	// Delete rooms from a location
	public boolean deleteRooms(int id, String location)
		throws RemoteException
	{
		return deleteItem(id, Hotel.getKey(location));
		
	}

	// Create a new car location or add cars to an existing location
	//  NOTE: if price <= 0 and the location already exists, it maintains its current price
	public boolean addCars(int id, String location, int count, int price)
		throws RemoteException
	{
		Car curObj = (Car) readData( id, Car.getKey(location) );
		if( curObj == null ) {
			// car location doesn't exist...add it
			Car newObj = new Car( location, count, price );
			writeData( id, newObj.getKey(), newObj );
		} else {
			// add count to existing car location and update price...
			curObj.setCount( curObj.getCount() + count );
			if( price > 0 ) {
				curObj.setPrice( price );
			} // if
			writeData( id, curObj.getKey(), curObj );
		} // else
		return(true);
	}


	// Delete cars from a location
	public boolean deleteCars(int id, String location)
		throws RemoteException
	{
		return deleteItem(id, Car.getKey(location));
	}



	// Returns the number of empty seats on this flight
	public int queryFlight(int id, int flightNum)
		throws RemoteException
	{
		return queryNum(id, Flight.getKey(flightNum));
	}

	// Returns price of this flight
	public int queryFlightPrice(int id, int flightNum )
		throws RemoteException
	{
		return queryPrice(id, Flight.getKey(flightNum));
	}


	// Returns the number of rooms available at a location
	public int queryRooms(int id, String location)
		throws RemoteException
	{
		return queryNum(id, Hotel.getKey(location));
	}


	
	
	// Returns room price at this location
	public int queryRoomsPrice(int id, String location)
		throws RemoteException
	{
		return queryPrice(id, Hotel.getKey(location));
	}


	// Returns the number of cars available at a location
	public int queryCars(int id, String location)
		throws RemoteException
	{
		return queryNum(id, Car.getKey(location));
	}


	// Returns price of cars at this location
	public int queryCarsPrice(int id, String location)
		throws RemoteException
	{
		return queryPrice(id, Car.getKey(location));
	}

	// Returns data structure containing customer reservation info. Returns null if the
	//  customer doesn't exist. Returns empty RMHashtable if customer exists but has no
	//  reservations.
	public RMHashtable getCustomerReservations(int id, int customerID)
		throws RemoteException
	{
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			return null;
		} else {
			return cust.getReservations();
		} // if
	}

	// return a bill
	public String queryCustomerInfo(int id, int customerID)
		throws RemoteException
	{
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			return "";   // NOTE: don't change this--WC counts on this value indicating a customer does not exist...
		} else {
				String s = cust.printBill();
				System.out.println( s );
				return s;
		} // if
	}

  // customer functions
  // new customer just returns a unique customer identifier
	
  public int newCustomer(int id)
		throws RemoteException
	{
		// Generate a globally unique ID for the new customer
		int cid = Integer.parseInt( String.valueOf(id) +
								String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
								String.valueOf( Math.round( Math.random() * 100 + 1 )));
		Customer cust = new Customer( cid );
		writeData( id, cust.getKey(), cust );
		return cid;
	}

	// I opted to pass in customerID instead. This makes testing easier
  public boolean newCustomer(int id, int customerID )
		throws RemoteException
	{
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			cust = new Customer(customerID);
			writeData( id, cust.getKey(), cust );
			return true;
		} else {
			return false;
		} // else
	}


	// Deletes customer from the database. 
	public boolean deleteCustomer(int id, int customerID)
			throws RemoteException
	{
		Customer cust = (Customer) readData( id, Customer.getKey(customerID) );
		if( cust == null ) {
			return false;
		} else {			
			// Increase the reserved numbers of all reservable items which the customer reserved. 
			RMHashtable reservationHT = cust.getReservations();
			for(Enumeration e = reservationHT.keys(); e.hasMoreElements();){		
				String reservedkey = (String) (e.nextElement());
				ReservedItem reserveditem = cust.getReservedItem(reservedkey);
				ReservableItem item  = (ReservableItem) readData(id, reserveditem.getKey());
				item.setReserved(item.getReserved()-reserveditem.getCount());
				item.setCount(item.getCount()+reserveditem.getCount());
			}
			
			// remove the customer from the storage
			removeData(id, cust.getKey());
			
			return true;
		} // if
	}

	
	// Adds car reservation to this customer. 
	public boolean reserveCar(int id, int customerID, String location)
		throws RemoteException
	{
		return reserveItem(id, customerID, Car.getKey(location), location);
	}


	// Adds room reservation to this customer. 
	public boolean reserveRoom(int id, int customerID, String location)
		throws RemoteException
	{
		return reserveItem(id, customerID, Hotel.getKey(location), location);
	}
	// Adds flight reservation to this customer.  
	public boolean reserveFlight(int id, int customerID, int flightNum)
		throws RemoteException
	{
		return reserveItem(id, customerID, Flight.getKey(flightNum), String.valueOf(flightNum));
	}
	
	// /* reserve an itinerary */
    // public boolean itinerary(int id,int customer,Vector flightNumbers,String location,boolean Car,boolean Room)
	// throws RemoteException {
    // 	boolean boolCar = true;
	//     boolean boolRoom = true;
	//     boolean boolFlights = true;
	//     for (int i = 0; i < flightNumbers.size(); i++)
	//     {
	//         boolFlights = flightRM.reserveFlight(id, customer, Integer.parseInt((String)flightNumbers.get(i))) && boolFlights;
	//     }
	    
    // 	if(Car)
    // 	    boolCar = carRM.reserveCar(id, customer, location);
    // 	if(Room)
    //         boolRoom = roomRM.reserveRoom(id, customer, location);
    //     return boolFlights && boolCar && boolRoom;
    // }
}
