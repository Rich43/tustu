package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SpinnerUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicSpinnerUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthSpinnerUI.class */
public class SynthSpinnerUI extends BasicSpinnerUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private EditorFocusHandler editorFocusHandler = new EditorFocusHandler();

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthSpinnerUI();
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected void installListeners() {
        JFormattedTextField textField;
        super.installListeners();
        this.spinner.addPropertyChangeListener(this);
        JComponent editor = this.spinner.getEditor();
        if ((editor instanceof JSpinner.DefaultEditor) && (textField = ((JSpinner.DefaultEditor) editor).getTextField()) != null) {
            textField.addFocusListener(this.editorFocusHandler);
        }
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected void uninstallListeners() {
        JFormattedTextField textField;
        super.uninstallListeners();
        this.spinner.removePropertyChangeListener(this);
        JComponent editor = this.spinner.getEditor();
        if ((editor instanceof JSpinner.DefaultEditor) && (textField = ((JSpinner.DefaultEditor) editor).getTextField()) != null) {
            textField.removeFocusListener(this.editorFocusHandler);
        }
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected void installDefaults() {
        LayoutManager layout = this.spinner.getLayout();
        if (layout == null || (layout instanceof UIResource)) {
            this.spinner.setLayout(createLayout());
        }
        updateStyle(this.spinner);
    }

    private void updateStyle(JSpinner jSpinner) {
        SynthContext context = getContext(jSpinner, 1);
        SynthStyle synthStyle = this.style;
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle && synthStyle != null) {
            installKeyboardActions();
        }
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected void uninstallDefaults() {
        if (this.spinner.getLayout() instanceof UIResource) {
            this.spinner.setLayout(null);
        }
        SynthContext context = getContext(this.spinner, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected LayoutManager createLayout() {
        return new SpinnerLayout();
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected Component createPreviousButton() {
        SynthArrowButton synthArrowButton = new SynthArrowButton(5);
        synthArrowButton.setName("Spinner.previousButton");
        installPreviousButtonListeners(synthArrowButton);
        return synthArrowButton;
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected Component createNextButton() {
        SynthArrowButton synthArrowButton = new SynthArrowButton(1);
        synthArrowButton.setName("Spinner.nextButton");
        installNextButtonListeners(synthArrowButton);
        return synthArrowButton;
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected JComponent createEditor() {
        JComponent editor = this.spinner.getEditor();
        editor.setName("Spinner.editor");
        updateEditorAlignment(editor);
        return editor;
    }

    @Override // javax.swing.plaf.basic.BasicSpinnerUI
    protected void replaceEditor(JComponent jComponent, JComponent jComponent2) {
        JFormattedTextField textField;
        JFormattedTextField textField2;
        this.spinner.remove(jComponent);
        this.spinner.add(jComponent2, "Editor");
        if ((jComponent instanceof JSpinner.DefaultEditor) && (textField2 = ((JSpinner.DefaultEditor) jComponent).getTextField()) != null) {
            textField2.removeFocusListener(this.editorFocusHandler);
        }
        if ((jComponent2 instanceof JSpinner.DefaultEditor) && (textField = ((JSpinner.DefaultEditor) jComponent2).getTextField()) != null) {
            textField.addFocusListener(this.editorFocusHandler);
        }
    }

    private void updateEditorAlignment(JComponent jComponent) {
        if (jComponent instanceof JSpinner.DefaultEditor) {
            SynthContext context = getContext(this.spinner);
            Integer num = (Integer) context.getStyle().get(context, "Spinner.editorAlignment");
            JFormattedTextField textField = ((JSpinner.DefaultEditor) jComponent).getTextField();
            if (num != null) {
                textField.setHorizontalAlignment(num.intValue());
            }
            textField.putClientProperty("JComponent.sizeVariant", this.spinner.getClientProperty("JComponent.sizeVariant"));
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, SynthLookAndFeel.getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintSpinnerBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintSpinnerBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthSpinnerUI$SpinnerLayout.class */
    private static class SpinnerLayout implements LayoutManager, UIResource {
        private Component nextButton;
        private Component previousButton;
        private Component editor;

        private SpinnerLayout() {
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
            return component == null ? new Dimension(0, 0) : component.getPreferredSize();
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
            Insets insets = container.getInsets();
            int width = container.getWidth() - (insets.left + insets.right);
            int height = container.getHeight() - (insets.top + insets.bottom);
            int i4 = height / 2;
            int i5 = height - i4;
            int iMax = Math.max(preferredSize(this.nextButton).width, preferredSize(this.previousButton).width);
            int i6 = width - iMax;
            if (container.getComponentOrientation().isLeftToRight()) {
                i3 = insets.left;
                i2 = i3 + i6;
            } else {
                i2 = insets.left;
                i3 = i2 + iMax;
            }
            int i7 = insets.top + i4;
            setBounds(this.editor, i3, insets.top, i6, height);
            setBounds(this.nextButton, i2, insets.top, iMax, i4);
            setBounds(this.previousButton, i2, i7, iMax, i5);
        }
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        JSpinner jSpinner = (JSpinner) propertyChangeEvent.getSource();
        SpinnerUI ui = jSpinner.getUI();
        if (ui instanceof SynthSpinnerUI) {
            SynthSpinnerUI synthSpinnerUI = (SynthSpinnerUI) ui;
            if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
                synthSpinnerUI.updateStyle(jSpinner);
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthSpinnerUI$EditorFocusHandler.class */
    private class EditorFocusHandler implements FocusListener {
        private EditorFocusHandler() {
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            SynthSpinnerUI.this.spinner.repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            SynthSpinnerUI.this.spinner.repaint();
        }
    }
}
