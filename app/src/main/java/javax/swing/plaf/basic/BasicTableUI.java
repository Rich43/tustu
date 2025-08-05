package javax.swing.plaf.basic;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.CellRendererPane;
import javax.swing.DefaultListSelectionModel;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.TransferHandler;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.DragRecognitionSupport;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI.class */
public class BasicTableUI extends TableUI {
    protected JTable table;
    protected CellRendererPane rendererPane;
    protected KeyListener keyListener;
    protected FocusListener focusListener;
    protected MouseInputListener mouseInputListener;
    private Handler handler;
    private boolean isFileList = false;
    private static final StringBuilder BASELINE_COMPONENT_KEY = new StringBuilder("Table.baselineComponent");
    private static final TransferHandler defaultTransferHandler = new TableTransferHandler();

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI$Actions.class */
    private static class Actions extends UIAction {
        private static final String CANCEL_EDITING = "cancel";
        private static final String SELECT_ALL = "selectAll";
        private static final String CLEAR_SELECTION = "clearSelection";
        private static final String START_EDITING = "startEditing";
        private static final String NEXT_ROW = "selectNextRow";
        private static final String NEXT_ROW_CELL = "selectNextRowCell";
        private static final String NEXT_ROW_EXTEND_SELECTION = "selectNextRowExtendSelection";
        private static final String NEXT_ROW_CHANGE_LEAD = "selectNextRowChangeLead";
        private static final String PREVIOUS_ROW = "selectPreviousRow";
        private static final String PREVIOUS_ROW_CELL = "selectPreviousRowCell";
        private static final String PREVIOUS_ROW_EXTEND_SELECTION = "selectPreviousRowExtendSelection";
        private static final String PREVIOUS_ROW_CHANGE_LEAD = "selectPreviousRowChangeLead";
        private static final String NEXT_COLUMN = "selectNextColumn";
        private static final String NEXT_COLUMN_CELL = "selectNextColumnCell";
        private static final String NEXT_COLUMN_EXTEND_SELECTION = "selectNextColumnExtendSelection";
        private static final String NEXT_COLUMN_CHANGE_LEAD = "selectNextColumnChangeLead";
        private static final String PREVIOUS_COLUMN = "selectPreviousColumn";
        private static final String PREVIOUS_COLUMN_CELL = "selectPreviousColumnCell";
        private static final String PREVIOUS_COLUMN_EXTEND_SELECTION = "selectPreviousColumnExtendSelection";
        private static final String PREVIOUS_COLUMN_CHANGE_LEAD = "selectPreviousColumnChangeLead";
        private static final String SCROLL_LEFT_CHANGE_SELECTION = "scrollLeftChangeSelection";
        private static final String SCROLL_LEFT_EXTEND_SELECTION = "scrollLeftExtendSelection";
        private static final String SCROLL_RIGHT_CHANGE_SELECTION = "scrollRightChangeSelection";
        private static final String SCROLL_RIGHT_EXTEND_SELECTION = "scrollRightExtendSelection";
        private static final String SCROLL_UP_CHANGE_SELECTION = "scrollUpChangeSelection";
        private static final String SCROLL_UP_EXTEND_SELECTION = "scrollUpExtendSelection";
        private static final String SCROLL_DOWN_CHANGE_SELECTION = "scrollDownChangeSelection";
        private static final String SCROLL_DOWN_EXTEND_SELECTION = "scrollDownExtendSelection";
        private static final String FIRST_COLUMN = "selectFirstColumn";
        private static final String FIRST_COLUMN_EXTEND_SELECTION = "selectFirstColumnExtendSelection";
        private static final String LAST_COLUMN = "selectLastColumn";
        private static final String LAST_COLUMN_EXTEND_SELECTION = "selectLastColumnExtendSelection";
        private static final String FIRST_ROW = "selectFirstRow";
        private static final String FIRST_ROW_EXTEND_SELECTION = "selectFirstRowExtendSelection";
        private static final String LAST_ROW = "selectLastRow";
        private static final String LAST_ROW_EXTEND_SELECTION = "selectLastRowExtendSelection";
        private static final String ADD_TO_SELECTION = "addToSelection";
        private static final String TOGGLE_AND_ANCHOR = "toggleAndAnchor";
        private static final String EXTEND_TO = "extendTo";
        private static final String MOVE_SELECTION_TO = "moveSelectionTo";
        private static final String FOCUS_HEADER = "focusHeader";
        protected int dx;
        protected int dy;
        protected boolean extend;
        protected boolean inSelection;
        protected boolean forwards;
        protected boolean vertically;
        protected boolean toLimit;
        protected int leadRow;
        protected int leadColumn;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BasicTableUI.class.desiredAssertionStatus();
        }

        Actions(String str) {
            super(str);
        }

        Actions(String str, int i2, int i3, boolean z2, boolean z3) {
            super(str);
            if (z3) {
                this.inSelection = true;
                i2 = sign(i2);
                i3 = sign(i3);
                if (!$assertionsDisabled && ((i2 != 0 && i3 != 0) || (i2 == 0 && i3 == 0))) {
                    throw new AssertionError();
                }
            }
            this.dx = i2;
            this.dy = i3;
            this.extend = z2;
        }

        Actions(String str, boolean z2, boolean z3, boolean z4, boolean z5) {
            this(str, 0, 0, z2, false);
            this.forwards = z3;
            this.vertically = z4;
            this.toLimit = z5;
        }

        private static int clipToRange(int i2, int i3, int i4) {
            return Math.min(Math.max(i2, i3), i4 - 1);
        }

        private void moveWithinTableRange(JTable jTable, int i2, int i3) {
            this.leadRow = clipToRange(this.leadRow + i3, 0, jTable.getRowCount());
            this.leadColumn = clipToRange(this.leadColumn + i2, 0, jTable.getColumnCount());
        }

        private static int sign(int i2) {
            if (i2 < 0) {
                return -1;
            }
            return i2 == 0 ? 0 : 1;
        }

        private boolean moveWithinSelectedRange(JTable jTable, int i2, int i3, ListSelectionModel listSelectionModel, ListSelectionModel listSelectionModel2) {
            int selectedColumnCount;
            int rowCount;
            int minSelectionIndex;
            int maxSelectionIndex;
            int minSelectionIndex2;
            boolean z2;
            boolean rowSelectionAllowed = jTable.getRowSelectionAllowed();
            boolean columnSelectionAllowed = jTable.getColumnSelectionAllowed();
            if (rowSelectionAllowed && columnSelectionAllowed) {
                selectedColumnCount = jTable.getSelectedRowCount() * jTable.getSelectedColumnCount();
                minSelectionIndex2 = listSelectionModel2.getMinSelectionIndex();
                maxSelectionIndex = listSelectionModel2.getMaxSelectionIndex();
                minSelectionIndex = listSelectionModel.getMinSelectionIndex();
                rowCount = listSelectionModel.getMaxSelectionIndex();
            } else if (rowSelectionAllowed) {
                selectedColumnCount = jTable.getSelectedRowCount();
                minSelectionIndex2 = 0;
                maxSelectionIndex = jTable.getColumnCount() - 1;
                minSelectionIndex = listSelectionModel.getMinSelectionIndex();
                rowCount = listSelectionModel.getMaxSelectionIndex();
            } else if (columnSelectionAllowed) {
                selectedColumnCount = jTable.getSelectedColumnCount();
                minSelectionIndex2 = listSelectionModel2.getMinSelectionIndex();
                maxSelectionIndex = listSelectionModel2.getMaxSelectionIndex();
                minSelectionIndex = 0;
                rowCount = jTable.getRowCount() - 1;
            } else {
                selectedColumnCount = 0;
                rowCount = 0;
                minSelectionIndex = 0;
                maxSelectionIndex = 0;
                minSelectionIndex2 = 0;
            }
            if (selectedColumnCount == 0 || (selectedColumnCount == 1 && jTable.isCellSelected(this.leadRow, this.leadColumn))) {
                z2 = false;
                maxSelectionIndex = jTable.getColumnCount() - 1;
                rowCount = jTable.getRowCount() - 1;
                minSelectionIndex2 = Math.min(0, maxSelectionIndex);
                minSelectionIndex = Math.min(0, rowCount);
            } else {
                z2 = true;
            }
            if (i3 == 1 && this.leadColumn == -1) {
                this.leadColumn = minSelectionIndex2;
                this.leadRow = -1;
            } else if (i2 == 1 && this.leadRow == -1) {
                this.leadRow = minSelectionIndex;
                this.leadColumn = -1;
            } else if (i3 == -1 && this.leadColumn == -1) {
                this.leadColumn = maxSelectionIndex;
                this.leadRow = rowCount + 1;
            } else if (i2 == -1 && this.leadRow == -1) {
                this.leadRow = rowCount;
                this.leadColumn = maxSelectionIndex + 1;
            }
            this.leadRow = Math.min(Math.max(this.leadRow, minSelectionIndex - 1), rowCount + 1);
            this.leadColumn = Math.min(Math.max(this.leadColumn, minSelectionIndex2 - 1), maxSelectionIndex + 1);
            do {
                calcNextPos(i2, minSelectionIndex2, maxSelectionIndex, i3, minSelectionIndex, rowCount);
                if (!z2) {
                    break;
                }
            } while (!jTable.isCellSelected(this.leadRow, this.leadColumn));
            return z2;
        }

        private void calcNextPos(int i2, int i3, int i4, int i5, int i6, int i7) {
            if (i2 != 0) {
                this.leadColumn += i2;
                if (this.leadColumn > i4) {
                    this.leadColumn = i3;
                    this.leadRow++;
                    if (this.leadRow > i7) {
                        this.leadRow = i6;
                        return;
                    }
                    return;
                }
                if (this.leadColumn < i3) {
                    this.leadColumn = i4;
                    this.leadRow--;
                    if (this.leadRow < i6) {
                        this.leadRow = i7;
                        return;
                    }
                    return;
                }
                return;
            }
            this.leadRow += i5;
            if (this.leadRow > i7) {
                this.leadRow = i6;
                this.leadColumn++;
                if (this.leadColumn > i4) {
                    this.leadColumn = i3;
                    return;
                }
                return;
            }
            if (this.leadRow < i6) {
                this.leadRow = i7;
                this.leadColumn--;
                if (this.leadColumn < i3) {
                    this.leadColumn = i4;
                }
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            int columnCount;
            JTableHeader tableHeader;
            String name = getName();
            JTable jTable = (JTable) actionEvent.getSource();
            ListSelectionModel selectionModel = jTable.getSelectionModel();
            this.leadRow = BasicTableUI.getAdjustedLead(jTable, true, selectionModel);
            ListSelectionModel selectionModel2 = jTable.getColumnModel().getSelectionModel();
            this.leadColumn = BasicTableUI.getAdjustedLead(jTable, false, selectionModel2);
            if (name == SCROLL_LEFT_CHANGE_SELECTION || name == SCROLL_LEFT_EXTEND_SELECTION || name == SCROLL_RIGHT_CHANGE_SELECTION || name == SCROLL_RIGHT_EXTEND_SELECTION || name == SCROLL_UP_CHANGE_SELECTION || name == SCROLL_UP_EXTEND_SELECTION || name == SCROLL_DOWN_CHANGE_SELECTION || name == SCROLL_DOWN_EXTEND_SELECTION || name == FIRST_COLUMN || name == FIRST_COLUMN_EXTEND_SELECTION || name == FIRST_ROW || name == FIRST_ROW_EXTEND_SELECTION || name == LAST_COLUMN || name == LAST_COLUMN_EXTEND_SELECTION || name == LAST_ROW || name == LAST_ROW_EXTEND_SELECTION) {
                if (this.toLimit) {
                    if (this.vertically) {
                        int rowCount = jTable.getRowCount();
                        this.dx = 0;
                        this.dy = this.forwards ? rowCount : -rowCount;
                    } else {
                        int columnCount2 = jTable.getColumnCount();
                        this.dx = this.forwards ? columnCount2 : -columnCount2;
                        this.dy = 0;
                    }
                } else {
                    if (!(SwingUtilities.getUnwrappedParent(jTable).getParent() instanceof JScrollPane)) {
                        return;
                    }
                    Dimension size = jTable.getParent().getSize();
                    if (this.vertically) {
                        Rectangle cellRect = jTable.getCellRect(this.leadRow, 0, true);
                        if (this.forwards) {
                            cellRect.f12373y += Math.max(size.height, cellRect.height);
                        } else {
                            cellRect.f12373y -= size.height;
                        }
                        this.dx = 0;
                        int iRowAtPoint = jTable.rowAtPoint(cellRect.getLocation());
                        if (iRowAtPoint == -1 && this.forwards) {
                            iRowAtPoint = jTable.getRowCount();
                        }
                        this.dy = iRowAtPoint - this.leadRow;
                    } else {
                        Rectangle cellRect2 = jTable.getCellRect(0, this.leadColumn, true);
                        if (this.forwards) {
                            cellRect2.f12372x += Math.max(size.width, cellRect2.width);
                        } else {
                            cellRect2.f12372x -= size.width;
                        }
                        int iColumnAtPoint = jTable.columnAtPoint(cellRect2.getLocation());
                        if (iColumnAtPoint == -1) {
                            boolean zIsLeftToRight = jTable.getComponentOrientation().isLeftToRight();
                            if (this.forwards) {
                                columnCount = zIsLeftToRight ? jTable.getColumnCount() : 0;
                            } else {
                                columnCount = zIsLeftToRight ? 0 : jTable.getColumnCount();
                            }
                            iColumnAtPoint = columnCount;
                        }
                        this.dx = iColumnAtPoint - this.leadColumn;
                        this.dy = 0;
                    }
                }
            }
            if (name == NEXT_ROW || name == NEXT_ROW_CELL || name == NEXT_ROW_EXTEND_SELECTION || name == NEXT_ROW_CHANGE_LEAD || name == NEXT_COLUMN || name == NEXT_COLUMN_CELL || name == NEXT_COLUMN_EXTEND_SELECTION || name == NEXT_COLUMN_CHANGE_LEAD || name == PREVIOUS_ROW || name == PREVIOUS_ROW_CELL || name == PREVIOUS_ROW_EXTEND_SELECTION || name == PREVIOUS_ROW_CHANGE_LEAD || name == PREVIOUS_COLUMN || name == PREVIOUS_COLUMN_CELL || name == PREVIOUS_COLUMN_EXTEND_SELECTION || name == PREVIOUS_COLUMN_CHANGE_LEAD || name == SCROLL_LEFT_CHANGE_SELECTION || name == SCROLL_LEFT_EXTEND_SELECTION || name == SCROLL_RIGHT_CHANGE_SELECTION || name == SCROLL_RIGHT_EXTEND_SELECTION || name == SCROLL_UP_CHANGE_SELECTION || name == SCROLL_UP_EXTEND_SELECTION || name == SCROLL_DOWN_CHANGE_SELECTION || name == SCROLL_DOWN_EXTEND_SELECTION || name == FIRST_COLUMN || name == FIRST_COLUMN_EXTEND_SELECTION || name == FIRST_ROW || name == FIRST_ROW_EXTEND_SELECTION || name == LAST_COLUMN || name == LAST_COLUMN_EXTEND_SELECTION || name == LAST_ROW || name == LAST_ROW_EXTEND_SELECTION) {
                if (jTable.isEditing() && !jTable.getCellEditor().stopCellEditing()) {
                    return;
                }
                boolean z2 = false;
                if (name == NEXT_ROW_CHANGE_LEAD || name == PREVIOUS_ROW_CHANGE_LEAD) {
                    z2 = selectionModel.getSelectionMode() == 2;
                } else if (name == NEXT_COLUMN_CHANGE_LEAD || name == PREVIOUS_COLUMN_CHANGE_LEAD) {
                    z2 = selectionModel2.getSelectionMode() == 2;
                }
                if (z2) {
                    moveWithinTableRange(jTable, this.dx, this.dy);
                    if (this.dy != 0) {
                        ((DefaultListSelectionModel) selectionModel).moveLeadSelectionIndex(this.leadRow);
                        if (BasicTableUI.getAdjustedLead(jTable, false, selectionModel2) == -1 && jTable.getColumnCount() > 0) {
                            ((DefaultListSelectionModel) selectionModel2).moveLeadSelectionIndex(0);
                        }
                    } else {
                        ((DefaultListSelectionModel) selectionModel2).moveLeadSelectionIndex(this.leadColumn);
                        if (BasicTableUI.getAdjustedLead(jTable, true, selectionModel) == -1 && jTable.getRowCount() > 0) {
                            ((DefaultListSelectionModel) selectionModel).moveLeadSelectionIndex(0);
                        }
                    }
                    Rectangle cellRect3 = jTable.getCellRect(this.leadRow, this.leadColumn, false);
                    if (cellRect3 != null) {
                        jTable.scrollRectToVisible(cellRect3);
                        return;
                    }
                    return;
                }
                if (!this.inSelection) {
                    moveWithinTableRange(jTable, this.dx, this.dy);
                    jTable.changeSelection(this.leadRow, this.leadColumn, false, this.extend);
                    return;
                }
                if (jTable.getRowCount() <= 0 || jTable.getColumnCount() <= 0) {
                    return;
                }
                if (moveWithinSelectedRange(jTable, this.dx, this.dy, selectionModel, selectionModel2)) {
                    if (selectionModel.isSelectedIndex(this.leadRow)) {
                        selectionModel.addSelectionInterval(this.leadRow, this.leadRow);
                    } else {
                        selectionModel.removeSelectionInterval(this.leadRow, this.leadRow);
                    }
                    if (selectionModel2.isSelectedIndex(this.leadColumn)) {
                        selectionModel2.addSelectionInterval(this.leadColumn, this.leadColumn);
                    } else {
                        selectionModel2.removeSelectionInterval(this.leadColumn, this.leadColumn);
                    }
                    Rectangle cellRect4 = jTable.getCellRect(this.leadRow, this.leadColumn, false);
                    if (cellRect4 != null) {
                        jTable.scrollRectToVisible(cellRect4);
                        return;
                    }
                    return;
                }
                jTable.changeSelection(this.leadRow, this.leadColumn, false, false);
                return;
            }
            if (name == CANCEL_EDITING) {
                jTable.removeEditor();
                return;
            }
            if (name == SELECT_ALL) {
                jTable.selectAll();
                return;
            }
            if (name == CLEAR_SELECTION) {
                jTable.clearSelection();
                return;
            }
            if (name == START_EDITING) {
                if (!jTable.hasFocus()) {
                    TableCellEditor cellEditor = jTable.getCellEditor();
                    if (cellEditor != null && !cellEditor.stopCellEditing()) {
                        return;
                    }
                    jTable.requestFocus();
                    return;
                }
                jTable.editCellAt(this.leadRow, this.leadColumn, actionEvent);
                Component editorComponent = jTable.getEditorComponent();
                if (editorComponent != null) {
                    editorComponent.requestFocus();
                    return;
                }
                return;
            }
            if (name == ADD_TO_SELECTION) {
                if (!jTable.isCellSelected(this.leadRow, this.leadColumn)) {
                    int anchorSelectionIndex = selectionModel.getAnchorSelectionIndex();
                    int anchorSelectionIndex2 = selectionModel2.getAnchorSelectionIndex();
                    selectionModel.setValueIsAdjusting(true);
                    selectionModel2.setValueIsAdjusting(true);
                    jTable.changeSelection(this.leadRow, this.leadColumn, true, false);
                    selectionModel.setAnchorSelectionIndex(anchorSelectionIndex);
                    selectionModel2.setAnchorSelectionIndex(anchorSelectionIndex2);
                    selectionModel.setValueIsAdjusting(false);
                    selectionModel2.setValueIsAdjusting(false);
                    return;
                }
                return;
            }
            if (name == TOGGLE_AND_ANCHOR) {
                jTable.changeSelection(this.leadRow, this.leadColumn, true, false);
                return;
            }
            if (name == EXTEND_TO) {
                jTable.changeSelection(this.leadRow, this.leadColumn, false, true);
                return;
            }
            if (name == MOVE_SELECTION_TO) {
                jTable.changeSelection(this.leadRow, this.leadColumn, false, false);
                return;
            }
            if (name == FOCUS_HEADER && (tableHeader = jTable.getTableHeader()) != null) {
                int selectedColumn = jTable.getSelectedColumn();
                if (selectedColumn >= 0) {
                    TableHeaderUI ui = tableHeader.getUI();
                    if (ui instanceof BasicTableHeaderUI) {
                        ((BasicTableHeaderUI) ui).selectColumn(selectedColumn);
                    }
                }
                tableHeader.requestFocusInWindow();
            }
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            String name = getName();
            if ((obj instanceof JTable) && Boolean.TRUE.equals(((JTable) obj).getClientProperty("Table.isFileList")) && (name == NEXT_COLUMN || name == NEXT_COLUMN_CELL || name == NEXT_COLUMN_EXTEND_SELECTION || name == NEXT_COLUMN_CHANGE_LEAD || name == PREVIOUS_COLUMN || name == PREVIOUS_COLUMN_CELL || name == PREVIOUS_COLUMN_EXTEND_SELECTION || name == PREVIOUS_COLUMN_CHANGE_LEAD || name == SCROLL_LEFT_CHANGE_SELECTION || name == SCROLL_LEFT_EXTEND_SELECTION || name == SCROLL_RIGHT_CHANGE_SELECTION || name == SCROLL_RIGHT_EXTEND_SELECTION || name == FIRST_COLUMN || name == FIRST_COLUMN_EXTEND_SELECTION || name == LAST_COLUMN || name == LAST_COLUMN_EXTEND_SELECTION || name == NEXT_ROW_CELL || name == PREVIOUS_ROW_CELL)) {
                return false;
            }
            if (name == CANCEL_EDITING && (obj instanceof JTable)) {
                return ((JTable) obj).isEditing();
            }
            if (name == NEXT_ROW_CHANGE_LEAD || name == PREVIOUS_ROW_CHANGE_LEAD) {
                return obj != null && (((JTable) obj).getSelectionModel() instanceof DefaultListSelectionModel);
            }
            if (name == NEXT_COLUMN_CHANGE_LEAD || name == PREVIOUS_COLUMN_CHANGE_LEAD) {
                return obj != null && (((JTable) obj).getColumnModel().getSelectionModel() instanceof DefaultListSelectionModel);
            }
            if (name != ADD_TO_SELECTION || !(obj instanceof JTable)) {
                return (name == FOCUS_HEADER && (obj instanceof JTable) && ((JTable) obj).getTableHeader() == null) ? false : true;
            }
            JTable jTable = (JTable) obj;
            return (jTable.isEditing() || jTable.isCellSelected(BasicTableUI.getAdjustedLead(jTable, true), BasicTableUI.getAdjustedLead(jTable, false))) ? false : true;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI$KeyHandler.class */
    public class KeyHandler implements KeyListener {
        public KeyHandler() {
        }

        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            BasicTableUI.this.getHandler().keyPressed(keyEvent);
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
            BasicTableUI.this.getHandler().keyReleased(keyEvent);
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
            BasicTableUI.this.getHandler().keyTyped(keyEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI$FocusHandler.class */
    public class FocusHandler implements FocusListener {
        public FocusHandler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            BasicTableUI.this.getHandler().focusGained(focusEvent);
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            BasicTableUI.this.getHandler().focusLost(focusEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI$MouseInputHandler.class */
    public class MouseInputHandler implements MouseInputListener {
        public MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mouseClicked(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mousePressed(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mouseReleased(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mouseEntered(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mouseExited(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mouseMoved(mouseEvent);
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            BasicTableUI.this.getHandler().mouseDragged(mouseEvent);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI$Handler.class */
    private class Handler implements FocusListener, MouseInputListener, PropertyChangeListener, ListSelectionListener, ActionListener, DragRecognitionSupport.BeforeDrag {
        private Component dispatchComponent;
        private int pressedRow;
        private int pressedCol;
        private MouseEvent pressedEvent;
        private boolean dragPressDidSelection;
        private boolean dragStarted;
        private boolean shouldStartTimer;
        private boolean outsidePrefSize;
        private Timer timer;

        private Handler() {
            this.timer = null;
        }

        private void repaintLeadCell() {
            int adjustedLead = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, true);
            int adjustedLead2 = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, false);
            if (adjustedLead < 0 || adjustedLead2 < 0) {
                return;
            }
            BasicTableUI.this.table.repaint(BasicTableUI.this.table.getCellRect(adjustedLead, adjustedLead2, false));
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            repaintLeadCell();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            repaintLeadCell();
        }

        public void keyPressed(KeyEvent keyEvent) {
        }

        public void keyReleased(KeyEvent keyEvent) {
        }

        public void keyTyped(KeyEvent keyEvent) {
            KeyStroke keyStroke = KeyStroke.getKeyStroke(keyEvent.getKeyChar(), keyEvent.getModifiers());
            InputMap inputMap = BasicTableUI.this.table.getInputMap(0);
            if (inputMap != null && inputMap.get(keyStroke) != null) {
                return;
            }
            InputMap inputMap2 = BasicTableUI.this.table.getInputMap(1);
            if (inputMap2 != null && inputMap2.get(keyStroke) != null) {
                return;
            }
            KeyStroke keyStrokeForEvent = KeyStroke.getKeyStrokeForEvent(keyEvent);
            if (keyEvent.getKeyChar() != '\r') {
                int adjustedLead = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, true);
                int adjustedLead2 = BasicTableUI.getAdjustedLead(BasicTableUI.this.table, false);
                if (adjustedLead != -1 && adjustedLead2 != -1 && !BasicTableUI.this.table.isEditing() && !BasicTableUI.this.table.editCellAt(adjustedLead, adjustedLead2)) {
                    return;
                }
                Component editorComponent = BasicTableUI.this.table.getEditorComponent();
                if (BasicTableUI.this.table.isEditing() && editorComponent != null && (editorComponent instanceof JComponent)) {
                    JComponent jComponent = (JComponent) editorComponent;
                    InputMap inputMap3 = jComponent.getInputMap(0);
                    Object obj = inputMap3 != null ? inputMap3.get(keyStrokeForEvent) : null;
                    if (obj == null) {
                        InputMap inputMap4 = jComponent.getInputMap(1);
                        obj = inputMap4 != null ? inputMap4.get(keyStrokeForEvent) : null;
                    }
                    if (obj != null) {
                        ActionMap actionMap = jComponent.getActionMap();
                        Action action = actionMap != null ? actionMap.get(obj) : null;
                        if (action != null && SwingUtilities.notifyAction(action, keyStrokeForEvent, keyEvent, jComponent, keyEvent.getModifiers())) {
                            keyEvent.consume();
                        }
                    }
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        private void setDispatchComponent(MouseEvent mouseEvent) {
            Component editorComponent = BasicTableUI.this.table.getEditorComponent();
            Point pointConvertPoint = SwingUtilities.convertPoint(BasicTableUI.this.table, mouseEvent.getPoint(), editorComponent);
            this.dispatchComponent = SwingUtilities.getDeepestComponentAt(editorComponent, pointConvertPoint.f12370x, pointConvertPoint.f12371y);
            SwingUtilities2.setSkipClickCount(this.dispatchComponent, mouseEvent.getClickCount() - 1);
        }

        private boolean repostEvent(MouseEvent mouseEvent) {
            if (this.dispatchComponent == null || !BasicTableUI.this.table.isEditing()) {
                return false;
            }
            this.dispatchComponent.dispatchEvent(SwingUtilities.convertMouseEvent(BasicTableUI.this.table, mouseEvent, this.dispatchComponent));
            return true;
        }

        private void setValueIsAdjusting(boolean z2) {
            BasicTableUI.this.table.getSelectionModel().setValueIsAdjusting(z2);
            BasicTableUI.this.table.getColumnModel().getSelectionModel().setValueIsAdjusting(z2);
        }

        private boolean canStartDrag() {
            if (this.pressedRow != -1 && this.pressedCol != -1) {
                if (BasicTableUI.this.isFileList) {
                    return !this.outsidePrefSize;
                }
                if (BasicTableUI.this.table.getSelectionModel().getSelectionMode() == 0 && BasicTableUI.this.table.getColumnModel().getSelectionModel().getSelectionMode() == 0) {
                    return true;
                }
                return BasicTableUI.this.table.isCellSelected(this.pressedRow, this.pressedCol);
            }
            return false;
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicTableUI.this.table)) {
                return;
            }
            if (BasicTableUI.this.table.isEditing() && !BasicTableUI.this.table.getCellEditor().stopCellEditing()) {
                Component editorComponent = BasicTableUI.this.table.getEditorComponent();
                if (editorComponent != null && !editorComponent.hasFocus()) {
                    SwingUtilities2.compositeRequestFocus(editorComponent);
                    return;
                }
                return;
            }
            Point point = mouseEvent.getPoint();
            this.pressedRow = BasicTableUI.this.table.rowAtPoint(point);
            this.pressedCol = BasicTableUI.this.table.columnAtPoint(point);
            this.outsidePrefSize = BasicTableUI.this.pointOutsidePrefSize(this.pressedRow, this.pressedCol, point);
            if (BasicTableUI.this.isFileList) {
                this.shouldStartTimer = (!BasicTableUI.this.table.isCellSelected(this.pressedRow, this.pressedCol) || mouseEvent.isShiftDown() || BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent) || this.outsidePrefSize) ? false : true;
            }
            if (BasicTableUI.this.table.getDragEnabled()) {
                mousePressedDND(mouseEvent);
                return;
            }
            SwingUtilities2.adjustFocus(BasicTableUI.this.table);
            if (!BasicTableUI.this.isFileList) {
                setValueIsAdjusting(true);
            }
            adjustSelection(mouseEvent);
        }

        private void mousePressedDND(MouseEvent mouseEvent) {
            this.pressedEvent = mouseEvent;
            boolean z2 = true;
            this.dragStarted = false;
            if (!canStartDrag() || !DragRecognitionSupport.mousePressed(mouseEvent)) {
                if (!BasicTableUI.this.isFileList) {
                    setValueIsAdjusting(true);
                }
            } else {
                this.dragPressDidSelection = false;
                if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent) && BasicTableUI.this.isFileList) {
                    return;
                }
                if (!mouseEvent.isShiftDown() && BasicTableUI.this.table.isCellSelected(this.pressedRow, this.pressedCol)) {
                    BasicTableUI.this.table.getSelectionModel().addSelectionInterval(this.pressedRow, this.pressedRow);
                    BasicTableUI.this.table.getColumnModel().getSelectionModel().addSelectionInterval(this.pressedCol, this.pressedCol);
                    return;
                } else {
                    this.dragPressDidSelection = true;
                    z2 = false;
                }
            }
            if (z2) {
                SwingUtilities2.adjustFocus(BasicTableUI.this.table);
            }
            adjustSelection(mouseEvent);
        }

        private void adjustSelection(MouseEvent mouseEvent) {
            if (this.outsidePrefSize) {
                if (mouseEvent.getID() == 501) {
                    if (!mouseEvent.isShiftDown() || BasicTableUI.this.table.getSelectionModel().getSelectionMode() == 0) {
                        BasicTableUI.this.table.clearSelection();
                        TableCellEditor cellEditor = BasicTableUI.this.table.getCellEditor();
                        if (cellEditor != null) {
                            cellEditor.stopCellEditing();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            if (this.pressedCol == -1 || this.pressedRow == -1) {
                return;
            }
            boolean dragEnabled = BasicTableUI.this.table.getDragEnabled();
            if (!dragEnabled && !BasicTableUI.this.isFileList && BasicTableUI.this.table.editCellAt(this.pressedRow, this.pressedCol, mouseEvent)) {
                setDispatchComponent(mouseEvent);
                repostEvent(mouseEvent);
            }
            TableCellEditor cellEditor2 = BasicTableUI.this.table.getCellEditor();
            if (dragEnabled || cellEditor2 == null || cellEditor2.shouldSelectCell(mouseEvent)) {
                BasicTableUI.this.table.changeSelection(this.pressedRow, this.pressedCol, BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent), mouseEvent.isShiftDown());
            }
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            if (this.timer != null) {
                this.timer.stop();
                this.timer = null;
            }
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicTableUI.this.table.editCellAt(this.pressedRow, this.pressedCol, null);
            Component editorComponent = BasicTableUI.this.table.getEditorComponent();
            if (editorComponent != null && !editorComponent.hasFocus()) {
                SwingUtilities2.compositeRequestFocus(editorComponent);
            }
        }

        private void maybeStartTimer() {
            if (!this.shouldStartTimer) {
                return;
            }
            if (this.timer == null) {
                this.timer = new Timer(1200, this);
                this.timer.setRepeats(false);
            }
            this.timer.start();
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicTableUI.this.table)) {
                return;
            }
            if (!BasicTableUI.this.table.getDragEnabled()) {
                if (BasicTableUI.this.isFileList) {
                    maybeStartTimer();
                }
            } else {
                mouseReleasedDND(mouseEvent);
            }
            this.pressedEvent = null;
            repostEvent(mouseEvent);
            this.dispatchComponent = null;
            setValueIsAdjusting(false);
        }

        private void mouseReleasedDND(MouseEvent mouseEvent) {
            MouseEvent mouseEventMouseReleased = DragRecognitionSupport.mouseReleased(mouseEvent);
            if (mouseEventMouseReleased != null) {
                SwingUtilities2.adjustFocus(BasicTableUI.this.table);
                if (!this.dragPressDidSelection) {
                    adjustSelection(mouseEventMouseReleased);
                }
            }
            if (!this.dragStarted) {
                if (BasicTableUI.this.isFileList) {
                    maybeStartTimer();
                    return;
                }
                Point point = mouseEvent.getPoint();
                if (this.pressedEvent != null && BasicTableUI.this.table.rowAtPoint(point) == this.pressedRow && BasicTableUI.this.table.columnAtPoint(point) == this.pressedCol && BasicTableUI.this.table.editCellAt(this.pressedRow, this.pressedCol, this.pressedEvent)) {
                    setDispatchComponent(this.pressedEvent);
                    repostEvent(this.pressedEvent);
                    TableCellEditor cellEditor = BasicTableUI.this.table.getCellEditor();
                    if (cellEditor != null) {
                        cellEditor.shouldSelectCell(this.pressedEvent);
                    }
                }
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.plaf.basic.DragRecognitionSupport.BeforeDrag
        public void dragStarting(MouseEvent mouseEvent) {
            this.dragStarted = true;
            if (BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent) && BasicTableUI.this.isFileList) {
                BasicTableUI.this.table.getSelectionModel().addSelectionInterval(this.pressedRow, this.pressedRow);
                BasicTableUI.this.table.getColumnModel().getSelectionModel().addSelectionInterval(this.pressedCol, this.pressedCol);
            }
            this.pressedEvent = null;
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            if (SwingUtilities2.shouldIgnore(mouseEvent, BasicTableUI.this.table)) {
                return;
            }
            if (BasicTableUI.this.table.getDragEnabled() && (DragRecognitionSupport.mouseDragged(mouseEvent, this) || this.dragStarted)) {
                return;
            }
            repostEvent(mouseEvent);
            if (BasicTableUI.this.isFileList || BasicTableUI.this.table.isEditing()) {
                return;
            }
            Point point = mouseEvent.getPoint();
            int iRowAtPoint = BasicTableUI.this.table.rowAtPoint(point);
            int iColumnAtPoint = BasicTableUI.this.table.columnAtPoint(point);
            if (iColumnAtPoint == -1 || iRowAtPoint == -1) {
                return;
            }
            BasicTableUI.this.table.changeSelection(iRowAtPoint, iColumnAtPoint, BasicGraphicsUtils.isMenuShortcutKeyDown(mouseEvent), true);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            if ("componentOrientation" == propertyName) {
                SwingUtilities.replaceUIInputMap(BasicTableUI.this.table, 1, BasicTableUI.this.getInputMap(1));
                JTableHeader tableHeader = BasicTableUI.this.table.getTableHeader();
                if (tableHeader != null) {
                    tableHeader.setComponentOrientation((ComponentOrientation) propertyChangeEvent.getNewValue());
                    return;
                }
                return;
            }
            if ("dropLocation" == propertyName) {
                repaintDropLocation((JTable.DropLocation) propertyChangeEvent.getOldValue());
                repaintDropLocation(BasicTableUI.this.table.getDropLocation());
                return;
            }
            if ("Table.isFileList" == propertyName) {
                BasicTableUI.this.isFileList = Boolean.TRUE.equals(BasicTableUI.this.table.getClientProperty("Table.isFileList"));
                BasicTableUI.this.table.revalidate();
                BasicTableUI.this.table.repaint();
                if (BasicTableUI.this.isFileList) {
                    BasicTableUI.this.table.getSelectionModel().addListSelectionListener(BasicTableUI.this.getHandler());
                    return;
                } else {
                    BasicTableUI.this.table.getSelectionModel().removeListSelectionListener(BasicTableUI.this.getHandler());
                    this.timer = null;
                    return;
                }
            }
            if ("selectionModel" == propertyName && BasicTableUI.this.isFileList) {
                ((ListSelectionModel) propertyChangeEvent.getOldValue()).removeListSelectionListener(BasicTableUI.this.getHandler());
                BasicTableUI.this.table.getSelectionModel().addListSelectionListener(BasicTableUI.this.getHandler());
            }
        }

        private void repaintDropLocation(JTable.DropLocation dropLocation) {
            Rectangle rectangleExtendRect;
            Rectangle rectangleExtendRect2;
            if (dropLocation == null) {
                return;
            }
            if (!dropLocation.isInsertRow() && !dropLocation.isInsertColumn()) {
                Rectangle cellRect = BasicTableUI.this.table.getCellRect(dropLocation.getRow(), dropLocation.getColumn(), false);
                if (cellRect != null) {
                    BasicTableUI.this.table.repaint(cellRect);
                    return;
                }
                return;
            }
            if (dropLocation.isInsertRow() && (rectangleExtendRect2 = BasicTableUI.this.extendRect(BasicTableUI.this.getHDropLineRect(dropLocation), true)) != null) {
                BasicTableUI.this.table.repaint(rectangleExtendRect2);
            }
            if (dropLocation.isInsertColumn() && (rectangleExtendRect = BasicTableUI.this.extendRect(BasicTableUI.this.getVDropLineRect(dropLocation), false)) != null) {
                BasicTableUI.this.table.repaint(rectangleExtendRect);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean pointOutsidePrefSize(int i2, int i3, Point point) {
        if (!this.isFileList) {
            return false;
        }
        return SwingUtilities2.pointOutsidePrefSize(this.table, i2, i3, point);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected KeyListener createKeyListener() {
        return null;
    }

    protected FocusListener createFocusListener() {
        return getHandler();
    }

    protected MouseInputListener createMouseInputListener() {
        return getHandler();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTableUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.table = (JTable) jComponent;
        this.rendererPane = new CellRendererPane();
        this.table.add(this.rendererPane);
        installDefaults();
        installDefaults2();
        installListeners();
        installKeyboardActions();
    }

    protected void installDefaults() {
        Container parent;
        LookAndFeel.installColorsAndFont(this.table, "Table.background", "Table.foreground", "Table.font");
        LookAndFeel.installProperty(this.table, "opaque", Boolean.TRUE);
        Color selectionBackground = this.table.getSelectionBackground();
        if (selectionBackground == null || (selectionBackground instanceof UIResource)) {
            Color color = UIManager.getColor("Table.selectionBackground");
            this.table.setSelectionBackground(color != null ? color : UIManager.getColor("textHighlight"));
        }
        Color selectionForeground = this.table.getSelectionForeground();
        if (selectionForeground == null || (selectionForeground instanceof UIResource)) {
            Color color2 = UIManager.getColor("Table.selectionForeground");
            this.table.setSelectionForeground(color2 != null ? color2 : UIManager.getColor("textHighlightText"));
        }
        Color gridColor = this.table.getGridColor();
        if (gridColor == null || (gridColor instanceof UIResource)) {
            Color color3 = UIManager.getColor("Table.gridColor");
            this.table.setGridColor(color3 != null ? color3 : Color.GRAY);
        }
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this.table);
        if (unwrappedParent != null && (parent = unwrappedParent.getParent()) != null && (parent instanceof JScrollPane)) {
            LookAndFeel.installBorder((JScrollPane) parent, "Table.scrollPaneBorder");
        }
        this.isFileList = Boolean.TRUE.equals(this.table.getClientProperty("Table.isFileList"));
    }

    private void installDefaults2() {
        TransferHandler transferHandler = this.table.getTransferHandler();
        if (transferHandler == null || (transferHandler instanceof UIResource)) {
            this.table.setTransferHandler(defaultTransferHandler);
            if (this.table.getDropTarget() instanceof UIResource) {
                this.table.setDropTarget(null);
            }
        }
    }

    protected void installListeners() {
        this.focusListener = createFocusListener();
        this.keyListener = createKeyListener();
        this.mouseInputListener = createMouseInputListener();
        this.table.addFocusListener(this.focusListener);
        this.table.addKeyListener(this.keyListener);
        this.table.addMouseListener(this.mouseInputListener);
        this.table.addMouseMotionListener(this.mouseInputListener);
        this.table.addPropertyChangeListener(getHandler());
        if (this.isFileList) {
            this.table.getSelectionModel().addListSelectionListener(getHandler());
        }
    }

    protected void installKeyboardActions() {
        LazyActionMap.installLazyActionMap(this.table, BasicTableUI.class, "Table.actionMap");
        SwingUtilities.replaceUIInputMap(this.table, 1, getInputMap(1));
    }

    InputMap getInputMap(int i2) {
        InputMap inputMap;
        if (i2 == 1) {
            InputMap inputMap2 = (InputMap) DefaultLookup.get(this.table, this, "Table.ancestorInputMap");
            if (this.table.getComponentOrientation().isLeftToRight() || (inputMap = (InputMap) DefaultLookup.get(this.table, this, "Table.ancestorInputMap.RightToLeft")) == null) {
                return inputMap2;
            }
            inputMap.setParent(inputMap2);
            return inputMap;
        }
        return null;
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions("selectNextColumn", 1, 0, false, false));
        lazyActionMap.put(new Actions("selectNextColumnChangeLead", 1, 0, false, false));
        lazyActionMap.put(new Actions("selectPreviousColumn", -1, 0, false, false));
        lazyActionMap.put(new Actions("selectPreviousColumnChangeLead", -1, 0, false, false));
        lazyActionMap.put(new Actions("selectNextRow", 0, 1, false, false));
        lazyActionMap.put(new Actions("selectNextRowChangeLead", 0, 1, false, false));
        lazyActionMap.put(new Actions("selectPreviousRow", 0, -1, false, false));
        lazyActionMap.put(new Actions("selectPreviousRowChangeLead", 0, -1, false, false));
        lazyActionMap.put(new Actions("selectNextColumnExtendSelection", 1, 0, true, false));
        lazyActionMap.put(new Actions("selectPreviousColumnExtendSelection", -1, 0, true, false));
        lazyActionMap.put(new Actions("selectNextRowExtendSelection", 0, 1, true, false));
        lazyActionMap.put(new Actions("selectPreviousRowExtendSelection", 0, -1, true, false));
        lazyActionMap.put(new Actions("scrollUpChangeSelection", false, false, true, false));
        lazyActionMap.put(new Actions("scrollDownChangeSelection", false, true, true, false));
        lazyActionMap.put(new Actions("selectFirstColumn", false, false, false, true));
        lazyActionMap.put(new Actions("selectLastColumn", false, true, false, true));
        lazyActionMap.put(new Actions("scrollUpExtendSelection", true, false, true, false));
        lazyActionMap.put(new Actions("scrollDownExtendSelection", true, true, true, false));
        lazyActionMap.put(new Actions("selectFirstColumnExtendSelection", true, false, false, true));
        lazyActionMap.put(new Actions("selectLastColumnExtendSelection", true, true, false, true));
        lazyActionMap.put(new Actions("selectFirstRow", false, false, true, true));
        lazyActionMap.put(new Actions("selectLastRow", false, true, true, true));
        lazyActionMap.put(new Actions("selectFirstRowExtendSelection", true, false, true, true));
        lazyActionMap.put(new Actions("selectLastRowExtendSelection", true, true, true, true));
        lazyActionMap.put(new Actions("selectNextColumnCell", 1, 0, false, true));
        lazyActionMap.put(new Actions("selectPreviousColumnCell", -1, 0, false, true));
        lazyActionMap.put(new Actions("selectNextRowCell", 0, 1, false, true));
        lazyActionMap.put(new Actions("selectPreviousRowCell", 0, -1, false, true));
        lazyActionMap.put(new Actions("selectAll"));
        lazyActionMap.put(new Actions("clearSelection"));
        lazyActionMap.put(new Actions("cancel"));
        lazyActionMap.put(new Actions("startEditing"));
        lazyActionMap.put(TransferHandler.getCutAction().getValue("Name"), TransferHandler.getCutAction());
        lazyActionMap.put(TransferHandler.getCopyAction().getValue("Name"), TransferHandler.getCopyAction());
        lazyActionMap.put(TransferHandler.getPasteAction().getValue("Name"), TransferHandler.getPasteAction());
        lazyActionMap.put(new Actions("scrollLeftChangeSelection", false, false, false, false));
        lazyActionMap.put(new Actions("scrollRightChangeSelection", false, true, false, false));
        lazyActionMap.put(new Actions("scrollLeftExtendSelection", true, false, false, false));
        lazyActionMap.put(new Actions("scrollRightExtendSelection", true, true, false, false));
        lazyActionMap.put(new Actions("addToSelection"));
        lazyActionMap.put(new Actions("toggleAndAnchor"));
        lazyActionMap.put(new Actions("extendTo"));
        lazyActionMap.put(new Actions("moveSelectionTo"));
        lazyActionMap.put(new Actions("focusHeader"));
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults();
        uninstallListeners();
        uninstallKeyboardActions();
        this.table.remove(this.rendererPane);
        this.rendererPane = null;
        this.table = null;
    }

    protected void uninstallDefaults() {
        if (this.table.getTransferHandler() instanceof UIResource) {
            this.table.setTransferHandler(null);
        }
    }

    protected void uninstallListeners() {
        this.table.removeFocusListener(this.focusListener);
        this.table.removeKeyListener(this.keyListener);
        this.table.removeMouseListener(this.mouseInputListener);
        this.table.removeMouseMotionListener(this.mouseInputListener);
        this.table.removePropertyChangeListener(getHandler());
        if (this.isFileList) {
            this.table.getSelectionModel().removeListSelectionListener(getHandler());
        }
        this.focusListener = null;
        this.keyListener = null;
        this.mouseInputListener = null;
        this.handler = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.table, 1, null);
        SwingUtilities.replaceUIActionMap(this.table, null);
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        UIDefaults lookAndFeelDefaults = UIManager.getLookAndFeelDefaults();
        Component tableCellRendererComponent = (Component) lookAndFeelDefaults.get(BASELINE_COMPONENT_KEY);
        if (tableCellRendererComponent == null) {
            tableCellRendererComponent = new DefaultTableCellRenderer().getTableCellRendererComponent(this.table, "a", false, false, -1, -1);
            lookAndFeelDefaults.put(BASELINE_COMPONENT_KEY, tableCellRendererComponent);
        }
        tableCellRendererComponent.setFont(this.table.getFont());
        int rowMargin = this.table.getRowMargin();
        return tableCellRendererComponent.getBaseline(Integer.MAX_VALUE, this.table.getRowHeight() - rowMargin) + (rowMargin / 2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
    }

    private Dimension createTableSize(long j2) {
        int i2 = 0;
        int rowCount = this.table.getRowCount();
        if (rowCount > 0 && this.table.getColumnCount() > 0) {
            Rectangle cellRect = this.table.getCellRect(rowCount - 1, 0, true);
            i2 = cellRect.f12373y + cellRect.height;
        }
        long jAbs = Math.abs(j2);
        if (jAbs > 2147483647L) {
            jAbs = 2147483647L;
        }
        return new Dimension((int) jAbs, i2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        long minWidth = 0;
        while (this.table.getColumnModel().getColumns().hasMoreElements()) {
            minWidth += r0.nextElement2().getMinWidth();
        }
        return createTableSize(minWidth);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        long preferredWidth = 0;
        while (this.table.getColumnModel().getColumns().hasMoreElements()) {
            preferredWidth += r0.nextElement2().getPreferredWidth();
        }
        return createTableSize(preferredWidth);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        long maxWidth = 0;
        while (this.table.getColumnModel().getColumns().hasMoreElements()) {
            maxWidth += r0.nextElement2().getMaxWidth();
        }
        return createTableSize(maxWidth);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Rectangle clipBounds = graphics.getClipBounds();
        Rectangle bounds = this.table.getBounds();
        bounds.f12373y = 0;
        bounds.f12372x = 0;
        if (this.table.getRowCount() <= 0 || this.table.getColumnCount() <= 0 || !bounds.intersects(clipBounds)) {
            paintDropLines(graphics);
            return;
        }
        boolean zIsLeftToRight = this.table.getComponentOrientation().isLeftToRight();
        Point location = clipBounds.getLocation();
        Point point = new Point((clipBounds.f12372x + clipBounds.width) - 1, (clipBounds.f12373y + clipBounds.height) - 1);
        int iRowAtPoint = this.table.rowAtPoint(location);
        int iRowAtPoint2 = this.table.rowAtPoint(point);
        if (iRowAtPoint == -1) {
            iRowAtPoint = 0;
        }
        if (iRowAtPoint2 == -1) {
            iRowAtPoint2 = this.table.getRowCount() - 1;
        }
        int iColumnAtPoint = this.table.columnAtPoint(zIsLeftToRight ? location : point);
        int iColumnAtPoint2 = this.table.columnAtPoint(zIsLeftToRight ? point : location);
        if (iColumnAtPoint == -1) {
            iColumnAtPoint = 0;
        }
        if (iColumnAtPoint2 == -1) {
            iColumnAtPoint2 = this.table.getColumnCount() - 1;
        }
        paintGrid(graphics, iRowAtPoint, iRowAtPoint2, iColumnAtPoint, iColumnAtPoint2);
        paintCells(graphics, iRowAtPoint, iRowAtPoint2, iColumnAtPoint, iColumnAtPoint2);
        paintDropLines(graphics);
    }

    private void paintDropLines(Graphics graphics) {
        JTable.DropLocation dropLocation = this.table.getDropLocation();
        if (dropLocation == null) {
            return;
        }
        Color color = UIManager.getColor("Table.dropLineColor");
        Color color2 = UIManager.getColor("Table.dropLineShortColor");
        if (color == null && color2 == null) {
            return;
        }
        Rectangle hDropLineRect = getHDropLineRect(dropLocation);
        if (hDropLineRect != null) {
            int i2 = hDropLineRect.f12372x;
            int i3 = hDropLineRect.width;
            if (color != null) {
                extendRect(hDropLineRect, true);
                graphics.setColor(color);
                graphics.fillRect(hDropLineRect.f12372x, hDropLineRect.f12373y, hDropLineRect.width, hDropLineRect.height);
            }
            if (!dropLocation.isInsertColumn() && color2 != null) {
                graphics.setColor(color2);
                graphics.fillRect(i2, hDropLineRect.f12373y, i3, hDropLineRect.height);
            }
        }
        Rectangle vDropLineRect = getVDropLineRect(dropLocation);
        if (vDropLineRect != null) {
            int i4 = vDropLineRect.f12373y;
            int i5 = vDropLineRect.height;
            if (color != null) {
                extendRect(vDropLineRect, false);
                graphics.setColor(color);
                graphics.fillRect(vDropLineRect.f12372x, vDropLineRect.f12373y, vDropLineRect.width, vDropLineRect.height);
            }
            if (!dropLocation.isInsertRow() && color2 != null) {
                graphics.setColor(color2);
                graphics.fillRect(vDropLineRect.f12372x, i4, vDropLineRect.width, i5);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle getHDropLineRect(JTable.DropLocation dropLocation) {
        if (!dropLocation.isInsertRow()) {
            return null;
        }
        int row = dropLocation.getRow();
        int column = dropLocation.getColumn();
        if (column >= this.table.getColumnCount()) {
            column--;
        }
        Rectangle cellRect = this.table.getCellRect(row, column, true);
        if (row >= this.table.getRowCount()) {
            Rectangle cellRect2 = this.table.getCellRect(row - 1, column, true);
            cellRect.f12373y = cellRect2.f12373y + cellRect2.height;
        }
        if (cellRect.f12373y == 0) {
            cellRect.f12373y = -1;
        } else {
            cellRect.f12373y -= 2;
        }
        cellRect.height = 3;
        return cellRect;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle getVDropLineRect(JTable.DropLocation dropLocation) {
        if (!dropLocation.isInsertColumn()) {
            return null;
        }
        boolean zIsLeftToRight = this.table.getComponentOrientation().isLeftToRight();
        int column = dropLocation.getColumn();
        Rectangle cellRect = this.table.getCellRect(dropLocation.getRow(), column, true);
        if (column >= this.table.getColumnCount()) {
            cellRect = this.table.getCellRect(dropLocation.getRow(), column - 1, true);
            if (zIsLeftToRight) {
                cellRect.f12372x += cellRect.width;
            }
        } else if (!zIsLeftToRight) {
            cellRect.f12372x += cellRect.width;
        }
        if (cellRect.f12372x == 0) {
            cellRect.f12372x = -1;
        } else {
            cellRect.f12372x -= 2;
        }
        cellRect.width = 3;
        return cellRect;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Rectangle extendRect(Rectangle rectangle, boolean z2) {
        if (rectangle == null) {
            return rectangle;
        }
        if (z2) {
            rectangle.f12372x = 0;
            rectangle.width = this.table.getWidth();
        } else {
            rectangle.f12373y = 0;
            if (this.table.getRowCount() != 0) {
                Rectangle cellRect = this.table.getCellRect(this.table.getRowCount() - 1, 0, true);
                rectangle.height = cellRect.f12373y + cellRect.height;
            } else {
                rectangle.height = this.table.getHeight();
            }
        }
        return rectangle;
    }

    private void paintGrid(Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.setColor(this.table.getGridColor());
        Rectangle rectangleUnion = this.table.getCellRect(i2, i4, true).union(this.table.getCellRect(i3, i5, true));
        if (this.table.getShowHorizontalLines()) {
            int i6 = rectangleUnion.f12372x + rectangleUnion.width;
            int rowHeight = rectangleUnion.f12373y;
            for (int i7 = i2; i7 <= i3; i7++) {
                rowHeight += this.table.getRowHeight(i7);
                graphics.drawLine(rectangleUnion.f12372x, rowHeight - 1, i6 - 1, rowHeight - 1);
            }
        }
        if (this.table.getShowVerticalLines()) {
            TableColumnModel columnModel = this.table.getColumnModel();
            int i8 = rectangleUnion.f12373y + rectangleUnion.height;
            if (this.table.getComponentOrientation().isLeftToRight()) {
                int width = rectangleUnion.f12372x;
                for (int i9 = i4; i9 <= i5; i9++) {
                    width += columnModel.getColumn(i9).getWidth();
                    graphics.drawLine(width - 1, 0, width - 1, i8 - 1);
                }
                return;
            }
            int width2 = rectangleUnion.f12372x;
            for (int i10 = i5; i10 >= i4; i10--) {
                width2 += columnModel.getColumn(i10).getWidth();
                graphics.drawLine(width2 - 1, 0, width2 - 1, i8 - 1);
            }
        }
    }

    private int viewIndexForColumn(TableColumn tableColumn) {
        TableColumnModel columnModel = this.table.getColumnModel();
        for (int i2 = 0; i2 < columnModel.getColumnCount(); i2++) {
            if (columnModel.getColumn(i2) == tableColumn) {
                return i2;
            }
        }
        return -1;
    }

    private void paintCells(Graphics graphics, int i2, int i3, int i4, int i5) {
        JTableHeader tableHeader = this.table.getTableHeader();
        TableColumn draggedColumn = tableHeader == null ? null : tableHeader.getDraggedColumn();
        TableColumnModel columnModel = this.table.getColumnModel();
        int columnMargin = columnModel.getColumnMargin();
        if (this.table.getComponentOrientation().isLeftToRight()) {
            for (int i6 = i2; i6 <= i3; i6++) {
                Rectangle cellRect = this.table.getCellRect(i6, i4, false);
                for (int i7 = i4; i7 <= i5; i7++) {
                    TableColumn column = columnModel.getColumn(i7);
                    int width = column.getWidth();
                    cellRect.width = width - columnMargin;
                    if (column != draggedColumn) {
                        paintCell(graphics, cellRect, i6, i7);
                    }
                    cellRect.f12372x += width;
                }
            }
        } else {
            for (int i8 = i2; i8 <= i3; i8++) {
                Rectangle cellRect2 = this.table.getCellRect(i8, i4, false);
                TableColumn column2 = columnModel.getColumn(i4);
                if (column2 != draggedColumn) {
                    cellRect2.width = column2.getWidth() - columnMargin;
                    paintCell(graphics, cellRect2, i8, i4);
                }
                for (int i9 = i4 + 1; i9 <= i5; i9++) {
                    TableColumn column3 = columnModel.getColumn(i9);
                    int width2 = column3.getWidth();
                    cellRect2.width = width2 - columnMargin;
                    cellRect2.f12372x -= width2;
                    if (column3 != draggedColumn) {
                        paintCell(graphics, cellRect2, i8, i9);
                    }
                }
            }
        }
        if (draggedColumn != null) {
            paintDraggedArea(graphics, i2, i3, draggedColumn, tableHeader.getDraggedDistance());
        }
        this.rendererPane.removeAll();
    }

    private void paintDraggedArea(Graphics graphics, int i2, int i3, TableColumn tableColumn, int i4) {
        int iViewIndexForColumn = viewIndexForColumn(tableColumn);
        Rectangle rectangleUnion = this.table.getCellRect(i2, iViewIndexForColumn, true).union(this.table.getCellRect(i3, iViewIndexForColumn, true));
        graphics.setColor(this.table.getParent().getBackground());
        graphics.fillRect(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
        rectangleUnion.f12372x += i4;
        graphics.setColor(this.table.getBackground());
        graphics.fillRect(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
        if (this.table.getShowVerticalLines()) {
            graphics.setColor(this.table.getGridColor());
            int i5 = rectangleUnion.f12372x;
            int i6 = rectangleUnion.f12373y;
            int i7 = (i5 + rectangleUnion.width) - 1;
            int i8 = (i6 + rectangleUnion.height) - 1;
            graphics.drawLine(i5 - 1, i6, i5 - 1, i8);
            graphics.drawLine(i7, i6, i7, i8);
        }
        for (int i9 = i2; i9 <= i3; i9++) {
            Rectangle cellRect = this.table.getCellRect(i9, iViewIndexForColumn, false);
            cellRect.f12372x += i4;
            paintCell(graphics, cellRect, i9, iViewIndexForColumn);
            if (this.table.getShowHorizontalLines()) {
                graphics.setColor(this.table.getGridColor());
                Rectangle cellRect2 = this.table.getCellRect(i9, iViewIndexForColumn, true);
                cellRect2.f12372x += i4;
                int i10 = cellRect2.f12372x;
                int i11 = cellRect2.f12373y;
                int i12 = (i10 + cellRect2.width) - 1;
                int i13 = (i11 + cellRect2.height) - 1;
                graphics.drawLine(i10, i13, i12, i13);
            }
        }
    }

    private void paintCell(Graphics graphics, Rectangle rectangle, int i2, int i3) {
        if (this.table.isEditing() && this.table.getEditingRow() == i2 && this.table.getEditingColumn() == i3) {
            Component editorComponent = this.table.getEditorComponent();
            editorComponent.setBounds(rectangle);
            editorComponent.validate();
        } else {
            this.rendererPane.paintComponent(graphics, this.table.prepareRenderer(this.table.getCellRenderer(i2, i3), i2, i3), this.table, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAdjustedLead(JTable jTable, boolean z2, ListSelectionModel listSelectionModel) {
        int leadSelectionIndex = listSelectionModel.getLeadSelectionIndex();
        if (leadSelectionIndex < (z2 ? jTable.getRowCount() : jTable.getColumnCount())) {
            return leadSelectionIndex;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getAdjustedLead(JTable jTable, boolean z2) {
        return z2 ? getAdjustedLead(jTable, z2, jTable.getSelectionModel()) : getAdjustedLead(jTable, z2, jTable.getColumnModel().getSelectionModel());
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableUI$TableTransferHandler.class */
    static class TableTransferHandler extends TransferHandler implements UIResource {
        TableTransferHandler() {
        }

        @Override // javax.swing.TransferHandler
        protected Transferable createTransferable(JComponent jComponent) {
            int[] selectedRows;
            int[] selectedColumns;
            if (jComponent instanceof JTable) {
                JTable jTable = (JTable) jComponent;
                if (!jTable.getRowSelectionAllowed() && !jTable.getColumnSelectionAllowed()) {
                    return null;
                }
                if (!jTable.getRowSelectionAllowed()) {
                    int rowCount = jTable.getRowCount();
                    selectedRows = new int[rowCount];
                    for (int i2 = 0; i2 < rowCount; i2++) {
                        selectedRows[i2] = i2;
                    }
                } else {
                    selectedRows = jTable.getSelectedRows();
                }
                if (!jTable.getColumnSelectionAllowed()) {
                    int columnCount = jTable.getColumnCount();
                    selectedColumns = new int[columnCount];
                    for (int i3 = 0; i3 < columnCount; i3++) {
                        selectedColumns[i3] = i3;
                    }
                } else {
                    selectedColumns = jTable.getSelectedColumns();
                }
                if (selectedRows == null || selectedColumns == null || selectedRows.length == 0 || selectedColumns.length == 0) {
                    return null;
                }
                StringBuffer stringBuffer = new StringBuffer();
                StringBuffer stringBuffer2 = new StringBuffer();
                stringBuffer2.append("<html>\n<body>\n<table>\n");
                for (int i4 : selectedRows) {
                    stringBuffer2.append("<tr>\n");
                    for (int i5 : selectedColumns) {
                        Object valueAt = jTable.getValueAt(i4, i5);
                        String string = valueAt == null ? "" : valueAt.toString();
                        stringBuffer.append(string + "\t");
                        stringBuffer2.append("  <td>" + string + "</td>\n");
                    }
                    stringBuffer.deleteCharAt(stringBuffer.length() - 1).append("\n");
                    stringBuffer2.append("</tr>\n");
                }
                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                stringBuffer2.append("</table>\n</body>\n</html>");
                return new BasicTransferable(stringBuffer.toString(), stringBuffer2.toString());
            }
            return null;
        }

        @Override // javax.swing.TransferHandler
        public int getSourceActions(JComponent jComponent) {
            return 1;
        }
    }
}
