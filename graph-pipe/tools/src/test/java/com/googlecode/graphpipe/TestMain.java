package com.googlecode.graphpipe;

import org.easymock.classextension.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class TestMain extends Assert {

    private Graph graph;
    private GraphController graphController;
    private DataReaderRegistry registry;
    private DataReader dataReader;

    @Before
    public void setUp() throws Exception {
        Main.testing = true;
        registry = EasyMock.createMock(DataReaderRegistry.class);
        dataReader = EasyMock.createMock(DataReader.class);
        EasyMock.expect(registry.find("spaces")).andReturn(dataReader).anyTimes();
        EasyMock.expect(registry.find("test")).andReturn(dataReader).anyTimes();
        EasyMock.expect(registry.find("unknown")).andReturn(null).anyTimes();
        graph = EasyMock.createMock(Graph.class);
        graphController = EasyMock.createMock(GraphController.class);
    }
    
    @After
    public void tearDown() throws Exception {
        EasyMock.verify(registry, dataReader, graph, graphController);
    }

    @Test
    public void testNoArguments() throws Exception {

        dataReader.setInputStream(System.in);
        graphController.setDataReader(dataReader);
        EasyMock.replay(registry, dataReader, graph, graphController);      
        String[] args = {};
        Main.processArgs(args, registry, graphController, graph);
        assertEquals(0, Main.testingStatus);

    }

    @Test
    public void testSimpleArguments() throws Exception {

        dataReader.setInputStream(System.in);
        graphController.setDataReader(dataReader);
        graph.setBase(16);

        EasyMock.replay(registry, dataReader, graph, graphController);
        
        String[] args = {"--base", "16"};
        Main.processArgs(args, registry, graphController, graph);
        
        assertEquals(0, Main.testingStatus);
    }


    @Test
    public void testAllArguments() throws Exception {

        String[] COLORS = {"pink", "green", "orange"};
        dataReader.setInputStream(System.in);
        graph.setBase(2);
        graph.setShowMin("green");
        graph.setShowMax("red");
        graph.setDebug(true);
        dataReader.setDebug(true);
        graphController.setDebug(true);
        graphController.setDataReader(dataReader);
        graphController.setSample(24);
        graph.setTitle("Hello");
        graph.setColors(EasyMock.aryEq(COLORS));

        EasyMock.replay(registry, dataReader, graph, graphController);
        
        String[] args = {"--base", "2",
                         "--sample", "24",
                         "--title", "Hello",
                         "--format", "test",
                         "--min", "green",
                         "--max",
                         "--debug",
                         "--colors", "pink,green,orange"
                         };
        Main.processArgs(args, registry, graphController, graph);
       
        assertEquals(0, Main.testingStatus);
    }
    
    @Test
    public void testHelpArgument() throws Exception {
        EasyMock.replay(registry, dataReader, graph, graphController);
        
        String[] args = {"--help"};
        try {
            Main.processArgs(args, registry, graphController, graph);
            fail("expected exception not raised");
        } catch (RuntimeException e) {
            assertEquals("exiting with status 0", e.getMessage());
        }
        
        assertEquals(0, Main.testingStatus);
    }   
        
    @Test
    public void testBadArgument() throws Exception {
        EasyMock.replay(registry, dataReader, graph, graphController);
        
        String[] args = {"--funky"};
        try {
            Main.processArgs(args, registry, graphController, graph);
            fail("expected exception not raised");
        } catch (RuntimeException e) {
            assertEquals("exiting with status 1", e.getMessage());
        }
        
        assertEquals(1, Main.testingStatus);
    }   

    @Test
    public void testUnknownFormatArgument() throws Exception {
        EasyMock.expect(registry.getSupportedFormats()).andReturn("...");
        EasyMock.replay(registry, dataReader, graph, graphController);
        
        String[] args = {"--format", "unknown"};
        try {
            Main.processArgs(args, registry, graphController, graph);
            fail("expected exception not raised");
        } catch (RuntimeException e) {
            assertEquals("exiting with status 1", e.getMessage());
        }
        
        assertEquals(1, Main.testingStatus);
    }   

    @Test
    public void testBadArgumentValue() throws Exception {
        dataReader.setInputStream(System.in);
        graphController.setDataReader(dataReader);
        EasyMock.replay(registry, dataReader, graph, graphController);
        
        String[] args = {"--base", "funky"};
        try {
            Main.processArgs(args, registry, graphController, graph);
            fail("expected exception not raised");
        } catch (RuntimeException e) {
            assertEquals("exiting with status 1", e.getMessage());
        }
        
        assertEquals(1, Main.testingStatus);
    }   
}
