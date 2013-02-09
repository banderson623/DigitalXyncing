package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Implementation of {@link Endpoint} which represents the notion of a "client" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqClientZmqEndpoint extends AbstractZmqEndpoint {

    private static final int POOL_SIZE = 5;

    /**
     * Creates a new {@code ZmqClientZmqEndpoint} instance.
     *
     * @param hostAddress           the host address to connect to
     * @param hostPort              the host port to connect to
     * @param port                  the port to bind to for sending messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     */
    public ZmqClientZmqEndpoint(String hostAddress, int hostPort, int port, MessageHandlerFactory messageHandlerFactory) {
        super(hostAddress, hostPort, port, ZMQ.SUB, POOL_SIZE, messageHandlerFactory);
    }

    @Override
    protected ZMQ.Socket getSocket(ZMQ.Context context) {
        return context.socket(ZMQ.PUSH);
    }
}
