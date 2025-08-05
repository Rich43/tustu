package java.util.logging;

import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:java/util/logging/MemoryHandler.class */
public class MemoryHandler extends Handler {
    private static final int DEFAULT_SIZE = 1000;
    private volatile Level pushLevel;
    private int size;
    private Handler target;
    private LogRecord[] buffer;
    int start;
    int count;

    private void configure() {
        LogManager logManager = LogManager.getLogManager();
        String name = getClass().getName();
        this.pushLevel = logManager.getLevelProperty(name + ".push", Level.SEVERE);
        this.size = logManager.getIntProperty(name + ".size", 1000);
        if (this.size <= 0) {
            this.size = 1000;
        }
        setLevel(logManager.getLevelProperty(name + ".level", Level.ALL));
        setFilter(logManager.getFilterProperty(name + ".filter", null));
        setFormatter(logManager.getFormatterProperty(name + ".formatter", new SimpleFormatter()));
    }

    public MemoryHandler() {
        this.sealed = false;
        configure();
        this.sealed = true;
        LogManager logManager = LogManager.getLogManager();
        String name = getClass().getName();
        String property = logManager.getProperty(name + ".target");
        if (property == null) {
            throw new RuntimeException("The handler " + name + " does not specify a target");
        }
        try {
            this.target = (Handler) ClassLoader.getSystemClassLoader().loadClass(property).newInstance();
            init();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e2) {
            throw new RuntimeException("MemoryHandler can't load handler target \"" + property + PdfOps.DOUBLE_QUOTE__TOKEN, e2);
        }
    }

    private void init() {
        this.buffer = new LogRecord[this.size];
        this.start = 0;
        this.count = 0;
    }

    public MemoryHandler(Handler handler, int i2, Level level) {
        if (handler == null || level == null) {
            throw new NullPointerException();
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException();
        }
        this.sealed = false;
        configure();
        this.sealed = true;
        this.target = handler;
        this.pushLevel = level;
        this.size = i2;
        init();
    }

    @Override // java.util.logging.Handler
    public synchronized void publish(LogRecord logRecord) {
        if (!isLoggable(logRecord)) {
            return;
        }
        this.buffer[(this.start + this.count) % this.buffer.length] = logRecord;
        if (this.count < this.buffer.length) {
            this.count++;
        } else {
            this.start++;
            this.start %= this.buffer.length;
        }
        if (logRecord.getLevel().intValue() >= this.pushLevel.intValue()) {
            push();
        }
    }

    public synchronized void push() {
        for (int i2 = 0; i2 < this.count; i2++) {
            this.target.publish(this.buffer[(this.start + i2) % this.buffer.length]);
        }
        this.start = 0;
        this.count = 0;
    }

    @Override // java.util.logging.Handler
    public void flush() {
        this.target.flush();
    }

    @Override // java.util.logging.Handler
    public void close() throws SecurityException {
        this.target.close();
        setLevel(Level.OFF);
    }

    public synchronized void setPushLevel(Level level) throws SecurityException {
        if (level == null) {
            throw new NullPointerException();
        }
        checkPermission();
        this.pushLevel = level;
    }

    public Level getPushLevel() {
        return this.pushLevel;
    }

    @Override // java.util.logging.Handler
    public boolean isLoggable(LogRecord logRecord) {
        return super.isLoggable(logRecord);
    }
}
