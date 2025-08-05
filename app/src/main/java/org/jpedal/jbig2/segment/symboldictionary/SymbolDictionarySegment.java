package org.jpedal.jbig2.segment.symboldictionary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jpedal.jbig2.JBIG2Exception;
import org.jpedal.jbig2.decoders.ArithmeticDecoderStats;
import org.jpedal.jbig2.decoders.DecodeIntResult;
import org.jpedal.jbig2.decoders.HuffmanDecoder;
import org.jpedal.jbig2.decoders.JBIG2StreamDecoder;
import org.jpedal.jbig2.image.JBIG2Bitmap;
import org.jpedal.jbig2.segment.Segment;
import org.jpedal.jbig2.segment.tables.JBIG2CodeTable;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/segment/symboldictionary/SymbolDictionarySegment.class */
public class SymbolDictionarySegment extends Segment {
    private int noOfExportedSymbols;
    private int noOfNewSymbols;
    short[] symbolDictionaryAdaptiveTemplateX;
    short[] symbolDictionaryAdaptiveTemplateY;
    short[] symbolDictionaryRAdaptiveTemplateX;
    short[] symbolDictionaryRAdaptiveTemplateY;
    private JBIG2Bitmap[] bitmaps;
    private SymbolDictionaryFlags symbolDictionaryFlags;
    private ArithmeticDecoderStats genericRegionStats;
    private ArithmeticDecoderStats refinementRegionStats;

    public SymbolDictionarySegment(JBIG2StreamDecoder streamDecoder) {
        super(streamDecoder);
        this.symbolDictionaryAdaptiveTemplateX = new short[4];
        this.symbolDictionaryAdaptiveTemplateY = new short[4];
        this.symbolDictionaryRAdaptiveTemplateX = new short[2];
        this.symbolDictionaryRAdaptiveTemplateY = new short[2];
        this.symbolDictionaryFlags = new SymbolDictionaryFlags();
    }

    @Override // org.jpedal.jbig2.segment.Segment
    public void readSegment() throws JBIG2Exception, IOException {
        int run;
        int instanceDeltaHeight;
        DecodeIntResult decodeIntResult;
        int refAggNum;
        int symbolID;
        int referenceDX;
        int referenceDY;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("==== Read Segment Symbol Dictionary ====");
        }
        readSymbolDictionaryFlags();
        List<Object> codeTables = new ArrayList<>();
        int numberOfInputSymbols = 0;
        int noOfReferredToSegments = this.segmentHeader.getReferredToSegmentCount();
        int[] referredToSegments = this.segmentHeader.getReferredToSegments();
        for (int i2 = 0; i2 < noOfReferredToSegments; i2++) {
            Segment seg = this.decoder.findSegment(referredToSegments[i2]);
            int type = seg.getSegmentHeader().getSegmentType();
            if (type == 0) {
                numberOfInputSymbols += ((SymbolDictionarySegment) seg).noOfExportedSymbols;
            } else if (type == 53) {
                codeTables.add(seg);
            }
        }
        int symbolCodeLength = 0;
        int i3 = 1;
        while (true) {
            int i4 = i3;
            if (i4 >= numberOfInputSymbols + this.noOfNewSymbols) {
                break;
            }
            symbolCodeLength++;
            i3 = i4 << 1;
        }
        JBIG2Bitmap[] bitmaps = new JBIG2Bitmap[numberOfInputSymbols + this.noOfNewSymbols];
        int k2 = 0;
        SymbolDictionarySegment inputSymbolDictionary = null;
        for (int i5 = 0; i5 < noOfReferredToSegments; i5++) {
            Segment seg2 = this.decoder.findSegment(referredToSegments[i5]);
            if (seg2.getSegmentHeader().getSegmentType() == 0) {
                inputSymbolDictionary = (SymbolDictionarySegment) seg2;
                for (int j2 = 0; j2 < inputSymbolDictionary.noOfExportedSymbols; j2++) {
                    int i6 = k2;
                    k2++;
                    bitmaps[i6] = inputSymbolDictionary.bitmaps[j2];
                }
            }
        }
        int[][] huffmanDHTable = (int[][]) null;
        int[][] huffmanDWTable = (int[][]) null;
        int[][] huffmanBMSizeTable = (int[][]) null;
        int[][] huffmanAggInstTable = (int[][]) null;
        boolean sdHuffman = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_HUFF) != 0;
        int sdHuffmanDifferenceHeight = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_HUFF_DH);
        int sdHuffmanDiferrenceWidth = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_HUFF_DW);
        int sdHuffBitmapSize = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_HUFF_BM_SIZE);
        int sdHuffAggregationInstances = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_HUFF_AGG_INST);
        int i7 = 0;
        if (sdHuffman) {
            if (sdHuffmanDifferenceHeight == 0) {
                huffmanDHTable = HuffmanDecoder.huffmanTableD;
            } else if (sdHuffmanDifferenceHeight != 1) {
                i7 = 0 + 1;
                huffmanDHTable = JBIG2CodeTable.getHuffTable();
            } else {
                huffmanDHTable = HuffmanDecoder.huffmanTableE;
            }
            if (sdHuffmanDiferrenceWidth == 0) {
                huffmanDWTable = HuffmanDecoder.huffmanTableB;
            } else if (sdHuffmanDiferrenceWidth == 1) {
                huffmanDWTable = HuffmanDecoder.huffmanTableC;
            } else {
                int i8 = i7;
                i7++;
                huffmanDWTable = JBIG2CodeTable.getHuffTable();
            }
            if (sdHuffBitmapSize == 0) {
                huffmanBMSizeTable = HuffmanDecoder.huffmanTableA;
            } else {
                int i9 = i7;
                i7++;
                huffmanBMSizeTable = JBIG2CodeTable.getHuffTable();
            }
            if (sdHuffAggregationInstances == 0) {
                huffmanAggInstTable = HuffmanDecoder.huffmanTableA;
            } else {
                int i10 = i7;
                int i11 = i7 + 1;
                huffmanAggInstTable = JBIG2CodeTable.getHuffTable();
            }
        }
        int contextUsed = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.BITMAP_CC_USED);
        int sdTemplate = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_TEMPLATE);
        if (!sdHuffman) {
            if (contextUsed != 0 && inputSymbolDictionary != null) {
                this.arithmeticDecoder.resetGenericStats(sdTemplate, inputSymbolDictionary.genericRegionStats);
            } else {
                this.arithmeticDecoder.resetGenericStats(sdTemplate, null);
            }
            this.arithmeticDecoder.resetIntStats(symbolCodeLength);
            this.arithmeticDecoder.start();
        }
        int sdRefinementAggregate = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_REF_AGG);
        int sdRefinementTemplate = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_R_TEMPLATE);
        if (sdRefinementAggregate != 0) {
            if (contextUsed != 0 && inputSymbolDictionary != null) {
                this.arithmeticDecoder.resetRefinementStats(sdRefinementTemplate, inputSymbolDictionary.refinementRegionStats);
            } else {
                this.arithmeticDecoder.resetRefinementStats(sdRefinementTemplate, null);
            }
        }
        int[] deltaWidths = new int[this.noOfNewSymbols];
        int deltaHeight = 0;
        int i12 = 0;
        while (i12 < this.noOfNewSymbols) {
            if (sdHuffman) {
                instanceDeltaHeight = this.huffmanDecoder.decodeInt(huffmanDHTable).intResult();
            } else {
                instanceDeltaHeight = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadhStats).intResult();
            }
            if (instanceDeltaHeight < 0 && (-instanceDeltaHeight) >= deltaHeight && JBIG2StreamDecoder.debug) {
                System.out.println("Bad delta-height value in JBIG2 symbol dictionary");
            }
            deltaHeight += instanceDeltaHeight;
            int symbolWidth = 0;
            int totalWidth = 0;
            while (true) {
                if (sdHuffman) {
                    decodeIntResult = this.huffmanDecoder.decodeInt(huffmanDWTable);
                } else {
                    decodeIntResult = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iadwStats);
                }
                if (!decodeIntResult.booleanResult()) {
                    break;
                }
                int deltaWidth = decodeIntResult.intResult();
                if (deltaWidth < 0 && (-deltaWidth) >= symbolWidth && JBIG2StreamDecoder.debug) {
                    System.out.println("Bad delta-width value in JBIG2 symbol dictionary");
                }
                symbolWidth += deltaWidth;
                if (sdHuffman && sdRefinementAggregate == 0) {
                    deltaWidths[i12] = symbolWidth;
                    totalWidth += symbolWidth;
                } else if (sdRefinementAggregate == 1) {
                    if (sdHuffman) {
                        refAggNum = this.huffmanDecoder.decodeInt(huffmanAggInstTable).intResult();
                    } else {
                        refAggNum = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iaaiStats).intResult();
                    }
                    if (refAggNum == 1) {
                        if (sdHuffman) {
                            symbolID = this.decoder.readBits(symbolCodeLength);
                            referenceDX = this.huffmanDecoder.decodeInt(HuffmanDecoder.huffmanTableO).intResult();
                            referenceDY = this.huffmanDecoder.decodeInt(HuffmanDecoder.huffmanTableO).intResult();
                            this.decoder.consumeRemainingBits();
                            this.arithmeticDecoder.start();
                        } else {
                            symbolID = (int) this.arithmeticDecoder.decodeIAID(symbolCodeLength, this.arithmeticDecoder.iaidStats);
                            referenceDX = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardxStats).intResult();
                            referenceDY = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iardyStats).intResult();
                        }
                        JBIG2Bitmap referredToBitmap = bitmaps[symbolID];
                        JBIG2Bitmap bitmap = new JBIG2Bitmap(symbolWidth, deltaHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
                        bitmap.readGenericRefinementRegion(sdRefinementTemplate, false, referredToBitmap, referenceDX, referenceDY, this.symbolDictionaryRAdaptiveTemplateX, this.symbolDictionaryRAdaptiveTemplateY);
                        bitmaps[numberOfInputSymbols + i12] = bitmap;
                    } else {
                        JBIG2Bitmap bitmap2 = new JBIG2Bitmap(symbolWidth, deltaHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
                        bitmap2.readTextRegion(sdHuffman, true, refAggNum, 0, numberOfInputSymbols + i12, (int[][]) null, symbolCodeLength, bitmaps, 0, 0, false, 1, 0, HuffmanDecoder.huffmanTableF, HuffmanDecoder.huffmanTableH, HuffmanDecoder.huffmanTableK, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableO, HuffmanDecoder.huffmanTableA, sdRefinementTemplate, this.symbolDictionaryRAdaptiveTemplateX, this.symbolDictionaryRAdaptiveTemplateY, this.decoder);
                        bitmaps[numberOfInputSymbols + i12] = bitmap2;
                    }
                } else {
                    JBIG2Bitmap bitmap3 = new JBIG2Bitmap(symbolWidth, deltaHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
                    bitmap3.readBitmap(false, sdTemplate, false, false, null, this.symbolDictionaryAdaptiveTemplateX, this.symbolDictionaryAdaptiveTemplateY, 0);
                    bitmaps[numberOfInputSymbols + i12] = bitmap3;
                }
                i12++;
            }
            if (sdHuffman && sdRefinementAggregate == 0) {
                int bmSize = this.huffmanDecoder.decodeInt(huffmanBMSizeTable).intResult();
                this.decoder.consumeRemainingBits();
                JBIG2Bitmap collectiveBitmap = new JBIG2Bitmap(totalWidth, deltaHeight, this.arithmeticDecoder, this.huffmanDecoder, this.mmrDecoder);
                if (bmSize == 0) {
                    int padding = totalWidth % 8;
                    int bytesPerRow = (int) Math.ceil(totalWidth / 8.0d);
                    int size = deltaHeight * ((totalWidth + 7) >> 3);
                    short[] bitmap4 = new short[size];
                    this.decoder.readByte(bitmap4);
                    short[][] logicalMap = new short[deltaHeight][bytesPerRow];
                    int count = 0;
                    for (int row = 0; row < deltaHeight; row++) {
                        for (int col = 0; col < bytesPerRow; col++) {
                            logicalMap[row][col] = bitmap4[count];
                            count++;
                        }
                    }
                    int collectiveBitmapRow = 0;
                    int collectiveBitmapCol = 0;
                    for (int row2 = 0; row2 < deltaHeight; row2++) {
                        for (int col2 = 0; col2 < bytesPerRow; col2++) {
                            if (col2 == bytesPerRow - 1) {
                                short currentByte = logicalMap[row2][col2];
                                for (int bitPointer = 7; bitPointer >= padding; bitPointer--) {
                                    short mask = (short) (1 << bitPointer);
                                    int bit = (currentByte & mask) >> bitPointer;
                                    collectiveBitmap.setPixel(collectiveBitmapCol, collectiveBitmapRow, bit);
                                    collectiveBitmapCol++;
                                }
                                collectiveBitmapRow++;
                                collectiveBitmapCol = 0;
                            } else {
                                short currentByte2 = logicalMap[row2][col2];
                                for (int bitPointer2 = 7; bitPointer2 >= 0; bitPointer2--) {
                                    short mask2 = (short) (1 << bitPointer2);
                                    int bit2 = (currentByte2 & mask2) >> bitPointer2;
                                    collectiveBitmap.setPixel(collectiveBitmapCol, collectiveBitmapRow, bit2);
                                    collectiveBitmapCol++;
                                }
                            }
                        }
                    }
                } else {
                    collectiveBitmap.readBitmap(true, 0, false, false, null, null, null, bmSize);
                }
                int x2 = 0;
                for (int j3 = i12; j3 < i12; j3++) {
                    bitmaps[numberOfInputSymbols + j3] = collectiveBitmap.getSlice(x2, 0, deltaWidths[j3], deltaHeight);
                    x2 += deltaWidths[j3];
                }
            }
        }
        this.bitmaps = new JBIG2Bitmap[this.noOfExportedSymbols];
        int i13 = 0;
        int j4 = 0;
        boolean z2 = false;
        while (true) {
            boolean export = z2;
            if (i13 >= numberOfInputSymbols + this.noOfNewSymbols) {
                break;
            }
            if (sdHuffman) {
                run = this.huffmanDecoder.decodeInt(HuffmanDecoder.huffmanTableA).intResult();
            } else {
                run = this.arithmeticDecoder.decodeInt(this.arithmeticDecoder.iaexStats).intResult();
            }
            if (export) {
                for (int cnt = 0; cnt < run; cnt++) {
                    int i14 = j4;
                    j4++;
                    int i15 = i13;
                    i13++;
                    this.bitmaps[i14] = bitmaps[i15];
                }
            } else {
                i13 += run;
            }
            z2 = !export;
        }
        int contextRetained = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.BITMAP_CC_RETAINED);
        if (!sdHuffman && contextRetained == 1) {
            this.genericRegionStats = this.genericRegionStats.copy();
            if (sdRefinementAggregate == 1) {
                this.refinementRegionStats = this.refinementRegionStats.copy();
            }
        }
        this.decoder.consumeRemainingBits();
    }

    private void readSymbolDictionaryFlags() throws IOException {
        short[] symbolDictionaryFlagsField = new short[2];
        this.decoder.readByte(symbolDictionaryFlagsField);
        int flags = BinaryOperation.getInt16(symbolDictionaryFlagsField);
        this.symbolDictionaryFlags.setFlags(flags);
        if (JBIG2StreamDecoder.debug) {
            System.out.println("symbolDictionaryFlags = " + flags);
        }
        int sdHuff = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_HUFF);
        int sdTemplate = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_TEMPLATE);
        if (sdHuff == 0) {
            if (sdTemplate == 0) {
                this.symbolDictionaryAdaptiveTemplateX[0] = readATValue();
                this.symbolDictionaryAdaptiveTemplateY[0] = readATValue();
                this.symbolDictionaryAdaptiveTemplateX[1] = readATValue();
                this.symbolDictionaryAdaptiveTemplateY[1] = readATValue();
                this.symbolDictionaryAdaptiveTemplateX[2] = readATValue();
                this.symbolDictionaryAdaptiveTemplateY[2] = readATValue();
                this.symbolDictionaryAdaptiveTemplateX[3] = readATValue();
                this.symbolDictionaryAdaptiveTemplateY[3] = readATValue();
            } else {
                this.symbolDictionaryAdaptiveTemplateX[0] = readATValue();
                this.symbolDictionaryAdaptiveTemplateY[0] = readATValue();
            }
        }
        int refAgg = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_REF_AGG);
        int sdrTemplate = this.symbolDictionaryFlags.getFlagValue(SymbolDictionaryFlags.SD_R_TEMPLATE);
        if (refAgg != 0 && sdrTemplate == 0) {
            this.symbolDictionaryRAdaptiveTemplateX[0] = readATValue();
            this.symbolDictionaryRAdaptiveTemplateY[0] = readATValue();
            this.symbolDictionaryRAdaptiveTemplateX[1] = readATValue();
            this.symbolDictionaryRAdaptiveTemplateY[1] = readATValue();
        }
        short[] noOfExportedSymbolsField = new short[4];
        this.decoder.readByte(noOfExportedSymbolsField);
        int noOfExportedSymbols = BinaryOperation.getInt32(noOfExportedSymbolsField);
        this.noOfExportedSymbols = noOfExportedSymbols;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("noOfExportedSymbols = " + noOfExportedSymbols);
        }
        short[] noOfNewSymbolsField = new short[4];
        this.decoder.readByte(noOfNewSymbolsField);
        int noOfNewSymbols = BinaryOperation.getInt32(noOfNewSymbolsField);
        this.noOfNewSymbols = noOfNewSymbols;
        if (JBIG2StreamDecoder.debug) {
            System.out.println("noOfNewSymbols = " + noOfNewSymbols);
        }
    }

    public int getNoOfExportedSymbols() {
        return this.noOfExportedSymbols;
    }

    public void setNoOfExportedSymbols(int noOfExportedSymbols) {
        this.noOfExportedSymbols = noOfExportedSymbols;
    }

    public int getNoOfNewSymbols() {
        return this.noOfNewSymbols;
    }

    public void setNoOfNewSymbols(int noOfNewSymbols) {
        this.noOfNewSymbols = noOfNewSymbols;
    }

    public JBIG2Bitmap[] getBitmaps() {
        return this.bitmaps;
    }

    public SymbolDictionaryFlags getSymbolDictionaryFlags() {
        return this.symbolDictionaryFlags;
    }

    public void setSymbolDictionaryFlags(SymbolDictionaryFlags symbolDictionaryFlags) {
        this.symbolDictionaryFlags = symbolDictionaryFlags;
    }

    private ArithmeticDecoderStats getGenericRegionStats() {
        return this.genericRegionStats;
    }

    private void setGenericRegionStats(ArithmeticDecoderStats genericRegionStats) {
        this.genericRegionStats = genericRegionStats;
    }

    private void setRefinementRegionStats(ArithmeticDecoderStats refinementRegionStats) {
        this.refinementRegionStats = refinementRegionStats;
    }

    private ArithmeticDecoderStats getRefinementRegionStats() {
        return this.refinementRegionStats;
    }
}
