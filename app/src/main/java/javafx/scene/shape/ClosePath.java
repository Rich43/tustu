package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;

/* loaded from: jfxrt.jar:javafx/scene/shape/ClosePath.class */
public class ClosePath extends PathElement {
    @Override // javafx.scene.shape.PathElement
    void addTo(NGPath pgPath) {
        pgPath.addClosePath();
    }

    @Override // javafx.scene.shape.PathElement
    @Deprecated
    public void impl_addTo(Path2D path) {
        path.closePath();
    }

    public String toString() {
        return "ClosePath";
    }
}
