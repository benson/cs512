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

public class TCPRMIClient implements TCPResourceManager {
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public static TCPRMIClient(String host, int port) throws UnknownHostException, IOException {
        connection = Socket(host, port);
        input = new ObjectInputStream(connection.getInputStream());
        output = new ObjectOutputStream(connection.getOutputStream());        
    }

    // NOTE: returned object will need to be cast to the appropriate class.
    // If a primitive type is desired, that value must also be extracted.
    private Serializable callMethod(int method, Object[] args) {
        output.writeObject(new MethodCall(method, new Vector(args)));
        return input.readObject();
    }

    public boolean addFlight(int id, int flightNum, int flightSeats, int flightPrice) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNum), Integer(flightSeats), Integer(flightPrice)};
        return ((Boolean) callMethod(ADD_FLIGHT, argsArray)).booleanValue();
    }
    
    public boolean addCars(int id, String location, int numCars, int price) throws RemoteException {
        Object[] argsArray = {Integer(id), location, Integer(numCars), Integer(price)};
        return ((Boolean) callMethod(ADD_CARS, argsArray)).booleanValue();
    }

    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        Object[] argsArray = {Integer(id), location, Integer(numRooms), Integer(price)};
        return ((Boolean) callMethod(ADD_ROOMS, argsArray)).booleanValue();
    }

    public int newCustomer(int id) throws RemoteException {
        Object[] argsArray = {Integer(id)};
        return ((Integer) callMethod(NEW_CUSTOMER1, argsArray)).intValue();
    }
    
    public boolean newCustomer(int id, int cid)throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(cid) };
        return ((Boolean) callMethod(NEW_CUSTOMER2, argsArray)).booleanValue();
    }

    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNum)};
        return ((Boolean) callMethod(DELETE_FLIGHT, argsArray))).booleanValue();
    }
    
    public boolean deleteCars(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return ((Boolean) callMethod(DELETE_CARS, argsArray)).booleanValue();
    }

    public boolean deleteRooms(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return ((Boolean) callMethod(DELETE_ROOMS, argsArray)).booleanValue();
    }
    
    public boolean deleteCustomer(int id,int customer) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer)};
        return ((Boolean) callMethod(DELETE_CUSTOMER, argsArray)).booleanValue();
    }

    public int queryFlight(int id, int flightNumber) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNumber)};
        return ((Integer) callMethod(QUERY_FLIGHT, argsArray)).intValue();
    }

    public int queryCars(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return ((Integer) callMethod(QUERY_CARS, argsArray)).intValue();
    }

    public int queryRooms(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return ((Integer) callMethod(QUERY_ROOMS, argsArray)).intValue();
    }

    public String queryCustomerInfo(int id,int customer) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer)};
        return (String) callMethod(QUERY_CUSTOMER_INFO, argsArray);
    }
    
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNumber)};
        return ((Integer) callMethod(QUERY_FLIGHT_PRICE, argsArray)).intValue();
    }

    public int queryCarsPrice(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return ((Integer) callMethod(QUERY_CARS_PRICE, argsArray)).intValue();
    }

    public int queryRoomsPrice(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return ((Integer) callMethod(QUERY_ROOMS_PRICE, argsArray)).intValue();
    }

    public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), Integer(flightNumber)};
        return ((Boolean) callMethod(RESERVE_FLIGHT, argsArray)).booleanValue();
    }

    public boolean reserveCar(int id, int customer, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), location};
        return ((Boolean) callMethod(RESERVE_CAR, argsArray)).booleanValue();
    }

    public boolean reserveRoom(int id, int customer, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), location};
        return ((Boolean) callMethod(RESERVE_ROOM, argsArray)).booleanValue();
    }

    public boolean itinerary(int id, int customer, Vector flightNumbers, String location, boolean Car, boolean Room) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), flightNumbers, location, Car, Room};
        return ((Boolean) callMethod(INTINERARY, argsArray)).booleanValue();
    }
}
