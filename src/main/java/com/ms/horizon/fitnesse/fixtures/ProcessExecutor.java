package com.ms.horizon.fitnesse.fixtures;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessExecutor implements Runnable
{
	private static Logger logger = LoggerFactory.getLogger(ProcessExecutor.class);

    private String cmd[];
    private Map<String, String> env = null;
    private String root;
    private Process process;
    private Thread execThr;
    
    private static final int QSLEEP = Integer.valueOf( System.getProperty("local.q.sleep", "1000") );
    
    public ProcessExecutor(String cmd[])
    {
        this.cmd = cmd;
    }
    
    public ProcessExecutor(String cmd[], String root, Map<String, String> env)
    {
        this(cmd);
        this.root = root;
        this.env = env;
    }

    public ProcessExecutor spawn()
    {
        // TODO -- use ExecutionService
        execThr = new Thread(this);
        execThr.start();
        try
        {
            Thread.sleep(QSLEEP);
        }
        catch (InterruptedException e)
        {
        }
        return this;
    }
    
    @Override
    public void run()
    {
        try
        {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            if (env != null)
            {
                pb.environment().putAll(env);
            }
            String cwd = System.getProperty("user.dir");
            if (root != null)
            {
                pb.directory(new File(root));
                cwd = pb.directory().getAbsolutePath();
            }
            logger.debug("Starting " + Arrays.toString(cmd) + " in: " + cwd);
            this.process = pb.start();
            logger.debug("Starting StreamGobblers ...");
            new StreamGobbler(process.getInputStream(), "stdout").start();
            new StreamGobbler(process.getErrorStream(), "stderr").start();
            try
            {
                Thread.sleep(100);
            }
            catch (InterruptedException e)
            {
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    public void reap() 
    {
        long start = System.currentTimeMillis();
        logger.debug("Waiting for process " + Arrays.toString(cmd) + " to return ...");
        try
        {
            execThr.join(1000);
            process.waitFor();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace(System.err);
        }
        logger.debug(" Done." + "(" + (System.currentTimeMillis() - start) / 1000 + " ms.)");
    }

    /***
     * Inner Class to create a Stream for each thread for the Executing command
     */
    class StreamGobbler extends Thread
    {
        private InputStream inputStream;

        StreamGobbler(InputStream inputStream, String name)
        {
            setName(name);
            setDaemon(true); // Daemon threads don't stop the JVM exiting.
            this.inputStream = inputStream;
        }

        @Override
        public void run()
        {
            try
            {
                BufferedReader reader = null;
                try
                {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line = null;
                    logger.debug("StreamGobbler: " + getName() + ": starts reading ...");
                    while (!isInterrupted() && (line = reader.readLine()) != null)
                    {
                        logger.debug(getName() + ": " + line);
                    }
                }
                finally
                {
                    logger.debug("StreamGobbler: " + getName() + ": stops reading ...");
                    if (reader != null)
                        reader.close();
                }
            }
            catch (Exception e)
            {
                logger.debug("StreamGobbler: " + getName() + ": ", e);
            }
        }
    }
}
