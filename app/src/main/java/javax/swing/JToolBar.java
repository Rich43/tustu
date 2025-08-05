package javax.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleStateSet;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ToolBarUI;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/JToolBar.class */
public class JToolBar extends JComponent implements SwingConstants, Accessible {
    private static final String uiClassID = "ToolBarUI";
    private boolean paintBorder;
    private Insets margin;
    private boolean floatable;
    private int orientation;

    public JToolBar() {
        this(0);
    }

    public JToolBar(int i2) {
        this(null, i2);
    }

    public JToolBar(String str) {
        this(str, 0);
    }

    public JToolBar(String str, int i2) {
        this.paintBorder = true;
        this.margin = null;
        this.floatable = true;
        this.orientation = 0;
        setName(str);
        checkOrientation(i2);
        this.orientation = i2;
        DefaultToolBarLayout defaultToolBarLayout = new DefaultToolBarLayout(i2);
        setLayout(defaultToolBarLayout);
        addPropertyChangeListener(defaultToolBarLayout);
        updateUI();
    }

    public ToolBarUI getUI() {
        return (ToolBarUI) this.ui;
    }

    public void setUI(ToolBarUI toolBarUI) {
        super.setUI((ComponentUI) toolBarUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ToolBarUI) UIManager.getUI(this));
        if (getLayout() == null) {
            setLayout(new DefaultToolBarLayout(getOrientation()));
        }
        invalidate();
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public int getComponentIndex(Component component) {
        int componentCount = getComponentCount();
        Component[] components = getComponents();
        for (int i2 = 0; i2 < componentCount; i2++) {
            if (components[i2] == component) {
                return i2;
            }
        }
        return -1;
    }

    public Component getComponentAtIndex(int i2) {
        int componentCount = getComponentCount();
        if (i2 >= 0 && i2 < componentCount) {
            return getComponents()[i2];
        }
        return null;
    }

    public void setMargin(Insets insets) {
        Insets insets2 = this.margin;
        this.margin = insets;
        firePropertyChange(AbstractButton.MARGIN_CHANGED_PROPERTY, insets2, insets);
        revalidate();
        repaint();
    }

    public Insets getMargin() {
        if (this.margin == null) {
            return new Insets(0, 0, 0, 0);
        }
        return this.margin;
    }

    public boolean isBorderPainted() {
        return this.paintBorder;
    }

    public void setBorderPainted(boolean z2) {
        if (this.paintBorder != z2) {
            boolean z3 = this.paintBorder;
            this.paintBorder = z2;
            firePropertyChange(AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, z3, z2);
            revalidate();
            repaint();
        }
    }

    @Override // javax.swing.JComponent
    protected void paintBorder(Graphics graphics) {
        if (isBorderPainted()) {
            super.paintBorder(graphics);
        }
    }

    public boolean isFloatable() {
        return this.floatable;
    }

    public void setFloatable(boolean z2) {
        if (this.floatable != z2) {
            boolean z3 = this.floatable;
            this.floatable = z2;
            firePropertyChange("floatable", z3, z2);
            revalidate();
            repaint();
        }
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i2) {
        checkOrientation(i2);
        if (this.orientation != i2) {
            int i3 = this.orientation;
            this.orientation = i2;
            firePropertyChange("orientation", i3, i2);
            revalidate();
            repaint();
        }
    }

    public void setRollover(boolean z2) {
        putClientProperty("JToolBar.isRollover", z2 ? Boolean.TRUE : Boolean.FALSE);
    }

    public boolean isRollover() {
        Boolean bool = (Boolean) getClientProperty("JToolBar.isRollover");
        if (bool != null) {
            return bool.booleanValue();
        }
        return false;
    }

    private void checkOrientation(int i2) {
        switch (i2) {
            case 0:
            case 1:
                return;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    public void addSeparator() {
        addSeparator(null);
    }

    public void addSeparator(Dimension dimension) {
        add(new Separator(dimension));
    }

    public JButton add(Action action) {
        JButton jButtonCreateActionComponent = createActionComponent(action);
        jButtonCreateActionComponent.setAction(action);
        add(jButtonCreateActionComponent);
        return jButtonCreateActionComponent;
    }

    protected JButton createActionComponent(Action action) {
        JButton jButton = new JButton() { // from class: javax.swing.JToolBar.1
            @Override // javax.swing.AbstractButton
            protected PropertyChangeListener createActionPropertyChangeListener(Action action2) {
                PropertyChangeListener propertyChangeListenerCreateActionChangeListener = JToolBar.this.createActionChangeListener(this);
                if (propertyChangeListenerCreateActionChangeListener == null) {
                    propertyChangeListenerCreateActionChangeListener = super.createActionPropertyChangeListener(action2);
                }
                return propertyChangeListenerCreateActionChangeListener;
            }
        };
        if (action != null && (action.getValue(Action.SMALL_ICON) != null || action.getValue(Action.LARGE_ICON_KEY) != null)) {
            jButton.setHideActionText(true);
        }
        jButton.setHorizontalTextPosition(0);
        jButton.setVerticalTextPosition(3);
        return jButton;
    }

    protected PropertyChangeListener createActionChangeListener(JButton jButton) {
        return null;
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        if (component instanceof Separator) {
            if (getOrientation() == 1) {
                ((Separator) component).setOrientation(0);
            } else {
                ((Separator) component).setOrientation(1);
            }
        }
        super.addImpl(component, obj, i2);
        if (component instanceof JButton) {
            ((JButton) component).setDefaultCapable(false);
        }
    }

    /* loaded from: rt.jar:javax/swing/JToolBar$Separator.class */
    public static class Separator extends JSeparator {
        private Dimension separatorSize;

        public Separator() {
            this(null);
        }

        public Separator(Dimension dimension) {
            super(0);
            setSeparatorSize(dimension);
        }

        @Override // javax.swing.JSeparator, javax.swing.JComponent
        public String getUIClassID() {
            return "ToolBarSeparatorUI";
        }

        public void setSeparatorSize(Dimension dimension) {
            if (dimension != null) {
                this.separatorSize = dimension;
            } else {
                super.updateUI();
            }
            invalidate();
        }

        public Dimension getSeparatorSize() {
            return this.separatorSize;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getMinimumSize() {
            if (this.separatorSize != null) {
                return this.separatorSize.getSize();
            }
            return super.getMinimumSize();
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getMaximumSize() {
            if (this.separatorSize != null) {
                return this.separatorSize.getSize();
            }
            return super.getMaximumSize();
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            if (this.separatorSize != null) {
                return this.separatorSize.getSize();
            }
            return super.getPreferredSize();
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        if (getUIClassID().equals(uiClassID)) {
            byte writeObjCounter = (byte) (JComponent.getWriteObjCounter(this) - 1);
            JComponent.setWriteObjCounter(this, writeObjCounter);
            if (writeObjCounter == 0 && this.ui != null) {
                this.ui.installUI(this);
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",floatable=" + (this.floatable ? "true" : "false") + ",margin=" + (this.margin != null ? this.margin.toString() : "") + ",orientation=" + (this.orientation == 0 ? "HORIZONTAL" : "VERTICAL") + ",paintBorder=" + (this.paintBorder ? "true" : "false");
    }

    /* loaded from: rt.jar:javax/swing/JToolBar$DefaultToolBarLayout.class */
    private class DefaultToolBarLayout implements LayoutManager2, Serializable, PropertyChangeListener, UIResource {
        BoxLayout lm;

        DefaultToolBarLayout(int i2) {
            if (i2 == 1) {
                this.lm = new BoxLayout(JToolBar.this, 3);
            } else {
                this.lm = new BoxLayout(JToolBar.this, 2);
            }
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
            this.lm.addLayoutComponent(str, component);
        }

        @Override // java.awt.LayoutManager2
        public void addLayoutComponent(Component component, Object obj) {
            this.lm.addLayoutComponent(component, obj);
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
            this.lm.removeLayoutComponent(component);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            return this.lm.preferredLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return this.lm.minimumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager2
        public Dimension maximumLayoutSize(Container container) {
            return this.lm.maximumLayoutSize(container);
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            this.lm.layoutContainer(container);
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentX(Container container) {
            return this.lm.getLayoutAlignmentX(container);
        }

        @Override // java.awt.LayoutManager2
        public float getLayoutAlignmentY(Container container) {
            return this.lm.getLayoutAlignmentY(container);
        }

        @Override // java.awt.LayoutManager2
        public void invalidateLayout(Container container) {
            this.lm.invalidateLayout(container);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals("orientation")) {
                if (((Integer) propertyChangeEvent.getNewValue()).intValue() == 1) {
                    this.lm = new BoxLayout(JToolBar.this, 3);
                } else {
                    this.lm = new BoxLayout(JToolBar.this, 2);
                }
            }
        }
    }

    @Override // java.awt.Container
    public void setLayout(LayoutManager layoutManager) {
        LayoutManager layout = getLayout();
        if (layout instanceof PropertyChangeListener) {
            removePropertyChangeListener((PropertyChangeListener) layout);
        }
        super.setLayout(layoutManager);
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJToolBar();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JToolBar$AccessibleJToolBar.class */
    protected class AccessibleJToolBar extends JComponent.AccessibleJComponent {
        protected AccessibleJToolBar() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            return super.getAccessibleStateSet();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.TOOL_BAR;
        }
    }
}
