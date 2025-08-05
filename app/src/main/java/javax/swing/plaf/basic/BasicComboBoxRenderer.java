package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Dimension;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxRenderer.class */
public class BasicComboBoxRenderer extends JLabel implements ListCellRenderer, Serializable {
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    private static final Border SAFE_NO_FOCUS_BORDER = new EmptyBorder(1, 1, 1, 1);

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicComboBoxRenderer$UIResource.class */
    public static class UIResource extends BasicComboBoxRenderer implements javax.swing.plaf.UIResource {
    }

    public BasicComboBoxRenderer() {
        setOpaque(true);
        setBorder(getNoFocusBorder());
    }

    private static Border getNoFocusBorder() {
        if (System.getSecurityManager() != null) {
            return SAFE_NO_FOCUS_BORDER;
        }
        return noFocusBorder;
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getPreferredSize() {
        Dimension preferredSize;
        if (getText() == null || getText().equals("")) {
            setText(" ");
            preferredSize = super.getPreferredSize();
            setText("");
        } else {
            preferredSize = super.getPreferredSize();
        }
        return preferredSize;
    }

    public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
        if (z2) {
            setBackground(jList.getSelectionBackground());
            setForeground(jList.getSelectionForeground());
        } else {
            setBackground(jList.getBackground());
            setForeground(jList.getForeground());
        }
        setFont(jList.getFont());
        if (obj instanceof Icon) {
            setIcon((Icon) obj);
        } else {
            setText(obj == null ? "" : obj.toString());
        }
        return this;
    }
}
