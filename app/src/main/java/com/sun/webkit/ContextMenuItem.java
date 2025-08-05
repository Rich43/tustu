package com.sun.webkit;

/* loaded from: jfxrt.jar:com/sun/webkit/ContextMenuItem.class */
public final class ContextMenuItem {
    public static final int ACTION_TYPE = 0;
    public static final int SEPARATOR_TYPE = 1;
    public static final int SUBMENU_TYPE = 2;
    private String title;
    private int action;
    private boolean isEnabled;
    private boolean isChecked;
    private int type;
    private ContextMenu submenu;

    public String getTitle() {
        return this.title;
    }

    public int getAction() {
        return this.action;
    }

    public boolean isEnabled() {
        return this.isEnabled;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public int getType() {
        return this.type;
    }

    public ContextMenu getSubmenu() {
        return this.submenu;
    }

    public String toString() {
        return String.format("%s[title='%s', action=%d, enabled=%b, checked=%b, type=%d]", super.toString(), this.title, Integer.valueOf(this.action), Boolean.valueOf(this.isEnabled), Boolean.valueOf(this.isChecked), Integer.valueOf(this.type));
    }

    private static ContextMenuItem fwkCreateContextMenuItem() {
        return new ContextMenuItem();
    }

    private void fwkSetTitle(String title) {
        this.title = title;
    }

    private String fwkGetTitle() {
        return getTitle();
    }

    private void fwkSetAction(int action) {
        this.action = action;
    }

    private int fwkGetAction() {
        return getAction();
    }

    private void fwkSetEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    private boolean fwkIsEnabled() {
        return isEnabled();
    }

    private void fwkSetChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    private void fwkSetType(int type) {
        this.type = type;
    }

    private int fwkGetType() {
        return getType();
    }

    private void fwkSetSubmenu(ContextMenu submenu) {
        this.submenu = submenu;
    }

    private ContextMenu fwkGetSubmenu() {
        return getSubmenu();
    }
}
