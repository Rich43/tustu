package com.sun.imageio.plugins.bmp;

import com.sun.imageio.plugins.common.I18N;
import com.sun.imageio.plugins.common.ImageUtil;
import com.sun.imageio.plugins.common.ReaderUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentSampleModel;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.SinglePixelPackedSampleModel;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.event.IIOReadProgressListener;
import javax.imageio.event.IIOReadUpdateListener;
import javax.imageio.event.IIOReadWarningListener;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPImageReader.class */
public class BMPImageReader extends ImageReader implements BMPConstants {
    private static final int VERSION_2_1_BIT = 0;
    private static final int VERSION_2_4_BIT = 1;
    private static final int VERSION_2_8_BIT = 2;
    private static final int VERSION_2_24_BIT = 3;
    private static final int VERSION_3_1_BIT = 4;
    private static final int VERSION_3_4_BIT = 5;
    private static final int VERSION_3_8_BIT = 6;
    private static final int VERSION_3_24_BIT = 7;
    private static final int VERSION_3_NT_16_BIT = 8;
    private static final int VERSION_3_NT_32_BIT = 9;
    private static final int VERSION_4_1_BIT = 10;
    private static final int VERSION_4_4_BIT = 11;
    private static final int VERSION_4_8_BIT = 12;
    private static final int VERSION_4_16_BIT = 13;
    private static final int VERSION_4_24_BIT = 14;
    private static final int VERSION_4_32_BIT = 15;
    private static final int VERSION_3_XP_EMBEDDED = 16;
    private static final int VERSION_4_XP_EMBEDDED = 17;
    private static final int VERSION_5_XP_EMBEDDED = 18;
    private long bitmapFileSize;
    private long bitmapOffset;
    private long compression;
    private long imageSize;
    private byte[] palette;
    private int imageType;
    private int numBands;
    private boolean isBottomUp;
    private int bitsPerPixel;
    private int redMask;
    private int greenMask;
    private int blueMask;
    private int alphaMask;
    private SampleModel sampleModel;
    private SampleModel originalSampleModel;
    private ColorModel colorModel;
    private ColorModel originalColorModel;
    private ImageInputStream iis;
    private boolean gotHeader;
    private int width;
    private int height;
    private Rectangle destinationRegion;
    private Rectangle sourceRegion;
    private BMPMetadata metadata;

    /* renamed from: bi, reason: collision with root package name */
    private BufferedImage f11835bi;
    private boolean noTransform;
    private boolean seleBand;
    private int scaleX;
    private int scaleY;
    private int[] sourceBands;
    private int[] destBands;
    private static Boolean isLinkedProfileAllowed = null;

    public BMPImageReader(ImageReaderSpi imageReaderSpi) {
        super(imageReaderSpi);
        this.iis = null;
        this.gotHeader = false;
        this.noTransform = true;
        this.seleBand = false;
    }

    @Override // javax.imageio.ImageReader
    public void setInput(Object obj, boolean z2, boolean z3) {
        super.setInput(obj, z2, z3);
        this.iis = (ImageInputStream) obj;
        if (this.iis != null) {
            this.iis.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        }
        resetHeaderInfo();
    }

    @Override // javax.imageio.ImageReader
    public int getNumImages(boolean z2) throws IOException {
        if (this.iis == null) {
            throw new IllegalStateException(I18N.getString("GetNumImages0"));
        }
        if (this.seekForwardOnly && z2) {
            throw new IllegalStateException(I18N.getString("GetNumImages1"));
        }
        return 1;
    }

    @Override // javax.imageio.ImageReader
    public int getWidth(int i2) throws IOException {
        checkIndex(i2);
        try {
            readHeader();
            return this.width;
        } catch (IllegalArgumentException e2) {
            throw new IIOException(I18N.getString("BMPImageReader6"), e2);
        }
    }

    @Override // javax.imageio.ImageReader
    public int getHeight(int i2) throws IOException {
        checkIndex(i2);
        try {
            readHeader();
            return this.height;
        } catch (IllegalArgumentException e2) {
            throw new IIOException(I18N.getString("BMPImageReader6"), e2);
        }
    }

    private void checkIndex(int i2) {
        if (i2 != 0) {
            throw new IndexOutOfBoundsException(I18N.getString("BMPImageReader0"));
        }
    }

    private void readColorPalette(int i2) throws IOException {
        if (i2 < 1024000) {
            this.palette = new byte[i2];
            this.iis.readFully(this.palette, 0, i2);
            return;
        }
        int i3 = i2;
        int i4 = 0;
        ArrayList<byte[]> arrayList = new ArrayList();
        while (i3 != 0) {
            int iMin = Math.min(i3, 1024000);
            byte[] bArr = new byte[iMin];
            this.iis.readFully(bArr, 0, iMin);
            arrayList.add(bArr);
            i4 += iMin;
            i3 -= iMin;
        }
        byte[] bArr2 = new byte[i4];
        int length = 0;
        for (byte[] bArr3 : arrayList) {
            System.arraycopy(bArr3, 0, bArr2, length, bArr3.length);
            length += bArr3.length;
        }
        this.palette = bArr2;
    }

    protected void readHeader() throws IOException, IllegalArgumentException {
        long length;
        byte[] bArr;
        byte[] bArr2;
        byte[] bArr3;
        if (this.gotHeader) {
            return;
        }
        if (this.iis == null) {
            throw new IllegalStateException("Input source not set!");
        }
        int i2 = 0;
        int i3 = 0;
        this.metadata = new BMPMetadata();
        this.iis.mark();
        byte[] bArr4 = new byte[2];
        this.iis.read(bArr4);
        if (bArr4[0] != 66 || bArr4[1] != 77) {
            throw new IllegalArgumentException(I18N.getString("BMPImageReader1"));
        }
        this.bitmapFileSize = this.iis.readUnsignedInt();
        this.iis.skipBytes(4);
        this.bitmapOffset = this.iis.readUnsignedInt();
        long unsignedInt = this.iis.readUnsignedInt();
        if (unsignedInt == 12) {
            this.width = this.iis.readShort();
            this.height = this.iis.readShort();
        } else {
            this.width = this.iis.readInt();
            this.height = this.iis.readInt();
        }
        this.metadata.width = this.width;
        this.metadata.height = this.height;
        this.iis.readUnsignedShort();
        this.bitsPerPixel = this.iis.readUnsignedShort();
        this.metadata.bitsPerPixel = (short) this.bitsPerPixel;
        this.numBands = 3;
        if (unsignedInt == 12) {
            this.metadata.bmpVersion = BMPConstants.VERSION_2;
            if (this.bitsPerPixel == 1) {
                this.imageType = 0;
            } else if (this.bitsPerPixel == 4) {
                this.imageType = 1;
            } else if (this.bitsPerPixel == 8) {
                this.imageType = 2;
            } else if (this.bitsPerPixel == 24) {
                this.imageType = 3;
            }
            int i4 = (int) (((this.bitmapOffset - 14) - unsignedInt) / 3);
            readColorPalette(i4 * 3);
            this.metadata.palette = this.palette;
            this.metadata.paletteSize = i4;
        } else {
            this.compression = this.iis.readUnsignedInt();
            this.imageSize = this.iis.readUnsignedInt();
            long j2 = this.iis.readInt();
            long j3 = this.iis.readInt();
            long unsignedInt2 = this.iis.readUnsignedInt();
            long unsignedInt3 = this.iis.readUnsignedInt();
            this.metadata.compression = (int) this.compression;
            this.metadata.xPixelsPerMeter = (int) j2;
            this.metadata.yPixelsPerMeter = (int) j3;
            this.metadata.colorsUsed = (int) unsignedInt2;
            this.metadata.colorsImportant = (int) unsignedInt3;
            if (unsignedInt == 40) {
                switch ((int) this.compression) {
                    case 0:
                    case 1:
                    case 2:
                        if (this.bitmapOffset < unsignedInt + 14) {
                            throw new IIOException(I18N.getString("BMPImageReader7"));
                        }
                        int i5 = (int) (((this.bitmapOffset - 14) - unsignedInt) / 4);
                        readColorPalette(i5 * 4);
                        this.metadata.palette = this.palette;
                        this.metadata.paletteSize = i5;
                        if (this.bitsPerPixel == 1) {
                            this.imageType = 4;
                        } else if (this.bitsPerPixel == 4) {
                            this.imageType = 5;
                        } else if (this.bitsPerPixel == 8) {
                            this.imageType = 6;
                        } else if (this.bitsPerPixel == 24) {
                            this.imageType = 7;
                        } else if (this.bitsPerPixel == 16) {
                            this.imageType = 8;
                            this.redMask = 31744;
                            this.greenMask = 992;
                            this.blueMask = 31;
                            this.metadata.redMask = this.redMask;
                            this.metadata.greenMask = this.greenMask;
                            this.metadata.blueMask = this.blueMask;
                        } else if (this.bitsPerPixel == 32) {
                            this.imageType = 9;
                            this.redMask = 16711680;
                            this.greenMask = NormalizerImpl.CC_MASK;
                            this.blueMask = 255;
                            this.metadata.redMask = this.redMask;
                            this.metadata.greenMask = this.greenMask;
                            this.metadata.blueMask = this.blueMask;
                        }
                        this.metadata.bmpVersion = BMPConstants.VERSION_3;
                        break;
                    case 3:
                        if (this.bitsPerPixel == 16) {
                            this.imageType = 8;
                        } else if (this.bitsPerPixel == 32) {
                            this.imageType = 9;
                        }
                        this.redMask = (int) this.iis.readUnsignedInt();
                        this.greenMask = (int) this.iis.readUnsignedInt();
                        this.blueMask = (int) this.iis.readUnsignedInt();
                        this.metadata.redMask = this.redMask;
                        this.metadata.greenMask = this.greenMask;
                        this.metadata.blueMask = this.blueMask;
                        if (unsignedInt2 != 0) {
                            readColorPalette(((int) unsignedInt2) * 4);
                            this.metadata.palette = this.palette;
                            this.metadata.paletteSize = (int) unsignedInt2;
                        }
                        this.metadata.bmpVersion = BMPConstants.VERSION_3_NT;
                        break;
                    case 4:
                    case 5:
                        this.metadata.bmpVersion = BMPConstants.VERSION_3;
                        this.imageType = 16;
                        break;
                    default:
                        throw new IIOException(I18N.getString("BMPImageReader2"));
                }
            } else if (unsignedInt == 108 || unsignedInt == 124) {
                if (unsignedInt == 108) {
                    this.metadata.bmpVersion = BMPConstants.VERSION_4;
                } else if (unsignedInt == 124) {
                    this.metadata.bmpVersion = BMPConstants.VERSION_5;
                }
                this.redMask = (int) this.iis.readUnsignedInt();
                this.greenMask = (int) this.iis.readUnsignedInt();
                this.blueMask = (int) this.iis.readUnsignedInt();
                this.alphaMask = (int) this.iis.readUnsignedInt();
                long unsignedInt4 = this.iis.readUnsignedInt();
                int i6 = this.iis.readInt();
                int i7 = this.iis.readInt();
                int i8 = this.iis.readInt();
                int i9 = this.iis.readInt();
                int i10 = this.iis.readInt();
                int i11 = this.iis.readInt();
                int i12 = this.iis.readInt();
                int i13 = this.iis.readInt();
                int i14 = this.iis.readInt();
                long unsignedInt5 = this.iis.readUnsignedInt();
                long unsignedInt6 = this.iis.readUnsignedInt();
                long unsignedInt7 = this.iis.readUnsignedInt();
                if (unsignedInt == 124) {
                    this.metadata.intent = this.iis.readInt();
                    i2 = this.iis.readInt();
                    i3 = this.iis.readInt();
                    this.iis.skipBytes(4);
                }
                this.metadata.colorSpace = (int) unsignedInt4;
                if (unsignedInt4 == 0) {
                    this.metadata.redX = i6;
                    this.metadata.redY = i7;
                    this.metadata.redZ = i8;
                    this.metadata.greenX = i9;
                    this.metadata.greenY = i10;
                    this.metadata.greenZ = i11;
                    this.metadata.blueX = i12;
                    this.metadata.blueY = i13;
                    this.metadata.blueZ = i14;
                    this.metadata.gammaRed = (int) unsignedInt5;
                    this.metadata.gammaGreen = (int) unsignedInt6;
                    this.metadata.gammaBlue = (int) unsignedInt7;
                }
                int i15 = (int) (((this.bitmapOffset - 14) - unsignedInt) / 4);
                readColorPalette(i15 * 4);
                this.metadata.palette = this.palette;
                this.metadata.paletteSize = i15;
                switch ((int) this.compression) {
                    case 4:
                    case 5:
                        if (unsignedInt == 108) {
                            this.imageType = 17;
                            break;
                        } else if (unsignedInt == 124) {
                            this.imageType = 18;
                            break;
                        }
                        break;
                    default:
                        if (this.bitsPerPixel == 1) {
                            this.imageType = 10;
                        } else if (this.bitsPerPixel == 4) {
                            this.imageType = 11;
                        } else if (this.bitsPerPixel == 8) {
                            this.imageType = 12;
                        } else if (this.bitsPerPixel == 16) {
                            this.imageType = 13;
                            if (((int) this.compression) == 0) {
                                this.redMask = 31744;
                                this.greenMask = 992;
                                this.blueMask = 31;
                            }
                        } else if (this.bitsPerPixel == 24) {
                            this.imageType = 14;
                        } else if (this.bitsPerPixel == 32) {
                            this.imageType = 15;
                            if (((int) this.compression) == 0) {
                                this.redMask = 16711680;
                                this.greenMask = NormalizerImpl.CC_MASK;
                                this.blueMask = 255;
                            }
                        }
                        this.metadata.redMask = this.redMask;
                        this.metadata.greenMask = this.greenMask;
                        this.metadata.blueMask = this.blueMask;
                        this.metadata.alphaMask = this.alphaMask;
                        break;
                }
            } else {
                throw new IIOException(I18N.getString("BMPImageReader3"));
            }
        }
        if (this.height > 0) {
            this.isBottomUp = true;
        } else {
            this.isBottomUp = false;
            this.height = Math.abs(this.height);
        }
        if (this.metadata.compression == 0 && this.width * this.height * (this.bitsPerPixel / 8) > this.bitmapFileSize - this.bitmapOffset) {
            throw new IIOException(I18N.getString("BMPImageReader9"));
        }
        ColorSpace colorSpace = ColorSpace.getInstance(1000);
        if (this.metadata.colorSpace == 3 || this.metadata.colorSpace == 4) {
            this.iis.mark();
            this.iis.skipBytes(i2 - unsignedInt);
            byte[] bArrStaggeredReadByteStream = ReaderUtil.staggeredReadByteStream(this.iis, i3);
            this.iis.reset();
            if (this.metadata.colorSpace == 3 && isLinkedProfileAllowed()) {
                colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(new String(bArrStaggeredReadByteStream, "windows-1252")));
            } else if (this.metadata.colorSpace == 4) {
                colorSpace = new ICC_ColorSpace(ICC_Profile.getInstance(bArrStaggeredReadByteStream));
            }
        }
        if (this.bitsPerPixel == 0 || this.compression == 4 || this.compression == 5) {
            this.colorModel = null;
            this.sampleModel = null;
        } else if (this.bitsPerPixel == 1 || this.bitsPerPixel == 4 || this.bitsPerPixel == 8) {
            this.numBands = 1;
            if (this.bitsPerPixel == 8) {
                int[] iArr = new int[this.numBands];
                for (int i16 = 0; i16 < this.numBands; i16++) {
                    iArr[i16] = (this.numBands - 1) - i16;
                }
                this.sampleModel = new PixelInterleavedSampleModel(0, this.width, this.height, this.numBands, this.numBands * this.width, iArr);
            } else {
                this.sampleModel = new MultiPixelPackedSampleModel(0, this.width, this.height, this.bitsPerPixel);
            }
            if (this.imageType == 0 || this.imageType == 1 || this.imageType == 2) {
                length = this.palette.length / 3;
                if (length > 256) {
                    length = 256;
                }
                bArr = new byte[(int) length];
                bArr2 = new byte[(int) length];
                bArr3 = new byte[(int) length];
                for (int i17 = 0; i17 < ((int) length); i17++) {
                    int i18 = 3 * i17;
                    bArr3[i17] = this.palette[i18];
                    bArr2[i17] = this.palette[i18 + 1];
                    bArr[i17] = this.palette[i18 + 2];
                }
            } else {
                length = this.palette.length / 4;
                if (length > 256) {
                    length = 256;
                }
                bArr = new byte[(int) length];
                bArr2 = new byte[(int) length];
                bArr3 = new byte[(int) length];
                for (int i19 = 0; i19 < length; i19++) {
                    int i20 = 4 * i19;
                    bArr3[i19] = this.palette[i20];
                    bArr2[i19] = this.palette[i20 + 1];
                    bArr[i19] = this.palette[i20 + 2];
                }
            }
            if (ImageUtil.isIndicesForGrayscale(bArr, bArr2, bArr3)) {
                this.colorModel = ImageUtil.createColorModel(null, this.sampleModel);
            } else {
                this.colorModel = new IndexColorModel(this.bitsPerPixel, (int) length, bArr, bArr2, bArr3);
            }
        } else if (this.bitsPerPixel == 16) {
            this.numBands = 3;
            this.sampleModel = new SinglePixelPackedSampleModel(1, this.width, this.height, new int[]{this.redMask, this.greenMask, this.blueMask});
            this.colorModel = new DirectColorModel(colorSpace, 16, this.redMask, this.greenMask, this.blueMask, 0, false, 1);
        } else if (this.bitsPerPixel == 32) {
            this.numBands = this.alphaMask == 0 ? 3 : 4;
            this.sampleModel = new SinglePixelPackedSampleModel(3, this.width, this.height, this.numBands == 3 ? new int[]{this.redMask, this.greenMask, this.blueMask} : new int[]{this.redMask, this.greenMask, this.blueMask, this.alphaMask});
            this.colorModel = new DirectColorModel(colorSpace, 32, this.redMask, this.greenMask, this.blueMask, this.alphaMask, false, 3);
        } else {
            this.numBands = 3;
            int[] iArr2 = new int[this.numBands];
            for (int i21 = 0; i21 < this.numBands; i21++) {
                iArr2[i21] = (this.numBands - 1) - i21;
            }
            this.sampleModel = new PixelInterleavedSampleModel(0, this.width, this.height, this.numBands, this.numBands * this.width, iArr2);
            this.colorModel = ImageUtil.createColorModel(colorSpace, this.sampleModel);
        }
        this.originalSampleModel = this.sampleModel;
        this.originalColorModel = this.colorModel;
        this.iis.reset();
        this.iis.skipBytes(this.bitmapOffset);
        this.gotHeader = true;
    }

    @Override // javax.imageio.ImageReader
    public Iterator getImageTypes(int i2) throws IOException {
        checkIndex(i2);
        try {
            readHeader();
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(new ImageTypeSpecifier(this.originalColorModel, this.originalSampleModel));
            return arrayList.iterator();
        } catch (IllegalArgumentException e2) {
            throw new IIOException(I18N.getString("BMPImageReader6"), e2);
        }
    }

    @Override // javax.imageio.ImageReader
    public ImageReadParam getDefaultReadParam() {
        return new ImageReadParam();
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getImageMetadata(int i2) throws IOException {
        checkIndex(i2);
        if (this.metadata == null) {
            try {
                readHeader();
            } catch (IllegalArgumentException e2) {
                throw new IIOException(I18N.getString("BMPImageReader6"), e2);
            }
        }
        return this.metadata;
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override // javax.imageio.ImageReader
    public boolean isRandomAccessEasy(int i2) throws IOException {
        checkIndex(i2);
        try {
            readHeader();
            return this.metadata.compression == 0;
        } catch (IllegalArgumentException e2) {
            throw new IIOException(I18N.getString("BMPImageReader6"), e2);
        }
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage read(int i2, ImageReadParam imageReadParam) throws IOException {
        if (this.iis == null) {
            throw new IllegalStateException(I18N.getString("BMPImageReader5"));
        }
        checkIndex(i2);
        clearAbortRequest();
        processImageStarted(i2);
        if (imageReadParam == null) {
            imageReadParam = getDefaultReadParam();
        }
        try {
            readHeader();
            this.sourceRegion = new Rectangle(0, 0, 0, 0);
            this.destinationRegion = new Rectangle(0, 0, 0, 0);
            computeRegions(imageReadParam, this.width, this.height, imageReadParam.getDestination(), this.sourceRegion, this.destinationRegion);
            this.scaleX = imageReadParam.getSourceXSubsampling();
            this.scaleY = imageReadParam.getSourceYSubsampling();
            this.sourceBands = imageReadParam.getSourceBands();
            this.destBands = imageReadParam.getDestinationBands();
            this.seleBand = (this.sourceBands == null || this.destBands == null) ? false : true;
            this.noTransform = this.destinationRegion.equals(new Rectangle(0, 0, this.width, this.height)) || this.seleBand;
            if (!this.seleBand) {
                this.sourceBands = new int[this.numBands];
                this.destBands = new int[this.numBands];
                for (int i3 = 0; i3 < this.numBands; i3++) {
                    int i4 = i3;
                    this.sourceBands[i3] = i4;
                    this.destBands[i3] = i4;
                }
            }
            this.f11835bi = imageReadParam.getDestination();
            WritableRaster writableTile = null;
            if (this.f11835bi == null) {
                if (this.sampleModel != null && this.colorModel != null) {
                    this.sampleModel = this.sampleModel.createCompatibleSampleModel(this.destinationRegion.f12372x + this.destinationRegion.width, this.destinationRegion.f12373y + this.destinationRegion.height);
                    if (this.seleBand) {
                        this.sampleModel = this.sampleModel.createSubsetSampleModel(this.sourceBands);
                    }
                    writableTile = Raster.createWritableRaster(this.sampleModel, new Point());
                    this.f11835bi = new BufferedImage(this.colorModel, writableTile, false, (Hashtable<?, ?>) null);
                }
            } else {
                writableTile = this.f11835bi.getWritableTile(0, 0);
                this.sampleModel = this.f11835bi.getSampleModel();
                this.colorModel = this.f11835bi.getColorModel();
                this.noTransform &= this.destinationRegion.equals(writableTile.getBounds());
            }
            byte[] data = null;
            short[] data2 = null;
            int[] data3 = null;
            if (this.sampleModel != null) {
                if (this.sampleModel.getDataType() == 0) {
                    data = ((DataBufferByte) writableTile.getDataBuffer()).getData();
                } else if (this.sampleModel.getDataType() == 1) {
                    data2 = ((DataBufferUShort) writableTile.getDataBuffer()).getData();
                } else if (this.sampleModel.getDataType() == 3) {
                    data3 = ((DataBufferInt) writableTile.getDataBuffer()).getData();
                }
            }
            switch (this.imageType) {
                case 0:
                    read1Bit(data);
                    break;
                case 1:
                    read4Bit(data);
                    break;
                case 2:
                    read8Bit(data);
                    break;
                case 3:
                    read24Bit(data);
                    break;
                case 4:
                    read1Bit(data);
                    break;
                case 5:
                    switch ((int) this.compression) {
                        case 0:
                            read4Bit(data);
                            break;
                        case 2:
                            readRLE4(data);
                            break;
                        default:
                            throw new IIOException(I18N.getString("BMPImageReader1"));
                    }
                case 6:
                    switch ((int) this.compression) {
                        case 0:
                            read8Bit(data);
                            break;
                        case 1:
                            readRLE8(data);
                            break;
                        default:
                            throw new IIOException(I18N.getString("BMPImageReader1"));
                    }
                case 7:
                    read24Bit(data);
                    break;
                case 8:
                    read16Bit(data2);
                    break;
                case 9:
                    read32Bit(data3);
                    break;
                case 10:
                    read1Bit(data);
                    break;
                case 11:
                    switch ((int) this.compression) {
                        case 0:
                            read4Bit(data);
                            break;
                        case 2:
                            readRLE4(data);
                            break;
                        default:
                            throw new IIOException(I18N.getString("BMPImageReader1"));
                    }
                case 12:
                    switch ((int) this.compression) {
                        case 0:
                            read8Bit(data);
                            break;
                        case 1:
                            readRLE8(data);
                            break;
                        default:
                            throw new IIOException(I18N.getString("BMPImageReader1"));
                    }
                case 13:
                    read16Bit(data2);
                    break;
                case 14:
                    read24Bit(data);
                    break;
                case 15:
                    read32Bit(data3);
                    break;
                case 16:
                case 17:
                case 18:
                    this.f11835bi = readEmbedded((int) this.compression, this.f11835bi, imageReadParam);
                    break;
            }
            if (abortRequested()) {
                processReadAborted();
            } else {
                processImageComplete();
            }
            return this.f11835bi;
        } catch (IllegalArgumentException e2) {
            throw new IIOException(I18N.getString("BMPImageReader6"), e2);
        }
    }

    @Override // javax.imageio.ImageReader
    public boolean canReadRaster() {
        return true;
    }

    @Override // javax.imageio.ImageReader
    public Raster readRaster(int i2, ImageReadParam imageReadParam) throws IOException {
        return read(i2, imageReadParam).getData();
    }

    private void resetHeaderInfo() {
        this.gotHeader = false;
        this.f11835bi = null;
        this.originalSampleModel = null;
        this.sampleModel = null;
        this.originalColorModel = null;
        this.colorModel = null;
    }

    @Override // javax.imageio.ImageReader
    public void reset() {
        super.reset();
        this.iis = null;
        resetHeaderInfo();
    }

    private void read1Bit(byte[] bArr) throws IOException {
        int i2 = (this.width + 7) / 8;
        int i3 = i2 % 4;
        if (i3 != 0) {
            i3 = 4 - i3;
        }
        int i4 = i2 + i3;
        if (this.noTransform) {
            int i5 = this.isBottomUp ? (this.height - 1) * i2 : 0;
            for (int i6 = 0; i6 < this.height && !abortRequested(); i6++) {
                this.iis.readFully(bArr, i5, i2);
                this.iis.skipBytes(i3);
                i5 += this.isBottomUp ? -i2 : i2;
                processImageUpdate(this.f11835bi, 0, i6, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i6) / this.destinationRegion.height);
            }
            return;
        }
        byte[] bArr2 = new byte[i4];
        int scanlineStride = ((MultiPixelPackedSampleModel) this.sampleModel).getScanlineStride();
        if (this.isBottomUp) {
            this.iis.skipBytes(i4 * ((this.height - 1) - (this.sourceRegion.f12373y + ((this.destinationRegion.height - 1) * this.scaleY))));
        } else {
            this.iis.skipBytes(i4 * this.sourceRegion.f12373y);
        }
        int i7 = i4 * (this.scaleY - 1);
        int[] iArr = new int[this.destinationRegion.width];
        int[] iArr2 = new int[this.destinationRegion.width];
        int[] iArr3 = new int[this.destinationRegion.width];
        int[] iArr4 = new int[this.destinationRegion.width];
        int i8 = this.destinationRegion.f12372x;
        int i9 = this.sourceRegion.f12372x;
        int i10 = 0;
        while (i8 < this.destinationRegion.f12372x + this.destinationRegion.width) {
            iArr3[i10] = i9 >> 3;
            iArr[i10] = 7 - (i9 & 7);
            iArr4[i10] = i8 >> 3;
            iArr2[i10] = 7 - (i8 & 7);
            i8++;
            i10++;
            i9 += this.scaleX;
        }
        int i11 = this.destinationRegion.f12373y * scanlineStride;
        if (this.isBottomUp) {
            i11 += (this.destinationRegion.height - 1) * scanlineStride;
        }
        int i12 = 0;
        int i13 = this.sourceRegion.f12373y;
        while (true) {
            int i14 = i13;
            if (i12 < this.destinationRegion.height && !abortRequested()) {
                this.iis.read(bArr2, 0, i4);
                for (int i15 = 0; i15 < this.destinationRegion.width; i15++) {
                    int i16 = (bArr2[iArr3[i15]] >> iArr[i15]) & 1;
                    int i17 = i11 + iArr4[i15];
                    bArr[i17] = (byte) (bArr[i17] | (i16 << iArr2[i15]));
                }
                i11 += this.isBottomUp ? -scanlineStride : scanlineStride;
                this.iis.skipBytes(i7);
                processImageUpdate(this.f11835bi, 0, i12, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i12) / this.destinationRegion.height);
                i12++;
                i13 = i14 + this.scaleY;
            } else {
                return;
            }
        }
    }

    private void read4Bit(byte[] bArr) throws IOException {
        int i2 = (this.width + 1) / 2;
        int i3 = i2 % 4;
        if (i3 != 0) {
            i3 = 4 - i3;
        }
        int i4 = i2 + i3;
        if (this.noTransform) {
            int i5 = this.isBottomUp ? (this.height - 1) * i2 : 0;
            for (int i6 = 0; i6 < this.height && !abortRequested(); i6++) {
                this.iis.readFully(bArr, i5, i2);
                this.iis.skipBytes(i3);
                i5 += this.isBottomUp ? -i2 : i2;
                processImageUpdate(this.f11835bi, 0, i6, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i6) / this.destinationRegion.height);
            }
            return;
        }
        byte[] bArr2 = new byte[i4];
        int scanlineStride = ((MultiPixelPackedSampleModel) this.sampleModel).getScanlineStride();
        if (this.isBottomUp) {
            this.iis.skipBytes(i4 * ((this.height - 1) - (this.sourceRegion.f12373y + ((this.destinationRegion.height - 1) * this.scaleY))));
        } else {
            this.iis.skipBytes(i4 * this.sourceRegion.f12373y);
        }
        int i7 = i4 * (this.scaleY - 1);
        int[] iArr = new int[this.destinationRegion.width];
        int[] iArr2 = new int[this.destinationRegion.width];
        int[] iArr3 = new int[this.destinationRegion.width];
        int[] iArr4 = new int[this.destinationRegion.width];
        int i8 = this.destinationRegion.f12372x;
        int i9 = this.sourceRegion.f12372x;
        int i10 = 0;
        while (i8 < this.destinationRegion.f12372x + this.destinationRegion.width) {
            iArr3[i10] = i9 >> 1;
            iArr[i10] = (1 - (i9 & 1)) << 2;
            iArr4[i10] = i8 >> 1;
            iArr2[i10] = (1 - (i8 & 1)) << 2;
            i8++;
            i10++;
            i9 += this.scaleX;
        }
        int i11 = this.destinationRegion.f12373y * scanlineStride;
        if (this.isBottomUp) {
            i11 += (this.destinationRegion.height - 1) * scanlineStride;
        }
        int i12 = 0;
        int i13 = this.sourceRegion.f12373y;
        while (true) {
            int i14 = i13;
            if (i12 < this.destinationRegion.height && !abortRequested()) {
                this.iis.read(bArr2, 0, i4);
                for (int i15 = 0; i15 < this.destinationRegion.width; i15++) {
                    int i16 = (bArr2[iArr3[i15]] >> iArr[i15]) & 15;
                    int i17 = i11 + iArr4[i15];
                    bArr[i17] = (byte) (bArr[i17] | (i16 << iArr2[i15]));
                }
                i11 += this.isBottomUp ? -scanlineStride : scanlineStride;
                this.iis.skipBytes(i7);
                processImageUpdate(this.f11835bi, 0, i12, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i12) / this.destinationRegion.height);
                i12++;
                i13 = i14 + this.scaleY;
            } else {
                return;
            }
        }
    }

    private void read8Bit(byte[] bArr) throws IOException {
        int i2 = this.width % 4;
        if (i2 != 0) {
            i2 = 4 - i2;
        }
        int i3 = this.width + i2;
        if (this.noTransform) {
            int i4 = this.isBottomUp ? (this.height - 1) * this.width : 0;
            for (int i5 = 0; i5 < this.height && !abortRequested(); i5++) {
                this.iis.readFully(bArr, i4, this.width);
                this.iis.skipBytes(i2);
                i4 += this.isBottomUp ? -this.width : this.width;
                processImageUpdate(this.f11835bi, 0, i5, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i5) / this.destinationRegion.height);
            }
            return;
        }
        byte[] bArr2 = new byte[i3];
        int scanlineStride = ((ComponentSampleModel) this.sampleModel).getScanlineStride();
        if (this.isBottomUp) {
            this.iis.skipBytes(i3 * ((this.height - 1) - (this.sourceRegion.f12373y + ((this.destinationRegion.height - 1) * this.scaleY))));
        } else {
            this.iis.skipBytes(i3 * this.sourceRegion.f12373y);
        }
        int i6 = i3 * (this.scaleY - 1);
        int i7 = this.destinationRegion.f12373y * scanlineStride;
        if (this.isBottomUp) {
            i7 += (this.destinationRegion.height - 1) * scanlineStride;
        }
        int i8 = i7 + this.destinationRegion.f12372x;
        int i9 = 0;
        int i10 = this.sourceRegion.f12373y;
        while (true) {
            int i11 = i10;
            if (i9 < this.destinationRegion.height && !abortRequested()) {
                this.iis.read(bArr2, 0, i3);
                int i12 = 0;
                int i13 = this.sourceRegion.f12372x;
                while (true) {
                    int i14 = i13;
                    if (i12 >= this.destinationRegion.width) {
                        break;
                    }
                    bArr[i8 + i12] = bArr2[i14];
                    i12++;
                    i13 = i14 + this.scaleX;
                }
                i8 += this.isBottomUp ? -scanlineStride : scanlineStride;
                this.iis.skipBytes(i6);
                processImageUpdate(this.f11835bi, 0, i9, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i9) / this.destinationRegion.height);
                i9++;
                i10 = i11 + this.scaleY;
            } else {
                return;
            }
        }
    }

    private void read24Bit(byte[] bArr) throws IOException {
        int i2 = (this.width * 3) % 4;
        if (i2 != 0) {
            i2 = 4 - i2;
        }
        int i3 = this.width * 3;
        int i4 = i3 + i2;
        if (this.noTransform) {
            int i5 = this.isBottomUp ? (this.height - 1) * this.width * 3 : 0;
            for (int i6 = 0; i6 < this.height && !abortRequested(); i6++) {
                this.iis.readFully(bArr, i5, i3);
                this.iis.skipBytes(i2);
                i5 += this.isBottomUp ? -i3 : i3;
                processImageUpdate(this.f11835bi, 0, i6, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i6) / this.destinationRegion.height);
            }
            return;
        }
        byte[] bArr2 = new byte[i4];
        int scanlineStride = ((ComponentSampleModel) this.sampleModel).getScanlineStride();
        if (this.isBottomUp) {
            this.iis.skipBytes(i4 * ((this.height - 1) - (this.sourceRegion.f12373y + ((this.destinationRegion.height - 1) * this.scaleY))));
        } else {
            this.iis.skipBytes(i4 * this.sourceRegion.f12373y);
        }
        int i7 = i4 * (this.scaleY - 1);
        int i8 = this.destinationRegion.f12373y * scanlineStride;
        if (this.isBottomUp) {
            i8 += (this.destinationRegion.height - 1) * scanlineStride;
        }
        int i9 = i8 + (this.destinationRegion.f12372x * 3);
        int i10 = 0;
        int i11 = this.sourceRegion.f12373y;
        while (true) {
            int i12 = i11;
            if (i10 < this.destinationRegion.height && !abortRequested()) {
                this.iis.read(bArr2, 0, i4);
                int i13 = 0;
                int i14 = 3 * this.sourceRegion.f12372x;
                while (true) {
                    int i15 = i14;
                    if (i13 >= this.destinationRegion.width) {
                        break;
                    }
                    int i16 = (3 * i13) + i9;
                    for (int i17 = 0; i17 < this.destBands.length; i17++) {
                        bArr[i16 + this.destBands[i17]] = bArr2[i15 + this.sourceBands[i17]];
                    }
                    i13++;
                    i14 = i15 + (3 * this.scaleX);
                }
                i9 += this.isBottomUp ? -scanlineStride : scanlineStride;
                this.iis.skipBytes(i7);
                processImageUpdate(this.f11835bi, 0, i10, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i10) / this.destinationRegion.height);
                i10++;
                i11 = i12 + this.scaleY;
            } else {
                return;
            }
        }
    }

    private void read16Bit(short[] sArr) throws IOException {
        int i2 = (this.width * 2) % 4;
        if (i2 != 0) {
            i2 = 4 - i2;
        }
        int i3 = this.width + (i2 / 2);
        if (this.noTransform) {
            int i4 = this.isBottomUp ? (this.height - 1) * this.width : 0;
            for (int i5 = 0; i5 < this.height && !abortRequested(); i5++) {
                this.iis.readFully(sArr, i4, this.width);
                this.iis.skipBytes(i2);
                i4 += this.isBottomUp ? -this.width : this.width;
                processImageUpdate(this.f11835bi, 0, i5, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i5) / this.destinationRegion.height);
            }
            return;
        }
        short[] sArr2 = new short[i3];
        int scanlineStride = ((SinglePixelPackedSampleModel) this.sampleModel).getScanlineStride();
        if (this.isBottomUp) {
            this.iis.skipBytes((i3 * ((this.height - 1) - (this.sourceRegion.f12373y + ((this.destinationRegion.height - 1) * this.scaleY)))) << 1);
        } else {
            this.iis.skipBytes((i3 * this.sourceRegion.f12373y) << 1);
        }
        int i6 = (i3 * (this.scaleY - 1)) << 1;
        int i7 = this.destinationRegion.f12373y * scanlineStride;
        if (this.isBottomUp) {
            i7 += (this.destinationRegion.height - 1) * scanlineStride;
        }
        int i8 = i7 + this.destinationRegion.f12372x;
        int i9 = 0;
        int i10 = this.sourceRegion.f12373y;
        while (true) {
            int i11 = i10;
            if (i9 < this.destinationRegion.height && !abortRequested()) {
                this.iis.readFully(sArr2, 0, i3);
                int i12 = 0;
                int i13 = this.sourceRegion.f12372x;
                while (true) {
                    int i14 = i13;
                    if (i12 >= this.destinationRegion.width) {
                        break;
                    }
                    sArr[i8 + i12] = sArr2[i14];
                    i12++;
                    i13 = i14 + this.scaleX;
                }
                i8 += this.isBottomUp ? -scanlineStride : scanlineStride;
                this.iis.skipBytes(i6);
                processImageUpdate(this.f11835bi, 0, i9, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i9) / this.destinationRegion.height);
                i9++;
                i10 = i11 + this.scaleY;
            } else {
                return;
            }
        }
    }

    private void read32Bit(int[] iArr) throws IOException {
        if (this.noTransform) {
            int i2 = this.isBottomUp ? (this.height - 1) * this.width : 0;
            for (int i3 = 0; i3 < this.height && !abortRequested(); i3++) {
                this.iis.readFully(iArr, i2, this.width);
                i2 += this.isBottomUp ? -this.width : this.width;
                processImageUpdate(this.f11835bi, 0, i3, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i3) / this.destinationRegion.height);
            }
            return;
        }
        int[] iArr2 = new int[this.width];
        int scanlineStride = ((SinglePixelPackedSampleModel) this.sampleModel).getScanlineStride();
        if (this.isBottomUp) {
            this.iis.skipBytes((this.width * ((this.height - 1) - (this.sourceRegion.f12373y + ((this.destinationRegion.height - 1) * this.scaleY)))) << 2);
        } else {
            this.iis.skipBytes((this.width * this.sourceRegion.f12373y) << 2);
        }
        int i4 = (this.width * (this.scaleY - 1)) << 2;
        int i5 = this.destinationRegion.f12373y * scanlineStride;
        if (this.isBottomUp) {
            i5 += (this.destinationRegion.height - 1) * scanlineStride;
        }
        int i6 = i5 + this.destinationRegion.f12372x;
        int i7 = 0;
        int i8 = this.sourceRegion.f12373y;
        while (true) {
            int i9 = i8;
            if (i7 < this.destinationRegion.height && !abortRequested()) {
                this.iis.readFully(iArr2, 0, this.width);
                int i10 = 0;
                int i11 = this.sourceRegion.f12372x;
                while (true) {
                    int i12 = i11;
                    if (i10 >= this.destinationRegion.width) {
                        break;
                    }
                    iArr[i6 + i10] = iArr2[i12];
                    i10++;
                    i11 = i12 + this.scaleX;
                }
                i6 += this.isBottomUp ? -scanlineStride : scanlineStride;
                this.iis.skipBytes(i4);
                processImageUpdate(this.f11835bi, 0, i7, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i7) / this.destinationRegion.height);
                i7++;
                i8 = i9 + this.scaleY;
            } else {
                return;
            }
        }
    }

    private void readRLE8(byte[] bArr) throws IOException {
        int i2 = (int) this.imageSize;
        if (i2 == 0) {
            i2 = (int) (this.bitmapFileSize - this.bitmapOffset);
        }
        int i3 = 0;
        int i4 = this.width % 4;
        if (i4 != 0) {
            i3 = 4 - i4;
        }
        decodeRLE8(i2, i3, ReaderUtil.staggeredReadByteStream(this.iis, i2), bArr);
    }

    private void decodeRLE8(int i2, int i3, byte[] bArr, byte[] bArr2) throws IOException {
        byte[] bArr3 = new byte[this.width * this.height];
        int i4 = 0;
        int i5 = 0;
        boolean z2 = false;
        int i6 = this.isBottomUp ? this.height - 1 : 0;
        int scanlineStride = ((ComponentSampleModel) this.sampleModel).getScanlineStride();
        int i7 = 0;
        while (i4 != i2) {
            int i8 = i4;
            int i9 = i4 + 1;
            int i10 = bArr[i8] & 255;
            if (i10 == 0) {
                i4 = i9 + 1;
                switch (bArr[i9] & 255) {
                    case 0:
                        if (i6 >= this.sourceRegion.f12373y && i6 < this.sourceRegion.f12373y + this.sourceRegion.height) {
                            if (this.noTransform) {
                                int i11 = i6 * this.width;
                                for (int i12 = 0; i12 < this.width; i12++) {
                                    int i13 = i11;
                                    i11++;
                                    bArr2[i13] = bArr3[i12];
                                }
                                processImageUpdate(this.f11835bi, 0, i6, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                                i7++;
                            } else if ((i6 - this.sourceRegion.f12373y) % this.scaleY == 0) {
                                int i14 = ((i6 - this.sourceRegion.f12373y) / this.scaleY) + this.destinationRegion.f12373y;
                                int i15 = (i14 * scanlineStride) + this.destinationRegion.f12372x;
                                int i16 = this.sourceRegion.f12372x;
                                while (true) {
                                    int i17 = i16;
                                    if (i17 < this.sourceRegion.f12372x + this.sourceRegion.width) {
                                        int i18 = i15;
                                        i15++;
                                        bArr2[i18] = bArr3[i17];
                                        i16 = i17 + this.scaleX;
                                    } else {
                                        processImageUpdate(this.f11835bi, 0, i14, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                                        i7++;
                                    }
                                }
                            }
                        }
                        processImageProgress((100.0f * i7) / this.destinationRegion.height);
                        i6 += this.isBottomUp ? -1 : 1;
                        i5 = 0;
                        if (abortRequested()) {
                            z2 = true;
                            break;
                        }
                        break;
                    case 1:
                        z2 = true;
                        break;
                    case 2:
                        i4++;
                        i5 += (bArr[i4] & 255) + ((bArr[i4] & 255) * this.width);
                        break;
                    default:
                        int i19 = bArr[i4 - 1] & 255;
                        for (int i20 = 0; i20 < i19; i20++) {
                            int i21 = i5;
                            i5++;
                            int i22 = i4;
                            i4++;
                            bArr3[i21] = (byte) (bArr[i22] & 255);
                        }
                        if ((i19 & 1) == 1) {
                            i4++;
                            break;
                        }
                        break;
                }
            } else {
                for (int i23 = 0; i23 < i10; i23++) {
                    int i24 = i5;
                    i5++;
                    bArr3[i24] = (byte) (bArr[i9] & 255);
                }
                i4 = i9 + 1;
            }
            if (z2) {
                return;
            }
        }
    }

    private void readRLE4(byte[] bArr) throws IOException {
        int i2 = (int) this.imageSize;
        if (i2 == 0) {
            i2 = (int) (this.bitmapFileSize - this.bitmapOffset);
        }
        int i3 = 0;
        int i4 = this.width % 4;
        if (i4 != 0) {
            i3 = 4 - i4;
        }
        decodeRLE4(i2, i3, ReaderUtil.staggeredReadByteStream(this.iis, i2), bArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v77, types: [int] */
    private void decodeRLE4(int i2, int i3, byte[] bArr, byte[] bArr2) throws IOException {
        int i4;
        byte[] bArr3 = new byte[this.width];
        int i5 = 0;
        int i6 = 0;
        boolean z2 = false;
        int i7 = this.isBottomUp ? this.height - 1 : 0;
        int scanlineStride = ((MultiPixelPackedSampleModel) this.sampleModel).getScanlineStride();
        int i8 = 0;
        while (i5 != i2) {
            int i9 = i5;
            int i10 = i5 + 1;
            int i11 = bArr[i9] & 255;
            if (i11 == 0) {
                i5 = i10 + 1;
                switch (bArr[i10] & 255) {
                    case 0:
                        if (i7 >= this.sourceRegion.f12373y && i7 < this.sourceRegion.f12373y + this.sourceRegion.height) {
                            if (this.noTransform) {
                                int i12 = i7 * ((this.width + 1) >> 1);
                                int i13 = 0;
                                for (int i14 = 0; i14 < (this.width >> 1); i14++) {
                                    int i15 = i12;
                                    i12++;
                                    int i16 = i13;
                                    int i17 = i13 + 1;
                                    i13 = i17 + 1;
                                    bArr2[i15] = (byte) ((bArr3[i16] << 4) | bArr3[i17]);
                                }
                                if ((this.width & 1) == 1) {
                                    int i18 = i12;
                                    bArr2[i18] = (byte) (bArr2[i18] | (bArr3[this.width - 1] << 4));
                                }
                                processImageUpdate(this.f11835bi, 0, i7, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                                i8++;
                            } else if ((i7 - this.sourceRegion.f12373y) % this.scaleY == 0) {
                                int i19 = ((i7 - this.sourceRegion.f12373y) / this.scaleY) + this.destinationRegion.f12373y;
                                int i20 = (i19 * scanlineStride) + (this.destinationRegion.f12372x >> 1);
                                byte b2 = (1 - (this.destinationRegion.f12372x & 1)) << 2;
                                int i21 = this.sourceRegion.f12372x;
                                while (true) {
                                    int i22 = i21;
                                    if (i22 < this.sourceRegion.f12372x + this.sourceRegion.width) {
                                        int i23 = i20;
                                        bArr2[i23] = (byte) (bArr2[i23] | (bArr3[i22] << b2));
                                        int i24 = b2 + 4;
                                        if (i24 == 4) {
                                            i20++;
                                        }
                                        b2 = (i24 & 7) == true ? 1 : 0;
                                        i21 = i22 + this.scaleX;
                                    } else {
                                        processImageUpdate(this.f11835bi, 0, i19, this.destinationRegion.width, 1, 1, 1, new int[]{0});
                                        i8++;
                                    }
                                }
                            }
                        }
                        processImageProgress((100.0f * i8) / this.destinationRegion.height);
                        i7 += this.isBottomUp ? -1 : 1;
                        i6 = 0;
                        z2 = z2;
                        if (abortRequested()) {
                            z2 = true;
                            break;
                        }
                        break;
                    case 1:
                        z2 = true;
                        break;
                    case 2:
                        i5++;
                        i6 += (bArr[i5] & 255) + ((bArr[i5] & 255) * this.width);
                        z2 = z2;
                        break;
                    default:
                        int i25 = bArr[i5 - 1] & 255;
                        for (int i26 = 0; i26 < i25; i26++) {
                            int i27 = i6;
                            i6++;
                            if ((i26 & 1) == 0) {
                                i4 = (bArr[i5] & 240) >> 4;
                            } else {
                                int i28 = i5;
                                i5++;
                                i4 = bArr[i28] & 15;
                            }
                            bArr3[i27] = (byte) i4;
                        }
                        if ((i25 & 1) == 1) {
                            i5++;
                        }
                        z2 = z2;
                        if ((((int) Math.ceil(i25 / 2)) & 1) == 1) {
                            i5++;
                            z2 = z2;
                            break;
                        }
                        break;
                }
            } else {
                int[] iArr = {(bArr[i10] & 240) >> 4, bArr[i10] & 15};
                for (int i29 = 0; i29 < i11 && i6 < this.width; i29++) {
                    int i30 = i6;
                    i6++;
                    bArr3[i30] = (byte) iArr[i29 & 1];
                }
                i5 = i10 + 1;
                z2 = z2;
            }
            if (z2) {
                return;
            }
        }
    }

    private BufferedImage readEmbedded(int i2, BufferedImage bufferedImage, ImageReadParam imageReadParam) throws IOException {
        String str;
        switch (i2) {
            case 4:
                str = "JPEG";
                break;
            case 5:
                str = "PNG";
                break;
            default:
                throw new IOException("Unexpected compression type: " + i2);
        }
        ImageReader next = ImageIO.getImageReadersByFormatName(str).next();
        if (next == null) {
            throw new RuntimeException(I18N.getString("BMPImageReader4") + " " + str);
        }
        byte[] bArr = new byte[(int) this.imageSize];
        this.iis.read(bArr);
        next.setInput(ImageIO.createImageInputStream(new ByteArrayInputStream(bArr)));
        if (bufferedImage == null) {
            bufferedImage = next.getImageTypes(0).next().createBufferedImage(this.destinationRegion.f12372x + this.destinationRegion.width, this.destinationRegion.f12373y + this.destinationRegion.height);
        }
        next.addIIOReadProgressListener(new EmbeddedProgressAdapter() { // from class: com.sun.imageio.plugins.bmp.BMPImageReader.1
            @Override // com.sun.imageio.plugins.bmp.BMPImageReader.EmbeddedProgressAdapter, javax.imageio.event.IIOReadProgressListener
            public void imageProgress(ImageReader imageReader, float f2) {
                BMPImageReader.this.processImageProgress(f2);
            }
        });
        next.addIIOReadUpdateListener(new IIOReadUpdateListener() { // from class: com.sun.imageio.plugins.bmp.BMPImageReader.2
            @Override // javax.imageio.event.IIOReadUpdateListener
            public void imageUpdate(ImageReader imageReader, BufferedImage bufferedImage2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr) {
                BMPImageReader.this.processImageUpdate(bufferedImage2, i3, i4, i5, i6, i7, i8, iArr);
            }

            @Override // javax.imageio.event.IIOReadUpdateListener
            public void passComplete(ImageReader imageReader, BufferedImage bufferedImage2) {
                BMPImageReader.this.processPassComplete(bufferedImage2);
            }

            @Override // javax.imageio.event.IIOReadUpdateListener
            public void passStarted(ImageReader imageReader, BufferedImage bufferedImage2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int[] iArr) {
                BMPImageReader.this.processPassStarted(bufferedImage2, i3, i4, i5, i6, i7, i8, i9, iArr);
            }

            @Override // javax.imageio.event.IIOReadUpdateListener
            public void thumbnailPassComplete(ImageReader imageReader, BufferedImage bufferedImage2) {
            }

            @Override // javax.imageio.event.IIOReadUpdateListener
            public void thumbnailPassStarted(ImageReader imageReader, BufferedImage bufferedImage2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int[] iArr) {
            }

            @Override // javax.imageio.event.IIOReadUpdateListener
            public void thumbnailUpdate(ImageReader imageReader, BufferedImage bufferedImage2, int i3, int i4, int i5, int i6, int i7, int i8, int[] iArr) {
            }
        });
        next.addIIOReadWarningListener(new IIOReadWarningListener() { // from class: com.sun.imageio.plugins.bmp.BMPImageReader.3
            @Override // javax.imageio.event.IIOReadWarningListener
            public void warningOccurred(ImageReader imageReader, String str2) {
                BMPImageReader.this.processWarningOccurred(str2);
            }
        });
        ImageReadParam defaultReadParam = next.getDefaultReadParam();
        defaultReadParam.setDestination(bufferedImage);
        defaultReadParam.setDestinationBands(imageReadParam.getDestinationBands());
        defaultReadParam.setDestinationOffset(imageReadParam.getDestinationOffset());
        defaultReadParam.setSourceBands(imageReadParam.getSourceBands());
        defaultReadParam.setSourceRegion(imageReadParam.getSourceRegion());
        defaultReadParam.setSourceSubsampling(imageReadParam.getSourceXSubsampling(), imageReadParam.getSourceYSubsampling(), imageReadParam.getSubsamplingXOffset(), imageReadParam.getSubsamplingYOffset());
        next.read(0, defaultReadParam);
        return bufferedImage;
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/bmp/BMPImageReader$EmbeddedProgressAdapter.class */
    private class EmbeddedProgressAdapter implements IIOReadProgressListener {
        private EmbeddedProgressAdapter() {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void imageComplete(ImageReader imageReader) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void imageProgress(ImageReader imageReader, float f2) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void imageStarted(ImageReader imageReader, int i2) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void thumbnailComplete(ImageReader imageReader) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void thumbnailProgress(ImageReader imageReader, float f2) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void thumbnailStarted(ImageReader imageReader, int i2, int i3) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void sequenceComplete(ImageReader imageReader) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void sequenceStarted(ImageReader imageReader, int i2) {
        }

        @Override // javax.imageio.event.IIOReadProgressListener
        public void readAborted(ImageReader imageReader) {
        }
    }

    private static boolean isLinkedProfileAllowed() {
        if (isLinkedProfileAllowed == null) {
            isLinkedProfileAllowed = (Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: com.sun.imageio.plugins.bmp.BMPImageReader.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Boolean run2() {
                    return Boolean.valueOf(Boolean.getBoolean("sun.imageio.bmp.enableLinkedProfiles"));
                }
            });
        }
        return isLinkedProfileAllowed.booleanValue();
    }
}
