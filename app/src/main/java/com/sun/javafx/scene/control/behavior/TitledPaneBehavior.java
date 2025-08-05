package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.TitledPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TitledPaneBehavior.class */
public class TitledPaneBehavior extends BehaviorBase<TitledPane> {
    private TitledPane titledPane;
    private static final String PRESS_ACTION = "Press";
    protected static final List<KeyBinding> TITLEDPANE_BINDINGS = new ArrayList();

    public TitledPaneBehavior(TitledPane pane) {
        super(pane, TITLEDPANE_BINDINGS);
        this.titledPane = pane;
    }

    static {
        TITLEDPANE_BINDINGS.add(new KeyBinding(KeyCode.SPACE, PRESS_ACTION));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        switch (name) {
            case "Press":
                if (this.titledPane.isCollapsible() && this.titledPane.isFocused()) {
                    this.titledPane.setExpanded(!this.titledPane.isExpanded());
                    this.titledPane.requestFocus();
                    break;
                }
                break;
            default:
                super.callAction(name);
                break;
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        TitledPane tp = getControl();
        tp.requestFocus();
    }

    public void expand() {
        this.titledPane.setExpanded(true);
    }

    public void collapse() {
        this.titledPane.setExpanded(false);
    }

    public void toggle() {
        this.titledPane.setExpanded(!this.titledPane.isExpanded());
    }
}
