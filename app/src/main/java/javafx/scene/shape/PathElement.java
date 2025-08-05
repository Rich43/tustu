package javafx.scene.shape;

import com.sun.javafx.geom.Path2D;
import com.sun.javafx.sg.prism.NGPath;
import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Iterator;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/shape/PathElement.class */
public abstract class PathElement {
    WeakReferenceQueue impl_nodes = new WeakReferenceQueue();
    private BooleanProperty absolute;

    abstract void addTo(NGPath nGPath);

    @Deprecated
    public abstract void impl_addTo(Path2D path2D);

    void addNode(Node n2) {
        this.impl_nodes.add(n2);
    }

    void removeNode(Node n2) {
        this.impl_nodes.remove(n2);
    }

    void u() {
        Iterator iterator = this.impl_nodes.iterator();
        while (iterator.hasNext()) {
            ((Path) iterator.next()).markPathDirty();
        }
    }

    public final void setAbsolute(boolean value) {
        absoluteProperty().set(value);
    }

    public final boolean isAbsolute() {
        return this.absolute == null || this.absolute.get();
    }

    public final BooleanProperty absoluteProperty() {
        if (this.absolute == null) {
            this.absolute = new BooleanPropertyBase(true) { // from class: javafx.scene.shape.PathElement.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    PathElement.this.u();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return PathElement.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "absolute";
                }
            };
        }
        return this.absolute;
    }
}
