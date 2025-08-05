package com.sun.imageio.plugins.jpeg;

import com.sun.imageio.plugins.jpeg.DHTMarkerSegment;
import com.sun.imageio.plugins.jpeg.DQTMarkerSegment;
import com.sun.imageio.plugins.jpeg.JPEG;
import com.sun.imageio.plugins.jpeg.SOFMarkerSegment;
import java.awt.Dimension;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.ColorConvertOp;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataFormatImpl;
import javax.imageio.plugins.jpeg.JPEGHuffmanTable;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.plugins.jpeg.JPEGQTable;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageWriter.class */
public class JPEGImageWriter extends ImageWriter {
    private boolean debug;
    private long structPointer;
    private ImageOutputStream ios;
    private Raster srcRas;
    private WritableRaster raster;
    private boolean indexed;
    private IndexColorModel indexCM;
    private boolean convertTosRGB;
    private WritableRaster converted;
    private boolean isAlphaPremultiplied;
    private ColorModel srcCM;
    private List thumbnails;
    private ICC_Profile iccProfile;
    private int sourceXOffset;
    private int sourceYOffset;
    private int sourceWidth;
    private int[] srcBands;
    private int sourceHeight;
    private int currentImage;
    private ColorConvertOp convertOp;
    private JPEGQTable[] streamQTables;
    private JPEGHuffmanTable[] streamDCHuffmanTables;
    private JPEGHuffmanTable[] streamACHuffmanTables;
    private boolean ignoreJFIF;
    private boolean forceJFIF;
    private boolean ignoreAdobe;
    private int newAdobeTransform;
    private boolean writeDefaultJFIF;
    private boolean writeAdobe;
    private JPEGMetadata metadata;
    private boolean sequencePrepared;
    private int numScans;
    private Object disposerReferent;
    private DisposerRecord disposerRecord;
    protected static final int WARNING_DEST_IGNORED = 0;
    protected static final int WARNING_STREAM_METADATA_IGNORED = 1;
    protected static final int WARNING_DEST_METADATA_COMP_MISMATCH = 2;
    protected static final int WARNING_DEST_METADATA_JFIF_MISMATCH = 3;
    protected static final int WARNING_DEST_METADATA_ADOBE_MISMATCH = 4;
    protected static final int WARNING_IMAGE_METADATA_JFIF_MISMATCH = 5;
    protected static final int WARNING_IMAGE_METADATA_ADOBE_MISMATCH = 6;
    protected static final int WARNING_METADATA_NOT_JPEG_FOR_RASTER = 7;
    protected static final int WARNING_NO_BANDS_ON_INDEXED = 8;
    protected static final int WARNING_ILLEGAL_THUMBNAIL = 9;
    protected static final int WARNING_IGNORING_THUMBS = 10;
    protected static final int WARNING_FORCING_JFIF = 11;
    protected static final int WARNING_THUMB_CLIPPED = 12;
    protected static final int WARNING_METADATA_ADJUSTED_FOR_THUMB = 13;
    protected static final int WARNING_NO_RGB_THUMB_AS_INDEXED = 14;
    protected static final int WARNING_NO_GRAY_THUMB_AS_INDEXED = 15;
    private static final int MAX_WARNING = 15;
    static final Dimension[] preferredThumbSizes;
    private Thread theThread;
    private int theLockCount;
    private CallBackLock cbLock;

    private static native void initWriterIDs(Class cls, Class cls2);

    private native long initJPEGImageWriter();

    private native void setDest(long j2);

    private native boolean writeImage(long j2, byte[] bArr, int i2, int i3, int i4, int[] iArr, int i5, int i6, int i7, int i8, int i9, JPEGQTable[] jPEGQTableArr, boolean z2, JPEGHuffmanTable[] jPEGHuffmanTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr2, boolean z3, boolean z4, boolean z5, int i10, int[] iArr2, int[] iArr3, int[] iArr4, int[] iArr5, int[] iArr6, boolean z6, int i11);

    private native void writeTables(long j2, JPEGQTable[] jPEGQTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr, JPEGHuffmanTable[] jPEGHuffmanTableArr2);

    private native void abortWrite(long j2);

    private native void resetWriter(long j2);

    /* JADX INFO: Access modifiers changed from: private */
    public static native void disposeWriter(long j2);

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.imageio.plugins.jpeg.JPEGImageWriter.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                System.loadLibrary("jpeg");
                return null;
            }
        });
        initWriterIDs(JPEGQTable.class, JPEGHuffmanTable.class);
        preferredThumbSizes = new Dimension[]{new Dimension(1, 1), new Dimension(255, 255)};
    }

    public JPEGImageWriter(ImageWriterSpi imageWriterSpi) {
        super(imageWriterSpi);
        this.debug = false;
        this.structPointer = 0L;
        this.ios = null;
        this.srcRas = null;
        this.raster = null;
        this.indexed = false;
        this.indexCM = null;
        this.convertTosRGB = false;
        this.converted = null;
        this.isAlphaPremultiplied = false;
        this.srcCM = null;
        this.thumbnails = null;
        this.iccProfile = null;
        this.sourceXOffset = 0;
        this.sourceYOffset = 0;
        this.sourceWidth = 0;
        this.srcBands = null;
        this.sourceHeight = 0;
        this.currentImage = 0;
        this.convertOp = null;
        this.streamQTables = null;
        this.streamDCHuffmanTables = null;
        this.streamACHuffmanTables = null;
        this.ignoreJFIF = false;
        this.forceJFIF = false;
        this.ignoreAdobe = false;
        this.newAdobeTransform = -1;
        this.writeDefaultJFIF = false;
        this.writeAdobe = false;
        this.metadata = null;
        this.sequencePrepared = false;
        this.numScans = 0;
        this.disposerReferent = new Object();
        this.theThread = null;
        this.theLockCount = 0;
        this.cbLock = new CallBackLock();
        this.structPointer = initJPEGImageWriter();
        this.disposerRecord = new JPEGWriterDisposerRecord(this.structPointer);
        Disposer.addRecord(this.disposerReferent, this.disposerRecord);
    }

    @Override // javax.imageio.ImageWriter
    public void setOutput(Object obj) {
        setThreadLock();
        try {
            this.cbLock.check();
            super.setOutput(obj);
            resetInternalState();
            this.ios = (ImageOutputStream) obj;
            setDest(this.structPointer);
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageWriter
    public ImageWriteParam getDefaultWriteParam() {
        return new JPEGImageWriteParam(null);
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultStreamMetadata(ImageWriteParam imageWriteParam) {
        setThreadLock();
        try {
            return new JPEGMetadata(imageWriteParam, this);
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageWriter
    public IIOMetadata getDefaultImageMetadata(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        setThreadLock();
        try {
            JPEGMetadata jPEGMetadata = new JPEGMetadata(imageTypeSpecifier, imageWriteParam, this);
            clearThreadLock();
            return jPEGMetadata;
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertStreamMetadata(IIOMetadata iIOMetadata, ImageWriteParam imageWriteParam) {
        if ((iIOMetadata instanceof JPEGMetadata) && ((JPEGMetadata) iIOMetadata).isStream) {
            return iIOMetadata;
        }
        return null;
    }

    @Override // javax.imageio.ImageWriter, javax.imageio.ImageTranscoder
    public IIOMetadata convertImageMetadata(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        setThreadLock();
        try {
            IIOMetadata iIOMetadataConvertImageMetadataOnThread = convertImageMetadataOnThread(iIOMetadata, imageTypeSpecifier, imageWriteParam);
            clearThreadLock();
            return iIOMetadataConvertImageMetadataOnThread;
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    private IIOMetadata convertImageMetadataOnThread(IIOMetadata iIOMetadata, ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam) {
        Node asTree;
        if (iIOMetadata instanceof JPEGMetadata) {
            if (!((JPEGMetadata) iIOMetadata).isStream) {
                return iIOMetadata;
            }
            return null;
        }
        if (iIOMetadata.isStandardMetadataFormatSupported() && (asTree = iIOMetadata.getAsTree(IIOMetadataFormatImpl.standardMetadataFormatName)) != null) {
            JPEGMetadata jPEGMetadata = new JPEGMetadata(imageTypeSpecifier, imageWriteParam, this);
            try {
                jPEGMetadata.setFromTree(IIOMetadataFormatImpl.standardMetadataFormatName, asTree);
                return jPEGMetadata;
            } catch (IIOInvalidTreeException e2) {
                return null;
            }
        }
        return null;
    }

    @Override // javax.imageio.ImageWriter
    public int getNumThumbnailsSupported(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam, IIOMetadata iIOMetadata, IIOMetadata iIOMetadata2) {
        if (jfifOK(imageTypeSpecifier, imageWriteParam, iIOMetadata, iIOMetadata2)) {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    @Override // javax.imageio.ImageWriter
    public Dimension[] getPreferredThumbnailSizes(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam, IIOMetadata iIOMetadata, IIOMetadata iIOMetadata2) {
        if (jfifOK(imageTypeSpecifier, imageWriteParam, iIOMetadata, iIOMetadata2)) {
            return (Dimension[]) preferredThumbSizes.clone();
        }
        return null;
    }

    private boolean jfifOK(ImageTypeSpecifier imageTypeSpecifier, ImageWriteParam imageWriteParam, IIOMetadata iIOMetadata, IIOMetadata iIOMetadata2) {
        JPEGMetadata jPEGMetadata;
        if (imageTypeSpecifier != null && !JPEG.isJFIFcompliant(imageTypeSpecifier, true)) {
            return false;
        }
        if (iIOMetadata2 != null) {
            if (iIOMetadata2 instanceof JPEGMetadata) {
                jPEGMetadata = (JPEGMetadata) iIOMetadata2;
            } else {
                jPEGMetadata = (JPEGMetadata) convertImageMetadata(iIOMetadata2, imageTypeSpecifier, imageWriteParam);
            }
            if (jPEGMetadata.findMarkerSegment(JFIFMarkerSegment.class, true) == null) {
                return false;
            }
            return true;
        }
        return true;
    }

    @Override // javax.imageio.ImageWriter
    public boolean canWriteRasters() {
        return true;
    }

    @Override // javax.imageio.ImageWriter
    public void write(IIOMetadata iIOMetadata, IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            writeOnThread(iIOMetadata, iIOImage, imageWriteParam);
            clearThreadLock();
        } catch (Throwable th) {
            clearThreadLock();
            throw th;
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:119:0x04af  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void writeOnThread(javax.imageio.metadata.IIOMetadata r30, javax.imageio.IIOImage r31, javax.imageio.ImageWriteParam r32) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 3284
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.imageio.plugins.jpeg.JPEGImageWriter.writeOnThread(javax.imageio.metadata.IIOMetadata, javax.imageio.IIOImage, javax.imageio.ImageWriteParam):void");
    }

    @Override // javax.imageio.ImageWriter
    public boolean canWriteSequence() {
        return true;
    }

    @Override // javax.imageio.ImageWriter
    public void prepareWriteSequence(IIOMetadata iIOMetadata) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            prepareWriteSequenceOnThread(iIOMetadata);
        } finally {
            clearThreadLock();
        }
    }

    private void prepareWriteSequenceOnThread(IIOMetadata iIOMetadata) throws IOException {
        if (this.ios == null) {
            throw new IllegalStateException("Output has not been set!");
        }
        if (iIOMetadata != null) {
            if (iIOMetadata instanceof JPEGMetadata) {
                JPEGMetadata jPEGMetadata = (JPEGMetadata) iIOMetadata;
                if (!jPEGMetadata.isStream) {
                    throw new IllegalArgumentException("Invalid stream metadata object.");
                }
                if (this.currentImage != 0) {
                    throw new IIOException("JPEG Stream metadata must precede all images");
                }
                if (this.sequencePrepared) {
                    throw new IIOException("Stream metadata already written!");
                }
                this.streamQTables = collectQTablesFromMetadata(jPEGMetadata);
                if (this.debug) {
                    System.out.println("after collecting from stream metadata, streamQTables.length is " + this.streamQTables.length);
                }
                if (this.streamQTables == null) {
                    this.streamQTables = JPEG.getDefaultQTables();
                }
                this.streamDCHuffmanTables = collectHTablesFromMetadata(jPEGMetadata, true);
                if (this.streamDCHuffmanTables == null) {
                    this.streamDCHuffmanTables = JPEG.getDefaultHuffmanTables(true);
                }
                this.streamACHuffmanTables = collectHTablesFromMetadata(jPEGMetadata, false);
                if (this.streamACHuffmanTables == null) {
                    this.streamACHuffmanTables = JPEG.getDefaultHuffmanTables(false);
                }
                writeTables(this.structPointer, this.streamQTables, this.streamDCHuffmanTables, this.streamACHuffmanTables);
            } else {
                throw new IIOException("Stream metadata must be JPEG metadata");
            }
        }
        this.sequencePrepared = true;
    }

    @Override // javax.imageio.ImageWriter
    public void writeToSequence(IIOImage iIOImage, ImageWriteParam imageWriteParam) throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            if (!this.sequencePrepared) {
                throw new IllegalStateException("sequencePrepared not called!");
            }
            write(null, iIOImage, imageWriteParam);
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageWriter
    public void endWriteSequence() throws IOException {
        setThreadLock();
        try {
            this.cbLock.check();
            if (!this.sequencePrepared) {
                throw new IllegalStateException("sequencePrepared not called!");
            }
            this.sequencePrepared = false;
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageWriter
    public synchronized void abort() {
        setThreadLock();
        try {
            super.abort();
            abortWrite(this.structPointer);
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageWriter
    protected synchronized void clearAbortRequest() {
        setThreadLock();
        try {
            this.cbLock.check();
            if (abortRequested()) {
                super.clearAbortRequest();
                resetWriter(this.structPointer);
                setDest(this.structPointer);
            }
        } finally {
            clearThreadLock();
        }
    }

    private void resetInternalState() {
        resetWriter(this.structPointer);
        this.srcRas = null;
        this.raster = null;
        this.convertTosRGB = false;
        this.currentImage = 0;
        this.numScans = 0;
        this.metadata = null;
    }

    @Override // javax.imageio.ImageWriter
    public void reset() {
        setThreadLock();
        try {
            this.cbLock.check();
            super.reset();
        } finally {
            clearThreadLock();
        }
    }

    @Override // javax.imageio.ImageWriter
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

    void warningOccurred(int i2) {
        this.cbLock.lock();
        try {
            if (i2 < 0 || i2 > 15) {
                throw new InternalError("Invalid warning index");
            }
            processWarningOccurred(this.currentImage, "com.sun.imageio.plugins.jpeg.JPEGImageWriterResources", Integer.toString(i2));
        } finally {
            this.cbLock.unlock();
        }
    }

    void warningWithMessage(String str) {
        this.cbLock.lock();
        try {
            processWarningOccurred(this.currentImage, str);
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

    private void checkSOFBands(SOFMarkerSegment sOFMarkerSegment, int i2) throws IIOException {
        if (sOFMarkerSegment != null && sOFMarkerSegment.componentSpecs.length != i2) {
            throw new IIOException("Metadata components != number of destination bands");
        }
    }

    private void checkJFIF(JFIFMarkerSegment jFIFMarkerSegment, ImageTypeSpecifier imageTypeSpecifier, boolean z2) {
        if (jFIFMarkerSegment != null && !JPEG.isJFIFcompliant(imageTypeSpecifier, z2)) {
            this.ignoreJFIF = true;
            warningOccurred(z2 ? 5 : 3);
        }
    }

    private void checkAdobe(AdobeMarkerSegment adobeMarkerSegment, ImageTypeSpecifier imageTypeSpecifier, boolean z2) {
        int iTransformForType;
        if (adobeMarkerSegment != null && adobeMarkerSegment.transform != (iTransformForType = JPEG.transformForType(imageTypeSpecifier, z2))) {
            warningOccurred(z2 ? 6 : 4);
            if (iTransformForType == -1) {
                this.ignoreAdobe = true;
            } else {
                this.newAdobeTransform = iTransformForType;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    private int[] collectScans(JPEGMetadata jPEGMetadata, SOFMarkerSegment sOFMarkerSegment) {
        ArrayList arrayList = new ArrayList();
        for (MarkerSegment markerSegment : jPEGMetadata.markerSequence) {
            if (markerSegment instanceof SOSMarkerSegment) {
                arrayList.add(markerSegment);
            }
        }
        int[] iArr = null;
        this.numScans = 0;
        if (!arrayList.isEmpty()) {
            this.numScans = arrayList.size();
            iArr = new int[this.numScans * 9];
            int i2 = 0;
            for (int i3 = 0; i3 < this.numScans; i3++) {
                SOSMarkerSegment sOSMarkerSegment = (SOSMarkerSegment) arrayList.get(i3);
                int i4 = i2;
                int i5 = i2 + 1;
                iArr[i4] = sOSMarkerSegment.componentSpecs.length;
                for (int i6 = 0; i6 < 4; i6++) {
                    if (i6 < sOSMarkerSegment.componentSpecs.length) {
                        int i7 = sOSMarkerSegment.componentSpecs[i6].componentSelector;
                        int i8 = 0;
                        while (true) {
                            if (i8 >= sOFMarkerSegment.componentSpecs.length) {
                                break;
                            }
                            if (i7 != sOFMarkerSegment.componentSpecs[i8].componentId) {
                                i8++;
                            } else {
                                int i9 = i5;
                                i5++;
                                iArr[i9] = i8;
                                break;
                            }
                        }
                    } else {
                        int i10 = i5;
                        i5++;
                        iArr[i10] = 0;
                    }
                }
                int i11 = i5;
                int i12 = i5 + 1;
                iArr[i11] = sOSMarkerSegment.startSpectralSelection;
                int i13 = i12 + 1;
                iArr[i12] = sOSMarkerSegment.endSpectralSelection;
                int i14 = i13 + 1;
                iArr[i13] = sOSMarkerSegment.approxHigh;
                i2 = i14 + 1;
                iArr[i14] = sOSMarkerSegment.approxLow;
            }
        }
        return iArr;
    }

    private JPEGQTable[] collectQTablesFromMetadata(JPEGMetadata jPEGMetadata) {
        ArrayList arrayList = new ArrayList();
        for (MarkerSegment markerSegment : jPEGMetadata.markerSequence) {
            if (markerSegment instanceof DQTMarkerSegment) {
                arrayList.addAll(((DQTMarkerSegment) markerSegment).tables);
            }
        }
        JPEGQTable[] jPEGQTableArr = null;
        if (arrayList.size() != 0) {
            jPEGQTableArr = new JPEGQTable[arrayList.size()];
            for (int i2 = 0; i2 < jPEGQTableArr.length; i2++) {
                jPEGQTableArr[i2] = new JPEGQTable(((DQTMarkerSegment.Qtable) arrayList.get(i2)).data);
            }
        }
        return jPEGQTableArr;
    }

    private JPEGHuffmanTable[] collectHTablesFromMetadata(JPEGMetadata jPEGMetadata, boolean z2) throws IIOException {
        ArrayList arrayList = new ArrayList();
        for (MarkerSegment markerSegment : jPEGMetadata.markerSequence) {
            if (markerSegment instanceof DHTMarkerSegment) {
                DHTMarkerSegment dHTMarkerSegment = (DHTMarkerSegment) markerSegment;
                for (int i2 = 0; i2 < dHTMarkerSegment.tables.size(); i2++) {
                    DHTMarkerSegment.Htable htable = (DHTMarkerSegment.Htable) dHTMarkerSegment.tables.get(i2);
                    if (htable.tableClass == (z2 ? 0 : 1)) {
                        arrayList.add(htable);
                    }
                }
            }
        }
        JPEGHuffmanTable[] jPEGHuffmanTableArr = null;
        if (arrayList.size() != 0) {
            DHTMarkerSegment.Htable[] htableArr = new DHTMarkerSegment.Htable[arrayList.size()];
            arrayList.toArray(htableArr);
            jPEGHuffmanTableArr = new JPEGHuffmanTable[arrayList.size()];
            for (int i3 = 0; i3 < jPEGHuffmanTableArr.length; i3++) {
                jPEGHuffmanTableArr[i3] = null;
                for (int i4 = 0; i4 < arrayList.size(); i4++) {
                    if (htableArr[i4].tableID == i3) {
                        if (jPEGHuffmanTableArr[i3] != null) {
                            throw new IIOException("Metadata has duplicate Htables!");
                        }
                        jPEGHuffmanTableArr[i3] = new JPEGHuffmanTable(htableArr[i4].numCodes, htableArr[i4].values);
                    }
                }
            }
        }
        return jPEGHuffmanTableArr;
    }

    private int getSrcCSType(ImageTypeSpecifier imageTypeSpecifier) {
        return getSrcCSType(imageTypeSpecifier.getColorModel());
    }

    private int getSrcCSType(RenderedImage renderedImage) {
        return getSrcCSType(renderedImage.getColorModel());
    }

    private int getSrcCSType(ColorModel colorModel) {
        int i2 = 0;
        if (colorModel != null) {
            boolean zHasAlpha = colorModel.hasAlpha();
            ColorSpace colorSpace = colorModel.getColorSpace();
            switch (colorSpace.getType()) {
                case 3:
                    if (zHasAlpha) {
                        i2 = 7;
                        break;
                    } else {
                        i2 = 3;
                        break;
                    }
                case 5:
                    if (zHasAlpha) {
                        i2 = 6;
                        break;
                    } else {
                        i2 = 2;
                        break;
                    }
                case 6:
                    i2 = 1;
                    break;
                case 13:
                    if (colorSpace == JPEG.JCS.getYCC() && !zHasAlpha) {
                    }
                    break;
                case 9:
                    i2 = 4;
                    break;
            }
        }
        return i2;
    }

    private int getDestCSType(ImageTypeSpecifier imageTypeSpecifier) {
        ColorModel colorModel = imageTypeSpecifier.getColorModel();
        boolean zHasAlpha = colorModel.hasAlpha();
        ColorSpace colorSpace = colorModel.getColorSpace();
        int i2 = 0;
        switch (colorSpace.getType()) {
            case 3:
                if (zHasAlpha) {
                    i2 = 7;
                    break;
                } else {
                    i2 = 3;
                    break;
                }
            case 5:
                if (zHasAlpha) {
                    i2 = 6;
                    break;
                } else {
                    i2 = 2;
                    break;
                }
            case 6:
                i2 = 1;
                break;
            case 13:
                if (colorSpace == JPEG.JCS.getYCC() && !zHasAlpha) {
                }
                break;
            case 9:
                i2 = 4;
                break;
        }
        return i2;
    }

    private int getDefaultDestCSType(ImageTypeSpecifier imageTypeSpecifier) {
        return getDefaultDestCSType(imageTypeSpecifier.getColorModel());
    }

    private int getDefaultDestCSType(RenderedImage renderedImage) {
        return getDefaultDestCSType(renderedImage.getColorModel());
    }

    private int getDefaultDestCSType(ColorModel colorModel) {
        int i2 = 0;
        if (colorModel != null) {
            boolean zHasAlpha = colorModel.hasAlpha();
            ColorSpace colorSpace = colorModel.getColorSpace();
            switch (colorSpace.getType()) {
                case 3:
                    if (zHasAlpha) {
                        i2 = 7;
                        break;
                    } else {
                        i2 = 3;
                        break;
                    }
                case 5:
                    if (zHasAlpha) {
                        i2 = 7;
                        break;
                    } else {
                        i2 = 3;
                        break;
                    }
                case 6:
                    i2 = 1;
                    break;
                case 13:
                    if (colorSpace == JPEG.JCS.getYCC() && !zHasAlpha) {
                    }
                    break;
                case 9:
                    i2 = 11;
                    break;
            }
        }
        return i2;
    }

    private boolean isSubsampled(SOFMarkerSegment.ComponentSpec[] componentSpecArr) {
        int i2 = componentSpecArr[0].HsamplingFactor;
        int i3 = componentSpecArr[0].VsamplingFactor;
        for (int i4 = 1; i4 < componentSpecArr.length; i4++) {
            if (componentSpecArr[i4].HsamplingFactor != i2 || componentSpecArr[i4].HsamplingFactor != i2) {
                return true;
            }
        }
        return false;
    }

    private void writeMetadata() throws IOException {
        if (this.metadata == null) {
            if (this.writeDefaultJFIF) {
                JFIFMarkerSegment.writeDefaultJFIF(this.ios, this.thumbnails, this.iccProfile, this);
            }
            if (this.writeAdobe) {
                AdobeMarkerSegment.writeAdobeSegment(this.ios, this.newAdobeTransform);
                return;
            }
            return;
        }
        this.metadata.writeToStream(this.ios, this.ignoreJFIF, this.forceJFIF, this.thumbnails, this.iccProfile, this.ignoreAdobe, this.newAdobeTransform, this);
    }

    private void grabPixels(int i2) {
        Raster rasterCreateChild;
        if (this.indexed) {
            rasterCreateChild = this.indexCM.convertToIntDiscrete(this.srcRas.createChild(this.sourceXOffset, this.sourceYOffset + i2, this.sourceWidth, 1, 0, 0, new int[]{0}), this.indexCM.getTransparency() != 1).getRaster();
        } else {
            rasterCreateChild = this.srcRas.createChild(this.sourceXOffset, this.sourceYOffset + i2, this.sourceWidth, 1, 0, 0, this.srcBands);
        }
        if (this.convertTosRGB) {
            if (this.debug) {
                System.out.println("Converting to sRGB");
            }
            this.converted = this.convertOp.filter(rasterCreateChild, this.converted);
            rasterCreateChild = this.converted;
        }
        if (this.isAlphaPremultiplied) {
            WritableRaster writableRasterCreateCompatibleWritableRaster = rasterCreateChild.createCompatibleWritableRaster();
            writableRasterCreateCompatibleWritableRaster.setPixels(rasterCreateChild.getMinX(), rasterCreateChild.getMinY(), rasterCreateChild.getWidth(), rasterCreateChild.getHeight(), rasterCreateChild.getPixels(rasterCreateChild.getMinX(), rasterCreateChild.getMinY(), rasterCreateChild.getWidth(), rasterCreateChild.getHeight(), (int[]) null));
            this.srcCM.coerceData(writableRasterCreateCompatibleWritableRaster, false);
            rasterCreateChild = writableRasterCreateCompatibleWritableRaster.createChild(writableRasterCreateCompatibleWritableRaster.getMinX(), writableRasterCreateCompatibleWritableRaster.getMinY(), writableRasterCreateCompatibleWritableRaster.getWidth(), writableRasterCreateCompatibleWritableRaster.getHeight(), 0, 0, this.srcBands);
        }
        this.raster.setRect(rasterCreateChild);
        if (i2 <= 7 || i2 % 8 != 0) {
            return;
        }
        this.cbLock.lock();
        try {
            processImageProgress((i2 / this.sourceHeight) * 100.0f);
            this.cbLock.unlock();
        } catch (Throwable th) {
            this.cbLock.unlock();
            throw th;
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageWriter$JPEGWriterDisposerRecord.class */
    private static class JPEGWriterDisposerRecord implements DisposerRecord {
        private long pData;

        public JPEGWriterDisposerRecord(long j2) {
            this.pData = j2;
        }

        @Override // sun.java2d.DisposerRecord
        public synchronized void dispose() {
            if (this.pData != 0) {
                JPEGImageWriter.disposeWriter(this.pData);
                this.pData = 0L;
            }
        }
    }

    private void writeOutputData(byte[] bArr, int i2, int i3) throws IOException {
        this.cbLock.lock();
        try {
            this.ios.write(bArr, i2, i3);
            this.cbLock.unlock();
        } catch (Throwable th) {
            this.cbLock.unlock();
            throw th;
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
            throw new IllegalStateException("Attempt to clear thread lock form wrong thread. Locked thread: " + ((Object) this.theThread) + "; current thread: " + ((Object) threadCurrentThread));
        }
        this.theLockCount--;
        if (this.theLockCount == 0) {
            this.theThread = null;
        }
    }

    /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageWriter$CallBackLock.class */
    private static class CallBackLock {
        private State lockState = State.Unlocked;

        /* loaded from: rt.jar:com/sun/imageio/plugins/jpeg/JPEGImageWriter$CallBackLock$State.class */
        private enum State {
            Unlocked,
            Locked
        }

        CallBackLock() {
        }

        void check() {
            if (this.lockState != State.Unlocked) {
                throw new IllegalStateException("Access to the writer is not allowed");
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
