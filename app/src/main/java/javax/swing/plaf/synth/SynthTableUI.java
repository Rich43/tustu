package javax.swing.plaf.synth;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DateFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.border.Border;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthTableUI.class */
public class SynthTableUI extends BasicTableUI implements SynthUI, PropertyChangeListener {
    private SynthStyle style;
    private boolean useTableColors;
    private boolean useUIBorder;
    private Color alternateColor;
    private TableCellRenderer dateRenderer;
    private TableCellRenderer numberRenderer;
    private TableCellRenderer doubleRender;
    private TableCellRenderer floatRenderer;
    private TableCellRenderer iconRenderer;
    private TableCellRenderer imageIconRenderer;
    private TableCellRenderer booleanRenderer;
    private TableCellRenderer objectRenderer;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthTableUI();
    }

    @Override // javax.swing.plaf.basic.BasicTableUI
    protected void installDefaults() {
        this.dateRenderer = installRendererIfPossible(Date.class, null);
        this.numberRenderer = installRendererIfPossible(Number.class, null);
        this.doubleRender = installRendererIfPossible(Double.class, null);
        this.floatRenderer = installRendererIfPossible(Float.class, null);
        this.iconRenderer = installRendererIfPossible(Icon.class, null);
        this.imageIconRenderer = installRendererIfPossible(ImageIcon.class, null);
        this.booleanRenderer = installRendererIfPossible(Boolean.class, new SynthBooleanTableCellRenderer());
        this.objectRenderer = installRendererIfPossible(Object.class, new SynthTableCellRenderer());
        updateStyle(this.table);
    }

    private TableCellRenderer installRendererIfPossible(Class cls, TableCellRenderer tableCellRenderer) {
        TableCellRenderer defaultRenderer = this.table.getDefaultRenderer(cls);
        if (defaultRenderer instanceof UIResource) {
            this.table.setDefaultRenderer(cls, tableCellRenderer);
        }
        return defaultRenderer;
    }

    private void updateStyle(JTable jTable) {
        SynthContext context = getContext(jTable, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            context.setComponentState(513);
            Color selectionBackground = this.table.getSelectionBackground();
            if (selectionBackground == null || (selectionBackground instanceof UIResource)) {
                this.table.setSelectionBackground(this.style.getColor(context, ColorType.TEXT_BACKGROUND));
            }
            Color selectionForeground = this.table.getSelectionForeground();
            if (selectionForeground == null || (selectionForeground instanceof UIResource)) {
                this.table.setSelectionForeground(this.style.getColor(context, ColorType.TEXT_FOREGROUND));
            }
            context.setComponentState(1);
            Color gridColor = this.table.getGridColor();
            if (gridColor == null || (gridColor instanceof UIResource)) {
                Color color = (Color) this.style.get(context, "Table.gridColor");
                if (color == null) {
                    color = this.style.getColor(context, ColorType.FOREGROUND);
                }
                this.table.setGridColor(color == null ? new ColorUIResource(Color.GRAY) : color);
            }
            this.useTableColors = this.style.getBoolean(context, "Table.rendererUseTableColors", true);
            this.useUIBorder = this.style.getBoolean(context, "Table.rendererUseUIBorder", true);
            Object obj = this.style.get(context, "Table.rowHeight");
            if (obj != null) {
                LookAndFeel.installProperty(this.table, JTree.ROW_HEIGHT_PROPERTY, obj);
            }
            if (!this.style.getBoolean(context, "Table.showGrid", true)) {
                this.table.setShowGrid(false);
            }
            Dimension intercellSpacing = this.table.getIntercellSpacing();
            if (intercellSpacing != null) {
                intercellSpacing = (Dimension) this.style.get(context, "Table.intercellSpacing");
            }
            this.alternateColor = (Color) this.style.get(context, "Table.alternateRowColor");
            if (intercellSpacing != null) {
                this.table.setIntercellSpacing(intercellSpacing);
            }
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicTableUI
    protected void installListeners() {
        super.installListeners();
        this.table.addPropertyChangeListener(this);
    }

    @Override // javax.swing.plaf.basic.BasicTableUI
    protected void uninstallDefaults() {
        this.table.setDefaultRenderer(Date.class, this.dateRenderer);
        this.table.setDefaultRenderer(Number.class, this.numberRenderer);
        this.table.setDefaultRenderer(Double.class, this.doubleRender);
        this.table.setDefaultRenderer(Float.class, this.floatRenderer);
        this.table.setDefaultRenderer(Icon.class, this.iconRenderer);
        this.table.setDefaultRenderer(ImageIcon.class, this.imageIconRenderer);
        this.table.setDefaultRenderer(Boolean.class, this.booleanRenderer);
        this.table.setDefaultRenderer(Object.class, this.objectRenderer);
        if (this.table.getTransferHandler() instanceof UIResource) {
            this.table.setTransferHandler(null);
        }
        SynthContext context = getContext(this.table, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicTableUI
    protected void uninstallListeners() {
        this.table.removePropertyChangeListener(this);
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintTableBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintTableBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicTableUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        Rectangle clipBounds = graphics.getClipBounds();
        Rectangle bounds = this.table.getBounds();
        bounds.f12373y = 0;
        bounds.f12372x = 0;
        if (this.table.getRowCount() <= 0 || this.table.getColumnCount() <= 0 || !bounds.intersects(clipBounds)) {
            paintDropLines(synthContext, graphics);
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
        paintCells(synthContext, graphics, iRowAtPoint, iRowAtPoint2, iColumnAtPoint, iColumnAtPoint2);
        paintGrid(synthContext, graphics, iRowAtPoint, iRowAtPoint2, iColumnAtPoint, iColumnAtPoint2);
        paintDropLines(synthContext, graphics);
    }

    private void paintDropLines(SynthContext synthContext, Graphics graphics) {
        JTable.DropLocation dropLocation = this.table.getDropLocation();
        if (dropLocation == null) {
            return;
        }
        Color color = (Color) this.style.get(synthContext, "Table.dropLineColor");
        Color color2 = (Color) this.style.get(synthContext, "Table.dropLineShortColor");
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

    private Rectangle getHDropLineRect(JTable.DropLocation dropLocation) {
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

    private Rectangle getVDropLineRect(JTable.DropLocation dropLocation) {
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

    private Rectangle extendRect(Rectangle rectangle, boolean z2) {
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

    private void paintGrid(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        graphics.setColor(this.table.getGridColor());
        Rectangle rectangleUnion = this.table.getCellRect(i2, i4, true).union(this.table.getCellRect(i3, i5, true));
        SynthGraphicsUtils graphicsUtils = synthContext.getStyle().getGraphicsUtils(synthContext);
        if (this.table.getShowHorizontalLines()) {
            int i6 = rectangleUnion.f12372x + rectangleUnion.width;
            int rowHeight = rectangleUnion.f12373y;
            for (int i7 = i2; i7 <= i3; i7++) {
                rowHeight += this.table.getRowHeight(i7);
                graphicsUtils.drawLine(synthContext, "Table.grid", graphics, rectangleUnion.f12372x, rowHeight - 1, i6 - 1, rowHeight - 1);
            }
        }
        if (this.table.getShowVerticalLines()) {
            TableColumnModel columnModel = this.table.getColumnModel();
            int i8 = rectangleUnion.f12373y + rectangleUnion.height;
            if (this.table.getComponentOrientation().isLeftToRight()) {
                int width = rectangleUnion.f12372x;
                for (int i9 = i4; i9 <= i5; i9++) {
                    width += columnModel.getColumn(i9).getWidth();
                    graphicsUtils.drawLine(synthContext, "Table.grid", graphics, width - 1, 0, width - 1, i8 - 1);
                }
                return;
            }
            int width2 = rectangleUnion.f12372x;
            for (int i10 = i5; i10 >= i4; i10--) {
                width2 += columnModel.getColumn(i10).getWidth();
                graphicsUtils.drawLine(synthContext, "Table.grid", graphics, width2 - 1, 0, width2 - 1, i8 - 1);
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

    private void paintCells(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
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
                        paintCell(synthContext, graphics, cellRect, i6, i7);
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
                    paintCell(synthContext, graphics, cellRect2, i8, i4);
                }
                for (int i9 = i4 + 1; i9 <= i5; i9++) {
                    TableColumn column3 = columnModel.getColumn(i9);
                    int width2 = column3.getWidth();
                    cellRect2.width = width2 - columnMargin;
                    cellRect2.f12372x -= width2;
                    if (column3 != draggedColumn) {
                        paintCell(synthContext, graphics, cellRect2, i8, i9);
                    }
                }
            }
        }
        if (draggedColumn != null) {
            paintDraggedArea(synthContext, graphics, i2, i3, draggedColumn, tableHeader.getDraggedDistance());
        }
        this.rendererPane.removeAll();
    }

    private void paintDraggedArea(SynthContext synthContext, Graphics graphics, int i2, int i3, TableColumn tableColumn, int i4) {
        int iViewIndexForColumn = viewIndexForColumn(tableColumn);
        Rectangle rectangleUnion = this.table.getCellRect(i2, iViewIndexForColumn, true).union(this.table.getCellRect(i3, iViewIndexForColumn, true));
        graphics.setColor(this.table.getParent().getBackground());
        graphics.fillRect(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
        rectangleUnion.f12372x += i4;
        graphics.setColor(synthContext.getStyle().getColor(synthContext, ColorType.BACKGROUND));
        graphics.fillRect(rectangleUnion.f12372x, rectangleUnion.f12373y, rectangleUnion.width, rectangleUnion.height);
        SynthGraphicsUtils graphicsUtils = synthContext.getStyle().getGraphicsUtils(synthContext);
        if (this.table.getShowVerticalLines()) {
            graphics.setColor(this.table.getGridColor());
            int i5 = rectangleUnion.f12372x;
            int i6 = rectangleUnion.f12373y;
            int i7 = (i5 + rectangleUnion.width) - 1;
            int i8 = (i6 + rectangleUnion.height) - 1;
            graphicsUtils.drawLine(synthContext, "Table.grid", graphics, i5 - 1, i6, i5 - 1, i8);
            graphicsUtils.drawLine(synthContext, "Table.grid", graphics, i7, i6, i7, i8);
        }
        for (int i9 = i2; i9 <= i3; i9++) {
            Rectangle cellRect = this.table.getCellRect(i9, iViewIndexForColumn, false);
            cellRect.f12372x += i4;
            paintCell(synthContext, graphics, cellRect, i9, iViewIndexForColumn);
            if (this.table.getShowHorizontalLines()) {
                graphics.setColor(this.table.getGridColor());
                Rectangle cellRect2 = this.table.getCellRect(i9, iViewIndexForColumn, true);
                cellRect2.f12372x += i4;
                int i10 = cellRect2.f12372x;
                int i11 = cellRect2.f12373y;
                int i12 = (i10 + cellRect2.width) - 1;
                int i13 = (i11 + cellRect2.height) - 1;
                graphicsUtils.drawLine(synthContext, "Table.grid", graphics, i10, i13, i12, i13);
            }
        }
    }

    private void paintCell(SynthContext synthContext, Graphics graphics, Rectangle rectangle, int i2, int i3) {
        if (this.table.isEditing() && this.table.getEditingRow() == i2 && this.table.getEditingColumn() == i3) {
            Component editorComponent = this.table.getEditorComponent();
            editorComponent.setBounds(rectangle);
            editorComponent.validate();
            return;
        }
        Component componentPrepareRenderer = this.table.prepareRenderer(this.table.getCellRenderer(i2, i3), i2, i3);
        Color background = componentPrepareRenderer.getBackground();
        if ((background == null || (background instanceof UIResource) || (componentPrepareRenderer instanceof SynthBooleanTableCellRenderer)) && !this.table.isCellSelected(i2, i3) && this.alternateColor != null && i2 % 2 != 0) {
            componentPrepareRenderer.setBackground(this.alternateColor);
        }
        this.rendererPane.paintComponent(graphics, componentPrepareRenderer, this.table, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, true);
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle((JTable) propertyChangeEvent.getSource());
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTableUI$SynthBooleanTableCellRenderer.class */
    private class SynthBooleanTableCellRenderer extends JCheckBox implements TableCellRenderer {
        private boolean isRowSelected;

        public SynthBooleanTableCellRenderer() {
            setHorizontalAlignment(0);
            setName("Table.cellRenderer");
        }

        @Override // javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            this.isRowSelected = z2;
            if (z2) {
                setForeground(unwrap(jTable.getSelectionForeground()));
                setBackground(unwrap(jTable.getSelectionBackground()));
            } else {
                setForeground(unwrap(jTable.getForeground()));
                setBackground(unwrap(jTable.getBackground()));
            }
            setSelected(obj != null && ((Boolean) obj).booleanValue());
            return this;
        }

        private Color unwrap(Color color) {
            if (color instanceof UIResource) {
                return new Color(color.getRGB());
            }
            return color;
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public boolean isOpaque() {
            if (this.isRowSelected) {
                return true;
            }
            return super.isOpaque();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthTableUI$SynthTableCellRenderer.class */
    private class SynthTableCellRenderer extends DefaultTableCellRenderer {
        private Object numberFormat;
        private Object dateFormat;
        private boolean opaque;

        private SynthTableCellRenderer() {
        }

        @Override // javax.swing.JComponent
        public void setOpaque(boolean z2) {
            this.opaque = z2;
        }

        @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.JComponent, java.awt.Component
        public boolean isOpaque() {
            return this.opaque;
        }

        @Override // java.awt.Component
        public String getName() {
            String name = super.getName();
            if (name == null) {
                return "Table.cellRenderer";
            }
            return name;
        }

        @Override // javax.swing.JComponent
        public void setBorder(Border border) {
            if (SynthTableUI.this.useUIBorder || (border instanceof SynthBorder)) {
                super.setBorder(border);
            }
        }

        @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
        public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
            if (!SynthTableUI.this.useTableColors && (z2 || z3)) {
                SynthLookAndFeel.setSelectedUI((SynthLabelUI) SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), z2, z3, jTable.isEnabled(), false);
            } else {
                SynthLookAndFeel.resetSelectedUI();
            }
            super.getTableCellRendererComponent(jTable, obj, z2, z3, i2, i3);
            setIcon(null);
            if (jTable != null) {
                configureValue(obj, jTable.getColumnClass(i3));
            }
            return this;
        }

        private void configureValue(Object obj, Class cls) {
            if (cls == Object.class || cls == null) {
                setHorizontalAlignment(10);
                return;
            }
            if (cls == Float.class || cls == Double.class) {
                if (this.numberFormat == null) {
                    this.numberFormat = NumberFormat.getInstance();
                }
                setHorizontalAlignment(11);
                setText(obj == null ? "" : ((NumberFormat) this.numberFormat).format(obj));
                return;
            }
            if (cls == Number.class) {
                setHorizontalAlignment(11);
                return;
            }
            if (cls == Icon.class || cls == ImageIcon.class) {
                setHorizontalAlignment(0);
                setIcon(obj instanceof Icon ? (Icon) obj : null);
                setText("");
            } else {
                if (cls == Date.class) {
                    if (this.dateFormat == null) {
                        this.dateFormat = DateFormat.getDateInstance();
                    }
                    setHorizontalAlignment(10);
                    setText(obj == null ? "" : ((Format) this.dateFormat).format(obj));
                    return;
                }
                configureValue(obj, cls.getSuperclass());
            }
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            super.paint(graphics);
            SynthLookAndFeel.resetSelectedUI();
        }
    }
}
