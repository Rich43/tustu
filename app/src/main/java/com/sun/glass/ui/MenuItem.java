package com.sun.glass.ui;

import com.sun.glass.ui.delegate.MenuItemDelegate;

/* loaded from: jfxrt.jar:com/sun/glass/ui/MenuItem.class */
public final class MenuItem {
    public static final MenuItem Separator = null;
    private final MenuItemDelegate delegate;
    private String title;
    private Callback callback;
    private boolean enabled;
    private boolean checked;
    private int shortcutKey;
    private int shortcutModifiers;

    /* loaded from: jfxrt.jar:com/sun/glass/ui/MenuItem$Callback.class */
    public interface Callback {
        void action();

        void validate();
    }

    protected MenuItem(String title) {
        this(title, null);
    }

    protected MenuItem(String title, Callback callback) {
        this(title, callback, 0, 0);
    }

    protected MenuItem(String title, Callback callback, int shortcutKey, int shortcutModifiers) {
        this(title, callback, shortcutKey, shortcutModifiers, null);
    }

    protected MenuItem(String title, Callback callback, int shortcutKey, int shortcutModifiers, Pixels pixels) {
        Application.checkEventThread();
        this.title = title;
        this.callback = callback;
        this.shortcutKey = shortcutKey;
        this.shortcutModifiers = shortcutModifiers;
        this.enabled = true;
        this.checked = false;
        this.delegate = PlatformFactory.getPlatformFactory().createMenuItemDelegate(this);
        if (!this.delegate.createMenuItem(title, callback, shortcutKey, shortcutModifiers, pixels, this.enabled, this.checked)) {
            throw new RuntimeException("MenuItem creation error.");
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

    public Callback getCallback() {
        Application.checkEventThread();
        return this.callback;
    }

    public void setCallback(Callback callback) {
        Application.checkEventThread();
        if (this.delegate.setCallback(callback)) {
            this.callback = callback;
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

    public boolean isChecked() {
        Application.checkEventThread();
        return this.checked;
    }

    public void setChecked(boolean checked) {
        Application.checkEventThread();
        if (this.delegate.setChecked(checked)) {
            this.checked = checked;
        }
    }

    public int getShortcutKey() {
        Application.checkEventThread();
        return this.shortcutKey;
    }

    public int getShortcutModifiers() {
        Application.checkEventThread();
        return this.shortcutModifiers;
    }

    public void setShortcut(int shortcutKey, int shortcutModifiers) {
        Application.checkEventThread();
        if (this.delegate.setShortcut(shortcutKey, shortcutModifiers)) {
            this.shortcutKey = shortcutKey;
            this.shortcutModifiers = shortcutModifiers;
        }
    }

    public boolean setPixels(Pixels pixels) {
        Application.checkEventThread();
        return this.delegate.setPixels(pixels);
    }

    MenuItemDelegate getDelegate() {
        return this.delegate;
    }
}
