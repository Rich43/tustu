package org.jpedal.jbig2.jai;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.jpedal.jbig2.JBIG2Decoder;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.image.JBIG2Bitmap;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/jai/JBIG2ImageReader.class */
public class JBIG2ImageReader extends ImageReader {
    private static final Logger logger = Logger.getLogger(JBIG2ImageReader.class.toString());
    private JBIG2Decoder decoder;
    private ImageInputStream stream;
    private boolean readFile;

    protected JBIG2ImageReader(ImageReaderSpi originatingProvider) {
        super(originatingProvider);
    }

    @Override // javax.imageio.ImageReader
    public void setInput(Object input, boolean seekForwardOnly, boolean ignoreMetadata) {
        super.setInput(input, seekForwardOnly, ignoreMetadata);
        if (input == null) {
            this.stream = null;
        } else {
            if (input instanceof ImageInputStream) {
                this.stream = (ImageInputStream) input;
                return;
            }
            throw new IllegalArgumentException("ImageInputStream expected!");
        }
    }

    @Override // javax.imageio.ImageReader
    public BufferedImage read(int imageIndex, ImageReadParam param) throws IOException {
        Raster raster;
        BufferedImage dst = null;
        try {
            int width = getWidth(imageIndex);
            int height = getHeight(imageIndex);
            Rectangle sourceRegion = getSourceRegion(param, width, height);
            int sourceXSubsampling = 1;
            int sourceYSubsampling = 1;
            Point destinationOffset = new Point(0, 0);
            if (param != null) {
                sourceXSubsampling = param.getSourceXSubsampling();
                sourceYSubsampling = param.getSourceYSubsampling();
                destinationOffset = param.getDestinationOffset();
            }
            dst = getDestination(param, getImageTypes(0), width, height);
            WritableRaster wrDst = dst.getRaster();
            JBIG2Bitmap bitmap = this.decoder.getPageAsJBIG2Bitmap(imageIndex).getSlice(sourceRegion.f12372x, sourceRegion.f12373y, sourceRegion.width, sourceRegion.height);
            BufferedImage image = bitmap.getBufferedImage();
            int newWidth = (int) (image.getWidth() * (1.0d / sourceXSubsampling));
            int newHeight = (int) (image.getHeight() * (1.0d / sourceYSubsampling));
            BufferedImage scaledImage = scaleImage(image.getRaster(), newWidth, newHeight, 1, 1);
            if (scaledImage != null) {
                raster = scaledImage.getRaster();
            } else {
                raster = image.getRaster();
            }
            wrDst.setRect(destinationOffset.f12370x, destinationOffset.f12371y, raster);
        } catch (RuntimeException e2) {
            logger.log(Level.FINE, "Error reading JBIG2 image data", (Throwable) e2);
        }
        return dst;
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
        return null;
    }

    @Override // javax.imageio.ImageReader
    public IIOMetadata getStreamMetadata() throws IOException {
        return null;
    }

    @Override // javax.imageio.ImageReader
    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex) throws IOException {
        readFile();
        checkIndex(imageIndex);
        List<ImageTypeSpecifier> l2 = new ArrayList<>();
        l2.add(ImageTypeSpecifier.createFromBufferedImageType(12));
        return l2.iterator();
    }

    @Override // javax.imageio.ImageReader
    public int getNumImages(boolean allowSearch) throws IOException {
        readFile();
        return this.decoder.getNumberOfPages();
    }

    @Override // javax.imageio.ImageReader
    public int getHeight(int imageIndex) throws IOException {
        readFile();
        checkIndex(imageIndex);
        return this.decoder.getPageAsJBIG2Bitmap(imageIndex).getHeight();
    }

    @Override // javax.imageio.ImageReader
    public int getWidth(int imageIndex) throws IOException {
        readFile();
        checkIndex(imageIndex);
        return this.decoder.getPageAsJBIG2Bitmap(imageIndex).getWidth();
    }

    private void checkIndex(int imageIndex) {
        int noOfPages = this.decoder.getNumberOfPages();
        if (imageIndex < 0 || imageIndex > noOfPages) {
            throw new IndexOutOfBoundsException("Bad index!");
        }
    }

    private static BufferedImage scaleImage(Raster ras, int pX, int pY, int comp, int d2) {
        int w2 = ras.getWidth();
        int h2 = ras.getHeight();
        byte[] data = ((DataBufferByte) ras.getDataBuffer()).getData();
        int newW = w2;
        int sampling = 1;
        int smallestH = pY << 2;
        int smallestW = pX << 2;
        for (int newH = h2; newW > smallestW && newH > smallestH; newH >>= 1) {
            sampling <<= 1;
            newW >>= 1;
        }
        int scaleX = w2 / pX;
        if (scaleX < 1) {
            scaleX = 1;
        }
        int scaleY = h2 / pY;
        if (scaleY < 1) {
            scaleY = 1;
        }
        int sampling2 = scaleX;
        if (sampling2 > scaleY) {
            sampling2 = scaleY;
        }
        if (sampling2 > 1) {
            int newW2 = w2 / sampling2;
            int newH2 = h2 / sampling2;
            if (d2 == 1) {
                int size = newW2 * newH2;
                byte[] newData = new byte[size];
                int[] flag = {1, 2, 4, 8, 16, 32, 64, 128};
                int origLineLength = (w2 + 7) >> 3;
                for (int y2 = 0; y2 < newH2; y2++) {
                    for (int x2 = 0; x2 < newW2; x2++) {
                        int bytes = 0;
                        int count = 0;
                        int wCount = sampling2;
                        int hCount = sampling2;
                        int wGapLeft = w2 - x2;
                        int hGapLeft = h2 - y2;
                        if (wCount > wGapLeft) {
                            wCount = wGapLeft;
                        }
                        if (hCount > hGapLeft) {
                            hCount = hGapLeft;
                        }
                        for (int yy = 0; yy < hCount; yy++) {
                            for (int xx = 0; xx < wCount; xx++) {
                                byte currentByte = data[((yy + (y2 * sampling2)) * origLineLength) + (((x2 * sampling2) + xx) >> 3)];
                                int bit = currentByte & flag[7 - (((x2 * sampling2) + xx) & 7)];
                                if (bit != 0) {
                                    bytes++;
                                }
                                count++;
                            }
                        }
                        int offset = x2 + (newW2 * y2);
                        if (count > 0) {
                            newData[offset] = (byte) ((255 * bytes) / count);
                        } else {
                            newData[offset] = -1;
                        }
                    }
                }
                data = newData;
                h2 = newH2;
                w2 = newW2;
            } else if (d2 == 8) {
                int x3 = 0;
                int y3 = 0;
                int xx2 = 0;
                int yy2 = 0;
                int jj = 0;
                int origLineLength2 = 0;
                try {
                    if (w2 * h2 == data.length) {
                        comp = 1;
                    }
                    byte[] newData2 = new byte[newW2 * newH2 * comp];
                    origLineLength2 = w2 * comp;
                    y3 = 0;
                    while (y3 < newH2) {
                        x3 = 0;
                        while (x3 < newW2) {
                            int wCount2 = sampling2;
                            int hCount2 = sampling2;
                            int wGapLeft2 = w2 - x3;
                            int hGapLeft2 = h2 - y3;
                            if (wCount2 > wGapLeft2) {
                                wCount2 = wGapLeft2;
                            }
                            if (hCount2 > hGapLeft2) {
                                hCount2 = hGapLeft2;
                            }
                            jj = 0;
                            while (jj < comp) {
                                int byteTotal = 0;
                                int count2 = 0;
                                yy2 = 0;
                                while (yy2 < hCount2) {
                                    xx2 = 0;
                                    while (xx2 < wCount2) {
                                        byteTotal += data[((yy2 + (y3 * sampling2)) * origLineLength2) + (x3 * sampling2 * comp) + (xx2 * comp) + jj] & 255;
                                        count2++;
                                        xx2++;
                                    }
                                    yy2++;
                                }
                                if (count2 > 0) {
                                    newData2[jj + (x3 * comp) + (newW2 * y3 * comp)] = (byte) (byteTotal / count2);
                                }
                                jj++;
                            }
                            x3++;
                        }
                        y3++;
                    }
                    data = newData2;
                    h2 = newH2;
                    w2 = newW2;
                } catch (Exception e2) {
                    if (logger.isLoggable(Level.FINE)) {
                        logger.fine("xx=" + xx2 + " yy=" + yy2 + " jj=" + jj + " ptr=" + ((yy2 + (y3 * sampling2)) * origLineLength2) + ((x3 * sampling2) + (xx2 * comp) + jj) + '/' + data.length);
                        logger.fine(((yy2 + (y3 * sampling2)) * origLineLength2) + " " + ((x3 * sampling2) + (xx2 * comp) + jj));
                        logger.fine("w=" + w2 + " h=" + h2 + " sampling=" + sampling2 + " x=" + x3 + " y=" + y3);
                        logger.log(Level.FINE, "Error scaling image", (Throwable) e2);
                    }
                }
            }
        }
        if (sampling2 > 1) {
            int[] bands = {0};
            Raster raster = Raster.createInterleavedRaster(new DataBufferByte(data, data.length), w2, h2, w2, 1, bands, (Point) null);
            BufferedImage image = new BufferedImage(w2, h2, 10);
            image.setData(raster);
            return image;
        }
        return null;
    }

    private void readFile() {
        byte[] data;
        if (this.readFile) {
            return;
        }
        if (this.stream == null) {
            throw new IllegalStateException("No input stream!");
        }
        this.decoder = new JBIG2Decoder();
        try {
            int size = (int) this.stream.length();
            if (size == -1) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                byte[] temp = new byte[8192];
                while (true) {
                    int len = this.stream.read(temp);
                    if (len <= 0) {
                        break;
                    } else {
                        bos.write(temp, 0, len);
                    }
                }
                bos.close();
                data = bos.toByteArray();
            } else {
                data = new byte[size];
                this.stream.readFully(data);
            }
            this.decoder.decodeJBIG2(data);
        } catch (IOException e2) {
            logger.log(Level.FINE, "Error reading JBIG2 image data", (Throwable) e2);
        } catch (JBIG2Exception e3) {
            logger.log(Level.FINE, "Error reading JBIG2 image data", (Throwable) e3);
        }
        this.readFile = true;
    }
}
