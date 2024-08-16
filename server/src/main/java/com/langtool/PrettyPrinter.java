package com.langtool;

public class PrettyPrinter {
    // ANSI color codes
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public static void info(String message) {
        System.out.println(ANSI_BLUE + "[INFO] " + message + ANSI_RESET);
    }

    public static void warn(String message) {
        System.out.println(ANSI_YELLOW + "[WARN] " + message + ANSI_RESET);
    }

    public static void err(String message) {
        System.out.println(ANSI_RED + "[ERROR] " + message + ANSI_RESET);
    }

    public static void infoWithLine(String message) {
        System.out.println(ANSI_BLUE + "[INFO] " + getCallerInfo() + message + ANSI_RESET);
    }

    public static void warnWithLine(String message) {
        System.out.println(ANSI_YELLOW + "[WARN] " + getCallerInfo() + message + ANSI_RESET);
    }

    public static void errWithLine(String message) {
        System.out.println(ANSI_RED + "[ERROR] " + getCallerInfo() + message + ANSI_RESET);
    }

    private static String getCallerInfo() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length >= 4) {
            StackTraceElement caller = stackTraceElements[3];
            return "(" + caller.getFileName() + ":" + caller.getLineNumber() + ") ";
        }
        return "";
    }
}