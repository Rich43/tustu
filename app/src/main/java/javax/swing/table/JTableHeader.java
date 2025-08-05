package javax.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.MenuContainer;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.Transient;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleComponent;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleSelection;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import sun.awt.AWTAccessor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

/* loaded from: rt.jar:javax/swing/table/JTableHeader.class */
public class JTableHeader extends JComponent implements TableColumnModelListener, Accessible {
    private static final String uiClassID = "TableHeaderUI";
    protected JTable table;
    protected TableColumnModel columnModel;
    protected boolean reorderingAllowed;
    protected boolean resizingAllowed;
    protected boolean updateTableInRealTime;
    protected transient TableColumn resizingColumn;
    protected transient TableColumn draggedColumn;
    protected transient int draggedDistance;
    private TableCellRenderer defaultRenderer;

    public JTableHeader() {
        this(null);
    }

    public JTableHeader(TableColumnModel tableColumnModel) {
        setColumnModel(tableColumnModel == null ? createDefaultColumnModel() : tableColumnModel);
        initializeLocalVars();
        updateUI();
    }

    public void setTable(JTable jTable) {
        JTable jTable2 = this.table;
        this.table = jTable;
        firePropertyChange("table", jTable2, jTable);
    }

    public JTable getTable() {
        return this.table;
    }

    public void setReorderingAllowed(boolean z2) {
        boolean z3 = this.reorderingAllowed;
        this.reorderingAllowed = z2;
        firePropertyChange("reorderingAllowed", z3, z2);
    }

    public boolean getReorderingAllowed() {
        return this.reorderingAllowed;
    }

    public void setResizingAllowed(boolean z2) {
        boolean z3 = this.resizingAllowed;
        this.resizingAllowed = z2;
        firePropertyChange("resizingAllowed", z3, z2);
    }

    public boolean getResizingAllowed() {
        return this.resizingAllowed;
    }

    public TableColumn getDraggedColumn() {
        return this.draggedColumn;
    }

    public int getDraggedDistance() {
        return this.draggedDistance;
    }

    public TableColumn getResizingColumn() {
        return this.resizingColumn;
    }

    public void setUpdateTableInRealTime(boolean z2) {
        this.updateTableInRealTime = z2;
    }

    public boolean getUpdateTableInRealTime() {
        return this.updateTableInRealTime;
    }

    public void setDefaultRenderer(TableCellRenderer tableCellRenderer) {
        this.defaultRenderer = tableCellRenderer;
    }

    @Transient
    public TableCellRenderer getDefaultRenderer() {
        return this.defaultRenderer;
    }

    public int columnAtPoint(Point point) {
        int widthInRightToLeft = point.f12370x;
        if (!getComponentOrientation().isLeftToRight()) {
            widthInRightToLeft = (getWidthInRightToLeft() - widthInRightToLeft) - 1;
        }
        return getColumnModel().getColumnIndexAtX(widthInRightToLeft);
    }

    public Rectangle getHeaderRect(int i2) {
        Rectangle rectangle = new Rectangle();
        TableColumnModel columnModel = getColumnModel();
        rectangle.height = getHeight();
        if (i2 < 0) {
            if (!getComponentOrientation().isLeftToRight()) {
                rectangle.f12372x = getWidthInRightToLeft();
            }
        } else if (i2 >= columnModel.getColumnCount()) {
            if (getComponentOrientation().isLeftToRight()) {
                rectangle.f12372x = getWidth();
            }
        } else {
            for (int i3 = 0; i3 < i2; i3++) {
                rectangle.f12372x += columnModel.getColumn(i3).getWidth();
            }
            if (!getComponentOrientation().isLeftToRight()) {
                rectangle.f12372x = (getWidthInRightToLeft() - rectangle.f12372x) - columnModel.getColumn(i2).getWidth();
            }
            rectangle.width = columnModel.getColumn(i2).getWidth();
        }
        return rectangle;
    }

    @Override // javax.swing.JComponent
    public String getToolTipText(MouseEvent mouseEvent) {
        String toolTipText = null;
        Point point = mouseEvent.getPoint();
        int iColumnAtPoint = columnAtPoint(point);
        if (iColumnAtPoint != -1) {
            TableColumn column = this.columnModel.getColumn(iColumnAtPoint);
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                headerRenderer = this.defaultRenderer;
            }
            Component tableCellRendererComponent = headerRenderer.getTableCellRendererComponent(getTable(), column.getHeaderValue(), false, false, -1, iColumnAtPoint);
            if (tableCellRendererComponent instanceof JComponent) {
                Rectangle headerRect = getHeaderRect(iColumnAtPoint);
                point.translate(-headerRect.f12372x, -headerRect.f12373y);
                MouseEvent mouseEvent2 = new MouseEvent(tableCellRendererComponent, mouseEvent.getID(), mouseEvent.getWhen(), mouseEvent.getModifiers(), point.f12370x, point.f12371y, mouseEvent.getXOnScreen(), mouseEvent.getYOnScreen(), mouseEvent.getClickCount(), mouseEvent.isPopupTrigger(), 0);
                AWTAccessor.MouseEventAccessor mouseEventAccessor = AWTAccessor.getMouseEventAccessor();
                mouseEventAccessor.setCausedByTouchEvent(mouseEvent2, mouseEventAccessor.isCausedByTouchEvent(mouseEvent));
                toolTipText = ((JComponent) tableCellRendererComponent).getToolTipText(mouseEvent2);
            }
        }
        if (toolTipText == null) {
            toolTipText = getToolTipText();
        }
        return toolTipText;
    }

    public TableHeaderUI getUI() {
        return (TableHeaderUI) this.ui;
    }

    public void setUI(TableHeaderUI tableHeaderUI) {
        if (this.ui != tableHeaderUI) {
            super.setUI((ComponentUI) tableHeaderUI);
            repaint();
        }
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((TableHeaderUI) UIManager.getUI(this));
        Object defaultRenderer = getDefaultRenderer();
        if (defaultRenderer instanceof Component) {
            SwingUtilities.updateComponentTreeUI((Component) defaultRenderer);
        }
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
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
            firePropertyChange("columnModel", tableColumnModel2, tableColumnModel);
            resizeAndRepaint();
        }
    }

    public TableColumnModel getColumnModel() {
        return this.columnModel;
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnAdded(TableColumnModelEvent tableColumnModelEvent) {
        resizeAndRepaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnRemoved(TableColumnModelEvent tableColumnModelEvent) {
        resizeAndRepaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnMoved(TableColumnModelEvent tableColumnModelEvent) {
        repaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnMarginChanged(ChangeEvent changeEvent) {
        resizeAndRepaint();
    }

    @Override // javax.swing.event.TableColumnModelListener
    public void columnSelectionChanged(ListSelectionEvent listSelectionEvent) {
    }

    protected TableColumnModel createDefaultColumnModel() {
        return new DefaultTableColumnModel();
    }

    protected TableCellRenderer createDefaultRenderer() {
        return new DefaultTableCellHeaderRenderer();
    }

    protected void initializeLocalVars() {
        setOpaque(true);
        this.table = null;
        this.reorderingAllowed = true;
        this.resizingAllowed = true;
        this.draggedColumn = null;
        this.draggedDistance = 0;
        this.resizingColumn = null;
        this.updateTableInRealTime = true;
        ToolTipManager.sharedInstance().registerComponent(this);
        setDefaultRenderer(createDefaultRenderer());
    }

    public void resizeAndRepaint() {
        revalidate();
        repaint();
    }

    public void setDraggedColumn(TableColumn tableColumn) {
        this.draggedColumn = tableColumn;
    }

    public void setDraggedDistance(int i2) {
        this.draggedDistance = i2;
    }

    public void setResizingColumn(TableColumn tableColumn) {
        this.resizingColumn = tableColumn;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (this.ui != null && getUIClassID().equals(uiClassID)) {
            this.ui.installUI(this);
        }
    }

    private int getWidthInRightToLeft() {
        if (this.table != null && this.table.getAutoResizeMode() != 0) {
            return this.table.getWidth();
        }
        return super.getWidth();
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",draggedDistance=" + this.draggedDistance + ",reorderingAllowed=" + (this.reorderingAllowed ? "true" : "false") + ",resizingAllowed=" + (this.resizingAllowed ? "true" : "false") + ",updateTableInRealTime=" + (this.updateTableInRealTime ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJTableHeader();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/table/JTableHeader$AccessibleJTableHeader.class */
    protected class AccessibleJTableHeader extends JComponent.AccessibleJComponent {
        protected AccessibleJTableHeader() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PANEL;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleComponent
        public Accessible getAccessibleAt(Point point) {
            int iColumnAtPoint = JTableHeader.this.columnAtPoint(point);
            if (iColumnAtPoint != -1) {
                TableColumn column = JTableHeader.this.columnModel.getColumn(iColumnAtPoint);
                TableCellRenderer headerRenderer = column.getHeaderRenderer();
                if (headerRenderer == null) {
                    if (JTableHeader.this.defaultRenderer != null) {
                        headerRenderer = JTableHeader.this.defaultRenderer;
                    } else {
                        return null;
                    }
                }
                headerRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), column.getHeaderValue(), false, false, -1, iColumnAtPoint);
                return new AccessibleJTableHeaderEntry(iColumnAtPoint, JTableHeader.this, JTableHeader.this.table);
            }
            return null;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            return JTableHeader.this.columnModel.getColumnCount();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (i2 < 0 || i2 >= getAccessibleChildrenCount()) {
                return null;
            }
            TableColumn column = JTableHeader.this.columnModel.getColumn(i2);
            TableCellRenderer headerRenderer = column.getHeaderRenderer();
            if (headerRenderer == null) {
                if (JTableHeader.this.defaultRenderer != null) {
                    headerRenderer = JTableHeader.this.defaultRenderer;
                } else {
                    return null;
                }
            }
            headerRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), column.getHeaderValue(), false, false, -1, i2);
            return new AccessibleJTableHeaderEntry(i2, JTableHeader.this, JTableHeader.this.table);
        }

        /* loaded from: rt.jar:javax/swing/table/JTableHeader$AccessibleJTableHeader$AccessibleJTableHeaderEntry.class */
        protected class AccessibleJTableHeaderEntry extends AccessibleContext implements Accessible, AccessibleComponent {
            private JTableHeader parent;
            private int column;
            private JTable table;

            public AccessibleJTableHeaderEntry(int i2, JTableHeader jTableHeader, JTable jTable) {
                this.parent = jTableHeader;
                this.column = i2;
                this.table = jTable;
                setAccessibleParent(this.parent);
            }

            @Override // javax.accessibility.Accessible
            public AccessibleContext getAccessibleContext() {
                return this;
            }

            private AccessibleContext getCurrentAccessibleContext() {
                TableColumnModel columnModel = this.table.getColumnModel();
                if (columnModel == null || this.column < 0 || this.column >= columnModel.getColumnCount()) {
                    return null;
                }
                TableColumn column = columnModel.getColumn(this.column);
                TableCellRenderer headerRenderer = column.getHeaderRenderer();
                if (headerRenderer == null) {
                    if (JTableHeader.this.defaultRenderer != null) {
                        headerRenderer = JTableHeader.this.defaultRenderer;
                    } else {
                        return null;
                    }
                }
                MenuContainer tableCellRendererComponent = headerRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), column.getHeaderValue(), false, false, -1, this.column);
                if (tableCellRendererComponent instanceof Accessible) {
                    return ((Accessible) tableCellRendererComponent).getAccessibleContext();
                }
                return null;
            }

            private Component getCurrentComponent() {
                TableColumnModel columnModel = this.table.getColumnModel();
                if (columnModel == null || this.column < 0 || this.column >= columnModel.getColumnCount()) {
                    return null;
                }
                TableColumn column = columnModel.getColumn(this.column);
                TableCellRenderer headerRenderer = column.getHeaderRenderer();
                if (headerRenderer == null) {
                    if (JTableHeader.this.defaultRenderer != null) {
                        headerRenderer = JTableHeader.this.defaultRenderer;
                    } else {
                        return null;
                    }
                }
                return headerRenderer.getTableCellRendererComponent(JTableHeader.this.getTable(), column.getHeaderValue(), false, false, -1, this.column);
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
                String str = (String) JTableHeader.this.getClientProperty(AccessibleContext.ACCESSIBLE_NAME_PROPERTY);
                if (str != null) {
                    return str;
                }
                return this.table.getColumnName(this.column);
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
                return AccessibleRole.COLUMN_HEADER;
            }

            @Override // javax.accessibility.AccessibleContext
            public AccessibleStateSet getAccessibleStateSet() {
                AccessibleContext currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext != null) {
                    AccessibleStateSet accessibleStateSet = currentAccessibleContext.getAccessibleStateSet();
                    if (isShowing()) {
                        accessibleStateSet.add(AccessibleState.SHOWING);
                    }
                    return accessibleStateSet;
                }
                return new AccessibleStateSet();
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

            @Override // javax.accessibility.AccessibleComponent
            public boolean isShowing() {
                if (isVisible() && JTableHeader.this.isShowing()) {
                    return true;
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
                if (this.parent != null) {
                    Point locationOnScreen = this.parent.getLocationOnScreen();
                    Point location = getLocation();
                    location.translate(locationOnScreen.f12370x, locationOnScreen.f12371y);
                    return location;
                }
                return null;
            }

            @Override // javax.accessibility.AccessibleComponent
            public Point getLocation() {
                Object currentAccessibleContext = getCurrentAccessibleContext();
                if (currentAccessibleContext instanceof AccessibleComponent) {
                    return ((AccessibleComponent) currentAccessibleContext).getBounds().getLocation();
                }
                Component currentComponent = getCurrentComponent();
                if (currentComponent != null) {
                    return currentComponent.getBounds().getLocation();
                }
                return getBounds().getLocation();
            }

            @Override // javax.accessibility.AccessibleComponent
            public void setLocation(Point point) {
            }

            @Override // javax.accessibility.AccessibleComponent
            public Rectangle getBounds() {
                Rectangle cellRect = this.table.getCellRect(-1, this.column, false);
                cellRect.f12373y = 0;
                return cellRect;
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
                return getBounds().getSize();
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
