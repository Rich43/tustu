package com.sun.prism.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.Shape;
import com.sun.prism.Graphics;

/* loaded from: jfxrt.jar:com/sun/prism/shape/ShapeRep.class */
public interface ShapeRep {

    /* loaded from: jfxrt.jar:com/sun/prism/shape/ShapeRep$InvalidationType.class */
    public enum InvalidationType {
        LOCATION,
        LOCATION_AND_GEOMETRY
    }

    boolean is3DCapable();

    void invalidate(InvalidationType invalidationType);

    void fill(Graphics graphics, Shape shape, BaseBounds baseBounds);

    void draw(Graphics graphics, Shape shape, BaseBounds baseBounds);

    void dispose();
}
