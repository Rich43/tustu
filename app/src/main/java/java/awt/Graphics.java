package java.awt;

import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/* loaded from: rt.jar:java/awt/Graphics.class */
public abstract class Graphics {
    public abstract Graphics create();

    public abstract void translate(int i2, int i3);

    public abstract Color getColor();

    public abstract void setColor(Color color);

    public abstract void setPaintMode();

    public abstract void setXORMode(Color color);

    public abstract Font getFont();

    public abstract void setFont(Font font);

    public abstract FontMetrics getFontMetrics(Font font);

    public abstract Rectangle getClipBounds();

    public abstract void clipRect(int i2, int i3, int i4, int i5);

    public abstract void setClip(int i2, int i3, int i4, int i5);

    public abstract Shape getClip();

    public abstract void setClip(Shape shape);

    public abstract void copyArea(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void drawLine(int i2, int i3, int i4, int i5);

    public abstract void fillRect(int i2, int i3, int i4, int i5);

    public abstract void clearRect(int i2, int i3, int i4, int i5);

    public abstract void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void drawOval(int i2, int i3, int i4, int i5);

    public abstract void fillOval(int i2, int i3, int i4, int i5);

    public abstract void drawArc(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void fillArc(int i2, int i3, int i4, int i5, int i6, int i7);

    public abstract void drawPolyline(int[] iArr, int[] iArr2, int i2);

    public abstract void drawPolygon(int[] iArr, int[] iArr2, int i2);

    public abstract void fillPolygon(int[] iArr, int[] iArr2, int i2);

    public abstract void drawString(String str, int i2, int i3);

    public abstract void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3);

    public abstract boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver);

    public abstract boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver);

    public abstract boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver);

    public abstract boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver);

    public abstract boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver);

    public abstract boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver);

    public abstract void dispose();

    protected Graphics() {
    }

    public Graphics create(int i2, int i3, int i4, int i5) {
        Graphics graphicsCreate = create();
        if (graphicsCreate == null) {
            return null;
        }
        graphicsCreate.translate(i2, i3);
        graphicsCreate.clipRect(0, 0, i4, i5);
        return graphicsCreate;
    }

    public FontMetrics getFontMetrics() {
        return getFontMetrics(getFont());
    }

    public void drawRect(int i2, int i3, int i4, int i5) {
        if (i4 < 0 || i5 < 0) {
            return;
        }
        if (i5 == 0 || i4 == 0) {
            drawLine(i2, i3, i2 + i4, i3 + i5);
            return;
        }
        drawLine(i2, i3, (i2 + i4) - 1, i3);
        drawLine(i2 + i4, i3, i2 + i4, (i3 + i5) - 1);
        drawLine(i2 + i4, i3 + i5, i2 + 1, i3 + i5);
        drawLine(i2, i3 + i5, i2, i3 + 1);
    }

    public void draw3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        Color color = getColor();
        Color colorBrighter = color.brighter();
        Color colorDarker = color.darker();
        setColor(z2 ? colorBrighter : colorDarker);
        drawLine(i2, i3, i2, i3 + i5);
        drawLine(i2 + 1, i3, (i2 + i4) - 1, i3);
        setColor(z2 ? colorDarker : colorBrighter);
        drawLine(i2 + 1, i3 + i5, i2 + i4, i3 + i5);
        drawLine(i2 + i4, i3, i2 + i4, (i3 + i5) - 1);
        setColor(color);
    }

    public void fill3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        Color color = getColor();
        Color colorBrighter = color.brighter();
        Color colorDarker = color.darker();
        if (!z2) {
            setColor(colorDarker);
        }
        fillRect(i2 + 1, i3 + 1, i4 - 2, i5 - 2);
        setColor(z2 ? colorBrighter : colorDarker);
        drawLine(i2, i3, i2, (i3 + i5) - 1);
        drawLine(i2 + 1, i3, (i2 + i4) - 2, i3);
        setColor(z2 ? colorDarker : colorBrighter);
        drawLine(i2 + 1, (i3 + i5) - 1, (i2 + i4) - 1, (i3 + i5) - 1);
        drawLine((i2 + i4) - 1, i3, (i2 + i4) - 1, (i3 + i5) - 2);
        setColor(color);
    }

    public void drawPolygon(Polygon polygon) {
        drawPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
    }

    public void fillPolygon(Polygon polygon) {
        fillPolygon(polygon.xpoints, polygon.ypoints, polygon.npoints);
    }

    public void drawChars(char[] cArr, int i2, int i3, int i4, int i5) {
        drawString(new String(cArr, i2, i3), i4, i5);
    }

    public void drawBytes(byte[] bArr, int i2, int i3, int i4, int i5) {
        drawString(new String(bArr, 0, i2, i3), i4, i5);
    }

    public void finalize() {
        dispose();
    }

    public String toString() {
        return getClass().getName() + "[font=" + ((Object) getFont()) + ",color=" + ((Object) getColor()) + "]";
    }

    @Deprecated
    public Rectangle getClipRect() {
        return getClipBounds();
    }

    public boolean hitClip(int i2, int i3, int i4, int i5) {
        Rectangle clipBounds = getClipBounds();
        if (clipBounds == null) {
            return true;
        }
        return clipBounds.intersects(i2, i3, i4, i5);
    }

    public Rectangle getClipBounds(Rectangle rectangle) {
        Rectangle clipBounds = getClipBounds();
        if (clipBounds != null) {
            rectangle.f12372x = clipBounds.f12372x;
            rectangle.f12373y = clipBounds.f12373y;
            rectangle.width = clipBounds.width;
            rectangle.height = clipBounds.height;
        } else if (rectangle == null) {
            throw new NullPointerException("null rectangle parameter");
        }
        return rectangle;
    }
}
