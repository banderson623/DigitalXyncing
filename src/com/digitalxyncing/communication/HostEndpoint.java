package com.digitalxyncing.communication;

import java.io.Serializable;

/**
 * {@code HostEndpoint} represents the notion of a "host" in a distributed client-host
 * cluster application.
 */
public interface HostEndpoint<T extends Serializable> extends Endpoint<T> {

    /**
     * Adds the given client, identified by an address and port, to the {@code HostEndpoint}.
     * This allows the {@code HostEndpoint} to receive messages from it.
     *
     * @param address the address of the peer to add
     * @param port    the port of the peer to add
     */
    void addClient(String address, int port);

}
