import java.net.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class MiddleHandler extends ClientHandler {
    private TCPRMIClient flightRM;
    private TCPRMIClient roomRM;
    private TCPRMIClient carRM;
    
    public MiddleHandler(Socket connection, RMHashtable table,
           TCPRMIClient flightRM, TCPRMIClient roomRM, TCPRMIClient carRM) {
        super(connection, table);
        this.flightRM = flightRM;
        this.roomRM = roomRM;
        this.carRM = carRM;

    }

    public boolean itinerary(int id,int customer,Vector flightNumbers,String location,boolean Car,boolean Room)
        	throws RemoteException {
        boolean boolCar = true;
        boolean boolRoom = true;
        boolean boolFlights = true;
        synchronized(roomRM){
            synchronized(carRM){
                synchronized(flightRM){
                	
	                for (int i = 0; i < flightNumbers.size(); i++)
	                {
	                    boolFlights = flightRM.reserveFlight(id, customer, Integer.parseInt((String)flightNumbers.get(i))) && boolFlights;
	                }
	                
                	if(Car)
                	    boolCar = carRM.reserveCar(id, customer, location);
                	if(Room)
                        boolRoom = roomRM.reserveRoom(id, customer, location);
               }
           }
        }
        return boolFlights && boolCar && boolRoom;
    }
    
    // Create a new flight, or add seats to existing flight
	//  NOTE: if flightPrice <= 0 and the flight already exists, it maintains its current price
	public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice)
		throws RemoteException
	{
	    boolean result;
	    synchronized(flightRM){
	        result = flightRM.addFlight(id, flightNum, flightSeats, flightPrice);
	    }
	    return result;
	}


	
	public boolean deleteFlight(int id, int flightNum)
		throws RemoteException
	{
	    boolean result;
	    synchronized(flightRM){
	        result = flightRM.deleteFlight(id, flightNum);
	    }
	    return result;
	}



	// Create a new room location or add rooms to an existing location
	//  NOTE: if price <= 0 and the room location already exists, it maintains its current price
	public boolean addRooms(int id, String location, int count, int price)
		throws RemoteException
	{
	    
	    boolean result;	    
	    synchronized(roomRM){
	        result = roomRM.addRooms(id, location, count, price);
	    }
	    return result;
	}

	// Delete rooms from a location
	public boolean deleteRooms(int id, String location)
		throws RemoteException
	{
	    boolean result;	    
	    synchronized(roomRM){
	        result = roomRM.deleteRooms(id, location);	
	    }
	    return result;
	}

	// Create a new car location or add cars to an existing location
	//  NOTE: if price <= 0 and the location already exists, it maintains its current price
	public boolean addCars(int id, String location, int count, int price)
		throws RemoteException
	{
	    boolean result;	    
	    synchronized(carRM){
	        result = carRM.addCars(id, location, count, price);
	    }
	    return result;
	}


	// Delete cars from a location
	public boolean deleteCars(int id, String location)
		throws RemoteException
	{
	    boolean result;	    
	    synchronized(carRM){
	        result = carRM.deleteCars(id, location);
	    }
	    return result;
	}



	// Returns the number of empty seats on this flight
	public int queryFlight(int id, int flightNum)
		throws RemoteException
	{
	    int result;	    
	    synchronized(flightRM){
	        result = flightRM.queryFlight(id, flightNum);
	    }
	    return result;
	}


	// Returns price of this flight
	public int queryFlightPrice(int id, int flightNum )
		throws RemoteException
	{
	    int result;	    
	    synchronized(flightRM){
	        result = flightRM.queryFlightPrice(id, flightNum);
	    }
	    return result;
	}


	// Returns the number of rooms available at a location
	public int queryRooms(int id, String location)
		throws RemoteException
	{
	    int result;	    
	    synchronized(roomRM){
	        result = roomRM.queryRooms(id, location);
	    }
	    return result;
	}


	
	
	// Returns room price at this location
	public int queryRoomsPrice(int id, String location)
		throws RemoteException
	{
	    int result;	    
	    synchronized(roomRM){
	        result = roomRM.queryRoomsPrice(id, location);
	    }
	    return result;
	}


	// Returns the number of cars available at a location
	public int queryCars(int id, String location)
		throws RemoteException
	{
	    int result;	    
	    synchronized(carRM){
	        result = carRM.queryCars(id, location);
	    }
	    return result;
	}


	// Returns price of cars at this location
	public int queryCarsPrice(int id, String location)
		throws RemoteException
	{
	    int result;	    
	    synchronized(carRM){
	        result = carRM.queryCarsPrice(id, location);
	    }
	    return result;
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
	            }
	        }
	    }
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
		synchronized(carRM){
	        synchronized(flightRM){
	            synchronized(roomRM){					    
                    carRM.newCustomer(id, cid);
                    roomRM.newCustomer(id, cid);
                    flightRM.newCustomer(id, cid);
                }
            }
        }
	    return cid;
    }

	// I opted to pass in customerID instead. This makes testing easier
	
    public boolean newCustomer(int id, int customerID )
		throws RemoteException
	{  
	    boolean carBool, roomBool, flightBool;
	    synchronized(carRM){
	        synchronized(flightRM){
	            synchronized(roomRM){
	                carBool = carRM.newCustomer(id, customerID);
	                roomBool = roomRM.newCustomer(id, customerID);
	                flightBool = flightRM.newCustomer(id, customerID);
	            }
	        }
	    }
	    return carBool && roomBool && flightBool;
	}


	// Deletes customer from the database. 
	public boolean deleteCustomer(int id, int customerID)
			throws RemoteException
	{   
	    boolean car, room, flight;
	    synchronized(carRM){
	        synchronized(flightRM){
	            synchronized(roomRM){
	                car = carRM.deleteCustomer(id, customerID);
	                room = roomRM.deleteCustomer(id, customerID);
	                flight = flightRM.deleteCustomer(id, customerID);
	            }
	        }
	    }	                
		return car && room && flight; 
	}

	
	// Adds car reservation to this customer. 
	public boolean reserveCar(int id, int customerID, String location)
		throws RemoteException
	{
	    boolean result;	    
	    synchronized(carRM){
	        result = carRM.reserveCar(id, customerID, location);
	    }
	    return result;
	}


	// Adds room reservation to this customer. 
	public boolean reserveRoom(int id, int customerID, String location)
		throws RemoteException
	{
	    boolean result;	    
	    synchronized(roomRM){
	        result = roomRM.reserveRoom(id, customerID, location);
	    }
	    return result;
	}
	// Adds flight reservation to this customer.  
	public boolean reserveFlight(int id, int customerID, int flightNum)
		throws RemoteException
	{
	    boolean result;	    
	    synchronized(flightRM){
	        result = flightRM.reserveFlight(id, customerID, flightNum);
	    }
	    return result;
	}
}
