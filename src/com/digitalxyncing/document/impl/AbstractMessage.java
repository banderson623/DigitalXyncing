package com.digitalxyncing.document.impl;

import com.digitalxyncing.document.Message;

/**
 * Abstract implementation of {@link com.digitalxyncing.document.Message}.
 */
public abstract class AbstractMessage implements Message {

    protected byte[] data;
    protected byte prefix;

    public AbstractMessage(MessageType messageType) {
        prefix = messageType.getPrefix();
    }

    @Override
    public final byte[] getPrefixedByteArray() {
        if (data == null)
            return new byte[] { prefix };
        int dataLen = data.length;
        byte[] buffer = new byte[dataLen + 1];
        buffer[0] = prefix;
        System.arraycopy(data, 0, buffer, 1, dataLen);
        return buffer;
    }

    @Override
    public byte[] getData() {
        return data;
    }

}
