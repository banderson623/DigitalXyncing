package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.ChannelListener;
import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

import java.util.Map;

/**
 * Concrete implementation of {@link ChannelListener} which relies on ZeroMQ for communication.
 */
public class ZmqChannelListener extends ChannelListener {

    private int mType;
    private ZMQ.Socket mSocket;

    /**
     * Creates a new {@code ZmqChannelListener} instance.
     *
     * @param address                  the address to connect to
     * @param port                  the port to connect to
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link com.digitalxyncing.communication.MessageHandler} instances
     */
    public ZmqChannelListener(String address, int port, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        this(ZMQ.SUB, threadPoolSize, messageHandlerFactory);
        mPeers.put(address, port);
    }

    public ZmqChannelListener(int type, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        super(threadPoolSize, messageHandlerFactory);
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
    protected void listen() {
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
            mThreadPool.execute(mMessageHandlerFactory.build(message));
        }
        mSocket.close();
        context.term();
    }

}
