package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.traversal.Direction;
import javafx.beans.value.ChangeListener;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventDispatcher;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Control;
import javafx.scene.control.PopupControl;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TwoLevelFocusBehavior.class */
public class TwoLevelFocusBehavior {
    Node tlNode;
    PopupControl tlPopup;
    EventDispatcher origEventDispatcher;
    final EventDispatcher preemptiveEventDispatcher;
    final EventDispatcher tlfEventDispatcher;
    private final EventHandler<KeyEvent> keyEventListener;
    final ChangeListener<Boolean> focusListener;
    private final EventHandler<MouseEvent> mouseEventListener;
    private boolean externalFocus;
    private static final PseudoClass INTERNAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("internal-focus");
    private static final PseudoClass EXTERNAL_PSEUDOCLASS_STATE = PseudoClass.getPseudoClass("external-focus");

    public TwoLevelFocusBehavior() {
        this.tlNode = null;
        this.tlPopup = null;
        this.origEventDispatcher = null;
        this.preemptiveEventDispatcher = (event, tail) -> {
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
        this.tlfEventDispatcher = (event2, tail2) -> {
            if ((event2 instanceof KeyEvent) && isExternalFocus()) {
                return tail2.prepend(this.preemptiveEventDispatcher).dispatchEvent(event2);
            }
            return this.origEventDispatcher.dispatchEvent(event2, tail2);
        };
        this.keyEventListener = e2 -> {
            postDispatchTidyup(e2);
        };
        this.focusListener = (observable, oldVal, newVal) -> {
            if (newVal.booleanValue() && this.tlPopup != null) {
                setExternalFocus(false);
            } else {
                setExternalFocus(true);
            }
        };
        this.mouseEventListener = e3 -> {
            setExternalFocus(false);
        };
        this.externalFocus = true;
    }

    public TwoLevelFocusBehavior(Node node) {
        this.tlNode = null;
        this.tlPopup = null;
        this.origEventDispatcher = null;
        this.preemptiveEventDispatcher = (event, tail) -> {
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
        this.tlfEventDispatcher = (event2, tail2) -> {
            if ((event2 instanceof KeyEvent) && isExternalFocus()) {
                return tail2.prepend(this.preemptiveEventDispatcher).dispatchEvent(event2);
            }
            return this.origEventDispatcher.dispatchEvent(event2, tail2);
        };
        this.keyEventListener = e2 -> {
            postDispatchTidyup(e2);
        };
        this.focusListener = (observable, oldVal, newVal) -> {
            if (newVal.booleanValue() && this.tlPopup != null) {
                setExternalFocus(false);
            } else {
                setExternalFocus(true);
            }
        };
        this.mouseEventListener = e3 -> {
            setExternalFocus(false);
        };
        this.externalFocus = true;
        this.tlNode = node;
        this.tlPopup = null;
        this.tlNode.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.tlNode.addEventHandler(MouseEvent.MOUSE_PRESSED, this.mouseEventListener);
        this.tlNode.focusedProperty().addListener(this.focusListener);
        this.origEventDispatcher = this.tlNode.getEventDispatcher();
        this.tlNode.setEventDispatcher(this.tlfEventDispatcher);
    }

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

    public boolean isExternalFocus() {
        return this.externalFocus;
    }

    public void setExternalFocus(boolean value) {
        this.externalFocus = value;
        if (this.tlNode != null && (this.tlNode instanceof Control)) {
            this.tlNode.pseudoClassStateChanged(INTERNAL_PSEUDOCLASS_STATE, !value);
            this.tlNode.pseudoClassStateChanged(EXTERNAL_PSEUDOCLASS_STATE, value);
        } else if (this.tlPopup != null) {
            this.tlPopup.pseudoClassStateChanged(INTERNAL_PSEUDOCLASS_STATE, !value);
            this.tlPopup.pseudoClassStateChanged(EXTERNAL_PSEUDOCLASS_STATE, value);
        }
    }
}
