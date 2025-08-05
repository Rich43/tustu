package org.jpedal.jbig2.decoders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.io.StreamReader;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.SegmentHeader;
import org.jpedal.jbig2.segment.extensions.ExtensionSegment;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.pattern.PatternDictionarySegment;
import org.jpedal.jbig2.segment.region.generic.GenericRegionSegment;
import org.jpedal.jbig2.segment.region.halftone.HalftoneRegionSegment;
import org.jpedal.jbig2.segment.region.refinement.RefinementRegionSegment;
import org.jpedal.jbig2.segment.region.text.TextRegionSegment;
import org.jpedal.jbig2.segment.stripes.EndOfStripeSegment;
import org.jpedal.jbig2.segment.symboldictionary.SymbolDictionarySegment;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/decoders/JBIG2StreamDecoder.class */
public class JBIG2StreamDecoder {
    private StreamReader reader;
    private boolean noOfPagesKnown;
    private boolean randomAccessOrganisation;
    private int noOfPages = -1;
    private List<Segment> segments = new ArrayList();
    private List<JBIG2Bitmap> bitmaps = new ArrayList();
    private byte[] globalData;
    private ArithmeticDecoder arithmeticDecoder;
    private HuffmanDecoder huffmanDecoder;
    private MMRDecoder mmrDecoder;
    public static boolean debug = false;

    public void movePointer(int i2) {
        this.reader.movePointer(i2);
    }

    public void setGlobalData(byte[] data) {
        this.globalData = data;
    }

    public void decodeJBIG2(byte[] data) throws JBIG2Exception, IOException {
        this.reader = new StreamReader(data);
        resetDecoder();
        boolean validFile = checkHeader();
        if (debug) {
            System.out.println("validFile = " + validFile);
        }
        if (!validFile) {
            this.noOfPagesKnown = true;
            this.randomAccessOrganisation = false;
            this.noOfPages = 1;
            if (this.globalData != null) {
                this.reader = new StreamReader(this.globalData);
                this.huffmanDecoder = new HuffmanDecoder(this.reader);
                this.mmrDecoder = new MMRDecoder(this.reader);
                this.arithmeticDecoder = new ArithmeticDecoder(this.reader);
                readSegments();
                this.reader = new StreamReader(data);
            } else {
                this.reader.movePointer(-8);
            }
        } else {
            if (debug) {
                System.out.println("==== File Header ====");
            }
            setFileHeaderFlags();
            if (debug) {
                System.out.println("randomAccessOrganisation = " + this.randomAccessOrganisation);
                System.out.println("noOfPagesKnown = " + this.noOfPagesKnown);
            }
            if (this.noOfPagesKnown) {
                this.noOfPages = getNoOfPages();
                if (debug) {
                    System.out.println("noOfPages = " + this.noOfPages);
                }
            }
        }
        this.huffmanDecoder = new HuffmanDecoder(this.reader);
        this.mmrDecoder = new MMRDecoder(this.reader);
        this.arithmeticDecoder = new ArithmeticDecoder(this.reader);
        readSegments();
    }

    public HuffmanDecoder getHuffmanDecoder() {
        return this.huffmanDecoder;
    }

    public MMRDecoder getMMRDecoder() {
        return this.mmrDecoder;
    }

    public ArithmeticDecoder getArithmeticDecoder() {
        return this.arithmeticDecoder;
    }

    private void resetDecoder() {
        this.noOfPagesKnown = false;
        this.randomAccessOrganisation = false;
        this.noOfPages = -1;
        this.segments.clear();
        this.bitmaps.clear();
    }

    public void cleanupPostDecode() {
        Iterator it = this.segments.iterator();
        while (it.hasNext()) {
            Segment segment = it.next();
            SegmentHeader segmentHeader = segment.getSegmentHeader();
            if (segmentHeader.getSegmentType() != 48) {
                it.remove();
            }
        }
        this.bitmaps.clear();
    }

    private void readSegments() throws JBIG2Exception, IOException {
        if (debug) {
            System.out.println("==== Segments ====");
        }
        boolean finished = false;
        while (!this.reader.isFinished() && !finished) {
            SegmentHeader segmentHeader = new SegmentHeader();
            if (debug) {
                System.out.println("==== Segment Header ====");
            }
            readSegmentHeader(segmentHeader);
            Segment segment = null;
            int segmentType = segmentHeader.getSegmentType();
            int[] referredToSegments = segmentHeader.getReferredToSegments();
            int noOfReferredToSegments = segmentHeader.getReferredToSegmentCount();
            switch (segmentType) {
                case 0:
                    if (debug) {
                        System.out.println("==== Segment Symbol Dictionary ====");
                    }
                    segment = new SymbolDictionarySegment(this);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 1:
                case 2:
                case 3:
                case 5:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:
                case 14:
                case 15:
                case 17:
                case 18:
                case 19:
                case 21:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 37:
                case 41:
                case 44:
                case 45:
                case 46:
                case 47:
                case 54:
                case 55:
                case 56:
                case 57:
                case 58:
                case 59:
                case 60:
                case 61:
                default:
                    System.out.println("Unknown Segment type in JBIG2 stream");
                    break;
                case 4:
                    if (debug) {
                        System.out.println("==== Intermediate Text Region ====");
                    }
                    segment = new TextRegionSegment(this, false);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 6:
                    if (debug) {
                        System.out.println("==== Immediate Text Region ====");
                    }
                    segment = new TextRegionSegment(this, true);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 7:
                    if (debug) {
                        System.out.println("==== Immediate Lossless Text Region ====");
                    }
                    segment = new TextRegionSegment(this, true);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 16:
                    if (debug) {
                        System.out.println("==== Pattern Dictionary ====");
                    }
                    segment = new PatternDictionarySegment(this);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 20:
                    if (debug) {
                        System.out.println("==== Intermediate Halftone Region ====");
                    }
                    segment = new HalftoneRegionSegment(this, false);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 22:
                    if (debug) {
                        System.out.println("==== Immediate Halftone Region ====");
                    }
                    segment = new HalftoneRegionSegment(this, true);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 23:
                    if (debug) {
                        System.out.println("==== Immediate Lossless Halftone Region ====");
                    }
                    segment = new HalftoneRegionSegment(this, true);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 36:
                    if (debug) {
                        System.out.println("==== Intermediate Generic Region ====");
                    }
                    segment = new GenericRegionSegment(this, false);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 38:
                    if (debug) {
                        System.out.println("==== Immediate Generic Region ====");
                    }
                    segment = new GenericRegionSegment(this, true);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 39:
                    if (debug) {
                        System.out.println("==== Immediate Lossless Generic Region ====");
                    }
                    segment = new GenericRegionSegment(this, true);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 40:
                    if (debug) {
                        System.out.println("==== Intermediate Generic Refinement Region ====");
                    }
                    segment = new RefinementRegionSegment(this, false, referredToSegments, noOfReferredToSegments);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 42:
                    if (debug) {
                        System.out.println("==== Immediate Generic Refinement Region ====");
                    }
                    segment = new RefinementRegionSegment(this, true, referredToSegments, noOfReferredToSegments);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 43:
                    if (debug) {
                        System.out.println("==== Immediate lossless Generic Refinement Region ====");
                    }
                    segment = new RefinementRegionSegment(this, true, referredToSegments, noOfReferredToSegments);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 48:
                    if (debug) {
                        System.out.println("==== Page Information Dictionary ====");
                    }
                    segment = new PageInformationSegment(this);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 49:
                case 50:
                    if (debug) {
                        System.out.println("==== End of Stripes ====");
                    }
                    segment = new EndOfStripeSegment(this);
                    segment.setSegmentHeader(segmentHeader);
                    break;
                case 51:
                    if (debug) {
                        System.out.println("==== End of File ====");
                    }
                    finished = true;
                    continue;
                case 52:
                    if (debug) {
                        System.out.println("PROFILES UNIMPLEMENTED");
                        break;
                    }
                    break;
                case 53:
                    if (debug) {
                        System.out.println("TABLES UNIMPLEMENTED");
                        break;
                    }
                    break;
                case 62:
                    if (debug) {
                        System.out.println("==== Extensions ====");
                    }
                    segment = new ExtensionSegment(this);
                    segment.setSegmentHeader(segmentHeader);
                    break;
            }
            if (!this.randomAccessOrganisation && segment != null) {
                segment.readSegment();
            }
            if (this.segments != null) {
                this.segments.add(segment);
            }
        }
        if (this.randomAccessOrganisation && this.segments != null) {
            for (Object segment1 : this.segments) {
                ((Segment) segment1).readSegment();
            }
        }
    }

    public PageInformationSegment findPageSegement(int page) {
        for (Object segment1 : this.segments) {
            Segment segment = (Segment) segment1;
            SegmentHeader segmentHeader = segment.getSegmentHeader();
            if (segmentHeader.getSegmentType() == 48 && segmentHeader.getPageAssociation() == page) {
                return (PageInformationSegment) segment;
            }
        }
        return null;
    }

    public Segment findSegment(int segmentNumber) {
        for (Object segment1 : this.segments) {
            Segment segment = (Segment) segment1;
            if (segment.getSegmentHeader().getSegmentNumber() == segmentNumber) {
                return segment;
            }
        }
        return null;
    }

    private void readSegmentHeader(SegmentHeader segmentHeader) throws JBIG2Exception, IOException {
        handleSegmentNumber(segmentHeader);
        handleSegmentHeaderFlags(segmentHeader);
        handleSegmentReferredToCountAndRententionFlags(segmentHeader);
        handleReferedToSegmentNumbers(segmentHeader);
        handlePageAssociation(segmentHeader);
        if (segmentHeader.getSegmentType() != 51) {
            handleSegmentDataLength(segmentHeader);
        }
    }

    private void handlePageAssociation(SegmentHeader segmentHeader) throws IOException {
        int pageAssociation;
        boolean isPageAssociationSizeSet = segmentHeader.isPageAssociationSizeSet();
        if (isPageAssociationSizeSet) {
            short[] buf = new short[4];
            this.reader.readByte(buf);
            pageAssociation = BinaryOperation.getInt32(buf);
        } else {
            pageAssociation = this.reader.readByte();
        }
        segmentHeader.setPageAssociation(pageAssociation);
        if (debug) {
            System.out.println("pageAssociation = " + pageAssociation);
        }
    }

    private void handleSegmentNumber(SegmentHeader segmentHeader) throws IOException {
        short[] segmentBytes = new short[4];
        this.reader.readByte(segmentBytes);
        int segmentNumber = BinaryOperation.getInt32(segmentBytes);
        if (debug) {
            System.out.println("SegmentNumber = " + segmentNumber);
        }
        segmentHeader.setSegmentNumber(segmentNumber);
    }

    private void handleSegmentHeaderFlags(SegmentHeader segmentHeader) throws IOException {
        short segmentHeaderFlags = this.reader.readByte();
        segmentHeader.setSegmentHeaderFlags(segmentHeaderFlags);
    }

    private void handleSegmentReferredToCountAndRententionFlags(SegmentHeader segmentHeader) throws JBIG2Exception, IOException {
        short[] retentionFlags;
        short referedToSegmentCountAndRetentionFlags = this.reader.readByte();
        int referredToSegmentCount = (referedToSegmentCountAndRetentionFlags & 224) >> 5;
        short firstByte = (short) (referedToSegmentCountAndRetentionFlags & 31);
        if (referredToSegmentCount <= 4) {
            retentionFlags = new short[]{firstByte};
        } else if (referredToSegmentCount == 7) {
            short[] longFormCountAndFlags = new short[4];
            longFormCountAndFlags[0] = firstByte;
            for (int i2 = 1; i2 < 4; i2++) {
                longFormCountAndFlags[i2] = this.reader.readByte();
            }
            referredToSegmentCount = BinaryOperation.getInt32(longFormCountAndFlags);
            int noOfBytesInField = (int) Math.ceil(4.0d + ((referredToSegmentCount + 1) / 8.0d));
            int noOfRententionFlagBytes = noOfBytesInField - 4;
            retentionFlags = new short[noOfRententionFlagBytes];
            this.reader.readByte(retentionFlags);
        } else {
            throw new JBIG2Exception("Error, 3 bit Segment count field = " + referredToSegmentCount);
        }
        segmentHeader.setReferredToSegmentCount(referredToSegmentCount);
        if (debug) {
            System.out.println("referredToSegmentCount = " + referredToSegmentCount);
        }
        segmentHeader.setRententionFlags(retentionFlags);
        if (debug) {
            System.out.print("retentionFlags = ");
        }
        if (debug) {
            short[] arr$ = retentionFlags;
            for (short retentionFlag : arr$) {
                System.out.print(((int) retentionFlag) + " ");
            }
            System.out.println("");
        }
    }

    private void handleReferedToSegmentNumbers(SegmentHeader segmentHeader) throws IOException {
        int referredToSegmentCount = segmentHeader.getReferredToSegmentCount();
        int[] referredToSegments = new int[referredToSegmentCount];
        int segmentNumber = segmentHeader.getSegmentNumber();
        if (segmentNumber <= 256) {
            for (int i2 = 0; i2 < referredToSegmentCount; i2++) {
                referredToSegments[i2] = this.reader.readByte();
            }
        } else if (segmentNumber <= 65536) {
            short[] buf = new short[2];
            for (int i3 = 0; i3 < referredToSegmentCount; i3++) {
                this.reader.readByte(buf);
                referredToSegments[i3] = BinaryOperation.getInt16(buf);
            }
        } else {
            short[] buf2 = new short[4];
            for (int i4 = 0; i4 < referredToSegmentCount; i4++) {
                this.reader.readByte(buf2);
                referredToSegments[i4] = BinaryOperation.getInt32(buf2);
            }
        }
        segmentHeader.setReferredToSegments(referredToSegments);
        if (debug) {
            System.out.print("referredToSegments = ");
            for (int referredToSegment : referredToSegments) {
                System.out.print(referredToSegment + " ");
            }
            System.out.println("");
        }
    }

    private int getNoOfPages() throws IOException {
        short[] noOfPages = new short[4];
        this.reader.readByte(noOfPages);
        return BinaryOperation.getInt32(noOfPages);
    }

    private void handleSegmentDataLength(SegmentHeader segmentHeader) throws IOException {
        short[] buf = new short[4];
        this.reader.readByte(buf);
        int dateLength = BinaryOperation.getInt32(buf);
        segmentHeader.setDataLength(dateLength);
        if (debug) {
            System.out.println("dateLength = " + dateLength);
        }
    }

    private void setFileHeaderFlags() throws IOException {
        int headerFlags = this.reader.readByte();
        if ((headerFlags & 252) != 0) {
            System.out.println("Warning, reserved bits (2-7) of file header flags are not zero " + headerFlags);
        }
        int fileOrganisation = headerFlags & 1;
        this.randomAccessOrganisation = fileOrganisation == 0;
        int pagesKnown = headerFlags & 2;
        this.noOfPagesKnown = pagesKnown == 0;
    }

    private boolean checkHeader() throws IOException {
        short[] controlHeader = {151, 74, 66, 50, 13, 10, 26, 10};
        short[] actualHeader = new short[8];
        this.reader.readByte(actualHeader);
        return Arrays.equals(controlHeader, actualHeader);
    }

    public int readBits(int num) throws IOException {
        return this.reader.readBits(num);
    }

    public int readBit() throws IOException {
        return this.reader.readBit();
    }

    public void readByte(short[] buff) throws IOException {
        this.reader.readByte(buff);
    }

    public void consumeRemainingBits() throws IOException {
        this.reader.consumeRemainingBits();
    }

    public short readByte() throws IOException {
        return this.reader.readByte();
    }

    public void appendBitmap(JBIG2Bitmap bitmap) {
        this.bitmaps.add(bitmap);
    }

    public JBIG2Bitmap findBitmap(int bitmapNumber) {
        for (Object bitmap1 : this.bitmaps) {
            JBIG2Bitmap bitmap = (JBIG2Bitmap) bitmap1;
            if (bitmap.getBitmapNumber() == bitmapNumber) {
                return bitmap;
            }
        }
        return null;
    }

    public JBIG2Bitmap getPageAsJBIG2Bitmap(int i2) {
        return findPageSegement(i2).getPageBitmap();
    }

    public boolean isNumberOfPagesKnown() {
        return this.noOfPagesKnown;
    }

    public int getNumberOfPages() {
        return this.noOfPages;
    }

    public boolean isRandomAccessOrganisationUsed() {
        return this.randomAccessOrganisation;
    }

    public List getAllSegments() {
        return this.segments;
    }
}
