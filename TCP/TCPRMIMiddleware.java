import java.net.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;

public class TCPRMIMiddleware extends TCPRMIServer {

    private static TCPRMIClient flightRM;
    private static TCPRMIClient roomRM;
    private static TCPRMIClient carRM;
    
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        // usage: java ResImpl.TCPRMIServer port_to_host_on flightServer flightPort roomServer roomPort carServer carPort
            ServerSocket server;
            int port, flightPort, roomPort, carPort;
            String flightServer, roomServer, carServer;
            if(args.length != 7)
            {
                System.out.println("usage: java ResImpl.TCPRMIServer port_to_host_on flightServer flightPort roomServer roomPort carServer carPort");
                return;
            }
            else
            {
                port = Integer.parseInt(args[0]);
                flightServer = args[1];
                flightPort = Integer.parseInt(args[2]);
                roomServer = args[3];
                roomPort = Integer.parseInt(args[4]);
                carServer = args[5];
                carPort = Integer.parseInt(args[6]);
            }
            RMHashtable m_itemHT = new RMHashtable();
            flightRM = new TCPRMIClient(flightServer, flightPort);
            roomRM = new TCPRMIClient(roomServer, roomPort);
            carRM = new TCPRMIClient(carServer, carPort);            
            try
            {
                server = new ServerSocket(port);
                System.out.println("running server!");
            }
            catch(Exception e)
            {
                System.out.println("exception" + e);
                return;
            }
            while (true) {
                try
                {
                    makeHandler(server.accept(), m_itemHT).start();
                    System.out.println("NEW CONNECTION!");
                }
                catch(Exception e)
                {
                    System.out.println("exception" + e);
                    return;
                }
            }
    }
    
    protected static Thread makeHandler(Socket connection, RMHashtable m_itemHT) {
        return new MiddleHandler(connection, m_itemHT, flightRM, roomRM, carRM);
    }
}
