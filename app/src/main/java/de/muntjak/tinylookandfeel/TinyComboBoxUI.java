package de.muntjak.tinylookandfeel;

import de.muntjak.tinylookandfeel.TinyComboBoxEditor;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxEditor;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.basic.BasicComboBoxUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxUI.class */
public class TinyComboBoxUI extends BasicComboBoxUI {
    static final int COMBO_BUTTON_WIDTH = 18;
    protected boolean isDisplaySizeDirty = true;
    protected Dimension cachedDisplaySize = new Dimension(0, 0);
    private static final Insets DEFAULT_INSETS = new Insets(0, 0, 0, 0);

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxUI$TinyComboBoxLayoutManager.class */
    public class TinyComboBoxLayoutManager implements LayoutManager {
        private final TinyComboBoxUI this$0;

        public TinyComboBoxLayoutManager(TinyComboBoxUI tinyComboBoxUI) {
            this.this$0 = tinyComboBoxUI;
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return container.getPreferredSize();
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return container.getMinimumSize();
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            JComboBox jComboBox = (JComboBox) container;
            int width = jComboBox.getWidth();
            int height = jComboBox.getHeight();
            if (!this.this$0.comboBox.isEditable()) {
                this.this$0.arrowButton.setBounds(0, 0, width, height);
                return;
            }
            if (this.this$0.arrowButton != null) {
                this.this$0.arrowButton.setBounds(width - 18, 0, 18, height);
            }
            if (this.this$0.editor != null) {
                this.this$0.editor.setBounds(this.this$0.rectangleForCurrentValue2());
            }
        }
    }

    /* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyComboBoxUI$TinyPropertyChangeListener.class */
    public class TinyPropertyChangeListener extends BasicComboBoxUI.PropertyChangeHandler {
        private final TinyComboBoxUI this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public TinyPropertyChangeListener(TinyComboBoxUI tinyComboBoxUI) {
            super();
            this.this$0 = tinyComboBoxUI;
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxUI.PropertyChangeHandler, java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            super.propertyChange(propertyChangeEvent);
            String propertyName = propertyChangeEvent.getPropertyName();
            if (propertyName.equals(JTree.EDITABLE_PROPERTY)) {
                ((TinyComboBoxButton) this.this$0.arrowButton).setIconOnly(this.this$0.comboBox.isEditable());
                this.this$0.isMinimumSizeDirty = true;
                this.this$0.isDisplaySizeDirty = true;
                this.this$0.comboBox.revalidate();
                return;
            }
            if (propertyName.equals("background")) {
                this.this$0.listBox.setBackground((Color) propertyChangeEvent.getNewValue());
            } else if (propertyName.equals("foreground")) {
                this.this$0.listBox.setForeground((Color) propertyChangeEvent.getNewValue());
            }
        }
    }

    public static ComponentUI createUI(JComponent jComponent) {
        return new TinyComboBoxUI();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboBoxEditor createEditor() {
        return new TinyComboBoxEditor.UIResource();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected JButton createArrowButton() {
        TinyComboBoxButton tinyComboBoxButton = new TinyComboBoxButton(this.comboBox, null, this.comboBox.isEditable(), this.currentValuePane, this.listBox);
        tinyComboBoxButton.setMargin(DEFAULT_INSETS);
        tinyComboBoxButton.putClientProperty("isComboBoxButton", Boolean.TRUE);
        return tinyComboBoxButton;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void installComponents() {
        super.installComponents();
        if (this.arrowButton != null) {
            this.arrowButton.setFocusable(false);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public PropertyChangeListener createPropertyChangeListener() {
        return new TinyPropertyChangeListener(this);
    }

    protected void editablePropertyChanged(PropertyChangeEvent propertyChangeEvent) {
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected LayoutManager createLayoutManager() {
        return new TinyComboBoxLayoutManager(this);
    }

    protected Rectangle rectangleForCurrentValue2() {
        int width = this.comboBox.getWidth();
        int height = this.comboBox.getHeight();
        Insets insets = getInsets();
        int i2 = height - (insets.top + insets.bottom);
        if (this.arrowButton != null) {
            i2 = 18;
        }
        return this.comboBox.getComponentOrientation().isLeftToRight() ? new Rectangle(insets.left, insets.top, width - ((insets.left + insets.right) + i2), height - (insets.top + insets.bottom)) : new Rectangle(insets.left + i2, insets.top, width - ((insets.left + insets.right) + i2), height - (insets.top + insets.bottom));
    }

    protected void removeListeners() {
        if (this.propertyChangeListener != null) {
            this.comboBox.removePropertyChangeListener(this.propertyChangeListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        if (!this.isMinimumSizeDirty) {
            this.isDisplaySizeDirty = true;
            return new Dimension(this.cachedMinimumSize);
        }
        InsetsUIResource insetsUIResource = Theme.comboInsets;
        Dimension displaySize = getDisplaySize();
        displaySize.width += 18;
        displaySize.width += insetsUIResource.left + insetsUIResource.right;
        displaySize.height += insetsUIResource.top + insetsUIResource.bottom;
        this.cachedMinimumSize.setSize(displaySize.width, displaySize.height);
        this.isMinimumSizeDirty = false;
        return new Dimension(this.cachedMinimumSize);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected Dimension getDisplaySize() {
        if (!this.isDisplaySizeDirty) {
            return new Dimension(this.cachedDisplaySize);
        }
        Dimension dimension = new Dimension();
        ListCellRenderer renderer = this.comboBox.getRenderer();
        if (renderer == null) {
            renderer = new DefaultListCellRenderer();
        }
        Object prototypeDisplayValue = this.comboBox.getPrototypeDisplayValue();
        if (prototypeDisplayValue != null) {
            dimension = getSizeForComponent(renderer.getListCellRendererComponent(this.listBox, prototypeDisplayValue, -1, false, false));
        } else {
            ComboBoxModel model = this.comboBox.getModel();
            int size = model.getSize();
            if (size > 0) {
                for (int i2 = 0; i2 < size; i2++) {
                    Dimension sizeForComponent = getSizeForComponent(renderer.getListCellRendererComponent(this.listBox, model.getElementAt(i2), -1, false, false));
                    dimension.width = Math.max(dimension.width, sizeForComponent.width);
                    dimension.height = Math.max(dimension.height, sizeForComponent.height);
                }
            } else {
                dimension = getDefaultSize();
                if (this.comboBox.isEditable()) {
                    dimension.width = 100;
                }
            }
        }
        if (this.comboBox.isEditable()) {
            Dimension preferredSize = this.editor.getPreferredSize();
            dimension.width = Math.max(dimension.width, preferredSize.width);
            dimension.height = Math.max(dimension.height, preferredSize.height);
        }
        this.cachedDisplaySize.setSize(dimension.width, dimension.height);
        this.isDisplaySizeDirty = false;
        return dimension;
    }

    private Dimension getSizeForComponent(Component component) {
        this.currentValuePane.add(component);
        component.setFont(this.comboBox.getFont());
        Dimension preferredSize = component.getPreferredSize();
        this.currentValuePane.remove(component);
        return preferredSize;
    }
}
