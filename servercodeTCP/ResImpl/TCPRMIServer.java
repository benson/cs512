package ResImpl;

import java.util.*;
import java.rmi.RemoteException;
import java.net.ServerSocket;
import java.io.*;
import java.nio.channels.*;
import java.net.*;
import ResInterface.*;

// essentially, implement the ResourceManager interface

public class TCPRMIServer {
    
    public static void main(String[] args) throws UnknownHostException, IOException
    {
        ServerSocket server;
        int port = 2469;
        if(args.length == 1)
        {
            port = Integer.parseInt(args[0]);
        }
        else if(args.length > 1)
        {
            System.out.println("usage: java TCPRMIServer [port]");
        }
        RMHashtable m_itemHT = new RMHashtable();

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
        return new ClientHandler(connection, m_itemHT);
    }
}
