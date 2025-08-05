package javafx.scene.paint;

import com.sun.javafx.sg.prism.NGPhongMaterial;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:javafx/scene/paint/Material.class */
public abstract class Material {
    private final BooleanProperty dirty = new SimpleBooleanProperty(true);

    @Deprecated
    public abstract void impl_updatePG();

    @Deprecated
    public abstract NGPhongMaterial impl_getNGMaterial();

    protected Material() {
        if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
            String logname = Material.class.getName();
            PlatformLogger.getLogger(logname).warning("System can't support ConditionalFeature.SCENE3D");
        }
    }

    final boolean isDirty() {
        return this.dirty.getValue2().booleanValue();
    }

    void setDirty(boolean value) {
        this.dirty.setValue(Boolean.valueOf(value));
    }

    @Deprecated
    public final BooleanProperty impl_dirtyProperty() {
        return this.dirty;
    }
}
