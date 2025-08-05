package javax.swing.plaf.metal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalRadioButtonUI.class */
public class MetalRadioButtonUI extends BasicRadioButtonUI {
    private static final Object METAL_RADIO_BUTTON_UI_KEY = new Object();
    protected Color focusColor;
    protected Color selectColor;
    protected Color disabledTextColor;
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MetalRadioButtonUI metalRadioButtonUI = (MetalRadioButtonUI) appContext.get(METAL_RADIO_BUTTON_UI_KEY);
        if (metalRadioButtonUI == null) {
            metalRadioButtonUI = new MetalRadioButtonUI();
            appContext.put(METAL_RADIO_BUTTON_UI_KEY, metalRadioButtonUI);
        }
        return metalRadioButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
            this.selectColor = UIManager.getColor(getPropertyPrefix() + Constants.ATTRNAME_SELECT);
            this.disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");
            this.defaults_initialized = true;
        }
        LookAndFeel.installProperty(abstractButton, "opaque", Boolean.TRUE);
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    protected void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
        this.defaults_initialized = false;
    }

    protected Color getSelectColor() {
        return this.selectColor;
    }

    protected Color getDisabledTextColor() {
        return this.disabledTextColor;
    }

    protected Color getFocusColor() {
        return this.focusColor;
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicToggleButtonUI, javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public synchronized void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        Dimension size = jComponent.getSize();
        int i2 = size.width;
        int i3 = size.height;
        Font font = jComponent.getFont();
        graphics.setFont(font);
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics, font);
        Rectangle rectangle = new Rectangle(size);
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        Insets insets = jComponent.getInsets();
        rectangle.f12372x += insets.left;
        rectangle.f12373y += insets.top;
        rectangle.width -= insets.right + rectangle.f12372x;
        rectangle.height -= insets.bottom + rectangle.f12373y;
        Icon icon = abstractButton.getIcon();
        String strLayoutCompoundLabel = SwingUtilities.layoutCompoundLabel(jComponent, fontMetrics, abstractButton.getText(), icon != null ? icon : getDefaultIcon(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalTextPosition(), abstractButton.getHorizontalTextPosition(), rectangle, rectangle2, rectangle3, abstractButton.getIconTextGap());
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
            icon.paintIcon(jComponent, graphics, rectangle2.f12372x, rectangle2.f12373y);
        } else {
            getDefaultIcon().paintIcon(jComponent, graphics, rectangle2.f12372x, rectangle2.f12373y);
        }
        if (strLayoutCompoundLabel != null) {
            View view = (View) jComponent.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, rectangle3);
            } else {
                int displayedMnemonicIndex = abstractButton.getDisplayedMnemonicIndex();
                if (model.isEnabled()) {
                    graphics.setColor(abstractButton.getForeground());
                } else {
                    graphics.setColor(getDisabledTextColor());
                }
                SwingUtilities2.drawStringUnderlineCharAt(jComponent, graphics, strLayoutCompoundLabel, displayedMnemonicIndex, rectangle3.f12372x, rectangle3.f12373y + fontMetrics.getAscent());
            }
            if (abstractButton.hasFocus() && abstractButton.isFocusPainted() && rectangle3.width > 0 && rectangle3.height > 0) {
                paintFocus(graphics, rectangle3, size);
            }
        }
    }

    @Override // javax.swing.plaf.basic.BasicRadioButtonUI
    protected void paintFocus(Graphics graphics, Rectangle rectangle, Dimension dimension) {
        graphics.setColor(getFocusColor());
        graphics.drawRect(rectangle.f12372x - 1, rectangle.f12373y - 1, rectangle.width + 1, rectangle.height + 1);
    }
}
