package com.sun.javafx.scene.control.skin;

import com.sun.javafx.scene.control.MultiplePropertyChangeListenerHandler;
import com.sun.javafx.scene.control.behavior.BehaviorBase;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.control.SkinBase;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/skin/BehaviorSkinBase.class */
public abstract class BehaviorSkinBase<C extends Control, BB extends BehaviorBase<C>> extends SkinBase<C> {
    protected static final boolean IS_TOUCH_SUPPORTED = Platform.isSupported(ConditionalFeature.INPUT_TOUCH);
    private BB behavior;
    private MultiplePropertyChangeListenerHandler changeListenerHandler;
    private final EventHandler<MouseEvent> mouseHandler;
    private final EventHandler<ContextMenuEvent> contextMenuHandler;

    protected BehaviorSkinBase(C control, BB behavior) {
        super(control);
        this.mouseHandler = new EventHandler<MouseEvent>() { // from class: com.sun.javafx.scene.control.skin.BehaviorSkinBase.1
            @Override // javafx.event.EventHandler
            public void handle(MouseEvent e2) {
                EventType<?> type = e2.getEventType();
                if (type == MouseEvent.MOUSE_ENTERED) {
                    BehaviorSkinBase.this.behavior.mouseEntered(e2);
                    return;
                }
                if (type == MouseEvent.MOUSE_EXITED) {
                    BehaviorSkinBase.this.behavior.mouseExited(e2);
                    return;
                }
                if (type == MouseEvent.MOUSE_PRESSED) {
                    BehaviorSkinBase.this.behavior.mousePressed(e2);
                } else if (type == MouseEvent.MOUSE_RELEASED) {
                    BehaviorSkinBase.this.behavior.mouseReleased(e2);
                } else {
                    if (type == MouseEvent.MOUSE_DRAGGED) {
                        BehaviorSkinBase.this.behavior.mouseDragged(e2);
                        return;
                    }
                    throw new AssertionError((Object) "Unsupported event type received");
                }
            }
        };
        this.contextMenuHandler = new EventHandler<ContextMenuEvent>() { // from class: com.sun.javafx.scene.control.skin.BehaviorSkinBase.2
            @Override // javafx.event.EventHandler
            public void handle(ContextMenuEvent event) {
                BehaviorSkinBase.this.behavior.contextMenuRequested(event);
            }
        };
        if (behavior == null) {
            throw new IllegalArgumentException("Cannot pass null for behavior");
        }
        this.behavior = behavior;
        control.addEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
        control.addEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
        control.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, this.contextMenuHandler);
    }

    public final BB getBehavior() {
        return this.behavior;
    }

    @Override // javafx.scene.control.SkinBase, javafx.scene.control.Skin
    public void dispose() {
        if (this.changeListenerHandler != null) {
            this.changeListenerHandler.dispose();
        }
        C control = getSkinnable();
        if (control != null) {
            control.removeEventHandler(MouseEvent.MOUSE_ENTERED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_EXITED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_RELEASED, this.mouseHandler);
            control.removeEventHandler(MouseEvent.MOUSE_DRAGGED, this.mouseHandler);
        }
        if (this.behavior != null) {
            this.behavior.dispose();
            this.behavior = null;
        }
        super.dispose();
    }

    protected final void registerChangeListener(ObservableValue<?> property, String reference) {
        if (this.changeListenerHandler == null) {
            this.changeListenerHandler = new MultiplePropertyChangeListenerHandler(p2 -> {
                handleControlPropertyChanged(p2);
                return null;
            });
        }
        this.changeListenerHandler.registerChangeListener(property, reference);
    }

    protected final void unregisterChangeListener(ObservableValue<?> property) {
        this.changeListenerHandler.unregisterChangeListener(property);
    }

    protected void handleControlPropertyChanged(String propertyReference) {
    }
}
