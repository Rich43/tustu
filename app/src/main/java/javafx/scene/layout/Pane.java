package javafx.scene.layout;

import com.sun.org.apache.xalan.internal.templates.Constants;
import javafx.beans.DefaultProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;

@DefaultProperty(Constants.ELEMNAME_CHILDREN_STRING)
/* loaded from: jfxrt.jar:javafx/scene/layout/Pane.class */
public class Pane extends Region {
    static void setConstraint(Node node, Object key, Object value) {
        if (value == null) {
            node.getProperties().remove(key);
        } else {
            node.getProperties().put(key, value);
        }
        if (node.getParent() != null) {
            node.getParent().requestLayout();
        }
    }

    static Object getConstraint(Node node, Object key) {
        Object value;
        if (node.hasProperties() && (value = node.getProperties().get(key)) != null) {
            return value;
        }
        return null;
    }

    public Pane() {
    }

    public Pane(Node... children) {
        getChildren().addAll(children);
    }

    @Override // javafx.scene.Parent
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }
}
