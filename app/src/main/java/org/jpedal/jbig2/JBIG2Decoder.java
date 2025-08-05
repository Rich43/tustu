package org.jpedal.jbig2;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.DataInput;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/JBIG2Decoder.class */
public class JBIG2Decoder {
    private JBIG2StreamDecoder streamDecoder = new JBIG2StreamDecoder();

    public void setGlobalData(byte[] data) throws JBIG2Exception, IOException {
        this.streamDecoder.setGlobalData(data);
    }

    public void decodeJBIG2(File file) throws JBIG2Exception, IOException {
        decodeJBIG2(file.getAbsolutePath());
    }

    public void decodeJBIG2(String file) throws JBIG2Exception, IOException {
        decodeJBIG2(new FileInputStream(file));
    }

    public void decodeJBIG2(InputStream inputStream) throws JBIG2Exception, IOException {
        int availiable = inputStream.available();
        byte[] bytes = new byte[availiable];
        inputStream.read(bytes);
        decodeJBIG2(bytes);
    }

    public void decodeJBIG2(DataInput dataInput) throws JBIG2Exception, IOException {
    }

    public void decodeJBIG2(byte[] data) throws JBIG2Exception, IOException {
        this.streamDecoder.decodeJBIG2(data);
    }

    public void cleanupPostDecode() {
        this.streamDecoder.cleanupPostDecode();
    }

    public BufferedImage getPageAsBufferedImage(int page) {
        JBIG2Bitmap pageBitmap = this.streamDecoder.findPageSegement(page + 1).getPageBitmap();
        int width = pageBitmap.getWidth();
        int height = pageBitmap.getHeight();
        BufferedImage image = new BufferedImage(width, height, 12);
        byte[] bytes = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        pageBitmap.getData(bytes, true);
        return image;
    }

    public boolean isNumberOfPagesKnown() {
        return this.streamDecoder.isNumberOfPagesKnown();
    }

    public int getNumberOfPages() {
        int pages = this.streamDecoder.getNumberOfPages();
        if (this.streamDecoder.isNumberOfPagesKnown() && pages != 0) {
            return pages;
        }
        int noOfPages = 0;
        List<Segment> segments = getAllSegments();
        for (Segment segment : segments) {
            if (segment.getSegmentHeader().getSegmentType() == 48) {
                noOfPages++;
            }
        }
        return noOfPages;
    }

    public List getAllSegments() {
        return this.streamDecoder.getAllSegments();
    }

    public PageInformationSegment findPageSegement(int page) {
        return this.streamDecoder.findPageSegement(page + 1);
    }

    public Segment findSegment(int segmentNumber) {
        return this.streamDecoder.findSegment(segmentNumber);
    }

    public JBIG2Bitmap getPageAsJBIG2Bitmap(int page) {
        return this.streamDecoder.findPageSegement(page + 1).getPageBitmap();
    }

    public boolean isRandomAccessOrganisationUsed() {
        return this.streamDecoder.isRandomAccessOrganisationUsed();
    }
}
