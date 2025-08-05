package org.jpedal.jbig2.decoders;

import android.R;
import java.io.IOException;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.jpedal.jbig2.io.StreamReader;
import org.jpedal.jbig2.util.BinaryOperation;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/decoders/ArithmeticDecoder.class */
public class ArithmeticDecoder {
    private StreamReader reader;
    public ArithmeticDecoderStats genericRegionStats;
    public ArithmeticDecoderStats refinementRegionStats;
    public ArithmeticDecoderStats iadhStats;
    public ArithmeticDecoderStats iadwStats;
    public ArithmeticDecoderStats iaexStats;
    public ArithmeticDecoderStats iaaiStats;
    public ArithmeticDecoderStats iadtStats;
    public ArithmeticDecoderStats iaitStats;
    public ArithmeticDecoderStats iafsStats;
    public ArithmeticDecoderStats iadsStats;
    public ArithmeticDecoderStats iardxStats;
    public ArithmeticDecoderStats iardyStats;
    public ArithmeticDecoderStats iardwStats;
    public ArithmeticDecoderStats iardhStats;
    public ArithmeticDecoderStats iariStats;
    public ArithmeticDecoderStats iaidStats;
    int[] contextSize;
    int[] referredToContextSize;
    long buffer0;
    long buffer1;

    /* renamed from: c, reason: collision with root package name */
    long f13135c;

    /* renamed from: a, reason: collision with root package name */
    long f13136a;
    long previous;
    int counter;
    int[] qeTable;
    int[] nmpsTable;
    int[] nlpsTable;
    int[] switchTable;

    private ArithmeticDecoder() {
        this.contextSize = new int[]{16, 13, 10, 10};
        this.referredToContextSize = new int[]{13, 10};
        this.qeTable = new int[]{1442906112, 872480768, 402718720, 180420608, 86048768, 35717120, 1442906112, 1409351680, 1208025088, 939589632, 805371904, 604045312, 469827584, 369164288, 1442906112, 1409351680, 1359020032, 1208025088, 939589632, 872480768, 805371904, 671154176, 604045312, 570490880, 469827584, 402718720, 369164288, 335609856, 302055424, 285278208, 180420608, 163643392, 144769024, 86048768, 71368704, 44105728, 35717120, 21037056, R.bool.config_sendPackageName, 8716288, 4784128, 2424832, 1376256, 589824, Opcodes.ASM5, 65536, 1442906112};
        this.nmpsTable = new int[]{1, 2, 3, 4, 5, 38, 7, 8, 9, 10, 11, 12, 13, 29, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 45, 46};
        this.nlpsTable = new int[]{1, 6, 9, 12, 29, 33, 6, 14, 14, 14, 17, 18, 20, 21, 14, 14, 15, 16, 17, 18, 19, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 46};
        this.switchTable = new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    }

    public ArithmeticDecoder(StreamReader reader) {
        this.contextSize = new int[]{16, 13, 10, 10};
        this.referredToContextSize = new int[]{13, 10};
        this.qeTable = new int[]{1442906112, 872480768, 402718720, 180420608, 86048768, 35717120, 1442906112, 1409351680, 1208025088, 939589632, 805371904, 604045312, 469827584, 369164288, 1442906112, 1409351680, 1359020032, 1208025088, 939589632, 872480768, 805371904, 671154176, 604045312, 570490880, 469827584, 402718720, 369164288, 335609856, 302055424, 285278208, 180420608, 163643392, 144769024, 86048768, 71368704, 44105728, 35717120, 21037056, R.bool.config_sendPackageName, 8716288, 4784128, 2424832, 1376256, 589824, Opcodes.ASM5, 65536, 1442906112};
        this.nmpsTable = new int[]{1, 2, 3, 4, 5, 38, 7, 8, 9, 10, 11, 12, 13, 29, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 45, 46};
        this.nlpsTable = new int[]{1, 6, 9, 12, 29, 33, 6, 14, 14, 14, 17, 18, 20, 21, 14, 14, 15, 16, 17, 18, 19, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 46};
        this.switchTable = new int[]{1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        this.reader = reader;
        this.genericRegionStats = new ArithmeticDecoderStats(2);
        this.refinementRegionStats = new ArithmeticDecoderStats(2);
        this.iadhStats = new ArithmeticDecoderStats(512);
        this.iadwStats = new ArithmeticDecoderStats(512);
        this.iaexStats = new ArithmeticDecoderStats(512);
        this.iaaiStats = new ArithmeticDecoderStats(512);
        this.iadtStats = new ArithmeticDecoderStats(512);
        this.iaitStats = new ArithmeticDecoderStats(512);
        this.iafsStats = new ArithmeticDecoderStats(512);
        this.iadsStats = new ArithmeticDecoderStats(512);
        this.iardxStats = new ArithmeticDecoderStats(512);
        this.iardyStats = new ArithmeticDecoderStats(512);
        this.iardwStats = new ArithmeticDecoderStats(512);
        this.iardhStats = new ArithmeticDecoderStats(512);
        this.iariStats = new ArithmeticDecoderStats(512);
        this.iaidStats = new ArithmeticDecoderStats(2);
    }

    public void resetIntStats(int symbolCodeLength) {
        this.iadhStats.reset();
        this.iadwStats.reset();
        this.iaexStats.reset();
        this.iaaiStats.reset();
        this.iadtStats.reset();
        this.iaitStats.reset();
        this.iafsStats.reset();
        this.iadsStats.reset();
        this.iardxStats.reset();
        this.iardyStats.reset();
        this.iardwStats.reset();
        this.iardhStats.reset();
        this.iariStats.reset();
        if (this.iaidStats.getContextSize() == (1 << (symbolCodeLength + 1))) {
            this.iaidStats.reset();
        } else {
            this.iaidStats = new ArithmeticDecoderStats(1 << (symbolCodeLength + 1));
        }
    }

    public void resetGenericStats(int template, ArithmeticDecoderStats previousStats) {
        int size = this.contextSize[template];
        if (previousStats != null && previousStats.getContextSize() == size) {
            if (this.genericRegionStats.getContextSize() == size) {
                this.genericRegionStats.overwrite(previousStats);
                return;
            } else {
                this.genericRegionStats = previousStats.copy();
                return;
            }
        }
        if (this.genericRegionStats.getContextSize() == size) {
            this.genericRegionStats.reset();
        } else {
            this.genericRegionStats = new ArithmeticDecoderStats(1 << size);
        }
    }

    public void resetRefinementStats(int template, ArithmeticDecoderStats previousStats) {
        int size = this.referredToContextSize[template];
        if (previousStats != null && previousStats.getContextSize() == size) {
            if (this.refinementRegionStats.getContextSize() == size) {
                this.refinementRegionStats.overwrite(previousStats);
                return;
            } else {
                this.refinementRegionStats = previousStats.copy();
                return;
            }
        }
        if (this.refinementRegionStats.getContextSize() == size) {
            this.refinementRegionStats.reset();
        } else {
            this.refinementRegionStats = new ArithmeticDecoderStats(1 << size);
        }
    }

    public void start() throws IOException {
        this.buffer0 = this.reader.readByte();
        this.buffer1 = this.reader.readByte();
        this.f13135c = BinaryOperation.bit32Shift(this.buffer0 ^ 255, 16, 0);
        readByte();
        this.f13135c = BinaryOperation.bit32Shift(this.f13135c, 7, 0);
        this.counter -= 7;
        this.f13136a = 2147483648L;
    }

    public DecodeIntResult decodeInt(ArithmeticDecoderStats stats) throws IOException {
        long value;
        int decodedInt;
        this.previous = 1L;
        int s2 = decodeIntBit(stats);
        if (decodeIntBit(stats) != 0) {
            if (decodeIntBit(stats) != 0) {
                if (decodeIntBit(stats) != 0) {
                    if (decodeIntBit(stats) != 0) {
                        if (decodeIntBit(stats) != 0) {
                            long value2 = 0;
                            for (int i2 = 0; i2 < 32; i2++) {
                                value2 = BinaryOperation.bit32Shift(value2, 1, 0) | decodeIntBit(stats);
                            }
                            value = value2 + 4436;
                        } else {
                            long value3 = 0;
                            for (int i3 = 0; i3 < 12; i3++) {
                                value3 = BinaryOperation.bit32Shift(value3, 1, 0) | decodeIntBit(stats);
                            }
                            value = value3 + 340;
                        }
                    } else {
                        long value4 = 0;
                        for (int i4 = 0; i4 < 8; i4++) {
                            value4 = BinaryOperation.bit32Shift(value4, 1, 0) | decodeIntBit(stats);
                        }
                        value = value4 + 84;
                    }
                } else {
                    long value5 = 0;
                    for (int i5 = 0; i5 < 6; i5++) {
                        value5 = BinaryOperation.bit32Shift(value5, 1, 0) | decodeIntBit(stats);
                    }
                    value = value5 + 20;
                }
            } else {
                long value6 = decodeIntBit(stats);
                value = (BinaryOperation.bit32Shift(BinaryOperation.bit32Shift(BinaryOperation.bit32Shift(value6, 1, 0) | decodeIntBit(stats), 1, 0) | decodeIntBit(stats), 1, 0) | decodeIntBit(stats)) + 4;
            }
        } else {
            long value7 = decodeIntBit(stats);
            value = BinaryOperation.bit32Shift(value7, 1, 0) | decodeIntBit(stats);
        }
        if (s2 != 0) {
            if (value == 0) {
                return new DecodeIntResult((int) value, false);
            }
            decodedInt = (int) (-value);
        } else {
            decodedInt = (int) value;
        }
        return new DecodeIntResult(decodedInt, true);
    }

    public long decodeIAID(long codeLen, ArithmeticDecoderStats stats) throws IOException {
        this.previous = 1L;
        long j2 = 0;
        while (true) {
            long i2 = j2;
            if (i2 < codeLen) {
                int bit = decodeBit(this.previous, stats);
                this.previous = BinaryOperation.bit32Shift(this.previous, 1, 0) | bit;
                j2 = i2 + 1;
            } else {
                return this.previous - (1 << ((int) codeLen));
            }
        }
    }

    public int decodeBit(long context, ArithmeticDecoderStats stats) throws IOException {
        int bit;
        int iCX = BinaryOperation.bit8Shift(stats.getContextCodingTableValue((int) context), 1, 1);
        int mpsCX = stats.getContextCodingTableValue((int) context) & 1;
        int qe = this.qeTable[iCX];
        this.f13136a -= qe;
        if (this.f13135c >= this.f13136a) {
            this.f13135c -= this.f13136a;
            if (this.f13136a < qe) {
                bit = mpsCX;
                stats.setContextCodingTableValue((int) context, (this.nmpsTable[iCX] << 1) | mpsCX);
            } else {
                bit = 1 - mpsCX;
                if (this.switchTable[iCX] != 0) {
                    stats.setContextCodingTableValue((int) context, (this.nlpsTable[iCX] << 1) | (1 - mpsCX));
                } else {
                    stats.setContextCodingTableValue((int) context, (this.nlpsTable[iCX] << 1) | mpsCX);
                }
            }
            this.f13136a = qe;
            do {
                if (this.counter == 0) {
                    readByte();
                }
                this.f13136a = BinaryOperation.bit32Shift(this.f13136a, 1, 0);
                this.f13135c = BinaryOperation.bit32Shift(this.f13135c, 1, 0);
                this.counter--;
            } while ((this.f13136a & (-2147483648L)) == 0);
        } else if ((this.f13136a & (-2147483648L)) != 0) {
            bit = mpsCX;
        } else {
            if (this.f13136a < qe) {
                bit = 1 - mpsCX;
                if (this.switchTable[iCX] != 0) {
                    stats.setContextCodingTableValue((int) context, (this.nlpsTable[iCX] << 1) | (1 - mpsCX));
                } else {
                    stats.setContextCodingTableValue((int) context, (this.nlpsTable[iCX] << 1) | mpsCX);
                }
            } else {
                bit = mpsCX;
                stats.setContextCodingTableValue((int) context, (this.nmpsTable[iCX] << 1) | mpsCX);
            }
            do {
                if (this.counter == 0) {
                    readByte();
                }
                this.f13136a = BinaryOperation.bit32Shift(this.f13136a, 1, 0);
                this.f13135c = BinaryOperation.bit32Shift(this.f13135c, 1, 0);
                this.counter--;
            } while ((this.f13136a & (-2147483648L)) == 0);
        }
        return bit;
    }

    private void readByte() throws IOException {
        if (this.buffer0 != 255) {
            this.buffer0 = this.buffer1;
            this.buffer1 = this.reader.readByte();
            this.f13135c = (this.f13135c + 65280) - BinaryOperation.bit32Shift(this.buffer0, 8, 0);
            this.counter = 8;
            return;
        }
        if (this.buffer1 > 143) {
            this.counter = 8;
            return;
        }
        this.buffer0 = this.buffer1;
        this.buffer1 = this.reader.readByte();
        this.f13135c = (this.f13135c + 65024) - BinaryOperation.bit32Shift(this.buffer0, 9, 0);
        this.counter = 7;
    }

    private int decodeIntBit(ArithmeticDecoderStats stats) throws IOException {
        int bit = decodeBit(this.previous, stats);
        if (this.previous < 256) {
            this.previous = BinaryOperation.bit32Shift(this.previous, 1, 0) | bit;
        } else {
            this.previous = ((BinaryOperation.bit32Shift(this.previous, 1, 0) | bit) & 511) | 256;
        }
        return bit;
    }
}
