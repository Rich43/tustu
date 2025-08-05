package com.sun.jmx.remote.util;

import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/jmx/remote/util/ClassLogger.class */
public class ClassLogger {
    private static final boolean ok = true;
    private final String className;
    private final Logger logger;

    public ClassLogger(String str, String str2) {
        if (ok) {
            this.logger = Logger.getLogger(str);
        } else {
            this.logger = null;
        }
        this.className = str2;
    }

    public final boolean traceOn() {
        return finerOn();
    }

    public final boolean debugOn() {
        return finestOn();
    }

    public final boolean warningOn() {
        return ok && this.logger.isLoggable(Level.WARNING);
    }

    public final boolean infoOn() {
        return ok && this.logger.isLoggable(Level.INFO);
    }

    public final boolean configOn() {
        return ok && this.logger.isLoggable(Level.CONFIG);
    }

    public final boolean fineOn() {
        return ok && this.logger.isLoggable(Level.FINE);
    }

    public final boolean finerOn() {
        return ok && this.logger.isLoggable(Level.FINER);
    }

    public final boolean finestOn() {
        return ok && this.logger.isLoggable(Level.FINEST);
    }

    public final void debug(String str, String str2) {
        finest(str, str2);
    }

    public final void debug(String str, Throwable th) {
        finest(str, th);
    }

    public final void debug(String str, String str2, Throwable th) {
        finest(str, str2, th);
    }

    public final void trace(String str, String str2) {
        finer(str, str2);
    }

    public final void trace(String str, Throwable th) {
        finer(str, th);
    }

    public final void trace(String str, String str2, Throwable th) {
        finer(str, str2, th);
    }

    public final void error(String str, String str2) {
        severe(str, str2);
    }

    public final void error(String str, Throwable th) {
        severe(str, th);
    }

    public final void error(String str, String str2, Throwable th) {
        severe(str, str2, th);
    }

    public final void finest(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.FINEST, this.className, str, str2);
        }
    }

    public final void finest(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.FINEST, this.className, str, th.toString(), th);
        }
    }

    public final void finest(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.FINEST, this.className, str, str2, th);
        }
    }

    public final void finer(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.FINER, this.className, str, str2);
        }
    }

    public final void finer(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.FINER, this.className, str, th.toString(), th);
        }
    }

    public final void finer(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.FINER, this.className, str, str2, th);
        }
    }

    public final void fine(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.FINE, this.className, str, str2);
        }
    }

    public final void fine(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.FINE, this.className, str, th.toString(), th);
        }
    }

    public final void fine(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.FINE, this.className, str, str2, th);
        }
    }

    public final void config(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.CONFIG, this.className, str, str2);
        }
    }

    public final void config(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.CONFIG, this.className, str, th.toString(), th);
        }
    }

    public final void config(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.CONFIG, this.className, str, str2, th);
        }
    }

    public final void info(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.INFO, this.className, str, str2);
        }
    }

    public final void info(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.INFO, this.className, str, th.toString(), th);
        }
    }

    public final void info(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.INFO, this.className, str, str2, th);
        }
    }

    public final void warning(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.WARNING, this.className, str, str2);
        }
    }

    public final void warning(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.WARNING, this.className, str, th.toString(), th);
        }
    }

    public final void warning(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.WARNING, this.className, str, str2, th);
        }
    }

    public final void severe(String str, String str2) {
        if (ok) {
            this.logger.logp(Level.SEVERE, this.className, str, str2);
        }
    }

    public final void severe(String str, Throwable th) {
        if (ok) {
            this.logger.logp(Level.SEVERE, this.className, str, th.toString(), th);
        }
    }

    public final void severe(String str, String str2, Throwable th) {
        if (ok) {
            this.logger.logp(Level.SEVERE, this.className, str, str2, th);
        }
    }
}
