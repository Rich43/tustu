package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.QuadCurve2D;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGQuadCurve.class */
public class NGQuadCurve extends NGShape {
    private QuadCurve2D curve = new QuadCurve2D();

    @Override // com.sun.javafx.sg.prism.NGShape
    public final Shape getShape() {
        return this.curve;
    }

    public void updateQuadCurve(float x1, float y1, float x2, float y2, float ctrlx, float ctrly) {
        this.curve.x1 = x1;
        this.curve.y1 = y1;
        this.curve.x2 = x2;
        this.curve.y2 = y2;
        this.curve.ctrlx = ctrlx;
        this.curve.ctrly = ctrly;
        geometryChanged();
    }
}
