package javax.swing.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/table/DefaultTableCellRenderer.class */
public class DefaultTableCellRenderer extends JLabel implements TableCellRenderer, Serializable {
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;
    private Color unselectedForeground;
    private Color unselectedBackground;

    /* loaded from: rt.jar:javax/swing/table/DefaultTableCellRenderer$UIResource.class */
    public static class UIResource extends DefaultTableCellRenderer implements javax.swing.plaf.UIResource {
    }

    public DefaultTableCellRenderer() {
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("Table.cellRenderer");
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, this.ui, "Table.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            return border != null ? border : SAFE_NO_FOCUS_BORDER;
        }
        if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
            return border;
        }
        return noFocusBorder;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setForeground(Color color) {
        super.setForeground(color);
        this.unselectedForeground = color;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void setBackground(Color color) {
        super.setBackground(color);
        this.unselectedBackground = color;
    }

    @Override // javax.swing.JLabel, javax.swing.JComponent
    public void updateUI() {
        super.updateUI();
        setForeground(null);
        setBackground(null);
    }

    public Component getTableCellRendererComponent(JTable jTable, Object obj, boolean z2, boolean z3, int i2, int i3) {
        Color color;
        if (jTable == null) {
            return this;
        }
        Color color2 = null;
        Color color3 = null;
        JTable.DropLocation dropLocation = jTable.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsertRow() && !dropLocation.isInsertColumn() && dropLocation.getRow() == i2 && dropLocation.getColumn() == i3) {
            color2 = DefaultLookup.getColor(this, this.ui, "Table.dropCellForeground");
            color3 = DefaultLookup.getColor(this, this.ui, "Table.dropCellBackground");
            z2 = true;
        }
        if (z2) {
            super.setForeground(color2 == null ? jTable.getSelectionForeground() : color2);
            super.setBackground(color3 == null ? jTable.getSelectionBackground() : color3);
        } else {
            Color background = this.unselectedBackground != null ? this.unselectedBackground : jTable.getBackground();
            if ((background == null || (background instanceof javax.swing.plaf.UIResource)) && (color = DefaultLookup.getColor(this, this.ui, "Table.alternateRowColor")) != null && i2 % 2 != 0) {
                background = color;
            }
            super.setForeground(this.unselectedForeground != null ? this.unselectedForeground : jTable.getForeground());
            super.setBackground(background);
        }
        setFont(jTable.getFont());
        if (z3) {
            Border border = null;
            if (z2) {
                border = DefaultLookup.getBorder(this, this.ui, "Table.focusSelectedCellHighlightBorder");
            }
            if (border == null) {
                border = DefaultLookup.getBorder(this, this.ui, "Table.focusCellHighlightBorder");
            }
            setBorder(border);
            if (!z2 && jTable.isCellEditable(i2, i3)) {
                Color color4 = DefaultLookup.getColor(this, this.ui, "Table.focusCellForeground");
                if (color4 != null) {
                    super.setForeground(color4);
                }
                Color color5 = DefaultLookup.getColor(this, this.ui, "Table.focusCellBackground");
                if (color5 != null) {
                    super.setBackground(color5);
                }
            }
        } else {
            setBorder(getNoFocusBorder());
        }
        setValue(obj);
        return this;
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public boolean isOpaque() {
        Color background = getBackground();
        Container parent = getParent();
        if (parent != null) {
            parent = parent.getParent();
        }
        return !(background != null && parent != null && background.equals(parent.getBackground()) && parent.isOpaque()) && super.isOpaque();
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
    }

    @Override // java.awt.Container, java.awt.Component
    public void validate() {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void revalidate() {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void repaint(long j2, int i2, int i3, int i4, int i5) {
    }

    @Override // javax.swing.JComponent
    public void repaint(Rectangle rectangle) {
    }

    @Override // java.awt.Component
    public void repaint() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.awt.Component
    public void firePropertyChange(String str, Object obj, Object obj2) {
        if (str == "text" || str == "labelFor" || str == "displayedMnemonic" || ((str == "font" || str == "foreground") && obj != obj2 && getClientProperty("html") != null)) {
            super.firePropertyChange(str, obj, obj2);
        }
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, boolean z2, boolean z3) {
    }

    protected void setValue(Object obj) {
        setText(obj == null ? "" : obj.toString());
    }
}
