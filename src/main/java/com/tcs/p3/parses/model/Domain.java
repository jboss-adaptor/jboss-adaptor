package com.tcs.p3.parses.model;

import com.tcs.p3.parses.GestorConnection;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import java.io.File;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 29/04/14
 * Time: 15:47
 * To change this template use File | Settings | File Templates.
 */
public class Domain {

    private final String product = "jboss";
    private String version;
    private String name;
    private String domain;
    private String maquina;
    private String pais = "ES";
    private File homeDir;
    private String ipaddres;
    private int port;
    private String usuario;
    private SNMP snmpData;
    private Instance instancia;
    private List<Datasource> datasourcesList = null;



    public Domain()
    {
        snmpData = new SNMP();
        instancia = new Instance();
        datasourcesList = new LinkedList<Datasource>();
    }

    public Domain populate() throws Exception {
        assert snmpData != null;
        assert instancia != null;

        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        ObjectName serverConfig=new ObjectName("jboss.system:type=ServerConfig");
        ObjectName serverInfo=new ObjectName("jboss.system:type=ServerInfo");
        ObjectName connectorMbean=new ObjectName("jboss:service=invoker,type=jrmp");
        ObjectName connectorWeb= new ObjectName("jboss.web:type=Connector,*");
        version = (String) connection.getAttribute(serverConfig,"SpecificationVersion");
        name = (String) connection.getAttribute(serverConfig,"ServerName");
        instancia.name = name;
        assert name.length() > 4;
        domain = name.substring(0,4);
        maquina = (String) connection.getAttribute(serverInfo,"HostName");
        instancia.maquina = maquina;
        assert maquina != null;
        homeDir = (File) connection.getAttribute(serverConfig,"ServerHomeDir");
        assert homeDir != null;
        ipaddres = (String) connection.getAttribute(connectorMbean,"ServerAddress");
        instancia.ipaddress = ipaddres;
        port = 8080;
        instancia.port = port;
        snmpData.populate();
        instancia.populate();
        usuario="usagest";
        Set<ObjectInstance> connectors = connection.queryMBeans(connectorWeb,null);
        for (ObjectInstance connector : connectors) {
            String protocol =(String) connection.getAttribute(connector.getObjectName(),"protocol");
            if (protocol.contains("HTTP"))
            {
              port = (Integer) connection.getAttribute(connector.getObjectName(),"port");
            }
        }

        findDatasources();

        return this;
    }

    private void findDatasources() throws Exception {
        //To change body of created methods use File | Settings | File Templates.
        ObjectName datasourcesDeployed = new ObjectName("jboss.deployment:id=\"jboss.jdbc:datasource=*,service=metadata\",type=Component");
        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        Set<ObjectInstance> datasourcesDply = connection.queryMBeans(datasourcesDeployed,null);
        for (Iterator<ObjectInstance> iterator = datasourcesDply.iterator(); iterator.hasNext();) {
            ObjectInstance next = iterator.next();
            String descriptorDSName = (String) connection.getAttribute(next.getObjectName(),"SimpleName");
            Datasource ds = new Datasource();
            ds.populate(descriptorDSName);
            this.datasourcesList.add(ds);
        }

    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getMaquina() {
        return maquina;
    }

    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public File getHomeDir() {
        return homeDir;
    }

    public void setHomeDir(File homeDir) {
        this.homeDir = homeDir;
    }

    public String getIpaddres() {
        return ipaddres;
    }

    public void setIpaddres(String ipaddres) {
        this.ipaddres = ipaddres;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public SNMP getSnmpData() {
        return snmpData;
    }

    public void setSnmpData(SNMP snmpData) {
        this.snmpData = snmpData;
    }

    public String write(File file) throws Exception
    {
        PrintWriter writer = new PrintWriter(file);
        String key = product + version + "_"+ domain;
        String linea =  key + "|" + maquina + "|" + pais + "|" + domain + "|" + version + "|" + this.homeDir + "|"
                + maquina + "|" + port + "|" + this.usuario + "|1|0|null|null|0|" + snmpData.getPort() + "|" + snmpData.getCommunity() + "|120|JBOSS|" + key + maquina + pais + maquina ;
        System.out.println("Linea:" + linea);
        writer.println(linea);
        writer.flush();
        writer.close();
        return linea;
    }

    public Instance getInstancia() {
        return instancia;
    }

    public void setInstancia(Instance instancia) {
        this.instancia = instancia;
    }

    public String getProduct() {
        return product;
    }

    public String writeInstance(File file)throws Exception
    {
        assert file != null && file.canWrite();
        PrintWriter writer = new PrintWriter(file);
        String key = product +"_"+  version + "_"+ domain;
        String linea =  key + "|" + maquina + "|" + pais + "|" + this.instancia.name + "|" + this.instancia.ipaddress + "|" + this.instancia.port +
                "|null|" + instancia.applications.toString() + "|1|null|" + instancia.numThreads + "|" + instancia.mx_mem + "|" + instancia.init_mem + "|null|" + instancia.cluster +  "|null|" + key + maquina + pais + name ;
        System.out.println("Linea:" + linea);
        writer.println(linea);

        writer.flush();
        writer.close();
        return linea;


    }

    public String writeBackends (File file) throws Exception
    {
        assert file != null && file.canWrite();
        String lineaFinal = "";
        for (Iterator<Datasource> iterator = datasourcesList.iterator(); iterator.hasNext();) {
            Datasource next = iterator.next();
            String linea = next.write(file, this);
            lineaFinal = lineaFinal + "\n" + linea;
        }
        return lineaFinal;
    }
}
