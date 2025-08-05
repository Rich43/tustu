package org.jpedal.jbig2.segment.region.refinement;

import java.io.IOException;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.pageinformation.PageInformationFlags;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/refinement/RefinementRegionSegment.class */
public class RefinementRegionSegment extends RegionSegment {
    private RefinementRegionFlags refinementRegionFlags;
    private boolean inlineImage;
    private int noOfReferedToSegments;
    int[] referedToSegments;

    public RefinementRegionSegment(JBIG2StreamDecoder streamDecoder, boolean inlineImage, int[] referedToSegments, int noOfReferedToSegments) {
        super(streamDecoder);
        this.refinementRegionFlags = new RefinementRegionFlags();
        this.inlineImage = inlineImage;
        this.referedToSegments = referedToSegments;
        this.noOfReferedToSegments = noOfReferedToSegments;
    }

    @Override // org.jpedal.jbig2.segment.region.RegionSegment, org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        JBIG2Bitmap referedToBitmap;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("==== Reading Generic Refinement Region ====");
        }
        super.readSegment();
        readGenericRegionFlags();
        short[] genericRegionAdaptiveTemplateX = new short[2];
        short[] genericRegionAdaptiveTemplateY = new short[2];
        int template = this.refinementRegionFlags.getFlagValue(RefinementRegionFlags.GR_TEMPLATE);
        if (template == 0) {
            genericRegionAdaptiveTemplateX[0] = readATValue();
            genericRegionAdaptiveTemplateY[0] = readATValue();
            genericRegionAdaptiveTemplateX[1] = readATValue();
            genericRegionAdaptiveTemplateY[1] = readATValue();
        }
        if (this.noOfReferedToSegments == 0 || this.inlineImage) {
            PageInformationSegment pageSegment = this.decoder.findPageSegement(this.segmentHeader.getPageAssociation());
            JBIG2Bitmap pageBitmap = pageSegment.getPageBitmap();
            if (pageSegment.getPageBitmapHeight() == -1 && this.regionBitmapYLocation + this.regionBitmapHeight > pageBitmap.getHeight()) {
                pageBitmap.expand(this.regionBitmapYLocation + this.regionBitmapHeight, pageSegment.getPageInformationFlags().getFlagValue(PageInformationFlags.DEFAULT_PIXEL_VALUE));
            }
        }
        if (this.noOfReferedToSegments > 1) {
            if (JBIG2StreamDecoder.debug) {
                System.out.println("Bad reference in JBIG2 generic refinement Segment");
                return;
            }
            return;
        }
        if (this.noOfReferedToSegments == 1) {
            referedToBitmap = this.decoder.findBitmap(this.referedToSegments[0]);
        } else {
            referedToBitmap = this.decoder.findPageSegement(this.segmentHeader.getPageAssociation()).getPageBitmap().getSlice(this.regionBitmapXLocation, this.regionBitmapYLocation, this.regionBitmapWidth, this.regionBitmapHeight);
        }
        this.arithmeticDecoder.resetRefinementStats(template, null);
        this.arithmeticDecoder.start();
        boolean typicalPredictionGenericRefinementOn = this.refinementRegionFlags.getFlagValue(RefinementRegionFlags.TPGDON) != 0;
        JBIG2Bitmap bitmap = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        bitmap.readGenericRefinementRegion(template, typicalPredictionGenericRefinementOn, referedToBitmap, 0, 0, genericRegionAdaptiveTemplateX, genericRegionAdaptiveTemplateY);
        if (this.inlineImage) {
            JBIG2Bitmap pageBitmap2 = this.decoder.findPageSegement(this.segmentHeader.getPageAssociation()).getPageBitmap();
            int extCombOp = this.regionFlags.getFlagValue(RegionFlags.EXTERNAL_COMBINATION_OPERATOR);
            pageBitmap2.combine(bitmap, this.regionBitmapXLocation, this.regionBitmapYLocation, extCombOp);
        } else {
            bitmap.setBitmapNumber(getSegmentHeader().getSegmentNumber());
            this.decoder.appendBitmap(bitmap);
        }
    }

    private void readGenericRegionFlags() throws IOException {
        short refinementRegionFlagsField = this.decoder.readByte();
        this.refinementRegionFlags.setFlags(refinementRegionFlagsField);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("generic region Segment flags = " + ((int) refinementRegionFlagsField));
        }
    }

    public RefinementRegionFlags getGenericRegionFlags() {
        return this.refinementRegionFlags;
    }
}
