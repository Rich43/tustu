package sun.java2d.pipe.hw;

import java.awt.image.VolatileImage;

/* loaded from: rt.jar:sun/java2d/pipe/hw/AccelGraphicsConfig.class */
public interface AccelGraphicsConfig extends BufferedContextProvider {
    VolatileImage createCompatibleVolatileImage(int i2, int i3, int i4, int i5);

    ContextCapabilities getContextCapabilities();

    void addDeviceEventListener(AccelDeviceEventListener accelDeviceEventListener);

    void removeDeviceEventListener(AccelDeviceEventListener accelDeviceEventListener);
}
