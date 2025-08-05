package javafx.scene.control;

import com.sun.javafx.css.converters.EnumConverter;
import com.sun.javafx.scene.control.skin.SeparatorSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.Styleable;
import javafx.css.StyleableObjectProperty;
import javafx.css.StyleableProperty;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.VPos;

/* loaded from: jfxrt.jar:javafx/scene/control/Separator.class */
public class Separator extends Control {
    private ObjectProperty<Orientation> orientation;
    private ObjectProperty<HPos> halignment;
    private ObjectProperty<VPos> valignment;
    private static final String DEFAULT_STYLE_CLASS = "separator";
    private static final PseudoClass VERTICAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("vertical");
    private static final PseudoClass HORIZONTAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("horizontal");

    public Separator() {
        this(Orientation.HORIZONTAL);
    }

    public Separator(Orientation orientation) {
        this.orientation = new StyleableObjectProperty<Orientation>(Orientation.HORIZONTAL) { // from class: javafx.scene.control.Separator.1
            @Override // javafx.beans.property.ObjectPropertyBase
            protected void invalidated() {
                boolean isVertical = get() == Orientation.VERTICAL;
                Separator.this.pseudoClassStateChanged(Separator.VERTICAL_PSEUDOCLASS_STATE, isVertical);
                Separator.this.pseudoClassStateChanged(Separator.HORIZONTAL_PSEUDOCLASS_STATE, !isVertical);
            }

            @Override // javafx.css.StyleableProperty
            public CssMetaData<Separator, Orientation> getCssMetaData() {
                return StyleableProperties.ORIENTATION;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public Object getBean() {
                return Separator.this;
            }

            @Override // javafx.beans.property.ReadOnlyProperty
            public String getName() {
                return "orientation";
            }
        };
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
        pseudoClassStateChanged(HORIZONTAL_PSEUDOCLASS_STATE, orientation != Orientation.VERTICAL);
        pseudoClassStateChanged(VERTICAL_PSEUDOCLASS_STATE, orientation == Orientation.VERTICAL);
        ((StyleableProperty) orientationProperty()).applyStyle(null, orientation != null ? orientation : Orientation.HORIZONTAL);
    }

    public final void setOrientation(Orientation value) {
        this.orientation.set(value);
    }

    public final Orientation getOrientation() {
        return this.orientation.get();
    }

    public final ObjectProperty<Orientation> orientationProperty() {
        return this.orientation;
    }

    public final void setHalignment(HPos value) {
        halignmentProperty().set(value);
    }

    public final HPos getHalignment() {
        return this.halignment == null ? HPos.CENTER : this.halignment.get();
    }

    public final ObjectProperty<HPos> halignmentProperty() {
        if (this.halignment == null) {
            this.halignment = new StyleableObjectProperty<HPos>(HPos.CENTER) { // from class: javafx.scene.control.Separator.2
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Separator.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "halignment";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Separator, HPos> getCssMetaData() {
                    return StyleableProperties.HALIGNMENT;
                }
            };
        }
        return this.halignment;
    }

    public final void setValignment(VPos value) {
        valignmentProperty().set(value);
    }

    public final VPos getValignment() {
        return this.valignment == null ? VPos.CENTER : this.valignment.get();
    }

    public final ObjectProperty<VPos> valignmentProperty() {
        if (this.valignment == null) {
            this.valignment = new StyleableObjectProperty<VPos>(VPos.CENTER) { // from class: javafx.scene.control.Separator.3
                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return Separator.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "valignment";
                }

                @Override // javafx.css.StyleableProperty
                public CssMetaData<Separator, VPos> getCssMetaData() {
                    return StyleableProperties.VALIGNMENT;
                }
            };
        }
        return this.valignment;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new SeparatorSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/Separator$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<Separator, Orientation> ORIENTATION = new CssMetaData<Separator, Orientation>("-fx-orientation", new EnumConverter(Orientation.class), Orientation.HORIZONTAL) { // from class: javafx.scene.control.Separator.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public Orientation getInitialValue(Separator node) {
                return node.getOrientation();
            }

            @Override // javafx.css.CssMetaData
            public boolean isSettable(Separator n2) {
                return n2.orientation == null || !n2.orientation.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Orientation> getStyleableProperty(Separator n2) {
                return (StyleableProperty) n2.orientationProperty();
            }
        };
        private static final CssMetaData<Separator, HPos> HALIGNMENT = new CssMetaData<Separator, HPos>("-fx-halignment", new EnumConverter(HPos.class), HPos.CENTER) { // from class: javafx.scene.control.Separator.StyleableProperties.2
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Separator n2) {
                return n2.halignment == null || !n2.halignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<HPos> getStyleableProperty(Separator n2) {
                return (StyleableProperty) n2.halignmentProperty();
            }
        };
        private static final CssMetaData<Separator, VPos> VALIGNMENT = new CssMetaData<Separator, VPos>("-fx-valignment", new EnumConverter(VPos.class), VPos.CENTER) { // from class: javafx.scene.control.Separator.StyleableProperties.3
            @Override // javafx.css.CssMetaData
            public boolean isSettable(Separator n2) {
                return n2.valignment == null || !n2.valignment.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<VPos> getStyleableProperty(Separator n2) {
                return (StyleableProperty) n2.valignmentProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(ORIENTATION);
            styleables.add(HALIGNMENT);
            styleables.add(VALIGNMENT);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
        return StyleableProperties.STYLEABLES;
    }

    @Override // javafx.scene.control.Control
    @Deprecated
    protected List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return getClassCssMetaData();
    }

    @Override // javafx.scene.control.Control, javafx.scene.Node
    @Deprecated
    protected Boolean impl_cssGetFocusTraversableInitialValue() {
        return Boolean.FALSE;
    }
}
