package javax.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.beans.ConstructorProperties;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SplitPaneUI;

/* loaded from: rt.jar:javax/swing/JSplitPane.class */
public class JSplitPane extends JComponent implements Accessible {
    private static final String uiClassID = "SplitPaneUI";
    public static final int VERTICAL_SPLIT = 0;
    public static final int HORIZONTAL_SPLIT = 1;
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final String TOP = "top";
    public static final String BOTTOM = "bottom";
    public static final String DIVIDER = "divider";
    public static final String ORIENTATION_PROPERTY = "orientation";
    public static final String CONTINUOUS_LAYOUT_PROPERTY = "continuousLayout";
    public static final String DIVIDER_SIZE_PROPERTY = "dividerSize";
    public static final String ONE_TOUCH_EXPANDABLE_PROPERTY = "oneTouchExpandable";
    public static final String LAST_DIVIDER_LOCATION_PROPERTY = "lastDividerLocation";
    public static final String DIVIDER_LOCATION_PROPERTY = "dividerLocation";
    public static final String RESIZE_WEIGHT_PROPERTY = "resizeWeight";
    protected int orientation;
    protected boolean continuousLayout;
    protected Component leftComponent;
    protected Component rightComponent;
    protected int dividerSize;
    private boolean dividerSizeSet;
    protected boolean oneTouchExpandable;
    private boolean oneTouchExpandableSet;
    protected int lastDividerLocation;
    private double resizeWeight;
    private int dividerLocation;

    public JSplitPane() {
        this(1, UIManager.getBoolean("SplitPane.continuousLayout"), new JButton(UIManager.getString("SplitPane.leftButtonText")), new JButton(UIManager.getString("SplitPane.rightButtonText")));
    }

    @ConstructorProperties({"orientation"})
    public JSplitPane(int i2) {
        this(i2, UIManager.getBoolean("SplitPane.continuousLayout"));
    }

    public JSplitPane(int i2, boolean z2) {
        this(i2, z2, null, null);
    }

    public JSplitPane(int i2, Component component, Component component2) {
        this(i2, UIManager.getBoolean("SplitPane.continuousLayout"), component, component2);
    }

    public JSplitPane(int i2, boolean z2, Component component, Component component2) {
        this.dividerSizeSet = false;
        this.dividerLocation = -1;
        setLayout(null);
        setUIProperty("opaque", Boolean.TRUE);
        this.orientation = i2;
        if (this.orientation != 1 && this.orientation != 0) {
            throw new IllegalArgumentException("cannot create JSplitPane, orientation must be one of JSplitPane.HORIZONTAL_SPLIT or JSplitPane.VERTICAL_SPLIT");
        }
        this.continuousLayout = z2;
        if (component != null) {
            setLeftComponent(component);
        }
        if (component2 != null) {
            setRightComponent(component2);
        }
        updateUI();
    }

    public void setUI(SplitPaneUI splitPaneUI) {
        if (((SplitPaneUI) this.ui) != splitPaneUI) {
            super.setUI((ComponentUI) splitPaneUI);
            revalidate();
        }
    }

    public SplitPaneUI getUI() {
        return (SplitPaneUI) this.ui;
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((SplitPaneUI) UIManager.getUI(this));
        revalidate();
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    public void setDividerSize(int i2) {
        int i3 = this.dividerSize;
        this.dividerSizeSet = true;
        if (i3 != i2) {
            this.dividerSize = i2;
            firePropertyChange(DIVIDER_SIZE_PROPERTY, i3, i2);
        }
    }

    public int getDividerSize() {
        return this.dividerSize;
    }

    public void setLeftComponent(Component component) {
        if (component == null) {
            if (this.leftComponent != null) {
                remove(this.leftComponent);
                this.leftComponent = null;
                return;
            }
            return;
        }
        add(component, LEFT);
    }

    public Component getLeftComponent() {
        return this.leftComponent;
    }

    public void setTopComponent(Component component) {
        setLeftComponent(component);
    }

    public Component getTopComponent() {
        return this.leftComponent;
    }

    public void setRightComponent(Component component) {
        if (component == null) {
            if (this.rightComponent != null) {
                remove(this.rightComponent);
                this.rightComponent = null;
                return;
            }
            return;
        }
        add(component, RIGHT);
    }

    public Component getRightComponent() {
        return this.rightComponent;
    }

    public void setBottomComponent(Component component) {
        setRightComponent(component);
    }

    public Component getBottomComponent() {
        return this.rightComponent;
    }

    public void setOneTouchExpandable(boolean z2) {
        boolean z3 = this.oneTouchExpandable;
        this.oneTouchExpandable = z2;
        this.oneTouchExpandableSet = true;
        firePropertyChange(ONE_TOUCH_EXPANDABLE_PROPERTY, z3, z2);
        repaint();
    }

    public boolean isOneTouchExpandable() {
        return this.oneTouchExpandable;
    }

    public void setLastDividerLocation(int i2) {
        int i3 = this.lastDividerLocation;
        this.lastDividerLocation = i2;
        firePropertyChange(LAST_DIVIDER_LOCATION_PROPERTY, i3, i2);
    }

    public int getLastDividerLocation() {
        return this.lastDividerLocation;
    }

    public void setOrientation(int i2) {
        if (i2 != 0 && i2 != 1) {
            throw new IllegalArgumentException("JSplitPane: orientation must be one of JSplitPane.VERTICAL_SPLIT or JSplitPane.HORIZONTAL_SPLIT");
        }
        int i3 = this.orientation;
        this.orientation = i2;
        firePropertyChange("orientation", i3, i2);
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setContinuousLayout(boolean z2) {
        boolean z3 = this.continuousLayout;
        this.continuousLayout = z2;
        firePropertyChange(CONTINUOUS_LAYOUT_PROPERTY, z3, z2);
    }

    public boolean isContinuousLayout() {
        return this.continuousLayout;
    }

    public void setResizeWeight(double d2) {
        if (d2 < 0.0d || d2 > 1.0d) {
            throw new IllegalArgumentException("JSplitPane weight must be between 0 and 1");
        }
        double d3 = this.resizeWeight;
        this.resizeWeight = d2;
        firePropertyChange(RESIZE_WEIGHT_PROPERTY, d3, d2);
    }

    public double getResizeWeight() {
        return this.resizeWeight;
    }

    public void resetToPreferredSizes() {
        SplitPaneUI ui = getUI();
        if (ui != null) {
            ui.resetToPreferredSizes(this);
        }
    }

    public void setDividerLocation(double d2) {
        if (d2 < 0.0d || d2 > 1.0d) {
            throw new IllegalArgumentException("proportional location must be between 0.0 and 1.0.");
        }
        if (getOrientation() == 0) {
            setDividerLocation((int) ((getHeight() - getDividerSize()) * d2));
        } else {
            setDividerLocation((int) ((getWidth() - getDividerSize()) * d2));
        }
    }

    public void setDividerLocation(int i2) {
        int i3 = this.dividerLocation;
        this.dividerLocation = i2;
        SplitPaneUI ui = getUI();
        if (ui != null) {
            ui.setDividerLocation(this, i2);
        }
        firePropertyChange(DIVIDER_LOCATION_PROPERTY, i3, i2);
        setLastDividerLocation(i3);
    }

    public int getDividerLocation() {
        return this.dividerLocation;
    }

    public int getMinimumDividerLocation() {
        SplitPaneUI ui = getUI();
        if (ui != null) {
            return ui.getMinimumDividerLocation(this);
        }
        return -1;
    }

    public int getMaximumDividerLocation() {
        SplitPaneUI ui = getUI();
        if (ui != null) {
            return ui.getMaximumDividerLocation(this);
        }
        return -1;
    }

    @Override // java.awt.Container
    public void remove(Component component) {
        if (component == this.leftComponent) {
            this.leftComponent = null;
        } else if (component == this.rightComponent) {
            this.rightComponent = null;
        }
        super.remove(component);
        revalidate();
        repaint();
    }

    @Override // java.awt.Container
    public void remove(int i2) {
        Component component = getComponent(i2);
        if (component == this.leftComponent) {
            this.leftComponent = null;
        } else if (component == this.rightComponent) {
            this.rightComponent = null;
        }
        super.remove(i2);
        revalidate();
        repaint();
    }

    @Override // java.awt.Container
    public void removeAll() {
        this.rightComponent = null;
        this.leftComponent = null;
        super.removeAll();
        revalidate();
        repaint();
    }

    @Override // javax.swing.JComponent, java.awt.Container
    public boolean isValidateRoot() {
        return true;
    }

    @Override // java.awt.Container
    protected void addImpl(Component component, Object obj, int i2) {
        if (obj != null && !(obj instanceof String)) {
            throw new IllegalArgumentException("cannot add to layout: constraint must be a string (or null)");
        }
        if (obj == null) {
            if (getLeftComponent() == null) {
                obj = LEFT;
            } else if (getRightComponent() == null) {
                obj = RIGHT;
            }
        }
        if (obj != null && (obj.equals(LEFT) || obj.equals(TOP))) {
            Component leftComponent = getLeftComponent();
            if (leftComponent != null) {
                remove(leftComponent);
            }
            this.leftComponent = component;
            i2 = -1;
        } else if (obj != null && (obj.equals(RIGHT) || obj.equals(BOTTOM))) {
            Component rightComponent = getRightComponent();
            if (rightComponent != null) {
                remove(rightComponent);
            }
            this.rightComponent = component;
            i2 = -1;
        } else if (obj != null && obj.equals(DIVIDER)) {
            i2 = -1;
        }
        super.addImpl(component, obj, i2);
        revalidate();
        repaint();
    }

    @Override // javax.swing.JComponent
    protected void paintChildren(Graphics graphics) {
        super.paintChildren(graphics);
        SplitPaneUI ui = getUI();
        if (ui != null) {
            Graphics graphicsCreate = graphics.create();
            ui.finishedPaintingChildren(this, graphicsCreate);
            graphicsCreate.dispose();
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

    @Override // javax.swing.JComponent
    void setUIProperty(String str, Object obj) {
        if (str == DIVIDER_SIZE_PROPERTY) {
            if (!this.dividerSizeSet) {
                setDividerSize(((Number) obj).intValue());
                this.dividerSizeSet = false;
                return;
            }
            return;
        }
        if (str == ONE_TOUCH_EXPANDABLE_PROPERTY) {
            if (!this.oneTouchExpandableSet) {
                setOneTouchExpandable(((Boolean) obj).booleanValue());
                this.oneTouchExpandableSet = false;
                return;
            }
            return;
        }
        super.setUIProperty(str, obj);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    protected String paramString() {
        return super.paramString() + ",continuousLayout=" + (this.continuousLayout ? "true" : "false") + ",dividerSize=" + this.dividerSize + ",lastDividerLocation=" + this.lastDividerLocation + ",oneTouchExpandable=" + (this.oneTouchExpandable ? "true" : "false") + ",orientation=" + (this.orientation == 1 ? "HORIZONTAL_SPLIT" : "VERTICAL_SPLIT");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJSplitPane();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JSplitPane$AccessibleJSplitPane.class */
    protected class AccessibleJSplitPane extends JComponent.AccessibleJComponent implements AccessibleValue {
        protected AccessibleJSplitPane() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JSplitPane.this.getOrientation() == 0) {
                accessibleStateSet.add(AccessibleState.VERTICAL);
            } else {
                accessibleStateSet.add(AccessibleState.HORIZONTAL);
            }
            return accessibleStateSet;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(JSplitPane.this.getDividerLocation());
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number == null) {
                return false;
            }
            JSplitPane.this.setDividerLocation(number.intValue());
            return true;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(JSplitPane.this.getUI().getMinimumDividerLocation(JSplitPane.this));
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return Integer.valueOf(JSplitPane.this.getUI().getMaximumDividerLocation(JSplitPane.this));
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SPLIT_PANE;
        }
    }
}
