package com.sun.javafx.menu;

import javafx.beans.property.BooleanProperty;

/* loaded from: jfxrt.jar:com/sun/javafx/menu/CheckMenuItemBase.class */
public interface CheckMenuItemBase extends MenuItemBase {
    void setSelected(boolean z2);

    boolean isSelected();

    BooleanProperty selectedProperty();
}
