package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import javax.swing.CellRendererPane;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import sun.swing.DefaultLookup;
import sun.swing.SwingUtilities2;
import sun.swing.UIAction;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableHeaderUI.class */
public class BasicTableHeaderUI extends TableHeaderUI {
    protected JTableHeader header;
    protected CellRendererPane rendererPane;
    protected MouseInputListener mouseInputListener;
    private int rolloverColumn = -1;
    private int selectedColumnIndex = 0;
    private static Cursor resizeCursor = Cursor.getPredefinedCursor(11);
    private static FocusListener focusListener = new FocusListener() { // from class: javax.swing.plaf.basic.BasicTableHeaderUI.1
        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            repaintHeader(focusEvent.getSource());
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            repaintHeader(focusEvent.getSource());
        }

        private void repaintHeader(Object obj) {
            if (obj instanceof JTableHeader) {
                JTableHeader jTableHeader = (JTableHeader) obj;
                BasicTableHeaderUI basicTableHeaderUI = (BasicTableHeaderUI) BasicLookAndFeel.getUIOfType(jTableHeader.getUI(), BasicTableHeaderUI.class);
                if (basicTableHeaderUI == null) {
                    return;
                }
                jTableHeader.repaint(jTableHeader.getHeaderRect(basicTableHeaderUI.getSelectedColumnIndex()));
            }
        }
    };

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableHeaderUI$MouseInputHandler.class */
    public class MouseInputHandler implements MouseInputListener {
        private int mouseXOffset;
        private Cursor otherCursor = BasicTableHeaderUI.resizeCursor;

        public MouseInputHandler() {
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
            JTable table;
            RowSorter<? extends TableModel> rowSorter;
            int iColumnAtPoint;
            if (BasicTableHeaderUI.this.header.isEnabled() && mouseEvent.getClickCount() % 2 == 1 && SwingUtilities.isLeftMouseButton(mouseEvent) && (table = BasicTableHeaderUI.this.header.getTable()) != null && (rowSorter = table.getRowSorter()) != null && (iColumnAtPoint = BasicTableHeaderUI.this.header.columnAtPoint(mouseEvent.getPoint())) != -1) {
                rowSorter.toggleSortOrder(table.convertColumnIndexToModel(iColumnAtPoint));
            }
        }

        private TableColumn getResizingColumn(Point point) {
            return getResizingColumn(point, BasicTableHeaderUI.this.header.columnAtPoint(point));
        }

        private TableColumn getResizingColumn(Point point, int i2) {
            int i3;
            if (i2 == -1) {
                return null;
            }
            Rectangle headerRect = BasicTableHeaderUI.this.header.getHeaderRect(i2);
            headerRect.grow(-3, 0);
            if (headerRect.contains(point)) {
                return null;
            }
            int i4 = headerRect.f12372x + (headerRect.width / 2);
            if (BasicTableHeaderUI.this.header.getComponentOrientation().isLeftToRight()) {
                i3 = point.f12370x < i4 ? i2 - 1 : i2;
            } else {
                i3 = point.f12370x < i4 ? i2 : i2 - 1;
            }
            if (i3 == -1) {
                return null;
            }
            return BasicTableHeaderUI.this.header.getColumnModel().getColumn(i3);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (!BasicTableHeaderUI.this.header.isEnabled()) {
                return;
            }
            BasicTableHeaderUI.this.header.setDraggedColumn(null);
            BasicTableHeaderUI.this.header.setResizingColumn(null);
            BasicTableHeaderUI.this.header.setDraggedDistance(0);
            Point point = mouseEvent.getPoint();
            TableColumnModel columnModel = BasicTableHeaderUI.this.header.getColumnModel();
            int iColumnAtPoint = BasicTableHeaderUI.this.header.columnAtPoint(point);
            if (iColumnAtPoint != -1) {
                TableColumn resizingColumn = getResizingColumn(point, iColumnAtPoint);
                if (BasicTableHeaderUI.canResize(resizingColumn, BasicTableHeaderUI.this.header)) {
                    BasicTableHeaderUI.this.header.setResizingColumn(resizingColumn);
                    if (BasicTableHeaderUI.this.header.getComponentOrientation().isLeftToRight()) {
                        this.mouseXOffset = point.f12370x - resizingColumn.getWidth();
                    } else {
                        this.mouseXOffset = point.f12370x + resizingColumn.getWidth();
                    }
                } else if (BasicTableHeaderUI.this.header.getReorderingAllowed()) {
                    BasicTableHeaderUI.this.header.setDraggedColumn(columnModel.getColumn(iColumnAtPoint));
                    this.mouseXOffset = point.f12370x;
                }
            }
            if (BasicTableHeaderUI.this.header.getReorderingAllowed()) {
                int i2 = BasicTableHeaderUI.this.rolloverColumn;
                BasicTableHeaderUI.this.rolloverColumn = -1;
                BasicTableHeaderUI.this.rolloverColumnUpdated(i2, BasicTableHeaderUI.this.rolloverColumn);
            }
        }

        private void swapCursor() {
            Cursor cursor = BasicTableHeaderUI.this.header.getCursor();
            BasicTableHeaderUI.this.header.setCursor(this.otherCursor);
            this.otherCursor = cursor;
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseMoved(MouseEvent mouseEvent) {
            if (BasicTableHeaderUI.this.header.isEnabled()) {
                if (BasicTableHeaderUI.canResize(getResizingColumn(mouseEvent.getPoint()), BasicTableHeaderUI.this.header) != (BasicTableHeaderUI.this.header.getCursor() == BasicTableHeaderUI.resizeCursor)) {
                    swapCursor();
                }
                BasicTableHeaderUI.this.updateRolloverColumn(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseMotionListener
        public void mouseDragged(MouseEvent mouseEvent) {
            int i2;
            if (!BasicTableHeaderUI.this.header.isEnabled()) {
                return;
            }
            int x2 = mouseEvent.getX();
            TableColumn resizingColumn = BasicTableHeaderUI.this.header.getResizingColumn();
            TableColumn draggedColumn = BasicTableHeaderUI.this.header.getDraggedColumn();
            boolean zIsLeftToRight = BasicTableHeaderUI.this.header.getComponentOrientation().isLeftToRight();
            if (resizingColumn != null) {
                int width = resizingColumn.getWidth();
                if (zIsLeftToRight) {
                    i2 = x2 - this.mouseXOffset;
                } else {
                    i2 = this.mouseXOffset - x2;
                }
                this.mouseXOffset += BasicTableHeaderUI.this.changeColumnWidth(resizingColumn, BasicTableHeaderUI.this.header, width, i2);
            } else if (draggedColumn != null) {
                TableColumnModel columnModel = BasicTableHeaderUI.this.header.getColumnModel();
                int i3 = x2 - this.mouseXOffset;
                int i4 = i3 < 0 ? -1 : 1;
                int iViewIndexForColumn = BasicTableHeaderUI.this.viewIndexForColumn(draggedColumn);
                int i5 = iViewIndexForColumn + (zIsLeftToRight ? i4 : -i4);
                if (0 <= i5 && i5 < columnModel.getColumnCount()) {
                    int width2 = columnModel.getColumn(i5).getWidth();
                    if (Math.abs(i3) > width2 / 2) {
                        this.mouseXOffset += i4 * width2;
                        BasicTableHeaderUI.this.header.setDraggedDistance(i3 - (i4 * width2));
                        int iConvertColumnIndexToModel = SwingUtilities2.convertColumnIndexToModel(BasicTableHeaderUI.this.header.getColumnModel(), BasicTableHeaderUI.this.getSelectedColumnIndex());
                        columnModel.moveColumn(iViewIndexForColumn, i5);
                        BasicTableHeaderUI.this.selectColumn(SwingUtilities2.convertColumnIndexToView(BasicTableHeaderUI.this.header.getColumnModel(), iConvertColumnIndexToModel), false);
                        return;
                    }
                }
                setDraggedDistance(i3, iViewIndexForColumn);
            }
            BasicTableHeaderUI.this.updateRolloverColumn(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            if (!BasicTableHeaderUI.this.header.isEnabled()) {
                return;
            }
            setDraggedDistance(0, BasicTableHeaderUI.this.viewIndexForColumn(BasicTableHeaderUI.this.header.getDraggedColumn()));
            BasicTableHeaderUI.this.header.setResizingColumn(null);
            BasicTableHeaderUI.this.header.setDraggedColumn(null);
            BasicTableHeaderUI.this.updateRolloverColumn(mouseEvent);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            if (BasicTableHeaderUI.this.header.isEnabled()) {
                BasicTableHeaderUI.this.updateRolloverColumn(mouseEvent);
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (BasicTableHeaderUI.this.header.isEnabled()) {
                int i2 = BasicTableHeaderUI.this.rolloverColumn;
                BasicTableHeaderUI.this.rolloverColumn = -1;
                BasicTableHeaderUI.this.rolloverColumnUpdated(i2, BasicTableHeaderUI.this.rolloverColumn);
            }
        }

        private void setDraggedDistance(int i2, int i3) {
            BasicTableHeaderUI.this.header.setDraggedDistance(i2);
            if (i3 != -1) {
                BasicTableHeaderUI.this.header.getColumnModel().moveColumn(i3, i3);
            }
        }
    }

    protected MouseInputListener createMouseInputListener() {
        return new MouseInputHandler();
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicTableHeaderUI();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.header = (JTableHeader) jComponent;
        this.rendererPane = new CellRendererPane();
        this.header.add(this.rendererPane);
        installDefaults();
        installListeners();
        installKeyboardActions();
    }

    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(this.header, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
        LookAndFeel.installProperty(this.header, "opaque", Boolean.TRUE);
    }

    protected void installListeners() {
        this.mouseInputListener = createMouseInputListener();
        this.header.addMouseListener(this.mouseInputListener);
        this.header.addMouseMotionListener(this.mouseInputListener);
        this.header.addFocusListener(focusListener);
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.header, 1, (InputMap) DefaultLookup.get(this.header, this, "TableHeader.ancestorInputMap"));
        LazyActionMap.installLazyActionMap(this.header, BasicTableHeaderUI.class, "TableHeader.actionMap");
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults();
        uninstallListeners();
        uninstallKeyboardActions();
        this.header.remove(this.rendererPane);
        this.rendererPane = null;
        this.header = null;
    }

    protected void uninstallDefaults() {
    }

    protected void uninstallListeners() {
        this.header.removeMouseListener(this.mouseInputListener);
        this.header.removeMouseMotionListener(this.mouseInputListener);
        this.mouseInputListener = null;
    }

    protected void uninstallKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.header, 0, null);
        SwingUtilities.replaceUIActionMap(this.header, null);
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put(new Actions(Actions.TOGGLE_SORT_ORDER));
        lazyActionMap.put(new Actions(Actions.SELECT_COLUMN_TO_LEFT));
        lazyActionMap.put(new Actions(Actions.SELECT_COLUMN_TO_RIGHT));
        lazyActionMap.put(new Actions(Actions.MOVE_COLUMN_LEFT));
        lazyActionMap.put(new Actions(Actions.MOVE_COLUMN_RIGHT));
        lazyActionMap.put(new Actions(Actions.RESIZE_LEFT));
        lazyActionMap.put(new Actions(Actions.RESIZE_RIGHT));
        lazyActionMap.put(new Actions(Actions.FOCUS_TABLE));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public int getRolloverColumn() {
        return this.rolloverColumn;
    }

    protected void rolloverColumnUpdated(int i2, int i3) {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateRolloverColumn(MouseEvent mouseEvent) {
        int iColumnAtPoint;
        if (this.header.getDraggedColumn() == null && this.header.contains(mouseEvent.getPoint()) && (iColumnAtPoint = this.header.columnAtPoint(mouseEvent.getPoint())) != this.rolloverColumn) {
            int i2 = this.rolloverColumn;
            this.rolloverColumn = iColumnAtPoint;
            rolloverColumnUpdated(i2, this.rolloverColumn);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int selectNextColumn(boolean z2) {
        int selectedColumnIndex = getSelectedColumnIndex();
        if (selectedColumnIndex < this.header.getColumnModel().getColumnCount() - 1) {
            selectedColumnIndex++;
            if (z2) {
                selectColumn(selectedColumnIndex);
            }
        }
        return selectedColumnIndex;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int selectPreviousColumn(boolean z2) {
        int selectedColumnIndex = getSelectedColumnIndex();
        if (selectedColumnIndex > 0) {
            selectedColumnIndex--;
            if (z2) {
                selectColumn(selectedColumnIndex);
            }
        }
        return selectedColumnIndex;
    }

    void selectColumn(int i2) {
        selectColumn(i2, true);
    }

    void selectColumn(int i2, boolean z2) {
        this.header.repaint(this.header.getHeaderRect(this.selectedColumnIndex));
        this.selectedColumnIndex = i2;
        this.header.repaint(this.header.getHeaderRect(i2));
        if (z2) {
            scrollToColumn(i2);
        }
    }

    private void scrollToColumn(int i2) {
        Container parent;
        JTable table;
        if (this.header.getParent() == null || (parent = this.header.getParent().getParent()) == null || !(parent instanceof JScrollPane) || (table = this.header.getTable()) == null) {
            return;
        }
        Rectangle visibleRect = table.getVisibleRect();
        Rectangle cellRect = table.getCellRect(0, i2, true);
        visibleRect.f12372x = cellRect.f12372x;
        visibleRect.width = cellRect.width;
        table.scrollRectToVisible(visibleRect);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int getSelectedColumnIndex() {
        int columnCount = this.header.getColumnModel().getColumnCount();
        if (this.selectedColumnIndex >= columnCount && columnCount > 0) {
            this.selectedColumnIndex = columnCount - 1;
        }
        return this.selectedColumnIndex;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean canResize(TableColumn tableColumn, JTableHeader jTableHeader) {
        return tableColumn != null && jTableHeader.getResizingAllowed() && tableColumn.getResizable();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int changeColumnWidth(TableColumn tableColumn, JTableHeader jTableHeader, int i2, int i3) {
        Container parent;
        JTable table;
        tableColumn.setWidth(i3);
        if (jTableHeader.getParent() != null && (parent = jTableHeader.getParent().getParent()) != null && (parent instanceof JScrollPane) && (table = jTableHeader.getTable()) != null && !parent.getComponentOrientation().isLeftToRight() && !jTableHeader.getComponentOrientation().isLeftToRight()) {
            JViewport viewport = ((JScrollPane) parent).getViewport();
            int width = viewport.getWidth();
            int i4 = i3 - i2;
            int width2 = table.getWidth() + i4;
            Dimension size = table.getSize();
            size.width += i4;
            table.setSize(size);
            if (width2 >= width && table.getAutoResizeMode() == 0) {
                Point viewPosition = viewport.getViewPosition();
                viewPosition.f12370x = Math.max(0, Math.min(width2 - width, viewPosition.f12370x + i4));
                viewport.setViewPosition(viewPosition);
                return i4;
            }
            return 0;
        }
        return 0;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        int i4 = -1;
        TableColumnModel columnModel = this.header.getColumnModel();
        int i5 = 0;
        while (true) {
            if (i5 >= columnModel.getColumnCount()) {
                break;
            }
            columnModel.getColumn(i5);
            Component headerRenderer = getHeaderRenderer(i5);
            int baseline = headerRenderer.getBaseline(headerRenderer.getPreferredSize().width, i3);
            if (baseline >= 0) {
                if (i4 == -1) {
                    i4 = baseline;
                } else if (i4 != baseline) {
                    i4 = -1;
                    break;
                }
            }
            i5++;
        }
        return i4;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (this.header.getColumnModel().getColumnCount() <= 0) {
            return;
        }
        boolean zIsLeftToRight = this.header.getComponentOrientation().isLeftToRight();
        Rectangle clipBounds = graphics.getClipBounds();
        Point location = clipBounds.getLocation();
        Point point = new Point((clipBounds.f12372x + clipBounds.width) - 1, clipBounds.f12373y);
        TableColumnModel columnModel = this.header.getColumnModel();
        int iColumnAtPoint = this.header.columnAtPoint(zIsLeftToRight ? location : point);
        int iColumnAtPoint2 = this.header.columnAtPoint(zIsLeftToRight ? point : location);
        if (iColumnAtPoint == -1) {
            iColumnAtPoint = 0;
        }
        if (iColumnAtPoint2 == -1) {
            iColumnAtPoint2 = columnModel.getColumnCount() - 1;
        }
        TableColumn draggedColumn = this.header.getDraggedColumn();
        Rectangle headerRect = this.header.getHeaderRect(zIsLeftToRight ? iColumnAtPoint : iColumnAtPoint2);
        if (zIsLeftToRight) {
            for (int i2 = iColumnAtPoint; i2 <= iColumnAtPoint2; i2++) {
                TableColumn column = columnModel.getColumn(i2);
                int width = column.getWidth();
                headerRect.width = width;
                if (column != draggedColumn) {
                    paintCell(graphics, headerRect, i2);
                }
                headerRect.f12372x += width;
            }
        } else {
            for (int i3 = iColumnAtPoint2; i3 >= iColumnAtPoint; i3--) {
                TableColumn column2 = columnModel.getColumn(i3);
                int width2 = column2.getWidth();
                headerRect.width = width2;
                if (column2 != draggedColumn) {
                    paintCell(graphics, headerRect, i3);
                }
                headerRect.f12372x += width2;
            }
        }
        if (draggedColumn != null) {
            int iViewIndexForColumn = viewIndexForColumn(draggedColumn);
            Rectangle headerRect2 = this.header.getHeaderRect(iViewIndexForColumn);
            graphics.setColor(this.header.getParent().getBackground());
            graphics.fillRect(headerRect2.f12372x, headerRect2.f12373y, headerRect2.width, headerRect2.height);
            headerRect2.f12372x += this.header.getDraggedDistance();
            graphics.setColor(this.header.getBackground());
            graphics.fillRect(headerRect2.f12372x, headerRect2.f12373y, headerRect2.width, headerRect2.height);
            paintCell(graphics, headerRect2, iViewIndexForColumn);
        }
        this.rendererPane.removeAll();
    }

    private Component getHeaderRenderer(int i2) {
        TableColumn column = this.header.getColumnModel().getColumn(i2);
        TableCellRenderer headerRenderer = column.getHeaderRenderer();
        if (headerRenderer == null) {
            headerRenderer = this.header.getDefaultRenderer();
        }
        return headerRenderer.getTableCellRendererComponent(this.header.getTable(), column.getHeaderValue(), false, !this.header.isPaintingForPrint() && i2 == getSelectedColumnIndex() && this.header.hasFocus(), -1, i2);
    }

    private void paintCell(Graphics graphics, Rectangle rectangle, int i2) {
        this.rendererPane.paintComponent(graphics, getHeaderRenderer(i2), this.header, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, true);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int viewIndexForColumn(TableColumn tableColumn) {
        TableColumnModel columnModel = this.header.getColumnModel();
        for (int i2 = 0; i2 < columnModel.getColumnCount(); i2++) {
            if (columnModel.getColumn(i2) == tableColumn) {
                return i2;
            }
        }
        return -1;
    }

    private int getHeaderHeight() {
        Object headerValue;
        String string;
        int iMax = 0;
        boolean z2 = false;
        TableColumnModel columnModel = this.header.getColumnModel();
        for (int i2 = 0; i2 < columnModel.getColumnCount(); i2++) {
            TableColumn column = columnModel.getColumn(i2);
            boolean z3 = column.getHeaderRenderer() == null;
            if (!z3 || !z2) {
                int i3 = getHeaderRenderer(i2).getPreferredSize().height;
                iMax = Math.max(iMax, i3);
                if (z3 && i3 > 0 && (headerValue = column.getHeaderValue()) != null && (string = headerValue.toString()) != null && !string.equals("")) {
                    z2 = true;
                }
            }
        }
        return iMax;
    }

    private Dimension createHeaderSize(long j2) {
        if (j2 > 2147483647L) {
            j2 = 2147483647L;
        }
        return new Dimension((int) j2, getHeaderHeight());
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        long minWidth = 0;
        while (this.header.getColumnModel().getColumns().hasMoreElements()) {
            minWidth += r0.nextElement2().getMinWidth();
        }
        return createHeaderSize(minWidth);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        long preferredWidth = 0;
        while (this.header.getColumnModel().getColumns().hasMoreElements()) {
            preferredWidth += r0.nextElement2().getPreferredWidth();
        }
        return createHeaderSize(preferredWidth);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        long maxWidth = 0;
        while (this.header.getColumnModel().getColumns().hasMoreElements()) {
            maxWidth += r0.nextElement2().getMaxWidth();
        }
        return createHeaderSize(maxWidth);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicTableHeaderUI$Actions.class */
    private static class Actions extends UIAction {
        public static final String TOGGLE_SORT_ORDER = "toggleSortOrder";
        public static final String SELECT_COLUMN_TO_LEFT = "selectColumnToLeft";
        public static final String SELECT_COLUMN_TO_RIGHT = "selectColumnToRight";
        public static final String MOVE_COLUMN_LEFT = "moveColumnLeft";
        public static final String MOVE_COLUMN_RIGHT = "moveColumnRight";
        public static final String RESIZE_LEFT = "resizeLeft";
        public static final String RESIZE_RIGHT = "resizeRight";
        public static final String FOCUS_TABLE = "focusTable";

        public Actions(String str) {
            super(str);
        }

        @Override // sun.swing.UIAction
        public boolean isEnabled(Object obj) {
            if (obj instanceof JTableHeader) {
                JTableHeader jTableHeader = (JTableHeader) obj;
                TableColumnModel columnModel = jTableHeader.getColumnModel();
                if (columnModel.getColumnCount() <= 0) {
                    return false;
                }
                String name = getName();
                BasicTableHeaderUI basicTableHeaderUI = (BasicTableHeaderUI) BasicLookAndFeel.getUIOfType(jTableHeader.getUI(), BasicTableHeaderUI.class);
                if (basicTableHeaderUI != null) {
                    if (name == MOVE_COLUMN_LEFT) {
                        return jTableHeader.getReorderingAllowed() && maybeMoveColumn(true, jTableHeader, basicTableHeaderUI, false);
                    }
                    if (name == MOVE_COLUMN_RIGHT) {
                        return jTableHeader.getReorderingAllowed() && maybeMoveColumn(false, jTableHeader, basicTableHeaderUI, false);
                    }
                    if (name == RESIZE_LEFT || name == RESIZE_RIGHT) {
                        return BasicTableHeaderUI.canResize(columnModel.getColumn(basicTableHeaderUI.getSelectedColumnIndex()), jTableHeader);
                    }
                    return (name == FOCUS_TABLE && jTableHeader.getTable() == null) ? false : true;
                }
                return true;
            }
            return true;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            JTable table;
            JTableHeader jTableHeader = (JTableHeader) actionEvent.getSource();
            BasicTableHeaderUI basicTableHeaderUI = (BasicTableHeaderUI) BasicLookAndFeel.getUIOfType(jTableHeader.getUI(), BasicTableHeaderUI.class);
            if (basicTableHeaderUI == null) {
                return;
            }
            String name = getName();
            if (TOGGLE_SORT_ORDER == name) {
                JTable table2 = jTableHeader.getTable();
                RowSorter<? extends TableModel> rowSorter = table2 == null ? null : table2.getRowSorter();
                if (rowSorter != null) {
                    rowSorter.toggleSortOrder(table2.convertColumnIndexToModel(basicTableHeaderUI.getSelectedColumnIndex()));
                    return;
                }
                return;
            }
            if (SELECT_COLUMN_TO_LEFT == name) {
                if (jTableHeader.getComponentOrientation().isLeftToRight()) {
                    basicTableHeaderUI.selectPreviousColumn(true);
                    return;
                } else {
                    basicTableHeaderUI.selectNextColumn(true);
                    return;
                }
            }
            if (SELECT_COLUMN_TO_RIGHT == name) {
                if (jTableHeader.getComponentOrientation().isLeftToRight()) {
                    basicTableHeaderUI.selectNextColumn(true);
                    return;
                } else {
                    basicTableHeaderUI.selectPreviousColumn(true);
                    return;
                }
            }
            if (MOVE_COLUMN_LEFT == name) {
                moveColumn(true, jTableHeader, basicTableHeaderUI);
                return;
            }
            if (MOVE_COLUMN_RIGHT == name) {
                moveColumn(false, jTableHeader, basicTableHeaderUI);
                return;
            }
            if (RESIZE_LEFT == name) {
                resize(true, jTableHeader, basicTableHeaderUI);
                return;
            }
            if (RESIZE_RIGHT == name) {
                resize(false, jTableHeader, basicTableHeaderUI);
            } else if (FOCUS_TABLE == name && (table = jTableHeader.getTable()) != null) {
                table.requestFocusInWindow();
            }
        }

        private void moveColumn(boolean z2, JTableHeader jTableHeader, BasicTableHeaderUI basicTableHeaderUI) {
            maybeMoveColumn(z2, jTableHeader, basicTableHeaderUI, true);
        }

        private boolean maybeMoveColumn(boolean z2, JTableHeader jTableHeader, BasicTableHeaderUI basicTableHeaderUI, boolean z3) {
            int iSelectNextColumn;
            int selectedColumnIndex = basicTableHeaderUI.getSelectedColumnIndex();
            if (jTableHeader.getComponentOrientation().isLeftToRight()) {
                iSelectNextColumn = z2 ? basicTableHeaderUI.selectPreviousColumn(z3) : basicTableHeaderUI.selectNextColumn(z3);
            } else {
                iSelectNextColumn = z2 ? basicTableHeaderUI.selectNextColumn(z3) : basicTableHeaderUI.selectPreviousColumn(z3);
            }
            if (iSelectNextColumn != selectedColumnIndex) {
                if (z3) {
                    jTableHeader.getColumnModel().moveColumn(selectedColumnIndex, iSelectNextColumn);
                    return false;
                }
                return true;
            }
            return false;
        }

        private void resize(boolean z2, JTableHeader jTableHeader, BasicTableHeaderUI basicTableHeaderUI) {
            int i2;
            TableColumn column = jTableHeader.getColumnModel().getColumn(basicTableHeaderUI.getSelectedColumnIndex());
            jTableHeader.setResizingColumn(column);
            int width = column.getWidth();
            if (jTableHeader.getComponentOrientation().isLeftToRight()) {
                i2 = width + (z2 ? -1 : 1);
            } else {
                i2 = width + (z2 ? 1 : -1);
            }
            basicTableHeaderUI.changeColumnWidth(column, jTableHeader, width, i2);
        }
    }
}
