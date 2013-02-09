package com.digitalxyncing.communication;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A {@code ChannelListener} is responsible for listening for incoming messages and delegating them to a
 * {@link MessageHandler}.
 */
public abstract class ChannelListener extends Thread {

    protected final ExecutorService mThreadPool;
    protected final MessageHandlerFactory mMessageHandlerFactory;
    protected Map<String, Integer> mPeers;
    protected volatile boolean mTerminate;

    /**
     * Creates a new {@code ChannelListener} instance.
     *
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link MessageHandler} instances
     */
    public ChannelListener(int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        mMessageHandlerFactory = messageHandlerFactory;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
        mPeers = new HashMap<String, Integer>();
    }

    @Override
    public void run() {
        listen();
    }

    /**
     * Notifies the {@code ChannelListener} to stop listening and to release any system resources.
     */
    public void terminate() {
        mTerminate = true;
    }

    /**
     * Adds the given peer, identified by an address and port, to the {@code ChannelListener}.
     *
     * @param address the address of the peer to add
     * @param port    the port of the peer to add
     */
    public abstract void addPeer(String address, int port);

    /**
     * Notifies the {@code ChannelListener} to start listening.
     */
    protected abstract void listen();

}