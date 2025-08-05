package com.sun.javafx.stage;

import com.sun.javafx.event.BasicEventDispatcher;
import javafx.event.Event;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/WindowCloseRequestHandler.class */
public final class WindowCloseRequestHandler extends BasicEventDispatcher {
    private final Window window;

    public WindowCloseRequestHandler(Window window) {
        this.window = window;
    }

    @Override // com.sun.javafx.event.BasicEventDispatcher
    public Event dispatchBubblingEvent(Event event) {
        if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
            this.window.hide();
            event.consume();
        }
        return event;
    }
}
