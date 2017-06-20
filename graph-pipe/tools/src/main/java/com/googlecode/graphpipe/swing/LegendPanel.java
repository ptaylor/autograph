package com.googlecode.graphpipe.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.border.Border;

import com.googlecode.graphpipe.Graph;

public class LegendPanel extends JPanel {

    private SwingGraph graph;

    public LegendPanel(SwingGraph g) {
        graph = g;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        /*
        g.setColor(Color.ORANGE);
        g.drawLine(0, 0, size.width, size.height);
        */
        
        String[] names = graph.getDataNames();
        if (names == null) {
            return;
        }
        
        Color[] colors = graph.getColors();
        if (colors == null) {
            return;
        }
        
        Graphics2D g2d = (Graphics2D) g;
        
        Dimension size = getSize();
        
        for (int i = 0; i < names.length; i++) {
            int y = size.height - 30 - i * 25;
            int x = 5;
            g2d.setColor(colors[i % colors.length]);
            g2d.drawLine(0, y - 5, x + 20, y - 5);
            g2d.drawString(names[i], x + 30, y);
        }
                        
    }
}
