package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Sniffy;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * User: brian_anderson
 * Date: 4/29/13
 * <p/>
 * Add some readme here about how this operates
 */
public class ZmqSniffy implements Sniffy {
    @Override
    public List<String> discoverXyncersOnPort(final int portNumber)
    {
        List<String> foundHosts = new ArrayList<String>(255);
        try
        {
            // get my IP address
            String myIpAddress = InetAddress.getLocalHost().getHostAddress();
            // find where the last octect starts (192.168.0 <--- right here )
            int lastOctetIndex = myIpAddress.lastIndexOf('.');

            // This will be the first three octets of the IP "192.168.0"
            String firstPartOfIpAddress = myIpAddress.substring(0, lastOctetIndex);

            final ExecutorService exec = Executors.newFixedThreadPool(20);
            final int timeout = 200;

            final List<Future<FoundHost>> futures = new ArrayList<Future<FoundHost>>();
            for (int lastOctetValue = 1; lastOctetValue <= 255; lastOctetValue++) {
                // Build up the IP to scan
                final String fullIpAddressToScan = firstPartOfIpAddress  + "." + lastOctetValue;
                // add the scanner to the future.
                futures.add(portIsOpen(exec, fullIpAddressToScan, portNumber, timeout));
            }
            // All done with my executor service
            exec.shutdown();
            for (final Future<FoundHost> f : futures) {
                FoundHost host = f.get();
                if(host.wasFound){
                    foundHosts.add(host.ip);
                }
            }
        }
        catch (UnknownHostException e)
        {
            // do nothing...
        } catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return foundHosts;
    }

    private final class FoundHost {
        private String ip;
        private boolean wasFound;

        public FoundHost(String ip, boolean wasFound){
            this.ip = ip;
            this.wasFound = wasFound;
        }
    }

    // heavily borrowed from http://stackoverflow.com/questions/11547082/fastest-way-to-scan-ports-with-java
    public Future<FoundHost> portIsOpen(final ExecutorService executor,
                                             final String ip,
                                             final int port,
                                             final int timeout) {
        // This will create a new anonymous inner class a port scan
        return executor.submit(new Callable<FoundHost>() {
            @Override public FoundHost call() {
                try {
                    Socket socket = new Socket();
                    // try to connect to the socket!
                    socket.connect(new InetSocketAddress(ip, port), timeout);
                    // now close it, we are good!
                    socket.close();
                    return new FoundHost(ip,true);

                } catch (Exception ex) {
                    // fail quietly, very very quietly.
//                    return false;
                    return new FoundHost(ip,false);
                }
            }
        });
    }
}
