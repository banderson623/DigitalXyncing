package com.digitalxyncing.communication.impl;

import com.digitalxyncing.communication.Authenticator;
import com.digitalxyncing.communication.ClientAddedListener;
import com.digitalxyncing.communication.HostEndpoint;
import com.digitalxyncing.communication.MessageHandlerFactory;
import com.digitalxyncing.util.ThreadUtils;
import org.zeromq.ZMQ;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implementation of {@link HostEndpoint} which represents the notion of a "host" in a distributed client-host
 * cluster application. This implementation relies on ZeroMQ for communication.
 */
public class ZmqHostEndpoint<T> extends AbstractZmqEndpoint<T> implements HostEndpoint<T> {

    private ConnectionManager connectionManager;
    private Authenticator authenticator;
    private List<ClientAddedListener> clientAddedListeners;

    /**
     * Creates a new {@code ZmqHostEndpoint} instance.
     *
     * @param port                  the port to bind to
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     */
    public ZmqHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory) {
        super(port, ZMQ.PULL, messageHandlerFactory);
        connectionManager = new ConnectionManager();
        clientAddedListeners = new ArrayList<ClientAddedListener>();
    }

    /**
     * Creates a new {@code ZmqHostEndpoint} instance.
     *
     * @param port                  the port to bind to
     * @param messageHandlerFactory the {@link MessageHandlerFactory} to use for handling incoming messages
     * @param authenticator         the {@link Authenticator} to use to authenticate connection requests
     */
    public ZmqHostEndpoint(int port, MessageHandlerFactory<T> messageHandlerFactory, Authenticator authenticator) {
        this(port, messageHandlerFactory);
        this.authenticator = authenticator;
    }

    @Override
    protected ZMQ.Socket getSocket(ZMQ.Context context) {
        return context.socket(ZMQ.PUB);
    }

    @Override
    public void addClient(String address, int port) {
        abstractChannelListener.addPeer(address, port);
        for (ClientAddedListener listener : clientAddedListeners) {
            listener.onClientAdded(address, port);
        }
    }

    @Override
    public int getConnectionRequestPort() {
        return connectionManager.getPort();
    }

    @Override
    public void addClientAddedListener(ClientAddedListener listener) {
        clientAddedListeners.add(listener);
    }

    @Override
    public void openInboundChannel() {
        if (!connectionManager.isAlive())
            connectionManager.start();
        super.openInboundChannel();
    }

    @Override
    public void closeInboundChannel() {
        if (connectionManager.isAlive())
            connectionManager.terminate();
        super.closeInboundChannel();
    }

    @Override
    public boolean isHost() {
        return true;
    }

    @Override
    public boolean requestFullDocument() {
        throw new UnsupportedOperationException();
    }

    private class ConnectionManager extends Thread {

        private ServerSocket connectionSocket;
        private ExecutorService connectionPool;
        private volatile boolean terminate;

        public ConnectionManager() {
            try {
                connectionSocket = new ServerSocket(0);
            } catch (IOException e) {
                // TODO
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            connectionPool = Executors.newSingleThreadExecutor();
            while (!terminate) {
                try {
                    connectionPool.execute(new ConnectionHandler(connectionSocket.accept()));
                } catch (IOException e) {
                    // TODO
                    e.printStackTrace();
                }
            }
            ThreadUtils.shutdownAndAwaitTermination(connectionPool);
        }

        public void terminate() {
            terminate = true;
        }

        public int getPort() {
            return connectionSocket.getLocalPort();
        }

        private class ConnectionHandler implements Runnable {

            private Socket connection;

            public ConnectionHandler(Socket connection) {
                this.connection = connection;
            }

            @Override
            public void run() {
                try {
                    String address = connection.getInetAddress().getHostAddress();
                    if (address.equals("127.0.0.1"))
                        address = "10.0.2.2";
                    int port = connection.getPort();
                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    if (authenticator == null) {
                        // Just add the peer if there's no Authenticator
                        ZmqHostEndpoint.this.addClient(address, port);
                        outputStream.writeUTF("1 " + ZmqHostEndpoint.this.port);
                        outputStream.flush();
                    } else {
                        // Otherwise authenticate the connection request
                        DataInputStream inputStream = new DataInputStream(connection.getInputStream());
                        String token = inputStream.readUTF(); // Blocking call
                        if (authenticator.isAuthenticated(address, port, token)) {
                            ZmqHostEndpoint.this.addClient(address, port);
                            outputStream.writeUTF("1 " + ZmqHostEndpoint.this.port);
                            outputStream.flush();
                        } else {
                            System.out.println("Connection request from " + address + ":" + port + " is not authenticated");
                            outputStream.writeUTF("0 Authentication Failed");
                            outputStream.flush();
                        }
                        inputStream.close();
                    }
                    outputStream.close();
                } catch (IOException e) {
                    // TODO
                    e.printStackTrace();
                } finally {
                    try {
                        connection.close();
                    } catch (IOException e) {
                        // We're boned
                        e.printStackTrace();
                    }
                }
            }

        }

    }

}
