package sun.java2d.pisces;

import java.awt.geom.AffineTransform;
import sun.awt.geom.PathConsumer2D;

/* loaded from: rt.jar:sun/java2d/pisces/TransformingPathConsumer2D.class */
final class TransformingPathConsumer2D {
    TransformingPathConsumer2D() {
    }

    public static PathConsumer2D transformConsumer(PathConsumer2D pathConsumer2D, AffineTransform affineTransform) {
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
                return new DeltaTransformFilter(pathConsumer2D, scaleX, shearX, shearY, scaleY);
            }
            return new TransformFilter(pathConsumer2D, scaleX, shearX, translateX, shearY, scaleY, translateY);
        }
        if (scaleX == 1.0f && scaleY == 1.0f) {
            if (translateX == 0.0f && translateY == 0.0f) {
                return pathConsumer2D;
            }
            return new TranslateFilter(pathConsumer2D, translateX, translateY);
        }
        if (translateX == 0.0f && translateY == 0.0f) {
            return new DeltaScaleFilter(pathConsumer2D, scaleX, scaleY);
        }
        return new ScaleFilter(pathConsumer2D, scaleX, scaleY, translateX, translateY);
    }

    public static PathConsumer2D deltaTransformConsumer(PathConsumer2D pathConsumer2D, AffineTransform affineTransform) {
        if (affineTransform == null) {
            return pathConsumer2D;
        }
        float scaleX = (float) affineTransform.getScaleX();
        float shearX = (float) affineTransform.getShearX();
        float shearY = (float) affineTransform.getShearY();
        float scaleY = (float) affineTransform.getScaleY();
        if (shearX != 0.0f || shearY != 0.0f) {
            return new DeltaTransformFilter(pathConsumer2D, scaleX, shearX, shearY, scaleY);
        }
        if (scaleX == 1.0f && scaleY == 1.0f) {
            return pathConsumer2D;
        }
        return new DeltaScaleFilter(pathConsumer2D, scaleX, scaleY);
    }

    public static PathConsumer2D inverseDeltaTransformConsumer(PathConsumer2D pathConsumer2D, AffineTransform affineTransform) {
        if (affineTransform == null) {
            return pathConsumer2D;
        }
        float scaleX = (float) affineTransform.getScaleX();
        float shearX = (float) affineTransform.getShearX();
        float shearY = (float) affineTransform.getShearY();
        float scaleY = (float) affineTransform.getScaleY();
        if (shearX != 0.0f || shearY != 0.0f) {
            float f2 = (scaleX * scaleY) - (shearX * shearY);
            return new DeltaTransformFilter(pathConsumer2D, scaleY / f2, (-shearX) / f2, (-shearY) / f2, scaleX / f2);
        }
        if (scaleX == 1.0f && scaleY == 1.0f) {
            return pathConsumer2D;
        }
        return new DeltaScaleFilter(pathConsumer2D, 1.0f / scaleX, 1.0f / scaleY);
    }

    /* loaded from: rt.jar:sun/java2d/pisces/TransformingPathConsumer2D$TranslateFilter.class */
    static final class TranslateFilter implements PathConsumer2D {
        private final PathConsumer2D out;
        private final float tx;
        private final float ty;

        TranslateFilter(PathConsumer2D pathConsumer2D, float f2, float f3) {
            this.out = pathConsumer2D;
            this.tx = f2;
            this.ty = f3;
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

    /* loaded from: rt.jar:sun/java2d/pisces/TransformingPathConsumer2D$ScaleFilter.class */
    static final class ScaleFilter implements PathConsumer2D {
        private final PathConsumer2D out;
        private final float sx;
        private final float sy;
        private final float tx;
        private final float ty;

        ScaleFilter(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5) {
            this.out = pathConsumer2D;
            this.sx = f2;
            this.sy = f3;
            this.tx = f4;
            this.ty = f5;
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

    /* loaded from: rt.jar:sun/java2d/pisces/TransformingPathConsumer2D$TransformFilter.class */
    static final class TransformFilter implements PathConsumer2D {
        private final PathConsumer2D out;
        private final float Mxx;
        private final float Mxy;
        private final float Mxt;
        private final float Myx;
        private final float Myy;
        private final float Myt;

        TransformFilter(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out = pathConsumer2D;
            this.Mxx = f2;
            this.Mxy = f3;
            this.Mxt = f4;
            this.Myx = f5;
            this.Myy = f6;
            this.Myt = f7;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo((f2 * this.Mxx) + (f3 * this.Mxy) + this.Mxt, (f2 * this.Myx) + (f3 * this.Myy) + this.Myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo((f2 * this.Mxx) + (f3 * this.Mxy) + this.Mxt, (f2 * this.Myx) + (f3 * this.Myy) + this.Myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo((f2 * this.Mxx) + (f3 * this.Mxy) + this.Mxt, (f2 * this.Myx) + (f3 * this.Myy) + this.Myt, (f4 * this.Mxx) + (f5 * this.Mxy) + this.Mxt, (f4 * this.Myx) + (f5 * this.Myy) + this.Myt);
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo((f2 * this.Mxx) + (f3 * this.Mxy) + this.Mxt, (f2 * this.Myx) + (f3 * this.Myy) + this.Myt, (f4 * this.Mxx) + (f5 * this.Mxy) + this.Mxt, (f4 * this.Myx) + (f5 * this.Myy) + this.Myt, (f6 * this.Mxx) + (f7 * this.Mxy) + this.Mxt, (f6 * this.Myx) + (f7 * this.Myy) + this.Myt);
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

    /* loaded from: rt.jar:sun/java2d/pisces/TransformingPathConsumer2D$DeltaScaleFilter.class */
    static final class DeltaScaleFilter implements PathConsumer2D {
        private final float sx;
        private final float sy;
        private final PathConsumer2D out;

        public DeltaScaleFilter(PathConsumer2D pathConsumer2D, float f2, float f3) {
            this.sx = f2;
            this.sy = f3;
            this.out = pathConsumer2D;
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

    /* loaded from: rt.jar:sun/java2d/pisces/TransformingPathConsumer2D$DeltaTransformFilter.class */
    static final class DeltaTransformFilter implements PathConsumer2D {
        private PathConsumer2D out;
        private final float Mxx;
        private final float Mxy;
        private final float Myx;
        private final float Myy;

        DeltaTransformFilter(PathConsumer2D pathConsumer2D, float f2, float f3, float f4, float f5) {
            this.out = pathConsumer2D;
            this.Mxx = f2;
            this.Mxy = f3;
            this.Myx = f4;
            this.Myy = f5;
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void moveTo(float f2, float f3) {
            this.out.moveTo((f2 * this.Mxx) + (f3 * this.Mxy), (f2 * this.Myx) + (f3 * this.Myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void lineTo(float f2, float f3) {
            this.out.lineTo((f2 * this.Mxx) + (f3 * this.Mxy), (f2 * this.Myx) + (f3 * this.Myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void quadTo(float f2, float f3, float f4, float f5) {
            this.out.quadTo((f2 * this.Mxx) + (f3 * this.Mxy), (f2 * this.Myx) + (f3 * this.Myy), (f4 * this.Mxx) + (f5 * this.Mxy), (f4 * this.Myx) + (f5 * this.Myy));
        }

        @Override // sun.awt.geom.PathConsumer2D
        public void curveTo(float f2, float f3, float f4, float f5, float f6, float f7) {
            this.out.curveTo((f2 * this.Mxx) + (f3 * this.Mxy), (f2 * this.Myx) + (f3 * this.Myy), (f4 * this.Mxx) + (f5 * this.Mxy), (f4 * this.Myx) + (f5 * this.Myy), (f6 * this.Mxx) + (f7 * this.Mxy), (f6 * this.Myx) + (f7 * this.Myy));
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
}
