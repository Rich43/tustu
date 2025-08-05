package java.awt;

import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/awt/MouseInfo.class */
public class MouseInfo {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MouseInfo.class.desiredAssertionStatus();
    }

    private MouseInfo() {
    }

    public static PointerInfo getPointerInfo() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.AWT.WATCH_MOUSE_PERMISSION);
        }
        Point point = new Point(0, 0);
        int iFillPointWithCoords = Toolkit.getDefaultToolkit().getMouseInfoPeer().fillPointWithCoords(point);
        GraphicsDevice[] screenDevices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        PointerInfo pointerInfo = null;
        if (areScreenDevicesIndependent(screenDevices)) {
            pointerInfo = new PointerInfo(screenDevices[iFillPointWithCoords], point);
        } else {
            for (int i2 = 0; i2 < screenDevices.length; i2++) {
                if (screenDevices[i2].getDefaultConfiguration().getBounds().contains(point)) {
                    pointerInfo = new PointerInfo(screenDevices[i2], point);
                }
            }
        }
        return pointerInfo;
    }

    private static boolean areScreenDevicesIndependent(GraphicsDevice[] graphicsDeviceArr) {
        for (GraphicsDevice graphicsDevice : graphicsDeviceArr) {
            Rectangle bounds = graphicsDevice.getDefaultConfiguration().getBounds();
            if (bounds.f12372x != 0 || bounds.f12373y != 0) {
                return false;
            }
        }
        return true;
    }

    public static int getNumberOfButtons() throws HeadlessException {
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        Object desktopProperty = Toolkit.getDefaultToolkit().getDesktopProperty("awt.mouse.numButtons");
        if (desktopProperty instanceof Integer) {
            return ((Integer) desktopProperty).intValue();
        }
        if ($assertionsDisabled) {
            return 0;
        }
        throw new AssertionError((Object) "awt.mouse.numButtons is not an integer property");
    }
}
