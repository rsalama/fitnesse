package com.ms.horizon.fitnesse.fixtures;

import java.io.EOFException;

import kx.K;
import kx.K.KException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorizonConnection
{
	private static Logger logger = LoggerFactory.getLogger(HorizonConnection.class);

    private static final String HORIZON_VERSION = System.getProperty("horizon.version"); 
    private static final String QSCRIPT_PATH = System.getProperty("local.qpath");
    private static final boolean QUSE_SCREEN = Boolean.valueOf( System.getProperty("local.q.usescreen", "false") );
    private static final String SP  = ".sp.path: {`$\":\",x}@'\":\" vs " + "\"" + QSCRIPT_PATH + "\"";
    private static final String FND = ".sp.fnd:{[sp;f] key ` sv (sp[first where {not ()~key ` sv (x;y)}[;f]@'sp];f)}"; 
    private static final String LD  = ".sp.ld:{[f] $[not ()~ p:.sp.fnd[.sp.path;f]; [system \"l \", p:1_string p; p]; ::]}";
    
    private K k;
    private ProcessExecutor processExecutor;
    private KDBConnection connection;
    private String environment;
    
    public HorizonConnection(String environment, String host, int port) throws Exception
    {
        // Start a local q process that is then used to talk to horizon
        connection = new KDBConnection(host, port);

        String[] cmd;
        if (QUSE_SCREEN) 
        {
            String a[] = {"screen", "-d", "-m", "-S", "FitNesse::" + System.getProperty("user.name"), 
                    "q", "-p", "" + connection.getPort()};
            cmd = a;
        }
        else
        {
            String a[] = {"q", "-p", "" + connection.getPort()};
            cmd = a;
        }
        processExecutor = new ProcessExecutor(cmd).spawn();
        
        k = new K(connection.getHost(), connection.getPort());
       
        initHorizon(environment);
        this.environment = environment;
    }
    
    private void initHorizon(String environment) throws Exception
    {
        logger.debug("Horizon Version: " + HORIZON_VERSION);
        logger.debug("Horizon Area: " + environment);
        logger.debug("Q Search PATH: " + SP);
        
        // set path
        k.k(SP);
        
        // load the find and load functions
        logger.debug("Loading Q FND function: " + FND);
        k.k(FND);
        
        logger.debug("Loading Q LD function: " + LD);
        k.k(LD);
    }

    public void loadScript(String script) throws Exception
    {
        try
        {
            logger.debug("Loading script: " + ".sp.ld`" + script);
            k.k(".sp.ld`" + script);
        }
        catch (KException e)
        {
        	logger.info("no script?", e);
            try
            {
                k.ks("exit 0;");
            }
            catch (EOFException e1)
            {
            	logger.debug("loadScript", e);
                processExecutor.reap();
            }
        }
    }

    public void exit()
    {
        try
        {
            logger.debug("DEBUG: exit 0;");
            k.ks("exit 0;");
            processExecutor.reap();
        }
        catch (Exception e)
        {
            // Silently ignored - throws an exception because readFully fails
            logger.debug("loadScript", e);
        }
        processExecutor = null;
    }
    
    public boolean isRunning()
    {
        return processExecutor != null;
    }
    
    public String toString()
    {
        return "HorizonConnection: " + connection.toString();
    }

    public Object execute(String kdbFunction) throws Exception
    {
        return k.k(kdbFunction);
    }
    
    public Object execute(String kdbFunction, Object x) throws Exception
    {
        return k.k(kdbFunction, x);
    }
    
    public Object execute(String kdbFunction, Object x, Object y) throws Exception
    {
        return k.k(kdbFunction, x, y);
    }
    
    public Object execute(String kdbFunction, Object x, Object y, Object z) throws Exception
    {
        return k.k(kdbFunction, x, y, z);
    }

    public void executeAsync(String kdbFunction) throws Exception
    {
        k.ks(kdbFunction);
    }

    public void executeAsync(String kdbFunction, Object x) throws Exception
    {
        k.ks(kdbFunction, x);
    }
    
    public void executeAsync(String kdbFunction, Object x, Object y) throws Exception
    {
        k.ks(kdbFunction, x, y);
    }
    
    public void executeAsync(String kdbFunction, Object x, Object y, Object z) throws Exception
    {
        k.ks(kdbFunction, x, y, z);
    }

    public Object read() throws Exception
    {
        return k.k();
    }
    public String getEnvironment()
    {
        return environment;
    }
    
    public KDBConnection getConnecton()
    {
        return connection;
    }
}