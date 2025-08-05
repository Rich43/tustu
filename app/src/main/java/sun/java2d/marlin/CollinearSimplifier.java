package sun.java2d.marlin;

import sun.awt.geom.PathConsumer2D;

/* loaded from: rt.jar:sun/java2d/marlin/CollinearSimplifier.class */
final class CollinearSimplifier implements PathConsumer2D {
    static final float EPS = 1.0E-4f;
    PathConsumer2D delegate;
    SimplifierState state;
    float px1;
    float py1;
    float px2;
    float py2;
    float pslope;

    /* loaded from: rt.jar:sun/java2d/marlin/CollinearSimplifier$SimplifierState.class */
    enum SimplifierState {
        Empty,
        PreviousPoint,
        PreviousLine
    }

    CollinearSimplifier() {
    }

    public CollinearSimplifier init(PathConsumer2D pathConsumer2D) {
        this.delegate = pathConsumer2D;
        this.state = SimplifierState.Empty;
        return this;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void pathDone() {
        emitStashedLine();
        this.state = SimplifierState.Empty;
        this.delegate.pathDone();
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void closePath() {
        emitStashedLine();
        this.state = SimplifierState.Empty;
        this.delegate.closePath();
    }

    @Override // sun.awt.geom.PathConsumer2D
    public long getNativeConsumer() {
        return 0L;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void quadTo(float f2, float f3, float f4, float f5) {
        emitStashedLine();
        this.delegate.quadTo(f2, f3, f4, f5);
        this.state = SimplifierState.PreviousPoint;
        this.px1 = f4;
        this.py1 = f5;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
        emitStashedLine();
        this.delegate.curveTo(f2, f3, f4, f5, f6, f7);
        this.state = SimplifierState.PreviousPoint;
        this.px1 = f6;
        this.py1 = f7;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void moveTo(float f2, float f3) {
        emitStashedLine();
        this.delegate.moveTo(f2, f3);
        this.state = SimplifierState.PreviousPoint;
        this.px1 = f2;
        this.py1 = f3;
    }

    @Override // sun.awt.geom.PathConsumer2D
    public void lineTo(float f2, float f3) {
        switch (this.state) {
            case Empty:
                this.delegate.lineTo(f2, f3);
                this.state = SimplifierState.PreviousPoint;
                this.px1 = f2;
                this.py1 = f3;
                break;
            case PreviousPoint:
                this.state = SimplifierState.PreviousLine;
                this.px2 = f2;
                this.py2 = f3;
                this.pslope = getSlope(this.px1, this.py1, f2, f3);
                break;
            case PreviousLine:
                float slope = getSlope(this.px2, this.py2, f2, f3);
                if (slope == this.pslope || Math.abs(this.pslope - slope) < EPS) {
                    this.px2 = f2;
                    this.py2 = f3;
                    break;
                } else {
                    this.delegate.lineTo(this.px2, this.py2);
                    this.px1 = this.px2;
                    this.py1 = this.py2;
                    this.px2 = f2;
                    this.py2 = f3;
                    this.pslope = slope;
                    break;
                }
                break;
        }
    }

    private void emitStashedLine() {
        if (this.state == SimplifierState.PreviousLine) {
            this.delegate.lineTo(this.px2, this.py2);
        }
    }

    private static float getSlope(float f2, float f3, float f4, float f5) {
        float f6 = f5 - f3;
        if (f6 == 0.0f) {
            return f4 > f2 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
        }
        return (f4 - f2) / f6;
    }
}
