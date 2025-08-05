package com.sun.glass.ui.win;

import com.sun.glass.ui.MenuItem;
import com.sun.glass.ui.Pixels;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinMenuItemDelegate.class */
final class WinMenuItemDelegate implements MenuItemDelegate {
    private final MenuItem owner;
    private WinMenuImpl parent = null;
    private int cmdID = -1;

    public WinMenuItemDelegate(MenuItem item) {
        this.owner = item;
    }

    public MenuItem getOwner() {
        return this.owner;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean createMenuItem(String title, MenuItem.Callback callback, int shortcutKey, int shortcutModifiers, Pixels pixels, boolean enabled, boolean checked) {
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean setTitle(String title) {
        if (this.parent != null) {
            return this.parent.setItemTitle(this, getTitle(title, getOwner().getShortcutKey(), getOwner().getShortcutModifiers()));
        }
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean setCallback(MenuItem.Callback callback) {
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean setShortcut(int shortcutKey, int shortcutModifiers) {
        if (this.parent != null) {
            String title = getTitle(getOwner().getTitle(), shortcutKey, shortcutModifiers);
            return this.parent.setItemTitle(this, title);
        }
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean setPixels(Pixels pixels) {
        return false;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean setEnabled(boolean enabled) {
        if (this.parent != null) {
            return this.parent.enableItem(this, enabled);
        }
        return true;
    }

    @Override // com.sun.glass.ui.delegate.MenuItemDelegate
    public boolean setChecked(boolean checked) {
        if (this.parent != null) {
            return this.parent.checkItem(this, checked);
        }
        return true;
    }

    private String getTitle(String title, int key, int modifiers) {
        if (key == 0) {
            return title;
        }
        return title;
    }

    WinMenuImpl getParent() {
        return this.parent;
    }

    void setParent(WinMenuImpl newParent) {
        if (this.parent != null) {
            CommandIDManager.freeID(this.cmdID);
            this.cmdID = -1;
        }
        if (newParent != null) {
            this.cmdID = CommandIDManager.getID(this);
        }
        this.parent = newParent;
    }

    int getCmdID() {
        return this.cmdID;
    }

    /* loaded from: jfxrt.jar:com/sun/glass/ui/win/WinMenuItemDelegate$CommandIDManager.class */
    static class CommandIDManager {
        private static final int FIRST_ID = 1;
        private static final int LAST_ID = 65535;
        private static List<Integer> freeList = new ArrayList();
        private static final Map<Integer, WinMenuItemDelegate> map = new HashMap();
        private static int nextID = 1;

        CommandIDManager() {
        }

        public static synchronized int getID(WinMenuItemDelegate menu) {
            Integer id;
            if (freeList.isEmpty()) {
                if (nextID > 65535) {
                    nextID = 1;
                }
                id = Integer.valueOf(nextID);
                nextID++;
            } else {
                id = freeList.remove(freeList.size() - 1);
            }
            map.put(id, menu);
            return id.intValue();
        }

        public static synchronized void freeID(int cmdID) {
            Integer id = Integer.valueOf(cmdID);
            if (map.remove(id) != null) {
                freeList.add(id);
            }
        }

        public static WinMenuItemDelegate getHandler(int cmdID) {
            return map.get(Integer.valueOf(cmdID));
        }
    }
}
