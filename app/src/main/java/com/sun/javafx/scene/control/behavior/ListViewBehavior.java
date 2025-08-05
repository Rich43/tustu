package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.skin.Utils;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.event.EventType;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Orientation;
import javafx.scene.control.Control;
import javafx.scene.control.FocusModel;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ListViewBehavior.class */
public class ListViewBehavior<T> extends BehaviorBase<ListView<T>> {
    protected static final List<KeyBinding> LIST_VIEW_BINDINGS = new ArrayList();
    private boolean isShiftDown;
    private boolean isShortcutDown;
    private Callback<Boolean, Integer> onScrollPageUp;
    private Callback<Boolean, Integer> onScrollPageDown;
    private Runnable onFocusPreviousRow;
    private Runnable onFocusNextRow;
    private Runnable onSelectPreviousRow;
    private Runnable onSelectNextRow;
    private Runnable onMoveToFirstCell;
    private Runnable onMoveToLastCell;
    private boolean selectionChanging;
    private final ListChangeListener<Integer> selectedIndicesListener;
    private final ListChangeListener<T> itemsListListener;
    private final ChangeListener<ObservableList<T>> itemsListener;
    private final ChangeListener<MultipleSelectionModel<T>> selectionModelListener;
    private final WeakChangeListener<ObservableList<T>> weakItemsListener;
    private final WeakListChangeListener<Integer> weakSelectedIndicesListener;
    private final WeakListChangeListener<T> weakItemsListListener;
    private final WeakChangeListener<MultipleSelectionModel<T>> weakSelectionModelListener;
    private TwoLevelFocusListBehavior tlFocus;

    static {
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectAllToLastRow").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocus").shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor").shortcut().shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Activate"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Activate"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Activate"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.A, "SelectAll").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "FocusFirstRow").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "FocusLastRow").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp").shortcut());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown").shortcut());
        if (PlatformUtil.isMac()) {
            LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl().shortcut());
        } else {
            LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl());
        }
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "SelectPreviousRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_UP, "SelectPreviousRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "SelectNextRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_DOWN, "SelectNextRow").vertical());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "AlsoSelectPreviousRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_UP, "AlsoSelectPreviousRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "AlsoSelectNextRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_DOWN, "AlsoSelectNextRow").vertical().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "FocusPreviousRow").vertical().shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "FocusNextRow").vertical().shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow").vertical().shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "SelectPreviousRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_LEFT, "SelectPreviousRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "SelectNextRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_RIGHT, "SelectNextRow"));
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "AlsoSelectPreviousRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_LEFT, "AlsoSelectPreviousRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "AlsoSelectNextRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.KP_RIGHT, "AlsoSelectNextRow").shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "FocusPreviousRow").shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "FocusNextRow").shortcut());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.LEFT, "DiscontinuousSelectPreviousRow").shortcut().shift());
        LIST_VIEW_BINDINGS.add(new ListViewKeyBinding(KeyCode.RIGHT, "DiscontinuousSelectNextRow").shortcut().shift());
        LIST_VIEW_BINDINGS.add(new KeyBinding(KeyCode.BACK_SLASH, "ClearSelection").shortcut());
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected String matchActionForEvent(KeyEvent e2) {
        String action = super.matchActionForEvent(e2);
        if (action != null) {
            if (e2.getCode() == KeyCode.LEFT || e2.getCode() == KeyCode.KP_LEFT) {
                if (getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                    if (e2.isShiftDown()) {
                        action = "AlsoSelectNextRow";
                    } else if (e2.isShortcutDown()) {
                        action = "FocusNextRow";
                    } else {
                        action = getControl().getOrientation() == Orientation.HORIZONTAL ? "SelectNextRow" : "TraverseRight";
                    }
                }
            } else if ((e2.getCode() == KeyCode.RIGHT || e2.getCode() == KeyCode.KP_RIGHT) && getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT) {
                if (e2.isShiftDown()) {
                    action = "AlsoSelectPreviousRow";
                } else if (e2.isShortcutDown()) {
                    action = "FocusPreviousRow";
                } else {
                    action = getControl().getOrientation() == Orientation.HORIZONTAL ? "SelectPreviousRow" : "TraverseLeft";
                }
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
                        if (!"SelectAllToFirstRow".equals(name)) {
                            if (!"SelectAllToLastRow".equals(name)) {
                                if (!"SelectAllPageUp".equals(name)) {
                                    if (!"SelectAllPageDown".equals(name)) {
                                        if (!"AlsoSelectNextRow".equals(name)) {
                                            if (!"AlsoSelectPreviousRow".equals(name)) {
                                                if (!"ClearSelection".equals(name)) {
                                                    if (!"SelectAll".equals(name)) {
                                                        if (!"ScrollUp".equals(name)) {
                                                            if (!"ScrollDown".equals(name)) {
                                                                if (!"FocusPreviousRow".equals(name)) {
                                                                    if (!"FocusNextRow".equals(name)) {
                                                                        if (!"FocusPageUp".equals(name)) {
                                                                            if (!"FocusPageDown".equals(name)) {
                                                                                if (!"Activate".equals(name)) {
                                                                                    if (!"CancelEdit".equals(name)) {
                                                                                        if (!"FocusFirstRow".equals(name)) {
                                                                                            if (!"FocusLastRow".equals(name)) {
                                                                                                if (!"toggleFocusOwnerSelection".equals(name)) {
                                                                                                    if (!"SelectAllToFocus".equals(name)) {
                                                                                                        if (!"SelectAllToFocusAndSetAnchor".equals(name)) {
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
                                                                                activate();
                                                                                return;
                                                                            }
                                                                            focusPageDown();
                                                                            return;
                                                                        }
                                                                        focusPageUp();
                                                                        return;
                                                                    }
                                                                    focusNextRow();
                                                                    return;
                                                                }
                                                                focusPreviousRow();
                                                                return;
                                                            }
                                                            scrollPageDown();
                                                            return;
                                                        }
                                                        scrollPageUp();
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
                                    selectAllPageDown();
                                    return;
                                }
                                selectAllPageUp();
                                return;
                            }
                            selectAllToLastRow();
                            return;
                        }
                        selectAllToFirstRow();
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

    public void setOnFocusPreviousRow(Runnable r2) {
        this.onFocusPreviousRow = r2;
    }

    public void setOnFocusNextRow(Runnable r2) {
        this.onFocusNextRow = r2;
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

    public ListViewBehavior(ListView<T> control) {
        super(control, LIST_VIEW_BINDINGS);
        this.isShiftDown = false;
        this.isShortcutDown = false;
        this.selectionChanging = false;
        this.selectedIndicesListener = c2 -> {
            while (c2.next()) {
                if (c2.wasReplaced() && ListCellBehavior.hasDefaultAnchor(getControl())) {
                    ListCellBehavior.removeAnchor(getControl());
                }
                int shift = c2.wasPermutated() ? c2.getTo() - c2.getFrom() : 0;
                MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        this.itemsListListener = c3 -> {
            while (c3.next()) {
                if (c3.wasAdded() && c3.getFrom() <= getAnchor()) {
                    setAnchor(getAnchor() + c3.getAddedSize());
                } else if (c3.wasRemoved() && c3.getFrom() <= getAnchor()) {
                    setAnchor(getAnchor() - c3.getRemovedSize());
                }
            }
        };
        this.itemsListener = new ChangeListener<ObservableList<T>>() { // from class: com.sun.javafx.scene.control.behavior.ListViewBehavior.1
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends ObservableList<T>> observable, ObservableList<T> oldValue, ObservableList<T> newValue) {
                if (oldValue != null) {
                    oldValue.removeListener(ListViewBehavior.this.weakItemsListListener);
                }
                if (newValue != null) {
                    newValue.addListener(ListViewBehavior.this.weakItemsListListener);
                }
            }
        };
        this.selectionModelListener = new ChangeListener<MultipleSelectionModel<T>>() { // from class: com.sun.javafx.scene.control.behavior.ListViewBehavior.2
            @Override // javafx.beans.value.ChangeListener
            public void changed(ObservableValue<? extends MultipleSelectionModel<T>> observable, MultipleSelectionModel<T> oldValue, MultipleSelectionModel<T> newValue) {
                if (oldValue != null) {
                    oldValue.getSelectedIndices().removeListener(ListViewBehavior.this.weakSelectedIndicesListener);
                }
                if (newValue != null) {
                    newValue.getSelectedIndices().addListener(ListViewBehavior.this.weakSelectedIndicesListener);
                }
            }
        };
        this.weakItemsListener = new WeakChangeListener<>(this.itemsListener);
        this.weakSelectedIndicesListener = new WeakListChangeListener<>(this.selectedIndicesListener);
        this.weakItemsListListener = new WeakListChangeListener<>(this.itemsListListener);
        this.weakSelectionModelListener = new WeakChangeListener<>(this.selectionModelListener);
        control.itemsProperty().addListener(this.weakItemsListener);
        if (control.getItems() != null) {
            control.getItems().addListener(this.weakItemsListListener);
        }
        getControl().selectionModelProperty().addListener(this.weakSelectionModelListener);
        if (control.getSelectionModel() != null) {
            control.getSelectionModel().getSelectedIndices().addListener(this.weakSelectedIndicesListener);
        }
        if (Utils.isTwoLevelFocus()) {
            this.tlFocus = new TwoLevelFocusListBehavior(control);
        }
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void dispose() {
        ListCellBehavior.removeAnchor(getControl());
        if (this.tlFocus != null) {
            this.tlFocus.dispose();
        }
        super.dispose();
    }

    private void setAnchor(int anchor) {
        ListCellBehavior.setAnchor(getControl(), anchor < 0 ? null : Integer.valueOf(anchor), false);
    }

    private int getAnchor() {
        return ((Integer) ListCellBehavior.getAnchor(getControl(), Integer.valueOf(getControl().getFocusModel().getFocusedIndex()))).intValue();
    }

    private boolean hasAnchor() {
        return ListCellBehavior.hasNonDefaultAnchor(getControl());
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        if (!e2.isShiftDown() && !e2.isSynthesized()) {
            int index = getControl().getSelectionModel().getSelectedIndex();
            setAnchor(index);
        }
        if (!getControl().isFocused() && getControl().isFocusTraversable()) {
            getControl().requestFocus();
        }
    }

    private int getRowCount() {
        if (getControl().getItems() == null) {
            return 0;
        }
        return getControl().getItems().size();
    }

    private void clearSelection() {
        getControl().getSelectionModel().clearSelection();
    }

    private void scrollPageUp() {
        MultipleSelectionModel<T> sm;
        int newSelectedIndex = -1;
        if (this.onScrollPageUp != null) {
            newSelectedIndex = this.onScrollPageUp.call(false).intValue();
        }
        if (newSelectedIndex == -1 || (sm = getControl().getSelectionModel()) == null) {
            return;
        }
        sm.clearAndSelect(newSelectedIndex);
    }

    private void scrollPageDown() {
        MultipleSelectionModel<T> sm;
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
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(0);
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    private void focusLastRow() {
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(getRowCount() - 1);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void focusPreviousRow() {
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(newFocusIndex);
    }

    private void focusPageDown() {
        int newFocusIndex = this.onScrollPageDown.call(true).intValue();
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        fm.focus(newFocusIndex);
    }

    private void alsoSelectPreviousRow() {
        MultipleSelectionModel<T> sm;
        FocusModel<T> fm = getControl().getFocusModel();
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
        MultipleSelectionModel<T> sm;
        FocusModel<T> fm = getControl().getFocusModel();
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
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null || (focusIndex = fm.getFocusedIndex()) <= 0) {
            return;
        }
        setAnchor(focusIndex - 1);
        getControl().getSelectionModel().clearAndSelect(focusIndex - 1);
        this.onSelectPreviousRow.run();
    }

    private void selectNextRow() {
        int focusIndex;
        MultipleSelectionModel<T> sm;
        ListView<T> listView = getControl();
        FocusModel<T> fm = listView.getFocusModel();
        if (fm == null || (focusIndex = fm.getFocusedIndex()) == getRowCount() - 1 || (sm = listView.getSelectionModel()) == null) {
            return;
        }
        setAnchor(focusIndex + 1);
        sm.clearAndSelect(focusIndex + 1);
        if (this.onSelectNextRow != null) {
            this.onSelectNextRow.run();
        }
    }

    private void selectFirstRow() {
        if (getRowCount() > 0) {
            getControl().getSelectionModel().clearAndSelect(0);
            if (this.onMoveToFirstCell != null) {
                this.onMoveToFirstCell.run();
            }
        }
    }

    private void selectLastRow() {
        getControl().getSelectionModel().clearAndSelect(getRowCount() - 1);
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void selectAllPageUp() {
        FocusModel<T> fm = getControl().getFocusModel();
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
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        FocusModel<T> fm = getControl().getFocusModel();
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
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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

    private void selectAllToFirstRow() {
        FocusModel<T> fm;
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        FocusModel<T> fm;
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        if (this.isShiftDown) {
            leadIndex = hasAnchor() ? getAnchor() : leadIndex;
        }
        sm.clearSelection();
        sm.selectRange(leadIndex, getRowCount());
        if (this.isShiftDown) {
            setAnchor(leadIndex);
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    private void selectAll() {
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        sm.selectAll();
    }

    private void selectAllToFocus(boolean setAnchorToFocusIndex) {
        MultipleSelectionModel<T> sm;
        FocusModel<T> fm;
        ListView<T> listView = getControl();
        if (listView.getEditingIndex() >= 0 || (sm = listView.getSelectionModel()) == null || (fm = listView.getFocusModel()) == null) {
            return;
        }
        int focusIndex = fm.getFocusedIndex();
        int anchor = getAnchor();
        sm.clearSelection();
        int endPos = anchor > focusIndex ? focusIndex - 1 : focusIndex + 1;
        sm.selectRange(anchor, endPos);
        setAnchor(setAnchorToFocusIndex ? focusIndex : anchor);
    }

    private void cancelEdit() {
        getControl().edit(-1);
    }

    private void activate() {
        int focusedIndex = getControl().getFocusModel().getFocusedIndex();
        getControl().getSelectionModel().select(focusedIndex);
        setAnchor(focusedIndex);
        if (focusedIndex >= 0) {
            getControl().edit(focusedIndex);
        }
    }

    private void toggleFocusOwnerSelection() {
        FocusModel<T> fm;
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() != SelectionMode.MULTIPLE) {
            selectPreviousRow();
            return;
        }
        FocusModel<T> fm = getControl().getFocusModel();
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
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() != SelectionMode.MULTIPLE) {
            selectNextRow();
            return;
        }
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int focusIndex = fm.getFocusedIndex();
        int newFocusIndex = focusIndex + 1;
        if (newFocusIndex >= getRowCount()) {
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
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int anchor = getAnchor();
        int leadSelectedIndex = this.onScrollPageUp.call(false).intValue();
        sm.selectRange(anchor, leadSelectedIndex - 1);
    }

    private void discontinuousSelectPageDown() {
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null) {
            return;
        }
        FocusModel<T> fm = getControl().getFocusModel();
        if (fm == null) {
            return;
        }
        int anchor = getAnchor();
        int leadSelectedIndex = this.onScrollPageDown.call(false).intValue();
        sm.selectRange(anchor, leadSelectedIndex + 1);
    }

    private void discontinuousSelectAllToFirstRow() {
        FocusModel<T> fm;
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
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
        FocusModel<T> fm;
        MultipleSelectionModel<T> sm = getControl().getSelectionModel();
        if (sm == null || (fm = getControl().getFocusModel()) == null) {
            return;
        }
        int index = fm.getFocusedIndex() + 1;
        sm.selectRange(index, getRowCount());
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/ListViewBehavior$ListViewKeyBinding.class */
    private static class ListViewKeyBinding extends OrientedKeyBinding {
        public ListViewKeyBinding(KeyCode code, String action) {
            super(code, action);
        }

        public ListViewKeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
            super(code, type, action);
        }

        @Override // com.sun.javafx.scene.control.behavior.OrientedKeyBinding
        public boolean getVertical(Control control) {
            return ((ListView) control).getOrientation() == Orientation.VERTICAL;
        }
    }
}
