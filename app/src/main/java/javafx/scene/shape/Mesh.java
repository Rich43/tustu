package javafx.scene.shape;

import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.PickRay;
import com.sun.javafx.scene.input.PickResultChooser;
import com.sun.javafx.sg.prism.NGTriangleMesh;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/shape/Mesh.class */
public abstract class Mesh {
    private final BooleanProperty dirty = new SimpleBooleanProperty(true);

    abstract NGTriangleMesh getPGMesh();

    abstract void impl_updatePG();

    abstract BaseBounds computeBounds(BaseBounds baseBounds);

    @Deprecated
    protected abstract boolean impl_computeIntersects(PickRay pickRay, PickResultChooser pickResultChooser, Node node, CullFace cullFace, boolean z2);

    protected Mesh() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = Mesh.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
    }

    final boolean isDirty() {
        return this.dirty.getValue2().booleanValue();
    }

    void setDirty(boolean value) {
        this.dirty.setValue(Boolean.valueOf(value));
    }

    final BooleanProperty dirtyProperty() {
        return this.dirty;
    }
}
