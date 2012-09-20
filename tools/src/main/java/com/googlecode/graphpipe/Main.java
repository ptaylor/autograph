package com.googlecode.graphpipe;

import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PatternOptionBuilder;
import org.apache.commons.cli.PosixParser;

import com.googlecode.graphpipe.swing.SwingGraph;

public class Main {


    private static final String DEFAULT_FORMAT = "spaces";
    private static final String FORMAT_OPTION = "format";
    private static final String FORMAT_SHORT_OPTION = "f";
    private static final String TITLE_OPTION = "title";
    private static final String TITLE_SHORT_OPTION = "t";
    private static final String MIN_OPTION = "min";
    private static final String MAX_OPTION = "max";
    private static final String DEFAULT_MIN_COLOR = "red";    
    private static final String DEFAULT_MAX_COLOR = "red";
    private static final String DEBUG_OPTION = "debug";
    private static final String DEBUG_SHORT_OPTION = "d";
    private static final String HELP_OPTION = "help";
    private static final String HELP_SHORT_OPTION = "h";
    private static final String COLORS_OPTION = "colors";
    private static final String COLORS_SHORT_OPTION = "c";
    private static final String BASE_OPTION = "base";
    private static final String BASE_SHORT_OPTION = "b";
    private static final String SAMPLE_OPTION = "sample";
    private static final String SAMPLE_SHORT_OPTION = "s";
    
    private static Logger LOG = Logger.getLogger(Main.class.getName());
    
    static boolean testing = false;
    static int testingStatus = 0;

    public static void main(String[] args) {
                      

        DataReaderRegistry reg = initRegistry();
        Graph graph = new SwingGraph();
        GraphController controller = new GraphController(graph);

        processArgs(args, reg, controller, graph);

        controller.run();
    }

    static void processArgs(String[] args,
                                    DataReaderRegistry reg,
                                    GraphController controller, 
                                    Graph graph) {

        try {
            CommandLine cmdLine = parseArgs(args);
            if (cmdLine == null) {
                exit(0);
            }

            String format = DEFAULT_FORMAT;
            if (cmdLine.hasOption(FORMAT_OPTION)) {
                format = (String) cmdLine.getParsedOptionValue(FORMAT_OPTION);
            }

            LOG.info("data input format: " + format);
            DataReader dr = reg.find(format);
            if (dr == null) {            
                error("data input format '" + format + "' is not supported\n"
                        + "supported formats: " + reg.getSupportedFormats());
            }
            dr.setInputStream(System.in);
            controller.setDataReader(dr);

            if (cmdLine.hasOption(TITLE_OPTION)) {
                graph.setTitle(cmdLine.getOptionValue(TITLE_OPTION));
            }

            if (cmdLine.hasOption(MIN_OPTION)) {
                String c = cmdLine.getOptionValue(MIN_OPTION);
                if (c == null) {
                    c = DEFAULT_MIN_COLOR;
                }
                graph.setShowMin(c);
            }

            if (cmdLine.hasOption(MAX_OPTION)) {
                String c = cmdLine.getOptionValue(MAX_OPTION);
                if (c == null) {
                    c = DEFAULT_MAX_COLOR;
                }
                graph.setShowMax(c);
            }

            if (cmdLine.hasOption(DEBUG_OPTION)) {
                graph.setDebug(true);
                dr.setDebug(true);
                controller.setDebug(true);
            }

            if (cmdLine.hasOption(COLORS_OPTION)) {
                graph.setColors(cmdLine.getOptionValues(COLORS_OPTION));
            }

            if (cmdLine.hasOption(BASE_OPTION)) {
                graph.setBase(((Long)cmdLine.getParsedOptionValue(BASE_OPTION)).intValue());
            }

            if (cmdLine.hasOption(SAMPLE_OPTION)) {
                controller.setSample(((Long)cmdLine.getParsedOptionValue(SAMPLE_OPTION)).intValue());
            }

        } catch (ParseException e) {
            usage(e); 
            LOG.warning("exiting with status 1");
            exit(1);
        }
        

    }

    @SuppressWarnings({ "static-access", "unchecked" })
    static CommandLine parseArgs(String[] args) throws ParseException {
        
            Options options = new Options()
                .addOption(OptionBuilder.withLongOpt(HELP_OPTION)
                                        .withDescription("print this message")
                                        .create(HELP_SHORT_OPTION))
                .addOption(OptionBuilder.withLongOpt(DEBUG_OPTION)
                                        .withDescription("debug mode")
                                        .create(DEBUG_SHORT_OPTION))
                .addOption(OptionBuilder.withLongOpt(TITLE_OPTION)
                                        .withDescription("Sets the window title")
                                        .hasArg()
                                        .withType(PatternOptionBuilder.STRING_VALUE)
                                        .create(TITLE_SHORT_OPTION))
                .addOption(OptionBuilder.withLongOpt(FORMAT_OPTION)
                                        .withDescription("Sets the input format")
                                        .hasArg()
                                        .withType(PatternOptionBuilder.STRING_VALUE)
                                        .create(FORMAT_SHORT_OPTION))
                .addOption(OptionBuilder.withLongOpt(SAMPLE_OPTION)
                                        .withDescription("Sets the time between samples")
                                        .hasArg()
                                        .withType(PatternOptionBuilder.NUMBER_VALUE)
                                        .create(SAMPLE_SHORT_OPTION))
                .addOption(OptionBuilder.withLongOpt(BASE_OPTION)
                                        .withDescription("Sets the number base for the values")
                                        .withType(PatternOptionBuilder.NUMBER_VALUE)
                                        .hasArg()
                                        .create(BASE_SHORT_OPTION))
                .addOption(OptionBuilder.withLongOpt(MIN_OPTION)
                                        .withDescription("Show minimum values with optional color")
                                        .hasOptionalArg()
                                        .withType(PatternOptionBuilder.NUMBER_VALUE)
                                        .create())
                .addOption(OptionBuilder.withLongOpt(MAX_OPTION)
                                        .withDescription("Show maximum values with optional color")
                                        .hasOptionalArg()
                                        .withType(PatternOptionBuilder.NUMBER_VALUE)
                                        .create())
                .addOption(OptionBuilder.withLongOpt(COLORS_OPTION)
                                        .withDescription("Colors to use for graph lines")
                                        .hasArgs()             
                                        .withValueSeparator(',')
                                        .withType(PatternOptionBuilder.STRING_VALUE)
                                        .create(COLORS_SHORT_OPTION));

            CommandLineParser parser = new GnuParser();
            CommandLine cmdLine = parser.parse(options, args);

            Iterator it = cmdLine.iterator();
            while (it.hasNext()) {
                LOG.info(it.next().toString());
            }
               
            if (cmdLine.hasOption("help")) {
                new HelpFormatter().printHelp( "graphpipe", options );
                return null;
            }
            
            return cmdLine;            
    }

    private static DataReaderRegistry initRegistry() {
        // TODO: add tests for these data readers
        DataReaderRegistry reg = new DataReaderRegistry();
        reg.add(new TextDataReader(new RegexpTextDataSplitter("\\s+")), "spaces");
        reg.add(new TextDataReader(new RegexpTextDataSplitter("\\s*,\\s*")), "csv");
        reg.add(new TextDataReader(new RegexpTextDataSplitter("\\s*:\\s*")), "colon");
        // TODO: allow regexp to be set using a command line argument
        return reg;
    }
    


    private static void error(String msg) {
        System.err.println("error: " + msg);
        exit(1);
    }
    
    private static void usage(ParseException e) {
        System.err.println("error: " + e.getLocalizedMessage());
        System.err.println("Use 'graphpipe --help' for information on command line arguments.");
        exit(1);
    }

    
    private static void exit(int status) {
        if (testing) {
            testingStatus = status;
            throw new RuntimeException("exiting with status " + status);
        }
        LOG.warning("exiting with status " + status);

        System.exit(status);
    }
}
