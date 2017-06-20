package com.googlecode.graphpipe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DecimalFormat;
import java.util.Vector;

public class SwingGraphFrame extends Frame {

    private static final long serialVersionUID = -7887319889139931802L;
    private double m_data_min;
    private double m_data_max;
    private double m_graph_min;
    private double m_graph_max;
    private int m_inc;
    private static int sm_left_margin    = 40;
    private static int sm_right_margin   = 80;
    private static int sm_top_margin     = 40;
    private static int sm_bot_margin     = 40;
    private static int sm_title_height   = 40;
    private static int sm_legend_width   = 60;
    private static int sm_default_width  = 600;
    private static int sm_default_height = 400;

    private Dimension  m_dim;
    private Dimension  m_graph_dim;

    private int        m_time;
    private DataBuffer m_buffer;
    private Vector     m_samples;
    private String     m_names[];
    //private Color      m_colors[];

    private boolean    m_show_min;
    private boolean    m_show_max;

    private static final Color  sm_color_graph_bg  = Color.black;
    private static final Color  sm_color_title     = Color.black;
    private static final Color  sm_color_grid_line = Color.green;
    private static final Color  sm_color_grid_text = Color.black;
    private static final Color  sm_color_minmax    = Color.red;
    private static final Color  sm_color_data_line = Color.yellow;
    private static final Color  sm_color_bg        = Color.lightGray;

    private static final int    sm_sleep_time  = 100;
    private Image   m_image;

    private DecimalFormat m_format;

    private static Color[] sm_colors = {Color.yellow, 
                                        Color.orange,
                                        Color.magenta,
                                        Color.pink,
                                        Color.cyan,
                                        Color.gray};

    

    public SwingGraphFrame(Graph graph) {
        
        m_data_min  = Double.MAX_VALUE;
        m_data_max  = Double.MIN_VALUE;
        m_graph_min = Double.MAX_VALUE;
        m_graph_max = Double.MIN_VALUE;


        m_inc       = 1;

        //m_time      = time;
        //m_samples   = null;

        //m_format    = new DecimalFormat();

        addNotify(); // Needed for createImage()


        setSize(m_dim);
        setVisible(true);

        
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.out.println("***** EXITING *** ");
                System.exit(0);
            }
        });
    }
}

