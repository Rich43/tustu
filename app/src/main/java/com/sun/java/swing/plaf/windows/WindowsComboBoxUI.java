package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import com.sun.java.swing.plaf.windows.WindowsBorders;
import com.sun.java.swing.plaf.windows.XPStyle;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.ButtonModel;
import javax.swing.ComboBoxEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import sun.swing.DefaultLookup;
import sun.swing.StringUIClientPropertyKey;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsComboBoxUI.class */
public class WindowsComboBoxUI extends BasicComboBoxUI {
    private boolean isRollover = false;
    private static final MouseListener rolloverListener = new MouseAdapter() { // from class: com.sun.java.swing.plaf.windows.WindowsComboBoxUI.1
        private void handleRollover(MouseEvent mouseEvent, boolean z2) {
            JComboBox comboBox = getComboBox(mouseEvent);
            WindowsComboBoxUI windowsComboBoxUI = getWindowsComboBoxUI(mouseEvent);
            if (comboBox == null || windowsComboBoxUI == null) {
                return;
            }
            if (!comboBox.isEditable()) {
                ButtonModel model = null;
                if (windowsComboBoxUI.arrowButton != null) {
                    model = windowsComboBoxUI.arrowButton.getModel();
                }
                if (model != null) {
                    model.setRollover(z2);
                }
            }
            windowsComboBoxUI.isRollover = z2;
            comboBox.repaint();
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseEntered(MouseEvent mouseEvent) {
            handleRollover(mouseEvent, true);
        }

        @Override // java.awt.event.MouseAdapter, java.awt.event.MouseListener
        public void mouseExited(MouseEvent mouseEvent) {
            handleRollover(mouseEvent, false);
        }

        private JComboBox getComboBox(MouseEvent mouseEvent) {
            Object source = mouseEvent.getSource();
            JComboBox jComboBox = null;
            if (source instanceof JComboBox) {
                jComboBox = (JComboBox) source;
            } else if (source instanceof XPComboBoxButton) {
                jComboBox = ((XPComboBoxButton) source).getWindowsComboBoxUI().comboBox;
            }
            return jComboBox;
        }

        private WindowsComboBoxUI getWindowsComboBoxUI(MouseEvent mouseEvent) {
            JComboBox comboBox = getComboBox(mouseEvent);
            WindowsComboBoxUI windowsComboBoxUI = null;
            if (comboBox != null && (comboBox.getUI() instanceof WindowsComboBoxUI)) {
                windowsComboBoxUI = (WindowsComboBoxUI) comboBox.getUI();
            }
            return windowsComboBoxUI;
        }
    };
    private static final PropertyChangeListener componentOrientationListener = new PropertyChangeListener() { // from class: com.sun.java.swing.plaf.windows.WindowsComboBoxUI.2
        @Override // java.beans.PropertyChangeListener
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("componentOrientation" == propertyChangeEvent.getPropertyName()) {
                Object source = propertyChangeEvent.getSource();
                if ((source instanceof JComboBox) && (((JComboBox) source).getUI() instanceof WindowsComboBoxUI)) {
                    JComboBox jComboBox = (JComboBox) source;
                    WindowsComboBoxUI windowsComboBoxUI = (WindowsComboBoxUI) jComboBox.getUI();
                    if (windowsComboBoxUI.arrowButton instanceof XPComboBoxButton) {
                        ((XPComboBoxButton) windowsComboBoxUI.arrowButton).setPart(jComboBox.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT ? TMSchema.Part.CP_DROPDOWNBUTTONLEFT : TMSchema.Part.CP_DROPDOWNBUTTONRIGHT);
                    }
                }
            }
        }
    };

    public static ComponentUI createUI(JComponent jComponent) {
        return new WindowsComboBoxUI();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        super.installUI(jComponent);
        this.isRollover = false;
        this.comboBox.setRequestFocusEnabled(true);
        if (XPStyle.getXP() != null && this.arrowButton != null) {
            this.comboBox.addMouseListener(rolloverListener);
            this.arrowButton.addMouseListener(rolloverListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        this.comboBox.removeMouseListener(rolloverListener);
        if (this.arrowButton != null) {
            this.arrowButton.removeMouseListener(rolloverListener);
        }
        super.uninstallUI(jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void installListeners() {
        super.installListeners();
        XPStyle xp = XPStyle.getXP();
        if (xp != null && xp.isSkinDefined(this.comboBox, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT)) {
            this.comboBox.addPropertyChangeListener("componentOrientation", componentOrientationListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void uninstallListeners() {
        super.uninstallListeners();
        this.comboBox.removePropertyChangeListener("componentOrientation", componentOrientationListener);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void configureEditor() {
        super.configureEditor();
        if (XPStyle.getXP() != null) {
            this.editor.addMouseListener(rolloverListener);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void unconfigureEditor() {
        super.unconfigureEditor();
        this.editor.removeMouseListener(rolloverListener);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        if (XPStyle.getXP() != null) {
            paintXPComboBoxBackground(graphics, jComponent);
        }
        super.paint(graphics, jComponent);
    }

    TMSchema.State getXPComboBoxState(JComponent jComponent) {
        TMSchema.State state = TMSchema.State.NORMAL;
        if (!jComponent.isEnabled()) {
            state = TMSchema.State.DISABLED;
        } else if (isPopupVisible(this.comboBox)) {
            state = TMSchema.State.PRESSED;
        } else if (this.isRollover) {
            state = TMSchema.State.HOT;
        }
        return state;
    }

    private void paintXPComboBoxBackground(Graphics graphics, JComponent jComponent) {
        XPStyle xp = XPStyle.getXP();
        if (xp == null) {
            return;
        }
        TMSchema.State xPComboBoxState = getXPComboBoxState(jComponent);
        XPStyle.Skin skin = null;
        if (!this.comboBox.isEditable() && xp.isSkinDefined(jComponent, TMSchema.Part.CP_READONLY)) {
            skin = xp.getSkin(jComponent, TMSchema.Part.CP_READONLY);
        }
        if (skin == null) {
            skin = xp.getSkin(jComponent, TMSchema.Part.CP_COMBOBOX);
        }
        skin.paintSkin(graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight(), xPComboBoxState);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void paintCurrentValue(Graphics graphics, Rectangle rectangle, boolean z2) {
        Component listCellRendererComponent;
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            rectangle.f12372x += 2;
            rectangle.f12373y += 2;
            rectangle.width -= 4;
            rectangle.height -= 4;
        } else {
            rectangle.f12372x++;
            rectangle.f12373y++;
            rectangle.width -= 2;
            rectangle.height -= 2;
        }
        if (!this.comboBox.isEditable() && xp != null && xp.isSkinDefined(this.comboBox, TMSchema.Part.CP_READONLY)) {
            ListCellRenderer renderer = this.comboBox.getRenderer();
            if (z2 && !isPopupVisible(this.comboBox)) {
                listCellRendererComponent = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, true, false);
            } else {
                listCellRendererComponent = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
            }
            listCellRendererComponent.setFont(this.comboBox.getFont());
            if (this.comboBox.isEnabled()) {
                listCellRendererComponent.setForeground(this.comboBox.getForeground());
                listCellRendererComponent.setBackground(this.comboBox.getBackground());
            } else {
                listCellRendererComponent.setForeground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledForeground", null));
                listCellRendererComponent.setBackground(DefaultLookup.getColor(this.comboBox, this, "ComboBox.disabledBackground", null));
            }
            boolean z3 = false;
            if (listCellRendererComponent instanceof JPanel) {
                z3 = true;
            }
            this.currentValuePane.paintComponent(graphics, listCellRendererComponent, this.comboBox, rectangle.f12372x, rectangle.f12373y, rectangle.width, rectangle.height, z3);
            return;
        }
        super.paintCurrentValue(graphics, rectangle, z2);
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    public void paintCurrentValueBackground(Graphics graphics, Rectangle rectangle, boolean z2) {
        if (XPStyle.getXP() == null) {
            super.paintCurrentValueBackground(graphics, rectangle, z2);
        }
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI, javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension minimumSize = super.getMinimumSize(jComponent);
        if (XPStyle.getXP() != null) {
            minimumSize.width += 5;
        } else {
            minimumSize.width += 4;
        }
        minimumSize.height += 2;
        return minimumSize;
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected LayoutManager createLayoutManager() {
        return new BasicComboBoxUI.ComboBoxLayoutManager() { // from class: com.sun.java.swing.plaf.windows.WindowsComboBoxUI.3
            @Override // javax.swing.plaf.basic.BasicComboBoxUI.ComboBoxLayoutManager, java.awt.LayoutManager
            public void layoutContainer(Container container) {
                super.layoutContainer(container);
                if (XPStyle.getXP() != null && WindowsComboBoxUI.this.arrowButton != null) {
                    Dimension size = container.getSize();
                    Insets insets = WindowsComboBoxUI.this.getInsets();
                    int i2 = WindowsComboBoxUI.this.arrowButton.getPreferredSize().width;
                    WindowsComboBoxUI.this.arrowButton.setBounds(WindowsGraphicsUtils.isLeftToRight((JComboBox) container) ? (size.width - insets.right) - i2 : insets.left, insets.top, i2, (size.height - insets.top) - insets.bottom);
                }
            }
        };
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected void installKeyboardActions() {
        super.installKeyboardActions();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboPopup createPopup() {
        return super.createPopup();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ComboBoxEditor createEditor() {
        return new WindowsComboBoxEditor();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected ListCellRenderer createRenderer() {
        XPStyle xp = XPStyle.getXP();
        if (xp != null && xp.isSkinDefined(this.comboBox, TMSchema.Part.CP_READONLY)) {
            return new WindowsComboBoxRenderer();
        }
        return super.createRenderer();
    }

    @Override // javax.swing.plaf.basic.BasicComboBoxUI
    protected JButton createArrowButton() {
        XPStyle xp = XPStyle.getXP();
        if (xp != null) {
            return new XPComboBoxButton(xp);
        }
        return super.createArrowButton();
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsComboBoxUI$XPComboBoxButton.class */
    private class XPComboBoxButton extends XPStyle.GlyphButton {
        /* JADX WARN: Illegal instructions before constructor call */
        public XPComboBoxButton(XPStyle xPStyle) {
            TMSchema.Part part;
            if (xPStyle.isSkinDefined(WindowsComboBoxUI.this.comboBox, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT)) {
                part = WindowsComboBoxUI.this.comboBox.getComponentOrientation() == ComponentOrientation.RIGHT_TO_LEFT ? TMSchema.Part.CP_DROPDOWNBUTTONLEFT : TMSchema.Part.CP_DROPDOWNBUTTONRIGHT;
            } else {
                part = TMSchema.Part.CP_DROPDOWNBUTTON;
            }
            super(null, part);
            setRequestFocusEnabled(false);
        }

        @Override // com.sun.java.swing.plaf.windows.XPStyle.GlyphButton
        protected TMSchema.State getState() {
            TMSchema.State state = super.getState();
            XPStyle xp = XPStyle.getXP();
            if (state != TMSchema.State.DISABLED && WindowsComboBoxUI.this.comboBox != null && !WindowsComboBoxUI.this.comboBox.isEditable() && xp != null && xp.isSkinDefined(WindowsComboBoxUI.this.comboBox, TMSchema.Part.CP_DROPDOWNBUTTONRIGHT)) {
                state = TMSchema.State.NORMAL;
            }
            return state;
        }

        @Override // javax.swing.JComponent, java.awt.Container, java.awt.Component
        public Dimension getPreferredSize() {
            return new Dimension(17, 21);
        }

        void setPart(TMSchema.Part part) {
            setPart(WindowsComboBoxUI.this.comboBox, part);
        }

        WindowsComboBoxUI getWindowsComboBoxUI() {
            return WindowsComboBoxUI.this;
        }
    }

    @Deprecated
    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsComboBoxUI$WindowsComboPopup.class */
    protected class WindowsComboPopup extends BasicComboPopup {
        public WindowsComboPopup(JComboBox jComboBox) {
            super(jComboBox);
        }

        @Override // javax.swing.plaf.basic.BasicComboPopup
        protected KeyListener createKeyListener() {
            return new InvocationKeyHandler();
        }

        /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsComboBoxUI$WindowsComboPopup$InvocationKeyHandler.class */
        protected class InvocationKeyHandler extends BasicComboPopup.InvocationKeyHandler {
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            protected InvocationKeyHandler() {
                super();
                WindowsComboPopup.this.getClass();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsComboBoxUI$WindowsComboBoxEditor.class */
    public static class WindowsComboBoxEditor extends BasicComboBoxEditor.UIResource {
        @Override // javax.swing.plaf.basic.BasicComboBoxEditor
        protected JTextField createEditorComponent() {
            JTextField jTextFieldCreateEditorComponent = super.createEditorComponent();
            Border border = (Border) UIManager.get("ComboBox.editorBorder");
            if (border != null) {
                jTextFieldCreateEditorComponent.setBorder(border);
            }
            jTextFieldCreateEditorComponent.setOpaque(false);
            return jTextFieldCreateEditorComponent;
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxEditor, javax.swing.ComboBoxEditor
        public void setItem(Object obj) {
            super.setItem(obj);
            Component focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            if (focusOwner == this.editor || focusOwner == this.editor.getParent()) {
                this.editor.selectAll();
            }
        }
    }

    /* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsComboBoxUI$WindowsComboBoxRenderer.class */
    private static class WindowsComboBoxRenderer extends BasicComboBoxRenderer.UIResource {
        private static final Object BORDER_KEY = new StringUIClientPropertyKey("BORDER_KEY");
        private static final Border NULL_BORDER = new EmptyBorder(0, 0, 0, 0);

        private WindowsComboBoxRenderer() {
        }

        @Override // javax.swing.plaf.basic.BasicComboBoxRenderer, javax.swing.ListCellRenderer
        public Component getListCellRendererComponent(JList jList, Object obj, int i2, boolean z2, boolean z3) {
            Component listCellRendererComponent = super.getListCellRendererComponent(jList, obj, i2, z2, z3);
            if (listCellRendererComponent instanceof JComponent) {
                JComponent jComponent = (JComponent) listCellRendererComponent;
                if (i2 == -1 && z2) {
                    Border border = jComponent.getBorder();
                    jComponent.setBorder(new WindowsBorders.DashedBorder(jList.getForeground()));
                    if (jComponent.getClientProperty(BORDER_KEY) == null) {
                        jComponent.putClientProperty(BORDER_KEY, border == null ? NULL_BORDER : border);
                    }
                } else if (jComponent.getBorder() instanceof WindowsBorders.DashedBorder) {
                    Object clientProperty = jComponent.getClientProperty(BORDER_KEY);
                    if (clientProperty instanceof Border) {
                        jComponent.setBorder(clientProperty == NULL_BORDER ? null : (Border) clientProperty);
                    }
                    jComponent.putClientProperty(BORDER_KEY, null);
                }
                if (i2 == -1) {
                    jComponent.setOpaque(false);
                    jComponent.setForeground(jList.getForeground());
                } else {
                    jComponent.setOpaque(true);
                }
            }
            return listCellRendererComponent;
        }
    }
}
