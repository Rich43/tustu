package javax.swing;

import java.awt.Adjustable;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleState;
import javax.accessibility.AccessibleStateSet;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.ScrollBarUI;

/* loaded from: rt.jar:javax/swing/JScrollBar.class */
public class JScrollBar extends JComponent implements Adjustable, Accessible {
    private static final String uiClassID = "ScrollBarUI";
    private ChangeListener fwdAdjustmentEvents;
    protected BoundedRangeModel model;
    protected int orientation;
    protected int unitIncrement;
    protected int blockIncrement;

    private void checkOrientation(int i2) {
        switch (i2) {
            case 0:
            case 1:
                return;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    public JScrollBar(int i2, int i3, int i4, int i5, int i6) {
        this.fwdAdjustmentEvents = new ModelListener();
        checkOrientation(i2);
        this.unitIncrement = 1;
        this.blockIncrement = i4 == 0 ? 1 : i4;
        this.orientation = i2;
        this.model = new DefaultBoundedRangeModel(i3, i4, i5, i6);
        this.model.addChangeListener(this.fwdAdjustmentEvents);
        setRequestFocusEnabled(false);
        updateUI();
    }

    public JScrollBar(int i2) {
        this(i2, 0, 10, 0, 100);
    }

    public JScrollBar() {
        this(1);
    }

    public void setUI(ScrollBarUI scrollBarUI) {
        super.setUI((ComponentUI) scrollBarUI);
    }

    public ScrollBarUI getUI() {
        return (ScrollBarUI) this.ui;
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ScrollBarUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // java.awt.Adjustable
    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i2) {
        checkOrientation(i2);
        int i3 = this.orientation;
        this.orientation = i2;
        firePropertyChange("orientation", i3, i2);
        if (i3 != i2 && this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, i3 == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL, i2 == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL);
        }
        if (i2 != i3) {
            revalidate();
        }
    }

    public BoundedRangeModel getModel() {
        return this.model;
    }

    public void setModel(BoundedRangeModel boundedRangeModel) {
        Integer numValueOf = null;
        BoundedRangeModel boundedRangeModel2 = this.model;
        if (this.model != null) {
            this.model.removeChangeListener(this.fwdAdjustmentEvents);
            numValueOf = Integer.valueOf(this.model.getValue());
        }
        this.model = boundedRangeModel;
        if (this.model != null) {
            this.model.addChangeListener(this.fwdAdjustmentEvents);
        }
        firePropertyChange("model", boundedRangeModel2, this.model);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, numValueOf, new Integer(this.model.getValue()));
        }
    }

    public int getUnitIncrement(int i2) {
        return this.unitIncrement;
    }

    @Override // java.awt.Adjustable
    public void setUnitIncrement(int i2) {
        int i3 = this.unitIncrement;
        this.unitIncrement = i2;
        firePropertyChange("unitIncrement", i3, i2);
    }

    public int getBlockIncrement(int i2) {
        return this.blockIncrement;
    }

    @Override // java.awt.Adjustable
    public void setBlockIncrement(int i2) {
        int i3 = this.blockIncrement;
        this.blockIncrement = i2;
        firePropertyChange("blockIncrement", i3, i2);
    }

    @Override // java.awt.Adjustable
    public int getUnitIncrement() {
        return this.unitIncrement;
    }

    @Override // java.awt.Adjustable
    public int getBlockIncrement() {
        return this.blockIncrement;
    }

    @Override // java.awt.Adjustable
    public int getValue() {
        return getModel().getValue();
    }

    @Override // java.awt.Adjustable
    public void setValue(int i2) {
        BoundedRangeModel model = getModel();
        int value = model.getValue();
        model.setValue(i2);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, Integer.valueOf(value), Integer.valueOf(model.getValue()));
        }
    }

    @Override // java.awt.Adjustable
    public int getVisibleAmount() {
        return getModel().getExtent();
    }

    @Override // java.awt.Adjustable
    public void setVisibleAmount(int i2) {
        getModel().setExtent(i2);
    }

    @Override // java.awt.Adjustable
    public int getMinimum() {
        return getModel().getMinimum();
    }

    @Override // java.awt.Adjustable
    public void setMinimum(int i2) {
        getModel().setMinimum(i2);
    }

    @Override // java.awt.Adjustable
    public int getMaximum() {
        return getModel().getMaximum();
    }

    @Override // java.awt.Adjustable
    public void setMaximum(int i2) {
        getModel().setMaximum(i2);
    }

    public boolean getValueIsAdjusting() {
        return getModel().getValueIsAdjusting();
    }

    public void setValueIsAdjusting(boolean z2) {
        BoundedRangeModel model = getModel();
        boolean valueIsAdjusting = model.getValueIsAdjusting();
        model.setValueIsAdjusting(z2);
        if (valueIsAdjusting != z2 && this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, valueIsAdjusting ? AccessibleState.BUSY : null, z2 ? AccessibleState.BUSY : null);
        }
    }

    public void setValues(int i2, int i3, int i4, int i5) {
        BoundedRangeModel model = getModel();
        int value = model.getValue();
        model.setRangeProperties(i2, i3, i4, i5, model.getValueIsAdjusting());
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, Integer.valueOf(value), Integer.valueOf(model.getValue()));
        }
    }

    @Override // java.awt.Adjustable
    public void addAdjustmentListener(AdjustmentListener adjustmentListener) {
        this.listenerList.add(AdjustmentListener.class, adjustmentListener);
    }

    @Override // java.awt.Adjustable
    public void removeAdjustmentListener(AdjustmentListener adjustmentListener) {
        this.listenerList.remove(AdjustmentListener.class, adjustmentListener);
    }

    public AdjustmentListener[] getAdjustmentListeners() {
        return (AdjustmentListener[]) this.listenerList.getListeners(AdjustmentListener.class);
    }

    protected void fireAdjustmentValueChanged(int i2, int i3, int i4) {
        fireAdjustmentValueChanged(i2, i3, i4, getValueIsAdjusting());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void fireAdjustmentValueChanged(int i2, int i3, int i4, boolean z2) {
        Object[] listenerList = this.listenerList.getListenerList();
        AdjustmentEvent adjustmentEvent = null;
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == AdjustmentListener.class) {
                if (adjustmentEvent == null) {
                    adjustmentEvent = new AdjustmentEvent(this, i2, i3, i4, z2);
                }
                ((AdjustmentListener) listenerList[length + 1]).adjustmentValueChanged(adjustmentEvent);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JScrollBar$ModelListener.class */
    private class ModelListener implements ChangeListener, Serializable {
        private ModelListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            Object source = changeEvent.getSource();
            if (source instanceof BoundedRangeModel) {
                BoundedRangeModel boundedRangeModel = (BoundedRangeModel) source;
                JScrollBar.this.fireAdjustmentValueChanged(601, 5, boundedRangeModel.getValue(), boundedRangeModel.getValueIsAdjusting());
            }
        }
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMinimumSize() {
        Dimension preferredSize = getPreferredSize();
        if (this.orientation == 1) {
            return new Dimension(preferredSize.width, 5);
        }
        return new Dimension(5, preferredSize.height);
    }

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public Dimension getMaximumSize() {
        Dimension preferredSize = getPreferredSize();
        if (getOrientation() == 1) {
            return new Dimension(preferredSize.width, Short.MAX_VALUE);
        }
        return new Dimension(Short.MAX_VALUE, preferredSize.height);
    }

    @Override // javax.swing.JComponent, java.awt.Component, c.InterfaceC1385d
    public void setEnabled(boolean z2) {
        super.setEnabled(z2);
        for (Component component : getComponents()) {
            component.setEnabled(z2);
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
        return super.paramString() + ",blockIncrement=" + this.blockIncrement + ",orientation=" + (this.orientation == 0 ? "HORIZONTAL" : "VERTICAL") + ",unitIncrement=" + this.unitIncrement;
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJScrollBar();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JScrollBar$AccessibleJScrollBar.class */
    protected class AccessibleJScrollBar extends JComponent.AccessibleJComponent implements AccessibleValue {
        protected AccessibleJScrollBar() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JScrollBar.this.getValueIsAdjusting()) {
                accessibleStateSet.add(AccessibleState.BUSY);
            }
            if (JScrollBar.this.getOrientation() == 1) {
                accessibleStateSet.add(AccessibleState.VERTICAL);
            } else {
                accessibleStateSet.add(AccessibleState.HORIZONTAL);
            }
            return accessibleStateSet;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SCROLL_BAR;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(JScrollBar.this.getValue());
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number == null) {
                return false;
            }
            JScrollBar.this.setValue(number.intValue());
            return true;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(JScrollBar.this.getMinimum());
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return new Integer(JScrollBar.this.model.getMaximum() - JScrollBar.this.model.getExtent());
        }
    }
}
