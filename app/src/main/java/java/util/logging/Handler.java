package java.util.logging;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;

/* loaded from: rt.jar:java/util/logging/Handler.class */
public abstract class Handler {
    private static final int offValue = Level.OFF.intValue();
    private volatile Filter filter;
    private volatile Formatter formatter;
    private volatile String encoding;
    private final LogManager manager = LogManager.getLogManager();
    private volatile Level logLevel = Level.ALL;
    private volatile ErrorManager errorManager = new ErrorManager();
    boolean sealed = true;

    public abstract void publish(LogRecord logRecord);

    public abstract void flush();

    public abstract void close() throws SecurityException;

    protected Handler() {
    }

    public synchronized void setFormatter(Formatter formatter) throws SecurityException {
        checkPermission();
        formatter.getClass();
        this.formatter = formatter;
    }

    public Formatter getFormatter() {
        return this.formatter;
    }

    public synchronized void setEncoding(String str) throws SecurityException, UnsupportedEncodingException {
        checkPermission();
        if (str != null) {
            try {
                if (!Charset.isSupported(str)) {
                    throw new UnsupportedEncodingException(str);
                }
            } catch (IllegalCharsetNameException e2) {
                throw new UnsupportedEncodingException(str);
            }
        }
        this.encoding = str;
    }

    public String getEncoding() {
        return this.encoding;
    }

    public synchronized void setFilter(Filter filter) throws SecurityException {
        checkPermission();
        this.filter = filter;
    }

    public Filter getFilter() {
        return this.filter;
    }

    public synchronized void setErrorManager(ErrorManager errorManager) throws SecurityException {
        checkPermission();
        if (errorManager == null) {
            throw new NullPointerException();
        }
        this.errorManager = errorManager;
    }

    public ErrorManager getErrorManager() throws SecurityException {
        checkPermission();
        return this.errorManager;
    }

    protected void reportError(String str, Exception exc, int i2) {
        try {
            this.errorManager.error(str, exc, i2);
        } catch (Exception e2) {
            System.err.println("Handler.reportError caught:");
            e2.printStackTrace();
        }
    }

    public synchronized void setLevel(Level level) throws SecurityException {
        if (level == null) {
            throw new NullPointerException();
        }
        checkPermission();
        this.logLevel = level;
    }

    public Level getLevel() {
        return this.logLevel;
    }

    public boolean isLoggable(LogRecord logRecord) {
        int iIntValue = getLevel().intValue();
        if (logRecord.getLevel().intValue() < iIntValue || iIntValue == offValue) {
            return false;
        }
        Filter filter = getFilter();
        if (filter == null) {
            return true;
        }
        return filter.isLoggable(logRecord);
    }

    void checkPermission() throws SecurityException {
        if (this.sealed) {
            this.manager.checkPermission();
        }
    }
}
