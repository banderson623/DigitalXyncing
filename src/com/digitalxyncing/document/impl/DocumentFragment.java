package com.digitalxyncing.document.impl;

/**
 * Implementation of {@link AbstractMessage} representing a partial fragment of a {@link Document}.
 */
public abstract class DocumentFragment<T> extends AbstractMessage {

    public DocumentFragment() {
        super(DOCUMENT_FRAGMENT);
    }

    @Override
    public MessageType getType() {
        return MessageType.DOCUMENT_FRAGMENT;
    }

}