package com.sun.javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import java.util.Collection;
import javafx.scene.shape.PathElement;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/shape/PathUtils.class */
public final class PathUtils {
    private PathUtils() {
    }

    public static Path2D configShape(Collection<PathElement> pathElements, boolean evenOddFillRule) {
        Path2D path = new Path2D(evenOddFillRule ? 0 : 1, pathElements.size());
        for (PathElement el : pathElements) {
            el.impl_addTo(path);
        }
        return path;
    }
}
