package javafx.scene;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.util.Collection;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;

@DefaultProperty(Constants.ELEMNAME_CHILDREN_STRING)
/* loaded from: jfxrt.jar:javafx/scene/Group.class */
public class Group extends Parent {
    private BooleanProperty autoSizeChildren;

    public Group() {
    }

    public Group(Node... children) {
        getChildren().addAll(children);
    }

    public Group(Collection<Node> children) {
        getChildren().addAll(children);
    }

    public final void setAutoSizeChildren(boolean value) {
        autoSizeChildrenProperty().set(value);
    }

    public final boolean isAutoSizeChildren() {
        if (this.autoSizeChildren == null) {
            return true;
        }
        return this.autoSizeChildren.get();
    }

    public final BooleanProperty autoSizeChildrenProperty() {
        if (this.autoSizeChildren == null) {
            this.autoSizeChildren = new BooleanPropertyBase(true) { // from class: javafx.scene.Group.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Group.this.requestLayout();
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Group.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "autoSizeChildren";
                }
            };
        }
        return this.autoSizeChildren;
    }

    @Override // javafx.scene.Parent
    public ObservableList<Node> getChildren() {
        return super.getChildren();
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected Bounds impl_computeLayoutBounds() {
        layout();
        return super.impl_computeLayoutBounds();
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public double prefWidth(double height) {
        if (isAutoSizeChildren()) {
            layout();
        }
        double result = getLayoutBounds().getWidth();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public double prefHeight(double width) {
        if (isAutoSizeChildren()) {
            layout();
        }
        double result = getLayoutBounds().getHeight();
        if (Double.isNaN(result) || result < 0.0d) {
            return 0.0d;
        }
        return result;
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public double minHeight(double width) {
        return prefHeight(width);
    }

    @Override // javafx.scene.Parent, javafx.scene.Node
    public double minWidth(double height) {
        return prefWidth(height);
    }

    @Override // javafx.scene.Parent
    protected void layoutChildren() {
        if (isAutoSizeChildren()) {
            super.layoutChildren();
        }
    }
}
