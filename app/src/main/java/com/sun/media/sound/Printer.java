package com.sun.media.sound;

/* loaded from: rt.jar:com/sun/media/sound/Printer.class */
final class Printer {
    static final boolean err = false;
    static final boolean debug = false;
    static final boolean trace = false;
    static final boolean verbose = false;
    static final boolean release = false;
    static final boolean SHOW_THREADID = false;
    static final boolean SHOW_TIMESTAMP = false;
    private static long startTime = 0;

    private Printer() {
    }

    public static void err(String str) {
    }

    public static void debug(String str) {
    }

    public static void trace(String str) {
    }

    public static void verbose(String str) {
    }

    public static void release(String str) {
    }

    public static void println(String str) {
        System.out.println("" + str);
    }

    public static void println() {
        System.out.println();
    }
}
