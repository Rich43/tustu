package javax.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
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
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.UIResource;

/* loaded from: rt.jar:javax/swing/JSlider.class */
public class JSlider extends JComponent implements SwingConstants, Accessible {
    private static final String uiClassID = "SliderUI";
    private boolean paintTicks;
    private boolean paintTrack;
    private boolean paintLabels;
    private boolean isInverted;
    protected BoundedRangeModel sliderModel;
    protected int majorTickSpacing;
    protected int minorTickSpacing;
    protected boolean snapToTicks;
    boolean snapToValue;
    protected int orientation;
    private Dictionary labelTable;
    protected ChangeListener changeListener;
    protected transient ChangeEvent changeEvent;

    private void checkOrientation(int i2) {
        switch (i2) {
            case 0:
            case 1:
                return;
            default:
                throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
        }
    }

    public JSlider() {
        this(0, 0, 100, 50);
    }

    public JSlider(int i2) {
        this(i2, 0, 100, 50);
    }

    public JSlider(int i2, int i3) {
        this(0, i2, i3, (i2 + i3) / 2);
    }

    public JSlider(int i2, int i3, int i4) {
        this(0, i2, i3, i4);
    }

    public JSlider(int i2, int i3, int i4, int i5) {
        this.paintTicks = false;
        this.paintTrack = true;
        this.paintLabels = false;
        this.isInverted = false;
        this.snapToTicks = false;
        this.snapToValue = true;
        this.changeListener = createChangeListener();
        this.changeEvent = null;
        checkOrientation(i2);
        this.orientation = i2;
        setModel(new DefaultBoundedRangeModel(i5, 0, i3, i4));
        updateUI();
    }

    public JSlider(BoundedRangeModel boundedRangeModel) {
        this.paintTicks = false;
        this.paintTrack = true;
        this.paintLabels = false;
        this.isInverted = false;
        this.snapToTicks = false;
        this.snapToValue = true;
        this.changeListener = createChangeListener();
        this.changeEvent = null;
        this.orientation = 0;
        setModel(boundedRangeModel);
        updateUI();
    }

    public SliderUI getUI() {
        return (SliderUI) this.ui;
    }

    public void setUI(SliderUI sliderUI) {
        super.setUI((ComponentUI) sliderUI);
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((SliderUI) UIManager.getUI(this));
        updateLabelUIs();
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    /* loaded from: rt.jar:javax/swing/JSlider$ModelListener.class */
    private class ModelListener implements ChangeListener, Serializable {
        private ModelListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JSlider.this.fireStateChanged();
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
        return this.sliderModel;
    }

    public void setModel(BoundedRangeModel boundedRangeModel) {
        BoundedRangeModel model = getModel();
        if (model != null) {
            model.removeChangeListener(this.changeListener);
        }
        this.sliderModel = boundedRangeModel;
        if (boundedRangeModel != null) {
            boundedRangeModel.addChangeListener(this.changeListener);
        }
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, model == null ? null : Integer.valueOf(model.getValue()), boundedRangeModel == null ? null : Integer.valueOf(boundedRangeModel.getValue()));
        }
        firePropertyChange("model", model, this.sliderModel);
    }

    public int getValue() {
        return getModel().getValue();
    }

    public void setValue(int i2) {
        BoundedRangeModel model = getModel();
        int value = model.getValue();
        if (value == i2) {
            return;
        }
        model.setValue(i2);
        if (this.accessibleContext != null) {
            this.accessibleContext.firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, Integer.valueOf(value), Integer.valueOf(model.getValue()));
        }
    }

    public int getMinimum() {
        return getModel().getMinimum();
    }

    public void setMinimum(int i2) {
        int minimum = getModel().getMinimum();
        getModel().setMinimum(i2);
        firePropertyChange("minimum", Integer.valueOf(minimum), Integer.valueOf(i2));
    }

    public int getMaximum() {
        return getModel().getMaximum();
    }

    public void setMaximum(int i2) {
        int maximum = getModel().getMaximum();
        getModel().setMaximum(i2);
        firePropertyChange(JInternalFrame.IS_MAXIMUM_PROPERTY, Integer.valueOf(maximum), Integer.valueOf(i2));
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

    public int getExtent() {
        return getModel().getExtent();
    }

    public void setExtent(int i2) {
        getModel().setExtent(i2);
    }

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

    @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
    public void setFont(Font font) {
        super.setFont(font);
        updateLabelSizes();
    }

    @Override // java.awt.Component, java.awt.image.ImageObserver
    public boolean imageUpdate(Image image, int i2, int i3, int i4, int i5, int i6) {
        if (!isShowing()) {
            return false;
        }
        Enumeration enumerationElements = this.labelTable.elements();
        while (enumerationElements.hasMoreElements()) {
            Component component = (Component) enumerationElements.nextElement2();
            if (component instanceof JLabel) {
                JLabel jLabel = (JLabel) component;
                if (SwingUtilities.doesIconReferenceImage(jLabel.getIcon(), image) || SwingUtilities.doesIconReferenceImage(jLabel.getDisabledIcon(), image)) {
                    return super.imageUpdate(image, i2, i3, i4, i5, i6);
                }
            }
        }
        return false;
    }

    public Dictionary getLabelTable() {
        return this.labelTable;
    }

    public void setLabelTable(Dictionary dictionary) {
        Dictionary dictionary2 = this.labelTable;
        this.labelTable = dictionary;
        updateLabelUIs();
        firePropertyChange("labelTable", dictionary2, this.labelTable);
        if (dictionary != dictionary2) {
            revalidate();
            repaint();
        }
    }

    protected void updateLabelUIs() {
        Dictionary labelTable = getLabelTable();
        if (labelTable == null) {
            return;
        }
        Enumeration enumerationKeys = labelTable.keys();
        while (enumerationKeys.hasMoreElements()) {
            JComponent jComponent = (JComponent) labelTable.get(enumerationKeys.nextElement2());
            jComponent.updateUI();
            jComponent.setSize(jComponent.getPreferredSize());
        }
    }

    private void updateLabelSizes() {
        Dictionary labelTable = getLabelTable();
        if (labelTable != null) {
            Enumeration enumerationElements = labelTable.elements();
            while (enumerationElements.hasMoreElements()) {
                JComponent jComponent = (JComponent) enumerationElements.nextElement2();
                jComponent.setSize(jComponent.getPreferredSize());
            }
        }
    }

    public Hashtable createStandardLabels(int i2) {
        return createStandardLabels(i2, getMinimum());
    }

    public Hashtable createStandardLabels(int i2, int i3) {
        if (i3 > getMaximum() || i3 < getMinimum()) {
            throw new IllegalArgumentException("Slider label start point out of range.");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("Label incremement must be > 0");
        }
        C1SmartHashtable c1SmartHashtable = new C1SmartHashtable(i2, i3);
        Object labelTable = getLabelTable();
        if (labelTable != null && (labelTable instanceof PropertyChangeListener)) {
            removePropertyChangeListener((PropertyChangeListener) labelTable);
        }
        addPropertyChangeListener(c1SmartHashtable);
        return c1SmartHashtable;
    }

    /* renamed from: javax.swing.JSlider$1SmartHashtable, reason: invalid class name */
    /* loaded from: rt.jar:javax/swing/JSlider$1SmartHashtable.class */
    class C1SmartHashtable extends Hashtable<Object, Object> implements PropertyChangeListener {
        int increment;
        int start;
        boolean startAtMin;

        /* renamed from: javax.swing.JSlider$1SmartHashtable$LabelUIResource */
        /* loaded from: rt.jar:javax/swing/JSlider$1SmartHashtable$LabelUIResource.class */
        class LabelUIResource extends JLabel implements UIResource {
            public LabelUIResource(String str, int i2) {
                super(str, i2);
                setName("Slider.label");
            }

            @Override // java.awt.Component, java.awt.MenuContainer
            public Font getFont() {
                Font font = super.getFont();
                if (font != null && !(font instanceof UIResource)) {
                    return font;
                }
                return JSlider.this.getFont();
            }

            @Override // java.awt.Component
            public Color getForeground() {
                Color foreground = super.getForeground();
                if (foreground != null && !(foreground instanceof UIResource)) {
                    return foreground;
                }
                if (!(JSlider.this.getForeground() instanceof UIResource)) {
                    return JSlider.this.getForeground();
                }
                return foreground;
            }
        }

        public C1SmartHashtable(int i2, int i3) {
            this.increment = 0;
            this.start = 0;
            this.startAtMin = false;
            this.increment = i2;
            this.start = i3;
            this.startAtMin = i3 == JSlider.this.getMinimum();
            createLabels();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if (propertyChangeEvent.getPropertyName().equals("minimum") && this.startAtMin) {
                this.start = JSlider.this.getMinimum();
            }
            if (propertyChangeEvent.getPropertyName().equals("minimum") || propertyChangeEvent.getPropertyName().equals(JInternalFrame.IS_MAXIMUM_PROPERTY)) {
                Enumeration enumerationKeys = JSlider.this.getLabelTable().keys();
                Hashtable hashtable = new Hashtable();
                while (enumerationKeys.hasMoreElements()) {
                    Object objNextElement = enumerationKeys.nextElement2();
                    Object obj = JSlider.this.labelTable.get(objNextElement);
                    if (!(obj instanceof LabelUIResource)) {
                        hashtable.put(objNextElement, obj);
                    }
                }
                clear();
                createLabels();
                Enumeration enumerationKeys2 = hashtable.keys();
                while (enumerationKeys2.hasMoreElements()) {
                    Object objNextElement2 = enumerationKeys2.nextElement2();
                    put(objNextElement2, hashtable.get(objNextElement2));
                }
                ((JSlider) propertyChangeEvent.getSource()).setLabelTable(this);
            }
        }

        void createLabels() {
            int i2 = this.start;
            while (true) {
                int i3 = i2;
                if (i3 <= JSlider.this.getMaximum()) {
                    put(Integer.valueOf(i3), new LabelUIResource("" + i3, 0));
                    i2 = i3 + this.increment;
                } else {
                    return;
                }
            }
        }
    }

    public boolean getInverted() {
        return this.isInverted;
    }

    public void setInverted(boolean z2) {
        boolean z3 = this.isInverted;
        this.isInverted = z2;
        firePropertyChange("inverted", z3, this.isInverted);
        if (z2 != z3) {
            repaint();
        }
    }

    public int getMajorTickSpacing() {
        return this.majorTickSpacing;
    }

    public void setMajorTickSpacing(int i2) {
        int i3 = this.majorTickSpacing;
        this.majorTickSpacing = i2;
        if (this.labelTable == null && getMajorTickSpacing() > 0 && getPaintLabels()) {
            setLabelTable(createStandardLabels(getMajorTickSpacing()));
        }
        firePropertyChange("majorTickSpacing", i3, this.majorTickSpacing);
        if (this.majorTickSpacing != i3 && getPaintTicks()) {
            repaint();
        }
    }

    public int getMinorTickSpacing() {
        return this.minorTickSpacing;
    }

    public void setMinorTickSpacing(int i2) {
        int i3 = this.minorTickSpacing;
        this.minorTickSpacing = i2;
        firePropertyChange("minorTickSpacing", i3, this.minorTickSpacing);
        if (this.minorTickSpacing != i3 && getPaintTicks()) {
            repaint();
        }
    }

    public boolean getSnapToTicks() {
        return this.snapToTicks;
    }

    boolean getSnapToValue() {
        return this.snapToValue;
    }

    public void setSnapToTicks(boolean z2) {
        boolean z3 = this.snapToTicks;
        this.snapToTicks = z2;
        firePropertyChange("snapToTicks", z3, this.snapToTicks);
    }

    void setSnapToValue(boolean z2) {
        boolean z3 = this.snapToValue;
        this.snapToValue = z2;
        firePropertyChange("snapToValue", z3, this.snapToValue);
    }

    public boolean getPaintTicks() {
        return this.paintTicks;
    }

    public void setPaintTicks(boolean z2) {
        boolean z3 = this.paintTicks;
        this.paintTicks = z2;
        firePropertyChange("paintTicks", z3, this.paintTicks);
        if (this.paintTicks != z3) {
            revalidate();
            repaint();
        }
    }

    public boolean getPaintTrack() {
        return this.paintTrack;
    }

    public void setPaintTrack(boolean z2) {
        boolean z3 = this.paintTrack;
        this.paintTrack = z2;
        firePropertyChange("paintTrack", z3, this.paintTrack);
        if (this.paintTrack != z3) {
            repaint();
        }
    }

    public boolean getPaintLabels() {
        return this.paintLabels;
    }

    public void setPaintLabels(boolean z2) {
        boolean z3 = this.paintLabels;
        this.paintLabels = z2;
        if (this.labelTable == null && getMajorTickSpacing() > 0) {
            setLabelTable(createStandardLabels(getMajorTickSpacing()));
        }
        firePropertyChange("paintLabels", z3, this.paintLabels);
        if (this.paintLabels != z3) {
            revalidate();
            repaint();
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
        String str = this.paintTicks ? "true" : "false";
        String str2 = this.paintTrack ? "true" : "false";
        String str3 = this.paintLabels ? "true" : "false";
        return super.paramString() + ",isInverted=" + (this.isInverted ? "true" : "false") + ",majorTickSpacing=" + this.majorTickSpacing + ",minorTickSpacing=" + this.minorTickSpacing + ",orientation=" + (this.orientation == 0 ? "HORIZONTAL" : "VERTICAL") + ",paintLabels=" + str3 + ",paintTicks=" + str + ",paintTrack=" + str2 + ",snapToTicks=" + (this.snapToTicks ? "true" : "false") + ",snapToValue=" + (this.snapToValue ? "true" : "false");
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJSlider();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JSlider$AccessibleJSlider.class */
    protected class AccessibleJSlider extends JComponent.AccessibleJComponent implements AccessibleValue {
        protected AccessibleJSlider() {
            super();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleStateSet getAccessibleStateSet() {
            AccessibleStateSet accessibleStateSet = super.getAccessibleStateSet();
            if (JSlider.this.getValueIsAdjusting()) {
                accessibleStateSet.add(AccessibleState.BUSY);
            }
            if (JSlider.this.getOrientation() == 1) {
                accessibleStateSet.add(AccessibleState.VERTICAL);
            } else {
                accessibleStateSet.add(AccessibleState.HORIZONTAL);
            }
            return accessibleStateSet;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SLIDER;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            return Integer.valueOf(JSlider.this.getValue());
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            if (number == null) {
                return false;
            }
            JSlider.this.setValue(number.intValue());
            return true;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            return Integer.valueOf(JSlider.this.getMinimum());
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            BoundedRangeModel model = JSlider.this.getModel();
            return Integer.valueOf(model.getMaximum() - model.getExtent());
        }
    }
}
