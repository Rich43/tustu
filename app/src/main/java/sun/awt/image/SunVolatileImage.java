package sun.awt.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Image;
import java.awt.ImageCapabilities;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import sun.java2d.DestSurfaceProvider;
import sun.java2d.SunGraphics2D;
import sun.java2d.Surface;
import sun.java2d.SurfaceManagerFactory;
import sun.print.PrinterGraphicsConfig;

/* loaded from: rt.jar:sun/awt/image/SunVolatileImage.class */
public class SunVolatileImage extends VolatileImage implements DestSurfaceProvider {
    protected VolatileSurfaceManager volSurfaceManager;
    protected Component comp;
    private GraphicsConfiguration graphicsConfig;
    private Font defaultFont;
    private int width;
    private int height;
    private int forcedAccelSurfaceType;

    protected SunVolatileImage(Component component, GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object obj, int i4, ImageCapabilities imageCapabilities, int i5) {
        this.comp = component;
        this.graphicsConfig = graphicsConfiguration;
        if (i2 <= 0 || i3 <= 0) {
            throw new IllegalArgumentException("Width (" + i2 + ") and height (" + i3 + ") cannot be <= 0");
        }
        this.width = i2;
        this.height = i3;
        this.forcedAccelSurfaceType = i5;
        if (i4 != 1 && i4 != 2 && i4 != 3) {
            throw new IllegalArgumentException("Unknown transparency type:" + i4);
        }
        this.transparency = i4;
        this.volSurfaceManager = createSurfaceManager(obj, imageCapabilities);
        SurfaceManager.setManager(this, this.volSurfaceManager);
        this.volSurfaceManager.initialize();
        this.volSurfaceManager.initContents();
    }

    private SunVolatileImage(Component component, GraphicsConfiguration graphicsConfiguration, int i2, int i3, Object obj, ImageCapabilities imageCapabilities) {
        this(component, graphicsConfiguration, i2, i3, obj, 1, imageCapabilities, 0);
    }

    public SunVolatileImage(Component component, int i2, int i3) {
        this(component, i2, i3, null);
    }

    public SunVolatileImage(Component component, int i2, int i3, Object obj) {
        this(component, component.getGraphicsConfiguration(), i2, i3, obj, null);
    }

    public SunVolatileImage(GraphicsConfiguration graphicsConfiguration, int i2, int i3, int i4, ImageCapabilities imageCapabilities) {
        this(null, graphicsConfiguration, i2, i3, null, i4, imageCapabilities, 0);
    }

    @Override // java.awt.image.VolatileImage
    public int getWidth() {
        return this.width;
    }

    @Override // java.awt.image.VolatileImage
    public int getHeight() {
        return this.height;
    }

    public GraphicsConfiguration getGraphicsConfig() {
        return this.graphicsConfig;
    }

    public void updateGraphicsConfig() {
        GraphicsConfiguration graphicsConfiguration;
        if (this.comp != null && (graphicsConfiguration = this.comp.getGraphicsConfiguration()) != null) {
            this.graphicsConfig = graphicsConfiguration;
        }
    }

    public Component getComponent() {
        return this.comp;
    }

    public int getForcedAccelSurfaceType() {
        return this.forcedAccelSurfaceType;
    }

    protected VolatileSurfaceManager createSurfaceManager(Object obj, ImageCapabilities imageCapabilities) {
        if ((this.graphicsConfig instanceof BufferedImageGraphicsConfig) || (this.graphicsConfig instanceof PrinterGraphicsConfig) || (imageCapabilities != null && !imageCapabilities.isAccelerated())) {
            return new BufImgVolatileSurfaceManager(this, obj);
        }
        return SurfaceManagerFactory.getInstance().createVolatileManager(this, obj);
    }

    private Color getForeground() {
        if (this.comp != null) {
            return this.comp.getForeground();
        }
        return Color.black;
    }

    private Color getBackground() {
        if (this.comp != null) {
            return this.comp.getBackground();
        }
        return Color.white;
    }

    private Font getFont() {
        if (this.comp != null) {
            return this.comp.getFont();
        }
        if (this.defaultFont == null) {
            this.defaultFont = new Font(Font.DIALOG, 0, 12);
        }
        return this.defaultFont;
    }

    @Override // java.awt.image.VolatileImage
    public Graphics2D createGraphics() {
        return new SunGraphics2D(this.volSurfaceManager.getPrimarySurfaceData(), getForeground(), getBackground(), getFont());
    }

    @Override // java.awt.Image
    public Object getProperty(String str, ImageObserver imageObserver) {
        if (str == null) {
            throw new NullPointerException("null property name is not allowed");
        }
        return Image.UndefinedProperty;
    }

    @Override // java.awt.Image
    public int getWidth(ImageObserver imageObserver) {
        return getWidth();
    }

    @Override // java.awt.Image
    public int getHeight(ImageObserver imageObserver) {
        return getHeight();
    }

    public BufferedImage getBackupImage() {
        return this.graphicsConfig.createCompatibleImage(getWidth(), getHeight(), getTransparency());
    }

    @Override // java.awt.image.VolatileImage
    public BufferedImage getSnapshot() {
        BufferedImage backupImage = getBackupImage();
        Graphics2D graphics2DCreateGraphics = backupImage.createGraphics();
        graphics2DCreateGraphics.setComposite(AlphaComposite.Src);
        graphics2DCreateGraphics.drawImage(this, 0, 0, (ImageObserver) null);
        graphics2DCreateGraphics.dispose();
        return backupImage;
    }

    @Override // java.awt.image.VolatileImage
    public int validate(GraphicsConfiguration graphicsConfiguration) {
        return this.volSurfaceManager.validate(graphicsConfiguration);
    }

    @Override // java.awt.image.VolatileImage
    public boolean contentsLost() {
        return this.volSurfaceManager.contentsLost();
    }

    @Override // java.awt.image.VolatileImage
    public ImageCapabilities getCapabilities() {
        return this.volSurfaceManager.getCapabilities(this.graphicsConfig);
    }

    @Override // sun.java2d.DestSurfaceProvider
    public Surface getDestSurface() {
        return this.volSurfaceManager.getPrimarySurfaceData();
    }
}
