package com.sun.java.swing.plaf.motif;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.Serializable;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifComboBoxUI.class */
public class MotifComboBoxUI extends BasicComboBoxUI implements Serializable {
    Icon arrowIcon;
    static final int HORIZ_MARGIN = 3;

    public static ComponentUI createUI(JComponent jComponent) {
        return new MotifComboBoxUI();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.arrowIcon = new MotifComboBoxArrowIcon(UIManager.getColor("controlHighlight"), UIManager.getColor("controlShadow"), UIManager.getColor("control"));
        SwingUtilities.invokeLater(new Runnable() { // from class: com.sun.java.swing.plaf.motif.MotifComboBoxUI.1
            @Override // java.lang.Runnable
            public void run() {
                if (MotifComboBoxUI.this.motifGetEditor() != null) {
                    MotifComboBoxUI.this.motifGetEditor().setBackground(UIManager.getColor("text"));
                }
            }
        });
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (!this.isMinimumSizeDirty) {
            return new Dimension(this.cachedMinimumSize);
        }
        Insets insets = getInsets();
        Dimension displaySize = getDisplaySize();
        displaySize.height += insets.top + insets.bottom;
        displaySize.width += insets.left + insets.right + iconAreaWidth();
        this.cachedMinimumSize.setSize(displaySize.width, displaySize.height);
        this.isMinimumSizeDirty = false;
        return displaySize;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboPopup createPopup() {
        return new MotifComboPopup(this.comboBox);
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifComboBoxUI$MotifComboPopup.class */
    protected class MotifComboPopup extends BasicComboPopup {
        public MotifComboPopup(JComboBox jComboBox) {
            super(jComboBox);
        }

        @Override // javax.swing.plaf.basic.BasicComboPopup
        public MouseMotionListener createListMouseMotionListener() {
            return new MouseMotionAdapter() { // from class: com.sun.java.swing.plaf.motif.MotifComboBoxUI.MotifComboPopup.1
            };
        }

        @Override // javax.swing.plaf.basic.BasicComboPopup
        public KeyListener createKeyListener() {
            return super.createKeyListener();
        }

        /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifComboBoxUI$MotifComboPopup$InvocationKeyHandler.class */
        protected class InvocationKeyHandler extends BasicComboPopup.InvocationKeyHandler {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            protected InvocationKeyHandler() {
                super();
                MotifComboPopup.this.getClass();
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void installComponents() {
        if (this.comboBox.isEditable()) {
            addEditor();
        }
        this.comboBox.add(this.currentValuePane);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void uninstallComponents() {
        removeEditor();
        this.comboBox.removeAll();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        Insets insets;
        boolean zHasFocus = this.comboBox.hasFocus();
        if (this.comboBox.isEnabled()) {
            graphics.setColor(this.comboBox.getBackground());
        } else {
            graphics.setColor(UIManager.getColor("ComboBox.disabledBackground"));
        }
        graphics.fillRect(0, 0, jComponent.getWidth(), jComponent.getHeight());
        if (!this.comboBox.isEditable()) {
            paintCurrentValue(graphics, rectangleForCurrentValue(), zHasFocus);
        }
        Rectangle rectangleRectangleForArrowIcon = rectangleForArrowIcon();
        this.arrowIcon.paintIcon(jComponent, graphics, rectangleRectangleForArrowIcon.f12372x, rectangleRectangleForArrowIcon.f12373y);
        if (!this.comboBox.isEditable()) {
            Border border = this.comboBox.getBorder();
            if (border != null) {
                insets = border.getBorderInsets(this.comboBox);
            } else {
                insets = new Insets(0, 0, 0, 0);
            }
            if (MotifGraphicsUtils.isLeftToRight(this.comboBox)) {
                rectangleRectangleForArrowIcon.f12372x -= 5;
            } else {
                rectangleRectangleForArrowIcon.f12372x += rectangleRectangleForArrowIcon.width + 3 + 1;
            }
            rectangleRectangleForArrowIcon.f12373y = insets.top;
            rectangleRectangleForArrowIcon.width = 1;
            rectangleRectangleForArrowIcon.height = (this.comboBox.getBounds().height - insets.bottom) - insets.top;
            graphics.setColor(UIManager.getColor("controlShadow"));
            graphics.fillRect(rectangleRectangleForArrowIcon.f12372x, rectangleRectangleForArrowIcon.f12373y, rectangleRectangleForArrowIcon.width, rectangleRectangleForArrowIcon.height);
            rectangleRectangleForArrowIcon.f12372x++;
            graphics.setColor(UIManager.getColor("controlHighlight"));
            graphics.fillRect(rectangleRectangleForArrowIcon.f12372x, rectangleRectangleForArrowIcon.f12373y, rectangleRectangleForArrowIcon.width, rectangleRectangleForArrowIcon.height);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void paintCurrentValue(Graphics graphics, Rectangle rectangle, boolean z2) {
        Component listCellRendererComponent = this.comboBox.getRenderer().getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
        listCellRendererComponent.setFont(this.comboBox.getFont());
        if (this.comboBox.isEnabled()) {
            listCellRendererComponent.setForeground(this.comboBox.getForeground());
            listCellRendererComponent.setBackground(this.comboBox.getBackground());
        } else {
            listCellRendererComponent.setForeground(UIManager.getColor("ComboBox.disabledForeground"));
            listCellRendererComponent.setBackground(UIManager.getColor("ComboBox.disabledBackground"));
        }
        this.currentValuePane.paintComponent(graphics, listCellRendererComponent, this.comboBox, rectangle.f12372x, rectangle.f12373y, rectangle.width, listCellRendererComponent.getPreferredSize().height);
    }

    protected Rectangle rectangleForArrowIcon() {
        Insets insets;
        Rectangle bounds = this.comboBox.getBounds();
        Border border = this.comboBox.getBorder();
        if (border != null) {
            insets = border.getBorderInsets(this.comboBox);
        } else {
            insets = new Insets(0, 0, 0, 0);
        }
        bounds.f12372x = insets.left;
        bounds.f12373y = insets.top;
        bounds.width -= insets.left + insets.right;
        bounds.height -= insets.top + insets.bottom;
        if (MotifGraphicsUtils.isLeftToRight(this.comboBox)) {
            bounds.f12372x = ((bounds.f12372x + bounds.width) - 3) - this.arrowIcon.getIconWidth();
        } else {
            bounds.f12372x += 3;
        }
        bounds.f12373y += (bounds.height - this.arrowIcon.getIconHeight()) / 2;
        bounds.width = this.arrowIcon.getIconWidth();
        bounds.height = this.arrowIcon.getIconHeight();
        return bounds;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected Rectangle rectangleForCurrentValue() {
        int width = this.comboBox.getWidth();
        int height = this.comboBox.getHeight();
        Insets insets = getInsets();
        if (MotifGraphicsUtils.isLeftToRight(this.comboBox)) {
            return new Rectangle(insets.left, insets.top, (width - (insets.left + insets.right)) - iconAreaWidth(), height - (insets.top + insets.bottom));
        }
        return new Rectangle(insets.left + iconAreaWidth(), insets.top, (width - (insets.left + insets.right)) - iconAreaWidth(), height - (insets.top + insets.bottom));
    }

    public int iconAreaWidth() {
        if (this.comboBox.isEditable()) {
            return this.arrowIcon.getIconWidth() + 6;
        }
        return this.arrowIcon.getIconWidth() + 9 + 2;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void configureEditor() {
        super.configureEditor();
        this.editor.setBackground(UIManager.getColor("text"));
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected LayoutManager createLayoutManager() {
        return new ComboBoxLayoutManager();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Component motifGetEditor() {
        return this.editor;
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifComboBoxUI$ComboBoxLayoutManager.class */
    public class ComboBoxLayoutManager extends BasicComboBoxUI.ComboBoxLayoutManager {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ComboBoxLayoutManager() {
            super();
            MotifComboBoxUI.this.getClass();
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager, java.awt.LayoutManager
        public void layoutContainer(Container container) {
            if (MotifComboBoxUI.this.motifGetEditor() != null) {
                Rectangle rectangleRectangleForCurrentValue = MotifComboBoxUI.this.rectangleForCurrentValue();
                rectangleRectangleForCurrentValue.f12372x++;
                rectangleRectangleForCurrentValue.f12373y++;
                rectangleRectangleForCurrentValue.width--;
                rectangleRectangleForCurrentValue.height -= 2;
                MotifComboBoxUI.this.motifGetEditor().setBounds(rectangleRectangleForCurrentValue);
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifComboBoxUI$MotifComboBoxArrowIcon.class */
    static class MotifComboBoxArrowIcon implements Icon, Serializable {
        private Color lightShadow;
        private Color darkShadow;
        private Color fill;

        public MotifComboBoxArrowIcon(Color color, Color color2, Color color3) {
            this.lightShadow = color;
            this.darkShadow = color2;
            this.fill = color3;
        }

        @Override // javax.swing.Icon
        public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
            int iconWidth = getIconWidth();
            int iconHeight = getIconHeight();
            graphics.setColor(this.lightShadow);
            graphics.drawLine(i2, i3, (i2 + iconWidth) - 1, i3);
            graphics.drawLine(i2, i3 + 1, (i2 + iconWidth) - 3, i3 + 1);
            graphics.setColor(this.darkShadow);
            graphics.drawLine((i2 + iconWidth) - 2, i3 + 1, (i2 + iconWidth) - 1, i3 + 1);
            int i4 = i2 + 1;
            int i5 = iconWidth - 6;
            for (int i6 = i3 + 2; i6 + 1 < i3 + iconHeight; i6 += 2) {
                graphics.setColor(this.lightShadow);
                graphics.drawLine(i4, i6, i4 + 1, i6);
                graphics.drawLine(i4, i6 + 1, i4 + 1, i6 + 1);
                if (i5 > 0) {
                    graphics.setColor(this.fill);
                    graphics.drawLine(i4 + 2, i6, i4 + 1 + i5, i6);
                    graphics.drawLine(i4 + 2, i6 + 1, i4 + 1 + i5, i6 + 1);
                }
                graphics.setColor(this.darkShadow);
                graphics.drawLine(i4 + i5 + 2, i6, i4 + i5 + 3, i6);
                graphics.drawLine(i4 + i5 + 2, i6 + 1, i4 + i5 + 3, i6 + 1);
                i4++;
                i5 -= 2;
            }
            graphics.setColor(this.darkShadow);
            graphics.drawLine(i2 + (iconWidth / 2), (i3 + iconHeight) - 1, i2 + (iconWidth / 2), (i3 + iconHeight) - 1);
        }

        @Override // javax.swing.Icon
        public int getIconWidth() {
            return 11;
        }

        @Override // javax.swing.Icon
        public int getIconHeight() {
            return 11;
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected PropertyChangeListener createPropertyChangeListener() {
        return new MotifPropertyChangeListener();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/motif/MotifComboBoxUI$MotifPropertyChangeListener.class */
    private class MotifPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler {
        private MotifPropertyChangeListener() {
            super();
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            Component componentMotifGetEditor;
            super.propertyChange(propertyChangeEvent);
            if (propertyChangeEvent.getPropertyName() == Enabled.NAME && MotifComboBoxUI.this.comboBox.isEnabled() && (componentMotifGetEditor = MotifComboBoxUI.this.motifGetEditor()) != null) {
                componentMotifGetEditor.setBackground(UIManager.getColor("text"));
            }
        }
    }
}
