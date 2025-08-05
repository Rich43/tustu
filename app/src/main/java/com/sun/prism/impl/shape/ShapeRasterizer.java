package com.sun.prism.impl.shape;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/ShapeRasterizer.class */
public interface ShapeRasterizer {
    MaskData getMaskData(Shape shape, BasicStroke basicStroke, RectBounds rectBounds, BaseTransform baseTransform, boolean z2, boolean z3);
}
