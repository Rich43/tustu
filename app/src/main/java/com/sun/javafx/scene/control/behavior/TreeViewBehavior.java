package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.FocusModel;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import sun.security.tools.policytool.ToolWindow;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TreeViewBehavior.class */
public class TreeViewBehavior<T> extends BehaviorBase<TreeView<T>> {
    protected static final List<KeyBinding> TREE_VIEW_BINDINGS = new ArrayList();
    private boolean isShiftDown;
    private boolean isShortcutDown;
    private Callback<Boolean, Integer> onScrollPageUp;
    private Callback<Boolean, Integer> onScrollPageDown;
    private Runnable onSelectPreviousRow;
    private Runnable onSelectNextRow;
    private Runnable onMoveToFirstCell;
    private Runnable onMoveToLastCell;
    private Runnable onFocusPreviousRow;
    private Runnable onFocusNextRow;
    private boolean selectionChanging;
    private final ListChangeListener<Integer> selectedIndicesListener;
    private final ChangeListener<MultipleSelectionModel<TreeItem<T>>> selectionModelListener;
    private final WeakListChangeListener<Integer> weakSelectedIndicesListener;
    private final WeakChangeListener<MultipleSelectionModel<TreeItem<T>>> weakSelectionModelListener;

    static {
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectAllToLastRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocus").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "FocusFirstRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "FocusLastRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection"));
        if (PlatformUtil.isMac()) {
            TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl().shortcut());
        } else {
            TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl());
        }
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.A, "SelectAll").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "FocusPreviousRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "FocusNextRow").shortcut());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow").shortcut().shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "CollapseRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "CollapseRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "ExpandRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "ExpandRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.MULTIPLY, "ExpandAll"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ADD, "ExpandRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SUBTRACT, "CollapseRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "SelectPreviousRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "SelectPreviousRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "SelectNextRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "SelectNextRow"));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "AlsoSelectPreviousRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "AlsoSelectPreviousRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "AlsoSelectNextRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "AlsoSelectNextRow").shift());
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, ToolWindow.EDIT_KEYSTORE));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, ToolWindow.EDIT_KEYSTORE));
        TREE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
            if ("CollapseRow".equals(action) && (e2.getCode() == KeyCode.LEFT || e2.getCode() == KeyCode.KP_LEFT)) {
                action = "ExpandRow";
            } else if ("ExpandRow".equals(action) && (e2.getCode() == KeyCode.RIGHT || e2.getCode() == KeyCode.KP_RIGHT)) {
                action = "CollapseRow";
            }
        }
        return action;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        if (!"SelectPreviousRow".equals(name)) {
            if (!"SelectNextRow".equals(name)) {
                if (!"SelectFirstRow".equals(name)) {
                    if (!"SelectLastRow".equals(name)) {
                        if (!"SelectAllPageUp".equals(name)) {
                            if (!"SelectAllPageDown".equals(name)) {
                                if (!"SelectAllToFirstRow".equals(name)) {
                                    if (!"SelectAllToLastRow".equals(name)) {
                                        if (!"AlsoSelectNextRow".equals(name)) {
                                            if (!"AlsoSelectPreviousRow".equals(name)) {
                                                if (!"ClearSelection".equals(name)) {
                                                    if (!"SelectAll".equals(name)) {
                                                        if (!"ScrollUp".equals(name)) {
                                                            if (!"ScrollDown".equals(name)) {
                                                                if (!"ExpandRow".equals(name)) {
                                                                    if (!"CollapseRow".equals(name)) {
                                                                        if (!"ExpandAll".equals(name)) {
                                                                            if (!ToolWindow.EDIT_KEYSTORE.equals(name)) {
                                                                                if (!"CancelEdit".equals(name)) {
                                                                                    if (!"FocusFirstRow".equals(name)) {
                                                                                        if (!"FocusLastRow".equals(name)) {
                                                                                            if (!"toggleFocusOwnerSelection".equals(name)) {
                                                                                                if (!"SelectAllToFocus".equals(name)) {
                                                                                                    if (!"SelectAllToFocusAndSetAnchor".equals(name)) {
                                                                                                        if (!"FocusPageUp".equals(name)) {
                                                                                                            if (!"FocusPageDown".equals(name)) {
                                                                                                                if (!"FocusPreviousRow".equals(name)) {
                                                                                                                    if (!"FocusNextRow".equals(name)) {
                                                                                                                        if (!"DiscontinuousSelectNextRow".equals(name)) {
                                                                                                                            if (!"DiscontinuousSelectPreviousRow".equals(name)) {
                                                                                                                                if (!"DiscontinuousSelectPageUp".equals(name)) {
                                                                                                                                    if (!"DiscontinuousSelectPageDown".equals(name)) {
                                                                                                                                        if (!"DiscontinuousSelectAllToLastRow".equals(name)) {
                                                                                                                                            if (!"DiscontinuousSelectAllToFirstRow".equals(name)) {
                                                                                                                                                super.callAction(name);
                                                                                                                                                return;
                                                                                                                                            } else {
                                                                                                                                                discontinuousSelectAllToFirstRow();
                                                                                                                                                return;
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                        discontinuousSelectAllToLastRow();
                                                                                                                                        return;
                                                                                                                                    }
                                                                                                                                    discontinuousSelectPageDown();
                                                                                                                                    return;
                                                                                                                                }
                                                                                                                                discontinuousSelectPageUp();
                                                                                                                                return;
                                                                                                                            }
                                                                                                                            discontinuousSelectPreviousRow();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                        discontinuousSelectNextRow();
                                                                                                                        return;
                                                                                                                    }
                                                                                                                    focusNextRow();
                                                                                                                    return;
                                                                                                                }
                                                                                                                focusPreviousRow();
                                                                                                                return;
                                                                                                            }
                                                                                                            focusPageDown();
                                                                                                            return;
                                                                                                        }
                                                                                                        focusPageUp();
                                                                                                        return;
                                                                                                    }
                                                                                                    selectAllToFocus(true);
                                                                                                    return;
                                                                                                }
                                                                                                selectAllToFocus(false);
                                                                                                return;
                                                                                            }
                                                                                            toggleFocusOwnerSelection();
                                                                                            return;
                                                                                        }
                                                                                        focusLastRow();
                                                                                        return;
                                                                                    }
                                                                                    focusFirstRow();
                                                                                    return;
                                                                                }
                                                                                cancelEdit();
                                                                                return;
                                                                            }
                                                                            edit();
                                                                            return;
                                                                        }
                                                                        expandAll();
                                                                        return;
                                                                    }
                                                                    collapseRow();
                                                                    return;
                                                                }
                                                                expandRow();
                                                                return;
                                                            }
                                                            scrollDown();
                                                            return;
                                                        }
                                                        scrollUp();
                                                        return;
                                                    }
                                                    selectAll();
                                                    return;
                                                }
                                                clearSelection();
                                                return;
                                            }
                                            alsoSelectPreviousRow();
                                            return;
                                        }
                                        alsoSelectNextRow();
                                        return;
                                    }
                                    selectAllToLastRow();
                                    return;
                                }
                                selectAllToFirstRow();
                                return;
                            }
                            selectAllPageDown();
                            return;
                        }
                        selectAllPageUp();
                        return;
                    }
                    selectLastRow();
                    return;
                }
                selectFirstRow();
                return;
            }
            selectNextRow();
            return;
        }
        selectPreviousRow();
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callActionForEvent(KeyEvent e2) {
        this.isShiftDown = e2.getEventType() == KeyEvent.KEY_PRESSED && e2.isShiftDown();
        this.isShortcutDown = e2.getEventType() == KeyEvent.KEY_PRESSED && e2.isShortcutDown();
        super.callActionForEvent(e2);
    }

    public void setOnScrollPageUp(Callback<Boolean, Integer> c2) {
        this.onScrollPageUp = c2;
    }

    public void setOnScrollPageDown(Callback<Boolean, Integer> c2) {
        this.onScrollPageDown = c2;
    }

    public void setOnSelectPreviousRow(Runnable r2) {
        this.onSelectPreviousRow = r2;
    }

    public void setOnSelectNextRow(Runnable r2) {
        this.onSelectNextRow = r2;
    }

    public void setOnMoveToFirstCell(Runnable r2) {
        this.onMoveToFirstCell = r2;
    }

    public void setOnMoveToLastCell(Runnable r2) {
        this.onMoveToLastCell = r2;
    }

    public void setOnFocusPreviousRow(Runnable r2) {
        this.onFocusPreviousRow = r2;
    }

    public void setOnFocusNextRow(Runnable r2) {
        this.onFocusNextRow = r2;
    }

    public TreeViewBehavior(TreeView<T> control) {
        super(control, TREE_VIEW_BINDINGS);
        this.isShiftDown = false;
        this.isShortcutDown = false;
        this.selectionChanging = false;
        this.selectedIndicesListener = c2 -> {
            while (c2.next()) {
                if (c2.wasReplaced() && TreeCellBehavior.hasDefaultAnchor(getControl())) {
                    TreeCellBehavior.removeAnchor(getControl());
                }
                int shift = c2.wasPermutated() ? c2.getTo() - c2.getFrom() : 0;
                MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
                if (!this.selectionChanging) {
                    if (sm.isEmpty()) {
                        setAnchor(-1);
                    } else if (hasAnchor() && !sm.isSelected(getAnchor() + shift)) {
                        setAnchor(-1);
                    }
                }
                int addedSize = c2.getAddedSize();
                if (addedSize > 0 && !hasAnchor()) {
                    List<? extends Integer> addedSubList = c2.getAddedSubList();
                    int index = ((Integer) addedSubList.get(addedSize - 1)).intValue();
                    setAnchor(index);
                }
            }
        };
        this.selectionModelListener = new ChangeListener<MultipleSelectionModel<TreeItem<T>>>() { // from class: com.sun.javafx.scene.control.behavior.TreeViewBehavior.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends MultipleSelectionModel<TreeItem<T>>> observable, MultipleSelectionModel<TreeItem<T>> oldValue, MultipleSelectionModel<TreeItem<T>> newValue) {
                if (oldValue != null) {
                    oldValue.getSelectedIndices().removeListener(TreeViewBehavior.this.weakSelectedIndicesListener);
                }
                if (newValue != null) {
                    newValue.getSelectedIndices().addListener(TreeViewBehavior.this.weakSelectedIndicesListener);
                }
            }
        };
        this.weakSelectedIndicesListener = new WeakListChangeListener<>(this.selectedIndicesListener);
        this.weakSelectionModelListener = new WeakChangeListener<>(this.selectionModelListener);
        getControl().selectionModelProperty().addListener(this.weakSelectionModelListener);
        if (control.getSelectionModel() != null) {
            control.getSelectionModel().getSelectedIndices().addListener(this.weakSelectedIndicesListener);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        TreeCellBehavior.removeAnchor(getControl());
        super.dispose();
    }

    private void setAnchor(int anchor) {
        TreeCellBehavior.setAnchor(getControl(), anchor < 0 ? null : Integer.valueOf(anchor), false);
    }

    private int getAnchor() {
        return ((Integer) TreeCellBehavior.getAnchor(getControl(), Integer.valueOf(getControl().getFocusModel().getFocusedIndex()))).intValue();
    }

    private boolean hasAnchor() {
        return TreeCellBehavior.hasNonDefaultAnchor(getControl());
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        if (!e2.isShiftDown()) {
            int index = getControl().getSelectionModel().getSelectedIndex();
            setAnchor(index);
        }
        if (!getControl().isFocused() && getControl().isFocusTraversable()) {
            getControl().requestFocus();
        }
    }

    private void clearSelection() {
        getControl().getSelectionModel().clearSelection();
    }

    private void scrollUp() {
        MultipleSelectionModel<TreeItem<T>> sm;
        int newSelectedIndex = -1;
        if (this.onScrollPageUp != null) {
            newSelectedIndex = this.onScrollPageUp.call(false).intValue();
        }
        if (newSelectedIndex == -1 || (sm = getControl().getSelectionModel()) == null) {
            return;
        }
        sm.clearAndSelect(newSelectedIndex);
    }

    private void scrollDown() {
        MultipleSelectionModel<TreeItem<T>> sm;
        int newSelectedIndex = -1;
        if (this.onScrollPageDown != null) {
            newSelectedIndex = this.onScrollPageDown.call(false).intValue();
        }
        if (newSelectedIndex == -1 || (sm = getControl().getSelectionModel()) == null) {
            return;
        }
        sm.clearAndSelect(newSelectedIndex);
    }

    private void focusFirstRow() {
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(0);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void focusLastRow() {
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(getControl().getExpandedItemCount() - 1);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void focusPreviousRow() {
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        fm.focusPrevious();
        if (!this.isShortcutDown || getAnchor() == -1) {
            setAnchor(fm.getFocusedIndex());
        }
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    private void focusNextRow() {
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        fm.focusNext();
        if (!this.isShortcutDown || getAnchor() == -1) {
            setAnchor(fm.getFocusedIndex());
        }
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    private void focusPageUp() {
        int newFocusIndex = this.onScrollPageUp.call(true).intValue();
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(newFocusIndex);
    }

    private void focusPageDown() {
        int newFocusIndex = this.onScrollPageDown.call(true).intValue();
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(newFocusIndex);
    }

    private void alsoSelectPreviousRow() {
        MultipleSelectionModel<TreeItem<T>> sm;
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null || (sm = getControl().getSelectionModel()) == null) {
            return;
        }
        if (this.isShiftDown && getAnchor() != -1) {
            int newRow = fm.getFocusedIndex() - 1;
            if (newRow < 0) {
                return;
            }
            int anchor = getAnchor();
            if (!hasAnchor()) {
                setAnchor(fm.getFocusedIndex());
            }
            if (sm.getSelectedIndices().size() > 1) {
                clearSelectionOutsideRange(anchor, newRow);
            }
            if (anchor > newRow) {
                sm.selectRange(anchor, newRow - 1);
            } else {
                sm.selectRange(anchor, newRow + 1);
            }
        } else {
            sm.selectPrevious();
        }
        this.onSelectPreviousRow.run();
    }

    private void alsoSelectNextRow() {
        MultipleSelectionModel<TreeItem<T>> sm;
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null || (sm = getControl().getSelectionModel()) == null) {
            return;
        }
        if (this.isShiftDown && getAnchor() != -1) {
            int newRow = fm.getFocusedIndex() + 1;
            int anchor = getAnchor();
            if (!hasAnchor()) {
                setAnchor(fm.getFocusedIndex());
            }
            if (sm.getSelectedIndices().size() > 1) {
                clearSelectionOutsideRange(anchor, newRow);
            }
            if (anchor > newRow) {
                sm.selectRange(anchor, newRow - 1);
            } else {
                sm.selectRange(anchor, newRow + 1);
            }
        } else {
            sm.selectNext();
        }
        this.onSelectNextRow.run();
    }

    private void clearSelectionOutsideRange(int start, int end) {
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        int min = Math.min(start, end);
        int max = Math.max(start, end);
        List<Integer> indices = new ArrayList<>(sm.getSelectedIndices());
        this.selectionChanging = true;
        for (int i2 = 0; i2 < indices.size(); i2++) {
            int index = indices.get(i2).intValue();
            if (index < min || index > max) {
                sm.clearSelection(index);
            }
        }
        this.selectionChanging = false;
    }

    private void selectPreviousRow() {
        int focusIndex;
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null || (focusIndex = fm.getFocusedIndex()) <= 0) {
            return;
        }
        setAnchor(focusIndex - 1);
        getControl().getSelectionModel().clearAndSelect(focusIndex - 1);
        this.onSelectPreviousRow.run();
    }

    private void selectNextRow() {
        int focusIndex;
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null || (focusIndex = fm.getFocusedIndex()) == getControl().getExpandedItemCount() - 1) {
            return;
        }
        setAnchor(focusIndex + 1);
        getControl().getSelectionModel().clearAndSelect(focusIndex + 1);
        this.onSelectNextRow.run();
    }

    private void selectFirstRow() {
        if (getControl().getExpandedItemCount() > 0) {
            getControl().getSelectionModel().clearAndSelect(0);
            if (this.onMoveToFirstCell != null) {
                this.onMoveToFirstCell.run();
            }
        }
    }

    private void selectLastRow() {
        getControl().getSelectionModel().clearAndSelect(getControl().getExpandedItemCount() - 1);
        this.onMoveToLastCell.run();
    }

    private void selectAllToFirstRow() {
        FocusModel<TreeItem<T>> fm;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        if (this.isShiftDown) {
            leadIndex = hasAnchor() ? getAnchor() : leadIndex;
        }
        sm.clearSelection();
        sm.selectRange(leadIndex, -1);
        fm.focus(0);
        if (this.isShiftDown) {
            setAnchor(leadIndex);
        }
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void selectAllToLastRow() {
        FocusModel<TreeItem<T>> fm;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        if (this.isShiftDown) {
            leadIndex = hasAnchor() ? getAnchor() : leadIndex;
        }
        sm.clearSelection();
        sm.selectRange(leadIndex, getControl().getExpandedItemCount());
        if (this.isShiftDown) {
            setAnchor(leadIndex);
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void selectAll() {
        getControl().getSelectionModel().selectAll();
    }

    private void selectAllPageUp() {
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        if (this.isShiftDown) {
            leadIndex = getAnchor() == -1 ? leadIndex : getAnchor();
            setAnchor(leadIndex);
        }
        int leadSelectedIndex = this.onScrollPageUp.call(false).intValue();
        int adjust = leadIndex < leadSelectedIndex ? 1 : -1;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        this.selectionChanging = true;
        if (sm.getSelectionMode() == SelectionMode.SINGLE) {
            sm.select(leadSelectedIndex);
        } else {
            sm.clearSelection();
            sm.selectRange(leadIndex, leadSelectedIndex + adjust);
        }
        this.selectionChanging = false;
    }

    private void selectAllPageDown() {
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        if (this.isShiftDown) {
            leadIndex = getAnchor() == -1 ? leadIndex : getAnchor();
            setAnchor(leadIndex);
        }
        int leadSelectedIndex = this.onScrollPageDown.call(false).intValue();
        int adjust = leadIndex < leadSelectedIndex ? 1 : -1;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        this.selectionChanging = true;
        if (sm.getSelectionMode() == SelectionMode.SINGLE) {
            sm.select(leadSelectedIndex);
        } else {
            sm.clearSelection();
            sm.selectRange(leadIndex, leadSelectedIndex + adjust);
        }
        this.selectionChanging = false;
    }

    private void selectAllToFocus(boolean setAnchorToFocusIndex) {
        MultipleSelectionModel<TreeItem<T>> sm;
        FocusModel<TreeItem<T>> fm;
        TreeView<T> treeView = getControl();
        if (treeView.getEditingItem() != null || (sm = treeView.getSelectionModel()) == null || (fm = treeView.getFocusModel()) == null) {
            return;
        }
        int focusIndex = fm.getFocusedIndex();
        int anchor = getAnchor();
        sm.clearSelection();
        int endPos = anchor > focusIndex ? focusIndex - 1 : focusIndex + 1;
        sm.selectRange(anchor, endPos);
        setAnchor(setAnchorToFocusIndex ? focusIndex : anchor);
    }

    private void expandRow() {
        Callback<TreeItem<T>, Integer> getIndex = p2 -> {
            return Integer.valueOf(getControl().getRow(p2));
        };
        expandRow(getControl().getSelectionModel(), getIndex);
    }

    private void expandAll() {
        expandAll(getControl().getRoot());
    }

    private void collapseRow() {
        TreeView<T> control = getControl();
        collapseRow(control.getSelectionModel(), control.getRoot(), control.isShowRoot());
    }

    static <T> void expandRow(MultipleSelectionModel<TreeItem<T>> sm, Callback<TreeItem<T>, Integer> getIndex) {
        TreeItem<T> treeItem;
        if (sm == null || (treeItem = sm.getSelectedItem()) == null || treeItem.isLeaf()) {
            return;
        }
        if (treeItem.isExpanded()) {
            List<TreeItem<T>> children = treeItem.getChildren();
            if (!children.isEmpty()) {
                sm.clearAndSelect(getIndex.call(children.get(0)).intValue());
                return;
            }
            return;
        }
        treeItem.setExpanded(true);
    }

    static <T> void expandAll(TreeItem<T> root) {
        if (root == null) {
            return;
        }
        root.setExpanded(true);
        expandChildren(root);
    }

    private static <T> void expandChildren(TreeItem<T> node) {
        List<TreeItem<T>> children;
        if (node == null || (children = node.getChildren()) == null) {
            return;
        }
        for (int i2 = 0; i2 < children.size(); i2++) {
            TreeItem<T> child = children.get(i2);
            if (child != null && !child.isLeaf()) {
                child.setExpanded(true);
                expandChildren(child);
            }
        }
    }

    static <T> void collapseRow(MultipleSelectionModel<TreeItem<T>> sm, TreeItem<T> root, boolean isShowRoot) {
        TreeItem<T> selectedItem;
        if (sm == null || (selectedItem = sm.getSelectedItem()) == null || root == null) {
            return;
        }
        if (!isShowRoot && !selectedItem.isExpanded() && root.equals(selectedItem.getParent())) {
            return;
        }
        if (root.equals(selectedItem) && (!root.isExpanded() || root.getChildren().isEmpty())) {
            return;
        }
        if (selectedItem.isLeaf() || !selectedItem.isExpanded()) {
            sm.clearSelection();
            sm.select((MultipleSelectionModel<TreeItem<T>>) selectedItem.getParent());
        } else {
            selectedItem.setExpanded(false);
        }
    }

    private void cancelEdit() {
        getControl().edit(null);
    }

    private void edit() {
        TreeItem<T> treeItem = getControl().getSelectionModel().getSelectedItem();
        if (treeItem == null) {
            return;
        }
        getControl().edit(treeItem);
    }

    private void toggleFocusOwnerSelection() {
        FocusModel<TreeItem<T>> fm;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int focusedIndex = fm.getFocusedIndex();
        if (sm.isSelected(focusedIndex)) {
            sm.clearSelection(focusedIndex);
            fm.focus(focusedIndex);
        } else {
            sm.select(focusedIndex);
        }
        setAnchor(focusedIndex);
    }

    private void discontinuousSelectPreviousRow() {
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() != SelectionMode.MULTIPLE) {
            selectPreviousRow();
            return;
        }
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int focusIndex = fm.getFocusedIndex();
        int newFocusIndex = focusIndex - 1;
        if (newFocusIndex < 0) {
            return;
        }
        int startIndex = focusIndex;
        if (this.isShiftDown) {
            startIndex = getAnchor() == -1 ? focusIndex : getAnchor();
        }
        sm.selectRange(newFocusIndex, startIndex + 1);
        fm.focus(newFocusIndex);
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    private void discontinuousSelectNextRow() {
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() != SelectionMode.MULTIPLE) {
            selectNextRow();
            return;
        }
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int focusIndex = fm.getFocusedIndex();
        int newFocusIndex = focusIndex + 1;
        if (newFocusIndex >= getControl().getExpandedItemCount()) {
            return;
        }
        int startIndex = focusIndex;
        if (this.isShiftDown) {
            startIndex = getAnchor() == -1 ? focusIndex : getAnchor();
        }
        sm.selectRange(startIndex, newFocusIndex + 1);
        fm.focus(newFocusIndex);
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    private void discontinuousSelectPageUp() {
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int anchor = getAnchor();
        int leadSelectedIndex = this.onScrollPageUp.call(false).intValue();
        sm.selectRange(anchor, leadSelectedIndex - 1);
    }

    private void discontinuousSelectPageDown() {
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        FocusModel<TreeItem<T>> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int anchor = getAnchor();
        int leadSelectedIndex = this.onScrollPageDown.call(false).intValue();
        sm.selectRange(anchor, leadSelectedIndex + 1);
    }

    private void discontinuousSelectAllToFirstRow() {
        FocusModel<TreeItem<T>> fm;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int index = fm.getFocusedIndex();
        sm.selectRange(0, index);
        fm.focus(0);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void discontinuousSelectAllToLastRow() {
        FocusModel<TreeItem<T>> fm;
        MultipleSelectionModel<TreeItem<T>> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int index = fm.getFocusedIndex() + 1;
        sm.selectRange(index, getControl().getExpandedItemCount());
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }
}
