package com.sun.prism.impl.ps;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;

/* loaded from: jfxrt.jar:com/sun/prism/impl/ps/CachingShapeRep.class */
public class CachingShapeRep implements ShapeRep {
    private CachingShapeRepState fillState;
    private CachingShapeRepState drawState;

    CachingShapeRepState createState() {
        return new CachingShapeRepState();
    }

    @Override // com.sun.prism.shape.ShapeRep
    public boolean is3DCapable() {
        return false;
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void invalidate(ShapeRep.InvalidationType type) {
        if (this.fillState != null) {
            this.fillState.invalidate();
        }
        if (this.drawState != null) {
            this.drawState.invalidate();
        }
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void fill(Graphics g2, Shape shape, BaseBounds bounds) {
        if (this.fillState == null) {
            this.fillState = createState();
        }
        this.fillState.render(g2, shape, (RectBounds) bounds, null);
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void draw(Graphics g2, Shape shape, BaseBounds bounds) {
        if (this.drawState == null) {
            this.drawState = createState();
        }
        this.drawState.render(g2, shape, (RectBounds) bounds, g2.getStroke());
    }

    @Override // com.sun.prism.shape.ShapeRep
    public void dispose() {
        if (this.fillState != null) {
            this.fillState.dispose();
            this.fillState = null;
        }
        if (this.drawState != null) {
            this.drawState.dispose();
            this.drawState = null;
        }
    }
}
