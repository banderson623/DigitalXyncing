package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Implementation of {@link Endpoint} which represents the notion of a "host" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqHostEndpoint implements Endpoint {

    private int mPort;
    private ZMQ.Context mContext;
    private ZMQ.Socket mSocket;

    /**
     * Creates a new {@code ZmqHostEndpoint} instance.
     *
     * @param port the port to use
     */
    public ZmqHostEndpoint(int port) {
        mPort = port;
    }

    @Override
    public void openOutboundChannel() {
        mContext = ZMQ.context(1);
        mSocket = mContext.socket(ZMQ.PUB);
        mSocket.bind(Endpoint.SCHEME + "*:" + mPort);
    }

    @Override
    public void closeOutboundChannel() {
        if (mSocket != null)
            mSocket.close();
        if (mContext != null)
            mContext.term();
    }

    @Override
    public void openInboundChannel(MessageHandlerFactory messageHandlerFactory) {
        // TODO connect to clients
    }

    @Override
    public void closeInboundChannel() {
        // TODO
    }

    @Override
    public void send(byte[] data) {
        if (mSocket == null)
            throw new IllegalStateException("Outbound channel not open!");
        // Host will publish messages to all subscribers
        mSocket.send(data, 0);
    }

}
