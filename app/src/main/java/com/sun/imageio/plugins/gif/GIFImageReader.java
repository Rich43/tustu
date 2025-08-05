package com.sun.imageio.plugins.gif;

import com.sun.imageio.plugins.common.ReaderUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.PixelInterleavedSampleModel;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/gif/GIFImageReader.class */
public class GIFImageReader extends ImageReader {
    ImageInputStream stream;
    boolean gotHeader;
    GIFStreamMetadata streamMetadata;
    int currIndex;
    GIFImageMetadata imageMetadata;
    List imageStartPosition;
    int imageMetadataLength;
    int numImages;
    byte[] block;
    int blockLength;
    int bitPos;
    int nextByte;
    int initCodeSize;
    int clearCode;
    int eofCode;
    int next32Bits;
    boolean lastBlockFound;
    BufferedImage theImage;
    WritableRaster theTile;
    int width;
    int height;
    int streamX;
    int streamY;
    int rowsDone;
    int interlacePass;
    private byte[] fallbackColorTable;
    Rectangle sourceRegion;
    int sourceXSubsampling;
    int sourceYSubsampling;
    int sourceMinProgressivePass;
    int sourceMaxProgressivePass;
    Point destinationOffset;
    Rectangle destinationRegion;
    int updateMinY;
    int updateYStep;
    boolean decodeThisRow;
    int destY;
    byte[] rowBuf;
    static final int[] interlaceIncrement = {8, 8, 4, 2, -1};
    static final int[] interlaceOffset = {0, 4, 2, 1, -1};
    private static byte[] defaultPalette = null;

    public GIFImageReader(ImageReaderSpi imageReaderSpi) {
        super(imageReaderSpi);
        this.stream = null;
        this.gotHeader = false;
        this.streamMetadata = null;
        this.currIndex = -1;
        this.imageMetadata = null;
        this.imageStartPosition = new ArrayList();
        this.numImages = -1;
        this.block = new byte[255];
        this.blockLength = 0;
        this.bitPos = 0;
        this.nextByte = 0;
        this.next32Bits = 0;
        this.lastBlockFound = false;
        this.theImage = null;
        this.theTile = null;
        this.width = -1;
        this.height = -1;
        this.streamX = -1;
        this.streamY = -1;
        this.rowsDone = 0;
        this.interlacePass = 0;
        this.fallbackColorTable = null;
        this.decodeThisRow = true;
        this.destY = 0;
    }

    @Override // javax.imageio.ImageReader
    public void setInput(Object obj, boolean z2, boolean z3) {
        super.setInput(obj, z2, z3);
        if (obj != null) {
            if (!(obj instanceof ImageInputStream)) {
                throw new IllegalArgumentException("input not an ImageInputStream!");
            }
            this.stream = (ImageInputStream) obj;
        } else {
            this.stream = null;
        }
        resetStreamSettings();
    }

    @Override // javax.imageio.ImageReader
    public int getNumImages(boolean z2) throws IIOException {
        if (this.stream == null) {
            throw new IllegalStateException("Input not set!");
        }
        if (this.seekForwardOnly && z2) {
            throw new IllegalStateException("seekForwardOnly and allowSearch can't both be true!");
        }
        if (this.numImages > 0) {
            return this.numImages;
        }
        if (z2) {
            this.numImages = locateImage(Integer.MAX_VALUE) + 1;
        }
        return this.numImages;
    }

    private void checkIndex(int i2) {
        if (i2 < this.minIndex) {
            throw new IndexOutOfBoundsException("imageIndex < minIndex!");
        }
        if (this.seekForwardOnly) {
            this.minIndex = i2;
        }
    }

    @Override // javax.imageio.ImageReader
    public int getWidth(int i2) throws IIOException {
        checkIndex(i2);
        if (locateImage(i2) != i2) {
            throw new IndexOutOfBoundsException();
        }
        readMetadata();
        return this.imageMetadata.imageWidth;
    }

    @Override // javax.imageio.ImageReader
    public int getHeight(int i2) throws IIOException {
        checkIndex(i2);
        if (locateImage(i2) != i2) {
            throw new IndexOutOfBoundsException();
        }
        readMetadata();
        return this.imageMetadata.imageHeight;
    }

    private ImageTypeSpecifier createIndexed(byte[] bArr, byte[] bArr2, byte[] bArr3, int i2) {
        IndexColorModel indexColorModel;
        SampleModel multiPixelPackedSampleModel;
        if (this.imageMetadata.transparentColorFlag) {
            indexColorModel = new IndexColorModel(i2, bArr.length, bArr, bArr2, bArr3, Math.min(this.imageMetadata.transparentColorIndex, bArr.length - 1));
        } else {
            indexColorModel = new IndexColorModel(i2, bArr.length, bArr, bArr2, bArr3);
        }
        if (i2 == 8) {
            multiPixelPackedSampleModel = new PixelInterleavedSampleModel(0, 1, 1, 1, 1, new int[]{0});
        } else {
            multiPixelPackedSampleModel = new MultiPixelPackedSampleModel(0, 1, 1, i2);
        }
        return new ImageTypeSpecifier(indexColorModel, multiPixelPackedSampleModel);
    }

    @Override // javax.imageio.ImageReader
    public Iterator getImageTypes(int i2) throws IIOException {
        byte[] bArr;
        int i3;
        checkIndex(i2);
        if (locateImage(i2) != i2) {
            throw new IndexOutOfBoundsException();
        }
        readMetadata();
        ArrayList arrayList = new ArrayList(1);
        if (this.imageMetadata.localColorTable != null) {
            bArr = this.imageMetadata.localColorTable;
            this.fallbackColorTable = this.imageMetadata.localColorTable;
        } else {
            bArr = this.streamMetadata.globalColorTable;
        }
        if (bArr == null) {
            if (this.fallbackColorTable == null) {
                processWarningOccurred("Use default color table.");
                this.fallbackColorTable = getDefaultPalette();
            }
            bArr = this.fallbackColorTable;
        }
        int length = bArr.length / 3;
        if (length == 2) {
            i3 = 1;
        } else if (length == 4) {
            i3 = 2;
        } else if (length == 8 || length == 16) {
            i3 = 4;
        } else {
            i3 = 8;
        }
        int i4 = 1 << i3;
        byte[] bArr2 = new byte[i4];
        byte[] bArr3 = new byte[i4];
        byte[] bArr4 = new byte[i4];
        int i5 = 0;
        for (int i6 = 0; i6 < length; i6++) {
            int i7 = i5;
            int i8 = i5 + 1;
            bArr2[i6] = bArr[i7];
            int i9 = i8 + 1;
            bArr3[i6] = bArr[i8];
            i5 = i9 + 1;
            bArr4[i6] = bArr[i9];
        }
        arrayList.add(createIndexed(bArr2, bArr3, bArr4, i3));
        return arrayList.iterator();
    }

    @Override // javax.imageio.ImageReader
    public ImageReadParam getDefaultReadParam() {
        return new ImageReadParam();
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getStreamMetadata() throws IIOException {
        readHeader();
        return this.streamMetadata;
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getImageMetadata(int i2) throws IIOException {
        checkIndex(i2);
        if (locateImage(i2) != i2) {
            throw new IndexOutOfBoundsException("Bad image index!");
        }
        readMetadata();
        return this.imageMetadata;
    }

    private void initNext32Bits() {
        this.next32Bits = this.block[0] & 255;
        this.next32Bits |= (this.block[1] & 255) << 8;
        this.next32Bits |= (this.block[2] & 255) << 16;
        this.next32Bits |= this.block[3] << 24;
        this.nextByte = 4;
    }

    private int getCode(int i2, int i3) throws IOException {
        if (this.bitPos + i2 > 32) {
            return this.eofCode;
        }
        int i4 = (this.next32Bits >> this.bitPos) & i3;
        this.bitPos += i2;
        while (this.bitPos >= 8 && !this.lastBlockFound) {
            this.next32Bits >>>= 8;
            this.bitPos -= 8;
            if (this.nextByte >= this.blockLength) {
                this.blockLength = this.stream.readUnsignedByte();
                if (this.blockLength == 0) {
                    this.lastBlockFound = true;
                    return i4;
                }
                int i5 = this.blockLength;
                int i6 = 0;
                while (i5 > 0) {
                    int i7 = this.stream.read(this.block, i6, i5);
                    i6 += i7;
                    i5 -= i7;
                }
                this.nextByte = 0;
            }
            int i8 = this.next32Bits;
            byte[] bArr = this.block;
            int i9 = this.nextByte;
            this.nextByte = i9 + 1;
            this.next32Bits = i8 | (bArr[i9] << 24);
        }
        return i4;
    }

    public void initializeStringTable(int[] iArr, byte[] bArr, byte[] bArr2, int[] iArr2) {
        int i2 = 1 << this.initCodeSize;
        for (int i3 = 0; i3 < i2; i3++) {
            iArr[i3] = -1;
            bArr[i3] = (byte) i3;
            bArr2[i3] = (byte) i3;
            iArr2[i3] = 1;
        }
        for (int i4 = i2; i4 < 4096; i4++) {
            iArr[i4] = -1;
            iArr2[i4] = 1;
        }
    }

    private void outputRow() {
        int iMin = Math.min(this.sourceRegion.width, this.destinationRegion.width * this.sourceXSubsampling);
        int i2 = this.destinationRegion.f12372x;
        if (this.sourceXSubsampling == 1) {
            this.theTile.setDataElements(i2, this.destY, iMin, 1, this.rowBuf);
        } else {
            int i3 = 0;
            while (i3 < iMin) {
                this.theTile.setSample(i2, this.destY, 0, this.rowBuf[i3] & 255);
                i3 += this.sourceXSubsampling;
                i2++;
            }
        }
        if (this.updateListeners != null) {
            processImageUpdate(this.theImage, i2, this.destY, iMin, 1, 1, this.updateYStep, new int[]{0});
        }
    }

    private void computeDecodeThisRow() {
        this.decodeThisRow = this.destY < this.destinationRegion.f12373y + this.destinationRegion.height && this.streamY >= this.sourceRegion.f12373y && this.streamY < this.sourceRegion.f12373y + this.sourceRegion.height && (this.streamY - this.sourceRegion.f12373y) % this.sourceYSubsampling == 0;
    }

    private void outputPixels(byte[] bArr, int i2) {
        if (this.interlacePass < this.sourceMinProgressivePass || this.interlacePass > this.sourceMaxProgressivePass) {
            return;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            if (this.streamX >= this.sourceRegion.f12372x) {
                this.rowBuf[this.streamX - this.sourceRegion.f12372x] = bArr[i3];
            }
            this.streamX++;
            if (this.streamX == this.width) {
                this.rowsDone++;
                processImageProgress((100.0f * this.rowsDone) / this.height);
                if (this.decodeThisRow) {
                    outputRow();
                }
                this.streamX = 0;
                if (this.imageMetadata.interlaceFlag) {
                    this.streamY += interlaceIncrement[this.interlacePass];
                    if (this.streamY >= this.height) {
                        if (this.updateListeners != null) {
                            processPassComplete(this.theImage);
                        }
                        this.interlacePass++;
                        if (this.interlacePass > this.sourceMaxProgressivePass) {
                            return;
                        }
                        this.streamY = interlaceOffset[this.interlacePass];
                        startPass(this.interlacePass);
                    }
                } else {
                    this.streamY++;
                }
                this.destY = this.destinationRegion.f12373y + ((this.streamY - this.sourceRegion.f12373y) / this.sourceYSubsampling);
                computeDecodeThisRow();
            }
        }
    }

    private void readHeader() throws IIOException {
        if (this.gotHeader) {
            return;
        }
        if (this.stream == null) {
            throw new IllegalStateException("Input not set!");
        }
        this.streamMetadata = new GIFStreamMetadata();
        try {
            this.stream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
            byte[] bArr = new byte[6];
            this.stream.readFully(bArr);
            StringBuffer stringBuffer = new StringBuffer(3);
            stringBuffer.append((char) bArr[3]);
            stringBuffer.append((char) bArr[4]);
            stringBuffer.append((char) bArr[5]);
            this.streamMetadata.version = stringBuffer.toString();
            this.streamMetadata.logicalScreenWidth = this.stream.readUnsignedShort();
            this.streamMetadata.logicalScreenHeight = this.stream.readUnsignedShort();
            int unsignedByte = this.stream.readUnsignedByte();
            boolean z2 = (unsignedByte & 128) != 0;
            this.streamMetadata.colorResolution = ((unsignedByte >> 4) & 7) + 1;
            this.streamMetadata.sortFlag = (unsignedByte & 8) != 0;
            int i2 = 1 << ((unsignedByte & 7) + 1);
            this.streamMetadata.backgroundColorIndex = this.stream.readUnsignedByte();
            this.streamMetadata.pixelAspectRatio = this.stream.readUnsignedByte();
            if (z2) {
                this.streamMetadata.globalColorTable = new byte[3 * i2];
                this.stream.readFully(this.streamMetadata.globalColorTable);
            } else {
                this.streamMetadata.globalColorTable = null;
            }
            this.imageStartPosition.add(Long.valueOf(this.stream.getStreamPosition()));
            this.gotHeader = true;
        } catch (IOException e2) {
            throw new IIOException("I/O error reading header!", e2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x006a, code lost:
    
        return true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:4:0x0010, code lost:
    
        r5.stream.skipBytes(8);
        r0 = r5.stream.readUnsignedByte();
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x002b, code lost:
    
        if ((r0 & 128) == 0) goto L7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x002e, code lost:
    
        r5.stream.skipBytes(3 * (1 << ((r0 & 7) + 1)));
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0044, code lost:
    
        r5.stream.skipBytes(1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0051, code lost:
    
        r0 = r5.stream.readUnsignedByte();
        r5.stream.skipBytes(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0067, code lost:
    
        if (r0 > 0) goto L47;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean skipImage() throws javax.imageio.IIOException {
        /*
            Method dump skipped, instructions count: 213
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.imageio.plugins.gif.GIFImageReader.skipImage():boolean");
    }

    private int locateImage(int i2) throws IIOException {
        readHeader();
        try {
            int iMin = Math.min(i2, this.imageStartPosition.size() - 1);
            this.stream.seek(((Long) this.imageStartPosition.get(iMin)).longValue());
            while (iMin < i2) {
                if (!skipImage()) {
                    return iMin - 1;
                }
                this.imageStartPosition.add(new Long(this.stream.getStreamPosition()));
                iMin++;
            }
            if (this.currIndex != i2) {
                this.imageMetadata = null;
            }
            this.currIndex = i2;
            return i2;
        } catch (IOException e2) {
            throw new IIOException("Couldn't seek!", e2);
        }
    }

    private byte[] concatenateBlocks() throws IOException {
        byte[] bArr = new byte[0];
        while (true) {
            byte[] bArr2 = bArr;
            int unsignedByte = this.stream.readUnsignedByte();
            if (unsignedByte != 0) {
                byte[] bArr3 = new byte[bArr2.length + unsignedByte];
                System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
                this.stream.readFully(bArr3, bArr2.length, unsignedByte);
                bArr = bArr3;
            } else {
                return bArr2;
            }
        }
    }

    private void readMetadata() throws IIOException {
        int unsignedByte;
        if (this.stream == null) {
            throw new IllegalStateException("Input not set!");
        }
        try {
            this.imageMetadata = new GIFImageMetadata();
            long streamPosition = this.stream.getStreamPosition();
            while (true) {
                int unsignedByte2 = this.stream.readUnsignedByte();
                if (unsignedByte2 == 44) {
                    this.imageMetadata.imageLeftPosition = this.stream.readUnsignedShort();
                    this.imageMetadata.imageTopPosition = this.stream.readUnsignedShort();
                    this.imageMetadata.imageWidth = this.stream.readUnsignedShort();
                    this.imageMetadata.imageHeight = this.stream.readUnsignedShort();
                    int unsignedByte3 = this.stream.readUnsignedByte();
                    boolean z2 = (unsignedByte3 & 128) != 0;
                    this.imageMetadata.interlaceFlag = (unsignedByte3 & 64) != 0;
                    this.imageMetadata.sortFlag = (unsignedByte3 & 32) != 0;
                    int i2 = 1 << ((unsignedByte3 & 7) + 1);
                    if (z2) {
                        this.imageMetadata.localColorTable = new byte[3 * i2];
                        this.stream.readFully(this.imageMetadata.localColorTable);
                    } else {
                        this.imageMetadata.localColorTable = null;
                    }
                    this.imageMetadataLength = (int) (this.stream.getStreamPosition() - streamPosition);
                    return;
                }
                if (unsignedByte2 != 33) {
                    if (unsignedByte2 == 59) {
                        throw new IndexOutOfBoundsException("Attempt to read past end of image sequence!");
                    }
                    throw new IIOException("Unexpected block type " + unsignedByte2 + "!");
                }
                int unsignedByte4 = this.stream.readUnsignedByte();
                if (unsignedByte4 == 249) {
                    this.stream.readUnsignedByte();
                    int unsignedByte5 = this.stream.readUnsignedByte();
                    this.imageMetadata.disposalMethod = (unsignedByte5 >> 2) & 3;
                    this.imageMetadata.userInputFlag = (unsignedByte5 & 2) != 0;
                    this.imageMetadata.transparentColorFlag = (unsignedByte5 & 1) != 0;
                    this.imageMetadata.delayTime = this.stream.readUnsignedShort();
                    this.imageMetadata.transparentColorIndex = this.stream.readUnsignedByte();
                    this.stream.readUnsignedByte();
                } else if (unsignedByte4 == 1) {
                    this.stream.readUnsignedByte();
                    this.imageMetadata.hasPlainTextExtension = true;
                    this.imageMetadata.textGridLeft = this.stream.readUnsignedShort();
                    this.imageMetadata.textGridTop = this.stream.readUnsignedShort();
                    this.imageMetadata.textGridWidth = this.stream.readUnsignedShort();
                    this.imageMetadata.textGridHeight = this.stream.readUnsignedShort();
                    this.imageMetadata.characterCellWidth = this.stream.readUnsignedByte();
                    this.imageMetadata.characterCellHeight = this.stream.readUnsignedByte();
                    this.imageMetadata.textForegroundColor = this.stream.readUnsignedByte();
                    this.imageMetadata.textBackgroundColor = this.stream.readUnsignedByte();
                    this.imageMetadata.text = concatenateBlocks();
                } else if (unsignedByte4 == 254) {
                    byte[] bArrConcatenateBlocks = concatenateBlocks();
                    if (this.imageMetadata.comments == null) {
                        this.imageMetadata.comments = new ArrayList();
                    }
                    this.imageMetadata.comments.add(bArrConcatenateBlocks);
                } else if (unsignedByte4 == 255) {
                    int unsignedByte6 = this.stream.readUnsignedByte();
                    byte[] bArr = new byte[8];
                    byte[] bArr2 = new byte[3];
                    byte[] bArr3 = new byte[unsignedByte6];
                    this.stream.readFully(bArr3);
                    int iCopyData = copyData(bArr3, copyData(bArr3, 0, bArr), bArr2);
                    byte[] bArrConcatenateBlocks2 = concatenateBlocks();
                    if (iCopyData < unsignedByte6) {
                        int i3 = unsignedByte6 - iCopyData;
                        byte[] bArr4 = new byte[i3 + bArrConcatenateBlocks2.length];
                        System.arraycopy(bArr3, iCopyData, bArr4, 0, i3);
                        System.arraycopy(bArrConcatenateBlocks2, 0, bArr4, i3, bArrConcatenateBlocks2.length);
                        bArrConcatenateBlocks2 = bArr4;
                    }
                    if (this.imageMetadata.applicationIDs == null) {
                        this.imageMetadata.applicationIDs = new ArrayList();
                        this.imageMetadata.authenticationCodes = new ArrayList();
                        this.imageMetadata.applicationData = new ArrayList();
                    }
                    this.imageMetadata.applicationIDs.add(bArr);
                    this.imageMetadata.authenticationCodes.add(bArr2);
                    this.imageMetadata.applicationData.add(bArrConcatenateBlocks2);
                } else {
                    do {
                        unsignedByte = this.stream.readUnsignedByte();
                        this.stream.skipBytes(unsignedByte);
                    } while (unsignedByte > 0);
                }
            }
        } catch (IIOException e2) {
            throw e2;
        } catch (IOException e3) {
            throw new IIOException("I/O error reading image metadata!", e3);
        }
    }

    private int copyData(byte[] bArr, int i2, byte[] bArr2) {
        int length = bArr2.length;
        int length2 = bArr.length - i2;
        if (length > length2) {
            length = length2;
        }
        System.arraycopy(bArr, i2, bArr2, 0, length);
        return i2 + length;
    }

    private void startPass(int i2) {
        if (this.updateListeners == null || !this.imageMetadata.interlaceFlag) {
            return;
        }
        int i3 = interlaceOffset[this.interlacePass];
        int i4 = interlaceIncrement[this.interlacePass];
        int[] iArrComputeUpdatedPixels = ReaderUtil.computeUpdatedPixels(this.sourceRegion, this.destinationOffset, this.destinationRegion.f12372x, this.destinationRegion.f12373y, (this.destinationRegion.f12372x + this.destinationRegion.width) - 1, (this.destinationRegion.f12373y + this.destinationRegion.height) - 1, this.sourceXSubsampling, this.sourceYSubsampling, 0, i3, this.destinationRegion.width, ((this.destinationRegion.height + i4) - 1) / i4, 1, i4);
        this.updateMinY = iArrComputeUpdatedPixels[1];
        this.updateYStep = iArrComputeUpdatedPixels[5];
        processPassStarted(this.theImage, this.interlacePass, this.sourceMinProgressivePass, this.sourceMaxProgressivePass, 0, this.updateMinY, 1, this.updateYStep, new int[]{0});
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage read(int i2, ImageReadParam imageReadParam) throws IIOException {
        int i3;
        if (this.stream == null) {
            throw new IllegalStateException("Input not set!");
        }
        checkIndex(i2);
        if (locateImage(i2) != i2) {
            throw new IndexOutOfBoundsException("imageIndex out of bounds!");
        }
        clearAbortRequest();
        readMetadata();
        if (imageReadParam == null) {
            imageReadParam = getDefaultReadParam();
        }
        this.theImage = getDestination(imageReadParam, getImageTypes(i2), this.imageMetadata.imageWidth, this.imageMetadata.imageHeight);
        this.theTile = this.theImage.getWritableTile(0, 0);
        this.width = this.imageMetadata.imageWidth;
        this.height = this.imageMetadata.imageHeight;
        this.streamX = 0;
        this.streamY = 0;
        this.rowsDone = 0;
        this.interlacePass = 0;
        this.sourceRegion = new Rectangle(0, 0, 0, 0);
        this.destinationRegion = new Rectangle(0, 0, 0, 0);
        computeRegions(imageReadParam, this.width, this.height, this.theImage, this.sourceRegion, this.destinationRegion);
        this.destinationOffset = new Point(this.destinationRegion.f12372x, this.destinationRegion.f12373y);
        this.sourceXSubsampling = imageReadParam.getSourceXSubsampling();
        this.sourceYSubsampling = imageReadParam.getSourceYSubsampling();
        this.sourceMinProgressivePass = Math.max(imageReadParam.getSourceMinProgressivePass(), 0);
        this.sourceMaxProgressivePass = Math.min(imageReadParam.getSourceMaxProgressivePass(), 3);
        this.destY = this.destinationRegion.f12373y + ((this.streamY - this.sourceRegion.f12373y) / this.sourceYSubsampling);
        computeDecodeThisRow();
        processImageStarted(i2);
        startPass(0);
        this.rowBuf = new byte[this.width];
        try {
            this.initCodeSize = this.stream.readUnsignedByte();
            if (this.initCodeSize < 1 || this.initCodeSize > 8) {
                throw new IIOException("Bad code size:" + this.initCodeSize);
            }
            this.blockLength = this.stream.readUnsignedByte();
            int i4 = this.blockLength;
            int i5 = 0;
            while (i4 > 0) {
                int i6 = this.stream.read(this.block, i5, i4);
                i4 -= i6;
                i5 += i6;
            }
            this.bitPos = 0;
            this.nextByte = 0;
            this.lastBlockFound = false;
            this.interlacePass = 0;
            initNext32Bits();
            this.clearCode = 1 << this.initCodeSize;
            this.eofCode = this.clearCode + 1;
            int i7 = -1;
            int[] iArr = new int[4096];
            byte[] bArr = new byte[4096];
            byte[] bArr2 = new byte[4096];
            int[] iArr2 = new int[4096];
            byte[] bArr3 = new byte[4096];
            initializeStringTable(iArr, bArr, bArr2, iArr2);
            int i8 = (1 << this.initCodeSize) + 2;
            int i9 = this.initCodeSize + 1;
            int i10 = (1 << i9) - 1;
            while (!abortRequested()) {
                int code = getCode(i9, i10);
                if (code == this.clearCode) {
                    initializeStringTable(iArr, bArr, bArr2, iArr2);
                    i8 = (1 << this.initCodeSize) + 2;
                    i9 = this.initCodeSize + 1;
                    i10 = (1 << i9) - 1;
                    code = getCode(i9, i10);
                    if (code == this.eofCode) {
                        processImageComplete();
                        return this.theImage;
                    }
                } else {
                    if (code == this.eofCode) {
                        processImageComplete();
                        return this.theImage;
                    }
                    if (code < i8) {
                        i3 = code;
                    } else {
                        i3 = i7;
                        if (code != i8) {
                            processWarningOccurred("Out-of-sequence code!");
                        }
                    }
                    if (-1 != i7 && i8 < 4096) {
                        int i11 = i8;
                        int i12 = i7;
                        iArr[i11] = i12;
                        bArr[i11] = bArr2[i3];
                        bArr2[i11] = bArr2[i12];
                        iArr2[i11] = iArr2[i12] + 1;
                        i8++;
                        if (i8 == (1 << i9) && i8 < 4096) {
                            i9++;
                            i10 = (1 << i9) - 1;
                        }
                    }
                }
                int i13 = code;
                int i14 = iArr2[i13];
                for (int i15 = i14 - 1; i15 >= 0; i15--) {
                    bArr3[i15] = bArr[i13];
                    i13 = iArr[i13];
                }
                outputPixels(bArr3, i14);
                i7 = code;
            }
            processReadAborted();
            return this.theImage;
        } catch (IOException e2) {
            throw new IIOException("I/O error reading image!", e2);
        }
    }

    @Override // javax.imageio.ImageReader
    public void reset() {
        super.reset();
        resetStreamSettings();
    }

    private void resetStreamSettings() {
        this.gotHeader = false;
        this.streamMetadata = null;
        this.currIndex = -1;
        this.imageMetadata = null;
        this.imageStartPosition = new ArrayList();
        this.numImages = -1;
        this.blockLength = 0;
        this.bitPos = 0;
        this.nextByte = 0;
        this.next32Bits = 0;
        this.lastBlockFound = false;
        this.theImage = null;
        this.theTile = null;
        this.width = -1;
        this.height = -1;
        this.streamX = -1;
        this.streamY = -1;
        this.rowsDone = 0;
        this.interlacePass = 0;
        this.fallbackColorTable = null;
    }

    private static synchronized byte[] getDefaultPalette() {
        if (defaultPalette == null) {
            IndexColorModel indexColorModel = (IndexColorModel) new BufferedImage(1, 1, 13).getColorModel();
            int mapSize = indexColorModel.getMapSize();
            byte[] bArr = new byte[mapSize];
            byte[] bArr2 = new byte[mapSize];
            byte[] bArr3 = new byte[mapSize];
            indexColorModel.getReds(bArr);
            indexColorModel.getGreens(bArr2);
            indexColorModel.getBlues(bArr3);
            defaultPalette = new byte[mapSize * 3];
            for (int i2 = 0; i2 < mapSize; i2++) {
                defaultPalette[(3 * i2) + 0] = bArr[i2];
                defaultPalette[(3 * i2) + 1] = bArr2[i2];
                defaultPalette[(3 * i2) + 2] = bArr3[i2];
            }
        }
        return defaultPalette;
    }
}
