package com.googlecode.graphpipe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

public class TestBase extends Assert {

    public InputStream getInputStream(String s) {
        return new ByteArrayInputStream(s.getBytes()); 
    }
    
    public void assertArrayEquals(String[] exp, String[] a) throws Exception {
        assertEquals(exp.length, a.length);
        for (int i = 0; i < exp.length; i++) {
            assertEquals(exp[i], a[i]);
        }
    }
    
    @Test
    public void testBase() {
        // Keeps maven happy
    }

}
