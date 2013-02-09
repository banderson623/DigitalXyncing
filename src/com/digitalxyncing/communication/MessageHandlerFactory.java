package com.digitalxyncing.communication;

/**
 * A {@code MessageHandlerFactory} is used to build new {@link MessageHandler} instances to be used by
 * a {@link ChannelListener} so that it can be used to handle incoming messages.
 */
public interface MessageHandlerFactory {

    /**
     * Constructs a new {@link MessageHandler} for the given message.
     *
     * @param message the message data to handle
     * @return new {@code MessageHandler}
     */
    MessageHandler build(byte[] message);

}