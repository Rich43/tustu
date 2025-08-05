package com.sun.prism.impl.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/BasicShapeRep.class */
public class BasicShapeRep implements ShapeRep {
    @Override // com.sun.prism.shape.ShapeRep
    public boolean is3DCapable() {
        return false;
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void invalidate(ShapeRep.InvalidationType type) {
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void fill(Graphics g2, Shape shape, BaseBounds bounds) {
        g2.fill(shape);
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void draw(Graphics g2, Shape shape, BaseBounds bounds) {
        g2.draw(shape);
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void dispose() {
    }
}
