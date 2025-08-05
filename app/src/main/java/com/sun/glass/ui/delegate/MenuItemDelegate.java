package com.sun.glass.ui.delegate;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;

/* loaded from: jfxrt.jar:com/sun/glass/ui/delegate/MenuItemDelegate.class */
public interface MenuItemDelegate {
    boolean createMenuItem(String str, MenuItem.Callback callback, int i2, int i3, Pixels pixels, boolean z2, boolean z3);

    boolean setTitle(String str);

    boolean setCallback(MenuItem.Callback callback);

    boolean setShortcut(int i2, int i3);

    boolean setPixels(Pixels pixels);

    boolean setEnabled(boolean z2);

    boolean setChecked(boolean z2);
}
