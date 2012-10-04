import java.util.*;
import java.io.*;
import java.net.*;
import java.rmi.RemoteException;

// essentially, implement the ResourceManager interface

public class TCPRMIClient implements TCPResourceManager {
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public TCPRMIClient(String host, int port) throws UnknownHostException, IOException {
        connection = new Socket(host, port);
        System.out.println("foo");
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        System.out.println(output != null ? "foo1" : "bar1");
        input = new ObjectInputStream(connection.getInputStream());
        System.out.println(input != null ? "foo2" : "bar2");  
    }

    // NOTE: returned object will need to be cast to the appropriate class.
    // If a primitive type is desired, that value must also be extracted.
    private Serializable callMethod(Methods method, Object[] args) {
        Object ret = null;
        try {
            output.writeObject(new MethodCall(method, new Vector(Arrays.asList(args))));
            ret = input.readObject();
        } catch (Exception e) {
            System.err.println("in client's callMethod: " + e);
        }
        
        return (Serializable) ret;
    }

    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(flightNum), new Integer(flightSeats), new Integer(flightPrice)};
        return ((Boolean) callMethod(Methods.ADD_FLIGHT, argsArray)).booleanValue();
    }
    
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        Object[] argsArray = {new Integer(id), location, new Integer(numCars), new Integer(price)};
        return ((Boolean) callMethod(Methods.ADD_CARS, argsArray)).booleanValue();
    }

    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        Object[] argsArray = {new Integer(id), location, new Integer(numRooms), new Integer(price)};
        return ((Boolean) callMethod(Methods.ADD_ROOMS, argsArray)).booleanValue();
    }

    public int newCustomer(int id) throws RemoteException {
        Object[] argsArray = {new Integer(id)};
        return ((Integer) callMethod(Methods.NEW_CUSTOMER1, argsArray)).intValue();
    }
    
    public boolean newCustomer(int id, int cid)throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(cid) };
        return ((Boolean) callMethod(Methods.NEW_CUSTOMER2, argsArray)).booleanValue();
    }

    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(flightNum)};
        return ((Boolean) callMethod(Methods.DELETE_FLIGHT, argsArray)).booleanValue();
    }
    
    public boolean deleteCars(int id, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), location};
        return ((Boolean) callMethod(Methods.DELETE_CARS, argsArray)).booleanValue();
    }

    public boolean deleteRooms(int id, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), location};
        return ((Boolean) callMethod(Methods.DELETE_ROOMS, argsArray)).booleanValue();
    }
    
    public boolean deleteCustomer(int id,int customer) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(customer)};
        return ((Boolean) callMethod(Methods.DELETE_CUSTOMER, argsArray)).booleanValue();
    }

    public int queryFlight(int id, int flightNumber) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(flightNumber)};
        return ((Integer) callMethod(Methods.QUERY_FLIGHT, argsArray)).intValue();
    }

    public int queryCars(int id, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), location};
        return ((Integer) callMethod(Methods.QUERY_CARS, argsArray)).intValue();
    }

    public int queryRooms(int id, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), location};
        return ((Integer) callMethod(Methods.QUERY_ROOMS, argsArray)).intValue();
    }

    public String queryCustomerInfo(int id,int customer) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(customer)};
        return (String) callMethod(Methods.QUERY_CUSTOMER_INFO, argsArray);
    }
    
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(flightNumber)};
        return ((Integer) callMethod(Methods.QUERY_FLIGHT_PRICE, argsArray)).intValue();
    }

    public int queryCarsPrice(int id, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), location};
        return ((Integer) callMethod(Methods.QUERY_CARS_PRICE, argsArray)).intValue();
    }

    public int queryRoomsPrice(int id, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), location};
        return ((Integer) callMethod(Methods.QUERY_ROOMS_PRICE, argsArray)).intValue();
    }

    public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(customer), new Integer(flightNumber)};
        return ((Boolean) callMethod(Methods.RESERVE_FLIGHT, argsArray)).booleanValue();
    }

    public boolean reserveCar(int id, int customer, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(customer), location};
        return ((Boolean) callMethod(Methods.RESERVE_CAR, argsArray)).booleanValue();
    }

    public boolean reserveRoom(int id, int customer, String location) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(customer), location};
        return ((Boolean) callMethod(Methods.RESERVE_ROOM, argsArray)).booleanValue();
    }

    public boolean itinerary(int id, int customer, Vector flightNumbers, String location, boolean Car, boolean Room) throws RemoteException {
        Object[] argsArray = {new Integer(id), new Integer(customer), flightNumbers, location, Car, Room};
        return ((Boolean) callMethod(Methods.ITINERARY, argsArray)).booleanValue();
    }
}
