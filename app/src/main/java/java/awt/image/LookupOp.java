package java.awt.image;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import sun.awt.image.ImagingLib;

/* loaded from: rt.jar:java/awt/image/LookupOp.class */
public class LookupOp implements BufferedImageOp, RasterOp {
    private LookupTable ltable;
    private int numComponents;
    RenderingHints hints;

    public LookupOp(LookupTable lookupTable, RenderingHints renderingHints) {
        this.ltable = lookupTable;
        this.hints = renderingHints;
        this.numComponents = this.ltable.getNumComponents();
    }

    public final LookupTable getTable() {
        return this.ltable;
    }

    @Override // java.awt.image.BufferedImageOp
    public final BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        ColorModel colorModel;
        ColorModel colorModel2 = bufferedImage.getColorModel();
        int numColorComponents = colorModel2.getNumColorComponents();
        if (colorModel2 instanceof IndexColorModel) {
            throw new IllegalArgumentException("LookupOp cannot be performed on an indexed image");
        }
        int numComponents = this.ltable.getNumComponents();
        if (numComponents != 1 && numComponents != colorModel2.getNumComponents() && numComponents != colorModel2.getNumColorComponents()) {
            throw new IllegalArgumentException("Number of arrays in the  lookup table (" + numComponents + " is not compatible with the  src image: " + ((Object) bufferedImage));
        }
        boolean z2 = false;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (bufferedImage2 == null) {
            bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            colorModel = colorModel2;
        } else {
            if (width != bufferedImage2.getWidth()) {
                throw new IllegalArgumentException("Src width (" + width + ") not equal to dst width (" + bufferedImage2.getWidth() + ")");
            }
            if (height != bufferedImage2.getHeight()) {
                throw new IllegalArgumentException("Src height (" + height + ") not equal to dst height (" + bufferedImage2.getHeight() + ")");
            }
            colorModel = bufferedImage2.getColorModel();
            if (colorModel2.getColorSpace().getType() != colorModel.getColorSpace().getType()) {
                z2 = true;
                bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            }
        }
        BufferedImage bufferedImage3 = bufferedImage2;
        if (ImagingLib.filter(this, bufferedImage, bufferedImage2) == null) {
            WritableRaster raster = bufferedImage.getRaster();
            WritableRaster raster2 = bufferedImage2.getRaster();
            if (colorModel2.hasAlpha() && (numColorComponents - 1 == numComponents || numComponents == 1)) {
                int minX = raster.getMinX();
                int minY = raster.getMinY();
                int[] iArr = new int[numColorComponents - 1];
                for (int i2 = 0; i2 < numColorComponents - 1; i2++) {
                    iArr[i2] = i2;
                }
                raster = raster.createWritableChild(minX, minY, raster.getWidth(), raster.getHeight(), minX, minY, iArr);
            }
            if (colorModel.hasAlpha() && (raster2.getNumBands() - 1 == numComponents || numComponents == 1)) {
                int minX2 = raster2.getMinX();
                int minY2 = raster2.getMinY();
                int[] iArr2 = new int[numColorComponents - 1];
                for (int i3 = 0; i3 < numColorComponents - 1; i3++) {
                    iArr2[i3] = i3;
                }
                raster2 = raster2.createWritableChild(minX2, minY2, raster2.getWidth(), raster2.getHeight(), minX2, minY2, iArr2);
            }
            filter(raster, raster2);
        }
        if (z2) {
            new ColorConvertOp(this.hints).filter(bufferedImage2, bufferedImage3);
        }
        return bufferedImage3;
    }

    @Override // java.awt.image.RasterOp
    public final WritableRaster filter(Raster raster, WritableRaster writableRaster) {
        int numBands = raster.getNumBands();
        writableRaster.getNumBands();
        int height = raster.getHeight();
        int width = raster.getWidth();
        int[] iArr = new int[numBands];
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        } else if (height != writableRaster.getHeight() || width != writableRaster.getWidth()) {
            throw new IllegalArgumentException("Width or height of Rasters do not match");
        }
        int numBands2 = writableRaster.getNumBands();
        if (numBands != numBands2) {
            throw new IllegalArgumentException("Number of channels in the src (" + numBands + ") does not match number of channels in the destination (" + numBands2 + ")");
        }
        int numComponents = this.ltable.getNumComponents();
        if (numComponents != 1 && numComponents != raster.getNumBands()) {
            throw new IllegalArgumentException("Number of arrays in the  lookup table (" + numComponents + " is not compatible with the  src Raster: " + ((Object) raster));
        }
        if (ImagingLib.filter(this, raster, writableRaster) != null) {
            return writableRaster;
        }
        if (this.ltable instanceof ByteLookupTable) {
            byteFilter((ByteLookupTable) this.ltable, raster, writableRaster, width, height, numBands);
        } else if (this.ltable instanceof ShortLookupTable) {
            shortFilter((ShortLookupTable) this.ltable, raster, writableRaster, width, height, numBands);
        } else {
            int minX = raster.getMinX();
            int minY = raster.getMinY();
            int minX2 = writableRaster.getMinX();
            int minY2 = writableRaster.getMinY();
            int i2 = 0;
            while (i2 < height) {
                int i3 = minX;
                int i4 = minX2;
                int i5 = 0;
                while (i5 < width) {
                    raster.getPixel(i3, minY, iArr);
                    this.ltable.lookupPixel(iArr, iArr);
                    writableRaster.setPixel(i4, minY2, iArr);
                    i5++;
                    i3++;
                    i4++;
                }
                i2++;
                minY++;
                minY2++;
            }
        }
        return writableRaster;
    }

    @Override // java.awt.image.BufferedImageOp
    public final Rectangle2D getBounds2D(BufferedImage bufferedImage) {
        return getBounds2D(bufferedImage.getRaster());
    }

    @Override // java.awt.image.RasterOp
    public final Rectangle2D getBounds2D(Raster raster) {
        return raster.getBounds();
    }

    @Override // java.awt.image.BufferedImageOp
    public BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
        BufferedImage bufferedImage2;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        int i2 = 0;
        if (colorModel == null) {
            ColorModel colorModel2 = bufferedImage.getColorModel();
            WritableRaster raster = bufferedImage.getRaster();
            if (colorModel2 instanceof ComponentColorModel) {
                DataBuffer dataBuffer = raster.getDataBuffer();
                boolean zHasAlpha = colorModel2.hasAlpha();
                boolean zIsAlphaPremultiplied = colorModel2.isAlphaPremultiplied();
                int transparency = colorModel2.getTransparency();
                int[] iArr = null;
                if (this.ltable instanceof ByteLookupTable) {
                    if (dataBuffer.getDataType() == 1) {
                        if (zHasAlpha) {
                            iArr = new int[2];
                            if (transparency == 2) {
                                iArr[1] = 1;
                            } else {
                                iArr[1] = 8;
                            }
                        } else {
                            iArr = new int[1];
                        }
                        iArr[0] = 8;
                    }
                } else if (this.ltable instanceof ShortLookupTable) {
                    i2 = 1;
                    if (dataBuffer.getDataType() == 0) {
                        if (zHasAlpha) {
                            iArr = new int[2];
                            if (transparency == 2) {
                                iArr[1] = 1;
                            } else {
                                iArr[1] = 16;
                            }
                        } else {
                            iArr = new int[1];
                        }
                        iArr[0] = 16;
                    }
                }
                if (iArr != null) {
                    colorModel2 = new ComponentColorModel(colorModel2.getColorSpace(), iArr, zHasAlpha, zIsAlphaPremultiplied, transparency, i2);
                }
            }
            bufferedImage2 = new BufferedImage(colorModel2, colorModel2.createCompatibleWritableRaster(width, height), colorModel2.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        } else {
            bufferedImage2 = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(width, height), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        }
        return bufferedImage2;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster raster) {
        return raster.createCompatibleWritableRaster();
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Float();
        }
        point2D2.setLocation(point2D.getX(), point2D.getY());
        return point2D2;
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final RenderingHints getRenderingHints() {
        return this.hints;
    }

    private final void byteFilter(ByteLookupTable byteLookupTable, Raster raster, WritableRaster writableRaster, int i2, int i3, int i4) {
        int[] samples = null;
        byte[][] table = byteLookupTable.getTable();
        int offset = byteLookupTable.getOffset();
        int i5 = 1;
        if (table.length == 1) {
            i5 = 0;
        }
        int length = table[0].length;
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = 0;
            int i8 = 0;
            while (i8 < i4) {
                samples = raster.getSamples(0, i6, i2, 1, i8, samples);
                for (int i9 = 0; i9 < i2; i9++) {
                    int i10 = samples[i9] - offset;
                    if (i10 < 0 || i10 > length) {
                        throw new IllegalArgumentException("index (" + i10 + "(out of range:  srcPix[" + i9 + "]=" + samples[i9] + " offset=" + offset);
                    }
                    samples[i9] = table[i7][i10];
                }
                writableRaster.setSamples(0, i6, i2, 1, i8, samples);
                i8++;
                i7 += i5;
            }
        }
    }

    private final void shortFilter(ShortLookupTable shortLookupTable, Raster raster, WritableRaster writableRaster, int i2, int i3, int i4) {
        int[] samples = null;
        short[][] table = shortLookupTable.getTable();
        int offset = shortLookupTable.getOffset();
        int i5 = 1;
        if (table.length == 1) {
            i5 = 0;
        }
        for (int i6 = 0; i6 < i3; i6++) {
            int i7 = 0;
            int i8 = 0;
            while (i8 < i4) {
                samples = raster.getSamples(0, i6, i2, 1, i8, samples);
                for (int i9 = 0; i9 < i2; i9++) {
                    int i10 = samples[i9] - offset;
                    if (i10 < 0 || i10 > 65535) {
                        throw new IllegalArgumentException("index out of range " + i10 + " x is " + i9 + "srcPix[x]=" + samples[i9] + " offset=" + offset);
                    }
                    samples[i9] = table[i7][i10];
                }
                writableRaster.setSamples(0, i6, i2, 1, i8, samples);
                i8++;
                i7 += i5;
            }
        }
    }
}
