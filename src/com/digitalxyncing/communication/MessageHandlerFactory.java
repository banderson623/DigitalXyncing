package com.digitalxyncing.communication;

/**
 * A {@code MessageHandlerFactory} is used to build new {@link MessageHandler} instances to be used by
 * a {@link com.digitalxyncing.communication.impl.AbstractChannelListener} so that it can be used to handle incoming messages.
 */
public interface MessageHandlerFactory<T> {

    /**
     * Constructs a new {@link MessageHandler} for the given message.
     *
     * @param endpoint    the {@link Endpoint} that received the message
     * @param messageData the message data to handle
     */
    MessageHandler build(Endpoint<T> endpoint, byte[] messageData);

}