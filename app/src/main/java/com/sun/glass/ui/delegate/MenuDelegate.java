package com.sun.glass.ui.delegate;

import com.sun.glass.ui.Pixels;

/* loaded from: jfxrt.jar:com/sun/glass/ui/delegate/MenuDelegate.class */
public interface MenuDelegate {
    boolean createMenu(String str, boolean z2);

    boolean setTitle(String str);

    boolean setEnabled(boolean z2);

    boolean setPixels(Pixels pixels);

    boolean insert(MenuDelegate menuDelegate, int i2);

    boolean insert(MenuItemDelegate menuItemDelegate, int i2);

    boolean remove(MenuDelegate menuDelegate, int i2);

    boolean remove(MenuItemDelegate menuItemDelegate, int i2);
}
