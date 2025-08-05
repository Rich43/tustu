package sun.rmi.runtime;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.rmi.server.LogStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/rmi/runtime/Log.class */
public abstract class Log {
    public static final Level BRIEF = Level.FINE;
    public static final Level VERBOSE = Level.FINER;
    private static final LogFactory logFactory;

    /* loaded from: rt.jar:sun/rmi/runtime/Log$LogFactory.class */
    private interface LogFactory {
        Log createLog(String str, String str2, Level level);
    }

    public abstract boolean isLoggable(Level level);

    public abstract void log(Level level, String str);

    public abstract void log(Level level, String str, Throwable th);

    public abstract void setOutputStream(OutputStream outputStream);

    public abstract PrintStream getPrintStream();

    static {
        logFactory = Boolean.valueOf((String) AccessController.doPrivileged(new GetPropertyAction("sun.rmi.log.useOld"))).booleanValue() ? new LogStreamLogFactory() : new LoggerLogFactory();
    }

    public static Log getLog(String str, String str2, int i2) {
        Level level;
        if (i2 < 0) {
            level = null;
        } else if (i2 == 0) {
            level = Level.OFF;
        } else if (i2 > 0 && i2 <= 10) {
            level = BRIEF;
        } else if (i2 > 10 && i2 <= 20) {
            level = VERBOSE;
        } else {
            level = Level.FINEST;
        }
        return logFactory.createLog(str, str2, level);
    }

    public static Log getLog(String str, String str2, boolean z2) {
        return logFactory.createLog(str, str2, z2 ? VERBOSE : null);
    }

    /* loaded from: rt.jar:sun/rmi/runtime/Log$LoggerLogFactory.class */
    private static class LoggerLogFactory implements LogFactory {
        LoggerLogFactory() {
        }

        @Override // sun.rmi.runtime.Log.LogFactory
        public Log createLog(String str, String str2, Level level) {
            return new LoggerLog(Logger.getLogger(str), level);
        }
    }

    /* loaded from: rt.jar:sun/rmi/runtime/Log$LoggerLog.class */
    private static class LoggerLog extends Log {
        private static final Handler alternateConsole = (Handler) AccessController.doPrivileged(new PrivilegedAction<Handler>() { // from class: sun.rmi.runtime.Log.LoggerLog.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Handler run2() {
                InternalStreamHandler internalStreamHandler = new InternalStreamHandler(System.err);
                internalStreamHandler.setLevel(Level.ALL);
                return internalStreamHandler;
            }
        });
        private InternalStreamHandler copyHandler;
        private final Logger logger;
        private LoggerPrintStream loggerSandwich;

        private LoggerLog(final Logger logger, final Level level) {
            this.copyHandler = null;
            this.logger = logger;
            if (level != null) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.rmi.runtime.Log.LoggerLog.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() throws SecurityException {
                        if (!logger.isLoggable(level)) {
                            logger.setLevel(level);
                        }
                        logger.addHandler(LoggerLog.alternateConsole);
                        return null;
                    }
                });
            }
        }

        @Override // sun.rmi.runtime.Log
        public boolean isLoggable(Level level) {
            return this.logger.isLoggable(level);
        }

        @Override // sun.rmi.runtime.Log
        public void log(Level level, String str) {
            if (isLoggable(level)) {
                String[] source = Log.getSource();
                this.logger.logp(level, source[0], source[1], Thread.currentThread().getName() + ": " + str);
            }
        }

        @Override // sun.rmi.runtime.Log
        public void log(Level level, String str, Throwable th) {
            if (isLoggable(level)) {
                String[] source = Log.getSource();
                this.logger.logp(level, source[0], source[1], Thread.currentThread().getName() + ": " + str, th);
            }
        }

        @Override // sun.rmi.runtime.Log
        public synchronized void setOutputStream(OutputStream outputStream) throws SecurityException {
            if (outputStream != null) {
                if (!this.logger.isLoggable(VERBOSE)) {
                    this.logger.setLevel(VERBOSE);
                }
                this.copyHandler = new InternalStreamHandler(outputStream);
                this.copyHandler.setLevel(Log.VERBOSE);
                this.logger.addHandler(this.copyHandler);
                return;
            }
            if (this.copyHandler != null) {
                this.logger.removeHandler(this.copyHandler);
            }
            this.copyHandler = null;
        }

        @Override // sun.rmi.runtime.Log
        public synchronized PrintStream getPrintStream() {
            if (this.loggerSandwich == null) {
                this.loggerSandwich = new LoggerPrintStream(this.logger);
            }
            return this.loggerSandwich;
        }
    }

    /* loaded from: rt.jar:sun/rmi/runtime/Log$InternalStreamHandler.class */
    private static class InternalStreamHandler extends StreamHandler {
        InternalStreamHandler(OutputStream outputStream) {
            super(outputStream, new SimpleFormatter());
        }

        @Override // java.util.logging.StreamHandler, java.util.logging.Handler
        public void publish(LogRecord logRecord) {
            super.publish(logRecord);
            flush();
        }

        @Override // java.util.logging.StreamHandler, java.util.logging.Handler
        public void close() {
            flush();
        }
    }

    /* loaded from: rt.jar:sun/rmi/runtime/Log$LoggerPrintStream.class */
    private static class LoggerPrintStream extends PrintStream {
        private final Logger logger;
        private int last;
        private final ByteArrayOutputStream bufOut;

        private LoggerPrintStream(Logger logger) {
            super(new ByteArrayOutputStream());
            this.last = -1;
            this.bufOut = (ByteArrayOutputStream) this.out;
            this.logger = logger;
        }

        @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
        public void write(int i2) {
            if (this.last == 13 && i2 == 10) {
                this.last = -1;
                return;
            }
            if (i2 == 10 || i2 == 13) {
                try {
                    this.logger.logp(Level.INFO, "LogStream", "print", Thread.currentThread().getName() + ": " + this.bufOut.toString());
                } finally {
                    this.bufOut.reset();
                }
            } else {
                super.write(i2);
            }
            this.last = i2;
        }

        @Override // java.io.PrintStream, java.io.FilterOutputStream, java.io.OutputStream
        public void write(byte[] bArr, int i2, int i3) {
            if (i3 < 0) {
                throw new ArrayIndexOutOfBoundsException(i3);
            }
            for (int i4 = 0; i4 < i3; i4++) {
                write(bArr[i2 + i4]);
            }
        }

        public String toString() {
            return "RMI";
        }
    }

    /* loaded from: rt.jar:sun/rmi/runtime/Log$LogStreamLogFactory.class */
    private static class LogStreamLogFactory implements LogFactory {
        LogStreamLogFactory() {
        }

        @Override // sun.rmi.runtime.Log.LogFactory
        public Log createLog(String str, String str2, Level level) {
            LogStream logStreamLog = null;
            if (str2 != null) {
                logStreamLog = LogStream.log(str2);
            }
            return new LogStreamLog(logStreamLog, level);
        }
    }

    /* loaded from: rt.jar:sun/rmi/runtime/Log$LogStreamLog.class */
    private static class LogStreamLog extends Log {
        private final LogStream stream;
        private int levelValue;

        private LogStreamLog(LogStream logStream, Level level) {
            this.levelValue = Level.OFF.intValue();
            if (logStream != null && level != null) {
                this.levelValue = level.intValue();
            }
            this.stream = logStream;
        }

        @Override // sun.rmi.runtime.Log
        public synchronized boolean isLoggable(Level level) {
            return level.intValue() >= this.levelValue;
        }

        @Override // sun.rmi.runtime.Log
        public void log(Level level, String str) {
            if (isLoggable(level)) {
                String[] source = Log.getSource();
                this.stream.println(unqualifiedName(source[0]) + "." + source[1] + ": " + str);
            }
        }

        @Override // sun.rmi.runtime.Log
        public void log(Level level, String str, Throwable th) {
            if (isLoggable(level)) {
                synchronized (this.stream) {
                    String[] source = Log.getSource();
                    this.stream.println(unqualifiedName(source[0]) + "." + source[1] + ": " + str);
                    th.printStackTrace(this.stream);
                }
            }
        }

        @Override // sun.rmi.runtime.Log
        public PrintStream getPrintStream() {
            return this.stream;
        }

        @Override // sun.rmi.runtime.Log
        public synchronized void setOutputStream(OutputStream outputStream) {
            if (outputStream != null) {
                if (VERBOSE.intValue() < this.levelValue) {
                    this.levelValue = VERBOSE.intValue();
                }
                this.stream.setOutputStream(outputStream);
                return;
            }
            this.levelValue = Level.OFF.intValue();
        }

        private static String unqualifiedName(String str) {
            int iLastIndexOf = str.lastIndexOf(".");
            if (iLastIndexOf >= 0) {
                str = str.substring(iLastIndexOf + 1);
            }
            return str.replace('$', '.');
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String[] getSource() {
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        return new String[]{stackTrace[3].getClassName(), stackTrace[3].getMethodName()};
    }
}
