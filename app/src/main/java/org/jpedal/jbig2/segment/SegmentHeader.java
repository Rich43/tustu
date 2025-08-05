package org.jpedal.jbig2.segment;

import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/SegmentHeader.class */
public class SegmentHeader {
    private int segmentNumber;
    private int segmentType;
    private boolean pageAssociationSizeSet;
    private boolean deferredNonRetainSet;
    private int referredToSegmentCount;
    private short[] rententionFlags;
    private int[] referredToSegments;
    private int pageAssociation;
    private int dataLength;

    public void setSegmentNumber(int SegmentNumber) {
        this.segmentNumber = SegmentNumber;
    }

    public void setSegmentHeaderFlags(short SegmentHeaderFlags) {
        this.segmentType = SegmentHeaderFlags & 63;
        this.pageAssociationSizeSet = (SegmentHeaderFlags & 64) == 64;
        this.deferredNonRetainSet = (SegmentHeaderFlags & 80) == 80;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("SegmentType = " + this.segmentType);
            System.out.println("pageAssociationSizeSet = " + this.pageAssociationSizeSet);
            System.out.println("deferredNonRetainSet = " + this.deferredNonRetainSet);
        }
    }

    public void setReferredToSegmentCount(int referredToSegmentCount) {
        this.referredToSegmentCount = referredToSegmentCount;
    }

    public void setRententionFlags(short[] rententionFlags) {
        this.rententionFlags = rententionFlags;
    }

    public void setReferredToSegments(int[] referredToSegments) {
        this.referredToSegments = referredToSegments;
    }

    public int[] getReferredToSegments() {
        return this.referredToSegments;
    }

    public int getSegmentType() {
        return this.segmentType;
    }

    public int getSegmentNumber() {
        return this.segmentNumber;
    }

    public boolean isPageAssociationSizeSet() {
        return this.pageAssociationSizeSet;
    }

    public boolean isDeferredNonRetainSet() {
        return this.deferredNonRetainSet;
    }

    public int getReferredToSegmentCount() {
        return this.referredToSegmentCount;
    }

    public short[] getRententionFlags() {
        return this.rententionFlags;
    }

    public int getPageAssociation() {
        return this.pageAssociation;
    }

    public void setPageAssociation(int pageAssociation) {
        this.pageAssociation = pageAssociation;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public void setSegmentType(int type) {
        this.segmentType = type;
    }

    public int getSegmentDataLength() {
        return this.dataLength;
    }
}
