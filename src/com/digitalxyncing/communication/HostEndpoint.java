package com.digitalxyncing.communication;

/**
 * {@code HostEndpoint} represents the notion of a "host" in a distributed client-host
 * cluster application.
 */
public interface HostEndpoint<T> extends Endpoint<T> {

    /**
     * Adds the given client, identified by an address and port, to the {@code HostEndpoint}.
     * This allows the {@code HostEndpoint} to receive messages from it.
     *
     * @param address the address of the peer to add
     * @param port    the port of the peer to add
     */
    void addClient(String address, int port);

    /**
     * Returns the port that's listening for connection requests.
     *
     * @return connection request port
     */
    int getConnectionRequestPort();

}
