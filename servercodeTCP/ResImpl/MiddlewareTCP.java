package ResImpl;

import ResInterface.*;

import java.util.*;
import java.rmi.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class MiddlewareTCP implements ResourceManager
{
    static ResourceManager flightRM = null;
    static ResourceManager roomRM = null;
    static ResourceManager carRM = null;
    
    public static void main(String args[])
	{	    
	    // PARAMS: server_where_rms_are_hosted FlightRM RoomRM CarRM

        /*  SERVER  */	 
        try 
        {
            // create a new Server object
            Middleware serverObj = new Middleware();
            // dynamically generate the stub (client proxy)
            ResourceManager rm = (ResourceManager) UnicastRemoteObject.exportObject(serverObj, 0);

            // Bind the remote object's stub in the registry
            Registry registry = LocateRegistry.getRegistry(2468);
            registry.rebind("Group7ResourceManager", rm);

            System.err.println("Server ready");
        } 
        catch (Exception e) 
        {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        
        
        /*  CLIENT */
		if (args.length == 4)
		{
		    String server = args[0];
		    try
		    {
		        Registry registry = LocateRegistry.getRegistry(server, 2468);
		        // get the proxy and the remote reference by rmiregistry lookup
		        flightRM = (ResourceManager) registry.lookup(args[1]);
		        roomRM = (ResourceManager) registry.lookup(args[2]);
		        carRM = (ResourceManager) registry.lookup(args[3]);
		        if(carRM!=null && flightRM!=null && roomRM!=null)
			    {
			        System.out.println("Successful");
			        System.out.println("Connected to RM");
			    }
			    else
			    {
			        System.out.println("Unsuccessful");
			    }
		    }
		    catch (Exception e) 
		    {	
		        System.err.println("Client exception: " + e.toString());
		        e.printStackTrace();
		    }
		}
		else
		{
		    System.out.println ("Usage: java client rmihost RM1 RM2 RM3 (none are optional)"); 
			System.exit(1); 
		}
	}	 
	
	// Create a new flight, or add seats to existing flight
	//  NOTE: if flightPrice <= 0 and the flight already exists, it maintains its current price
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice)
		throws RemoteException
	{
	    return flightRM.addFlight(id, flightNum, flightSeats, flightPrice);
	}


	
	public boolean deleteFlight(int id, int flightNum)
		throws RemoteException
	{
	    return flightRM.deleteFlight(id, flightNum);
	}



	// Create a new room location or add rooms to an existing location
	//  NOTE: if price <= 0 and the room location already exists, it maintains its current price
	public boolean addRooms(int id, String location, int count, int price)
		throws RemoteException
	{
	    return roomRM.addRooms(id, location, count, price);
	}

	// Delete rooms from a location
	public boolean deleteRooms(int id, String location)
		throws RemoteException
	{
	    return roomRM.deleteRooms(id, location);		
	}

	// Create a new car location or add cars to an existing location
	//  NOTE: if price <= 0 and the location already exists, it maintains its current price
	public boolean addCars(int id, String location, int count, int price)
		throws RemoteException
	{
	    return carRM.addCars(id, location, count, price);
	}


	// Delete cars from a location
	public boolean deleteCars(int id, String location)
		throws RemoteException
	{
	    return carRM.deleteCars(id, location);
		//return deleteItem(id, Car.getKey(location));
	}



	// Returns the number of empty seats on this flight
	public int queryFlight(int id, int flightNum)
		throws RemoteException
	{
	    return flightRM.queryFlight(id, flightNum);
		//return queryNum(id, Flight.getKey(flightNum));
	}

	// Returns the number of reservations for this flight. 
//	public int queryFlightReservations(int id, int flightNum)
//		throws RemoteException
//	{
//		Trace.info("RM::queryFlightReservations(" + id + ", #" + flightNum + ") called" );
//		RMInteger numReservations = (RMInteger) readData( id, Flight.getNumReservationsKey(flightNum) );
//		if( numReservations == null ) {
//			numReservations = new RMInteger(0);
//		} // if
//		Trace.info("RM::queryFlightReservations(" + id + ", #" + flightNum + ") returns " + numReservations );
//		return numReservations.getValue();
//	}


	// Returns price of this flight
	public int queryFlightPrice(int id, int flightNum )
		throws RemoteException
	{
	    return flightRM.queryFlightPrice(id, flightNum);
		//return queryPrice(id, Flight.getKey(flightNum));
	}


	// Returns the number of rooms available at a location
	public int queryRooms(int id, String location)
		throws RemoteException
	{
	    return roomRM.queryRooms(id, location);
		//return queryNum(id, Hotel.getKey(location));
	}


	
	
	// Returns room price at this location
	public int queryRoomsPrice(int id, String location)
		throws RemoteException
	{
	    return roomRM.queryRoomsPrice(id, location);
		//return queryPrice(id, Hotel.getKey(location));
	}


	// Returns the number of cars available at a location
	public int queryCars(int id, String location)
		throws RemoteException
	{
	    return carRM.queryCars(id, location);
		//return queryNum(id, Car.getKey(location));
	}


	// Returns price of cars at this location
	public int queryCarsPrice(int id, String location)
		throws RemoteException
	{
	    return carRM.queryCarsPrice(id, location);
		//return queryPrice(id, Car.getKey(location));
	}

	// return a bill
	
	public String queryCustomerInfo(int id, int customerID)
		throws RemoteException
	{  
        int i;
        String carBill;
        String flightBill;
        String roomBill;
	    synchronized(carRM){
	        synchronized(flightRM){
	            synchronized(roomRM){
	                carBill = carRM.queryCustomerInfo(id, customerID);
	                flightBill = flightRM.queryCustomerInfo(id, customerID);
	                roomBill = roomRM.queryCustomerInfo(id, customerID);

                    i = flightBill.indexOf('\n') + 1;
	                if (i != flightBill.length()){
	                    flightBill = flightBill.substring(i);
	                }else{
	                    flightBill = "";
	                }
	                i = roomBill.indexOf('\n') + 1;
	                if (i != roomBill.length()){
	                    roomBill = roomBill.substring(i);
	                }else{
	                    roomBill = "";
	                }
	            }
	        }
	    }
	    return(carBill + flightBill + roomBill);
	} 

    // customer functions
    // new customer just returns a unique customer identifier

    public int newCustomer(int id)
	    throws RemoteException
    {
	    int cid = Integer.parseInt( String.valueOf(id) +
							    String.valueOf(Calendar.getInstance().get(Calendar.MILLISECOND)) +
							    String.valueOf( Math.round( Math.random() * 100 + 1 )));
        carRM.newCustomer(id, cid);
        roomRM.newCustomer(id, cid);
        flightRM.newCustomer(id, cid);
	    return cid;
    }

	// I opted to pass in customerID instead. This makes testing easier
	
    public boolean newCustomer(int id, int customerID )
		throws RemoteException
	{  
	    boolean carBool = carRM.newCustomer(id, customerID);
	    boolean roomBool = roomRM.newCustomer(id, customerID);
	    boolean flightBool = flightRM.newCustomer(id, customerID);
	    return carBool && roomBool && flightBool;
	}


	// Deletes customer from the database. 
	public boolean deleteCustomer(int id, int customerID)
			throws RemoteException
	{   
		return carRM.deleteCustomer(id, customerID) && roomRM.deleteCustomer(id, customerID) && flightRM.deleteCustomer(id, customerID);
	}

	
	// Adds car reservation to this customer. 
	public boolean reserveCar(int id, int customerID, String location)
		throws RemoteException
	{
	    return carRM.reserveCar(id, customerID, location);
		//return reserveItem(id, customerID, Car.getKey(location), location);
	}


	// Adds room reservation to this customer. 
	public boolean reserveRoom(int id, int customerID, String location)
		throws RemoteException
	{
	    return roomRM.reserveRoom(id, customerID, location);
		//return reserveItem(id, customerID, Hotel.getKey(location), location);
	}
	// Adds flight reservation to this customer.  
	public boolean reserveFlight(int id, int customerID, int flightNum)
		throws RemoteException
	{
	    return flightRM.reserveFlight(id, customerID, flightNum);
		//return reserveItem(id, customerID, Flight.getKey(flightNum), String.valueOf(flightNum));
	}
	
	/* reserve an itinerary */
    public boolean itinerary(int id,int customer,Vector flightNumbers,String location,boolean Car,boolean Room)
	throws RemoteException {
	    boolean boolCar = true;
	    boolean boolRoom = true;
	    boolean boolFlights = true;
	    for (int i = 0; i < flightNumbers.size(); i++)
	    {
	        boolFlights = flightRM.reserveFlight(id, customer, Integer.parseInt((String)flightNumbers.get(i))) && boolFlights;
	    }
	    
    	if(Car)
    	    boolCar = carRM.reserveCar(id, customer, location);
    	if(Room)
            boolRoom = roomRM.reserveRoom(id, customer, location);
        return boolFlights && boolCar && boolRoom;
    }    
}
