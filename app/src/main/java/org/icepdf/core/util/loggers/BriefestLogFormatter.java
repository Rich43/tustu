package org.icepdf.core.util.loggers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/* loaded from: icepdf-core.jar:org/icepdf/core/util/loggers/BriefestLogFormatter.class */
public class BriefestLogFormatter extends Formatter {
    private static final DateFormat format = new SimpleDateFormat("h:mm:ss");
    private static final String lineSep = System.getProperty("line.separator");

    @Override // java.util.logging.Formatter
    public String format(LogRecord record) {
        String loggerName = record.getLoggerName();
        if (loggerName == null) {
        }
        StringBuilder output = new StringBuilder().append(record.getMessage()).append(lineSep);
        return output.toString();
    }
}
