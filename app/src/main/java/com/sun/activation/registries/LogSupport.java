package com.sun.activation.registries;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/activation/registries/LogSupport.class */
public class LogSupport {
    private static boolean debug;
    private static Logger logger;
    private static final Level level = Level.FINE;

    static {
        debug = false;
        try {
            debug = Boolean.getBoolean("javax.activation.debug");
        } catch (Throwable th) {
        }
        logger = Logger.getLogger("javax.activation");
    }

    private LogSupport() {
    }

    public static void log(String msg) {
        if (debug) {
            System.out.println(msg);
        }
        logger.log(level, msg);
    }

    public static void log(String msg, Throwable t2) {
        if (debug) {
            System.out.println(msg + "; Exception: " + ((Object) t2));
        }
        logger.log(level, msg, t2);
    }

    public static boolean isLoggable() {
        return debug || logger.isLoggable(level);
    }
}
