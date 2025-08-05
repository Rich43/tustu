package org.icepdf.core.pobjects.filters;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/filters/G4State.class */
class G4State {
    int[] ref;
    int[] cur;
    boolean white = true;
    int a0 = 0;
    int b1;
    int refIndex;
    int curIndex;
    int runLength;
    int width;
    int longrun;

    G4State(int w2) {
        this.width = w2;
        this.ref = new int[this.width + 1];
        this.cur = new int[this.width + 1];
        this.b1 = this.width;
        this.ref[0] = this.width;
        this.ref[1] = 0;
        this.runLength = 0;
        this.longrun = 0;
        this.refIndex = 1;
        this.curIndex = 0;
    }
}
