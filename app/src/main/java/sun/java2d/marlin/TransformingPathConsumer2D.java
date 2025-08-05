package sun.java2d.marlin;

import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import sun.awt.geom.PathConsumer2D;

/* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D.class */
final class TransformingPathConsumer2D {
    private final Path2DWrapper wp_Path2DWrapper = new Path2DWrapper();
    private final TranslateFilter tx_TranslateFilter = new TranslateFilter();
    private final DeltaScaleFilter tx_DeltaScaleFilter = new DeltaScaleFilter();
    private final ScaleFilter tx_ScaleFilter = new ScaleFilter();
    private final DeltaTransformFilter tx_DeltaTransformFilter = new DeltaTransformFilter();
    private final TransformFilter tx_TransformFilter = new TransformFilter();
    private final DeltaScaleFilter dt_DeltaScaleFilter = new DeltaScaleFilter();
    private final DeltaTransformFilter dt_DeltaTransformFilter = new DeltaTransformFilter();
    private final DeltaScaleFilter iv_DeltaScaleFilter = new DeltaScaleFilter();
    private final DeltaTransformFilter iv_DeltaTransformFilter = new DeltaTransformFilter();

    TransformingPathConsumer2D() {
    }

    PathConsumer2D wrapPath2d(Path2D.Float r4) {
        return this.wp_Path2DWrapper.init(r4);
    }

    PathConsumer2D transformConsumer(PathConsumer2D pathConsumer2D, AffineTransform affineTransform) {
        if (affineTransform == null) {
            return pathConsumer2D;
        }
        float scaleX = (float) affineTransform.getScaleX();
        float shearX = (float) affineTransform.getShearX();
        float translateX = (float) affineTransform.getTranslateX();
        float shearY = (float) affineTransform.getShearY();
        float scaleY = (float) affineTransform.getScaleY();
        float translateY = (float) affineTransform.getTranslateY();
        if (shearX != 0.0f || shearY != 0.0f) {
            if (translateX == 0.0f && translateY == 0.0f) {
                return this.tx_DeltaTransformFilter.init(pathConsumer2D, scaleX, shearX, shearY, scaleY);
            }
            return this.tx_TransformFilter.init(pathConsumer2D, scaleX, shearX, translateX, shearY, scaleY, translateY);
        }
        if (scaleX == 1.0f && scaleY == 1.0f) {
            if (translateX == 0.0f && translateY == 0.0f) {
                return pathConsumer2D;
            }
            return this.tx_TranslateFilter.init(pathConsumer2D, translateX, translateY);
        }
        if (translateX == 0.0f && translateY == 0.0f) {
            return this.tx_DeltaScaleFilter.init(pathConsumer2D, scaleX, scaleY);
        }
        return this.tx_ScaleFilter.init(pathConsumer2D, scaleX, scaleY, translateX, translateY);
    }

    PathConsumer2D deltaTransformConsumer(PathConsumer2D pathConsumer2D, AffineTransform affineTransform) {
        if (affineTransform == null) {
            return pathConsumer2D;
        }
        float scaleX = (float) affineTransform.getScaleX();
        float shearX = (float) affineTransform.getShearX();
        float shearY = (float) affineTransform.getShearY();
        float scaleY = (float) affineTransform.getScaleY();
        if (shearX != 0.0f || shearY != 0.0f) {
            return this.dt_DeltaTransformFilter.init(pathConsumer2D, scaleX, shearX, shearY, scaleY);
        }
        if (scaleX == 1.0f && scaleY == 1.0f) {
            return pathConsumer2D;
        }
        return this.dt_DeltaScaleFilter.init(pathConsumer2D, scaleX, scaleY);
    }

    PathConsumer2D inverseDeltaTransformConsumer(PathConsumer2D pathConsumer2D, AffineTransform affineTransform) {
        if (affineTransform == null) {
            return pathConsumer2D;
        }
        float scaleX = (float) affineTransform.getScaleX();
        float shearX = (float) affineTransform.getShearX();
        float shearY = (float) affineTransform.getShearY();
        float scaleY = (float) affineTransform.getScaleY();
        if (shearX != 0.0f || shearY != 0.0f) {
            float f2 = (scaleX * scaleY) - (shearX * shearY);
            return this.iv_DeltaTransformFilter.init(pathConsumer2D, scaleY / f2, (-shearX) / f2, (-shearY) / f2, scaleX / f2);
        }
        if (scaleX == 1.0f && scaleY == 1.0f) {
            return pathConsumer2D;
        }
        return this.iv_DeltaScaleFilter.init(pathConsumer2D, 1.0f / scaleX, 1.0f / scaleY);
    }

    /* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D$TranslateFilter.class */
    static final class TranslateFilter implements PathConsumer2D {
        private PathConsumer2D out;
        private float tx;
        private float ty;

        TranslateFilter() {
        }

        TranslateFilter init(PathConsumer2D pathConsumer2D, float f2, float f3) {
            this.out = pathConsumer2D;
            this.tx = f2;
            this.ty = f3;
            return this;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 + this.tx, f3 + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 + this.tx, f3 + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 + this.tx, f3 + this.ty, f4 + this.tx, f5 + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 + this.tx, f3 + this.ty, f4 + this.tx, f5 + this.ty, f6 + this.tx, f7 + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D$ScaleFilter.class */
    static final class ScaleFilter implements PathConsumer2D {
        private PathConsumer2D out;
        private float sx;
        private float sy;
        private float tx;
        private float ty;

        ScaleFilter() {
        }

        ScaleFilter init(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5) {
            this.out = pathConsumer2D;
            this.sx = f2;
            this.sy = f3;
            this.tx = f4;
            this.ty = f5;
            return this;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo((f2 * this.sx) + this.tx, (f3 * this.sy) + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo((f2 * this.sx) + this.tx, (f3 * this.sy) + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo((f2 * this.sx) + this.tx, (f3 * this.sy) + this.ty, (f4 * this.sx) + this.tx, (f5 * this.sy) + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo((f2 * this.sx) + this.tx, (f3 * this.sy) + this.ty, (f4 * this.sx) + this.tx, (f5 * this.sy) + this.ty, (f6 * this.sx) + this.tx, (f7 * this.sy) + this.ty);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D$TransformFilter.class */
    static final class TransformFilter implements PathConsumer2D {
        private PathConsumer2D out;
        private float mxx;
        private float mxy;
        private float mxt;
        private float myx;
        private float myy;
        private float myt;

        TransformFilter() {
        }

        TransformFilter init(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out = pathConsumer2D;
            this.mxx = f2;
            this.mxy = f3;
            this.mxt = f4;
            this.myx = f5;
            this.myy = f6;
            this.myt = f7;
            return this;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo((f2 * this.mxx) + (f3 * this.mxy) + this.mxt, (f2 * this.myx) + (f3 * this.myy) + this.myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo((f2 * this.mxx) + (f3 * this.mxy) + this.mxt, (f2 * this.myx) + (f3 * this.myy) + this.myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo((f2 * this.mxx) + (f3 * this.mxy) + this.mxt, (f2 * this.myx) + (f3 * this.myy) + this.myt, (f4 * this.mxx) + (f5 * this.mxy) + this.mxt, (f4 * this.myx) + (f5 * this.myy) + this.myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo((f2 * this.mxx) + (f3 * this.mxy) + this.mxt, (f2 * this.myx) + (f3 * this.myy) + this.myt, (f4 * this.mxx) + (f5 * this.mxy) + this.mxt, (f4 * this.myx) + (f5 * this.myy) + this.myt, (f6 * this.mxx) + (f7 * this.mxy) + this.mxt, (f6 * this.myx) + (f7 * this.myy) + this.myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D$DeltaScaleFilter.class */
    static final class DeltaScaleFilter implements PathConsumer2D {
        private PathConsumer2D out;
        private float sx;
        private float sy;

        DeltaScaleFilter() {
        }

        DeltaScaleFilter init(PathConsumer2D pathConsumer2D, float f2, float f3) {
            this.out = pathConsumer2D;
            this.sx = f2;
            this.sy = f3;
            return this;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo(f2 * this.sx, f3 * this.sy);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo(f2 * this.sx, f3 * this.sy);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo(f2 * this.sx, f3 * this.sy, f4 * this.sx, f5 * this.sy);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo(f2 * this.sx, f3 * this.sy, f4 * this.sx, f5 * this.sy, f6 * this.sx, f7 * this.sy);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D$DeltaTransformFilter.class */
    static final class DeltaTransformFilter implements PathConsumer2D {
        private PathConsumer2D out;
        private float mxx;
        private float mxy;
        private float myx;
        private float myy;

        DeltaTransformFilter() {
        }

        DeltaTransformFilter init(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5) {
            this.out = pathConsumer2D;
            this.mxx = f2;
            this.mxy = f3;
            this.myx = f4;
            this.myy = f5;
            return this;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo((f2 * this.mxx) + (f3 * this.mxy), (f2 * this.myx) + (f3 * this.myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo((f2 * this.mxx) + (f3 * this.mxy), (f2 * this.myx) + (f3 * this.myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo((f2 * this.mxx) + (f3 * this.mxy), (f2 * this.myx) + (f3 * this.myy), (f4 * this.mxx) + (f5 * this.mxy), (f4 * this.myx) + (f5 * this.myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo((f2 * this.mxx) + (f3 * this.mxy), (f2 * this.myx) + (f3 * this.myy), (f4 * this.mxx) + (f5 * this.mxy), (f4 * this.myx) + (f5 * this.myy), (f6 * this.mxx) + (f7 * this.mxy), (f6 * this.myx) + (f7 * this.myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void closePath() {
            this.out.closePath();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void pathDone() {
            this.out.pathDone();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public long getNativeConsumer() {
            return 0L;
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/TransformingPathConsumer2D$Path2DWrapper.class */
    static final class Path2DWrapper implements PathConsumer2D {
        private Path2D.Float p2d;

        Path2DWrapper() {
        }

        Path2DWrapper init(Path2D.Float r4) {
            this.p2d = r4;
            return this;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.p2d.moveTo(f2, f3);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.p2d.lineTo(f2, f3);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void closePath() {
            this.p2d.closePath();
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void pathDone() {
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.p2d.curveTo(f2, f3, f4, f5, f6, f7);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.p2d.quadTo(f2, f3, f4, f5);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public long getNativeConsumer() {
            throw new InternalError("Not using a native peer");
        }
    }
}
