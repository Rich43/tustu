package sun.print;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

/* loaded from: rt.jar:sun/print/ProxyGraphics.class */
public class ProxyGraphics extends Graphics {

    /* renamed from: g, reason: collision with root package name */
    private Graphics f13600g;

    public ProxyGraphics(Graphics graphics) {
        this.f13600g = graphics;
    }

    Graphics getGraphics() {
        return this.f13600g;
    }

    @Override // java.awt.Graphics
    public Graphics create() {
        return new ProxyGraphics(this.f13600g.create());
    }

    @Override // java.awt.Graphics
    public Graphics create(int i2, int i3, int i4, int i5) {
        return new ProxyGraphics(this.f13600g.create(i2, i3, i4, i5));
    }

    @Override // java.awt.Graphics
    public void translate(int i2, int i3) {
        this.f13600g.translate(i2, i3);
    }

    @Override // java.awt.Graphics
    public Color getColor() {
        return this.f13600g.getColor();
    }

    @Override // java.awt.Graphics
    public void setColor(Color color) {
        this.f13600g.setColor(color);
    }

    @Override // java.awt.Graphics
    public void setPaintMode() {
        this.f13600g.setPaintMode();
    }

    @Override // java.awt.Graphics
    public void setXORMode(Color color) {
        this.f13600g.setXORMode(color);
    }

    @Override // java.awt.Graphics
    public Font getFont() {
        return this.f13600g.getFont();
    }

    @Override // java.awt.Graphics
    public void setFont(Font font) {
        this.f13600g.setFont(font);
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics() {
        return this.f13600g.getFontMetrics();
    }

    @Override // java.awt.Graphics
    public FontMetrics getFontMetrics(Font font) {
        return this.f13600g.getFontMetrics(font);
    }

    @Override // java.awt.Graphics
    public Rectangle getClipBounds() {
        return this.f13600g.getClipBounds();
    }

    @Override // java.awt.Graphics
    public void clipRect(int i2, int i3, int i4, int i5) {
        this.f13600g.clipRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void setClip(int i2, int i3, int i4, int i5) {
        this.f13600g.setClip(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public Shape getClip() {
        return this.f13600g.getClip();
    }

    @Override // java.awt.Graphics
    public void setClip(Shape shape) {
        this.f13600g.setClip(shape);
    }

    @Override // java.awt.Graphics
    public void copyArea(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f13600g.copyArea(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawLine(int i2, int i3, int i4, int i5) {
        this.f13600g.drawLine(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void fillRect(int i2, int i3, int i4, int i5) {
        this.f13600g.fillRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawRect(int i2, int i3, int i4, int i5) {
        this.f13600g.drawRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void clearRect(int i2, int i3, int i4, int i5) {
        this.f13600g.clearRect(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f13600g.drawRoundRect(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void fillRoundRect(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f13600g.fillRoundRect(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void draw3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        this.f13600g.draw3DRect(i2, i3, i4, i5, z2);
    }

    @Override // java.awt.Graphics
    public void fill3DRect(int i2, int i3, int i4, int i5, boolean z2) {
        this.f13600g.fill3DRect(i2, i3, i4, i5, z2);
    }

    @Override // java.awt.Graphics
    public void drawOval(int i2, int i3, int i4, int i5) {
        this.f13600g.drawOval(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void fillOval(int i2, int i3, int i4, int i5) {
        this.f13600g.fillOval(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f13600g.drawArc(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void fillArc(int i2, int i3, int i4, int i5, int i6, int i7) {
        this.f13600g.fillArc(i2, i3, i4, i5, i6, i7);
    }

    @Override // java.awt.Graphics
    public void drawPolyline(int[] iArr, int[] iArr2, int i2) {
        this.f13600g.drawPolyline(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void drawPolygon(int[] iArr, int[] iArr2, int i2) {
        this.f13600g.drawPolygon(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void drawPolygon(Polygon polygon) {
        this.f13600g.drawPolygon(polygon);
    }

    @Override // java.awt.Graphics
    public void fillPolygon(int[] iArr, int[] iArr2, int i2) {
        this.f13600g.fillPolygon(iArr, iArr2, i2);
    }

    @Override // java.awt.Graphics
    public void fillPolygon(Polygon polygon) {
        this.f13600g.fillPolygon(polygon);
    }

    @Override // java.awt.Graphics
    public void drawString(String str, int i2, int i3) {
        this.f13600g.drawString(str, i2, i3);
    }

    @Override // java.awt.Graphics
    public void drawString(AttributedCharacterIterator attributedCharacterIterator, int i2, int i3) {
        this.f13600g.drawString(attributedCharacterIterator, i2, i3);
    }

    @Override // java.awt.Graphics
    public void drawChars(char[] cArr, int i2, int i3, int i4, int i5) {
        this.f13600g.drawChars(cArr, i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public void drawBytes(byte[] bArr, int i2, int i3, int i4, int i5) {
        this.f13600g.drawBytes(bArr, i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, ImageObserver imageObserver) {
        return this.f13600g.drawImage(image, i2, i3, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, ImageObserver imageObserver) {
        return this.f13600g.drawImage(image, i2, i3, i4, i5, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        return this.f13600g.drawImage(image, i2, i3, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        return this.f13600g.drawImage(image, i2, i3, i4, i5, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, ImageObserver imageObserver) {
        return this.f13600g.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, imageObserver);
    }

    @Override // java.awt.Graphics
    public boolean drawImage(Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        return this.f13600g.drawImage(image, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
    }

    @Override // java.awt.Graphics
    public void dispose() {
        this.f13600g.dispose();
    }

    @Override // java.awt.Graphics
    public void finalize() {
    }

    @Override // java.awt.Graphics
    public String toString() {
        return getClass().getName() + "[font=" + ((Object) getFont()) + ",color=" + ((Object) getColor()) + "]";
    }

    @Override // java.awt.Graphics
    @Deprecated
    public Rectangle getClipRect() {
        return this.f13600g.getClipRect();
    }

    @Override // java.awt.Graphics
    public boolean hitClip(int i2, int i3, int i4, int i5) {
        return this.f13600g.hitClip(i2, i3, i4, i5);
    }

    @Override // java.awt.Graphics
    public Rectangle getClipBounds(Rectangle rectangle) {
        return this.f13600g.getClipBounds(rectangle);
    }
}
