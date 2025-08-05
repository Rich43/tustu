package org.icepdf.core.pobjects.graphics;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import org.icepdf.core.pobjects.Name;

/* loaded from: icepdf-core.jar:org/icepdf/core/pobjects/graphics/Pattern.class */
public interface Pattern {
    public static final int PATTERN_TYPE_TILING = 1;
    public static final int PATTERN_TYPE_SHADING = 2;
    public static final Name TYPE_VALUE = new Name("pattern");

    Name getType();

    int getPatternType();

    AffineTransform getMatrix();

    void setMatrix(AffineTransform affineTransform);

    Rectangle2D getBBox();

    void init();

    Paint getPaint();

    void setParentGraphicState(GraphicsState graphicsState);
}
