package sun.applet;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;

/* loaded from: rt.jar:sun/applet/AppletMessageHandler.class */
class AppletMessageHandler {
    private static ResourceBundle rb;
    private String baseKey;

    static {
        try {
            rb = ResourceBundle.getBundle("sun.applet.resources.MsgAppletViewer");
        } catch (MissingResourceException e2) {
            System.out.println(e2.getMessage());
            System.exit(1);
        }
    }

    AppletMessageHandler(String str) {
        this.baseKey = null;
        this.baseKey = str;
    }

    String getMessage(String str) {
        return rb.getString(getQualifiedKey(str));
    }

    String getMessage(String str, Object obj) {
        MessageFormat messageFormat = new MessageFormat(rb.getString(getQualifiedKey(str)));
        Object[] objArr = new Object[1];
        if (obj == null) {
            obj = FXMLLoader.NULL_KEYWORD;
        }
        objArr[0] = obj;
        return messageFormat.format(objArr);
    }

    String getMessage(String str, Object obj, Object obj2) {
        MessageFormat messageFormat = new MessageFormat(rb.getString(getQualifiedKey(str)));
        Object[] objArr = new Object[2];
        if (obj == null) {
            obj = FXMLLoader.NULL_KEYWORD;
        }
        if (obj2 == null) {
            obj2 = FXMLLoader.NULL_KEYWORD;
        }
        objArr[0] = obj;
        objArr[1] = obj2;
        return messageFormat.format(objArr);
    }

    String getMessage(String str, Object obj, Object obj2, Object obj3) {
        MessageFormat messageFormat = new MessageFormat(rb.getString(getQualifiedKey(str)));
        Object[] objArr = new Object[3];
        if (obj == null) {
            obj = FXMLLoader.NULL_KEYWORD;
        }
        if (obj2 == null) {
            obj2 = FXMLLoader.NULL_KEYWORD;
        }
        if (obj3 == null) {
            obj3 = FXMLLoader.NULL_KEYWORD;
        }
        objArr[0] = obj;
        objArr[1] = obj2;
        objArr[2] = obj3;
        return messageFormat.format(objArr);
    }

    String getMessage(String str, Object[] objArr) {
        return new MessageFormat(rb.getString(getQualifiedKey(str))).format(objArr);
    }

    String getQualifiedKey(String str) {
        return this.baseKey + "." + str;
    }
}
