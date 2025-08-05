package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGSVGPath.class */
public class NGSVGPath extends NGShape {
    private Shape path;

    public void setContent(Object content) {
        this.path = (Shape) content;
        geometryChanged();
    }

    public Object getGeometry() {
        return this.path;
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public Shape getShape() {
        return this.path;
    }

    public boolean acceptsPath2dOnUpdate() {
        return true;
    }
}
