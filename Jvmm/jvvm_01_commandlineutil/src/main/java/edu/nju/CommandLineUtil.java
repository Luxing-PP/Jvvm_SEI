package edu.nju;

import org.apache.commons.cli.*;

import java.util.ArrayList;

public class CommandLineUtil {
    private static CommandLine commandLine;
    private static CommandLineParser parser = new DefaultParser();
    private static Options options = new Options();
    private boolean sideEffect = false;
    public static final String WRONG_MESSAGE = "Invalid input.";

    /**
     * you can define options here
     * or you can create a func such as [static void defineOptions()] and call it before parse input
     */
    static {
        options.addOption("h","help",false,"打印出所有预定义的选项与用法");
        options.addOption("p","print",true,"打印出arg");
        options.addOption("s",false,"将CommandlineUtil中sideEffect变量置为true");
    }
        //这结构化绝了= =，这就是学长吗
    public void main(String[] args){
        parseInput(args);
        handleOptions();
    }

    /**
     * Print the usage of all options
     */
    private static void printHelpMessage() {
        //???
        System.out.println("help");
//        HelpFormatter helpFormatter = new HelpFormatter();
//        helpFormatter.printHelp("help",options,true);
    }

    /**
     * Parse the input and handle exception
     * @param args origin args form input
     */
    public void parseInput(String[] args)  {
        try{
            commandLine = parser.parse(options,args);
        }catch (ParseException exp){
            System.out.println(exp.getMessage());
            System.exit(-1);
            //good
        }
    }

    /**
     * You can handle options here or create your own func
     */
    public void handleOptions() {
        if(commandLine.hasOption("h")){

            printHelpMessage();
            return;
        }
        else if(commandLine.hasOption("s")){
            String[] args    = commandLine.getArgs();
            if(args.length<1){
                System.out.println(WRONG_MESSAGE);
                return;
            }
            sideEffect=true;
        }
        else if(commandLine.hasOption("p")){
            String[] args    = commandLine.getArgs();
            if(args.length<1){
                System.out.println(WRONG_MESSAGE);
                return;
            }
            System.out.println(commandLine.getOptionValue("p"));
        }


    }

    public boolean getSideEffectFlag(){

        return sideEffect;
    }

}
