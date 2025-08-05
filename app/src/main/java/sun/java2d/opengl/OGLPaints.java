package sun.java2d.opengl;

import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;

/* loaded from: rt.jar:sun/java2d/opengl/OGLPaints.class */
abstract class OGLPaints {
    private static Map<Integer, OGLPaints> impls = new HashMap(4, 1.0f);

    abstract boolean isPaintValid(SunGraphics2D sunGraphics2D);

    OGLPaints() {
    }

    static {
        impls.put(2, new Gradient());
        impls.put(3, new LinearGradient());
        impls.put(4, new RadialGradient());
        impls.put(5, new Texture());
    }

    static boolean isValid(SunGraphics2D sunGraphics2D) {
        OGLPaints oGLPaints = impls.get(Integer.valueOf(sunGraphics2D.paintState));
        return oGLPaints != null && oGLPaints.isPaintValid(sunGraphics2D);
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLPaints$Gradient.class */
    private static class Gradient extends OGLPaints {
        private Gradient() {
        }

        @Override // sun.java2d.opengl.OGLPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            return true;
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLPaints$Texture.class */
    private static class Texture extends OGLPaints {
        private Texture() {
        }

        @Override // sun.java2d.opengl.OGLPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            TexturePaint texturePaint = (TexturePaint) sunGraphics2D.paint;
            OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sunGraphics2D.surfaceData;
            BufferedImage image = texturePaint.getImage();
            if (!oGLSurfaceData.isTexNonPow2Available()) {
                int width = image.getWidth();
                int height = image.getHeight();
                if ((width & (width - 1)) != 0 || (height & (height - 1)) != 0) {
                    return false;
                }
            }
            SurfaceData sourceSurfaceData = oGLSurfaceData.getSourceSurfaceData(image, 0, CompositeType.SrcOver, null);
            if (!(sourceSurfaceData instanceof OGLSurfaceData)) {
                sourceSurfaceData = oGLSurfaceData.getSourceSurfaceData(image, 0, CompositeType.SrcOver, null);
                if (!(sourceSurfaceData instanceof OGLSurfaceData)) {
                    return false;
                }
            }
            if (((OGLSurfaceData) sourceSurfaceData).getType() != 3) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLPaints$MultiGradient.class */
    private static abstract class MultiGradient extends OGLPaints {
        protected MultiGradient() {
        }

        @Override // sun.java2d.opengl.OGLPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            if (((MultipleGradientPaint) sunGraphics2D.paint).getFractions().length > 12 || !((OGLSurfaceData) sunGraphics2D.surfaceData).getOGLGraphicsConfig().isCapPresent(524288)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLPaints$LinearGradient.class */
    private static class LinearGradient extends MultiGradient {
        private LinearGradient() {
        }

        @Override // sun.java2d.opengl.OGLPaints.MultiGradient, sun.java2d.opengl.OGLPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            LinearGradientPaint linearGradientPaint = (LinearGradientPaint) sunGraphics2D.paint;
            if (linearGradientPaint.getFractions().length == 2 && linearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.REPEAT && linearGradientPaint.getColorSpace() != MultipleGradientPaint.ColorSpaceType.LINEAR_RGB) {
                return true;
            }
            return super.isPaintValid(sunGraphics2D);
        }
    }

    /* loaded from: rt.jar:sun/java2d/opengl/OGLPaints$RadialGradient.class */
    private static class RadialGradient extends MultiGradient {
        private RadialGradient() {
        }
    }
}
