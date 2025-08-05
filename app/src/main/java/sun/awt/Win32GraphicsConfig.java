package sun.awt;

import java.awt.AWTException;
import java.awt.BufferCapabilities;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.VolatileImage;
import sun.awt.image.OffScreenImage;
import sun.awt.image.SunVolatileImage;
import sun.awt.image.SurfaceManager;
import sun.awt.windows.WComponentPeer;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.RenderLoops;
import sun.java2d.loops.SurfaceType;
import sun.java2d.windows.GDIWindowSurfaceData;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/Win32GraphicsConfig.class */
public class Win32GraphicsConfig extends GraphicsConfiguration implements DisplayChangedListener, SurfaceManager.ProxiedGraphicsConfig {
    protected Win32GraphicsDevice screen;
    protected int visual;
    protected RenderLoops solidloops;
    private SurfaceType sTypeOrig = null;

    private static native void initIDs();

    private native Rectangle getBounds(int i2);

    static {
        initIDs();
    }

    public static Win32GraphicsConfig getConfig(Win32GraphicsDevice win32GraphicsDevice, int i2) {
        return new Win32GraphicsConfig(win32GraphicsDevice, i2);
    }

    @Deprecated
    public Win32GraphicsConfig(GraphicsDevice graphicsDevice, int i2) {
        this.screen = (Win32GraphicsDevice) graphicsDevice;
        this.visual = i2;
        ((Win32GraphicsDevice) graphicsDevice).addDisplayChangedListener(this);
    }

    @Override // java.awt.GraphicsConfiguration
    public GraphicsDevice getDevice() {
        return this.screen;
    }

    public int getVisual() {
        return this.visual;
    }

    @Override // sun.awt.image.SurfaceManager.ProxiedGraphicsConfig
    public Object getProxyKey() {
        return this.screen;
    }

    public synchronized RenderLoops getSolidLoops(SurfaceType surfaceType) {
        if (this.solidloops == null || this.sTypeOrig != surfaceType) {
            this.solidloops = SurfaceData.makeRenderLoops(SurfaceType.OpaqueColor, CompositeType.SrcNoEa, surfaceType);
            this.sTypeOrig = surfaceType;
        }
        return this.solidloops;
    }

    @Override // java.awt.GraphicsConfiguration
    public synchronized ColorModel getColorModel() {
        return this.screen.getColorModel();
    }

    public ColorModel getDeviceColorModel() {
        return this.screen.getDynamicColorModel();
    }

    @Override // java.awt.GraphicsConfiguration
    public ColorModel getColorModel(int i2) {
        switch (i2) {
            case 1:
                return getColorModel();
            case 2:
                return new DirectColorModel(25, 16711680, NormalizerImpl.CC_MASK, 255, 16777216);
            case 3:
                return ColorModel.getRGBdefault();
            default:
                return null;
        }
    }

    @Override // java.awt.GraphicsConfiguration
    public AffineTransform getDefaultTransform() {
        return new AffineTransform();
    }

    @Override // java.awt.GraphicsConfiguration
    public AffineTransform getNormalizingTransform() {
        Win32GraphicsEnvironment win32GraphicsEnvironment = (Win32GraphicsEnvironment) GraphicsEnvironment.getLocalGraphicsEnvironment();
        return new AffineTransform(win32GraphicsEnvironment.getXResolution() / 72.0d, 0.0d, 0.0d, win32GraphicsEnvironment.getYResolution() / 72.0d, 0.0d, 0.0d);
    }

    public String toString() {
        return super.toString() + "[dev=" + ((Object) this.screen) + ",pixfmt=" + this.visual + "]";
    }

    @Override // java.awt.GraphicsConfiguration
    public Rectangle getBounds() {
        return getBounds(this.screen.getScreen());
    }

    @Override // sun.awt.DisplayChangedListener
    public synchronized void displayChanged() {
        this.solidloops = null;
    }

    @Override // sun.awt.DisplayChangedListener
    public void paletteChanged() {
    }

    public SurfaceData createSurfaceData(WComponentPeer wComponentPeer, int i2) {
        return GDIWindowSurfaceData.createData(wComponentPeer);
    }

    public Image createAcceleratedImage(Component component, int i2, int i3) {
        ColorModel colorModel = getColorModel(1);
        return new OffScreenImage(component, colorModel, colorModel.createCompatibleWritableRaster(i2, i3), colorModel.isAlphaPremultiplied());
    }

    public void assertOperationSupported(Component component, int i2, BufferCapabilities bufferCapabilities) throws AWTException {
        throw new AWTException("The operation requested is not supported");
    }

    public VolatileImage createBackBuffer(WComponentPeer wComponentPeer) {
        Component component = (Component) wComponentPeer.getTarget();
        return new SunVolatileImage(component, component.getWidth(), component.getHeight(), Boolean.TRUE);
    }

    public void flip(WComponentPeer wComponentPeer, Component component, VolatileImage volatileImage, int i2, int i3, int i4, int i5, BufferCapabilities.FlipContents flipContents) {
        if (flipContents == BufferCapabilities.FlipContents.COPIED || flipContents == BufferCapabilities.FlipContents.UNDEFINED) {
            Graphics graphics = wComponentPeer.getGraphics();
            try {
                graphics.drawImage(volatileImage, i2, i3, i4, i5, i2, i3, i4, i5, null);
                graphics.dispose();
                return;
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

    @Override // java.awt.GraphicsConfiguration
    public boolean isTranslucencyCapable() {
        return true;
    }
}
