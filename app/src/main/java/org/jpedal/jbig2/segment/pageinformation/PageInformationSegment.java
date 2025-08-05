package org.jpedal.jbig2.segment.pageinformation;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/pageinformation/PageInformationSegment.class */
public class PageInformationSegment extends Segment {
    private int pageBitmapHeight;
    private int pageBitmapWidth;
    private int yResolution;
    private int xResolution;
    PageInformationFlags pageInformationFlags;
    private int pageStriping;
    private JBIG2Bitmap pageBitmap;

    public PageInformationSegment(JBIG2StreamDecoder streamDecoder) {
        super(streamDecoder);
        this.pageInformationFlags = new PageInformationFlags();
    }

    public PageInformationFlags getPageInformationFlags() {
        return this.pageInformationFlags;
    }

    public JBIG2Bitmap getPageBitmap() {
        return this.pageBitmap;
    }

    @Override // org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        int height;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("==== Reading Page Information Dictionary ====");
        }
        short[] buff = new short[4];
        this.decoder.readByte(buff);
        this.pageBitmapWidth = BinaryOperation.getInt32(buff);
        short[] buff2 = new short[4];
        this.decoder.readByte(buff2);
        this.pageBitmapHeight = BinaryOperation.getInt32(buff2);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("Bitmap size = " + this.pageBitmapWidth + 'x' + this.pageBitmapHeight);
        }
        short[] buff3 = new short[4];
        this.decoder.readByte(buff3);
        this.xResolution = BinaryOperation.getInt32(buff3);
        short[] buff4 = new short[4];
        this.decoder.readByte(buff4);
        this.yResolution = BinaryOperation.getInt32(buff4);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("Resolution = " + this.xResolution + 'x' + this.yResolution);
        }
        short pageInformationFlagsField = this.decoder.readByte();
        this.pageInformationFlags.setFlags(pageInformationFlagsField);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("symbolDictionaryFlags = " + ((int) pageInformationFlagsField));
        }
        short[] buff5 = new short[2];
        this.decoder.readByte(buff5);
        this.pageStriping = BinaryOperation.getInt16(buff5);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("Page Striping = " + this.pageStriping);
        }
        int defPix = this.pageInformationFlags.getFlagValue(PageInformationFlags.DEFAULT_PIXEL_VALUE);
        if (this.pageBitmapHeight == -1) {
            height = this.pageStriping & Short.MAX_VALUE;
        } else {
            height = this.pageBitmapHeight;
        }
        this.pageBitmap = new JBIG2Bitmap(this.pageBitmapWidth, height, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        this.pageBitmap.clear(defPix);
    }

    public int getPageBitmapHeight() {
        return this.pageBitmapHeight;
    }
}
