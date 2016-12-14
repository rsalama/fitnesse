package com.ms.horizon.fitnesse.fixtures;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class HorizonConnectionFactory
{
    private static HorizonConnectionFactory connectionManager = null;
    private static Set<String> environments;
    private static Map<String, HorizonConnection> horizonConnections;
    private static String LOCALHOST = "localhost";
    
    static
    {
        // TODO -- get from configuration
        environments = new HashSet<String>();
        environments.add("default");
        environments.add("devsys");
        environments.add("demo");
        environments.add("prod");
        environments.add("qa");
        
        horizonConnections = new HashMap<String, HorizonConnection>();
    }

    
    public static synchronized HorizonConnectionFactory getInstance()
    {
        if (connectionManager == null)
        {
            connectionManager = new HorizonConnectionFactory();
        }
        return connectionManager;
    }
    
    public HorizonConnection create(String name, String env) throws Exception
    {
        if (! environments.contains(env))
        {
            throw new HorizonException("Environment: " + env + " not recognized");
        }

        int localPort = getFreePort();
        /*
        HorizonConnection hrz = horizonConnections.get(name);
        if (hrz == null)
        {
            hrz = new HorizonConnection(env, LOCALHOST, localPort);
            horizonConnections.put(name, hrz);
        }
        */
        HorizonConnection hrz = new HorizonConnection(env, LOCALHOST, localPort);
        return hrz;
    }

    private int getFreePort() throws HorizonException
    {
        int port;
        try
        {
            ServerSocket server = new ServerSocket(0);
            port = server.getLocalPort();
            server.close();
            return port;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            throw new HorizonException("Cannot create qport", e);
        }
    }

    public HorizonConnection get(String name)
    {
        return horizonConnections.get(name);
    }

    public boolean end(String name)
    {
        boolean done = false;
        HorizonConnection hrz = HorizonConnectionFactory.getInstance().get(name);
        if (hrz != null)
        {
            hrz.exit();
            done = hrz.isRunning();
            horizonConnections.remove(name);
        }
        return done;
    }
}
