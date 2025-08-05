package org.jpedal.jbig2.decoders;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/decoders/ArithmeticDecoderStats.class */
public class ArithmeticDecoderStats {
    private int contextSize;
    private int[] codingContextTable;

    public ArithmeticDecoderStats(int contextSize) {
        this.contextSize = contextSize;
        this.codingContextTable = new int[contextSize];
        reset();
    }

    public void reset() {
        for (int i2 = 0; i2 < this.contextSize; i2++) {
            this.codingContextTable[i2] = 0;
        }
    }

    public void setEntry(int codingContext, int i2, int moreProbableSymbol) {
        this.codingContextTable[codingContext] = (i2 << i2) + moreProbableSymbol;
    }

    public int getContextCodingTableValue(int index) {
        return this.codingContextTable[index];
    }

    public void setContextCodingTableValue(int index, int value) {
        this.codingContextTable[index] = value;
    }

    public int getContextSize() {
        return this.contextSize;
    }

    public void overwrite(ArithmeticDecoderStats stats) {
        System.arraycopy(stats.codingContextTable, 0, this.codingContextTable, 0, this.contextSize);
    }

    public ArithmeticDecoderStats copy() {
        ArithmeticDecoderStats stats = new ArithmeticDecoderStats(this.contextSize);
        System.arraycopy(this.codingContextTable, 0, stats.codingContextTable, 0, this.contextSize);
        return stats;
    }
}
