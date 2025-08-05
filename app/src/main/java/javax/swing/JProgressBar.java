package javax.swing;

import java.awt.Graphics;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.Format;
import java.text.NumberFormat;
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
import javax.swing.plaf.ProgressBarUI;

/* loaded from: rt.jar:javax/swing/JProgressBar.class */
public class JProgressBar extends JComponent implements SwingConstants, Accessible {
    private static final String uiClassID = "ProgressBarUI";
    protected int orientation;
    protected boolean paintBorder;
    protected BoundedRangeModel model;
    protected String progressString;
    protected boolean paintString;
    private static final int defaultMinimum = 0;
    private static final int defaultMaximum = 100;
    private static final int defaultOrientation = 0;
    protected transient ChangeEvent changeEvent;
    protected ChangeListener changeListener;
    private transient Format format;
    private boolean indeterminate;

    public JProgressBar() {
        this(0);
    }

    public JProgressBar(int i2) {
        this(i2, 0, 100);
    }

    public JProgressBar(int i2, int i3) {
        this(0, i2, i3);
    }

    public JProgressBar(int i2, int i3, int i4) {
        this.changeEvent = null;
        this.changeListener = null;
        setModel(new DefaultBoundedRangeModel(i3, 0, i3, i4));
        updateUI();
        setOrientation(i2);
        setBorderPainted(true);
        setStringPainted(false);
        setString(null);
        setIndeterminate(false);
    }

    public JProgressBar(BoundedRangeModel boundedRangeModel) {
        this.changeEvent = null;
        this.changeListener = null;
        setModel(boundedRangeModel);
        updateUI();
        setOrientation(0);
        setBorderPainted(true);
        setStringPainted(false);
        setString(null);
        setIndeterminate(false);
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int i2) {
        if (this.orientation != i2) {
            switch (i2) {
                case 0:
                case 1:
                    int i3 = this.orientation;
                    this.orientation = i2;
                    firePropertyChange("orientation", i3, i2);
                    if (this.accessibleContext != null) {
                        this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_STATE_PROPERTY, i3 == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL, this.orientation == 1 ? AccessibleState.VERTICAL : AccessibleState.HORIZONTAL);
                    }
                    revalidate();
                    return;
                default:
                    throw new IllegalArgumentException(i2 + " is not a legal orientation");
            }
        }
    }

    public boolean isStringPainted() {
        return this.paintString;
    }

    public void setStringPainted(boolean z2) {
        boolean z3 = this.paintString;
        this.paintString = z2;
        firePropertyChange("stringPainted", z3, this.paintString);
        if (this.paintString != z3) {
            revalidate();
            repaint();
        }
    }

    public String getString() {
        if (this.progressString != null) {
            return this.progressString;
        }
        if (this.format == null) {
            this.format = NumberFormat.getPercentInstance();
        }
        return this.format.format(new Double(getPercentComplete()));
    }

    public void setString(String str) {
        String str2 = this.progressString;
        this.progressString = str;
        firePropertyChange("string", str2, this.progressString);
        if (this.progressString == null || str2 == null || !this.progressString.equals(str2)) {
            repaint();
        }
    }

    public double getPercentComplete() {
        return (this.model.getValue() - this.model.getMinimum()) / (this.model.getMaximum() - this.model.getMinimum());
    }

    public boolean isBorderPainted() {
        return this.paintBorder;
    }

    public void setBorderPainted(boolean z2) {
        boolean z3 = this.paintBorder;
        this.paintBorder = z2;
        firePropertyChange(AbstractButton.BORDER_PAINTED_CHANGED_PROPERTY, z3, this.paintBorder);
        if (this.paintBorder != z3) {
            repaint();
        }
    }

    @Override // javax.swing.JComponent
    protected void paintBorder(Graphics graphics) {
        if (isBorderPainted()) {
            super.paintBorder(graphics);
        }
    }

    public ProgressBarUI getUI() {
        return (ProgressBarUI) this.ui;
    }

    public void setUI(ProgressBarUI progressBarUI) {
        super.setUI((ComponentUI) progressBarUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((ProgressBarUI) UIManager.getUI(this));
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    /* loaded from: rt.jar:javax/swing/JProgressBar$ModelListener.class */
    private class ModelListener implements ChangeListener, Serializable {
        private ModelListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JProgressBar.this.fireStateChanged();
        }
    }

    protected ChangeListener createChangeListener() {
        return new ModelListener();
    }

    public void addChangeListener(ChangeListener changeListener) {
        this.listenerList.add(ChangeListener.class, changeListener);
    }

    public void removeChangeListener(ChangeListener changeListener) {
        this.listenerList.remove(ChangeListener.class, changeListener);
    }

    public ChangeListener[] getChangeListeners() {
        return (ChangeListener[]) this.listenerList.getListeners(ChangeListener.class);
    }

    protected void fireStateChanged() {
        Object[] listenerList = this.listenerList.getListenerList();
        for (int length = listenerList.length - 2; length >= 0; length -= 2) {
            if (listenerList[length] == ChangeListener.class) {
                if (this.changeEvent == null) {
                    this.changeEvent = new ChangeEvent(this);
                }
                ((ChangeListener) listenerList[length + 1]).stateChanged(this.changeEvent);
            }
        }
    }

    public BoundedRangeModel getModel() {
        return this.model;
    }

    public void setModel(BoundedRangeModel boundedRangeModel) {
        BoundedRangeModel model = getModel();
        if (boundedRangeModel != model) {
            if (model != null) {
                model.removeChangeListener(this.changeListener);
                this.changeListener = null;
            }
            this.model = boundedRangeModel;
            if (boundedRangeModel != null) {
                this.changeListener = createChangeListener();
                boundedRangeModel.addChangeListener(this.changeListener);
            }
            if (this.accessibleContext != null) {
                this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, model == null ? null : Integer.valueOf(model.getValue()), boundedRangeModel == null ? null : Integer.valueOf(boundedRangeModel.getValue()));
            }
            if (this.model != null) {
                this.model.setExtent(0);
            }
            repaint();
        }
    }

    public int getValue() {
        return getModel().getValue();
    }

    public int getMinimum() {
        return getModel().getMinimum();
    }

    public int getMaximum() {
        return getModel().getMaximum();
    }

    public void setValue(int i2) {
        BoundedRangeModel model = getModel();
        int value = model.getValue();
        model.setValue(i2);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, Integer.valueOf(value), Integer.valueOf(model.getValue()));
        }
    }

    public void setMinimum(int i2) {
        getModel().setMinimum(i2);
    }

    public void setMaximum(int i2) {
        getModel().setMaximum(i2);
    }

    public void setIndeterminate(boolean z2) {
        boolean z3 = this.indeterminate;
        this.indeterminate = z2;
        firePropertyChange("indeterminate", z3, this.indeterminate);
    }

    public boolean isIndeterminate() {
        return this.indeterminate;
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
        return super.paramString() + ",orientation=" + (this.orientation == 0 ? "HORIZONTAL" : "VERTICAL") + ",paintBorder=" + (this.paintBorder ? "true" : "false") + ",paintString=" + (this.paintString ? "true" : "false") + ",progressString=" + (this.progressString != null ? this.progressString : "") + ",indeterminateString=" + (this.indeterminate ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJProgressBar();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JProgressBar$AccessibleJProgressBar.class */
    protected class AccessibleJProgressBar extends JComponent.AccessibleJComponent implements AccessibleValue {
        protected AccessibleJProgressBar() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JProgressBar.this.getModel().getValueIsAdjusting()) {
                accessibleStateSet.add(AccessibleState.BUSY);
            }
            if (JProgressBar.this.getOrientation() == 1) {
                accessibleStateSet.add(AccessibleState.VERTICAL);
            } else {
                accessibleStateSet.add(AccessibleState.HORIZONTAL);
            }
            return accessibleStateSet;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.PROGRESS_BAR;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(JProgressBar.this.getValue());
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number == null) {
                return false;
            }
            JProgressBar.this.setValue(number.intValue());
            return true;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(JProgressBar.this.getMinimum());
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            return Integer.valueOf(JProgressBar.this.model.getMaximum() - JProgressBar.this.model.getExtent());
        }
    }
}
