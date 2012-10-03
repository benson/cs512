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

private class ClientHandler implements Thread {
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public static ClientHandler(Socket connection) {
        this.connection = connection;
        input = ObjectInputStream(connection.getInputStream());
        output = ObjectOutputStream(connection.getOutputStream());
    }

    private Serializable handleCall(MethodCall call) {
        Vector args = call.arguments;
        switch (call.method) {
        case ADD_FLIGHT:
            return Boolean(addFlight(((Integer) args.get(0)).intValue,
                                     ((Integer) args.get(1)).intValue,
                                     ((Integer) args.get(2)).intValue,
                                     ((Integer) args.get(3)).intValue));
            break;
        case ADD_CARS:
        case ADD_ROOMS:
        case NEW_CUSTOMER1:
        case NEW_CUSTOMER2:
        case DELETE_FLIGHT:
        case DELETE_CARS:
        case DELETE_ROOMS:
        case DELETE_CUSTOMER:
        case QUERY_FLIGHT:
        case QUERY_CARS:
        case QUERY_ROOMS:
        case QUERY_CUSTOMER_INFO:
        case QUERY_FLIGHT_PRICE:
        case QUERY_CARS_PRICE:
        case QUERY_ROOMS_PRICE:
        case RESERVE_FLIGHT:
        case RESERVE_CAR:
        case RESERVE_ROOM:
        case ITINERARY:
        }
    }
}

public class TCPRMIServer {
    private ServerSocket server;

    public static TCPRMIServer(int port) {
        server = new ServerSocket(port);
        while (true) {
            (new ClientHandler(server.accept())).start();
        }
    }

    public void close() {
        server.close();
    }
}
