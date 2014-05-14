package com.tcs.p3.parses;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.net.MalformedURLException;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 29/04/14
 * Time: 15:30
 * To change this template use File | Settings | File Templates.
 */
public class GestorConnection {

    static private GestorConnection instancia = null;
    String host = "192.168.56.103";  // Your JBoss Bind Address default is localhost
    int port = 1090;  // JBoss remoting port
    String urlString ="service:jmx:rmi://" + host + "/jndi/rmi://"+ host + ":" + port + "/jmxconnector";
    MBeanServerConnection connection = null;

    private GestorConnection()
    {
      String urlString ="service:jmx:rmi://" + host + "/jndi/rmi://"+ host + ":" + port + "/jmxconnector";
      System.out.println("        \n\n\t****  urlString: "+urlString);
        JMXServiceURL serviceURL = null;
        try {
            serviceURL = new JMXServiceURL(urlString);
            JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
            connection = jmxConnector.getMBeanServerConnection();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static GestorConnection getInstancia()
    {
      if (instancia == null)
      {
          instancia = new GestorConnection();
      }
      return instancia;
    }

    public MBeanServerConnection getConnection()
    {
        assert this.connection != null;
        return this.connection;
    }


}
