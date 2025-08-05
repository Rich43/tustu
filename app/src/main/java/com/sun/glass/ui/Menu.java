package com.sun.glass.ui;

import com.sun.glass.ui.delegate.MenuDelegate;
import com.sun.glass.ui.delegate.MenuItemDelegate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/glass/ui/Menu.class */
public final class Menu {
    private final MenuDelegate delegate;
    private String title;
    private boolean enabled;
    private final List<Object> items;
    private EventHandler eventHandler;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/Menu$EventHandler.class */
    public static class EventHandler {
        public void handleMenuOpening(Menu menu, long time) {
        }

        public void handleMenuClosed(Menu menu, long time) {
        }
    }

    public EventHandler getEventHandler() {
        Application.checkEventThread();
        return this.eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        Application.checkEventThread();
        this.eventHandler = eventHandler;
    }

    protected Menu(String title) {
        this(title, true);
    }

    protected Menu(String title, boolean enabled) {
        this.items = new ArrayList();
        Application.checkEventThread();
        this.title = title;
        this.enabled = enabled;
        this.delegate = PlatformFactory.getPlatformFactory().createMenuDelegate(this);
        if (!this.delegate.createMenu(title, enabled)) {
            throw new RuntimeException("Menu creation error.");
        }
    }

    public String getTitle() {
        Application.checkEventThread();
        return this.title;
    }

    public void setTitle(String title) {
        Application.checkEventThread();
        if (this.delegate.setTitle(title)) {
            this.title = title;
        }
    }

    public boolean isEnabled() {
        Application.checkEventThread();
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        Application.checkEventThread();
        if (this.delegate.setEnabled(enabled)) {
            this.enabled = enabled;
        }
    }

    public boolean setPixels(Pixels pixels) {
        Application.checkEventThread();
        return this.delegate.setPixels(pixels);
    }

    public List<Object> getItems() {
        Application.checkEventThread();
        return Collections.unmodifiableList(this.items);
    }

    public void add(Menu menu) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        insert(menu, this.items.size());
    }

    public void add(MenuItem item) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        insert(item, this.items.size());
    }

    public void insert(Menu menu, int pos) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        if (menu == null) {
            throw new IllegalArgumentException();
        }
        synchronized (this.items) {
            if (pos >= 0) {
                if (pos <= this.items.size()) {
                    MenuDelegate menuDelegate = menu.getDelegate();
                    if (this.delegate.insert(menuDelegate, pos)) {
                        this.items.add(pos, menu);
                    }
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    public void insert(MenuItem item, int pos) throws IndexOutOfBoundsException {
        Application.checkEventThread();
        synchronized (this.items) {
            if (pos >= 0) {
                if (pos <= this.items.size()) {
                    MenuItemDelegate itemDelegate = item != null ? item.getDelegate() : null;
                    if (this.delegate.insert(itemDelegate, pos)) {
                        this.items.add(pos, item);
                    }
                }
            }
            throw new IndexOutOfBoundsException();
        }
    }

    public void remove(int pos) throws IndexOutOfBoundsException {
        boolean success;
        Application.checkEventThread();
        synchronized (this.items) {
            Object item = this.items.get(pos);
            if (item == MenuItem.Separator) {
                success = this.delegate.remove((MenuItemDelegate) null, pos);
            } else if (item instanceof MenuItem) {
                success = this.delegate.remove(((MenuItem) item).getDelegate(), pos);
            } else {
                success = this.delegate.remove(((Menu) item).getDelegate(), pos);
            }
            if (success) {
                this.items.remove(pos);
            }
        }
    }

    MenuDelegate getDelegate() {
        return this.delegate;
    }

    protected void notifyMenuOpening() {
        if (this.eventHandler != null) {
            this.eventHandler.handleMenuOpening(this, System.nanoTime());
        }
    }

    protected void notifyMenuClosed() {
        if (this.eventHandler != null) {
            this.eventHandler.handleMenuClosed(this, System.nanoTime());
        }
    }
}
