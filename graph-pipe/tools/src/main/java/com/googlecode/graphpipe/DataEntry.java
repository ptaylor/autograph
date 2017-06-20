package com.googlecode.graphpipe;


public class DataEntry {
    
    private double[] data;
    private DataEntry next;
    private DataEntry prev;
    
    public DataEntry(double[] d, DataEntry prev) {
        data = d.clone();
        next = null;
        this.prev = prev;
    }
    
    public double[] getObject() {
        return data;
    }
    
    void setNext(DataEntry o) {
        next = o;
    }
    
    DataEntry getNext() {
        return next;
    }
    
    DataEntry getPrev() {
        return prev;
    }
    
    void chop() {
        prev = null;
    }
}
