package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGEllipse.class */
public class NGEllipse extends NGShape {
    private Ellipse2D ellipse = new Ellipse2D();
    private float cx;
    private float cy;

    public void updateEllipse(float cx, float cy, float rx, float ry) {
        this.ellipse.f11899x = cx - rx;
        this.ellipse.width = rx * 2.0f;
        this.ellipse.f11900y = cy - ry;
        this.ellipse.height = ry * 2.0f;
        this.cx = cx;
        this.cy = cy;
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public final Shape getShape() {
        return this.ellipse;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    protected ShapeRep createShapeRep(Graphics g2) {
        return g2.getResourceFactory().createEllipseRep();
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected boolean supportsOpaqueRegions() {
        return true;
    }

    @Override // com.sun.javafx.sg.prism.NGShape, com.sun.javafx.sg.prism.NGNode
    protected boolean hasOpaqueRegion() {
        return super.hasOpaqueRegion() && this.ellipse.width > 0.0f && this.ellipse.height > 0.0f;
    }

    @Override // com.sun.javafx.sg.prism.NGNode
    protected RectBounds computeOpaqueRegion(RectBounds opaqueRegion) {
        float halfWidth = this.ellipse.width * 0.353f;
        float halfHeight = this.ellipse.height * 0.353f;
        return (RectBounds) opaqueRegion.deriveWithNewBounds(this.cx - halfWidth, this.cy - halfHeight, 0.0f, this.cx + halfWidth, this.cy + halfHeight, 0.0f);
    }
}
