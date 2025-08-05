package sun.awt.image;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ImageProducer;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;

/* loaded from: rt.jar:sun/awt/image/OffScreenImage.class */
public class OffScreenImage extends BufferedImage {

    /* renamed from: c, reason: collision with root package name */
    protected Component f13549c;
    private OffScreenImageSource osis;
    private Font defaultFont;

    public OffScreenImage(Component component, ColorModel colorModel, WritableRaster writableRaster, boolean z2) {
        super(colorModel, writableRaster, z2, (Hashtable<?, ?>) null);
        this.f13549c = component;
        initSurface(writableRaster.getWidth(), writableRaster.getHeight());
    }

    @Override // java.awt.image.BufferedImage, java.awt.Image
    public Graphics getGraphics() {
        return createGraphics();
    }

    @Override // java.awt.image.BufferedImage
    public Graphics2D createGraphics() {
        if (this.f13549c == null) {
            return GraphicsEnvironment.getLocalGraphicsEnvironment().createGraphics(this);
        }
        Color background = this.f13549c.getBackground();
        if (background == null) {
            background = SystemColor.window;
        }
        Color foreground = this.f13549c.getForeground();
        if (foreground == null) {
            foreground = SystemColor.windowText;
        }
        Font font = this.f13549c.getFont();
        if (font == null) {
            if (this.defaultFont == null) {
                this.defaultFont = new Font(Font.DIALOG, 0, 12);
            }
            font = this.defaultFont;
        }
        return new SunGraphics2D(SurfaceData.getPrimarySurfaceData(this), foreground, background, font);
    }

    private void initSurface(int i2, int i3) {
        Graphics2D graphics2DCreateGraphics = createGraphics();
        try {
            graphics2DCreateGraphics.clearRect(0, 0, i2, i3);
            graphics2DCreateGraphics.dispose();
        } catch (Throwable th) {
            graphics2DCreateGraphics.dispose();
            throw th;
        }
    }

    @Override // java.awt.image.BufferedImage, java.awt.Image
    public ImageProducer getSource() {
        if (this.osis == null) {
            this.osis = new OffScreenImageSource(this);
        }
        return this.osis;
    }
}
