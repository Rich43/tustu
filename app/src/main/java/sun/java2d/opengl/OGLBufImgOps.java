package sun.java2d.opengl;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.pipe.BufferedBufImgOps;

/* loaded from: rt.jar:sun/java2d/opengl/OGLBufImgOps.class */
class OGLBufImgOps extends BufferedBufImgOps {
    OGLBufImgOps() {
    }

    static boolean renderImageWithOp(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        if (bufferedImageOp instanceof ConvolveOp) {
            if (!isConvolveOpValid((ConvolveOp) bufferedImageOp)) {
                return false;
            }
        } else if (bufferedImageOp instanceof RescaleOp) {
            if (!isRescaleOpValid((RescaleOp) bufferedImageOp, bufferedImage)) {
                return false;
            }
        } else if (!(bufferedImageOp instanceof LookupOp) || !isLookupOpValid((LookupOp) bufferedImageOp, bufferedImage)) {
            return false;
        }
        SurfaceData surfaceData = sunGraphics2D.surfaceData;
        if (!(surfaceData instanceof OGLSurfaceData) || sunGraphics2D.interpolationType == 3 || sunGraphics2D.compositeState > 1) {
            return false;
        }
        SurfaceData sourceSurfaceData = surfaceData.getSourceSurfaceData(bufferedImage, 0, CompositeType.SrcOver, null);
        if (!(sourceSurfaceData instanceof OGLSurfaceData)) {
            sourceSurfaceData = surfaceData.getSourceSurfaceData(bufferedImage, 0, CompositeType.SrcOver, null);
            if (!(sourceSurfaceData instanceof OGLSurfaceData)) {
                return false;
            }
        }
        OGLSurfaceData oGLSurfaceData = (OGLSurfaceData) sourceSurfaceData;
        OGLGraphicsConfig oGLGraphicsConfig = oGLSurfaceData.getOGLGraphicsConfig();
        if (oGLSurfaceData.getType() != 3 || !oGLGraphicsConfig.isCapPresent(262144)) {
            return false;
        }
        OGLBlitLoops.IsoBlit(sourceSurfaceData, surfaceData, bufferedImage, bufferedImageOp, sunGraphics2D.composite, sunGraphics2D.getCompClip(), sunGraphics2D.transform, sunGraphics2D.interpolationType, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), i2, i3, i2 + r0, i3 + r0, true);
        return true;
    }
}
