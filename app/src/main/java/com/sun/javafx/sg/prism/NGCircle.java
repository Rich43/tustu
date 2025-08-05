package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGCircle.class */
public class NGCircle extends NGShape {
    static final float HALF_SQRT_HALF = 0.353f;
    private Ellipse2D ellipse = new Ellipse2D();
    private float cx;
    private float cy;

    public void updateCircle(float cx, float cy, float r2) {
        this.ellipse.f11899x = cx - r2;
        this.ellipse.f11900y = cy - r2;
        this.ellipse.width = r2 * 2.0f;
        this.ellipse.height = this.ellipse.width;
        this.cx = cx;
        this.cy = cy;
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public Shape getShape() {
        return this.ellipse;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGShape, com.sun.javafx.sg.prism.NGNode
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && this.ellipse.width > 0.0f;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected RectBounds computeOpaqueRegion(RectBounds opaqueRegion) {
        float halfSquareLength = this.ellipse.width * HALF_SQRT_HALF;
        return (RectBounds) opaqueRegion.deriveWithNewBounds(this.cx - halfSquareLength, this.cy - halfSquareLength, 0.0f, this.cx + halfSquareLength, this.cy + halfSquareLength, 0.0f);
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    protected ShapeRep createShapeRep(Graphics g2) {
        return g2.getResourceFactory().createEllipseRep();
    }
}
