package org.jpedal.jbig2.segment.region.text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.pageinformation.PageInformationSegment;
import org.jpedal.jbig2.segment.region.RegionFlags;
import org.jpedal.jbig2.segment.region.RegionSegment;
import org.jpedal.jbig2.segment.symboldictionary.SymbolDictionarySegment;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/region/text/TextRegionSegment.class */
public class TextRegionSegment extends RegionSegment {
    private TextRegionFlags textRegionFlags;
    private TextRegionHuffmanFlags textRegionHuffmanFlags;
    private int noOfSymbolInstances;
    private boolean inlineImage;
    private short[] symbolRegionAdaptiveTemplateX;
    private short[] symbolRegionAdaptiveTemplateY;

    public TextRegionSegment(JBIG2StreamDecoder streamDecoder, boolean inlineImage) {
        super(streamDecoder);
        this.textRegionFlags = new TextRegionFlags();
        this.textRegionHuffmanFlags = new TextRegionHuffmanFlags();
        this.symbolRegionAdaptiveTemplateX = new short[2];
        this.symbolRegionAdaptiveTemplateY = new short[2];
        this.inlineImage = inlineImage;
    }

    @Override // org.jpedal.jbig2.segment.region.RegionSegment, org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        int[][] symbolCodeTable;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("==== Reading Text Region ====");
        }
        super.readSegment();
        readTextRegionFlags();
        short[] buff = new short[4];
        this.decoder.readByte(buff);
        this.noOfSymbolInstances = BinaryOperation.getInt32(buff);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("noOfSymbolInstances = " + this.noOfSymbolInstances);
        }
        int noOfReferredToSegments = this.segmentHeader.getReferredToSegmentCount();
        int[] referredToSegments = this.segmentHeader.getReferredToSegments();
        List<Segment> codeTables = new ArrayList<>();
        List<Segment> segmentsReferenced = new ArrayList<>();
        int noOfSymbols = 0;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("noOfReferredToSegments = " + noOfReferredToSegments);
        }
        for (int i2 = 0; i2 < noOfReferredToSegments; i2++) {
            Segment seg = this.decoder.findSegment(referredToSegments[i2]);
            int type = seg.getSegmentHeader().getSegmentType();
            if (type == 0) {
                segmentsReferenced.add(seg);
                noOfSymbols += ((SymbolDictionarySegment) seg).getNoOfExportedSymbols();
            } else if (type == 53) {
                codeTables.add(seg);
            }
        }
        int symbolCodeLength = 0;
        int i3 = 1;
        while (true) {
            int count = i3;
            if (count >= noOfSymbols) {
                break;
            }
            symbolCodeLength++;
            i3 = count << 1;
        }
        int currentSymbol = 0;
        JBIG2Bitmap[] symbols = new JBIG2Bitmap[noOfSymbols];
        for (Segment seg2 : segmentsReferenced) {
            if (seg2.getSegmentHeader().getSegmentType() == 0) {
                JBIG2Bitmap[] bitmaps = ((SymbolDictionarySegment) seg2).getBitmaps();
                for (JBIG2Bitmap jBIG2Bitmap : bitmaps) {
                    symbols[currentSymbol] = jBIG2Bitmap;
                    currentSymbol++;
                }
            }
        }
        int[][] huffmanFSTable = (int[][]) null;
        int[][] huffmanDSTable = (int[][]) null;
        int[][] huffmanDTTable = (int[][]) null;
        int[][] huffmanRDWTable = (int[][]) null;
        int[][] huffmanRDHTable = (int[][]) null;
        int[][] huffmanRDXTable = (int[][]) null;
        int[][] huffmanRDYTable = (int[][]) null;
        int[][] huffmanRSizeTable = (int[][]) null;
        boolean sbHuffman = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_HUFF) != 0;
        if (sbHuffman) {
            int sbHuffFS = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_FS);
            if (sbHuffFS == 0) {
                huffmanFSTable = HuffmanDecoder.huffmanTableF;
            } else if (sbHuffFS == 1) {
                huffmanFSTable = HuffmanDecoder.huffmanTableG;
            }
            int sbHuffDS = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_DS);
            if (sbHuffDS == 0) {
                huffmanDSTable = HuffmanDecoder.huffmanTableH;
            } else if (sbHuffDS == 1) {
                huffmanDSTable = HuffmanDecoder.huffmanTableI;
            } else if (sbHuffDS == 2) {
                huffmanDSTable = HuffmanDecoder.huffmanTableJ;
            }
            int sbHuffDT = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_DT);
            if (sbHuffDT == 0) {
                huffmanDTTable = HuffmanDecoder.huffmanTableK;
            } else if (sbHuffDT == 1) {
                huffmanDTTable = HuffmanDecoder.huffmanTableL;
            } else if (sbHuffDT == 2) {
                huffmanDTTable = HuffmanDecoder.huffmanTableM;
            }
            int sbHuffRDW = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_RDW);
            if (sbHuffRDW == 0) {
                huffmanRDWTable = HuffmanDecoder.huffmanTableN;
            } else if (sbHuffRDW == 1) {
                huffmanRDWTable = HuffmanDecoder.huffmanTableO;
            }
            int sbHuffRDH = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_RDH);
            if (sbHuffRDH == 0) {
                huffmanRDHTable = HuffmanDecoder.huffmanTableN;
            } else if (sbHuffRDH == 1) {
                huffmanRDHTable = HuffmanDecoder.huffmanTableO;
            }
            int sbHuffRDX = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_RDX);
            if (sbHuffRDX == 0) {
                huffmanRDXTable = HuffmanDecoder.huffmanTableN;
            } else if (sbHuffRDX == 1) {
                huffmanRDXTable = HuffmanDecoder.huffmanTableO;
            }
            int sbHuffRDY = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_RDY);
            if (sbHuffRDY == 0) {
                huffmanRDYTable = HuffmanDecoder.huffmanTableN;
            } else if (sbHuffRDY == 1) {
                huffmanRDYTable = HuffmanDecoder.huffmanTableO;
            }
            int sbHuffRSize = this.textRegionHuffmanFlags.getFlagValue(TextRegionHuffmanFlags.SB_HUFF_RSIZE);
            if (sbHuffRSize == 0) {
                huffmanRSizeTable = HuffmanDecoder.huffmanTableA;
            }
        }
        int[][] runLengthTable = new int[36][4];
        int[][] symbolCodeTable2 = new int[noOfSymbols + 1][4];
        if (sbHuffman) {
            this.decoder.consumeRemainingBits();
            for (int i4 = 0; i4 < 32; i4++) {
                runLengthTable[i4] = new int[]{i4, this.decoder.readBits(4), 0, 0};
            }
            runLengthTable[32] = new int[]{259, this.decoder.readBits(4), 2, 0};
            runLengthTable[33] = new int[]{515, this.decoder.readBits(4), 3, 0};
            runLengthTable[34] = new int[]{523, this.decoder.readBits(4), 7, 0};
            runLengthTable[35] = new int[]{0, 0, HuffmanDecoder.jbig2HuffmanEOT};
            int[][] runLengthTable2 = HuffmanDecoder.buildTable(runLengthTable, 35);
            for (int i5 = 0; i5 < noOfSymbols; i5++) {
                symbolCodeTable2[i5] = new int[]{i5, 0, 0, 0};
            }
            int i6 = 0;
            while (i6 < noOfSymbols) {
                int j2 = this.huffmanDecoder.decodeInt(runLengthTable2).intResult();
                if (j2 > 512) {
                    for (int j3 = j2 - 512; j3 != 0 && i6 < noOfSymbols; j3--) {
                        int i7 = i6;
                        i6++;
                        symbolCodeTable2[i7][1] = 0;
                    }
                } else if (j2 > 256) {
                    for (int j4 = j2 - 256; j4 != 0 && i6 < noOfSymbols; j4--) {
                        symbolCodeTable2[i6][1] = symbolCodeTable2[i6 - 1][1];
                        i6++;
                    }
                } else {
                    int i8 = i6;
                    i6++;
                    symbolCodeTable2[i8][1] = j2;
                }
            }
            symbolCodeTable2[noOfSymbols][1] = 0;
            symbolCodeTable2[noOfSymbols][2] = HuffmanDecoder.jbig2HuffmanEOT;
            symbolCodeTable = HuffmanDecoder.buildTable(symbolCodeTable2, noOfSymbols);
            this.decoder.consumeRemainingBits();
        } else {
            symbolCodeTable = (int[][]) null;
            this.arithmeticDecoder.resetIntStats(symbolCodeLength);
            this.arithmeticDecoder.start();
        }
        boolean symbolRefine = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_REFINE) != 0;
        int logStrips = this.textRegionFlags.getFlagValue(TextRegionFlags.LOG_SB_STRIPES);
        int defaultPixel = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_DEF_PIXEL);
        int combinationOperator = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_COMB_OP);
        boolean transposed = this.textRegionFlags.getFlagValue(TextRegionFlags.TRANSPOSED) != 0;
        int referenceCorner = this.textRegionFlags.getFlagValue(TextRegionFlags.REF_CORNER);
        int sOffset = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_DS_OFFSET);
        int template = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_R_TEMPLATE);
        if (symbolRefine) {
            this.arithmeticDecoder.resetRefinementStats(template, null);
        }
        JBIG2Bitmap bitmap = new JBIG2Bitmap(this.regionBitmapWidth, this.regionBitmapHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
        bitmap.readTextRegion(sbHuffman, symbolRefine, this.noOfSymbolInstances, logStrips, noOfSymbols, symbolCodeTable, symbolCodeLength, symbols, defaultPixel, combinationOperator, transposed, referenceCorner, sOffset, huffmanFSTable, huffmanDSTable, huffmanDTTable, huffmanRDWTable, huffmanRDHTable, huffmanRDXTable, huffmanRDYTable, huffmanRSizeTable, template, this.symbolRegionAdaptiveTemplateX, this.symbolRegionAdaptiveTemplateY, this.decoder);
        if (this.inlineImage) {
            PageInformationSegment pageSegment = this.decoder.findPageSegement(this.segmentHeader.getPageAssociation());
            JBIG2Bitmap pageBitmap = pageSegment.getPageBitmap();
            if (JBIG2StreamDecoder.debug) {
                System.out.println(((Object) pageBitmap) + " " + ((Object) bitmap));
            }
            int externalCombinationOperator = this.regionFlags.getFlagValue(RegionFlags.EXTERNAL_COMBINATION_OPERATOR);
            pageBitmap.combine(bitmap, this.regionBitmapXLocation, this.regionBitmapYLocation, externalCombinationOperator);
        } else {
            bitmap.setBitmapNumber(getSegmentHeader().getSegmentNumber());
            this.decoder.appendBitmap(bitmap);
        }
        this.decoder.consumeRemainingBits();
    }

    private void readTextRegionFlags() throws IOException {
        short[] textRegionFlagsField = new short[2];
        this.decoder.readByte(textRegionFlagsField);
        int flags = BinaryOperation.getInt16(textRegionFlagsField);
        this.textRegionFlags.setFlags(flags);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("text region Segment flags = " + flags);
        }
        boolean sbHuff = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_HUFF) != 0;
        if (sbHuff) {
            short[] textRegionHuffmanFlagsField = new short[2];
            this.decoder.readByte(textRegionHuffmanFlagsField);
            int flags2 = BinaryOperation.getInt16(textRegionHuffmanFlagsField);
            this.textRegionHuffmanFlags.setFlags(flags2);
            if (JBIG2StreamDecoder.debug) {
                System.out.println("text region segment Huffman flags = " + flags2);
            }
        }
        boolean sbRefine = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_REFINE) != 0;
        int sbrTemplate = this.textRegionFlags.getFlagValue(TextRegionFlags.SB_R_TEMPLATE);
        if (sbRefine && sbrTemplate == 0) {
            this.symbolRegionAdaptiveTemplateX[0] = readATValue();
            this.symbolRegionAdaptiveTemplateY[0] = readATValue();
            this.symbolRegionAdaptiveTemplateX[1] = readATValue();
            this.symbolRegionAdaptiveTemplateY[1] = readATValue();
        }
    }

    public TextRegionFlags getTextRegionFlags() {
        return this.textRegionFlags;
    }

    public TextRegionHuffmanFlags getTextRegionHuffmanFlags() {
        return this.textRegionHuffmanFlags;
    }
}
