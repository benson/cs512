import java.util.*;
import java.rmi.RemoteException;
import java.net.ServerSocket;

// essentially, implement the ResourceManager interface

public class TCPRMIServer {
    private ServerSocket server;

    public TCPRMIServer(int port) {
        server = new ServerSocket(port);
        while (true) {
            (new ClientHandler(server.accept())).start();
        }
    }

    public void close() {
        server.close();
    }
}
