package com.digitalxyncing.document.impl;

import com.digitalxyncing.document.Message;

/**
 * Abstract implementation of {@link com.digitalxyncing.document.Message}.
 */
public abstract class AbstractMessage<T> implements Message<T> {

    public static final byte FULL_DOCUMENT = 0x00;
    public static final byte DOCUMENT_FRAGMENT = 0x01;

    protected byte[] data;
    protected byte prefix;

    public AbstractMessage(byte prefix) {
        this.prefix = prefix;
    }

    @Override
    public final byte[] getPrefixedByteArray() {
        int dataLen = data.length;
        byte[] buffer = new byte[dataLen + 1];
        buffer[0] = prefix;
        System.arraycopy(data, 0, buffer, 1, dataLen);
        return buffer;
    }

}
