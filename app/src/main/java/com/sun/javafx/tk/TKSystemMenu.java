package com.sun.javafx.tk;

import com.sun.javafx.menu.MenuBase;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/javafx/tk/TKSystemMenu.class */
public interface TKSystemMenu {
    boolean isSupported();

    void setMenus(List<MenuBase> list);
}
