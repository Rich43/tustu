package sun.java2d;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import sun.awt.Win32GraphicsConfig;
import sun.awt.windows.WComponentPeer;
import sun.java2d.d3d.D3DScreenUpdateManager;
import sun.java2d.windows.WindowsFlags;

/* loaded from: rt.jar:sun/java2d/ScreenUpdateManager.class */
public class ScreenUpdateManager {
    private static ScreenUpdateManager theInstance;

    protected ScreenUpdateManager() {
    }

    public synchronized Graphics2D createGraphics(SurfaceData surfaceData, WComponentPeer wComponentPeer, Color color, Color color2, Font font) {
        return new SunGraphics2D(surfaceData, color, color2, font);
    }

    public SurfaceData createScreenSurface(Win32GraphicsConfig win32GraphicsConfig, WComponentPeer wComponentPeer, int i2, boolean z2) {
        return win32GraphicsConfig.createSurfaceData(wComponentPeer, i2);
    }

    public void dropScreenSurface(SurfaceData surfaceData) {
    }

    public SurfaceData getReplacementScreenSurface(WComponentPeer wComponentPeer, SurfaceData surfaceData) {
        SurfaceData surfaceData2 = wComponentPeer.getSurfaceData();
        if (surfaceData2 == null || surfaceData2.isValid()) {
            return surfaceData2;
        }
        wComponentPeer.replaceSurfaceData();
        return wComponentPeer.getSurfaceData();
    }

    public static synchronized ScreenUpdateManager getInstance() {
        if (theInstance == null) {
            if (WindowsFlags.isD3DEnabled()) {
                theInstance = new D3DScreenUpdateManager();
            } else {
                theInstance = new ScreenUpdateManager();
            }
        }
        return theInstance;
    }
}
