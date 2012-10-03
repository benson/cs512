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
        Object[] argsArray = {Integer(id), location, Integer(numCars), Integer(price)};
        return callMethod(new MethodCall(ADD_CARS, new Vector(argsArray));
    }
   
    public boolean addRooms(int id, String location, int numRooms, int price) throws RemoteException {
        Object[] argsArray = {Integer(id), location, Integer(numRooms), Integer(price)};
        return callMethod(new MethodCall(ADD_ROOMS, new Vector(argsArray));
    }

    public int newCustomer(int id) throws RemoteException {
        Object[] argsArray = {Integer(id)};
        return callMethod(new MethodCall(NEW_CUSTOMER1, new Vector(argsArray));
    }
    
    public boolean newCustomer(int id, int cid)throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(cid) };
        return callMethod(new MethodCall(NEW_CUSTOMER2, new Vector(argsArray));
    }

    public boolean deleteFlight(int id, int flightNum) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNum)};
        return callMethod(new MethodCall(DELETE_FLIGHT, new Vector(argsArray));
    }
    
    public boolean deleteCars(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return callMethod(new MethodCall(DELETE_CARS, new Vector(argsArray));
    }

    public boolean deleteRooms(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return callMethod(new MethodCall(DELETE_ROOMS, new Vector(argsArray));
    }
    
    public boolean deleteCustomer(int id,int customer) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer)};
        return callMethod(new MethodCall(DELETE_CUSTOMER, new Vector(argsArray));
    }

    public int queryFlight(int id, int flightNumber) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNumber)};
        return callMethod(new MethodCall(QUERY_FLIGHT, new Vector(argsArray));
    }

    public int queryCars(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return callMethod(new MethodCall(QUERY_CARS, new Vector(argsArray));
    }

    public int queryRooms(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return callMethod(new MethodCall(QUERY_ROOMS, new Vector(argsArray));
    }

    public String queryCustomerInfo(int id,int customer) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer)};
        return callMethod(new MethodCall(QUERY_CUSTOMER_INFO, new Vector(argsArray));
    }
    
    public int queryFlightPrice(int id, int flightNumber) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(flightNumber)};
        return callMethod(new MethodCall(QUERY_FLIGHT_PRICE, new Vector(argsArray));
    }

    public int queryCarsPrice(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return callMethod(new MethodCall(QUERY_CARS_PRICE, new Vector(argsArray));
    }

    public int queryRoomsPrice(int id, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), location};
        return callMethod(new MethodCall(QUERY_ROOMS_PRICE, new Vector(argsArray));
    }

    public boolean reserveFlight(int id, int customer, int flightNumber) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), Integer(flightNumber)};
        return callMethod(new MethodCall(RESERVE_FLIGHT, new Vector(argsArray));
    }

    public boolean reserveCar(int id, int customer, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), location};
        return callMethod(new MethodCall(RESERVE_CAR, new Vector(argsArray));
    }

    public boolean reserveRoom(int id, int customer, String location) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), location};
        return callMethod(new MethodCall(RESERVE_ROOM, new Vector(argsArray));
    }

    public boolean itinerary(int id,int customer,Vector flightNumbers,String location, boolean Car, boolean Room) throws RemoteException {
        Object[] argsArray = {Integer(id), Integer(customer), flightNumbers, location, Car, Room};
        return callMethod(new MethodCall(INTINERARY, new Vector(argsArray));
    }
}
