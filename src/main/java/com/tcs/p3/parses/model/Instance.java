package com.tcs.p3.parses.model;

import com.tcs.p3.parses.GestorConnection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 7/05/14
 * Time: 10:56
 * To change this template use File | Settings | File Templates.
 */
public class Instance {

    public String maquina;
    public String name;
    public String ipaddress;
    public int port;
    public List<String> applications;
    public int numThreads;
    public Long mx_mem;
    public Long init_mem;
    public String cluster;
    public List<Datasource> listaDatasources;

    public Instance()
    {
      applications = new LinkedList<String>();
      listaDatasources = new LinkedList<Datasource>();
    }

    public Instance populate() throws Exception
    {
        ObjectName connectorThreadPool= new ObjectName("jboss.system:service=ThreadPool");
        ObjectName serverInfo=new ObjectName("jboss.system:type=ServerInfo");
        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        numThreads=(Integer) connection.getAttribute(connectorThreadPool,"MaximumPoolSize");
        mx_mem = (Long) connection.getAttribute(serverInfo,"MaxMemory");
        init_mem = mx_mem;
        applications= getApplications();
        cluster = getCluster();
        return this;
    }

    private String getCluster() {
        return "-";
    }

    private List<String> getApplications() throws Exception
    {
        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        ObjectName connectorThreadPool = new ObjectName("jboss.classloader:domain=*,*");
        Set<ObjectInstance> connectors = connection.queryMBeans(connectorThreadPool,null);

        List<String> listOutput = new LinkedList<String>();
        Iterator iter = connectors.iterator();
        while (iter.hasNext()) {
            ObjectInstance next = (ObjectInstance) iter.next();
            String nameClassLoader = (String) connection.getAttribute(next.getObjectName(),"Name");
            if (nameClassLoader.endsWith(".war/"))
            {
                String[] listaPaths = nameClassLoader.split("/");
                listOutput.add(listaPaths[listaPaths.length -1]);
            }
        }
        return listOutput;
    }


}
