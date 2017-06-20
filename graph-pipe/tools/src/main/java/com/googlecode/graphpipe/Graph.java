package com.googlecode.graphpipe;

public interface Graph {

    void setTitle(String title);

    void setColors(String[] colors);

    void setShowMin(String color);

    void setShowMax(String color);
     
    void setBase(int base);

    void setDebug(boolean b);
    
    void init(Handler h);

    void setDataNames(String[] names);

    void showData(DataBuffer data);
    
    int getGraphSize();

}
