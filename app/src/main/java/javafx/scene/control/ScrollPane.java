package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.scene.AccessibleAttribute;
import javafx.scene.AccessibleRole;
import javafx.scene.Node;
import javax.swing.text.AbstractDocument;

@DefaultProperty(AbstractDocument.ContentElementName)
/* loaded from: jfxrt.jar:javafx/scene/control/ScrollPane.class */
public class ScrollPane extends Control {
    private ObjectProperty<ScrollBarPolicy> hbarPolicy;
    private ObjectProperty<ScrollBarPolicy> vbarPolicy;
    private ObjectProperty<Node> content;
    private DoubleProperty hvalue;
    private DoubleProperty vvalue;
    private DoubleProperty hmin;
    private DoubleProperty vmin;
    private DoubleProperty hmax;
    private DoubleProperty vmax;
    private BooleanProperty fitToWidth;
    private BooleanProperty fitToHeight;
    private BooleanProperty pannable;
    private DoubleProperty prefViewportWidth;
    private DoubleProperty prefViewportHeight;
    private DoubleProperty minViewportWidth;
    private DoubleProperty minViewportHeight;
    private ObjectProperty<Bounds> viewportBounds;
    private static final String DEFAULT_STYLE_CLASS = "scroll-pane";
    private static final PseudoClass PANNABLE_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("pannable");
    private static final PseudoClass FIT_TO_WIDTH_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("fitToWidth");
    private static final PseudoClass FIT_TO_HEIGHT_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("fitToHeight");

    /* loaded from: jfxrt.jar:javafx/scene/control/ScrollPane$ScrollBarPolicy.class */
    public enum ScrollBarPolicy {
        NEVER,
        ALWAYS,
        AS_NEEDED
    }

    public ScrollPane() {
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.SCROLL_PANE);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
    }

    public ScrollPane(Node content) {
        this();
        setContent(content);
    }

    public final void setHbarPolicy(ScrollBarPolicy value) {
        hbarPolicyProperty().set(value);
    }

    public final ScrollBarPolicy getHbarPolicy() {
        return this.hbarPolicy == null ? ScrollBarPolicy.AS_NEEDED : this.hbarPolicy.get();
    }

    public final ObjectProperty<ScrollBarPolicy> hbarPolicyProperty() {
        if (this.hbarPolicy == null) {
            this.hbarPolicy = new StyleableObjectProperty<ScrollBarPolicy>(ScrollBarPolicy.AS_NEEDED) { // from class: javafx.scene.control.ScrollPane.1
                @Override // javafx.css.StyleableProperty
                public CssMetaData<ScrollPane, ScrollBarPolicy> getCssMetaData() {
                    return StyleableProperties.HBAR_POLICY;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "hbarPolicy";
                }
            };
        }
        return this.hbarPolicy;
    }

    public final void setVbarPolicy(ScrollBarPolicy value) {
        vbarPolicyProperty().set(value);
    }

    public final ScrollBarPolicy getVbarPolicy() {
        return this.vbarPolicy == null ? ScrollBarPolicy.AS_NEEDED : this.vbarPolicy.get();
    }

    public final ObjectProperty<ScrollBarPolicy> vbarPolicyProperty() {
        if (this.vbarPolicy == null) {
            this.vbarPolicy = new StyleableObjectProperty<ScrollBarPolicy>(ScrollBarPolicy.AS_NEEDED) { // from class: javafx.scene.control.ScrollPane.2
                @Override // javafx.css.StyleableProperty
                public CssMetaData<ScrollPane, ScrollBarPolicy> getCssMetaData() {
                    return StyleableProperties.VBAR_POLICY;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "vbarPolicy";
                }
            };
        }
        return this.vbarPolicy;
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

    public final void setHvalue(double value) {
        hvalueProperty().set(value);
    }

    public final double getHvalue() {
        if (this.hvalue == null) {
            return 0.0d;
        }
        return this.hvalue.get();
    }

    public final DoubleProperty hvalueProperty() {
        if (this.hvalue == null) {
            this.hvalue = new SimpleDoubleProperty(this, "hvalue");
        }
        return this.hvalue;
    }

    public final void setVvalue(double value) {
        vvalueProperty().set(value);
    }

    public final double getVvalue() {
        if (this.vvalue == null) {
            return 0.0d;
        }
        return this.vvalue.get();
    }

    public final DoubleProperty vvalueProperty() {
        if (this.vvalue == null) {
            this.vvalue = new SimpleDoubleProperty(this, "vvalue");
        }
        return this.vvalue;
    }

    public final void setHmin(double value) {
        hminProperty().set(value);
    }

    public final double getHmin() {
        if (this.hmin == null) {
            return 0.0d;
        }
        return this.hmin.get();
    }

    public final DoubleProperty hminProperty() {
        if (this.hmin == null) {
            this.hmin = new SimpleDoubleProperty(this, "hmin", 0.0d);
        }
        return this.hmin;
    }

    public final void setVmin(double value) {
        vminProperty().set(value);
    }

    public final double getVmin() {
        if (this.vmin == null) {
            return 0.0d;
        }
        return this.vmin.get();
    }

    public final DoubleProperty vminProperty() {
        if (this.vmin == null) {
            this.vmin = new SimpleDoubleProperty(this, "vmin", 0.0d);
        }
        return this.vmin;
    }

    public final void setHmax(double value) {
        hmaxProperty().set(value);
    }

    public final double getHmax() {
        if (this.hmax == null) {
            return 1.0d;
        }
        return this.hmax.get();
    }

    public final DoubleProperty hmaxProperty() {
        if (this.hmax == null) {
            this.hmax = new SimpleDoubleProperty(this, "hmax", 1.0d);
        }
        return this.hmax;
    }

    public final void setVmax(double value) {
        vmaxProperty().set(value);
    }

    public final double getVmax() {
        if (this.vmax == null) {
            return 1.0d;
        }
        return this.vmax.get();
    }

    public final DoubleProperty vmaxProperty() {
        if (this.vmax == null) {
            this.vmax = new SimpleDoubleProperty(this, "vmax", 1.0d);
        }
        return this.vmax;
    }

    public final void setFitToWidth(boolean value) {
        fitToWidthProperty().set(value);
    }

    public final boolean isFitToWidth() {
        if (this.fitToWidth == null) {
            return false;
        }
        return this.fitToWidth.get();
    }

    public final BooleanProperty fitToWidthProperty() {
        if (this.fitToWidth == null) {
            this.fitToWidth = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.ScrollPane.3
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    ScrollPane.this.pseudoClassStateChanged(ScrollPane.FIT_TO_WIDTH_PSEUDOCLASS_STATE, get());
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.FIT_TO_WIDTH;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fitToWidth";
                }
            };
        }
        return this.fitToWidth;
    }

    public final void setFitToHeight(boolean value) {
        fitToHeightProperty().set(value);
    }

    public final boolean isFitToHeight() {
        if (this.fitToHeight == null) {
            return false;
        }
        return this.fitToHeight.get();
    }

    public final BooleanProperty fitToHeightProperty() {
        if (this.fitToHeight == null) {
            this.fitToHeight = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.ScrollPane.4
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    ScrollPane.this.pseudoClassStateChanged(ScrollPane.FIT_TO_HEIGHT_PSEUDOCLASS_STATE, get());
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.FIT_TO_HEIGHT;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "fitToHeight";
                }
            };
        }
        return this.fitToHeight;
    }

    public final void setPannable(boolean value) {
        pannableProperty().set(value);
    }

    public final boolean isPannable() {
        if (this.pannable == null) {
            return false;
        }
        return this.pannable.get();
    }

    public final BooleanProperty pannableProperty() {
        if (this.pannable == null) {
            this.pannable = new StyleableBooleanProperty(false) { // from class: javafx.scene.control.ScrollPane.5
                @Override // javafx.beans.property.BooleanPropertyBase
                public void invalidated() {
                    ScrollPane.this.pseudoClassStateChanged(ScrollPane.PANNABLE_PSEUDOCLASS_STATE, get());
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.PANNABLE;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return ScrollPane.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "pannable";
                }
            };
        }
        return this.pannable;
    }

    public final void setPrefViewportWidth(double value) {
        prefViewportWidthProperty().set(value);
    }

    public final double getPrefViewportWidth() {
        if (this.prefViewportWidth == null) {
            return 0.0d;
        }
        return this.prefViewportWidth.get();
    }

    public final DoubleProperty prefViewportWidthProperty() {
        if (this.prefViewportWidth == null) {
            this.prefViewportWidth = new SimpleDoubleProperty(this, "prefViewportWidth");
        }
        return this.prefViewportWidth;
    }

    public final void setPrefViewportHeight(double value) {
        prefViewportHeightProperty().set(value);
    }

    public final double getPrefViewportHeight() {
        if (this.prefViewportHeight == null) {
            return 0.0d;
        }
        return this.prefViewportHeight.get();
    }

    public final DoubleProperty prefViewportHeightProperty() {
        if (this.prefViewportHeight == null) {
            this.prefViewportHeight = new SimpleDoubleProperty(this, "prefViewportHeight");
        }
        return this.prefViewportHeight;
    }

    public final void setMinViewportWidth(double value) {
        minViewportWidthProperty().set(value);
    }

    public final double getMinViewportWidth() {
        if (this.minViewportWidth == null) {
            return 0.0d;
        }
        return this.minViewportWidth.get();
    }

    public final DoubleProperty minViewportWidthProperty() {
        if (this.minViewportWidth == null) {
            this.minViewportWidth = new SimpleDoubleProperty(this, "minViewportWidth");
        }
        return this.minViewportWidth;
    }

    public final void setMinViewportHeight(double value) {
        minViewportHeightProperty().set(value);
    }

    public final double getMinViewportHeight() {
        if (this.minViewportHeight == null) {
            return 0.0d;
        }
        return this.minViewportHeight.get();
    }

    public final DoubleProperty minViewportHeightProperty() {
        if (this.minViewportHeight == null) {
            this.minViewportHeight = new SimpleDoubleProperty(this, "minViewportHeight");
        }
        return this.minViewportHeight;
    }

    public final void setViewportBounds(Bounds value) {
        viewportBoundsProperty().set(value);
    }

    public final Bounds getViewportBounds() {
        return this.viewportBounds == null ? new BoundingBox(0.0d, 0.0d, 0.0d, 0.0d) : this.viewportBounds.get();
    }

    public final ObjectProperty<Bounds> viewportBoundsProperty() {
        if (this.viewportBounds == null) {
            this.viewportBounds = new SimpleObjectProperty(this, "viewportBounds", new BoundingBox(0.0d, 0.0d, 0.0d, 0.0d));
        }
        return this.viewportBounds;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new ScrollPaneSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/ScrollPane$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<ScrollPane, ScrollBarPolicy> HBAR_POLICY = new CssMetaData<ScrollPane, ScrollBarPolicy>("-fx-hbar-policy", new EnumConverter(ScrollBarPolicy.class), ScrollBarPolicy.AS_NEEDED) { // from class: javafx.scene.control.ScrollPane.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollPane n2) {
                return n2.hbarPolicy == null || !n2.hbarPolicy.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<ScrollBarPolicy> getStyleableProperty(ScrollPane n2) {
                return (StyleableProperty) n2.hbarPolicyProperty();
            }
        };
        private static final CssMetaData<ScrollPane, ScrollBarPolicy> VBAR_POLICY = new CssMetaData<ScrollPane, ScrollBarPolicy>("-fx-vbar-policy", new EnumConverter(ScrollBarPolicy.class), ScrollBarPolicy.AS_NEEDED) { // from class: javafx.scene.control.ScrollPane.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollPane n2) {
                return n2.vbarPolicy == null || !n2.vbarPolicy.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<ScrollBarPolicy> getStyleableProperty(ScrollPane n2) {
                return (StyleableProperty) n2.vbarPolicyProperty();
            }
        };
        private static final CssMetaData<ScrollPane, Boolean> FIT_TO_WIDTH = new CssMetaData<ScrollPane, Boolean>("-fx-fit-to-width", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.ScrollPane.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollPane n2) {
                return n2.fitToWidth == null || !n2.fitToWidth.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ScrollPane n2) {
                return (StyleableProperty) n2.fitToWidthProperty();
            }
        };
        private static final CssMetaData<ScrollPane, Boolean> FIT_TO_HEIGHT = new CssMetaData<ScrollPane, Boolean>("-fx-fit-to-height", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.ScrollPane.StyleableProperties.4
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollPane n2) {
                return n2.fitToHeight == null || !n2.fitToHeight.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ScrollPane n2) {
                return (StyleableProperty) n2.fitToHeightProperty();
            }
        };
        private static final CssMetaData<ScrollPane, Boolean> PANNABLE = new CssMetaData<ScrollPane, Boolean>("-fx-pannable", BooleanConverter.getInstance(), Boolean.FALSE) { // from class: javafx.scene.control.ScrollPane.StyleableProperties.5
            @Override // javafx.css.CssMetaData
            public boolean isSettable(ScrollPane n2) {
                return n2.pannable == null || !n2.pannable.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(ScrollPane n2) {
                return (StyleableProperty) n2.pannableProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(HBAR_POLICY);
            styleables.add(VBAR_POLICY);
            styleables.add(FIT_TO_WIDTH);
            styleables.add(FIT_TO_HEIGHT);
            styleables.add(PANNABLE);
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

    @Override // javafx.scene.control.Control, javafx.scene.Parent, javafx.scene.Node
    public Object queryAccessibleAttribute(AccessibleAttribute attribute, Object... parameters) {
        switch (attribute) {
            case CONTENTS:
                return getContent();
            default:
                return super.queryAccessibleAttribute(attribute, parameters);
        }
    }
}
