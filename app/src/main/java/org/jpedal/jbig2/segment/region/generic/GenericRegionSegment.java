package org.jpedal.jbig2.segment.region.generic;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.pageinformation.PageInformationFlags;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/generic/GenericRegionSegment.class */
public class GenericRegionSegment extends RegionSegment {
    private GenericRegionFlags genericRegionFlags;
    private boolean inlineImage;
    private boolean unknownLength;

    public GenericRegionSegment(JBIG2StreamDecoder streamDecoder, boolean inlineImage) {
        super(streamDecoder);
        this.genericRegionFlags = new GenericRegionFlags();
        this.unknownLength = false;
        this.inlineImage = inlineImage;
    }

    @Override // org.jpedal.jbig2.segment.region.RegionSegment, org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        short match1;
        short match2;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("==== Reading Immediate Generic Region ====");
        }
        super.readSegment();
        readGenericRegionFlags();
        boolean useMMR = this.genericRegionFlags.getFlagValue(GenericRegionFlags.MMR) != 0;
        int template = this.genericRegionFlags.getFlagValue(GenericRegionFlags.GB_TEMPLATE);
        short[] genericBAdaptiveTemplateX = new short[4];
        short[] genericBAdaptiveTemplateY = new short[4];
        if (!useMMR) {
            if (template == 0) {
                genericBAdaptiveTemplateX[0] = readATValue();
                genericBAdaptiveTemplateY[0] = readATValue();
                genericBAdaptiveTemplateX[1] = readATValue();
                genericBAdaptiveTemplateY[1] = readATValue();
                genericBAdaptiveTemplateX[2] = readATValue();
                genericBAdaptiveTemplateY[2] = readATValue();
                genericBAdaptiveTemplateX[3] = readATValue();
                genericBAdaptiveTemplateY[3] = readATValue();
            } else {
                genericBAdaptiveTemplateX[0] = readATValue();
                genericBAdaptiveTemplateY[0] = readATValue();
            }
            this.arithmeticDecoder.resetGenericStats(template, null);
            this.arithmeticDecoder.start();
        }
        boolean typicalPredictionGenericDecodingOn = this.genericRegionFlags.getFlagValue(GenericRegionFlags.TPGDON) != 0;
        int length = this.segmentHeader.getSegmentDataLength();
        if (length == -1) {
            this.unknownLength = true;
            if (useMMR) {
                match1 = 0;
                match2 = 0;
            } else {
                match1 = 255;
                match2 = 172;
            }
            int bytesRead = 0;
            while (true) {
                short bite1 = this.decoder.readByte();
                bytesRead++;
                if (bite1 == match1) {
                    short bite2 = this.decoder.readByte();
                    bytesRead++;
                    if (bite2 == match2) {
                        break;
                    }
                }
            }
            length = bytesRead - 2;
            this.decoder.movePointer(-bytesRead);
        }
        JBIG2Bitmap bitmap = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        bitmap.clear(0);
        bitmap.readBitmap(useMMR, template, typicalPredictionGenericDecodingOn, false, null, genericBAdaptiveTemplateX, genericBAdaptiveTemplateY, useMMR ? 0 : length - 18);
        if (this.inlineImage) {
            PageInformationSegment pageSegment = this.decoder.findPageSegement(this.segmentHeader.getPageAssociation());
            JBIG2Bitmap pageBitmap = pageSegment.getPageBitmap();
            int extCombOp = this.regionFlags.getFlagValue(RegionFlags.EXTERNAL_COMBINATION_OPERATOR);
            if (pageSegment.getPageBitmapHeight() == -1 && this.regionBitmapYLocation + this.regionBitmapHeight > pageBitmap.getHeight()) {
                pageBitmap.expand(this.regionBitmapYLocation + this.regionBitmapHeight, pageSegment.getPageInformationFlags().getFlagValue(PageInformationFlags.DEFAULT_PIXEL_VALUE));
            }
            pageBitmap.combine(bitmap, this.regionBitmapXLocation, this.regionBitmapYLocation, extCombOp);
        } else {
            bitmap.setBitmapNumber(getSegmentHeader().getSegmentNumber());
            this.decoder.appendBitmap(bitmap);
        }
        if (this.unknownLength) {
            this.decoder.movePointer(4);
        }
    }

    private void readGenericRegionFlags() throws IOException {
        short genericRegionFlagsField = this.decoder.readByte();
        this.genericRegionFlags.setFlags(genericRegionFlagsField);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("generic region Segment flags = " + ((int) genericRegionFlagsField));
        }
    }

    public GenericRegionFlags getGenericRegionFlags() {
        return this.genericRegionFlags;
    }
}
