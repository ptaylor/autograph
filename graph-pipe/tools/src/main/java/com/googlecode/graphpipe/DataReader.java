package com.googlecode.graphpipe;

import java.io.IOException;
import java.io.InputStream;

public interface DataReader {
    
    void setInputStream(InputStream is);

    String[] next() throws IOException;

    void setDebug(boolean b); 
}
