package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import com.digitalxyncing.document.Message.MessageType;
import com.digitalxyncing.document.impl.MessageImpl;
import org.zeromq.ZMQ;

/**
 * Implementation of {@link Endpoint} which represents the notion of a "client" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqClientEndpoint extends AbstractZmqEndpoint {

    /**
     * Creates a new {@code ZmqClientEndpoint} instance.
     *
     * @param hostAddress           the host address to connect to
     * @param hostPort              the host port to connect to
     * @param port                  the port to bind to for sending messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     */
    public ZmqClientEndpoint(String hostAddress, int hostPort, int port, MessageHandlerFactory messageHandlerFactory) {
        super(hostAddress, hostPort, port, ZMQ.SUB, messageHandlerFactory);
    }

    @Override
    protected ZMQ.Socket getSocket(ZMQ.Context context) {
        return context.socket(ZMQ.PUSH);
    }

    @Override
    public boolean isHost() {
        return false;
    }

    @Override
    public boolean requestFullDocument() {
        return send(new MessageImpl(MessageType.FULL_DOCUMENT_REQUEST));
    }
}
