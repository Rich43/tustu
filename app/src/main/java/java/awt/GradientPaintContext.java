package java.awt;

import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.lang.ref.WeakReference;
import sun.awt.image.IntegerComponentRaster;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:java/awt/GradientPaintContext.class */
class GradientPaintContext implements PaintContext {
    static ColorModel xrgbmodel = new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255);
    static ColorModel xbgrmodel = new DirectColorModel(24, 255, NormalizerImpl.CC_MASK, 16711680);
    static ColorModel cachedModel;
    static WeakReference<Raster> cached;
    double x1;
    double y1;
    double dx;
    double dy;
    boolean cyclic;
    int[] interp;
    Raster saved;
    ColorModel model;

    static synchronized Raster getCachedRaster(ColorModel colorModel, int i2, int i3) {
        Raster raster;
        if (colorModel == cachedModel && cached != null && (raster = cached.get()) != null && raster.getWidth() >= i2 && raster.getHeight() >= i3) {
            cached = null;
            return raster;
        }
        return colorModel.createCompatibleWritableRaster(i2, i3);
    }

    static synchronized void putCachedRaster(ColorModel colorModel, Raster raster) {
        Raster raster2;
        if (cached != null && (raster2 = cached.get()) != null) {
            int width = raster2.getWidth();
            int height = raster2.getHeight();
            int width2 = raster.getWidth();
            int height2 = raster.getHeight();
            if ((width >= width2 && height >= height2) || width * height >= width2 * height2) {
                return;
            }
        }
        cachedModel = colorModel;
        cached = new WeakReference<>(raster);
    }

    public GradientPaintContext(ColorModel colorModel, Point2D point2D, Point2D point2D2, AffineTransform affineTransform, Color color, Color color2, boolean z2) {
        DirectColorModel directColorModel;
        int alphaMask;
        Point2D.Double r0 = new Point2D.Double(1.0d, 0.0d);
        Point2D.Double r02 = new Point2D.Double(0.0d, 1.0d);
        try {
            AffineTransform affineTransformCreateInverse = affineTransform.createInverse();
            affineTransformCreateInverse.deltaTransform(r0, r0);
            affineTransformCreateInverse.deltaTransform(r02, r02);
        } catch (NoninvertibleTransformException e2) {
            r0.setLocation(0.0d, 0.0d);
            r02.setLocation(0.0d, 0.0d);
        }
        double x2 = point2D2.getX() - point2D.getX();
        double y2 = point2D2.getY() - point2D.getY();
        double d2 = (x2 * x2) + (y2 * y2);
        if (d2 <= Double.MIN_VALUE) {
            this.dx = 0.0d;
            this.dy = 0.0d;
        } else {
            this.dx = ((r0.getX() * x2) + (r0.getY() * y2)) / d2;
            this.dy = ((r02.getX() * x2) + (r02.getY() * y2)) / d2;
            if (z2) {
                this.dx %= 1.0d;
                this.dy %= 1.0d;
            } else if (this.dx < 0.0d) {
                point2D = point2D2;
                color = color2;
                color2 = color;
                this.dx = -this.dx;
                this.dy = -this.dy;
            }
        }
        Point2D point2DTransform = affineTransform.transform(point2D, null);
        this.x1 = point2DTransform.getX();
        this.y1 = point2DTransform.getY();
        this.cyclic = z2;
        int rgb = color.getRGB();
        int rgb2 = color2.getRGB();
        int i2 = (rgb >> 24) & 255;
        int i3 = (rgb >> 16) & 255;
        int i4 = (rgb >> 8) & 255;
        int i5 = rgb & 255;
        int i6 = ((rgb2 >> 24) & 255) - i2;
        int i7 = ((rgb2 >> 16) & 255) - i3;
        int i8 = ((rgb2 >> 8) & 255) - i4;
        int i9 = (rgb2 & 255) - i5;
        if (i2 == 255 && i6 == 0) {
            this.model = xrgbmodel;
            if ((colorModel instanceof DirectColorModel) && (((alphaMask = (directColorModel = (DirectColorModel) colorModel).getAlphaMask()) == 0 || alphaMask == 255) && directColorModel.getRedMask() == 255 && directColorModel.getGreenMask() == 65280 && directColorModel.getBlueMask() == 16711680)) {
                this.model = xbgrmodel;
                i3 = i5;
                i5 = i3;
                i7 = i9;
                i9 = i7;
            }
        } else {
            this.model = ColorModel.getRGBdefault();
        }
        this.interp = new int[z2 ? 513 : 257];
        for (int i10 = 0; i10 <= 256; i10++) {
            float f2 = i10 / 256.0f;
            int i11 = (((int) (i2 + (i6 * f2))) << 24) | (((int) (i3 + (i7 * f2))) << 16) | (((int) (i4 + (i8 * f2))) << 8) | ((int) (i5 + (i9 * f2)));
            this.interp[i10] = i11;
            if (z2) {
                this.interp[512 - i10] = i11;
            }
        }
    }

    @Override // java.awt.PaintContext
    public void dispose() {
        if (this.saved != null) {
            putCachedRaster(this.model, this.saved);
            this.saved = null;
        }
    }

    @Override // java.awt.PaintContext
    public ColorModel getColorModel() {
        return this.model;
    }

    @Override // java.awt.PaintContext
    public Raster getRaster(int i2, int i3, int i4, int i5) {
        double d2 = ((i2 - this.x1) * this.dx) + ((i3 - this.y1) * this.dy);
        Raster cachedRaster = this.saved;
        if (cachedRaster == null || cachedRaster.getWidth() < i4 || cachedRaster.getHeight() < i5) {
            cachedRaster = getCachedRaster(this.model, i4, i5);
            this.saved = cachedRaster;
        }
        IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) cachedRaster;
        int dataOffset = integerComponentRaster.getDataOffset(0);
        int scanlineStride = integerComponentRaster.getScanlineStride() - i4;
        int[] dataStorage = integerComponentRaster.getDataStorage();
        if (this.cyclic) {
            cycleFillRaster(dataStorage, dataOffset, scanlineStride, i4, i5, d2, this.dx, this.dy);
        } else {
            clipFillRaster(dataStorage, dataOffset, scanlineStride, i4, i5, d2, this.dx, this.dy);
        }
        integerComponentRaster.markDirty();
        return cachedRaster;
    }

    void cycleFillRaster(int[] iArr, int i2, int i3, int i4, int i5, double d2, double d3, double d4) {
        int i6 = ((int) ((d2 % 2.0d) * 1.073741824E9d)) << 1;
        int i7 = (int) ((-d3) * (-2.147483648E9d));
        int i8 = (int) ((-d4) * (-2.147483648E9d));
        while (true) {
            i5--;
            if (i5 >= 0) {
                int i9 = i6;
                for (int i10 = i4; i10 > 0; i10--) {
                    int i11 = i2;
                    i2++;
                    iArr[i11] = this.interp[i9 >>> 23];
                    i9 += i7;
                }
                i2 += i3;
                i6 += i8;
            } else {
                return;
            }
        }
    }

    void clipFillRaster(int[] iArr, int i2, int i3, int i4, int i5, double d2, double d3, double d4) {
        while (true) {
            i5--;
            if (i5 >= 0) {
                double d5 = d2;
                int i6 = i4;
                if (d5 <= 0.0d) {
                    int i7 = this.interp[0];
                    do {
                        int i8 = i2;
                        i2++;
                        iArr[i8] = i7;
                        d5 += d3;
                        i6--;
                        if (i6 <= 0) {
                            break;
                        }
                    } while (d5 <= 0.0d);
                }
                while (d5 < 1.0d) {
                    i6--;
                    if (i6 < 0) {
                        break;
                    }
                    int i9 = i2;
                    i2++;
                    iArr[i9] = this.interp[(int) (d5 * 256.0d)];
                    d5 += d3;
                }
                if (i6 > 0) {
                    int i10 = this.interp[256];
                    do {
                        int i11 = i2;
                        i2++;
                        iArr[i11] = i10;
                        i6--;
                    } while (i6 > 0);
                }
                i2 += i3;
                d2 += d4;
            } else {
                return;
            }
        }
    }
}
