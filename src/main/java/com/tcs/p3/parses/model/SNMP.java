package com.tcs.p3.parses.model;

import com.tcs.p3.parses.GestorConnection;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 30/04/14
 * Time: 15:41
 * To change this template use File | Settings | File Templates.
 */
public class SNMP {

    private String stateSnmp;

    public String getStateSnmp() {
        return stateSnmp;
    }

    public String getCommunity() {
        return community;
    }

    public int getPort() {
        return port;
    }

    private String community;
    private int port;

    public SNMP()
    {

    }


    public SNMP populate() throws  Exception
    {
        MBeanServerConnection connection = GestorConnection.getInstancia().getConnection();
        ObjectName configSNMP=new ObjectName("jboss.jmx:name=SnmpAgent,service=snmp,type=adaptor");
        stateSnmp = (String) connection.getAttribute(configSNMP,"StateString");
        if ( stateSnmp == null || stateSnmp != "Started")
        {
            stateSnmp="Stopped";
            community = "";
            port=0;
        }
        else
        {

            community = (String) connection.getAttribute(configSNMP,"ReadCommunity");
            port = (Integer) connection.getAttribute(configSNMP,"Port");

        }

        return this;
    }
}
