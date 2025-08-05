package java.util.logging;

/* loaded from: rt.jar:java/util/logging/ConsoleHandler.class */
public class ConsoleHandler extends StreamHandler {
    private void configure() {
        LogManager logManager = LogManager.getLogManager();
        String name = getClass().getName();
        setLevel(logManager.getLevelProperty(name + ".level", Level.INFO));
        setFilter(logManager.getFilterProperty(name + ".filter", null));
        setFormatter(logManager.getFormatterProperty(name + ".formatter", new SimpleFormatter()));
        try {
            setEncoding(logManager.getStringProperty(name + ".encoding", null));
        } catch (Exception e2) {
            try {
                setEncoding(null);
            } catch (Exception e3) {
            }
        }
    }

    public ConsoleHandler() {
        this.sealed = false;
        configure();
        setOutputStream(System.err);
        this.sealed = true;
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
