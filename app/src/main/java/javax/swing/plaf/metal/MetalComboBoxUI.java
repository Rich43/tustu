package javax.swing.plaf.metal;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.plaf.metal.MetalComboBoxEditor;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxUI.class */
public class MetalComboBoxUI extends BasicComboBoxUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new MetalComboBoxUI();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (MetalLookAndFeel.usingOcean()) {
            super.paint(graphics, jComponent);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void paintCurrentValue(Graphics graphics, Rectangle rectangle, boolean z2) {
        if (MetalLookAndFeel.usingOcean()) {
            rectangle.f12372x += 2;
            rectangle.width -= 3;
            if (this.arrowButton != null) {
                Insets insets = this.arrowButton.getInsets();
                rectangle.f12373y += insets.top;
                rectangle.height -= insets.top + insets.bottom;
            } else {
                rectangle.f12373y += 2;
                rectangle.height -= 4;
            }
            super.paintCurrentValue(graphics, rectangle, z2);
            return;
        }
        if (graphics == null || rectangle == null) {
            throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void paintCurrentValueBackground(Graphics graphics, Rectangle rectangle, boolean z2) {
        if (MetalLookAndFeel.usingOcean()) {
            graphics.setColor(MetalLookAndFeel.getControlDarkShadow());
            graphics.drawRect(rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height - 1);
            graphics.setColor(MetalLookAndFeel.getControlShadow());
            graphics.drawRect(rectangle.f12372x + 1, rectangle.f12373y + 1, rectangle.width - 2, rectangle.height - 3);
            if (z2 && !isPopupVisible(this.comboBox) && this.arrowButton != null) {
                graphics.setColor(this.listBox.getSelectionBackground());
                Insets insets = this.arrowButton.getInsets();
                if (insets.top > 2) {
                    graphics.fillRect(rectangle.f12372x + 2, rectangle.f12373y + 2, rectangle.width - 3, insets.top - 2);
                }
                if (insets.bottom > 2) {
                    graphics.fillRect(rectangle.f12372x + 2, (rectangle.f12373y + rectangle.height) - insets.bottom, rectangle.width - 3, insets.bottom - 2);
                    return;
                }
                return;
            }
            return;
        }
        if (graphics == null || rectangle == null) {
            throw new NullPointerException("Must supply a non-null Graphics and Rectangle");
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) throws IllegalArgumentException {
        int baseline;
        if (MetalLookAndFeel.usingOcean() && i3 >= 4) {
            baseline = super.getBaseline(jComponent, i2, i3 - 4);
            if (baseline >= 0) {
                baseline += 2;
            }
        } else {
            baseline = super.getBaseline(jComponent, i2, i3);
        }
        return baseline;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboBoxEditor createEditor() {
        return new MetalComboBoxEditor.UIResource();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboPopup createPopup() {
        return super.createPopup();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected JButton createArrowButton() {
        MetalComboBoxButton metalComboBoxButton = new MetalComboBoxButton(this.comboBox, new MetalComboBoxIcon(), this.comboBox.isEditable() || MetalLookAndFeel.usingOcean(), this.currentValuePane, this.listBox);
        metalComboBoxButton.setMargin(new Insets(0, 1, 1, 3));
        if (MetalLookAndFeel.usingOcean()) {
            metalComboBoxButton.putClientProperty(MetalBorders.NO_BUTTON_ROLLOVER, Boolean.TRUE);
        }
        updateButtonForOcean(metalComboBoxButton);
        return metalComboBoxButton;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateButtonForOcean(JButton jButton) {
        if (MetalLookAndFeel.usingOcean()) {
            jButton.setFocusPainted(this.comboBox.isEditable());
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public PropertyChangeListener createPropertyChangeListener() {
        return new MetalPropertyChangeListener();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxUI$MetalPropertyChangeListener.class */
    public class MetalPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler {
        public MetalPropertyChangeListener() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            super.propertyChange(propertyChangeEvent);
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName == JTree.EDITABLE_PROPERTY) {
                if (MetalComboBoxUI.this.arrowButton instanceof MetalComboBoxButton) {
                    ((MetalComboBoxButton) MetalComboBoxUI.this.arrowButton).setIconOnly(MetalComboBoxUI.this.comboBox.isEditable() || MetalLookAndFeel.usingOcean());
                }
                MetalComboBoxUI.this.comboBox.repaint();
                MetalComboBoxUI.this.updateButtonForOcean(MetalComboBoxUI.this.arrowButton);
                return;
            }
            if (propertyName == "background") {
                Color color = (Color) propertyChangeEvent.getNewValue();
                MetalComboBoxUI.this.arrowButton.setBackground(color);
                MetalComboBoxUI.this.listBox.setBackground(color);
            } else if (propertyName == "foreground") {
                Color color2 = (Color) propertyChangeEvent.getNewValue();
                MetalComboBoxUI.this.arrowButton.setForeground(color2);
                MetalComboBoxUI.this.listBox.setForeground(color2);
            }
        }
    }

    @Deprecated
    protected void editablePropertyChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected LayoutManager createLayoutManager() {
        return new MetalComboBoxLayoutManager();
    }

    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxUI$MetalComboBoxLayoutManager.class */
    public class MetalComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager {
        public MetalComboBoxLayoutManager() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager, java.awt.LayoutManager
        public void layoutContainer(Container container) {
            MetalComboBoxUI.this.layoutComboBox(container, this);
        }

        public void superLayout(Container container) {
            super.layoutContainer(container);
        }
    }

    public void layoutComboBox(Container container, MetalComboBoxLayoutManager metalComboBoxLayoutManager) {
        if (this.comboBox.isEditable() && !MetalLookAndFeel.usingOcean()) {
            metalComboBoxLayoutManager.superLayout(container);
            return;
        }
        if (this.arrowButton != null) {
            if (MetalLookAndFeel.usingOcean()) {
                Insets insets = this.comboBox.getInsets();
                int i2 = this.arrowButton.getMinimumSize().width;
                this.arrowButton.setBounds(MetalUtils.isLeftToRight(this.comboBox) ? (this.comboBox.getWidth() - insets.right) - i2 : insets.left, insets.top, i2, (this.comboBox.getHeight() - insets.top) - insets.bottom);
            } else {
                Insets insets2 = this.comboBox.getInsets();
                this.arrowButton.setBounds(insets2.left, insets2.top, this.comboBox.getWidth() - (insets2.left + insets2.right), this.comboBox.getHeight() - (insets2.top + insets2.bottom));
            }
        }
        if (this.editor != null && MetalLookAndFeel.usingOcean()) {
            this.editor.setBounds(rectangleForCurrentValue());
        }
    }

    @Deprecated
    protected void removeListeners() {
        if (this.propertyChangeListener != null) {
            this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void configureEditor() {
        super.configureEditor();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void unconfigureEditor() {
        super.unconfigureEditor();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension minimumSize;
        if (!this.isMinimumSizeDirty) {
            return new Dimension(this.cachedMinimumSize);
        }
        if (!this.comboBox.isEditable() && this.arrowButton != null) {
            Insets insets = this.arrowButton.getInsets();
            Insets insets2 = this.comboBox.getInsets();
            minimumSize = getDisplaySize();
            minimumSize.width += insets2.left + insets2.right;
            minimumSize.width += insets.right;
            minimumSize.width += this.arrowButton.getMinimumSize().width;
            minimumSize.height += insets2.top + insets2.bottom;
            minimumSize.height += insets.top + insets.bottom;
        } else if (this.comboBox.isEditable() && this.arrowButton != null && this.editor != null) {
            minimumSize = super.getMinimumSize(jComponent);
            Insets margin = this.arrowButton.getMargin();
            minimumSize.height += margin.top + margin.bottom;
            minimumSize.width += margin.left + margin.right;
        } else {
            minimumSize = super.getMinimumSize(jComponent);
        }
        this.cachedMinimumSize.setSize(minimumSize.width, minimumSize.height);
        this.isMinimumSizeDirty = false;
        return new Dimension(this.cachedMinimumSize);
    }

    @Deprecated
    /* loaded from: rt.jar:javax/swing/plaf/metal/MetalComboBoxUI$MetalComboPopup.class */
    public class MetalComboPopup extends BasicComboPopup {
        public MetalComboPopup(JComboBox jComboBox) {
            super(jComboBox);
        }

        @Override // javax.swing.plaf.basic.BasicComboPopup
        public void delegateFocus(MouseEvent mouseEvent) {
            super.delegateFocus(mouseEvent);
        }
    }
}
