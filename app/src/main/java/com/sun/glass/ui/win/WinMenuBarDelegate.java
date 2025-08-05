package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuBar;
import com.sun.glass.ui.delegate.MenuBarDelegate;
import com.sun.glass.ui.delegate.MenuDelegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinMenuBarDelegate.class */
final class WinMenuBarDelegate extends WinMenuImpl implements MenuBarDelegate {
    private final MenuBar owner;

    WinMenuBarDelegate(MenuBar menubar) {
        this.owner = menubar;
    }

    public MenuBar getOwner() {
        return this.owner;
    }

    @Override // com.sun.glass.ui.delegate.MenuBarDelegate
    public boolean createMenuBar() {
        return create();
    }

    @Override // com.sun.glass.ui.delegate.MenuBarDelegate
    public boolean insert(MenuDelegate menuDelegate, int pos) {
        WinMenuDelegate menu = (WinMenuDelegate) menuDelegate;
        if (menu.getParent() != null) {
        }
        return insertSubmenu(menu, pos);
    }

    @Override // com.sun.glass.ui.delegate.MenuBarDelegate
    public boolean remove(MenuDelegate menuDelegate, int pos) {
        WinMenuDelegate menu = (WinMenuDelegate) menuDelegate;
        return removeMenu(menu, pos);
    }

    @Override // com.sun.glass.ui.delegate.MenuBarDelegate
    public long getNativeMenu() {
        return getHMENU();
    }
}
