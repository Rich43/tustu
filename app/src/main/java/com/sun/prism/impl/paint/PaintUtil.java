package com.sun.prism.impl.paint;

import com.sun.javafx.geom.transform.Affine2D;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.paint.Color;
import com.sun.prism.paint.Gradient;
import com.sun.prism.paint.LinearGradient;
import com.sun.prism.paint.Paint;
import com.sun.prism.paint.RadialGradient;
import com.sun.prism.paint.Stop;

/* loaded from: jfxrt.jar:com/sun/prism/impl/paint/PaintUtil.class */
public class PaintUtil {
    private static final Affine2D gradXform = new Affine2D();

    public static void fillImageWithGradient(int[] pixels, Gradient grad, BaseTransform xform, int px, int py, int pw, int ph, float bx2, float by2, float bw2, float bh2) {
        MultipleGradientContext context;
        float x1;
        float y1;
        float x2;
        float y2;
        int numStops = grad.getNumStops();
        float[] fractions = new float[numStops];
        Color[] colors = new Color[numStops];
        for (int i2 = 0; i2 < numStops; i2++) {
            Stop stop = grad.getStops().get(i2);
            fractions[i2] = stop.getOffset();
            colors[i2] = stop.getColor();
        }
        if (grad.getType() == Paint.Type.LINEAR_GRADIENT) {
            LinearGradient lgrad = (LinearGradient) grad;
            if (lgrad.isProportional()) {
                x1 = (lgrad.getX1() * bw2) + bx2;
                y1 = (lgrad.getY1() * bh2) + by2;
                x2 = (lgrad.getX2() * bw2) + bx2;
                y2 = (lgrad.getY2() * bh2) + by2;
            } else {
                x1 = lgrad.getX1();
                y1 = lgrad.getY1();
                x2 = lgrad.getX2();
                y2 = lgrad.getY2();
            }
            if (x1 == x2 && y1 == y2) {
                x1 -= 1.0E-6f;
                x2 += 1.0E-6f;
            }
            context = new LinearGradientContext(lgrad, xform, x1, y1, x2, y2, fractions, colors, lgrad.getSpreadMethod());
        } else {
            RadialGradient rgrad = (RadialGradient) grad;
            gradXform.setTransform(xform);
            float radius = rgrad.getRadius();
            float cx = rgrad.getCenterX();
            float cy = rgrad.getCenterY();
            double fa = Math.toRadians(rgrad.getFocusAngle());
            float fd = rgrad.getFocusDistance();
            if (rgrad.isProportional()) {
                float bcx = bx2 + (bw2 / 2.0f);
                float bcy = by2 + (bh2 / 2.0f);
                float scale = Math.min(bw2, bh2);
                cx = ((cx - 0.5f) * scale) + bcx;
                cy = ((cy - 0.5f) * scale) + bcy;
                if (bw2 != bh2 && bw2 != 0.0f && bh2 != 0.0f) {
                    gradXform.translate(bcx, bcy);
                    gradXform.scale(bw2 / scale, bh2 / scale);
                    gradXform.translate(-bcx, -bcy);
                }
                radius *= scale;
            }
            if (radius <= 0.0f) {
                radius = 0.001f;
            }
            float fd2 = fd * radius;
            float fx = (float) (cx + (fd2 * Math.cos(fa)));
            float fy = (float) (cy + (fd2 * Math.sin(fa)));
            context = new RadialGradientContext(rgrad, gradXform, cx, cy, radius, fx, fy, fractions, colors, rgrad.getSpreadMethod());
        }
        context.fillRaster(pixels, 0, 0, px, py, pw, ph);
    }
}
