package com.googlecode.graphpipe;


import java.io.InputStream;
import org.junit.Test;

import com.googlecode.graphpipe.DataReader;
import com.googlecode.graphpipe.RegexpTextDataSplitter;
import com.googlecode.graphpipe.TextDataReader;

public class TestTextDataReader extends TestBase {
    
    private static final String TEST1 = "A,B,C\n" 
                                       + " D,E,F\n"
                                       + "G,H,I \n";
    private static final String[] TEST1_LINE1 = {"A", "B", "C"};
    private static final String[] TEST1_LINE2 = {"D", "E", "F"};
    private static final String[] TEST1_LINE3 = {"G", "H", "I"};

    @Test
    public void testTextDataReader() throws Exception {
        
        RegexpTextDataSplitter splitter = new RegexpTextDataSplitter(",");
        InputStream is = getInputStream(TEST1);
        DataReader reader = new TextDataReader(splitter);        
        reader.setInputStream(is);
                
        assertArrayEquals(TEST1_LINE1, reader.next());
        assertArrayEquals(TEST1_LINE2, reader.next());
        assertArrayEquals(TEST1_LINE3, reader.next());
        assertNull(reader.next());
        
    }

}
