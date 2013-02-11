package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Endpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import org.zeromq.ZMQ;

import java.io.Serializable;
import java.util.Map;

/**
 * Concrete implementation of {@link AbstractChannelListener} which relies on ZeroMQ for communication.
 */
public class ZmqChannelListener<T extends Serializable> extends AbstractChannelListener<T> {

    private int type;
    private ZMQ.Socket socket;

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
    public ZmqChannelListener(Endpoint<T> endpoint, String address, int port, int type, int threadPoolSize, MessageHandlerFactory<T> messageHandlerFactory) {
        this(endpoint, type, threadPoolSize, messageHandlerFactory);
        this.peers.put(address, port);
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
    public ZmqChannelListener(Endpoint<T> endpoint, int type, int threadPoolSize, MessageHandlerFactory<T> messageHandlerFactory) {
        super(endpoint, threadPoolSize, messageHandlerFactory);
        this.type = type;
    }

    @Override
    public void addPeer(String address, int port) {
        if (socket != null) {
            String connect = Endpoint.SCHEME + address + ':' + port;
            socket.connect(connect);
        }
        peers.put(address, port);
    }

    @Override
    public void listen() {
        terminate = false;
        ZMQ.Context context = ZMQ.context(1);
        socket = context.socket(type);
        for (Map.Entry<String, Integer> peer : peers.entrySet()) {
            String connect = Endpoint.SCHEME + peer.getKey() + ':' + peer.getValue();
            System.out.println("Connecting to " + connect);
            socket.connect(connect);
        }
        if (type == ZMQ.SUB) {
            // Subscribe to ALL messages
            socket.subscribe("".getBytes());
        }
        while (!terminate) {
            // This is a blocking call which waits for incoming data
            // When data is received, it's sent to a handler which executes on a worker thread
            threadPool.execute(messageHandlerFactory.build(endpoint, socket.recv(0)));
        }
        socket.close();
        context.term();
    }

}
