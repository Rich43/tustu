package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseMotionListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicButtonUI.class */
public class BasicButtonUI extends ButtonUI {
    protected int defaultTextIconGap;
    private int shiftOffset = 0;
    protected int defaultTextShiftOffset;
    private static final String propertyPrefix = "Button.";
    private static final Object BASIC_BUTTON_UI_KEY = new Object();
    private static Rectangle viewRect = new Rectangle();
    private static Rectangle textRect = new Rectangle();
    private static Rectangle iconRect = new Rectangle();

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        BasicButtonUI basicButtonUI = (BasicButtonUI) appContext.get(BASIC_BUTTON_UI_KEY);
        if (basicButtonUI == null) {
            basicButtonUI = new BasicButtonUI();
            appContext.put(BASIC_BUTTON_UI_KEY, basicButtonUI);
        }
        return basicButtonUI;
    }

    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        installDefaults((AbstractButton) jComponent);
        installListeners((AbstractButton) jComponent);
        installKeyboardActions((AbstractButton) jComponent);
        BasicHTML.updateRenderer(jComponent, ((AbstractButton) jComponent).getText());
    }

    protected void installDefaults(AbstractButton abstractButton) {
        String propertyPrefix2 = getPropertyPrefix();
        this.defaultTextShiftOffset = UIManager.getInt(propertyPrefix2 + "textShiftOffset");
        if (abstractButton.isContentAreaFilled()) {
            LookAndFeel.installProperty(abstractButton, "opaque", Boolean.TRUE);
        } else {
            LookAndFeel.installProperty(abstractButton, "opaque", Boolean.FALSE);
        }
        if (abstractButton.getMargin() == null || (abstractButton.getMargin() instanceof UIResource)) {
            abstractButton.setMargin(UIManager.getInsets(propertyPrefix2 + AbstractButton.MARGIN_CHANGED_PROPERTY));
        }
        LookAndFeel.installColorsAndFont(abstractButton, propertyPrefix2 + "background", propertyPrefix2 + "foreground", propertyPrefix2 + "font");
        LookAndFeel.installBorder(abstractButton, propertyPrefix2 + "border");
        Object obj = UIManager.get(propertyPrefix2 + "rollover");
        if (obj != null) {
            LookAndFeel.installProperty(abstractButton, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY, obj);
        }
        LookAndFeel.installProperty(abstractButton, "iconTextGap", 4);
    }

    protected void installListeners(AbstractButton abstractButton) {
        BasicButtonListener basicButtonListenerCreateButtonListener = createButtonListener(abstractButton);
        if (basicButtonListenerCreateButtonListener != null) {
            abstractButton.addMouseListener(basicButtonListenerCreateButtonListener);
            abstractButton.addMouseMotionListener(basicButtonListenerCreateButtonListener);
            abstractButton.addFocusListener(basicButtonListenerCreateButtonListener);
            abstractButton.addPropertyChangeListener(basicButtonListenerCreateButtonListener);
            abstractButton.addChangeListener(basicButtonListenerCreateButtonListener);
        }
    }

    protected void installKeyboardActions(AbstractButton abstractButton) {
        BasicButtonListener buttonListener = getButtonListener(abstractButton);
        if (buttonListener != null) {
            buttonListener.installKeyboardActions(abstractButton);
        }
    }

    @Override // javax.swing.plaf.ComponentUI
    public void uninstallUI(JComponent jComponent) {
        uninstallKeyboardActions((AbstractButton) jComponent);
        uninstallListeners((AbstractButton) jComponent);
        uninstallDefaults((AbstractButton) jComponent);
        BasicHTML.updateRenderer(jComponent, "");
    }

    protected void uninstallKeyboardActions(AbstractButton abstractButton) {
        BasicButtonListener buttonListener = getButtonListener(abstractButton);
        if (buttonListener != null) {
            buttonListener.uninstallKeyboardActions(abstractButton);
        }
    }

    protected void uninstallListeners(AbstractButton abstractButton) {
        BasicButtonListener buttonListener = getButtonListener(abstractButton);
        if (buttonListener != null) {
            abstractButton.removeMouseListener(buttonListener);
            abstractButton.removeMouseMotionListener(buttonListener);
            abstractButton.removeFocusListener(buttonListener);
            abstractButton.removeChangeListener(buttonListener);
            abstractButton.removePropertyChangeListener(buttonListener);
        }
    }

    protected void uninstallDefaults(AbstractButton abstractButton) {
        LookAndFeel.uninstallBorder(abstractButton);
    }

    protected BasicButtonListener createButtonListener(AbstractButton abstractButton) {
        return new BasicButtonListener(abstractButton);
    }

    public int getDefaultTextIconGap(AbstractButton abstractButton) {
        return this.defaultTextIconGap;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        String strLayout = layout(abstractButton, SwingUtilities2.getFontMetrics(abstractButton, graphics), abstractButton.getWidth(), abstractButton.getHeight());
        clearTextShiftOffset();
        if (model.isArmed() && model.isPressed()) {
            paintButtonPressed(graphics, abstractButton);
        }
        if (abstractButton.getIcon() != null) {
            paintIcon(graphics, jComponent, iconRect);
        }
        if (strLayout != null && !strLayout.equals("")) {
            View view = (View) jComponent.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, textRect);
            } else {
                paintText(graphics, abstractButton, textRect, strLayout);
            }
        }
        if (abstractButton.isFocusPainted() && abstractButton.hasFocus()) {
            paintFocus(graphics, abstractButton, viewRect, textRect, iconRect);
        }
    }

    protected void paintIcon(Graphics graphics, JComponent jComponent, Rectangle rectangle) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        Icon icon = abstractButton.getIcon();
        Icon rolloverIcon = null;
        if (icon == null) {
            return;
        }
        Icon selectedIcon = null;
        if (model.isSelected()) {
            selectedIcon = abstractButton.getSelectedIcon();
            if (selectedIcon != null) {
                icon = selectedIcon;
            }
        }
        if (!model.isEnabled()) {
            if (model.isSelected()) {
                rolloverIcon = abstractButton.getDisabledSelectedIcon();
                if (rolloverIcon == null) {
                    rolloverIcon = selectedIcon;
                }
            }
            if (rolloverIcon == null) {
                rolloverIcon = abstractButton.getDisabledIcon();
            }
        } else if (model.isPressed() && model.isArmed()) {
            rolloverIcon = abstractButton.getPressedIcon();
            if (rolloverIcon != null) {
                clearTextShiftOffset();
            }
        } else if (abstractButton.isRolloverEnabled() && model.isRollover()) {
            if (model.isSelected()) {
                rolloverIcon = abstractButton.getRolloverSelectedIcon();
                if (rolloverIcon == null) {
                    rolloverIcon = selectedIcon;
                }
            }
            if (rolloverIcon == null) {
                rolloverIcon = abstractButton.getRolloverIcon();
            }
        }
        if (rolloverIcon != null) {
            icon = rolloverIcon;
        }
        if (model.isPressed() && model.isArmed()) {
            icon.paintIcon(jComponent, graphics, rectangle.f12372x + getTextShiftOffset(), rectangle.f12373y + getTextShiftOffset());
        } else {
            icon.paintIcon(jComponent, graphics, rectangle.f12372x, rectangle.f12373y);
        }
    }

    protected void paintText(Graphics graphics, JComponent jComponent, Rectangle rectangle, String str) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics);
        int displayedMnemonicIndex = abstractButton.getDisplayedMnemonicIndex();
        if (model.isEnabled()) {
            graphics.setColor(abstractButton.getForeground());
            SwingUtilities2.drawStringUnderlineCharAt(jComponent, graphics, str, displayedMnemonicIndex, rectangle.f12372x + getTextShiftOffset(), rectangle.f12373y + fontMetrics.getAscent() + getTextShiftOffset());
        } else {
            graphics.setColor(abstractButton.getBackground().brighter());
            SwingUtilities2.drawStringUnderlineCharAt(jComponent, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
            graphics.setColor(abstractButton.getBackground().darker());
            SwingUtilities2.drawStringUnderlineCharAt(jComponent, graphics, str, displayedMnemonicIndex, rectangle.f12372x - 1, (rectangle.f12373y + fontMetrics.getAscent()) - 1);
        }
    }

    protected void paintText(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, String str) {
        paintText(graphics, (JComponent) abstractButton, rectangle, str);
    }

    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
    }

    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
    }

    protected void clearTextShiftOffset() {
        this.shiftOffset = 0;
    }

    protected void setTextShiftOffset() {
        this.shiftOffset = this.defaultTextShiftOffset;
    }

    protected int getTextShiftOffset() {
        return this.shiftOffset;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMinimumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width - (view.getPreferredSpan(0) - view.getMinimumSpan(0)));
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getPreferredSize(JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        return BasicGraphicsUtils.getPreferredButtonSize(abstractButton, abstractButton.getIconTextGap());
    }

    @Override // javax.swing.plaf.ComponentUI
    public Dimension getMaximumSize(JComponent jComponent) {
        Dimension preferredSize = getPreferredSize(jComponent);
        View view = (View) jComponent.getClientProperty("html");
        if (view != null) {
            preferredSize.width = (int) (preferredSize.width + (view.getMaximumSpan(0) - view.getPreferredSpan(0)));
        }
        return preferredSize;
    }

    @Override // javax.swing.plaf.ComponentUI
    public int getBaseline(JComponent jComponent, int i2, int i3) {
        super.getBaseline(jComponent, i2, i3);
        AbstractButton abstractButton = (AbstractButton) jComponent;
        String text = abstractButton.getText();
        if (text == null || "".equals(text)) {
            return -1;
        }
        FontMetrics fontMetrics = abstractButton.getFontMetrics(abstractButton.getFont());
        layout(abstractButton, fontMetrics, i2, i3);
        return BasicHTML.getBaseline(abstractButton, textRect.f12373y, fontMetrics.getAscent(), textRect.width, textRect.height);
    }

    @Override // javax.swing.plaf.ComponentUI
    public Component.BaselineResizeBehavior getBaselineResizeBehavior(JComponent jComponent) {
        super.getBaselineResizeBehavior(jComponent);
        if (jComponent.getClientProperty("html") != null) {
            return Component.BaselineResizeBehavior.OTHER;
        }
        switch (((AbstractButton) jComponent).getVerticalAlignment()) {
            case 0:
                return Component.BaselineResizeBehavior.CENTER_OFFSET;
            case 1:
                return Component.BaselineResizeBehavior.CONSTANT_ASCENT;
            case 2:
            default:
                return Component.BaselineResizeBehavior.OTHER;
            case 3:
                return Component.BaselineResizeBehavior.CONSTANT_DESCENT;
        }
    }

    private String layout(AbstractButton abstractButton, FontMetrics fontMetrics, int i2, int i3) {
        Insets insets = abstractButton.getInsets();
        viewRect.f12372x = insets.left;
        viewRect.f12373y = insets.top;
        viewRect.width = i2 - (insets.right + viewRect.f12372x);
        viewRect.height = i3 - (insets.bottom + viewRect.f12373y);
        Rectangle rectangle = textRect;
        Rectangle rectangle2 = textRect;
        Rectangle rectangle3 = textRect;
        textRect.height = 0;
        rectangle3.width = 0;
        rectangle2.f12373y = 0;
        rectangle.f12372x = 0;
        Rectangle rectangle4 = iconRect;
        Rectangle rectangle5 = iconRect;
        Rectangle rectangle6 = iconRect;
        iconRect.height = 0;
        rectangle6.width = 0;
        rectangle5.f12373y = 0;
        rectangle4.f12372x = 0;
        return SwingUtilities.layoutCompoundLabel(abstractButton, fontMetrics, abstractButton.getText(), abstractButton.getIcon(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalTextPosition(), abstractButton.getHorizontalTextPosition(), viewRect, iconRect, textRect, abstractButton.getText() == null ? 0 : abstractButton.getIconTextGap());
    }

    private BasicButtonListener getButtonListener(AbstractButton abstractButton) {
        MouseMotionListener[] mouseMotionListeners = abstractButton.getMouseMotionListeners();
        if (mouseMotionListeners != null) {
            for (MouseMotionListener mouseMotionListener : mouseMotionListeners) {
                if (mouseMotionListener instanceof BasicButtonListener) {
                    return (BasicButtonListener) mouseMotionListener;
                }
            }
            return null;
        }
        return null;
    }
}
