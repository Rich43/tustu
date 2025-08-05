package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TwoLevelFocusPopupBehavior.class */
public class TwoLevelFocusPopupBehavior extends TwoLevelFocusBehavior {
    final EventDispatcher preemptiveEventDispatcher = (event, tail) -> {
        if ((event instanceof KeyEvent) && event.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent) event).isMetaDown() && !((KeyEvent) event).isControlDown() && !((KeyEvent) event).isAltDown() && isExternalFocus()) {
            Object obj = event.getTarget();
            switch (((KeyEvent) event).getCode()) {
                case TAB:
                    if (((KeyEvent) event).isShiftDown()) {
                        ((Node) obj).impl_traverse(Direction.PREVIOUS);
                    } else {
                        ((Node) obj).impl_traverse(Direction.NEXT);
                    }
                    event.consume();
                    break;
                case UP:
                    ((Node) obj).impl_traverse(Direction.UP);
                    event.consume();
                    break;
                case DOWN:
                    ((Node) obj).impl_traverse(Direction.DOWN);
                    event.consume();
                    break;
                case LEFT:
                    ((Node) obj).impl_traverse(Direction.LEFT);
                    event.consume();
                    break;
                case RIGHT:
                    ((Node) obj).impl_traverse(Direction.RIGHT);
                    event.consume();
                    break;
                case ENTER:
                    setExternalFocus(false);
                    event.consume();
                    break;
                default:
                    Scene s2 = this.tlNode.getScene();
                    Event.fireEvent(s2, event);
                    event.consume();
                    break;
            }
        }
        return event;
    };
    final EventDispatcher preemptivePopupEventDispatcher = (event, tail) -> {
        Node node;
        Node node2;
        if ((event instanceof KeyEvent) && event.getEventType() == KeyEvent.KEY_PRESSED && !((KeyEvent) event).isMetaDown() && !((KeyEvent) event).isControlDown() && !((KeyEvent) event).isAltDown() && !isExternalFocus()) {
            Object obj = event.getTarget();
            switch (((KeyEvent) event).getCode()) {
                case TAB:
                case ENTER:
                    event.consume();
                    break;
                case UP:
                case DOWN:
                    break;
                case LEFT:
                    if (obj instanceof Node) {
                        ((Node) obj).impl_traverse(Direction.LEFT);
                        event.consume();
                        break;
                    } else if ((obj instanceof Scene) && (node2 = ((Scene) obj).getFocusOwner()) != null) {
                        node2.impl_traverse(Direction.LEFT);
                        event.consume();
                        break;
                    }
                    break;
                case RIGHT:
                    if (obj instanceof Node) {
                        ((Node) obj).impl_traverse(Direction.RIGHT);
                        event.consume();
                        break;
                    } else if ((obj instanceof Scene) && (node = ((Scene) obj).getFocusOwner()) != null) {
                        node.impl_traverse(Direction.RIGHT);
                        event.consume();
                        break;
                    }
                    break;
                default:
                    if (this.tlNode != null) {
                        Scene s2 = this.tlNode.getScene();
                        Event.fireEvent(s2, event);
                    }
                    event.consume();
                    break;
            }
        }
        return event;
    };
    final EventDispatcher tlfEventDispatcher = (event, tail) -> {
        if (event instanceof KeyEvent) {
            if (isExternalFocus()) {
                return tail.prepend(this.preemptiveEventDispatcher).dispatchEvent(event);
            }
            return tail.prepend(this.preemptivePopupEventDispatcher).prepend(this.origEventDispatcher).dispatchEvent(event);
        }
        return this.origEventDispatcher.dispatchEvent(event, tail);
    };
    private final EventHandler<KeyEvent> keyEventListener = e2 -> {
        postDispatchTidyup(e2);
    };
    final ChangeListener<Boolean> focusListener = (observable, oldVal, newVal) -> {
    };
    private final EventHandler<MouseEvent> mouseEventListener = e2 -> {
        setExternalFocus(false);
    };

    public TwoLevelFocusPopupBehavior(PopupControl popup) {
        this.tlPopup = popup;
        setExternalFocus(false);
        this.tlPopup.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlPopup.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlPopup.focusedProperty().addListener(this.focusListener);
        this.origEventDispatcher = this.tlPopup.getEventDispatcher();
        this.tlPopup.setEventDispatcher(this.tlfEventDispatcher);
    }

    public TwoLevelFocusPopupBehavior(Node node) {
        this.tlNode = node;
        setExternalFocus(false);
        this.tlNode.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlNode.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlNode.focusedProperty().addListener(this.focusListener);
        this.origEventDispatcher = this.tlNode.getEventDispatcher();
        this.tlNode.setEventDispatcher(this.tlfEventDispatcher);
    }

    @Override // com.sun.javafx.scene.control.behavior.TwoLevelFocusBehavior
    public void dispose() {
        this.tlNode.removeEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlNode.removeEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlNode.focusedProperty().removeListener(this.focusListener);
        this.tlNode.setEventDispatcher(this.origEventDispatcher);
    }

    private Event postDispatchTidyup(Event event) {
        if ((event instanceof KeyEvent) && event.getEventType() == KeyEvent.KEY_PRESSED && !isExternalFocus() && !((KeyEvent) event).isMetaDown() && !((KeyEvent) event).isControlDown() && !((KeyEvent) event).isAltDown()) {
            switch (((KeyEvent) event).getCode()) {
                case TAB:
                case UP:
                case DOWN:
                case LEFT:
                case RIGHT:
                    event.consume();
                    break;
                case ENTER:
                    setExternalFocus(true);
                    event.consume();
                    break;
            }
        }
        return event;
    }
}
