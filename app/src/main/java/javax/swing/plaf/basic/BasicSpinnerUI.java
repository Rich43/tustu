package javax.swing.plaf.basic;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import javax.swing.AbstractAction;
import javax.swing.ButtonModel;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.LookAndFeel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerModel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SpinnerUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.InternationalFormatter;
import jdk.jfr.Enabled;
import sun.swing.DefaultLookup;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicSpinnerUI.class */
public class BasicSpinnerUI extends SpinnerUI {
    protected JSpinner spinner;
    private Handler handler;
    private PropertyChangeListener propertyChangeListener;
    private static final ArrowButtonHandler nextButtonHandler = new ArrowButtonHandler("increment", true);
    private static final ArrowButtonHandler previousButtonHandler = new ArrowButtonHandler("decrement", false);
    private static final Dimension zeroSize = new Dimension(0, 0);

    public static ComponentUI createUI(JComponent jComponent) {
        return new BasicSpinnerUI();
    }

    private void maybeAdd(Component component, String str) {
        if (component != null) {
            this.spinner.add(component, str);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.spinner = (JSpinner) jComponent;
        installDefaults();
        installListeners();
        maybeAdd(createNextButton(), "Next");
        maybeAdd(createPreviousButton(), "Previous");
        maybeAdd(createEditor(), "Editor");
        updateEnabledState();
        installKeyboardActions();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallDefaults();
        uninstallListeners();
        this.spinner = null;
        jComponent.removeAll();
    }

    protected void installListeners() {
        JFormattedTextField textField;
        this.propertyChangeListener = createPropertyChangeListener();
        this.spinner.addPropertyChangeListener(this.propertyChangeListener);
        if (DefaultLookup.getBoolean(this.spinner, this, "Spinner.disableOnBoundaryValues", false)) {
            this.spinner.addChangeListener(getHandler());
        }
        JComponent editor = this.spinner.getEditor();
        if (editor != null && (editor instanceof JSpinner.DefaultEditor) && (textField = ((JSpinner.DefaultEditor) editor).getTextField()) != null) {
            textField.addFocusListener(nextButtonHandler);
            textField.addFocusListener(previousButtonHandler);
        }
    }

    protected void uninstallListeners() {
        JFormattedTextField textField;
        this.spinner.removePropertyChangeListener(this.propertyChangeListener);
        this.spinner.removeChangeListener(this.handler);
        JComponent editor = this.spinner.getEditor();
        removeEditorBorderListener(editor);
        if ((editor instanceof JSpinner.DefaultEditor) && (textField = ((JSpinner.DefaultEditor) editor).getTextField()) != null) {
            textField.removeFocusListener(nextButtonHandler);
            textField.removeFocusListener(previousButtonHandler);
        }
        this.propertyChangeListener = null;
        this.handler = null;
    }

    protected void installDefaults() {
        this.spinner.setLayout(createLayout());
        LookAndFeel.installBorder(this.spinner, "Spinner.border");
        LookAndFeel.installColorsAndFont(this.spinner, "Spinner.background", "Spinner.foreground", "Spinner.font");
        LookAndFeel.installProperty(this.spinner, "opaque", Boolean.TRUE);
    }

    protected void uninstallDefaults() {
        this.spinner.setLayout(null);
    }

    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    protected void installNextButtonListeners(Component component) {
        installButtonListeners(component, nextButtonHandler);
    }

    protected void installPreviousButtonListeners(Component component) {
        installButtonListeners(component, previousButtonHandler);
    }

    private void installButtonListeners(Component component, ArrowButtonHandler arrowButtonHandler) {
        if (component instanceof JButton) {
            ((JButton) component).addActionListener(arrowButtonHandler);
        }
        component.addMouseListener(arrowButtonHandler);
    }

    protected LayoutManager createLayout() {
        return getHandler();
    }

    protected PropertyChangeListener createPropertyChangeListener() {
        return getHandler();
    }

    protected Component createPreviousButton() {
        Component componentCreateArrowButton = createArrowButton(5);
        componentCreateArrowButton.setName("Spinner.previousButton");
        installPreviousButtonListeners(componentCreateArrowButton);
        return componentCreateArrowButton;
    }

    protected Component createNextButton() {
        Component componentCreateArrowButton = createArrowButton(1);
        componentCreateArrowButton.setName("Spinner.nextButton");
        installNextButtonListeners(componentCreateArrowButton);
        return componentCreateArrowButton;
    }

    private Component createArrowButton(int i2) {
        BasicArrowButton basicArrowButton = new BasicArrowButton(i2);
        Border border = UIManager.getBorder("Spinner.arrowButtonBorder");
        if (border instanceof UIResource) {
            basicArrowButton.setBorder(new CompoundBorder(border, null));
        } else {
            basicArrowButton.setBorder(border);
        }
        basicArrowButton.setInheritsPopupMenu(true);
        return basicArrowButton;
    }

    protected JComponent createEditor() {
        JComponent editor = this.spinner.getEditor();
        maybeRemoveEditorBorder(editor);
        installEditorBorderListener(editor);
        editor.setInheritsPopupMenu(true);
        updateEditorAlignment(editor);
        return editor;
    }

    protected void replaceEditor(JComponent jComponent, JComponent jComponent2) {
        this.spinner.remove(jComponent);
        maybeRemoveEditorBorder(jComponent2);
        installEditorBorderListener(jComponent2);
        jComponent2.setInheritsPopupMenu(true);
        this.spinner.add(jComponent2, "Editor");
    }

    private void updateEditorAlignment(JComponent jComponent) {
        if (jComponent instanceof JSpinner.DefaultEditor) {
            ((JSpinner.DefaultEditor) jComponent).getTextField().setHorizontalAlignment(UIManager.getInt("Spinner.editorAlignment"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void maybeRemoveEditorBorder(JComponent jComponent) {
        if (!UIManager.getBoolean("Spinner.editorBorderPainted")) {
            if ((jComponent instanceof JPanel) && jComponent.getBorder() == null && jComponent.getComponentCount() > 0) {
                jComponent = (JComponent) jComponent.getComponent(0);
            }
            if (jComponent != null && (jComponent.getBorder() instanceof UIResource)) {
                jComponent.setBorder(null);
            }
        }
    }

    private void installEditorBorderListener(JComponent jComponent) {
        if (!UIManager.getBoolean("Spinner.editorBorderPainted")) {
            if ((jComponent instanceof JPanel) && jComponent.getBorder() == null && jComponent.getComponentCount() > 0) {
                jComponent = (JComponent) jComponent.getComponent(0);
            }
            if (jComponent != null) {
                if (jComponent.getBorder() == null || (jComponent.getBorder() instanceof UIResource)) {
                    jComponent.addPropertyChangeListener(getHandler());
                }
            }
        }
    }

    private void removeEditorBorderListener(JComponent jComponent) {
        if (!UIManager.getBoolean("Spinner.editorBorderPainted")) {
            if ((jComponent instanceof JPanel) && jComponent.getComponentCount() > 0) {
                jComponent = (JComponent) jComponent.getComponent(0);
            }
            if (jComponent != null) {
                jComponent.removePropertyChangeListener(getHandler());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateEnabledState() {
        updateEnabledState(this.spinner, this.spinner.isEnabled());
    }

    private void updateEnabledState(Container container, boolean z2) {
        for (int componentCount = container.getComponentCount() - 1; componentCount >= 0; componentCount--) {
            Component component = container.getComponent(componentCount);
            if (DefaultLookup.getBoolean(this.spinner, this, "Spinner.disableOnBoundaryValues", false)) {
                SpinnerModel model = this.spinner.getModel();
                if (component.getName() == "Spinner.nextButton" && model.getNextValue() == null) {
                    component.setEnabled(false);
                } else if (component.getName() == "Spinner.previousButton" && model.getPreviousValue() == null) {
                    component.setEnabled(false);
                } else {
                    component.setEnabled(z2);
                }
            } else {
                component.setEnabled(z2);
            }
            if (component instanceof Container) {
                updateEnabledState((Container) component, z2);
            }
        }
    }

    protected void installKeyboardActions() {
        SwingUtilities.replaceUIInputMap(this.spinner, 1, getInputMap(1));
        LazyActionMap.installLazyActionMap(this.spinner, BasicSpinnerUI.class, "Spinner.actionMap");
    }

    private InputMap getInputMap(int i2) {
        if (i2 == 1) {
            return (InputMap) DefaultLookup.get(this.spinner, this, "Spinner.ancestorInputMap");
        }
        return null;
    }

    static void loadActionMap(LazyActionMap lazyActionMap) {
        lazyActionMap.put("increment", nextButtonHandler);
        lazyActionMap.put("decrement", previousButtonHandler);
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        int baseline;
        super.getBaseline(jComponent, i2, i3);
        JComponent editor = this.spinner.getEditor();
        Insets insets = this.spinner.getInsets();
        int i4 = (i2 - insets.left) - insets.right;
        int i5 = (i3 - insets.top) - insets.bottom;
        if (i4 >= 0 && i5 >= 0 && (baseline = editor.getBaseline(i4, i5)) >= 0) {
            return insets.top + baseline;
        }
        return -1;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        return this.spinner.getEditor().getBaselineResizeBehavior();
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSpinnerUI$ArrowButtonHandler.class */
    private static class ArrowButtonHandler extends AbstractAction implements FocusListener, MouseListener, UIResource {
        final Timer autoRepeatTimer;
        final boolean isNext;
        JSpinner spinner;
        JButton arrowButton;

        ArrowButtonHandler(String str, boolean z2) {
            super(str);
            this.spinner = null;
            this.arrowButton = null;
            this.isNext = z2;
            this.autoRepeatTimer = new Timer(60, this);
            this.autoRepeatTimer.setInitialDelay(300);
        }

        private JSpinner eventToSpinner(AWTEvent aWTEvent) {
            Object obj;
            Object source = aWTEvent.getSource();
            while (true) {
                obj = source;
                if (!(obj instanceof Component) || (obj instanceof JSpinner)) {
                    break;
                }
                source = ((Component) obj).getParent();
            }
            if (obj instanceof JSpinner) {
                return (JSpinner) obj;
            }
            return null;
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) throws SecurityException {
            JSpinner jSpinnerEventToSpinner = this.spinner;
            if (!(actionEvent.getSource() instanceof Timer)) {
                jSpinnerEventToSpinner = eventToSpinner(actionEvent);
                if (actionEvent.getSource() instanceof JButton) {
                    this.arrowButton = (JButton) actionEvent.getSource();
                }
            } else if (this.arrowButton != null && !this.arrowButton.getModel().isPressed() && this.autoRepeatTimer.isRunning()) {
                this.autoRepeatTimer.stop();
                jSpinnerEventToSpinner = null;
                this.arrowButton = null;
            }
            if (jSpinnerEventToSpinner != null) {
                try {
                    int calendarField = getCalendarField(jSpinnerEventToSpinner);
                    jSpinnerEventToSpinner.commitEdit();
                    if (calendarField != -1) {
                        ((SpinnerDateModel) jSpinnerEventToSpinner.getModel()).setCalendarField(calendarField);
                    }
                    Object nextValue = this.isNext ? jSpinnerEventToSpinner.getNextValue() : jSpinnerEventToSpinner.getPreviousValue();
                    if (nextValue != null) {
                        jSpinnerEventToSpinner.setValue(nextValue);
                        select(jSpinnerEventToSpinner);
                    }
                } catch (IllegalArgumentException e2) {
                    UIManager.getLookAndFeel().provideErrorFeedback(jSpinnerEventToSpinner);
                } catch (ParseException e3) {
                    UIManager.getLookAndFeel().provideErrorFeedback(jSpinnerEventToSpinner);
                }
            }
        }

        private void select(JSpinner jSpinner) {
            Object value;
            DateFormat.Field fieldOfCalendarField;
            JComponent editor = jSpinner.getEditor();
            if (editor instanceof JSpinner.DateEditor) {
                JSpinner.DateEditor dateEditor = (JSpinner.DateEditor) editor;
                JFormattedTextField textField = dateEditor.getTextField();
                SimpleDateFormat format = dateEditor.getFormat();
                if (format != null && (value = jSpinner.getValue()) != null && (fieldOfCalendarField = DateFormat.Field.ofCalendarField(dateEditor.getModel().getCalendarField())) != null) {
                    try {
                        AttributedCharacterIterator toCharacterIterator = format.formatToCharacterIterator(value);
                        if (!select(textField, toCharacterIterator, fieldOfCalendarField) && fieldOfCalendarField == DateFormat.Field.HOUR0) {
                            select(textField, toCharacterIterator, DateFormat.Field.HOUR1);
                        }
                    } catch (IllegalArgumentException e2) {
                    }
                }
            }
        }

        private boolean select(JFormattedTextField jFormattedTextField, AttributedCharacterIterator attributedCharacterIterator, DateFormat.Field field) {
            int length = jFormattedTextField.getDocument().getLength();
            attributedCharacterIterator.first();
            do {
                Map<AttributedCharacterIterator.Attribute, Object> attributes = attributedCharacterIterator.getAttributes();
                if (attributes != null && attributes.containsKey(field)) {
                    int runStart = attributedCharacterIterator.getRunStart(field);
                    int runLimit = attributedCharacterIterator.getRunLimit(field);
                    if (runStart != -1 && runLimit != -1 && runStart <= length && runLimit <= length) {
                        jFormattedTextField.select(runStart, runLimit);
                        return true;
                    }
                    return true;
                }
            } while (attributedCharacterIterator.next() != 65535);
            return false;
        }

        private int getCalendarField(JSpinner jSpinner) throws SecurityException {
            int calendarField;
            JComponent editor = jSpinner.getEditor();
            if (editor instanceof JSpinner.DateEditor) {
                JFormattedTextField textField = ((JSpinner.DateEditor) editor).getTextField();
                int selectionStart = textField.getSelectionStart();
                JFormattedTextField.AbstractFormatter formatter = textField.getFormatter();
                if (formatter instanceof InternationalFormatter) {
                    Format.Field[] fields = ((InternationalFormatter) formatter).getFields(selectionStart);
                    for (int i2 = 0; i2 < fields.length; i2++) {
                        if (fields[i2] instanceof DateFormat.Field) {
                            if (fields[i2] == DateFormat.Field.HOUR1) {
                                calendarField = 10;
                            } else {
                                calendarField = ((DateFormat.Field) fields[i2]).getCalendarField();
                            }
                            if (calendarField != -1) {
                                return calendarField;
                            }
                        }
                    }
                    return -1;
                }
                return -1;
            }
            return -1;
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            if (SwingUtilities.isLeftMouseButton(mouseEvent) && mouseEvent.getComponent().isEnabled()) {
                this.spinner = eventToSpinner(mouseEvent);
                this.autoRepeatTimer.start();
                focusSpinnerIfNecessary();
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            this.autoRepeatTimer.stop();
            this.arrowButton = null;
            this.spinner = null;
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            if (this.spinner != null && !this.autoRepeatTimer.isRunning() && this.spinner == eventToSpinner(mouseEvent)) {
                this.autoRepeatTimer.start();
            }
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            if (this.autoRepeatTimer.isRunning()) {
                this.autoRepeatTimer.stop();
            }
        }

        private void focusSpinnerIfNecessary() {
            Component componentAfter;
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (this.spinner.isRequestFocusEnabled()) {
                if (focusOwner == null || !SwingUtilities.isDescendingFrom(focusOwner, this.spinner)) {
                    JSpinner focusCycleRootAncestor = this.spinner;
                    if (!focusCycleRootAncestor.isFocusCycleRoot()) {
                        focusCycleRootAncestor = focusCycleRootAncestor.getFocusCycleRootAncestor();
                    }
                    if (focusCycleRootAncestor != null && (componentAfter = focusCycleRootAncestor.getFocusTraversalPolicy().getComponentAfter(focusCycleRootAncestor, this.spinner)) != null && SwingUtilities.isDescendingFrom(componentAfter, this.spinner)) {
                        componentAfter.requestFocus();
                    }
                }
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            if (this.spinner == eventToSpinner(focusEvent)) {
                if (this.autoRepeatTimer.isRunning()) {
                    this.autoRepeatTimer.stop();
                }
                this.spinner = null;
                if (this.arrowButton != null) {
                    ButtonModel model = this.arrowButton.getModel();
                    model.setPressed(false);
                    model.setArmed(false);
                    this.arrowButton = null;
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicSpinnerUI$Handler.class */
    private static class Handler implements LayoutManager, PropertyChangeListener, ChangeListener {
        private Component nextButton;
        private Component previousButton;
        private Component editor;

        private Handler() {
            this.nextButton = null;
            this.previousButton = null;
            this.editor = null;
        }

        @Override // java.awt.LayoutManager
        public void addLayoutComponent(String str, Component component) {
            if ("Next".equals(str)) {
                this.nextButton = component;
            } else if ("Previous".equals(str)) {
                this.previousButton = component;
            } else if ("Editor".equals(str)) {
                this.editor = component;
            }
        }

        @Override // java.awt.LayoutManager
        public void removeLayoutComponent(Component component) {
            if (component == this.nextButton) {
                this.nextButton = null;
            } else if (component == this.previousButton) {
                this.previousButton = null;
            } else if (component == this.editor) {
                this.editor = null;
            }
        }

        private Dimension preferredSize(Component component) {
            return component == null ? BasicSpinnerUI.zeroSize : component.getPreferredSize();
        }

        @Override // java.awt.LayoutManager
        public Dimension preferredLayoutSize(Container container) {
            Dimension dimensionPreferredSize = preferredSize(this.nextButton);
            Dimension dimensionPreferredSize2 = preferredSize(this.previousButton);
            Dimension dimensionPreferredSize3 = preferredSize(this.editor);
            dimensionPreferredSize3.height = ((dimensionPreferredSize3.height + 1) / 2) * 2;
            Dimension dimension = new Dimension(dimensionPreferredSize3.width, dimensionPreferredSize3.height);
            dimension.width += Math.max(dimensionPreferredSize.width, dimensionPreferredSize2.width);
            Insets insets = container.getInsets();
            dimension.width += insets.left + insets.right;
            dimension.height += insets.top + insets.bottom;
            return dimension;
        }

        @Override // java.awt.LayoutManager
        public Dimension minimumLayoutSize(Container container) {
            return preferredLayoutSize(container);
        }

        private void setBounds(Component component, int i2, int i3, int i4, int i5) {
            if (component != null) {
                component.setBounds(i2, i3, i4, i5);
            }
        }

        @Override // java.awt.LayoutManager
        public void layoutContainer(Container container) {
            int i2;
            int i3;
            int i4;
            int width = container.getWidth();
            int height = container.getHeight();
            Insets insets = container.getInsets();
            if (this.nextButton == null && this.previousButton == null) {
                setBounds(this.editor, insets.left, insets.top, (width - insets.left) - insets.right, (height - insets.top) - insets.bottom);
                return;
            }
            int iMax = Math.max(preferredSize(this.nextButton).width, preferredSize(this.previousButton).width);
            int i5 = height - (insets.top + insets.bottom);
            Insets insets2 = UIManager.getInsets("Spinner.arrowButtonInsets");
            if (insets2 == null) {
                insets2 = insets;
            }
            if (container.getComponentOrientation().isLeftToRight()) {
                i3 = insets.left;
                i4 = ((width - insets.left) - iMax) - insets2.right;
                i2 = (width - iMax) - insets2.right;
            } else {
                i2 = insets2.left;
                i3 = i2 + iMax;
                i4 = ((width - insets2.left) - iMax) - insets.right;
            }
            int i6 = insets2.top;
            int i7 = ((height / 2) + (height % 2)) - i6;
            int i8 = insets2.top + i7;
            int i9 = (height - i8) - insets2.bottom;
            setBounds(this.editor, i3, insets.top, i4, i5);
            setBounds(this.nextButton, i2, i6, iMax, i7);
            setBounds(this.previousButton, i2, i8, iMax, i9);
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            ComponentOrientation componentOrientation;
            JFormattedTextField textField;
            JFormattedTextField textField2;
            JFormattedTextField textField3;
            String propertyName = propertyChangeEvent.getPropertyName();
            if (!(propertyChangeEvent.getSource() instanceof JSpinner)) {
                if (propertyChangeEvent.getSource() instanceof JComponent) {
                    JComponent jComponent = (JComponent) propertyChangeEvent.getSource();
                    if ((jComponent.getParent() instanceof JPanel) && (jComponent.getParent().getParent() instanceof JSpinner) && "border".equals(propertyName)) {
                        SpinnerUI ui = ((JSpinner) jComponent.getParent().getParent()).getUI();
                        if (ui instanceof BasicSpinnerUI) {
                            ((BasicSpinnerUI) ui).maybeRemoveEditorBorder(jComponent);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            JSpinner jSpinner = (JSpinner) propertyChangeEvent.getSource();
            SpinnerUI ui2 = jSpinner.getUI();
            if (ui2 instanceof BasicSpinnerUI) {
                BasicSpinnerUI basicSpinnerUI = (BasicSpinnerUI) ui2;
                if ("editor".equals(propertyName)) {
                    JComponent jComponent2 = (JComponent) propertyChangeEvent.getOldValue();
                    JComponent jComponent3 = (JComponent) propertyChangeEvent.getNewValue();
                    basicSpinnerUI.replaceEditor(jComponent2, jComponent3);
                    basicSpinnerUI.updateEnabledState();
                    if ((jComponent2 instanceof JSpinner.DefaultEditor) && (textField3 = ((JSpinner.DefaultEditor) jComponent2).getTextField()) != null) {
                        textField3.removeFocusListener(BasicSpinnerUI.nextButtonHandler);
                        textField3.removeFocusListener(BasicSpinnerUI.previousButtonHandler);
                    }
                    if ((jComponent3 instanceof JSpinner.DefaultEditor) && (textField2 = ((JSpinner.DefaultEditor) jComponent3).getTextField()) != null) {
                        if (textField2.getFont() instanceof UIResource) {
                            textField2.setFont(jSpinner.getFont());
                        }
                        textField2.addFocusListener(BasicSpinnerUI.nextButtonHandler);
                        textField2.addFocusListener(BasicSpinnerUI.previousButtonHandler);
                        return;
                    }
                    return;
                }
                if (Enabled.NAME.equals(propertyName) || "model".equals(propertyName)) {
                    basicSpinnerUI.updateEnabledState();
                    return;
                }
                if ("font".equals(propertyName)) {
                    JComponent editor = jSpinner.getEditor();
                    if (editor != null && (editor instanceof JSpinner.DefaultEditor) && (textField = ((JSpinner.DefaultEditor) editor).getTextField()) != null && (textField.getFont() instanceof UIResource)) {
                        textField.setFont(jSpinner.getFont());
                        return;
                    }
                    return;
                }
                if (JComponent.TOOL_TIP_TEXT_KEY.equals(propertyName)) {
                    updateToolTipTextForChildren(jSpinner);
                    return;
                }
                if ("componentOrientation".equals(propertyName) && (componentOrientation = (ComponentOrientation) propertyChangeEvent.getNewValue()) != ((ComponentOrientation) propertyChangeEvent.getOldValue())) {
                    JComponent editor2 = jSpinner.getEditor();
                    if (editor2 != null) {
                        editor2.applyComponentOrientation(componentOrientation);
                    }
                    jSpinner.revalidate();
                    jSpinner.repaint();
                }
            }
        }

        private void updateToolTipTextForChildren(JComponent jComponent) {
            String toolTipText = jComponent.getToolTipText();
            Component[] components = jComponent.getComponents();
            for (int i2 = 0; i2 < components.length; i2++) {
                if (components[i2] instanceof JSpinner.DefaultEditor) {
                    JFormattedTextField textField = ((JSpinner.DefaultEditor) components[i2]).getTextField();
                    if (textField != null) {
                        textField.setToolTipText(toolTipText);
                    }
                } else if (components[i2] instanceof JComponent) {
                    ((JComponent) components[i2]).setToolTipText(jComponent.getToolTipText());
                }
            }
        }

        @Override // javax.swing.event.ChangeListener
        public void stateChanged(ChangeEvent changeEvent) {
            if (changeEvent.getSource() instanceof JSpinner) {
                JSpinner jSpinner = (JSpinner) changeEvent.getSource();
                SpinnerUI ui = jSpinner.getUI();
                if (DefaultLookup.getBoolean(jSpinner, ui, "Spinner.disableOnBoundaryValues", false) && (ui instanceof BasicSpinnerUI)) {
                    ((BasicSpinnerUI) ui).updateEnabledState();
                }
            }
        }
    }
}
