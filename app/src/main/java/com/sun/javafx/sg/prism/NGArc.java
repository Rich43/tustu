package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Arc2D;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;
import com.sun.prism.shape.ShapeRep;
import javafx.scene.shape.ArcType;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGArc.class */
public class NGArc extends NGShape {
    private Arc2D arc = new Arc2D();

    public void updateArc(float cx, float cy, float rx, float ry, float start, float extent, ArcType type) {
        this.arc.f11893x = cx - rx;
        this.arc.width = rx * 2.0f;
        this.arc.f11894y = cy - ry;
        this.arc.height = ry * 2.0f;
        this.arc.start = start;
        this.arc.extent = extent;
        if (type == ArcType.CHORD) {
            this.arc.setArcType(1);
        } else if (type == ArcType.OPEN) {
            this.arc.setArcType(0);
        } else if (type == ArcType.ROUND) {
            this.arc.setArcType(2);
        } else {
            throw new AssertionError((Object) "Unknown arc type specified");
        }
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public Shape getShape() {
        return this.arc;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    protected ShapeRep createShapeRep(Graphics g2) {
        return g2.getResourceFactory().createArcRep();
    }
}
