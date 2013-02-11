package com.digitalxyncing.document.impl;

import java.io.Serializable;

/**
 * Implementation of {@link AbstractMessage} representing a complete {@link Document}.
 */
public abstract class Document<T extends Serializable> extends AbstractMessage {

    public Document(byte[] documentData) {
        super(FULL_DOCUMENT);
        data = documentData;
    }

    public Document(T document) {
        super(FULL_DOCUMENT);
        data = serialize(document);
    }

    /**
     * Serializes the object into a byte array.
     *
     * @return byte array
     */
    protected abstract byte[] serialize(T object);

    /**
     * Updates the {@code Document} with the given {@link DocumentFragment}.
     *
     * @param newFragment the {@code DocumentFragment} to update the {@code Document} with
     * @return {@code true} if the update succeeded, {@code false} if not
     */
    public abstract boolean update(DocumentFragment<T> newFragment);

    /**
     * Returns the complete document object.
     *
     * @return full document
     */
    public abstract T getFullState();

    @Override
     public MessageType getType() {
        return MessageType.FULL_DOCUMENT;
    }

}
