package java.util.logging;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:java/util/logging/Formatter.class */
public abstract class Formatter {
    public abstract String format(LogRecord logRecord);

    protected Formatter() {
    }

    public String getHead(Handler handler) {
        return "";
    }

    public String getTail(Handler handler) {
        return "";
    }

    public synchronized String formatMessage(LogRecord logRecord) {
        String message = logRecord.getMessage();
        ResourceBundle resourceBundle = logRecord.getResourceBundle();
        if (resourceBundle != null) {
            try {
                message = resourceBundle.getString(logRecord.getMessage());
            } catch (MissingResourceException e2) {
                message = logRecord.getMessage();
            }
        }
        try {
            Object[] parameters = logRecord.getParameters();
            if (parameters == null || parameters.length == 0) {
                return message;
            }
            if (message.indexOf("{0") >= 0 || message.indexOf("{1") >= 0 || message.indexOf("{2") >= 0 || message.indexOf("{3") >= 0) {
                return MessageFormat.format(message, parameters);
            }
            return message;
        } catch (Exception e3) {
            return message;
        }
    }
}
