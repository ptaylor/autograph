package com.googlecode.graphpipe.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import com.googlecode.graphpipe.DataBuffer;
import com.googlecode.graphpipe.Graph;
import com.googlecode.graphpipe.GraphController;
import com.googlecode.graphpipe.Handler;

public class SwingGraph implements Graph {

    private static final long serialVersionUID = -4925920040985045625L;

    private static Logger LOG = Logger.getLogger(SwingGraph.class.getName());

    private Handler handler;

    private String title;

    private GraphPanel graph;

    private String[] names;

    private Color[] colors;
    
    private static final int GRAPH_INITIAL_WIDTH = 700;
    private static final int GRAPH_INITIAL_HEIGHT = 300;
    private static final int LEGEND_INITIAL_WIDTH = 120;
    private static final int LEGEND_INITIAL_HEIGHT = GRAPH_INITIAL_HEIGHT;

    private static final String LEGEND_TITLE = "Legend";

    private static final String GRAPH_TITLE = "GraphPipe";

    private static final String PROPERTIES_NAME = "graph.properties";

    private static final String[] DEFAULT_COLORS = {
        "0x009966",
        "0xFF6633",
        "0x6699FF",
        "0x996699",
        "0x003366",
        "0x003300",        
        "0x330099",
        "0x990066"
        };


    public SwingGraph() {
        

        setColors(DEFAULT_COLORS);
    }
    
    public void init(Handler h) {
        handler = h;
        
     // TODO: this is needed to get resizing working
        JFrame.setDefaultLookAndFeelDecorated(true);
        //System.setProperty("sun.awt.noerasebackground", "false");
        
        final JFrame frame = new JFrame();
 
        Properties props = new Properties();
        URL url = null;
        try {
            url = getClass().getResource(PROPERTIES_NAME);
            LOG.info("reading properties from " + url);
            props.load(url.openStream());
        } catch (Exception e) {
            h.error("cannot read properties " + PROPERTIES_NAME
                  + " from " + url + ", exception " + e);
        }
                

        frame.setTitle(GRAPH_TITLE + ": " + title);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Color lineColor = new Color(0x00CCCC);
        Color bgColor = Color.BLACK;
        
        Font textFont = null; // TODO allow this to be set.
        
        Dimension graphSize = new Dimension(GRAPH_INITIAL_WIDTH, GRAPH_INITIAL_HEIGHT);        
        graph = new GraphPanel(this);
        graph.setBackground(bgColor);
        graph.setPreferredSize(graphSize);
        
        final JPanel graphBorder = new JPanel();
        graphBorder.setBackground(bgColor);
        graphBorder.setBorder(BorderFactory.createTitledBorder(
                                          new LineBorder(lineColor),
                                          title,
                                          TitledBorder.CENTER,
                                          TitledBorder.TOP,
                                          textFont,
                                          lineColor));

        graphBorder.add(graph);
        
        Dimension legendSize = new Dimension(LEGEND_INITIAL_WIDTH, LEGEND_INITIAL_HEIGHT);
        final JPanel legend = new LegendPanel(this);
        legend.setBackground(bgColor);
        legend.setPreferredSize(legendSize);
        
        final JPanel legendBorder = new JPanel();
        legendBorder.setBackground(bgColor);
        legendBorder.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(lineColor),
                LEGEND_TITLE,
                TitledBorder.CENTER,
                TitledBorder.TOP,
                textFont,
                lineColor));
        
        legendBorder.add(legend);
        
        //frame.add(title, BorderLayout.NORTH );
        
        frame.add(graphBorder, BorderLayout.WEST);
        
        frame.add(legendBorder, BorderLayout.EAST);

        frame.pack();
        
        final int FRAME_INITIAL_HEIGHT = frame.getHeight();
        final int FRAME_INITIAL_WIDTH = frame.getWidth();
        System.err.println("INITIAL " + FRAME_INITIAL_WIDTH + " x " + FRAME_INITIAL_HEIGHT);
        
        frame.getContentPane().addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                
                
                int dx = (frame.getWidth() - FRAME_INITIAL_WIDTH) / 2; 
                int dy = frame.getHeight() - FRAME_INITIAL_HEIGHT;
                
                System.out.println("RESIZE " + dx + ", " + dy);
                Dimension dim = new Dimension(GRAPH_INITIAL_WIDTH + dx,
                        GRAPH_INITIAL_HEIGHT + dy);
                graph.setPreferredSize(dim);
                graph.setSize(dim);
                Dimension dim2 = new Dimension(LEGEND_INITIAL_WIDTH + dx,
                        LEGEND_INITIAL_HEIGHT + dy);
                legend.setPreferredSize(dim2);
                legend.setSize(dim2);
                frame.repaint();
                
                handler.resized();
                
            }
        });

        frame.setVisible(true);

    }

    public void setBase(int base) {
        // TODO Auto-generated method stub

    }

    public void setColors(String[] colorNames) {
        colors = new Color[colorNames.length];
        for (int i = 0; i < colorNames.length; i++) {
            colors[i] = Color.decode(colorNames[i]);
            
            LOG.info("color " + i + ": " + colors[i]);
        }
    }
    
    public Color[] getColors() {
        return colors;
    }

    public void setDataNames(String[] names) {
        this.names = names;
    }
    
    public String[] getDataNames() {
        return names;
    }
   

    public void setDebug(boolean b) {
        // TODO Auto-generated method stub

    }

    public void setShowMax(String color) {
        // TODO Auto-generated method stub

    }

    public void setShowMin(String color) {
        // TODO Auto-generated method stub

    }

    public void showData(DataBuffer data) {
        // TODO Auto-generated method stub

    }

    public int getGraphSize() {
        return graph.getWidth();       
    }

    public void setTitle(String t) {
        title = t;        
    }



}
