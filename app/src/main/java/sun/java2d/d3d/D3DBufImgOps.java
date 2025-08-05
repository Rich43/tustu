package sun.java2d.d3d;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.LookupOp;
import java.awt.image.RescaleOp;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;
import sun.java2d.pipe.BufferedBufImgOps;

/* loaded from: rt.jar:sun/java2d/d3d/D3DBufImgOps.class */
class D3DBufImgOps extends BufferedBufImgOps {
    D3DBufImgOps() {
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
        if (!(surfaceData instanceof D3DSurfaceData) || sunGraphics2D.interpolationType == 3 || sunGraphics2D.compositeState > 1) {
            return false;
        }
        SurfaceData sourceSurfaceData = surfaceData.getSourceSurfaceData(bufferedImage, 0, CompositeType.SrcOver, null);
        if (!(sourceSurfaceData instanceof D3DSurfaceData)) {
            sourceSurfaceData = surfaceData.getSourceSurfaceData(bufferedImage, 0, CompositeType.SrcOver, null);
            if (!(sourceSurfaceData instanceof D3DSurfaceData)) {
                return false;
            }
        }
        D3DSurfaceData d3DSurfaceData = (D3DSurfaceData) sourceSurfaceData;
        D3DGraphicsDevice d3DGraphicsDevice = (D3DGraphicsDevice) d3DSurfaceData.getDeviceConfiguration().getDevice();
        if (d3DSurfaceData.getType() != 3 || !d3DGraphicsDevice.isCapPresent(65536)) {
            return false;
        }
        D3DBlitLoops.IsoBlit(sourceSurfaceData, surfaceData, bufferedImage, bufferedImageOp, sunGraphics2D.composite, sunGraphics2D.getCompClip(), sunGraphics2D.transform, sunGraphics2D.interpolationType, 0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), i2, i3, i2 + r0, i3 + r0, true);
        return true;
    }
}
