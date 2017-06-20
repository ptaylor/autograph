package com.googlecode.graphpipe;

import java.util.logging.Logger;

public class RegexpTextDataSplitter implements TextDataSplitter {

    private static Logger LOG = Logger.getLogger(RegexpTextDataSplitter.class.getName());

    private String regexp;
    public RegexpTextDataSplitter(String regexp) {
        this.regexp = regexp;
        
        LOG.info("regexp: '" + regexp + "'");        
    }
    
    public String[] split(String text) {
        return text.trim().split(regexp);
    }

}
