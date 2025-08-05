package sun.awt.image;

import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/BufImgVolatileSurfaceManager.class */
public class BufImgVolatileSurfaceManager extends VolatileSurfaceManager {
    public BufImgVolatileSurfaceManager(SunVolatileImage sunVolatileImage, Object obj) {
        super(sunVolatileImage, obj);
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected boolean isAccelerationEnabled() {
        return false;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected SurfaceData initAcceleratedSurface() {
        return null;
    }
}
