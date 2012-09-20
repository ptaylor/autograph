package com.googlecode.graphpipe.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ContainerAdapter;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


public class Swing {

    private static final int GRAPH_INITIAL_WIDTH = 700;
    private static final int GRAPH_INITIAL_HEIGHT = 300;
    private static final int LEGEND_INITIAL_WIDTH = 120;
    private static final int LEGEND_INITIAL_HEIGHT = GRAPH_INITIAL_HEIGHT;

    public static void main(String[] args) {
        



        // TODO: this is needed to get resizing working
        JFrame.setDefaultLookAndFeelDecorated(true);
        //System.setProperty("sun.awt.noerasebackground", "false");
        
        final JFrame frame = new JFrame();
 
        Properties props = new Properties();
        try {
            System.out.println("FILE : " + Swing.class.getResource("graph.properties"));
            props.load(Swing.class.getResourceAsStream("graph.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
                

        frame.setTitle("This is a Swing Test");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel title = new JLabel();
        title.setText("Swing Text");
        
        //Color lineColor = Color.CYAN;
        Color lineColor = new Color(0x00CCCC);
        Color bgColor = Color.BLACK;
        
        Font textFont = null; // TODO allow this to be set.
        
        Dimension graphSize = new Dimension(GRAPH_INITIAL_WIDTH, GRAPH_INITIAL_HEIGHT);        
        final JPanel graph = new GraphPanel();
        graph.setBackground(bgColor);
        graph.setPreferredSize(graphSize);
        
        final JPanel graphBorder = new JPanel();
        graphBorder.setBackground(bgColor);
        graphBorder.setBorder(BorderFactory.createTitledBorder(
                                          new LineBorder(lineColor),
                                          "Sample Data",
                                          TitledBorder.CENTER,
                                          TitledBorder.TOP,
                                          textFont,
                                          lineColor));

        graphBorder.add(graph);
        
        Dimension legendSize = new Dimension(LEGEND_INITIAL_WIDTH, LEGEND_INITIAL_HEIGHT);
        final JPanel legend = new LegendPanel(null);
        legend.setBackground(bgColor);
        legend.setPreferredSize(legendSize);
        
        final JPanel legendBorder = new JPanel();
        legendBorder.setBackground(bgColor);
        legendBorder.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(lineColor),
                "Legend",
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
                
            }
        });

        frame.setVisible(true);

    }
}
