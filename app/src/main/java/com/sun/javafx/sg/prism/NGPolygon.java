package com.sun.javafx.sg.prism;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.geom.Shape;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/NGPolygon.class */
public class NGPolygon extends NGShape {
    private Path2D path = new Path2D();

    public void updatePolygon(float[] points) {
        this.path.reset();
        if (points == null || points.length == 0 || points.length % 2 != 0) {
            return;
        }
        this.path.moveTo(points[0], points[1]);
        for (int i2 = 1; i2 < points.length / 2; i2++) {
            float px = points[(i2 * 2) + 0];
            float py = points[(i2 * 2) + 1];
            this.path.lineTo(px, py);
        }
        this.path.closePath();
        geometryChanged();
    }

    @Override // com.sun.javafx.sg.prism.NGShape
    public Shape getShape() {
        return this.path;
    }
}
