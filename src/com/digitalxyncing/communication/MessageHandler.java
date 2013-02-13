package com.digitalxyncing.communication;

import com.digitalxyncing.document.Message.MessageType;
import com.digitalxyncing.document.impl.AbstractMessage;

/**
 * A {@code MessageHandler} is used to react to a message that has been received by an {@link Endpoint}.
 * It implements {@link Runnable} so that it can be executed within the context of a worker thread
 * asynchronously.
 */
public abstract class MessageHandler implements Runnable {

    private final byte[] message;

    /**
     * Creates a new {@code MessageHandler} instance.
     *
     * @param message the message to handle
     */
    public MessageHandler(byte[] message) {
        this.message = message;
    }

    /**
     * Processes the message asynchronously.
     *
     * @param message     the message to process
     * @param pos         the starting index of the real payload, normally 1
     * @param messageType the {@link MessageType} of the message to handle
     */
    protected abstract void handle(byte[] message, int pos, MessageType messageType);

    @Override
    public final void run() {
        byte marker = message[0];
        MessageType type = marker == AbstractMessage.FULL_DOCUMENT ?
                MessageType.FULL_DOCUMENT : MessageType.DOCUMENT_FRAGMENT;
        handle(message, 1, type);
    }

}