package java.util.logging;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import sun.util.logging.LoggingSupport;

/* loaded from: rt.jar:java/util/logging/SimpleFormatter.class */
public class SimpleFormatter extends Formatter {
    private static final String format = LoggingSupport.getSimpleFormat();
    private final Date dat = new Date();

    @Override // java.util.logging.Formatter
    public synchronized String format(LogRecord logRecord) {
        String loggerName;
        this.dat.setTime(logRecord.getMillis());
        if (logRecord.getSourceClassName() != null) {
            loggerName = logRecord.getSourceClassName();
            if (logRecord.getSourceMethodName() != null) {
                loggerName = loggerName + " " + logRecord.getSourceMethodName();
            }
        } else {
            loggerName = logRecord.getLoggerName();
        }
        String message = formatMessage(logRecord);
        String string = "";
        if (logRecord.getThrown() != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            printWriter.println();
            logRecord.getThrown().printStackTrace(printWriter);
            printWriter.close();
            string = stringWriter.toString();
        }
        return String.format(format, this.dat, loggerName, logRecord.getLoggerName(), logRecord.getLevel().getLocalizedLevelName(), message, string);
    }
}
