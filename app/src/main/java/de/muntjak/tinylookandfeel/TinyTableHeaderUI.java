package de.muntjak.tinylookandfeel;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import de.muntjak.tinylookandfeel.controlpanel.TinyTableModel;
import de.muntjak.tinylookandfeel.table.SortableTableData;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTableHeaderUI.class */
public class TinyTableHeaderUI extends BasicTableHeaderUI {
    private static final String ROLLOVER_COLUMN_KEY = "rolloverColumn";
    private static final String SORTED_COLUMN_KEY = "sortedColumn";
    private static final String SORTING_DIRECTION_KEY = "sortingDirection";
    private static final int ADD_COLUMN = 0;
    private static final int REMOVE_COLUMN = 1;
    private static final int REPLACE_COLUMN = 2;
    private static final int MINIMUM_DRAG_DISTANCE = 5;
    private static final HashMap sortingCache = new HashMap();
    private static final String DEFAULT_RENDERER_KEY = "defaultRenderer";
    protected SortableTableHandler handler;
    protected TinyTableHeaderRenderer headerRenderer;
    protected boolean rendererInstalled = false;
    static Class class$java$lang$Double;
    static Class class$javax$swing$Icon;
    static Class class$java$lang$Integer;
    static Class class$java$lang$String;

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTableHeaderUI$SortableTableHandler.class */
    private class SortableTableHandler implements MouseListener, MouseMotionListener, TableColumnModelListener {
        private int[] alignments;
        private int rolloverColumn;
        private int pressedColumn;
        private Vector sortedViewColumns;
        private Vector sortedModelColumns;
        private Vector sortingDirections;
        private boolean mouseInside;
        private boolean mouseDragged;
        private boolean inDrag;
        private Point pressedPoint;
        private final TinyTableHeaderUI this$0;

        private SortableTableHandler(TinyTableHeaderUI tinyTableHeaderUI) {
            this.this$0 = tinyTableHeaderUI;
            this.rolloverColumn = -1;
            this.pressedColumn = -1;
            this.sortedViewColumns = new Vector();
            this.sortedModelColumns = new Vector();
            this.sortingDirections = new Vector();
            this.mouseInside = false;
            this.mouseDragged = false;
            this.inDrag = false;
        }

        void sortColumns(int[] iArr, int[] iArr2, JTable jTable) {
            if (iArr == null) {
                throw new IllegalArgumentException("columns argument may not be null");
            }
            if (iArr2 == null) {
                throw new IllegalArgumentException("directions argument may not be null");
            }
            if (jTable == null) {
                throw new IllegalArgumentException("table argument may not be null");
            }
            if (iArr.length != iArr2.length) {
                throw new IllegalArgumentException("columns argument and directions argument must be of equal length");
            }
            JTableHeader tableHeader = jTable.getTableHeader();
            SortableTableData tableModel = getTableModel(tableHeader);
            if (tableModel == null) {
                return;
            }
            this.sortedViewColumns.clear();
            this.sortedModelColumns.clear();
            this.sortingDirections.clear();
            for (int i2 = 0; i2 < iArr.length; i2++) {
                this.sortedViewColumns.add(new Integer(iArr[i2]));
                this.sortedModelColumns.add(new Integer(getModelColumn(tableHeader, iArr[i2])));
                this.sortingDirections.add(new Integer(iArr2[i2]));
            }
            tableHeader.putClientProperty(TinyTableHeaderUI.SORTED_COLUMN_KEY, vectorToIntArray(this.sortedViewColumns));
            tableHeader.putClientProperty(TinyTableHeaderUI.SORTING_DIRECTION_KEY, vectorToIntArray(this.sortingDirections));
            tableModel.sortColumns(vectorToIntArray(this.sortedModelColumns), vectorToIntArray(this.sortingDirections), jTable);
            tableHeader.repaint();
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            SortableTableData tableModel;
            this.mouseInside = true;
            if (this.mouseDragged || (tableModel = getTableModel(mouseEvent.getSource())) == null) {
                return;
            }
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            int iColumnAtPoint = jTableHeader.columnAtPoint(mouseEvent.getPoint());
            if (tableModel.isColumnSortable(getModelColumnAt(mouseEvent))) {
                this.rolloverColumn = iColumnAtPoint;
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, new Integer(this.rolloverColumn));
            } else if (this.rolloverColumn != -1) {
                this.rolloverColumn = -1;
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, null);
            }
            jTableHeader.repaint();
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            this.mouseInside = false;
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            if ((this.inDrag && jTableHeader.getReorderingAllowed()) || getTableModel(mouseEvent.getSource()) == null || this.rolloverColumn == -1) {
                return;
            }
            this.rolloverColumn = -1;
            jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, null);
            jTableHeader.repaint();
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            this.inDrag = false;
            if (mouseEvent.isPopupTrigger()) {
                return;
            }
            if (!this.mouseInside) {
                this.mouseDragged = false;
                return;
            }
            SortableTableData tableModel = getTableModel(mouseEvent.getSource());
            if (tableModel == null) {
                this.mouseDragged = false;
                return;
            }
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            int iColumnAtPoint = jTableHeader.columnAtPoint(mouseEvent.getPoint());
            if (iColumnAtPoint == -1) {
                this.mouseDragged = false;
                return;
            }
            int modelColumnAt = getModelColumnAt(mouseEvent);
            if (tableModel.isColumnSortable(modelColumnAt)) {
                this.rolloverColumn = iColumnAtPoint;
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, new Integer(this.rolloverColumn));
            } else if (this.rolloverColumn != -1) {
                this.rolloverColumn = -1;
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, null);
            }
            if (this.mouseDragged) {
                this.mouseDragged = false;
                return;
            }
            if (tableModel.isColumnSortable(modelColumnAt) && this.pressedColumn == iColumnAtPoint) {
                Integer num = new Integer(iColumnAtPoint);
                if (this.sortedViewColumns.contains(num)) {
                    int iIndexOf = this.sortedViewColumns.indexOf(num);
                    if (mouseEvent.isAltDown()) {
                        this.sortedViewColumns.remove(iIndexOf);
                        this.sortedModelColumns.remove(iIndexOf);
                        this.sortingDirections.remove(iIndexOf);
                    } else if ((mouseEvent.isControlDown() && tableModel.supportsMultiColumnSort()) || this.sortedModelColumns.size() == 1) {
                        int i2 = ((Integer) this.sortingDirections.get(iIndexOf)).intValue() != 2 ? 2 : 1;
                        this.sortingDirections.remove(iIndexOf);
                        this.sortingDirections.add(iIndexOf, new Integer(i2));
                    } else {
                        int i3 = ((Integer) this.sortingDirections.get(iIndexOf)).intValue() != 2 ? 2 : 1;
                        this.sortedViewColumns.clear();
                        this.sortedModelColumns.clear();
                        this.sortingDirections.clear();
                        this.sortedViewColumns.add(num);
                        this.sortedModelColumns.add(new Integer(getModelColumn(mouseEvent, iColumnAtPoint)));
                        this.sortingDirections.add(new Integer(i3));
                    }
                } else {
                    if (mouseEvent.isAltDown()) {
                        return;
                    }
                    if (mouseEvent.isControlDown() && tableModel.supportsMultiColumnSort()) {
                        this.sortedViewColumns.add(num);
                        this.sortedModelColumns.add(new Integer(getModelColumn(mouseEvent, iColumnAtPoint)));
                        this.sortingDirections.add(new Integer(1));
                    } else {
                        this.sortedViewColumns.clear();
                        this.sortedModelColumns.clear();
                        this.sortingDirections.clear();
                        this.sortedViewColumns.add(num);
                        this.sortedModelColumns.add(new Integer(getModelColumn(mouseEvent, iColumnAtPoint)));
                        this.sortingDirections.add(new Integer(1));
                    }
                }
                jTableHeader.putClientProperty(TinyTableHeaderUI.SORTED_COLUMN_KEY, vectorToIntArray(this.sortedViewColumns));
                jTableHeader.putClientProperty(TinyTableHeaderUI.SORTING_DIRECTION_KEY, vectorToIntArray(this.sortingDirections));
                tableModel.sortColumns(vectorToIntArray(this.sortedModelColumns), vectorToIntArray(this.sortingDirections), jTableHeader.getTable());
                jTableHeader.repaint();
            }
        }

        private int[] vectorToIntArray(Vector vector) {
            int[] iArr = new int[vector.size()];
            for (int i2 = 0; i2 < iArr.length; i2++) {
                iArr[i2] = ((Integer) vector.get(i2)).intValue();
            }
            return iArr;
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (mouseEvent.isPopupTrigger()) {
                return;
            }
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            this.pressedPoint = mouseEvent.getPoint();
            this.pressedColumn = jTableHeader.columnAtPoint(this.pressedPoint);
            this.mouseDragged = false;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            int iColumnAtPoint;
            SortableTableData tableModel = getTableModel(mouseEvent.getSource());
            if (tableModel == null) {
                return;
            }
            this.inDrag = true;
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            if (jTableHeader.getResizingColumn() != null && !this.mouseDragged) {
                this.mouseDragged = true;
            }
            if (!jTableHeader.getReorderingAllowed() && !tableModel.isColumnSortable(getModelColumnAt(mouseEvent))) {
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, null);
                jTableHeader.repaint();
                return;
            }
            if (!this.mouseDragged && isMouseDragged(mouseEvent.getPoint(), this.pressedPoint)) {
                this.mouseDragged = true;
            }
            if (this.mouseInside) {
                if (!jTableHeader.getReorderingAllowed() && (iColumnAtPoint = jTableHeader.columnAtPoint(mouseEvent.getPoint())) != this.rolloverColumn) {
                    this.rolloverColumn = iColumnAtPoint;
                }
                if (this.rolloverColumn != -1) {
                    jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, new Integer(this.rolloverColumn));
                }
            } else {
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, null);
            }
            jTableHeader.repaint();
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            JTableHeader jTableHeader;
            int iColumnAtPoint;
            SortableTableData tableModel;
            if (!this.mouseInside || (iColumnAtPoint = (jTableHeader = (JTableHeader) mouseEvent.getSource()).columnAtPoint(mouseEvent.getPoint())) == -1 || (tableModel = getTableModel(mouseEvent.getSource())) == null) {
                return;
            }
            if (tableModel.isColumnSortable(getModelColumnAt(mouseEvent))) {
                if (iColumnAtPoint != this.rolloverColumn) {
                    this.rolloverColumn = iColumnAtPoint;
                    jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, new Integer(this.rolloverColumn));
                    jTableHeader.repaint();
                    return;
                }
                return;
            }
            if (this.rolloverColumn != -1) {
                this.rolloverColumn = -1;
                jTableHeader.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, null);
                jTableHeader.repaint();
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
            removeSorting();
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
            if (tableColumnModelEvent.getFromIndex() == tableColumnModelEvent.getToIndex() || this.this$0.header == null) {
                return;
            }
            if (this.rolloverColumn == tableColumnModelEvent.getFromIndex()) {
                this.rolloverColumn = tableColumnModelEvent.getToIndex();
                if (this.mouseInside) {
                    this.this$0.header.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, new Integer(this.rolloverColumn));
                }
            }
            int[] iArrVectorToIntArray = vectorToIntArray(this.sortedViewColumns);
            boolean z2 = false;
            for (int i2 = 0; i2 < iArrVectorToIntArray.length; i2++) {
                if (iArrVectorToIntArray[i2] == tableColumnModelEvent.getFromIndex()) {
                    iArrVectorToIntArray[i2] = tableColumnModelEvent.getToIndex();
                    z2 = true;
                } else if (iArrVectorToIntArray[i2] == tableColumnModelEvent.getToIndex()) {
                    iArrVectorToIntArray[i2] = tableColumnModelEvent.getFromIndex();
                    z2 = true;
                }
            }
            if (z2) {
                this.sortedViewColumns.clear();
                for (int i3 : iArrVectorToIntArray) {
                    this.sortedViewColumns.add(new Integer(i3));
                }
                this.this$0.header.putClientProperty(TinyTableHeaderUI.SORTED_COLUMN_KEY, vectorToIntArray(this.sortedViewColumns));
            }
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
            removeSorting();
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnMarginChanged(ChangeEvent changeEvent) {
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
        }

        private void removeSorting() {
            if (this.this$0.header == null) {
                return;
            }
            if (this.rolloverColumn != -1) {
                this.rolloverColumn = -1;
                this.this$0.header.putClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY, new Integer(this.rolloverColumn));
            }
            this.sortedModelColumns.clear();
            this.sortedViewColumns.clear();
            this.sortingDirections.clear();
            this.this$0.header.putClientProperty(TinyTableHeaderUI.SORTING_DIRECTION_KEY, null);
            this.this$0.header.putClientProperty(TinyTableHeaderUI.SORTED_COLUMN_KEY, null);
            this.this$0.header.repaint();
        }

        void removeSortingInformation() {
            SortableTableData tableModel;
            if (this.this$0.header == null || (tableModel = getTableModel(this.this$0.header)) == null) {
                return;
            }
            TinyTableHeaderUI.sortingCache.put(this.this$0.header, new SortingInformation(this.this$0, this.sortedViewColumns, this.sortedModelColumns, this.sortingDirections, this.alignments));
            tableModel.sortColumns(new int[0], new int[0], this.this$0.header.getTable());
            this.this$0.header.repaint();
        }

        void restoreSortingInformation(JTableHeader jTableHeader, SortingInformation sortingInformation) {
            SortableTableData tableModel;
            if (jTableHeader == null || (tableModel = getTableModel(jTableHeader)) == null) {
                return;
            }
            this.sortedViewColumns = sortingInformation.sortedViewColumns;
            this.sortedModelColumns = sortingInformation.sortedModelColumns;
            this.sortingDirections = sortingInformation.sortingDirections;
            this.alignments = sortingInformation.alignments;
            if (this.alignments != null && this.alignments.length > 0) {
                this.this$0.setHorizontalAlignments(this.alignments);
            }
            tableModel.sortColumns(vectorToIntArray(this.sortedModelColumns), vectorToIntArray(this.sortingDirections), jTableHeader.getTable());
            jTableHeader.repaint();
        }

        private SortableTableData getTableModel(Object obj) {
            return getTableModel((JTableHeader) obj);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public SortableTableData getTableModel(JTableHeader jTableHeader) {
            TableModel model;
            JTable table = jTableHeader.getTable();
            if (table == null || (model = table.getModel()) == null || !(model instanceof SortableTableData)) {
                return null;
            }
            if (!this.this$0.rendererInstalled) {
                this.this$0.rendererInstalled = true;
                if (((TableCellRenderer) jTableHeader.getClientProperty(TinyTableHeaderUI.DEFAULT_RENDERER_KEY)) == null) {
                    jTableHeader.putClientProperty(TinyTableHeaderUI.DEFAULT_RENDERER_KEY, jTableHeader.getDefaultRenderer());
                }
                if (this.this$0.headerRenderer == null) {
                    this.this$0.headerRenderer = new TinyTableHeaderRenderer();
                }
                jTableHeader.setDefaultRenderer(this.this$0.headerRenderer);
            }
            return (SortableTableData) model;
        }

        private int getModelColumnAt(MouseEvent mouseEvent) {
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            int iColumnAtPoint = jTableHeader.columnAtPoint(mouseEvent.getPoint());
            if (iColumnAtPoint == -1) {
                return -1;
            }
            return jTableHeader.getColumnModel().getColumn(iColumnAtPoint).getModelIndex();
        }

        private int getModelColumn(MouseEvent mouseEvent, int i2) {
            if (i2 == -1) {
                return -1;
            }
            return getModelColumn((JTableHeader) mouseEvent.getSource(), i2);
        }

        private int getModelColumn(JTableHeader jTableHeader, int i2) {
            return jTableHeader.getColumnModel().getColumn(i2).getModelIndex();
        }

        private boolean isMouseDragged(Point point, Point point2) {
            return Math.abs(point.f12370x - point2.f12370x) >= 5;
        }

        private void showHeaderPopup(MouseEvent mouseEvent) {
            TinyTableModel tableModel = getTableModel(mouseEvent.getSource());
            if (tableModel == null) {
                return;
            }
            JTableHeader jTableHeader = (JTableHeader) mouseEvent.getSource();
            int iColumnAtPoint = jTableHeader.columnAtPoint(mouseEvent.getPoint());
            JPopupMenu jPopupMenu = new JPopupMenu();
            JMenu jMenu = new JMenu("Add");
            JMenuItem jMenuItem = new JMenuItem("Double Column");
            jMenuItem.addActionListener(new ActionListener(this, tableModel, iColumnAtPoint) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.1
                private final SortableTableData val$model;
                private final int val$viewColumn;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                    this.val$viewColumn = iColumnAtPoint;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws Throwable {
                    Class clsClass$;
                    TinyTableModel tinyTableModel = this.val$model;
                    if (TinyTableHeaderUI.class$java$lang$Double == null) {
                        clsClass$ = TinyTableHeaderUI.class$(Constants.DOUBLE_CLASS);
                        TinyTableHeaderUI.class$java$lang$Double = clsClass$;
                    } else {
                        clsClass$ = TinyTableHeaderUI.class$java$lang$Double;
                    }
                    tinyTableModel.addColumn(clsClass$, this.val$viewColumn);
                }
            });
            jMenu.add(jMenuItem);
            JMenuItem jMenuItem2 = new JMenuItem("Icon Column");
            jMenuItem2.addActionListener(new ActionListener(this, tableModel, iColumnAtPoint) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.2
                private final SortableTableData val$model;
                private final int val$viewColumn;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                    this.val$viewColumn = iColumnAtPoint;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws Throwable {
                    Class clsClass$;
                    TinyTableModel tinyTableModel = this.val$model;
                    if (TinyTableHeaderUI.class$javax$swing$Icon == null) {
                        clsClass$ = TinyTableHeaderUI.class$("javax.swing.Icon");
                        TinyTableHeaderUI.class$javax$swing$Icon = clsClass$;
                    } else {
                        clsClass$ = TinyTableHeaderUI.class$javax$swing$Icon;
                    }
                    tinyTableModel.addColumn(clsClass$, this.val$viewColumn);
                }
            });
            jMenu.add(jMenuItem2);
            JMenuItem jMenuItem3 = new JMenuItem("Integer Column");
            jMenuItem3.addActionListener(new ActionListener(this, tableModel, iColumnAtPoint) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.3
                private final SortableTableData val$model;
                private final int val$viewColumn;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                    this.val$viewColumn = iColumnAtPoint;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws Throwable {
                    Class clsClass$;
                    TinyTableModel tinyTableModel = this.val$model;
                    if (TinyTableHeaderUI.class$java$lang$Integer == null) {
                        clsClass$ = TinyTableHeaderUI.class$(Constants.INTEGER_CLASS);
                        TinyTableHeaderUI.class$java$lang$Integer = clsClass$;
                    } else {
                        clsClass$ = TinyTableHeaderUI.class$java$lang$Integer;
                    }
                    tinyTableModel.addColumn(clsClass$, this.val$viewColumn);
                }
            });
            jMenu.add(jMenuItem3);
            JMenuItem jMenuItem4 = new JMenuItem("String Column");
            jMenuItem4.addActionListener(new ActionListener(this, tableModel, iColumnAtPoint) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.4
                private final SortableTableData val$model;
                private final int val$viewColumn;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                    this.val$viewColumn = iColumnAtPoint;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) throws Throwable {
                    Class clsClass$;
                    TinyTableModel tinyTableModel = this.val$model;
                    if (TinyTableHeaderUI.class$java$lang$String == null) {
                        clsClass$ = TinyTableHeaderUI.class$("java.lang.String");
                        TinyTableHeaderUI.class$java$lang$String = clsClass$;
                    } else {
                        clsClass$ = TinyTableHeaderUI.class$java$lang$String;
                    }
                    tinyTableModel.addColumn(clsClass$, this.val$viewColumn);
                }
            });
            jMenu.add(jMenuItem4);
            jPopupMenu.add((JMenuItem) jMenu);
            jPopupMenu.addSeparator();
            JMenuItem jMenuItem5 = new JMenuItem("Remove Column");
            jMenuItem5.addActionListener(new ActionListener(this, tableModel, iColumnAtPoint) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.5
                private final SortableTableData val$model;
                private final int val$viewColumn;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                    this.val$viewColumn = iColumnAtPoint;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) {
                    this.val$model.removeColumn(this.val$viewColumn);
                }
            });
            if (tableModel.getColumnCount() < 2) {
                jMenuItem5.setEnabled(false);
            }
            jPopupMenu.add(jMenuItem5);
            jPopupMenu.addSeparator();
            JMenuItem jMenuItem6 = new JMenuItem("Remove all Rows");
            jMenuItem6.addActionListener(new ActionListener(this, tableModel) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.6
                private final SortableTableData val$model;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) {
                    this.val$model.removeAllRows();
                }
            });
            if (tableModel.getRowCount() == 0) {
                jMenuItem6.setEnabled(false);
            }
            jPopupMenu.add(jMenuItem6);
            jPopupMenu.addSeparator();
            JMenuItem jMenuItem7 = new JMenuItem("Create new Data");
            jMenuItem7.addActionListener(new ActionListener(this, tableModel) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.SortableTableHandler.7
                private final SortableTableData val$model;
                private final SortableTableHandler this$1;

                {
                    this.this$1 = this;
                    this.val$model = tableModel;
                }

                @Override // java.awt.event.ActionListener
                public void actionPerformed(ActionEvent actionEvent) {
                    this.val$model.createNewData();
                }
            });
            if (tableModel.getRowCount() > 0) {
                jMenuItem7.setEnabled(false);
            }
            jPopupMenu.add(jMenuItem7);
            jPopupMenu.show(jTableHeader, mouseEvent.getX(), 0);
        }

        SortableTableHandler(TinyTableHeaderUI tinyTableHeaderUI, AnonymousClass1 anonymousClass1) {
            this(tinyTableHeaderUI);
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTableHeaderUI$SortingInformation.class */
    private class SortingInformation {
        private Vector sortedViewColumns;
        private Vector sortedModelColumns;
        private Vector sortingDirections;
        private int[] alignments;
        private final TinyTableHeaderUI this$0;

        SortingInformation(TinyTableHeaderUI tinyTableHeaderUI, Vector vector, Vector vector2, Vector vector3, int[] iArr) {
            this.this$0 = tinyTableHeaderUI;
            this.sortedViewColumns = vector;
            this.sortedModelColumns = vector2;
            this.sortingDirections = vector3;
            this.alignments = iArr;
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTableHeaderUI$TinyTableHeaderRenderer.class */
    static class TinyTableHeaderRenderer extends DefaultTableCellRenderer implements UIResource {
        private static final Icon arrowNo = new Arrow(true, -1);
        private static final Icon[][] arrows = new Icon[2][4];
        private int[] horizontalAlignments;

        /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyTableHeaderUI$TinyTableHeaderRenderer$Arrow.class */
        private static class Arrow implements Icon {
            private static final int height = 11;
            private boolean descending;
            private int priority;

            public Arrow(boolean z2, int i2) {
                this.descending = z2;
                this.priority = Math.min(3, i2);
            }

            @Override // javax.swing.Icon
            public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
                if (this.priority == -1) {
                    return;
                }
                int i4 = this.priority;
                int i5 = (6 + this.priority) / 2;
                graphics.translate(i2 + i4, i3 + i5);
                graphics.setColor(Theme.tableHeaderArrowColor.getColor());
                if (!this.descending) {
                    switch (this.priority) {
                        case 0:
                            graphics.drawLine(4, 0, 4, 0);
                            graphics.drawLine(3, 1, 5, 1);
                            graphics.drawLine(2, 2, 6, 2);
                            graphics.drawLine(1, 3, 7, 3);
                            graphics.drawLine(0, 4, 8, 4);
                            break;
                        case 1:
                            graphics.drawLine(3, 0, 3, 0);
                            graphics.drawLine(2, 1, 4, 1);
                            graphics.drawLine(1, 2, 5, 2);
                            graphics.drawLine(0, 3, 6, 3);
                            break;
                        case 2:
                            graphics.drawLine(2, 0, 2, 0);
                            graphics.drawLine(1, 1, 3, 1);
                            graphics.drawLine(0, 2, 4, 2);
                            break;
                        case 3:
                        default:
                            graphics.drawLine(1, 0, 1, 0);
                            graphics.drawLine(0, 1, 2, 1);
                            break;
                    }
                } else {
                    switch (this.priority) {
                        case 0:
                            graphics.drawLine(4, 4, 4, 4);
                            graphics.drawLine(3, 3, 5, 3);
                            graphics.drawLine(2, 2, 6, 2);
                            graphics.drawLine(1, 1, 7, 1);
                            graphics.drawLine(0, 0, 8, 0);
                            break;
                        case 1:
                            graphics.drawLine(3, 3, 3, 3);
                            graphics.drawLine(2, 2, 4, 2);
                            graphics.drawLine(1, 1, 5, 1);
                            graphics.drawLine(0, 0, 6, 0);
                            break;
                        case 2:
                            graphics.drawLine(2, 2, 2, 2);
                            graphics.drawLine(1, 1, 3, 1);
                            graphics.drawLine(0, 0, 4, 0);
                            break;
                        case 3:
                        default:
                            graphics.drawLine(1, 1, 1, 1);
                            graphics.drawLine(0, 0, 2, 0);
                            break;
                    }
                }
                graphics.translate(-(i2 + i4), -(i3 + i5));
            }

            @Override // javax.swing.Icon
            public int getIconWidth() {
                return 9;
            }

            @Override // javax.swing.Icon
            public int getIconHeight() {
                return 11;
            }
        }

        public TinyTableHeaderRenderer() {
            setHorizontalAlignment(0);
            setHorizontalTextPosition(2);
        }

        public void setHorizontalAlignments(int[] iArr) {
            this.horizontalAlignments = iArr;
        }

        @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            JTableHeader tableHeader;
            LookAndFeel lookAndFeel = UIManager.getLookAndFeel();
            if (lookAndFeel == null || !"TinyLookAndFeel".equals(lookAndFeel.getName())) {
                if (jTable != null && (tableHeader = jTable.getTableHeader()) != null) {
                    setBackground(tableHeader.getBackground());
                    setForeground(tableHeader.getForeground());
                    setFont(tableHeader.getFont());
                }
                setIcon(null);
                setText(obj == null ? "" : obj.toString());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                return this;
            }
            boolean z4 = false;
            Icon icon = arrowNo;
            int i4 = 1;
            int i5 = -1;
            boolean z5 = false;
            if (jTable != null) {
                JTableHeader tableHeader2 = jTable.getTableHeader();
                if (tableHeader2 != null) {
                    Object clientProperty = tableHeader2.getClientProperty(TinyTableHeaderUI.ROLLOVER_COLUMN_KEY);
                    if (clientProperty != null && ((Integer) clientProperty).intValue() == i3) {
                        z4 = true;
                    }
                    Object clientProperty2 = tableHeader2.getClientProperty(TinyTableHeaderUI.SORTED_COLUMN_KEY);
                    if (clientProperty2 != null) {
                        int[] iArr = (int[]) clientProperty2;
                        i5 = -1;
                        for (int i6 = 0; i6 < iArr.length; i6++) {
                            if (iArr[i6] == i3) {
                                i5 = i6;
                            }
                        }
                        if (i5 > -1) {
                            z5 = true;
                            Object clientProperty3 = tableHeader2.getClientProperty(TinyTableHeaderUI.SORTING_DIRECTION_KEY);
                            if (clientProperty3 != null) {
                                i4 = ((int[]) clientProperty3)[i5];
                            }
                        }
                    }
                    if (z4) {
                        setBackground(Theme.tableHeaderRolloverBackColor.getColor());
                    } else {
                        setBackground(tableHeader2.getBackground());
                    }
                    setForeground(tableHeader2.getForeground());
                    setFont(tableHeader2.getFont());
                }
                TableModel model = jTable.getModel();
                if (!(model instanceof SortableTableData)) {
                    icon = null;
                    setToolTipText(null);
                } else if (z5) {
                    int iMin = Math.min(3, i5);
                    if (i4 == 1) {
                        if (arrows[0][iMin] == null) {
                            arrows[0][iMin] = new Arrow(false, i5);
                        }
                        icon = arrows[0][iMin];
                    } else {
                        if (arrows[1][iMin] == null) {
                            arrows[1][iMin] = new Arrow(true, i5);
                        }
                        icon = arrows[1][iMin];
                    }
                } else if (i3 >= 0) {
                    if (!((SortableTableData) model).isColumnSortable(jTable.getColumnModel().getColumn(i3).getModelIndex())) {
                        icon = null;
                    }
                }
                if (i3 >= 0) {
                    int modelIndex = jTable.getColumnModel().getColumn(i3).getModelIndex();
                    if (this.horizontalAlignments != null && this.horizontalAlignments.length > modelIndex) {
                        setHorizontalAlignment(this.horizontalAlignments[modelIndex]);
                    }
                }
            }
            setIcon(icon);
            setText(obj == null ? "" : obj.toString());
            if (z4) {
                setBorder(UIManager.getBorder("TableHeader.cellRolloverBorder"));
            } else {
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            }
            return this;
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyTableHeaderUI();
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void installListeners() {
        super.installListeners();
        this.handler = new SortableTableHandler(this, null);
        this.header.addMouseListener(this.handler);
        this.header.addMouseMotionListener(this.handler);
        this.header.getColumnModel().addColumnModelListener(this.handler);
        if (this.header.getTable() != null) {
            this.handler.getTableModel(this.header);
            this.header.getTable().addPropertyChangeListener("model", new PropertyChangeListener(this) { // from class: de.muntjak.tinylookandfeel.TinyTableHeaderUI.1
                private final TinyTableHeaderUI this$0;

                {
                    this.this$0 = this;
                }

                @Override // java.beans.PropertyChangeListener
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    if (this.this$0.header == null) {
                        return;
                    }
                    TableModel tableModel = (TableModel) propertyChangeEvent.getNewValue();
                    if (this.this$0.rendererInstalled && (tableModel == null || !(tableModel instanceof SortableTableData))) {
                        this.this$0.rendererInstalled = false;
                        this.this$0.header.setDefaultRenderer((TableCellRenderer) this.this$0.header.getClientProperty(TinyTableHeaderUI.DEFAULT_RENDERER_KEY));
                    } else {
                        if (this.this$0.rendererInstalled || tableModel == null || !(tableModel instanceof SortableTableData)) {
                            return;
                        }
                        this.this$0.rendererInstalled = true;
                        if (((TableCellRenderer) this.this$0.header.getClientProperty(TinyTableHeaderUI.DEFAULT_RENDERER_KEY)) == null) {
                            this.this$0.header.putClientProperty(TinyTableHeaderUI.DEFAULT_RENDERER_KEY, this.this$0.header.getDefaultRenderer());
                        }
                        if (this.this$0.headerRenderer == null) {
                            this.this$0.headerRenderer = new TinyTableHeaderRenderer();
                        }
                        this.this$0.header.setDefaultRenderer(this.this$0.headerRenderer);
                    }
                }
            });
        }
        SortingInformation sortingInformation = (SortingInformation) sortingCache.get(this.header);
        if (sortingInformation != null) {
            this.handler.restoreSortingInformation(this.header, sortingInformation);
        }
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.handler.removeSortingInformation();
        this.header.removeMouseListener(this.handler);
        this.header.removeMouseMotionListener(this.handler);
        this.header.getColumnModel().removeColumnModelListener(this.handler);
    }

    @Override // javax.swing.plaf.basic.BasicTableHeaderUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        Dimension preferredSize = super.getPreferredSize(jComponent);
        preferredSize.height = Math.max(16, preferredSize.height);
        return preferredSize;
    }

    public void sortColumns(int[] iArr, int[] iArr2, JTable jTable) {
        if (this.handler == null) {
            return;
        }
        this.handler.sortColumns(iArr, iArr2, jTable);
    }

    public void setHorizontalAlignments(int[] iArr) {
        if (this.headerRenderer == null || !this.rendererInstalled) {
            return;
        }
        if (this.handler != null) {
            if (iArr == null) {
                this.handler.alignments = null;
            } else {
                this.handler.alignments = new int[iArr.length];
                System.arraycopy(iArr, 0, this.handler.alignments, 0, iArr.length);
            }
        }
        this.headerRenderer.setHorizontalAlignments(iArr);
    }

    static Class class$(String str) throws Throwable {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            throw new NoClassDefFoundError().initCause(e2);
        }
    }
}
