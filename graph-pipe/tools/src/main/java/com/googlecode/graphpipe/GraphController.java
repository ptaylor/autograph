package com.googlecode.graphpipe;

import java.io.IOException;
import java.util.logging.Logger;

public class GraphController implements Handler {

    private static final int SAMPLE_NONE = -1;

    private static final int DEFAULT_GRAPH_SIZE = 100;

    private static Logger LOG = Logger.getLogger(GraphController.class.getName());

    private Graph graph;
    private DataReader dataReader;
    private int sample = SAMPLE_NONE;
    private boolean debug = false;

    private DataBuffer buffer;


    public GraphController(Graph g) {
        graph = g;
    }

    public void setDataReader(DataReader dr) {
        dataReader = dr;
    }
    
    public void setDebug(boolean b) {
        debug = b;
    }


    public void run() {
        
        graph.init(this);
        String[] N = {"one", "two", "three", "four", "five", "six", "seven"};
        double[] D = {1, 2, 3, 4, 5, 6, 7};
        buffer = new DataBuffer(graph.getGraphSize());
        graph.setDataNames(N);
        buffer.add(D);
        graph.showData(buffer);
        try {
            if (sample != SAMPLE_NONE) {
                LOG.severe("--sample option not yet supported");
                System.exit(1);
            }
            String[] data = dataReader.next();
            while (data != null) {
                double[] numData = toDoubles(data);
                if (numData == null) {
                    graph.setDataNames(data);
                } else {
                    buffer.add(numData);
                    //graph.showData(numData);
                }
                data = dataReader.next();
            }
        } catch (IOException e) {
            System.err.println("error: exception " + e);
            e.printStackTrace(System.err);
        }
    }
    
    public void setSample(int s) {
        sample = s;
        
    }

    public void resized() {
        LOG.info("resized to " + graph.getGraphSize());
        
    }
    public void closed() {
        LOG.info("graph closed");
        
    }

    public void error(String msg) {
        LOG.severe("error: " + msg);        
    }
    


    private double[] toDoubles(String[] data) {
        double[] numData = new double[data.length];
        for (int i = 0; i < data.length; i++) {
            try {
                numData[i] = Double.parseDouble(data[i]);
            } catch (NumberFormatException e) {
                LOG.info("cannot convert data '" + data[i] + "' to a double");
                return null;
            }
        }
        return numData;
    }

    private void graphData(String[] data) {
        System.out.println("data " + data);
        
    }






}
