package sun.java2d.pipe;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.RoundRectangle2D;
import sun.java2d.SunGraphics2D;

/* loaded from: rt.jar:sun/java2d/pipe/PixelToShapeConverter.class */
public class PixelToShapeConverter implements PixelDrawPipe, PixelFillPipe {
    ShapeDrawPipe outpipe;

    public PixelToShapeConverter(ShapeDrawPipe shapeDrawPipe) {
        this.outpipe = shapeDrawPipe;
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawLine(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.outpipe.draw(sunGraphics2D, new Line2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.outpipe.draw(sunGraphics2D, new Rectangle(i2, i3, i4, i5));
    }

    public void fillRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.outpipe.fill(sunGraphics2D, new Rectangle(i2, i3, i4, i5));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.outpipe.draw(sunGraphics2D, new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillRoundRect(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.outpipe.fill(sunGraphics2D, new RoundRectangle2D.Float(i2, i3, i4, i5, i6, i7));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.outpipe.draw(sunGraphics2D, new Ellipse2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillOval(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5) {
        this.outpipe.fill(sunGraphics2D, new Ellipse2D.Float(i2, i3, i4, i5));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.outpipe.draw(sunGraphics2D, new Arc2D.Float(i2, i3, i4, i5, i6, i7, 0));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillArc(SunGraphics2D sunGraphics2D, int i2, int i3, int i4, int i5, int i6, int i7) {
        this.outpipe.fill(sunGraphics2D, new Arc2D.Float(i2, i3, i4, i5, i6, i7, 2));
    }

    private Shape makePoly(int[] iArr, int[] iArr2, int i2, boolean z2) {
        GeneralPath generalPath = new GeneralPath(0);
        if (i2 > 0) {
            generalPath.moveTo(iArr[0], iArr2[0]);
            for (int i3 = 1; i3 < i2; i3++) {
                generalPath.lineTo(iArr[i3], iArr2[i3]);
            }
            if (z2) {
                generalPath.closePath();
            }
        }
        return generalPath;
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolyline(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        this.outpipe.draw(sunGraphics2D, makePoly(iArr, iArr2, i2, false));
    }

    @Override // sun.java2d.pipe.PixelDrawPipe
    public void drawPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        this.outpipe.draw(sunGraphics2D, makePoly(iArr, iArr2, i2, true));
    }

    @Override // sun.java2d.pipe.PixelFillPipe
    public void fillPolygon(SunGraphics2D sunGraphics2D, int[] iArr, int[] iArr2, int i2) {
        this.outpipe.fill(sunGraphics2D, makePoly(iArr, iArr2, i2, true));
    }
}
