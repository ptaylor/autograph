package com.googlecode.graphpipe;

import java.util.logging.Logger;

public class DataBuffer {
    
    private static Logger LOG = Logger.getLogger(DataBuffer.class.getName());

    private int size;
    private int numElems;
    private DataEntry top;
    private DataEntry tail;

    
    public DataBuffer(int size) {
        LOG.info("size: " + size);
        this.size = size;
        numElems = 0;
        top = null;
        tail = null;
    }
    
    public void add(double[] d) {
        DataEntry data = new DataEntry(d, top);
        if (top != null) {
            top.setNext(data);
        } else {
            tail = data;
        }

        top = data;
        
        if (numElems == size) {
            tail = tail.getNext();
            if (tail != null) {
                tail.chop();
            }
        } else {
            numElems++;
        }
    }
    
    public DataBufferIterator getIterator() {
        return new DataBufferIterator(top);
    }
    
    public void resize(int newSize) {
        LOG.info("new size " + newSize);
        
        // Do not truncate when resized smaller since we want to
        // be able to resize to a larger size later and keep the 
        // old data.
        if (newSize >= size) {
            size = newSize;
        }
    }

}