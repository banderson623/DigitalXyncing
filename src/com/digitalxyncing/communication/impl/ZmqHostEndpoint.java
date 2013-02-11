package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.*;
import org.zeromq.ZMQ;

/**
 * Implementation of {@link HostEndpoint} which represents the notion of a "host" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqHostEndpoint extends AbstractZmqEndpoint implements HostEndpoint {

    private static final int POOL_SIZE = 5;

    /**
     * Creates a new {@code ZmqHostEndpoint} instance.
     *
     * @param port                  the port to bind to
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     */
    public ZmqHostEndpoint(int port, MessageHandlerFactory messageHandlerFactory) {
        super(port, ZMQ.PULL, POOL_SIZE, messageHandlerFactory);
    }

    @Override
    protected ZMQ.Socket getSocket(ZMQ.Context context) {
        return context.socket(ZMQ.PUB);
    }

    @Override
    public void addClient(String address, int port) {
        abstractChannelListener.addPeer(address, port);
    }

}