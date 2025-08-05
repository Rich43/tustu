package com.sun.imageio.plugins.wbmp;

import com.sun.imageio.plugins.common.I18N;
import com.sun.imageio.plugins.common.ReaderUtil;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.MultiPixelPackedSampleModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.imageio.IIOException;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/plugins/wbmp/WBMPImageReader.class */
public class WBMPImageReader extends ImageReader {
    private ImageInputStream iis;
    private boolean gotHeader;
    private int width;
    private int height;
    private int wbmpType;
    private WBMPMetadata metadata;

    public WBMPImageReader(ImageReaderSpi imageReaderSpi) {
        super(imageReaderSpi);
        this.iis = null;
        this.gotHeader = false;
    }

    @Override // javax.imageio.ImageReader
    public void setInput(Object obj, boolean z2, boolean z3) {
        super.setInput(obj, z2, z3);
        this.iis = (ImageInputStream) obj;
        this.gotHeader = false;
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
        readHeader();
        return this.width;
    }

    @Override // javax.imageio.ImageReader
    public int getHeight(int i2) throws IOException {
        checkIndex(i2);
        readHeader();
        return this.height;
    }

    @Override // javax.imageio.ImageReader
    public boolean isRandomAccessEasy(int i2) throws IOException {
        checkIndex(i2);
        return true;
    }

    private void checkIndex(int i2) {
        if (i2 != 0) {
            throw new IndexOutOfBoundsException(I18N.getString("WBMPImageReader0"));
        }
    }

    public void readHeader() throws IOException {
        if (this.gotHeader) {
            return;
        }
        if (this.iis == null) {
            throw new IllegalStateException("Input source not set!");
        }
        this.metadata = new WBMPMetadata();
        this.wbmpType = this.iis.readByte();
        if (this.iis.readByte() != 0 || !isValidWbmpType(this.wbmpType)) {
            throw new IIOException(I18N.getString("WBMPImageReader2"));
        }
        this.metadata.wbmpType = this.wbmpType;
        this.width = ReaderUtil.readMultiByteInteger(this.iis);
        this.metadata.width = this.width;
        this.height = ReaderUtil.readMultiByteInteger(this.iis);
        this.metadata.height = this.height;
        this.gotHeader = true;
    }

    @Override // javax.imageio.ImageReader
    public Iterator getImageTypes(int i2) throws IOException {
        checkIndex(i2);
        readHeader();
        BufferedImage bufferedImage = new BufferedImage(1, 1, 12);
        ArrayList arrayList = new ArrayList(1);
        arrayList.add(new ImageTypeSpecifier(bufferedImage));
        return arrayList.iterator();
    }

    @Override // javax.imageio.ImageReader
    public ImageReadParam getDefaultReadParam() {
        return new ImageReadParam();
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getImageMetadata(int i2) throws IOException {
        checkIndex(i2);
        if (this.metadata == null) {
            readHeader();
        }
        return this.metadata;
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage read(int i2, ImageReadParam imageReadParam) throws IOException {
        if (this.iis == null) {
            throw new IllegalStateException(I18N.getString("WBMPImageReader1"));
        }
        checkIndex(i2);
        clearAbortRequest();
        processImageStarted(i2);
        if (imageReadParam == null) {
            imageReadParam = getDefaultReadParam();
        }
        readHeader();
        Rectangle rectangle = new Rectangle(0, 0, 0, 0);
        Rectangle rectangle2 = new Rectangle(0, 0, 0, 0);
        computeRegions(imageReadParam, this.width, this.height, imageReadParam.getDestination(), rectangle, rectangle2);
        int sourceXSubsampling = imageReadParam.getSourceXSubsampling();
        int sourceYSubsampling = imageReadParam.getSourceYSubsampling();
        imageReadParam.getSubsamplingXOffset();
        imageReadParam.getSubsamplingYOffset();
        BufferedImage destination = imageReadParam.getDestination();
        if (destination == null) {
            destination = new BufferedImage(rectangle2.f12372x + rectangle2.width, rectangle2.f12373y + rectangle2.height, 12);
        }
        boolean z2 = rectangle2.equals(new Rectangle(0, 0, this.width, this.height)) && rectangle2.equals(new Rectangle(0, 0, destination.getWidth(), destination.getHeight()));
        WritableRaster writableTile = destination.getWritableTile(0, 0);
        MultiPixelPackedSampleModel multiPixelPackedSampleModel = (MultiPixelPackedSampleModel) destination.getSampleModel();
        if (z2) {
            if (abortRequested()) {
                processReadAborted();
                return destination;
            }
            this.iis.read(((DataBufferByte) writableTile.getDataBuffer()).getData(), 0, this.height * multiPixelPackedSampleModel.getScanlineStride());
            processImageUpdate(destination, 0, 0, this.width, this.height, 1, 1, new int[]{0});
            processImageProgress(100.0f);
        } else {
            int i3 = (this.width + 7) / 8;
            byte[] bArr = new byte[i3];
            byte[] data = ((DataBufferByte) writableTile.getDataBuffer()).getData();
            int scanlineStride = multiPixelPackedSampleModel.getScanlineStride();
            this.iis.skipBytes(i3 * rectangle.f12373y);
            int i4 = i3 * (sourceYSubsampling - 1);
            int[] iArr = new int[rectangle2.width];
            int[] iArr2 = new int[rectangle2.width];
            int[] iArr3 = new int[rectangle2.width];
            int[] iArr4 = new int[rectangle2.width];
            int i5 = rectangle2.f12372x;
            int i6 = rectangle.f12372x;
            int i7 = 0;
            while (i5 < rectangle2.f12372x + rectangle2.width) {
                iArr3[i7] = i6 >> 3;
                iArr[i7] = 7 - (i6 & 7);
                iArr4[i7] = i5 >> 3;
                iArr2[i7] = 7 - (i5 & 7);
                i5++;
                i7++;
                i6 += sourceXSubsampling;
            }
            int i8 = 0;
            int i9 = rectangle.f12373y;
            int i10 = rectangle2.f12373y * scanlineStride;
            while (i8 < rectangle2.height && !abortRequested()) {
                this.iis.read(bArr, 0, i3);
                for (int i11 = 0; i11 < rectangle2.width; i11++) {
                    int i12 = (bArr[iArr3[i11]] >> iArr[i11]) & 1;
                    int i13 = i10 + iArr4[i11];
                    data[i13] = (byte) (data[i13] | (i12 << iArr2[i11]));
                }
                i10 += scanlineStride;
                this.iis.skipBytes(i4);
                processImageUpdate(destination, 0, i8, rectangle2.width, 1, 1, 1, new int[]{0});
                processImageProgress((100.0f * i8) / rectangle2.height);
                i8++;
                i9 += sourceYSubsampling;
            }
        }
        if (abortRequested()) {
            processReadAborted();
        } else {
            processImageComplete();
        }
        return destination;
    }

    @Override // javax.imageio.ImageReader
    public boolean canReadRaster() {
        return true;
    }

    @Override // javax.imageio.ImageReader
    public Raster readRaster(int i2, ImageReadParam imageReadParam) throws IOException {
        return read(i2, imageReadParam).getData();
    }

    @Override // javax.imageio.ImageReader
    public void reset() {
        super.reset();
        this.iis = null;
        this.gotHeader = false;
    }

    boolean isValidWbmpType(int i2) {
        return i2 == 0;
    }
}
