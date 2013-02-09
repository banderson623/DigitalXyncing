package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.ChannelListener;
import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.HostEndpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Implementation of {@link HostEndpoint} which represents the notion of a "host" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqHostEndpoint implements HostEndpoint {

    private static final int POOL_SIZE = 5;

    private int mPort;
    private ZMQ.Context mContext;
    private ZMQ.Socket mSocket;
    private ChannelListener mChannelListener;

    /**
     * Creates a new {@code ZmqHostEndpoint} instance.
     *
     * @param port                  the port to bind to
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     */
    public ZmqHostEndpoint(int port, MessageHandlerFactory messageHandlerFactory) {
        mPort = port;
        mChannelListener = new ZmqChannelListener(ZMQ.PULL, POOL_SIZE, messageHandlerFactory);
    }

    @Override
    public void openOutboundChannel() {
        if (mSocket != null)
            return;
        if (mContext == null)
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
    public void openInboundChannel() {
        if (!mChannelListener.isAlive())
            mChannelListener.start();
    }

    @Override
    public void closeInboundChannel() {
        if (mChannelListener.isAlive())
            mChannelListener.terminate();
    }

    @Override
    public void send(byte[] data) {
        if (mSocket == null)
            throw new IllegalStateException("Outbound channel not open!");
        // Host will publish messages to all subscribers
        mSocket.send(data, 0);
    }

    @Override
    public void addClient(String address, int port) {
        mChannelListener.addPeer(address, port);
    }
    
}
