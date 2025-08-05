package sun.java2d.d3d;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.ImageCapabilities;
import java.awt.color.ColorSpace;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.VolatileImage;
import sun.awt.Win32GraphicsConfig;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.windows.WComponentPeer;
import sun.java2d.Surface;
import sun.java2d.SurfaceData;
import sun.java2d.pipe.hw.AccelDeviceEventListener;
import sun.java2d.pipe.hw.AccelDeviceEventNotifier;
import sun.java2d.pipe.hw.AccelGraphicsConfig;
import sun.java2d.pipe.hw.AccelSurface;
import sun.java2d.pipe.hw.AccelTypedVolatileImage;
import sun.java2d.pipe.hw.ContextCapabilities;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsConfig.class */
public class D3DGraphicsConfig extends Win32GraphicsConfig implements AccelGraphicsConfig {
    private static ImageCapabilities imageCaps = new D3DImageCaps();
    private BufferCapabilities bufferCaps;
    private D3DGraphicsDevice device;

    protected D3DGraphicsConfig(D3DGraphicsDevice d3DGraphicsDevice) {
        super(d3DGraphicsDevice, 0);
        this.device = d3DGraphicsDevice;
    }

    public SurfaceData createManagedSurface(int i2, int i3, int i4) {
        return D3DSurfaceData.createData(this, i2, i3, getColorModel(i4), null, 3);
    }

    @Override // sun.awt.Win32GraphicsConfig, sun.awt.DisplayChangedListener
    public synchronized void displayChanged() {
        super.displayChanged();
        D3DRenderQueue d3DRenderQueue = D3DRenderQueue.getInstance();
        d3DRenderQueue.lock();
        try {
            D3DContext.invalidateCurrentContext();
        } finally {
            d3DRenderQueue.unlock();
        }
    }

    @Override // sun.awt.Win32GraphicsConfig, java.awt.GraphicsConfiguration
    public ColorModel getColorModel(int i2) {
        switch (i2) {
            case 1:
                return new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
            case 2:
                return new DirectColorModel(25, 16711680, NormalizerImpl.CC_MASK, 255, 16777216);
            case 3:
                return new DirectColorModel(ColorSpace.getInstance(1000), 32, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, true, 3);
            default:
                return null;
        }
    }

    @Override // sun.awt.Win32GraphicsConfig
    public String toString() {
        return "D3DGraphicsConfig[dev=" + ((Object) this.screen) + ",pixfmt=" + this.visual + "]";
    }

    @Override // sun.awt.Win32GraphicsConfig
    public SurfaceData createSurfaceData(WComponentPeer wComponentPeer, int i2) {
        return super.createSurfaceData(wComponentPeer, i2);
    }

    @Override // sun.awt.Win32GraphicsConfig
    public void assertOperationSupported(Component component, int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        if (i2 < 2 || i2 > 4) {
            throw new AWTException("Only 2-4 buffers supported");
        }
        if (bufferCapabilities.getFlipContents() == BufferCapabilities.FlipContents.COPIED && i2 != 2) {
            throw new AWTException("FlipContents.COPIED is onlysupported for 2 buffers");
        }
    }

    @Override // sun.awt.Win32GraphicsConfig
    public VolatileImage createBackBuffer(WComponentPeer wComponentPeer) {
        Component component = (Component) wComponentPeer.getTarget();
        return new SunVolatileImage(component, Math.max(1, component.getWidth()), Math.max(1, component.getHeight()), Boolean.TRUE);
    }

    @Override // sun.awt.Win32GraphicsConfig
    public void flip(WComponentPeer wComponentPeer, Component component, VolatileImage volatileImage, int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents) {
        SurfaceData primarySurfaceData = SurfaceManager.getManager(volatileImage).getPrimarySurfaceData();
        if (primarySurfaceData instanceof D3DSurfaceData) {
            D3DSurfaceData.swapBuffers((D3DSurfaceData) primarySurfaceData, i2, i3, i4, i5);
        } else {
            Graphics graphics = wComponentPeer.getGraphics();
            try {
                graphics.drawImage(volatileImage, i2, i3, i4, i5, i2, i3, i4, i5, null);
                graphics.dispose();
            } catch (Throwable th) {
                graphics.dispose();
                throw th;
            }
        }
        if (flipContents == BufferCapabilities.FlipContents.BACKGROUND) {
            Graphics graphics2 = volatileImage.getGraphics();
            try {
                graphics2.setColor(component.getBackground());
                graphics2.fillRect(0, 0, volatileImage.getWidth(), volatileImage.getHeight());
                graphics2.dispose();
            } catch (Throwable th2) {
                graphics2.dispose();
                throw th2;
            }
        }
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsConfig$D3DBufferCaps.class */
    private static class D3DBufferCaps extends BufferCapabilities {
        public D3DBufferCaps() {
            super(D3DGraphicsConfig.imageCaps, D3DGraphicsConfig.imageCaps, BufferCapabilities.FlipContents.UNDEFINED);
        }

        @Override // java.awt.BufferCapabilities
        public boolean isMultiBufferAvailable() {
            return true;
        }
    }

    @Override // java.awt.GraphicsConfiguration
    public BufferCapabilities getBufferCapabilities() {
        if (this.bufferCaps == null) {
            this.bufferCaps = new D3DBufferCaps();
        }
        return this.bufferCaps;
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DGraphicsConfig$D3DImageCaps.class */
    private static class D3DImageCaps extends ImageCapabilities {
        private D3DImageCaps() {
            super(true);
        }

        @Override // java.awt.ImageCapabilities
        public boolean isTrueVolatile() {
            return true;
        }
    }

    @Override // java.awt.GraphicsConfiguration
    public ImageCapabilities getImageCapabilities() {
        return imageCaps;
    }

    D3DGraphicsDevice getD3DDevice() {
        return this.device;
    }

    @Override // sun.java2d.pipe.hw.BufferedContextProvider
    public D3DContext getContext() {
        return this.device.getContext();
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public VolatileImage createCompatibleVolatileImage(int i2, int i3, int i4, int i5) {
        if (i5 == 4 || i5 == 1 || i5 == 0 || i4 == 2) {
            return null;
        }
        boolean z2 = i4 == 1;
        if (i5 == 5) {
            if (!this.device.isCapPresent(z2 ? 8 : 4)) {
                return null;
            }
        } else if (i5 == 2 && !z2 && !this.device.isCapPresent(2)) {
            return null;
        }
        AccelTypedVolatileImage accelTypedVolatileImage = new AccelTypedVolatileImage(this, i2, i3, i4, i5);
        Surface destSurface = accelTypedVolatileImage.getDestSurface();
        if (!(destSurface instanceof AccelSurface) || ((AccelSurface) destSurface).getType() != i5) {
            accelTypedVolatileImage.flush();
            accelTypedVolatileImage = null;
        }
        return accelTypedVolatileImage;
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public ContextCapabilities getContextCapabilities() {
        return this.device.getContextCapabilities();
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public void addDeviceEventListener(AccelDeviceEventListener accelDeviceEventListener) {
        AccelDeviceEventNotifier.addListener(accelDeviceEventListener, this.device.getScreen());
    }

    @Override // sun.java2d.pipe.hw.AccelGraphicsConfig
    public void removeDeviceEventListener(AccelDeviceEventListener accelDeviceEventListener) {
        AccelDeviceEventNotifier.removeListener(accelDeviceEventListener);
    }
}
