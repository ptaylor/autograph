package com.googlecode.graphpipe;

public class OldSwingGraph implements Graph {
    
    private static final String DEFAULT_TITLE = "Graph Pipe";
    
    private static final String[] DEFAULT_COLORS = {
        "red", "blue"
    };
    
    private static final int DEFAULT_BASE = 10;
    
    private String title = DEFAULT_TITLE;
    private String[] colors = DEFAULT_COLORS;
    private String minColor = null;
    private String maxColor = null;;
    private int base = DEFAULT_BASE;
    private boolean debug = false;

    private SwingGraphFrame frame;

    public void setTitle(String title) {
        this.title = title;        
    }

    public void setColors(String[] colors) {
        this.colors = colors;
        
    }

    public void setShowMin(String color) {
        minColor = color;
    }

    public void setShowMax(String color) {
        maxColor = color;        
    }

    public void setBase(int base) {
        this.base = base;
        
    }

    public void setDebug(boolean b) {
        debug = b;
        
    }
    

    public void init() {
        frame = new SwingGraphFrame(null);
    }
    
    public void addData(double[] data) {
        System.out.println("DOUBLE " + data[0]);
        
        System.out.println("DOUBLE " + data[0]);


    }
    public void setDataNames(String[] data) {
        System.out.println("NAME " + data[0]);
        
    }

    public void init(int size) {
        // TODO Auto-generated method stub
        
    }

    public int getGraphSize() {
        // TODO Auto-generated method stub
        return 0;
    }

    public void init(Handler h) {
        // TODO Auto-generated method stub
        
    }

    public void showData(DataBuffer data) {
        // TODO Auto-generated method stub
        
    }


    
}
