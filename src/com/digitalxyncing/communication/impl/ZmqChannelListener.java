package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

import java.util.Map;

/**
 * Concrete implementation of {@link AbstractChannelListener} which relies on ZeroMQ for communication.
 */
public class ZmqChannelListener extends AbstractChannelListener {

    private int mType;
    private ZMQ.Socket mSocket;

    /**
     * Creates a new {@code ZmqChannelListener} instance.
     *
     * @param endpoint              the {@link Endpoint} this {@code AbstractChannelListener} belongs to
     * @param address               the address to connect to
     * @param port                  the port to connect to
     * @param type                  the ZMQ socket type
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link com.digitalxyncing.communication.MessageHandler} instances
     */
    public ZmqChannelListener(Endpoint endpoint, String address, int port, int type, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        this(endpoint, type, threadPoolSize, messageHandlerFactory);
        mPeers.put(address, port);
    }

    /**
     * Creates a new {@code ZmqChannelListener} instance.
     *
     * @param endpoint              the {@link Endpoint} this {@code AbstractChannelListener} belongs to
     * @param type                  the ZMQ socket type
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link com.digitalxyncing.communication.MessageHandler} instances
     */
    public ZmqChannelListener(Endpoint endpoint, int type, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        super(endpoint, threadPoolSize, messageHandlerFactory);
        mType = type;
    }

    @Override
    public void addPeer(String address, int port) {
        if (mSocket != null) {
            String connect = Endpoint.SCHEME + address + ':' + port;
            mSocket.connect(connect);
        }
        mPeers.put(address, port);
    }

    @Override
    public void listen() {
        mTerminate = false;
        ZMQ.Context context = ZMQ.context(1);
        mSocket = context.socket(mType);
        for (Map.Entry<String, Integer> peer : mPeers.entrySet()) {
            String connect = Endpoint.SCHEME + peer.getKey() + ':' + peer.getValue();
            System.out.println("Connecting to " + connect);
            mSocket.connect(connect);
        }
        if (mType == ZMQ.SUB)
            mSocket.subscribe("".getBytes());
        while (!mTerminate) {
            byte[] message = mSocket.recv(0);
            mThreadPool.execute(mMessageHandlerFactory.build(mEndpoint, message));
        }
        mSocket.close();
        context.term();
    }

}
