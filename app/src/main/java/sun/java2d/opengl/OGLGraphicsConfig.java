package sun.java2d.opengl;

import sun.awt.image.SurfaceManager;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.hw.AccelGraphicsConfig;

/* loaded from: rt.jar:sun/java2d/opengl/OGLGraphicsConfig.class */
interface OGLGraphicsConfig extends AccelGraphicsConfig, SurfaceManager.ProxiedGraphicsConfig {
    @Override // sun.java2d.pipe.hw.BufferedContextProvider
    OGLContext getContext();

    long getNativeConfigInfo();

    boolean isCapPresent(int i2);

    SurfaceData createManagedSurface(int i2, int i3, int i4);
}
