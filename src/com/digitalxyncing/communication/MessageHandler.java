package com.digitalxyncing.communication;

import com.digitalxyncing.document.Message.MessageType;

/**
 * A {@code MessageHandler} is used to react to a message that has been received by an {@link Endpoint}.
 * It implements {@link Runnable} so that it can be executed within the context of a worker thread
 * asynchronously.
 */
public abstract class MessageHandler implements Runnable {

    private final byte[] message;
    private final MessageType messageType;

    /**
     * Creates a new {@code MessageHandler} instance.
     *
     * @param message     the message to handle
     * @param messageType the {@link MessageType} of the message
     */
    public MessageHandler(byte[] message, MessageType messageType) {
        this.message = message;
        this.messageType = messageType;
    }

    /**
     * Processes the message asynchronously.
     *
     * @param message     the message to process
     * @param messageType the {@link MessageType} of the message to handle
     */
    protected abstract void handle(byte[] message, MessageType messageType);

    @Override
    public final void run() {
        handle(message, messageType);
    }

}