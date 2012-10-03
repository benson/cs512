// essentially, implement the ResourceManager interface

public enum Methods {
    ADD_FLIGHT,
    ADD_CARS,
    ADD_ROOMS,
    NEW_CUSTOMER1,
    NEW_CUSTOMER2,
    DELETE_FLIGHT,
    DELETE_CARS,
    DELETE_ROOMS,
    DELETE_CUSTOMER,
    QUERY_FLIGHT,
    QUERY_CARS,
    QUERY_ROOMS,
    QUERY_CUSTOMER_INFO,
    QUERY_FLIGHT_PRICE,
    QUERY_CARS_PRICE,
    QUERY_ROOMS_PRICE,
    RESERVE_FLIGHT,
    RESERVE_CAR,
    RESERVE_ROOM,
    ITINERARY
}

public class MethodCall implements Serializable {
    public int method;
    public Vector arguments;

    public static MethodCall(int method, Vector arguments) {
        this.method = method;
        this.arguments = arguments;
    }
}

// client
public class TCPRMIClient implements TCPResourceManager {
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public static TCPRMIClient(String host, int port) throws UnknownHostException, IOException {
        connection = Socket(host, port);
        input = new ObjectInputStream(connection.getInputStream());
        output = new ObjectOutputStream(connection.getOutputStream());        
    }

    private Serializable callMethod(MethodCall call) {
        output.writeObject(call);
        // block
        return input.readObject();
        // note: return value will need to be cast to the appropriate type
    }

    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNum), Integer(flightSeats), Integer(flightPrice)};
        return callMethod(new MethodCall(ADD_FLIGHT, new Vector(argsArray));
    }
    
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {

    }
   
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {

    }

    public int newCustomer(int id) throws RemoteException {

    }
    
    public boolean newCustomer(int id, int cid)throws RemoteException {

    }

    public boolean deleteFlight(int id, int flightNum) throws RemoteException {

    }
    
    public boolean deleteCars(int id, String location) throws RemoteException {

    }

    public boolean deleteRooms(int id, String location) throws RemoteException {

    }
    
    public boolean deleteCustomer(int id,int customer) throws RemoteException {

    }

    public int queryFlight(int id, int flightNumber) throws RemoteException {

    }

    public int queryCars(int id, String location) throws RemoteException {

    }

    public int queryRooms(int id, String location) throws RemoteException {

    }

    public String queryCustomerInfo(int id,int customer) throws RemoteException {

    }
    
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {

    }

    public int queryCarsPrice(int id, String location) throws RemoteException {

    }

    public int queryRoomsPrice(int id, String location) throws RemoteException {

    }

    public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {

    }

    public boolean reserveCar(int id, int customer, String location) throws RemoteException {

    }

    public boolean reserveRoom(int id, int customer, String locationd) throws RemoteException {

    }

    public boolean itinerary(int id,int customer,Vector flightNumbers,String location, boolean Car, boolean Room) throws RemoteException {

    }
}