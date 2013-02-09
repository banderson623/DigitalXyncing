package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Abstract implementation of {@link com.digitalxyncing.communication.Endpoint} containing common code for ZeroMQ implementations.
 */
public abstract class AbstractZmqEndpoint implements Endpoint {

    protected AbstractChannelListener mAbstractChannelListener;
    protected ZMQ.Socket mSocket;
    protected int mPort;
    private ZMQ.Context mContext;

    /**
     * Creates a new {@code AbstractZmqEndpoint} instance.
     *
     * @param hostAddress           the host address to connect to
     * @param hostPort              the host port to connect to
     * @param port                  the port to bind to for sending messages
     * @param type                  the ZMQ socket type
     * @param threadPoolSize        the number of worker threads to allocate
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use
     */
    public AbstractZmqEndpoint(String hostAddress, int hostPort, int port, int type, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        mAbstractChannelListener = new ZmqChannelListener(this, hostAddress, hostPort, type, threadPoolSize, messageHandlerFactory);
        mPort = port;
    }

    /**
     * Creates a new {@code AbstractZmqEndpoint} instance.
     *
     * @param type                  the ZMQ socket type
     * @param threadPoolSize        the number of worker threads to allocate
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use
     */
    public AbstractZmqEndpoint(int port, int type, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        mAbstractChannelListener = new ZmqChannelListener(this, type, threadPoolSize, messageHandlerFactory);
        mPort = port;
    }

    /**
     * Creates a {@link ZMQ.Socket} for use by the {@link Endpoint}.
     *
     * @param context the {@link ZMQ.Context} to use to acquire the socket
     * @return {@code ZMQ.Socket}
     */
    protected abstract ZMQ.Socket getSocket(ZMQ.Context context);

    @Override
    public void openInboundChannel() {
        if (!mAbstractChannelListener.isAlive())
            mAbstractChannelListener.start();
    }

    @Override
    public void closeInboundChannel() {
        if (mAbstractChannelListener.isAlive())
            mAbstractChannelListener.terminate();
    }

    @Override
    public void openOutboundChannel() {
        if (mSocket == null) {
            if (mContext == null)
                mContext = ZMQ.context(1);
            mSocket = getSocket(mContext);
        }
        mSocket.bind(Endpoint.SCHEME + "*:" + mPort);
    }

    @Override
    public void closeOutboundChannel() {
        if (mSocket != null) {
            mSocket.close();
            mSocket = null;
        }
        if (mContext != null) {
            mContext.term();
            mContext = null;
        }
    }

    @Override
    public boolean send(byte[] data) {
        if (mSocket == null)
            throw new IllegalStateException("Outbound channel not open!");
        return mSocket.send(data, ZMQ.NOBLOCK);
    }

}
