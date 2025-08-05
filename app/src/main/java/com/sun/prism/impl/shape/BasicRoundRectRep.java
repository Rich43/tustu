package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RoundRectangle2D;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.Graphics;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/BasicRoundRectRep.class */
public class BasicRoundRectRep extends BasicShapeRep {
    private static final float[] TMP_ARR = new float[4];

    @Override // com.sun.prism.impl.shape.BasicShapeRep, com.sun.prism.shape.ShapeRep
    public void fill(Graphics g2, Shape shape, BaseBounds bounds) {
        fillRoundRect(g2, (RoundRectangle2D) shape);
    }

    public static void fillRoundRect(Graphics g2, RoundRectangle2D r2) {
        if (r2.width <= 0.0f || r2.height <= 0.0f) {
            return;
        }
        float arcw = r2.arcWidth;
        float arch = r2.arcHeight;
        if (arcw > 0.0f && arch > 0.0f) {
            g2.fillRoundRect(r2.f11924x, r2.f11925y, r2.width, r2.height, arcw, arch);
        } else if (isAARequiredForFill(g2, r2)) {
            g2.fillRect(r2.f11924x, r2.f11925y, r2.width, r2.height);
        } else {
            g2.fillQuad(r2.f11924x, r2.f11925y, r2.f11924x + r2.width, r2.f11925y + r2.height);
        }
    }

    @Override // com.sun.prism.impl.shape.BasicShapeRep, com.sun.prism.shape.ShapeRep
    public void draw(Graphics g2, Shape shape, BaseBounds bounds) {
        drawRoundRect(g2, (RoundRectangle2D) shape);
    }

    public static void drawRoundRect(Graphics g2, RoundRectangle2D r2) {
        float arcw = r2.arcWidth;
        float arch = r2.arcHeight;
        if (arcw > 0.0f && arch > 0.0f) {
            g2.drawRoundRect(r2.f11924x, r2.f11925y, r2.width, r2.height, arcw, arch);
        } else {
            g2.drawRect(r2.f11924x, r2.f11925y, r2.width, r2.height);
        }
    }

    private static boolean notIntEnough(float f2) {
        return ((double) Math.abs(f2 - ((float) Math.round(f2)))) > 0.06d;
    }

    private static boolean notOnIntGrid(float x1, float y1, float x2, float y2) {
        return notIntEnough(x1) || notIntEnough(y1) || notIntEnough(x2) || notIntEnough(y2);
    }

    protected static boolean isAARequiredForFill(Graphics g2, RoundRectangle2D rrect) {
        BaseTransform xform = g2.getTransformNoClone();
        long t2 = xform.getType();
        boolean aaRequiredForSure = (t2 & (-16)) != 0;
        if (aaRequiredForSure) {
            return true;
        }
        if (xform == null || xform.isIdentity()) {
            return notOnIntGrid(rrect.f11924x, rrect.f11925y, rrect.f11924x + rrect.width, rrect.f11925y + rrect.height);
        }
        TMP_ARR[0] = rrect.f11924x;
        TMP_ARR[1] = rrect.f11925y;
        TMP_ARR[2] = rrect.f11924x + rrect.width;
        TMP_ARR[3] = rrect.f11925y + rrect.height;
        xform.transform(TMP_ARR, 0, TMP_ARR, 0, 2);
        return notOnIntGrid(TMP_ARR[0], TMP_ARR[1], TMP_ARR[2], TMP_ARR[3]);
    }
}
