package com.sun.webkit;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/EventLoop.class */
public abstract class EventLoop {
    private static final Logger logger = Logger.getLogger(EventLoop.class.getName());
    private static EventLoop instance;

    protected abstract void cycle();

    public static void setEventLoop(EventLoop eventLoop) {
        instance = eventLoop;
    }

    private static void fwkCycle() {
        logger.log(Level.FINE, "Executing event loop cycle");
        instance.cycle();
    }
}
