package com.digitalxyncing.communication;

/**
 * An {@code Endpoint} is a single node in the application cluster. It has the ability to both transmit
 * and receive messages.
 */
public interface Endpoint {

    public static final String SCHEME = "tcp://";

    /**
     * Opens an outbound channel for transmitting messages. This method is idempotent, meaning it
     * has no effect if an outbound channel is already open.
     */
    void openOutboundChannel();

    /**
     * Closes the outbound channel, releasing any sockets or other system resources. This method is
     * idempotent, meaning it has no effect if an outbound channel is not open.
     */
    void closeOutboundChannel();

    /**
     * Opens an inbound channel for receiving messages. This method is idempotent, meaning it
     * has no effect if an inbound channel is already open.
     */
    void openInboundChannel();

    /**
     * Closes the inbound channel, releasing any sockets or other system resources. This method is
     * idempotent, meaning it has no effect if an inbound channel is not open.
     */
    void closeInboundChannel();

    /**
     * Transmits the given message.
     *
     * @param data the message data to send
     */
    boolean send(byte[] data);

}
