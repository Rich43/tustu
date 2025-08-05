package com.sun.javafx.scene;

import com.sun.javafx.event.BasicEventDispatcher;
import com.sun.javafx.event.CompositeEventDispatcher;
import com.sun.javafx.event.EventHandlerManager;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/SceneEventDispatcher.class */
public class SceneEventDispatcher extends CompositeEventDispatcher {
    private final KeyboardShortcutsHandler keyboardShortcutsHandler;
    private final EnteredExitedHandler enteredExitedHandler;
    private final EventHandlerManager eventHandlerManager;

    public SceneEventDispatcher(Object eventSource) {
        this(new KeyboardShortcutsHandler(), new EnteredExitedHandler(eventSource), new EventHandlerManager(eventSource));
    }

    public SceneEventDispatcher(KeyboardShortcutsHandler keyboardShortcutsHandler, EnteredExitedHandler enteredExitedHandler, EventHandlerManager eventHandlerManager) {
        this.keyboardShortcutsHandler = keyboardShortcutsHandler;
        this.enteredExitedHandler = enteredExitedHandler;
        this.eventHandlerManager = eventHandlerManager;
        keyboardShortcutsHandler.insertNextDispatcher(enteredExitedHandler);
        enteredExitedHandler.insertNextDispatcher(eventHandlerManager);
    }

    public final KeyboardShortcutsHandler getKeyboardShortcutsHandler() {
        return this.keyboardShortcutsHandler;
    }

    public final EnteredExitedHandler getEnteredExitedHandler() {
        return this.enteredExitedHandler;
    }

    public final EventHandlerManager getEventHandlerManager() {
        return this.eventHandlerManager;
    }

    @Override // com.sun.javafx.event.CompositeEventDispatcher
    public BasicEventDispatcher getFirstDispatcher() {
        return this.keyboardShortcutsHandler;
    }

    @Override // com.sun.javafx.event.CompositeEventDispatcher
    public BasicEventDispatcher getLastDispatcher() {
        return this.eventHandlerManager;
    }
}
