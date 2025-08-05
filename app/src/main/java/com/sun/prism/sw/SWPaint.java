package com.sun.prism.sw;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.pisces.PiscesRenderer;
import com.sun.pisces.Transform6;
import com.sun.prism.Image;
import com.sun.prism.PixelFormat;
import com.sun.prism.Texture;
import com.sun.prism.impl.PrismSettings;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.ImagePattern;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;

/* loaded from: jfxrt.jar:com/sun/prism/sw/SWPaint.class */
final class SWPaint {
    private final SWContext context;
    private final PiscesRenderer pr;
    private final BaseTransform paintTx = new Affine2D();
    private final Transform6 piscesTx = new Transform6();
    private float compositeAlpha = 1.0f;
    private float px;
    private float py;
    private float pw;
    private float ph;

    SWPaint(SWContext context, PiscesRenderer pr) {
        this.context = context;
        this.pr = pr;
    }

    float getCompositeAlpha() {
        return this.compositeAlpha;
    }

    void setCompositeAlpha(float newValue) {
        this.compositeAlpha = newValue;
    }

    void setColor(Color c2, float compositeAlpha) {
        if (PrismSettings.debug) {
            System.out.println("PR.setColor: " + ((Object) c2));
        }
        this.pr.setColor((int) (c2.getRed() * 255.0f), (int) (255.0f * c2.getGreen()), (int) (255.0f * c2.getBlue()), (int) (255.0f * c2.getAlpha() * compositeAlpha));
    }

    void setPaintFromShape(Paint p2, BaseTransform tx, Shape shape, RectBounds nodeBounds, float localX, float localY, float localWidth, float localHeight) {
        computePaintBounds(p2, shape, nodeBounds, localX, localY, localWidth, localHeight);
        setPaintBeforeDraw(p2, tx, this.px, this.py, this.pw, this.ph);
    }

    private void computePaintBounds(Paint p2, Shape shape, RectBounds nodeBounds, float localX, float localY, float localWidth, float localHeight) {
        if (p2.isProportional()) {
            if (nodeBounds != null) {
                this.px = nodeBounds.getMinX();
                this.py = nodeBounds.getMinY();
                this.pw = nodeBounds.getWidth();
                this.ph = nodeBounds.getHeight();
                return;
            }
            if (shape != null) {
                RectBounds bounds = shape.getBounds();
                this.px = bounds.getMinX();
                this.py = bounds.getMinY();
                this.pw = bounds.getWidth();
                this.ph = bounds.getHeight();
                return;
            }
            this.px = localX;
            this.py = localY;
            this.pw = localWidth;
            this.ph = localHeight;
            return;
        }
        this.ph = 0.0f;
        this.pw = 0.0f;
        this.py = 0.0f;
        this.px = 0.0f;
    }

    void setPaintBeforeDraw(Paint p2, BaseTransform tx, float x2, float y2, float width, float height) {
        switch (p2.getType()) {
            case COLOR:
                setColor((Color) p2, this.compositeAlpha);
                return;
            case LINEAR_GRADIENT:
                LinearGradient lg = (LinearGradient) p2;
                if (PrismSettings.debug) {
                    System.out.println("PR.setLinearGradient: " + lg.getX1() + ", " + lg.getY1() + ", " + lg.getX2() + ", " + lg.getY2());
                }
                this.paintTx.setTransform(tx);
                SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
                float x1 = lg.getX1();
                float y1 = lg.getY1();
                float x22 = lg.getX2();
                float y22 = lg.getY2();
                if (lg.isProportional()) {
                    x1 = x2 + (width * x1);
                    y1 = y2 + (height * y1);
                    x22 = x2 + (width * x22);
                    y22 = y2 + (height * y22);
                }
                this.pr.setLinearGradient((int) (65536.0f * x1), (int) (65536.0f * y1), (int) (65536.0f * x22), (int) (65536.0f * y22), getFractions(lg), getARGB(lg, this.compositeAlpha), getPiscesGradientCycleMethod(lg.getSpreadMethod()), this.piscesTx);
                return;
            case RADIAL_GRADIENT:
                RadialGradient rg = (RadialGradient) p2;
                if (PrismSettings.debug) {
                    System.out.println("PR.setRadialGradient: " + rg.getCenterX() + ", " + rg.getCenterY() + ", " + rg.getFocusAngle() + ", " + rg.getFocusDistance() + ", " + rg.getRadius());
                }
                this.paintTx.setTransform(tx);
                float cx = rg.getCenterX();
                float cy = rg.getCenterY();
                float r2 = rg.getRadius();
                if (rg.isProportional()) {
                    float dim = Math.min(width, height);
                    float bcx = x2 + (width * 0.5f);
                    float bcy = y2 + (height * 0.5f);
                    cx = bcx + ((cx - 0.5f) * dim);
                    cy = bcy + ((cy - 0.5f) * dim);
                    r2 *= dim;
                    if (width != height && width != 0.0d && height != 0.0d) {
                        this.paintTx.deriveWithTranslation(bcx, bcy);
                        this.paintTx.deriveWithConcatenation(width / dim, 0.0d, 0.0d, height / dim, 0.0d, 0.0d);
                        this.paintTx.deriveWithTranslation(-bcx, -bcy);
                    }
                }
                SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
                float fx = (float) (cx + (rg.getFocusDistance() * r2 * Math.cos(Math.toRadians(rg.getFocusAngle()))));
                float fy = (float) (cy + (rg.getFocusDistance() * r2 * Math.sin(Math.toRadians(rg.getFocusAngle()))));
                this.pr.setRadialGradient((int) (65536.0f * cx), (int) (65536.0f * cy), (int) (65536.0f * fx), (int) (65536.0f * fy), (int) (65536.0f * r2), getFractions(rg), getARGB(rg, this.compositeAlpha), getPiscesGradientCycleMethod(rg.getSpreadMethod()), this.piscesTx);
                return;
            case IMAGE_PATTERN:
                ImagePattern ip = (ImagePattern) p2;
                if (ip.getImage().getPixelFormat() == PixelFormat.BYTE_ALPHA) {
                    throw new UnsupportedOperationException("Alpha image is not supported as an image pattern.");
                }
                computeImagePatternTransform(ip, tx, x2, y2, width, height);
                SWArgbPreTexture tex = this.context.validateImagePaintTexture(ip.getImage().getWidth(), ip.getImage().getHeight());
                tex.update(ip.getImage());
                if (this.compositeAlpha < 1.0f) {
                    tex.applyCompositeAlpha(this.compositeAlpha);
                }
                this.pr.setTexture(1, tex.getDataNoClone(), tex.getContentWidth(), tex.getContentHeight(), tex.getPhysicalWidth(), this.piscesTx, tex.getWrapMode() == Texture.WrapMode.REPEAT, tex.getLinearFiltering(), tex.hasAlpha());
                return;
            default:
                throw new IllegalArgumentException("Unknown paint type: " + ((Object) p2.getType()));
        }
    }

    private static int[] getARGB(Gradient grd, float compositeAlpha) {
        int nstops = grd.getNumStops();
        int[] argb = new int[nstops];
        for (int i2 = 0; i2 < nstops; i2++) {
            Stop stop = grd.getStops().get(i2);
            Color stopColor = stop.getColor();
            float alpha255 = 255.0f * stopColor.getAlpha() * compositeAlpha;
            argb[i2] = ((((int) alpha255) & 255) << 24) + ((((int) (alpha255 * stopColor.getRed())) & 255) << 16) + ((((int) (alpha255 * stopColor.getGreen())) & 255) << 8) + (((int) (alpha255 * stopColor.getBlue())) & 255);
        }
        return argb;
    }

    private static int[] getFractions(Gradient grd) {
        int nstops = grd.getNumStops();
        int[] fractions = new int[nstops];
        for (int i2 = 0; i2 < nstops; i2++) {
            Stop stop = grd.getStops().get(i2);
            fractions[i2] = (int) (65536.0f * stop.getOffset());
        }
        return fractions;
    }

    private static int getPiscesGradientCycleMethod(int prismCycleMethod) {
        switch (prismCycleMethod) {
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 1;
            default:
                return 0;
        }
    }

    Transform6 computeDrawTexturePaintTransform(BaseTransform tx, float dx1, float dy1, float dx2, float dy2, float sx1, float sy1, float sx2, float sy2) {
        this.paintTx.setTransform(tx);
        float scaleX = computeScale(dx1, dx2, sx1, sx2);
        float scaleY = computeScale(dy1, dy2, sy1, sy2);
        if (scaleX == 1.0f && scaleY == 1.0f) {
            this.paintTx.deriveWithTranslation((-Math.min(sx1, sx2)) + Math.min(dx1, dx2), (-Math.min(sy1, sy2)) + Math.min(dy1, dy2));
        } else {
            this.paintTx.deriveWithTranslation(Math.min(dx1, dx2), Math.min(dy1, dy2));
            this.paintTx.deriveWithTranslation(scaleX >= 0.0f ? 0.0d : Math.abs(dx2 - dx1), scaleY >= 0.0f ? 0.0d : Math.abs(dy2 - dy1));
            this.paintTx.deriveWithConcatenation(scaleX, 0.0d, 0.0d, scaleY, 0.0d, 0.0d);
            this.paintTx.deriveWithTranslation(-Math.min(sx1, sx2), -Math.min(sy1, sy2));
        }
        SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
        return this.piscesTx;
    }

    private float computeScale(float dv1, float dv2, float sv1, float sv2) {
        float dv_diff = dv2 - dv1;
        float scale = dv_diff / (sv2 - sv1);
        if (Math.abs(scale) > 32767.0f) {
            scale = Math.signum(scale) * 32767.0f;
        }
        return scale;
    }

    Transform6 computeSetTexturePaintTransform(Paint p2, BaseTransform tx, RectBounds nodeBounds, float localX, float localY, float localWidth, float localHeight) {
        computePaintBounds(p2, null, nodeBounds, localX, localY, localWidth, localHeight);
        ImagePattern ip = (ImagePattern) p2;
        computeImagePatternTransform(ip, tx, this.px, this.py, this.pw, this.ph);
        return this.piscesTx;
    }

    private void computeImagePatternTransform(ImagePattern ip, BaseTransform tx, float x2, float y2, float width, float height) {
        Image image = ip.getImage();
        if (PrismSettings.debug) {
            System.out.println("PR.setTexturePaint: " + ((Object) image));
            System.out.println("imagePattern: x: " + ip.getX() + ", y: " + ip.getY() + ", w: " + ip.getWidth() + ", h: " + ip.getHeight() + ", proportional: " + ip.isProportional());
        }
        this.paintTx.setTransform(tx);
        this.paintTx.deriveWithConcatenation(ip.getPatternTransformNoClone());
        if (ip.isProportional()) {
            this.paintTx.deriveWithConcatenation((width / image.getWidth()) * ip.getWidth(), 0.0d, 0.0d, (height / image.getHeight()) * ip.getHeight(), x2 + (width * ip.getX()), y2 + (height * ip.getY()));
        } else {
            this.paintTx.deriveWithConcatenation(ip.getWidth() / image.getWidth(), 0.0d, 0.0d, ip.getHeight() / image.getHeight(), x2 + ip.getX(), y2 + ip.getY());
        }
        SWUtils.convertToPiscesTransform(this.paintTx, this.piscesTx);
    }
}
