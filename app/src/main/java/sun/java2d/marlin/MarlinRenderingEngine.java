package sun.java2d.marlin;

import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.security.AccessController;
import sun.awt.geom.PathConsumer2D;
import sun.java2d.ReentrantContextProvider;
import sun.java2d.ReentrantContextProviderCLQ;
import sun.java2d.ReentrantContextProviderTL;
import sun.java2d.pipe.AATileGenerator;
import sun.java2d.pipe.Region;
import sun.java2d.pipe.RenderingEngine;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/java2d/marlin/MarlinRenderingEngine.class */
public class MarlinRenderingEngine extends RenderingEngine implements MarlinConst {
    private static final float MIN_PEN_SIZE = 1.0f / NORM_SUBPIXELS;
    private static final boolean useThreadLocal = MarlinProperties.isUseThreadLocal();
    static final int REF_TYPE;
    private static final ReentrantContextProvider<RendererContext> rdrCtxProvider;
    private static boolean settingsLogged;

    /* loaded from: rt.jar:sun/java2d/marlin/MarlinRenderingEngine$NormMode.class */
    private enum NormMode {
        ON_WITH_AA,
        ON_NO_AA,
        OFF
    }

    /* JADX WARN: Failed to restore switch over string. Please report as a decompilation issue */
    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("sun.java2d.renderer.useRef", "soft"));
        boolean z2 = -1;
        switch (str.hashCode()) {
            case 3195115:
                if (str.equals("hard")) {
                    z2 = 3;
                    break;
                }
                break;
            case 3535914:
                if (str.equals("soft")) {
                    z2 = true;
                    break;
                }
                break;
            case 3645304:
                if (str.equals("weak")) {
                    z2 = 2;
                    break;
                }
                break;
        }
        switch (z2) {
            case true:
            default:
                REF_TYPE = 1;
                break;
            case true:
                REF_TYPE = 2;
                break;
            case true:
                REF_TYPE = 0;
                break;
        }
        if (useThreadLocal) {
            rdrCtxProvider = new ReentrantContextProviderTL<RendererContext>(REF_TYPE) { // from class: sun.java2d.marlin.MarlinRenderingEngine.1
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // sun.java2d.ReentrantContextProvider
                public RendererContext newContext() {
                    return RendererContext.createContext();
                }
            };
        } else {
            rdrCtxProvider = new ReentrantContextProviderCLQ<RendererContext>(REF_TYPE) { // from class: sun.java2d.marlin.MarlinRenderingEngine.2
                /* JADX INFO: Access modifiers changed from: protected */
                @Override // sun.java2d.ReentrantContextProvider
                public RendererContext newContext() {
                    return RendererContext.createContext();
                }
            };
        }
        settingsLogged = !enableLogs;
    }

    public MarlinRenderingEngine() {
        logSettings(MarlinRenderingEngine.class.getName());
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public Shape createStrokedShape(Shape shape, float f2, int i2, int i3, float f3, float[] fArr, float f4) {
        Path2D.Float r0;
        RendererContext rendererContext = getRendererContext();
        try {
            if (rendererContext.p2d == null) {
                Path2D.Float r1 = new Path2D.Float(1, 4096);
                r0 = r1;
                rendererContext.p2d = r1;
            } else {
                r0 = rendererContext.p2d;
            }
            Path2D.Float r23 = r0;
            r23.reset();
            strokeTo(rendererContext, shape, null, f2, NormMode.OFF, i2, i3, f3, fArr, f4, rendererContext.transformerPC2D.wrapPath2d(r23));
            Path2D.Float r02 = new Path2D.Float(r23);
            returnRendererContext(rendererContext);
            return r02;
        } catch (Throwable th) {
            returnRendererContext(rendererContext);
            throw th;
        }
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public void strokeTo(Shape shape, AffineTransform affineTransform, BasicStroke basicStroke, boolean z2, boolean z3, boolean z4, PathConsumer2D pathConsumer2D) {
        NormMode normMode = z3 ? z4 ? NormMode.ON_WITH_AA : NormMode.ON_NO_AA : NormMode.OFF;
        RendererContext rendererContext = getRendererContext();
        try {
            strokeTo(rendererContext, shape, affineTransform, basicStroke, z2, normMode, z4, pathConsumer2D);
            returnRendererContext(rendererContext);
        } catch (Throwable th) {
            returnRendererContext(rendererContext);
            throw th;
        }
    }

    final void strokeTo(RendererContext rendererContext, Shape shape, AffineTransform affineTransform, BasicStroke basicStroke, boolean z2, NormMode normMode, boolean z3, PathConsumer2D pathConsumer2D) {
        float lineWidth;
        if (z2) {
            if (z3) {
                lineWidth = userSpaceLineWidth(affineTransform, MIN_PEN_SIZE);
            } else {
                lineWidth = userSpaceLineWidth(affineTransform, 1.0f);
            }
        } else {
            lineWidth = basicStroke.getLineWidth();
        }
        strokeTo(rendererContext, shape, affineTransform, lineWidth, normMode, basicStroke.getEndCap(), basicStroke.getLineJoin(), basicStroke.getMiterLimit(), basicStroke.getDashArray(), basicStroke.getDashPhase(), pathConsumer2D);
    }

    private final float userSpaceLineWidth(AffineTransform affineTransform, float f2) {
        float fSqrt;
        if (affineTransform == null) {
            fSqrt = 1.0f;
        } else if ((affineTransform.getType() & 36) != 0) {
            fSqrt = (float) Math.sqrt(affineTransform.getDeterminant());
        } else {
            double scaleX = affineTransform.getScaleX();
            double shearX = affineTransform.getShearX();
            double shearY = affineTransform.getShearY();
            double scaleY = affineTransform.getScaleY();
            double d2 = (scaleX * scaleX) + (shearY * shearY);
            double d3 = 2.0d * ((scaleX * shearX) + (shearY * scaleY));
            double d4 = (shearX * shearX) + (scaleY * scaleY);
            fSqrt = (float) Math.sqrt(((d2 + d4) + Math.sqrt((d3 * d3) + ((d2 - d4) * (d2 - d4)))) / 2.0d);
        }
        return f2 / fSqrt;
    }

    final void strokeTo(RendererContext rendererContext, Shape shape, AffineTransform affineTransform, float f2, NormMode normMode, int i2, int i3, float f3, float[] fArr, float f4, PathConsumer2D pathConsumer2D) {
        PathIterator normalizingPathIterator;
        float[] dirtyFloatArray;
        AffineTransform affineTransform2 = null;
        AffineTransform affineTransform3 = null;
        int length = -1;
        boolean z2 = false;
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
            if (nearZero((scaleX * shearX) + (shearY * scaleY)) && nearZero(((scaleX * scaleX) + (shearY * shearY)) - ((shearX * shearX) + (scaleY * scaleY)))) {
                float fSqrt = (float) Math.sqrt((scaleX * scaleX) + (shearY * shearY));
                if (fArr != null) {
                    z2 = true;
                    length = fArr.length;
                    if (length <= 256) {
                        dirtyFloatArray = rendererContext.dasher.dashes_initial;
                    } else {
                        if (doStats) {
                            RendererContext.stats.stat_array_dasher_dasher.add(length);
                        }
                        dirtyFloatArray = rendererContext.getDirtyFloatArray(length);
                    }
                    System.arraycopy(fArr, 0, dirtyFloatArray, 0, length);
                    fArr = dirtyFloatArray;
                    for (int i4 = 0; i4 < length; i4++) {
                        fArr[i4] = fSqrt * fArr[i4];
                    }
                    f4 = fSqrt * f4;
                }
                f2 = fSqrt * f2;
                normalizingPathIterator = getNormalizingPathIterator(rendererContext, normMode, shape.getPathIterator(affineTransform));
            } else if (normMode != NormMode.OFF) {
                affineTransform2 = affineTransform;
                normalizingPathIterator = getNormalizingPathIterator(rendererContext, normMode, shape.getPathIterator(affineTransform));
            } else {
                affineTransform3 = affineTransform;
                normalizingPathIterator = shape.getPathIterator(null);
            }
        } else {
            normalizingPathIterator = getNormalizingPathIterator(rendererContext, normMode, shape.getPathIterator(null));
        }
        if (useSimplifier) {
            pathConsumer2D = rendererContext.simplifier.init(pathConsumer2D);
        }
        TransformingPathConsumer2D transformingPathConsumer2D = rendererContext.transformerPC2D;
        PathConsumer2D pathConsumer2DInit = rendererContext.stroker.init(transformingPathConsumer2D.deltaTransformConsumer(transformingPathConsumer2D.transformConsumer(pathConsumer2D, affineTransform3), affineTransform2), f2, i2, i3, f3);
        if (fArr != null) {
            if (!z2) {
                length = fArr.length;
            }
            pathConsumer2DInit = rendererContext.dasher.init(pathConsumer2DInit, fArr, length, f4, z2);
        }
        pathTo(rendererContext, normalizingPathIterator, transformingPathConsumer2D.inverseDeltaTransformConsumer(pathConsumer2DInit, affineTransform2));
    }

    private static boolean nearZero(double d2) {
        return Math.abs(d2) < 2.0d * Math.ulp(d2);
    }

    PathIterator getNormalizingPathIterator(RendererContext rendererContext, NormMode normMode, PathIterator pathIterator) {
        switch (normMode) {
            case ON_WITH_AA:
                return rendererContext.nPCPathIterator.init(pathIterator);
            case ON_NO_AA:
                return rendererContext.nPQPathIterator.init(pathIterator);
            case OFF:
                return pathIterator;
            default:
                throw new InternalError("Unrecognized normalization mode");
        }
    }

    /* loaded from: rt.jar:sun/java2d/marlin/MarlinRenderingEngine$NormalizingPathIterator.class */
    static abstract class NormalizingPathIterator implements PathIterator {
        private PathIterator src;
        private float curx_adjust;
        private float cury_adjust;
        private float movx_adjust;
        private float movy_adjust;
        private final float[] tmp;

        abstract float normCoord(float f2);

        NormalizingPathIterator(float[] fArr) {
            this.tmp = fArr;
        }

        final NormalizingPathIterator init(PathIterator pathIterator) {
            this.src = pathIterator;
            return this;
        }

        final void dispose() {
            this.src = null;
        }

        @Override // java.awt.geom.PathIterator
        public final int currentSegment(float[] fArr) {
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
            float f2 = fArr[i2];
            float fNormCoord = normCoord(f2);
            fArr[i2] = fNormCoord;
            float f3 = fNormCoord - f2;
            float f4 = fArr[i2 + 1];
            float fNormCoord2 = normCoord(f4);
            fArr[i2 + 1] = fNormCoord2;
            float f5 = fNormCoord2 - f4;
            switch (iCurrentSegment) {
                case 0:
                    this.movx_adjust = f3;
                    this.movy_adjust = f5;
                    break;
                case 2:
                    fArr[0] = fArr[0] + ((this.curx_adjust + f3) / 2.0f);
                    fArr[1] = fArr[1] + ((this.cury_adjust + f5) / 2.0f);
                    break;
                case 3:
                    fArr[0] = fArr[0] + this.curx_adjust;
                    fArr[1] = fArr[1] + this.cury_adjust;
                    fArr[2] = fArr[2] + f3;
                    fArr[3] = fArr[3] + f5;
                    break;
            }
            this.curx_adjust = f3;
            this.cury_adjust = f5;
            return iCurrentSegment;
        }

        @Override // java.awt.geom.PathIterator
        public final int currentSegment(double[] dArr) {
            int iCurrentSegment = currentSegment(this.tmp);
            for (int i2 = 0; i2 < 6; i2++) {
                dArr[i2] = r0[i2];
            }
            return iCurrentSegment;
        }

        @Override // java.awt.geom.PathIterator
        public final int getWindingRule() {
            return this.src.getWindingRule();
        }

        @Override // java.awt.geom.PathIterator
        public final boolean isDone() {
            if (this.src.isDone()) {
                dispose();
                return true;
            }
            return false;
        }

        @Override // java.awt.geom.PathIterator
        public final void next() {
            this.src.next();
        }

        /* loaded from: rt.jar:sun/java2d/marlin/MarlinRenderingEngine$NormalizingPathIterator$NearestPixelCenter.class */
        static final class NearestPixelCenter extends NormalizingPathIterator {
            NearestPixelCenter(float[] fArr) {
                super(fArr);
            }

            @Override // sun.java2d.marlin.MarlinRenderingEngine.NormalizingPathIterator
            float normCoord(float f2) {
                return FloatMath.floor_f(f2) + 0.5f;
            }
        }

        /* loaded from: rt.jar:sun/java2d/marlin/MarlinRenderingEngine$NormalizingPathIterator$NearestPixelQuarter.class */
        static final class NearestPixelQuarter extends NormalizingPathIterator {
            NearestPixelQuarter(float[] fArr) {
                super(fArr);
            }

            @Override // sun.java2d.marlin.MarlinRenderingEngine.NormalizingPathIterator
            float normCoord(float f2) {
                return FloatMath.floor_f(f2 + 0.25f) + 0.25f;
            }
        }
    }

    private static void pathTo(RendererContext rendererContext, PathIterator pathIterator, PathConsumer2D pathConsumer2D) {
        rendererContext.dirty = true;
        pathToLoop(rendererContext.float6, pathIterator, pathConsumer2D);
        rendererContext.dirty = false;
    }

    private static void pathToLoop(float[] fArr, PathIterator pathIterator, PathConsumer2D pathConsumer2D) {
        while (!pathIterator.isDone()) {
            switch (pathIterator.currentSegment(fArr)) {
                case 0:
                    pathConsumer2D.moveTo(fArr[0], fArr[1]);
                    break;
                case 1:
                    pathConsumer2D.lineTo(fArr[0], fArr[1]);
                    break;
                case 2:
                    pathConsumer2D.quadTo(fArr[0], fArr[1], fArr[2], fArr[3]);
                    break;
                case 3:
                    pathConsumer2D.curveTo(fArr[0], fArr[1], fArr[2], fArr[3], fArr[4], fArr[5]);
                    break;
                case 4:
                    pathConsumer2D.closePath();
                    break;
            }
            pathIterator.next();
        }
        pathConsumer2D.pathDone();
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x001a  */
    @Override // sun.java2d.pipe.RenderingEngine
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public sun.java2d.pipe.AATileGenerator getAATileGenerator(java.awt.Shape r11, java.awt.geom.AffineTransform r12, sun.java2d.pipe.Region r13, java.awt.BasicStroke r14, boolean r15, boolean r16, int[] r17) {
        /*
            Method dump skipped, instructions count: 227
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.java2d.marlin.MarlinRenderingEngine.getAATileGenerator(java.awt.Shape, java.awt.geom.AffineTransform, sun.java2d.pipe.Region, java.awt.BasicStroke, boolean, boolean, int[]):sun.java2d.pipe.AATileGenerator");
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public final AATileGenerator getAATileGenerator(double d2, double d3, double d4, double d5, double d6, double d7, double d8, double d9, Region region, int[] iArr) {
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
        MarlinTileGenerator marlinTileGeneratorInit = null;
        Renderer rendererInit = null;
        RendererContext rendererContext = getRendererContext();
        try {
            rendererInit = rendererContext.renderer.init(region.getLoX(), region.getLoY(), region.getWidth(), region.getHeight(), 0);
            rendererInit.moveTo((float) d2, (float) d3);
            rendererInit.lineTo((float) (d2 + d4), (float) (d3 + d5));
            rendererInit.lineTo((float) (d2 + d4 + d6), (float) (d3 + d5 + d7));
            rendererInit.lineTo((float) (d2 + d6), (float) (d3 + d7));
            rendererInit.closePath();
            if (z2) {
                double d14 = d2 + d13 + d11;
                double d15 = d3 + d12 + d10;
                double d16 = d4 - (2.0d * d13);
                double d17 = d5 - (2.0d * d12);
                double d18 = d6 - (2.0d * d11);
                double d19 = d7 - (2.0d * d10);
                rendererInit.moveTo((float) d14, (float) d15);
                rendererInit.lineTo((float) (d14 + d16), (float) (d15 + d17));
                rendererInit.lineTo((float) (d14 + d16 + d18), (float) (d15 + d17 + d19));
                rendererInit.lineTo((float) (d14 + d18), (float) (d15 + d19));
                rendererInit.closePath();
            }
            rendererInit.pathDone();
            if (rendererInit.endRendering()) {
                marlinTileGeneratorInit = rendererContext.ptg.init();
                marlinTileGeneratorInit.getBbox(iArr);
                rendererInit = null;
            }
            return marlinTileGeneratorInit;
        } finally {
            if (rendererInit != null) {
                rendererInit.dispose();
                returnRendererContext(rendererContext);
            }
        }
    }

    @Override // sun.java2d.pipe.RenderingEngine
    public float getMinimumAAPenSize() {
        return MIN_PEN_SIZE;
    }

    private static void logSettings(String str) {
        String str2;
        if (settingsLogged) {
            return;
        }
        settingsLogged = true;
        switch (REF_TYPE) {
            case 0:
            default:
                str2 = "hard";
                break;
            case 1:
                str2 = "soft";
                break;
            case 2:
                str2 = "weak";
                break;
        }
        MarlinUtils.logInfo("===============================================================================");
        MarlinUtils.logInfo("Marlin software rasterizer           = ENABLED");
        MarlinUtils.logInfo("Version                              = [" + Version.getVersion() + "]");
        MarlinUtils.logInfo("sun.java2d.renderer                  = " + str);
        MarlinUtils.logInfo("sun.java2d.renderer.useThreadLocal   = " + useThreadLocal);
        MarlinUtils.logInfo("sun.java2d.renderer.useRef           = " + str2);
        MarlinUtils.logInfo("sun.java2d.renderer.pixelsize        = " + MarlinConst.INITIAL_PIXEL_DIM);
        MarlinUtils.logInfo("sun.java2d.renderer.subPixel_log2_X  = " + MarlinConst.SUBPIXEL_LG_POSITIONS_X);
        MarlinUtils.logInfo("sun.java2d.renderer.subPixel_log2_Y  = " + MarlinConst.SUBPIXEL_LG_POSITIONS_Y);
        MarlinUtils.logInfo("sun.java2d.renderer.tileSize_log2    = " + MarlinConst.TILE_SIZE_LG);
        MarlinUtils.logInfo("sun.java2d.renderer.blockSize_log2   = " + MarlinConst.BLOCK_SIZE_LG);
        MarlinUtils.logInfo("sun.java2d.renderer.blockSize_log2   = " + MarlinConst.BLOCK_SIZE_LG);
        MarlinUtils.logInfo("sun.java2d.renderer.forceRLE         = " + MarlinProperties.isForceRLE());
        MarlinUtils.logInfo("sun.java2d.renderer.forceNoRLE       = " + MarlinProperties.isForceNoRLE());
        MarlinUtils.logInfo("sun.java2d.renderer.useTileFlags     = " + MarlinProperties.isUseTileFlags());
        MarlinUtils.logInfo("sun.java2d.renderer.useTileFlags.useHeuristics = " + MarlinProperties.isUseTileFlagsWithHeuristics());
        MarlinUtils.logInfo("sun.java2d.renderer.rleMinWidth      = " + MarlinCache.RLE_MIN_WIDTH);
        MarlinUtils.logInfo("sun.java2d.renderer.useSimplifier    = " + MarlinConst.useSimplifier);
        MarlinUtils.logInfo("sun.java2d.renderer.doStats          = " + MarlinConst.doStats);
        MarlinUtils.logInfo("sun.java2d.renderer.doMonitors       = false");
        MarlinUtils.logInfo("sun.java2d.renderer.doChecks         = " + MarlinConst.doChecks);
        MarlinUtils.logInfo("sun.java2d.renderer.useLogger        = " + MarlinConst.useLogger);
        MarlinUtils.logInfo("sun.java2d.renderer.logCreateContext = " + MarlinConst.logCreateContext);
        MarlinUtils.logInfo("sun.java2d.renderer.logUnsafeMalloc  = " + MarlinConst.logUnsafeMalloc);
        MarlinUtils.logInfo("Renderer settings:");
        MarlinUtils.logInfo("CUB_COUNT_LG = 2");
        MarlinUtils.logInfo("CUB_DEC_BND  = " + Renderer.CUB_DEC_BND);
        MarlinUtils.logInfo("CUB_INC_BND  = " + Renderer.CUB_INC_BND);
        MarlinUtils.logInfo("QUAD_DEC_BND = " + Renderer.QUAD_DEC_BND);
        MarlinUtils.logInfo("===============================================================================");
    }

    static RendererContext getRendererContext() {
        return (RendererContext) rdrCtxProvider.acquire();
    }

    static void returnRendererContext(RendererContext rendererContext) {
        rendererContext.dispose();
        rdrCtxProvider.release(rendererContext);
    }
}
