package sun.applet;

import java.io.IOException;

/* loaded from: rt.jar:sun/applet/AppletIOException.class */
public class AppletIOException extends IOException {
    private String key;
    private Object msgobj;
    private static AppletMessageHandler amh = new AppletMessageHandler("appletioexception");

    public AppletIOException(String str) {
        super(str);
        this.key = null;
        this.msgobj = null;
        this.key = str;
    }

    public AppletIOException(String str, Object obj) {
        this(str);
        this.msgobj = obj;
    }

    @Override // java.lang.Throwable
    public String getLocalizedMessage() {
        if (this.msgobj != null) {
            return amh.getMessage(this.key, this.msgobj);
        }
        return amh.getMessage(this.key);
    }
}
