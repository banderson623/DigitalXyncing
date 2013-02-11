package com.digitalxyncing.document.impl;

import java.io.Serializable;

/**
 * Implementation of {@link AbstractMessage} representing a partial fragment of a {@link Document}.
 */
public abstract class DocumentFragment<T extends Serializable> extends AbstractMessage {

    public DocumentFragment(byte[] fragmentData) {
        super(DOCUMENT_FRAGMENT);
        data = fragmentData;
    }

    @Override
    public MessageType getType() {
        return MessageType.DOCUMENT_FRAGMENT;
    }

}