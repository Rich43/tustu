package javax.swing.table;

import com.sun.media.jfxmedia.MetadataParser;
import java.awt.Component;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.SwingPropertyChangeSupport;

/* loaded from: rt.jar:javax/swing/table/TableColumn.class */
public class TableColumn implements Serializable {
    public static final String COLUMN_WIDTH_PROPERTY = "columWidth";
    public static final String HEADER_VALUE_PROPERTY = "headerValue";
    public static final String HEADER_RENDERER_PROPERTY = "headerRenderer";
    public static final String CELL_RENDERER_PROPERTY = "cellRenderer";
    protected int modelIndex;
    protected Object identifier;
    protected int width;
    protected int minWidth;
    private int preferredWidth;
    protected int maxWidth;
    protected TableCellRenderer headerRenderer;
    protected Object headerValue;
    protected TableCellRenderer cellRenderer;
    protected TableCellEditor cellEditor;
    protected boolean isResizable;

    @Deprecated
    protected transient int resizedPostingDisableCount;
    private SwingPropertyChangeSupport changeSupport;

    public TableColumn() {
        this(0);
    }

    public TableColumn(int i2) {
        this(i2, 75, null, null);
    }

    public TableColumn(int i2, int i3) {
        this(i2, i3, null, null);
    }

    public TableColumn(int i2, int i3, TableCellRenderer tableCellRenderer, TableCellEditor tableCellEditor) {
        this.modelIndex = i2;
        int iMax = Math.max(i3, 0);
        this.width = iMax;
        this.preferredWidth = iMax;
        this.cellRenderer = tableCellRenderer;
        this.cellEditor = tableCellEditor;
        this.minWidth = Math.min(15, this.width);
        this.maxWidth = Integer.MAX_VALUE;
        this.isResizable = true;
        this.resizedPostingDisableCount = 0;
        this.headerValue = null;
    }

    private void firePropertyChange(String str, Object obj, Object obj2) {
        if (this.changeSupport != null) {
            this.changeSupport.firePropertyChange(str, obj, obj2);
        }
    }

    private void firePropertyChange(String str, int i2, int i3) {
        if (i2 != i3) {
            firePropertyChange(str, Integer.valueOf(i2), Integer.valueOf(i3));
        }
    }

    private void firePropertyChange(String str, boolean z2, boolean z3) {
        if (z2 != z3) {
            firePropertyChange(str, Boolean.valueOf(z2), Boolean.valueOf(z3));
        }
    }

    public void setModelIndex(int i2) {
        int i3 = this.modelIndex;
        this.modelIndex = i2;
        firePropertyChange("modelIndex", i3, i2);
    }

    public int getModelIndex() {
        return this.modelIndex;
    }

    public void setIdentifier(Object obj) {
        Object obj2 = this.identifier;
        this.identifier = obj;
        firePropertyChange("identifier", obj2, obj);
    }

    public Object getIdentifier() {
        return this.identifier != null ? this.identifier : getHeaderValue();
    }

    public void setHeaderValue(Object obj) {
        Object obj2 = this.headerValue;
        this.headerValue = obj;
        firePropertyChange(HEADER_VALUE_PROPERTY, obj2, obj);
    }

    public Object getHeaderValue() {
        return this.headerValue;
    }

    public void setHeaderRenderer(TableCellRenderer tableCellRenderer) {
        TableCellRenderer tableCellRenderer2 = this.headerRenderer;
        this.headerRenderer = tableCellRenderer;
        firePropertyChange(HEADER_RENDERER_PROPERTY, tableCellRenderer2, tableCellRenderer);
    }

    public TableCellRenderer getHeaderRenderer() {
        return this.headerRenderer;
    }

    public void setCellRenderer(TableCellRenderer tableCellRenderer) {
        TableCellRenderer tableCellRenderer2 = this.cellRenderer;
        this.cellRenderer = tableCellRenderer;
        firePropertyChange("cellRenderer", tableCellRenderer2, tableCellRenderer);
    }

    public TableCellRenderer getCellRenderer() {
        return this.cellRenderer;
    }

    public void setCellEditor(TableCellEditor tableCellEditor) {
        TableCellEditor tableCellEditor2 = this.cellEditor;
        this.cellEditor = tableCellEditor;
        firePropertyChange(JTree.CELL_EDITOR_PROPERTY, tableCellEditor2, tableCellEditor);
    }

    public TableCellEditor getCellEditor() {
        return this.cellEditor;
    }

    public void setWidth(int i2) {
        int i3 = this.width;
        this.width = Math.min(Math.max(i2, this.minWidth), this.maxWidth);
        firePropertyChange(MetadataParser.WIDTH_TAG_NAME, i3, this.width);
    }

    public int getWidth() {
        return this.width;
    }

    public void setPreferredWidth(int i2) {
        int i3 = this.preferredWidth;
        this.preferredWidth = Math.min(Math.max(i2, this.minWidth), this.maxWidth);
        firePropertyChange("preferredWidth", i3, this.preferredWidth);
    }

    public int getPreferredWidth() {
        return this.preferredWidth;
    }

    public void setMinWidth(int i2) {
        int i3 = this.minWidth;
        this.minWidth = Math.max(Math.min(i2, this.maxWidth), 0);
        if (this.width < this.minWidth) {
            setWidth(this.minWidth);
        }
        if (this.preferredWidth < this.minWidth) {
            setPreferredWidth(this.minWidth);
        }
        firePropertyChange("minWidth", i3, this.minWidth);
    }

    public int getMinWidth() {
        return this.minWidth;
    }

    public void setMaxWidth(int i2) {
        int i3 = this.maxWidth;
        this.maxWidth = Math.max(this.minWidth, i2);
        if (this.width > this.maxWidth) {
            setWidth(this.maxWidth);
        }
        if (this.preferredWidth > this.maxWidth) {
            setPreferredWidth(this.maxWidth);
        }
        firePropertyChange("maxWidth", i3, this.maxWidth);
    }

    public int getMaxWidth() {
        return this.maxWidth;
    }

    public void setResizable(boolean z2) {
        boolean z3 = this.isResizable;
        this.isResizable = z2;
        firePropertyChange("isResizable", z3, this.isResizable);
    }

    public boolean getResizable() {
        return this.isResizable;
    }

    public void sizeWidthToFit() {
        if (this.headerRenderer == null) {
            return;
        }
        Component tableCellRendererComponent = this.headerRenderer.getTableCellRendererComponent(null, getHeaderValue(), false, false, 0, 0);
        setMinWidth(tableCellRendererComponent.getMinimumSize().width);
        setMaxWidth(tableCellRendererComponent.getMaximumSize().width);
        setPreferredWidth(tableCellRendererComponent.getPreferredSize().width);
        setWidth(getPreferredWidth());
    }

    @Deprecated
    public void disableResizedPosting() {
        this.resizedPostingDisableCount++;
    }

    @Deprecated
    public void enableResizedPosting() {
        this.resizedPostingDisableCount--;
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport == null) {
            this.changeSupport = new SwingPropertyChangeSupport(this);
        }
        this.changeSupport.addPropertyChangeListener(propertyChangeListener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        if (this.changeSupport != null) {
            this.changeSupport.removePropertyChangeListener(propertyChangeListener);
        }
    }

    public synchronized PropertyChangeListener[] getPropertyChangeListeners() {
        if (this.changeSupport == null) {
            return new PropertyChangeListener[0];
        }
        return this.changeSupport.getPropertyChangeListeners();
    }

    protected TableCellRenderer createDefaultHeaderRenderer() {
        DefaultTableCellRenderer defaultTableCellRenderer = new DefaultTableCellRenderer() { // from class: javax.swing.table.TableColumn.1
            @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
            public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
                JTableHeader tableHeader;
                if (jTable != null && (tableHeader = jTable.getTableHeader()) != null) {
                    setForeground(tableHeader.getForeground());
                    setBackground(tableHeader.getBackground());
                    setFont(tableHeader.getFont());
                }
                setText(obj == null ? "" : obj.toString());
                setBorder(UIManager.getBorder("TableHeader.cellBorder"));
                return this;
            }
        };
        defaultTableCellRenderer.setHorizontalAlignment(0);
        return defaultTableCellRenderer;
    }
}
