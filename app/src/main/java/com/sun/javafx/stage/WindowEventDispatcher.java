package com.sun.javafx.stage;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;
import com.sun.javafx.event.EventRedirector;
import javafx.stage.Window;

/* loaded from: jfxrt.jar:com/sun/javafx/stage/WindowEventDispatcher.class */
public class WindowEventDispatcher extends CompositeEventDispatcher {
    private final EventRedirector eventRedirector;
    private final WindowCloseRequestHandler windowCloseRequestHandler;
    private final EventHandlerManager eventHandlerManager;

    public WindowEventDispatcher(Window window) {
        this(new EventRedirector(window), new WindowCloseRequestHandler(window), new EventHandlerManager(window));
    }

    public WindowEventDispatcher(EventRedirector eventRedirector, WindowCloseRequestHandler windowCloseRequestHandler, EventHandlerManager eventHandlerManager) {
        this.eventRedirector = eventRedirector;
        this.windowCloseRequestHandler = windowCloseRequestHandler;
        this.eventHandlerManager = eventHandlerManager;
        eventRedirector.insertNextDispatcher(windowCloseRequestHandler);
        windowCloseRequestHandler.insertNextDispatcher(eventHandlerManager);
    }

    public final EventRedirector getEventRedirector() {
        return this.eventRedirector;
    }

    public final WindowCloseRequestHandler getWindowCloseRequestHandler() {
        return this.windowCloseRequestHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    @Override // com.sun.javafx.event.CompositeEventDispatcher
    public BasicEventDispatcher getFirstDispatcher() {
        return this.eventRedirector;
    }

    @Override // com.sun.javafx.event.CompositeEventDispatcher
    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}
