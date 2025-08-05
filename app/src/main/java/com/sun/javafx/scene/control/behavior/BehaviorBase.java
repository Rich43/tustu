package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.traversal.Direction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/BehaviorBase.class */
public class BehaviorBase<C extends Control> {
    protected static final boolean IS_TOUCH_SUPPORTED = Platform.isSupported(ConditionalFeature.INPUT_TOUCH);
    protected static final List<KeyBinding> TRAVERSAL_BINDINGS = new ArrayList();
    static final String TRAVERSE_UP = "TraverseUp";
    static final String TRAVERSE_DOWN = "TraverseDown";
    static final String TRAVERSE_LEFT = "TraverseLeft";
    static final String TRAVERSE_RIGHT = "TraverseRight";
    static final String TRAVERSE_NEXT = "TraverseNext";
    static final String TRAVERSE_PREVIOUS = "TraversePrevious";
    private final C control;
    private final List<KeyBinding> keyBindings;
    private final EventHandler<KeyEvent> keyEventListener = e2 -> {
        if (!e2.isConsumed()) {
            callActionForEvent(e2);
        }
    };
    private final InvalidationListener focusListener = property -> {
        focusChanged();
    };

    static {
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.UP, TRAVERSE_UP));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, TRAVERSE_DOWN));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, TRAVERSE_LEFT));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, TRAVERSE_RIGHT));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_NEXT));
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_PREVIOUS).shift());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.UP, TRAVERSE_UP).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, TRAVERSE_DOWN).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, TRAVERSE_LEFT).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, TRAVERSE_RIGHT).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_NEXT).shift().alt().ctrl());
        TRAVERSAL_BINDINGS.add(new KeyBinding(KeyCode.TAB, TRAVERSE_PREVIOUS).alt().ctrl());
    }

    public BehaviorBase(C control, List<KeyBinding> keyBindings) {
        this.control = control;
        this.keyBindings = keyBindings == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(keyBindings));
        control.addEventHandler(KeyEvent.ANY, this.keyEventListener);
        control.focusedProperty().addListener(this.focusListener);
    }

    public void dispose() {
        this.control.removeEventHandler(KeyEvent.ANY, this.keyEventListener);
        this.control.focusedProperty().removeListener(this.focusListener);
    }

    public final C getControl() {
        return this.control;
    }

    protected void callActionForEvent(KeyEvent e2) {
        String action = matchActionForEvent(e2);
        if (action != null) {
            callAction(action);
            e2.consume();
        }
    }

    protected String matchActionForEvent(KeyEvent e2) {
        if (e2 == null) {
            throw new NullPointerException("KeyEvent must not be null");
        }
        KeyBinding match = null;
        int specificity = 0;
        int maxBindings = this.keyBindings.size();
        for (int i2 = 0; i2 < maxBindings; i2++) {
            KeyBinding binding = this.keyBindings.get(i2);
            int s2 = binding.getSpecificity(this.control, e2);
            if (s2 > specificity) {
                specificity = s2;
                match = binding;
            }
        }
        String action = null;
        if (match != null) {
            action = match.getAction();
        }
        return action;
    }

    protected void callAction(String name) {
        switch (name) {
            case "TraverseUp":
                traverseUp();
                break;
            case "TraverseDown":
                traverseDown();
                break;
            case "TraverseLeft":
                traverseLeft();
                break;
            case "TraverseRight":
                traverseRight();
                break;
            case "TraverseNext":
                traverseNext();
                break;
            case "TraversePrevious":
                traversePrevious();
                break;
        }
    }

    protected void traverse(Node node, Direction dir) {
        node.impl_traverse(dir);
    }

    public final void traverseUp() {
        traverse(this.control, Direction.UP);
    }

    public final void traverseDown() {
        traverse(this.control, Direction.DOWN);
    }

    public final void traverseLeft() {
        traverse(this.control, Direction.LEFT);
    }

    public final void traverseRight() {
        traverse(this.control, Direction.RIGHT);
    }

    public final void traverseNext() {
        traverse(this.control, Direction.NEXT);
    }

    public final void traversePrevious() {
        traverse(this.control, Direction.PREVIOUS);
    }

    protected void focusChanged() {
    }

    public void mousePressed(MouseEvent e2) {
    }

    public void mouseDragged(MouseEvent e2) {
    }

    public void mouseReleased(MouseEvent e2) {
    }

    public void mouseEntered(MouseEvent e2) {
    }

    public void mouseExited(MouseEvent e2) {
    }

    public void contextMenuRequested(ContextMenuEvent e2) {
    }
}
