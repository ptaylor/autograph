package com.googlecode.graphpipe;

import org.junit.Test;

import com.googlecode.graphpipe.RegexpTextDataSplitter;

public class TestRegexpTextDataSplitter extends TestBase {

    @Test
    public void testRegexpTextDataSplitter() throws Exception {
        
        doTest("A,B,C", ",", "A", "B", "C");
        doTest(" A,B,C", ",", "A", "B", "C");
        doTest(" A,B ,C ", ",", "A", "B ", "C");
        
        doTest("A B C,D", " ", "A", "B", "C,D");
        doTest("A  B  \tC", "\\s+", "A", "B", "C");
        doTest(" A  B  \tC\t ", "\\s+", "A", "B", "C");

    }

    private void doTest(String text, String regexp, String... expected) throws Exception {
        
        RegexpTextDataSplitter splitter = new RegexpTextDataSplitter(regexp);
        String[] result = splitter.split(text);
        
        assertArrayEquals(expected, result);
        
    }
}
