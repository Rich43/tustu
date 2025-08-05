package java.awt.image;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import sun.java2d.cmm.CMSManager;
import sun.java2d.cmm.ColorTransform;
import sun.java2d.cmm.PCMM;
import sun.java2d.cmm.ProfileDeferralMgr;

/* loaded from: rt.jar:java/awt/image/ColorConvertOp.class */
public class ColorConvertOp implements BufferedImageOp, RasterOp {
    ICC_Profile[] profileList;
    ColorSpace[] CSList;
    ColorTransform thisTransform;
    ColorTransform thisRasterTransform;
    ICC_Profile thisSrcProfile;
    ICC_Profile thisDestProfile;
    RenderingHints hints;
    boolean gotProfiles;
    float[] srcMinVals;
    float[] srcMaxVals;
    float[] dstMinVals;
    float[] dstMaxVals;

    static {
        if (ProfileDeferralMgr.deferring) {
            ProfileDeferralMgr.activateProfiles();
        }
    }

    public ColorConvertOp(RenderingHints renderingHints) {
        this.profileList = new ICC_Profile[0];
        this.hints = renderingHints;
    }

    public ColorConvertOp(ColorSpace colorSpace, RenderingHints renderingHints) {
        if (colorSpace == null) {
            throw new NullPointerException("ColorSpace cannot be null");
        }
        if (colorSpace instanceof ICC_ColorSpace) {
            this.profileList = new ICC_Profile[1];
            this.profileList[0] = ((ICC_ColorSpace) colorSpace).getProfile();
        } else {
            this.CSList = new ColorSpace[1];
            this.CSList[0] = colorSpace;
        }
        this.hints = renderingHints;
    }

    public ColorConvertOp(ColorSpace colorSpace, ColorSpace colorSpace2, RenderingHints renderingHints) {
        if (colorSpace == null || colorSpace2 == null) {
            throw new NullPointerException("ColorSpaces cannot be null");
        }
        if ((colorSpace instanceof ICC_ColorSpace) && (colorSpace2 instanceof ICC_ColorSpace)) {
            this.profileList = new ICC_Profile[2];
            this.profileList[0] = ((ICC_ColorSpace) colorSpace).getProfile();
            this.profileList[1] = ((ICC_ColorSpace) colorSpace2).getProfile();
            getMinMaxValsFromColorSpaces(colorSpace, colorSpace2);
        } else {
            this.CSList = new ColorSpace[2];
            this.CSList[0] = colorSpace;
            this.CSList[1] = colorSpace2;
        }
        this.hints = renderingHints;
    }

    public ColorConvertOp(ICC_Profile[] iCC_ProfileArr, RenderingHints renderingHints) {
        if (iCC_ProfileArr == null) {
            throw new NullPointerException("Profiles cannot be null");
        }
        this.gotProfiles = true;
        this.profileList = new ICC_Profile[iCC_ProfileArr.length];
        for (int i2 = 0; i2 < iCC_ProfileArr.length; i2++) {
            this.profileList[i2] = iCC_ProfileArr[i2];
        }
        this.hints = renderingHints;
    }

    public final ICC_Profile[] getICC_Profiles() {
        if (this.gotProfiles) {
            ICC_Profile[] iCC_ProfileArr = new ICC_Profile[this.profileList.length];
            for (int i2 = 0; i2 < this.profileList.length; i2++) {
                iCC_ProfileArr[i2] = this.profileList[i2];
            }
            return iCC_ProfileArr;
        }
        return null;
    }

    @Override // java.awt.image.BufferedImageOp
    public final BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        ColorSpace colorSpace;
        BufferedImage bufferedImageNonICCBIFilter;
        BufferedImage bufferedImage3 = null;
        if (bufferedImage.getColorModel() instanceof IndexColorModel) {
            bufferedImage = ((IndexColorModel) bufferedImage.getColorModel()).convertToIntDiscrete(bufferedImage.getRaster(), true);
        }
        ColorSpace colorSpace2 = bufferedImage.getColorModel().getColorSpace();
        if (bufferedImage2 != null) {
            if (bufferedImage2.getColorModel() instanceof IndexColorModel) {
                bufferedImage3 = bufferedImage2;
                bufferedImage2 = null;
                colorSpace = null;
            } else {
                colorSpace = bufferedImage2.getColorModel().getColorSpace();
            }
        } else {
            colorSpace = null;
        }
        if (this.CSList != null || !(colorSpace2 instanceof ICC_ColorSpace) || (bufferedImage2 != null && !(colorSpace instanceof ICC_ColorSpace))) {
            bufferedImageNonICCBIFilter = nonICCBIFilter(bufferedImage, colorSpace2, bufferedImage2, colorSpace);
        } else {
            bufferedImageNonICCBIFilter = ICCBIFilter(bufferedImage, colorSpace2, bufferedImage2, colorSpace);
        }
        if (bufferedImage3 != null) {
            Graphics2D graphics2DCreateGraphics = bufferedImage3.createGraphics();
            try {
                graphics2DCreateGraphics.drawImage(bufferedImageNonICCBIFilter, 0, 0, (ImageObserver) null);
                graphics2DCreateGraphics.dispose();
                return bufferedImage3;
            } catch (Throwable th) {
                graphics2DCreateGraphics.dispose();
                throw th;
            }
        }
        return bufferedImageNonICCBIFilter;
    }

    private final BufferedImage ICCBIFilter(BufferedImage bufferedImage, ColorSpace colorSpace, BufferedImage bufferedImage2, ColorSpace colorSpace2) {
        ICC_Profile profile;
        int length = this.profileList.length;
        ICC_Profile profile2 = ((ICC_ColorSpace) colorSpace).getProfile();
        if (bufferedImage2 == null) {
            if (length == 0) {
                throw new IllegalArgumentException("Destination ColorSpace is undefined");
            }
            profile = this.profileList[length - 1];
            bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
        } else {
            if (bufferedImage.getHeight() != bufferedImage2.getHeight() || bufferedImage.getWidth() != bufferedImage2.getWidth()) {
                throw new IllegalArgumentException("Width or height of BufferedImages do not match");
            }
            profile = ((ICC_ColorSpace) colorSpace2).getProfile();
        }
        if (profile2 == profile) {
            boolean z2 = true;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                if (profile2 == this.profileList[i2]) {
                    i2++;
                } else {
                    z2 = false;
                    break;
                }
            }
            if (z2) {
                Graphics2D graphics2DCreateGraphics = bufferedImage2.createGraphics();
                try {
                    graphics2DCreateGraphics.drawImage(bufferedImage, 0, 0, (ImageObserver) null);
                    graphics2DCreateGraphics.dispose();
                    return bufferedImage2;
                } catch (Throwable th) {
                    graphics2DCreateGraphics.dispose();
                    throw th;
                }
            }
        }
        if (this.thisTransform == null || this.thisSrcProfile != profile2 || this.thisDestProfile != profile) {
            updateBITransform(profile2, profile);
        }
        this.thisTransform.colorConvert(bufferedImage, bufferedImage2);
        return bufferedImage2;
    }

    private void updateBITransform(ICC_Profile iCC_Profile, ICC_Profile iCC_Profile2) {
        int renderingIntent;
        boolean z2 = false;
        boolean z3 = false;
        int length = this.profileList.length;
        int i2 = length;
        if (length == 0 || iCC_Profile != this.profileList[0]) {
            i2++;
            z2 = true;
        }
        if (length == 0 || iCC_Profile2 != this.profileList[length - 1] || i2 < 2) {
            i2++;
            z3 = true;
        }
        ICC_Profile[] iCC_ProfileArr = new ICC_Profile[i2];
        int i3 = 0;
        if (z2) {
            i3 = 0 + 1;
            iCC_ProfileArr[0] = iCC_Profile;
        }
        for (int i4 = 0; i4 < length; i4++) {
            int i5 = i3;
            i3++;
            iCC_ProfileArr[i5] = this.profileList[i4];
        }
        if (z3) {
            iCC_ProfileArr[i3] = iCC_Profile2;
        }
        ColorTransform[] colorTransformArr = new ColorTransform[i2];
        if (iCC_ProfileArr[0].getProfileClass() == 2) {
            renderingIntent = 1;
        } else {
            renderingIntent = 0;
        }
        int i6 = 1;
        PCMM module = CMSManager.getModule();
        for (int i7 = 0; i7 < i2; i7++) {
            if (i7 == i2 - 1) {
                i6 = 2;
            } else if (i6 == 4 && iCC_ProfileArr[i7].getProfileClass() == 5) {
                renderingIntent = 0;
                i6 = 1;
            }
            colorTransformArr[i7] = module.createTransform(iCC_ProfileArr[i7], renderingIntent, i6);
            renderingIntent = getRenderingIntent(iCC_ProfileArr[i7]);
            i6 = 4;
        }
        this.thisTransform = module.createTransform(colorTransformArr);
        this.thisSrcProfile = iCC_Profile;
        this.thisDestProfile = iCC_Profile2;
    }

    @Override // java.awt.image.RasterOp
    public final WritableRaster filter(Raster raster, WritableRaster writableRaster) {
        int renderingIntent;
        if (this.CSList != null) {
            return nonICCRasterFilter(raster, writableRaster);
        }
        int length = this.profileList.length;
        if (length < 2) {
            throw new IllegalArgumentException("Source or Destination ColorSpace is undefined");
        }
        if (raster.getNumBands() != this.profileList[0].getNumComponents()) {
            throw new IllegalArgumentException("Numbers of source Raster bands and source color space components do not match");
        }
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        } else {
            if (raster.getHeight() != writableRaster.getHeight() || raster.getWidth() != writableRaster.getWidth()) {
                throw new IllegalArgumentException("Width or height of Rasters do not match");
            }
            if (writableRaster.getNumBands() != this.profileList[length - 1].getNumComponents()) {
                throw new IllegalArgumentException("Numbers of destination Raster bands and destination color space components do not match");
            }
        }
        if (this.thisRasterTransform == null) {
            ColorTransform[] colorTransformArr = new ColorTransform[length];
            if (this.profileList[0].getProfileClass() == 2) {
                renderingIntent = 1;
            } else {
                renderingIntent = 0;
            }
            int i2 = 1;
            PCMM module = CMSManager.getModule();
            for (int i3 = 0; i3 < length; i3++) {
                if (i3 == length - 1) {
                    i2 = 2;
                } else if (i2 == 4 && this.profileList[i3].getProfileClass() == 5) {
                    renderingIntent = 0;
                    i2 = 1;
                }
                colorTransformArr[i3] = module.createTransform(this.profileList[i3], renderingIntent, i2);
                renderingIntent = getRenderingIntent(this.profileList[i3]);
                i2 = 4;
            }
            this.thisRasterTransform = module.createTransform(colorTransformArr);
        }
        int transferType = raster.getTransferType();
        int transferType2 = writableRaster.getTransferType();
        if (transferType == 4 || transferType == 5 || transferType2 == 4 || transferType2 == 5) {
            if (this.srcMinVals == null) {
                getMinMaxValsFromProfiles(this.profileList[0], this.profileList[length - 1]);
            }
            this.thisRasterTransform.colorConvert(raster, writableRaster, this.srcMinVals, this.srcMaxVals, this.dstMinVals, this.dstMaxVals);
        } else {
            this.thisRasterTransform.colorConvert(raster, writableRaster);
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
        ColorSpace iCC_ColorSpace = null;
        if (colorModel == null) {
            if (this.CSList == null) {
                int length = this.profileList.length;
                if (length == 0) {
                    throw new IllegalArgumentException("Destination ColorSpace is undefined");
                }
                iCC_ColorSpace = new ICC_ColorSpace(this.profileList[length - 1]);
            } else {
                iCC_ColorSpace = this.CSList[this.CSList.length - 1];
            }
        }
        return createCompatibleDestImage(bufferedImage, colorModel, iCC_ColorSpace);
    }

    private BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel, ColorSpace colorSpace) {
        if (colorModel == null) {
            ColorModel colorModel2 = bufferedImage.getColorModel();
            int numComponents = colorSpace.getNumComponents();
            boolean zHasAlpha = colorModel2.hasAlpha();
            if (zHasAlpha) {
                numComponents++;
            }
            int[] iArr = new int[numComponents];
            for (int i2 = 0; i2 < numComponents; i2++) {
                iArr[i2] = 8;
            }
            colorModel = new ComponentColorModel(colorSpace, iArr, zHasAlpha, colorModel2.isAlphaPremultiplied(), colorModel2.getTransparency(), 0);
        }
        return new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(bufferedImage.getWidth(), bufferedImage.getHeight()), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster raster) {
        int numComponents;
        if (this.CSList != null) {
            if (this.CSList.length != 2) {
                throw new IllegalArgumentException("Destination ColorSpace is undefined");
            }
            numComponents = this.CSList[1].getNumComponents();
        } else {
            int length = this.profileList.length;
            if (length < 2) {
                throw new IllegalArgumentException("Destination ColorSpace is undefined");
            }
            numComponents = this.profileList[length - 1].getNumComponents();
        }
        return Raster.createInterleavedRaster(0, raster.getWidth(), raster.getHeight(), numComponents, new Point(raster.getMinX(), raster.getMinY()));
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Float();
        }
        point2D2.setLocation(point2D.getX(), point2D.getY());
        return point2D2;
    }

    private int getRenderingIntent(ICC_Profile iCC_Profile) {
        byte[] data = iCC_Profile.getData(1751474532);
        return ((data[64 + 2] & 255) << 8) | (data[64 + 3] & 255);
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final RenderingHints getRenderingHints() {
        return this.hints;
    }

    private final BufferedImage nonICCBIFilter(BufferedImage bufferedImage, ColorSpace colorSpace, BufferedImage bufferedImage2, ColorSpace colorSpace2) {
        int length;
        float[] fArr;
        boolean z2;
        ICC_Profile profile;
        boolean z3;
        ICC_Profile profile2;
        ColorSpace colorSpace3;
        int i2;
        ColorSpace colorSpace4;
        int i3;
        float[] fArr2;
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ICC_ColorSpace iCC_ColorSpace = (ICC_ColorSpace) ColorSpace.getInstance(1001);
        if (bufferedImage2 == null) {
            bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            colorSpace2 = bufferedImage2.getColorModel().getColorSpace();
        } else if (height != bufferedImage2.getHeight() || width != bufferedImage2.getWidth()) {
            throw new IllegalArgumentException("Width or height of BufferedImages do not match");
        }
        WritableRaster raster = bufferedImage.getRaster();
        WritableRaster raster2 = bufferedImage2.getRaster();
        ColorModel colorModel = bufferedImage.getColorModel();
        ColorModel colorModel2 = bufferedImage2.getColorModel();
        int numColorComponents = colorModel.getNumColorComponents();
        int numColorComponents2 = colorModel2.getNumColorComponents();
        boolean zHasAlpha = colorModel2.hasAlpha();
        boolean z4 = colorModel.hasAlpha() && zHasAlpha;
        if (this.CSList == null && this.profileList.length != 0) {
            if (!(colorSpace instanceof ICC_ColorSpace)) {
                z2 = true;
                profile = iCC_ColorSpace.getProfile();
            } else {
                z2 = false;
                profile = ((ICC_ColorSpace) colorSpace).getProfile();
            }
            if (!(colorSpace2 instanceof ICC_ColorSpace)) {
                z3 = true;
                profile2 = iCC_ColorSpace.getProfile();
            } else {
                z3 = false;
                profile2 = ((ICC_ColorSpace) colorSpace2).getProfile();
            }
            if (this.thisTransform == null || this.thisSrcProfile != profile || this.thisDestProfile != profile2) {
                updateBITransform(profile, profile2);
            }
            if (z2) {
                colorSpace3 = iCC_ColorSpace;
                i2 = 3;
            } else {
                colorSpace3 = colorSpace;
                i2 = numColorComponents;
            }
            float[] fArr3 = new float[i2];
            float[] fArr4 = new float[i2];
            for (int i4 = 0; i4 < numColorComponents; i4++) {
                fArr3[i4] = colorSpace3.getMinValue(i4);
                fArr4[i4] = 65535.0f / (colorSpace3.getMaxValue(i4) - fArr3[i4]);
            }
            if (z3) {
                colorSpace4 = iCC_ColorSpace;
                i3 = 3;
            } else {
                colorSpace4 = colorSpace2;
                i3 = numColorComponents2;
            }
            float[] fArr5 = new float[i3];
            float[] fArr6 = new float[i3];
            for (int i5 = 0; i5 < numColorComponents2; i5++) {
                fArr5[i5] = colorSpace4.getMinValue(i5);
                fArr6[i5] = (colorSpace4.getMaxValue(i5) - fArr5[i5]) / 65535.0f;
            }
            if (zHasAlpha) {
                fArr2 = new float[numColorComponents2 + 1 > 3 ? numColorComponents2 + 1 : 3];
            } else {
                fArr2 = new float[numColorComponents2 > 3 ? numColorComponents2 : 3];
            }
            short[] sArr = new short[width * i2];
            short[] sArr2 = new short[width * i3];
            float[] fArr7 = null;
            if (z4) {
                fArr7 = new float[width];
            }
            for (int i6 = 0; i6 < height; i6++) {
                Object dataElements = null;
                float[] normalizedComponents = null;
                int i7 = 0;
                for (int i8 = 0; i8 < width; i8++) {
                    dataElements = raster.getDataElements(i8, i6, dataElements);
                    normalizedComponents = colorModel.getNormalizedComponents(dataElements, normalizedComponents, 0);
                    if (z4) {
                        fArr7[i8] = normalizedComponents[numColorComponents];
                    }
                    if (z2) {
                        normalizedComponents = colorSpace.toCIEXYZ(normalizedComponents);
                    }
                    for (int i9 = 0; i9 < i2; i9++) {
                        int i10 = i7;
                        i7++;
                        sArr[i10] = (short) (((normalizedComponents[i9] - fArr3[i9]) * fArr4[i9]) + 0.5f);
                    }
                }
                this.thisTransform.colorConvert(sArr, sArr2);
                Object dataElements2 = null;
                int i11 = 0;
                for (int i12 = 0; i12 < width; i12++) {
                    for (int i13 = 0; i13 < i3; i13++) {
                        int i14 = i11;
                        i11++;
                        fArr2[i13] = ((sArr2[i14] & 65535) * fArr6[i13]) + fArr5[i13];
                    }
                    if (z3) {
                        float[] fArrFromCIEXYZ = colorSpace.fromCIEXYZ(fArr2);
                        for (int i15 = 0; i15 < numColorComponents2; i15++) {
                            fArr2[i15] = fArrFromCIEXYZ[i15];
                        }
                    }
                    if (z4) {
                        fArr2[numColorComponents2] = fArr7[i12];
                    } else if (zHasAlpha) {
                        fArr2[numColorComponents2] = 1.0f;
                    }
                    dataElements2 = colorModel2.getDataElements(fArr2, 0, dataElements2);
                    raster2.setDataElements(i12, i6, dataElements2);
                }
            }
        } else {
            if (this.CSList == null) {
                length = 0;
            } else {
                length = this.CSList.length;
            }
            if (zHasAlpha) {
                fArr = new float[numColorComponents2 + 1];
            } else {
                fArr = new float[numColorComponents2];
            }
            Object dataElements3 = null;
            Object dataElements4 = null;
            float[] normalizedComponents2 = null;
            for (int i16 = 0; i16 < height; i16++) {
                for (int i17 = 0; i17 < width; i17++) {
                    dataElements3 = raster.getDataElements(i17, i16, dataElements3);
                    normalizedComponents2 = colorModel.getNormalizedComponents(dataElements3, normalizedComponents2, 0);
                    float[] ciexyz = colorSpace.toCIEXYZ(normalizedComponents2);
                    for (int i18 = 0; i18 < length; i18++) {
                        ciexyz = this.CSList[i18].toCIEXYZ(this.CSList[i18].fromCIEXYZ(ciexyz));
                    }
                    float[] fArrFromCIEXYZ2 = colorSpace2.fromCIEXYZ(ciexyz);
                    for (int i19 = 0; i19 < numColorComponents2; i19++) {
                        fArr[i19] = fArrFromCIEXYZ2[i19];
                    }
                    if (z4) {
                        fArr[numColorComponents2] = normalizedComponents2[numColorComponents];
                    } else if (zHasAlpha) {
                        fArr[numColorComponents2] = 1.0f;
                    }
                    dataElements4 = colorModel2.getDataElements(fArr, 0, dataElements4);
                    raster2.setDataElements(i17, i16, dataElements4);
                }
            }
        }
        return bufferedImage2;
    }

    private final WritableRaster nonICCRasterFilter(Raster raster, WritableRaster writableRaster) {
        boolean z2;
        boolean z3;
        if (this.CSList.length != 2) {
            throw new IllegalArgumentException("Destination ColorSpace is undefined");
        }
        if (raster.getNumBands() != this.CSList[0].getNumComponents()) {
            throw new IllegalArgumentException("Numbers of source Raster bands and source color space components do not match");
        }
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        } else {
            if (raster.getHeight() != writableRaster.getHeight() || raster.getWidth() != writableRaster.getWidth()) {
                throw new IllegalArgumentException("Width or height of Rasters do not match");
            }
            if (writableRaster.getNumBands() != this.CSList[1].getNumComponents()) {
                throw new IllegalArgumentException("Numbers of destination Raster bands and destination color space components do not match");
            }
        }
        if (this.srcMinVals == null) {
            getMinMaxValsFromColorSpaces(this.CSList[0], this.CSList[1]);
        }
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
        float[] fArr = null;
        float[] fArr2 = null;
        if (!z2) {
            fArr = new float[numBands];
            for (int i2 = 0; i2 < numBands; i2++) {
                if (transferType == 2) {
                    fArr[i2] = (this.srcMaxVals[i2] - this.srcMinVals[i2]) / 32767.0f;
                } else {
                    fArr[i2] = (this.srcMaxVals[i2] - this.srcMinVals[i2]) / ((1 << sampleModel.getSampleSize(i2)) - 1);
                }
            }
        }
        if (!z3) {
            fArr2 = new float[numBands2];
            for (int i3 = 0; i3 < numBands2; i3++) {
                if (transferType2 == 2) {
                    fArr2[i3] = 32767.0f / (this.dstMaxVals[i3] - this.dstMinVals[i3]);
                } else {
                    fArr2[i3] = ((1 << sampleModel2.getSampleSize(i3)) - 1) / (this.dstMaxVals[i3] - this.dstMinVals[i3]);
                }
            }
        }
        int minY = raster.getMinY();
        int minY2 = writableRaster.getMinY();
        float[] fArr3 = new float[numBands];
        ColorSpace colorSpace = this.CSList[0];
        ColorSpace colorSpace2 = this.CSList[1];
        int i4 = 0;
        while (i4 < height) {
            int minX = raster.getMinX();
            int minX2 = writableRaster.getMinX();
            int i5 = 0;
            while (i5 < width) {
                for (int i6 = 0; i6 < numBands; i6++) {
                    float sampleFloat = raster.getSampleFloat(minX, minY, i6);
                    if (!z2) {
                        sampleFloat = (sampleFloat * fArr[i6]) + this.srcMinVals[i6];
                    }
                    fArr3[i6] = sampleFloat;
                }
                float[] fArrFromCIEXYZ = colorSpace2.fromCIEXYZ(colorSpace.toCIEXYZ(fArr3));
                for (int i7 = 0; i7 < numBands2; i7++) {
                    float f2 = fArrFromCIEXYZ[i7];
                    if (!z3) {
                        f2 = (f2 - this.dstMinVals[i7]) * fArr2[i7];
                    }
                    writableRaster.setSample(minX2, minY2, i7, f2);
                }
                i5++;
                minX++;
                minX2++;
            }
            i4++;
            minY++;
            minY2++;
        }
        return writableRaster;
    }

    private void getMinMaxValsFromProfiles(ICC_Profile iCC_Profile, ICC_Profile iCC_Profile2) {
        int colorSpaceType = iCC_Profile.getColorSpaceType();
        int numComponents = iCC_Profile.getNumComponents();
        this.srcMinVals = new float[numComponents];
        this.srcMaxVals = new float[numComponents];
        setMinMax(colorSpaceType, numComponents, this.srcMinVals, this.srcMaxVals);
        int colorSpaceType2 = iCC_Profile2.getColorSpaceType();
        int numComponents2 = iCC_Profile2.getNumComponents();
        this.dstMinVals = new float[numComponents2];
        this.dstMaxVals = new float[numComponents2];
        setMinMax(colorSpaceType2, numComponents2, this.dstMinVals, this.dstMaxVals);
    }

    private void setMinMax(int i2, int i3, float[] fArr, float[] fArr2) {
        if (i2 == 1) {
            fArr[0] = 0.0f;
            fArr2[0] = 100.0f;
            fArr[1] = -128.0f;
            fArr2[1] = 127.0f;
            fArr[2] = -128.0f;
            fArr2[2] = 127.0f;
            return;
        }
        if (i2 == 0) {
            fArr[2] = 0.0f;
            fArr[1] = 0.0f;
            fArr[0] = 0.0f;
            fArr2[2] = 1.9999695f;
            fArr2[1] = 1.9999695f;
            fArr2[0] = 1.9999695f;
            return;
        }
        for (int i4 = 0; i4 < i3; i4++) {
            fArr[i4] = 0.0f;
            fArr2[i4] = 1.0f;
        }
    }

    private void getMinMaxValsFromColorSpaces(ColorSpace colorSpace, ColorSpace colorSpace2) {
        int numComponents = colorSpace.getNumComponents();
        this.srcMinVals = new float[numComponents];
        this.srcMaxVals = new float[numComponents];
        for (int i2 = 0; i2 < numComponents; i2++) {
            this.srcMinVals[i2] = colorSpace.getMinValue(i2);
            this.srcMaxVals[i2] = colorSpace.getMaxValue(i2);
        }
        int numComponents2 = colorSpace2.getNumComponents();
        this.dstMinVals = new float[numComponents2];
        this.dstMaxVals = new float[numComponents2];
        for (int i3 = 0; i3 < numComponents2; i3++) {
            this.dstMinVals[i3] = colorSpace2.getMinValue(i3);
            this.dstMaxVals[i3] = colorSpace2.getMaxValue(i3);
        }
    }
}
