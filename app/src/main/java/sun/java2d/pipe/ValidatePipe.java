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

/* loaded from: rt.jar:sun/java2d/pipe/ValidatePipe.class */
public class ValidatePipe implements PixelDrawPipe, PixelFillPipe, ShapeDrawPipe, TextPipe, DrawImagePipe {
    public boolean validate(SunGraphics2D sunGraphics2D) {
        sunGraphics2D.validatePipe();
        return true;
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawLine(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawRect(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.fillpipe.fillRect(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawRoundRect(sunGraphics2D, i2, i3, i4, i5, i6, i7);
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.fillpipe.fillRoundRect(sunGraphics2D, i2, i3, i4, i5, i6, i7);
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawOval(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.fillpipe.fillOval(sunGraphics2D, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawArc(sunGraphics2D, i2, i3, i4, i5, i6, i7);
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.fillpipe.fillArc(sunGraphics2D, i2, i3, i4, i5, i6, i7);
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolyline(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawPolyline(sunGraphics2D, iArr, iArr2, i2);
        }
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.drawpipe.drawPolygon(sunGraphics2D, iArr, iArr2, i2);
        }
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.fillpipe.fillPolygon(sunGraphics2D, iArr, iArr2, i2);
        }
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void draw(SunGraphics2D sunGraphics2D, Shape shape) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.shapepipe.draw(sunGraphics2D, shape);
        }
    }

    @Override // sun.java2d.pipe.ShapeDrawPipe
    public void fill(SunGraphics2D sunGraphics2D, Shape shape) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.shapepipe.fill(sunGraphics2D, shape);
        }
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawString(SunGraphics2D sunGraphics2D, String str, double d2, double d3) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.textpipe.drawString(sunGraphics2D, str, d2, d3);
        }
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawGlyphVector(SunGraphics2D sunGraphics2D, GlyphVector glyphVector, float f2, float f3) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.textpipe.drawGlyphVector(sunGraphics2D, glyphVector, f2, f3);
        }
    }

    @Override // sun.java2d.pipe.TextPipe
    public void drawChars(SunGraphics2D sunGraphics2D, char[] cArr, int i2, int i3, int i4, int i5) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.textpipe.drawChars(sunGraphics2D, cArr, i2, i3, i4, i5);
        }
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        if (validate(sunGraphics2D)) {
            return sunGraphics2D.imagepipe.copyImage(sunGraphics2D, image, i2, i3, color, imageObserver);
        }
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver) {
        if (validate(sunGraphics2D)) {
            return sunGraphics2D.imagepipe.copyImage(sunGraphics2D, image, i2, i3, i4, i5, i6, i7, color, imageObserver);
        }
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        if (validate(sunGraphics2D)) {
            return sunGraphics2D.imagepipe.scaleImage(sunGraphics2D, image, i2, i3, i4, i5, color, imageObserver);
        }
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        if (validate(sunGraphics2D)) {
            return sunGraphics2D.imagepipe.scaleImage(sunGraphics2D, image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
        }
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean transformImage(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        if (validate(sunGraphics2D)) {
            return sunGraphics2D.imagepipe.transformImage(sunGraphics2D, image, affineTransform, imageObserver);
        }
        return false;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public void transformImage(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        if (validate(sunGraphics2D)) {
            sunGraphics2D.imagepipe.transformImage(sunGraphics2D, bufferedImage, bufferedImageOp, i2, i3);
        }
    }
}
