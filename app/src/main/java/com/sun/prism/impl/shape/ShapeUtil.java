package com.sun.prism.impl.shape;

import com.sun.javafx.geom.RectBounds;
import com.sun.javafx.geom.Shape;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.prism.BasicStroke;
import com.sun.prism.impl.PrismSettings;

/* loaded from: jfxrt.jar:com/sun/prism/impl/shape/ShapeUtil.class */
public class ShapeUtil {
    private static final ShapeRasterizer shapeRasterizer;

    static {
        if (PrismSettings.doNativePisces) {
            shapeRasterizer = new NativePiscesRasterizer();
        } else {
            shapeRasterizer = new OpenPiscesRasterizer();
        }
    }

    public static MaskData rasterizeShape(Shape shape, BasicStroke stroke, RectBounds xformBounds, BaseTransform xform, boolean close, boolean antialiasedShape) {
        return shapeRasterizer.getMaskData(shape, stroke, xformBounds, xform, close, antialiasedShape);
    }

    private ShapeUtil() {
    }
}
