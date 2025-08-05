package sun.awt;

import java.net.URL;

/* loaded from: rt.jar:sun/awt/DesktopBrowse.class */
public abstract class DesktopBrowse {
    private static volatile DesktopBrowse mInstance;

    public abstract void browse(URL url);

    public static void setInstance(DesktopBrowse desktopBrowse) {
        if (mInstance != null) {
            throw new IllegalStateException("DesktopBrowse instance has already been set.");
        }
        mInstance = desktopBrowse;
    }

    public static DesktopBrowse getInstance() {
        return mInstance;
    }
}
