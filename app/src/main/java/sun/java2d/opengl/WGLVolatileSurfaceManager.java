package sun.java2d.opengl;

import java.awt.BufferCapabilities;
import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.image.ColorModel;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.VolatileSurfaceManager;
import sun.awt.windows.WComponentPeer;
import sun.java2d.SurfaceData;
import sun.java2d.opengl.WGLSurfaceData;
import sun.java2d.pipe.hw.ExtendedBufferCapabilities;

/* loaded from: rt.jar:sun/java2d/opengl/WGLVolatileSurfaceManager.class */
public class WGLVolatileSurfaceManager extends VolatileSurfaceManager {
    private final boolean accelerationEnabled;

    public WGLVolatileSurfaceManager(SunVolatileImage sunVolatileImage, Object obj) {
        super(sunVolatileImage, obj);
        this.accelerationEnabled = ((WGLGraphicsConfig) sunVolatileImage.getGraphicsConfig()).isCapPresent(12) && sunVolatileImage.getTransparency() != 2;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected boolean isAccelerationEnabled() {
        return this.accelerationEnabled;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected SurfaceData initAcceleratedSurface() {
        WGLSurfaceData.WGLOffScreenSurfaceData wGLOffScreenSurfaceDataCreateData;
        Component component = this.vImg.getComponent();
        WComponentPeer wComponentPeer = component != null ? (WComponentPeer) component.getPeer() : null;
        try {
            boolean z2 = false;
            boolean zBooleanValue = false;
            if (this.context instanceof Boolean) {
                zBooleanValue = ((Boolean) this.context).booleanValue();
                if (zBooleanValue) {
                    BufferCapabilities backBufferCaps = wComponentPeer.getBackBufferCaps();
                    if (backBufferCaps instanceof ExtendedBufferCapabilities) {
                        ExtendedBufferCapabilities extendedBufferCapabilities = (ExtendedBufferCapabilities) backBufferCaps;
                        if (extendedBufferCapabilities.getVSync() == ExtendedBufferCapabilities.VSyncType.VSYNC_ON && extendedBufferCapabilities.getFlipContents() == BufferCapabilities.FlipContents.COPIED) {
                            z2 = true;
                            zBooleanValue = false;
                        }
                    }
                }
            }
            if (zBooleanValue) {
                wGLOffScreenSurfaceDataCreateData = WGLSurfaceData.createData(wComponentPeer, this.vImg, 4);
            } else {
                WGLGraphicsConfig wGLGraphicsConfig = (WGLGraphicsConfig) this.vImg.getGraphicsConfig();
                ColorModel colorModel = wGLGraphicsConfig.getColorModel(this.vImg.getTransparency());
                int forcedAccelSurfaceType = this.vImg.getForcedAccelSurfaceType();
                if (forcedAccelSurfaceType == 0) {
                    forcedAccelSurfaceType = 5;
                }
                if (z2) {
                    wGLOffScreenSurfaceDataCreateData = WGLSurfaceData.createData(wComponentPeer, this.vImg, forcedAccelSurfaceType);
                } else {
                    wGLOffScreenSurfaceDataCreateData = WGLSurfaceData.createData(wGLGraphicsConfig, this.vImg.getWidth(), this.vImg.getHeight(), colorModel, this.vImg, forcedAccelSurfaceType);
                }
            }
        } catch (NullPointerException e2) {
            wGLOffScreenSurfaceDataCreateData = null;
        } catch (OutOfMemoryError e3) {
            wGLOffScreenSurfaceDataCreateData = null;
        }
        return wGLOffScreenSurfaceDataCreateData;
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    protected boolean isConfigValid(GraphicsConfiguration graphicsConfiguration) {
        return graphicsConfiguration == null || ((graphicsConfiguration instanceof WGLGraphicsConfig) && graphicsConfiguration == this.vImg.getGraphicsConfig());
    }

    @Override // sun.awt.image.VolatileSurfaceManager
    public void initContents() {
        if (this.vImg.getForcedAccelSurfaceType() != 3) {
            super.initContents();
        }
    }
}
