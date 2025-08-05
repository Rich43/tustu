package com.sun.javafx.scene.control.behavior;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Cell;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.IndexedCell;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/CellBehaviorBase.class */
public abstract class CellBehaviorBase<T extends Cell> extends BehaviorBase<T> {
    private static final String ANCHOR_PROPERTY_KEY = "anchor";
    private static final String IS_DEFAULT_ANCHOR_KEY = "isDefaultAnchor";
    private boolean latePress;

    protected abstract Control getCellContainer();

    protected abstract MultipleSelectionModel<?> getSelectionModel();

    protected abstract FocusModel<?> getFocusModel();

    protected abstract void edit(T t2);

    public static <T> T getAnchor(Control control, T t2) {
        return hasNonDefaultAnchor(control) ? (T) control.getProperties().get("anchor") : t2;
    }

    public static <T> void setAnchor(Control control, T anchor, boolean isDefaultAnchor) {
        if (control != null && anchor == null) {
            removeAnchor(control);
        } else {
            control.getProperties().put("anchor", anchor);
            control.getProperties().put(IS_DEFAULT_ANCHOR_KEY, Boolean.valueOf(isDefaultAnchor));
        }
    }

    public static boolean hasNonDefaultAnchor(Control control) {
        Boolean isDefaultAnchor = (Boolean) control.getProperties().remove(IS_DEFAULT_ANCHOR_KEY);
        return (isDefaultAnchor == null || !isDefaultAnchor.booleanValue()) && hasAnchor(control);
    }

    public static boolean hasDefaultAnchor(Control control) {
        Boolean isDefaultAnchor = (Boolean) control.getProperties().remove(IS_DEFAULT_ANCHOR_KEY);
        return isDefaultAnchor != null && isDefaultAnchor.booleanValue() && hasAnchor(control);
    }

    private static boolean hasAnchor(Control control) {
        return control.getProperties().get("anchor") != null;
    }

    public static void removeAnchor(Control control) {
        control.getProperties().remove("anchor");
        control.getProperties().remove(IS_DEFAULT_ANCHOR_KEY);
    }

    public CellBehaviorBase(T control, List<KeyBinding> bindings) {
        super(control, bindings);
        this.latePress = false;
    }

    protected boolean handleDisclosureNode(double x2, double y2) {
        return false;
    }

    protected boolean isClickPositionValid(double x2, double y2) {
        return true;
    }

    protected int getIndex() {
        if (getControl() instanceof IndexedCell) {
            return ((IndexedCell) getControl()).getIndex();
        }
        return -1;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        if (e2.isSynthesized()) {
            this.latePress = true;
            return;
        }
        this.latePress = isSelected();
        if (!this.latePress) {
            doSelect(e2.getX(), e2.getY(), e2.getButton(), e2.getClickCount(), e2.isShiftDown(), e2.isShortcutDown());
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseReleased(MouseEvent e2) {
        if (this.latePress) {
            this.latePress = false;
            doSelect(e2.getX(), e2.getY(), e2.getButton(), e2.getClickCount(), e2.isShiftDown(), e2.isShortcutDown());
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mouseDragged(MouseEvent e2) {
        this.latePress = false;
    }

    protected void doSelect(double x2, double y2, MouseButton button, int clickCount, boolean shiftDown, boolean shortcutDown) {
        FocusModel<?> fm;
        T cell = getControl();
        Control cellContainer = getCellContainer();
        if (cell.isEmpty() || !cell.contains(x2, y2)) {
            return;
        }
        int index = getIndex();
        boolean selected = cell.isSelected();
        MultipleSelectionModel<?> sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null || handleDisclosureNode(x2, y2) || !isClickPositionValid(x2, y2)) {
            return;
        }
        if (shiftDown) {
            if (!hasNonDefaultAnchor(cellContainer)) {
                setAnchor(cellContainer, Integer.valueOf(fm.getFocusedIndex()), false);
            }
        } else {
            removeAnchor(cellContainer);
        }
        if (button == MouseButton.PRIMARY || (button == MouseButton.SECONDARY && !selected)) {
            if (sm.getSelectionMode() == SelectionMode.SINGLE) {
                simpleSelect(button, clickCount, shortcutDown);
                return;
            }
            if (shortcutDown) {
                if (selected) {
                    sm.clearSelection(index);
                    fm.focus(index);
                    return;
                } else {
                    sm.select(index);
                    return;
                }
            }
            if (shiftDown && clickCount == 1) {
                int focusedIndex = ((Integer) getAnchor(cellContainer, Integer.valueOf(fm.getFocusedIndex()))).intValue();
                selectRows(focusedIndex, index);
                fm.focus(index);
                return;
            }
            simpleSelect(button, clickCount, shortcutDown);
        }
    }

    protected void simpleSelect(MouseButton button, int clickCount, boolean shortcutDown) {
        int index = getIndex();
        MultipleSelectionModel<?> sm = getSelectionModel();
        boolean isAlreadySelected = sm.isSelected(index);
        if (isAlreadySelected && shortcutDown) {
            sm.clearSelection(index);
            getFocusModel().focus(index);
            isAlreadySelected = false;
        } else {
            sm.clearAndSelect(index);
        }
        handleClicks(button, clickCount, isAlreadySelected);
    }

    protected void handleClicks(MouseButton button, int clickCount, boolean isAlreadySelected) {
        if (button == MouseButton.PRIMARY) {
            if (clickCount == 1 && isAlreadySelected) {
                edit(getControl());
                return;
            }
            if (clickCount == 1) {
                edit(null);
            } else if (clickCount == 2 && getControl().isEditable()) {
                edit(getControl());
            }
        }
    }

    void selectRows(int focusedIndex, int index) {
        boolean asc = focusedIndex < index;
        int minRow = Math.min(focusedIndex, index);
        int maxRow = Math.max(focusedIndex, index);
        List<Integer> selectedIndices = new ArrayList<>(getSelectionModel().getSelectedIndices());
        int max = selectedIndices.size();
        for (int i2 = 0; i2 < max; i2++) {
            int selectedIndex = selectedIndices.get(i2).intValue();
            if (selectedIndex < minRow || selectedIndex > maxRow) {
                getSelectionModel().clearSelection(selectedIndex);
            }
        }
        if (minRow == maxRow) {
            getSelectionModel().select(minRow);
        } else if (asc) {
            getSelectionModel().selectRange(minRow, maxRow + 1);
        } else {
            getSelectionModel().selectRange(maxRow, minRow - 1);
        }
    }

    protected boolean isSelected() {
        return getControl().isSelected();
    }
}
