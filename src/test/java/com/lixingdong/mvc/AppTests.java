package com.lixingdong.mvc;


import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class AppTests {
    public static void main(String[] args){
        Server jettyServer = new Server();

        SocketConnector conn = new SocketConnector();
        conn.setPort(8081);
        jettyServer.setConnectors(new Connector[]{conn});

        WebAppContext wah = new WebAppContext();
        wah.setContextPath("/lxd");
        wah.setWar("src/main/webapp");
        jettyServer.setHandler(wah);
        try {
            jettyServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
