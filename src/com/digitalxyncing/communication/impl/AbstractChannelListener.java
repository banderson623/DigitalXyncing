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

    protected final Endpoint endpoint;
    protected final ExecutorService threadPool;
    protected final MessageHandlerFactory messageHandlerFactory;
    protected Map<String, Integer> peers;
    protected volatile boolean terminate;

    /**
     * Creates a new {@code AbstractChannelListener} instance.
     *
     * @param endpoint              the {@link Endpoint} this {@code AbstractChannelListener} belongs to
     * @param threadPoolSize        the number of worker threads to use to handle messages
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use to construct
     *                              {@link com.digitalxyncing.communication.MessageHandler} instances
     */
    public AbstractChannelListener(Endpoint endpoint, int threadPoolSize, MessageHandlerFactory messageHandlerFactory) {
        this.endpoint = endpoint;
        this.messageHandlerFactory = messageHandlerFactory;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
        this.peers = new HashMap<String, Integer>();
    }

    @Override
    public void run() {
        listen();
    }

    @Override
    public void terminate() {
        terminate = true;
    }

}