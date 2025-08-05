package javafx.scene.layout;

import com.sun.javafx.util.WeakReferenceQueue;
import java.util.Iterator;
import javafx.scene.Parent;

/* loaded from: jfxrt.jar:javafx/scene/layout/ConstraintsBase.class */
public abstract class ConstraintsBase {
    public static final double CONSTRAIN_TO_PREF = Double.NEGATIVE_INFINITY;
    WeakReferenceQueue impl_nodes = new WeakReferenceQueue();

    ConstraintsBase() {
    }

    void add(Parent node) {
        this.impl_nodes.add(node);
    }

    void remove(Parent node) {
        this.impl_nodes.remove(node);
    }

    protected void requestLayout() {
        Iterator<Parent> nodeIter = this.impl_nodes.iterator();
        while (nodeIter.hasNext()) {
            nodeIter.next().requestLayout();
        }
    }
}
