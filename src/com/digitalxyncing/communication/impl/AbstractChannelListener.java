package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.ChannelListener;
import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Abstract implementation of {@link ChannelListener}.
 */
public abstract class AbstractChannelListener extends Thread implements ChannelListener {

    protected final Endpoint mEndpoint;
    protected final ExecutorService mThreadPool;
    protected final MessageHandlerFactory mMessageHandlerFactory;
    protected Map<String, Integer> mPeers;
    protected volatile boolean mTerminate;

    /**
     * Creates a new {@code AbstractChannelListener} instance.
     *
     * @param endpoint              the {@link Endpoint} this {@code AbstractChannelListener} belongs to
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link com.digitalxyncing.communication.MessageHandler} instances
     */
    public AbstractChannelListener(Endpoint endpoint, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        mEndpoint = endpoint;
        mMessageHandlerFactory = messageHandlerFactory;
        mThreadPool = Executors.newFixedThreadPool(threadPoolSize);
        mPeers = new HashMap<String, Integer>();
    }

    @Override
    public void run() {
        listen();
    }

    @Override
    public void terminate() {
        mTerminate = true;
    }

}