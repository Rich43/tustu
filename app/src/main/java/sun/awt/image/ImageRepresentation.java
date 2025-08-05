package sun.awt.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferInt;
import java.awt.image.DirectColorModel;
import java.awt.image.ImageConsumer;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import sun.reflect.ClassFileConstants;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:sun/awt/image/ImageRepresentation.class */
public class ImageRepresentation extends ImageWatched implements ImageConsumer {
    InputStreamImageSource src;
    ToolkitImage image;
    int tag;
    long pData;
    int hints;
    int availinfo;
    Rectangle newbits;
    BufferedImage bimage;
    WritableRaster biRaster;
    protected ColorModel cmodel;
    boolean forceCMhint;
    int sstride;
    static boolean s_useNative;
    private int numWaiters;
    int width = -1;
    int height = -1;
    ColorModel srcModel = null;
    int[] srcLUT = null;
    int srcLUTtransIndex = -1;
    int numSrcLUT = 0;
    boolean isDefaultBI = false;
    boolean isSameCM = false;
    private boolean consuming = false;

    private static native void initIDs();

    private native boolean setICMpixels(int i2, int i3, int i4, int i5, int[] iArr, byte[] bArr, int i6, int i7, IntegerComponentRaster integerComponentRaster);

    private native boolean setDiffICM(int i2, int i3, int i4, int i5, int[] iArr, int i6, int i7, IndexColorModel indexColorModel, byte[] bArr, int i8, int i9, ByteComponentRaster byteComponentRaster, int i10);

    static {
        NativeLibLoader.loadLibraries();
        initIDs();
        s_useNative = true;
    }

    public ImageRepresentation(ToolkitImage toolkitImage, ColorModel colorModel, boolean z2) {
        this.image = toolkitImage;
        if (this.image.getSource() instanceof InputStreamImageSource) {
            this.src = (InputStreamImageSource) this.image.getSource();
        }
        setColorModel(colorModel);
        this.forceCMhint = z2;
    }

    public synchronized void reconstruct(int i2) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        int i3 = i2 & (this.availinfo ^ (-1));
        if ((this.availinfo & 64) == 0 && i3 != 0) {
            this.numWaiters++;
            try {
                startProduction();
                int i4 = i2 & (this.availinfo ^ (-1));
                while ((this.availinfo & 64) == 0 && i4 != 0) {
                    try {
                        wait();
                        i4 = i2 & (this.availinfo ^ (-1));
                    } catch (InterruptedException e2) {
                        Thread.currentThread().interrupt();
                        decrementWaiters();
                        return;
                    }
                }
            } finally {
                decrementWaiters();
            }
        }
    }

    @Override // java.awt.image.ImageConsumer
    public void setDimensions(int i2, int i3) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        this.image.setDimensions(i2, i3);
        newInfo(this.image, 3, 0, 0, i2, i3);
        if (i2 <= 0 || i3 <= 0) {
            imageComplete(1);
            return;
        }
        if (this.width != i2 || this.height != i3) {
            this.bimage = null;
        }
        this.width = i2;
        this.height = i3;
        this.availinfo |= 3;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    ColorModel getColorModel() {
        return this.cmodel;
    }

    BufferedImage getBufferedImage() {
        return this.bimage;
    }

    protected BufferedImage createImage(ColorModel colorModel, WritableRaster writableRaster, boolean z2, Hashtable hashtable) {
        BufferedImage bufferedImage = new BufferedImage(colorModel, writableRaster, z2, (Hashtable<?, ?>) null);
        bufferedImage.setAccelerationPriority(this.image.getAccelerationPriority());
        return bufferedImage;
    }

    @Override // java.awt.image.ImageConsumer
    public void setProperties(Hashtable<?, ?> hashtable) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        this.image.setProperties(hashtable);
        newInfo(this.image, 4, 0, 0, 0, 0);
    }

    @Override // java.awt.image.ImageConsumer
    public void setColorModel(ColorModel colorModel) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        this.srcModel = colorModel;
        if (colorModel instanceof IndexColorModel) {
            if (colorModel.getTransparency() == 3) {
                this.cmodel = ColorModel.getRGBdefault();
                this.srcLUT = null;
            } else {
                IndexColorModel indexColorModel = (IndexColorModel) colorModel;
                this.numSrcLUT = indexColorModel.getMapSize();
                this.srcLUT = new int[Math.max(this.numSrcLUT, 256)];
                indexColorModel.getRGBs(this.srcLUT);
                this.srcLUTtransIndex = indexColorModel.getTransparentPixel();
                this.cmodel = colorModel;
            }
        } else if (this.cmodel == null) {
            this.cmodel = colorModel;
            this.srcLUT = null;
        } else if (colorModel instanceof DirectColorModel) {
            DirectColorModel directColorModel = (DirectColorModel) colorModel;
            if (directColorModel.getRedMask() == 16711680 && directColorModel.getGreenMask() == 65280 && directColorModel.getBlueMask() == 255) {
                this.cmodel = colorModel;
                this.srcLUT = null;
            }
        }
        this.isSameCM = this.cmodel == colorModel;
    }

    void createBufferedImage() {
        this.isDefaultBI = false;
        try {
            this.biRaster = this.cmodel.createCompatibleWritableRaster(this.width, this.height);
            this.bimage = createImage(this.cmodel, this.biRaster, this.cmodel.isAlphaPremultiplied(), null);
        } catch (Exception e2) {
            this.cmodel = ColorModel.getRGBdefault();
            this.biRaster = this.cmodel.createCompatibleWritableRaster(this.width, this.height);
            this.bimage = createImage(this.cmodel, this.biRaster, false, null);
        }
        int type = this.bimage.getType();
        if (this.cmodel == ColorModel.getRGBdefault() || type == 1 || type == 3) {
            this.isDefaultBI = true;
            return;
        }
        if (this.cmodel instanceof DirectColorModel) {
            DirectColorModel directColorModel = (DirectColorModel) this.cmodel;
            if (directColorModel.getRedMask() == 16711680 && directColorModel.getGreenMask() == 65280 && directColorModel.getBlueMask() == 255) {
                this.isDefaultBI = true;
            }
        }
    }

    private void convertToRGB() {
        int width = this.bimage.getWidth();
        int height = this.bimage.getHeight();
        int i2 = width * height;
        DataBufferInt dataBufferInt = new DataBufferInt(i2);
        int[] iArrStealData = SunWritableRaster.stealData(dataBufferInt, 0);
        if ((this.cmodel instanceof IndexColorModel) && (this.biRaster instanceof ByteComponentRaster) && this.biRaster.getNumDataElements() == 1) {
            ByteComponentRaster byteComponentRaster = (ByteComponentRaster) this.biRaster;
            byte[] dataStorage = byteComponentRaster.getDataStorage();
            int dataOffset = byteComponentRaster.getDataOffset(0);
            for (int i3 = 0; i3 < i2; i3++) {
                iArrStealData[i3] = this.srcLUT[dataStorage[dataOffset + i3] & 255];
            }
        } else {
            Object dataElements = null;
            int i4 = 0;
            for (int i5 = 0; i5 < height; i5++) {
                for (int i6 = 0; i6 < width; i6++) {
                    dataElements = this.biRaster.getDataElements(i6, i5, dataElements);
                    int i7 = i4;
                    i4++;
                    iArrStealData[i7] = this.cmodel.getRGB(dataElements);
                }
            }
        }
        SunWritableRaster.markDirty(dataBufferInt);
        this.isSameCM = false;
        this.cmodel = ColorModel.getRGBdefault();
        this.biRaster = Raster.createPackedRaster(dataBufferInt, width, height, width, new int[]{16711680, NormalizerImpl.CC_MASK, 255, -16777216}, (Point) null);
        this.bimage = createImage(this.cmodel, this.biRaster, this.cmodel.isAlphaPremultiplied(), null);
        this.srcLUT = null;
        this.isDefaultBI = true;
    }

    @Override // java.awt.image.ImageConsumer
    public void setHints(int i2) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        this.hints = i2;
    }

    @Override // java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, byte[] bArr, int i6, int i7) {
        int i8;
        int i9 = i6;
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        synchronized (this) {
            if (this.bimage == null) {
                if (this.cmodel == null) {
                    this.cmodel = colorModel;
                }
                createBufferedImage();
            }
            if (i4 <= 0 || i5 <= 0) {
                return;
            }
            int width = this.biRaster.getWidth();
            int height = this.biRaster.getHeight();
            int i10 = i2 + i4;
            int i11 = i3 + i5;
            if (i2 < 0) {
                i6 -= i2;
                i2 = 0;
            } else if (i10 < 0) {
                i10 = width;
            }
            if (i3 < 0) {
                i6 -= i3 * i7;
                i3 = 0;
            } else if (i11 < 0) {
                i11 = height;
            }
            if (i10 > width) {
                i10 = width;
            }
            if (i11 > height) {
                i11 = height;
            }
            if (i2 >= i10 || i3 >= i11) {
                return;
            }
            int i12 = i10 - i2;
            int i13 = i11 - i3;
            if (i6 < 0 || i6 >= bArr.length) {
                throw new ArrayIndexOutOfBoundsException("Data offset out of bounds.");
            }
            int length = bArr.length - i6;
            if (length < i12) {
                throw new ArrayIndexOutOfBoundsException("Data array is too short.");
            }
            if (i7 < 0) {
                i8 = (i6 / (-i7)) + 1;
            } else if (i7 > 0) {
                i8 = ((length - i12) / i7) + 1;
            } else {
                i8 = i13;
            }
            if (i13 > i8) {
                throw new ArrayIndexOutOfBoundsException("Data array is too short.");
            }
            if (this.isSameCM && this.cmodel != colorModel && this.srcLUT != null && (colorModel instanceof IndexColorModel) && (this.biRaster instanceof ByteComponentRaster)) {
                IndexColorModel indexColorModel = (IndexColorModel) colorModel;
                ByteComponentRaster byteComponentRaster = (ByteComponentRaster) this.biRaster;
                int i14 = this.numSrcLUT;
                if (!setDiffICM(i2, i3, i12, i13, this.srcLUT, this.srcLUTtransIndex, this.numSrcLUT, indexColorModel, bArr, i6, i7, byteComponentRaster, byteComponentRaster.getDataOffset(0))) {
                    convertToRGB();
                } else {
                    byteComponentRaster.markDirty();
                    if (i14 != this.numSrcLUT) {
                        boolean zHasAlpha = indexColorModel.hasAlpha();
                        if (this.srcLUTtransIndex != -1) {
                            zHasAlpha = true;
                        }
                        int pixelSize = indexColorModel.getPixelSize();
                        ColorModel indexColorModel2 = new IndexColorModel(pixelSize, this.numSrcLUT, this.srcLUT, 0, zHasAlpha, this.srcLUTtransIndex, pixelSize > 8 ? 1 : 0);
                        this.cmodel = indexColorModel2;
                        this.bimage = createImage(indexColorModel2, byteComponentRaster, false, null);
                    }
                    return;
                }
            }
            if (this.isDefaultBI) {
                IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) this.biRaster;
                if (this.srcLUT != null && (colorModel instanceof IndexColorModel)) {
                    if (colorModel != this.srcModel) {
                        ((IndexColorModel) colorModel).getRGBs(this.srcLUT);
                        this.srcModel = colorModel;
                    }
                    if (s_useNative) {
                        if (setICMpixels(i2, i3, i12, i13, this.srcLUT, bArr, i6, i7, integerComponentRaster)) {
                            integerComponentRaster.markDirty();
                        } else {
                            abort();
                            return;
                        }
                    } else {
                        int[] iArr = new int[i12 * i13];
                        int i15 = 0;
                        int i16 = 0;
                        while (i16 < i13) {
                            int i17 = i9;
                            for (int i18 = 0; i18 < i12; i18++) {
                                int i19 = i15;
                                i15++;
                                int i20 = i17;
                                i17++;
                                iArr[i19] = this.srcLUT[bArr[i20] & 255];
                            }
                            i16++;
                            i9 += i7;
                        }
                        integerComponentRaster.setDataElements(i2, i3, i12, i13, iArr);
                    }
                } else {
                    int[] iArr2 = new int[i12];
                    int i21 = i3;
                    while (i21 < i3 + i13) {
                        int i22 = i9;
                        for (int i23 = 0; i23 < i12; i23++) {
                            int i24 = i22;
                            i22++;
                            iArr2[i23] = colorModel.getRGB(bArr[i24] & 255);
                        }
                        integerComponentRaster.setDataElements(i2, i21, i12, 1, iArr2);
                        i21++;
                        i9 += i7;
                    }
                    this.availinfo |= 8;
                }
            } else if (this.cmodel == colorModel && (this.biRaster instanceof ByteComponentRaster) && this.biRaster.getNumDataElements() == 1) {
                ByteComponentRaster byteComponentRaster2 = (ByteComponentRaster) this.biRaster;
                if (i6 == 0 && i7 == i12) {
                    byteComponentRaster2.putByteData(i2, i3, i12, i13, bArr);
                } else {
                    byte[] bArr2 = new byte[i12];
                    int i25 = i6;
                    for (int i26 = i3; i26 < i3 + i13; i26++) {
                        System.arraycopy(bArr, i25, bArr2, 0, i12);
                        byteComponentRaster2.putByteData(i2, i26, i12, 1, bArr2);
                        i25 += i7;
                    }
                }
            } else {
                int i27 = i3;
                while (i27 < i3 + i13) {
                    int i28 = i9;
                    for (int i29 = i2; i29 < i2 + i12; i29++) {
                        int i30 = i28;
                        i28++;
                        this.bimage.setRGB(i29, i27, colorModel.getRGB(bArr[i30] & 255));
                    }
                    i27++;
                    i9 += i7;
                }
                this.availinfo |= 8;
            }
            if ((this.availinfo & 16) == 0) {
                newInfo(this.image, 8, i2, i3, i12, i13);
            }
        }
    }

    @Override // java.awt.image.ImageConsumer
    public void setPixels(int i2, int i3, int i4, int i5, ColorModel colorModel, int[] iArr, int i6, int i7) {
        int i8 = i6;
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        synchronized (this) {
            if (this.bimage == null) {
                if (this.cmodel == null) {
                    this.cmodel = colorModel;
                }
                createBufferedImage();
            }
            int[] iArr2 = new int[i4];
            if (this.cmodel instanceof IndexColorModel) {
                convertToRGB();
            }
            if (colorModel == this.cmodel && (this.biRaster instanceof IntegerComponentRaster)) {
                IntegerComponentRaster integerComponentRaster = (IntegerComponentRaster) this.biRaster;
                if (i6 == 0 && i7 == i4) {
                    integerComponentRaster.setDataElements(i2, i3, i4, i5, iArr);
                } else {
                    int i9 = i3;
                    while (i9 < i3 + i5) {
                        System.arraycopy(iArr, i8, iArr2, 0, i4);
                        integerComponentRaster.setDataElements(i2, i9, i4, 1, iArr2);
                        i9++;
                        i8 += i7;
                    }
                }
            } else {
                if (colorModel.getTransparency() != 1) {
                    int transparency = this.cmodel.getTransparency();
                    ColorModel colorModel2 = this.cmodel;
                    if (transparency == 1) {
                        convertToRGB();
                    }
                }
                if (this.isDefaultBI) {
                    IntegerComponentRaster integerComponentRaster2 = (IntegerComponentRaster) this.biRaster;
                    int[] dataStorage = integerComponentRaster2.getDataStorage();
                    if (this.cmodel.equals(colorModel)) {
                        int scanlineStride = integerComponentRaster2.getScanlineStride();
                        int i10 = (i3 * scanlineStride) + i2;
                        int i11 = 0;
                        while (i11 < i5) {
                            System.arraycopy(iArr, i8, dataStorage, i10, i4);
                            i10 += scanlineStride;
                            i11++;
                            i8 += i7;
                        }
                        integerComponentRaster2.markDirty();
                    } else {
                        int i12 = i3;
                        while (i12 < i3 + i5) {
                            int i13 = i8;
                            for (int i14 = 0; i14 < i4; i14++) {
                                int i15 = i13;
                                i13++;
                                iArr2[i14] = colorModel.getRGB(iArr[i15]);
                            }
                            integerComponentRaster2.setDataElements(i2, i12, i4, 1, iArr2);
                            i12++;
                            i8 += i7;
                        }
                    }
                    this.availinfo |= 8;
                } else {
                    Object dataElements = null;
                    int i16 = i3;
                    while (i16 < i3 + i5) {
                        int i17 = i8;
                        for (int i18 = i2; i18 < i2 + i4; i18++) {
                            int i19 = i17;
                            i17++;
                            dataElements = this.cmodel.getDataElements(colorModel.getRGB(iArr[i19]), dataElements);
                            this.biRaster.setDataElements(i18, i16, dataElements);
                        }
                        i16++;
                        i8 += i7;
                    }
                    this.availinfo |= 8;
                }
            }
        }
        if ((this.availinfo & 16) == 0) {
            newInfo(this.image, 8, i2, i3, i4, i5);
        }
    }

    public BufferedImage getOpaqueRGBImage() {
        if (this.bimage.getType() == 2) {
            int width = this.bimage.getWidth();
            int height = this.bimage.getHeight();
            int i2 = width * height;
            DataBufferInt dataBufferInt = (DataBufferInt) this.biRaster.getDataBuffer();
            int[] iArrStealData = SunWritableRaster.stealData(dataBufferInt, 0);
            for (int i3 = 0; i3 < i2; i3++) {
                if ((iArrStealData[i3] >>> 24) != 255) {
                    return this.bimage;
                }
            }
            try {
                return createImage(new DirectColorModel(24, 16711680, NormalizerImpl.CC_MASK, 255), Raster.createPackedRaster(dataBufferInt, width, height, width, new int[]{16711680, NormalizerImpl.CC_MASK, 255}, (Point) null), false, null);
            } catch (Exception e2) {
                return this.bimage;
            }
        }
        return this.bimage;
    }

    @Override // java.awt.image.ImageConsumer
    public void imageComplete(int i2) {
        boolean z2;
        int i3;
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        switch (i2) {
            case 1:
                this.image.addInfo(64);
                z2 = true;
                i3 = 64;
                dispose();
                break;
            case 2:
                z2 = false;
                i3 = 16;
                break;
            case 3:
                z2 = true;
                i3 = 32;
                break;
            case 4:
            default:
                z2 = true;
                i3 = 128;
                break;
        }
        synchronized (this) {
            if (z2) {
                this.image.getSource().removeConsumer(this);
                this.consuming = false;
                this.newbits = null;
                if (this.bimage != null) {
                    this.bimage = getOpaqueRGBImage();
                }
                this.availinfo |= i3;
                notifyAll();
            } else {
                this.availinfo |= i3;
                notifyAll();
            }
        }
        newInfo(this.image, i3, 0, 0, this.width, this.height);
        this.image.infoDone(i2);
    }

    void startProduction() {
        if (!this.consuming) {
            this.consuming = true;
            this.image.getSource().startProduction(this);
        }
    }

    private synchronized void checkConsumption() {
        if (isWatcherListEmpty() && this.numWaiters == 0 && (this.availinfo & 32) == 0) {
            dispose();
        }
    }

    @Override // sun.awt.image.ImageWatched
    public synchronized void notifyWatcherListEmpty() {
        checkConsumption();
    }

    private synchronized void decrementWaiters() {
        this.numWaiters--;
        checkConsumption();
    }

    public boolean prepare(ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 64) != 0) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        boolean z2 = (this.availinfo & 32) != 0;
        if (!z2) {
            addWatcher(imageObserver);
            startProduction();
            z2 = (this.availinfo & 32) != 0;
        }
        return z2;
    }

    public int check(ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 96) == 0) {
            addWatcher(imageObserver);
        }
        return this.availinfo;
    }

    public boolean drawToBufImage(Graphics graphics, ToolkitImage toolkitImage, int i2, int i3, Color color, ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 64) != 0) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        boolean z2 = (this.availinfo & 32) != 0;
        boolean z3 = (this.availinfo & 128) != 0;
        if (!z2 && !z3) {
            addWatcher(imageObserver);
            startProduction();
            z2 = (this.availinfo & 32) != 0;
        }
        if (z2 || 0 != (this.availinfo & 16)) {
            graphics.drawImage(this.bimage, i2, i3, color, null);
        }
        return z2;
    }

    public boolean drawToBufImage(Graphics graphics, ToolkitImage toolkitImage, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 64) != 0) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        boolean z2 = (this.availinfo & 32) != 0;
        boolean z3 = (this.availinfo & 128) != 0;
        if (!z2 && !z3) {
            addWatcher(imageObserver);
            startProduction();
            z2 = (this.availinfo & 32) != 0;
        }
        if (z2 || 0 != (this.availinfo & 16)) {
            graphics.drawImage(this.bimage, i2, i3, i4, i5, color, null);
        }
        return z2;
    }

    public boolean drawToBufImage(Graphics graphics, ToolkitImage toolkitImage, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 64) != 0) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        boolean z2 = (this.availinfo & 32) != 0;
        boolean z3 = (this.availinfo & 128) != 0;
        if (!z2 && !z3) {
            addWatcher(imageObserver);
            startProduction();
            z2 = (this.availinfo & 32) != 0;
        }
        if (z2 || 0 != (this.availinfo & 16)) {
            graphics.drawImage(this.bimage, i2, i3, i4, i5, i6, i7, i8, i9, color, null);
        }
        return z2;
    }

    public boolean drawToBufImage(Graphics graphics, ToolkitImage toolkitImage, AffineTransform affineTransform, ImageObserver imageObserver) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        if (this.src != null) {
            this.src.checkSecurity(null, false);
        }
        if ((this.availinfo & 64) != 0) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(this.image, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        boolean z2 = (this.availinfo & 32) != 0;
        boolean z3 = (this.availinfo & 128) != 0;
        if (!z2 && !z3) {
            addWatcher(imageObserver);
            startProduction();
            z2 = (this.availinfo & 32) != 0;
        }
        if (z2 || 0 != (this.availinfo & 16)) {
            graphics2D.drawImage(this.bimage, affineTransform, null);
        }
        return z2;
    }

    synchronized void abort() {
        this.image.getSource().removeConsumer(this);
        this.consuming = false;
        this.newbits = null;
        this.bimage = null;
        this.biRaster = null;
        this.cmodel = null;
        this.srcLUT = null;
        this.isDefaultBI = false;
        this.isSameCM = false;
        newInfo(this.image, 128, -1, -1, -1, -1);
        this.availinfo &= ClassFileConstants.opc_i2d;
    }

    synchronized void dispose() {
        this.image.getSource().removeConsumer(this);
        this.consuming = false;
        this.newbits = null;
        this.availinfo &= -57;
    }

    public void setAccelerationPriority(float f2) {
        if (this.bimage != null) {
            this.bimage.setAccelerationPriority(f2);
        }
    }
}
