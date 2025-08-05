package sun.java2d.pipe;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import sun.awt.image.PixelConverter;
import sun.java2d.SunGraphics2D;
import sun.java2d.SurfaceData;
import sun.java2d.loops.CompositeType;

/* loaded from: rt.jar:sun/java2d/pipe/BufferedPaints.class */
public class BufferedPaints {
    public static final int MULTI_MAX_FRACTIONS = 12;

    static void setPaint(RenderQueue renderQueue, SunGraphics2D sunGraphics2D, Paint paint, int i2) {
        if (sunGraphics2D.paintState <= 1) {
            setColor(renderQueue, sunGraphics2D.pixel);
        }
        boolean z2 = (i2 & 2) != 0;
        switch (sunGraphics2D.paintState) {
            case 2:
                setGradientPaint(renderQueue, sunGraphics2D, (GradientPaint) paint, z2);
                break;
            case 3:
                setLinearGradientPaint(renderQueue, sunGraphics2D, (LinearGradientPaint) paint, z2);
                break;
            case 4:
                setRadialGradientPaint(renderQueue, sunGraphics2D, (RadialGradientPaint) paint, z2);
                break;
            case 5:
                setTexturePaint(renderQueue, sunGraphics2D, (TexturePaint) paint, z2);
                break;
        }
    }

    static void resetPaint(RenderQueue renderQueue) {
        renderQueue.ensureCapacity(4);
        renderQueue.getBuffer().putInt(100);
    }

    private static void setColor(RenderQueue renderQueue, int i2) {
        renderQueue.ensureCapacity(8);
        RenderBuffer buffer = renderQueue.getBuffer();
        buffer.putInt(101);
        buffer.putInt(i2);
    }

    private static void setGradientPaint(RenderQueue renderQueue, AffineTransform affineTransform, Color color, Color color2, Point2D point2D, Point2D point2D2, boolean z2, boolean z3) {
        double translateX;
        double shearX;
        double scaleX;
        PixelConverter pixelConverter = PixelConverter.ArgbPre.instance;
        int iRgbToPixel = pixelConverter.rgbToPixel(color.getRGB(), null);
        int iRgbToPixel2 = pixelConverter.rgbToPixel(color2.getRGB(), null);
        double x2 = point2D.getX();
        double y2 = point2D.getY();
        affineTransform.translate(x2, y2);
        double x3 = point2D2.getX() - x2;
        double y3 = point2D2.getY() - y2;
        double dSqrt = Math.sqrt((x3 * x3) + (y3 * y3));
        affineTransform.rotate(x3, y3);
        affineTransform.scale(2.0d * dSqrt, 1.0d);
        affineTransform.translate(-0.25d, 0.0d);
        try {
            affineTransform.invert();
            scaleX = affineTransform.getScaleX();
            shearX = affineTransform.getShearX();
            translateX = affineTransform.getTranslateX();
        } catch (NoninvertibleTransformException e2) {
            translateX = 0.0d;
            shearX = 0.0d;
            scaleX = 0.0d;
        }
        renderQueue.ensureCapacityAndAlignment(44, 12);
        RenderBuffer buffer = renderQueue.getBuffer();
        buffer.putInt(102);
        buffer.putInt(z3 ? 1 : 0);
        buffer.putInt(z2 ? 1 : 0);
        buffer.putDouble(scaleX).putDouble(shearX).putDouble(translateX);
        buffer.putInt(iRgbToPixel).putInt(iRgbToPixel2);
    }

    private static void setGradientPaint(RenderQueue renderQueue, SunGraphics2D sunGraphics2D, GradientPaint gradientPaint, boolean z2) {
        setGradientPaint(renderQueue, (AffineTransform) sunGraphics2D.transform.clone(), gradientPaint.getColor1(), gradientPaint.getColor2(), gradientPaint.getPoint1(), gradientPaint.getPoint2(), gradientPaint.isCyclic(), z2);
    }

    private static void setTexturePaint(RenderQueue renderQueue, SunGraphics2D sunGraphics2D, TexturePaint texturePaint, boolean z2) {
        double translateY;
        double scaleY;
        double shearY;
        double translateX;
        double shearX;
        double scaleX;
        SurfaceData sourceSurfaceData = sunGraphics2D.surfaceData.getSourceSurfaceData(texturePaint.getImage(), 0, CompositeType.SrcOver, null);
        boolean z3 = sunGraphics2D.interpolationType != 1;
        AffineTransform affineTransform = (AffineTransform) sunGraphics2D.transform.clone();
        Rectangle2D anchorRect = texturePaint.getAnchorRect();
        affineTransform.translate(anchorRect.getX(), anchorRect.getY());
        double width = anchorRect.getWidth();
        double height = anchorRect.getHeight();
        affineTransform.scale(width, height);
        try {
            affineTransform.invert();
            scaleX = affineTransform.getScaleX();
            shearX = affineTransform.getShearX();
            translateX = affineTransform.getTranslateX();
            shearY = affineTransform.getShearY();
            scaleY = affineTransform.getScaleY();
            translateY = affineTransform.getTranslateY();
        } catch (NoninvertibleTransformException e2) {
            translateY = 0.0d;
            scaleY = height;
            shearY = height;
            translateX = height;
            shearX = height;
            scaleX = height;
        }
        renderQueue.ensureCapacityAndAlignment(68, 12);
        RenderBuffer buffer = renderQueue.getBuffer();
        buffer.putInt(105);
        buffer.putInt(z2 ? 1 : 0);
        buffer.putInt(z3 ? 1 : 0);
        buffer.putLong(sourceSurfaceData.getNativeOps());
        buffer.putDouble(scaleX).putDouble(shearX).putDouble(translateX);
        buffer.putDouble(shearY).putDouble(scaleY).putDouble(translateY);
    }

    public static int convertSRGBtoLinearRGB(int i2) {
        float fPow;
        float f2 = i2 / 255.0f;
        if (f2 <= 0.04045f) {
            fPow = f2 / 12.92f;
        } else {
            fPow = (float) Math.pow((f2 + 0.055d) / 1.055d, 2.4d);
        }
        return Math.round(fPow * 255.0f);
    }

    private static int colorToIntArgbPrePixel(Color color, boolean z2) {
        int rgb = color.getRGB();
        if (!z2 && (rgb >> 24) == -1) {
            return rgb;
        }
        int i2 = rgb >>> 24;
        int iConvertSRGBtoLinearRGB = (rgb >> 16) & 255;
        int iConvertSRGBtoLinearRGB2 = (rgb >> 8) & 255;
        int iConvertSRGBtoLinearRGB3 = rgb & 255;
        if (z2) {
            iConvertSRGBtoLinearRGB = convertSRGBtoLinearRGB(iConvertSRGBtoLinearRGB);
            iConvertSRGBtoLinearRGB2 = convertSRGBtoLinearRGB(iConvertSRGBtoLinearRGB2);
            iConvertSRGBtoLinearRGB3 = convertSRGBtoLinearRGB(iConvertSRGBtoLinearRGB3);
        }
        int i3 = i2 + (i2 >> 7);
        int i4 = (iConvertSRGBtoLinearRGB * i3) >> 8;
        int i5 = (iConvertSRGBtoLinearRGB2 * i3) >> 8;
        return (i2 << 24) | (i4 << 16) | (i5 << 8) | ((iConvertSRGBtoLinearRGB3 * i3) >> 8);
    }

    private static int[] convertToIntArgbPrePixels(Color[] colorArr, boolean z2) {
        int[] iArr = new int[colorArr.length];
        for (int i2 = 0; i2 < colorArr.length; i2++) {
            iArr[i2] = colorToIntArgbPrePixel(colorArr[i2], z2);
        }
        return iArr;
    }

    private static void setLinearGradientPaint(RenderQueue renderQueue, SunGraphics2D sunGraphics2D, LinearGradientPaint linearGradientPaint, boolean z2) {
        float translateX;
        float shearX;
        float scaleX;
        boolean z3 = linearGradientPaint.getColorSpace() == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB;
        Color[] colors = linearGradientPaint.getColors();
        int length = colors.length;
        Point2D startPoint = linearGradientPaint.getStartPoint();
        Point2D endPoint = linearGradientPaint.getEndPoint();
        AffineTransform transform = linearGradientPaint.getTransform();
        transform.preConcatenate(sunGraphics2D.transform);
        if (!z3 && length == 2 && linearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.REPEAT) {
            setGradientPaint(renderQueue, transform, colors[0], colors[1], startPoint, endPoint, linearGradientPaint.getCycleMethod() != MultipleGradientPaint.CycleMethod.NO_CYCLE, z2);
            return;
        }
        int iOrdinal = linearGradientPaint.getCycleMethod().ordinal();
        float[] fractions = linearGradientPaint.getFractions();
        int[] iArrConvertToIntArgbPrePixels = convertToIntArgbPrePixels(colors, z3);
        double x2 = startPoint.getX();
        double y2 = startPoint.getY();
        transform.translate(x2, y2);
        double x3 = endPoint.getX() - x2;
        double y3 = endPoint.getY() - y2;
        double dSqrt = Math.sqrt((x3 * x3) + (y3 * y3));
        transform.rotate(x3, y3);
        transform.scale(dSqrt, 1.0d);
        try {
            transform.invert();
            scaleX = (float) transform.getScaleX();
            shearX = (float) transform.getShearX();
            translateX = (float) transform.getTranslateX();
        } catch (NoninvertibleTransformException e2) {
            translateX = 0.0f;
            shearX = 0.0f;
            scaleX = 0.0f;
        }
        renderQueue.ensureCapacity(32 + (length * 4 * 2));
        RenderBuffer buffer = renderQueue.getBuffer();
        buffer.putInt(103);
        buffer.putInt(z2 ? 1 : 0);
        buffer.putInt(z3 ? 1 : 0);
        buffer.putInt(iOrdinal);
        buffer.putInt(length);
        buffer.putFloat(scaleX);
        buffer.putFloat(shearX);
        buffer.putFloat(translateX);
        buffer.put(fractions);
        buffer.put(iArrConvertToIntArgbPrePixels);
    }

    private static void setRadialGradientPaint(RenderQueue renderQueue, SunGraphics2D sunGraphics2D, RadialGradientPaint radialGradientPaint, boolean z2) {
        boolean z3 = radialGradientPaint.getColorSpace() == MultipleGradientPaint.ColorSpaceType.LINEAR_RGB;
        int iOrdinal = radialGradientPaint.getCycleMethod().ordinal();
        float[] fractions = radialGradientPaint.getFractions();
        Color[] colors = radialGradientPaint.getColors();
        int length = colors.length;
        int[] iArrConvertToIntArgbPrePixels = convertToIntArgbPrePixels(colors, z3);
        Point2D centerPoint = radialGradientPaint.getCenterPoint();
        Point2D focusPoint = radialGradientPaint.getFocusPoint();
        float radius = radialGradientPaint.getRadius();
        double x2 = centerPoint.getX();
        double y2 = centerPoint.getY();
        double x3 = focusPoint.getX();
        double y3 = focusPoint.getY();
        AffineTransform transform = radialGradientPaint.getTransform();
        transform.preConcatenate(sunGraphics2D.transform);
        Point2D point2DTransform = transform.transform(focusPoint, focusPoint);
        transform.translate(x2, y2);
        transform.rotate(x3 - x2, y3 - y2);
        transform.scale(radius, radius);
        try {
            transform.invert();
        } catch (Exception e2) {
            transform.setToScale(0.0d, 0.0d);
        }
        double dMin = Math.min(transform.transform(point2DTransform, point2DTransform).getX(), 0.99d);
        renderQueue.ensureCapacity(48 + (length * 4 * 2));
        RenderBuffer buffer = renderQueue.getBuffer();
        buffer.putInt(104);
        buffer.putInt(z2 ? 1 : 0);
        buffer.putInt(z3 ? 1 : 0);
        buffer.putInt(length);
        buffer.putInt(iOrdinal);
        buffer.putFloat((float) transform.getScaleX());
        buffer.putFloat((float) transform.getShearX());
        buffer.putFloat((float) transform.getTranslateX());
        buffer.putFloat((float) transform.getShearY());
        buffer.putFloat((float) transform.getScaleY());
        buffer.putFloat((float) transform.getTranslateY());
        buffer.putFloat((float) dMin);
        buffer.put(fractions);
        buffer.put(iArrConvertToIntArgbPrePixels);
    }
}
