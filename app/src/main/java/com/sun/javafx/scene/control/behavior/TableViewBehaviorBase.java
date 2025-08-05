package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.PlatformUtil;
import com.sun.javafx.scene.control.SizeLimitedList;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.WeakListChangeListener;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Control;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumnBase;
import javafx.scene.control.TableFocusModel;
import javafx.scene.control.TablePositionBase;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/TableViewBehaviorBase.class */
public abstract class TableViewBehaviorBase<C extends Control, T, TC extends TableColumnBase<T, ?>> extends BehaviorBase<C> {
    protected static final List<KeyBinding> TABLE_VIEW_BINDINGS = new ArrayList();
    protected boolean isShortcutDown;
    protected boolean isShiftDown;
    private boolean selectionPathDeviated;
    protected boolean selectionChanging;
    private final SizeLimitedList<TablePositionBase> selectionHistory;
    protected final ListChangeListener<TablePositionBase> selectedCellsListener;
    protected final WeakListChangeListener<TablePositionBase> weakSelectedCellsListener;
    private Callback<Boolean, Integer> onScrollPageUp;
    private Callback<Boolean, Integer> onScrollPageDown;
    private Runnable onFocusPreviousRow;
    private Runnable onFocusNextRow;
    private Runnable onSelectPreviousRow;
    private Runnable onSelectNextRow;
    private Runnable onMoveToFirstCell;
    private Runnable onMoveToLastCell;
    private Runnable onSelectRightCell;
    private Runnable onSelectLeftCell;

    protected abstract int getItemCount();

    protected abstract TableFocusModel getFocusModel();

    protected abstract TableSelectionModel<T> getSelectionModel();

    protected abstract ObservableList<? extends TablePositionBase> getSelectedCells();

    protected abstract TablePositionBase getFocusedCell();

    protected abstract int getVisibleLeafIndex(TableColumnBase tableColumnBase);

    protected abstract TableColumnBase getVisibleLeafColumn(int i2);

    protected abstract void editCell(int i2, TableColumnBase tableColumnBase);

    protected abstract ObservableList<? extends TableColumnBase> getVisibleLeafColumns();

    protected abstract TablePositionBase<TC> getTablePosition(int i2, TableColumnBase<T, ?> tableColumnBase);

    static {
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraverseNext"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.TAB, "TraversePrevious").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectFirstRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectLastRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "ScrollUp"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "ScrollDown"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "SelectLeftCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "SelectLeftCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "SelectRightCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "SelectRightCell"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "SelectPreviousRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "SelectPreviousRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "TraverseLeft"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "TraverseLeft"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "SelectNextRow"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "TraverseUp"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "TraverseUp"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "TraverseDown"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "TraverseDown"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "SelectAllToFirstRow").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "SelectAllToLastRow").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "SelectAllPageUp").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "SelectAllPageDown").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "AlsoSelectPrevious").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_UP, "AlsoSelectPrevious").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "AlsoSelectNext").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_DOWN, "AlsoSelectNext").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocus").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "SelectAllToFocusAndSetAnchor").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "AlsoSelectLeftCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "AlsoSelectLeftCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "AlsoSelectRightCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "AlsoSelectRightCell").shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "FocusPreviousRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "FocusNextRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "FocusRightCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_RIGHT, "FocusRightCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "FocusLeftCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.KP_LEFT, "FocusLeftCell").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.A, "SelectAll").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "FocusFirstRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "FocusLastRow").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "FocusPageUp").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "FocusPageDown").shortcut());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.UP, "DiscontinuousSelectPreviousRow").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.DOWN, "DiscontinuousSelectNextRow").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.LEFT, "DiscontinuousSelectPreviousColumn").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.RIGHT, "DiscontinuousSelectNextColumn").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_UP, "DiscontinuousSelectPageUp").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.PAGE_DOWN, "DiscontinuousSelectPageDown").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.HOME, "DiscontinuousSelectAllToFirstRow").shortcut().shift());
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.END, "DiscontinuousSelectAllToLastRow").shortcut().shift());
        if (PlatformUtil.isMac()) {
            TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl().shortcut());
        } else {
            TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "toggleFocusOwnerSelection").ctrl());
        }
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ENTER, "Activate"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.SPACE, "Activate"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.F2, "Activate"));
        TABLE_VIEW_BINDINGS.add(new KeyBinding(KeyCode.ESCAPE, "CancelEdit"));
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    protected void callAction(String name) {
        boolean rtl = getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
        if (!"SelectPreviousRow".equals(name)) {
            if (!"SelectNextRow".equals(name)) {
                if ("SelectLeftCell".equals(name)) {
                    if (rtl) {
                        selectRightCell();
                        return;
                    } else {
                        selectLeftCell();
                        return;
                    }
                }
                if ("SelectRightCell".equals(name)) {
                    if (rtl) {
                        selectLeftCell();
                        return;
                    } else {
                        selectRightCell();
                        return;
                    }
                }
                if (!"SelectFirstRow".equals(name)) {
                    if (!"SelectLastRow".equals(name)) {
                        if (!"SelectAll".equals(name)) {
                            if (!"SelectAllPageUp".equals(name)) {
                                if (!"SelectAllPageDown".equals(name)) {
                                    if (!"SelectAllToFirstRow".equals(name)) {
                                        if (!"SelectAllToLastRow".equals(name)) {
                                            if (!"AlsoSelectNext".equals(name)) {
                                                if (!"AlsoSelectPrevious".equals(name)) {
                                                    if ("AlsoSelectLeftCell".equals(name)) {
                                                        if (rtl) {
                                                            alsoSelectRightCell();
                                                            return;
                                                        } else {
                                                            alsoSelectLeftCell();
                                                            return;
                                                        }
                                                    }
                                                    if ("AlsoSelectRightCell".equals(name)) {
                                                        if (rtl) {
                                                            alsoSelectLeftCell();
                                                            return;
                                                        } else {
                                                            alsoSelectRightCell();
                                                            return;
                                                        }
                                                    }
                                                    if (!"ClearSelection".equals(name)) {
                                                        if (!"ScrollUp".equals(name)) {
                                                            if (!"ScrollDown".equals(name)) {
                                                                if (!"FocusPreviousRow".equals(name)) {
                                                                    if (!"FocusNextRow".equals(name)) {
                                                                        if ("FocusLeftCell".equals(name)) {
                                                                            if (rtl) {
                                                                                focusRightCell();
                                                                                return;
                                                                            } else {
                                                                                focusLeftCell();
                                                                                return;
                                                                            }
                                                                        }
                                                                        if ("FocusRightCell".equals(name)) {
                                                                            if (rtl) {
                                                                                focusLeftCell();
                                                                                return;
                                                                            } else {
                                                                                focusRightCell();
                                                                                return;
                                                                            }
                                                                        }
                                                                        if (!"Activate".equals(name)) {
                                                                            if (!"CancelEdit".equals(name)) {
                                                                                if (!"FocusFirstRow".equals(name)) {
                                                                                    if (!"FocusLastRow".equals(name)) {
                                                                                        if (!"toggleFocusOwnerSelection".equals(name)) {
                                                                                            if (!"SelectAllToFocus".equals(name)) {
                                                                                                if (!"SelectAllToFocusAndSetAnchor".equals(name)) {
                                                                                                    if (!"FocusPageUp".equals(name)) {
                                                                                                        if (!"FocusPageDown".equals(name)) {
                                                                                                            if (!"DiscontinuousSelectNextRow".equals(name)) {
                                                                                                                if (!"DiscontinuousSelectPreviousRow".equals(name)) {
                                                                                                                    if ("DiscontinuousSelectNextColumn".equals(name)) {
                                                                                                                        if (rtl) {
                                                                                                                            discontinuousSelectPreviousColumn();
                                                                                                                            return;
                                                                                                                        } else {
                                                                                                                            discontinuousSelectNextColumn();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                    }
                                                                                                                    if ("DiscontinuousSelectPreviousColumn".equals(name)) {
                                                                                                                        if (rtl) {
                                                                                                                            discontinuousSelectNextColumn();
                                                                                                                            return;
                                                                                                                        } else {
                                                                                                                            discontinuousSelectPreviousColumn();
                                                                                                                            return;
                                                                                                                        }
                                                                                                                    }
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
                                                                        activate();
                                                                        return;
                                                                    }
                                                                    focusNextRow();
                                                                    return;
                                                                }
                                                                focusPreviousRow();
                                                                return;
                                                            }
                                                            scrollDown();
                                                            return;
                                                        }
                                                        scrollUp();
                                                        return;
                                                    }
                                                    clearSelection();
                                                    return;
                                                }
                                                alsoSelectPrevious();
                                                return;
                                            }
                                            alsoSelectNext();
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
                        selectAll();
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

    public TableViewBehaviorBase(C control) {
        this(control, null);
    }

    public TableViewBehaviorBase(C control, List<KeyBinding> bindings) {
        super(control, bindings == null ? TABLE_VIEW_BINDINGS : bindings);
        this.isShortcutDown = false;
        this.isShiftDown = false;
        this.selectionPathDeviated = false;
        this.selectionChanging = false;
        this.selectionHistory = new SizeLimitedList<>(10);
        this.selectedCellsListener = c2 -> {
            while (c2.next()) {
                if (c2.wasReplaced() && TreeTableCellBehavior.hasDefaultAnchor(getControl())) {
                    TreeTableCellBehavior.removeAnchor(getControl());
                }
                if (c2.wasAdded()) {
                    TableSelectionModel sm = getSelectionModel();
                    if (sm == null) {
                        return;
                    }
                    TablePositionBase anchor = getAnchor();
                    boolean cellSelectionEnabled = sm.isCellSelectionEnabled();
                    int addedSize = c2.getAddedSize();
                    List<TablePositionBase> addedSubList = c2.getAddedSubList();
                    for (TablePositionBase tpb : addedSubList) {
                        if (!this.selectionHistory.contains(tpb)) {
                            this.selectionHistory.add(tpb);
                        }
                    }
                    if (addedSize > 0 && !hasAnchor()) {
                        setAnchor(addedSubList.get(addedSize - 1));
                    }
                    if (anchor != null && cellSelectionEnabled && !this.selectionPathDeviated) {
                        int i2 = 0;
                        while (true) {
                            if (i2 < addedSize) {
                                TablePositionBase tp = addedSubList.get(i2);
                                if (anchor.getRow() == -1 || tp.getRow() == anchor.getRow() || tp.getColumn() == anchor.getColumn()) {
                                    i2++;
                                } else {
                                    setSelectionPathDeviated(true);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        };
        this.weakSelectedCellsListener = new WeakListChangeListener<>(this.selectedCellsListener);
    }

    protected void setAnchor(TablePositionBase tp) {
        TableCellBehaviorBase.setAnchor(getControl(), tp, false);
        setSelectionPathDeviated(false);
    }

    protected TablePositionBase getAnchor() {
        return (TablePositionBase) TableCellBehaviorBase.getAnchor(getControl(), getFocusedCell());
    }

    protected boolean hasAnchor() {
        return TableCellBehaviorBase.hasNonDefaultAnchor(getControl());
    }

    protected void setAnchor(int row, TableColumnBase col) {
        setAnchor((row == -1 && col == null) ? null : getTablePosition(row, col));
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

    public void setOnSelectRightCell(Runnable r2) {
        this.onSelectRightCell = r2;
    }

    public void setOnSelectLeftCell(Runnable r2) {
        this.onSelectLeftCell = r2;
    }

    @Override // com.sun.javafx.scene.control.behavior.BehaviorBase
    public void mousePressed(MouseEvent e2) {
        super.mousePressed(e2);
        if (!getControl().isFocused() && getControl().isFocusTraversable()) {
            getControl().requestFocus();
        }
    }

    protected boolean isRTL() {
        return getControl().getEffectiveNodeOrientation() == NodeOrientation.RIGHT_TO_LEFT;
    }

    private void setSelectionPathDeviated(boolean selectionPathDeviated) {
        this.selectionPathDeviated = selectionPathDeviated;
    }

    protected void scrollUp() {
        TableSelectionModel<T> sm = getSelectionModel();
        if (sm == null || getSelectedCells().isEmpty()) {
            return;
        }
        TablePositionBase<TC> selectedCell = getSelectedCells().get(0);
        int newSelectedIndex = -1;
        if (this.onScrollPageUp != null) {
            newSelectedIndex = this.onScrollPageUp.call(false).intValue();
        }
        if (newSelectedIndex == -1) {
            return;
        }
        sm.clearAndSelect(newSelectedIndex, selectedCell.getTableColumn());
    }

    protected void scrollDown() {
        TableSelectionModel<T> sm = getSelectionModel();
        if (sm == null || getSelectedCells().isEmpty()) {
            return;
        }
        TablePositionBase<TC> selectedCell = getSelectedCells().get(0);
        int newSelectedIndex = -1;
        if (this.onScrollPageDown != null) {
            newSelectedIndex = this.onScrollPageDown.call(false).intValue();
        }
        if (newSelectedIndex == -1) {
            return;
        }
        sm.clearAndSelect(newSelectedIndex, selectedCell.getTableColumn());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [javafx.scene.control.TableColumnBase] */
    protected void focusFirstRow() {
        TableFocusModel focusModel = getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(0, getFocusedCell() == null ? null : getFocusedCell().getTableColumn());
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v7, types: [javafx.scene.control.TableColumnBase] */
    protected void focusLastRow() {
        TableFocusModel focusModel = getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(getItemCount() - 1, getFocusedCell() == null ? null : getFocusedCell().getTableColumn());
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    protected void focusPreviousRow() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        if (sm.isCellSelectionEnabled()) {
            fm.focusAboveCell();
        } else {
            fm.focusPrevious();
        }
        if (!this.isShortcutDown || getAnchor() == null) {
            setAnchor(fm.getFocusedIndex(), null);
        }
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    protected void focusNextRow() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        if (sm.isCellSelectionEnabled()) {
            fm.focusBelowCell();
        } else {
            fm.focusNext();
        }
        if (!this.isShortcutDown || getAnchor() == null) {
            setAnchor(fm.getFocusedIndex(), null);
        }
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    protected void focusLeftCell() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        fm.focusLeftCell();
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    protected void focusRightCell() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        fm.focusRightCell();
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [javafx.scene.control.TableColumnBase] */
    protected void focusPageUp() {
        int iIntValue = this.onScrollPageUp.call(true).intValue();
        TableFocusModel focusModel = getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(iIntValue, getFocusedCell() == null ? null : getFocusedCell().getTableColumn());
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [javafx.scene.control.TableColumnBase] */
    protected void focusPageDown() {
        int iIntValue = this.onScrollPageDown.call(true).intValue();
        TableFocusModel focusModel = getFocusModel();
        if (focusModel == null) {
            return;
        }
        focusModel.focus(iIntValue, getFocusedCell() == null ? null : getFocusedCell().getTableColumn());
    }

    protected void clearSelection() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        sm.clearSelection();
    }

    protected void clearSelectionOutsideRange(int start, int end, TableColumnBase<T, ?> column) {
        TableSelectionModel<T> sm = getSelectionModel();
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
                sm.clearSelection(index, column);
            }
        }
        this.selectionChanging = false;
    }

    protected void alsoSelectPrevious() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() == SelectionMode.SINGLE) {
            selectPreviousRow();
            return;
        }
        TableFocusModel fm = getFocusModel();
        if (fm == null) {
            return;
        }
        if (sm.isCellSelectionEnabled()) {
            updateCellVerticalSelection(-1, () -> {
                getSelectionModel().selectAboveCell();
            });
        } else if (this.isShiftDown && hasAnchor()) {
            updateRowSelection(-1);
        } else {
            sm.selectPrevious();
        }
        this.onSelectPreviousRow.run();
    }

    protected void alsoSelectNext() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() == SelectionMode.SINGLE) {
            selectNextRow();
            return;
        }
        TableFocusModel fm = getFocusModel();
        if (fm == null) {
            return;
        }
        if (sm.isCellSelectionEnabled()) {
            updateCellVerticalSelection(1, () -> {
                getSelectionModel().selectBelowCell();
            });
        } else if (this.isShiftDown && hasAnchor()) {
            updateRowSelection(1);
        } else {
            sm.selectNext();
        }
        this.onSelectNextRow.run();
    }

    protected void alsoSelectLeftCell() {
        updateCellHorizontalSelection(-1, () -> {
            getSelectionModel().selectLeftCell();
        });
        this.onSelectLeftCell.run();
    }

    protected void alsoSelectRightCell() {
        updateCellHorizontalSelection(1, () -> {
            getSelectionModel().selectRightCell();
        });
        this.onSelectRightCell.run();
    }

    protected void updateRowSelection(int delta) {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || sm.getSelectionMode() == SelectionMode.SINGLE || (fm = getFocusModel()) == null) {
            return;
        }
        int newRow = fm.getFocusedIndex() + delta;
        TablePositionBase anchor = getAnchor();
        if (!hasAnchor()) {
            setAnchor(getFocusedCell());
        }
        if (sm.getSelectedIndices().size() > 1) {
            clearSelectionOutsideRange(anchor.getRow(), newRow, null);
        }
        if (anchor.getRow() > newRow) {
            sm.selectRange(anchor.getRow(), newRow - 1);
        } else {
            sm.selectRange(anchor.getRow(), newRow + 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updateCellVerticalSelection(int delta, Runnable defaultAction) {
        TableFocusModel focusModel;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || sm.getSelectionMode() == SelectionMode.SINGLE || (focusModel = getFocusModel()) == 0) {
            return;
        }
        TablePositionBase focusedCell = getFocusedCell();
        int focusedCellRow = focusedCell.getRow();
        if (this.isShiftDown && sm.isSelected(focusedCellRow + delta, focusedCell.getTableColumn())) {
            int newFocusOwner = focusedCellRow + delta;
            boolean backtracking = false;
            if (this.selectionHistory.size() >= 2) {
                TablePositionBase<TC> secondToLastSelectedCell = this.selectionHistory.get(1);
                backtracking = secondToLastSelectedCell.getRow() == newFocusOwner && secondToLastSelectedCell.getColumn() == focusedCell.getColumn();
            }
            int i2 = (!this.selectionPathDeviated || backtracking) ? focusedCellRow : newFocusOwner;
            int cellRowToClear = i2;
            sm.clearSelection(cellRowToClear, focusedCell.getTableColumn());
            focusModel.focus(newFocusOwner, focusedCell.getTableColumn());
            return;
        }
        if (this.isShiftDown && getAnchor() != null && !this.selectionPathDeviated) {
            int newRow = Math.max(Math.min(getItemCount() - 1, focusModel.getFocusedIndex() + delta), 0);
            int start = Math.min(getAnchor().getRow(), newRow);
            int end = Math.max(getAnchor().getRow(), newRow);
            if (sm.getSelectedIndices().size() > 1) {
                clearSelectionOutsideRange(start, end, focusedCell.getTableColumn());
            }
            for (int _row = start; _row <= end; _row++) {
                if (!sm.isSelected(_row, focusedCell.getTableColumn())) {
                    sm.select(_row, focusedCell.getTableColumn());
                }
            }
            focusModel.focus(newRow, focusedCell.getTableColumn());
            return;
        }
        int focusIndex = focusModel.getFocusedIndex();
        if (!sm.isSelected(focusIndex, focusedCell.getTableColumn())) {
            sm.select(focusIndex, focusedCell.getTableColumn());
        }
        defaultAction.run();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void updateCellHorizontalSelection(int delta, Runnable defaultAction) {
        TableFocusModel focusModel;
        TablePositionBase focusedCell;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || sm.getSelectionMode() == SelectionMode.SINGLE || (focusModel = getFocusModel()) == 0 || (focusedCell = getFocusedCell()) == null || focusedCell.getTableColumn() == null) {
            return;
        }
        boolean atEnd = false;
        TableColumnBase adjacentColumn = getColumn(focusedCell.getTableColumn(), delta);
        if (adjacentColumn == null) {
            adjacentColumn = focusedCell.getTableColumn();
            atEnd = true;
        }
        int focusedCellRow = focusedCell.getRow();
        if (this.isShiftDown && sm.isSelected(focusedCellRow, adjacentColumn)) {
            if (atEnd) {
                return;
            }
            boolean backtracking = false;
            ObservableList<? extends TablePositionBase> selectedCells = getSelectedCells();
            if (selectedCells.size() >= 2) {
                TablePositionBase<TC> secondToLastSelectedCell = selectedCells.get(selectedCells.size() - 2);
                backtracking = secondToLastSelectedCell.getRow() == focusedCellRow && secondToLastSelectedCell.getTableColumn().equals(adjacentColumn);
            }
            TableColumnBase tableColumn = (!this.selectionPathDeviated || backtracking) ? focusedCell.getTableColumn() : adjacentColumn;
            TableColumnBase cellColumnToClear = tableColumn;
            sm.clearSelection(focusedCellRow, cellColumnToClear);
            focusModel.focus(focusedCellRow, adjacentColumn);
            return;
        }
        if (this.isShiftDown && getAnchor() != null && !this.selectionPathDeviated) {
            int anchorColumn = getAnchor().getColumn();
            int newColumn = Math.max(Math.min(getVisibleLeafColumns().size() - 1, getVisibleLeafIndex(focusedCell.getTableColumn()) + delta), 0);
            int start = Math.min(anchorColumn, newColumn);
            int end = Math.max(anchorColumn, newColumn);
            for (int _col = start; _col <= end; _col++) {
                sm.select(focusedCell.getRow(), getColumn(_col));
            }
            focusModel.focus(focusedCell.getRow(), getColumn(newColumn));
            return;
        }
        defaultAction.run();
    }

    protected TableColumnBase getColumn(int index) {
        return getVisibleLeafColumn(index);
    }

    protected TableColumnBase getColumn(TableColumnBase tc, int delta) {
        return getVisibleLeafColumn(getVisibleLeafIndex(tc) + delta);
    }

    protected void selectFirstRow() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        ObservableList<? extends TablePositionBase> selection = getSelectedCells();
        sm.clearAndSelect(0, selection.size() == 0 ? null : selection.get(0).getTableColumn());
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void selectLastRow() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        ObservableList<? extends TablePositionBase> selection = getSelectedCells();
        sm.clearAndSelect(getItemCount() - 1, selection.size() == 0 ? null : selection.get(0).getTableColumn());
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    protected void selectPreviousRow() {
        selectCell(-1, 0);
        if (this.onSelectPreviousRow != null) {
            this.onSelectPreviousRow.run();
        }
    }

    protected void selectNextRow() {
        selectCell(1, 0);
        if (this.onSelectNextRow != null) {
            this.onSelectNextRow.run();
        }
    }

    protected void selectLeftCell() {
        selectCell(0, -1);
        if (this.onSelectLeftCell != null) {
            this.onSelectLeftCell.run();
        }
    }

    protected void selectRightCell() {
        selectCell(0, 1);
        if (this.onSelectRightCell != null) {
            this.onSelectRightCell.run();
        }
    }

    protected void selectCell(int rowDiff, int columnDiff) {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        TableFocusModel fm = getFocusModel();
        if (fm == null) {
            return;
        }
        TablePositionBase<TC> focusedCell = getFocusedCell();
        int currentRow = focusedCell.getRow();
        int currentColumn = getVisibleLeafIndex(focusedCell.getTableColumn());
        if (rowDiff >= 0 || currentRow > 0) {
            if (rowDiff <= 0 || currentRow < getItemCount() - 1) {
                if (columnDiff >= 0 || currentColumn > 0) {
                    if (columnDiff <= 0 || currentColumn < getVisibleLeafColumns().size() - 1) {
                        if (columnDiff <= 0 || currentColumn != -1) {
                            TableColumnBase tc = getColumn(focusedCell.getTableColumn(), columnDiff);
                            int row = focusedCell.getRow() + rowDiff;
                            sm.clearAndSelect(row, tc);
                            setAnchor(row, tc);
                        }
                    }
                }
            }
        }
    }

    protected void cancelEdit() {
        editCell(-1, null);
    }

    protected void activate() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        TableFocusModel fm = getFocusModel();
        if (fm == null) {
            return;
        }
        TablePositionBase<TC> cell = getFocusedCell();
        sm.select(cell.getRow(), cell.getTableColumn());
        setAnchor(cell);
        if (cell.getRow() >= 0) {
            editCell(cell.getRow(), cell.getTableColumn());
        }
    }

    protected void selectAllToFocus(boolean setAnchorToFocusIndex) {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        TableFocusModel fm = getFocusModel();
        if (fm == null) {
            return;
        }
        TablePositionBase<TC> focusedCell = getFocusedCell();
        int focusRow = focusedCell.getRow();
        TablePositionBase<TC> anchor = getAnchor();
        int anchorRow = anchor.getRow();
        sm.clearSelection();
        if (!sm.isCellSelectionEnabled()) {
            int endPos = anchorRow > focusRow ? focusRow - 1 : focusRow + 1;
            sm.selectRange(anchorRow, endPos);
        } else {
            sm.selectRange(anchor.getRow(), anchor.getTableColumn(), focusedCell.getRow(), focusedCell.getTableColumn());
        }
        setAnchor(setAnchorToFocusIndex ? focusedCell : anchor);
    }

    protected void selectAll() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        sm.selectAll();
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void selectAllToFirstRow() {
        TableFocusModel focusModel;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (focusModel = getFocusModel()) == 0) {
            return;
        }
        boolean isSingleSelection = sm.getSelectionMode() == SelectionMode.SINGLE;
        TablePositionBase focusedCell = getFocusedCell();
        TableColumnBase<T, ?> tableColumn = getFocusedCell().getTableColumn();
        int leadIndex = focusedCell.getRow();
        if (this.isShiftDown) {
            leadIndex = getAnchor() == null ? leadIndex : getAnchor().getRow();
        }
        sm.clearSelection();
        if (!sm.isCellSelectionEnabled()) {
            if (isSingleSelection) {
                sm.select(0);
            } else {
                sm.selectRange(leadIndex, -1);
            }
            focusModel.focus(0);
        } else {
            if (isSingleSelection) {
                sm.select(0, tableColumn);
            } else {
                sm.selectRange(leadIndex, tableColumn, -1, tableColumn);
            }
            focusModel.focus(0, tableColumn);
        }
        if (this.isShiftDown) {
            setAnchor(leadIndex, tableColumn);
        }
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void selectAllToLastRow() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        TableFocusModel fm = getFocusModel();
        if (fm == null) {
            return;
        }
        int itemCount = getItemCount();
        TablePositionBase focusedCell = getFocusedCell();
        TableColumnBase<T, ?> tableColumn = getFocusedCell().getTableColumn();
        int leadIndex = focusedCell.getRow();
        if (this.isShiftDown) {
            leadIndex = getAnchor() == null ? leadIndex : getAnchor().getRow();
        }
        sm.clearSelection();
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(leadIndex, itemCount);
        } else {
            sm.selectRange(leadIndex, tableColumn, itemCount - 1, tableColumn);
        }
        if (this.isShiftDown) {
            setAnchor(leadIndex, tableColumn);
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }

    protected void selectAllPageUp() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        TableColumnBase col = sm.isCellSelectionEnabled() ? getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            leadIndex = getAnchor() == null ? leadIndex : getAnchor().getRow();
            setAnchor(leadIndex, col);
        }
        int leadSelectedIndex = this.onScrollPageUp.call(false).intValue();
        this.selectionChanging = true;
        if (sm.getSelectionMode() == null || sm.getSelectionMode() == SelectionMode.SINGLE) {
            if (sm.isCellSelectionEnabled()) {
                sm.select(leadSelectedIndex, col);
            } else {
                sm.select(leadSelectedIndex);
            }
        } else {
            sm.clearSelection();
            if (sm.isCellSelectionEnabled()) {
                sm.selectRange(leadIndex, col, leadSelectedIndex, col);
            } else {
                int adjust = leadIndex < leadSelectedIndex ? 1 : -1;
                sm.selectRange(leadIndex, leadSelectedIndex + adjust);
            }
        }
        this.selectionChanging = false;
    }

    protected void selectAllPageDown() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        int leadIndex = fm.getFocusedIndex();
        TableColumnBase col = sm.isCellSelectionEnabled() ? getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            leadIndex = getAnchor() == null ? leadIndex : getAnchor().getRow();
            setAnchor(leadIndex, col);
        }
        int leadSelectedIndex = this.onScrollPageDown.call(false).intValue();
        this.selectionChanging = true;
        if (sm.getSelectionMode() == null || sm.getSelectionMode() == SelectionMode.SINGLE) {
            if (sm.isCellSelectionEnabled()) {
                sm.select(leadSelectedIndex, col);
            } else {
                sm.select(leadSelectedIndex);
            }
        } else {
            sm.clearSelection();
            if (sm.isCellSelectionEnabled()) {
                sm.selectRange(leadIndex, col, leadSelectedIndex, col);
            } else {
                int adjust = leadIndex < leadSelectedIndex ? 1 : -1;
                sm.selectRange(leadIndex, leadSelectedIndex + adjust);
            }
        }
        this.selectionChanging = false;
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void toggleFocusOwnerSelection() {
        TableFocusModel focusModel;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (focusModel = getFocusModel()) == 0) {
            return;
        }
        TablePositionBase focusedCell = getFocusedCell();
        if (sm.isSelected(focusedCell.getRow(), focusedCell.getTableColumn())) {
            sm.clearSelection(focusedCell.getRow(), focusedCell.getTableColumn());
            focusModel.focus(focusedCell.getRow(), focusedCell.getTableColumn());
        } else {
            sm.select(focusedCell.getRow(), focusedCell.getTableColumn());
        }
        setAnchor(focusedCell.getRow(), focusedCell.getTableColumn());
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void discontinuousSelectPreviousRow() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() != SelectionMode.MULTIPLE) {
            selectPreviousRow();
            return;
        }
        TableFocusModel focusModel = getFocusModel();
        if (focusModel == 0) {
            return;
        }
        int focusIndex = focusModel.getFocusedIndex();
        int newFocusIndex = focusIndex - 1;
        if (newFocusIndex < 0) {
            return;
        }
        int startIndex = focusIndex;
        TableColumnBase col = sm.isCellSelectionEnabled() ? getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            startIndex = getAnchor() == null ? focusIndex : getAnchor().getRow();
        }
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(newFocusIndex, startIndex + 1);
            focusModel.focus(newFocusIndex);
        } else {
            for (int i2 = newFocusIndex; i2 < startIndex + 1; i2++) {
                sm.select(i2, col);
            }
            focusModel.focus(newFocusIndex, col);
        }
        if (this.onFocusPreviousRow != null) {
            this.onFocusPreviousRow.run();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void discontinuousSelectNextRow() {
        TableSelectionModel sm = getSelectionModel();
        if (sm == null) {
            return;
        }
        if (sm.getSelectionMode() != SelectionMode.MULTIPLE) {
            selectNextRow();
            return;
        }
        TableFocusModel focusModel = getFocusModel();
        if (focusModel == 0) {
            return;
        }
        int focusIndex = focusModel.getFocusedIndex();
        int newFocusIndex = focusIndex + 1;
        if (newFocusIndex >= getItemCount()) {
            return;
        }
        int startIndex = focusIndex;
        TableColumnBase col = sm.isCellSelectionEnabled() ? getFocusedCell().getTableColumn() : null;
        if (this.isShiftDown) {
            startIndex = getAnchor() == null ? focusIndex : getAnchor().getRow();
        }
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(startIndex, newFocusIndex + 1);
            focusModel.focus(newFocusIndex);
        } else {
            for (int i2 = startIndex; i2 < newFocusIndex + 1; i2++) {
                sm.select(i2, col);
            }
            focusModel.focus(newFocusIndex, col);
        }
        if (this.onFocusNextRow != null) {
            this.onFocusNextRow.run();
        }
    }

    protected void discontinuousSelectPreviousColumn() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || !sm.isCellSelectionEnabled() || (fm = getFocusModel()) == null) {
            return;
        }
        TableColumnBase tc = getColumn(getFocusedCell().getTableColumn(), -1);
        sm.select(fm.getFocusedIndex(), tc);
    }

    protected void discontinuousSelectNextColumn() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || !sm.isCellSelectionEnabled() || (fm = getFocusModel()) == null) {
            return;
        }
        TableColumnBase tc = getColumn(getFocusedCell().getTableColumn(), 1);
        sm.select(fm.getFocusedIndex(), tc);
    }

    protected void discontinuousSelectPageUp() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        int anchor = hasAnchor() ? getAnchor().getRow() : fm.getFocusedIndex();
        int leadSelectedIndex = this.onScrollPageUp.call(false).intValue();
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(anchor, leadSelectedIndex - 1);
        }
    }

    protected void discontinuousSelectPageDown() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        int anchor = hasAnchor() ? getAnchor().getRow() : fm.getFocusedIndex();
        int leadSelectedIndex = this.onScrollPageDown.call(false).intValue();
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(anchor, leadSelectedIndex + 1);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void discontinuousSelectAllToFirstRow() {
        TableFocusModel focusModel;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (focusModel = getFocusModel()) == 0) {
            return;
        }
        int index = focusModel.getFocusedIndex();
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(0, index);
            focusModel.focus(0);
        } else {
            for (int i2 = 0; i2 < index; i2++) {
                sm.select(i2, getFocusedCell().getTableColumn());
            }
            focusModel.focus(0, getFocusedCell().getTableColumn());
        }
        if (this.onMoveToFirstCell != null) {
            this.onMoveToFirstCell.run();
        }
    }

    protected void discontinuousSelectAllToLastRow() {
        TableFocusModel fm;
        TableSelectionModel sm = getSelectionModel();
        if (sm == null || (fm = getFocusModel()) == null) {
            return;
        }
        int index = fm.getFocusedIndex() + 1;
        if (!sm.isCellSelectionEnabled()) {
            sm.selectRange(index, getItemCount());
        } else {
            for (int i2 = index; i2 < getItemCount(); i2++) {
                sm.select(i2, getFocusedCell().getTableColumn());
            }
        }
        if (this.onMoveToLastCell != null) {
            this.onMoveToLastCell.run();
        }
    }
}
