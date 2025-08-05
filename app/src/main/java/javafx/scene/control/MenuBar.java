package javafx.scene.control;

import com.sun.javafx.css.converters.BooleanConverter;
import com.sun.javafx.scene.control.skin.MenuBarSkin;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.Styleable;
import javafx.css.StyleableBooleanProperty;
import javafx.css.StyleableProperty;
import javafx.scene.AccessibleRole;

@DefaultProperty("menus")
/* loaded from: jfxrt.jar:javafx/scene/control/MenuBar.class */
public class MenuBar extends Control {
    private ObservableList<Menu> menus;
    private BooleanProperty useSystemMenuBar;
    private static final String DEFAULT_STYLE_CLASS = "menu-bar";

    public MenuBar() {
        this((Menu[]) null);
    }

    public MenuBar(Menu... menus) {
        this.menus = FXCollections.observableArrayList();
        getStyleClass().setAll(DEFAULT_STYLE_CLASS);
        setAccessibleRole(AccessibleRole.MENU_BAR);
        if (menus != null) {
            getMenus().addAll(menus);
        }
        ((StyleableProperty) focusTraversableProperty()).applyStyle(null, Boolean.FALSE);
    }

    public final BooleanProperty useSystemMenuBarProperty() {
        if (this.useSystemMenuBar == null) {
            this.useSystemMenuBar = new StyleableBooleanProperty() { // from class: javafx.scene.control.MenuBar.1
                @Override // javafx.css.StyleableProperty
                public CssMetaData<? extends Styleable, Boolean> getCssMetaData() {
                    return StyleableProperties.USE_SYSTEM_MENU_BAR;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public Object getBean() {
                    return MenuBar.this;
                }

                @Override // javafx.beans.property.ReadOnlyProperty
                public String getName() {
                    return "useSystemMenuBar";
                }
            };
        }
        return this.useSystemMenuBar;
    }

    public final void setUseSystemMenuBar(boolean value) {
        useSystemMenuBarProperty().setValue(Boolean.valueOf(value));
    }

    public final boolean isUseSystemMenuBar() {
        if (this.useSystemMenuBar == null) {
            return false;
        }
        return this.useSystemMenuBar.getValue2().booleanValue();
    }

    public final ObservableList<Menu> getMenus() {
        return this.menus;
    }

    @Override // javafx.scene.control.Control
    protected Skin<?> createDefaultSkin() {
        return new MenuBarSkin(this);
    }

    /* loaded from: jfxrt.jar:javafx/scene/control/MenuBar$StyleableProperties.class */
    private static class StyleableProperties {
        private static final CssMetaData<MenuBar, Boolean> USE_SYSTEM_MENU_BAR = new CssMetaData<MenuBar, Boolean>("-fx-use-system-menu-bar", BooleanConverter.getInstance(), false) { // from class: javafx.scene.control.MenuBar.StyleableProperties.1
            @Override // javafx.css.CssMetaData
            public boolean isSettable(MenuBar n2) {
                return n2.useSystemMenuBar == null || !n2.useSystemMenuBar.isBound();
            }

            @Override // javafx.css.CssMetaData
            public StyleableProperty<Boolean> getStyleableProperty(MenuBar n2) {
                return (StyleableProperty) n2.useSystemMenuBarProperty();
            }
        };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;

        private StyleableProperties() {
        }

        static {
            List<CssMetaData<? extends Styleable, ?>> styleables = new ArrayList<>(Control.getClassCssMetaData());
            styleables.add(USE_SYSTEM_MENU_BAR);
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
