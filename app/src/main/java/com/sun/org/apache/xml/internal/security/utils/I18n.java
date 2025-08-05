package com.sun.org.apache.xml.internal.security.utils;

import com.sun.org.apache.xml.internal.security.Init;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/I18n.class */
public class I18n {
    public static final String NOT_INITIALIZED_MSG = "You must initialize the xml-security library correctly before you use it. Call the static method \"com.sun.org.apache.xml.internal.security.Init.init();\" to do that before you use any functionality from that library.";
    private static ResourceBundle resourceBundle;
    private static boolean alreadyInitialized = false;

    private I18n() {
    }

    public static String translate(String str, Object[] objArr) {
        return getExceptionMessage(str, objArr);
    }

    public static String translate(String str) {
        return getExceptionMessage(str);
    }

    public static String getExceptionMessage(String str) {
        try {
            return resourceBundle.getString(str);
        } catch (Throwable th) {
            if (Init.isInitialized()) {
                return "No message with ID \"" + str + "\" found in resource bundle \"" + Constants.exceptionMessagesResourceBundleBase + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            return NOT_INITIALIZED_MSG;
        }
    }

    public static String getExceptionMessage(String str, Exception exc) {
        try {
            return MessageFormat.format(resourceBundle.getString(str), exc.getMessage());
        } catch (Throwable th) {
            if (Init.isInitialized()) {
                return "No message with ID \"" + str + "\" found in resource bundle \"" + Constants.exceptionMessagesResourceBundleBase + "\". Original Exception was a " + exc.getClass().getName() + " and message " + exc.getMessage();
            }
            return NOT_INITIALIZED_MSG;
        }
    }

    public static String getExceptionMessage(String str, Object[] objArr) {
        try {
            return MessageFormat.format(resourceBundle.getString(str), objArr);
        } catch (Throwable th) {
            if (Init.isInitialized()) {
                return "No message with ID \"" + str + "\" found in resource bundle \"" + Constants.exceptionMessagesResourceBundleBase + PdfOps.DOUBLE_QUOTE__TOKEN;
            }
            return NOT_INITIALIZED_MSG;
        }
    }

    public static synchronized void init(String str, String str2) {
        if (alreadyInitialized) {
            return;
        }
        resourceBundle = ResourceBundle.getBundle(Constants.exceptionMessagesResourceBundleBase, new Locale(str, str2));
        alreadyInitialized = true;
    }

    public static synchronized void init(ResourceBundle resourceBundle2) {
        if (alreadyInitialized) {
            return;
        }
        resourceBundle = resourceBundle2;
        alreadyInitialized = true;
    }
}
