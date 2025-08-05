package java.awt.image;

import java.awt.color.ColorSpace;

/* loaded from: rt.jar:java/awt/image/PackedColorModel.class */
public abstract class PackedColorModel extends ColorModel {
    int[] maskArray;
    int[] maskOffsets;
    float[] scaleFactors;

    public PackedColorModel(ColorSpace colorSpace, int i2, int[] iArr, int i3, boolean z2, int i4, int i5) {
        super(i2, createBitsArray(iArr, i3), colorSpace, i3 != 0, z2, i4, i5);
        if (i2 < 1 || i2 > 32) {
            throw new IllegalArgumentException("Number of bits must be between 1 and 32.");
        }
        this.maskArray = new int[this.numComponents];
        this.maskOffsets = new int[this.numComponents];
        this.scaleFactors = new float[this.numComponents];
        for (int i6 = 0; i6 < this.numColorComponents; i6++) {
            DecomposeMask(iArr[i6], i6, colorSpace.getName(i6));
        }
        if (i3 != 0) {
            DecomposeMask(i3, this.numColorComponents, "alpha");
            if (this.nBits[this.numComponents - 1] == 1) {
                this.transparency = 2;
            }
        }
    }

    public PackedColorModel(ColorSpace colorSpace, int i2, int i3, int i4, int i5, int i6, boolean z2, int i7, int i8) {
        super(i2, createBitsArray(i3, i4, i5, i6), colorSpace, i6 != 0, z2, i7, i8);
        if (colorSpace.getType() != 5) {
            throw new IllegalArgumentException("ColorSpace must be TYPE_RGB.");
        }
        this.maskArray = new int[this.numComponents];
        this.maskOffsets = new int[this.numComponents];
        this.scaleFactors = new float[this.numComponents];
        DecomposeMask(i3, 0, "red");
        DecomposeMask(i4, 1, "green");
        DecomposeMask(i5, 2, "blue");
        if (i6 != 0) {
            DecomposeMask(i6, 3, "alpha");
            if (this.nBits[3] == 1) {
                this.transparency = 2;
            }
        }
    }

    public final int getMask(int i2) {
        return this.maskArray[i2];
    }

    public final int[] getMasks() {
        return (int[]) this.maskArray.clone();
    }

    private void DecomposeMask(int i2, int i3, String str) {
        int i4 = 0;
        int i5 = this.nBits[i3];
        this.maskArray[i3] = i2;
        if (i2 != 0) {
            while ((i2 & 1) == 0) {
                i2 >>>= 1;
                i4++;
            }
        }
        if (i4 + i5 > this.pixel_bits) {
            throw new IllegalArgumentException(str + " mask " + Integer.toHexString(this.maskArray[i3]) + " overflows pixel (expecting " + this.pixel_bits + " bits");
        }
        this.maskOffsets[i3] = i4;
        if (i5 == 0) {
            this.scaleFactors[i3] = 256.0f;
        } else {
            this.scaleFactors[i3] = 255.0f / ((1 << i5) - 1);
        }
    }

    @Override // java.awt.image.ColorModel
    public SampleModel createCompatibleSampleModel(int i2, int i3) {
        return new SinglePixelPackedSampleModel(this.transferType, i2, i3, this.maskArray);
    }

    @Override // java.awt.image.ColorModel
    public boolean isCompatibleSampleModel(SampleModel sampleModel) {
        if (!(sampleModel instanceof SinglePixelPackedSampleModel) || this.numComponents != sampleModel.getNumBands() || sampleModel.getTransferType() != this.transferType) {
            return false;
        }
        int[] bitMasks = ((SinglePixelPackedSampleModel) sampleModel).getBitMasks();
        if (bitMasks.length != this.maskArray.length) {
            return false;
        }
        int dataTypeSize = (int) ((1 << DataBuffer.getDataTypeSize(this.transferType)) - 1);
        for (int i2 = 0; i2 < bitMasks.length; i2++) {
            if ((dataTypeSize & bitMasks[i2]) != (dataTypeSize & this.maskArray[i2])) {
                return false;
            }
        }
        return true;
    }

    @Override // java.awt.image.ColorModel
    public WritableRaster getAlphaRaster(WritableRaster writableRaster) {
        if (!hasAlpha()) {
            return null;
        }
        int minX = writableRaster.getMinX();
        int minY = writableRaster.getMinY();
        return writableRaster.createWritableChild(minX, minY, writableRaster.getWidth(), writableRaster.getHeight(), minX, minY, new int[]{writableRaster.getNumBands() - 1});
    }

    @Override // java.awt.image.ColorModel
    public boolean equals(Object obj) {
        PackedColorModel packedColorModel;
        int numComponents;
        if (!(obj instanceof PackedColorModel) || !super.equals(obj) || (numComponents = (packedColorModel = (PackedColorModel) obj).getNumComponents()) != this.numComponents) {
            return false;
        }
        for (int i2 = 0; i2 < numComponents; i2++) {
            if (this.maskArray[i2] != packedColorModel.getMask(i2)) {
                return false;
            }
        }
        return true;
    }

    private static final int[] createBitsArray(int[] iArr, int i2) {
        int length = iArr.length;
        int[] iArr2 = new int[length + (i2 == 0 ? 0 : 1)];
        for (int i3 = 0; i3 < length; i3++) {
            iArr2[i3] = countBits(iArr[i3]);
            if (iArr2[i3] < 0) {
                throw new IllegalArgumentException("Noncontiguous color mask (" + Integer.toHexString(iArr[i3]) + "at index " + i3);
            }
        }
        if (i2 != 0) {
            iArr2[length] = countBits(i2);
            if (iArr2[length] < 0) {
                throw new IllegalArgumentException("Noncontiguous alpha mask (" + Integer.toHexString(i2));
            }
        }
        return iArr2;
    }

    private static final int[] createBitsArray(int i2, int i3, int i4, int i5) {
        int[] iArr = new int[3 + (i5 == 0 ? 0 : 1)];
        iArr[0] = countBits(i2);
        iArr[1] = countBits(i3);
        iArr[2] = countBits(i4);
        if (iArr[0] < 0) {
            throw new IllegalArgumentException("Noncontiguous red mask (" + Integer.toHexString(i2));
        }
        if (iArr[1] < 0) {
            throw new IllegalArgumentException("Noncontiguous green mask (" + Integer.toHexString(i3));
        }
        if (iArr[2] < 0) {
            throw new IllegalArgumentException("Noncontiguous blue mask (" + Integer.toHexString(i4));
        }
        if (i5 != 0) {
            iArr[3] = countBits(i5);
            if (iArr[3] < 0) {
                throw new IllegalArgumentException("Noncontiguous alpha mask (" + Integer.toHexString(i5));
            }
        }
        return iArr;
    }

    private static final int countBits(int i2) {
        int i3 = 0;
        if (i2 != 0) {
            while ((i2 & 1) == 0) {
                i2 >>>= 1;
            }
            while ((i2 & 1) == 1) {
                i2 >>>= 1;
                i3++;
            }
        }
        if (i2 != 0) {
            return -1;
        }
        return i3;
    }
}
