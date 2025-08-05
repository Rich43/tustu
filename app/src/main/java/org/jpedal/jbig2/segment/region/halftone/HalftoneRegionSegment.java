package org.jpedal.jbig2.segment.region.halftone;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.pattern.PatternDictionarySegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/halftone/HalftoneRegionSegment.class */
public class HalftoneRegionSegment extends RegionSegment {
    private HalftoneRegionFlags halftoneRegionFlags;
    private boolean inlineImage;

    public HalftoneRegionSegment(JBIG2StreamDecoder streamDecoder, boolean inlineImage) {
        super(streamDecoder);
        this.halftoneRegionFlags = new HalftoneRegionFlags();
        this.inlineImage = inlineImage;
    }

    @Override // org.jpedal.jbig2.segment.region.RegionSegment, org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        super.readSegment();
        readHalftoneRegionFlags();
        short[] buf = new short[4];
        this.decoder.readByte(buf);
        int gridWidth = BinaryOperation.getInt32(buf);
        short[] buf2 = new short[4];
        this.decoder.readByte(buf2);
        int gridHeight = BinaryOperation.getInt32(buf2);
        short[] buf3 = new short[4];
        this.decoder.readByte(buf3);
        int gridX = BinaryOperation.getInt32(buf3);
        short[] buf4 = new short[4];
        this.decoder.readByte(buf4);
        int gridY = BinaryOperation.getInt32(buf4);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("grid pos and size = " + gridX + ',' + gridY + ' ' + gridWidth + ',' + gridHeight);
        }
        short[] buf5 = new short[2];
        this.decoder.readByte(buf5);
        int stepX = BinaryOperation.getInt16(buf5);
        short[] buf6 = new short[2];
        this.decoder.readByte(buf6);
        int stepY = BinaryOperation.getInt16(buf6);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("step size = " + stepX + ',' + stepY);
        }
        int[] referedToSegments = this.segmentHeader.getReferredToSegments();
        if (referedToSegments.length != 1) {
            System.out.println("Error in halftone Segment. refSegs should == 1");
        }
        Segment segment = this.decoder.findSegment(referedToSegments[0]);
        if (segment.getSegmentHeader().getSegmentType() != 16 && JBIG2StreamDecoder.debug) {
            System.out.println("Error in halftone Segment. bad symbol dictionary reference");
        }
        PatternDictionarySegment patternDictionarySegment = (PatternDictionarySegment) segment;
        int bitsPerValue = 0;
        int i2 = 1;
        while (true) {
            int i3 = i2;
            if (i3 >= patternDictionarySegment.getSize()) {
                break;
            }
            bitsPerValue++;
            i2 = i3 << 1;
        }
        JBIG2Bitmap bitmap = patternDictionarySegment.getBitmaps()[0];
        int patternWidth = bitmap.getWidth();
        int patternHeight = bitmap.getHeight();
        if (JBIG2StreamDecoder.debug) {
            System.out.println("pattern size = " + patternWidth + ',' + patternHeight);
        }
        boolean useMMR = this.halftoneRegionFlags.getFlagValue(HalftoneRegionFlags.H_MMR) != 0;
        int template = this.halftoneRegionFlags.getFlagValue(HalftoneRegionFlags.H_TEMPLATE);
        if (!useMMR) {
            this.arithmeticDecoder.resetGenericStats(template, null);
            this.arithmeticDecoder.start();
        }
        int halftoneDefaultPixel = this.halftoneRegionFlags.getFlagValue(HalftoneRegionFlags.H_DEF_PIXEL);
        JBIG2Bitmap bitmap2 = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        bitmap2.clear(halftoneDefaultPixel);
        boolean enableSkip = this.halftoneRegionFlags.getFlagValue(HalftoneRegionFlags.H_ENABLE_SKIP) != 0;
        JBIG2Bitmap skipBitmap = null;
        if (enableSkip) {
            skipBitmap = new JBIG2Bitmap(gridWidth, gridHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
            skipBitmap.clear(0);
            for (int y2 = 0; y2 < gridHeight; y2++) {
                for (int x2 = 0; x2 < gridWidth; x2++) {
                    int xx = gridX + (y2 * stepY) + (x2 * stepX);
                    int yy = (gridY + (y2 * stepX)) - (x2 * stepY);
                    if (((xx + patternWidth) >> 8) <= 0 || (xx >> 8) >= this.regionBitmapWidth || ((yy + patternHeight) >> 8) <= 0 || (yy >> 8) >= this.regionBitmapHeight) {
                        skipBitmap.setPixel(y2, x2, 1);
                    }
                }
            }
        }
        int[] grayScaleImage = new int[gridWidth * gridHeight];
        short[] genericBAdaptiveTemplateX = new short[4];
        short[] genericBAdaptiveTemplateY = new short[4];
        genericBAdaptiveTemplateX[0] = (short) (template <= 1 ? 3 : 2);
        genericBAdaptiveTemplateY[0] = -1;
        genericBAdaptiveTemplateX[1] = -3;
        genericBAdaptiveTemplateY[1] = -1;
        genericBAdaptiveTemplateX[2] = 2;
        genericBAdaptiveTemplateY[2] = -2;
        genericBAdaptiveTemplateX[3] = -2;
        genericBAdaptiveTemplateY[3] = -2;
        for (int j2 = bitsPerValue - 1; j2 >= 0; j2--) {
            JBIG2Bitmap grayBitmap = new JBIG2Bitmap(gridWidth, gridHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
            grayBitmap.readBitmap(useMMR, template, false, enableSkip, skipBitmap, genericBAdaptiveTemplateX, genericBAdaptiveTemplateY, -1);
            int i4 = 0;
            for (int row = 0; row < gridHeight; row++) {
                for (int col = 0; col < gridWidth; col++) {
                    int bit = grayBitmap.getPixel(col, row) ^ (grayScaleImage[i4] & 1);
                    grayScaleImage[i4] = (grayScaleImage[i4] << 1) | bit;
                    i4++;
                }
            }
        }
        int combinationOperator = this.halftoneRegionFlags.getFlagValue(HalftoneRegionFlags.H_COMB_OP);
        int i5 = 0;
        for (int col2 = 0; col2 < gridHeight; col2++) {
            int xx2 = gridX + (col2 * stepY);
            int yy2 = gridY + (col2 * stepX);
            for (int row2 = 0; row2 < gridWidth; row2++) {
                if (!enableSkip || skipBitmap.getPixel(col2, row2) != 1) {
                    JBIG2Bitmap patternBitmap = patternDictionarySegment.getBitmaps()[grayScaleImage[i5]];
                    bitmap2.combine(patternBitmap, xx2 >> 8, yy2 >> 8, combinationOperator);
                }
                xx2 += stepX;
                yy2 -= stepY;
                i5++;
            }
        }
        if (this.inlineImage) {
            PageInformationSegment pageSegment = this.decoder.findPageSegement(this.segmentHeader.getPageAssociation());
            JBIG2Bitmap pageBitmap = pageSegment.getPageBitmap();
            int externalCombinationOperator = this.regionFlags.getFlagValue(RegionFlags.EXTERNAL_COMBINATION_OPERATOR);
            pageBitmap.combine(bitmap2, this.regionBitmapXLocation, this.regionBitmapYLocation, externalCombinationOperator);
            return;
        }
        bitmap2.setBitmapNumber(getSegmentHeader().getSegmentNumber());
        this.decoder.appendBitmap(bitmap2);
    }

    private void readHalftoneRegionFlags() throws IOException {
        short halftoneRegionFlagsField = this.decoder.readByte();
        this.halftoneRegionFlags.setFlags(halftoneRegionFlagsField);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("generic region Segment flags = " + ((int) halftoneRegionFlagsField));
        }
    }

    public HalftoneRegionFlags getHalftoneRegionFlags() {
        return this.halftoneRegionFlags;
    }
}
