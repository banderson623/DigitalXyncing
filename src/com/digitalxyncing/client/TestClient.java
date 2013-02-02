package com.digitalxyncing.client;

import java.net.*;

/**
 * User: brian_anderson
 * Date: 2/1/13
 * <p/>
 * Add some readme here about how this operates
 */
public class TestClient {
    public static void main(String[] args){
        // Listening
        System.out.println("Starting client");
        String myIP;

        try {
            InetAddress ip = InetAddress.getLocalHost();
            myIP =      ip.getHostAddress();
        } catch (UnknownHostException e) {
            myIP = "Unknown host";
        }

        DatagramClient host = new DatagramClient("");
        System.out.println("Listening on " + myIP + ":1337");
        host.listen();

    }
}
