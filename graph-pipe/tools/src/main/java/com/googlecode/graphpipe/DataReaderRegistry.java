package com.googlecode.graphpipe;

import java.util.HashMap;
import java.util.Map;

public class DataReaderRegistry {

    private Map<String, DataReader> dataReaders = new HashMap<String, DataReader>();
    
    public DataReader find(String name) {
        return dataReaders.get(name);
    }
    
    public void add(DataReader dr, String name) {
        dataReaders.put(name, dr);
    }

    public String getSupportedFormats() {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (String s : dataReaders.keySet()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(", ");
            }
            sb.append(s);
        }
        return sb.toString();
    }
}
