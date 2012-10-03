package ResImpl;

import java.util.*;
import java.rmi.RemoteException;
import java.net.ServerSocket;
import java.io.*;
import java.nio.channels.*;
import java.net.*;

// essentially, implement the ResourceManager interface

public class TCPRMIServer {
    private ServerSocket server;

    public TCPRMIServer(int port)
            throws IOException, SecurityException, SocketTimeoutException, IllegalBlockingModeException {
            
       	RMHashtable m_itemHT = new RMHashtable();

        try
        {
            server = new ServerSocket(port);
        }
        catch(Exception e)
        {
            System.out.println("exception" + e);
        }
        while (true) {
            (new ClientHandler(server.accept(), m_itemHT)).start();
        }
    }
}
