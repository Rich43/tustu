package sun.java2d;

import sun.awt.image.SunVolatileImage;
import sun.awt.image.VolatileSurfaceManager;

/* loaded from: rt.jar:sun/java2d/SurfaceManagerFactory.class */
public abstract class SurfaceManagerFactory {
    private static SurfaceManagerFactory instance;

    public abstract VolatileSurfaceManager createVolatileManager(SunVolatileImage sunVolatileImage, Object obj);

    public static synchronized SurfaceManagerFactory getInstance() {
        if (instance == null) {
            throw new IllegalStateException("No SurfaceManagerFactory set.");
        }
        return instance;
    }

    public static synchronized void setInstance(SurfaceManagerFactory surfaceManagerFactory) {
        if (surfaceManagerFactory == null) {
            throw new IllegalArgumentException("factory must be non-null");
        }
        if (instance != null) {
            throw new IllegalStateException("The surface manager factory is already initialized");
        }
        instance = surfaceManagerFactory;
    }
}
