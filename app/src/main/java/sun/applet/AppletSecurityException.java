package sun.applet;

/* loaded from: rt.jar:sun/applet/AppletSecurityException.class */
public class AppletSecurityException extends SecurityException {
    private String key;
    private Object[] msgobj;
    private static AppletMessageHandler amh = new AppletMessageHandler("appletsecurityexception");

    public AppletSecurityException(String str) {
        super(str);
        this.key = null;
        this.msgobj = null;
        this.key = str;
    }

    public AppletSecurityException(String str, String str2) {
        this(str);
        this.msgobj = new Object[1];
        this.msgobj[0] = str2;
    }

    public AppletSecurityException(String str, String str2, String str3) {
        this(str);
        this.msgobj = new Object[2];
        this.msgobj[0] = str2;
        this.msgobj[1] = str3;
    }

    @Override // java.lang.Throwable
    public String getLocalizedMessage() {
        if (this.msgobj != null) {
            return amh.getMessage(this.key, this.msgobj);
        }
        return amh.getMessage(this.key);
    }
}
