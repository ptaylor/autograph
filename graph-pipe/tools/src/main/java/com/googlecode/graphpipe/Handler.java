package com.googlecode.graphpipe;

public interface Handler {

    void resized();
    
    void closed();
        
    void error(String msg);
    
}
