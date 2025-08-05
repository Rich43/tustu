package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.CubicCurve2D;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCubicCurve.class */
public class NGCubicCurve extends NGShape {
    private CubicCurve2D curve = new CubicCurve2D();

    @Override // com.sun.javafx.sg.prism.NGShape
    public final Shape getShape() {
        return this.curve;
    }

    public void updateCubicCurve(float x1, float y1, float x2, float y2, float ctrlx1, float ctrly1, float ctrlx2, float ctrly2) {
        this.curve.x1 = x1;
        this.curve.y1 = y1;
        this.curve.x2 = x2;
        this.curve.y2 = y2;
        this.curve.ctrlx1 = ctrlx1;
        this.curve.ctrly1 = ctrly1;
        this.curve.ctrlx2 = ctrlx2;
        this.curve.ctrly2 = ctrly2;
        geometryChanged();
    }
}
