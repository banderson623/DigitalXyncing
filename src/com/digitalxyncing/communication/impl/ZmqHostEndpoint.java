package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.HostEndpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

import java.io.Serializable;

/**
 * Implementation of {@link HostEndpoint} which represents the notion of a "host" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqHostEndpoint<T extends Serializable> extends AbstractZmqEndpoint<T> implements HostEndpoint<T> {

    /**
     * Creates a new {@code ZmqHostEndpoint} instance.
     *
     * @param port                  the port to bind to
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     */
    public ZmqHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory) {
        super(port, ZMQ.PULL, messageHandlerFactory);
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
