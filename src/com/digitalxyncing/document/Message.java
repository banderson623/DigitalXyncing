package com.digitalxyncing.document;

/**
 * Represents a message sent from an {@link com.digitalxyncing.communication.Endpoint}.
 */
public interface Message<T> {

    public static enum MessageType {FULL_DOCUMENT, DOCUMENT_FRAGMENT};

    /**
     * Returns the {@link MessageType} of this {@code Message}.
     *
     * @return {@code MessageType}
     */
    MessageType getType();

    /**
     * Returns the {@code Message} as a byte array prefixed with the channel bytes.
     *
     * @return byte array
     */
    byte[] getPrefixedByteArray();

    byte[] getData();

}
