package com.sun.openpisces;

import com.sun.javafx.geom.PathConsumer2D;
import com.sun.javafx.geom.transform.BaseTransform;

/* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D.class */
public abstract class TransformingPathConsumer2D implements PathConsumer2D {
    protected PathConsumer2D out;

    public TransformingPathConsumer2D(PathConsumer2D out) {
        this.out = out;
    }

    public void setConsumer(PathConsumer2D out) {
        this.out = out;
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D$FilterSet.class */
    public static final class FilterSet {
        private TranslateFilter translater;
        private DeltaScaleFilter deltascaler;
        private ScaleTranslateFilter scaletranslater;
        private DeltaTransformFilter deltatransformer;
        private TransformFilter transformer;

        public PathConsumer2D getConsumer(PathConsumer2D out, BaseTransform tx) {
            if (tx == null) {
                return out;
            }
            float Mxx = (float) tx.getMxx();
            float Mxy = (float) tx.getMxy();
            float Mxt = (float) tx.getMxt();
            float Myx = (float) tx.getMyx();
            float Myy = (float) tx.getMyy();
            float Myt = (float) tx.getMyt();
            if (Mxy != 0.0f || Myx != 0.0f) {
                if (Mxt == 0.0f && Myt == 0.0f) {
                    if (this.deltatransformer == null) {
                        this.deltatransformer = new DeltaTransformFilter(out, Mxx, Mxy, Myx, Myy);
                    } else {
                        this.deltatransformer.set(Mxx, Mxy, Myx, Myy);
                    }
                    return this.deltatransformer;
                }
                if (this.transformer == null) {
                    this.transformer = new TransformFilter(out, Mxx, Mxy, Mxt, Myx, Myy, Myt);
                } else {
                    this.transformer.set(Mxx, Mxy, Mxt, Myx, Myy, Myt);
                }
                return this.transformer;
            }
            if (Mxx == 1.0f && Myy == 1.0f) {
                if (Mxt == 0.0f && Myt == 0.0f) {
                    return out;
                }
                if (this.translater == null) {
                    this.translater = new TranslateFilter(out, Mxt, Myt);
                } else {
                    this.translater.set(Mxt, Myt);
                }
                return this.translater;
            }
            if (Mxt == 0.0f && Myt == 0.0f) {
                if (this.deltascaler == null) {
                    this.deltascaler = new DeltaScaleFilter(out, Mxx, Myy);
                } else {
                    this.deltascaler.set(Mxx, Myy);
                }
                return this.deltascaler;
            }
            if (this.scaletranslater == null) {
                this.scaletranslater = new ScaleTranslateFilter(out, Mxx, Myy, Mxt, Myt);
            } else {
                this.scaletranslater.set(Mxx, Myy, Mxt, Myt);
            }
            return this.scaletranslater;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D$TranslateFilter.class */
    static final class TranslateFilter extends TransformingPathConsumer2D {
        private float tx;
        private float ty;

        TranslateFilter(PathConsumer2D out, float tx, float ty) {
            super(out);
            set(tx, ty);
        }

        public void set(float tx, float ty) {
            this.tx = tx;
            this.ty = ty;
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void moveTo(float x0, float y0) {
            this.out.moveTo(x0 + this.tx, y0 + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void lineTo(float x1, float y1) {
            this.out.lineTo(x1 + this.tx, y1 + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void quadTo(float x1, float y1, float x2, float y2) {
            this.out.quadTo(x1 + this.tx, y1 + this.ty, x2 + this.tx, y2 + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            this.out.curveTo(x1 + this.tx, y1 + this.ty, x2 + this.tx, y2 + this.ty, x3 + this.tx, y3 + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D$ScaleTranslateFilter.class */
    static final class ScaleTranslateFilter extends TransformingPathConsumer2D {
        private float sx;
        private float sy;
        private float tx;
        private float ty;

        ScaleTranslateFilter(PathConsumer2D out, float sx, float sy, float tx, float ty) {
            super(out);
            set(sx, sy, tx, ty);
        }

        public void set(float sx, float sy, float tx, float ty) {
            this.sx = sx;
            this.sy = sy;
            this.tx = tx;
            this.ty = ty;
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void moveTo(float x0, float y0) {
            this.out.moveTo((x0 * this.sx) + this.tx, (y0 * this.sy) + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void lineTo(float x1, float y1) {
            this.out.lineTo((x1 * this.sx) + this.tx, (y1 * this.sy) + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void quadTo(float x1, float y1, float x2, float y2) {
            this.out.quadTo((x1 * this.sx) + this.tx, (y1 * this.sy) + this.ty, (x2 * this.sx) + this.tx, (y2 * this.sy) + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            this.out.curveTo((x1 * this.sx) + this.tx, (y1 * this.sy) + this.ty, (x2 * this.sx) + this.tx, (y2 * this.sy) + this.ty, (x3 * this.sx) + this.tx, (y3 * this.sy) + this.ty);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D$TransformFilter.class */
    static final class TransformFilter extends TransformingPathConsumer2D {
        private float Mxx;
        private float Mxy;
        private float Mxt;
        private float Myx;
        private float Myy;
        private float Myt;

        TransformFilter(PathConsumer2D out, float Mxx, float Mxy, float Mxt, float Myx, float Myy, float Myt) {
            super(out);
            set(Mxx, Mxy, Mxt, Myx, Myy, Myt);
        }

        public void set(float Mxx, float Mxy, float Mxt, float Myx, float Myy, float Myt) {
            this.Mxx = Mxx;
            this.Mxy = Mxy;
            this.Mxt = Mxt;
            this.Myx = Myx;
            this.Myy = Myy;
            this.Myt = Myt;
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void moveTo(float x0, float y0) {
            this.out.moveTo((x0 * this.Mxx) + (y0 * this.Mxy) + this.Mxt, (x0 * this.Myx) + (y0 * this.Myy) + this.Myt);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void lineTo(float x1, float y1) {
            this.out.lineTo((x1 * this.Mxx) + (y1 * this.Mxy) + this.Mxt, (x1 * this.Myx) + (y1 * this.Myy) + this.Myt);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void quadTo(float x1, float y1, float x2, float y2) {
            this.out.quadTo((x1 * this.Mxx) + (y1 * this.Mxy) + this.Mxt, (x1 * this.Myx) + (y1 * this.Myy) + this.Myt, (x2 * this.Mxx) + (y2 * this.Mxy) + this.Mxt, (x2 * this.Myx) + (y2 * this.Myy) + this.Myt);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            this.out.curveTo((x1 * this.Mxx) + (y1 * this.Mxy) + this.Mxt, (x1 * this.Myx) + (y1 * this.Myy) + this.Myt, (x2 * this.Mxx) + (y2 * this.Mxy) + this.Mxt, (x2 * this.Myx) + (y2 * this.Myy) + this.Myt, (x3 * this.Mxx) + (y3 * this.Mxy) + this.Mxt, (x3 * this.Myx) + (y3 * this.Myy) + this.Myt);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D$DeltaScaleFilter.class */
    static final class DeltaScaleFilter extends TransformingPathConsumer2D {
        private float sx;
        private float sy;

        public DeltaScaleFilter(PathConsumer2D out, float Mxx, float Myy) {
            super(out);
            set(Mxx, Myy);
        }

        public void set(float Mxx, float Myy) {
            this.sx = Mxx;
            this.sy = Myy;
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void moveTo(float x0, float y0) {
            this.out.moveTo(x0 * this.sx, y0 * this.sy);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void lineTo(float x1, float y1) {
            this.out.lineTo(x1 * this.sx, y1 * this.sy);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void quadTo(float x1, float y1, float x2, float y2) {
            this.out.quadTo(x1 * this.sx, y1 * this.sy, x2 * this.sx, y2 * this.sy);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            this.out.curveTo(x1 * this.sx, y1 * this.sy, x2 * this.sx, y2 * this.sy, x3 * this.sx, y3 * this.sy);
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/openpisces/TransformingPathConsumer2D$DeltaTransformFilter.class */
    static final class DeltaTransformFilter extends TransformingPathConsumer2D {
        private float Mxx;
        private float Mxy;
        private float Myx;
        private float Myy;

        DeltaTransformFilter(PathConsumer2D out, float Mxx, float Mxy, float Myx, float Myy) {
            super(out);
            set(Mxx, Mxy, Myx, Myy);
        }

        public void set(float Mxx, float Mxy, float Myx, float Myy) {
            this.Mxx = Mxx;
            this.Mxy = Mxy;
            this.Myx = Myx;
            this.Myy = Myy;
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void moveTo(float x0, float y0) {
            this.out.moveTo((x0 * this.Mxx) + (y0 * this.Mxy), (x0 * this.Myx) + (y0 * this.Myy));
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void lineTo(float x1, float y1) {
            this.out.lineTo((x1 * this.Mxx) + (y1 * this.Mxy), (x1 * this.Myx) + (y1 * this.Myy));
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void quadTo(float x1, float y1, float x2, float y2) {
            this.out.quadTo((x1 * this.Mxx) + (y1 * this.Mxy), (x1 * this.Myx) + (y1 * this.Myy), (x2 * this.Mxx) + (y2 * this.Mxy), (x2 * this.Myx) + (y2 * this.Myy));
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) {
            this.out.curveTo((x1 * this.Mxx) + (y1 * this.Mxy), (x1 * this.Myx) + (y1 * this.Myy), (x2 * this.Mxx) + (y2 * this.Mxy), (x2 * this.Myx) + (y2 * this.Myy), (x3 * this.Mxx) + (y3 * this.Mxy), (x3 * this.Myx) + (y3 * this.Myy));
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // com.sun.javafx.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        public long getNativeConsumer() {
            return 0L;
        }
    }
}
