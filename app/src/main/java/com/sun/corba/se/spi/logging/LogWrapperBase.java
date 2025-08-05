package com.sun.corba.se.spi.logging;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/corba/se/spi/logging/LogWrapperBase.class */
public abstract class LogWrapperBase {
    protected Logger logger;
    protected String loggerName;

    protected LogWrapperBase(Logger logger) {
        this.logger = logger;
        this.loggerName = logger.getName();
    }

    protected void doLog(Level level, String str, Object[] objArr, Class cls, Throwable th) {
        LogRecord logRecord = new LogRecord(level, str);
        if (objArr != null) {
            logRecord.setParameters(objArr);
        }
        inferCaller(cls, logRecord);
        logRecord.setThrown(th);
        logRecord.setLoggerName(this.loggerName);
        logRecord.setResourceBundle(this.logger.getResourceBundle());
        this.logger.log(logRecord);
    }

    private void inferCaller(Class cls, LogRecord logRecord) {
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        StackTraceElement stackTraceElement = null;
        String name = cls.getName();
        String name2 = LogWrapperBase.class.getName();
        int i2 = 0;
        while (i2 < stackTrace.length) {
            stackTraceElement = stackTrace[i2];
            String className = stackTraceElement.getClassName();
            if (!className.equals(name) && !className.equals(name2)) {
                break;
            } else {
                i2++;
            }
        }
        if (i2 < stackTrace.length) {
            logRecord.setSourceClassName(stackTraceElement.getClassName());
            logRecord.setSourceMethodName(stackTraceElement.getMethodName());
        }
    }

    protected void doLog(Level level, String str, Class cls, Throwable th) {
        doLog(level, str, null, cls, th);
    }
}
