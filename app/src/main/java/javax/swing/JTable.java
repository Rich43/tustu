package javax.swing;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterAbortException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Vector;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleExtendedTable;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleTable;
import javax.accessibility.AccessibleTableModelChange;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JComponent;
import javax.swing.TransferHandler;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableUI;
import javax.swing.plaf.UIResource;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import sun.awt.AWTAccessor;
import sun.reflect.misc.ReflectUtil;
import sun.swing.PrintingStatus;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/JTable.class */
public class JTable extends JComponent implements TableModelListener, Scrollable, TableColumnModelListener, ListSelectionListener, CellEditorListener, Accessible, RowSorterListener {
    private static final String uiClassID = "TableUI";
    public static final int AUTO_RESIZE_OFF = 0;
    public static final int AUTO_RESIZE_NEXT_COLUMN = 1;
    public static final int AUTO_RESIZE_SUBSEQUENT_COLUMNS = 2;
    public static final int AUTO_RESIZE_LAST_COLUMN = 3;
    public static final int AUTO_RESIZE_ALL_COLUMNS = 4;
    protected TableModel dataModel;
    protected TableColumnModel columnModel;
    protected ListSelectionModel selectionModel;
    protected JTableHeader tableHeader;
    protected int rowHeight;
    protected int rowMargin;
    protected Color gridColor;
    protected boolean showHorizontalLines;
    protected boolean showVerticalLines;
    protected int autoResizeMode;
    protected boolean autoCreateColumnsFromModel;
    protected Dimension preferredViewportSize;
    protected boolean rowSelectionAllowed;
    protected boolean cellSelectionEnabled;
    protected transient Component editorComp;
    protected transient TableCellEditor cellEditor;
    protected transient int editingColumn;
    protected transient int editingRow;
    protected transient Hashtable defaultRenderersByColumnClass;
    protected transient Hashtable defaultEditorsByColumnClass;
    protected Color selectionForeground;
    protected Color selectionBackground;
    private SizeSequence rowModel;
    private boolean dragEnabled;
    private boolean surrendersFocusOnKeystroke;
    private PropertyChangeListener editorRemover;
    private boolean columnSelectionAdjusting;
    private boolean rowSelectionAdjusting;
    private Throwable printError;
    private boolean isRowHeightSet;
    private boolean updateSelectionOnSort;
    private transient SortManager sortManager;
    private boolean ignoreSortChange;
    private boolean sorterChanged;
    private boolean autoCreateRowSorter;
    private boolean fillsViewportHeight;
    private DropMode dropMode;
    private transient DropLocation dropLocation;
    static final /* synthetic */ boolean $assertionsDisabled;

    /* loaded from: rt.jar:javax/swing/JTable$PrintMode.class */
    public enum PrintMode {
        NORMAL,
        FIT_WIDTH
    }

    /* loaded from: rt.jar:javax/swing/JTable$Resizable2.class */
    private interface Resizable2 {
        int getElementCount();

        int getLowerBoundAt(int i2);

        int getUpperBoundAt(int i2);

        void setSizeAt(int i2, int i3);
    }

    /* loaded from: rt.jar:javax/swing/JTable$Resizable3.class */
    private interface Resizable3 extends Resizable2 {
        int getMidPointAt(int i2);
    }

    static {
        $assertionsDisabled = !JTable.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:javax/swing/JTable$DropLocation.class */
    public static final class DropLocation extends TransferHandler.DropLocation {
        private final int row;
        private final int col;
        private final boolean isInsertRow;
        private final boolean isInsertCol;

        private DropLocation(Point point, int i2, int i3, boolean z2, boolean z3) {
            super(point);
            this.row = i2;
            this.col = i3;
            this.isInsertRow = z2;
            this.isInsertCol = z3;
        }

        public int getRow() {
            return this.row;
        }

        public int getColumn() {
            return this.col;
        }

        public boolean isInsertRow() {
            return this.isInsertRow;
        }

        public boolean isInsertColumn() {
            return this.isInsertCol;
        }

        @Override // javax.swing.TransferHandler.DropLocation
        public String toString() {
            return getClass().getName() + "[dropPoint=" + ((Object) getDropPoint()) + ",row=" + this.row + ",column=" + this.col + ",insertRow=" + this.isInsertRow + ",insertColumn=" + this.isInsertCol + "]";
        }
    }

    public JTable() {
        this(null, null, null);
    }

    public JTable(TableModel tableModel) {
        this(tableModel, null, null);
    }

    public JTable(TableModel tableModel, TableColumnModel tableColumnModel) {
        this(tableModel, tableColumnModel, null);
    }

    public JTable(TableModel tableModel, TableColumnModel tableColumnModel, ListSelectionModel listSelectionModel) {
        this.editorRemover = null;
        this.dropMode = DropMode.USE_SELECTION;
        setLayout(null);
        setFocusTraversalKeys(0, JComponent.getManagingFocusForwardTraversalKeys());
        setFocusTraversalKeys(1, JComponent.getManagingFocusBackwardTraversalKeys());
        if (tableColumnModel == null) {
            tableColumnModel = createDefaultColumnModel();
            this.autoCreateColumnsFromModel = true;
        }
        setColumnModel(tableColumnModel);
        setSelectionModel(listSelectionModel == null ? createDefaultSelectionModel() : listSelectionModel);
        setModel(tableModel == null ? createDefaultDataModel() : tableModel);
        initializeLocalVars();
        updateUI();
    }

    public JTable(int i2, int i3) {
        this(new DefaultTableModel(i2, i3));
    }

    public JTable(Vector vector, Vector vector2) {
        this(new DefaultTableModel(vector, vector2));
    }

    public JTable(final Object[][] objArr, final Object[] objArr2) {
        this(new AbstractTableModel() { // from class: javax.swing.JTable.1
            @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
            public String getColumnName(int i2) {
                return objArr2[i2].toString();
            }

            @Override // javax.swing.table.TableModel
            public int getRowCount() {
                return objArr.length;
            }

            @Override // javax.swing.table.TableModel
            public int getColumnCount() {
                return objArr2.length;
            }

            @Override // javax.swing.table.TableModel
            public Object getValueAt(int i2, int i3) {
                return objArr[i2][i3];
            }

            @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
            public boolean isCellEditable(int i2, int i3) {
                return true;
            }

            @Override // javax.swing.table.AbstractTableModel, javax.swing.table.TableModel
            public void setValueAt(Object obj, int i2, int i3) {
                objArr[i2][i3] = obj;
                fireTableCellUpdated(i2, i3);
            }
        });
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void addNotify() {
        super.addNotify();
        configureEnclosingScrollPane();
    }

    protected void configureEnclosingScrollPane() {
        JScrollPane jScrollPane;
        JViewport viewport;
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        if (unwrappedParent instanceof JViewport) {
            Container parent = ((JViewport) unwrappedParent).getParent();
            if (!(parent instanceof JScrollPane) || (viewport = (jScrollPane = (JScrollPane) parent).getViewport()) == null || SwingUtilities.getUnwrappedView(viewport) != this) {
                return;
            }
            jScrollPane.setColumnHeaderView(getTableHeader());
            configureEnclosingScrollPaneUI();
        }
    }

    private void configureEnclosingScrollPaneUI() {
        JScrollPane jScrollPane;
        JViewport viewport;
        Border border;
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        if (unwrappedParent instanceof JViewport) {
            Container parent = ((JViewport) unwrappedParent).getParent();
            if (!(parent instanceof JScrollPane) || (viewport = (jScrollPane = (JScrollPane) parent).getViewport()) == null || SwingUtilities.getUnwrappedView(viewport) != this) {
                return;
            }
            Border border2 = jScrollPane.getBorder();
            if ((border2 == null || (border2 instanceof UIResource)) && (border = UIManager.getBorder("Table.scrollPaneBorder")) != null) {
                jScrollPane.setBorder(border);
            }
            Component corner = jScrollPane.getCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER);
            if (corner == null || (corner instanceof UIResource)) {
                Component component = null;
                try {
                    component = (Component) UIManager.get("Table.scrollPaneCornerComponent");
                } catch (Exception e2) {
                }
                jScrollPane.setCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER, component);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void removeNotify() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner", this.editorRemover);
        this.editorRemover = null;
        unconfigureEnclosingScrollPane();
        super.removeNotify();
    }

    protected void unconfigureEnclosingScrollPane() {
        JScrollPane jScrollPane;
        JViewport viewport;
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        if (unwrappedParent instanceof JViewport) {
            Container parent = ((JViewport) unwrappedParent).getParent();
            if (!(parent instanceof JScrollPane) || (viewport = (jScrollPane = (JScrollPane) parent).getViewport()) == null || SwingUtilities.getUnwrappedView(viewport) != this) {
                return;
            }
            jScrollPane.setColumnHeaderView(null);
            if (jScrollPane.getCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER) instanceof UIResource) {
                jScrollPane.setCorner(ScrollPaneConstants.UPPER_TRAILING_CORNER, null);
            }
        }
    }

    @Override // javax.swing.JComponent
    void setUIProperty(String str, Object obj) {
        if (str == JTree.ROW_HEIGHT_PROPERTY) {
            if (!this.isRowHeightSet) {
                setRowHeight(((Number) obj).intValue());
                this.isRowHeightSet = false;
                return;
            }
            return;
        }
        super.setUIProperty(str, obj);
    }

    @Deprecated
    public static JScrollPane createScrollPaneForTable(JTable jTable) {
        return new JScrollPane(jTable);
    }

    public void setTableHeader(JTableHeader jTableHeader) {
        if (this.tableHeader != jTableHeader) {
            JTableHeader jTableHeader2 = this.tableHeader;
            if (jTableHeader2 != null) {
                jTableHeader2.setTable(null);
            }
            this.tableHeader = jTableHeader;
            if (jTableHeader != null) {
                jTableHeader.setTable(this);
            }
            firePropertyChange("tableHeader", jTableHeader2, jTableHeader);
        }
    }

    public JTableHeader getTableHeader() {
        return this.tableHeader;
    }

    public void setRowHeight(int i2) {
        if (i2 <= 0) {
            throw new IllegalArgumentException("New row height less than 1");
        }
        int i3 = this.rowHeight;
        this.rowHeight = i2;
        this.rowModel = null;
        if (this.sortManager != null) {
            this.sortManager.modelRowSizes = null;
        }
        this.isRowHeightSet = true;
        resizeAndRepaint();
        firePropertyChange(JTree.ROW_HEIGHT_PROPERTY, i3, i2);
    }

    public int getRowHeight() {
        return this.rowHeight;
    }

    private SizeSequence getRowModel() {
        if (this.rowModel == null) {
            this.rowModel = new SizeSequence(getRowCount(), getRowHeight());
        }
        return this.rowModel;
    }

    public void setRowHeight(int i2, int i3) {
        if (i3 <= 0) {
            throw new IllegalArgumentException("New row height less than 1");
        }
        getRowModel().setSize(i2, i3);
        if (this.sortManager != null) {
            this.sortManager.setViewRowHeight(i2, i3);
        }
        resizeAndRepaint();
    }

    public int getRowHeight(int i2) {
        return this.rowModel == null ? getRowHeight() : this.rowModel.getSize(i2);
    }

    public void setRowMargin(int i2) {
        int i3 = this.rowMargin;
        this.rowMargin = i2;
        resizeAndRepaint();
        firePropertyChange("rowMargin", i3, i2);
    }

    public int getRowMargin() {
        return this.rowMargin;
    }

    public void setIntercellSpacing(Dimension dimension) {
        setRowMargin(dimension.height);
        getColumnModel().setColumnMargin(dimension.width);
        resizeAndRepaint();
    }

    public Dimension getIntercellSpacing() {
        return new Dimension(getColumnModel().getColumnMargin(), this.rowMargin);
    }

    public void setGridColor(Color color) {
        if (color == null) {
            throw new IllegalArgumentException("New color is null");
        }
        Color color2 = this.gridColor;
        this.gridColor = color;
        firePropertyChange("gridColor", color2, color);
        repaint();
    }

    public Color getGridColor() {
        return this.gridColor;
    }

    public void setShowGrid(boolean z2) {
        setShowHorizontalLines(z2);
        setShowVerticalLines(z2);
        repaint();
    }

    public void setShowHorizontalLines(boolean z2) {
        boolean z3 = this.showHorizontalLines;
        this.showHorizontalLines = z2;
        firePropertyChange("showHorizontalLines", z3, z2);
        repaint();
    }

    public void setShowVerticalLines(boolean z2) {
        boolean z3 = this.showVerticalLines;
        this.showVerticalLines = z2;
        firePropertyChange("showVerticalLines", z3, z2);
        repaint();
    }

    public boolean getShowHorizontalLines() {
        return this.showHorizontalLines;
    }

    public boolean getShowVerticalLines() {
        return this.showVerticalLines;
    }

    public void setAutoResizeMode(int i2) {
        if (i2 == 0 || i2 == 1 || i2 == 2 || i2 == 3 || i2 == 4) {
            int i3 = this.autoResizeMode;
            this.autoResizeMode = i2;
            resizeAndRepaint();
            if (this.tableHeader != null) {
                this.tableHeader.resizeAndRepaint();
            }
            firePropertyChange("autoResizeMode", i3, this.autoResizeMode);
        }
    }

    public int getAutoResizeMode() {
        return this.autoResizeMode;
    }

    public void setAutoCreateColumnsFromModel(boolean z2) {
        if (this.autoCreateColumnsFromModel != z2) {
            boolean z3 = this.autoCreateColumnsFromModel;
            this.autoCreateColumnsFromModel = z2;
            if (z2) {
                createDefaultColumnsFromModel();
            }
            firePropertyChange("autoCreateColumnsFromModel", z3, z2);
        }
    }

    public boolean getAutoCreateColumnsFromModel() {
        return this.autoCreateColumnsFromModel;
    }

    public void createDefaultColumnsFromModel() {
        TableModel model = getModel();
        if (model != null) {
            TableColumnModel columnModel = getColumnModel();
            while (columnModel.getColumnCount() > 0) {
                columnModel.removeColumn(columnModel.getColumn(0));
            }
            for (int i2 = 0; i2 < model.getColumnCount(); i2++) {
                addColumn(new TableColumn(i2));
            }
        }
    }

    public void setDefaultRenderer(Class<?> cls, TableCellRenderer tableCellRenderer) {
        if (tableCellRenderer != null) {
            this.defaultRenderersByColumnClass.put(cls, tableCellRenderer);
        } else {
            this.defaultRenderersByColumnClass.remove(cls);
        }
    }

    public TableCellRenderer getDefaultRenderer(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        Object obj = this.defaultRenderersByColumnClass.get(cls);
        if (obj != null) {
            return (TableCellRenderer) obj;
        }
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass == null && cls != Object.class) {
            superclass = Object.class;
        }
        return getDefaultRenderer(superclass);
    }

    public void setDefaultEditor(Class<?> cls, TableCellEditor tableCellEditor) {
        if (tableCellEditor != null) {
            this.defaultEditorsByColumnClass.put(cls, tableCellEditor);
        } else {
            this.defaultEditorsByColumnClass.remove(cls);
        }
    }

    public TableCellEditor getDefaultEditor(Class<?> cls) {
        if (cls == null) {
            return null;
        }
        Object obj = this.defaultEditorsByColumnClass.get(cls);
        if (obj != null) {
            return (TableCellEditor) obj;
        }
        return getDefaultEditor(cls.getSuperclass());
    }

    public void setDragEnabled(boolean z2) {
        if (z2 && GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        }
        this.dragEnabled = z2;
    }

    public boolean getDragEnabled() {
        return this.dragEnabled;
    }

    public final void setDropMode(DropMode dropMode) {
        if (dropMode != null) {
            switch (dropMode) {
                case USE_SELECTION:
                case ON:
                case INSERT:
                case INSERT_ROWS:
                case INSERT_COLS:
                case ON_OR_INSERT:
                case ON_OR_INSERT_ROWS:
                case ON_OR_INSERT_COLS:
                    this.dropMode = dropMode;
                    return;
            }
        }
        throw new IllegalArgumentException(((Object) dropMode) + ": Unsupported drop mode for table");
    }

    public final DropMode getDropMode() {
        return this.dropMode;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // javax.swing.JComponent
    public DropLocation dropLocationForPoint(Point point) {
        DropLocation dropLocation = null;
        int iRowAtPoint = rowAtPoint(point);
        int iColumnAtPoint = columnAtPoint(point);
        boolean z2 = Boolean.TRUE == getClientProperty("Table.isFileList") && SwingUtilities2.pointOutsidePrefSize(this, iRowAtPoint, iColumnAtPoint, point);
        Rectangle cellRect = getCellRect(iRowAtPoint, iColumnAtPoint, true);
        boolean z3 = false;
        boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
        switch (this.dropMode) {
            case USE_SELECTION:
            case ON:
                if (iRowAtPoint == -1 || iColumnAtPoint == -1 || z2) {
                    dropLocation = new DropLocation(point, -1, -1, false, false);
                    break;
                } else {
                    dropLocation = new DropLocation(point, iRowAtPoint, iColumnAtPoint, false, false);
                    break;
                }
            case INSERT:
                if (iRowAtPoint == -1 && iColumnAtPoint == -1) {
                    dropLocation = new DropLocation(point, 0, 0, true, true);
                    break;
                } else {
                    SwingUtilities2.Section sectionLiesInHorizontal = SwingUtilities2.liesInHorizontal(cellRect, point, zIsLeftToRight, true);
                    if (iRowAtPoint == -1) {
                        if (sectionLiesInHorizontal == SwingUtilities2.Section.LEADING) {
                            dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint, true, true);
                            break;
                        } else if (sectionLiesInHorizontal == SwingUtilities2.Section.TRAILING) {
                            dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint + 1, true, true);
                            break;
                        } else {
                            dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint, true, false);
                            break;
                        }
                    } else if (sectionLiesInHorizontal == SwingUtilities2.Section.LEADING || sectionLiesInHorizontal == SwingUtilities2.Section.TRAILING) {
                        SwingUtilities2.Section sectionLiesInVertical = SwingUtilities2.liesInVertical(cellRect, point, true);
                        if (sectionLiesInVertical == SwingUtilities2.Section.LEADING) {
                            z3 = true;
                        } else if (sectionLiesInVertical == SwingUtilities2.Section.TRAILING) {
                            iRowAtPoint++;
                            z3 = true;
                        }
                        dropLocation = new DropLocation(point, iRowAtPoint, sectionLiesInHorizontal == SwingUtilities2.Section.TRAILING ? iColumnAtPoint + 1 : iColumnAtPoint, z3, true);
                        break;
                    } else {
                        if (SwingUtilities2.liesInVertical(cellRect, point, false) == SwingUtilities2.Section.TRAILING) {
                            iRowAtPoint++;
                        }
                        dropLocation = new DropLocation(point, iRowAtPoint, iColumnAtPoint, true, false);
                        break;
                    }
                }
                break;
            case INSERT_ROWS:
                if (iRowAtPoint == -1 && iColumnAtPoint == -1) {
                    dropLocation = new DropLocation(point, -1, -1, false, false);
                    break;
                } else if (iRowAtPoint == -1) {
                    dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint, true, false);
                    break;
                } else {
                    if (SwingUtilities2.liesInVertical(cellRect, point, false) == SwingUtilities2.Section.TRAILING) {
                        iRowAtPoint++;
                    }
                    dropLocation = new DropLocation(point, iRowAtPoint, iColumnAtPoint, true, false);
                    break;
                }
                break;
            case INSERT_COLS:
                if (iRowAtPoint == -1) {
                    dropLocation = new DropLocation(point, -1, -1, false, false);
                    break;
                } else if (iColumnAtPoint == -1) {
                    dropLocation = new DropLocation(point, getColumnCount(), iColumnAtPoint, false, true);
                    break;
                } else {
                    if (SwingUtilities2.liesInHorizontal(cellRect, point, zIsLeftToRight, false) == SwingUtilities2.Section.TRAILING) {
                        iColumnAtPoint++;
                    }
                    dropLocation = new DropLocation(point, iRowAtPoint, iColumnAtPoint, false, true);
                    break;
                }
            case ON_OR_INSERT:
                if (iRowAtPoint == -1 && iColumnAtPoint == -1) {
                    dropLocation = new DropLocation(point, 0, 0, true, true);
                    break;
                } else {
                    SwingUtilities2.Section sectionLiesInHorizontal2 = SwingUtilities2.liesInHorizontal(cellRect, point, zIsLeftToRight, true);
                    if (iRowAtPoint == -1) {
                        if (sectionLiesInHorizontal2 == SwingUtilities2.Section.LEADING) {
                            dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint, true, true);
                            break;
                        } else if (sectionLiesInHorizontal2 == SwingUtilities2.Section.TRAILING) {
                            dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint + 1, true, true);
                            break;
                        } else {
                            dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint, true, false);
                            break;
                        }
                    } else {
                        SwingUtilities2.Section sectionLiesInVertical2 = SwingUtilities2.liesInVertical(cellRect, point, true);
                        if (sectionLiesInVertical2 == SwingUtilities2.Section.LEADING) {
                            z3 = true;
                        } else if (sectionLiesInVertical2 == SwingUtilities2.Section.TRAILING) {
                            iRowAtPoint++;
                            z3 = true;
                        }
                        dropLocation = new DropLocation(point, iRowAtPoint, sectionLiesInHorizontal2 == SwingUtilities2.Section.TRAILING ? iColumnAtPoint + 1 : iColumnAtPoint, z3, sectionLiesInHorizontal2 != SwingUtilities2.Section.MIDDLE);
                        break;
                    }
                }
                break;
            case ON_OR_INSERT_ROWS:
                if (iRowAtPoint == -1 && iColumnAtPoint == -1) {
                    dropLocation = new DropLocation(point, -1, -1, false, false);
                    break;
                } else if (iRowAtPoint == -1) {
                    dropLocation = new DropLocation(point, getRowCount(), iColumnAtPoint, true, false);
                    break;
                } else {
                    SwingUtilities2.Section sectionLiesInVertical3 = SwingUtilities2.liesInVertical(cellRect, point, true);
                    if (sectionLiesInVertical3 == SwingUtilities2.Section.LEADING) {
                        z3 = true;
                    } else if (sectionLiesInVertical3 == SwingUtilities2.Section.TRAILING) {
                        iRowAtPoint++;
                        z3 = true;
                    }
                    dropLocation = new DropLocation(point, iRowAtPoint, iColumnAtPoint, z3, false);
                    break;
                }
                break;
            case ON_OR_INSERT_COLS:
                if (iRowAtPoint == -1) {
                    dropLocation = new DropLocation(point, -1, -1, false, false);
                    break;
                } else if (iColumnAtPoint == -1) {
                    dropLocation = new DropLocation(point, iRowAtPoint, getColumnCount(), false, true);
                    break;
                } else {
                    SwingUtilities2.Section sectionLiesInHorizontal3 = SwingUtilities2.liesInHorizontal(cellRect, point, zIsLeftToRight, true);
                    if (sectionLiesInHorizontal3 == SwingUtilities2.Section.LEADING) {
                        z3 = true;
                    } else if (sectionLiesInHorizontal3 == SwingUtilities2.Section.TRAILING) {
                        iColumnAtPoint++;
                        z3 = true;
                    }
                    dropLocation = new DropLocation(point, iRowAtPoint, iColumnAtPoint, false, z3);
                    break;
                }
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError((Object) "Unexpected drop mode");
                }
                break;
        }
        return dropLocation;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javax.swing.JComponent
    Object setDropLocation(TransferHandler.DropLocation dropLocation, Object obj, boolean z2) {
        Object obj2 = null;
        DropLocation dropLocation2 = (DropLocation) dropLocation;
        if (this.dropMode == DropMode.USE_SELECTION) {
            if (dropLocation2 == null) {
                if (!z2 && obj != null) {
                    clearSelection();
                    int[] iArr = ((int[][]) obj)[0];
                    int[] iArr2 = ((int[][]) obj)[1];
                    int[] iArr3 = ((int[][]) obj)[2];
                    for (int i2 : iArr) {
                        addRowSelectionInterval(i2, i2);
                    }
                    for (int i3 : iArr2) {
                        addColumnSelectionInterval(i3, i3);
                    }
                    SwingUtilities2.setLeadAnchorWithoutSelection(getSelectionModel(), iArr3[1], iArr3[0]);
                    SwingUtilities2.setLeadAnchorWithoutSelection(getColumnModel().getSelectionModel(), iArr3[3], iArr3[2]);
                }
            } else {
                if (this.dropLocation == null) {
                    obj2 = new int[]{getSelectedRows(), getSelectedColumns(), new int[]{getAdjustedIndex(getSelectionModel().getAnchorSelectionIndex(), true), getAdjustedIndex(getSelectionModel().getLeadSelectionIndex(), true), getAdjustedIndex(getColumnModel().getSelectionModel().getAnchorSelectionIndex(), false), getAdjustedIndex(getColumnModel().getSelectionModel().getLeadSelectionIndex(), false)}};
                } else {
                    obj2 = obj;
                }
                if (dropLocation2.getRow() == -1) {
                    clearSelectionAndLeadAnchor();
                } else {
                    setRowSelectionInterval(dropLocation2.getRow(), dropLocation2.getRow());
                    setColumnSelectionInterval(dropLocation2.getColumn(), dropLocation2.getColumn());
                }
            }
        }
        DropLocation dropLocation3 = this.dropLocation;
        this.dropLocation = dropLocation2;
        firePropertyChange("dropLocation", dropLocation3, this.dropLocation);
        return obj2;
    }

    public final DropLocation getDropLocation() {
        return this.dropLocation;
    }

    public void setAutoCreateRowSorter(boolean z2) {
        boolean z3 = this.autoCreateRowSorter;
        this.autoCreateRowSorter = z2;
        if (z2) {
            setRowSorter(new TableRowSorter(getModel()));
        }
        firePropertyChange("autoCreateRowSorter", z3, z2);
    }

    public boolean getAutoCreateRowSorter() {
        return this.autoCreateRowSorter;
    }

    public void setUpdateSelectionOnSort(boolean z2) {
        if (this.updateSelectionOnSort != z2) {
            this.updateSelectionOnSort = z2;
            firePropertyChange("updateSelectionOnSort", !z2, z2);
        }
    }

    public boolean getUpdateSelectionOnSort() {
        return this.updateSelectionOnSort;
    }

    public void setRowSorter(RowSorter<? extends TableModel> rowSorter) {
        RowSorter<? extends TableModel> rowSorter2 = null;
        if (this.sortManager != null) {
            rowSorter2 = this.sortManager.sorter;
            this.sortManager.dispose();
            this.sortManager = null;
        }
        this.rowModel = null;
        clearSelectionAndLeadAnchor();
        if (rowSorter != null) {
            this.sortManager = new SortManager(rowSorter);
        }
        resizeAndRepaint();
        firePropertyChange("rowSorter", rowSorter2, rowSorter);
        firePropertyChange("sorter", rowSorter2, rowSorter);
    }

    public RowSorter<? extends TableModel> getRowSorter() {
        if (this.sortManager != null) {
            return this.sortManager.sorter;
        }
        return null;
    }

    public void setSelectionMode(int i2) {
        clearSelection();
        getSelectionModel().setSelectionMode(i2);
        getColumnModel().getSelectionModel().setSelectionMode(i2);
    }

    public void setRowSelectionAllowed(boolean z2) {
        boolean z3 = this.rowSelectionAllowed;
        this.rowSelectionAllowed = z2;
        if (z3 != z2) {
            repaint();
        }
        firePropertyChange("rowSelectionAllowed", z3, z2);
    }

    public boolean getRowSelectionAllowed() {
        return this.rowSelectionAllowed;
    }

    public void setColumnSelectionAllowed(boolean z2) {
        boolean columnSelectionAllowed = this.columnModel.getColumnSelectionAllowed();
        this.columnModel.setColumnSelectionAllowed(z2);
        if (columnSelectionAllowed != z2) {
            repaint();
        }
        firePropertyChange("columnSelectionAllowed", columnSelectionAllowed, z2);
    }

    public boolean getColumnSelectionAllowed() {
        return this.columnModel.getColumnSelectionAllowed();
    }

    public void setCellSelectionEnabled(boolean z2) {
        setRowSelectionAllowed(z2);
        setColumnSelectionAllowed(z2);
        boolean z3 = this.cellSelectionEnabled;
        this.cellSelectionEnabled = z2;
        firePropertyChange("cellSelectionEnabled", z3, z2);
    }

    public boolean getCellSelectionEnabled() {
        return getRowSelectionAllowed() && getColumnSelectionAllowed();
    }

    public void selectAll() {
        if (isEditing()) {
            removeEditor();
        }
        if (getRowCount() > 0 && getColumnCount() > 0) {
            ListSelectionModel listSelectionModel = this.selectionModel;
            listSelectionModel.setValueIsAdjusting(true);
            int adjustedIndex = getAdjustedIndex(listSelectionModel.getLeadSelectionIndex(), true);
            int adjustedIndex2 = getAdjustedIndex(listSelectionModel.getAnchorSelectionIndex(), true);
            setRowSelectionInterval(0, getRowCount() - 1);
            SwingUtilities2.setLeadAnchorWithoutSelection(listSelectionModel, adjustedIndex, adjustedIndex2);
            listSelectionModel.setValueIsAdjusting(false);
            ListSelectionModel selectionModel = this.columnModel.getSelectionModel();
            selectionModel.setValueIsAdjusting(true);
            int adjustedIndex3 = getAdjustedIndex(selectionModel.getLeadSelectionIndex(), false);
            int adjustedIndex4 = getAdjustedIndex(selectionModel.getAnchorSelectionIndex(), false);
            setColumnSelectionInterval(0, getColumnCount() - 1);
            SwingUtilities2.setLeadAnchorWithoutSelection(selectionModel, adjustedIndex3, adjustedIndex4);
            selectionModel.setValueIsAdjusting(false);
        }
    }

    public void clearSelection() {
        this.selectionModel.clearSelection();
        this.columnModel.getSelectionModel().clearSelection();
    }

    private void clearSelectionAndLeadAnchor() {
        this.selectionModel.setValueIsAdjusting(true);
        this.columnModel.getSelectionModel().setValueIsAdjusting(true);
        clearSelection();
        this.selectionModel.setAnchorSelectionIndex(-1);
        this.selectionModel.setLeadSelectionIndex(-1);
        this.columnModel.getSelectionModel().setAnchorSelectionIndex(-1);
        this.columnModel.getSelectionModel().setLeadSelectionIndex(-1);
        this.selectionModel.setValueIsAdjusting(false);
        this.columnModel.getSelectionModel().setValueIsAdjusting(false);
    }

    private int getAdjustedIndex(int i2, boolean z2) {
        if (i2 < (z2 ? getRowCount() : getColumnCount())) {
            return i2;
        }
        return -1;
    }

    private int boundRow(int i2) throws IllegalArgumentException {
        if (i2 < 0 || i2 >= getRowCount()) {
            throw new IllegalArgumentException("Row index out of range");
        }
        return i2;
    }

    private int boundColumn(int i2) {
        if (i2 < 0 || i2 >= getColumnCount()) {
            throw new IllegalArgumentException("Column index out of range");
        }
        return i2;
    }

    public void setRowSelectionInterval(int i2, int i3) {
        this.selectionModel.setSelectionInterval(boundRow(i2), boundRow(i3));
    }

    public void setColumnSelectionInterval(int i2, int i3) {
        this.columnModel.getSelectionModel().setSelectionInterval(boundColumn(i2), boundColumn(i3));
    }

    public void addRowSelectionInterval(int i2, int i3) {
        this.selectionModel.addSelectionInterval(boundRow(i2), boundRow(i3));
    }

    public void addColumnSelectionInterval(int i2, int i3) {
        this.columnModel.getSelectionModel().addSelectionInterval(boundColumn(i2), boundColumn(i3));
    }

    public void removeRowSelectionInterval(int i2, int i3) {
        this.selectionModel.removeSelectionInterval(boundRow(i2), boundRow(i3));
    }

    public void removeColumnSelectionInterval(int i2, int i3) {
        this.columnModel.getSelectionModel().removeSelectionInterval(boundColumn(i2), boundColumn(i3));
    }

    public int getSelectedRow() {
        return this.selectionModel.getMinSelectionIndex();
    }

    public int getSelectedColumn() {
        return this.columnModel.getSelectionModel().getMinSelectionIndex();
    }

    public int[] getSelectedRows() {
        int minSelectionIndex = this.selectionModel.getMinSelectionIndex();
        int maxSelectionIndex = this.selectionModel.getMaxSelectionIndex();
        if (minSelectionIndex == -1 || maxSelectionIndex == -1) {
            return new int[0];
        }
        int[] iArr = new int[1 + (maxSelectionIndex - minSelectionIndex)];
        int i2 = 0;
        for (int i3 = minSelectionIndex; i3 <= maxSelectionIndex; i3++) {
            if (this.selectionModel.isSelectedIndex(i3)) {
                int i4 = i2;
                i2++;
                iArr[i4] = i3;
            }
        }
        int[] iArr2 = new int[i2];
        System.arraycopy(iArr, 0, iArr2, 0, i2);
        return iArr2;
    }

    public int[] getSelectedColumns() {
        return this.columnModel.getSelectedColumns();
    }

    public int getSelectedRowCount() {
        int minSelectionIndex = this.selectionModel.getMinSelectionIndex();
        int maxSelectionIndex = this.selectionModel.getMaxSelectionIndex();
        int i2 = 0;
        for (int i3 = minSelectionIndex; i3 <= maxSelectionIndex; i3++) {
            if (this.selectionModel.isSelectedIndex(i3)) {
                i2++;
            }
        }
        return i2;
    }

    public int getSelectedColumnCount() {
        return this.columnModel.getSelectedColumnCount();
    }

    public boolean isRowSelected(int i2) {
        return this.selectionModel.isSelectedIndex(i2);
    }

    public boolean isColumnSelected(int i2) {
        return this.columnModel.getSelectionModel().isSelectedIndex(i2);
    }

    public boolean isCellSelected(int i2, int i3) {
        if (getRowSelectionAllowed() || getColumnSelectionAllowed()) {
            return (!getRowSelectionAllowed() || isRowSelected(i2)) && (!getColumnSelectionAllowed() || isColumnSelected(i3));
        }
        return false;
    }

    private void changeSelectionModel(ListSelectionModel listSelectionModel, int i2, boolean z2, boolean z3, boolean z4, int i3, boolean z5) {
        if (z3) {
            if (z2) {
                if (z5) {
                    listSelectionModel.addSelectionInterval(i3, i2);
                    return;
                }
                listSelectionModel.removeSelectionInterval(i3, i2);
                if (Boolean.TRUE == getClientProperty("Table.isFileList")) {
                    listSelectionModel.addSelectionInterval(i2, i2);
                    listSelectionModel.setAnchorSelectionIndex(i3);
                    return;
                }
                return;
            }
            listSelectionModel.setSelectionInterval(i3, i2);
            return;
        }
        if (z2) {
            if (z4) {
                listSelectionModel.removeSelectionInterval(i2, i2);
                return;
            } else {
                listSelectionModel.addSelectionInterval(i2, i2);
                return;
            }
        }
        listSelectionModel.setSelectionInterval(i2, i2);
    }

    public void changeSelection(int i2, int i3, boolean z2, boolean z3) {
        Rectangle cellRect;
        ListSelectionModel selectionModel = getSelectionModel();
        ListSelectionModel selectionModel2 = getColumnModel().getSelectionModel();
        int adjustedIndex = getAdjustedIndex(selectionModel.getAnchorSelectionIndex(), true);
        int adjustedIndex2 = getAdjustedIndex(selectionModel2.getAnchorSelectionIndex(), false);
        boolean z4 = true;
        if (adjustedIndex == -1) {
            if (getRowCount() > 0) {
                adjustedIndex = 0;
            }
            z4 = false;
        }
        if (adjustedIndex2 == -1) {
            if (getColumnCount() > 0) {
                adjustedIndex2 = 0;
            }
            z4 = false;
        }
        boolean zIsCellSelected = isCellSelected(i2, i3);
        boolean z5 = z4 && isCellSelected(adjustedIndex, adjustedIndex2);
        changeSelectionModel(selectionModel2, i3, z2, z3, zIsCellSelected, adjustedIndex2, z5);
        changeSelectionModel(selectionModel, i2, z2, z3, zIsCellSelected, adjustedIndex, z5);
        if (getAutoscrolls() && (cellRect = getCellRect(i2, i3, false)) != null) {
            scrollRectToVisible(cellRect);
        }
    }

    public Color getSelectionForeground() {
        return this.selectionForeground;
    }

    public void setSelectionForeground(Color color) {
        Color color2 = this.selectionForeground;
        this.selectionForeground = color;
        firePropertyChange("selectionForeground", color2, color);
        repaint();
    }

    public Color getSelectionBackground() {
        return this.selectionBackground;
    }

    public void setSelectionBackground(Color color) {
        Color color2 = this.selectionBackground;
        this.selectionBackground = color;
        firePropertyChange("selectionBackground", color2, color);
        repaint();
    }

    public TableColumn getColumn(Object obj) {
        TableColumnModel columnModel = getColumnModel();
        return columnModel.getColumn(columnModel.getColumnIndex(obj));
    }

    public int convertColumnIndexToModel(int i2) {
        return SwingUtilities2.convertColumnIndexToModel(getColumnModel(), i2);
    }

    public int convertColumnIndexToView(int i2) {
        return SwingUtilities2.convertColumnIndexToView(getColumnModel(), i2);
    }

    public int convertRowIndexToView(int i2) {
        RowSorter<? extends TableModel> rowSorter = getRowSorter();
        if (rowSorter != null) {
            return rowSorter.convertRowIndexToView(i2);
        }
        return i2;
    }

    public int convertRowIndexToModel(int i2) {
        RowSorter<? extends TableModel> rowSorter = getRowSorter();
        if (rowSorter != null) {
            return rowSorter.convertRowIndexToModel(i2);
        }
        return i2;
    }

    public int getRowCount() {
        RowSorter<? extends TableModel> rowSorter = getRowSorter();
        if (rowSorter != null) {
            return rowSorter.getViewRowCount();
        }
        return getModel().getRowCount();
    }

    public int getColumnCount() {
        return getColumnModel().getColumnCount();
    }

    public String getColumnName(int i2) {
        return getModel().getColumnName(convertColumnIndexToModel(i2));
    }

    public Class<?> getColumnClass(int i2) {
        return getModel().getColumnClass(convertColumnIndexToModel(i2));
    }

    public Object getValueAt(int i2, int i3) {
        return getModel().getValueAt(convertRowIndexToModel(i2), convertColumnIndexToModel(i3));
    }

    public void setValueAt(Object obj, int i2, int i3) {
        getModel().setValueAt(obj, convertRowIndexToModel(i2), convertColumnIndexToModel(i3));
    }

    public boolean isCellEditable(int i2, int i3) {
        return getModel().isCellEditable(convertRowIndexToModel(i2), convertColumnIndexToModel(i3));
    }

    public void addColumn(TableColumn tableColumn) {
        if (tableColumn.getHeaderValue() == null) {
            tableColumn.setHeaderValue(getModel().getColumnName(tableColumn.getModelIndex()));
        }
        getColumnModel().addColumn(tableColumn);
    }

    public void removeColumn(TableColumn tableColumn) {
        getColumnModel().removeColumn(tableColumn);
    }

    public void moveColumn(int i2, int i3) {
        getColumnModel().moveColumn(i2, i3);
    }

    public int columnAtPoint(Point point) {
        int width = point.f12370x;
        if (!getComponentOrientation().isLeftToRight()) {
            width = (getWidth() - width) - 1;
        }
        return getColumnModel().getColumnIndexAtX(width);
    }

    public int rowAtPoint(Point point) {
        int i2 = point.f12371y;
        int rowHeight = this.rowModel == null ? i2 / getRowHeight() : this.rowModel.getIndex(i2);
        if (rowHeight < 0 || rowHeight >= getRowCount()) {
            return -1;
        }
        return rowHeight;
    }

    public Rectangle getCellRect(int i2, int i3, boolean z2) {
        Rectangle rectangle = new Rectangle();
        boolean z3 = true;
        if (i2 < 0) {
            z3 = false;
        } else if (i2 >= getRowCount()) {
            rectangle.f12373y = getHeight();
            z3 = false;
        } else {
            rectangle.height = getRowHeight(i2);
            rectangle.f12373y = this.rowModel == null ? i2 * rectangle.height : this.rowModel.getPosition(i2);
        }
        if (i3 < 0) {
            if (!getComponentOrientation().isLeftToRight()) {
                rectangle.f12372x = getWidth();
            }
            z3 = false;
        } else if (i3 >= getColumnCount()) {
            if (getComponentOrientation().isLeftToRight()) {
                rectangle.f12372x = getWidth();
            }
            z3 = false;
        } else {
            TableColumnModel columnModel = getColumnModel();
            if (getComponentOrientation().isLeftToRight()) {
                for (int i4 = 0; i4 < i3; i4++) {
                    rectangle.f12372x += columnModel.getColumn(i4).getWidth();
                }
            } else {
                for (int columnCount = columnModel.getColumnCount() - 1; columnCount > i3; columnCount--) {
                    rectangle.f12372x += columnModel.getColumn(columnCount).getWidth();
                }
            }
            rectangle.width = columnModel.getColumn(i3).getWidth();
        }
        if (z3 && !z2) {
            int iMin = Math.min(getRowMargin(), rectangle.height);
            int iMin2 = Math.min(getColumnModel().getColumnMargin(), rectangle.width);
            rectangle.setBounds(rectangle.f12372x + (iMin2 / 2), rectangle.f12373y + (iMin / 2), rectangle.width - iMin2, rectangle.height - iMin);
        }
        return rectangle;
    }

    private int viewIndexForColumn(TableColumn tableColumn) {
        TableColumnModel columnModel = getColumnModel();
        for (int i2 = 0; i2 < columnModel.getColumnCount(); i2++) {
            if (columnModel.getColumn(i2) == tableColumn) {
                return i2;
            }
        }
        return -1;
    }

    @Override // java.awt.Container, java.awt.Component
    public void doLayout() {
        TableColumn resizingColumn = getResizingColumn();
        if (resizingColumn == null) {
            setWidthsFromPreferredWidths(false);
        } else {
            accommodateDelta(viewIndexForColumn(resizingColumn), getWidth() - getColumnModel().getTotalColumnWidth());
            int width = getWidth() - getColumnModel().getTotalColumnWidth();
            if (width != 0) {
                resizingColumn.setWidth(resizingColumn.getWidth() + width);
            }
            setWidthsFromPreferredWidths(true);
        }
        super.doLayout();
    }

    private TableColumn getResizingColumn() {
        if (this.tableHeader == null) {
            return null;
        }
        return this.tableHeader.getResizingColumn();
    }

    @Deprecated
    public void sizeColumnsToFit(boolean z2) {
        int i2 = this.autoResizeMode;
        setAutoResizeMode(z2 ? 3 : 4);
        sizeColumnsToFit(-1);
        setAutoResizeMode(i2);
    }

    public void sizeColumnsToFit(int i2) {
        if (i2 == -1) {
            setWidthsFromPreferredWidths(false);
        } else if (this.autoResizeMode == 0) {
            TableColumn column = getColumnModel().getColumn(i2);
            column.setPreferredWidth(column.getWidth());
        } else {
            accommodateDelta(i2, getWidth() - getColumnModel().getTotalColumnWidth());
            setWidthsFromPreferredWidths(true);
        }
    }

    private void setWidthsFromPreferredWidths(final boolean z2) {
        int width = !z2 ? getWidth() : getPreferredSize().width;
        final TableColumnModel tableColumnModel = this.columnModel;
        adjustSizes(width, new Resizable3() { // from class: javax.swing.JTable.2
            @Override // javax.swing.JTable.Resizable2
            public int getElementCount() {
                return tableColumnModel.getColumnCount();
            }

            @Override // javax.swing.JTable.Resizable2
            public int getLowerBoundAt(int i2) {
                return tableColumnModel.getColumn(i2).getMinWidth();
            }

            @Override // javax.swing.JTable.Resizable2
            public int getUpperBoundAt(int i2) {
                return tableColumnModel.getColumn(i2).getMaxWidth();
            }

            @Override // javax.swing.JTable.Resizable3
            public int getMidPointAt(int i2) {
                if (!z2) {
                    return tableColumnModel.getColumn(i2).getPreferredWidth();
                }
                return tableColumnModel.getColumn(i2).getWidth();
            }

            @Override // javax.swing.JTable.Resizable2
            public void setSizeAt(int i2, int i3) {
                if (!z2) {
                    tableColumnModel.getColumn(i3).setWidth(i2);
                } else {
                    tableColumnModel.getColumn(i3).setPreferredWidth(i2);
                }
            }
        }, z2);
    }

    private void accommodateDelta(int i2, int i3) {
        int i4;
        int iMin;
        int columnCount = getColumnCount();
        switch (this.autoResizeMode) {
            case 1:
                i4 = i2 + 1;
                iMin = Math.min(i4 + 1, columnCount);
                break;
            case 2:
                i4 = i2 + 1;
                iMin = columnCount;
                break;
            case 3:
                i4 = columnCount - 1;
                iMin = i4 + 1;
                break;
            case 4:
                i4 = 0;
                iMin = columnCount;
                break;
            default:
                return;
        }
        final int i5 = i4;
        final int i6 = iMin;
        final TableColumnModel tableColumnModel = this.columnModel;
        Resizable3 resizable3 = new Resizable3() { // from class: javax.swing.JTable.3
            @Override // javax.swing.JTable.Resizable2
            public int getElementCount() {
                return i6 - i5;
            }

            @Override // javax.swing.JTable.Resizable2
            public int getLowerBoundAt(int i7) {
                return tableColumnModel.getColumn(i7 + i5).getMinWidth();
            }

            @Override // javax.swing.JTable.Resizable2
            public int getUpperBoundAt(int i7) {
                return tableColumnModel.getColumn(i7 + i5).getMaxWidth();
            }

            @Override // javax.swing.JTable.Resizable3
            public int getMidPointAt(int i7) {
                return tableColumnModel.getColumn(i7 + i5).getWidth();
            }

            @Override // javax.swing.JTable.Resizable2
            public void setSizeAt(int i7, int i8) {
                tableColumnModel.getColumn(i8 + i5).setWidth(i7);
            }
        };
        int width = 0;
        for (int i7 = i4; i7 < iMin; i7++) {
            width += this.columnModel.getColumn(i7).getWidth();
        }
        adjustSizes(width + i3, resizable3, false);
    }

    private void adjustSizes(long j2, final Resizable3 resizable3, boolean z2) {
        Resizable2 resizable2;
        long midPointAt = 0;
        for (int i2 = 0; i2 < resizable3.getElementCount(); i2++) {
            midPointAt += resizable3.getMidPointAt(i2);
        }
        if ((j2 < midPointAt) == (!z2)) {
            resizable2 = new Resizable2() { // from class: javax.swing.JTable.4
                @Override // javax.swing.JTable.Resizable2
                public int getElementCount() {
                    return resizable3.getElementCount();
                }

                @Override // javax.swing.JTable.Resizable2
                public int getLowerBoundAt(int i3) {
                    return resizable3.getLowerBoundAt(i3);
                }

                @Override // javax.swing.JTable.Resizable2
                public int getUpperBoundAt(int i3) {
                    return resizable3.getMidPointAt(i3);
                }

                @Override // javax.swing.JTable.Resizable2
                public void setSizeAt(int i3, int i4) {
                    resizable3.setSizeAt(i3, i4);
                }
            };
        } else {
            resizable2 = new Resizable2() { // from class: javax.swing.JTable.5
                @Override // javax.swing.JTable.Resizable2
                public int getElementCount() {
                    return resizable3.getElementCount();
                }

                @Override // javax.swing.JTable.Resizable2
                public int getLowerBoundAt(int i3) {
                    return resizable3.getMidPointAt(i3);
                }

                @Override // javax.swing.JTable.Resizable2
                public int getUpperBoundAt(int i3) {
                    return resizable3.getUpperBoundAt(i3);
                }

                @Override // javax.swing.JTable.Resizable2
                public void setSizeAt(int i3, int i4) {
                    resizable3.setSizeAt(i3, i4);
                }
            };
        }
        adjustSizes(j2, resizable2, !z2);
    }

    private void adjustSizes(long j2, Resizable2 resizable2, boolean z2) {
        int iRound;
        long lowerBoundAt = 0;
        long upperBoundAt = 0;
        for (int i2 = 0; i2 < resizable2.getElementCount(); i2++) {
            lowerBoundAt += resizable2.getLowerBoundAt(i2);
            upperBoundAt += resizable2.getUpperBoundAt(i2);
        }
        if (z2) {
            j2 = Math.min(Math.max(lowerBoundAt, j2), upperBoundAt);
        }
        for (int i3 = 0; i3 < resizable2.getElementCount(); i3++) {
            int lowerBoundAt2 = resizable2.getLowerBoundAt(i3);
            int upperBoundAt2 = resizable2.getUpperBoundAt(i3);
            if (lowerBoundAt == upperBoundAt) {
                iRound = lowerBoundAt2;
            } else {
                iRound = (int) Math.round(lowerBoundAt2 + (((j2 - lowerBoundAt) / (upperBoundAt - lowerBoundAt)) * (upperBoundAt2 - lowerBoundAt2)));
            }
            int i4 = iRound;
            resizable2.setSizeAt(i4, i3);
            j2 -= i4;
            lowerBoundAt -= lowerBoundAt2;
            upperBoundAt -= upperBoundAt2;
        }
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        String toolTipText = null;
        Point point = mouseEvent.getPoint();
        int iColumnAtPoint = columnAtPoint(point);
        int iRowAtPoint = rowAtPoint(point);
        if (iColumnAtPoint != -1 && iRowAtPoint != -1) {
            Component componentPrepareRenderer = prepareRenderer(getCellRenderer(iRowAtPoint, iColumnAtPoint), iRowAtPoint, iColumnAtPoint);
            if (componentPrepareRenderer instanceof JComponent) {
                Rectangle cellRect = getCellRect(iRowAtPoint, iColumnAtPoint, false);
                point.translate(-cellRect.f12372x, -cellRect.f12373y);
                MouseEvent mouseEvent2 = new MouseEvent(componentPrepareRenderer, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                toolTipText = ((JComponent) componentPrepareRenderer).getToolTipText(mouseEvent2);
            }
        }
        if (toolTipText == null) {
            toolTipText = getToolTipText();
        }
        return toolTipText;
    }

    public void setSurrendersFocusOnKeystroke(boolean z2) {
        this.surrendersFocusOnKeystroke = z2;
    }

    public boolean getSurrendersFocusOnKeystroke() {
        return this.surrendersFocusOnKeystroke;
    }

    public boolean editCellAt(int i2, int i3) {
        return editCellAt(i2, i3, null);
    }

    public boolean editCellAt(int i2, int i3, EventObject eventObject) {
        if ((this.cellEditor != null && !this.cellEditor.stopCellEditing()) || i2 < 0 || i2 >= getRowCount() || i3 < 0 || i3 >= getColumnCount() || !isCellEditable(i2, i3)) {
            return false;
        }
        if (this.editorRemover == null) {
            KeyboardFocusManager currentKeyboardFocusManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            this.editorRemover = new CellEditorRemover(currentKeyboardFocusManager);
            currentKeyboardFocusManager.addPropertyChangeListener("permanentFocusOwner", this.editorRemover);
        }
        TableCellEditor cellEditor = getCellEditor(i2, i3);
        if (cellEditor != null && cellEditor.isCellEditable(eventObject)) {
            this.editorComp = prepareEditor(cellEditor, i2, i3);
            if (this.editorComp == null) {
                removeEditor();
                return false;
            }
            this.editorComp.setBounds(getCellRect(i2, i3, false));
            add(this.editorComp);
            this.editorComp.validate();
            this.editorComp.repaint();
            setCellEditor(cellEditor);
            setEditingRow(i2);
            setEditingColumn(i3);
            cellEditor.addCellEditorListener(this);
            return true;
        }
        return false;
    }

    public boolean isEditing() {
        return this.cellEditor != null;
    }

    public Component getEditorComponent() {
        return this.editorComp;
    }

    public int getEditingColumn() {
        return this.editingColumn;
    }

    public int getEditingRow() {
        return this.editingRow;
    }

    public TableUI getUI() {
        return (TableUI) this.ui;
    }

    public void setUI(TableUI tableUI) {
        if (this.ui != tableUI) {
            super.setUI((ComponentUI) tableUI);
            repaint();
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        TableColumnModel columnModel = getColumnModel();
        for (int i2 = 0; i2 < columnModel.getColumnCount(); i2++) {
            TableColumn column = columnModel.getColumn(i2);
            SwingUtilities.updateRendererOrEditorUI(column.getCellRenderer());
            SwingUtilities.updateRendererOrEditorUI(column.getCellEditor());
            SwingUtilities.updateRendererOrEditorUI(column.getHeaderRenderer());
        }
        Enumeration enumerationElements = this.defaultRenderersByColumnClass.elements();
        while (enumerationElements.hasMoreElements()) {
            SwingUtilities.updateRendererOrEditorUI(enumerationElements.nextElement2());
        }
        Enumeration enumerationElements2 = this.defaultEditorsByColumnClass.elements();
        while (enumerationElements2.hasMoreElements()) {
            SwingUtilities.updateRendererOrEditorUI(enumerationElements2.nextElement2());
        }
        if (this.tableHeader != null && this.tableHeader.getParent() == null) {
            this.tableHeader.updateUI();
        }
        configureEnclosingScrollPaneUI();
        setUI((TableUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public void setModel(TableModel tableModel) {
        if (tableModel == null) {
            throw new IllegalArgumentException("Cannot set a null TableModel");
        }
        if (this.dataModel != tableModel) {
            TableModel tableModel2 = this.dataModel;
            if (tableModel2 != null) {
                tableModel2.removeTableModelListener(this);
            }
            this.dataModel = tableModel;
            tableModel.addTableModelListener(this);
            tableChanged(new TableModelEvent(tableModel, -1));
            firePropertyChange("model", tableModel2, tableModel);
            if (getAutoCreateRowSorter()) {
                setRowSorter(new TableRowSorter<>(tableModel));
            }
        }
    }

    public TableModel getModel() {
        return this.dataModel;
    }

    public void setColumnModel(TableColumnModel tableColumnModel) {
        if (tableColumnModel == null) {
            throw new IllegalArgumentException("Cannot set a null ColumnModel");
        }
        TableColumnModel tableColumnModel2 = this.columnModel;
        if (tableColumnModel != tableColumnModel2) {
            if (tableColumnModel2 != null) {
                tableColumnModel2.removeColumnModelListener(this);
            }
            this.columnModel = tableColumnModel;
            tableColumnModel.addColumnModelListener(this);
            if (this.tableHeader != null) {
                this.tableHeader.setColumnModel(tableColumnModel);
            }
            firePropertyChange("columnModel", tableColumnModel2, tableColumnModel);
            resizeAndRepaint();
        }
    }

    public TableColumnModel getColumnModel() {
        return this.columnModel;
    }

    public void setSelectionModel(ListSelectionModel listSelectionModel) {
        if (listSelectionModel == null) {
            throw new IllegalArgumentException("Cannot set a null SelectionModel");
        }
        ListSelectionModel listSelectionModel2 = this.selectionModel;
        if (listSelectionModel != listSelectionModel2) {
            if (listSelectionModel2 != null) {
                listSelectionModel2.removeListSelectionListener(this);
            }
            this.selectionModel = listSelectionModel;
            listSelectionModel.addListSelectionListener(this);
            firePropertyChange("selectionModel", listSelectionModel2, listSelectionModel);
            repaint();
        }
    }

    public ListSelectionModel getSelectionModel() {
        return this.selectionModel;
    }

    @Override // javax.swing.event.RowSorterListener
    public void sorterChanged(RowSorterEvent rowSorterEvent) {
        if (rowSorterEvent.getType() == RowSorterEvent.Type.SORT_ORDER_CHANGED) {
            JTableHeader tableHeader = getTableHeader();
            if (tableHeader != null) {
                tableHeader.repaint();
                return;
            }
            return;
        }
        if (rowSorterEvent.getType() == RowSorterEvent.Type.SORTED) {
            this.sorterChanged = true;
            if (!this.ignoreSortChange) {
                sortedTableChanged(rowSorterEvent, null);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$SortManager.class */
    private final class SortManager {
        RowSorter<? extends TableModel> sorter;
        private ListSelectionModel modelSelection;
        private int modelLeadIndex;
        private boolean syncingSelection;
        private int[] lastModelSelection;
        private SizeSequence modelRowSizes;

        SortManager(RowSorter<? extends TableModel> rowSorter) {
            this.sorter = rowSorter;
            rowSorter.addRowSorterListener(JTable.this);
        }

        public void dispose() {
            if (this.sorter != null) {
                this.sorter.removeRowSorterListener(JTable.this);
            }
        }

        public void setViewRowHeight(int i2, int i3) {
            if (this.modelRowSizes == null) {
                this.modelRowSizes = new SizeSequence(JTable.this.getModel().getRowCount(), JTable.this.getRowHeight());
            }
            this.modelRowSizes.setSize(JTable.this.convertRowIndexToModel(i2), i3);
        }

        public void allChanged() {
            this.modelLeadIndex = -1;
            this.modelSelection = null;
            this.modelRowSizes = null;
        }

        public void viewSelectionChanged(ListSelectionEvent listSelectionEvent) {
            if (!this.syncingSelection && this.modelSelection != null) {
                this.modelSelection = null;
            }
        }

        public void prepareForChange(RowSorterEvent rowSorterEvent, ModelChange modelChange) {
            if (JTable.this.getUpdateSelectionOnSort()) {
                cacheSelection(rowSorterEvent, modelChange);
            }
        }

        private void cacheSelection(RowSorterEvent rowSorterEvent, ModelChange modelChange) {
            int iConvertRowIndexToModel;
            if (rowSorterEvent != null) {
                if (this.modelSelection != null || this.sorter.getViewRowCount() == JTable.this.getModel().getRowCount()) {
                    if (this.modelSelection == null) {
                        cacheModelSelection(rowSorterEvent);
                        return;
                    }
                    return;
                }
                this.modelSelection = new DefaultListSelectionModel();
                ListSelectionModel selectionModel = JTable.this.getSelectionModel();
                int minSelectionIndex = selectionModel.getMinSelectionIndex();
                int maxSelectionIndex = selectionModel.getMaxSelectionIndex();
                for (int i2 = minSelectionIndex; i2 <= maxSelectionIndex; i2++) {
                    if (selectionModel.isSelectedIndex(i2) && (iConvertRowIndexToModel = JTable.this.convertRowIndexToModel(rowSorterEvent, i2)) != -1) {
                        this.modelSelection.addSelectionInterval(iConvertRowIndexToModel, iConvertRowIndexToModel);
                    }
                }
                int iConvertRowIndexToModel2 = JTable.this.convertRowIndexToModel(rowSorterEvent, selectionModel.getLeadSelectionIndex());
                SwingUtilities2.setLeadAnchorWithoutSelection(this.modelSelection, iConvertRowIndexToModel2, iConvertRowIndexToModel2);
                return;
            }
            if (modelChange.allRowsChanged) {
                this.modelSelection = null;
                return;
            }
            if (this.modelSelection != null) {
                switch (modelChange.type) {
                    case -1:
                        this.modelSelection.removeIndexInterval(modelChange.startModelIndex, modelChange.endModelIndex);
                        break;
                    case 1:
                        this.modelSelection.insertIndexInterval(modelChange.startModelIndex, modelChange.length, true);
                        break;
                }
            }
            cacheModelSelection(null);
        }

        private void cacheModelSelection(RowSorterEvent rowSorterEvent) {
            this.lastModelSelection = JTable.this.convertSelectionToModel(rowSorterEvent);
            this.modelLeadIndex = JTable.this.convertRowIndexToModel(rowSorterEvent, JTable.this.selectionModel.getLeadSelectionIndex());
        }

        public void processChange(RowSorterEvent rowSorterEvent, ModelChange modelChange, boolean z2) {
            if (modelChange != null) {
                if (modelChange.allRowsChanged) {
                    this.modelRowSizes = null;
                    JTable.this.rowModel = null;
                } else if (this.modelRowSizes != null) {
                    if (modelChange.type == 1) {
                        this.modelRowSizes.insertEntries(modelChange.startModelIndex, (modelChange.endModelIndex - modelChange.startModelIndex) + 1, JTable.this.getRowHeight());
                    } else if (modelChange.type == -1) {
                        this.modelRowSizes.removeEntries(modelChange.startModelIndex, (modelChange.endModelIndex - modelChange.startModelIndex) + 1);
                    }
                }
            }
            if (z2) {
                setViewRowHeightsFromModel();
                restoreSelection(modelChange);
            }
        }

        private void setViewRowHeightsFromModel() {
            if (this.modelRowSizes != null) {
                JTable.this.rowModel.setSizes(JTable.this.getRowCount(), JTable.this.getRowHeight());
                for (int rowCount = JTable.this.getRowCount() - 1; rowCount >= 0; rowCount--) {
                    JTable.this.rowModel.setSize(rowCount, this.modelRowSizes.getSize(JTable.this.convertRowIndexToModel(rowCount)));
                }
            }
        }

        private void restoreSelection(ModelChange modelChange) {
            int iConvertRowIndexToView;
            this.syncingSelection = true;
            if (this.lastModelSelection != null) {
                JTable.this.restoreSortingSelection(this.lastModelSelection, this.modelLeadIndex, modelChange);
                this.lastModelSelection = null;
            } else if (this.modelSelection != null) {
                ListSelectionModel selectionModel = JTable.this.getSelectionModel();
                selectionModel.setValueIsAdjusting(true);
                selectionModel.clearSelection();
                int minSelectionIndex = this.modelSelection.getMinSelectionIndex();
                int maxSelectionIndex = this.modelSelection.getMaxSelectionIndex();
                for (int i2 = minSelectionIndex; i2 <= maxSelectionIndex; i2++) {
                    if (this.modelSelection.isSelectedIndex(i2) && (iConvertRowIndexToView = JTable.this.convertRowIndexToView(i2)) != -1) {
                        selectionModel.addSelectionInterval(iConvertRowIndexToView, iConvertRowIndexToView);
                    }
                }
                int leadSelectionIndex = this.modelSelection.getLeadSelectionIndex();
                if (leadSelectionIndex != -1 && !this.modelSelection.isSelectionEmpty()) {
                    leadSelectionIndex = JTable.this.convertRowIndexToView(leadSelectionIndex);
                }
                SwingUtilities2.setLeadAnchorWithoutSelection(selectionModel, leadSelectionIndex, leadSelectionIndex);
                selectionModel.setValueIsAdjusting(false);
            }
            this.syncingSelection = false;
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$ModelChange.class */
    private final class ModelChange {
        int startModelIndex;
        int endModelIndex;
        int type;
        int modelRowCount;
        TableModelEvent event;
        int length;
        boolean allRowsChanged;

        ModelChange(TableModelEvent tableModelEvent) {
            this.startModelIndex = Math.max(0, tableModelEvent.getFirstRow());
            this.endModelIndex = tableModelEvent.getLastRow();
            this.modelRowCount = JTable.this.getModel().getRowCount();
            if (this.endModelIndex < 0) {
                this.endModelIndex = Math.max(0, this.modelRowCount - 1);
            }
            this.length = (this.endModelIndex - this.startModelIndex) + 1;
            this.type = tableModelEvent.getType();
            this.event = tableModelEvent;
            this.allRowsChanged = tableModelEvent.getLastRow() == Integer.MAX_VALUE;
        }
    }

    private void sortedTableChanged(RowSorterEvent rowSorterEvent, TableModelEvent tableModelEvent) {
        int iConvertRowIndexToModel = -1;
        ModelChange modelChange = tableModelEvent != null ? new ModelChange(tableModelEvent) : null;
        if ((modelChange == null || !modelChange.allRowsChanged) && this.editingRow != -1) {
            iConvertRowIndexToModel = convertRowIndexToModel(rowSorterEvent, this.editingRow);
        }
        this.sortManager.prepareForChange(rowSorterEvent, modelChange);
        if (tableModelEvent != null) {
            if (modelChange.type == 0) {
                repaintSortedRows(modelChange);
            }
            notifySorter(modelChange);
            if (modelChange.type != 0) {
                this.sorterChanged = true;
            }
        } else {
            this.sorterChanged = true;
        }
        this.sortManager.processChange(rowSorterEvent, modelChange, this.sorterChanged);
        if (this.sorterChanged) {
            if (this.editingRow != -1) {
                restoreSortingEditingRow(iConvertRowIndexToModel == -1 ? -1 : convertRowIndexToView(iConvertRowIndexToModel, modelChange));
            }
            if (tableModelEvent == null || modelChange.type != 0) {
                resizeAndRepaint();
            }
        }
        if (modelChange != null && modelChange.allRowsChanged) {
            clearSelectionAndLeadAnchor();
            resizeAndRepaint();
        }
    }

    private void repaintSortedRows(ModelChange modelChange) {
        int iConvertColumnIndexToView;
        if (modelChange.startModelIndex > modelChange.endModelIndex || modelChange.startModelIndex + 10 < modelChange.endModelIndex) {
            repaint();
            return;
        }
        int column = modelChange.event.getColumn();
        if (column == -1) {
            iConvertColumnIndexToView = 0;
        } else {
            iConvertColumnIndexToView = convertColumnIndexToView(column);
            if (iConvertColumnIndexToView == -1) {
                return;
            }
        }
        int i2 = modelChange.startModelIndex;
        while (i2 <= modelChange.endModelIndex) {
            int i3 = i2;
            i2++;
            int iConvertRowIndexToView = convertRowIndexToView(i3);
            if (iConvertRowIndexToView != -1) {
                Rectangle cellRect = getCellRect(iConvertRowIndexToView, iConvertColumnIndexToView, false);
                int i4 = cellRect.f12372x;
                int width = cellRect.width;
                if (column == -1) {
                    i4 = 0;
                    width = getWidth();
                }
                repaint(i4, cellRect.f12373y, width, cellRect.height);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void restoreSortingSelection(int[] iArr, int i2, ModelChange modelChange) {
        for (int length = iArr.length - 1; length >= 0; length--) {
            iArr[length] = convertRowIndexToView(iArr[length], modelChange);
        }
        int iConvertRowIndexToView = convertRowIndexToView(i2, modelChange);
        if (iArr.length != 0) {
            if (iArr.length == 1 && iArr[0] == getSelectedRow()) {
                return;
            }
            this.selectionModel.setValueIsAdjusting(true);
            this.selectionModel.clearSelection();
            for (int length2 = iArr.length - 1; length2 >= 0; length2--) {
                if (iArr[length2] != -1) {
                    this.selectionModel.addSelectionInterval(iArr[length2], iArr[length2]);
                }
            }
            SwingUtilities2.setLeadAnchorWithoutSelection(this.selectionModel, iConvertRowIndexToView, iConvertRowIndexToView);
            this.selectionModel.setValueIsAdjusting(false);
        }
    }

    private void restoreSortingEditingRow(int i2) {
        if (i2 == -1) {
            TableCellEditor cellEditor = getCellEditor();
            if (cellEditor != null) {
                cellEditor.cancelCellEditing();
                if (getCellEditor() != null) {
                    removeEditor();
                    return;
                }
                return;
            }
            return;
        }
        this.editingRow = i2;
        repaint();
    }

    private void notifySorter(ModelChange modelChange) {
        try {
            this.ignoreSortChange = true;
            this.sorterChanged = false;
            switch (modelChange.type) {
                case -1:
                    this.sortManager.sorter.rowsDeleted(modelChange.startModelIndex, modelChange.endModelIndex);
                    break;
                case 0:
                    if (modelChange.event.getLastRow() == Integer.MAX_VALUE) {
                        this.sortManager.sorter.allRowsChanged();
                        break;
                    } else if (modelChange.event.getColumn() == -1) {
                        this.sortManager.sorter.rowsUpdated(modelChange.startModelIndex, modelChange.endModelIndex);
                        break;
                    } else {
                        this.sortManager.sorter.rowsUpdated(modelChange.startModelIndex, modelChange.endModelIndex, modelChange.event.getColumn());
                        break;
                    }
                case 1:
                    this.sortManager.sorter.rowsInserted(modelChange.startModelIndex, modelChange.endModelIndex);
                    break;
            }
        } finally {
            this.ignoreSortChange = false;
        }
    }

    private int convertRowIndexToView(int i2, ModelChange modelChange) {
        if (i2 < 0) {
            return -1;
        }
        if (modelChange != null && i2 >= modelChange.startModelIndex) {
            if (modelChange.type == 1) {
                if (i2 + modelChange.length >= modelChange.modelRowCount) {
                    return -1;
                }
                return this.sortManager.sorter.convertRowIndexToView(i2 + modelChange.length);
            }
            if (modelChange.type == -1) {
                if (i2 <= modelChange.endModelIndex || i2 - modelChange.length >= modelChange.modelRowCount) {
                    return -1;
                }
                return this.sortManager.sorter.convertRowIndexToView(i2 - modelChange.length);
            }
        }
        if (i2 >= getModel().getRowCount()) {
            return -1;
        }
        return this.sortManager.sorter.convertRowIndexToView(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] convertSelectionToModel(RowSorterEvent rowSorterEvent) {
        int[] selectedRows = getSelectedRows();
        for (int length = selectedRows.length - 1; length >= 0; length--) {
            selectedRows[length] = convertRowIndexToModel(rowSorterEvent, selectedRows[length]);
        }
        return selectedRows;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int convertRowIndexToModel(RowSorterEvent rowSorterEvent, int i2) {
        if (rowSorterEvent != null) {
            if (rowSorterEvent.getPreviousRowCount() == 0) {
                return i2;
            }
            return rowSorterEvent.convertPreviousRowIndexToModel(i2);
        }
        if (i2 < 0 || i2 >= getRowCount()) {
            return -1;
        }
        return convertRowIndexToModel(i2);
    }

    @Override // javax.swing.event.TableModelListener
    public void tableChanged(TableModelEvent tableModelEvent) {
        Rectangle cellRect;
        if (tableModelEvent == null || tableModelEvent.getFirstRow() == -1) {
            clearSelectionAndLeadAnchor();
            this.rowModel = null;
            if (this.sortManager != null) {
                try {
                    this.ignoreSortChange = true;
                    this.sortManager.sorter.modelStructureChanged();
                    this.sortManager.allChanged();
                } finally {
                    this.ignoreSortChange = false;
                }
            }
            if (getAutoCreateColumnsFromModel()) {
                createDefaultColumnsFromModel();
                return;
            } else {
                resizeAndRepaint();
                return;
            }
        }
        if (this.sortManager != null) {
            sortedTableChanged(null, tableModelEvent);
            return;
        }
        if (this.rowModel != null) {
            repaint();
        }
        if (tableModelEvent.getType() == 1) {
            tableRowsInserted(tableModelEvent);
            return;
        }
        if (tableModelEvent.getType() == -1) {
            tableRowsDeleted(tableModelEvent);
            return;
        }
        int column = tableModelEvent.getColumn();
        int firstRow = tableModelEvent.getFirstRow();
        int lastRow = tableModelEvent.getLastRow();
        if (column == -1) {
            cellRect = new Rectangle(0, firstRow * getRowHeight(), getColumnModel().getTotalColumnWidth(), 0);
        } else {
            cellRect = getCellRect(firstRow, convertColumnIndexToView(column), false);
        }
        if (lastRow != Integer.MAX_VALUE) {
            cellRect.height = ((lastRow - firstRow) + 1) * getRowHeight();
            repaint(cellRect.f12372x, cellRect.f12373y, cellRect.width, cellRect.height);
        } else {
            clearSelectionAndLeadAnchor();
            resizeAndRepaint();
            this.rowModel = null;
        }
    }

    private void tableRowsInserted(TableModelEvent tableModelEvent) {
        int firstRow = tableModelEvent.getFirstRow();
        int lastRow = tableModelEvent.getLastRow();
        if (firstRow < 0) {
            firstRow = 0;
        }
        if (lastRow < 0) {
            lastRow = getRowCount() - 1;
        }
        int i2 = (lastRow - firstRow) + 1;
        this.selectionModel.insertIndexInterval(firstRow, i2, true);
        if (this.rowModel != null) {
            this.rowModel.insertEntries(firstRow, i2, getRowHeight());
        }
        int rowHeight = getRowHeight();
        Rectangle rectangle = new Rectangle(0, firstRow * rowHeight, getColumnModel().getTotalColumnWidth(), (getRowCount() - firstRow) * rowHeight);
        revalidate();
        repaint(rectangle);
    }

    private void tableRowsDeleted(TableModelEvent tableModelEvent) {
        int firstRow = tableModelEvent.getFirstRow();
        int lastRow = tableModelEvent.getLastRow();
        if (firstRow < 0) {
            firstRow = 0;
        }
        if (lastRow < 0) {
            lastRow = getRowCount() - 1;
        }
        int i2 = (lastRow - firstRow) + 1;
        int rowCount = getRowCount() + i2;
        this.selectionModel.removeIndexInterval(firstRow, lastRow);
        if (this.rowModel != null) {
            this.rowModel.removeEntries(firstRow, i2);
        }
        int rowHeight = getRowHeight();
        Rectangle rectangle = new Rectangle(0, firstRow * rowHeight, getColumnModel().getTotalColumnWidth(), (rowCount - firstRow) * rowHeight);
        revalidate();
        repaint(rectangle);
    }

    public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
        if (isEditing()) {
            removeEditor();
        }
        resizeAndRepaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
        if (isEditing()) {
            removeEditor();
        }
        resizeAndRepaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
        if (isEditing() && !getCellEditor().stopCellEditing()) {
            getCellEditor().cancelCellEditing();
        }
        repaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnMarginChanged(ChangeEvent changeEvent) {
        if (isEditing() && !getCellEditor().stopCellEditing()) {
            getCellEditor().cancelCellEditing();
        }
        TableColumn resizingColumn = getResizingColumn();
        if (resizingColumn != null && this.autoResizeMode == 0) {
            resizingColumn.setPreferredWidth(resizingColumn.getWidth());
        }
        resizeAndRepaint();
    }

    private int limit(int i2, int i3, int i4) {
        return Math.min(i4, Math.max(i2, i3));
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
        boolean valueIsAdjusting = listSelectionEvent.getValueIsAdjusting();
        if (this.columnSelectionAdjusting && !valueIsAdjusting) {
            this.columnSelectionAdjusting = false;
            return;
        }
        this.columnSelectionAdjusting = valueIsAdjusting;
        if (getRowCount() <= 0 || getColumnCount() <= 0) {
            return;
        }
        int iLimit = limit(listSelectionEvent.getFirstIndex(), 0, getColumnCount() - 1);
        int iLimit2 = limit(listSelectionEvent.getLastIndex(), 0, getColumnCount() - 1);
        int minSelectionIndex = 0;
        int rowCount = getRowCount() - 1;
        if (getRowSelectionAllowed()) {
            minSelectionIndex = this.selectionModel.getMinSelectionIndex();
            rowCount = this.selectionModel.getMaxSelectionIndex();
            int adjustedIndex = getAdjustedIndex(this.selectionModel.getLeadSelectionIndex(), true);
            if (minSelectionIndex == -1 || rowCount == -1) {
                if (adjustedIndex == -1) {
                    return;
                }
                rowCount = adjustedIndex;
                minSelectionIndex = adjustedIndex;
            } else if (adjustedIndex != -1) {
                minSelectionIndex = Math.min(minSelectionIndex, adjustedIndex);
                rowCount = Math.max(rowCount, adjustedIndex);
            }
        }
        repaint(getCellRect(minSelectionIndex, iLimit, false).union(getCellRect(rowCount, iLimit2, false)));
    }

    @Override // javax.swing.event.ListSelectionListener
    public void valueChanged(ListSelectionEvent listSelectionEvent) {
        if (this.sortManager != null) {
            this.sortManager.viewSelectionChanged(listSelectionEvent);
        }
        boolean valueIsAdjusting = listSelectionEvent.getValueIsAdjusting();
        if (this.rowSelectionAdjusting && !valueIsAdjusting) {
            this.rowSelectionAdjusting = false;
            return;
        }
        this.rowSelectionAdjusting = valueIsAdjusting;
        if (getRowCount() <= 0 || getColumnCount() <= 0) {
            return;
        }
        repaint(getCellRect(limit(listSelectionEvent.getFirstIndex(), 0, getRowCount() - 1), 0, false).union(getCellRect(limit(listSelectionEvent.getLastIndex(), 0, getRowCount() - 1), getColumnCount() - 1, false)));
    }

    @Override // javax.swing.event.CellEditorListener
    public void editingStopped(ChangeEvent changeEvent) {
        TableCellEditor cellEditor = getCellEditor();
        if (cellEditor != null) {
            setValueAt(cellEditor.getCellEditorValue(), this.editingRow, this.editingColumn);
            removeEditor();
        }
    }

    @Override // javax.swing.event.CellEditorListener
    public void editingCanceled(ChangeEvent changeEvent) {
        removeEditor();
    }

    public void setPreferredScrollableViewportSize(Dimension dimension) {
        this.preferredViewportSize = dimension;
    }

    @Override // javax.swing.Scrollable
    public Dimension getPreferredScrollableViewportSize() {
        return this.preferredViewportSize;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableUnitIncrement(Rectangle rectangle, int i2, int i3) {
        int i4;
        int leadingRow = getLeadingRow(rectangle);
        int leadingCol = getLeadingCol(rectangle);
        if (i2 == 1 && leadingRow < 0) {
            return getRowHeight();
        }
        if (i2 == 0 && leadingCol < 0) {
            return 100;
        }
        Rectangle cellRect = getCellRect(leadingRow, leadingCol, true);
        int iLeadingEdge = leadingEdge(rectangle, i2);
        int iLeadingEdge2 = leadingEdge(cellRect, i2);
        if (i2 == 1) {
            i4 = cellRect.height;
        } else {
            i4 = cellRect.width;
        }
        if (iLeadingEdge == iLeadingEdge2) {
            if (i3 < 0) {
                int rowHeight = 0;
                if (i2 == 1) {
                    do {
                        leadingRow--;
                        if (leadingRow < 0) {
                            break;
                        }
                        rowHeight = getRowHeight(leadingRow);
                    } while (rowHeight == 0);
                } else {
                    do {
                        leadingCol--;
                        if (leadingCol < 0) {
                            break;
                        }
                        rowHeight = getCellRect(leadingRow, leadingCol, true).width;
                    } while (rowHeight == 0);
                }
                return rowHeight;
            }
            return i4;
        }
        int iAbs = Math.abs(iLeadingEdge - iLeadingEdge2);
        int i5 = i4 - iAbs;
        if (i3 > 0) {
            return i5;
        }
        return iAbs;
    }

    @Override // javax.swing.Scrollable
    public int getScrollableBlockIncrement(Rectangle rectangle, int i2, int i3) {
        if (getRowCount() == 0) {
            if (1 == i2) {
                int rowHeight = getRowHeight();
                return rowHeight > 0 ? Math.max(rowHeight, (rectangle.height / rowHeight) * rowHeight) : rectangle.height;
            }
            return rectangle.width;
        }
        if (null == this.rowModel && 1 == i2) {
            int iRowAtPoint = rowAtPoint(rectangle.getLocation());
            if (!$assertionsDisabled && iRowAtPoint == -1) {
                throw new AssertionError();
            }
            if (getCellRect(iRowAtPoint, columnAtPoint(rectangle.getLocation()), true).f12373y == rectangle.f12373y) {
                int rowHeight2 = getRowHeight();
                if ($assertionsDisabled || rowHeight2 > 0) {
                    return Math.max(rowHeight2, (rectangle.height / rowHeight2) * rowHeight2);
                }
                throw new AssertionError();
            }
        }
        if (i3 < 0) {
            return getPreviousBlockIncrement(rectangle, i2);
        }
        return getNextBlockIncrement(rectangle, i2);
    }

    private int getPreviousBlockIncrement(Rectangle rectangle, int i2) {
        int i3;
        Point point;
        int width;
        int iLeadingEdge = leadingEdge(rectangle, i2);
        boolean zIsLeftToRight = getComponentOrientation().isLeftToRight();
        if (i2 == 1) {
            i3 = iLeadingEdge - rectangle.height;
            point = new Point(rectangle.f12372x + (zIsLeftToRight ? 0 : rectangle.width), i3);
        } else if (zIsLeftToRight) {
            i3 = iLeadingEdge - rectangle.width;
            point = new Point(i3, rectangle.f12373y);
        } else {
            i3 = iLeadingEdge + rectangle.width;
            point = new Point(i3 - 1, rectangle.f12373y);
        }
        int iRowAtPoint = rowAtPoint(point);
        int iColumnAtPoint = columnAtPoint(point);
        if ((i2 == 1) && (iRowAtPoint < 0)) {
            width = 0;
        } else {
            if ((i2 == 0) & (iColumnAtPoint < 0)) {
                if (zIsLeftToRight) {
                    width = 0;
                } else {
                    width = getWidth();
                }
            } else {
                Rectangle cellRect = getCellRect(iRowAtPoint, iColumnAtPoint, true);
                int iLeadingEdge2 = leadingEdge(cellRect, i2);
                int iTrailingEdge = trailingEdge(cellRect, i2);
                if ((i2 == 1 || zIsLeftToRight) && iTrailingEdge >= iLeadingEdge) {
                    width = iLeadingEdge2;
                } else if ((i2 == 0 && !zIsLeftToRight && iTrailingEdge <= iLeadingEdge) || i3 == iLeadingEdge2) {
                    width = iLeadingEdge2;
                } else {
                    width = iTrailingEdge;
                }
            }
        }
        return Math.abs(iLeadingEdge - width);
    }

    private int getNextBlockIncrement(Rectangle rectangle, int i2) {
        boolean z2;
        int i3;
        int trailingRow = getTrailingRow(rectangle);
        int trailingCol = getTrailingCol(rectangle);
        int iLeadingEdge = leadingEdge(rectangle, i2);
        if (i2 == 1 && trailingRow < 0) {
            return rectangle.height;
        }
        if (i2 == 0 && trailingCol < 0) {
            return rectangle.width;
        }
        Rectangle cellRect = getCellRect(trailingRow, trailingCol, true);
        int iLeadingEdge2 = leadingEdge(cellRect, i2);
        int iTrailingEdge = trailingEdge(cellRect, i2);
        if (i2 == 1 || getComponentOrientation().isLeftToRight()) {
            z2 = iLeadingEdge2 <= iLeadingEdge;
        } else {
            z2 = iLeadingEdge2 >= iLeadingEdge;
        }
        if (z2 || iTrailingEdge == trailingEdge(rectangle, i2)) {
            i3 = iTrailingEdge;
        } else {
            i3 = iLeadingEdge2;
        }
        return Math.abs(i3 - iLeadingEdge);
    }

    private int getLeadingRow(Rectangle rectangle) {
        Point point;
        if (getComponentOrientation().isLeftToRight()) {
            point = new Point(rectangle.f12372x, rectangle.f12373y);
        } else {
            point = new Point((rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y);
        }
        return rowAtPoint(point);
    }

    private int getLeadingCol(Rectangle rectangle) {
        Point point;
        if (getComponentOrientation().isLeftToRight()) {
            point = new Point(rectangle.f12372x, rectangle.f12373y);
        } else {
            point = new Point((rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y);
        }
        return columnAtPoint(point);
    }

    private int getTrailingRow(Rectangle rectangle) {
        Point point;
        if (getComponentOrientation().isLeftToRight()) {
            point = new Point(rectangle.f12372x, (rectangle.f12373y + rectangle.height) - 1);
        } else {
            point = new Point((rectangle.f12372x + rectangle.width) - 1, (rectangle.f12373y + rectangle.height) - 1);
        }
        return rowAtPoint(point);
    }

    private int getTrailingCol(Rectangle rectangle) {
        Point point;
        if (getComponentOrientation().isLeftToRight()) {
            point = new Point((rectangle.f12372x + rectangle.width) - 1, rectangle.f12373y);
        } else {
            point = new Point(rectangle.f12372x, rectangle.f12373y);
        }
        return columnAtPoint(point);
    }

    private int leadingEdge(Rectangle rectangle, int i2) {
        if (i2 == 1) {
            return rectangle.f12373y;
        }
        if (getComponentOrientation().isLeftToRight()) {
            return rectangle.f12372x;
        }
        return rectangle.f12372x + rectangle.width;
    }

    private int trailingEdge(Rectangle rectangle, int i2) {
        if (i2 == 1) {
            return rectangle.f12373y + rectangle.height;
        }
        if (getComponentOrientation().isLeftToRight()) {
            return rectangle.f12372x + rectangle.width;
        }
        return rectangle.f12372x;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportWidth() {
        return this.autoResizeMode != 0;
    }

    @Override // javax.swing.Scrollable
    public boolean getScrollableTracksViewportHeight() {
        Container unwrappedParent = SwingUtilities.getUnwrappedParent(this);
        return getFillsViewportHeight() && (unwrappedParent instanceof JViewport) && unwrappedParent.getHeight() > getPreferredSize().height;
    }

    public void setFillsViewportHeight(boolean z2) {
        boolean z3 = this.fillsViewportHeight;
        this.fillsViewportHeight = z2;
        resizeAndRepaint();
        firePropertyChange("fillsViewportHeight", z3, z2);
    }

    public boolean getFillsViewportHeight() {
        return this.fillsViewportHeight;
    }

    @Override // javax.swing.JComponent
    protected boolean processKeyBinding(KeyStroke keyStroke, KeyEvent keyEvent, int i2, boolean z2) {
        int keyCode;
        boolean zProcessKeyBinding = super.processKeyBinding(keyStroke, keyEvent, i2, z2);
        if (!zProcessKeyBinding && i2 == 1 && isFocusOwner() && !Boolean.FALSE.equals(getClientProperty("JTable.autoStartsEdit"))) {
            Component editorComponent = getEditorComponent();
            if (editorComponent == null) {
                if (keyEvent == null || keyEvent.getID() != 401 || (keyCode = keyEvent.getKeyCode()) == 16 || keyCode == 17 || keyCode == 18) {
                    return false;
                }
                int leadSelectionIndex = getSelectionModel().getLeadSelectionIndex();
                int leadSelectionIndex2 = getColumnModel().getSelectionModel().getLeadSelectionIndex();
                if (leadSelectionIndex != -1 && leadSelectionIndex2 != -1 && !isEditing() && !editCellAt(leadSelectionIndex, leadSelectionIndex2, keyEvent)) {
                    return false;
                }
                editorComponent = getEditorComponent();
                if (editorComponent == null) {
                    return false;
                }
            }
            if (editorComponent instanceof JComponent) {
                zProcessKeyBinding = ((JComponent) editorComponent).processKeyBinding(keyStroke, keyEvent, 0, z2);
                if (getSurrendersFocusOnKeystroke()) {
                    editorComponent.requestFocus();
                }
            }
        }
        return zProcessKeyBinding;
    }

    protected void createDefaultRenderers() {
        this.defaultRenderersByColumnClass = new UIDefaults(8, 0.75f);
        this.defaultRenderersByColumnClass.put(Object.class, uIDefaults -> {
            return new DefaultTableCellRenderer.UIResource();
        });
        this.defaultRenderersByColumnClass.put(Number.class, uIDefaults2 -> {
            return new NumberRenderer();
        });
        this.defaultRenderersByColumnClass.put(Float.class, uIDefaults3 -> {
            return new DoubleRenderer();
        });
        this.defaultRenderersByColumnClass.put(Double.class, uIDefaults4 -> {
            return new DoubleRenderer();
        });
        this.defaultRenderersByColumnClass.put(Date.class, uIDefaults5 -> {
            return new DateRenderer();
        });
        this.defaultRenderersByColumnClass.put(Icon.class, uIDefaults6 -> {
            return new IconRenderer();
        });
        this.defaultRenderersByColumnClass.put(ImageIcon.class, uIDefaults7 -> {
            return new IconRenderer();
        });
        this.defaultRenderersByColumnClass.put(Boolean.class, uIDefaults8 -> {
            return new BooleanRenderer();
        });
    }

    /* loaded from: rt.jar:javax/swing/JTable$NumberRenderer.class */
    static class NumberRenderer extends DefaultTableCellRenderer.UIResource {
        public NumberRenderer() {
            setHorizontalAlignment(4);
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$DoubleRenderer.class */
    static class DoubleRenderer extends NumberRenderer {
        NumberFormat formatter;

        @Override // javax.swing.table.DefaultTableCellRenderer
        public void setValue(Object obj) {
            if (this.formatter == null) {
                this.formatter = NumberFormat.getInstance();
            }
            setText(obj == null ? "" : this.formatter.format(obj));
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$DateRenderer.class */
    static class DateRenderer extends DefaultTableCellRenderer.UIResource {
        DateFormat formatter;

        @Override // javax.swing.table.DefaultTableCellRenderer
        public void setValue(Object obj) {
            if (this.formatter == null) {
                this.formatter = DateFormat.getDateInstance();
            }
            setText(obj == null ? "" : this.formatter.format(obj));
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$IconRenderer.class */
    static class IconRenderer extends DefaultTableCellRenderer.UIResource {
        public IconRenderer() {
            setHorizontalAlignment(0);
        }

        @Override // javax.swing.table.DefaultTableCellRenderer
        public void setValue(Object obj) {
            setIcon(obj instanceof Icon ? (Icon) obj : null);
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$BooleanRenderer.class */
    static class BooleanRenderer extends JCheckBox implements TableCellRenderer, UIResource {
        private static final Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);

        public BooleanRenderer() {
            setHorizontalAlignment(0);
            setBorderPainted(true);
        }

        @Override // javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            if (z2) {
                setForeground(jTable.getSelectionForeground());
                super.setBackground(jTable.getSelectionBackground());
            } else {
                setForeground(jTable.getForeground());
                setBackground(jTable.getBackground());
            }
            setSelected(obj != null && ((Boolean) obj).booleanValue());
            if (z3) {
                setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
            } else {
                setBorder(noFocusBorder);
            }
            return this;
        }
    }

    protected void createDefaultEditors() {
        this.defaultEditorsByColumnClass = new UIDefaults(3, 0.75f);
        this.defaultEditorsByColumnClass.put(Object.class, uIDefaults -> {
            return new GenericEditor();
        });
        this.defaultEditorsByColumnClass.put(Number.class, uIDefaults2 -> {
            return new NumberEditor();
        });
        this.defaultEditorsByColumnClass.put(Boolean.class, uIDefaults3 -> {
            return new BooleanEditor();
        });
    }

    /* loaded from: rt.jar:javax/swing/JTable$GenericEditor.class */
    static class GenericEditor extends DefaultCellEditor {
        Class[] argTypes;
        Constructor constructor;
        Object value;

        public GenericEditor() {
            super(new JTextField());
            this.argTypes = new Class[]{String.class};
            getComponent().setName("Table.editor");
        }

        @Override // javax.swing.DefaultCellEditor, javax.swing.AbstractCellEditor, javax.swing.CellEditor
        public boolean stopCellEditing() {
            String str = (String) super.getCellEditorValue();
            try {
                if ("".equals(str)) {
                    if (this.constructor.getDeclaringClass() == String.class) {
                        this.value = str;
                    }
                    return super.stopCellEditing();
                }
                SwingUtilities2.checkAccess(this.constructor.getModifiers());
                this.value = this.constructor.newInstance(str);
                return super.stopCellEditing();
            } catch (Exception e2) {
                ((JComponent) getComponent()).setBorder(new LineBorder(Color.red));
                return false;
            }
        }

        @Override // javax.swing.DefaultCellEditor, javax.swing.table.TableCellEditor
        public Component getTableCellEditorComponent(JTable jTable, Object obj, boolean z2, int i2, int i3) {
            this.value = null;
            ((JComponent) getComponent()).setBorder(new LineBorder(Color.black));
            try {
                Class<?> columnClass = jTable.getColumnClass(i3);
                if (columnClass == Object.class) {
                    columnClass = String.class;
                }
                ReflectUtil.checkPackageAccess(columnClass);
                SwingUtilities2.checkAccess(columnClass.getModifiers());
                this.constructor = columnClass.getConstructor(this.argTypes);
                return super.getTableCellEditorComponent(jTable, obj, z2, i2, i3);
            } catch (Exception e2) {
                return null;
            }
        }

        @Override // javax.swing.DefaultCellEditor, javax.swing.CellEditor
        public Object getCellEditorValue() {
            return this.value;
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$NumberEditor.class */
    static class NumberEditor extends GenericEditor {
        public NumberEditor() {
            ((JTextField) getComponent()).setHorizontalAlignment(4);
        }
    }

    /* loaded from: rt.jar:javax/swing/JTable$BooleanEditor.class */
    static class BooleanEditor extends DefaultCellEditor {
        public BooleanEditor() {
            super(new JCheckBox());
            ((JCheckBox) getComponent()).setHorizontalAlignment(0);
        }
    }

    protected void initializeLocalVars() {
        this.updateSelectionOnSort = true;
        setOpaque(true);
        createDefaultRenderers();
        createDefaultEditors();
        setTableHeader(createDefaultTableHeader());
        setShowGrid(true);
        setAutoResizeMode(2);
        setRowHeight(16);
        this.isRowHeightSet = false;
        setRowMargin(1);
        setRowSelectionAllowed(true);
        setCellEditor(null);
        setEditingColumn(-1);
        setEditingRow(-1);
        setSurrendersFocusOnKeystroke(false);
        setPreferredScrollableViewportSize(new Dimension(450, 400));
        ToolTipManager.sharedInstance().registerComponent(this);
        setAutoscrolls(true);
    }

    protected TableModel createDefaultDataModel() {
        return new DefaultTableModel();
    }

    protected TableColumnModel createDefaultColumnModel() {
        return new DefaultTableColumnModel();
    }

    protected ListSelectionModel createDefaultSelectionModel() {
        return new DefaultListSelectionModel();
    }

    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(this.columnModel);
    }

    protected void resizeAndRepaint() {
        revalidate();
        repaint();
    }

    public TableCellEditor getCellEditor() {
        return this.cellEditor;
    }

    public void setCellEditor(TableCellEditor tableCellEditor) {
        TableCellEditor tableCellEditor2 = this.cellEditor;
        this.cellEditor = tableCellEditor;
        firePropertyChange("tableCellEditor", tableCellEditor2, tableCellEditor);
    }

    public void setEditingColumn(int i2) {
        this.editingColumn = i2;
    }

    public void setEditingRow(int i2) {
        this.editingRow = i2;
    }

    public TableCellRenderer getCellRenderer(int i2, int i3) {
        TableCellRenderer cellRenderer = getColumnModel().getColumn(i3).getCellRenderer();
        if (cellRenderer == null) {
            cellRenderer = getDefaultRenderer(getColumnClass(i3));
        }
        return cellRenderer;
    }

    public Component prepareRenderer(TableCellRenderer tableCellRenderer, int i2, int i3) {
        Object valueAt = getValueAt(i2, i3);
        boolean zIsCellSelected = false;
        boolean z2 = false;
        if (!isPaintingForPrint()) {
            zIsCellSelected = isCellSelected(i2, i3);
            z2 = (this.selectionModel.getLeadSelectionIndex() == i2) && (this.columnModel.getSelectionModel().getLeadSelectionIndex() == i3) && isFocusOwner();
        }
        return tableCellRenderer.getTableCellRendererComponent(this, valueAt, zIsCellSelected, z2, i2, i3);
    }

    public TableCellEditor getCellEditor(int i2, int i3) {
        TableCellEditor cellEditor = getColumnModel().getColumn(i3).getCellEditor();
        if (cellEditor == null) {
            cellEditor = getDefaultEditor(getColumnClass(i3));
        }
        return cellEditor;
    }

    public Component prepareEditor(TableCellEditor tableCellEditor, int i2, int i3) {
        Component tableCellEditorComponent = tableCellEditor.getTableCellEditorComponent(this, getValueAt(i2, i3), isCellSelected(i2, i3), i2, i3);
        if (tableCellEditorComponent instanceof JComponent) {
            JComponent jComponent = (JComponent) tableCellEditorComponent;
            if (jComponent.getNextFocusableComponent() == null) {
                jComponent.setNextFocusableComponent(this);
            }
        }
        return tableCellEditorComponent;
    }

    public void removeEditor() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().removePropertyChangeListener("permanentFocusOwner", this.editorRemover);
        this.editorRemover = null;
        TableCellEditor cellEditor = getCellEditor();
        if (cellEditor != null) {
            cellEditor.removeCellEditorListener(this);
            if (this.editorComp != null) {
                Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
                boolean zIsDescendingFrom = focusOwner != null ? SwingUtilities.isDescendingFrom(focusOwner, this) : false;
                remove(this.editorComp);
                if (zIsDescendingFrom) {
                    requestFocusInWindow();
                }
            }
            Rectangle cellRect = getCellRect(this.editingRow, this.editingColumn, false);
            setCellEditor(null);
            setEditingColumn(-1);
            setEditingRow(-1);
            this.editorComp = null;
            repaint(cellRect);
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (this.ui != null && getUIClassID().equals(uiClassID)) {
            this.ui.installUI(this);
        }
        createDefaultRenderers();
        createDefaultEditors();
        if (getToolTipText() == null) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    @Override // javax.swing.JComponent
    void compWriteObjectNotify() {
        super.compWriteObjectNotify();
        if (getToolTipText() == null) {
            ToolTipManager.sharedInstance().unregisterComponent(this);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        String str;
        String string = this.gridColor != null ? this.gridColor.toString() : "";
        String str2 = this.showHorizontalLines ? "true" : "false";
        String str3 = this.showVerticalLines ? "true" : "false";
        if (this.autoResizeMode == 0) {
            str = "AUTO_RESIZE_OFF";
        } else if (this.autoResizeMode == 1) {
            str = "AUTO_RESIZE_NEXT_COLUMN";
        } else if (this.autoResizeMode == 2) {
            str = "AUTO_RESIZE_SUBSEQUENT_COLUMNS";
        } else if (this.autoResizeMode == 3) {
            str = "AUTO_RESIZE_LAST_COLUMN";
        } else if (this.autoResizeMode == 4) {
            str = "AUTO_RESIZE_ALL_COLUMNS";
        } else {
            str = "";
        }
        return super.paramString() + ",autoCreateColumnsFromModel=" + (this.autoCreateColumnsFromModel ? "true" : "false") + ",autoResizeMode=" + str + ",cellSelectionEnabled=" + (this.cellSelectionEnabled ? "true" : "false") + ",editingColumn=" + this.editingColumn + ",editingRow=" + this.editingRow + ",gridColor=" + string + ",preferredViewportSize=" + (this.preferredViewportSize != null ? this.preferredViewportSize.toString() : "") + ",rowHeight=" + this.rowHeight + ",rowMargin=" + this.rowMargin + ",rowSelectionAllowed=" + (this.rowSelectionAllowed ? "true" : "false") + ",selectionBackground=" + (this.selectionBackground != null ? this.selectionBackground.toString() : "") + ",selectionForeground=" + (this.selectionForeground != null ? this.selectionForeground.toString() : "") + ",showHorizontalLines=" + str2 + ",showVerticalLines=" + str3;
    }

    /* loaded from: rt.jar:javax/swing/JTable$CellEditorRemover.class */
    class CellEditorRemover implements PropertyChangeListener {
        KeyboardFocusManager focusManager;

        public CellEditorRemover(KeyboardFocusManager keyboardFocusManager) {
            this.focusManager = keyboardFocusManager;
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Component component;
            if (!JTable.this.isEditing() || JTable.this.getClientProperty("terminateEditOnFocusLost") != Boolean.TRUE) {
                return;
            }
            Component permanentFocusOwner = this.focusManager.getPermanentFocusOwner();
            while (true) {
                component = permanentFocusOwner;
                if (component == null || component == JTable.this) {
                    return;
                }
                if ((component instanceof Window) || ((component instanceof Applet) && component.getParent() == null)) {
                    break;
                } else {
                    permanentFocusOwner = component.getParent();
                }
            }
            if (component == SwingUtilities.getRoot(JTable.this) && !JTable.this.getCellEditor().stopCellEditing()) {
                JTable.this.getCellEditor().cancelCellEditing();
            }
        }
    }

    public boolean print() throws PrinterException {
        return print(PrintMode.FIT_WIDTH);
    }

    public boolean print(PrintMode printMode) throws PrinterException {
        return print(printMode, null, null);
    }

    public boolean print(PrintMode printMode, MessageFormat messageFormat, MessageFormat messageFormat2) throws PrinterException {
        boolean z2 = !GraphicsEnvironment.isHeadless();
        return print(printMode, messageFormat, messageFormat2, z2, null, z2);
    }

    public boolean print(PrintMode printMode, MessageFormat messageFormat, MessageFormat messageFormat2, boolean z2, PrintRequestAttributeSet printRequestAttributeSet, boolean z3) throws PrinterException, HeadlessException {
        return print(printMode, messageFormat, messageFormat2, z2, printRequestAttributeSet, z3, null);
    }

    public boolean print(PrintMode printMode, MessageFormat messageFormat, MessageFormat messageFormat2, boolean z2, PrintRequestAttributeSet printRequestAttributeSet, boolean z3, PrintService printService) throws PrinterException, HeadlessException {
        PrintingStatus printingStatusCreatePrintingStatus;
        Throwable th;
        if (GraphicsEnvironment.isHeadless()) {
            if (z2) {
                throw new HeadlessException("Can't show print dialog.");
            }
            if (z3) {
                throw new HeadlessException("Can't run interactively.");
            }
        }
        final PrinterJob printerJob = PrinterJob.getPrinterJob();
        if (isEditing() && !getCellEditor().stopCellEditing()) {
            getCellEditor().cancelCellEditing();
        }
        if (printRequestAttributeSet == null) {
            printRequestAttributeSet = new HashPrintRequestAttributeSet();
        }
        Printable printable = getPrintable(printMode, messageFormat, messageFormat2);
        if (z3) {
            ThreadSafePrintable threadSafePrintable = new ThreadSafePrintable(printable);
            printingStatusCreatePrintingStatus = PrintingStatus.createPrintingStatus(this, printerJob);
            printable = printingStatusCreatePrintingStatus.createNotificationPrintable(threadSafePrintable);
        } else {
            printingStatusCreatePrintingStatus = null;
        }
        printerJob.setPrintable(printable);
        if (printService != null) {
            printerJob.setPrintService(printService);
        }
        if (z2 && !printerJob.printDialog(printRequestAttributeSet)) {
            return false;
        }
        if (!z3) {
            printerJob.print(printRequestAttributeSet);
            return true;
        }
        this.printError = null;
        final Object obj = new Object();
        final PrintRequestAttributeSet printRequestAttributeSet2 = printRequestAttributeSet;
        final PrintingStatus printingStatus = printingStatusCreatePrintingStatus;
        new Thread(new Runnable() { // from class: javax.swing.JTable.6
            @Override // java.lang.Runnable
            public void run() {
                try {
                    try {
                        printerJob.print(printRequestAttributeSet2);
                        printingStatus.dispose();
                    } catch (Throwable th2) {
                        synchronized (obj) {
                            JTable.this.printError = th2;
                            printingStatus.dispose();
                        }
                    }
                } catch (Throwable th3) {
                    printingStatus.dispose();
                    throw th3;
                }
            }
        }).start();
        printingStatusCreatePrintingStatus.showModal(true);
        synchronized (obj) {
            th = this.printError;
            this.printError = null;
        }
        if (th != null) {
            if (th instanceof PrinterAbortException) {
                return false;
            }
            if (th instanceof PrinterException) {
                throw ((PrinterException) th);
            }
            if (th instanceof RuntimeException) {
                throw ((RuntimeException) th);
            }
            if (th instanceof Error) {
                throw ((Error) th);
            }
            throw new AssertionError(th);
        }
        return true;
    }

    public Printable getPrintable(PrintMode printMode, MessageFormat messageFormat, MessageFormat messageFormat2) {
        return new TablePrintable(this, printMode, messageFormat, messageFormat2);
    }

    /* loaded from: rt.jar:javax/swing/JTable$ThreadSafePrintable.class */
    private class ThreadSafePrintable implements Printable {
        private Printable printDelegate;
        private int retVal;
        private Throwable retThrowable;

        public ThreadSafePrintable(Printable printable) {
            this.printDelegate = printable;
        }

        @Override // java.awt.print.Printable
        public int print(final Graphics graphics, final PageFormat pageFormat, final int i2) throws PrinterException {
            int i3;
            Runnable runnable = new Runnable() { // from class: javax.swing.JTable.ThreadSafePrintable.1
                @Override // java.lang.Runnable
                public synchronized void run() {
                    try {
                        ThreadSafePrintable.this.retVal = ThreadSafePrintable.this.printDelegate.print(graphics, pageFormat, i2);
                    } catch (Throwable th) {
                        ThreadSafePrintable.this.retThrowable = th;
                    } finally {
                        notifyAll();
                    }
                }
            };
            synchronized (runnable) {
                this.retVal = -1;
                this.retThrowable = null;
                SwingUtilities.invokeLater(runnable);
                while (this.retVal == -1 && this.retThrowable == null) {
                    try {
                        runnable.wait();
                    } catch (InterruptedException e2) {
                    }
                }
                if (this.retThrowable != null) {
                    if (this.retThrowable instanceof PrinterException) {
                        throw ((PrinterException) this.retThrowable);
                    }
                    if (this.retThrowable instanceof RuntimeException) {
                        throw ((RuntimeException) this.retThrowable);
                    }
                    if (this.retThrowable instanceof Error) {
                        throw ((Error) this.retThrowable);
                    }
                    throw new AssertionError(this.retThrowable);
                }
                i3 = this.retVal;
            }
            return i3;
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTable();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JTable$AccessibleJTable.class */
    protected class AccessibleJTable extends JComponent.AccessibleJComponent implements AccessibleSelection, ListSelectionListener, TableModelListener, TableColumnModelListener, CellEditorListener, PropertyChangeListener, AccessibleExtendedTable {
        int previousFocusedRow;
        int previousFocusedCol;
        private Accessible caption;
        private Accessible summary;
        private Accessible[] rowDescription;
        private Accessible[] columnDescription;

        protected AccessibleJTable() {
            super();
            JTable.this.addPropertyChangeListener(this);
            JTable.this.getSelectionModel().addListSelectionListener(this);
            TableColumnModel columnModel = JTable.this.getColumnModel();
            columnModel.addColumnModelListener(this);
            columnModel.getSelectionModel().addListSelectionListener(this);
            JTable.this.getModel().addTableModelListener(this);
            this.previousFocusedRow = JTable.this.getSelectionModel().getLeadSelectionIndex();
            this.previousFocusedCol = JTable.this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            String propertyName = propertyChangeEvent.getPropertyName();
            Object oldValue = propertyChangeEvent.getOldValue();
            Object newValue = propertyChangeEvent.getNewValue();
            if (propertyName.compareTo("model") == 0) {
                if (oldValue != null && (oldValue instanceof TableModel)) {
                    ((TableModel) oldValue).removeTableModelListener(this);
                }
                if (newValue != null && (newValue instanceof TableModel)) {
                    ((TableModel) newValue).addTableModelListener(this);
                    return;
                }
                return;
            }
            if (propertyName.compareTo("selectionModel") == 0) {
                Object source = propertyChangeEvent.getSource();
                if (source == JTable.this) {
                    if (oldValue != null && (oldValue instanceof ListSelectionModel)) {
                        ((ListSelectionModel) oldValue).removeListSelectionListener(this);
                    }
                    if (newValue != null && (newValue instanceof ListSelectionModel)) {
                        ((ListSelectionModel) newValue).addListSelectionListener(this);
                        return;
                    }
                    return;
                }
                if (source == JTable.this.getColumnModel()) {
                    if (oldValue != null && (oldValue instanceof ListSelectionModel)) {
                        ((ListSelectionModel) oldValue).removeListSelectionListener(this);
                    }
                    if (newValue != null && (newValue instanceof ListSelectionModel)) {
                        ((ListSelectionModel) newValue).addListSelectionListener(this);
                        return;
                    }
                    return;
                }
                return;
            }
            if (propertyName.compareTo("columnModel") == 0) {
                if (oldValue != null && (oldValue instanceof TableColumnModel)) {
                    TableColumnModel tableColumnModel = (TableColumnModel) oldValue;
                    tableColumnModel.removeColumnModelListener(this);
                    tableColumnModel.getSelectionModel().removeListSelectionListener(this);
                }
                if (newValue != null && (newValue instanceof TableColumnModel)) {
                    TableColumnModel tableColumnModel2 = (TableColumnModel) newValue;
                    tableColumnModel2.addColumnModelListener(this);
                    tableColumnModel2.getSelectionModel().addListSelectionListener(this);
                    return;
                }
                return;
            }
            if (propertyName.compareTo("tableCellEditor") == 0) {
                if (oldValue != null && (oldValue instanceof TableCellEditor)) {
                    ((TableCellEditor) oldValue).removeCellEditorListener(this);
                }
                if (newValue != null && (newValue instanceof TableCellEditor)) {
                    ((TableCellEditor) newValue).addCellEditorListener(this);
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/JTable$AccessibleJTable$AccessibleJTableModelChange.class */
        protected class AccessibleJTableModelChange implements AccessibleTableModelChange {
            protected int type;
            protected int firstRow;
            protected int lastRow;
            protected int firstColumn;
            protected int lastColumn;

            protected AccessibleJTableModelChange(int i2, int i3, int i4, int i5, int i6) {
                this.type = i2;
                this.firstRow = i3;
                this.lastRow = i4;
                this.firstColumn = i5;
                this.lastColumn = i6;
            }

            @Override // javax.accessibility.AccessibleTableModelChange
            public int getType() {
                return this.type;
            }

            @Override // javax.accessibility.AccessibleTableModelChange
            public int getFirstRow() {
                return this.firstRow;
            }

            @Override // javax.accessibility.AccessibleTableModelChange
            public int getLastRow() {
                return this.lastRow;
            }

            @Override // javax.accessibility.AccessibleTableModelChange
            public int getFirstColumn() {
                return this.firstColumn;
            }

            @Override // javax.accessibility.AccessibleTableModelChange
            public int getLastColumn() {
                return this.lastColumn;
            }
        }

        @Override // javax.swing.event.TableModelListener
        public void tableChanged(TableModelEvent tableModelEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
            if (tableModelEvent != null) {
                int column = tableModelEvent.getColumn();
                int column2 = tableModelEvent.getColumn();
                if (column == -1) {
                    column = 0;
                    column2 = JTable.this.getColumnCount() - 1;
                }
                firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(tableModelEvent.getType(), tableModelEvent.getFirstRow(), tableModelEvent.getLastRow(), column, column2));
            }
        }

        public void tableRowsInserted(TableModelEvent tableModelEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
            int column = tableModelEvent.getColumn();
            int column2 = tableModelEvent.getColumn();
            if (column == -1) {
                column = 0;
                column2 = JTable.this.getColumnCount() - 1;
            }
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(tableModelEvent.getType(), tableModelEvent.getFirstRow(), tableModelEvent.getLastRow(), column, column2));
        }

        public void tableRowsDeleted(TableModelEvent tableModelEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
            int column = tableModelEvent.getColumn();
            int column2 = tableModelEvent.getColumn();
            if (column == -1) {
                column = 0;
                column2 = JTable.this.getColumnCount() - 1;
            }
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(tableModelEvent.getType(), tableModelEvent.getFirstRow(), tableModelEvent.getLastRow(), column, column2));
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(1, 0, 0, tableColumnModelEvent.getFromIndex(), tableColumnModelEvent.getToIndex()));
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(-1, 0, 0, tableColumnModelEvent.getFromIndex(), tableColumnModelEvent.getToIndex()));
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(-1, 0, 0, tableColumnModelEvent.getFromIndex(), tableColumnModelEvent.getFromIndex()));
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_MODEL_CHANGED, null, new AccessibleJTableModelChange(1, 0, 0, tableColumnModelEvent.getToIndex(), tableColumnModelEvent.getToIndex()));
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnMarginChanged(ChangeEvent changeEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
        }

        @Override // javax.swing.event.TableColumnModelListener
        public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingStopped(ChangeEvent changeEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_VISIBLE_DATA_PROPERTY, null, null);
        }

        @Override // javax.swing.event.CellEditorListener
        public void editingCanceled(ChangeEvent changeEvent) {
        }

        @Override // javax.swing.event.ListSelectionListener
        public void valueChanged(ListSelectionEvent listSelectionEvent) {
            firePropertyChange(AccessibleContext.ACCESSIBLE_SELECTION_PROPERTY, false, true);
            int leadSelectionIndex = JTable.this.getSelectionModel().getLeadSelectionIndex();
            int leadSelectionIndex2 = JTable.this.getColumnModel().getSelectionModel().getLeadSelectionIndex();
            if (leadSelectionIndex != this.previousFocusedRow || leadSelectionIndex2 != this.previousFocusedCol) {
                firePropertyChange(AccessibleContext.ACCESSIBLE_ACTIVE_DESCENDANT_PROPERTY, getAccessibleAt(this.previousFocusedRow, this.previousFocusedCol), getAccessibleAt(leadSelectionIndex, leadSelectionIndex2));
                this.previousFocusedRow = leadSelectionIndex;
                this.previousFocusedCol = leadSelectionIndex2;
            }
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleSelection getAccessibleSelection() {
            return this;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TABLE;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            int iColumnAtPoint = JTable.this.columnAtPoint(point);
            int iRowAtPoint = JTable.this.rowAtPoint(point);
            if (iColumnAtPoint != -1 && iRowAtPoint != -1) {
                TableCellRenderer cellRenderer = JTable.this.getColumnModel().getColumn(iColumnAtPoint).getCellRenderer();
                if (cellRenderer == null) {
                    cellRenderer = JTable.this.getDefaultRenderer(JTable.this.getColumnClass(iColumnAtPoint));
                }
                cellRenderer.getTableCellRendererComponent(JTable.this, null, false, false, iRowAtPoint, iColumnAtPoint);
                return new AccessibleJTableCell(JTable.this, iRowAtPoint, iColumnAtPoint, getAccessibleIndexAt(iRowAtPoint, iColumnAtPoint));
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return JTable.this.getColumnCount() * JTable.this.getRowCount();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (i2 < 0 || i2 >= getAccessibleChildrenCount()) {
                return null;
            }
            int accessibleColumnAtIndex = getAccessibleColumnAtIndex(i2);
            int accessibleRowAtIndex = getAccessibleRowAtIndex(i2);
            TableCellRenderer cellRenderer = JTable.this.getColumnModel().getColumn(accessibleColumnAtIndex).getCellRenderer();
            if (cellRenderer == null) {
                cellRenderer = JTable.this.getDefaultRenderer(JTable.this.getColumnClass(accessibleColumnAtIndex));
            }
            cellRenderer.getTableCellRendererComponent(JTable.this, null, false, false, accessibleRowAtIndex, accessibleColumnAtIndex);
            return new AccessibleJTableCell(JTable.this, accessibleRowAtIndex, accessibleColumnAtIndex, getAccessibleIndexAt(accessibleRowAtIndex, accessibleColumnAtIndex));
        }

        @Override // javax.accessibility.AccessibleSelection
        public int getAccessibleSelectionCount() {
            int selectedRowCount = JTable.this.getSelectedRowCount();
            int selectedColumnCount = JTable.this.getSelectedColumnCount();
            if (JTable.this.cellSelectionEnabled) {
                return selectedRowCount * selectedColumnCount;
            }
            if (JTable.this.getRowSelectionAllowed() && JTable.this.getColumnSelectionAllowed()) {
                return ((selectedRowCount * JTable.this.getColumnCount()) + (selectedColumnCount * JTable.this.getRowCount())) - (selectedRowCount * selectedColumnCount);
            }
            if (JTable.this.getRowSelectionAllowed()) {
                return selectedRowCount * JTable.this.getColumnCount();
            }
            if (JTable.this.getColumnSelectionAllowed()) {
                return selectedColumnCount * JTable.this.getRowCount();
            }
            return 0;
        }

        @Override // javax.accessibility.AccessibleSelection
        public Accessible getAccessibleSelection(int i2) {
            if (i2 < 0 || i2 > getAccessibleSelectionCount()) {
                return null;
            }
            JTable.this.getSelectedRowCount();
            int selectedColumnCount = JTable.this.getSelectedColumnCount();
            int[] selectedRows = JTable.this.getSelectedRows();
            int[] selectedColumns = JTable.this.getSelectedColumns();
            int columnCount = JTable.this.getColumnCount();
            int rowCount = JTable.this.getRowCount();
            if (JTable.this.cellSelectionEnabled) {
                return getAccessibleChild((selectedRows[i2 / selectedColumnCount] * columnCount) + selectedColumns[i2 % selectedColumnCount]);
            }
            if (!JTable.this.getRowSelectionAllowed() || !JTable.this.getColumnSelectionAllowed()) {
                if (JTable.this.getRowSelectionAllowed()) {
                    return getAccessibleChild((selectedRows[i2 / columnCount] * columnCount) + (i2 % columnCount));
                }
                if (JTable.this.getColumnSelectionAllowed()) {
                    return getAccessibleChild(((i2 / selectedColumnCount) * columnCount) + selectedColumns[i2 % selectedColumnCount]);
                }
                return null;
            }
            int i3 = i2;
            boolean z2 = selectedRows[0] != 0;
            int i4 = 0;
            int i5 = -1;
            while (i4 < selectedRows.length) {
                switch (z2) {
                    case false:
                        if (i3 < columnCount) {
                            return getAccessibleChild((selectedRows[i4] * columnCount) + (i3 % columnCount));
                        }
                        i3 -= columnCount;
                        if (i4 + 1 == selectedRows.length || selectedRows[i4] != selectedRows[i4 + 1] - 1) {
                            z2 = true;
                            i5 = selectedRows[i4];
                        }
                        i4++;
                        break;
                        break;
                    case true:
                        if (i3 < selectedColumnCount * (selectedRows[i4] - (i5 == -1 ? 0 : i5 + 1))) {
                            return getAccessibleChild((((i4 > 0 ? selectedRows[i4 - 1] + 1 : 0) + (i3 / selectedColumnCount)) * columnCount) + selectedColumns[i3 % selectedColumnCount]);
                        }
                        i3 -= selectedColumnCount * (selectedRows[i4] - (i5 == -1 ? 0 : i5 + 1));
                        z2 = false;
                        break;
                }
            }
            if (i3 < selectedColumnCount * (rowCount - (i5 == -1 ? 0 : i5 + 1))) {
                return getAccessibleChild(((selectedRows[i4 - 1] + (i3 / selectedColumnCount) + 1) * columnCount) + selectedColumns[i3 % selectedColumnCount]);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleSelection
        public boolean isAccessibleChildSelected(int i2) {
            int accessibleColumnAtIndex = getAccessibleColumnAtIndex(i2);
            return JTable.this.isCellSelected(getAccessibleRowAtIndex(i2), accessibleColumnAtIndex);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void addAccessibleSelection(int i2) {
            int accessibleColumnAtIndex = getAccessibleColumnAtIndex(i2);
            JTable.this.changeSelection(getAccessibleRowAtIndex(i2), accessibleColumnAtIndex, true, false);
        }

        @Override // javax.accessibility.AccessibleSelection
        public void removeAccessibleSelection(int i2) {
            if (JTable.this.cellSelectionEnabled) {
                int accessibleColumnAtIndex = getAccessibleColumnAtIndex(i2);
                int accessibleRowAtIndex = getAccessibleRowAtIndex(i2);
                JTable.this.removeRowSelectionInterval(accessibleRowAtIndex, accessibleRowAtIndex);
                JTable.this.removeColumnSelectionInterval(accessibleColumnAtIndex, accessibleColumnAtIndex);
            }
        }

        @Override // javax.accessibility.AccessibleSelection
        public void clearAccessibleSelection() {
            JTable.this.clearSelection();
        }

        @Override // javax.accessibility.AccessibleSelection
        public void selectAllAccessibleSelection() {
            if (JTable.this.cellSelectionEnabled) {
                JTable.this.selectAll();
            }
        }

        @Override // javax.accessibility.AccessibleExtendedTable
        public int getAccessibleRow(int i2) {
            return getAccessibleRowAtIndex(i2);
        }

        @Override // javax.accessibility.AccessibleExtendedTable
        public int getAccessibleColumn(int i2) {
            return getAccessibleColumnAtIndex(i2);
        }

        @Override // javax.accessibility.AccessibleExtendedTable
        public int getAccessibleIndex(int i2, int i3) {
            return getAccessibleIndexAt(i2, i3);
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleTable getAccessibleTable() {
            return this;
        }

        @Override // javax.accessibility.AccessibleTable
        public Accessible getAccessibleCaption() {
            return this.caption;
        }

        @Override // javax.accessibility.AccessibleTable
        public void setAccessibleCaption(Accessible accessible) {
            Accessible accessible2 = this.caption;
            this.caption = accessible;
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_CAPTION_CHANGED, accessible2, this.caption);
        }

        @Override // javax.accessibility.AccessibleTable
        public Accessible getAccessibleSummary() {
            return this.summary;
        }

        @Override // javax.accessibility.AccessibleTable
        public void setAccessibleSummary(Accessible accessible) {
            Accessible accessible2 = this.summary;
            this.summary = accessible;
            firePropertyChange(AccessibleContext.ACCESSIBLE_TABLE_SUMMARY_CHANGED, accessible2, this.summary);
        }

        @Override // javax.accessibility.AccessibleTable
        public int getAccessibleRowCount() {
            return JTable.this.getRowCount();
        }

        @Override // javax.accessibility.AccessibleTable
        public int getAccessibleColumnCount() {
            return JTable.this.getColumnCount();
        }

        @Override // javax.accessibility.AccessibleTable
        public Accessible getAccessibleAt(int i2, int i3) {
            return getAccessibleChild((i2 * getAccessibleColumnCount()) + i3);
        }

        @Override // javax.accessibility.AccessibleTable
        public int getAccessibleRowExtentAt(int i2, int i3) {
            return 1;
        }

        @Override // javax.accessibility.AccessibleTable
        public int getAccessibleColumnExtentAt(int i2, int i3) {
            return 1;
        }

        @Override // javax.accessibility.AccessibleTable
        public AccessibleTable getAccessibleRowHeader() {
            return null;
        }

        @Override // javax.accessibility.AccessibleTable
        public void setAccessibleRowHeader(AccessibleTable accessibleTable) {
        }

        @Override // javax.accessibility.AccessibleTable
        public AccessibleTable getAccessibleColumnHeader() {
            JTableHeader tableHeader = JTable.this.getTableHeader();
            if (tableHeader == null) {
                return null;
            }
            return new AccessibleTableHeader(tableHeader);
        }

        /* loaded from: rt.jar:javax/swing/JTable$AccessibleJTable$AccessibleTableHeader.class */
        private class AccessibleTableHeader implements AccessibleTable {
            private JTableHeader header;
            private TableColumnModel headerModel;

            AccessibleTableHeader(JTableHeader jTableHeader) {
                this.header = jTableHeader;
                this.headerModel = jTableHeader.getColumnModel();
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleCaption() {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleCaption(Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleSummary() {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleSummary(Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleRowCount() {
                return 1;
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleColumnCount() {
                return this.headerModel.getColumnCount();
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleAt(int i2, int i3) {
                TableColumn column = this.headerModel.getColumn(i3);
                TableCellRenderer headerRenderer = column.getHeaderRenderer();
                if (headerRenderer == null) {
                    headerRenderer = this.header.getDefaultRenderer();
                }
                return AccessibleJTable.this.new AccessibleJTableHeaderCell(i2, i3, JTable.this.getTableHeader(), headerRenderer.getTableCellRendererComponent(this.header.getTable(), column.getHeaderValue(), false, false, -1, i3));
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleRowExtentAt(int i2, int i3) {
                return 1;
            }

            @Override // javax.accessibility.AccessibleTable
            public int getAccessibleColumnExtentAt(int i2, int i3) {
                return 1;
            }

            @Override // javax.accessibility.AccessibleTable
            public AccessibleTable getAccessibleRowHeader() {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleRowHeader(AccessibleTable accessibleTable) {
            }

            @Override // javax.accessibility.AccessibleTable
            public AccessibleTable getAccessibleColumnHeader() {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleColumnHeader(AccessibleTable accessibleTable) {
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleRowDescription(int i2) {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleRowDescription(int i2, Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public Accessible getAccessibleColumnDescription(int i2) {
                return null;
            }

            @Override // javax.accessibility.AccessibleTable
            public void setAccessibleColumnDescription(int i2, Accessible accessible) {
            }

            @Override // javax.accessibility.AccessibleTable
            public boolean isAccessibleSelected(int i2, int i3) {
                return false;
            }

            @Override // javax.accessibility.AccessibleTable
            public boolean isAccessibleRowSelected(int i2) {
                return false;
            }

            @Override // javax.accessibility.AccessibleTable
            public boolean isAccessibleColumnSelected(int i2) {
                return false;
            }

            @Override // javax.accessibility.AccessibleTable
            public int[] getSelectedAccessibleRows() {
                return new int[0];
            }

            @Override // javax.accessibility.AccessibleTable
            public int[] getSelectedAccessibleColumns() {
                return new int[0];
            }
        }

        @Override // javax.accessibility.AccessibleTable
        public void setAccessibleColumnHeader(AccessibleTable accessibleTable) {
        }

        @Override // javax.accessibility.AccessibleTable
        public Accessible getAccessibleRowDescription(int i2) {
            if (i2 < 0 || i2 >= getAccessibleRowCount()) {
                throw new IllegalArgumentException(Integer.toString(i2));
            }
            if (this.rowDescription == null) {
                return null;
            }
            return this.rowDescription[i2];
        }

        @Override // javax.accessibility.AccessibleTable
        public void setAccessibleRowDescription(int i2, Accessible accessible) {
            if (i2 < 0 || i2 >= getAccessibleRowCount()) {
                throw new IllegalArgumentException(Integer.toString(i2));
            }
            if (this.rowDescription == null) {
                this.rowDescription = new Accessible[getAccessibleRowCount()];
            }
            this.rowDescription[i2] = accessible;
        }

        @Override // javax.accessibility.AccessibleTable
        public Accessible getAccessibleColumnDescription(int i2) {
            if (i2 < 0 || i2 >= getAccessibleColumnCount()) {
                throw new IllegalArgumentException(Integer.toString(i2));
            }
            if (this.columnDescription == null) {
                return null;
            }
            return this.columnDescription[i2];
        }

        @Override // javax.accessibility.AccessibleTable
        public void setAccessibleColumnDescription(int i2, Accessible accessible) {
            if (i2 < 0 || i2 >= getAccessibleColumnCount()) {
                throw new IllegalArgumentException(Integer.toString(i2));
            }
            if (this.columnDescription == null) {
                this.columnDescription = new Accessible[getAccessibleColumnCount()];
            }
            this.columnDescription[i2] = accessible;
        }

        @Override // javax.accessibility.AccessibleTable
        public boolean isAccessibleSelected(int i2, int i3) {
            return JTable.this.isCellSelected(i2, i3);
        }

        @Override // javax.accessibility.AccessibleTable
        public boolean isAccessibleRowSelected(int i2) {
            return JTable.this.isRowSelected(i2);
        }

        @Override // javax.accessibility.AccessibleTable
        public boolean isAccessibleColumnSelected(int i2) {
            return JTable.this.isColumnSelected(i2);
        }

        @Override // javax.accessibility.AccessibleTable
        public int[] getSelectedAccessibleRows() {
            return JTable.this.getSelectedRows();
        }

        @Override // javax.accessibility.AccessibleTable
        public int[] getSelectedAccessibleColumns() {
            return JTable.this.getSelectedColumns();
        }

        public int getAccessibleRowAtIndex(int i2) {
            int accessibleColumnCount = getAccessibleColumnCount();
            if (accessibleColumnCount == 0) {
                return -1;
            }
            return i2 / accessibleColumnCount;
        }

        public int getAccessibleColumnAtIndex(int i2) {
            int accessibleColumnCount = getAccessibleColumnCount();
            if (accessibleColumnCount == 0) {
                return -1;
            }
            return i2 % accessibleColumnCount;
        }

        public int getAccessibleIndexAt(int i2, int i3) {
            return (i2 * getAccessibleColumnCount()) + i3;
        }

        /* loaded from: rt.jar:javax/swing/JTable$AccessibleJTable$AccessibleJTableCell.class */
        protected class AccessibleJTableCell extends AccessibleContext implements Accessible, AccessibleComponent {
            private JTable parent;
            private int row;
            private int column;
            private int index;

            public AccessibleJTableCell(JTable jTable, int i2, int i3, int i4) {
                this.parent = jTable;
                this.row = i2;
                this.column = i3;
                this.index = i4;
                setAccessibleParent(this.parent);
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                return this;
            }

            protected AccessibleContext getCurrentAccessibleContext() {
                TableCellRenderer cellRenderer = JTable.this.getColumnModel().getColumn(this.column).getCellRenderer();
                if (cellRenderer == null) {
                    cellRenderer = JTable.this.getDefaultRenderer(JTable.this.getColumnClass(this.column));
                }
                Component tableCellRendererComponent = cellRenderer.getTableCellRendererComponent(JTable.this, JTable.this.getValueAt(this.row, this.column), false, false, this.row, this.column);
                if (tableCellRendererComponent instanceof Accessible) {
                    return tableCellRendererComponent.getAccessibleContext();
                }
                return null;
            }

            protected Component getCurrentComponent() {
                TableCellRenderer cellRenderer = JTable.this.getColumnModel().getColumn(this.column).getCellRenderer();
                if (cellRenderer == null) {
                    cellRenderer = JTable.this.getDefaultRenderer(JTable.this.getColumnClass(this.column));
                }
                return cellRenderer.getTableCellRendererComponent(JTable.this, null, false, false, this.row, this.column);
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                String accessibleName;
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null && (accessibleName = currentAccessibleContext.getAccessibleName()) != null && accessibleName != "") {
                    return accessibleName;
                }
                if (this.accessibleName != null && this.accessibleName != "") {
                    return this.accessibleName;
                }
                return (String) JTable.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleName(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleName(str);
                } else {
                    super.setAccessibleName(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleDescription();
                }
                return super.getAccessibleDescription();
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleDescription(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleDescription(str);
                } else {
                    super.setAccessibleDescription(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleRole();
                }
                return AccessibleRole.UNKNOWN;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleStateSet getAccessibleStateSet() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                AccessibleStateSet accessibleStateSet = null;
                if (currentAccessibleContext != null) {
                    accessibleStateSet = currentAccessibleContext.getAccessibleStateSet();
                }
                if (accessibleStateSet == null) {
                    accessibleStateSet = new AccessibleStateSet();
                }
                if (JTable.this.getVisibleRect().intersects(JTable.this.getCellRect(this.row, this.column, false))) {
                    accessibleStateSet.add(AccessibleState.SHOWING);
                } else if (accessibleStateSet.contains(AccessibleState.SHOWING)) {
                    accessibleStateSet.remove(AccessibleState.SHOWING);
                }
                if (this.parent.isCellSelected(this.row, this.column)) {
                    accessibleStateSet.add(AccessibleState.SELECTED);
                } else if (accessibleStateSet.contains(AccessibleState.SELECTED)) {
                    accessibleStateSet.remove(AccessibleState.SELECTED);
                }
                if (this.row == JTable.this.getSelectedRow() && this.column == JTable.this.getSelectedColumn()) {
                    accessibleStateSet.add(AccessibleState.ACTIVE);
                }
                accessibleStateSet.add(AccessibleState.TRANSIENT);
                return accessibleStateSet;
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleParent() {
                return this.parent;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                return this.index;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleChildrenCount();
                }
                return 0;
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    Accessible accessibleChild = currentAccessibleContext.getAccessibleChild(i2);
                    currentAccessibleContext.setAccessibleParent(this);
                    return accessibleChild;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public Locale getLocale() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getLocale();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.addPropertyChangeListener(propertyChangeListener);
                } else {
                    super.addPropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.removePropertyChangeListener(propertyChangeListener);
                } else {
                    super.removePropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleAction getAccessibleAction() {
                return getCurrentAccessibleContext().getAccessibleAction();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleComponent getAccessibleComponent() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleSelection getAccessibleSelection() {
                return getCurrentAccessibleContext().getAccessibleSelection();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleText getAccessibleText() {
                return getCurrentAccessibleContext().getAccessibleText();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleValue getAccessibleValue() {
                return getCurrentAccessibleContext().getAccessibleValue();
            }

            @Override // javax.accessibility.AccessibleComponent
            public Color getBackground() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBackground();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBackground();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBackground(Color color) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBackground(color);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setBackground(color);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Color getForeground() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getForeground();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getForeground();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setForeground(Color color) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setForeground(color);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setForeground(color);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Cursor getCursor() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getCursor();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getCursor();
                }
                Accessible accessibleParent = getAccessibleParent();
                if (accessibleParent instanceof AccessibleComponent) {
                    return ((AccessibleComponent) accessibleParent).getCursor();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setCursor(Cursor cursor) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setCursor(cursor);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setCursor(cursor);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Font getFont() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getFont();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getFont();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setFont(Font font) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setFont(font);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setFont(font);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public FontMetrics getFontMetrics(Font font) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getFontMetrics(font);
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getFontMetrics(font);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isEnabled() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isEnabled();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isEnabled();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setEnabled(boolean z2) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setEnabled(z2);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setEnabled(z2);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isVisible() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isVisible();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isVisible();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setVisible(boolean z2) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setVisible(z2);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setVisible(z2);
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javax.accessibility.AccessibleComponent
            public boolean isShowing() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    if (currentAccessibleContext.getAccessibleParent() != null) {
                        return ((AccessibleComponent) currentAccessibleContext).isShowing();
                    }
                    return isVisible();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isShowing();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean contains(Point point) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBounds().contains(point);
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBounds().contains(point);
                }
                return getBounds().contains(point);
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocationOnScreen() {
                if (this.parent != null && this.parent.isShowing()) {
                    Point locationOnScreen = this.parent.getLocationOnScreen();
                    Point location = getLocation();
                    location.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                    return location;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocation() {
                Rectangle cellRect;
                if (this.parent != null && (cellRect = this.parent.getCellRect(this.row, this.column, false)) != null) {
                    return cellRect.getLocation();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setLocation(Point point) {
            }

            @Override // javax.accessibility.AccessibleComponent
            public Rectangle getBounds() {
                if (this.parent != null) {
                    return this.parent.getCellRect(this.row, this.column, false);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBounds(Rectangle rectangle) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBounds(rectangle);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setBounds(rectangle);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Dimension getSize() {
                Rectangle cellRect;
                if (this.parent != null && (cellRect = this.parent.getCellRect(this.row, this.column, false)) != null) {
                    return cellRect.getSize();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setSize(Dimension dimension) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setSize(dimension);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setSize(dimension);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Accessible getAccessibleAt(Point point) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getAccessibleAt(point);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isFocusTraversable() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isFocusTraversable();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isFocusTraversable();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void requestFocus() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).requestFocus();
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.requestFocus();
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public void addFocusListener(FocusListener focusListener) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).addFocusListener(focusListener);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.addFocusListener(focusListener);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public void removeFocusListener(FocusListener focusListener) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).removeFocusListener(focusListener);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.removeFocusListener(focusListener);
                }
            }
        }

        /* loaded from: rt.jar:javax/swing/JTable$AccessibleJTable$AccessibleJTableHeaderCell.class */
        private class AccessibleJTableHeaderCell extends AccessibleContext implements Accessible, AccessibleComponent {
            private int row;
            private int column;
            private JTableHeader parent;
            private Component rendererComponent;

            public AccessibleJTableHeaderCell(int i2, int i3, JTableHeader jTableHeader, Component component) {
                this.row = i2;
                this.column = i3;
                this.parent = jTableHeader;
                this.rendererComponent = component;
                setAccessibleParent(jTableHeader);
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                return this;
            }

            private AccessibleContext getCurrentAccessibleContext() {
                return this.rendererComponent.getAccessibleContext();
            }

            private Component getCurrentComponent() {
                return this.rendererComponent;
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleName() {
                String accessibleName;
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null && (accessibleName = currentAccessibleContext.getAccessibleName()) != null && accessibleName != "") {
                    return currentAccessibleContext.getAccessibleName();
                }
                if (this.accessibleName != null && this.accessibleName != "") {
                    return this.accessibleName;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleName(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleName(str);
                } else {
                    super.setAccessibleName(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public String getAccessibleDescription() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleDescription();
                }
                return super.getAccessibleDescription();
            }

            @Override // javax.accessibility.AccessibleContext
            public void setAccessibleDescription(String str) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.setAccessibleDescription(str);
                } else {
                    super.setAccessibleDescription(str);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleRole getAccessibleRole() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleRole();
                }
                return AccessibleRole.UNKNOWN;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleStateSet getAccessibleStateSet() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                AccessibleStateSet accessibleStateSet = null;
                if (currentAccessibleContext != null) {
                    accessibleStateSet = currentAccessibleContext.getAccessibleStateSet();
                }
                if (accessibleStateSet == null) {
                    accessibleStateSet = new AccessibleStateSet();
                }
                if (JTable.this.getVisibleRect().intersects(JTable.this.getCellRect(this.row, this.column, false))) {
                    accessibleStateSet.add(AccessibleState.SHOWING);
                } else if (accessibleStateSet.contains(AccessibleState.SHOWING)) {
                    accessibleStateSet.remove(AccessibleState.SHOWING);
                }
                if (JTable.this.isCellSelected(this.row, this.column)) {
                    accessibleStateSet.add(AccessibleState.SELECTED);
                } else if (accessibleStateSet.contains(AccessibleState.SELECTED)) {
                    accessibleStateSet.remove(AccessibleState.SELECTED);
                }
                if (this.row == JTable.this.getSelectedRow() && this.column == JTable.this.getSelectedColumn()) {
                    accessibleStateSet.add(AccessibleState.ACTIVE);
                }
                accessibleStateSet.add(AccessibleState.TRANSIENT);
                return accessibleStateSet;
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleParent() {
                return this.parent;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleIndexInParent() {
                return this.column;
            }

            @Override // javax.accessibility.AccessibleContext
            public int getAccessibleChildrenCount() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getAccessibleChildrenCount();
                }
                return 0;
            }

            @Override // javax.accessibility.AccessibleContext
            public Accessible getAccessibleChild(int i2) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    Accessible accessibleChild = currentAccessibleContext.getAccessibleChild(i2);
                    currentAccessibleContext.setAccessibleParent(this);
                    return accessibleChild;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public Locale getLocale() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    return currentAccessibleContext.getLocale();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleContext
            public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.addPropertyChangeListener(propertyChangeListener);
                } else {
                    super.addPropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    currentAccessibleContext.removePropertyChangeListener(propertyChangeListener);
                } else {
                    super.removePropertyChangeListener(propertyChangeListener);
                }
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleAction getAccessibleAction() {
                return getCurrentAccessibleContext().getAccessibleAction();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleComponent getAccessibleComponent() {
                return this;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleSelection getAccessibleSelection() {
                return getCurrentAccessibleContext().getAccessibleSelection();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleText getAccessibleText() {
                return getCurrentAccessibleContext().getAccessibleText();
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleValue getAccessibleValue() {
                return getCurrentAccessibleContext().getAccessibleValue();
            }

            @Override // javax.accessibility.AccessibleComponent
            public Color getBackground() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBackground();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBackground();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBackground(Color color) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBackground(color);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setBackground(color);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Color getForeground() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getForeground();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getForeground();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setForeground(Color color) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setForeground(color);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setForeground(color);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Cursor getCursor() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getCursor();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getCursor();
                }
                Accessible accessibleParent = getAccessibleParent();
                if (accessibleParent instanceof AccessibleComponent) {
                    return ((AccessibleComponent) accessibleParent).getCursor();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setCursor(Cursor cursor) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setCursor(cursor);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setCursor(cursor);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Font getFont() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getFont();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getFont();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setFont(Font font) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setFont(font);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setFont(font);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public FontMetrics getFontMetrics(Font font) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getFontMetrics(font);
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getFontMetrics(font);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isEnabled() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isEnabled();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isEnabled();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setEnabled(boolean z2) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setEnabled(z2);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setEnabled(z2);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isVisible() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isVisible();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isVisible();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setVisible(boolean z2) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setVisible(z2);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setVisible(z2);
                }
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // javax.accessibility.AccessibleComponent
            public boolean isShowing() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    if (currentAccessibleContext.getAccessibleParent() != null) {
                        return ((AccessibleComponent) currentAccessibleContext).isShowing();
                    }
                    return isVisible();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isShowing();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean contains(Point point) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBounds().contains(point);
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBounds().contains(point);
                }
                return getBounds().contains(point);
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocationOnScreen() {
                if (this.parent != null && this.parent.isShowing()) {
                    Point locationOnScreen = this.parent.getLocationOnScreen();
                    Point location = getLocation();
                    location.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                    return location;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocation() {
                Rectangle headerRect;
                if (this.parent != null && (headerRect = this.parent.getHeaderRect(this.column)) != null) {
                    return headerRect.getLocation();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setLocation(Point point) {
            }

            @Override // javax.accessibility.AccessibleComponent
            public Rectangle getBounds() {
                if (this.parent != null) {
                    return this.parent.getHeaderRect(this.column);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setBounds(Rectangle rectangle) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setBounds(rectangle);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setBounds(rectangle);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Dimension getSize() {
                Rectangle headerRect;
                if (this.parent != null && (headerRect = this.parent.getHeaderRect(this.column)) != null) {
                    return headerRect.getSize();
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setSize(Dimension dimension) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).setSize(dimension);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.setSize(dimension);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public Accessible getAccessibleAt(Point point) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getAccessibleAt(point);
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public boolean isFocusTraversable() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).isFocusTraversable();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.isFocusTraversable();
                }
                return false;
            }

            @Override // javax.accessibility.AccessibleComponent
            public void requestFocus() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).requestFocus();
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.requestFocus();
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public void addFocusListener(FocusListener focusListener) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).addFocusListener(focusListener);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.addFocusListener(focusListener);
                }
            }

            @Override // javax.accessibility.AccessibleComponent
            public void removeFocusListener(FocusListener focusListener) {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    ((AccessibleComponent) currentAccessibleContext).removeFocusListener(focusListener);
                    return;
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    currentComponent.removeFocusListener(focusListener);
                }
            }
        }
    }
}
