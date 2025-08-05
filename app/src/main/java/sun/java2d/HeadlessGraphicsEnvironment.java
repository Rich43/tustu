package sun.java2d;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Locale;

/* loaded from: rt.jar:sun/java2d/HeadlessGraphicsEnvironment.class */
public class HeadlessGraphicsEnvironment extends GraphicsEnvironment {
    private GraphicsEnvironment ge;

    public HeadlessGraphicsEnvironment(GraphicsEnvironment graphicsEnvironment) {
        this.ge = graphicsEnvironment;
    }

    @Override // java.awt.GraphicsEnvironment
    public GraphicsDevice[] getScreenDevices() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.GraphicsEnvironment
    public GraphicsDevice getDefaultScreenDevice() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.GraphicsEnvironment
    public Point getCenterPoint() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.GraphicsEnvironment
    public Rectangle getMaximumWindowBounds() throws HeadlessException {
        throw new HeadlessException();
    }

    @Override // java.awt.GraphicsEnvironment
    public Graphics2D createGraphics(BufferedImage bufferedImage) {
        return this.ge.createGraphics(bufferedImage);
    }

    @Override // java.awt.GraphicsEnvironment
    public Font[] getAllFonts() {
        return this.ge.getAllFonts();
    }

    @Override // java.awt.GraphicsEnvironment
    public String[] getAvailableFontFamilyNames() {
        return this.ge.getAvailableFontFamilyNames();
    }

    @Override // java.awt.GraphicsEnvironment
    public String[] getAvailableFontFamilyNames(Locale locale) {
        return this.ge.getAvailableFontFamilyNames(locale);
    }

    public GraphicsEnvironment getSunGraphicsEnvironment() {
        return this.ge;
    }
}
