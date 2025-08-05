package com.sun.org.apache.xerces.internal.xinclude;

import com.sun.org.apache.xerces.internal.util.MessageFormatter;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/xinclude/XIncludeMessageFormatter.class */
public class XIncludeMessageFormatter implements MessageFormatter {
    public static final String XINCLUDE_DOMAIN = "http://www.w3.org/TR/xinclude";
    private Locale fLocale = null;
    private ResourceBundle fResourceBundle = null;

    @Override // com.sun.org.apache.xerces.internal.util.MessageFormatter
    public String formatMessage(Locale locale, String key, Object[] arguments) throws MissingResourceException {
        if (this.fResourceBundle == null || locale != this.fLocale) {
            if (locale != null) {
                this.fResourceBundle = com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages", locale);
                this.fLocale = locale;
            }
            if (this.fResourceBundle == null) {
                this.fResourceBundle = com.sun.org.apache.xerces.internal.utils.SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages");
            }
        }
        String msg = this.fResourceBundle.getString(key);
        if (arguments != null) {
            try {
                msg = MessageFormat.format(msg, arguments);
            } catch (Exception e2) {
                msg = this.fResourceBundle.getString("FormatFailed") + " " + this.fResourceBundle.getString(key);
            }
        }
        if (msg == null) {
            throw new MissingResourceException(this.fResourceBundle.getString("BadMessageKey"), "com.sun.org.apache.xerces.internal.impl.msg.XIncludeMessages", key);
        }
        return msg;
    }
}
