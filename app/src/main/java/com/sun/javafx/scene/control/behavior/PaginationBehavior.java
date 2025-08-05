package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.PaginationSkin;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Pagination;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/PaginationBehavior.class */
public class PaginationBehavior extends BehaviorBase<Pagination> {
    private static final String LEFT = "Left";
    private static final String RIGHT = "Right";
    protected static final List<KeyBinding> PAGINATION_BINDINGS = new ArrayList();

    static {
        PAGINATION_BINDINGS.add(new KeyBinding(KeyCode.LEFT, LEFT));
        PAGINATION_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, RIGHT));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (action != null) {
            if (e2.getCode() == KeyCode.LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    action = RIGHT;
                }
            } else if (e2.getCode() == KeyCode.RIGHT && getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                action = LEFT;
            }
        }
        return action;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (LEFT.equals(name)) {
            PaginationSkin ps = (PaginationSkin) getControl().getSkin();
            ps.selectPrevious();
        } else if (RIGHT.equals(name)) {
            PaginationSkin ps2 = (PaginationSkin) getControl().getSkin();
            ps2.selectNext();
        } else {
            super.callAction(name);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        Pagination p2 = getControl();
        p2.requestFocus();
    }

    public PaginationBehavior(Pagination pagination) {
        super(pagination, PAGINATION_BINDINGS);
    }
}
