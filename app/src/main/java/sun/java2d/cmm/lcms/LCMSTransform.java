package sun.java2d.cmm.lcms;

import java.awt.color.CMMException;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import sun.java2d.cmm.ColorTransform;
import sun.java2d.cmm.ProfileDeferralMgr;
import sun.java2d.cmm.lcms.LCMSImageLayout;

/* loaded from: rt.jar:sun/java2d/cmm/lcms/LCMSTransform.class */
public class LCMSTransform implements ColorTransform {
    long ID;
    private int inFormatter;
    private boolean isInIntPacked;
    private int outFormatter;
    private boolean isOutIntPacked;
    ICC_Profile[] profiles;
    LCMSProfile[] lcmsProfiles;
    int renderType;
    int transformType;
    private int numInComponents;
    private int numOutComponents;
    private Object disposerReferent;

    static {
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
    }

    public LCMSTransform(ICC_Profile iCC_Profile, int i2, int i3) {
        this.inFormatter = 0;
        this.isInIntPacked = false;
        this.outFormatter = 0;
        this.isOutIntPacked = false;
        this.numInComponents = -1;
        this.numOutComponents = -1;
        this.disposerReferent = new Object();
        this.profiles = new ICC_Profile[1];
        this.profiles[0] = iCC_Profile;
        this.lcmsProfiles = new LCMSProfile[1];
        this.lcmsProfiles[0] = LCMS.getProfileID(iCC_Profile);
        this.renderType = i2 == -1 ? 0 : i2;
        this.transformType = i3;
        this.numInComponents = this.profiles[0].getNumComponents();
        this.numOutComponents = this.profiles[this.profiles.length - 1].getNumComponents();
    }

    public LCMSTransform(ColorTransform[] colorTransformArr) {
        this.inFormatter = 0;
        this.isInIntPacked = false;
        this.outFormatter = 0;
        this.isOutIntPacked = false;
        this.numInComponents = -1;
        this.numOutComponents = -1;
        this.disposerReferent = new Object();
        int length = 0;
        for (ColorTransform colorTransform : colorTransformArr) {
            length += ((LCMSTransform) colorTransform).profiles.length;
        }
        this.profiles = new ICC_Profile[length];
        this.lcmsProfiles = new LCMSProfile[length];
        int length2 = 0;
        for (ColorTransform colorTransform2 : colorTransformArr) {
            LCMSTransform lCMSTransform = (LCMSTransform) colorTransform2;
            System.arraycopy(lCMSTransform.profiles, 0, this.profiles, length2, lCMSTransform.profiles.length);
            System.arraycopy(lCMSTransform.lcmsProfiles, 0, this.lcmsProfiles, length2, lCMSTransform.lcmsProfiles.length);
            length2 += lCMSTransform.profiles.length;
        }
        this.renderType = ((LCMSTransform) colorTransformArr[0]).renderType;
        this.numInComponents = this.profiles[0].getNumComponents();
        this.numOutComponents = this.profiles[this.profiles.length - 1].getNumComponents();
    }

    @Override // sun.java2d.cmm.ColorTransform
    public int getNumInComponents() {
        return this.numInComponents;
    }

    @Override // sun.java2d.cmm.ColorTransform
    public int getNumOutComponents() {
        return this.numOutComponents;
    }

    private synchronized void doTransform(LCMSImageLayout lCMSImageLayout, LCMSImageLayout lCMSImageLayout2) {
        if (this.ID == 0 || this.inFormatter != lCMSImageLayout.pixelType || this.isInIntPacked != lCMSImageLayout.isIntPacked || this.outFormatter != lCMSImageLayout2.pixelType || this.isOutIntPacked != lCMSImageLayout2.isIntPacked) {
            if (this.ID != 0) {
                this.disposerReferent = new Object();
            }
            this.inFormatter = lCMSImageLayout.pixelType;
            this.isInIntPacked = lCMSImageLayout.isIntPacked;
            this.outFormatter = lCMSImageLayout2.pixelType;
            this.isOutIntPacked = lCMSImageLayout2.isIntPacked;
            this.ID = LCMS.createTransform(this.lcmsProfiles, this.renderType, this.inFormatter, this.isInIntPacked, this.outFormatter, this.isOutIntPacked, this.disposerReferent);
        }
        LCMS.colorConvert(this, lCMSImageLayout, lCMSImageLayout2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.java2d.cmm.ColorTransform
    public void colorConvert(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        float[] fArr;
        LCMSImageLayout lCMSImageLayoutCreateImageLayout;
        LCMSImageLayout lCMSImageLayoutCreateImageLayout2;
        try {
            if (!bufferedImage2.getColorModel().hasAlpha() && (lCMSImageLayoutCreateImageLayout = LCMSImageLayout.createImageLayout(bufferedImage2)) != null && (lCMSImageLayoutCreateImageLayout2 = LCMSImageLayout.createImageLayout(bufferedImage)) != null) {
                doTransform(lCMSImageLayoutCreateImageLayout2, lCMSImageLayoutCreateImageLayout);
                return;
            }
            WritableRaster raster = bufferedImage.getRaster();
            WritableRaster raster2 = bufferedImage2.getRaster();
            ColorModel colorModel = bufferedImage.getColorModel();
            ColorModel colorModel2 = bufferedImage2.getColorModel();
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            int numColorComponents = colorModel.getNumColorComponents();
            int numColorComponents2 = colorModel2.getNumColorComponents();
            boolean z2 = 8;
            float f2 = 255.0f;
            for (int i2 = 0; i2 < numColorComponents; i2++) {
                if (colorModel.getComponentSize(i2) > 8) {
                    z2 = 16;
                    f2 = 65535.0f;
                }
            }
            for (int i3 = 0; i3 < numColorComponents2; i3++) {
                if (colorModel2.getComponentSize(i3) > 8) {
                    z2 = 16;
                    f2 = 65535.0f;
                }
            }
            float[] fArr2 = new float[numColorComponents];
            float[] fArr3 = new float[numColorComponents];
            ColorSpace colorSpace = colorModel.getColorSpace();
            for (int i4 = 0; i4 < numColorComponents; i4++) {
                fArr2[i4] = colorSpace.getMinValue(i4);
                fArr3[i4] = f2 / (colorSpace.getMaxValue(i4) - fArr2[i4]);
            }
            ColorSpace colorSpace2 = colorModel2.getColorSpace();
            float[] fArr4 = new float[numColorComponents2];
            float[] fArr5 = new float[numColorComponents2];
            for (int i5 = 0; i5 < numColorComponents2; i5++) {
                fArr4[i5] = colorSpace2.getMinValue(i5);
                fArr5[i5] = (colorSpace2.getMaxValue(i5) - fArr4[i5]) / f2;
            }
            boolean zHasAlpha = colorModel2.hasAlpha();
            boolean z3 = colorModel.hasAlpha() && zHasAlpha;
            if (zHasAlpha) {
                fArr = new float[numColorComponents2 + 1];
            } else {
                fArr = new float[numColorComponents2];
            }
            if (z2 == 8) {
                byte[] bArr = new byte[width * numColorComponents];
                byte[] bArr2 = new byte[width * numColorComponents2];
                float[] fArr6 = null;
                if (z3) {
                    fArr6 = new float[width];
                }
                try {
                    LCMSImageLayout lCMSImageLayout = new LCMSImageLayout(bArr, bArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(1), getNumInComponents());
                    LCMSImageLayout lCMSImageLayout2 = new LCMSImageLayout(bArr2, bArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(1), getNumOutComponents());
                    for (int i6 = 0; i6 < height; i6++) {
                        Object dataElements = null;
                        float[] normalizedComponents = null;
                        int i7 = 0;
                        for (int i8 = 0; i8 < width; i8++) {
                            dataElements = raster.getDataElements(i8, i6, dataElements);
                            normalizedComponents = colorModel.getNormalizedComponents(dataElements, normalizedComponents, 0);
                            for (int i9 = 0; i9 < numColorComponents; i9++) {
                                int i10 = i7;
                                i7++;
                                bArr[i10] = (byte) (((normalizedComponents[i9] - fArr2[i9]) * fArr3[i9]) + 0.5f);
                            }
                            if (z3) {
                                fArr6[i8] = normalizedComponents[numColorComponents];
                            }
                        }
                        doTransform(lCMSImageLayout, lCMSImageLayout2);
                        Object dataElements2 = null;
                        int i11 = 0;
                        for (int i12 = 0; i12 < width; i12++) {
                            for (int i13 = 0; i13 < numColorComponents2; i13++) {
                                int i14 = i11;
                                i11++;
                                fArr[i13] = ((bArr2[i14] & 255) * fArr5[i13]) + fArr4[i13];
                            }
                            if (z3) {
                                fArr[numColorComponents2] = fArr6[i12];
                            } else if (zHasAlpha) {
                                fArr[numColorComponents2] = 1.0f;
                            }
                            dataElements2 = colorModel2.getDataElements(fArr, 0, dataElements2);
                            raster2.setDataElements(i12, i6, dataElements2);
                        }
                    }
                    return;
                } catch (LCMSImageLayout.ImageLayoutException e2) {
                    throw new CMMException("Unable to convert images");
                }
            }
            short[] sArr = new short[width * numColorComponents];
            short[] sArr2 = new short[width * numColorComponents2];
            float[] fArr7 = null;
            if (z3) {
                fArr7 = new float[width];
            }
            try {
                LCMSImageLayout lCMSImageLayout3 = new LCMSImageLayout(sArr, sArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
                LCMSImageLayout lCMSImageLayout4 = new LCMSImageLayout(sArr2, sArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
                for (int i15 = 0; i15 < height; i15++) {
                    Object dataElements3 = null;
                    float[] normalizedComponents2 = null;
                    int i16 = 0;
                    for (int i17 = 0; i17 < width; i17++) {
                        dataElements3 = raster.getDataElements(i17, i15, dataElements3);
                        normalizedComponents2 = colorModel.getNormalizedComponents(dataElements3, normalizedComponents2, 0);
                        for (int i18 = 0; i18 < numColorComponents; i18++) {
                            int i19 = i16;
                            i16++;
                            sArr[i19] = (short) (((normalizedComponents2[i18] - fArr2[i18]) * fArr3[i18]) + 0.5f);
                        }
                        if (z3) {
                            fArr7[i17] = normalizedComponents2[numColorComponents];
                        }
                    }
                    doTransform(lCMSImageLayout3, lCMSImageLayout4);
                    Object dataElements4 = null;
                    int i20 = 0;
                    for (int i21 = 0; i21 < width; i21++) {
                        for (int i22 = 0; i22 < numColorComponents2; i22++) {
                            int i23 = i20;
                            i20++;
                            fArr[i22] = ((sArr2[i23] & 65535) * fArr5[i22]) + fArr4[i22];
                        }
                        if (z3) {
                            fArr[numColorComponents2] = fArr7[i21];
                        } else if (zHasAlpha) {
                            fArr[numColorComponents2] = 1.0f;
                        }
                        dataElements4 = colorModel2.getDataElements(fArr, 0, dataElements4);
                        raster2.setDataElements(i21, i15, dataElements4);
                    }
                }
            } catch (LCMSImageLayout.ImageLayoutException e3) {
                throw new CMMException("Unable to convert images");
            }
        } catch (LCMSImageLayout.ImageLayoutException e4) {
            throw new CMMException("Unable to convert images");
        }
    }

    @Override // sun.java2d.cmm.ColorTransform
    public void colorConvert(Raster raster, WritableRaster writableRaster, float[] fArr, float[] fArr2, float[] fArr3, float[] fArr4) {
        boolean z2;
        boolean z3;
        SampleModel sampleModel = raster.getSampleModel();
        SampleModel sampleModel2 = writableRaster.getSampleModel();
        int transferType = raster.getTransferType();
        int transferType2 = writableRaster.getTransferType();
        if (transferType == 4 || transferType == 5) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (transferType2 == 4 || transferType2 == 5) {
            z3 = true;
        } else {
            z3 = false;
        }
        int width = raster.getWidth();
        int height = raster.getHeight();
        int numBands = raster.getNumBands();
        int numBands2 = writableRaster.getNumBands();
        float[] fArr5 = new float[numBands];
        float[] fArr6 = new float[numBands2];
        float[] fArr7 = new float[numBands];
        float[] fArr8 = new float[numBands2];
        for (int i2 = 0; i2 < numBands; i2++) {
            if (z2) {
                fArr5[i2] = 65535.0f / (fArr2[i2] - fArr[i2]);
                fArr7[i2] = fArr[i2];
            } else {
                if (transferType == 2) {
                    fArr5[i2] = 2.0000305f;
                } else {
                    fArr5[i2] = 65535.0f / ((1 << sampleModel.getSampleSize(i2)) - 1);
                }
                fArr7[i2] = 0.0f;
            }
        }
        for (int i3 = 0; i3 < numBands2; i3++) {
            if (z3) {
                fArr6[i3] = (fArr4[i3] - fArr3[i3]) / 65535.0f;
                fArr8[i3] = fArr3[i3];
            } else {
                if (transferType2 == 2) {
                    fArr6[i3] = 0.49999237f;
                } else {
                    fArr6[i3] = ((1 << sampleModel2.getSampleSize(i3)) - 1) / 65535.0f;
                }
                fArr8[i3] = 0.0f;
            }
        }
        int minY = raster.getMinY();
        int minY2 = writableRaster.getMinY();
        short[] sArr = new short[width * numBands];
        short[] sArr2 = new short[width * numBands2];
        try {
            LCMSImageLayout lCMSImageLayout = new LCMSImageLayout(sArr, sArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
            LCMSImageLayout lCMSImageLayout2 = new LCMSImageLayout(sArr2, sArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
            int i4 = 0;
            while (i4 < height) {
                int minX = raster.getMinX();
                int i5 = 0;
                int i6 = 0;
                while (i6 < width) {
                    for (int i7 = 0; i7 < numBands; i7++) {
                        int i8 = i5;
                        i5++;
                        sArr[i8] = (short) (((raster.getSampleFloat(minX, minY, i7) - fArr7[i7]) * fArr5[i7]) + 0.5f);
                    }
                    i6++;
                    minX++;
                }
                doTransform(lCMSImageLayout, lCMSImageLayout2);
                int minX2 = writableRaster.getMinX();
                int i9 = 0;
                int i10 = 0;
                while (i10 < width) {
                    for (int i11 = 0; i11 < numBands2; i11++) {
                        int i12 = i9;
                        i9++;
                        writableRaster.setSample(minX2, minY2, i11, ((sArr2[i12] & 65535) * fArr6[i11]) + fArr8[i11]);
                    }
                    i10++;
                    minX2++;
                }
                i4++;
                minY++;
                minY2++;
            }
        } catch (LCMSImageLayout.ImageLayoutException e2) {
            throw new CMMException("Unable to convert rasters");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // sun.java2d.cmm.ColorTransform
    public void colorConvert(Raster raster, WritableRaster writableRaster) {
        LCMSImageLayout lCMSImageLayoutCreateImageLayout;
        LCMSImageLayout lCMSImageLayoutCreateImageLayout2 = LCMSImageLayout.createImageLayout(writableRaster);
        if (lCMSImageLayoutCreateImageLayout2 != null && (lCMSImageLayoutCreateImageLayout = LCMSImageLayout.createImageLayout(raster)) != null) {
            doTransform(lCMSImageLayoutCreateImageLayout, lCMSImageLayoutCreateImageLayout2);
            return;
        }
        SampleModel sampleModel = raster.getSampleModel();
        SampleModel sampleModel2 = writableRaster.getSampleModel();
        int transferType = raster.getTransferType();
        int transferType2 = writableRaster.getTransferType();
        int width = raster.getWidth();
        int height = raster.getHeight();
        int numBands = raster.getNumBands();
        int numBands2 = writableRaster.getNumBands();
        boolean z2 = 8;
        float f2 = 255.0f;
        for (int i2 = 0; i2 < numBands; i2++) {
            if (sampleModel.getSampleSize(i2) > 8) {
                z2 = 16;
                f2 = 65535.0f;
            }
        }
        for (int i3 = 0; i3 < numBands2; i3++) {
            if (sampleModel2.getSampleSize(i3) > 8) {
                z2 = 16;
                f2 = 65535.0f;
            }
        }
        float[] fArr = new float[numBands];
        float[] fArr2 = new float[numBands2];
        for (int i4 = 0; i4 < numBands; i4++) {
            if (transferType == 2) {
                fArr[i4] = f2 / 32767.0f;
            } else {
                fArr[i4] = f2 / ((1 << sampleModel.getSampleSize(i4)) - 1);
            }
        }
        for (int i5 = 0; i5 < numBands2; i5++) {
            if (transferType2 == 2) {
                fArr2[i5] = 32767.0f / f2;
            } else {
                fArr2[i5] = ((1 << sampleModel2.getSampleSize(i5)) - 1) / f2;
            }
        }
        int minY = raster.getMinY();
        int minY2 = writableRaster.getMinY();
        if (z2 == 8) {
            byte[] bArr = new byte[width * numBands];
            byte[] bArr2 = new byte[width * numBands2];
            try {
                LCMSImageLayout lCMSImageLayout = new LCMSImageLayout(bArr, bArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(1), getNumInComponents());
                LCMSImageLayout lCMSImageLayout2 = new LCMSImageLayout(bArr2, bArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(1), getNumOutComponents());
                int i6 = 0;
                while (i6 < height) {
                    int minX = raster.getMinX();
                    int i7 = 0;
                    int i8 = 0;
                    while (i8 < width) {
                        for (int i9 = 0; i9 < numBands; i9++) {
                            int i10 = i7;
                            i7++;
                            bArr[i10] = (byte) ((raster.getSample(minX, minY, i9) * fArr[i9]) + 0.5f);
                        }
                        i8++;
                        minX++;
                    }
                    doTransform(lCMSImageLayout, lCMSImageLayout2);
                    int minX2 = writableRaster.getMinX();
                    int i11 = 0;
                    int i12 = 0;
                    while (i12 < width) {
                        for (int i13 = 0; i13 < numBands2; i13++) {
                            int i14 = i11;
                            i11++;
                            writableRaster.setSample(minX2, minY2, i13, (int) (((bArr2[i14] & 255) * fArr2[i13]) + 0.5f));
                        }
                        i12++;
                        minX2++;
                    }
                    i6++;
                    minY++;
                    minY2++;
                }
                return;
            } catch (LCMSImageLayout.ImageLayoutException e2) {
                throw new CMMException("Unable to convert rasters");
            }
        }
        short[] sArr = new short[width * numBands];
        short[] sArr2 = new short[width * numBands2];
        try {
            LCMSImageLayout lCMSImageLayout3 = new LCMSImageLayout(sArr, sArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2);
            LCMSImageLayout lCMSImageLayout4 = new LCMSImageLayout(sArr2, sArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2);
            int i15 = 0;
            while (i15 < height) {
                int minX3 = raster.getMinX();
                int i16 = 0;
                int i17 = 0;
                while (i17 < width) {
                    for (int i18 = 0; i18 < numBands; i18++) {
                        int i19 = i16;
                        i16++;
                        sArr[i19] = (short) ((raster.getSample(minX3, minY, i18) * fArr[i18]) + 0.5f);
                    }
                    i17++;
                    minX3++;
                }
                doTransform(lCMSImageLayout3, lCMSImageLayout4);
                int minX4 = writableRaster.getMinX();
                int i20 = 0;
                int i21 = 0;
                while (i21 < width) {
                    for (int i22 = 0; i22 < numBands2; i22++) {
                        int i23 = i20;
                        i20++;
                        writableRaster.setSample(minX4, minY2, i22, (int) (((sArr2[i23] & 65535) * fArr2[i22]) + 0.5f));
                    }
                    i21++;
                    minX4++;
                }
                i15++;
                minY++;
                minY2++;
            }
        } catch (LCMSImageLayout.ImageLayoutException e3) {
            throw new CMMException("Unable to convert rasters");
        }
    }

    @Override // sun.java2d.cmm.ColorTransform
    public short[] colorConvert(short[] sArr, short[] sArr2) {
        if (sArr2 == null) {
            sArr2 = new short[(sArr.length / getNumInComponents()) * getNumOutComponents()];
        }
        try {
            doTransform(new LCMSImageLayout(sArr, sArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(2), getNumInComponents() * 2), new LCMSImageLayout(sArr2, sArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(2), getNumOutComponents() * 2));
            return sArr2;
        } catch (LCMSImageLayout.ImageLayoutException e2) {
            throw new CMMException("Unable to convert data");
        }
    }

    @Override // sun.java2d.cmm.ColorTransform
    public byte[] colorConvert(byte[] bArr, byte[] bArr2) {
        if (bArr2 == null) {
            bArr2 = new byte[(bArr.length / getNumInComponents()) * getNumOutComponents()];
        }
        try {
            doTransform(new LCMSImageLayout(bArr, bArr.length / getNumInComponents(), LCMSImageLayout.CHANNELS_SH(getNumInComponents()) | LCMSImageLayout.BYTES_SH(1), getNumInComponents()), new LCMSImageLayout(bArr2, bArr2.length / getNumOutComponents(), LCMSImageLayout.CHANNELS_SH(getNumOutComponents()) | LCMSImageLayout.BYTES_SH(1), getNumOutComponents()));
            return bArr2;
        } catch (LCMSImageLayout.ImageLayoutException e2) {
            throw new CMMException("Unable to convert data");
        }
    }
}
