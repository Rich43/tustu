package com.sun.org.slf4j.internal;

import java.util.logging.Level;

/* loaded from: rt.jar:com/sun/org/slf4j/internal/Logger.class */
public class Logger {
    private final java.util.logging.Logger impl;

    public Logger(String str) {
        this.impl = java.util.logging.Logger.getLogger(str);
    }

    public boolean isDebugEnabled() {
        return this.impl.isLoggable(Level.FINE);
    }

    public boolean isTraceEnabled() {
        return this.impl.isLoggable(Level.FINE);
    }

    public void debug(String str) {
        this.impl.log(Level.FINE, str);
    }

    public void debug(String str, Throwable th) {
        this.impl.log(Level.FINE, str, th);
    }

    public void debug(String str, Object... objArr) {
        this.impl.log(Level.FINE, str, objArr);
    }

    public void trace(String str) {
        this.impl.log(Level.FINE, str);
    }

    public void error(String str) {
        this.impl.log(Level.SEVERE, str);
    }

    public void error(String str, Throwable th) {
        this.impl.log(Level.SEVERE, str, th);
    }

    public void error(String str, Object... objArr) {
        this.impl.log(Level.SEVERE, str, objArr);
    }

    public void warn(String str) {
        this.impl.log(Level.WARNING, str);
    }

    public void warn(String str, Throwable th) {
        this.impl.log(Level.WARNING, str, th);
    }
}
