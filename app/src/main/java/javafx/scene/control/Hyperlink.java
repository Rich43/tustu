package javafx.scene.control;

import com.sun.javafx.scene.control.skin.HyperlinkSkin;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.css.PseudoClass;
import javafx.css.StyleableProperty;
import javafx.event.ActionEvent;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Cursor;
import javafx.scene.Node;

/* loaded from: jfxrt.jar:javafx/scene/control/Hyperlink.class */
public class Hyperlink extends ButtonBase {
    private BooleanProperty visited;
    private static final String DEFAULT_STYLE_CLASS = "hyperlink";
    private static final PseudoClass PSEUDO_CLASS_VISITED = PseudoClass.getPseudoClass("visited");

    public Hyperlink() {
        initialize();
    }

    public Hyperlink(String text) {
        super(text);
        initialize();
    }

    public Hyperlink(String text, Node graphic) {
        super(text, graphic);
        initialize();
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.HYPERLINK);
        ((StyleableProperty) cursorProperty()).applyStyle(null, Cursor.HAND);
    }

    public final BooleanProperty visitedProperty() {
        if (this.visited == null) {
            this.visited = new BooleanPropertyBase() { // from class: javafx.scene.control.Hyperlink.1
                @Override // javafx.beans.property.BooleanPropertyBase
                protected void invalidated() {
                    Hyperlink.this.pseudoClassStateChanged(Hyperlink.PSEUDO_CLASS_VISITED, get());
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Hyperlink.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "visited";
                }
            };
        }
        return this.visited;
    }

    public final void setVisited(boolean value) {
        visitedProperty().set(value);
    }

    public final boolean isVisited() {
        if (this.visited == null) {
            return false;
        }
        return this.visited.get();
    }

    @Override // javafx.scene.control.ButtonBase
    public void fire() {
        if (!isDisabled()) {
            if (this.visited == null || !this.visited.isBound()) {
                setVisited(true);
            }
            fireEvent(new ActionEvent());
        }
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new HyperlinkSkin(this);
    }

    @Override // javafx.scene.Node
    @Deprecated
    protected Cursor impl_cssGetCursorInitialValue() {
        return Cursor.HAND;
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case VISITED:
                return Boolean.valueOf(isVisited());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
