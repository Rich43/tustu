package com.sun.javafx.stage;

import com.sun.javafx.tk.FocusCause;
import com.sun.javafx.tk.TKStageListener;
import javafx.event.Event;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/WindowPeerListener.class */
public class WindowPeerListener implements TKStageListener {
    private final Window window;

    public WindowPeerListener(Window window) {
        this.window = window;
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedLocation(float x2, float y2) {
        WindowHelper.notifyLocationChanged(this.window, x2, y2);
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedSize(float width, float height) {
        WindowHelper.notifySizeChanged(this.window, width, height);
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedFocused(boolean focused, FocusCause cause) {
        this.window.setFocused(focused);
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedIconified(boolean iconified) {
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedMaximized(boolean maximized) {
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedResizable(boolean resizable) {
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedFullscreen(boolean fs) {
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedAlwaysOnTop(boolean aot) {
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void changedScreen(Object from, Object to) {
        WindowHelper.getWindowAccessor().notifyScreenChanged(this.window, from, to);
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void closing() {
        Event.fireEvent(this.window, new WindowEvent(this.window, WindowEvent.WINDOW_CLOSE_REQUEST));
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void closed() {
        if (this.window.isShowing()) {
            this.window.hide();
        }
    }

    @Override // com.sun.javafx.tk.TKStageListener
    public void focusUngrab() {
        Event.fireEvent(this.window, new FocusUngrabEvent());
    }
}
