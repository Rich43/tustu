package com.sun.java.swing.plaf.windows;

import java.awt.Graphics;
import java.awt.Window;
import javax.swing.JWindow;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsPopupWindow.class */
class WindowsPopupWindow extends JWindow {
    static final int UNDEFINED_WINDOW_TYPE = 0;
    static final int TOOLTIP_WINDOW_TYPE = 1;
    static final int MENU_WINDOW_TYPE = 2;
    static final int SUBMENU_WINDOW_TYPE = 3;
    static final int POPUPMENU_WINDOW_TYPE = 4;
    static final int COMBOBOX_POPUP_WINDOW_TYPE = 5;
    private int windowType;

    WindowsPopupWindow(Window window) {
        super(window);
        setFocusableWindowState(false);
    }

    void setWindowType(int i2) {
        this.windowType = i2;
    }

    int getWindowType() {
        return this.windowType;
    }

    @Override // javax.swing.JWindow, java.awt.Container, java.awt.Component
    public void update(Graphics graphics) {
        paint(graphics);
    }

    @Override // java.awt.Window, java.awt.Component
    public void hide() {
        super.hide();
        removeNotify();
    }

    @Override // java.awt.Window, java.awt.Component
    public void show() {
        super.show();
        pack();
    }
}
