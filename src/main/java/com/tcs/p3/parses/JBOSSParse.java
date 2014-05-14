package com.tcs.p3.parses;

import com.tcs.p3.parses.model.Domain;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 30/04/14
 * Time: 9:08
 * To change this template use File | Settings | File Templates.
 */
public class JBOSSParse {


    private static void getDatosConfig() throws Exception
    {

        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        ObjectName objectName=new ObjectName("jboss.system:type=Server");



   }



    public static void main(String[] args) throws Exception {

        Domain domain = new Domain();
        domain.populate();
        File fichero = new File("WebLogic-"+ domain.getMaquina()+ ".txt");
        domain.write(fichero);







//        String name = (String) connection.getAttribute(objectName, "BuildJVM");
//        Integer threadPoolMaxSize=(Integer)connection.getAttribute(objectName, "threadPoolMaxSize");
        /*Boolean clustered=(Boolean)connection.getAttribute(objectName, "clustered");
        Boolean createBindingsDir=(Boolean)connection.getAttribute(objectName, "createBindingsDir");
        Long journalBufferSize=(Long)connection.getAttribute(objectName, "journalBufferSize");
        Long securityInvalidationInterval=(Long)connection.getAttribute(objectName, "securityInvalidationInterval");
        Boolean messageCounterEnabled=(Boolean)connection.getAttribute(objectName, "messageCounterEnabled");
        Integer journalCompactMinFiles=(Integer)connection.getAttribute(objectName, "journalCompactMinFiles");
        String journalType=(String)connection.getAttribute(objectName, "journalType");
        Boolean journalSyncTransactional=(Boolean)connection.getAttribute(objectName, "journalSyncTransactional");
        Integer scheduledThreadPoolMaxSize=(Integer)connection.getAttribute(objectName, "scheduledThreadPoolMaxSize");
        Boolean securityEnabled=(Boolean)connection.getAttribute(objectName, "securityEnabled");
        String jmxDomain=(String)connection.getAttribute(objectName, "jmxDomain");
        Long transactionTimeout=(Long)connection.getAttribute(objectName, "transactionTimeout");
        String clusterPassword=(String)connection.getAttribute(objectName, "clusterPassword");
        Boolean createJournalDir=(Boolean)connection.getAttribute(objectName, "createJournalDir");
        Long messageCounterSamplePeriod=(Long)connection.getAttribute(objectName, "messageCounterSamplePeriod");
        Boolean persistenceEnabled=(Boolean)connection.getAttribute(objectName, "persistenceEnabled");
        Boolean allowFailback=(Boolean)connection.getAttribute(objectName, "allowFailback");
        Long transactionTimeoutScanPeriod=(Long)connection.getAttribute(objectName, "transactionTimeoutScanPeriod");
        Boolean jmxManagementEnabled=(Boolean)connection.getAttribute(objectName, "jmxManagementEnabled");
        String securityDomain=(String)connection.getAttribute(objectName, "securityDomain");
        Long serverDumpInterval=(Long)connection.getAttribute(objectName, "serverDumpInterval");
        Long failbackDelay=(Long)connection.getAttribute(objectName, "failbackDelay");
        Integer idCacheSize=(Integer)connection.getAttribute(objectName, "idCacheSize");
        Long messageExpiryScanPeriod=(Long)connection.getAttribute(objectName, "messageExpiryScanPeriod");
        Boolean wildCardRoutingEnabled=(Boolean)connection.getAttribute(objectName, "wildCardRoutingEnabled");
        Integer messageCounterMaxDayHistory=(Integer)connection.getAttribute(objectName, "messageCounterMaxDayHistory");
        Boolean started=(Boolean)connection.getAttribute(objectName, "started");

        System.out.println("    threadPoolMaxSize              = "+threadPoolMaxSize);
        System.out.println("    clustered                      = "+clustered);
        System.out.println("    createBindingsDir              = "+createBindingsDir);
        System.out.println("    journalBufferSize              = "+journalBufferSize);
        System.out.println("    securityInvalidationInterval   = "+securityInvalidationInterval);
        System.out.println("    messageCounterEnabled          = "+messageCounterEnabled);
        System.out.println("    journalType                    = "+journalType);
        System.out.println("    journalSyncTransactional       = "+journalSyncTransactional);
        System.out.println("    scheduledThreadPoolMaxSize     = "+scheduledThreadPoolMaxSize);
        System.out.println("    securityEnabled                = "+securityEnabled);
        System.out.println("    jmxDomain                      = "+jmxDomain);
        System.out.println("    transactionTimeout             = "+transactionTimeout);
        System.out.println("    clusterPassword                = "+clusterPassword);
        System.out.println("    createJournalDir               = "+createJournalDir);
        System.out.println("    messageCounterSamplePeriod     = "+messageCounterSamplePeriod);
        System.out.println("    persistenceEnabled             = "+persistenceEnabled);
        System.out.println("    allowFailback                  = "+allowFailback);
        System.out.println("    transactionTimeoutScanPeriod   = "+transactionTimeoutScanPeriod);
        System.out.println("    jmxManagementEnabled           = "+jmxManagementEnabled);
        System.out.println("    securityDomain                 = "+securityDomain);
        System.out.println("    serverDumpInterval             = "+serverDumpInterval);
        System.out.println("    failbackDelay                  = "+failbackDelay);
        System.out.println("    idCacheSize                    = "+idCacheSize);
        System.out.println("    messageExpiryScanPeriod        = "+messageExpiryScanPeriod);
        System.out.println("    wildCardRoutingEnabled         = "+wildCardRoutingEnabled);
        System.out.println("    messageCounterMaxDayHistory    = "+messageCounterMaxDayHistory);
        System.out.println("    started                        = "+started);
*/

    }




}

