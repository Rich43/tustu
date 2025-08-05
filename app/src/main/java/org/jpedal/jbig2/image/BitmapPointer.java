package org.jpedal.jbig2.image;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/image/BitmapPointer.class */
public class BitmapPointer {

    /* renamed from: x, reason: collision with root package name */
    private int f13138x;

    /* renamed from: y, reason: collision with root package name */
    private int f13139y;
    private int width;
    private int height;
    private int bits;
    private int count;
    private JBIG2Bitmap bitmap;

    public BitmapPointer(JBIG2Bitmap bitmap) {
        this.bitmap = bitmap;
        this.height = bitmap.getHeight();
        this.width = bitmap.getWidth();
    }

    public void setPointer(int x2, int y2) {
        this.f13138x = x2;
        this.f13139y = y2;
        this.count = 0;
    }

    public int nextPixel() {
        if (this.f13139y < 0 || this.f13139y >= this.height || this.f13138x >= this.width) {
            return 0;
        }
        if (this.f13138x < 0) {
            this.f13138x++;
            return 0;
        }
        int pixel = this.bitmap.getPixel(this.f13138x, this.f13139y);
        this.f13138x++;
        return pixel;
    }
}
