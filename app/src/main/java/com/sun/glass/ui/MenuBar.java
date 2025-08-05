package com.sun.glass.ui;

import com.sun.glass.ui.delegate.MenuBarDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/glass/ui/MenuBar.class */
public final class MenuBar {
    private final MenuBarDelegate delegate;
    private final List<Menu> menus = new ArrayList();

    protected MenuBar() {
        Application.checkEventThread();
        this.delegate = PlatformFactory.getPlatformFactory().createMenuBarDelegate(this);
        if (!this.delegate.createMenuBar()) {
            throw new RuntimeException("MenuBar creation error.");
        }
    }

    long getNativeMenu() {
        return this.delegate.getNativeMenu();
    }

    public void add(Menu menu) {
        Application.checkEventThread();
        insert(menu, this.menus.size());
    }

    public void insert(Menu menu, int pos) {
        Application.checkEventThread();
        synchronized (this.menus) {
            if (this.delegate.insert(menu.getDelegate(), pos)) {
                this.menus.add(pos, menu);
            }
        }
    }

    public void remove(int pos) {
        Application.checkEventThread();
        synchronized (this.menus) {
            Menu menu = this.menus.get(pos);
            if (this.delegate.remove(menu.getDelegate(), pos)) {
                this.menus.remove(pos);
            }
        }
    }

    public void remove(Menu menu) {
        Application.checkEventThread();
        synchronized (this.menus) {
            int pos = this.menus.indexOf(menu);
            if (pos >= 0 && this.delegate.remove(menu.getDelegate(), pos)) {
                this.menus.remove(pos);
            }
        }
    }

    public List<Menu> getMenus() {
        Application.checkEventThread();
        return Collections.unmodifiableList(this.menus);
    }
}
