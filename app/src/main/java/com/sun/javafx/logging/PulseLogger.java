package com.sun.javafx.logging;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/logging/PulseLogger.class */
public class PulseLogger {
    public static final boolean PULSE_LOGGING_ENABLED;
    private static final Logger[] loggers;

    static {
        List<Logger> list = new ArrayList<>();
        Logger logger = PrintLogger.getInstance();
        if (logger != null) {
            list.add(logger);
        }
        try {
            Class klass = Class.forName("com.sun.javafx.logging.JFRLogger");
            if (klass != null) {
                Method method = klass.getDeclaredMethod("getInstance", new Class[0]);
                Logger logger2 = (Logger) method.invoke(null, new Object[0]);
                if (logger2 != null) {
                    list.add(logger2);
                }
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoClassDefFoundError | NoSuchMethodException | InvocationTargetException e2) {
        }
        loggers = (Logger[]) list.toArray(new Logger[list.size()]);
        PULSE_LOGGING_ENABLED = loggers.length > 0;
    }

    public static void pulseStart() {
        for (Logger logger : loggers) {
            logger.pulseStart();
        }
    }

    public static void pulseEnd() {
        for (Logger logger : loggers) {
            logger.pulseEnd();
        }
    }

    public static void renderStart() {
        for (Logger logger : loggers) {
            logger.renderStart();
        }
    }

    public static void renderEnd() {
        for (Logger logger : loggers) {
            logger.renderEnd();
        }
    }

    public static void addMessage(String message) {
        for (Logger logger : loggers) {
            logger.addMessage(message);
        }
    }

    public static void incrementCounter(String counter) {
        for (Logger logger : loggers) {
            logger.incrementCounter(counter);
        }
    }

    public static void newPhase(String name) {
        for (Logger logger : loggers) {
            logger.newPhase(name);
        }
    }

    public static void newInput(String name) {
        for (Logger logger : loggers) {
            logger.newInput(name);
        }
    }
}
