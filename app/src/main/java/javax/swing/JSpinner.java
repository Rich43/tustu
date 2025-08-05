package javax.swing;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.spi.DateFormatProvider;
import java.text.spi.NumberFormatProvider;
import java.util.Locale;
import javax.accessibility.Accessible;
import javax.accessibility.AccessibleAction;
import javax.accessibility.AccessibleContext;
import javax.accessibility.AccessibleEditableText;
import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleText;
import javax.accessibility.AccessibleValue;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SpinnerUI;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.DocumentFilter;
import javax.swing.text.NumberFormatter;
import sun.util.locale.provider.LocaleProviderAdapter;
import sun.util.locale.provider.LocaleResources;

/* loaded from: rt.jar:javax/swing/JSpinner.class */
public class JSpinner extends JComponent implements Accessible {
    private static final String uiClassID = "SpinnerUI";
    private static final Action DISABLED_ACTION = new DisabledAction();
    private SpinnerModel model;
    private JComponent editor;
    private ChangeListener modelListener;
    private transient ChangeEvent changeEvent;
    private boolean editorExplicitlySet;

    public JSpinner(SpinnerModel spinnerModel) {
        this.editorExplicitlySet = false;
        if (spinnerModel == null) {
            throw new NullPointerException("model cannot be null");
        }
        this.model = spinnerModel;
        this.editor = createEditor(spinnerModel);
        setUIProperty("opaque", true);
        updateUI();
    }

    public JSpinner() {
        this(new SpinnerNumberModel());
    }

    public SpinnerUI getUI() {
        return (SpinnerUI) this.ui;
    }

    public void setUI(SpinnerUI spinnerUI) {
        super.setUI((ComponentUI) spinnerUI);
    }

    @Override // javax.swing.JComponent
    public String getUIClassID() {
        return uiClassID;
    }

    @Override // javax.swing.JComponent
    public void updateUI() {
        setUI((SpinnerUI) UIManager.getUI(this));
        invalidate();
    }

    protected JComponent createEditor(SpinnerModel spinnerModel) {
        if (spinnerModel instanceof SpinnerDateModel) {
            return new DateEditor(this);
        }
        if (spinnerModel instanceof SpinnerListModel) {
            return new ListEditor(this);
        }
        if (spinnerModel instanceof SpinnerNumberModel) {
            return new NumberEditor(this);
        }
        return new DefaultEditor(this);
    }

    public void setModel(SpinnerModel spinnerModel) {
        if (spinnerModel == null) {
            throw new IllegalArgumentException("null model");
        }
        if (!spinnerModel.equals(this.model)) {
            SpinnerModel spinnerModel2 = this.model;
            this.model = spinnerModel;
            if (this.modelListener != null) {
                spinnerModel2.removeChangeListener(this.modelListener);
                this.model.addChangeListener(this.modelListener);
            }
            firePropertyChange("model", spinnerModel2, spinnerModel);
            if (!this.editorExplicitlySet) {
                setEditor(createEditor(spinnerModel));
                this.editorExplicitlySet = false;
            }
            repaint();
            revalidate();
        }
    }

    public SpinnerModel getModel() {
        return this.model;
    }

    public Object getValue() {
        return getModel().getValue();
    }

    public void setValue(Object obj) {
        getModel().setValue(obj);
    }

    public Object getNextValue() {
        return getModel().getNextValue();
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$ModelListener.class */
    private class ModelListener implements ChangeListener, Serializable {
        private ModelListener() {
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            JSpinner.this.fireStateChanged();
        }
    }

    public void addChangeListener(ChangeListener changeListener) {
        if (this.modelListener == null) {
            this.modelListener = new ModelListener();
            getModel().addChangeListener(this.modelListener);
        }
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

    public Object getPreviousValue() {
        return getModel().getPreviousValue();
    }

    public void setEditor(JComponent jComponent) {
        if (jComponent == null) {
            throw new IllegalArgumentException("null editor");
        }
        if (!jComponent.equals(this.editor)) {
            JComponent jComponent2 = this.editor;
            this.editor = jComponent;
            if (jComponent2 instanceof DefaultEditor) {
                ((DefaultEditor) jComponent2).dismiss(this);
            }
            this.editorExplicitlySet = true;
            firePropertyChange("editor", jComponent2, jComponent);
            revalidate();
            repaint();
        }
    }

    public JComponent getEditor() {
        return this.editor;
    }

    public void commitEdit() throws ParseException {
        JComponent editor = getEditor();
        if (editor instanceof DefaultEditor) {
            ((DefaultEditor) editor).commitEdit();
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

    /* loaded from: rt.jar:javax/swing/JSpinner$DefaultEditor.class */
    public static class DefaultEditor extends JPanel implements ChangeListener, PropertyChangeListener, LayoutManager {
        public DefaultEditor(JSpinner jSpinner) {
            super((LayoutManager) null);
            JFormattedTextField jFormattedTextField = new JFormattedTextField();
            jFormattedTextField.setName("Spinner.formattedTextField");
            jFormattedTextField.setValue(jSpinner.getValue());
            jFormattedTextField.addPropertyChangeListener(this);
            jFormattedTextField.setEditable(false);
            jFormattedTextField.setInheritsPopupMenu(true);
            String toolTipText = jSpinner.getToolTipText();
            if (toolTipText != null) {
                jFormattedTextField.setToolTipText(toolTipText);
            }
            add(jFormattedTextField);
            setLayout(this);
            jSpinner.addChangeListener(this);
            ActionMap actionMap = jFormattedTextField.getActionMap();
            if (actionMap != null) {
                actionMap.put("increment", JSpinner.DISABLED_ACTION);
                actionMap.put("decrement", JSpinner.DISABLED_ACTION);
            }
        }

        public void dismiss(JSpinner jSpinner) {
            jSpinner.removeChangeListener(this);
        }

        public JSpinner getSpinner() {
            Container parent = this;
            while (true) {
                Container container = parent;
                if (container != null) {
                    if (!(container instanceof JSpinner)) {
                        parent = container.getParent();
                    } else {
                        return (JSpinner) container;
                    }
                } else {
                    return null;
                }
            }
        }

        public JFormattedTextField getTextField() {
            return (JFormattedTextField) getComponent(0);
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            getTextField().setValue(((JSpinner) changeEvent.getSource()).getValue());
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            JSpinner spinner = getSpinner();
            if (spinner == null) {
                return;
            }
            Object source = propertyChangeEvent.getSource();
            String propertyName = propertyChangeEvent.getPropertyName();
            if ((source instanceof JFormattedTextField) && "value".equals(propertyName)) {
                Object value = spinner.getValue();
                try {
                    spinner.setValue(getTextField().getValue());
                } catch (IllegalArgumentException e2) {
                    try {
                        ((JFormattedTextField) source).setValue(value);
                    } catch (IllegalArgumentException e3) {
                    }
                }
            }
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
        }

        private Dimension insetSize(Container container) {
            Insets insets = container.getInsets();
            return new Dimension(insets.left + insets.right, insets.top + insets.bottom);
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            Dimension dimensionInsetSize = insetSize(container);
            if (container.getComponentCount() > 0) {
                Dimension preferredSize = getComponent(0).getPreferredSize();
                dimensionInsetSize.width += preferredSize.width;
                dimensionInsetSize.height += preferredSize.height;
            }
            return dimensionInsetSize;
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            Dimension dimensionInsetSize = insetSize(container);
            if (container.getComponentCount() > 0) {
                Dimension minimumSize = getComponent(0).getMinimumSize();
                dimensionInsetSize.width += minimumSize.width;
                dimensionInsetSize.height += minimumSize.height;
            }
            return dimensionInsetSize;
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            if (container.getComponentCount() > 0) {
                Insets insets = container.getInsets();
                getComponent(0).setBounds(insets.left, insets.top, container.getWidth() - (insets.left + insets.right), container.getHeight() - (insets.top + insets.bottom));
            }
        }

        public void commitEdit() throws ParseException {
            getTextField().commitEdit();
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public int getBaseline(int i2, int i3) {
            super.getBaseline(i2, i3);
            Insets insets = getInsets();
            int baseline = getComponent(0).getBaseline((i2 - insets.left) - insets.right, (i3 - insets.top) - insets.bottom);
            if (baseline >= 0) {
                return baseline + insets.top;
            }
            return -1;
        }

        @Override // javax.swing.JComponent, java.awt.Component
        public Component.BaselineResizeBehavior getBaselineResizeBehavior() {
            return getComponent(0).getBaselineResizeBehavior();
        }
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$DateEditorFormatter.class */
    private static class DateEditorFormatter extends DateFormatter {
        private final SpinnerDateModel model;

        DateEditorFormatter(SpinnerDateModel spinnerDateModel, DateFormat dateFormat) {
            super(dateFormat);
            this.model = spinnerDateModel;
        }

        @Override // javax.swing.text.InternationalFormatter
        public void setMinimum(Comparable comparable) {
            this.model.setStart(comparable);
        }

        @Override // javax.swing.text.InternationalFormatter
        public Comparable getMinimum() {
            return this.model.getStart();
        }

        @Override // javax.swing.text.InternationalFormatter
        public void setMaximum(Comparable comparable) {
            this.model.setEnd(comparable);
        }

        @Override // javax.swing.text.InternationalFormatter
        public Comparable getMaximum() {
            return this.model.getEnd();
        }
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$DateEditor.class */
    public static class DateEditor extends DefaultEditor {
        private static String getDefaultPattern(Locale locale) {
            LocaleResources localeResources = LocaleProviderAdapter.getAdapter(DateFormatProvider.class, locale).getLocaleResources(locale);
            if (localeResources == null) {
                localeResources = LocaleProviderAdapter.forJRE().getLocaleResources(locale);
            }
            return localeResources.getDateTimePattern(3, 3, null);
        }

        public DateEditor(JSpinner jSpinner) {
            this(jSpinner, getDefaultPattern(jSpinner.getLocale()));
        }

        public DateEditor(JSpinner jSpinner, String str) {
            this(jSpinner, new SimpleDateFormat(str, jSpinner.getLocale()));
        }

        private DateEditor(JSpinner jSpinner, DateFormat dateFormat) {
            super(jSpinner);
            if (!(jSpinner.getModel() instanceof SpinnerDateModel)) {
                throw new IllegalArgumentException("model not a SpinnerDateModel");
            }
            SpinnerDateModel spinnerDateModel = (SpinnerDateModel) jSpinner.getModel();
            DateEditorFormatter dateEditorFormatter = new DateEditorFormatter(spinnerDateModel, dateFormat);
            DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory(dateEditorFormatter);
            JFormattedTextField textField = getTextField();
            textField.setEditable(true);
            textField.setFormatterFactory(defaultFormatterFactory);
            try {
                textField.setColumns(Math.max(dateEditorFormatter.valueToString(spinnerDateModel.getStart()).length(), dateEditorFormatter.valueToString(spinnerDateModel.getEnd()).length()));
            } catch (ParseException e2) {
            }
        }

        public SimpleDateFormat getFormat() {
            return (SimpleDateFormat) ((DateFormatter) getTextField().getFormatter()).getFormat();
        }

        public SpinnerDateModel getModel() {
            return (SpinnerDateModel) getSpinner().getModel();
        }
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$NumberEditorFormatter.class */
    private static class NumberEditorFormatter extends NumberFormatter {
        private final SpinnerNumberModel model;

        NumberEditorFormatter(SpinnerNumberModel spinnerNumberModel, NumberFormat numberFormat) {
            super(numberFormat);
            this.model = spinnerNumberModel;
            setValueClass(spinnerNumberModel.getValue().getClass());
        }

        @Override // javax.swing.text.InternationalFormatter
        public void setMinimum(Comparable comparable) {
            this.model.setMinimum(comparable);
        }

        @Override // javax.swing.text.InternationalFormatter
        public Comparable getMinimum() {
            return this.model.getMinimum();
        }

        @Override // javax.swing.text.InternationalFormatter
        public void setMaximum(Comparable comparable) {
            this.model.setMaximum(comparable);
        }

        @Override // javax.swing.text.InternationalFormatter
        public Comparable getMaximum() {
            return this.model.getMaximum();
        }
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$NumberEditor.class */
    public static class NumberEditor extends DefaultEditor {
        private static String getDefaultPattern(Locale locale) {
            LocaleResources localeResources = LocaleProviderAdapter.getAdapter(NumberFormatProvider.class, locale).getLocaleResources(locale);
            if (localeResources == null) {
                localeResources = LocaleProviderAdapter.forJRE().getLocaleResources(locale);
            }
            return localeResources.getNumberPatterns()[0];
        }

        public NumberEditor(JSpinner jSpinner) {
            this(jSpinner, getDefaultPattern(jSpinner.getLocale()));
        }

        public NumberEditor(JSpinner jSpinner, String str) {
            this(jSpinner, new DecimalFormat(str));
        }

        private NumberEditor(JSpinner jSpinner, DecimalFormat decimalFormat) {
            super(jSpinner);
            if (!(jSpinner.getModel() instanceof SpinnerNumberModel)) {
                throw new IllegalArgumentException("model not a SpinnerNumberModel");
            }
            SpinnerNumberModel spinnerNumberModel = (SpinnerNumberModel) jSpinner.getModel();
            NumberEditorFormatter numberEditorFormatter = new NumberEditorFormatter(spinnerNumberModel, decimalFormat);
            DefaultFormatterFactory defaultFormatterFactory = new DefaultFormatterFactory(numberEditorFormatter);
            JFormattedTextField textField = getTextField();
            textField.setEditable(true);
            textField.setFormatterFactory(defaultFormatterFactory);
            textField.setHorizontalAlignment(4);
            try {
                textField.setColumns(Math.max(numberEditorFormatter.valueToString(spinnerNumberModel.getMinimum()).length(), numberEditorFormatter.valueToString(spinnerNumberModel.getMaximum()).length()));
            } catch (ParseException e2) {
            }
        }

        public DecimalFormat getFormat() {
            return (DecimalFormat) ((NumberFormatter) getTextField().getFormatter()).getFormat();
        }

        public SpinnerNumberModel getModel() {
            return (SpinnerNumberModel) getSpinner().getModel();
        }

        @Override // java.awt.Component
        public void setComponentOrientation(ComponentOrientation componentOrientation) {
            super.setComponentOrientation(componentOrientation);
            getTextField().setHorizontalAlignment(componentOrientation.isLeftToRight() ? 4 : 2);
        }
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$ListEditor.class */
    public static class ListEditor extends DefaultEditor {
        public ListEditor(JSpinner jSpinner) {
            super(jSpinner);
            if (!(jSpinner.getModel() instanceof SpinnerListModel)) {
                throw new IllegalArgumentException("model not a SpinnerListModel");
            }
            getTextField().setEditable(true);
            getTextField().setFormatterFactory(new DefaultFormatterFactory(new ListFormatter()));
        }

        public SpinnerListModel getModel() {
            return (SpinnerListModel) getSpinner().getModel();
        }

        /* loaded from: rt.jar:javax/swing/JSpinner$ListEditor$ListFormatter.class */
        private class ListFormatter extends JFormattedTextField.AbstractFormatter {
            private DocumentFilter filter;

            private ListFormatter() {
            }

            @Override // javax.swing.JFormattedTextField.AbstractFormatter
            public String valueToString(Object obj) throws ParseException {
                if (obj == null) {
                    return "";
                }
                return obj.toString();
            }

            @Override // javax.swing.JFormattedTextField.AbstractFormatter
            public Object stringToValue(String str) throws ParseException {
                return str;
            }

            @Override // javax.swing.JFormattedTextField.AbstractFormatter
            protected DocumentFilter getDocumentFilter() {
                if (this.filter == null) {
                    this.filter = new Filter();
                }
                return this.filter;
            }

            /* loaded from: rt.jar:javax/swing/JSpinner$ListEditor$ListFormatter$Filter.class */
            private class Filter extends DocumentFilter {
                private Filter() {
                }

                @Override // javax.swing.text.DocumentFilter
                public void replace(DocumentFilter.FilterBypass filterBypass, int i2, int i3, String str, AttributeSet attributeSet) throws BadLocationException {
                    if (str != null && i2 + i3 == filterBypass.getDocument().getLength()) {
                        Object objFindNextMatch = ListEditor.this.getModel().findNextMatch(filterBypass.getDocument().getText(0, i2) + str);
                        String string = objFindNextMatch != null ? objFindNextMatch.toString() : null;
                        if (string != null) {
                            filterBypass.remove(0, i2 + i3);
                            filterBypass.insertString(0, string, null);
                            ListFormatter.this.getFormattedTextField().select(i2 + str.length(), string.length());
                            return;
                        }
                    }
                    super.replace(filterBypass, i2, i3, str, attributeSet);
                }

                @Override // javax.swing.text.DocumentFilter
                public void insertString(DocumentFilter.FilterBypass filterBypass, int i2, String str, AttributeSet attributeSet) throws BadLocationException {
                    replace(filterBypass, i2, 0, str, attributeSet);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$DisabledAction.class */
    private static class DisabledAction implements Action {
        private DisabledAction() {
        }

        @Override // javax.swing.Action
        public Object getValue(String str) {
            return null;
        }

        @Override // javax.swing.Action
        public void putValue(String str, Object obj) {
        }

        @Override // javax.swing.Action
        public void setEnabled(boolean z2) {
        }

        @Override // javax.swing.Action
        public boolean isEnabled() {
            return false;
        }

        @Override // javax.swing.Action
        public void addPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        @Override // javax.swing.Action
        public void removePropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
        }
    }

    @Override // java.awt.Component
    public AccessibleContext getAccessibleContext() {
        if (this.accessibleContext == null) {
            this.accessibleContext = new AccessibleJSpinner();
        }
        return this.accessibleContext;
    }

    /* loaded from: rt.jar:javax/swing/JSpinner$AccessibleJSpinner.class */
    protected class AccessibleJSpinner extends JComponent.AccessibleJComponent implements AccessibleValue, AccessibleAction, AccessibleText, AccessibleEditableText, ChangeListener {
        private Object oldModelValue;

        protected AccessibleJSpinner() {
            super();
            this.oldModelValue = null;
            this.oldModelValue = JSpinner.this.model.getValue();
            JSpinner.this.addChangeListener(this);
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            if (changeEvent != null) {
                Object value = JSpinner.this.model.getValue();
                firePropertyChange(AccessibleContext.ACCESSIBLE_VALUE_PROPERTY, this.oldModelValue, value);
                firePropertyChange(AccessibleContext.ACCESSIBLE_TEXT_PROPERTY, null, 0);
                this.oldModelValue = value;
                return;
            }
            throw new NullPointerException();
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.SPIN_BOX;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public int getAccessibleChildrenCount() {
            if (JSpinner.this.editor.getAccessibleContext() != null) {
                return 1;
            }
            return 0;
        }

        @Override // javax.swing.JComponent.AccessibleJComponent, java.awt.Container.AccessibleAWTContainer, java.awt.Component.AccessibleAWTComponent, javax.accessibility.AccessibleContext
        public Accessible getAccessibleChild(int i2) {
            if (i2 == 0 && JSpinner.this.editor.getAccessibleContext() != null) {
                return (Accessible) JSpinner.this.editor;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleAction getAccessibleAction() {
            return this;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleText getAccessibleText() {
            return this;
        }

        private AccessibleContext getEditorAccessibleContext() {
            if (JSpinner.this.editor instanceof DefaultEditor) {
                JFormattedTextField textField = ((DefaultEditor) JSpinner.this.editor).getTextField();
                if (textField != null) {
                    return textField.getAccessibleContext();
                }
                return null;
            }
            if (JSpinner.this.editor instanceof Accessible) {
                return JSpinner.this.editor.getAccessibleContext();
            }
            return null;
        }

        private AccessibleText getEditorAccessibleText() {
            AccessibleContext editorAccessibleContext = getEditorAccessibleContext();
            if (editorAccessibleContext != null) {
                return editorAccessibleContext.getAccessibleText();
            }
            return null;
        }

        private AccessibleEditableText getEditorAccessibleEditableText() {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText instanceof AccessibleEditableText) {
                return (AccessibleEditableText) editorAccessibleText;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleContext
        public AccessibleValue getAccessibleValue() {
            return this;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getCurrentAccessibleValue() {
            Object value = JSpinner.this.model.getValue();
            if (value instanceof Number) {
                return (Number) value;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleValue
        public boolean setCurrentAccessibleValue(Number number) {
            try {
                JSpinner.this.model.setValue(number);
                return true;
            } catch (IllegalArgumentException e2) {
                return false;
            }
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMinimumAccessibleValue() {
            if (JSpinner.this.model instanceof SpinnerNumberModel) {
                Object minimum = ((SpinnerNumberModel) JSpinner.this.model).getMinimum();
                if (minimum instanceof Number) {
                    return (Number) minimum;
                }
                return null;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleValue
        public Number getMaximumAccessibleValue() {
            if (JSpinner.this.model instanceof SpinnerNumberModel) {
                Object maximum = ((SpinnerNumberModel) JSpinner.this.model).getMaximum();
                if (maximum instanceof Number) {
                    return (Number) maximum;
                }
                return null;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public int getAccessibleActionCount() {
            return 2;
        }

        @Override // javax.accessibility.AccessibleAction
        public String getAccessibleActionDescription(int i2) {
            if (i2 == 0) {
                return AccessibleAction.INCREMENT;
            }
            if (i2 == 1) {
                return AccessibleAction.DECREMENT;
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleAction
        public boolean doAccessibleAction(int i2) {
            Object previousValue;
            if (i2 < 0 || i2 > 1) {
                return false;
            }
            if (i2 == 0) {
                previousValue = JSpinner.this.getNextValue();
            } else {
                previousValue = JSpinner.this.getPreviousValue();
            }
            try {
                JSpinner.this.model.setValue(previousValue);
                return true;
            } catch (IllegalArgumentException e2) {
                return false;
            }
        }

        private boolean sameWindowAncestor(Component component, Component component2) {
            return (component == null || component2 == null || SwingUtilities.getWindowAncestor(component) != SwingUtilities.getWindowAncestor(component2)) ? false : true;
        }

        @Override // javax.accessibility.AccessibleText
        public int getIndexAtPoint(Point point) {
            Point pointConvertPoint;
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null && sameWindowAncestor(JSpinner.this, JSpinner.this.editor) && (pointConvertPoint = SwingUtilities.convertPoint(JSpinner.this, point, JSpinner.this.editor)) != null) {
                return editorAccessibleText.getIndexAtPoint(pointConvertPoint);
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public Rectangle getCharacterBounds(int i2) {
            Rectangle characterBounds;
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null && (characterBounds = editorAccessibleText.getCharacterBounds(i2)) != null && sameWindowAncestor(JSpinner.this, JSpinner.this.editor)) {
                return SwingUtilities.convertRectangle(JSpinner.this.editor, characterBounds, JSpinner.this);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getCharCount() {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getCharCount();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public int getCaretPosition() {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getCaretPosition();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAtIndex(int i2, int i3) {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getAtIndex(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public String getAfterIndex(int i2, int i3) {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getAfterIndex(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public String getBeforeIndex(int i2, int i3) {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getBeforeIndex(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public AttributeSet getCharacterAttribute(int i2) {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getCharacterAttribute(i2);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionStart() {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getSelectionStart();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public int getSelectionEnd() {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getSelectionEnd();
            }
            return -1;
        }

        @Override // javax.accessibility.AccessibleText
        public String getSelectedText() {
            AccessibleText editorAccessibleText = getEditorAccessibleText();
            if (editorAccessibleText != null) {
                return editorAccessibleText.getSelectedText();
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void setTextContents(String str) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.setTextContents(str);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void insertTextAtIndex(int i2, String str) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.insertTextAtIndex(i2, str);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText, javax.accessibility.AccessibleExtendedText
        public String getTextRange(int i2, int i3) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                return editorAccessibleEditableText.getTextRange(i2, i3);
            }
            return null;
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void delete(int i2, int i3) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.delete(i2, i3);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void cut(int i2, int i3) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.cut(i2, i3);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void paste(int i2) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.paste(i2);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void replaceText(int i2, int i3, String str) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.replaceText(i2, i3, str);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void selectText(int i2, int i3) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.selectText(i2, i3);
            }
        }

        @Override // javax.accessibility.AccessibleEditableText
        public void setAttributes(int i2, int i3, AttributeSet attributeSet) {
            AccessibleEditableText editorAccessibleEditableText = getEditorAccessibleEditableText();
            if (editorAccessibleEditableText != null) {
                editorAccessibleEditableText.setAttributes(i2, i3, attributeSet);
            }
        }
    }
}
