package com.sun.corba.se.impl.orbutil;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/CorbaResourceUtil.class */
public class CorbaResourceUtil {
    private static boolean resourcesInitialized = false;
    private static ResourceBundle resources;

    public static String getString(String str) {
        if (!resourcesInitialized) {
            initResources();
        }
        try {
            return resources.getString(str);
        } catch (MissingResourceException e2) {
            return null;
        }
    }

    public static String getText(String str) {
        String string = getString(str);
        if (string == null) {
            string = "no text found: \"" + str + PdfOps.DOUBLE_QUOTE__TOKEN;
        }
        return string;
    }

    public static String getText(String str, int i2) {
        return getText(str, Integer.toString(i2), null, null);
    }

    public static String getText(String str, String str2) {
        return getText(str, str2, null, null);
    }

    public static String getText(String str, String str2, String str3) {
        return getText(str, str2, str3, null);
    }

    public static String getText(String str, String str2, String str3, String str4) {
        String string = getString(str);
        if (string == null) {
            string = "no text found: key = \"" + str + "\", arguments = \"{0}\", \"{1}\", \"{2}\"";
        }
        String[] strArr = new String[3];
        strArr[0] = str2 != null ? str2.toString() : FXMLLoader.NULL_KEYWORD;
        strArr[1] = str3 != null ? str3.toString() : FXMLLoader.NULL_KEYWORD;
        strArr[2] = str4 != null ? str4.toString() : FXMLLoader.NULL_KEYWORD;
        return MessageFormat.format(string, strArr);
    }

    private static void initResources() {
        try {
            resources = ResourceBundle.getBundle("com.sun.corba.se.impl.orbutil.resources.sunorb");
            resourcesInitialized = true;
        } catch (MissingResourceException e2) {
            throw new Error("fatal: missing resource bundle: " + e2.getClassName());
        }
    }
}
