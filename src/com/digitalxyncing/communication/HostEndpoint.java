package com.digitalxyncing.communication;

/**
 * {@code HostEndpoint} represents the notion of a "host" in a distributed client-host
 * cluster application.
 */
public interface HostEndpoint extends Endpoint {

    /**
     * Adds the given client, identified by an address and port, to the {@code HostEndpoint}.
     *
     * @param address the address of the peer to add
     * @param port    the port of the peer to add
     */
    void addClient(String address, int port);

}
