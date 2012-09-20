package com.googlecode.graphpipe.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.text.FieldPosition;

import javax.swing.JPanel;
import javax.swing.border.Border;

public class GraphPanel extends JPanel {


    private static final long serialVersionUID = -405536504198324708L;
    private SwingGraph graph;

    public GraphPanel(SwingGraph graph) {
        this.graph = graph;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //System.out.println("GraphPanel repaint component");
        
        Graphics2D g2d = (Graphics2D) g;
        

        Border b = this.getBorder();
        
        Dimension size = getSize();
        g.setColor(Color.GREEN);
        g.drawLine(0, 0, size.width, size.height);
        
        drawScale(g2d);
    }
    
    private void drawScale(Graphics2D g)
    {
        
/*
        g.setColor(sm_color_graph_bg);
        g.fillRect(sm_left_margin,
                   sm_top_margin + sm_title_height,
                   m_graph_dim.width,
                   m_graph_dim.height);

*/
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

}
