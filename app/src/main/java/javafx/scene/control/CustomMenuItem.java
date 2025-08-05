package javafx.scene.control;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javax.swing.text.AbstractDocument;

/* loaded from: jfxrt.jar:javafx/scene/control/CustomMenuItem.class */
public class CustomMenuItem extends MenuItem {
    private ObjectProperty<Node> content;
    private BooleanProperty hideOnClick;
    private static final String DEFAULT_STYLE_CLASS = "custom-menu-item";

    public CustomMenuItem() {
        this(null, true);
    }

    public CustomMenuItem(Node node) {
        this(node, true);
    }

    public CustomMenuItem(Node node, boolean hideOnClick) {
        getStyleClass().add(DEFAULT_STYLE_CLASS);
        setContent(node);
        setHideOnClick(hideOnClick);
    }

    public final void setContent(Node value) {
        contentProperty().set(value);
    }

    public final Node getContent() {
        if (this.content == null) {
            return null;
        }
        return this.content.get();
    }

    public final ObjectProperty<Node> contentProperty() {
        if (this.content == null) {
            this.content = new SimpleObjectProperty(this, AbstractDocument.ContentElementName);
        }
        return this.content;
    }

    public final void setHideOnClick(boolean value) {
        hideOnClickProperty().set(value);
    }

    public final boolean isHideOnClick() {
        if (this.hideOnClick == null) {
            return true;
        }
        return this.hideOnClick.get();
    }

    public final BooleanProperty hideOnClickProperty() {
        if (this.hideOnClick == null) {
            this.hideOnClick = new SimpleBooleanProperty(this, "hideOnClick", true);
        }
        return this.hideOnClick;
    }
}
