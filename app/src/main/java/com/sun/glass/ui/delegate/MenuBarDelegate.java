package com.sun.glass.ui.delegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/delegate/MenuBarDelegate.class */
public interface MenuBarDelegate {
    boolean createMenuBar();

    boolean insert(MenuDelegate menuDelegate, int i2);

    boolean remove(MenuDelegate menuDelegate, int i2);

    long getNativeMenu();
}
