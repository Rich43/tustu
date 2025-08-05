package com.sun.glass.ui.win;

import com.sun.glass.ui.Menu;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinMenuDelegate.class */
final class WinMenuDelegate extends WinMenuImpl implements MenuDelegate {
    private final Menu owner;
    private WinMenuImpl parent = null;

    public WinMenuDelegate(Menu menu) {
        this.owner = menu;
    }

    public Menu getOwner() {
        return this.owner;
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean createMenu(String title, boolean enabled) {
        return create();
    }

    public void dispose() {
        destroy();
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean setTitle(String title) {
        if (this.parent != null) {
            return this.parent.setSubmenuTitle(this, title);
        }
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean setEnabled(boolean enabled) {
        if (this.parent != null) {
            return this.parent.enableSubmenu(this, enabled);
        }
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean setPixels(Pixels pixels) {
        return false;
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean insert(MenuDelegate menu, int pos) {
        return insertSubmenu((WinMenuDelegate) menu, pos);
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean insert(MenuItemDelegate item, int pos) {
        return insertItem((WinMenuItemDelegate) item, pos);
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean remove(MenuDelegate menu, int pos) {
        return removeMenu((WinMenuDelegate) menu, pos);
    }

    @Override // com.sun.glass.ui.delegate.MenuDelegate
    public boolean remove(MenuItemDelegate item, int pos) {
        return removeItem((WinMenuItemDelegate) item, pos);
    }

    WinMenuImpl getParent() {
        return this.parent;
    }

    void setParent(WinMenuImpl newParent) {
        this.parent = newParent;
    }
}
