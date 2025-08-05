package sun.java2d.opengl;

import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.TransformBlit;
import sun.java2d.pipe.DrawImage;

/* loaded from: rt.jar:sun/java2d/opengl/OGLDrawImage.class */
public class OGLDrawImage extends DrawImage {
    @Override // sun.java2d.pipe.DrawImage
    protected void renderImageXform(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, Color color) {
        SurfaceData surfaceData;
        SurfaceData sourceSurfaceData;
        if (i2 != 3 && (sourceSurfaceData = (surfaceData = sunGraphics2D.surfaceData).getSourceSurfaceData(image, 4, sunGraphics2D.imageComp, color)) != null && !isBgOperation(sourceSurfaceData, color) && (sourceSurfaceData.getSurfaceType() == OGLSurfaceData.OpenGLTexture || sourceSurfaceData.getSurfaceType() == OGLSurfaceData.OpenGLSurfaceRTT || i2 == 1)) {
            TransformBlit fromCache = TransformBlit.getFromCache(sourceSurfaceData.getSurfaceType(), sunGraphics2D.imageComp, surfaceData.getSurfaceType());
            if (fromCache != null) {
                fromCache.Transform(sourceSurfaceData, surfaceData, sunGraphics2D.composite, sunGraphics2D.getCompClip(), affineTransform, i2, i3, i4, 0, 0, i5 - i3, i6 - i4);
                return;
            }
        }
        super.renderImageXform(sunGraphics2D, image, affineTransform, i2, i3, i4, i5, i6, color);
    }

    @Override // sun.java2d.pipe.DrawImage, sun.java2d.pipe.DrawImagePipe
    public void transformImage(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        if (bufferedImageOp != null) {
            if (bufferedImageOp instanceof AffineTransformOp) {
                AffineTransformOp affineTransformOp = (AffineTransformOp) bufferedImageOp;
                transformImage(sunGraphics2D, bufferedImage, i2, i3, affineTransformOp.getTransform(), affineTransformOp.getInterpolationType());
                return;
            } else if (OGLBufImgOps.renderImageWithOp(sunGraphics2D, bufferedImage, bufferedImageOp, i2, i3)) {
                return;
            } else {
                bufferedImage = bufferedImageOp.filter(bufferedImage, null);
            }
        }
        copyImage(sunGraphics2D, bufferedImage, i2, i3, null);
    }
}
