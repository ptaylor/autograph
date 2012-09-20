package com.googlecode.graphpipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TextDataReader implements DataReader {

    private static Logger LOG = Logger.getLogger(TextDataReader.class.getName());
    private TextDataSplitter dataSplitter;
    private BufferedReader reader;
    private boolean debug = false;

    public TextDataReader(TextDataSplitter splitter) {     
        dataSplitter = splitter;
    }
    
    public void setInputStream(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is));   
    }
    
    public String[] next() throws IOException {
        String line = reader.readLine();
        
        if (line == null) {
            LOG.info("end of stream");
            return null;
        }
        
        LOG.info("input line (" + line + ")");
        String[] result = dataSplitter.split(line);
                
        if (LOG.isLoggable(Level.INFO)) {
            logResult(result);
        }
        return result;
    }

    private void logResult(String[] result) {
        StringBuilder sb = new StringBuilder()
            .append("data line: ");
        boolean first = true;
        for (String s : result) {
            if (first) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append("(").append(s).append(")");
        }
        LOG.info(sb.toString());
                   
    }

    public void setDebug(boolean b) {
        debug = b;
        
    }

}
