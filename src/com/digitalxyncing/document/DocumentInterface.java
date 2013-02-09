package com.digitalxyncing.document;

/**
 * User: brian_anderson
 * Date: 2/4/13
 * Just adding some ideas here. Please change as required.
 * Add some readme here about how this operates
 */
public interface DocumentInterface<TypeOfDocument extends Object> {
    public interface DocumentFragment<TypeOfDocument> {
        public String toString();
    }

    public boolean update(DocumentFragment<TypeOfDocument> newFragment);
    /**
     *
     * @return
     */
    public TypeOfDocument getFullState();


}
