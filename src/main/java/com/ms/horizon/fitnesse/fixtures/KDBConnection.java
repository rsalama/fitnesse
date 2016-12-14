package com.ms.horizon.fitnesse.fixtures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO -- move it into K, call it skey...
public class KDBConnection
{
	private static Logger logger = LoggerFactory.getLogger(KDBConnection.class);

    public String host;
    public int port;

    KDBConnection(String host, int port)
    {
        this.host = host;
        this.port = port;
        logger.debug("KDBConnection: " + this.toString());
    }

    public KDBConnection(String skey)
    {
        // :np16c2n1:5050
        this(skey.substring(1, skey.lastIndexOf(':') - 1), Integer.valueOf(skey.lastIndexOf(':')) + 1);
    }

    public String getHost()
    {
        return host;
    }

    public int getPort()
    {
        return port;
    }
    
    public String toString()
    {
        return ":" + host + ":" + port;
    }
}