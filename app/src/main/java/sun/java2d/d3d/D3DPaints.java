package sun.java2d.d3d;

import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;

/* loaded from: rt.jar:sun/java2d/d3d/D3DPaints.class */
abstract class D3DPaints {
    private static Map<Integer, D3DPaints> impls = new HashMap(4, 1.0f);

    abstract boolean isPaintValid(SunGraphics2D sunGraphics2D);

    D3DPaints() {
    }

    static {
        impls.put(2, new Gradient());
        impls.put(3, new LinearGradient());
        impls.put(4, new RadialGradient());
        impls.put(5, new Texture());
    }

    static boolean isValid(SunGraphics2D sunGraphics2D) {
        D3DPaints d3DPaints = impls.get(Integer.valueOf(sunGraphics2D.paintState));
        return d3DPaints != null && d3DPaints.isPaintValid(sunGraphics2D);
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DPaints$Gradient.class */
    private static class Gradient extends D3DPaints {
        private Gradient() {
        }

        @Override // sun.java2d.d3d.D3DPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            return ((D3DGraphicsDevice) ((D3DSurfaceData) sunGraphics2D.surfaceData).getDeviceConfiguration().getDevice()).isCapPresent(65536);
        }
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DPaints$Texture.class */
    private static class Texture extends D3DPaints {
        private Texture() {
        }

        @Override // sun.java2d.d3d.D3DPaints
        public boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            TexturePaint texturePaint = (TexturePaint) sunGraphics2D.paint;
            D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sunGraphics2D.surfaceData;
            BufferedImage image = texturePaint.getImage();
            D3DGraphicsDevice d3DGraphicsDevice = (D3DGraphicsDevice) d3DSurfaceData.getDeviceConfiguration().getDevice();
            int width = image.getWidth();
            int height = image.getHeight();
            if (!d3DGraphicsDevice.isCapPresent(32) && ((width & (width - 1)) != 0 || (height & (height - 1)) != 0)) {
                return false;
            }
            if (!d3DGraphicsDevice.isCapPresent(64) && width != height) {
                return false;
            }
            SurfaceData sourceSurfaceData = d3DSurfaceData.getSourceSurfaceData(image, 0, CompositeType.SrcOver, null);
            if (!(sourceSurfaceData instanceof D3DSurfaceData)) {
                sourceSurfaceData = d3DSurfaceData.getSourceSurfaceData(image, 0, CompositeType.SrcOver, null);
                if (!(sourceSurfaceData instanceof D3DSurfaceData)) {
                    return false;
                }
            }
            if (((D3DSurfaceData) sourceSurfaceData).getType() != 3) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DPaints$MultiGradient.class */
    private static abstract class MultiGradient extends D3DPaints {
        public static final int MULTI_MAX_FRACTIONS_D3D = 8;

        protected MultiGradient() {
        }

        @Override // sun.java2d.d3d.D3DPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            if (((MultipleGradientPaint) sunGraphics2D.paint).getFractions().length > 8 || !((D3DGraphicsDevice) ((D3DSurfaceData) sunGraphics2D.surfaceData).getDeviceConfiguration().getDevice()).isCapPresent(65536)) {
                return false;
            }
            return true;
        }
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DPaints$LinearGradient.class */
    private static class LinearGradient extends MultiGradient {
        private LinearGradient() {
        }

        @Override // sun.java2d.d3d.D3DPaints.MultiGradient, sun.java2d.d3d.D3DPaints
        boolean isPaintValid(SunGraphics2D sunGraphics2D) {
            LinearGradientPaint linearGradientPaint = (LinearGradientPaint) sunGraphics2D.paint;
            if (linearGradientPaint.getFractions().length == 2 && linearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.REPEAT && linearGradientPaint.getColorSpace() != MultipleGradientPaint.ColorSpaceType.LINEAR_RGB && ((D3DGraphicsDevice) ((D3DSurfaceData) sunGraphics2D.surfaceData).getDeviceConfiguration().getDevice()).isCapPresent(65536)) {
                return true;
            }
            return super.isPaintValid(sunGraphics2D);
        }
    }

    /* loaded from: rt.jar:sun/java2d/d3d/D3DPaints$RadialGradient.class */
    private static class RadialGradient extends MultiGradient {
        private RadialGradient() {
        }
    }
}
