package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Window;
import com.sun.glass.ui.win.WinMenuItemDelegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinMenuImpl.class */
class WinMenuImpl {
    private long ptr = 0;

    private static native void _initIDs();

    private native long _create();

    private native void _destroy(long j2);

    private native boolean _insertItem(long j2, int i2, int i3, String str, boolean z2, boolean z3, MenuItem.Callback callback, int i4, int i5);

    private native boolean _insertSubmenu(long j2, int i2, long j3, String str, boolean z2);

    private native boolean _insertSeparator(long j2, int i2);

    private native boolean _removeAtPos(long j2, int i2);

    private native boolean _setItemTitle(long j2, int i2, String str);

    private native boolean _setSubmenuTitle(long j2, long j3, String str);

    private native boolean _enableItem(long j2, int i2, boolean z2);

    private native boolean _enableSubmenu(long j2, long j3, boolean z2);

    private native boolean _checkItem(long j2, int i2, boolean z2);

    static {
        _initIDs();
    }

    WinMenuImpl() {
    }

    long getHMENU() {
        return this.ptr;
    }

    boolean create() {
        this.ptr = _create();
        return this.ptr != 0;
    }

    void destroy() {
        if (this.ptr != 0) {
            _destroy(this.ptr);
            this.ptr = 0L;
        }
    }

    boolean insertSubmenu(WinMenuDelegate menu, int pos) {
        menu.setParent(this);
        if (!_insertSubmenu(this.ptr, pos, menu.getHMENU(), menu.getOwner().getTitle(), menu.getOwner().isEnabled())) {
            menu.setParent(null);
            return false;
        }
        return true;
    }

    boolean insertItem(WinMenuItemDelegate item, int pos) {
        if (item == null) {
            return _insertSeparator(this.ptr, pos);
        }
        item.setParent(this);
        if (!_insertItem(this.ptr, pos, item.getCmdID(), item.getOwner().getTitle(), item.getOwner().isEnabled(), item.getOwner().isChecked(), item.getOwner().getCallback(), item.getOwner().getShortcutKey(), item.getOwner().getShortcutModifiers())) {
            item.setParent(null);
            return false;
        }
        return true;
    }

    boolean removeMenu(WinMenuDelegate submenu, int pos) {
        if (_removeAtPos(this.ptr, pos)) {
            submenu.setParent(null);
            return true;
        }
        return false;
    }

    boolean removeItem(WinMenuItemDelegate item, int pos) {
        if (_removeAtPos(this.ptr, pos)) {
            if (item != null) {
                item.setParent(null);
                return true;
            }
            return true;
        }
        return false;
    }

    boolean setSubmenuTitle(WinMenuDelegate submenu, String title) {
        return _setSubmenuTitle(this.ptr, submenu.getHMENU(), title);
    }

    boolean setItemTitle(WinMenuItemDelegate submenu, String title) {
        return _setItemTitle(this.ptr, submenu.getCmdID(), title);
    }

    boolean enableSubmenu(WinMenuDelegate submenu, boolean enable) {
        return _enableSubmenu(this.ptr, submenu.getHMENU(), enable);
    }

    boolean enableItem(WinMenuItemDelegate item, boolean enable) {
        return _enableItem(this.ptr, item.getCmdID(), enable);
    }

    public boolean checkItem(WinMenuItemDelegate item, boolean check) {
        return _checkItem(this.ptr, item.getCmdID(), check);
    }

    private static boolean notifyCommand(Window window, int cmdID) {
        MenuItem.Callback callback;
        WinMenuItemDelegate item = WinMenuItemDelegate.CommandIDManager.getHandler(cmdID);
        if (item != null && (callback = item.getOwner().getCallback()) != null) {
            callback.action();
            return true;
        }
        return false;
    }
}
