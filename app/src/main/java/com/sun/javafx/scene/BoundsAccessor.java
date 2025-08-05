package com.sun.javafx.scene;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/BoundsAccessor.class */
public interface BoundsAccessor {
    BaseBounds getGeomBounds(BaseBounds baseBounds, BaseTransform baseTransform, Node node);
}
