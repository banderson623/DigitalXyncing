package com.digitalxyncing.communication;

import com.digitalxyncing.document.Message.MessageType;

import java.io.Serializable;

/**
 * A {@code MessageHandlerFactory} is used to build new {@link MessageHandler} instances to be used by
 * a {@link com.digitalxyncing.communication.impl.AbstractChannelListener} so that it can be used to handle incoming messages.
 */
public interface MessageHandlerFactory<T extends Serializable> {

    /**
     * Constructs a new {@link MessageHandler} for the given message.
     *
     * @param endpoint    the {@link Endpoint} that received the message
     * @param messageData the message data to handle
     * @param messageType the {@link MessageType} of the received message
     * @return new {@code MessageHandler}
     */
    MessageHandler build(Endpoint<T> endpoint, byte[] messageData, MessageType messageType);

}