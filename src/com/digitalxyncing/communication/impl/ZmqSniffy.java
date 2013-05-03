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

    private List<String> discoverXyncersOnSubnetFromIPOnPort(final String IPAddress, final int portNumber)
    {
        System.out.println("Starting session discovery | discoverXyncersOnSubnetFromIPOnPort");
        List<String> foundHosts = new ArrayList<String>(255);
        try
        {
            // get my IP address
            final String myIpAddress = IPAddress;
            // find where the last octect starts (192.168.0 <--- right here )
            int lastOctetIndex = myIpAddress.lastIndexOf('.');

            // This will be the first three octets of the IP "192.168.0"
            String firstPartOfIpAddress = myIpAddress.substring(0, lastOctetIndex);

            System.out.println("Scanning " + firstPartOfIpAddress+".* for port: " +portNumber);

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
                // If we found a host and it is not US :)
                if(host.wasFound){ // && host.ip != myIpAddress){
                    foundHosts.add(host.ip);
                }
            }
        }

        catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ExecutionException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        System.out.println("Finished session discovery");
        return foundHosts;
    }


            @Override
    public List<String> discoverXyncersOnPort(final int portNumber)
    {
        List<String> foundHosts = new ArrayList<String>(255);

        try
        {
            String myIp = InetAddress.getLocalHost().getHostAddress();
            foundHosts.addAll(discoverXyncersOnSubnetFromIPOnPort(myIp,portNumber));
        } catch (UnknownHostException ignore){
        }
        return foundHosts;

    }

    @Override
    public void discoverXyncersOnMySubnetAndFromListOfIPsAndPort(final List<String> listOfOtherIps,
                                                                 final int portNumber,
                                                                 final HostListCallBack callBack)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                // Add discovered on my own subnet, and others
                List<String> foundHosts = discoverXyncersOnPort(portNumber);
                List<String> otherHosts = new ArrayList<String>();
                for(String ipAddress : listOfOtherIps){
                    otherHosts.addAll(discoverXyncersOnSubnetFromIPOnPort(ipAddress,portNumber));

                }

                // Make sure there are no duplicates
                for(String other : otherHosts){
                    if(!foundHosts.contains(other)){
                        foundHosts.add(other);
                    }
                }

                callBack.results(foundHosts);
            }
        }).start();
    }

    @Override
    public void discoverXyncersOnPort(final int portNumber,
                                      final HostListCallBack callBack)
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                List<String> foundHosts = discoverXyncersOnPort(portNumber);
                callBack.results(foundHosts);
            }
        }).start();

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
