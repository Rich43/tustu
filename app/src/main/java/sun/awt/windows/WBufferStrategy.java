package sun.awt.windows;

import java.awt.Component;
import java.awt.Image;

/* loaded from: rt.jar:sun/awt/windows/WBufferStrategy.class */
public final class WBufferStrategy {
    private static native void initIDs(Class<?> cls);

    public static native Image getDrawBuffer(Component component);

    static {
        initIDs(Component.class);
    }
}
