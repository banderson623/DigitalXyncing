package com.digitalxyncing.communication;

/**
 * A {@code MessageHandler} is used to react to a message that has been received by an {@link Endpoint}.
 * It implements {@link Runnable} so that it can be executed within the context of a worker thread.
 */
public abstract class MessageHandler implements Runnable {

    protected final byte[] message;

    /**
     * Creates a new {@code MessageHandler} instance.
     *
     * @param message the message data to handle
     */
    public MessageHandler(byte[] message) {
        this.message = message;
    }

}