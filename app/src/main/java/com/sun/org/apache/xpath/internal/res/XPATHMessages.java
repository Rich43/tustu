package com.sun.org.apache.xpath.internal.res;

import com.sun.org.apache.bcel.internal.util.SecuritySupport;
import com.sun.org.apache.xml.internal.res.XMLMessages;
import java.text.MessageFormat;
import java.util.ListResourceBundle;

/* loaded from: rt.jar:com/sun/org/apache/xpath/internal/res/XPATHMessages.class */
public class XPATHMessages extends XMLMessages {
    private static ListResourceBundle XPATHBundle = null;
    private static final String XPATH_ERROR_RESOURCES = "com.sun.org.apache.xpath.internal.res.XPATHErrorResources";

    public static final String createXPATHMessage(String msgKey, Object[] args) {
        if (XPATHBundle == null) {
            XPATHBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xpath.internal.res.XPATHErrorResources");
        }
        if (XPATHBundle != null) {
            return createXPATHMsg(XPATHBundle, msgKey, args);
        }
        return "Could not load any resource bundles.";
    }

    public static final String createXPATHWarning(String msgKey, Object[] args) {
        if (XPATHBundle == null) {
            XPATHBundle = SecuritySupport.getResourceBundle("com.sun.org.apache.xpath.internal.res.XPATHErrorResources");
        }
        if (XPATHBundle != null) {
            return createXPATHMsg(XPATHBundle, msgKey, args);
        }
        return "Could not load any resource bundles.";
    }

    public static final String createXPATHMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args) {
        String fmsg;
        boolean throwex = false;
        String msg = null;
        if (msgKey != null) {
            msg = fResourceBundle.getString(msgKey);
        }
        if (msg == null) {
            msg = fResourceBundle.getString("BAD_CODE");
            throwex = true;
        }
        if (args != null) {
            try {
                int n2 = args.length;
                for (int i2 = 0; i2 < n2; i2++) {
                    if (null == args[i2]) {
                        args[i2] = "";
                    }
                }
                fmsg = MessageFormat.format(msg, args);
            } catch (Exception e2) {
                String fmsg2 = fResourceBundle.getString("FORMAT_FAILED");
                fmsg = fmsg2 + " " + msg;
            }
        } else {
            fmsg = msg;
        }
        if (throwex) {
            throw new RuntimeException(fmsg);
        }
        return fmsg;
    }
}
