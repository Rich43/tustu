package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.utils.SecuritySupport;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/DOMMessageFormatter.class */
public class DOMMessageFormatter {
    public static final String DOM_DOMAIN = "http://www.w3.org/dom/DOMTR";
    public static final String XML_DOMAIN = "http://www.w3.org/TR/1998/REC-xml-19980210";
    public static final String SERIALIZER_DOMAIN = "http://apache.org/xml/serializer";
    private static ResourceBundle domResourceBundle = null;
    private static ResourceBundle xmlResourceBundle = null;
    private static ResourceBundle serResourceBundle = null;
    private static Locale locale = null;

    DOMMessageFormatter() {
        locale = Locale.getDefault();
    }

    public static String formatMessage(String domain, String key, Object[] arguments) throws MissingResourceException {
        ResourceBundle resourceBundle = getResourceBundle(domain);
        if (resourceBundle == null) {
            init();
            resourceBundle = getResourceBundle(domain);
            if (resourceBundle == null) {
                throw new MissingResourceException("Unknown domain" + domain, null, key);
            }
        }
        try {
            String msg = key + ": " + resourceBundle.getString(key);
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

    static ResourceBundle getResourceBundle(String domain) {
        if (domain == DOM_DOMAIN || domain.equals(DOM_DOMAIN)) {
            return domResourceBundle;
        }
        if (domain == "http://www.w3.org/TR/1998/REC-xml-19980210" || domain.equals("http://www.w3.org/TR/1998/REC-xml-19980210")) {
            return xmlResourceBundle;
        }
        if (domain == SERIALIZER_DOMAIN || domain.equals(SERIALIZER_DOMAIN)) {
            return serResourceBundle;
        }
        return null;
    }

    public static void init() {
        if (locale != null) {
            domResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.DOMMessages", locale);
            serResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSerializerMessages", locale);
            xmlResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLMessages", locale);
        } else {
            domResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.DOMMessages");
            serResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLSerializerMessages");
            xmlResourceBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xerces.internal.impl.msg.XMLMessages");
        }
    }

    public static void setLocale(Locale dlocale) {
        locale = dlocale;
    }
}
