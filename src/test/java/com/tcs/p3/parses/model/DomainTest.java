package com.tcs.p3.parses.model;

import com.tcs.p3.parses.GestorConnection;
import junit.framework.TestCase;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 7/05/14
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
public class DomainTest extends TestCase {
    Domain domain = null;
    File file = null;

    public void setUp() throws  Exception
    {
        GestorConnection.getInstancia().getConnection();
        domain = new Domain();
    }

    public void tearDown() throws Exception
    {

    }

    public void testPopulate() throws Exception {


        domain.populate();
        assertEquals("Prueba de address: ",domain.getIpaddres(),"192.168.56.103");
        assertEquals("Prueba de puerto : ",domain.getPort(),8080);
        assertNotNull("Prueba de instancia",domain.getInstancia());
        assertEquals("Prueba de thread de instancia",domain.getInstancia().numThreads, 10);
        assertEquals("Prueba de host","localhost.localdomain",domain.getMaquina());


    }

    public void testServer() throws Exception {


        domain.populate();

        Instance instance = domain.getInstancia();
        assertNotNull("Instancia server", instance);
        assertEquals("Instancia Nombre","default",instance.name);
        assertEquals("Prueba de thread de instancia",instance.numThreads, 10);
        assertTrue("Prueba de aplicaciones", instance.applications.contains("ROOT.war"));
        assertEquals("Prueba de aplicaciones", instance.applications.size(), 6);


    }

    public void testEscritura() throws  Exception
    {
        domain.populate();
        file = new File("./WebLogicInstances-"+domain.getMaquina()+".txt");
        String linea = domain.writeInstance(file);
        String fila = "jboss_5.1.0.GA_defa|localhost.localdomain|ES|default|192.168.56.103|8080|null|[jmx-console.war, jbossws-management.war, ROOT.war, invoker.war, web-console.war, sample.war]|1|null|10|518979584|518979584|null|-|null|jboss_5.1.0.GA_defalocalhost.localdomainESdefault";
        assertEquals("Prueba de escritura de Instancia",linea, fila);
        file = new File("./WebLogic-"+domain.getMaquina()+".txt");
        linea = domain.write(file);
        fila = "jboss5.1.0.GA_defa|localhost.localdomain|ES|defa|5.1.0.GA|\\root\\jboss-5.1.0.GA\\server\\default|localhost.localdomain|8080|usagest|1|0|null|null|0|0||120|JBOSS|jboss5.1.0.GA_defalocalhost.localdomainESlocalhost.localdomain";
        assertEquals("Prueba de escritura",linea, fila);
    }

    public void testDatasource() throws Exception
    {
        domain.populate();
        file = new File("./Backends-"+domain.getMaquina()+".txt");
        String linea = domain.writeBackends(file);


    }


}
