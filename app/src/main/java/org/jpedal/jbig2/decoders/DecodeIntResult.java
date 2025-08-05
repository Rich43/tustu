package org.jpedal.jbig2.decoders;

/* loaded from: icepdf-core.jar:org/jpedal/jbig2/decoders/DecodeIntResult.class */
public class DecodeIntResult {
    private int intResult;
    private boolean booleanResult;

    public DecodeIntResult(int intResult, boolean booleanResult) {
        this.intResult = intResult;
        this.booleanResult = booleanResult;
    }

    public int intResult() {
        return this.intResult;
    }

    public boolean booleanResult() {
        return this.booleanResult;
    }
}
