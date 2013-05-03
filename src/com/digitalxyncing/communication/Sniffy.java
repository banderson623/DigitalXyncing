package com.digitalxyncing.communication;

import java.util.List;

/**
 * Created by Brian Anderson, see impl/ZmqSniffy.java for an implementation of this.
 */
public interface Sniffy {
    /**
     * Discovers the other digital xyncing services and returns
     * a list of ip addresses..
     *
     * @param portNumber
     * @return list of ip addresses
     */
    List<String> discoverXyncersOnPort(int portNumber);

    /**
     * Will scan the local subnet, 10.0.0.* (or whatever the DX-node's current IP address is)
     * and will call "callBack.results(...)" passing in a list of Strings mapping to discovered
     * IP addresses
     *
     * @param fullIpAddress this is a list of full IP addresses. Each IP addresses' subnet
     *                      will be scanned and discovered DX-nodes will be return in the results
     *                      call back
     * @param portNumber    This is the port number to scan on
     * @param callBack      The Callback object's results() method will be called once all of the
     *                      required network-nodes have been scanned. This list will include the
     *                      IP addresses of all the Digital Xyncing nodes that are hosting a document.
     */
    void discoverXyncersOnMySubnetAndFromListOfIPsAndPort(final List<String> fullIpAddress,
                                                          final int portNumber,
                                                          final HostListCallBack callBack);


    void discoverXyncersOnPort(int portNumber, HostListCallBack callBack);

    public interface HostListCallBack {
        public void results(List<String> listOfDiscoveredIPs);
    }
}
