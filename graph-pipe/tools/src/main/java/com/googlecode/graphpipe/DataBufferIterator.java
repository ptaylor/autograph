package com.googlecode.graphpipe;

import java.util.Iterator;

public class DataBufferIterator implements Iterator<Object> {

    private DataEntry current;

    public DataBufferIterator(DataEntry top) {
        current = top;
    }
    
    public boolean hasNext() {
        return current != null;
    }

    public Object next() {
        if (current == null) {
            return null;
        }
        
        Object o = current.getObject();
        current = current.getPrev();
        return o;
    }

    public void remove() {
        // Complete        
    }

}
