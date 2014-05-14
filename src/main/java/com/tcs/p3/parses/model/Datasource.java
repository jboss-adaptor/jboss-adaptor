package com.tcs.p3.parses.model;

import com.sun.corba.se.spi.orbutil.fsm.Input;
import com.tcs.p3.parses.GestorConnection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Node;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 7/05/14
 * Time: 17:46
 * To change this template use File | Settings | File Templates.
 */
public class Datasource {

    private String nameDS;
    private String ipaddress;
    private int port;
    private String jdniName;
    private String maquina;
    private int max_connections;
    private int init_connectionts;
    private String username;
    private String driver;
    private String sid;


    public Datasource()
    {
     max_connections = 0;
     init_connectionts = 0;
    }

    public Datasource populate(String name) throws Exception
    {

        assert name != null;
        ObjectName datasourceJMX= new ObjectName("jboss.management.local:J2EEServer=Local,j2eeType=ServiceModule,name="+name);
        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        assert connection.getObjectInstance(datasourceJMX) != null;
        String driverDs = (String) connection.getAttribute(datasourceJMX,"deploymentDescriptor");
        InputSource inputDs = new InputSource(new StringReader(driverDs));
        parse(inputDs,name);

        return this;

    }

    // Parseamos el xml del driver, y pasamos las variables dependendiendo del driver.
    private void parse(InputSource inputDs, String name) throws  Exception
    {
        //To change body of created methods use File | Settings | File Templates.

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(inputDs);

        doc.normalize();

        Element datasourceNode = (Element) doc.getElementsByTagName("datasources").item(0);
        Element childDTLocal = (Element) datasourceNode.getElementsByTagName("local-tx-datasource").item(0);

        NodeList jndiNodes = childDTLocal.getElementsByTagName("jndi-name");
        for(int i=0;i<jndiNodes.getLength();i++){
            org.w3c.dom.Node nodo = jndiNodes.item(i);
            if (nodo instanceof Element){
                    System.out.println(" DS - Name " + nodo.getTextContent());
                    String nameTemporal = nodo.getTextContent();
                    if (nodo.getTextContent().equals(name))
                    {
                        jdniName = nodo.getTextContent();

                        NodeList connectionsNodes = childDTLocal.getElementsByTagName("connection-url");

                        for(int j=0;j<connectionsNodes.getLength();j++){
                            nodo = connectionsNodes.item(j);
                            if (nodo instanceof Element){
                                System.out.println(" URL :" + nodo.getTextContent());
                                parseUrl(nodo.getTextContent());
                                this.ipaddress = nodo.getTextContent();
                            }
                        }
                        NodeList connectionsNodes2 = childDTLocal.getElementsByTagName("user-name");

                        for(int j=0;j<connectionsNodes2.getLength();j++){
                            nodo = connectionsNodes2.item(j);
                            if (nodo instanceof Element){
                                System.out.println(" USERNAME :" + nodo.getTextContent());
                                this.username = nodo.getTextContent();
                            }
                        }

                        NodeList connectionsNodes3 = childDTLocal.getElementsByTagName("min-pool-size");

                        for(int j=0;j<connectionsNodes3.getLength();j++){
                            nodo = connectionsNodes3.item(j);
                            if (nodo instanceof Element){
                                System.out.println(" Min Pool Size :" + nodo.getTextContent());
                                this.init_connectionts = Integer.parseInt(nodo.getTextContent());
                            }
                        }

                        NodeList connectionsNodes4 = childDTLocal.getElementsByTagName("max-pool-size");

                        for(int j=0;j<connectionsNodes4.getLength();j++){
                            nodo = connectionsNodes4.item(j);
                            if (nodo instanceof Element){
                                System.out.println(" Max Pool Size :" + nodo.getTextContent());
                                this.max_connections = Integer.parseInt(nodo.getTextContent());
                            }
                        }
                    }
            }
        }




    }

    public void parseUrl(String textContent) {
        //To change body of created methods use File | Settings | File Templates.
        assert textContent != null;
        if (textContent.contains("jdbc:oracle:thin:@"))
        {
            String socket = textContent.split("@")[1];
            assert socket.split(":").length == 3;
            ipaddress = socket.split(":")[0];
            port = Integer.parseInt(socket.split(":")[1]);
            this.sid =  socket.split(":")[2];
        }
        else if (textContent.contains("jdbc:hsqldb:hsql:"))
        {
            String socket = textContent.split("//")[1];
            assert socket.split(":").length == 2;
            ipaddress = socket.split(":")[0];
            port = Integer.parseInt(socket.split(":")[1]);


        }
        else if (textContent.contains("jdbc:mysql:"))
        {
            String socket = textContent.split("//")[1];
            assert socket.split(":").length == 2;
            ipaddress = socket.split(":")[0];
            String port_string = (socket.split(":")[1]).split("/")[0];
            port = Integer.parseInt(port_string);
            this.sid =  socket.split("/")[1];

        }



    }


    public void populateDS(String name) throws Exception
    {

        List avaliableDatasources = new LinkedList<DataSource>();
// Get the starting point into the namespace
        Context ctx = new InitialContext();

// retrieve MBean server
        MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();

// create proper jmx name, (1)
        final ObjectName filterName = new ObjectName("jboss.jca:service=DataSourceBinding,*");

// get the results matching given filter. (2)
        final Set<ObjectInstance> mBeans = server.queryMBeans(filterName, null);

// iterate over mbeans and retrieve information about bind name
// (jndi name) of datasource
        for (Iterator<ObjectInstance> iterator = mBeans.iterator(); iterator.hasNext();) {
            ObjectInstance mBean = iterator.next();




 // simple check which guarantees that we are reading only real
 // datasources (not aliases) (3)
        if (isNotAlias(mBean)) {

  // get name for mbean describing current datasource
            final ObjectName dsBindingMBeanName = mBean.getObjectName();

  // one of the attributes of DataSourceBinding bean is
  // "bindname" , we will read that attribute now: (4)
            String bindName = (String) server.getAttribute(dsBindingMBeanName, "BindName");

  // having the jndi name, we can look up the datasource instance (5)
            final javax.sql.DataSource ds = (javax.sql.DataSource) ctx.lookup(bindName);

            avaliableDatasources.add(ds);
    }
  }
    }
//////////////////////

private boolean isNotAlias(ObjectInstance mBean) {

  String ALIAS_SERVICE_CLASS_NAME = "org.jboss.services.binding.AliasJndiService";
  boolean isNotAliasDS = !ALIAS_SERVICE_CLASS_NAME.equals(mBean.getClassName());
  return isNotAliasDS;
}

    public String getNameDS() {
        return nameDS;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public int getPort() {
        return port;
    }

    public String getJdniName() {
        return jdniName;
    }

    public String getMaquina() {
        return maquina;
    }

    public int getMax_connections() {
        return max_connections;
    }

    public int getInit_connectionts() {
        return init_connectionts;
    }

    public String getUsername() {
        return username;
    }

    public String getDriver() {
        return driver;
    }

    public String getSid() {
        return sid;
    }


    public String write(File file, Domain domain) throws Exception
    {
        //To change body of created methods use File | Settings | File Templates.
        assert file != null && file.canWrite() && domain != null;
        PrintWriter writer = null;

        String key = domain.getProduct() +"_"+  domain.getVersion() + "_"+ domain.getName();
        String linea ="key"+"|"+domain.getPais()+"|"+this.getIpaddress()+"|"+this.getPort()+"|"+domain.getMaquina()+"|"+
                this.getIpaddress()+"|JDBC|"+ this.getInit_connectionts()+"|"+this.getMax_connections()+"|"+this.getUsername()+"|null|null|"
                + this.getInit_connectionts()+"|"+domain.getDomain()+"|hawa|null|CPD_NUEVO|"+domain.getInstancia().name+"|null|"+this.getJdniName()+
                "|"+key+this.getIpaddress()+this.port+domain.getInstancia().name;
        System.out.println("Linea:" + linea);
        try
        {
            writer = new PrintWriter(file);
            writer.println(linea);
        } finally {
            writer.flush();
            writer.close();
        }

        return linea;


    }
}
