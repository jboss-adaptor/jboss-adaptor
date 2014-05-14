package com.tcs.p3.parses;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 30/04/14
 * Time: 10:35
 * To change this template use File | Settings | File Templates.
 */
    import javax.management.*;
    import java.io.*;
    import java.util.*;
    import java.rmi.*;
    import javax.naming.*;
    public class Test2
      {
          public static void main(String ar[]) throws Exception
          {
                Hashtable ht=new Hashtable();
                ht.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.security.jndi.JndiLoginInitialContextFactory");
                ht.put(Context.PROVIDER_URL,"jnp://10.10.10.10:1099");
                ht.put(Context.SECURITY_PRINCIPAL,"admin");
                ht.put(Context.SECURITY_CREDENTIALS,"admin");

                System.out.println("nt 1- Getting InitialContext...... ");
                Context ctx = new InitialContext(ht);
                System.out.println("nt 2- Got InitialContext: "+ctx);

                MBeanServerConnection server = (MBeanServerConnection) ctx.lookup("jmx/invoker/RMIAdaptor");
                System.out.println("nntConnectionCreatedCount = "+ (Integer)server.getAttribute(new ObjectName("jboss.jca:service=ManagedConnectionPool,name=TestDS"), new String("ConnectionCreatedCount")));
                System.out.println("nntAvailableConnectionCount = "+ (Long)server.getAttribute(new ObjectName("jboss.jca:service=ManagedConnectionPool,name=TestDS"), new String("AvailableConnectionCount")));
                System.out.println("nntInUseConnectionCount = "+ (Long)server.getAttribute(new ObjectName("jboss.jca:service=ManagedConnectionPool,name=TestDS"), new String("InUseConnectionCount")));
          }
      }
