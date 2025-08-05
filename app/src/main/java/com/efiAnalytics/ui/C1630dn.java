package com.efiAnalytics.ui;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Rectangle;

/* renamed from: com.efiAnalytics.ui.dn, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:com/efiAnalytics/ui/dn.class */
public class C1630dn {
    public static Rectangle a(int i2, int i3) {
        Rectangle bounds = null;
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice graphicsDevice : localGraphicsEnvironment.getScreenDevices()) {
            for (GraphicsConfiguration graphicsConfiguration : graphicsDevice.getConfigurations()) {
                Rectangle bounds2 = graphicsConfiguration.getBounds();
                if (bounds2.contains(i2, i3)) {
                    bounds = bounds2;
                }
            }
        }
        if (bounds == null) {
            bounds = localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
        }
        return bounds;
    }

    public static String[] a() {
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        String[] strArr = new String[screenDevices.length];
        for (int i2 = 0; i2 < screenDevices.length; i2++) {
            strArr[i2] = screenDevices[i2].getIDstring();
        }
        return strArr;
    }

    public static Rectangle a(int i2) {
        GraphicsEnvironment localGraphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration[] configurations = localGraphicsEnvironment.getScreenDevices()[i2].getConfigurations();
        return 0 < configurations.length ? configurations[0].getBounds() : localGraphicsEnvironment.getDefaultScreenDevice().getDefaultConfiguration().getBounds();
    }

    public static int b() throws HeadlessException {
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (screenDevices != null) {
            return screenDevices.length;
        }
        return 1;
    }
}
