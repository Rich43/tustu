package java.awt.image;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import sun.awt.image.ImagingLib;

/* loaded from: rt.jar:java/awt/image/RescaleOp.class */
public class RescaleOp implements BufferedImageOp, RasterOp {
    float[] scaleFactors;
    float[] offsets;
    int length;
    RenderingHints hints;
    private int srcNbits;
    private int dstNbits;

    public RescaleOp(float[] fArr, float[] fArr2, RenderingHints renderingHints) {
        this.length = 0;
        this.length = fArr.length;
        if (this.length > fArr2.length) {
            this.length = fArr2.length;
        }
        this.scaleFactors = new float[this.length];
        this.offsets = new float[this.length];
        for (int i2 = 0; i2 < this.length; i2++) {
            this.scaleFactors[i2] = fArr[i2];
            this.offsets[i2] = fArr2[i2];
        }
        this.hints = renderingHints;
    }

    public RescaleOp(float f2, float f3, RenderingHints renderingHints) {
        this.length = 0;
        this.length = 1;
        this.scaleFactors = new float[1];
        this.offsets = new float[1];
        this.scaleFactors[0] = f2;
        this.offsets[0] = f3;
        this.hints = renderingHints;
    }

    public final float[] getScaleFactors(float[] fArr) {
        if (fArr == null) {
            return (float[]) this.scaleFactors.clone();
        }
        System.arraycopy(this.scaleFactors, 0, fArr, 0, Math.min(this.scaleFactors.length, fArr.length));
        return fArr;
    }

    public final float[] getOffsets(float[] fArr) {
        if (fArr == null) {
            return (float[]) this.offsets.clone();
        }
        System.arraycopy(this.offsets, 0, fArr, 0, Math.min(this.offsets.length, fArr.length));
        return fArr;
    }

    public final int getNumFactors() {
        return this.length;
    }

    private ByteLookupTable createByteLut(float[] fArr, float[] fArr2, int i2, int i3) {
        byte[][] bArr = new byte[i2][i3];
        int i4 = 0;
        while (i4 < fArr.length) {
            float f2 = fArr[i4];
            float f3 = fArr2[i4];
            byte[] bArr2 = bArr[i4];
            for (int i5 = 0; i5 < i3; i5++) {
                int i6 = (int) ((i5 * f2) + f3);
                if ((i6 & (-256)) != 0) {
                    if (i6 < 0) {
                        i6 = 0;
                    } else {
                        i6 = 255;
                    }
                }
                bArr2[i5] = (byte) i6;
            }
            i4++;
        }
        int i7 = (i2 == 4 && fArr.length == 4) ? 4 : 3;
        while (i4 < bArr.length && i4 < i7) {
            System.arraycopy(bArr[i4 - 1], 0, bArr[i4], 0, i3);
            i4++;
        }
        if (i2 == 4 && i4 < i2) {
            byte[] bArr3 = bArr[i4];
            for (int i8 = 0; i8 < i3; i8++) {
                bArr3[i8] = (byte) i8;
            }
        }
        return new ByteLookupTable(0, bArr);
    }

    private ShortLookupTable createShortLut(float[] fArr, float[] fArr2, int i2, int i3) {
        short[][] sArr = new short[i2][i3];
        int i4 = 0;
        while (i4 < fArr.length) {
            float f2 = fArr[i4];
            float f3 = fArr2[i4];
            short[] sArr2 = sArr[i4];
            for (int i5 = 0; i5 < i3; i5++) {
                int i6 = (int) ((i5 * f2) + f3);
                if ((i6 & DTMManager.IDENT_DTM_DEFAULT) != 0) {
                    if (i6 < 0) {
                        i6 = 0;
                    } else {
                        i6 = 65535;
                    }
                }
                sArr2[i5] = (short) i6;
            }
            i4++;
        }
        int i7 = (i2 == 4 && fArr.length == 4) ? 4 : 3;
        while (i4 < sArr.length && i4 < i7) {
            System.arraycopy(sArr[i4 - 1], 0, sArr[i4], 0, i3);
            i4++;
        }
        if (i2 == 4 && i4 < i2) {
            short[] sArr3 = sArr[i4];
            for (int i8 = 0; i8 < i3; i8++) {
                sArr3[i8] = (short) i8;
            }
        }
        return new ShortLookupTable(0, sArr);
    }

    private boolean canUseLookup(Raster raster, Raster raster2) {
        int dataType = raster.getDataBuffer().getDataType();
        if (dataType != 0 && dataType != 1) {
            return false;
        }
        SampleModel sampleModel = raster2.getSampleModel();
        this.dstNbits = sampleModel.getSampleSize(0);
        if (this.dstNbits != 8 && this.dstNbits != 16) {
            return false;
        }
        for (int i2 = 1; i2 < raster.getNumBands(); i2++) {
            if (sampleModel.getSampleSize(i2) != this.dstNbits) {
                return false;
            }
        }
        SampleModel sampleModel2 = raster.getSampleModel();
        this.srcNbits = sampleModel2.getSampleSize(0);
        if (this.srcNbits > 16) {
            return false;
        }
        for (int i3 = 1; i3 < raster.getNumBands(); i3++) {
            if (sampleModel2.getSampleSize(i3) != this.srcNbits) {
                return false;
            }
        }
        if ((sampleModel instanceof ComponentSampleModel) && ((ComponentSampleModel) sampleModel).getPixelStride() != raster2.getNumBands()) {
            return false;
        }
        if ((sampleModel2 instanceof ComponentSampleModel) && ((ComponentSampleModel) sampleModel2).getPixelStride() != raster.getNumBands()) {
            return false;
        }
        return true;
    }

    @Override // java.awt.image.BufferedImageOp
    public final BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        ColorModel colorModel = bufferedImage.getColorModel();
        int numColorComponents = colorModel.getNumColorComponents();
        int i2 = this.length;
        if (colorModel instanceof IndexColorModel) {
            throw new IllegalArgumentException("Rescaling cannot be performed on an indexed image");
        }
        if (i2 != 1 && i2 != numColorComponents && i2 != colorModel.getNumComponents()) {
            throw new IllegalArgumentException("Number of scaling constants does not equal the number of of color or color/alpha  components");
        }
        boolean z2 = false;
        boolean z3 = false;
        if (i2 > numColorComponents && colorModel.hasAlpha()) {
            i2 = numColorComponents + 1;
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        if (bufferedImage2 == null) {
            bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
        } else {
            if (width != bufferedImage2.getWidth()) {
                throw new IllegalArgumentException("Src width (" + width + ") not equal to dst width (" + bufferedImage2.getWidth() + ")");
            }
            if (height != bufferedImage2.getHeight()) {
                throw new IllegalArgumentException("Src height (" + height + ") not equal to dst height (" + bufferedImage2.getHeight() + ")");
            }
            if (colorModel.getColorSpace().getType() != bufferedImage2.getColorModel().getColorSpace().getType()) {
                z2 = true;
                bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            }
        }
        if (ImagingLib.filter(this, bufferedImage, bufferedImage2) == null) {
            if (bufferedImage.getRaster().getNumBands() != bufferedImage2.getRaster().getNumBands()) {
                z3 = true;
                bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            }
            filterRasterImpl(bufferedImage.getRaster(), bufferedImage2.getRaster(), i2, false);
        }
        if (z3) {
            Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
            graphics2DCreateGraphics.setComposite(AlphaComposite.Src);
            graphics2DCreateGraphics.drawImage(bufferedImage2, 0, 0, width, height, null);
            graphics2DCreateGraphics.dispose();
        }
        if (z2) {
            bufferedImage2 = new ColorConvertOp(this.hints).filter(bufferedImage2, bufferedImage2);
        }
        return bufferedImage2;
    }

    @Override // java.awt.image.RasterOp
    public final WritableRaster filter(Raster raster, WritableRaster writableRaster) {
        return filterRasterImpl(raster, writableRaster, this.length, true);
    }

    private WritableRaster filterRasterImpl(Raster raster, WritableRaster writableRaster, int i2, boolean z2) {
        int i3;
        int numBands = raster.getNumBands();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int[] pixel = null;
        int i4 = 0;
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        } else {
            if (height != writableRaster.getHeight() || width != writableRaster.getWidth()) {
                throw new IllegalArgumentException("Width or height of Rasters do not match");
            }
            if (numBands != writableRaster.getNumBands()) {
                throw new IllegalArgumentException("Number of bands in src " + numBands + " does not equal number of bands in dest " + writableRaster.getNumBands());
            }
        }
        if (z2 && i2 != 1 && i2 != raster.getNumBands()) {
            throw new IllegalArgumentException("Number of scaling constants does not equal the number of of bands in the src raster");
        }
        if (ImagingLib.filter(this, raster, writableRaster) != null) {
            return writableRaster;
        }
        if (canUseLookup(raster, writableRaster)) {
            int i5 = 1 << this.srcNbits;
            if ((1 << this.dstNbits) == 256) {
                new LookupOp(createByteLut(this.scaleFactors, this.offsets, numBands, i5), this.hints).filter(raster, writableRaster);
            } else {
                new LookupOp(createShortLut(this.scaleFactors, this.offsets, numBands, i5), this.hints).filter(raster, writableRaster);
            }
        } else {
            if (i2 > 1) {
                i4 = 1;
            }
            int minX = raster.getMinX();
            int minY = raster.getMinY();
            int minX2 = writableRaster.getMinX();
            int minY2 = writableRaster.getMinY();
            int[] iArr = new int[numBands];
            int[] iArr2 = new int[numBands];
            SampleModel sampleModel = writableRaster.getSampleModel();
            for (int i6 = 0; i6 < numBands; i6++) {
                iArr[i6] = (1 << sampleModel.getSampleSize(i6)) - 1;
                iArr2[i6] = iArr[i6] ^ (-1);
            }
            int i7 = 0;
            while (i7 < height) {
                int i8 = minX2;
                int i9 = minX;
                int i10 = 0;
                while (i10 < width) {
                    pixel = raster.getPixel(i9, minY, pixel);
                    int i11 = 0;
                    int i12 = 0;
                    while (i12 < numBands) {
                        if ((i2 == 1 || i2 == 3) && i12 == 3 && numBands == 4) {
                            i3 = pixel[i12];
                        } else {
                            i3 = (int) ((pixel[i12] * this.scaleFactors[i11]) + this.offsets[i11]);
                        }
                        if ((i3 & iArr2[i12]) != 0) {
                            if (i3 < 0) {
                                i3 = 0;
                            } else {
                                i3 = iArr[i12];
                            }
                        }
                        pixel[i12] = i3;
                        i12++;
                        i11 += i4;
                    }
                    writableRaster.setPixel(i8, minY2, pixel);
                    i10++;
                    i9++;
                    i8++;
                }
                i7++;
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
        if (colorModel == null) {
            ColorModel colorModel2 = bufferedImage.getColorModel();
            bufferedImage2 = new BufferedImage(colorModel2, bufferedImage.getRaster().createCompatibleWritableRaster(), colorModel2.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        } else {
            bufferedImage2 = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight()), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        }
        return bufferedImage2;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster raster) {
        return raster.createCompatibleWritableRaster(raster.getWidth(), raster.getHeight());
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
}
