package com.googlecode.graphpipe;

import org.junit.Assert;
import org.junit.Test;

import com.googlecode.graphpipe.DataBuffer;
import com.googlecode.graphpipe.DataBufferIterator;

public class TestDataBuffer extends Assert {
    
    public static final double[] D1 = {1.0};
    public static final double[] D2 = {2.0, 1.5};
    public static final double[] D3 = {3.0, 1.0, 4.0};
    public static final double[] D4 = {4.0, 2.0};
    public static final double[] D5 = {5.0};
    public static final double[] D6 = {6.0, 7.0, 8.0};
    private static final double DELTA = 0.001;
    
    @Test
    public void testEmptyDataBuffer() throws Exception {
        
        DataBuffer buffer = new DataBuffer(0);        
        assertBufferEmpty(buffer);
        
        buffer.add(D1);
        // TODO fix this
        //assertBufferEmpty(buffer);
    }
    
    @Test
    public void testOneBuffer() throws Exception {
        DataBuffer buffer = new DataBuffer(1);
        assertBufferEmpty(buffer);
        
        buffer.add(D1);
        assertBufferContains(buffer, D1);
        
        buffer.add(D2);
        assertBufferContains(buffer, D2);

        buffer.add(D3);
        assertBufferContains(buffer, D3);
    }
    
    @Test
    public void testSmallDataBuffer() throws Exception {
        
        DataBuffer buffer = new DataBuffer(3);
        assertBufferEmpty(buffer);
        
        buffer.add(D1);
        assertBufferContains(buffer, D1);

        buffer.add(D2);
        assertBufferContains(buffer, D2, D1);

        buffer.add(D3);
        assertBufferContains(buffer, D3, D2, D1);
        
        buffer.add(D4);
        assertBufferContains(buffer, D4, D3, D2);
        
        buffer.add(D5);
        assertBufferContains(buffer, D5, D4, D3);
    }

    @Test
    public void testResizedDataBuffer() throws Exception {

        DataBuffer buffer = new DataBuffer(3);
        assertBufferEmpty(buffer);
        
        buffer.add(D1);
        buffer.add(D2);
        buffer.add(D3);
        assertBufferContains(buffer, D3, D2, D1);
        
        buffer.resize(4);
        assertBufferContains(buffer, D3, D2, D1);
        buffer.add(D4);
        assertBufferContains(buffer, D4, D3, D2, D1);
        
        buffer.add(D5);
        assertBufferContains(buffer, D5, D4, D3, D2);
        
        // Resizing does not remove data
        buffer.resize(2);
        assertBufferContains(buffer, D5, D4, D3, D2);
        
        buffer.add(D6);
        assertBufferContains(buffer, D6, D5, D4, D3);
    
    }
    
    private void assertBufferEmpty(DataBuffer buffer) {
        DataBufferIterator it = buffer.getIterator();
        assertNotNull(it);
        assertFalse(it.hasNext());
        assertNull(it.next());
    }
    
    private void assertBufferContains(DataBuffer buffer, double[]... expected) {
        
        DataBufferIterator it = buffer.getIterator();
        assertTrue(it.hasNext());
        
        int i = 0;
        while (it.hasNext()) {
            double[] expectedData = expected[i];
            double[] data = (double[]) it.next();
            assertNotNull(data);
            assertTrue(i < expected.length);
            assertEquals(expectedData.length, data.length);
            for (int j = 0; j < expectedData.length; j++) {
                assertEquals(expectedData[j], data[j], DELTA);
            }
            i++;
        }
        assertFalse(it.hasNext());
        assertNull(it.next());       
    }

}
