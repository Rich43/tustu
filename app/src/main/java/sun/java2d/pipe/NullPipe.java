package sun.java2d.pipe;

import java.awt.Color;
import java.awt.Image;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/NullPipe.class */
public class NullPipe implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe, TextPipe, DrawImagePipe {
    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolyline(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawString(SunGraphics2D sunGraphics2D, String str, double d2, double d3) {
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawGlyphVector(SunGraphics2D sunGraphics2D, GlyphVector glyphVector, float f2, float f3) {
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawChars(SunGraphics2D sunGraphics2D, char[] cArr, int i2, int i3, int i4, int i5) {
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver) {
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean transformImage(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public void transformImage(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
    }
}
