package com.intel.bluetooth;

import com.intel.bluetooth.DebugLog;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/* loaded from: bluecove-2.1.1.jar:com/intel/bluetooth/DebugLog4jAppender.class */
class DebugLog4jAppender implements DebugLog.LoggerAppenderExt {
    private static final String FQCN;
    private Logger logger = Logger.getLogger("com.intel.bluetooth");
    static Class class$com$intel$bluetooth$DebugLog;

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
    }

    static {
        Class clsClass$;
        if (class$com$intel$bluetooth$DebugLog == null) {
            clsClass$ = class$("com.intel.bluetooth.DebugLog");
            class$com$intel$bluetooth$DebugLog = clsClass$;
        } else {
            clsClass$ = class$com$intel$bluetooth$DebugLog;
        }
        FQCN = clsClass$.getName();
    }

    DebugLog4jAppender() {
    }

    @Override // com.intel.bluetooth.DebugLog.LoggerAppender
    public void appendLog(int level, String message, Throwable throwable) {
        switch (level) {
            case 1:
                this.logger.log(FQCN, Level.DEBUG, message, throwable);
                break;
            case 4:
                this.logger.log(FQCN, Level.ERROR, message, throwable);
                break;
        }
    }

    @Override // com.intel.bluetooth.DebugLog.LoggerAppenderExt
    public boolean isLogEnabled(int level) {
        switch (level) {
            case 1:
                return this.logger.isDebugEnabled();
            case 4:
                return true;
            default:
                return false;
        }
    }
}
