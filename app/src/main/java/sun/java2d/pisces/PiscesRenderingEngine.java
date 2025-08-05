package sun.java2d.pisces;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import sun.awt.geom.PathConsumer2D;
import sun.java2d.pipe.AATileGenerator;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderingEngine;

/* loaded from: rt.jar:sun/java2d/pisces/PiscesRenderingEngine.class */
public class PiscesRenderingEngine extends RenderingEngine {

    /* loaded from: rt.jar:sun/java2d/pisces/PiscesRenderingEngine$NormMode.class */
    private enum NormMode {
        OFF,
        ON_NO_AA,
        ON_WITH_AA
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public Shape createStrokedShape(Shape shape, float f2, int i2, int i3, float f3, float[] fArr, float f4) {
        final Path2D.Float r0 = new Path2D.Float();
        strokeTo(shape, null, f2, NormMode.OFF, i2, i3, f3, fArr, f4, new PathConsumer2D() { // from class: sun.java2d.pisces.PiscesRenderingEngine.1
            @Override // sun.awt.geom.PathConsumer2D
            public void moveTo(float f5, float f6) {
                r0.moveTo(f5, f6);
            }

            @Override // sun.awt.geom.PathConsumer2D
            public void lineTo(float f5, float f6) {
                r0.lineTo(f5, f6);
            }

            @Override // sun.awt.geom.PathConsumer2D
            public void closePath() {
                r0.closePath();
            }

            @Override // sun.awt.geom.PathConsumer2D
            public void pathDone() {
            }

            @Override // sun.awt.geom.PathConsumer2D
            public void curveTo(float f5, float f6, float f7, float f8, float f9, float f10) {
                r0.curveTo(f5, f6, f7, f8, f9, f10);
            }

            @Override // sun.awt.geom.PathConsumer2D
            public void quadTo(float f5, float f6, float f7, float f8) {
                r0.quadTo(f5, f6, f7, f8);
            }

            @Override // sun.awt.geom.PathConsumer2D
            public long getNativeConsumer() {
                throw new InternalError("Not using a native peer");
            }
        });
        return r0;
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public void strokeTo(Shape shape, AffineTransform affineTransform, BasicStroke basicStroke, boolean z2, boolean z3, boolean z4, PathConsumer2D pathConsumer2D) {
        strokeTo(shape, affineTransform, basicStroke, z2, z3 ? z4 ? NormMode.ON_WITH_AA : NormMode.ON_NO_AA : NormMode.OFF, z4, pathConsumer2D);
    }

    void strokeTo(Shape shape, AffineTransform affineTransform, BasicStroke basicStroke, boolean z2, NormMode normMode, boolean z3, PathConsumer2D pathConsumer2D) {
        float lineWidth;
        if (z2) {
            if (z3) {
                lineWidth = userSpaceLineWidth(affineTransform, 0.5f);
            } else {
                lineWidth = userSpaceLineWidth(affineTransform, 1.0f);
            }
        } else {
            lineWidth = basicStroke.getLineWidth();
        }
        strokeTo(shape, affineTransform, lineWidth, normMode, basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit(), basicStroke.getDashArray(), basicStroke.getDashPhase(), pathConsumer2D);
    }

    private float userSpaceLineWidth(AffineTransform affineTransform, float f2) {
        double dSqrt;
        if ((affineTransform.getType() & 36) != 0) {
            dSqrt = Math.sqrt(affineTransform.getDeterminant());
        } else {
            double scaleX = affineTransform.getScaleX();
            double shearX = affineTransform.getShearX();
            double shearY = affineTransform.getShearY();
            double scaleY = affineTransform.getScaleY();
            double d2 = (scaleX * scaleX) + (shearY * shearY);
            double d3 = 2.0d * ((scaleX * shearX) + (shearY * scaleY));
            double d4 = (shearX * shearX) + (scaleY * scaleY);
            dSqrt = Math.sqrt(((d2 + d4) + Math.sqrt((d3 * d3) + ((d2 - d4) * (d2 - d4)))) / 2.0d);
        }
        return (float) (f2 / dSqrt);
    }

    void strokeTo(Shape shape, AffineTransform affineTransform, float f2, NormMode normMode, int i2, int i3, float f3, float[] fArr, float f4, PathConsumer2D pathConsumer2D) {
        PathIterator pathIterator;
        AffineTransform affineTransform2 = null;
        AffineTransform affineTransform3 = null;
        if (affineTransform != null && !affineTransform.isIdentity()) {
            double scaleX = affineTransform.getScaleX();
            double shearX = affineTransform.getShearX();
            double shearY = affineTransform.getShearY();
            double scaleY = affineTransform.getScaleY();
            if (Math.abs((scaleX * scaleY) - (shearY * shearX)) <= 2.802596928649634E-45d) {
                pathConsumer2D.moveTo(0.0f, 0.0f);
                pathConsumer2D.pathDone();
                return;
            }
            if (nearZero((scaleX * shearX) + (shearY * scaleY), 2) && nearZero(((scaleX * scaleX) + (shearY * shearY)) - ((shearX * shearX) + (scaleY * scaleY)), 2)) {
                double dSqrt = Math.sqrt((scaleX * scaleX) + (shearY * shearY));
                if (fArr != null) {
                    fArr = Arrays.copyOf(fArr, fArr.length);
                    for (int i4 = 0; i4 < fArr.length; i4++) {
                        fArr[i4] = (float) (dSqrt * fArr[i4]);
                    }
                    f4 = (float) (dSqrt * f4);
                }
                f2 = (float) (dSqrt * f2);
                pathIterator = shape.getPathIterator(affineTransform);
                if (normMode != NormMode.OFF) {
                    pathIterator = new NormalizingPathIterator(pathIterator, normMode);
                }
            } else if (normMode != NormMode.OFF) {
                affineTransform2 = affineTransform;
                pathIterator = new NormalizingPathIterator(shape.getPathIterator(affineTransform), normMode);
            } else {
                affineTransform3 = affineTransform;
                pathIterator = shape.getPathIterator(null);
            }
        } else {
            pathIterator = shape.getPathIterator(null);
            if (normMode != NormMode.OFF) {
                pathIterator = new NormalizingPathIterator(pathIterator, normMode);
            }
        }
        PathConsumer2D stroker = new Stroker(TransformingPathConsumer2D.deltaTransformConsumer(TransformingPathConsumer2D.transformConsumer(pathConsumer2D, affineTransform3), affineTransform2), f2, i2, i3, f3);
        if (fArr != null) {
            stroker = new Dasher(stroker, fArr, f4);
        }
        pathTo(pathIterator, TransformingPathConsumer2D.inverseDeltaTransformConsumer(stroker, affineTransform2));
    }

    private static boolean nearZero(double d2, int i2) {
        return Math.abs(d2) < ((double) i2) * Math.ulp(d2);
    }

    /* loaded from: rt.jar:sun/java2d/pisces/PiscesRenderingEngine$NormalizingPathIterator.class */
    private static class NormalizingPathIterator implements PathIterator {
        private final PathIterator src;
        private float curx_adjust;
        private float cury_adjust;
        private float movx_adjust;
        private float movy_adjust;
        private final float lval;
        private final float rval;

        NormalizingPathIterator(PathIterator pathIterator, NormMode normMode) {
            this.src = pathIterator;
            switch (normMode) {
                case ON_NO_AA:
                    this.rval = 0.25f;
                    this.lval = 0.25f;
                    return;
                case ON_WITH_AA:
                    this.lval = 0.0f;
                    this.rval = 0.5f;
                    return;
                case OFF:
                    throw new InternalError("A NormalizingPathIterator should not be created if no normalization is being done");
                default:
                    throw new InternalError("Unrecognized normalization mode");
            }
        }

        @Override // java.awt.geom.PathIterator
        public int currentSegment(float[] fArr) {
            int i2;
            int iCurrentSegment = this.src.currentSegment(fArr);
            switch (iCurrentSegment) {
                case 0:
                case 1:
                    i2 = 0;
                    break;
                case 2:
                    i2 = 2;
                    break;
                case 3:
                    i2 = 4;
                    break;
                case 4:
                    this.curx_adjust = this.movx_adjust;
                    this.cury_adjust = this.movy_adjust;
                    return iCurrentSegment;
                default:
                    throw new InternalError("Unrecognized curve type");
            }
            float fFloor = (((float) Math.floor(fArr[i2] + this.lval)) + this.rval) - fArr[i2];
            float fFloor2 = (((float) Math.floor(fArr[i2 + 1] + this.lval)) + this.rval) - fArr[i2 + 1];
            int i3 = i2;
            fArr[i3] = fArr[i3] + fFloor;
            int i4 = i2 + 1;
            fArr[i4] = fArr[i4] + fFloor2;
            switch (iCurrentSegment) {
                case 0:
                    this.movx_adjust = fFloor;
                    this.movy_adjust = fFloor2;
                    break;
                case 2:
                    fArr[0] = fArr[0] + ((this.curx_adjust + fFloor) / 2.0f);
                    fArr[1] = fArr[1] + ((this.cury_adjust + fFloor2) / 2.0f);
                    break;
                case 3:
                    fArr[0] = fArr[0] + this.curx_adjust;
                    fArr[1] = fArr[1] + this.cury_adjust;
                    fArr[2] = fArr[2] + fFloor;
                    fArr[3] = fArr[3] + fFloor2;
                    break;
                case 4:
                    throw new InternalError("This should be handled earlier.");
            }
            this.curx_adjust = fFloor;
            this.cury_adjust = fFloor2;
            return iCurrentSegment;
        }

        @Override // java.awt.geom.PathIterator
        public int currentSegment(double[] dArr) {
            int iCurrentSegment = currentSegment(new float[6]);
            for (int i2 = 0; i2 < 6; i2++) {
                dArr[i2] = r0[i2];
            }
            return iCurrentSegment;
        }

        @Override // java.awt.geom.PathIterator
        public int getWindingRule() {
            return this.src.getWindingRule();
        }

        @Override // java.awt.geom.PathIterator
        public boolean isDone() {
            return this.src.isDone();
        }

        @Override // java.awt.geom.PathIterator
        public void next() {
            this.src.next();
        }
    }

    static void pathTo(PathIterator pathIterator, PathConsumer2D pathConsumer2D) {
        RenderingEngine.feedConsumer(pathIterator, pathConsumer2D);
        pathConsumer2D.pathDone();
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public AATileGenerator getAATileGenerator(Shape shape, AffineTransform affineTransform, Region region, BasicStroke basicStroke, boolean z2, boolean z3, int[] iArr) {
        Renderer renderer;
        PathIterator pathIterator;
        NormMode normMode = z3 ? NormMode.ON_WITH_AA : NormMode.OFF;
        if (basicStroke == null) {
            if (z3) {
                pathIterator = new NormalizingPathIterator(shape.getPathIterator(affineTransform), normMode);
            } else {
                pathIterator = shape.getPathIterator(affineTransform);
            }
            renderer = new Renderer(3, 3, region.getLoX(), region.getLoY(), region.getWidth(), region.getHeight(), pathIterator.getWindingRule());
            pathTo(pathIterator, renderer);
        } else {
            renderer = new Renderer(3, 3, region.getLoX(), region.getLoY(), region.getWidth(), region.getHeight(), 1);
            strokeTo(shape, affineTransform, basicStroke, z2, normMode, true, (PathConsumer2D) renderer);
        }
        renderer.endRendering();
        PiscesTileGenerator piscesTileGenerator = new PiscesTileGenerator(renderer, renderer.MAX_AA_ALPHA);
        piscesTileGenerator.getBbox(iArr);
        return piscesTileGenerator;
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public AATileGenerator getAATileGenerator(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, Region region, int[] iArr) {
        double d10;
        double d11;
        double d12;
        double d13;
        boolean z2 = d8 > 0.0d && d9 > 0.0d;
        if (z2) {
            d13 = d4 * d8;
            d12 = d5 * d8;
            d11 = d6 * d9;
            d10 = d7 * d9;
            d2 -= (d13 + d11) / 2.0d;
            d3 -= (d12 + d10) / 2.0d;
            d4 += d13;
            d5 += d12;
            d6 += d11;
            d7 += d10;
            if (d8 > 1.0d && d9 > 1.0d) {
                z2 = false;
            }
        } else {
            d10 = 0.0d;
            d11 = 0.0d;
            d12 = 0.0d;
            d13 = 0.0d;
        }
        Renderer renderer = new Renderer(3, 3, region.getLoX(), region.getLoY(), region.getWidth(), region.getHeight(), 0);
        renderer.moveTo((float) d2, (float) d3);
        renderer.lineTo((float) (d2 + d4), (float) (d3 + d5));
        renderer.lineTo((float) (d2 + d4 + d6), (float) (d3 + d5 + d7));
        renderer.lineTo((float) (d2 + d6), (float) (d3 + d7));
        renderer.closePath();
        if (z2) {
            double d14 = d2 + d13 + d11;
            double d15 = d3 + d12 + d10;
            double d16 = d4 - (2.0d * d13);
            double d17 = d5 - (2.0d * d12);
            double d18 = d6 - (2.0d * d11);
            double d19 = d7 - (2.0d * d10);
            renderer.moveTo((float) d14, (float) d15);
            renderer.lineTo((float) (d14 + d16), (float) (d15 + d17));
            renderer.lineTo((float) (d14 + d16 + d18), (float) (d15 + d17 + d19));
            renderer.lineTo((float) (d14 + d18), (float) (d15 + d19));
            renderer.closePath();
        }
        renderer.pathDone();
        renderer.endRendering();
        PiscesTileGenerator piscesTileGenerator = new PiscesTileGenerator(renderer, renderer.MAX_AA_ALPHA);
        piscesTileGenerator.getBbox(iArr);
        return piscesTileGenerator;
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public float getMinimumAAPenSize() {
        return 0.5f;
    }
}
