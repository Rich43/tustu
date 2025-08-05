package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Ellipse2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/BasicEllipseRep.class */
public class BasicEllipseRep extends BasicShapeRep {
    @Override // com.sun.prism.impl.shape.BasicShapeRep, com.sun.prism.shape.ShapeRep
    public void fill(Graphics g2, Shape shape, BaseBounds bounds) {
        Ellipse2D e2 = (Ellipse2D) shape;
        g2.fillEllipse(e2.f11899x, e2.f11900y, e2.width, e2.height);
    }

    @Override // com.sun.prism.impl.shape.BasicShapeRep, com.sun.prism.shape.ShapeRep
    public void draw(Graphics g2, Shape shape, BaseBounds bounds) {
        Ellipse2D e2 = (Ellipse2D) shape;
        g2.drawEllipse(e2.f11899x, e2.f11900y, e2.width, e2.height);
    }
}
