package javax.swing.plaf.synth;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.ComboPopup;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthComboBoxUI.class */
public class SynthComboBoxUI extends BasicComboBoxUI implements PropertyChangeListener, SynthUI {
    private SynthStyle style;
    private boolean useListColors;
    Insets popupInsets;
    private boolean buttonWhenNotEditable;
    private boolean pressedWhenPopupVisible;
    private ButtonHandler buttonHandler;
    private EditorFocusHandler editorFocusHandler;
    private boolean forceOpaque = false;

    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthComboBoxUI();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        this.buttonHandler = new ButtonHandler();
        super.installUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void installDefaults() {
        updateStyle(this.comboBox);
    }

    private void updateStyle(JComboBox jComboBox) {
        SynthStyle synthStyle = this.style;
        SynthContext context = getContext(jComboBox, 1);
        this.style = SynthLookAndFeel.updateStyle(context, this);
        if (this.style != synthStyle) {
            this.padding = (Insets) this.style.get(context, "ComboBox.padding");
            this.popupInsets = (Insets) this.style.get(context, "ComboBox.popupInsets");
            this.useListColors = this.style.getBoolean(context, "ComboBox.rendererUseListColors", true);
            this.buttonWhenNotEditable = this.style.getBoolean(context, "ComboBox.buttonWhenNotEditable", false);
            this.pressedWhenPopupVisible = this.style.getBoolean(context, "ComboBox.pressedWhenPopupVisible", false);
            this.squareButton = this.style.getBoolean(context, "ComboBox.squareButton", true);
            if (synthStyle != null) {
                uninstallKeyboardActions();
                installKeyboardActions();
            }
            this.forceOpaque = this.style.getBoolean(context, "ComboBox.forceOpaque", false);
        }
        context.dispose();
        if (this.listBox != null) {
            SynthLookAndFeel.updateStyles(this.listBox);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void installListeners() {
        this.comboBox.addPropertyChangeListener(this);
        this.comboBox.addMouseListener(this.buttonHandler);
        this.editorFocusHandler = new EditorFocusHandler(this.comboBox);
        super.installListeners();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        if (this.popup instanceof SynthComboPopup) {
            ((SynthComboPopup) this.popup).removePopupMenuListener(this.buttonHandler);
        }
        super.uninstallUI(jComponent);
        this.buttonHandler = null;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void uninstallDefaults() {
        SynthContext context = getContext(this.comboBox, 1);
        this.style.uninstallDefaults(context);
        context.dispose();
        this.style = null;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void uninstallListeners() {
        this.editorFocusHandler.unregister();
        this.comboBox.removePropertyChangeListener(this);
        this.comboBox.removeMouseListener(this.buttonHandler);
        this.buttonHandler.pressed = false;
        this.buttonHandler.over = false;
        super.uninstallListeners();
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public SynthContext getContext(JComponent jComponent) {
        return getContext(jComponent, getComponentState(jComponent));
    }

    private SynthContext getContext(JComponent jComponent, int i2) {
        return SynthContext.getContext(jComponent, this.style, i2);
    }

    private int getComponentState(JComponent jComponent) {
        if (!(jComponent instanceof JComboBox)) {
            return SynthLookAndFeel.getComponentState(jComponent);
        }
        JComboBox jComboBox = (JComboBox) jComponent;
        if (shouldActLikeButton()) {
            int i2 = 1;
            if (!jComponent.isEnabled()) {
                i2 = 8;
            }
            if (this.buttonHandler.isPressed()) {
                i2 |= 4;
            }
            if (this.buttonHandler.isRollover()) {
                i2 |= 2;
            }
            if (jComboBox.isFocusOwner()) {
                i2 |= 256;
            }
            return i2;
        }
        int componentState = SynthLookAndFeel.getComponentState(jComponent);
        if (jComboBox.isEditable() && jComboBox.getEditor().getEditorComponent().isFocusOwner()) {
            componentState |= 256;
        }
        return componentState;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboPopup createPopup() {
        SynthComboPopup synthComboPopup = new SynthComboPopup(this.comboBox);
        synthComboPopup.addPopupMenuListener(this.buttonHandler);
        return synthComboPopup;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ListCellRenderer createRenderer() {
        return new SynthComboBoxRenderer();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboBoxEditor createEditor() {
        return new SynthComboBoxEditor();
    }

    @Override // java.beans.PropertyChangeListener
    public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        if (SynthLookAndFeel.shouldUpdateStyle(propertyChangeEvent)) {
            updateStyle(this.comboBox);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected JButton createArrowButton() {
        SynthArrowButton synthArrowButton = new SynthArrowButton(5);
        synthArrowButton.setName("ComboBox.arrowButton");
        synthArrowButton.setModel(this.buttonHandler);
        return synthArrowButton;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        SynthLookAndFeel.update(context, graphics);
        context.getPainter().paintComboBoxBackground(context, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        paint(context, graphics);
        context.dispose();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        SynthContext context = getContext(jComponent);
        paint(context, graphics);
        context.dispose();
    }

    protected void paint(SynthContext synthContext, Graphics graphics) {
        this.hasFocus = this.comboBox.hasFocus();
        if (!this.comboBox.isEditable()) {
            paintCurrentValue(graphics, rectangleForCurrentValue(), this.hasFocus);
        }
    }

    @Override // javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintComboBoxBorder(synthContext, graphics, i2, i3, i4, i5);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void paintCurrentValue(Graphics graphics, Rectangle rectangle, boolean z2) {
        Component listCellRendererComponent = this.comboBox.getRenderer().getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
        boolean z3 = false;
        if (listCellRendererComponent instanceof JPanel) {
            z3 = true;
        }
        if (listCellRendererComponent instanceof UIResource) {
            listCellRendererComponent.setName("ComboBox.renderer");
        }
        boolean z4 = this.forceOpaque && (listCellRendererComponent instanceof JComponent);
        if (z4) {
            ((JComponent) listCellRendererComponent).setOpaque(false);
        }
        int i2 = rectangle.f12372x;
        int i3 = rectangle.f12373y;
        int i4 = rectangle.width;
        int i5 = rectangle.height;
        if (this.padding != null) {
            i2 = rectangle.f12372x + this.padding.left;
            i3 = rectangle.f12373y + this.padding.top;
            i4 = rectangle.width - (this.padding.left + this.padding.right);
            i5 = rectangle.height - (this.padding.top + this.padding.bottom);
        }
        this.currentValuePane.paintComponent(graphics, listCellRendererComponent, this.comboBox, i2, i3, i4, i5, z3);
        if (z4) {
            ((JComponent) listCellRendererComponent).setOpaque(true);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean shouldActLikeButton() {
        return this.buttonWhenNotEditable && !this.comboBox.isEditable();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected Dimension getDefaultSize() {
        Dimension sizeForComponent = getSizeForComponent(new SynthComboBoxRenderer().getListCellRendererComponent(this.listBox, " ", -1, false, false));
        return new Dimension(sizeForComponent.width, sizeForComponent.height);
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthComboBoxUI$SynthComboBoxRenderer.class */
    private class SynthComboBoxRenderer extends JLabel implements ListCellRenderer<Object>, UIResource {
        public SynthComboBoxRenderer() {
            setText(" ");
        }

        @Override // java.awt.Component
        public String getName() {
            String name = super.getName();
            return name == null ? "ComboBox.renderer" : name;
        }

        @Override // javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList<?> jList, Object obj, int i2, boolean z2, boolean z3) {
            setName("ComboBox.listRenderer");
            SynthLookAndFeel.resetSelectedUI();
            if (z2) {
                setBackground(jList.getSelectionBackground());
                setForeground(jList.getSelectionForeground());
                if (!SynthComboBoxUI.this.useListColors) {
                    SynthLookAndFeel.setSelectedUI((SynthLabelUI) SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), z2, z3, jList.isEnabled(), false);
                }
            } else {
                setBackground(jList.getBackground());
                setForeground(jList.getForeground());
            }
            setFont(jList.getFont());
            if (obj instanceof Icon) {
                setIcon((Icon) obj);
                setText("");
            } else {
                String string = obj == null ? " " : obj.toString();
                if ("".equals(string)) {
                    string = " ";
                }
                setText(string);
            }
            if (SynthComboBoxUI.this.comboBox != null) {
                setEnabled(SynthComboBoxUI.this.comboBox.isEnabled());
                setComponentOrientation(SynthComboBoxUI.this.comboBox.getComponentOrientation());
            }
            return this;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public void paint(Graphics graphics) {
            super.paint(graphics);
            SynthLookAndFeel.resetSelectedUI();
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthComboBoxUI$SynthComboBoxEditor.class */
    private static class SynthComboBoxEditor extends BasicComboBoxEditor.UIResource {
        private SynthComboBoxEditor() {
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxEditor
        public JTextField createEditorComponent() {
            JTextField jTextField = new JTextField("", 9);
            jTextField.setName("ComboBox.textField");
            return jTextField;
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthComboBoxUI$ButtonHandler.class */
    private final class ButtonHandler extends DefaultButtonModel implements MouseListener, PopupMenuListener {
        private boolean over;
        private boolean pressed;

        private ButtonHandler() {
        }

        private void updatePressed(boolean z2) {
            this.pressed = z2 && isEnabled();
            if (SynthComboBoxUI.this.shouldActLikeButton()) {
                SynthComboBoxUI.this.comboBox.repaint();
            }
        }

        private void updateOver(boolean z2) {
            boolean zIsRollover = isRollover();
            this.over = z2 && isEnabled();
            boolean zIsRollover2 = isRollover();
            if (SynthComboBoxUI.this.shouldActLikeButton() && zIsRollover != zIsRollover2) {
                SynthComboBoxUI.this.comboBox.repaint();
            }
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public boolean isPressed() {
            return (SynthComboBoxUI.this.shouldActLikeButton() ? this.pressed : super.isPressed()) || (SynthComboBoxUI.this.pressedWhenPopupVisible && SynthComboBoxUI.this.comboBox.isPopupVisible());
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public boolean isArmed() {
            return SynthComboBoxUI.this.shouldActLikeButton() || (SynthComboBoxUI.this.pressedWhenPopupVisible && SynthComboBoxUI.this.comboBox.isPopupVisible()) ? isPressed() : super.isArmed();
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public boolean isRollover() {
            return SynthComboBoxUI.this.shouldActLikeButton() ? this.over : super.isRollover();
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public void setPressed(boolean z2) {
            super.setPressed(z2);
            updatePressed(z2);
        }

        @Override // javax.swing.DefaultButtonModel, javax.swing.ButtonModel
        public void setRollover(boolean z2) {
            super.setRollover(z2);
            updateOver(z2);
        }

        @Override // java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            updateOver(true);
        }

        @Override // java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            updateOver(false);
        }

        @Override // java.awt.event.MouseListener
        public void mousePressed(MouseEvent mouseEvent) {
            updatePressed(true);
        }

        @Override // java.awt.event.MouseListener
        public void mouseReleased(MouseEvent mouseEvent) {
            updatePressed(false);
        }

        @Override // java.awt.event.MouseListener
        public void mouseClicked(MouseEvent mouseEvent) {
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuCanceled(PopupMenuEvent popupMenuEvent) {
            if (SynthComboBoxUI.this.shouldActLikeButton() || SynthComboBoxUI.this.pressedWhenPopupVisible) {
                SynthComboBoxUI.this.comboBox.repaint();
            }
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeVisible(PopupMenuEvent popupMenuEvent) {
        }

        @Override // javax.swing.event.PopupMenuListener
        public void popupMenuWillBecomeInvisible(PopupMenuEvent popupMenuEvent) {
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/synth/SynthComboBoxUI$EditorFocusHandler.class */
    private static class EditorFocusHandler implements FocusListener, PropertyChangeListener {
        private JComboBox comboBox;
        private ComboBoxEditor editor;
        private Component editorComponent;

        private EditorFocusHandler(JComboBox jComboBox) {
            this.editor = null;
            this.editorComponent = null;
            this.comboBox = jComboBox;
            this.editor = jComboBox.getEditor();
            if (this.editor != null) {
                this.editorComponent = this.editor.getEditorComponent();
                if (this.editorComponent != null) {
                    this.editorComponent.addFocusListener(this);
                }
            }
            jComboBox.addPropertyChangeListener("editor", this);
        }

        public void unregister() {
            this.comboBox.removePropertyChangeListener(this);
            if (this.editorComponent != null) {
                this.editorComponent.removeFocusListener(this);
            }
        }

        @Override // java.awt.event.FocusListener
        public void focusGained(FocusEvent focusEvent) {
            this.comboBox.repaint();
        }

        @Override // java.awt.event.FocusListener
        public void focusLost(FocusEvent focusEvent) {
            this.comboBox.repaint();
        }

        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            ComboBoxEditor editor = this.comboBox.getEditor();
            if (this.editor != editor) {
                if (this.editorComponent != null) {
                    this.editorComponent.removeFocusListener(this);
                }
                this.editor = editor;
                if (this.editor != null) {
                    this.editorComponent = this.editor.getEditorComponent();
                    if (this.editorComponent != null) {
                        this.editorComponent.addFocusListener(this);
                    }
                }
            }
        }
    }
}
