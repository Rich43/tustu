package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.ScrollPaneSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ScrollPaneBehavior.class */
public class ScrollPaneBehavior extends BehaviorBase<ScrollPane> {
    static final String TRAVERSE_DEBUG = "TraverseDebug";
    static final String HORIZONTAL_UNITDECREMENT = "HorizontalUnitDecrement";
    static final String HORIZONTAL_UNITINCREMENT = "HorizontalUnitIncrement";
    static final String VERTICAL_UNITDECREMENT = "VerticalUnitDecrement";
    static final String VERTICAL_UNITINCREMENT = "VerticalUnitIncrement";
    static final String VERTICAL_PAGEDECREMENT = "VerticalPageDecrement";
    static final String VERTICAL_PAGEINCREMENT = "VerticalPageIncrement";
    static final String VERTICAL_HOME = "VerticalHome";
    static final String VERTICAL_END = "VerticalEnd";
    protected static final List<KeyBinding> SCROLL_PANE_BINDINGS = new ArrayList();

    public ScrollPaneBehavior(ScrollPane scrollPane) {
        super(scrollPane, SCROLL_PANE_BINDINGS);
    }

    public void horizontalUnitIncrement() {
        ((ScrollPaneSkin) getControl().getSkin()).hsbIncrement();
    }

    public void horizontalUnitDecrement() {
        ((ScrollPaneSkin) getControl().getSkin()).hsbDecrement();
    }

    public void verticalUnitIncrement() {
        ((ScrollPaneSkin) getControl().getSkin()).vsbIncrement();
    }

    void verticalUnitDecrement() {
        ((ScrollPaneSkin) getControl().getSkin()).vsbDecrement();
    }

    void horizontalPageIncrement() {
        ((ScrollPaneSkin) getControl().getSkin()).hsbPageIncrement();
    }

    void horizontalPageDecrement() {
        ((ScrollPaneSkin) getControl().getSkin()).hsbPageDecrement();
    }

    void verticalPageIncrement() {
        ((ScrollPaneSkin) getControl().getSkin()).vsbPageIncrement();
    }

    void verticalPageDecrement() {
        ((ScrollPaneSkin) getControl().getSkin()).vsbPageDecrement();
    }

    void verticalHome() {
        ScrollPane sp = getControl();
        sp.setHvalue(sp.getHmin());
        sp.setVvalue(sp.getVmin());
    }

    void verticalEnd() {
        ScrollPane sp = getControl();
        sp.setHvalue(sp.getHmax());
        sp.setVvalue(sp.getVmax());
    }

    public void contentDragged(double deltaX, double deltaY) {
        ScrollPane scroll = getControl();
        if (scroll.isPannable()) {
            if ((deltaX < 0.0d && scroll.getHvalue() != 0.0d) || (deltaX > 0.0d && scroll.getHvalue() != scroll.getHmax())) {
                scroll.setHvalue(scroll.getHvalue() + deltaX);
            }
            if ((deltaY < 0.0d && scroll.getVvalue() != 0.0d) || (deltaY > 0.0d && scroll.getVvalue() != scroll.getVmax())) {
                scroll.setVvalue(scroll.getVvalue() + deltaY);
            }
        }
    }

    static {
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.F4, TRAVERSE_DEBUG).alt().ctrl().shift());
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.LEFT, HORIZONTAL_UNITDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, HORIZONTAL_UNITINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.UP, VERTICAL_UNITDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.DOWN, VERTICAL_UNITINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, VERTICAL_PAGEDECREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, VERTICAL_PAGEINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, VERTICAL_PAGEINCREMENT));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.HOME, VERTICAL_HOME));
        SCROLL_PANE_BINDINGS.add(new KeyBinding(KeyCode.END, VERTICAL_END));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (action != null) {
            if (e2.getCode() == KeyCode.LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = HORIZONTAL_UNITINCREMENT;
                }
            } else if (e2.getCode() == KeyCode.RIGHT && getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                action = HORIZONTAL_UNITDECREMENT;
            }
        }
        return action;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        switch (name) {
            case "HorizontalUnitDecrement":
                horizontalUnitDecrement();
                break;
            case "HorizontalUnitIncrement":
                horizontalUnitIncrement();
                break;
            case "VerticalUnitDecrement":
                verticalUnitDecrement();
                break;
            case "VerticalUnitIncrement":
                verticalUnitIncrement();
                break;
            case "VerticalPageDecrement":
                verticalPageDecrement();
                break;
            case "VerticalPageIncrement":
                verticalPageIncrement();
                break;
            case "VerticalHome":
                verticalHome();
                break;
            case "VerticalEnd":
                verticalEnd();
                break;
            default:
                super.callAction(name);
                break;
        }
    }

    public void mouseClicked() {
        getControl().requestFocus();
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        getControl().requestFocus();
    }
}
