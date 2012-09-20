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
import java.util.StringTokenizer;
import java.awt.event.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;



public class GraphMain
{

    public static void main(String args[]) 
    {
        parse_args(args);

        Graph g = new Graph(sm_show_min, 
                            sm_show_max,
                            sm_time,
                            sm_base);

        DataReader reader = new DataReader(g, System.in);

        reader.readData();
        
    }

    public static void error(String s)
    {
        System.err.println("error: " + s);
        System.exit(1);
    }

    public static void warning(String s)
    {
        System.err.println("warning: " + s);
    }
    
    public static boolean debug()
    {
        return sm_debug;
    }
     
 

    private static void parse_args(String[] args)
    {        
        try
        {
            for (int i = 0; i < args.length; i++)
            {
                if (args[i].equals("-debug"))
                {
                    sm_debug = true;
                }
                else if (args[i].equals("-help"))
                {
                    help();
                }
                else if (args[i].equals("-min"))
                {
                    sm_show_min = true;
                }
                else if (args[i].equals("-max"))
                {
                    sm_show_max = true;
                }
                else if (args[i].equals("-time"))
                {
                    if (i >= args.length - 1)
                    {
                        usage("-time needs argument");
                    }
                    i++;
                    sm_time = Integer.parseInt(args[i]);
                }
                else if (args[i].equals("-base"))
                {
                    if (i >= args.length - 1)
                    {
                        usage("-base needs argument");
                    }
                    i++;
                    sm_base = Double.parseDouble(args[i]);
                }
                else 
                {
                    usage("unknown argument `" + args[i] + "'");
                }
            }
        }
        catch (Exception e)
        {
            usage("exception " + e);
        }
    }
    
    private static void usage(String s)
    {
        if (s != null)
        {
            System.err.println("error: " + s);
        }
        System.err.println("usage: graph [-min] [-max] [-time <t>] [-base <b?] [-help]");
        System.exit(1);
    }

    private static void help()
    {
        System.err.println("\n"
+ "usage: graph [-min] [-max] [-time <t>] [-base <b>] [-help]\n"
+ "\n"
+ "Displays a graph of the numbers read from stdin.\n" 
+ "\n"
+ "     -min        -- Show minimum value. Not shown by default.\n"
+ "     -max        -- Show maximum value. Not shown by default.\n"
+ "     -time <t>   -- Sample data every <t> msecs.  If 0 only sample when\n"
+ "                    data is ready.  Default 0.\n"
+ "     -base <b>   -- Based used to scale the graph.  Default 10.\n"
+ "     -help       -- Show this help text.\n"
+ "\n"
                           );
        System.exit(0);
            }


    private static boolean sm_debug    = false;
    private static boolean sm_show_min = false;
    private static boolean sm_show_max = false;
    private static int     sm_time     = 0;
    private static double  sm_base     = 10;
    

}
