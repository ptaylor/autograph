package org.pt;


import java.awt.Frame;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Dimension;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.lang.Math;
import java.util.Random;
import java.util.Vector;
import java.util.Iterator;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.FieldPosition;

public class Graph extends Frame 
{    
    public Graph(boolean show_min, 
                 boolean show_max,
                 int time, 
                 double base)
    {
        super("Graph");

        m_data_min  = Double.MAX_VALUE;
        m_data_max  = Double.MIN_VALUE;
        m_graph_min = Double.MAX_VALUE;
        m_graph_max = Double.MIN_VALUE;

        m_show_min = show_min;
        m_show_max = show_max;

        //m_exp       = 1;
        m_inc       = 1;
        m_base      = base;

        m_time      = time;
        m_samples   = null;

        m_format    = new DecimalFormat();

        addNotify(); // Needed for createImage()

        resize_graph(new Dimension(sm_default_width, sm_default_height));

        setSize(m_dim);
        setVisible(true);

        if (m_time > 0)
        {
            SamplerThread th = new SamplerThread(this, m_time);
            th.start();
        }
        
        
        addWindowListener(new WindowAdapter()
                              {
                                  public void windowClosing(WindowEvent e)
                                  {
                                      dispose(); 
                                      System.exit(0);
                                  }
                              });
    }


    public synchronized Vector getSamples()
    {
        if (m_samples == null)
        {
            return null;
        }

        return (Vector) m_samples.clone();
    }

    public synchronized void setSamples(Vector v)
    {
        m_samples = (Vector) v.clone();
    }
    
    public void inputNames(Vector v)
    {
        if (m_names != null)
        {
            return;
        }

        m_names = new String[v.size()];

        for (int i = 0; i < v.size(); i++)
        {
            m_names[i] = (String) v.get(i);
        }
    }

    public void inputData(Vector v)
    {
        if (m_time == 0)
        {
            showData(v);
        }
        else
        {
            setSamples(v);
        }
    }

    public void showData(Vector v)
    {
        if (v == null)
        {
            return;
        }

        for (int i = 0; i < v.size(); i++)
        {
            Double d = (Double) v.get(i);
            double dd = d.doubleValue();
            if (dd < m_data_min || dd > m_data_max)
            {
                rescale(dd);
            }
        }

        m_buffer.add(v.clone());

        repaint();
    }
    
    

    public void paint(Graphics g)
    {
        Graphics g2 = m_image.getGraphics();
        
        draw_background(g2);
        draw_title(g2);
        draw_scale(g2);
        draw_data(g2);
        draw_legend(g2);

        g.drawImage(m_image, 0, 0, this);
    }
    

    public void update(Graphics g)
    {
        Dimension d = getSize();
        int w = d.width;
        int h = d.height;

        if (! m_dim.equals(d))
        {
            resize_graph(d);
        }
        paint(g);
    }


    public void resize_graph(Dimension d)
    {
        m_dim = d;
        m_graph_dim = new Dimension(m_dim.width -
                                    (sm_left_margin +
                                     sm_right_margin +
                                     sm_legend_width),
                                    m_dim.height - 
                                    (sm_top_margin +
                                     sm_bot_margin +
                                     sm_title_height));

        if (m_graph_dim.width < 1 ||
            m_graph_dim.height < 1)
        {
            GraphMain.warning("Too small: " + m_graph_dim);
        }

        
        m_image = createImage(m_dim.width, m_dim.height);
        if (m_buffer == null)
        {
            m_buffer = new DataBuffer(m_graph_dim.width);
        }
        else
        {
            m_buffer.resize(m_graph_dim.width);
        }
    }

    private void draw_background(Graphics g)
    {
        g.setColor(sm_color_bg);
        g.fillRect(0, 0, m_dim.width, m_dim.height);
    }

    private void draw_title(Graphics g)
    {
        g.setColor(sm_color_title);
        g.drawString("G1", 10, 10);
        g.drawString("Graph", m_dim.width / 2 - 10, sm_top_margin);
    }

    private void draw_scale(Graphics g)
    {
        
        g.setColor(sm_color_graph_bg);
        g.fillRect(sm_left_margin,
                   sm_top_margin + sm_title_height, 
                   m_graph_dim.width,
                   m_graph_dim.height);


        FieldPosition f = new FieldPosition(0);
        
        for (double d = 0.0; d < m_graph_max; d = d + m_inc)
        {
            StringBuffer s = new StringBuffer();
            m_format.format(d, s, f);

            g.setColor(sm_color_grid_line);
            plot_horizontal_line(g, 0, m_graph_dim.width - 1, d);

            g.setColor(sm_color_grid_text);
            plot_text(g, s.toString(), m_graph_dim.width + 5, d - m_inc * 0.1);
        }

        g.setColor(sm_color_minmax);
        if (m_show_max)
        {
            plot_dashed_line(g, 0, m_graph_dim.width - 1, m_data_max);
        }

        if (m_show_min)
        {
            plot_dashed_line(g, 0, m_graph_dim.width - 1, m_data_min);
        }

    }

    private void draw_data(Graphics g)
    {
        double[] last_values = null;
        int  x = m_graph_dim.width;

        Iterator it = m_buffer.getIterator();
        while (x >= 0 && it.hasNext())
        {
            Vector v = (Vector) it.next();

            if (last_values == null)
            {
                last_values = new double[v.size()];
                for (int i = 0; i < v.size(); i++)
                {
                    Double d = (Double) v.get(i);
                    last_values[i] = d.doubleValue();
                }
            }
            else
            {
                for (int i = 0; i < v.size(); i++)
                {
                    Double d = (Double) v.get(i);
                    double dd = d.doubleValue();
                    g.setColor(sm_colors[i]);
                    plot_line(g, x, dd, x - 1, last_values[i]);
                    last_values[i] = dd;
                }
            }
            x--;
        }
    }
    
    private void draw_legend(Graphics g)
    {
        if (m_names == null)
        {
            return;
        }

        for (int i = 0; i < m_names.length; i++)
        {
            int y = sm_bot_margin + m_graph_dim.height - 30 - i * 25;
            y = m_dim.height - y;
            int x = sm_left_margin + m_graph_dim.width + 50;
            g.setColor(sm_colors[i]);

            g.drawLine(x, y - 5, x + 20, y - 5);
            g.setColor(Color.black);
            g.drawString(m_names[i], x + 30, y);
        }
    }
                
    
    
    private void plot_line(Graphics g, int x1, double y1, int x2, double y2)
    {
        int y1_pos = y_pos(y1);
        int y2_pos = y_pos(y2);
        
        g.drawLine(sm_left_margin + x1,
                   m_dim.height - (sm_bot_margin + y1_pos),
                   sm_left_margin + x2,
                   m_dim.height - (sm_bot_margin + y2_pos));
    }
    
    private void plot_horizontal_line(Graphics g, int x1, int x2, double y)
    {
        plot_line(g, x1, y, x2, y);
    }

    private void plot_dashed_line(Graphics g, int x1, int x2, double y)
    {
        int d1 = 2;
        int d2 = 3;
        int d3 = d1 + d2;

        for (int i = x1; i < x2; i += d3)
        {
            plot_horizontal_line(g, i, i + d1, y);
        }
    }

    private void plot_text(Graphics g, String s, int x, double y)
    {
        int y_pos = y_pos(y);
        g.drawString(s,
                     sm_left_margin + x,
                     m_dim.height - (sm_bot_margin + y_pos - 1));
    }


    private int y_pos(double v)
    {
        double scale = ((double) m_graph_dim.height) /
                        (m_graph_max - m_graph_min);

        int y = (int) (v * scale - m_graph_min * scale);
        return y;
    }

   private void rescale(double v)
    {
        if (v < m_data_min)
        {
            m_data_min = v;
            if (m_data_min < 0.0)
            {
                GraphMain.warning("negative numbers not allowed");
                return;
            }
        }
        
        if (v > m_data_max)
        {
            m_data_max = v;
        }

        double exp = Math.ceil(log(m_data_max, m_base));
        m_inc       = Math.pow(m_base, exp - 1);
        m_graph_max = Math.pow(m_base, exp) + m_inc;
        m_graph_min = 0.0;

        int fr = 1 - (int) exp;
        if (fr < 0) fr = 0;
        m_format.setMaximumFractionDigits(fr);
        m_format.setMinimumFractionDigits(fr);

        if (GraphMain.debug())
        {
            System.out.println("* rescale: " +
                               "min=" + d2s2(m_data_min) +
                               ", max=" + d2s2(m_data_max) +
                               ", exp=" + d2s2(exp) +
                               ", m_max=" + d2s2(m_graph_max) + 
                               ", m_min=" + d2s2(m_graph_min) + 
                               ", m_inc=" + d2s2(m_inc) +
                               "");
        }
        
    }


    private String d2s2(double v)
    {
        
        DecimalFormat format = new DecimalFormat("#,##0.00");
        FieldPosition f = new FieldPosition(0);
        StringBuffer s = new StringBuffer();
        format.format(v, s, f);
        return s.toString();
    }

    private double log(double x, double b)
    {
        return Math.log(x) / Math.log(b);
    }
    


    private double m_data_min;
    private double m_data_max;
    private double m_graph_max;
    private double m_graph_min;

    private double m_inc;
    private double m_base;
    
    


    
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

}
