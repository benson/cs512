import java.rmi.*;
import ResInterface.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import java.util.*;
import java.io.*;

public class Test
{
    
    public static void main(String[] args)
    {
        int maxClients = 128;
        int warmup = 10000;
        int timingPeriod = 60000;
        Vector<timedClient> clients = new Vector<timedClient>();
        timedClient newc;
        double throughput = 0.0;
        double responseTime = 0.0;
        String fileLoc = "testsB/0";
        for (int i = 0; i < maxClients; i++)
        {
            // PASS PARAMS TO TIMEDCLIENT
            // new Integer(i).toString()
            String[] cargs = {new Integer(i).toString()};
            newc = new timedClient(cargs);
            clients.add(newc);
            newc.start();
        }
        try
        {
            Thread.sleep(warmup);
        }
        catch (Exception e) {}
        for (timedClient c : clients)
        {
            c.startTiming();
        }

        try
        {
            Thread.sleep(timingPeriod);
        }
        catch (Exception e) {}

        for (timedClient c : clients)
        {
            c.stopTiming();
        }

        for (timedClient c : clients)
        {
            throughput += c.fThroughput();
            responseTime += c.fResponseTime();
            c.interrupt();
        }


        System.out.println("throughput   : " + throughput + "\n" +
                           "response time: " + (responseTime / maxClients));
    }
}
