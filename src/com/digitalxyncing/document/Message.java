package com.digitalxyncing.document;

/**
 * Represents a message sent from an {@link com.digitalxyncing.communication.Endpoint}.
 */
public interface Message {

    public static final byte FULL_DOCUMENT_PREFIX = 0x00;
    public static final byte DOCUMENT_FRAGMENT_PREFIX = 0x01;
    public static final byte FULL_DOCUMENT_REQUEST_PREFIX = 0x02;

    public static enum MessageType {
        FULL_DOCUMENT(FULL_DOCUMENT_PREFIX), DOCUMENT_FRAGMENT(DOCUMENT_FRAGMENT_PREFIX),
        FULL_DOCUMENT_REQUEST(FULL_DOCUMENT_REQUEST_PREFIX);

        private byte prefix;

        MessageType(byte prefix) {
            this.prefix = prefix;
        }

        public byte getPrefix() {
            return prefix;
        }
    }

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
