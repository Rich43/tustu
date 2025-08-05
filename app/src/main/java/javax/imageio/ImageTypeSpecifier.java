package javax.imageio;

import java.awt.Point;
import java.awt.color.ColorSpace;
import java.awt.image.BandedSampleModel;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.SampleModel;
import java.util.Hashtable;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:javax/imageio/ImageTypeSpecifier.class */
public class ImageTypeSpecifier {
    protected ColorModel colorModel;
    protected SampleModel sampleModel;
    private static ColorSpace sRGB = ColorSpace.getInstance(1000);
    private static ImageTypeSpecifier[] BISpecifier = new ImageTypeSpecifier[14];

    private ImageTypeSpecifier() {
    }

    public ImageTypeSpecifier(ColorModel colorModel, SampleModel sampleModel) {
        if (colorModel == null) {
            throw new IllegalArgumentException("colorModel == null!");
        }
        if (sampleModel == null) {
            throw new IllegalArgumentException("sampleModel == null!");
        }
        if (!colorModel.isCompatibleSampleModel(sampleModel)) {
            throw new IllegalArgumentException("sampleModel is incompatible with colorModel!");
        }
        this.colorModel = colorModel;
        this.sampleModel = sampleModel;
    }

    public ImageTypeSpecifier(RenderedImage renderedImage) {
        if (renderedImage == null) {
            throw new IllegalArgumentException("image == null!");
        }
        this.colorModel = renderedImage.getColorModel();
        this.sampleModel = renderedImage.getSampleModel();
    }

    /* loaded from: rt.jar:javax/imageio/ImageTypeSpecifier$Packed.class */
    static class Packed extends ImageTypeSpecifier {
        ColorSpace colorSpace;
        int redMask;
        int greenMask;
        int blueMask;
        int alphaMask;
        int transferType;
        boolean isAlphaPremultiplied;

        public Packed(ColorSpace colorSpace, int i2, int i3, int i4, int i5, int i6, boolean z2) {
            super();
            if (colorSpace == null) {
                throw new IllegalArgumentException("colorSpace == null!");
            }
            if (colorSpace.getType() != 5) {
                throw new IllegalArgumentException("colorSpace is not of type TYPE_RGB!");
            }
            if (i6 != 0 && i6 != 1 && i6 != 3) {
                throw new IllegalArgumentException("Bad value for transferType!");
            }
            if (i2 == 0 && i3 == 0 && i4 == 0 && i5 == 0) {
                throw new IllegalArgumentException("No mask has at least 1 bit set!");
            }
            this.colorSpace = colorSpace;
            this.redMask = i2;
            this.greenMask = i3;
            this.blueMask = i4;
            this.alphaMask = i5;
            this.transferType = i6;
            this.isAlphaPremultiplied = z2;
            this.colorModel = new DirectColorModel(colorSpace, 32, i2, i3, i4, i5, z2, i6);
            this.sampleModel = this.colorModel.createCompatibleSampleModel(1, 1);
        }
    }

    public static ImageTypeSpecifier createPacked(ColorSpace colorSpace, int i2, int i3, int i4, int i5, int i6, boolean z2) {
        return new Packed(colorSpace, i2, i3, i4, i5, i6, z2);
    }

    static ColorModel createComponentCM(ColorSpace colorSpace, int i2, int i3, boolean z2, boolean z3) {
        int i4 = z2 ? 3 : 1;
        int[] iArr = new int[i2];
        int dataTypeSize = DataBuffer.getDataTypeSize(i3);
        for (int i5 = 0; i5 < i2; i5++) {
            iArr[i5] = dataTypeSize;
        }
        return new ComponentColorModel(colorSpace, iArr, z2, z3, i4, i3);
    }

    /* loaded from: rt.jar:javax/imageio/ImageTypeSpecifier$Interleaved.class */
    static class Interleaved extends ImageTypeSpecifier {
        ColorSpace colorSpace;
        int[] bandOffsets;
        int dataType;
        boolean hasAlpha;
        boolean isAlphaPremultiplied;

        public Interleaved(ColorSpace colorSpace, int[] iArr, int i2, boolean z2, boolean z3) {
            super();
            if (colorSpace == null) {
                throw new IllegalArgumentException("colorSpace == null!");
            }
            if (iArr == null) {
                throw new IllegalArgumentException("bandOffsets == null!");
            }
            if (iArr.length != colorSpace.getNumComponents() + (z2 ? 1 : 0)) {
                throw new IllegalArgumentException("bandOffsets.length is wrong!");
            }
            if (i2 != 0 && i2 != 2 && i2 != 1 && i2 != 3 && i2 != 4 && i2 != 5) {
                throw new IllegalArgumentException("Bad value for dataType!");
            }
            this.colorSpace = colorSpace;
            this.bandOffsets = (int[]) iArr.clone();
            this.dataType = i2;
            this.hasAlpha = z2;
            this.isAlphaPremultiplied = z3;
            this.colorModel = ImageTypeSpecifier.createComponentCM(colorSpace, iArr.length, i2, z2, z3);
            int iMin = iArr[0];
            int iMax = iMin;
            for (int i3 : iArr) {
                iMin = Math.min(i3, iMin);
                iMax = Math.max(i3, iMax);
            }
            int i4 = (iMax - iMin) + 1;
            this.sampleModel = new PixelInterleavedSampleModel(i2, 1, 1, i4, 1 * i4, iArr);
        }

        @Override // javax.imageio.ImageTypeSpecifier
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Interleaved)) {
                return false;
            }
            Interleaved interleaved = (Interleaved) obj;
            if (!this.colorSpace.equals(interleaved.colorSpace) || this.dataType != interleaved.dataType || this.hasAlpha != interleaved.hasAlpha || this.isAlphaPremultiplied != interleaved.isAlphaPremultiplied || this.bandOffsets.length != interleaved.bandOffsets.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.bandOffsets.length; i2++) {
                if (this.bandOffsets[i2] != interleaved.bandOffsets[i2]) {
                    return false;
                }
            }
            return true;
        }

        @Override // javax.imageio.ImageTypeSpecifier
        public int hashCode() {
            return super.hashCode() + (4 * this.bandOffsets.length) + (25 * this.dataType) + (this.hasAlpha ? 17 : 18);
        }
    }

    public static ImageTypeSpecifier createInterleaved(ColorSpace colorSpace, int[] iArr, int i2, boolean z2, boolean z3) {
        return new Interleaved(colorSpace, iArr, i2, z2, z3);
    }

    /* loaded from: rt.jar:javax/imageio/ImageTypeSpecifier$Banded.class */
    static class Banded extends ImageTypeSpecifier {
        ColorSpace colorSpace;
        int[] bankIndices;
        int[] bandOffsets;
        int dataType;
        boolean hasAlpha;
        boolean isAlphaPremultiplied;

        public Banded(ColorSpace colorSpace, int[] iArr, int[] iArr2, int i2, boolean z2, boolean z3) {
            super();
            if (colorSpace == null) {
                throw new IllegalArgumentException("colorSpace == null!");
            }
            if (iArr == null) {
                throw new IllegalArgumentException("bankIndices == null!");
            }
            if (iArr2 == null) {
                throw new IllegalArgumentException("bandOffsets == null!");
            }
            if (iArr.length != iArr2.length) {
                throw new IllegalArgumentException("bankIndices.length != bandOffsets.length!");
            }
            if (i2 != 0 && i2 != 2 && i2 != 1 && i2 != 3 && i2 != 4 && i2 != 5) {
                throw new IllegalArgumentException("Bad value for dataType!");
            }
            if (iArr2.length != colorSpace.getNumComponents() + (z2 ? 1 : 0)) {
                throw new IllegalArgumentException("bandOffsets.length is wrong!");
            }
            this.colorSpace = colorSpace;
            this.bankIndices = (int[]) iArr.clone();
            this.bandOffsets = (int[]) iArr2.clone();
            this.dataType = i2;
            this.hasAlpha = z2;
            this.isAlphaPremultiplied = z3;
            this.colorModel = ImageTypeSpecifier.createComponentCM(colorSpace, iArr.length, i2, z2, z3);
            this.sampleModel = new BandedSampleModel(i2, 1, 1, 1, iArr, iArr2);
        }

        @Override // javax.imageio.ImageTypeSpecifier
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Banded)) {
                return false;
            }
            Banded banded = (Banded) obj;
            if (!this.colorSpace.equals(banded.colorSpace) || this.dataType != banded.dataType || this.hasAlpha != banded.hasAlpha || this.isAlphaPremultiplied != banded.isAlphaPremultiplied || this.bankIndices.length != banded.bankIndices.length || this.bandOffsets.length != banded.bandOffsets.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.bankIndices.length; i2++) {
                if (this.bankIndices[i2] != banded.bankIndices[i2]) {
                    return false;
                }
            }
            for (int i3 = 0; i3 < this.bandOffsets.length; i3++) {
                if (this.bandOffsets[i3] != banded.bandOffsets[i3]) {
                    return false;
                }
            }
            return true;
        }

        @Override // javax.imageio.ImageTypeSpecifier
        public int hashCode() {
            return super.hashCode() + (3 * this.bandOffsets.length) + (7 * this.bankIndices.length) + (21 * this.dataType) + (this.hasAlpha ? 19 : 29);
        }
    }

    public static ImageTypeSpecifier createBanded(ColorSpace colorSpace, int[] iArr, int[] iArr2, int i2, boolean z2, boolean z3) {
        return new Banded(colorSpace, iArr, iArr2, i2, z2, z3);
    }

    /* loaded from: rt.jar:javax/imageio/ImageTypeSpecifier$Grayscale.class */
    static class Grayscale extends ImageTypeSpecifier {
        int bits;
        int dataType;
        boolean isSigned;
        boolean hasAlpha;
        boolean isAlphaPremultiplied;

        public Grayscale(int i2, int i3, boolean z2, boolean z3, boolean z4) {
            super();
            if (i2 != 1 && i2 != 2 && i2 != 4 && i2 != 8 && i2 != 16) {
                throw new IllegalArgumentException("Bad value for bits!");
            }
            if (i3 != 0 && i3 != 2 && i3 != 1) {
                throw new IllegalArgumentException("Bad value for dataType!");
            }
            if (i2 > 8 && i3 == 0) {
                throw new IllegalArgumentException("Too many bits for dataType!");
            }
            this.bits = i2;
            this.dataType = i3;
            this.isSigned = z2;
            this.hasAlpha = z3;
            this.isAlphaPremultiplied = z4;
            ColorSpace colorSpace = ColorSpace.getInstance(1003);
            if ((i2 == 8 && i3 == 0) || (i2 == 16 && (i3 == 2 || i3 == 1))) {
                int i4 = z3 ? 2 : 1;
                int i5 = z3 ? 3 : 1;
                int[] iArr = new int[i4];
                iArr[0] = i2;
                if (i4 == 2) {
                    iArr[1] = i2;
                }
                this.colorModel = new ComponentColorModel(colorSpace, iArr, z3, z4, i5, i3);
                int[] iArr2 = new int[i4];
                iArr2[0] = 0;
                if (i4 == 2) {
                    iArr2[1] = 1;
                }
                this.sampleModel = new PixelInterleavedSampleModel(i3, 1, 1, i4, 1 * i4, iArr2);
                return;
            }
            int i6 = 1 << i2;
            byte[] bArr = new byte[i6];
            for (int i7 = 0; i7 < i6; i7++) {
                bArr[i7] = (byte) ((i7 * 255) / (i6 - 1));
            }
            this.colorModel = new IndexColorModel(i2, i6, bArr, bArr, bArr);
            this.sampleModel = new MultiPixelPackedSampleModel(i3, 1, 1, i2);
        }
    }

    public static ImageTypeSpecifier createGrayscale(int i2, int i3, boolean z2) {
        return new Grayscale(i2, i3, z2, false, false);
    }

    public static ImageTypeSpecifier createGrayscale(int i2, int i3, boolean z2, boolean z3) {
        return new Grayscale(i2, i3, z2, true, z3);
    }

    /* loaded from: rt.jar:javax/imageio/ImageTypeSpecifier$Indexed.class */
    static class Indexed extends ImageTypeSpecifier {
        byte[] redLUT;
        byte[] greenLUT;
        byte[] blueLUT;
        byte[] alphaLUT;
        int bits;
        int dataType;

        public Indexed(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i2, int i3) {
            super();
            this.alphaLUT = null;
            if (bArr == null || bArr2 == null || bArr3 == null) {
                throw new IllegalArgumentException("LUT is null!");
            }
            if (i2 != 1 && i2 != 2 && i2 != 4 && i2 != 8 && i2 != 16) {
                throw new IllegalArgumentException("Bad value for bits!");
            }
            if (i3 != 0 && i3 != 2 && i3 != 1 && i3 != 3) {
                throw new IllegalArgumentException("Bad value for dataType!");
            }
            if ((i2 > 8 && i3 == 0) || (i2 > 16 && i3 != 3)) {
                throw new IllegalArgumentException("Too many bits for dataType!");
            }
            int i4 = 1 << i2;
            if (bArr.length != i4 || bArr2.length != i4 || bArr3.length != i4 || (bArr4 != null && bArr4.length != i4)) {
                throw new IllegalArgumentException("LUT has improper length!");
            }
            this.redLUT = (byte[]) bArr.clone();
            this.greenLUT = (byte[]) bArr2.clone();
            this.blueLUT = (byte[]) bArr3.clone();
            if (bArr4 != null) {
                this.alphaLUT = (byte[]) bArr4.clone();
            }
            this.bits = i2;
            this.dataType = i3;
            if (bArr4 == null) {
                this.colorModel = new IndexColorModel(i2, bArr.length, bArr, bArr2, bArr3);
            } else {
                this.colorModel = new IndexColorModel(i2, bArr.length, bArr, bArr2, bArr3, bArr4);
            }
            if ((i2 == 8 && i3 == 0) || (i2 == 16 && (i3 == 2 || i3 == 1))) {
                this.sampleModel = new PixelInterleavedSampleModel(i3, 1, 1, 1, 1, new int[]{0});
            } else {
                this.sampleModel = new MultiPixelPackedSampleModel(i3, 1, 1, i2);
            }
        }
    }

    public static ImageTypeSpecifier createIndexed(byte[] bArr, byte[] bArr2, byte[] bArr3, byte[] bArr4, int i2, int i3) {
        return new Indexed(bArr, bArr2, bArr3, bArr4, i2, i3);
    }

    public static ImageTypeSpecifier createFromBufferedImageType(int i2) {
        if (i2 >= 1 && i2 <= 13) {
            return getSpecifier(i2);
        }
        if (i2 == 0) {
            throw new IllegalArgumentException("Cannot create from TYPE_CUSTOM!");
        }
        throw new IllegalArgumentException("Invalid BufferedImage type!");
    }

    public static ImageTypeSpecifier createFromRenderedImage(RenderedImage renderedImage) {
        int type;
        if (renderedImage == null) {
            throw new IllegalArgumentException("image == null!");
        }
        if ((renderedImage instanceof BufferedImage) && (type = ((BufferedImage) renderedImage).getType()) != 0) {
            return getSpecifier(type);
        }
        return new ImageTypeSpecifier(renderedImage);
    }

    public int getBufferedImageType() {
        return createBufferedImage(1, 1).getType();
    }

    public int getNumComponents() {
        return this.colorModel.getNumComponents();
    }

    public int getNumBands() {
        return this.sampleModel.getNumBands();
    }

    public int getBitsPerBand(int i2) {
        if ((i2 < 0) | (i2 >= getNumBands())) {
            throw new IllegalArgumentException("band out of range!");
        }
        return this.sampleModel.getSampleSize(i2);
    }

    public SampleModel getSampleModel() {
        return this.sampleModel;
    }

    public SampleModel getSampleModel(int i2, int i3) {
        if (i2 * i3 > 2147483647L) {
            throw new IllegalArgumentException("width*height > Integer.MAX_VALUE!");
        }
        return this.sampleModel.createCompatibleSampleModel(i2, i3);
    }

    public ColorModel getColorModel() {
        return this.colorModel;
    }

    public BufferedImage createBufferedImage(int i2, int i3) {
        try {
            return new BufferedImage(this.colorModel, Raster.createWritableRaster(getSampleModel(i2, i3), new Point(0, 0)), this.colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) new Hashtable());
        } catch (NegativeArraySizeException e2) {
            throw new IllegalArgumentException("Array size > Integer.MAX_VALUE!");
        }
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof ImageTypeSpecifier)) {
            return false;
        }
        ImageTypeSpecifier imageTypeSpecifier = (ImageTypeSpecifier) obj;
        return this.colorModel.equals(imageTypeSpecifier.colorModel) && this.sampleModel.equals(imageTypeSpecifier.sampleModel);
    }

    public int hashCode() {
        return (9 * this.colorModel.hashCode()) + (14 * this.sampleModel.hashCode());
    }

    private static ImageTypeSpecifier getSpecifier(int i2) {
        if (BISpecifier[i2] == null) {
            BISpecifier[i2] = createSpecifier(i2);
        }
        return BISpecifier[i2];
    }

    private static ImageTypeSpecifier createSpecifier(int i2) {
        switch (i2) {
            case 1:
                return createPacked(sRGB, 16711680, NormalizerImpl.CC_MASK, 255, 0, 3, false);
            case 2:
                return createPacked(sRGB, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, 3, false);
            case 3:
                return createPacked(sRGB, 16711680, NormalizerImpl.CC_MASK, 255, -16777216, 3, true);
            case 4:
                return createPacked(sRGB, 255, NormalizerImpl.CC_MASK, 16711680, 0, 3, false);
            case 5:
                return createInterleaved(sRGB, new int[]{2, 1, 0}, 0, false, false);
            case 6:
                return createInterleaved(sRGB, new int[]{3, 2, 1, 0}, 0, true, false);
            case 7:
                return createInterleaved(sRGB, new int[]{3, 2, 1, 0}, 0, true, true);
            case 8:
                return createPacked(sRGB, 63488, 2016, 31, 0, 1, false);
            case 9:
                return createPacked(sRGB, 31744, 992, 31, 0, 1, false);
            case 10:
                return createGrayscale(8, 0, false);
            case 11:
                return createGrayscale(16, 1, false);
            case 12:
                return createGrayscale(1, 0, false);
            case 13:
                IndexColorModel indexColorModel = (IndexColorModel) new BufferedImage(1, 1, 13).getColorModel();
                int mapSize = indexColorModel.getMapSize();
                byte[] bArr = new byte[mapSize];
                byte[] bArr2 = new byte[mapSize];
                byte[] bArr3 = new byte[mapSize];
                byte[] bArr4 = new byte[mapSize];
                indexColorModel.getReds(bArr);
                indexColorModel.getGreens(bArr2);
                indexColorModel.getBlues(bArr3);
                indexColorModel.getAlphas(bArr4);
                return createIndexed(bArr, bArr2, bArr3, bArr4, 8, 0);
            default:
                throw new IllegalArgumentException("Invalid BufferedImage type!");
        }
    }
}
