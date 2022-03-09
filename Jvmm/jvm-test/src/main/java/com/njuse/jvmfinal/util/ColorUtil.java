package com.njuse.jvmfinal.util;

public class ColorUtil {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final int COLOR_UTIL_STATUS = 1; // ON 1 OFF 0


    public static void printWhite(String message) {
        if(COLOR_UTIL_STATUS==0)return;
        System.out.println(ANSI_WHITE + message + ANSI_RESET);
    }

    public static void printBlue(String message) {
        if(COLOR_UTIL_STATUS==0)return;
        System.out.println(ANSI_BLUE + message + ANSI_RESET);
    }

    public static void printYellow(String message) {
        if(COLOR_UTIL_STATUS==0)return;
        System.out.println(ANSI_YELLOW + message + ANSI_RESET);
    }

    public static void printGreen(String message) {
        if(COLOR_UTIL_STATUS==0)return;
        System.out.println(ANSI_GREEN + message + ANSI_RESET);
    }

    public static void printPurple(String message) {
        if(COLOR_UTIL_STATUS==0)return;

//        System.out.println(ANSI_PURPLE + message + ANSI_RESET);
    }

    public static void printRed(String message) {
        if(COLOR_UTIL_STATUS==0)return;
        System.out.println(ANSI_RED + message + ANSI_RESET);
    }

    public static void printCyan(String message) {
        if(COLOR_UTIL_STATUS==0)return;
        System.out.println(ANSI_CYAN + message + ANSI_RESET);
    }
}