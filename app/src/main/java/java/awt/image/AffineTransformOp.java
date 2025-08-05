package java.awt.image;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Hashtable;
import sun.awt.image.ImagingLib;

/* loaded from: rt.jar:java/awt/image/AffineTransformOp.class */
public class AffineTransformOp implements BufferedImageOp, RasterOp {
    private AffineTransform xform;
    RenderingHints hints;
    public static final int TYPE_NEAREST_NEIGHBOR = 1;
    public static final int TYPE_BILINEAR = 2;
    public static final int TYPE_BICUBIC = 3;
    int interpolationType;

    public AffineTransformOp(AffineTransform affineTransform, RenderingHints renderingHints) {
        this.interpolationType = 1;
        validateTransform(affineTransform);
        this.xform = (AffineTransform) affineTransform.clone();
        this.hints = renderingHints;
        if (renderingHints != null) {
            Object obj = renderingHints.get(RenderingHints.KEY_INTERPOLATION);
            if (obj == null) {
                Object obj2 = renderingHints.get(RenderingHints.KEY_RENDERING);
                if (obj2 == RenderingHints.VALUE_RENDER_SPEED) {
                    this.interpolationType = 1;
                    return;
                } else {
                    if (obj2 == RenderingHints.VALUE_RENDER_QUALITY) {
                        this.interpolationType = 2;
                        return;
                    }
                    return;
                }
            }
            if (obj == RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR) {
                this.interpolationType = 1;
                return;
            } else if (obj == RenderingHints.VALUE_INTERPOLATION_BILINEAR) {
                this.interpolationType = 2;
                return;
            } else {
                if (obj == RenderingHints.VALUE_INTERPOLATION_BICUBIC) {
                    this.interpolationType = 3;
                    return;
                }
                return;
            }
        }
        this.interpolationType = 1;
    }

    public AffineTransformOp(AffineTransform affineTransform, int i2) {
        this.interpolationType = 1;
        validateTransform(affineTransform);
        this.xform = (AffineTransform) affineTransform.clone();
        switch (i2) {
            case 1:
            case 2:
            case 3:
                this.interpolationType = i2;
                return;
            default:
                throw new IllegalArgumentException("Unknown interpolation type: " + i2);
        }
    }

    public final int getInterpolationType() {
        return this.interpolationType;
    }

    @Override // java.awt.image.BufferedImageOp
    public final BufferedImage filter(BufferedImage bufferedImage, BufferedImage bufferedImage2) {
        BufferedImage bufferedImage3;
        if (bufferedImage == null) {
            throw new NullPointerException("src image is null");
        }
        if (bufferedImage == bufferedImage2) {
            throw new IllegalArgumentException("src image cannot be the same as the dst image");
        }
        boolean z2 = false;
        ColorModel colorModel = bufferedImage.getColorModel();
        BufferedImage bufferedImage4 = bufferedImage2;
        if (bufferedImage2 == null) {
            bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
            bufferedImage4 = bufferedImage2;
        } else {
            ColorModel colorModel2 = bufferedImage2.getColorModel();
            if (colorModel.getColorSpace().getType() != colorModel2.getColorSpace().getType()) {
                int type = this.xform.getType();
                AffineTransform affineTransform = this.xform;
                AffineTransform affineTransform2 = this.xform;
                boolean z3 = (type & (24 | 32)) != 0;
                if (!z3) {
                    AffineTransform affineTransform3 = this.xform;
                    if (type != 1) {
                        AffineTransform affineTransform4 = this.xform;
                        if (type != 0) {
                            double[] dArr = new double[4];
                            this.xform.getMatrix(dArr);
                            z3 = (dArr[0] == ((double) ((int) dArr[0])) && dArr[3] == ((double) ((int) dArr[3]))) ? false : true;
                        }
                    }
                }
                if (z3 && colorModel.getTransparency() == 1) {
                    ColorConvertOp colorConvertOp = new ColorConvertOp(this.hints);
                    int width = bufferedImage.getWidth();
                    int height = bufferedImage.getHeight();
                    if (colorModel2.getTransparency() == 1) {
                        bufferedImage3 = new BufferedImage(width, height, 2);
                    } else {
                        bufferedImage3 = new BufferedImage(colorModel2, colorModel2.createCompatibleWritableRaster(width, height), colorModel2.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
                    }
                    bufferedImage = colorConvertOp.filter(bufferedImage, bufferedImage3);
                } else {
                    z2 = true;
                    bufferedImage2 = createCompatibleDestImage(bufferedImage, null);
                }
            }
        }
        if (this.interpolationType != 1 && (bufferedImage2.getColorModel() instanceof IndexColorModel)) {
            bufferedImage2 = new BufferedImage(bufferedImage2.getWidth(), bufferedImage2.getHeight(), 2);
        }
        if (ImagingLib.filter(this, bufferedImage, bufferedImage2) == null) {
            throw new ImagingOpException("Unable to transform src image");
        }
        if (z2) {
            new ColorConvertOp(this.hints).filter(bufferedImage2, bufferedImage4);
        } else if (bufferedImage4 != bufferedImage2) {
            Graphics2D graphics2DCreateGraphics = bufferedImage4.createGraphics();
            try {
                graphics2DCreateGraphics.setComposite(AlphaComposite.Src);
                graphics2DCreateGraphics.drawImage(bufferedImage2, 0, 0, (ImageObserver) null);
                graphics2DCreateGraphics.dispose();
            } catch (Throwable th) {
                graphics2DCreateGraphics.dispose();
                throw th;
            }
        }
        return bufferedImage4;
    }

    @Override // java.awt.image.RasterOp
    public final WritableRaster filter(Raster raster, WritableRaster writableRaster) {
        if (raster == null) {
            throw new NullPointerException("src image is null");
        }
        if (writableRaster == null) {
            writableRaster = createCompatibleDestRaster(raster);
        }
        if (raster == writableRaster) {
            throw new IllegalArgumentException("src image cannot be the same as the dst image");
        }
        if (raster.getNumBands() != writableRaster.getNumBands()) {
            throw new IllegalArgumentException("Number of src bands (" + raster.getNumBands() + ") does not match number of  dst bands (" + writableRaster.getNumBands() + ")");
        }
        if (ImagingLib.filter(this, raster, writableRaster) == null) {
            throw new ImagingOpException("Unable to transform src image");
        }
        return writableRaster;
    }

    @Override // java.awt.image.BufferedImageOp
    public final Rectangle2D getBounds2D(BufferedImage bufferedImage) {
        return getBounds2D(bufferedImage.getRaster());
    }

    @Override // java.awt.image.RasterOp
    public final Rectangle2D getBounds2D(Raster raster) {
        int width = raster.getWidth();
        int height = raster.getHeight();
        float[] fArr = {0.0f, 0.0f, width, 0.0f, width, height, 0.0f, height};
        this.xform.transform(fArr, 0, fArr, 0, 4);
        float f2 = fArr[0];
        float f3 = fArr[1];
        float f4 = fArr[0];
        float f5 = fArr[1];
        for (int i2 = 2; i2 < 8; i2 += 2) {
            if (fArr[i2] > f2) {
                f2 = fArr[i2];
            } else if (fArr[i2] < f4) {
                f4 = fArr[i2];
            }
            if (fArr[i2 + 1] > f3) {
                f3 = fArr[i2 + 1];
            } else if (fArr[i2 + 1] < f5) {
                f5 = fArr[i2 + 1];
            }
        }
        return new Rectangle2D.Float(f4, f5, f2 - f4, f3 - f5);
    }

    @Override // java.awt.image.BufferedImageOp
    public BufferedImage createCompatibleDestImage(BufferedImage bufferedImage, ColorModel colorModel) {
        BufferedImage bufferedImage2;
        Rectangle bounds = getBounds2D(bufferedImage).getBounds();
        int i2 = bounds.f12372x + bounds.width;
        int i3 = bounds.f12373y + bounds.height;
        if (i2 <= 0) {
            throw new RasterFormatException("Transformed width (" + i2 + ") is less than or equal to 0.");
        }
        if (i3 <= 0) {
            throw new RasterFormatException("Transformed height (" + i3 + ") is less than or equal to 0.");
        }
        if (colorModel == null) {
            ColorModel colorModel2 = bufferedImage.getColorModel();
            if (this.interpolationType != 1 && ((colorModel2 instanceof IndexColorModel) || colorModel2.getTransparency() == 1)) {
                bufferedImage2 = new BufferedImage(i2, i3, 2);
            } else {
                bufferedImage2 = new BufferedImage(colorModel2, bufferedImage.getRaster().createCompatibleWritableRaster(i2, i3), colorModel2.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
            }
        } else {
            bufferedImage2 = new BufferedImage(colorModel, colorModel.createCompatibleWritableRaster(i2, i3), colorModel.isAlphaPremultiplied(), (Hashtable<?, ?>) null);
        }
        return bufferedImage2;
    }

    @Override // java.awt.image.RasterOp
    public WritableRaster createCompatibleDestRaster(Raster raster) {
        Rectangle2D bounds2D = getBounds2D(raster);
        return raster.createCompatibleWritableRaster((int) bounds2D.getX(), (int) bounds2D.getY(), (int) bounds2D.getWidth(), (int) bounds2D.getHeight());
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final Point2D getPoint2D(Point2D point2D, Point2D point2D2) {
        return this.xform.transform(point2D, point2D2);
    }

    public final AffineTransform getTransform() {
        return (AffineTransform) this.xform.clone();
    }

    @Override // java.awt.image.BufferedImageOp, java.awt.image.RasterOp
    public final RenderingHints getRenderingHints() {
        Object obj;
        if (this.hints == null) {
            switch (this.interpolationType) {
                case 1:
                    obj = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
                    break;
                case 2:
                    obj = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
                    break;
                case 3:
                    obj = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
                    break;
                default:
                    throw new InternalError("Unknown interpolation type " + this.interpolationType);
            }
            this.hints = new RenderingHints(RenderingHints.KEY_INTERPOLATION, obj);
        }
        return this.hints;
    }

    void validateTransform(AffineTransform affineTransform) {
        if (Math.abs(affineTransform.getDeterminant()) <= Double.MIN_VALUE) {
            throw new ImagingOpException("Unable to invert transform " + ((Object) affineTransform));
        }
    }
}
