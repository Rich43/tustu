package sun.applet;

/* loaded from: rt.jar:sun/applet/AppletIllegalArgumentException.class */
public class AppletIllegalArgumentException extends IllegalArgumentException {
    private String key;
    private static AppletMessageHandler amh = new AppletMessageHandler("appletillegalargumentexception");

    public AppletIllegalArgumentException(String str) {
        super(str);
        this.key = null;
        this.key = str;
    }

    @Override // java.lang.Throwable
    public String getLocalizedMessage() {
        return amh.getMessage(this.key);
    }
}
