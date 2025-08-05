package sun.java2d.pipe;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;
import java.awt.image.ImageObserver;
import java.awt.image.IndexColorModel;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;
import sun.awt.image.BytePackedRaster;
import sun.awt.image.SurfaceManager;
import sun.awt.image.ToolkitImage;
import sun.java2d.InvalidPipeException;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.Blit;
import sun.java2d.loops.BlitBg;
import sun.java2d.loops.CompositeType;
import sun.java2d.loops.MaskBlit;
import sun.java2d.loops.ScaledBlit;
import sun.java2d.loops.SurfaceType;
import sun.java2d.loops.TransformHelper;

/* loaded from: rt.jar:sun/java2d/pipe/DrawImage.class */
public class DrawImage implements DrawImagePipe {
    private static final double MAX_TX_ERROR = 1.0E-4d;

    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, Color color) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (isSimpleTranslate(sunGraphics2D)) {
            return renderImageCopy(sunGraphics2D, image, color, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, 0, 0, width, height);
        }
        AffineTransform affineTransform = sunGraphics2D.transform;
        if ((i2 | i3) != 0) {
            affineTransform = new AffineTransform(affineTransform);
            affineTransform.translate(i2, i3);
        }
        transformImage(sunGraphics2D, image, affineTransform, sunGraphics2D.interpolationType, 0, 0, width, height, color);
        return true;
    }

    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, Color color) {
        if (isSimpleTranslate(sunGraphics2D)) {
            return renderImageCopy(sunGraphics2D, image, color, i2 + sunGraphics2D.transX, i3 + sunGraphics2D.transY, i4, i5, i6, i7);
        }
        scaleImage(sunGraphics2D, image, i2, i3, i2 + i6, i3 + i7, i4, i5, i4 + i6, i5 + i7, color);
        return true;
    }

    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, Color color) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (i4 > 0 && i5 > 0 && isSimpleTranslate(sunGraphics2D)) {
            double d2 = i2 + sunGraphics2D.transX;
            double d3 = i3 + sunGraphics2D.transY;
            if (renderImageScale(sunGraphics2D, image, color, sunGraphics2D.interpolationType, 0, 0, width, height, d2, d3, d2 + i4, d3 + i5)) {
                return true;
            }
        }
        AffineTransform affineTransform = sunGraphics2D.transform;
        if ((i2 | i3) != 0 || i4 != width || i5 != height) {
            affineTransform = new AffineTransform(affineTransform);
            affineTransform.translate(i2, i3);
            affineTransform.scale(i4 / width, i5 / height);
        }
        transformImage(sunGraphics2D, image, affineTransform, sunGraphics2D.interpolationType, 0, 0, width, height, color);
        return true;
    }

    protected void transformImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, AffineTransform affineTransform, int i4) {
        boolean z2;
        int type = affineTransform.getType();
        int width = image.getWidth(null);
        int height = image.getHeight(null);
        if (sunGraphics2D.transformState <= 2 && (type == 0 || type == 1)) {
            double translateX = affineTransform.getTranslateX();
            double translateY = affineTransform.getTranslateY();
            double translateX2 = translateX + sunGraphics2D.transform.getTranslateX();
            double translateY2 = translateY + sunGraphics2D.transform.getTranslateY();
            int iFloor = (int) Math.floor(translateX2 + 0.5d);
            int iFloor2 = (int) Math.floor(translateY2 + 0.5d);
            if (i4 == 1 || (closeToInteger(iFloor, translateX2) && closeToInteger(iFloor2, translateY2))) {
                renderImageCopy(sunGraphics2D, image, null, i2 + iFloor, i3 + iFloor2, 0, 0, width, height);
                return;
            }
            z2 = false;
        } else if (sunGraphics2D.transformState <= 3 && (type & 120) == 0) {
            double[] dArr = {0.0d, 0.0d, width, height};
            affineTransform.transform(dArr, 0, dArr, 0, 2);
            dArr[0] = dArr[0] + i2;
            dArr[1] = dArr[1] + i3;
            dArr[2] = dArr[2] + i2;
            dArr[3] = dArr[3] + i3;
            sunGraphics2D.transform.transform(dArr, 0, dArr, 0, 2);
            if (tryCopyOrScale(sunGraphics2D, image, 0, 0, width, height, null, i4, dArr)) {
                return;
            } else {
                z2 = false;
            }
        } else {
            z2 = true;
        }
        AffineTransform affineTransform2 = new AffineTransform(sunGraphics2D.transform);
        affineTransform2.translate(i2, i3);
        affineTransform2.concatenate(affineTransform);
        if (z2) {
            transformImage(sunGraphics2D, image, affineTransform2, i4, 0, 0, width, height, null);
        } else {
            renderImageXform(sunGraphics2D, image, affineTransform2, i4, 0, 0, width, height, null);
        }
    }

    protected void transformImage(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, Color color) {
        double d2 = i6 - i4;
        double[] dArr = {0.0d, 0.0d, i5 - i3, d2, 0.0d, d2};
        affineTransform.transform(dArr, 0, dArr, 0, 3);
        if (Math.abs(dArr[0] - dArr[4]) < MAX_TX_ERROR && Math.abs(dArr[3] - dArr[5]) < MAX_TX_ERROR && tryCopyOrScale(sunGraphics2D, image, i3, i4, i5, i6, color, i2, dArr)) {
            return;
        }
        renderImageXform(sunGraphics2D, image, affineTransform, i2, i3, i4, i5, i6, color);
    }

    protected boolean tryCopyOrScale(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, Color color, int i6, double[] dArr) {
        double d2 = dArr[0];
        double d3 = dArr[1];
        double d4 = dArr[2];
        double d5 = dArr[3];
        double d6 = d4 - d2;
        double d7 = d5 - d3;
        if (d2 < -2.147483648E9d || d2 > 2.147483647E9d || d3 < -2.147483648E9d || d3 > 2.147483647E9d || d4 < -2.147483648E9d || d4 > 2.147483647E9d || d5 < -2.147483648E9d || d5 > 2.147483647E9d) {
            return false;
        }
        if (closeToInteger(i4 - i2, d6) && closeToInteger(i5 - i3, d7)) {
            int iFloor = (int) Math.floor(d2 + 0.5d);
            int iFloor2 = (int) Math.floor(d3 + 0.5d);
            if (i6 == 1 || (closeToInteger(iFloor, d2) && closeToInteger(iFloor2, d3))) {
                renderImageCopy(sunGraphics2D, image, color, iFloor, iFloor2, i2, i3, i4 - i2, i5 - i3);
                return true;
            }
        }
        if (d6 > 0.0d && d7 > 0.0d && renderImageScale(sunGraphics2D, image, color, i6, i2, i3, i4, i5, d2, d3, d4, d5)) {
            return true;
        }
        return false;
    }

    BufferedImage makeBufferedImage(Image image, Color color, int i2, int i3, int i4, int i5, int i6) {
        int i7 = i5 - i3;
        int i8 = i6 - i4;
        BufferedImage bufferedImage = new BufferedImage(i7, i8, i2);
        SunGraphics2D sunGraphics2D = (SunGraphics2D) bufferedImage.createGraphics();
        sunGraphics2D.setComposite(AlphaComposite.Src);
        bufferedImage.setAccelerationPriority(0.0f);
        if (color != null) {
            sunGraphics2D.setColor(color);
            sunGraphics2D.fillRect(0, 0, i7, i8);
            sunGraphics2D.setComposite(AlphaComposite.SrcOver);
        }
        sunGraphics2D.copyImage(image, 0, 0, i3, i4, i7, i8, null, null);
        sunGraphics2D.dispose();
        return bufferedImage;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [double[]] */
    protected void renderImageXform(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, int i2, int i3, int i4, int i5, int i6, Color color) {
        try {
            AffineTransform affineTransformCreateInverse = affineTransform.createInverse();
            double d2 = i5 - i3;
            double d3 = i6 - i4;
            ?? r0 = {0.0d, 0.0d, d2, 0.0d, 0.0d, d3, d2, d3};
            affineTransform.transform((double[]) r0, 0, (double[]) r0, 0, 4);
            double d4 = r0;
            double d5 = r0[0];
            double d6 = r0;
            double d7 = r0[1];
            for (int i7 = 2; i7 < r0.length; i7 += 2) {
                long j2 = r0[i7];
                if (d5 > j2) {
                    d5 = j2;
                } else if (d4 < j2) {
                    d4 = j2;
                }
                long j3 = r0[i7 + 1];
                if (d7 > j3) {
                    d7 = j3;
                } else if (d6 < j3) {
                    d6 = j3;
                }
            }
            Region compClip = sunGraphics2D.getCompClip();
            int iMax = Math.max((int) Math.floor(d5), compClip.lox);
            int iMax2 = Math.max((int) Math.floor(d7), compClip.loy);
            int iMin = Math.min((int) Math.ceil(d4), compClip.hix);
            int iMin2 = Math.min((int) Math.ceil(d6), compClip.hiy);
            if (iMin <= iMax || iMin2 <= iMax2) {
                return;
            }
            SurfaceData surfaceData = sunGraphics2D.surfaceData;
            SurfaceData sourceSurfaceData = surfaceData.getSourceSurfaceData(image, 4, sunGraphics2D.imageComp, color);
            if (sourceSurfaceData == null) {
                image = getBufferedImage(image);
                sourceSurfaceData = surfaceData.getSourceSurfaceData(image, 4, sunGraphics2D.imageComp, color);
                if (sourceSurfaceData == null) {
                    return;
                }
            }
            if (isBgOperation(sourceSurfaceData, color)) {
                image = makeBufferedImage(image, color, 1, i3, i4, i5, i6);
                i5 -= i3;
                i6 -= i4;
                i4 = 0;
                i3 = 0;
                sourceSurfaceData = surfaceData.getSourceSurfaceData(image, 4, sunGraphics2D.imageComp, color);
            }
            TransformHelper fromCache = TransformHelper.getFromCache(sourceSurfaceData.getSurfaceType());
            if (fromCache == null) {
                BufferedImage bufferedImageMakeBufferedImage = makeBufferedImage(image, null, sourceSurfaceData.getTransparency() == 1 ? 1 : 2, i3, i4, i5, i6);
                i5 -= i3;
                i6 -= i4;
                i4 = 0;
                i3 = 0;
                sourceSurfaceData = surfaceData.getSourceSurfaceData(bufferedImageMakeBufferedImage, 4, sunGraphics2D.imageComp, null);
                fromCache = TransformHelper.getFromCache(sourceSurfaceData.getSurfaceType());
            }
            SurfaceType surfaceType = surfaceData.getSurfaceType();
            if (sunGraphics2D.compositeState <= 1) {
                MaskBlit fromCache2 = MaskBlit.getFromCache(SurfaceType.IntArgbPre, sunGraphics2D.imageComp, surfaceType);
                if (fromCache2.getNativePrim() != 0) {
                    fromCache.Transform(fromCache2, sourceSurfaceData, surfaceData, sunGraphics2D.composite, compClip, affineTransformCreateInverse, i2, i3, i4, i5, i6, iMax, iMax2, iMin, iMin2, null, 0, 0);
                    return;
                }
            }
            int i8 = iMin - iMax;
            int i9 = iMin2 - iMax2;
            SurfaceData primarySurfaceData = SurfaceData.getPrimarySurfaceData(new BufferedImage(i8, i9, 3));
            SurfaceType surfaceType2 = primarySurfaceData.getSurfaceType();
            MaskBlit fromCache3 = MaskBlit.getFromCache(SurfaceType.IntArgbPre, CompositeType.SrcNoEa, surfaceType2);
            int[] iArr = new int[(i9 * 2) + 2];
            fromCache.Transform(fromCache3, sourceSurfaceData, primarySurfaceData, AlphaComposite.Src, null, affineTransformCreateInverse, i2, i3, i4, i5, i6, 0, 0, i8, i9, iArr, iMax, iMax2);
            Blit.getFromCache(surfaceType2, sunGraphics2D.imageComp, surfaceType).Blit(primarySurfaceData, surfaceData, sunGraphics2D.composite, compClip.getIntersection(Region.getInstance(iMax, iMax2, iMin, iMin2, iArr)), 0, 0, iMax, iMax2, i8, i9);
        } catch (NoninvertibleTransformException e2) {
        }
    }

    protected boolean renderImageCopy(SunGraphics2D sunGraphics2D, Image image, Color color, int i2, int i3, int i4, int i5, int i6, int i7) {
        Region compClip = sunGraphics2D.getCompClip();
        SurfaceData surfaceData = sunGraphics2D.surfaceData;
        int i8 = 0;
        do {
            SurfaceData sourceSurfaceData = surfaceData.getSourceSurfaceData(image, 0, sunGraphics2D.imageComp, color);
            if (sourceSurfaceData == null) {
                return false;
            }
            try {
                blitSurfaceData(sunGraphics2D, compClip, sourceSurfaceData, surfaceData, sourceSurfaceData.getSurfaceType(), surfaceData.getSurfaceType(), i4, i5, i2, i3, i6, i7, color);
                return true;
            } catch (NullPointerException e2) {
                if (!SurfaceData.isNull(surfaceData) && !SurfaceData.isNull(sourceSurfaceData)) {
                    throw e2;
                }
                return false;
            } catch (InvalidPipeException e3) {
                i8++;
                compClip = sunGraphics2D.getCompClip();
                surfaceData = sunGraphics2D.surfaceData;
                if (SurfaceData.isNull(surfaceData) || SurfaceData.isNull(sourceSurfaceData)) {
                    return false;
                }
            }
        } while (i8 <= 1);
        return false;
    }

    protected boolean renderImageScale(SunGraphics2D sunGraphics2D, Image image, Color color, int i2, int i3, int i4, int i5, int i6, double d2, double d3, double d4, double d5) {
        if (i2 != 1) {
            return false;
        }
        Region compClip = sunGraphics2D.getCompClip();
        SurfaceData surfaceData = sunGraphics2D.surfaceData;
        int i7 = 0;
        do {
            SurfaceData sourceSurfaceData = surfaceData.getSourceSurfaceData(image, 3, sunGraphics2D.imageComp, color);
            if (sourceSurfaceData == null || isBgOperation(sourceSurfaceData, color)) {
                return false;
            }
            try {
                return scaleSurfaceData(sunGraphics2D, compClip, sourceSurfaceData, surfaceData, sourceSurfaceData.getSurfaceType(), surfaceData.getSurfaceType(), i3, i4, i5, i6, d2, d3, d4, d5);
            } catch (NullPointerException e2) {
                if (!SurfaceData.isNull(surfaceData)) {
                    throw e2;
                }
                return false;
            } catch (InvalidPipeException e3) {
                i7++;
                compClip = sunGraphics2D.getCompClip();
                surfaceData = sunGraphics2D.surfaceData;
                if (SurfaceData.isNull(surfaceData) || SurfaceData.isNull(sourceSurfaceData)) {
                    return false;
                }
            }
        } while (i7 <= 1);
        return false;
    }

    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color) {
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        if (i8 > i6) {
            i10 = i8 - i6;
            i11 = i6;
        } else {
            z2 = true;
            i10 = i6 - i8;
            i11 = i8;
        }
        if (i9 > i7) {
            i12 = i9 - i7;
            i13 = i7;
        } else {
            z3 = true;
            i12 = i7 - i9;
            i13 = i9;
        }
        if (i4 > i2) {
            i14 = i4 - i2;
            i15 = i2;
        } else {
            i14 = i2 - i4;
            z4 = true;
            i15 = i4;
        }
        if (i5 > i3) {
            i16 = i5 - i3;
            i17 = i3;
        } else {
            i16 = i3 - i5;
            z5 = true;
            i17 = i5;
        }
        if (i10 <= 0 || i12 <= 0) {
            return true;
        }
        if (z2 == z4 && z3 == z5 && isSimpleTranslate(sunGraphics2D)) {
            double d2 = i15 + sunGraphics2D.transX;
            double d3 = i17 + sunGraphics2D.transY;
            if (renderImageScale(sunGraphics2D, image, color, sunGraphics2D.interpolationType, i11, i13, i11 + i10, i13 + i12, d2, d3, d2 + i14, d3 + i16)) {
                return true;
            }
        }
        AffineTransform affineTransform = new AffineTransform(sunGraphics2D.transform);
        affineTransform.translate(i2, i3);
        affineTransform.scale((i4 - i2) / (i8 - i6), (i5 - i3) / (i9 - i7));
        affineTransform.translate(i11 - i6, i13 - i7);
        int imageScale = SurfaceManager.getImageScale(image);
        int width = image.getWidth(null) * imageScale;
        int height = image.getHeight(null) * imageScale;
        int i18 = i10 + i11;
        int i19 = i12 + i13;
        if (i18 > width) {
            i18 = width;
        }
        if (i19 > height) {
            i19 = height;
        }
        if (i11 < 0) {
            affineTransform.translate(-i11, 0.0d);
            i11 = 0;
        }
        if (i13 < 0) {
            affineTransform.translate(0.0d, -i13);
            i13 = 0;
        }
        if (i11 >= i18 || i13 >= i19) {
            return true;
        }
        transformImage(sunGraphics2D, image, affineTransform, sunGraphics2D.interpolationType, i11, i13, i18, i19, color);
        return true;
    }

    public static boolean closeToInteger(int i2, double d2) {
        return Math.abs(d2 - ((double) i2)) < MAX_TX_ERROR;
    }

    public static boolean isSimpleTranslate(SunGraphics2D sunGraphics2D) {
        int i2 = sunGraphics2D.transformState;
        if (i2 <= 1) {
            return true;
        }
        if (i2 < 3 && sunGraphics2D.interpolationType == 1) {
            return true;
        }
        return false;
    }

    protected static boolean isBgOperation(SurfaceData surfaceData, Color color) {
        return surfaceData == null || !(color == null || surfaceData.getTransparency() == 1);
    }

    protected BufferedImage getBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        return ((VolatileImage) image).getSnapshot();
    }

    private ColorModel getTransformColorModel(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, AffineTransform affineTransform) {
        ColorModel colorModel = bufferedImage.getColorModel();
        ColorModel rGBdefault = colorModel;
        if (affineTransform.isIdentity()) {
            return rGBdefault;
        }
        int type = affineTransform.getType();
        boolean z2 = (type & 56) != 0;
        if (!z2 && type != 1 && type != 0) {
            double[] dArr = new double[4];
            affineTransform.getMatrix(dArr);
            z2 = (dArr[0] == ((double) ((int) dArr[0])) && dArr[3] == ((double) ((int) dArr[3]))) ? false : true;
        }
        if (sunGraphics2D.renderHint != 2) {
            if (colorModel instanceof IndexColorModel) {
                WritableRaster raster = bufferedImage.getRaster();
                IndexColorModel indexColorModel = (IndexColorModel) colorModel;
                if (z2 && colorModel.getTransparency() == 1) {
                    if (raster instanceof BytePackedRaster) {
                        rGBdefault = ColorModel.getRGBdefault();
                    } else {
                        double[] dArr2 = new double[6];
                        affineTransform.getMatrix(dArr2);
                        if (dArr2[1] != 0.0d || dArr2[2] != 0.0d || dArr2[4] != 0.0d || dArr2[5] != 0.0d) {
                            int mapSize = indexColorModel.getMapSize();
                            if (mapSize < 256) {
                                int[] iArr = new int[mapSize + 1];
                                indexColorModel.getRGBs(iArr);
                                iArr[mapSize] = 0;
                                rGBdefault = new IndexColorModel(indexColorModel.getPixelSize(), mapSize + 1, iArr, 0, true, mapSize, 0);
                            } else {
                                rGBdefault = ColorModel.getRGBdefault();
                            }
                        }
                    }
                }
            } else if (z2 && colorModel.getTransparency() == 1) {
                rGBdefault = ColorModel.getRGBdefault();
            }
        } else if ((colorModel instanceof IndexColorModel) || (z2 && colorModel.getTransparency() == 1)) {
            rGBdefault = ColorModel.getRGBdefault();
        }
        return rGBdefault;
    }

    protected void blitSurfaceData(SunGraphics2D sunGraphics2D, Region region, SurfaceData surfaceData, SurfaceData surfaceData2, SurfaceType surfaceType, SurfaceType surfaceType2, int i2, int i3, int i4, int i5, int i6, int i7, Color color) {
        if (i6 <= 0 || i7 <= 0) {
            return;
        }
        CompositeType compositeType = sunGraphics2D.imageComp;
        if (CompositeType.SrcOverNoEa.equals(compositeType) && (surfaceData.getTransparency() == 1 || (color != null && color.getTransparency() == 1))) {
            compositeType = CompositeType.SrcNoEa;
        }
        if (!isBgOperation(surfaceData, color)) {
            Blit.getFromCache(surfaceType, compositeType, surfaceType2).Blit(surfaceData, surfaceData2, sunGraphics2D.composite, region, i2, i3, i4, i5, i6, i7);
        } else {
            BlitBg.getFromCache(surfaceType, compositeType, surfaceType2).BlitBg(surfaceData, surfaceData2, sunGraphics2D.composite, region, color.getRGB(), i2, i3, i4, i5, i6, i7);
        }
    }

    protected boolean scaleSurfaceData(SunGraphics2D sunGraphics2D, Region region, SurfaceData surfaceData, SurfaceData surfaceData2, SurfaceType surfaceType, SurfaceType surfaceType2, int i2, int i3, int i4, int i5, double d2, double d3, double d4, double d5) {
        CompositeType compositeType = sunGraphics2D.imageComp;
        if (CompositeType.SrcOverNoEa.equals(compositeType) && surfaceData.getTransparency() == 1) {
            compositeType = CompositeType.SrcNoEa;
        }
        ScaledBlit fromCache = ScaledBlit.getFromCache(surfaceType, compositeType, surfaceType2);
        if (fromCache != null) {
            fromCache.Scale(surfaceData, surfaceData2, sunGraphics2D.composite, region, i2, i3, i4, i5, d2, d3, d4, d5);
            return true;
        }
        return false;
    }

    protected static boolean imageReady(ToolkitImage toolkitImage, ImageObserver imageObserver) {
        if (toolkitImage.hasError()) {
            if (imageObserver != null) {
                imageObserver.imageUpdate(toolkitImage, 192, -1, -1, -1, -1);
                return false;
            }
            return false;
        }
        return true;
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, Color color, ImageObserver imageObserver) {
        if (!(image instanceof ToolkitImage)) {
            return copyImage(sunGraphics2D, image, i2, i3, color);
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (!imageReady(toolkitImage, imageObserver)) {
            return false;
        }
        return toolkitImage.getImageRep().drawToBufImage(sunGraphics2D, toolkitImage, i2, i3, color, imageObserver);
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean copyImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, Color color, ImageObserver imageObserver) {
        if (!(image instanceof ToolkitImage)) {
            return copyImage(sunGraphics2D, image, i2, i3, i4, i5, i6, i7, color);
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (!imageReady(toolkitImage, imageObserver)) {
            return false;
        }
        return toolkitImage.getImageRep().drawToBufImage(sunGraphics2D, toolkitImage, i2, i3, i2 + i6, i3 + i7, i4, i5, i4 + i6, i5 + i7, color, imageObserver);
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, Color color, ImageObserver imageObserver) {
        if (!(image instanceof ToolkitImage)) {
            return scaleImage(sunGraphics2D, image, i2, i3, i4, i5, color);
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (!imageReady(toolkitImage, imageObserver)) {
            return false;
        }
        return toolkitImage.getImageRep().drawToBufImage(sunGraphics2D, toolkitImage, i2, i3, i4, i5, color, imageObserver);
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean scaleImage(SunGraphics2D sunGraphics2D, Image image, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, Color color, ImageObserver imageObserver) {
        if (!(image instanceof ToolkitImage)) {
            return scaleImage(sunGraphics2D, image, i2, i3, i4, i5, i6, i7, i8, i9, color);
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (!imageReady(toolkitImage, imageObserver)) {
            return false;
        }
        return toolkitImage.getImageRep().drawToBufImage(sunGraphics2D, toolkitImage, i2, i3, i4, i5, i6, i7, i8, i9, color, imageObserver);
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public boolean transformImage(SunGraphics2D sunGraphics2D, Image image, AffineTransform affineTransform, ImageObserver imageObserver) {
        if (!(image instanceof ToolkitImage)) {
            transformImage(sunGraphics2D, image, 0, 0, affineTransform, sunGraphics2D.interpolationType);
            return true;
        }
        ToolkitImage toolkitImage = (ToolkitImage) image;
        if (!imageReady(toolkitImage, imageObserver)) {
            return false;
        }
        return toolkitImage.getImageRep().drawToBufImage(sunGraphics2D, toolkitImage, affineTransform, imageObserver);
    }

    @Override // sun.java2d.pipe.DrawImagePipe
    public void transformImage(SunGraphics2D sunGraphics2D, BufferedImage bufferedImage, BufferedImageOp bufferedImageOp, int i2, int i3) {
        if (bufferedImageOp != null) {
            if (bufferedImageOp instanceof AffineTransformOp) {
                AffineTransformOp affineTransformOp = (AffineTransformOp) bufferedImageOp;
                transformImage(sunGraphics2D, bufferedImage, i2, i3, affineTransformOp.getTransform(), affineTransformOp.getInterpolationType());
                return;
            }
            bufferedImage = bufferedImageOp.filter(bufferedImage, null);
        }
        copyImage(sunGraphics2D, bufferedImage, i2, i3, null);
    }
}
