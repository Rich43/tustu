package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.HashSet;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.DefaultButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.View;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicRadioButtonUI.class */
public class BasicRadioButtonUI extends BasicToggleButtonUI {
    protected Icon icon;
    private static final String propertyPrefix = "RadioButton.";
    private static final Object BASIC_RADIO_BUTTON_UI_KEY = new Object();
    private static Dimension size = new Dimension();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle prefViewRect = new Rectangle();
    private static Rectangle prefIconRect = new Rectangle();
    private static Rectangle prefTextRect = new Rectangle();
    private static Insets prefInsets = new Insets(0, 0, 0, 0);
    private boolean defaults_initialized = false;
    private KeyListener keyListener = null;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        BasicRadioButtonUI basicRadioButtonUI = (BasicRadioButtonUI) appContext.get(BASIC_RADIO_BUTTON_UI_KEY);
        if (basicRadioButtonUI == null) {
            basicRadioButtonUI = new BasicRadioButtonUI();
            appContext.put(BASIC_RADIO_BUTTON_UI_KEY, basicRadioButtonUI);
        }
        return basicRadioButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.icon = UIManager.getIcon(getPropertyPrefix() + "icon");
            this.defaults_initialized = true;
        }
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }

    public Icon getDefaultIcon() {
        return this.icon;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void installListeners(AbstractButton abstractButton) {
        super.installListeners(abstractButton);
        if (!(abstractButton instanceof JRadioButton)) {
            return;
        }
        this.keyListener = createKeyListener();
        abstractButton.addKeyListener(this.keyListener);
        abstractButton.setFocusTraversalKeysEnabled(false);
        abstractButton.getActionMap().put("Previous", new SelectPreviousBtn());
        abstractButton.getActionMap().put("Next", new SelectNextBtn());
        abstractButton.getInputMap(1).put(KeyStroke.getKeyStroke("UP"), "Previous");
        abstractButton.getInputMap(1).put(KeyStroke.getKeyStroke("DOWN"), "Next");
        abstractButton.getInputMap(1).put(KeyStroke.getKeyStroke("LEFT"), "Previous");
        abstractButton.getInputMap(1).put(KeyStroke.getKeyStroke("RIGHT"), "Next");
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallListeners(AbstractButton abstractButton) {
        super.uninstallListeners(abstractButton);
        if (!(abstractButton instanceof JRadioButton)) {
            return;
        }
        abstractButton.getActionMap().remove("Previous");
        abstractButton.getActionMap().remove("Next");
        abstractButton.getInputMap(1).remove(KeyStroke.getKeyStroke("UP"));
        abstractButton.getInputMap(1).remove(KeyStroke.getKeyStroke("DOWN"));
        abstractButton.getInputMap(1).remove(KeyStroke.getKeyStroke("LEFT"));
        abstractButton.getInputMap(1).remove(KeyStroke.getKeyStroke("RIGHT"));
        if (this.keyListener != null) {
            abstractButton.removeKeyListener(this.keyListener);
            this.keyListener = null;
        }
    }

    @Override // javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public synchronized void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        Font font = jComponent.getFont();
        graphics.setFont(font);
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics, font);
        Insets insets = jComponent.getInsets();
        size = abstractButton.getSize(size);
        viewRect.f12372x = insets.left;
        viewRect.f12373y = insets.top;
        viewRect.width = size.width - (insets.right + viewRect.f12372x);
        viewRect.height = size.height - (insets.bottom + viewRect.f12373y);
        Rectangle rectangle = iconRect;
        Rectangle rectangle2 = iconRect;
        Rectangle rectangle3 = iconRect;
        iconRect.height = 0;
        rectangle3.width = 0;
        rectangle2.f12373y = 0;
        rectangle.f12372x = 0;
        Rectangle rectangle4 = textRect;
        Rectangle rectangle5 = textRect;
        Rectangle rectangle6 = textRect;
        textRect.height = 0;
        rectangle6.width = 0;
        rectangle5.f12373y = 0;
        rectangle4.f12372x = 0;
        Icon icon = abstractButton.getIcon();
        String strLayoutCompoundLabel = SwingUtilities.layoutCompoundLabel(jComponent, fontMetrics, abstractButton.getText(), icon != null ? icon : getDefaultIcon(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalTextPosition(), abstractButton.getHorizontalTextPosition(), viewRect, iconRect, textRect, abstractButton.getText() == null ? 0 : abstractButton.getIconTextGap());
        if (jComponent.isOpaque()) {
            graphics.setColor(abstractButton.getBackground());
            graphics.fillRect(0, 0, size.width, size.height);
        }
        if (icon != null) {
            if (!model.isEnabled()) {
                icon = model.isSelected() ? abstractButton.getDisabledSelectedIcon() : abstractButton.getDisabledIcon();
            } else if (model.isPressed() && model.isArmed()) {
                icon = abstractButton.getPressedIcon();
                if (icon == null) {
                    icon = abstractButton.getSelectedIcon();
                }
            } else if (model.isSelected()) {
                if (abstractButton.isRolloverEnabled() && model.isRollover()) {
                    icon = abstractButton.getRolloverSelectedIcon();
                    if (icon == null) {
                        icon = abstractButton.getSelectedIcon();
                    }
                } else {
                    icon = abstractButton.getSelectedIcon();
                }
            } else if (abstractButton.isRolloverEnabled() && model.isRollover()) {
                icon = abstractButton.getRolloverIcon();
            }
            if (icon == null) {
                icon = abstractButton.getIcon();
            }
            icon.paintIcon(jComponent, graphics, iconRect.f12372x, iconRect.f12373y);
        } else {
            getDefaultIcon().paintIcon(jComponent, graphics, iconRect.f12372x, iconRect.f12373y);
        }
        if (strLayoutCompoundLabel != null) {
            View view = (View) jComponent.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, textRect);
            } else {
                paintText(graphics, abstractButton, textRect, strLayoutCompoundLabel);
            }
            if (abstractButton.hasFocus() && abstractButton.isFocusPainted() && textRect.width > 0 && textRect.height > 0) {
                paintFocus(graphics, textRect, size);
            }
        }
    }

    protected void paintFocus(Graphics graphics, Rectangle rectangle, Dimension dimension) {
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        if (jComponent.getComponentCount() > 0) {
            return null;
        }
        AbstractButton abstractButton = (AbstractButton) jComponent;
        String text = abstractButton.getText();
        Icon icon = abstractButton.getIcon();
        if (icon == null) {
            icon = getDefaultIcon();
        }
        FontMetrics fontMetrics = abstractButton.getFontMetrics(abstractButton.getFont());
        Rectangle rectangle = prefViewRect;
        prefViewRect.f12373y = 0;
        rectangle.f12372x = 0;
        prefViewRect.width = Short.MAX_VALUE;
        prefViewRect.height = Short.MAX_VALUE;
        Rectangle rectangle2 = prefIconRect;
        Rectangle rectangle3 = prefIconRect;
        Rectangle rectangle4 = prefIconRect;
        prefIconRect.height = 0;
        rectangle4.width = 0;
        rectangle3.f12373y = 0;
        rectangle2.f12372x = 0;
        Rectangle rectangle5 = prefTextRect;
        Rectangle rectangle6 = prefTextRect;
        Rectangle rectangle7 = prefTextRect;
        prefTextRect.height = 0;
        rectangle7.width = 0;
        rectangle6.f12373y = 0;
        rectangle5.f12372x = 0;
        SwingUtilities.layoutCompoundLabel(jComponent, fontMetrics, text, icon, abstractButton.getVerticalAlignment(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalTextPosition(), abstractButton.getHorizontalTextPosition(), prefViewRect, prefIconRect, prefTextRect, text == null ? 0 : abstractButton.getIconTextGap());
        int iMin = Math.min(prefIconRect.f12372x, prefTextRect.f12372x);
        int iMax = Math.max(prefIconRect.f12372x + prefIconRect.width, prefTextRect.f12372x + prefTextRect.width);
        int i2 = iMax - iMin;
        int iMax2 = Math.max(prefIconRect.f12373y + prefIconRect.height, prefTextRect.f12373y + prefTextRect.height) - Math.min(prefIconRect.f12373y, prefTextRect.f12373y);
        prefInsets = abstractButton.getInsets(prefInsets);
        return new Dimension(i2 + prefInsets.left + prefInsets.right, iMax2 + prefInsets.top + prefInsets.bottom);
    }

    private KeyListener createKeyListener() {
        if (this.keyListener == null) {
            this.keyListener = new KeyHandler();
        }
        return this.keyListener;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isValidRadioButtonObj(Object obj) {
        return (obj instanceof JRadioButton) && ((JRadioButton) obj).isVisible() && ((JRadioButton) obj).isEnabled();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void selectRadioButton(ActionEvent actionEvent, boolean z2) {
        Object source = actionEvent.getSource();
        if (!isValidRadioButtonObj(source)) {
            return;
        }
        new ButtonGroupInfo((JRadioButton) source).selectNewButton(z2);
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicRadioButtonUI$SelectPreviousBtn.class */
    private class SelectPreviousBtn extends AbstractAction {
        public SelectPreviousBtn() {
            super("Previous");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicRadioButtonUI.this.selectRadioButton(actionEvent, false);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicRadioButtonUI$SelectNextBtn.class */
    private class SelectNextBtn extends AbstractAction {
        public SelectNextBtn() {
            super("Next");
        }

        @Override // java.awt.event.ActionListener
        public void actionPerformed(ActionEvent actionEvent) {
            BasicRadioButtonUI.this.selectRadioButton(actionEvent, true);
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicRadioButtonUI$ButtonGroupInfo.class */
    private class ButtonGroupInfo {
        JRadioButton activeBtn;
        HashSet<JRadioButton> btnsInGroup;
        JRadioButton firstBtn = null;
        JRadioButton lastBtn = null;
        JRadioButton previousBtn = null;
        JRadioButton nextBtn = null;
        boolean srcFound = false;

        public ButtonGroupInfo(JRadioButton jRadioButton) {
            this.activeBtn = null;
            this.btnsInGroup = null;
            this.activeBtn = jRadioButton;
            this.btnsInGroup = new HashSet<>();
        }

        boolean containsInGroup(Object obj) {
            return this.btnsInGroup.contains(obj);
        }

        Component getFocusTransferBaseComponent(boolean z2) {
            JRadioButton jRadioButton = this.activeBtn;
            Container focusCycleRootAncestor = jRadioButton.getFocusCycleRootAncestor();
            if (focusCycleRootAncestor != null) {
                FocusTraversalPolicy focusTraversalPolicy = focusCycleRootAncestor.getFocusTraversalPolicy();
                if (containsInGroup(z2 ? focusTraversalPolicy.getComponentAfter(focusCycleRootAncestor, this.activeBtn) : focusTraversalPolicy.getComponentBefore(focusCycleRootAncestor, this.activeBtn))) {
                    jRadioButton = z2 ? this.lastBtn : this.firstBtn;
                }
            }
            return jRadioButton;
        }

        boolean getButtonGroupInfo() {
            ButtonGroup group;
            Enumeration<AbstractButton> elements;
            if (this.activeBtn == null) {
                return false;
            }
            this.btnsInGroup.clear();
            ButtonModel model = this.activeBtn.getModel();
            if (!(model instanceof DefaultButtonModel) || (group = ((DefaultButtonModel) model).getGroup()) == null || (elements = group.getElements()) == null) {
                return false;
            }
            while (elements.hasMoreElements()) {
                AbstractButton abstractButtonNextElement2 = elements.nextElement2();
                if (BasicRadioButtonUI.this.isValidRadioButtonObj(abstractButtonNextElement2)) {
                    this.btnsInGroup.add((JRadioButton) abstractButtonNextElement2);
                    if (null == this.firstBtn) {
                        this.firstBtn = (JRadioButton) abstractButtonNextElement2;
                    }
                    if (this.activeBtn == abstractButtonNextElement2) {
                        this.srcFound = true;
                    } else if (!this.srcFound) {
                        this.previousBtn = (JRadioButton) abstractButtonNextElement2;
                    } else if (this.nextBtn == null) {
                        this.nextBtn = (JRadioButton) abstractButtonNextElement2;
                    }
                    this.lastBtn = (JRadioButton) abstractButtonNextElement2;
                }
            }
            return true;
        }

        void selectNewButton(boolean z2) {
            JRadioButton jRadioButton;
            if (getButtonGroupInfo() && this.srcFound) {
                if (z2) {
                    jRadioButton = null == this.nextBtn ? this.firstBtn : this.nextBtn;
                } else {
                    jRadioButton = null == this.previousBtn ? this.lastBtn : this.previousBtn;
                }
                if (jRadioButton != null && jRadioButton != this.activeBtn) {
                    ButtonModel model = jRadioButton.getModel();
                    model.setPressed(true);
                    model.setArmed(true);
                    jRadioButton.requestFocusInWindow();
                    jRadioButton.setSelected(true);
                    model.setPressed(false);
                    model.setArmed(false);
                }
            }
        }

        void jumpToNextComponent(boolean z2) {
            if (!getButtonGroupInfo()) {
                if (this.activeBtn != null) {
                    this.lastBtn = this.activeBtn;
                    this.firstBtn = this.activeBtn;
                } else {
                    return;
                }
            }
            JRadioButton jRadioButton = this.activeBtn;
            Component focusTransferBaseComponent = getFocusTransferBaseComponent(z2);
            if (focusTransferBaseComponent != null) {
                if (z2) {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent(focusTransferBaseComponent);
                } else {
                    KeyboardFocusManager.getCurrentKeyboardFocusManager().focusPreviousComponent(focusTransferBaseComponent);
                }
            }
        }
    }

    /* loaded from: rt.jar:javax/swing/plaf/basic/BasicRadioButtonUI$KeyHandler.class */
    private class KeyHandler implements KeyListener {
        private KeyHandler() {
        }

        @Override // java.awt.event.KeyListener
        public void keyPressed(KeyEvent keyEvent) {
            if (keyEvent.getKeyCode() == 9) {
                Object source = keyEvent.getSource();
                if (BasicRadioButtonUI.this.isValidRadioButtonObj(source)) {
                    keyEvent.consume();
                    BasicRadioButtonUI.this.new ButtonGroupInfo((JRadioButton) source).jumpToNextComponent(!keyEvent.isShiftDown());
                }
            }
        }

        @Override // java.awt.event.KeyListener
        public void keyReleased(KeyEvent keyEvent) {
        }

        @Override // java.awt.event.KeyListener
        public void keyTyped(KeyEvent keyEvent) {
        }
    }
}
