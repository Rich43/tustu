package com.sun.org.apache.xml.internal.serializer.utils;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import java.text.MessageFormat;
import java.util.ListResourceBundle;
import java.util.Locale;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/serializer/utils/Messages.class */
public final class Messages {
    private final Locale m_locale = Locale.getDefault();
    private ListResourceBundle m_resourceBundle;
    private String m_resourceBundleName;

    Messages(String resourceBundle) {
        this.m_resourceBundleName = resourceBundle;
    }

    private Locale getLocale() {
        return this.m_locale;
    }

    public final String createMessage(String msgKey, Object[] args) {
        if (this.m_resourceBundle == null) {
            this.m_resourceBundle = SecuritySupport.getResourceBundle(this.m_resourceBundleName);
        }
        if (this.m_resourceBundle != null) {
            return createMsg(this.m_resourceBundle, msgKey, args);
        }
        return "Could not load the resource bundles: " + this.m_resourceBundleName;
    }

    private final String createMsg(ListResourceBundle fResourceBundle, String msgKey, Object[] args) {
        String fmsg = null;
        boolean throwex = false;
        String msg = null;
        if (msgKey != null) {
            msg = fResourceBundle.getString(msgKey);
        } else {
            msgKey = "";
        }
        if (msg == null) {
            throwex = true;
            try {
                MessageFormat.format(MsgKey.BAD_MSGKEY, msgKey, this.m_resourceBundleName);
            } catch (Exception e2) {
                String str = "The message key '" + msgKey + "' is not in the message class '" + this.m_resourceBundleName + PdfOps.SINGLE_QUOTE_TOKEN;
            }
        } else if (args != null) {
            try {
                int n2 = args.length;
                for (int i2 = 0; i2 < n2; i2++) {
                    if (null == args[i2]) {
                        args[i2] = "";
                    }
                }
                fmsg = MessageFormat.format(msg, args);
            } catch (Exception e3) {
                throwex = true;
                try {
                    fmsg = MessageFormat.format(MsgKey.BAD_MSGFORMAT, msgKey, this.m_resourceBundleName) + " " + msg;
                } catch (Exception e4) {
                    fmsg = "The format of message '" + msgKey + "' in message class '" + this.m_resourceBundleName + "' failed.";
                }
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
