package javax.swing.plaf.metal;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import javax.swing.CellRendererPane;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.UIManager;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxButton.class */
public class MetalComboBoxButton extends JButton {
    protected JComboBox comboBox;
    protected JList listBox;
    protected CellRendererPane rendererPane;
    protected Icon comboIcon;
    protected boolean iconOnly;

    public final JComboBox getComboBox() {
        return this.comboBox;
    }

    public final void setComboBox(JComboBox jComboBox) {
        this.comboBox = jComboBox;
    }

    public final Icon getComboIcon() {
        return this.comboIcon;
    }

    public final void setComboIcon(Icon icon) {
        this.comboIcon = icon;
    }

    public final boolean isIconOnly() {
        return this.iconOnly;
    }

    public final void setIconOnly(boolean z2) {
        this.iconOnly = z2;
    }

    MetalComboBoxButton() {
        super("");
        this.iconOnly = false;
        setModel(new DefaultButtonModel() { // from class: javax.swing.plaf.metal.MetalComboBoxButton.1
            @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
            public void setArmed(boolean z2) {
                super.setArmed(isPressed() ? true : z2);
            }
        });
    }

    public MetalComboBoxButton(JComboBox jComboBox, Icon icon, CellRendererPane cellRendererPane, JList jList) {
        this();
        this.comboBox = jComboBox;
        this.comboIcon = icon;
        this.rendererPane = cellRendererPane;
        this.listBox = jList;
        setEnabled(this.comboBox.isEnabled());
    }

    public MetalComboBoxButton(JComboBox jComboBox, Icon icon, boolean z2, CellRendererPane cellRendererPane, JList jList) {
        this(jComboBox, icon, cellRendererPane, jList);
        this.iconOnly = z2;
    }

    @Override // java.awt.Component
    public boolean isFocusTraversable() {
        return false;
    }

    @Override // javax.swing.AbstractButton, javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        if (z2) {
            setBackground(this.comboBox.getBackground());
            setForeground(this.comboBox.getForeground());
        } else {
            setBackground(UIManager.getColor("ComboBox.disabledBackground"));
            setForeground(UIManager.getColor("ComboBox.disabledForeground"));
        }
    }

    @Override // javax.swing.JComponent
    public void paintComponent(Graphics graphics) {
        int width;
        int height;
        boolean zIsLeftToRight = MetalUtils.isLeftToRight(this.comboBox);
        super.paintComponent(graphics);
        Insets insets = getInsets();
        int width2 = getWidth() - (insets.left + insets.right);
        int height2 = getHeight() - (insets.top + insets.bottom);
        if (height2 <= 0 || width2 <= 0) {
            return;
        }
        int i2 = insets.left;
        int i3 = insets.top;
        int i4 = i2 + (width2 - 1);
        int i5 = i3 + (height2 - 1);
        int iconWidth = 0;
        int i6 = zIsLeftToRight ? i4 : i2;
        if (this.comboIcon != null) {
            iconWidth = this.comboIcon.getIconWidth();
            int iconHeight = this.comboIcon.getIconHeight();
            if (this.iconOnly) {
                width = (getWidth() / 2) - (iconWidth / 2);
                height = (getHeight() / 2) - (iconHeight / 2);
            } else {
                if (zIsLeftToRight) {
                    width = (i2 + (width2 - 1)) - iconWidth;
                } else {
                    width = i2;
                }
                height = (i3 + ((i5 - i3) / 2)) - (iconHeight / 2);
            }
            this.comboIcon.paintIcon(this, graphics, width, height);
            if (this.comboBox.hasFocus() && (!MetalLookAndFeel.usingOcean() || this.comboBox.isEditable())) {
                graphics.setColor(MetalLookAndFeel.getFocusColor());
                graphics.drawRect(i2 - 1, i3 - 1, width2 + 3, height2 + 1);
            }
        }
        if (!MetalLookAndFeel.usingOcean() && !this.iconOnly && this.comboBox != null) {
            Component listCellRendererComponent = this.comboBox.getRenderer().getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, getModel().isPressed(), false);
            listCellRendererComponent.setFont(this.rendererPane.getFont());
            if (this.model.isArmed() && this.model.isPressed()) {
                if (isOpaque()) {
                    listCellRendererComponent.setBackground(UIManager.getColor("Button.select"));
                }
                listCellRendererComponent.setForeground(this.comboBox.getForeground());
            } else if (!this.comboBox.isEnabled()) {
                if (isOpaque()) {
                    listCellRendererComponent.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
                }
                listCellRendererComponent.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            } else {
                listCellRendererComponent.setForeground(this.comboBox.getForeground());
                listCellRendererComponent.setBackground(this.comboBox.getBackground());
            }
            int i7 = width2 - (insets.right + iconWidth);
            boolean z2 = false;
            if (listCellRendererComponent instanceof JPanel) {
                z2 = true;
            }
            if (zIsLeftToRight) {
                this.rendererPane.paintComponent(graphics, listCellRendererComponent, this, i2, i3, i7, height2, z2);
            } else {
                this.rendererPane.paintComponent(graphics, listCellRendererComponent, this, i2 + iconWidth, i3, i7, height2, z2);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension dimension = new Dimension();
        Insets insets = getInsets();
        dimension.width = insets.left + getComboIcon().getIconWidth() + insets.right;
        dimension.height = insets.bottom + getComboIcon().getIconHeight() + insets.top;
        return dimension;
    }
}
