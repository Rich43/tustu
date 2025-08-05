package sun.java2d.pipe;

/* loaded from: rt.jar:sun/java2d/pipe/RegionClipSpanIterator.class */
public class RegionClipSpanIterator implements SpanIterator {
    Region rgn;
    SpanIterator spanIter;
    RegionIterator resetState;
    RegionIterator lwm;
    RegionIterator row;
    RegionIterator box;
    int spanlox;
    int spanhix;
    int spanloy;
    int spanhiy;
    int lwmloy;
    int lwmhiy;
    int rgnlox;
    int rgnloy;
    int rgnhix;
    int rgnhiy;
    int rgnbndslox;
    int rgnbndsloy;
    int rgnbndshix;
    int rgnbndshiy;
    int[] rgnbox = new int[4];
    int[] spanbox = new int[4];
    boolean doNextSpan;
    boolean doNextBox;
    boolean done;

    public RegionClipSpanIterator(Region region, SpanIterator spanIterator) {
        this.done = false;
        this.spanIter = spanIterator;
        this.resetState = region.getIterator();
        this.lwm = this.resetState.createCopy();
        if (!this.lwm.nextYRange(this.rgnbox)) {
            this.done = true;
            return;
        }
        int i2 = this.rgnbox[1];
        this.lwmloy = i2;
        this.rgnloy = i2;
        int i3 = this.rgnbox[3];
        this.lwmhiy = i3;
        this.rgnhiy = i3;
        region.getBounds(this.rgnbox);
        this.rgnbndslox = this.rgnbox[0];
        this.rgnbndsloy = this.rgnbox[1];
        this.rgnbndshix = this.rgnbox[2];
        this.rgnbndshiy = this.rgnbox[3];
        if (this.rgnbndslox >= this.rgnbndshix || this.rgnbndsloy >= this.rgnbndshiy) {
            this.done = true;
            return;
        }
        this.rgn = region;
        this.row = this.lwm.createCopy();
        this.box = this.row.createCopy();
        this.doNextSpan = true;
        this.doNextBox = false;
    }

    @Override // sun.java2d.pipe.SpanIterator
    public void getPathBox(int[] iArr) {
        int[] iArr2 = new int[4];
        this.rgn.getBounds(iArr2);
        this.spanIter.getPathBox(iArr);
        if (iArr[0] < iArr2[0]) {
            iArr[0] = iArr2[0];
        }
        if (iArr[1] < iArr2[1]) {
            iArr[1] = iArr2[1];
        }
        if (iArr[2] > iArr2[2]) {
            iArr[2] = iArr2[2];
        }
        if (iArr[3] > iArr2[3]) {
            iArr[3] = iArr2[3];
        }
    }

    @Override // sun.java2d.pipe.SpanIterator
    public void intersectClipBox(int i2, int i3, int i4, int i5) {
        this.spanIter.intersectClipBox(i2, i3, i4, i5);
    }

    @Override // sun.java2d.pipe.SpanIterator
    public boolean nextSpan(int[] iArr) {
        int i2;
        int i3;
        int i4;
        int i5;
        if (this.done) {
            return false;
        }
        boolean z2 = false;
        while (true) {
            if (this.doNextSpan) {
                if (!this.spanIter.nextSpan(this.spanbox)) {
                    this.done = true;
                    return false;
                }
                this.spanlox = this.spanbox[0];
                if (this.spanlox < this.rgnbndshix) {
                    this.spanloy = this.spanbox[1];
                    if (this.spanloy < this.rgnbndshiy) {
                        this.spanhix = this.spanbox[2];
                        if (this.spanhix > this.rgnbndslox) {
                            this.spanhiy = this.spanbox[3];
                            if (this.spanhiy > this.rgnbndsloy) {
                                if (this.lwmloy > this.spanloy) {
                                    this.lwm.copyStateFrom(this.resetState);
                                    this.lwm.nextYRange(this.rgnbox);
                                    this.lwmloy = this.rgnbox[1];
                                    this.lwmhiy = this.rgnbox[3];
                                }
                                while (this.lwmhiy <= this.spanloy && this.lwm.nextYRange(this.rgnbox)) {
                                    this.lwmloy = this.rgnbox[1];
                                    this.lwmhiy = this.rgnbox[3];
                                }
                                if (this.lwmhiy > this.spanloy && this.lwmloy < this.spanhiy) {
                                    if (this.rgnloy != this.lwmloy) {
                                        this.row.copyStateFrom(this.lwm);
                                        this.rgnloy = this.lwmloy;
                                        this.rgnhiy = this.lwmhiy;
                                    }
                                    this.box.copyStateFrom(this.row);
                                    this.doNextBox = true;
                                    this.doNextSpan = false;
                                }
                            }
                        }
                    }
                }
            } else if (z2) {
                z2 = false;
                boolean zNextYRange = this.row.nextYRange(this.rgnbox);
                if (zNextYRange) {
                    this.rgnloy = this.rgnbox[1];
                    this.rgnhiy = this.rgnbox[3];
                }
                if (!zNextYRange || this.rgnloy >= this.spanhiy) {
                    this.doNextSpan = true;
                } else {
                    this.box.copyStateFrom(this.row);
                    this.doNextBox = true;
                }
            } else if (this.doNextBox) {
                boolean zNextXBand = this.box.nextXBand(this.rgnbox);
                if (zNextXBand) {
                    this.rgnlox = this.rgnbox[0];
                    this.rgnhix = this.rgnbox[2];
                }
                if (!zNextXBand || this.rgnlox >= this.spanhix) {
                    this.doNextBox = false;
                    if (this.rgnhiy >= this.spanhiy) {
                        this.doNextSpan = true;
                    } else {
                        z2 = true;
                    }
                } else {
                    this.doNextBox = this.rgnhix <= this.spanlox;
                }
            } else {
                this.doNextBox = true;
                if (this.spanlox > this.rgnlox) {
                    i2 = this.spanlox;
                } else {
                    i2 = this.rgnlox;
                }
                if (this.spanloy > this.rgnloy) {
                    i3 = this.spanloy;
                } else {
                    i3 = this.rgnloy;
                }
                if (this.spanhix < this.rgnhix) {
                    i4 = this.spanhix;
                } else {
                    i4 = this.rgnhix;
                }
                if (this.spanhiy < this.rgnhiy) {
                    i5 = this.spanhiy;
                } else {
                    i5 = this.rgnhiy;
                }
                if (i2 < i4 && i3 < i5) {
                    iArr[0] = i2;
                    iArr[1] = i3;
                    iArr[2] = i4;
                    iArr[3] = i5;
                    return true;
                }
            }
        }
    }

    @Override // sun.java2d.pipe.SpanIterator
    public void skipDownTo(int i2) {
        this.spanIter.skipDownTo(i2);
    }

    @Override // sun.java2d.pipe.SpanIterator
    public long getNativeIterator() {
        return 0L;
    }

    protected void finalize() {
    }
}
