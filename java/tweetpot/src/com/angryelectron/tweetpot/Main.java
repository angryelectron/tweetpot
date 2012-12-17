/*
 * Tweetpot - Tweets when the kettle has boiled
 * Copyright 2012 Andrew Bythell <abythell@ieee.org>
 */
package com.angryelectron.tweetpot;

import com.rapplogic.xbee.api.XBeeException;
import org.apache.commons.cli.*;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;


public class Main {

    static final String version = "1.0";
    static final String progName = "tweetpot";
    static final String progDesc = "Tweet when kettle boils";
    private static String port = "/dev/ttyUSB0";
    private static int baud = 9600;
    
    public static void main(String[] args) throws XBeeException {
        
        CommandLineParser parser = new PosixParser();
        CommandLine cmd = null;
        
        try {
            cmd = parser.parse(getOptions(), args);
        } catch (ParseException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.WARN, ex.getMessage());
                    
        }
        
        if (cmd.hasOption("h") || (cmd.hasOption("v"))) {
            getHelp();
            System.exit(0);            
        }
        
        if (cmd.hasOption("p")) {
            port = cmd.getOptionValue("p");
        }
        
        if (cmd.hasOption("b")) {
            try {
                baud = Integer.parseInt(cmd.getOptionValue("b"));
            } catch (NumberFormatException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.WARN, "invalid baud rate");
                System.exit(1);
            }
        }                        
        Tweetpot tweetPot = new Tweetpot(port, baud);        
    }

    private static Options getOptions() {
        Options options = new Options();
        options.addOption("p", false, "XBee's serial port (/dev/ttyUSB0)");
        options.addOption("b", false, "XBee's baud rate (9600)");
        options.addOption("h", false, "Help");
        options.addOption("v", false, "Version");
        return options;
    }

    private static void getHelp() {
        System.out.println(progName + " version " + version + " (C)2012 Andrew Bythell <abythell@ieee.org>");
        System.out.println(progDesc);
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(progName, getOptions());
    }
        
}    
