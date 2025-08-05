package sun.text.bidi;

/* loaded from: rt.jar:sun/text/bidi/BidiRun.class */
public class BidiRun {
    int start;
    int limit;
    int insertRemove;
    byte level;

    BidiRun() {
        this(0, 0, (byte) 0);
    }

    BidiRun(int i2, int i3, byte b2) {
        this.start = i2;
        this.limit = i3;
        this.level = b2;
    }

    void copyFrom(BidiRun bidiRun) {
        this.start = bidiRun.start;
        this.limit = bidiRun.limit;
        this.level = bidiRun.level;
        this.insertRemove = bidiRun.insertRemove;
    }

    public byte getEmbeddingLevel() {
        return this.level;
    }

    boolean isEvenRun() {
        return (this.level & 1) == 0;
    }
}
