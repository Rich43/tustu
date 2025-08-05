package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.ToolBarSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;

@DefaultProperty("items")
/* loaded from: jfxrt.jar:javafx/scene/control/ToolBar.class */
public class ToolBar extends Control {
    private final ObservableList<Node> items = FXCollections.observableArrayList();
    private ObjectProperty<Orientation> orientation;
    private static final String DEFAULT_STYLE_CLASS = "tool-bar";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public ToolBar() {
        initialize();
    }

    public ToolBar(Node... items) {
        initialize();
        this.items.addAll(items);
    }

    private void initialize() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TOOL_BAR);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, true);
    }

    public final ObservableList<Node> getItems() {
        return this.items;
    }

    public final void setOrientation(Orientation value) {
        orientationProperty().set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation == null ? Orientation.HORIZONTAL : this.orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        if (this.orientation == null) {
            this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) { // from class: javafx.scene.control.ToolBar.1
                @Override // javafx.beans.property.ObjectPropertyBase
                public void invalidated() {
                    boolean isVertical = get() == Orientation.VERTICAL;
                    ToolBar.this.pseudoClassStateChanged(ToolBar.VERTICAL_PSEUDOCLASS_STATE, isVertical);
                    ToolBar.this.pseudoClassStateChanged(ToolBar.HORIZONTAL_PSEUDOCLASS_STATE, !isVertical);
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ToolBar.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "orientation";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<ToolBar, Orientation> getCssMetaData() {
                    return StyleableProperties.ORIENTATION;
                }
            };
        }
        return this.orientation;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ToolBarSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ToolBar$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ToolBar, Orientation> ORIENTATION = new CssMetaData<ToolBar, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.control.ToolBar.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(ToolBar node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(ToolBar n2) {
                return n2.orientation == null || !n2.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(ToolBar n2) {
                return (StyleableProperty) n2.orientationProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(ORIENTATION);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}
