package com.sun.imageio.plugins.jpeg;

import com.sun.imageio.plugins.jpeg.JPEG;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.color.CMMException;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGImageReadParam;
import javax.imageio.plugins.jpeg.JPEGQTable;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageReader.class */
public class JPEGImageReader extends ImageReader {
    private boolean debug;
    private long structPointer;
    private ImageInputStream iis;
    private List imagePositions;
    private int numImages;
    protected static final int WARNING_NO_EOI = 0;
    protected static final int WARNING_NO_JFIF_IN_THUMB = 1;
    protected static final int WARNING_IGNORE_INVALID_ICC = 2;
    private static final int MAX_WARNING = 2;
    private int currentImage;
    private int width;
    private int height;
    private int colorSpaceCode;
    private int outColorSpaceCode;
    private int numComponents;
    private ColorSpace iccCS;
    private ColorConvertOp convert;
    private BufferedImage image;
    private WritableRaster raster;
    private WritableRaster target;
    private DataBufferByte buffer;
    private Rectangle destROI;
    private int[] destinationBands;
    private JPEGMetadata streamMetadata;
    private JPEGMetadata imageMetadata;
    private int imageMetadataIndex;
    private boolean haveSeeked;
    private JPEGQTable[] abbrevQTables;
    private JPEGHuffmanTable[] abbrevDCHuffmanTables;
    private JPEGHuffmanTable[] abbrevACHuffmanTables;
    private int minProgressivePass;
    private int maxProgressivePass;
    private static final int UNKNOWN = -1;
    private static final int MIN_ESTIMATED_PASSES = 10;
    private int knownPassCount;
    private int pass;
    private float percentToDate;
    private float previousPassPercentage;
    private int progInterval;
    private boolean tablesOnlyChecked;
    private Object disposerReferent;
    private DisposerRecord disposerRecord;
    private Thread theThread;
    private int theLockCount;
    private CallBackLock cbLock;

    private static native void initReaderIDs(Class cls, Class cls2, Class cls3);

    private native long initJPEGImageReader();

    private native void setSource(long j2);

    private native boolean readImageHeader(long j2, boolean z2, boolean z3) throws IOException;

    private native void setOutColorSpace(long j2, int i2);

    private native boolean readImage(int i2, long j2, byte[] bArr, int i3, int[] iArr, int[] iArr2, int i4, int i5, int i6, int i7, int i8, int i9, JPEGQTable[] jPEGQTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr2, int i10, int i11, boolean z2);

    private native void abortRead(long j2);

    private native void resetLibraryState(long j2);

    private native void resetReader(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void disposeReader(long j2);

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.imageio.plugins.jpeg.JPEGImageReader.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("jpeg");
                return null;
            }
        });
        initReaderIDs(ImageInputStream.class, JPEGQTable.class, JPEGHuffmanTable.class);
    }

    public JPEGImageReader(ImageReaderSpi imageReaderSpi) {
        super(imageReaderSpi);
        this.debug = false;
        this.structPointer = 0L;
        this.iis = null;
        this.imagePositions = null;
        this.numImages = 0;
        this.currentImage = -1;
        this.iccCS = null;
        this.convert = null;
        this.image = null;
        this.raster = null;
        this.target = null;
        this.buffer = null;
        this.destROI = null;
        this.destinationBands = null;
        this.streamMetadata = null;
        this.imageMetadata = null;
        this.imageMetadataIndex = -1;
        this.haveSeeked = false;
        this.abbrevQTables = null;
        this.abbrevDCHuffmanTables = null;
        this.abbrevACHuffmanTables = null;
        this.minProgressivePass = 0;
        this.maxProgressivePass = Integer.MAX_VALUE;
        this.knownPassCount = -1;
        this.pass = 0;
        this.percentToDate = 0.0f;
        this.previousPassPercentage = 0.0f;
        this.progInterval = 0;
        this.tablesOnlyChecked = false;
        this.disposerReferent = new Object();
        this.theThread = null;
        this.theLockCount = 0;
        this.cbLock = new CallBackLock();
        this.structPointer = initJPEGImageReader();
        this.disposerRecord = new JPEGReaderDisposerRecord(this.structPointer);
        Disposer.addRecord(this.disposerReferent, this.disposerRecord);
    }

    protected void warningOccurred(int i2) {
        this.cbLock.lock();
        try {
            if (i2 < 0 || i2 > 2) {
                throw new InternalError("Invalid warning index");
            }
            processWarningOccurred("com.sun.imageio.plugins.jpeg.JPEGImageReaderResources", Integer.toString(i2));
        } finally {
            this.cbLock.unlock();
        }
    }

    protected void warningWithMessage(String str) {
        this.cbLock.lock();
        try {
            processWarningOccurred(str);
        } finally {
            this.cbLock.unlock();
        }
    }

    @Override // javax.imageio.ImageReader
    public void setInput(Object obj, boolean z2, boolean z3) {
        setThreadLock();
        try {
            this.cbLock.check();
            super.setInput(obj, z2, z3);
            this.ignoreMetadata = z3;
            resetInternalState();
            this.iis = (ImageInputStream) obj;
            setSource(this.structPointer);
            clearThreadLock();
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    private int readInputData(byte[] bArr, int i2, int i3) throws IOException {
        this.cbLock.lock();
        try {
            int i4 = this.iis.read(bArr, i2, i3);
            this.cbLock.unlock();
            return i4;
        } catch (Throwable th) {
            this.cbLock.unlock();
            throw th;
        }
    }

    private long skipInputBytes(long j2) throws IOException {
        this.cbLock.lock();
        try {
            long jSkipBytes = this.iis.skipBytes(j2);
            this.cbLock.unlock();
            return jSkipBytes;
        } catch (Throwable th) {
            this.cbLock.unlock();
            throw th;
        }
    }

    private void checkTablesOnly() throws IOException {
        if (this.debug) {
            System.out.println("Checking for tables-only image");
        }
        long streamPosition = this.iis.getStreamPosition();
        if (this.debug) {
            System.out.println("saved pos is " + streamPosition);
            System.out.println("length is " + this.iis.length());
        }
        if (readNativeHeader(true)) {
            if (this.debug) {
                System.out.println("tables-only image found");
                System.out.println("pos after return from native is " + this.iis.getStreamPosition());
            }
            if (!this.ignoreMetadata) {
                this.iis.seek(streamPosition);
                this.haveSeeked = true;
                this.streamMetadata = new JPEGMetadata(true, false, this.iis, this);
                long streamPosition2 = this.iis.getStreamPosition();
                if (this.debug) {
                    System.out.println("pos after constructing stream metadata is " + streamPosition2);
                }
            }
            if (hasNextImage()) {
                this.imagePositions.add(new Long(this.iis.getStreamPosition()));
            }
        } else {
            this.imagePositions.add(new Long(streamPosition));
            this.currentImage = 0;
        }
        if (this.seekForwardOnly && !this.imagePositions.isEmpty()) {
            this.iis.flushBefore(((Long) this.imagePositions.get(this.imagePositions.size() - 1)).longValue());
        }
        this.tablesOnlyChecked = true;
    }

    @Override // javax.imageio.ImageReader
    public int getNumImages(boolean z2) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            return getNumImagesOnThread(z2);
        } finally {
            clearThreadLock();
        }
    }

    private void skipPastImage(int i2) {
        this.cbLock.lock();
        try {
            gotoImage(i2);
            skipImage();
        } catch (IOException | IndexOutOfBoundsException e2) {
        } finally {
            this.cbLock.unlock();
        }
    }

    private int getNumImagesOnThread(boolean z2) throws IOException {
        if (this.numImages != 0) {
            return this.numImages;
        }
        if (this.iis == null) {
            throw new IllegalStateException("Input not set");
        }
        if (z2) {
            if (this.seekForwardOnly) {
                throw new IllegalStateException("seekForwardOnly and allowSearch can't both be true!");
            }
            if (!this.tablesOnlyChecked) {
                checkTablesOnly();
            }
            this.iis.mark();
            gotoImage(0);
            JPEGBuffer jPEGBuffer = new JPEGBuffer(this.iis);
            jPEGBuffer.loadBuf(0);
            boolean zScanForFF = false;
            while (!zScanForFF) {
                zScanForFF = jPEGBuffer.scanForFF(this);
                switch (jPEGBuffer.buf[jPEGBuffer.bufPtr] & 255) {
                    case 0:
                    case 208:
                    case 209:
                    case 210:
                    case 211:
                    case 212:
                    case 213:
                    case 214:
                    case 215:
                    case 217:
                        break;
                    case 216:
                        this.numImages++;
                        break;
                    default:
                        jPEGBuffer.bufAvail--;
                        jPEGBuffer.bufPtr++;
                        jPEGBuffer.loadBuf(2);
                        byte[] bArr = jPEGBuffer.buf;
                        int i2 = jPEGBuffer.bufPtr;
                        jPEGBuffer.bufPtr = i2 + 1;
                        int i3 = (bArr[i2] & 255) << 8;
                        byte[] bArr2 = jPEGBuffer.buf;
                        int i4 = jPEGBuffer.bufPtr;
                        jPEGBuffer.bufPtr = i4 + 1;
                        int i5 = i3 | (bArr2[i4] & 255);
                        jPEGBuffer.bufAvail -= 2;
                        jPEGBuffer.skipData(i5 - 2);
                        continue;
                }
                jPEGBuffer.bufAvail--;
                jPEGBuffer.bufPtr++;
            }
            this.iis.reset();
            return this.numImages;
        }
        return -1;
    }

    private void gotoImage(int i2) throws IOException {
        if (this.iis == null) {
            throw new IllegalStateException("Input not set");
        }
        if (i2 < this.minIndex) {
            throw new IndexOutOfBoundsException();
        }
        if (!this.tablesOnlyChecked) {
            checkTablesOnly();
        }
        if (this.imagePositions.isEmpty()) {
            throw new IIOException("No image data present to read");
        }
        if (i2 < this.imagePositions.size()) {
            this.iis.seek(((Long) this.imagePositions.get(i2)).longValue());
        } else {
            this.iis.seek(((Long) this.imagePositions.get(this.imagePositions.size() - 1)).longValue());
            skipImage();
            for (int size = this.imagePositions.size(); size <= i2; size++) {
                if (!hasNextImage()) {
                    throw new IndexOutOfBoundsException();
                }
                Long l2 = new Long(this.iis.getStreamPosition());
                this.imagePositions.add(l2);
                if (this.seekForwardOnly) {
                    this.iis.flushBefore(l2.longValue());
                }
                if (size < i2) {
                    skipImage();
                }
            }
        }
        if (this.seekForwardOnly) {
            this.minIndex = i2;
        }
        this.haveSeeked = true;
    }

    private void skipImage() throws IOException {
        if (this.debug) {
            System.out.println("skipImage called");
        }
        boolean z2 = false;
        int i2 = this.iis.read();
        while (true) {
            int i3 = i2;
            if (i3 != -1) {
                if (z2 && i3 == 217) {
                    return;
                }
                z2 = i3 == 255;
                i2 = this.iis.read();
            } else {
                throw new IndexOutOfBoundsException();
            }
        }
    }

    private boolean hasNextImage() throws IOException {
        if (this.debug) {
            System.out.print("hasNextImage called; returning ");
        }
        this.iis.mark();
        boolean z2 = false;
        int i2 = this.iis.read();
        while (true) {
            int i3 = i2;
            if (i3 != -1) {
                if (z2 && i3 == 216) {
                    this.iis.reset();
                    if (this.debug) {
                        System.out.println("true");
                        return true;
                    }
                    return true;
                }
                z2 = i3 == 255;
                i2 = this.iis.read();
            } else {
                this.iis.reset();
                if (this.debug) {
                    System.out.println("false");
                    return false;
                }
                return false;
            }
        }
    }

    private void pushBack(int i2) throws IOException {
        if (this.debug) {
            System.out.println("pushing back " + i2 + " bytes");
        }
        this.cbLock.lock();
        try {
            this.iis.seek(this.iis.getStreamPosition() - i2);
        } finally {
            this.cbLock.unlock();
        }
    }

    private void readHeader(int i2, boolean z2) throws IOException {
        gotoImage(i2);
        readNativeHeader(z2);
        this.currentImage = i2;
    }

    private boolean readNativeHeader(boolean z2) throws IOException {
        boolean imageHeader = readImageHeader(this.structPointer, this.haveSeeked, z2);
        this.haveSeeked = false;
        return imageHeader;
    }

    private void setImageData(int i2, int i3, int i4, int i5, int i6, byte[] bArr) {
        this.width = i2;
        this.height = i3;
        this.colorSpaceCode = i4;
        this.outColorSpaceCode = i5;
        this.numComponents = i6;
        if (bArr == null) {
            this.iccCS = null;
            return;
        }
        try {
            ICC_Profile iCC_Profile = ICC_Profile.getInstance(bArr);
            byte[] data = iCC_Profile.getData();
            ICC_Profile profile = null;
            if (this.iccCS instanceof ICC_ColorSpace) {
                profile = ((ICC_ColorSpace) this.iccCS).getProfile();
            }
            byte[] data2 = null;
            if (profile != null) {
                data2 = profile.getData();
            }
            if (data2 == null || !Arrays.equals(data2, data)) {
                this.iccCS = new ICC_ColorSpace(iCC_Profile);
                try {
                    this.iccCS.fromRGB(new float[]{1.0f, 0.0f, 0.0f});
                } catch (CMMException e2) {
                    this.iccCS = null;
                    this.cbLock.lock();
                    try {
                        warningOccurred(2);
                        this.cbLock.unlock();
                    } catch (Throwable th) {
                        this.cbLock.unlock();
                        throw th;
                    }
                }
            }
        } catch (IllegalArgumentException e3) {
            this.iccCS = null;
            warningOccurred(2);
        }
    }

    @Override // javax.imageio.ImageReader
    public int getWidth(int i2) throws IOException {
        setThreadLock();
        try {
            if (this.currentImage != i2) {
                this.cbLock.check();
                readHeader(i2, true);
            }
            return this.width;
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public int getHeight(int i2) throws IOException {
        setThreadLock();
        try {
            if (this.currentImage != i2) {
                this.cbLock.check();
                readHeader(i2, true);
            }
            return this.height;
        } finally {
            clearThreadLock();
        }
    }

    private ImageTypeProducer getImageType(int i2) {
        ImageTypeProducer typeProducer = null;
        if (i2 > 0 && i2 < 12) {
            typeProducer = ImageTypeProducer.getTypeProducer(i2);
        }
        return typeProducer;
    }

    @Override // javax.imageio.ImageReader
    public ImageTypeSpecifier getRawImageType(int i2) throws IOException {
        setThreadLock();
        try {
            if (this.currentImage != i2) {
                this.cbLock.check();
                readHeader(i2, true);
            }
            return getImageType(this.colorSpaceCode).getType();
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public Iterator getImageTypes(int i2) throws IOException {
        setThreadLock();
        try {
            return getImageTypesOnThread(i2);
        } finally {
            clearThreadLock();
        }
    }

    private Iterator getImageTypesOnThread(int i2) throws IOException {
        if (this.currentImage != i2) {
            this.cbLock.check();
            readHeader(i2, true);
        }
        ImageTypeProducer imageType = getImageType(this.colorSpaceCode);
        ArrayList arrayList = new ArrayList(1);
        switch (this.colorSpaceCode) {
            case 1:
                arrayList.add(imageType);
                arrayList.add(getImageType(2));
                break;
            case 2:
                arrayList.add(imageType);
                arrayList.add(getImageType(1));
                arrayList.add(getImageType(5));
                break;
            case 3:
                arrayList.add(getImageType(2));
                if (this.iccCS != null) {
                    arrayList.add(new ImageTypeProducer() { // from class: com.sun.imageio.plugins.jpeg.JPEGImageReader.2
                        @Override // com.sun.imageio.plugins.jpeg.ImageTypeProducer
                        protected ImageTypeSpecifier produce() {
                            return ImageTypeSpecifier.createInterleaved(JPEGImageReader.this.iccCS, JPEG.bOffsRGB, 0, false, false);
                        }
                    });
                }
                arrayList.add(getImageType(1));
                arrayList.add(getImageType(5));
                break;
            case 5:
                if (imageType != null) {
                    arrayList.add(imageType);
                    arrayList.add(getImageType(2));
                    break;
                }
                break;
            case 6:
                arrayList.add(imageType);
                break;
            case 7:
                arrayList.add(getImageType(6));
                break;
            case 10:
                if (imageType != null) {
                    arrayList.add(imageType);
                    break;
                }
                break;
        }
        return new ImageTypeIterator(arrayList.iterator());
    }

    private void checkColorConversion(BufferedImage bufferedImage, ImageReadParam imageReadParam) throws IIOException {
        if (imageReadParam != null && (imageReadParam.getSourceBands() != null || imageReadParam.getDestinationBands() != null)) {
            return;
        }
        ColorModel colorModel = bufferedImage.getColorModel();
        if (colorModel instanceof IndexColorModel) {
            throw new IIOException("IndexColorModel not supported");
        }
        ColorSpace colorSpace = colorModel.getColorSpace();
        int type = colorSpace.getType();
        this.convert = null;
        switch (this.outColorSpaceCode) {
            case 1:
                if (type == 5) {
                    setOutColorSpace(this.structPointer, 2);
                    this.outColorSpaceCode = 2;
                    this.numComponents = 3;
                    return;
                } else {
                    if (type != 6) {
                        throw new IIOException("Incompatible color conversion");
                    }
                    return;
                }
            case 2:
                if (type == 6) {
                    if (this.colorSpaceCode == 3) {
                        setOutColorSpace(this.structPointer, 1);
                        this.outColorSpaceCode = 1;
                        this.numComponents = 1;
                        return;
                    }
                    return;
                }
                if (this.iccCS != null && colorModel.getNumComponents() == this.numComponents && colorSpace != this.iccCS) {
                    this.convert = new ColorConvertOp(this.iccCS, colorSpace, null);
                    return;
                }
                if (this.iccCS == null && !colorSpace.isCS_sRGB() && colorModel.getNumComponents() == this.numComponents) {
                    this.convert = new ColorConvertOp(JPEG.JCS.sRGB, colorSpace, null);
                    return;
                } else {
                    if (type != 5) {
                        throw new IIOException("Incompatible color conversion");
                    }
                    return;
                }
            case 3:
            case 4:
            case 7:
            case 8:
            case 9:
            default:
                throw new IIOException("Incompatible color conversion");
            case 5:
                ColorSpace ycc = JPEG.JCS.getYCC();
                if (ycc == null) {
                    throw new IIOException("Incompatible color conversion");
                }
                if (colorSpace != ycc && colorModel.getNumComponents() == this.numComponents) {
                    this.convert = new ColorConvertOp(ycc, colorSpace, null);
                    return;
                }
                return;
            case 6:
                if (type != 5 || colorModel.getNumComponents() != this.numComponents) {
                    throw new IIOException("Incompatible color conversion");
                }
                return;
            case 10:
                ColorSpace ycc2 = JPEG.JCS.getYCC();
                if (ycc2 == null || colorSpace != ycc2 || colorModel.getNumComponents() != this.numComponents) {
                    throw new IIOException("Incompatible color conversion");
                }
                return;
        }
    }

    @Override // javax.imageio.ImageReader
    public ImageReadParam getDefaultReadParam() {
        return new JPEGImageReadParam();
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getStreamMetadata() throws IOException {
        setThreadLock();
        try {
            if (!this.tablesOnlyChecked) {
                this.cbLock.check();
                checkTablesOnly();
            }
            return this.streamMetadata;
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getImageMetadata(int i2) throws IOException {
        setThreadLock();
        try {
            if (this.imageMetadataIndex == i2 && this.imageMetadata != null) {
                return this.imageMetadata;
            }
            this.cbLock.check();
            gotoImage(i2);
            this.imageMetadata = new JPEGMetadata(false, false, this.iis, this);
            this.imageMetadataIndex = i2;
            return this.imageMetadata;
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage read(int i2, ImageReadParam imageReadParam) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            try {
                readInternal(i2, imageReadParam, false);
                BufferedImage bufferedImage = this.image;
                this.image = null;
                clearThreadLock();
                return bufferedImage;
            } catch (IOException e2) {
                resetLibraryState(this.structPointer);
                throw e2;
            } catch (RuntimeException e3) {
                resetLibraryState(this.structPointer);
                throw e3;
            }
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    private Raster readInternal(int i2, ImageReadParam imageReadParam, boolean z2) throws IOException {
        readHeader(i2, false);
        WritableRaster raster = null;
        int numBands = 0;
        if (!z2) {
            Iterator imageTypes = getImageTypes(i2);
            if (!imageTypes.hasNext()) {
                throw new IIOException("Unsupported Image Type");
            }
            if (this.width * this.height > 2147483645) {
                throw new IIOException("Can not read image of the size " + this.width + " by " + this.height);
            }
            this.image = getDestination(imageReadParam, imageTypes, this.width, this.height);
            raster = this.image.getRaster();
            numBands = this.image.getSampleModel().getNumBands();
            checkColorConversion(this.image, imageReadParam);
            checkReadParamBandSettings(imageReadParam, this.numComponents, numBands);
        } else {
            setOutColorSpace(this.structPointer, this.colorSpaceCode);
            this.image = null;
        }
        int[] iArr = JPEG.bandOffsets[this.numComponents - 1];
        int length = z2 ? this.numComponents : numBands;
        this.destinationBands = null;
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        this.destROI = new Rectangle(0, 0, 0, 0);
        computeRegions(imageReadParam, this.width, this.height, this.image, rectangle, this.destROI);
        int sourceXSubsampling = 1;
        int sourceYSubsampling = 1;
        this.minProgressivePass = 0;
        this.maxProgressivePass = Integer.MAX_VALUE;
        if (imageReadParam != null) {
            sourceXSubsampling = imageReadParam.getSourceXSubsampling();
            sourceYSubsampling = imageReadParam.getSourceYSubsampling();
            int[] sourceBands = imageReadParam.getSourceBands();
            if (sourceBands != null) {
                iArr = sourceBands;
                length = iArr.length;
            }
            if (!z2) {
                this.destinationBands = imageReadParam.getDestinationBands();
            }
            this.minProgressivePass = imageReadParam.getSourceMinProgressivePass();
            this.maxProgressivePass = imageReadParam.getSourceMaxProgressivePass();
            if (imageReadParam instanceof JPEGImageReadParam) {
                JPEGImageReadParam jPEGImageReadParam = (JPEGImageReadParam) imageReadParam;
                if (jPEGImageReadParam.areTablesSet()) {
                    this.abbrevQTables = jPEGImageReadParam.getQTables();
                    this.abbrevDCHuffmanTables = jPEGImageReadParam.getDCHuffmanTables();
                    this.abbrevACHuffmanTables = jPEGImageReadParam.getACHuffmanTables();
                }
            }
        }
        int i3 = this.destROI.width * length;
        this.buffer = new DataBufferByte(i3);
        int[] iArr2 = JPEG.bandOffsets[length - 1];
        this.raster = Raster.createInterleavedRaster(this.buffer, this.destROI.width, 1, i3, length, iArr2, (Point) null);
        if (z2) {
            this.target = Raster.createInterleavedRaster(0, this.destROI.width, this.destROI.height, i3, length, iArr2, (Point) null);
        } else {
            this.target = raster;
        }
        int[] sampleSize = this.target.getSampleModel().getSampleSize();
        for (int i4 = 0; i4 < sampleSize.length; i4++) {
            if (sampleSize[i4] <= 0 || sampleSize[i4] > 8) {
                throw new IIOException("Illegal band size: should be 0 < size <= 8");
            }
        }
        boolean z3 = (this.updateListeners == null && this.progressListeners == null) ? false : true;
        initProgressData();
        if (i2 == this.imageMetadataIndex) {
            this.knownPassCount = 0;
            Iterator it = this.imageMetadata.markerSequence.iterator();
            while (it.hasNext()) {
                if (it.next() instanceof SOSMarkerSegment) {
                    this.knownPassCount++;
                }
            }
        }
        this.progInterval = Math.max((this.target.getHeight() - 1) / 20, 1);
        if (this.knownPassCount > 0) {
            this.progInterval *= this.knownPassCount;
        } else if (this.maxProgressivePass != Integer.MAX_VALUE) {
            this.progInterval *= (this.maxProgressivePass - this.minProgressivePass) + 1;
        }
        if (this.debug) {
            System.out.println("**** Read Data *****");
            System.out.println("numRasterBands is " + length);
            System.out.print("srcBands:");
            for (int i5 : iArr) {
                System.out.print(" " + i5);
            }
            System.out.println();
            System.out.println("destination bands is " + ((Object) this.destinationBands));
            if (this.destinationBands != null) {
                for (int i6 = 0; i6 < this.destinationBands.length; i6++) {
                    System.out.print(" " + this.destinationBands[i6]);
                }
                System.out.println();
            }
            System.out.println("sourceROI is " + ((Object) rectangle));
            System.out.println("destROI is " + ((Object) this.destROI));
            System.out.println("periodX is " + sourceXSubsampling);
            System.out.println("periodY is " + sourceYSubsampling);
            System.out.println("minProgressivePass is " + this.minProgressivePass);
            System.out.println("maxProgressivePass is " + this.maxProgressivePass);
            System.out.println("callbackUpdates is " + z3);
        }
        processImageStarted(this.currentImage);
        if (readImage(i2, this.structPointer, this.buffer.getData(), length, iArr, sampleSize, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, sourceXSubsampling, sourceYSubsampling, this.abbrevQTables, this.abbrevDCHuffmanTables, this.abbrevACHuffmanTables, this.minProgressivePass, this.maxProgressivePass, z3)) {
            processReadAborted();
        } else {
            processImageComplete();
        }
        return this.target;
    }

    private void acceptPixels(int i2, boolean z2) {
        if (this.convert != null) {
            this.convert.filter(this.raster, this.raster);
        }
        this.target.setRect(this.destROI.f12372x, this.destROI.f12373y + i2, this.raster);
        this.cbLock.lock();
        try {
            processImageUpdate(this.image, this.destROI.f12372x, this.destROI.f12373y + i2, this.raster.getWidth(), 1, 1, 1, this.destinationBands);
            if (i2 > 0 && i2 % this.progInterval == 0) {
                int height = this.target.getHeight() - 1;
                float f2 = i2 / height;
                if (z2) {
                    if (this.knownPassCount != -1) {
                        processImageProgress(((this.pass + f2) * 100.0f) / this.knownPassCount);
                    } else if (this.maxProgressivePass != Integer.MAX_VALUE) {
                        processImageProgress(((this.pass + f2) * 100.0f) / ((this.maxProgressivePass - this.minProgressivePass) + 1));
                    } else {
                        int iMax = Math.max(2, 10 - this.pass);
                        int i3 = (this.pass + iMax) - 1;
                        this.progInterval = Math.max((height / 20) * i3, i3);
                        if (i2 % this.progInterval == 0) {
                            this.percentToDate = this.previousPassPercentage + (((1.0f - this.previousPassPercentage) * f2) / iMax);
                            if (this.debug) {
                                System.out.print("pass= " + this.pass);
                                System.out.print(", y= " + i2);
                                System.out.print(", progInt= " + this.progInterval);
                                System.out.print(", % of pass: " + f2);
                                System.out.print(", rem. passes: " + iMax);
                                System.out.print(", prev%: " + this.previousPassPercentage);
                                System.out.print(", %ToDate: " + this.percentToDate);
                                System.out.print(" ");
                            }
                            processImageProgress(this.percentToDate * 100.0f);
                        }
                    }
                } else {
                    processImageProgress(f2 * 100.0f);
                }
            }
        } finally {
            this.cbLock.unlock();
        }
    }

    private void initProgressData() {
        this.knownPassCount = -1;
        this.pass = 0;
        this.percentToDate = 0.0f;
        this.previousPassPercentage = 0.0f;
        this.progInterval = 0;
    }

    private void passStarted(int i2) {
        this.cbLock.lock();
        try {
            this.pass = i2;
            this.previousPassPercentage = this.percentToDate;
            processPassStarted(this.image, i2, this.minProgressivePass, this.maxProgressivePass, 0, 0, 1, 1, this.destinationBands);
        } finally {
            this.cbLock.unlock();
        }
    }

    private void passComplete() {
        this.cbLock.lock();
        try {
            processPassComplete(this.image);
        } finally {
            this.cbLock.unlock();
        }
    }

    void thumbnailStarted(int i2) {
        this.cbLock.lock();
        try {
            processThumbnailStarted(this.currentImage, i2);
        } finally {
            this.cbLock.unlock();
        }
    }

    void thumbnailProgress(float f2) {
        this.cbLock.lock();
        try {
            processThumbnailProgress(f2);
        } finally {
            this.cbLock.unlock();
        }
    }

    void thumbnailComplete() {
        this.cbLock.lock();
        try {
            processThumbnailComplete();
        } finally {
            this.cbLock.unlock();
        }
    }

    @Override // javax.imageio.ImageReader
    public void abort() {
        setThreadLock();
        try {
            super.abort();
            abortRead(this.structPointer);
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public boolean canReadRaster() {
        return true;
    }

    @Override // javax.imageio.ImageReader
    public Raster readRaster(int i2, ImageReadParam imageReadParam) throws IOException {
        setThreadLock();
        try {
            try {
                try {
                    this.cbLock.check();
                    Point destinationOffset = null;
                    if (imageReadParam != null) {
                        destinationOffset = imageReadParam.getDestinationOffset();
                        imageReadParam.setDestinationOffset(new Point(0, 0));
                    }
                    Raster internal = readInternal(i2, imageReadParam, true);
                    if (destinationOffset != null) {
                        this.target = this.target.createWritableTranslatedChild(destinationOffset.f12370x, destinationOffset.f12371y);
                    }
                    return internal;
                } catch (IOException e2) {
                    resetLibraryState(this.structPointer);
                    throw e2;
                }
            } catch (RuntimeException e3) {
                resetLibraryState(this.structPointer);
                throw e3;
            }
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public boolean readerSupportsThumbnails() {
        return true;
    }

    @Override // javax.imageio.ImageReader
    public int getNumThumbnails(int i2) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            getImageMetadata(i2);
            JFIFMarkerSegment jFIFMarkerSegment = (JFIFMarkerSegment) this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true);
            int size = 0;
            if (jFIFMarkerSegment != null) {
                size = (jFIFMarkerSegment.thumb == null ? 0 : 1) + jFIFMarkerSegment.extSegments.size();
            }
            return size;
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public int getThumbnailWidth(int i2, int i3) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            if (i3 < 0 || i3 >= getNumThumbnails(i2)) {
                throw new IndexOutOfBoundsException("No such thumbnail");
            }
            int thumbnailWidth = ((JFIFMarkerSegment) this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true)).getThumbnailWidth(i3);
            clearThreadLock();
            return thumbnailWidth;
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    @Override // javax.imageio.ImageReader
    public int getThumbnailHeight(int i2, int i3) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            if (i3 < 0 || i3 >= getNumThumbnails(i2)) {
                throw new IndexOutOfBoundsException("No such thumbnail");
            }
            int thumbnailHeight = ((JFIFMarkerSegment) this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true)).getThumbnailHeight(i3);
            clearThreadLock();
            return thumbnailHeight;
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage readThumbnail(int i2, int i3) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            if (i3 < 0 || i3 >= getNumThumbnails(i2)) {
                throw new IndexOutOfBoundsException("No such thumbnail");
            }
            BufferedImage thumbnail = ((JFIFMarkerSegment) this.imageMetadata.findMarkerSegment(JFIFMarkerSegment.class, true)).getThumbnail(this.iis, i3, this);
            clearThreadLock();
            return thumbnail;
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    private void resetInternalState() {
        resetReader(this.structPointer);
        this.numImages = 0;
        this.imagePositions = new ArrayList();
        this.currentImage = -1;
        this.image = null;
        this.raster = null;
        this.target = null;
        this.buffer = null;
        this.destROI = null;
        this.destinationBands = null;
        this.streamMetadata = null;
        this.imageMetadata = null;
        this.imageMetadataIndex = -1;
        this.haveSeeked = false;
        this.tablesOnlyChecked = false;
        this.iccCS = null;
        initProgressData();
    }

    @Override // javax.imageio.ImageReader
    public void reset() {
        setThreadLock();
        try {
            this.cbLock.check();
            super.reset();
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageReader
    public void dispose() {
        setThreadLock();
        try {
            this.cbLock.check();
            if (this.structPointer != 0) {
                this.disposerRecord.dispose();
                this.structPointer = 0L;
            }
        } finally {
            clearThreadLock();
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageReader$JPEGReaderDisposerRecord.class */
    private static class JPEGReaderDisposerRecord implements DisposerRecord {
        private long pData;

        public JPEGReaderDisposerRecord(long j2) {
            this.pData = j2;
        }

        @Override // sun.java2d.DisposerRecord
        public synchronized void dispose() {
            if (this.pData != 0) {
                JPEGImageReader.disposeReader(this.pData);
                this.pData = 0L;
            }
        }
    }

    private synchronized void setThreadLock() {
        Thread threadCurrentThread = Thread.currentThread();
        if (this.theThread != null) {
            if (this.theThread != threadCurrentThread) {
                throw new IllegalStateException("Attempt to use instance of " + ((Object) this) + " locked on thread " + ((Object) this.theThread) + " from thread " + ((Object) threadCurrentThread));
            }
            this.theLockCount++;
        } else {
            this.theThread = threadCurrentThread;
            this.theLockCount = 1;
        }
    }

    private synchronized void clearThreadLock() {
        Thread threadCurrentThread = Thread.currentThread();
        if (this.theThread == null || this.theThread != threadCurrentThread) {
            throw new IllegalStateException("Attempt to clear thread lock  form wrong thread. Locked thread: " + ((Object) this.theThread) + "; current thread: " + ((Object) threadCurrentThread));
        }
        this.theLockCount--;
        if (this.theLockCount == 0) {
            this.theThread = null;
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageReader$CallBackLock.class */
    private static class CallBackLock {
        private State lockState = State.Unlocked;

        /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageReader$CallBackLock$State.class */
        private enum State {
            Unlocked,
            Locked
        }

        CallBackLock() {
        }

        void check() {
            if (this.lockState != State.Unlocked) {
                throw new IllegalStateException("Access to the reader is not allowed");
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void lock() {
            this.lockState = State.Locked;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void unlock() {
            this.lockState = State.Unlocked;
        }
    }
}
