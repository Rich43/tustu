package sun.java2d.opengl;

import java.awt.Component;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.ColorModel;
import sun.awt.SunToolkit;
import sun.awt.windows.WComponentPeer;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/java2d/opengl/WGLSurfaceData.class */
public abstract class WGLSurfaceData extends OGLSurfaceData {
    protected WComponentPeer peer;
    private WGLGraphicsConfig graphicsConfig;

    private native void initOps(OGLGraphicsConfig oGLGraphicsConfig, long j2, WComponentPeer wComponentPeer, long j3);

    public static native boolean updateWindowAccelImpl(long j2, WComponentPeer wComponentPeer, int i2, int i3);

    protected WGLSurfaceData(WComponentPeer wComponentPeer, WGLGraphicsConfig wGLGraphicsConfig, ColorModel colorModel, int i2) {
        super(wGLGraphicsConfig, colorModel, i2);
        this.peer = wComponentPeer;
        this.graphicsConfig = wGLGraphicsConfig;
        initOps(wGLGraphicsConfig, wGLGraphicsConfig.getNativeConfigInfo(), wComponentPeer, wComponentPeer != null ? wComponentPeer.getHWnd() : 0L);
    }

    @Override // sun.java2d.SurfaceData
    public GraphicsConfiguration getDeviceConfiguration() {
        return this.graphicsConfig;
    }

    public static WGLWindowSurfaceData createData(WComponentPeer wComponentPeer) {
        if (!wComponentPeer.isAccelCapable() || !SunToolkit.isContainingTopLevelOpaque((Component) wComponentPeer.getTarget())) {
            return null;
        }
        return new WGLWindowSurfaceData(wComponentPeer, getGC(wComponentPeer));
    }

    public static WGLOffScreenSurfaceData createData(WComponentPeer wComponentPeer, Image image, int i2) {
        if (!wComponentPeer.isAccelCapable() || !SunToolkit.isContainingTopLevelOpaque((Component) wComponentPeer.getTarget())) {
            return null;
        }
        WGLGraphicsConfig gc = getGC(wComponentPeer);
        Rectangle bounds = wComponentPeer.getBounds();
        if (i2 == 4) {
            return new WGLOffScreenSurfaceData(wComponentPeer, gc, bounds.width, bounds.height, image, wComponentPeer.getColorModel(), i2);
        }
        return new WGLVSyncOffScreenSurfaceData(wComponentPeer, gc, bounds.width, bounds.height, image, wComponentPeer.getColorModel(), i2);
    }

    public static WGLOffScreenSurfaceData createData(WGLGraphicsConfig wGLGraphicsConfig, int i2, int i3, ColorModel colorModel, Image image, int i4) {
        return new WGLOffScreenSurfaceData(null, wGLGraphicsConfig, i2, i3, image, colorModel, i4);
    }

    public static WGLGraphicsConfig getGC(WComponentPeer wComponentPeer) {
        if (wComponentPeer != null) {
            return (WGLGraphicsConfig) wComponentPeer.getGraphicsConfiguration();
        }
        return (WGLGraphicsConfig) GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLSurfaceData$WGLWindowSurfaceData.class */
    public static class WGLWindowSurfaceData extends WGLSurfaceData {
        public WGLWindowSurfaceData(WComponentPeer wComponentPeer, WGLGraphicsConfig wGLGraphicsConfig) {
            super(wComponentPeer, wGLGraphicsConfig, wComponentPeer.getColorModel(), 1);
        }

        @Override // sun.java2d.SurfaceData
        public SurfaceData getReplacement() {
            return this.peer.getSurfaceData();
        }

        @Override // sun.java2d.SurfaceData
        public Rectangle getBounds() {
            Rectangle bounds = this.peer.getBounds();
            bounds.f12373y = 0;
            bounds.f12372x = 0;
            return bounds;
        }

        @Override // sun.java2d.SurfaceData
        public Object getDestination() {
            return this.peer.getTarget();
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLSurfaceData$WGLVSyncOffScreenSurfaceData.class */
    public static class WGLVSyncOffScreenSurfaceData extends WGLOffScreenSurfaceData {
        private WGLOffScreenSurfaceData flipSurface;

        public WGLVSyncOffScreenSurfaceData(WComponentPeer wComponentPeer, WGLGraphicsConfig wGLGraphicsConfig, int i2, int i3, Image image, ColorModel colorModel, int i4) {
            super(wComponentPeer, wGLGraphicsConfig, i2, i3, image, colorModel, i4);
            this.flipSurface = WGLSurfaceData.createData(wComponentPeer, image, 4);
        }

        public SurfaceData getFlipSurface() {
            return this.flipSurface;
        }

        @Override // sun.java2d.opengl.OGLSurfaceData, sun.java2d.SurfaceData
        public void flush() {
            this.flipSurface.flush();
            super.flush();
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/WGLSurfaceData$WGLOffScreenSurfaceData.class */
    public static class WGLOffScreenSurfaceData extends WGLSurfaceData {
        private Image offscreenImage;
        private int width;
        private int height;

        public WGLOffScreenSurfaceData(WComponentPeer wComponentPeer, WGLGraphicsConfig wGLGraphicsConfig, int i2, int i3, Image image, ColorModel colorModel, int i4) {
            super(wComponentPeer, wGLGraphicsConfig, colorModel, i4);
            this.width = i2;
            this.height = i3;
            this.offscreenImage = image;
            initSurface(i2, i3);
        }

        @Override // sun.java2d.SurfaceData
        public SurfaceData getReplacement() {
            return restoreContents(this.offscreenImage);
        }

        @Override // sun.java2d.SurfaceData
        public Rectangle getBounds() {
            if (this.type == 4) {
                Rectangle bounds = this.peer.getBounds();
                bounds.f12373y = 0;
                bounds.f12372x = 0;
                return bounds;
            }
            return new Rectangle(this.width, this.height);
        }

        @Override // sun.java2d.SurfaceData
        public Object getDestination() {
            return this.offscreenImage;
        }
    }
}
