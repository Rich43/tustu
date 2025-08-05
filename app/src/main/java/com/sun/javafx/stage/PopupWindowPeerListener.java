package com.sun.javafx.stage;

import com.sun.javafx.tk.FocusCause;
import javafx.stage.PopupWindow;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/PopupWindowPeerListener.class */
public class PopupWindowPeerListener extends WindowPeerListener {
    private final PopupWindow popupWindow;

    public PopupWindowPeerListener(PopupWindow popupWindow) {
        super(popupWindow);
        this.popupWindow = popupWindow;
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedFocused(boolean cf, FocusCause cause) {
        this.popupWindow.setFocused(cf);
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void closing() {
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedLocation(float x2, float y2) {
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedIconified(boolean iconified) {
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedMaximized(boolean maximized) {
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedResizable(boolean resizable) {
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void changedFullscreen(boolean fs) {
    }

    @Override // com.sun.javafx.stage.WindowPeerListener, com.sun.javafx.tk.TKStageListener
    public void focusUngrab() {
    }
}
