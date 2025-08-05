package com.sun.prism.impl.ps;

import com.sun.javafx.geom.PickRay;
import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Vec3d;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.Affine3D;
import com.sun.javafx.geom.transform.AffineBase;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.geom.transform.NoninvertibleTransformException;
import com.sun.javafx.sg.prism.NGCamera;
import com.sun.javafx.sg.prism.NGPerspectiveCamera;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.ResourceFactory;
import com.sun.prism.Texture;
import com.sun.prism.impl.BufferUtil;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;
import com.sun.prism.ps.Shader;
import com.sun.prism.ps.ShaderGraphics;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.WeakHashMap;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/PaintHelper.class */
class PaintHelper {
    static final int MULTI_MAX_FRACTIONS = 12;
    private static final int MULTI_TEXTURE_SIZE = 16;
    private static final int MULTI_CACHE_SIZE = 256;
    private static final int GTEX_CLR_TABLE_SIZE = 101;
    private static final int GTEX_CLR_TABLE_MIRRORED_SIZE = 201;
    private static final float FULL_TEXEL_Y = 0.00390625f;
    private static final float HALF_TEXEL_Y = 0.001953125f;
    private static final FloatBuffer stopVals = BufferUtil.newFloatBuffer(48);
    private static final ByteBuffer bgraColors = BufferUtil.newByteBuffer(64);
    private static final Image colorsImg = Image.fromByteBgraPreData(bgraColors, 16, 1);
    private static final int[] previousColors = new int[16];
    private static final byte[] gtexColors = new byte[804];
    private static final Image gtexImg = Image.fromByteBgraPreData(ByteBuffer.wrap(gtexColors), 201, 1);
    private static long cacheOffset = -1;
    private static Texture gradientCacheTexture = null;
    private static Texture gtexCacheTexture = null;
    private static final WeakHashMap<Gradient, Void> gradientMap = new WeakHashMap<>();
    private static final Affine2D scratchXform2D = new Affine2D();
    private static final Affine3D scratchXform3D = new Affine3D();
    private static Color PINK = new Color(1.0f, 0.078431375f, 0.5764706f, 1.0f);

    PaintHelper() {
    }

    private static float len(float dx, float dy) {
        if (dx == 0.0f) {
            return Math.abs(dy);
        }
        if (dy == 0.0f) {
            return Math.abs(dx);
        }
        return (float) Math.sqrt((dx * dx) + (dy * dy));
    }

    static void initGradientTextures(ShaderGraphics g2) {
        cacheOffset = -1L;
        gradientMap.clear();
        gradientCacheTexture = g2.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_TO_EDGE, 16, 256);
        gradientCacheTexture.setLinearFiltering(true);
        gradientCacheTexture.contentsUseful();
        gradientCacheTexture.makePermanent();
        gtexCacheTexture = g2.getResourceFactory().createTexture(PixelFormat.BYTE_BGRA_PRE, Texture.Usage.DEFAULT, Texture.WrapMode.CLAMP_NOT_NEEDED, 201, 256);
        gtexCacheTexture.setLinearFiltering(true);
        gtexCacheTexture.contentsUseful();
        gtexCacheTexture.makePermanent();
    }

    static Texture getGradientTexture(ShaderGraphics g2, Gradient paint) {
        if (gradientCacheTexture == null || gradientCacheTexture.isSurfaceLost()) {
            initGradientTextures(g2);
        }
        gradientCacheTexture.lock();
        return gradientCacheTexture;
    }

    static Texture getWrapGradientTexture(ShaderGraphics g2) {
        if (gtexCacheTexture == null || gtexCacheTexture.isSurfaceLost()) {
            initGradientTextures(g2);
        }
        gtexCacheTexture.lock();
        return gtexCacheTexture;
    }

    private static void stopsToImage(List<Stop> stops, int numStops) {
        Color c2;
        if (numStops > 12) {
            throw new RuntimeException("Maximum number of gradient stops exceeded (paint uses " + numStops + " stops, but max is 12)");
        }
        bgraColors.clear();
        Color lastColor = null;
        for (int i2 = 0; i2 < 16; i2++) {
            if (i2 < numStops) {
                c2 = stops.get(i2).getColor();
                lastColor = c2;
            } else {
                c2 = lastColor;
            }
            c2.putBgraPreBytes(bgraColors);
            int argb = c2.getIntArgbPre();
            if (argb != previousColors[i2]) {
                previousColors[i2] = argb;
            }
        }
        bgraColors.rewind();
    }

    private static void insertInterpColor(byte[] colors, int index, Color c0, Color c1, float t2) {
        float u2 = 255.0f - (t2 * 255.0f);
        int index2 = index * 4;
        colors[index2 + 0] = (byte) ((c0.getBluePremult() * u2) + (c1.getBluePremult() * r0) + 0.5f);
        colors[index2 + 1] = (byte) ((c0.getGreenPremult() * u2) + (c1.getGreenPremult() * r0) + 0.5f);
        colors[index2 + 2] = (byte) ((c0.getRedPremult() * u2) + (c1.getRedPremult() * r0) + 0.5f);
        colors[index2 + 3] = (byte) ((c0.getAlpha() * u2) + (c1.getAlpha() * r0) + 0.5f);
    }

    private static void stopsToGtexImage(List<Stop> stops, int numStops) {
        Color lastColor = stops.get(0).getColor();
        float offset = stops.get(0).getOffset();
        int lastIndex = (int) ((offset * 100.0f) + 0.5f);
        insertInterpColor(gtexColors, 0, lastColor, lastColor, 0.0f);
        for (int i2 = 1; i2 < numStops; i2++) {
            Color color = stops.get(i2).getColor();
            float offset2 = stops.get(i2).getOffset();
            int index = (int) ((offset2 * 100.0f) + 0.5f);
            if (index == lastIndex) {
                insertInterpColor(gtexColors, index, lastColor, color, 0.5f);
            } else {
                for (int j2 = lastIndex + 1; j2 <= index; j2++) {
                    float t2 = j2 - lastIndex;
                    insertInterpColor(gtexColors, j2, lastColor, color, t2 / (index - lastIndex));
                }
            }
            lastIndex = index;
            lastColor = color;
        }
        for (int i3 = 1; i3 < 101; i3++) {
            int j3 = (100 + i3) * 4;
            int k2 = (100 - i3) * 4;
            gtexColors[j3 + 0] = gtexColors[k2 + 0];
            gtexColors[j3 + 1] = gtexColors[k2 + 1];
            gtexColors[j3 + 2] = gtexColors[k2 + 2];
            gtexColors[j3 + 3] = gtexColors[k2 + 3];
        }
    }

    public static int initGradient(Gradient paint) {
        long offset = paint.getGradientOffset();
        if (gradientMap.containsKey(paint) && offset >= 0 && offset > cacheOffset - 256) {
            return (int) (offset % 256);
        }
        List<Stop> stops = paint.getStops();
        int numStops = paint.getNumStops();
        stopsToImage(stops, numStops);
        stopsToGtexImage(stops, numStops);
        long nextOffset = cacheOffset + 1;
        cacheOffset = nextOffset;
        paint.setGradientOffset(nextOffset);
        int cacheIdx = (int) (nextOffset % 256);
        gradientCacheTexture.update(colorsImg, 0, cacheIdx);
        gtexCacheTexture.update(gtexImg, 0, cacheIdx);
        gradientMap.put(paint, null);
        return cacheIdx;
    }

    private static void setMultiGradient(Shader shader, Gradient paint) {
        List<Stop> stops = paint.getStops();
        int numStops = paint.getNumStops();
        stopVals.clear();
        int i2 = 0;
        while (i2 < 12) {
            stopVals.put(i2 < numStops ? stops.get(i2).getOffset() : 0.0f);
            stopVals.put(i2 < numStops - 1 ? 1.0f / (stops.get(i2 + 1).getOffset() - stops.get(i2).getOffset()) : 0.0f);
            stopVals.put(0.0f);
            stopVals.put(0.0f);
            i2++;
        }
        stopVals.rewind();
        shader.setConstants("fractions", stopVals, 0, 12);
        float index_y = initGradient(paint);
        shader.setConstant("offset", (index_y / 256.0f) + HALF_TEXEL_Y);
    }

    private static void setTextureGradient(Shader shader, Gradient paint) {
        float cy = initGradient(paint) + 0.5f;
        float fractmul = 0.0f;
        float clampmul = 0.0f;
        switch (paint.getSpreadMethod()) {
            case 0:
                clampmul = 100.0f;
                break;
            case 1:
                fractmul = 200.0f;
                break;
            case 2:
                fractmul = 100.0f;
                break;
        }
        float xscale = 1.0f / gtexCacheTexture.getPhysicalWidth();
        float yscale = 1.0f / gtexCacheTexture.getPhysicalHeight();
        float cx = 0.5f * xscale;
        shader.setConstant(AbstractDocument.ContentElementName, cx, cy * yscale, fractmul * xscale, clampmul * xscale);
    }

    static void setLinearGradient(ShaderGraphics g2, Shader shader, LinearGradient paint, float rx, float ry, float rw, float rh) {
        BaseTransform inv;
        BaseTransform paintXform = paint.getGradientTransformNoClone();
        Affine3D at2 = scratchXform3D;
        g2.getPaintShaderTransform(at2);
        if (paintXform != null) {
            at2.concatenate(paintXform);
        }
        float x1 = rx + (paint.getX1() * rw);
        float y1 = ry + (paint.getY1() * rh);
        float x2 = rx + (paint.getX2() * rw);
        float y2 = ry + (paint.getY2() * rh);
        at2.translate(x1, y1);
        float x3 = x2 - x1;
        float y3 = y2 - y1;
        double len = len(x3, y3);
        at2.rotate(Math.atan2(y3, x3));
        at2.scale(len, 1.0d);
        if (!at2.is2D()) {
            try {
                inv = at2.createInverse();
            } catch (NoninvertibleTransformException e2) {
                at2.setToScale(0.0d, 0.0d, 0.0d);
                inv = at2;
            }
            NGCamera cam = g2.getCameraNoClone();
            Vec3d tmpVec = new Vec3d();
            PickRay tmpvec = new PickRay();
            PickRay ray00 = project(0.0f, 0.0f, cam, inv, tmpvec, tmpVec, null);
            PickRay ray10 = project(1.0f, 0.0f, cam, inv, tmpvec, tmpVec, null);
            PickRay ray01 = project(0.0f, 1.0f, cam, inv, tmpvec, tmpVec, null);
            double p0 = ray10.getDirectionNoClone().f11930x - ray00.getDirectionNoClone().f11930x;
            double p1 = ray01.getDirectionNoClone().f11930x - ray00.getDirectionNoClone().f11930x;
            double p2 = ray00.getDirectionNoClone().f11930x;
            double p02 = p0 * (-ray00.getOriginNoClone().f11932z);
            double p12 = p1 * (-ray00.getOriginNoClone().f11932z);
            double p22 = p2 * (-ray00.getOriginNoClone().f11932z);
            double wv0 = ray10.getDirectionNoClone().f11932z - ray00.getDirectionNoClone().f11932z;
            double wv1 = ray01.getDirectionNoClone().f11932z - ray00.getDirectionNoClone().f11932z;
            double wv2 = ray00.getDirectionNoClone().f11932z;
            shader.setConstant("gradParams", (float) p02, (float) p12, (float) p22, (float) ray00.getOriginNoClone().f11930x);
            shader.setConstant("perspVec", (float) wv0, (float) wv1, (float) wv2);
        } else {
            try {
                at2.invert();
            } catch (NoninvertibleTransformException e3) {
                at2.setToScale(0.0d, 0.0d, 0.0d);
            }
            double p03 = (float) at2.getMxx();
            double p13 = (float) at2.getMxy();
            double p23 = (float) at2.getMxt();
            shader.setConstant("gradParams", (float) p03, (float) p13, (float) p23, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        setMultiGradient(shader, paint);
    }

    static AffineBase getLinearGradientTx(LinearGradient paint, Shader shader, BaseTransform renderTx, float rx, float ry, float rw, float rh) {
        AffineBase ret;
        float x1 = paint.getX1();
        float y1 = paint.getY1();
        float x2 = paint.getX2();
        float y2 = paint.getY2();
        if (paint.isProportional()) {
            x1 = rx + (x1 * rw);
            y1 = ry + (y1 * rh);
            x2 = rx + (x2 * rw);
            y2 = ry + (y2 * rh);
        }
        float dx = x2 - x1;
        float dy = y2 - y1;
        float len = len(dx, dy);
        if (paint.getSpreadMethod() == 1) {
            len *= 2.0f;
        }
        BaseTransform paintXform = paint.getGradientTransformNoClone();
        if (paintXform.isIdentity() && renderTx.isIdentity()) {
            Affine2D at2 = scratchXform2D;
            at2.setToTranslation(x1, y1);
            at2.rotate(dx, dy);
            at2.scale(len, 1.0d);
            ret = at2;
        } else {
            AffineBase at3 = scratchXform3D;
            at3.setTransform(renderTx);
            at3.concatenate(paintXform);
            at3.translate(x1, y1);
            at3.rotate(Math.atan2(dy, dx));
            at3.scale(len, 1.0d);
            ret = at3;
        }
        try {
            ret.invert();
        } catch (NoninvertibleTransformException e2) {
            scratchXform2D.setToScale(0.0d, 0.0d);
            ret = scratchXform2D;
        }
        setTextureGradient(shader, paint);
        return ret;
    }

    static void setRadialGradient(ShaderGraphics g2, Shader shader, RadialGradient paint, float rx, float ry, float rw, float rh) {
        Affine3D at2 = scratchXform3D;
        g2.getPaintShaderTransform(at2);
        float radius = paint.getRadius();
        float cx = paint.getCenterX();
        float cy = paint.getCenterY();
        float fa = paint.getFocusAngle();
        float fd = paint.getFocusDistance();
        if (fd < 0.0f) {
            fd = -fd;
            fa += 180.0f;
        }
        float fa2 = (float) Math.toRadians(fa);
        if (paint.isProportional()) {
            float bcx = rx + (rw / 2.0f);
            float bcy = ry + (rh / 2.0f);
            float scale = Math.min(rw, rh);
            cx = ((cx - 0.5f) * scale) + bcx;
            cy = ((cy - 0.5f) * scale) + bcy;
            if (rw != rh && rw != 0.0f && rh != 0.0f) {
                at2.translate(bcx, bcy);
                at2.scale(rw / scale, rh / scale);
                at2.translate(-bcx, -bcy);
            }
            radius *= scale;
        }
        BaseTransform paintXform = paint.getGradientTransformNoClone();
        if (paintXform != null) {
            at2.concatenate(paintXform);
        }
        at2.translate(cx, cy);
        at2.rotate(fa2);
        at2.scale(radius, radius);
        try {
            at2.invert();
        } catch (Exception e2) {
            at2.setToScale(0.0d, 0.0d, 0.0d);
        }
        if (!at2.is2D()) {
            NGCamera cam = g2.getCameraNoClone();
            Vec3d tmpVec = new Vec3d();
            PickRay tmpvec = new PickRay();
            PickRay ray00 = project(0.0f, 0.0f, cam, at2, tmpvec, tmpVec, null);
            PickRay ray10 = project(1.0f, 0.0f, cam, at2, tmpvec, tmpVec, null);
            PickRay ray01 = project(0.0f, 1.0f, cam, at2, tmpvec, tmpVec, null);
            double p0 = ray10.getDirectionNoClone().f11930x - ray00.getDirectionNoClone().f11930x;
            double p1 = ray01.getDirectionNoClone().f11930x - ray00.getDirectionNoClone().f11930x;
            double p2 = ray00.getDirectionNoClone().f11930x;
            double py0 = ray10.getDirectionNoClone().f11931y - ray00.getDirectionNoClone().f11931y;
            double py1 = ray01.getDirectionNoClone().f11931y - ray00.getDirectionNoClone().f11931y;
            double py2 = ray00.getDirectionNoClone().f11931y;
            double p02 = p0 * (-ray00.getOriginNoClone().f11932z);
            double p12 = p1 * (-ray00.getOriginNoClone().f11932z);
            double p22 = p2 * (-ray00.getOriginNoClone().f11932z);
            double py02 = py0 * (-ray00.getOriginNoClone().f11932z);
            double py12 = py1 * (-ray00.getOriginNoClone().f11932z);
            double py22 = py2 * (-ray00.getOriginNoClone().f11932z);
            double wv0 = ray10.getDirectionNoClone().f11932z - ray00.getDirectionNoClone().f11932z;
            double wv1 = ray01.getDirectionNoClone().f11932z - ray00.getDirectionNoClone().f11932z;
            double wv2 = ray00.getDirectionNoClone().f11932z;
            shader.setConstant("perspVec", (float) wv0, (float) wv1, (float) wv2);
            shader.setConstant("m0", (float) p02, (float) p12, (float) p22, (float) ray00.getOriginNoClone().f11930x);
            shader.setConstant("m1", (float) py02, (float) py12, (float) py22, (float) ray00.getOriginNoClone().f11931y);
        } else {
            float m00 = (float) at2.getMxx();
            float m01 = (float) at2.getMxy();
            float m02 = (float) at2.getMxt();
            shader.setConstant("m0", m00, m01, m02, 0.0f);
            float m10 = (float) at2.getMyx();
            float m11 = (float) at2.getMyy();
            float m12 = (float) at2.getMyt();
            shader.setConstant("m1", m10, m11, m12, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        float fd2 = Math.min(fd, 0.99f);
        float denom = 1.0f - (fd2 * fd2);
        float inv_denom = 1.0f / denom;
        shader.setConstant("precalc", fd2, denom, inv_denom);
        setMultiGradient(shader, paint);
    }

    static AffineBase getRadialGradientTx(RadialGradient paint, Shader shader, BaseTransform renderTx, float rx, float ry, float rw, float rh) {
        Affine3D at2 = scratchXform3D;
        at2.setTransform(renderTx);
        float radius = paint.getRadius();
        float cx = paint.getCenterX();
        float cy = paint.getCenterY();
        float fa = paint.getFocusAngle();
        float fd = paint.getFocusDistance();
        if (fd < 0.0f) {
            fd = -fd;
            fa += 180.0f;
        }
        float fa2 = (float) Math.toRadians(fa);
        if (paint.isProportional()) {
            float bcx = rx + (rw / 2.0f);
            float bcy = ry + (rh / 2.0f);
            float scale = Math.min(rw, rh);
            cx = ((cx - 0.5f) * scale) + bcx;
            cy = ((cy - 0.5f) * scale) + bcy;
            if (rw != rh && rw != 0.0f && rh != 0.0f) {
                at2.translate(bcx, bcy);
                at2.scale(rw / scale, rh / scale);
                at2.translate(-bcx, -bcy);
            }
            radius *= scale;
        }
        if (paint.getSpreadMethod() == 1) {
            radius *= 2.0f;
        }
        BaseTransform paintXform = paint.getGradientTransformNoClone();
        if (paintXform != null) {
            at2.concatenate(paintXform);
        }
        at2.translate(cx, cy);
        at2.rotate(fa2);
        at2.scale(radius, radius);
        try {
            at2.invert();
        } catch (Exception e2) {
            at2.setToScale(0.0d, 0.0d, 0.0d);
        }
        float fd2 = Math.min(fd, 0.99f);
        float denom = 1.0f - (fd2 * fd2);
        float inv_denom = 1.0f / denom;
        shader.setConstant("precalc", fd2, denom, inv_denom);
        setTextureGradient(shader, paint);
        return at2;
    }

    static void setImagePattern(ShaderGraphics g2, Shader shader, ImagePattern paint, float rx, float ry, float rw, float rh) {
        float x1 = rx + (paint.getX() * rw);
        float y1 = ry + (paint.getY() * rh);
        float x2 = x1 + (paint.getWidth() * rw);
        float y2 = y1 + (paint.getHeight() * rh);
        ResourceFactory rf = g2.getResourceFactory();
        Image img = paint.getImage();
        Texture paintTex = rf.getCachedTexture(img, Texture.WrapMode.REPEAT);
        float cx = paintTex.getContentX();
        float cy = paintTex.getContentY();
        float cw = paintTex.getContentWidth();
        float ch = paintTex.getContentHeight();
        float texw = paintTex.getPhysicalWidth();
        float texh = paintTex.getPhysicalHeight();
        paintTex.unlock();
        Affine3D at2 = scratchXform3D;
        g2.getPaintShaderTransform(at2);
        BaseTransform paintXform = paint.getPatternTransformNoClone();
        if (paintXform != null) {
            at2.concatenate(paintXform);
        }
        at2.translate(x1, y1);
        at2.scale(x2 - x1, y2 - y1);
        if (cw < texw) {
            at2.translate(0.5d / cw, 0.0d);
            cx += 0.5f;
        }
        if (ch < texh) {
            at2.translate(0.0d, 0.5d / ch);
            cy += 0.5f;
        }
        try {
            at2.invert();
        } catch (Exception e2) {
            at2.setToScale(0.0d, 0.0d, 0.0d);
        }
        if (!at2.is2D()) {
            NGCamera cam = g2.getCameraNoClone();
            Vec3d tmpVec = new Vec3d();
            PickRay tmpvec = new PickRay();
            PickRay ray00 = project(0.0f, 0.0f, cam, at2, tmpvec, tmpVec, null);
            PickRay ray10 = project(1.0f, 0.0f, cam, at2, tmpvec, tmpVec, null);
            PickRay ray01 = project(0.0f, 1.0f, cam, at2, tmpvec, tmpVec, null);
            double p0 = ray10.getDirectionNoClone().f11930x - ray00.getDirectionNoClone().f11930x;
            double p1 = ray01.getDirectionNoClone().f11930x - ray00.getDirectionNoClone().f11930x;
            double p2 = ray00.getDirectionNoClone().f11930x;
            double py0 = ray10.getDirectionNoClone().f11931y - ray00.getDirectionNoClone().f11931y;
            double py1 = ray01.getDirectionNoClone().f11931y - ray00.getDirectionNoClone().f11931y;
            double py2 = ray00.getDirectionNoClone().f11931y;
            double p02 = p0 * (-ray00.getOriginNoClone().f11932z);
            double p12 = p1 * (-ray00.getOriginNoClone().f11932z);
            double p22 = p2 * (-ray00.getOriginNoClone().f11932z);
            double py02 = py0 * (-ray00.getOriginNoClone().f11932z);
            double py12 = py1 * (-ray00.getOriginNoClone().f11932z);
            double py22 = py2 * (-ray00.getOriginNoClone().f11932z);
            double wv0 = ray10.getDirectionNoClone().f11932z - ray00.getDirectionNoClone().f11932z;
            double wv1 = ray01.getDirectionNoClone().f11932z - ray00.getDirectionNoClone().f11932z;
            double wv2 = ray00.getDirectionNoClone().f11932z;
            shader.setConstant("perspVec", (float) wv0, (float) wv1, (float) wv2);
            shader.setConstant("xParams", (float) p02, (float) p12, (float) p22, (float) ray00.getOriginNoClone().f11930x);
            shader.setConstant("yParams", (float) py02, (float) py12, (float) py22, (float) ray00.getOriginNoClone().f11931y);
        } else {
            float m00 = (float) at2.getMxx();
            float m01 = (float) at2.getMxy();
            float m02 = (float) at2.getMxt();
            shader.setConstant("xParams", m00, m01, m02, 0.0f);
            float m10 = (float) at2.getMyx();
            float m11 = (float) at2.getMyy();
            float m12 = (float) at2.getMyt();
            shader.setConstant("yParams", m10, m11, m12, 0.0f);
            shader.setConstant("perspVec", 0.0f, 0.0f, 1.0f);
        }
        shader.setConstant(AbstractDocument.ContentElementName, cx / texw, cy / texh, cw / texw, ch / texh);
    }

    static AffineBase getImagePatternTx(ShaderGraphics g2, ImagePattern paint, Shader shader, BaseTransform renderTx, float rx, float ry, float rw, float rh) {
        AffineBase ret;
        float px = paint.getX();
        float py = paint.getY();
        float pw = paint.getWidth();
        float ph = paint.getHeight();
        if (paint.isProportional()) {
            px = rx + (px * rw);
            py = ry + (py * rh);
            pw *= rw;
            ph *= rh;
        }
        ResourceFactory rf = g2.getResourceFactory();
        Image img = paint.getImage();
        Texture paintTex = rf.getCachedTexture(img, Texture.WrapMode.REPEAT);
        float cx = paintTex.getContentX();
        float cy = paintTex.getContentY();
        float cw = paintTex.getContentWidth();
        float ch = paintTex.getContentHeight();
        float texw = paintTex.getPhysicalWidth();
        float texh = paintTex.getPhysicalHeight();
        paintTex.unlock();
        BaseTransform paintXform = paint.getPatternTransformNoClone();
        if (paintXform.isIdentity() && renderTx.isIdentity()) {
            Affine2D at2 = scratchXform2D;
            at2.setToTranslation(px, py);
            at2.scale(pw, ph);
            ret = at2;
        } else {
            AffineBase at3 = scratchXform3D;
            at3.setTransform(renderTx);
            at3.concatenate(paintXform);
            at3.translate(px, py);
            at3.scale(pw, ph);
            ret = at3;
        }
        if (cw < texw) {
            ret.translate(0.5d / cw, 0.0d);
            cx += 0.5f;
        }
        if (ch < texh) {
            ret.translate(0.0d, 0.5d / ch);
            cy += 0.5f;
        }
        try {
            ret.invert();
        } catch (Exception e2) {
            ret = scratchXform2D;
            scratchXform2D.setToScale(0.0d, 0.0d);
        }
        shader.setConstant(AbstractDocument.ContentElementName, cx / texw, cy / texh, cw / texw, ch / texh);
        return ret;
    }

    static PickRay project(float x2, float y2, NGCamera cam, BaseTransform inv, PickRay tmpray, Vec3d tmpvec, Point2D ret) {
        return cam.computePickRay(x2, y2, tmpray).project(inv, cam instanceof NGPerspectiveCamera, tmpvec, ret);
    }
}
