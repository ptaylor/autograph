
package org.pt;

import java.util.Vector;

public class SamplerThread extends Thread
{
    
    public SamplerThread(Graph g, int period)
    {
        m_graph  = g;
        m_period = period;
    }


    public void run()
    {
        while (true)
        {
            m_graph.showData(m_graph.getSamples());
            
            sleep(m_period);
        }
    }

    private void sleep(int msecs)
    {
        try
        {
            java.lang.Thread.sleep(msecs);
        }
        catch (Exception e)
        {
        }
    }
    
    private Graph m_graph;
    private int   m_period;
}

        
