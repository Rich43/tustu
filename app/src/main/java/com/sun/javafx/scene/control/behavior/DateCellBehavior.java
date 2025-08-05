package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.scene.control.skin.DatePickerContent;
import com.sun.javafx.scene.traversal.Direction;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.DateCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/DateCellBehavior.class */
public class DateCellBehavior extends BehaviorBase<DateCell> {
    protected static final List<KeyBinding> DATE_CELL_BINDINGS = new ArrayList();

    static {
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "TraverseRight"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.ENTER, KeyEvent.KEY_RELEASED, "SelectDate"));
        DATE_CELL_BINDINGS.add(new KeyBinding(KeyCode.SPACE, KeyEvent.KEY_RELEASED, "SelectDate"));
    }

    public DateCellBehavior(DateCell dateCell) {
        super(dateCell, DATE_CELL_BINDINGS);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void callAction(String name) {
        DateCell cell = getControl();
        DatePickerContent dpc = findDatePickerContent(cell);
        if (dpc != null) {
            switch (name) {
                case "SelectDate":
                    dpc.selectDayCell(cell);
                    break;
                default:
                    super.callAction(name);
                    break;
            }
            return;
        }
        super.callAction(name);
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void traverse(Node node, Direction dir) {
        DatePickerContent dpc;
        boolean rtl = node.getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        switch (dir) {
            case UP:
            case DOWN:
            case LEFT:
            case RIGHT:
                if ((node instanceof DateCell) && (dpc = findDatePickerContent(node)) != null) {
                    DateCell cell = (DateCell) node;
                    switch (dir) {
                        case UP:
                            dpc.goToDayCell(cell, -1, ChronoUnit.WEEKS, true);
                            break;
                        case DOWN:
                            dpc.goToDayCell(cell, 1, ChronoUnit.WEEKS, true);
                            break;
                        case LEFT:
                            dpc.goToDayCell(cell, rtl ? 1 : -1, ChronoUnit.DAYS, true);
                            break;
                        case RIGHT:
                            dpc.goToDayCell(cell, rtl ? -1 : 1, ChronoUnit.DAYS, true);
                            break;
                    }
                    return;
                }
                break;
        }
        super.traverse(node, dir);
    }

    protected DatePickerContent findDatePickerContent(Node node) {
        Node parent = node;
        do {
            Parent parent2 = parent.getParent();
            parent = parent2;
            if (parent2 == null) {
                break;
            }
        } while (!(parent instanceof DatePickerContent));
        return (DatePickerContent) parent;
    }
}
