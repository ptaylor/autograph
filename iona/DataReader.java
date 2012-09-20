package org.pt;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Vector;
import java.util.StringTokenizer;


public class DataReader
{
    public DataReader(Graph g, InputStream is)
    {
        InputStreamReader stdin = new InputStreamReader(is);
        m_reader = new BufferedReader(stdin);
        m_graph  = g;
    }

    public void readData()
    {
        Vector data = new Vector(10);
        data.setSize(0);
        int data_size = -1;
        
        
        try
        {
            String line = m_reader.readLine();
            while (line != null)
            {
                data.setSize(0);
                
                boolean have_names = false;

                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens())
                {
                    String s = st.nextToken();
                    try
                    {
                        Double d = Double.valueOf(s);
                        data.add(d);
                    }
                    catch (NumberFormatException e)
                    {
                        int len = s.length();
                        char c = s.charAt(len -1);
                        String s2 = s.substring(0, len - 1);
                        
                        try
                        {
                            double d = Double.parseDouble(s2);
                            if (c == 'k' || c == 'K')
                            {
                                d = d * 1024.0;
                            }
                            else if (c == 'm' || c == 'M')
                            {
                                d = d * 1024.0 * 1024.0;
                            }
                            else if (c == 'g' || c == 'G')
                            {
                                d = d * 1024.0 * 1024.0 * 1024.0;
                            }
                            data.add(new Double(d));
                        }
                        catch (NumberFormatException e2)
                        {
                            if (data_size != -1)
                            {
                                GraphMain.warning("Name fields can only appear on the first line");
                            }
                            data.add(s);
                            have_names = true;
                        }
                    }                    
                }
                
                if (data.size() == 0)
                {
                    line = m_reader.readLine();
                    continue;
                }
                
                if (data_size == -1)
                {
                    data_size = data.size();
                }
                else
                {
                    if (data_size != data.size())
                    {
                        GraphMain.error("Inconsistent number of data fields");
                    }
                }
                
                if (have_names)
                {
                    m_graph.inputNames(data);
                }
                else 
                {
                    m_graph.inputData(data);
                }
                line = m_reader.readLine();
            }
        }
        catch (IOException e)
        {
            GraphMain.error("exception reading data: " + e);
        }
    }
    
    private BufferedReader m_reader;
    private Graph          m_graph;

}
        
