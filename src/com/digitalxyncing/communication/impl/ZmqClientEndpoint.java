package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.ChannelListener;
import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Implementation of {@link Endpoint} which represents the notion of a "client" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqClientEndpoint implements Endpoint {

    private static final int POOL_SIZE = 5;

    private String mHost;
    private int mPort;
    private ChannelListener mChannelListener;
    private ZMQ.Context mContext;
    private ZMQ.Socket mSocket;

    /**
     * Creates a new {@code ZmqClientEndpoint} instance.
     *
     * @param host the host address to connect to
     * @param port the host port to connect to
     */
    public ZmqClientEndpoint(String host, int port) {
        mHost = host;
        mPort = port;

    }

    @Override
    public void openOutboundChannel() {
        mContext = ZMQ.context(1);
        mSocket = mContext.socket(ZMQ.PUSH);
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
        if (mChannelListener == null) {
            mChannelListener = new ZmqChannelListener(mHost, mPort, POOL_SIZE, messageHandlerFactory);
            mChannelListener.start();
        }
    }

    @Override
    public void closeInboundChannel() {
        if (mChannelListener != null)
            mChannelListener.terminate();
    }

    @Override
    public void send(byte[] data) {
        if (mSocket == null)
            throw new IllegalStateException("Outbound channel not open!");
        // Client will push messages to host
        mSocket.send(data, 0);
    }

}
