package com.tcs.p3.parses.model;

import junit.framework.TestCase;

/**
 * Created by IntelliJ IDEA.
 * User: icorrales
 * Date: 8/05/14
 * Time: 12:07
 * To change this template use File | Settings | File Templates.
 */
public class DatasourceTest extends TestCase {
    public void testPopulate() throws Exception {

        Datasource ds = new Datasource();
        ds.populate("DefaultDS");


    }

    public void testVendorURLString() throws Exception {

        Datasource ds = new Datasource();
        ds.parseUrl("jdbc:mysql://mysql-hostname:3306/jbossdb");
        compareURLs(ds,"mysql-hostname",3306,"jbossdb");
        ds.parseUrl("jdbc:hsqldb:hsql://${jboss.bind.address}:1701");
        compareURLs(ds,"${jboss.bind.address}",1701,null);
        ds.parseUrl("jdbc:oracle:thin:@youroraclehost:1521:yoursid");
        compareURLs(ds,"youroraclehost",1521,"yoursid");
    }

    private void compareURLs(Datasource ds,String ip, int port, String sid) {
        assertEquals("Aserto de Ip ", ip, ds.getIpaddress());
        assertEquals("Aserto de puerto ", port, ds.getPort());
        if ( sid != null) { assertEquals("Aserto de SID", sid, ds.getSid());}
    }




}
