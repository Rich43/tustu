package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.io.Serializable;
import javax.swing.JList;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/DefaultListCellRenderer.class */
public class DefaultListCellRenderer extends JLabel implements ListCellRenderer<Object>, Serializable {
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    private static final Border DEFAULT_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);
    protected static Border noFocusBorder = DEFAULT_NO_FOCUS_BORDER;

    /* loaded from: rt.jar:javax/swing/DefaultListCellRenderer$UIResource.class */
    public static class UIResource extends DefaultListCellRenderer implements javax.swing.plaf.UIResource {
    }

    public DefaultListCellRenderer() {
        setOpaque(true);
        setBorder(getNoFocusBorder());
        setName("List.cellRenderer");
    }

    private Border getNoFocusBorder() {
        Border border = DefaultLookup.getBorder(this, this.ui, "List.cellNoFocusBorder");
        if (System.getSecurityManager() != null) {
            return border != null ? border : SAFE_NO_FOCUS_BORDER;
        }
        if (border != null && (noFocusBorder == null || noFocusBorder == DEFAULT_NO_FOCUS_BORDER)) {
            return border;
        }
        return noFocusBorder;
    }

    public Component getListCellRendererComponent(JList<?> jList, Object obj, int i2, boolean z2, boolean z3) {
        setComponentOrientation(jList.getComponentOrientation());
        Color color = null;
        Color color2 = null;
        JList.DropLocation dropLocation = jList.getDropLocation();
        if (dropLocation != null && !dropLocation.isInsert() && dropLocation.getIndex() == i2) {
            color = DefaultLookup.getColor(this, this.ui, "List.dropCellBackground");
            color2 = DefaultLookup.getColor(this, this.ui, "List.dropCellForeground");
            z2 = true;
        }
        if (z2) {
            setBackground(color == null ? jList.getSelectionBackground() : color);
            setForeground(color2 == null ? jList.getSelectionForeground() : color2);
        } else {
            setBackground(jList.getBackground());
            setForeground(jList.getForeground());
        }
        if (obj instanceof Icon) {
            setIcon((Icon) obj);
            setText("");
        } else {
            setIcon(null);
            setText(obj == null ? "" : obj.toString());
        }
        setEnabled(jList.isEnabled());
        setFont(jList.getFont());
        Border noFocusBorder2 = null;
        if (z3) {
            if (z2) {
                noFocusBorder2 = DefaultLookup.getBorder(this, this.ui, "List.focusSelectedCellHighlightBorder");
            }
            if (noFocusBorder2 == null) {
                noFocusBorder2 = DefaultLookup.getBorder(this, this.ui, "List.focusCellHighlightBorder");
            }
        } else {
            noFocusBorder2 = getNoFocusBorder();
        }
        setBorder(noFocusBorder2);
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
    public void validate() {
    }

    @Override // java.awt.Container, java.awt.Component
    public void invalidate() {
    }

    @Override // java.awt.Component
    public void repaint() {
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

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // java.awt.Component
    public void firePropertyChange(String str, Object obj, Object obj2) {
        if (str == "text" || ((str == "font" || str == "foreground") && obj != obj2 && getClientProperty("html") != null)) {
            super.firePropertyChange(str, obj, obj2);
        }
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, byte b2, byte b3) {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, char c2, char c3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, short s2, short s3) {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, int i2, int i3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, long j2, long j3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, float f2, float f3) {
    }

    @Override // java.awt.Component
    public void firePropertyChange(String str, double d2, double d3) {
    }

    @Override // javax.swing.JComponent, java.awt.Component
    public void firePropertyChange(String str, boolean z2, boolean z3) {
    }
}
