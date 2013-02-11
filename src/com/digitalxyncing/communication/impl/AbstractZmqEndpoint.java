package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Abstract implementation of {@link com.digitalxyncing.communication.Endpoint} containing common code for ZeroMQ implementations.
 */
public abstract class AbstractZmqEndpoint implements Endpoint {

    protected AbstractChannelListener abstractChannelListener;
    protected ZMQ.Socket socket;
    protected int port;
    private ZMQ.Context context;

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
        this.abstractChannelListener = new ZmqChannelListener(this, hostAddress, hostPort, type, threadPoolSize, messageHandlerFactory);
        this.port = port;
    }

    /**
     * Creates a new {@code AbstractZmqEndpoint} instance.
     *
     * @param type                  the ZMQ socket type
     * @param threadPoolSize        the number of worker threads to allocate
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use
     */
    public AbstractZmqEndpoint(int port, int type, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        this.abstractChannelListener = new ZmqChannelListener(this, type, threadPoolSize, messageHandlerFactory);
        this.port = port;
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
        if (!abstractChannelListener.isAlive())
            abstractChannelListener.start();
    }

    @Override
    public void closeInboundChannel() {
        if (abstractChannelListener.isAlive())
            abstractChannelListener.terminate();
    }

    @Override
    public void openOutboundChannel() {
        if (socket == null) {
            if (context == null)
                context = ZMQ.context(1);
            socket = getSocket(context);
        }
        socket.bind(Endpoint.SCHEME + "*:" + port);
    }

    @Override
    public void closeOutboundChannel() {
        if (socket != null) {
            socket.close();
            socket = null;
        }
        if (context != null) {
            context.term();
            context = null;
        }
    }

    @Override
    public boolean send(byte[] data) {
        if (socket == null)
            throw new IllegalStateException("Outbound channel not open!");
        return socket.send(data, ZMQ.NOBLOCK);
    }

}
