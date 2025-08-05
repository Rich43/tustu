package java.awt.image;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import sun.awt.image.ImagingLib;

/* loaded from: rt.jar:java/awt/image/ConvolveOp.class */
public class ConvolveOp implements BufferedImageOp, RasterOp {
    Kernel kernel;
    int edgeHint;
    RenderingHints hints;
    public static final int EDGE_ZERO_FILL = 0;
    public static final int EDGE_NO_OP = 1;

    public ConvolveOp(Kernel kernel, int i2, RenderingHints renderingHints) {
        this.kernel = kernel;
        this.edgeHint = i2;
        this.hints = renderingHints;
    }

    public ConvolveOp(Kernel kernel) {
        this.kernel = kernel;
        this.edgeHint = 0;
    }

    public int getEdgeCondition() {
        return this.edgeHint;
    }

    public final Kernel getKernel() {
        return (Kernel) this.kernel.clone();
    }

    @Override // java.awt.image.BufferedImageOp
    public final BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        if (bufferedImage == null) {
            throw new NullPointerException("src image is null");
        }
        if (bufferedImage == bufferedImage2) {
            throw new IllegalArgumentException("src image cannot be the same as the dst image");
        }
        boolean z2 = false;
        ColorModel colorModel = bufferedImage.getColorModel();
        BufferedImage bufferedImage3 = bufferedImage2;
        if (colorModel instanceof IndexColorModel) {
            bufferedImage = ((IndexColorModel) colorModel).convertToIntDiscrete(bufferedImage.getRaster(), false);
            colorModel = bufferedImage.getColorModel();
        }
        if (bufferedImage2 == null) {
            bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            bufferedImage3 = bufferedImage2;
        } else {
            ColorModel colorModel2 = bufferedImage2.getColorModel();
            if (colorModel.getColorSpace().getType() != colorModel2.getColorSpace().getType()) {
                z2 = true;
                bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
                bufferedImage2.getColorModel();
            } else if (colorModel2 instanceof IndexColorModel) {
                bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
                bufferedImage2.getColorModel();
            }
        }
        if (ImagingLib.filter(this, bufferedImage, bufferedImage2) == null) {
            throw new ImagingOpException("Unable to convolve src image");
        }
        if (z2) {
            new ColorConvertOp(this.hints).filter(bufferedImage2, bufferedImage3);
        } else if (bufferedImage3 != bufferedImage2) {
            Graphics2D graphics2DCreateGraphics = bufferedImage3.createGraphics();
            try {
                graphics2DCreateGraphics.drawImage(bufferedImage2, 0, 0, (ImageObserver) null);
                graphics2DCreateGraphics.dispose();
            } catch (Throwable th) {
                graphics2DCreateGraphics.dispose();
                throw th;
            }
        }
        return bufferedImage3;
    }

    @Override // java.awt.image.RasterOp
    public final WritableRaster filter(Raster raster, WritableRaster writableRaster) {
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        } else {
            if (raster == writableRaster) {
                throw new IllegalArgumentException("src image cannot be the same as the dst image");
            }
            if (raster.getNumBands() != writableRaster.getNumBands()) {
                throw new ImagingOpException("Different number of bands in src  and dst Rasters");
            }
        }
        if (ImagingLib.filter(this, raster, writableRaster) == null) {
            throw new ImagingOpException("Unable to convolve src image");
        }
        return writableRaster;
    }

    @Override // java.awt.image.BufferedImageOp
    public BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        WritableRaster writableRasterCreateCompatibleWritableRaster = null;
        if (colorModel == null) {
            colorModel = bufferedImage.getColorModel();
            if (colorModel instanceof IndexColorModel) {
                colorModel = ColorModel.getRGBdefault();
            } else {
                writableRasterCreateCompatibleWritableRaster = bufferedImage.getData().createCompatibleWritableRaster(width, height);
            }
        }
        if (writableRasterCreateCompatibleWritableRaster == null) {
            writableRasterCreateCompatibleWritableRaster = colorModel.createCompatibleWritableRaster(width, height);
        }
        return new BufferedImage(colorModel, writableRasterCreateCompatibleWritableRaster, colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster raster) {
        return raster.createCompatibleWritableRaster();
    }

    @Override // java.awt.image.BufferedImageOp
    public final Rectangle2D getBounds2D(BufferedImage bufferedImage) {
        return getBounds2D(bufferedImage.getRaster());
    }

    @Override // java.awt.image.RasterOp
    public final Rectangle2D getBounds2D(Raster raster) {
        return raster.getBounds();
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        if (point2D2 == null) {
            point2D2 = new Point2D.Float();
        }
        point2D2.setLocation(point2D.getX(), point2D.getY());
        return point2D2;
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final RenderingHints getRenderingHints() {
        return this.hints;
    }
}
