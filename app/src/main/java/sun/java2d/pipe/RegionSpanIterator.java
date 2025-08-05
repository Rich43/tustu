package sun.java2d.pipe;

/* loaded from: rt.jar:sun/java2d/pipe/RegionSpanIterator.class */
public class RegionSpanIterator implements SpanIterator {
    RegionIterator ri;
    int lox;
    int loy;
    int hix;
    int hiy;
    int curloy;
    int curhiy;
    boolean done = false;
    boolean isrect;

    public RegionSpanIterator(Region region) {
        int[] iArr = new int[4];
        region.getBounds(iArr);
        this.lox = iArr[0];
        this.loy = iArr[1];
        this.hix = iArr[2];
        this.hiy = iArr[3];
        this.isrect = region.isRectangular();
        this.ri = region.getIterator();
    }

    @Override // sun.java2d.pipe.SpanIterator
    public void getPathBox(int[] iArr) {
        iArr[0] = this.lox;
        iArr[1] = this.loy;
        iArr[2] = this.hix;
        iArr[3] = this.hiy;
    }

    @Override // sun.java2d.pipe.SpanIterator
    public void intersectClipBox(int i2, int i3, int i4, int i5) {
        if (i2 > this.lox) {
            this.lox = i2;
        }
        if (i3 > this.loy) {
            this.loy = i3;
        }
        if (i4 < this.hix) {
            this.hix = i4;
        }
        if (i5 < this.hiy) {
            this.hiy = i5;
        }
        this.done = this.lox >= this.hix || this.loy >= this.hiy;
    }

    @Override // sun.java2d.pipe.SpanIterator
    public boolean nextSpan(int[] iArr) {
        if (this.done) {
            return false;
        }
        if (this.isrect) {
            getPathBox(iArr);
            this.done = true;
            return true;
        }
        int i2 = this.curloy;
        int i3 = this.curhiy;
        while (true) {
            if (!this.ri.nextXBand(iArr)) {
                if (!this.ri.nextYRange(iArr)) {
                    this.done = true;
                    return false;
                }
                i2 = iArr[1];
                i3 = iArr[3];
                if (i2 < this.loy) {
                    i2 = this.loy;
                }
                if (i3 > this.hiy) {
                    i3 = this.hiy;
                }
                if (i2 >= this.hiy) {
                    this.done = true;
                    return false;
                }
            } else {
                int i4 = iArr[0];
                int i5 = iArr[2];
                if (i4 < this.lox) {
                    i4 = this.lox;
                }
                if (i5 > this.hix) {
                    i5 = this.hix;
                }
                if (i4 < i5 && i2 < i3) {
                    iArr[0] = i4;
                    int i6 = i2;
                    this.curloy = i6;
                    iArr[1] = i6;
                    iArr[2] = i5;
                    int i7 = i3;
                    this.curhiy = i7;
                    iArr[3] = i7;
                    return true;
                }
            }
        }
    }

    @Override // sun.java2d.pipe.SpanIterator
    public void skipDownTo(int i2) {
        this.loy = i2;
    }

    @Override // sun.java2d.pipe.SpanIterator
    public long getNativeIterator() {
        return 0L;
    }
}
