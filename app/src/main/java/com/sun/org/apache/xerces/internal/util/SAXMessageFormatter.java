package com.sun.org.apache.xerces.internal.util;

import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/util/SAXMessageFormatter.class */
public class SAXMessageFormatter {
    public static String formatMessage(Locale locale, String key, Object[] arguments) throws MissingResourceException {
        ResourceBundle resourceBundle;
        if (locale != null) {
            resourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.SAXMessages", locale);
        } else {
            resourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.SAXMessages");
        }
        try {
            String msg = resourceBundle.getString(key);
            if (arguments != null) {
                try {
                    msg = MessageFormat.format(msg, arguments);
                } catch (Exception e2) {
                    msg = resourceBundle.getString("FormatFailed") + " " + resourceBundle.getString(key);
                }
            }
            if (msg == null) {
                msg = key;
                if (arguments.length > 0) {
                    StringBuffer str = new StringBuffer(msg);
                    str.append('?');
                    for (int i2 = 0; i2 < arguments.length; i2++) {
                        if (i2 > 0) {
                            str.append('&');
                        }
                        str.append(String.valueOf(arguments[i2]));
                    }
                }
            }
            return msg;
        } catch (MissingResourceException e3) {
            throw new MissingResourceException(key, resourceBundle.getString("BadMessageKey"), key);
        }
    }
}
