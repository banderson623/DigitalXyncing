package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.ChannelListener;
import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

/**
 * Concrete implementation of {@link ChannelListener} which relies on ZeroMQ for communication.
 */
public class ZmqChannelListener extends ChannelListener {

    private String mHost;
    private int mPort;

    /**
     * Creates a new {@code ZmqChannelListener} instance.
     *
     * @param host                  the host to subscribe to
     * @param port                  the port to subscribe to
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link com.digitalxyncing.communication.MessageHandler} instances
     */
    public ZmqChannelListener(String host, int port, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        super(threadPoolSize, messageHandlerFactory);
        mHost = host;
        mPort = port;
    }

    @Override
    protected void listen() {
        mTerminate = false;
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket socket = context.socket(ZMQ.SUB);
        String connect = Endpoint.SCHEME + mHost + ':' + mPort;
        System.out.println("Subscribing to " + connect);
        socket.connect(connect);
        socket.subscribe("".getBytes());
        while (!mTerminate) {
            byte[] message = socket.recv(0);
            mThreadPool.execute(mMessageHandlerFactory.build(message));
        }
        socket.close();
        context.term();
    }

}
