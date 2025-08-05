package sun.java2d;

import java.awt.GraphicsConfiguration;
import sun.awt.image.BufImgVolatileSurfaceManager;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.VolatileSurfaceManager;
import sun.java2d.d3d.D3DGraphicsConfig;
import sun.java2d.d3d.D3DVolatileSurfaceManager;
import sun.java2d.opengl.WGLGraphicsConfig;
import sun.java2d.opengl.WGLVolatileSurfaceManager;

/* loaded from: rt.jar:sun/java2d/WindowsSurfaceManagerFactory.class */
public class WindowsSurfaceManagerFactory extends SurfaceManagerFactory {
    @Override // sun.java2d.SurfaceManagerFactory
    public VolatileSurfaceManager createVolatileManager(SunVolatileImage sunVolatileImage, Object obj) {
        GraphicsConfiguration graphicsConfig = sunVolatileImage.getGraphicsConfig();
        if (graphicsConfig instanceof D3DGraphicsConfig) {
            return new D3DVolatileSurfaceManager(sunVolatileImage, obj);
        }
        if (graphicsConfig instanceof WGLGraphicsConfig) {
            return new WGLVolatileSurfaceManager(sunVolatileImage, obj);
        }
        return new BufImgVolatileSurfaceManager(sunVolatileImage, obj);
    }
}
