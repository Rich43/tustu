package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.TitledPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.Orientation;
import javafx.scene.AccessibleAction;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javax.swing.text.AbstractDocument;

@DefaultProperty(AbstractDocument.ContentElementName)
/* loaded from: jfxrt.jar:javafx/scene/control/TitledPane.class */
public class TitledPane extends Labeled {
    private ObjectProperty<Node> content;
    private BooleanProperty expanded;
    private BooleanProperty animated;
    private BooleanProperty collapsible;
    private static final String DEFAULT_STYLE_CLASS = "titled-pane";
    private static final PseudoClass PSEUDO_CLASS_EXPANDED = PseudoClass.getPseudoClass("expanded");
    private static final PseudoClass PSEUDO_CLASS_COLLAPSED = PseudoClass.getPseudoClass("collapsed");

    public TitledPane() {
        this.expanded = new BooleanPropertyBase(true) { // from class: javafx.scene.control.TitledPane.1
            @Override // javafx.beans.property.BooleanPropertyBase
            protected void invalidated() {
                boolean active = get();
                TitledPane.this.pseudoClassStateChanged(TitledPane.PSEUDO_CLASS_EXPANDED, active);
                TitledPane.this.pseudoClassStateChanged(TitledPane.PSEUDO_CLASS_COLLAPSED, !active);
                TitledPane.this.notifyAccessibleAttributeChanged(AccessibleAttribute.EXPANDED);
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TitledPane.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "expanded";
            }
        };
        this.animated = new StyleableBooleanProperty(true) { // from class: javafx.scene.control.TitledPane.2
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TitledPane.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "animated";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.ANIMATED;
            }
        };
        this.collapsible = new StyleableBooleanProperty(true) { // from class: javafx.scene.control.TitledPane.3
            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return TitledPane.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "collapsible";
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                return StyleableProperties.COLLAPSIBLE;
            }
        };
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.TITLED_PANE);
        pseudoClassStateChanged(PSEUDO_CLASS_EXPANDED, true);
    }

    public TitledPane(String title, Node content) {
        this();
        setText(title);
        setContent(content);
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

    public final void setExpanded(boolean value) {
        expandedProperty().set(value);
    }

    public final boolean isExpanded() {
        return this.expanded.get();
    }

    public final BooleanProperty expandedProperty() {
        return this.expanded;
    }

    public final void setAnimated(boolean value) {
        animatedProperty().set(value);
    }

    public final boolean isAnimated() {
        return this.animated.get();
    }

    public final BooleanProperty animatedProperty() {
        return this.animated;
    }

    public final void setCollapsible(boolean value) {
        collapsibleProperty().set(value);
    }

    public final boolean isCollapsible() {
        return this.collapsible.get();
    }

    public final BooleanProperty collapsibleProperty() {
        return this.collapsible;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new TitledPaneSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/TitledPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<TitledPane, Boolean> COLLAPSIBLE = new CssMetaData<TitledPane, Boolean>("-fx-collapsible", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.control.TitledPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TitledPane n2) {
                return n2.collapsible == null || !n2.collapsible.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(TitledPane n2) {
                return (StyleableProperty) n2.collapsibleProperty();
            }
        };
        private static final CssMetaData<TitledPane, Boolean> ANIMATED = new CssMetaData<TitledPane, Boolean>("-fx-animated", BooleanConverter.getInstance(), Boolean.TRUE) { // from class: javafx.scene.control.TitledPane.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(TitledPane n2) {
                return n2.animated == null || !n2.animated.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(TitledPane n2) {
                return (StyleableProperty) n2.animatedProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Labeled.getClassCssMetaData());
            styleables.add(COLLAPSIBLE);
            styleables.add(ANIMATED);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Labeled, javafx.scene.control.Control
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Labeled, javafx.scene.Node
    public Orientation getContentBias() {
        Node c2 = getContent();
        return c2 == null ? super.getContentBias() : c2.getContentBias();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case TEXT:
                String accText = getAccessibleText();
                return (accText == null || accText.isEmpty()) ? getText() : accText;
            case EXPANDED:
                return Boolean.valueOf(isExpanded());
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    public void executeAccessibleAction(AccessibleAction action, Object... parameters) {
        switch (action) {
            case EXPAND:
                setExpanded(true);
                break;
            case COLLAPSE:
                setExpanded(false);
                break;
            default:
                super.executeAccessibleAction(action, new Object[0]);
                break;
        }
    }
}
