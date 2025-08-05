package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/NodeEventDispatcher.class */
public class NodeEventDispatcher extends CompositeEventDispatcher {
    private final EnteredExitedHandler enteredExitedHandler;
    private final EventHandlerManager eventHandlerManager;

    public NodeEventDispatcher(Object eventSource) {
        this(new EnteredExitedHandler(eventSource), new EventHandlerManager(eventSource));
    }

    public NodeEventDispatcher(EnteredExitedHandler enteredExitedHandler, EventHandlerManager eventHandlerManager) {
        this.enteredExitedHandler = enteredExitedHandler;
        this.eventHandlerManager = eventHandlerManager;
        enteredExitedHandler.insertNextDispatcher(eventHandlerManager);
    }

    public final EnteredExitedHandler getEnteredExitedHandler() {
        return this.enteredExitedHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    @Override // com.sun.javafx.event.CompositeEventDispatcher
    public BasicEventDispatcher getFirstDispatcher() {
        return this.enteredExitedHandler;
    }

    @Override // com.sun.javafx.event.CompositeEventDispatcher
    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}
