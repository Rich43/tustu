package sun.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.List;
import javax.swing.Icon;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:sun/swing/table/DefaultTableCellHeaderRenderer.class */
public class DefaultTableCellHeaderRenderer extends DefaultTableCellRenderer implements UIResource {
    private boolean horizontalTextPositionSet;
    private Icon sortArrow;
    private EmptyIcon emptyIcon = new EmptyIcon();

    public DefaultTableCellHeaderRenderer() {
        setHorizontalAlignment(0);
    }

    @Override // javax.swing.JLabel
    public void setHorizontalTextPosition(int i2) {
        this.horizontalTextPositionSet = true;
        super.setHorizontalTextPosition(i2);
    }

    @Override // javax.swing.table.DefaultTableCellRenderer, javax.swing.table.TableCellRenderer
    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Icon icon = null;
        boolean zIsPaintingForPrint = false;
        if (jTable != null) {
            JTableHeader tableHeader = jTable.getTableHeader();
            if (tableHeader != null) {
                Color foreground = null;
                Color background = null;
                if (z3) {
                    foreground = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellForeground");
                    background = DefaultLookup.getColor(this, this.ui, "TableHeader.focusCellBackground");
                }
                if (foreground == null) {
                    foreground = tableHeader.getForeground();
                }
                if (background == null) {
                    background = tableHeader.getBackground();
                }
                setForeground(foreground);
                setBackground(background);
                setFont(tableHeader.getFont());
                zIsPaintingForPrint = tableHeader.isPaintingForPrint();
            }
            if (!zIsPaintingForPrint && jTable.getRowSorter() != null) {
                if (!this.horizontalTextPositionSet) {
                    setHorizontalTextPosition(10);
                }
                SortOrder columnSortOrder = getColumnSortOrder(jTable, i3);
                if (columnSortOrder != null) {
                    switch (columnSortOrder) {
                        case ASCENDING:
                            icon = DefaultLookup.getIcon(this, this.ui, "Table.ascendingSortIcon");
                            break;
                        case DESCENDING:
                            icon = DefaultLookup.getIcon(this, this.ui, "Table.descendingSortIcon");
                            break;
                        case UNSORTED:
                            icon = DefaultLookup.getIcon(this, this.ui, "Table.naturalSortIcon");
                            break;
                    }
                }
            }
        }
        setText(obj == null ? "" : obj.toString());
        setIcon(icon);
        this.sortArrow = icon;
        Border border = null;
        if (z3) {
            border = DefaultLookup.getBorder(this, this.ui, "TableHeader.focusCellBorder");
        }
        if (border == null) {
            border = DefaultLookup.getBorder(this, this.ui, "TableHeader.cellBorder");
        }
        setBorder(border);
        return this;
    }

    public static SortOrder getColumnSortOrder(JTable jTable, int i2) {
        SortOrder sortOrder = null;
        if (jTable == null || jTable.getRowSorter() == null) {
            return null;
        }
        List<? extends RowSorter.SortKey> sortKeys = jTable.getRowSorter().getSortKeys();
        if (sortKeys.size() > 0 && sortKeys.get(0).getColumn() == jTable.convertColumnIndexToModel(i2)) {
            sortOrder = sortKeys.get(0).getSortOrder();
        }
        return sortOrder;
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        if (DefaultLookup.getBoolean(this, this.ui, "TableHeader.rightAlignSortArrow", false) && this.sortArrow != null) {
            this.emptyIcon.width = this.sortArrow.getIconWidth();
            this.emptyIcon.height = this.sortArrow.getIconHeight();
            setIcon(this.emptyIcon);
            super.paintComponent(graphics);
            Point pointComputeIconPosition = computeIconPosition(graphics);
            this.sortArrow.paintIcon(this, graphics, pointComputeIconPosition.f12370x, pointComputeIconPosition.f12371y);
            return;
        }
        super.paintComponent(graphics);
    }

    private Point computeIconPosition(Graphics graphics) {
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Rectangle rectangle = new Rectangle();
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        Insets insets = getInsets();
        rectangle.f12372x = insets.left;
        rectangle.f12373y = insets.top;
        rectangle.width = getWidth() - (insets.left + insets.right);
        rectangle.height = getHeight() - (insets.top + insets.bottom);
        SwingUtilities.layoutCompoundLabel(this, fontMetrics, getText(), this.sortArrow, getVerticalAlignment(), getHorizontalAlignment(), getVerticalTextPosition(), getHorizontalTextPosition(), rectangle, rectangle3, rectangle2, getIconTextGap());
        return new Point((getWidth() - insets.right) - this.sortArrow.getIconWidth(), rectangle3.f12373y);
    }

    /* loaded from: rt.jar:sun/swing/table/DefaultTableCellHeaderRenderer$EmptyIcon.class */
    private class EmptyIcon implements Icon, Serializable {
        int width;
        int height;

        private EmptyIcon() {
            this.width = 0;
            this.height = 0;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return this.width;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return this.height;
        }
    }
}
