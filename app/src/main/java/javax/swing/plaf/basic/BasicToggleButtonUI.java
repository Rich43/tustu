package javax.swing.plaf.basic;

import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ComponentUI;
import javax.swing.text.View;
import sun.awt.AppContext;

/* loaded from: rt.jar:javax/swing/plaf/basic/BasicToggleButtonUI.class */
public class BasicToggleButtonUI extends BasicButtonUI {
    private static final Object BASIC_TOGGLE_BUTTON_UI_KEY = new Object();
    private static final String propertyPrefix = "ToggleButton.";

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        BasicToggleButtonUI basicToggleButtonUI = (BasicToggleButtonUI) appContext.get(BASIC_TOGGLE_BUTTON_UI_KEY);
        if (basicToggleButtonUI == null) {
            basicToggleButtonUI = new BasicToggleButtonUI();
            appContext.put(BASIC_TOGGLE_BUTTON_UI_KEY, basicToggleButtonUI);
        }
        return basicToggleButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected String getPropertyPrefix() {
        return propertyPrefix;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void paint(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        Dimension size = abstractButton.getSize();
        FontMetrics fontMetrics = graphics.getFontMetrics();
        Insets insets = jComponent.getInsets();
        Rectangle rectangle = new Rectangle(size);
        rectangle.f12372x += insets.left;
        rectangle.f12373y += insets.top;
        rectangle.width -= insets.right + rectangle.f12372x;
        rectangle.height -= insets.bottom + rectangle.f12373y;
        Rectangle rectangle2 = new Rectangle();
        Rectangle rectangle3 = new Rectangle();
        graphics.setFont(jComponent.getFont());
        String strLayoutCompoundLabel = SwingUtilities.layoutCompoundLabel(jComponent, fontMetrics, abstractButton.getText(), abstractButton.getIcon(), abstractButton.getVerticalAlignment(), abstractButton.getHorizontalAlignment(), abstractButton.getVerticalTextPosition(), abstractButton.getHorizontalTextPosition(), rectangle, rectangle2, rectangle3, abstractButton.getText() == null ? 0 : abstractButton.getIconTextGap());
        graphics.setColor(abstractButton.getBackground());
        if ((model.isArmed() && model.isPressed()) || model.isSelected()) {
            paintButtonPressed(graphics, abstractButton);
        }
        if (abstractButton.getIcon() != null) {
            paintIcon(graphics, abstractButton, rectangle2);
        }
        if (strLayoutCompoundLabel != null && !strLayoutCompoundLabel.equals("")) {
            View view = (View) jComponent.getClientProperty("html");
            if (view != null) {
                view.paint(graphics, rectangle3);
            } else {
                paintText(graphics, abstractButton, rectangle3, strLayoutCompoundLabel);
            }
        }
        if (abstractButton.isFocusPainted() && abstractButton.hasFocus()) {
            paintFocus(graphics, abstractButton, rectangle, rectangle3, rectangle2);
        }
    }

    protected void paintIcon(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle) {
        ButtonModel model = abstractButton.getModel();
        Icon rolloverIcon = null;
        if (!model.isEnabled()) {
            rolloverIcon = model.isSelected() ? abstractButton.getDisabledSelectedIcon() : abstractButton.getDisabledIcon();
        } else if (model.isPressed() && model.isArmed()) {
            rolloverIcon = abstractButton.getPressedIcon();
            if (rolloverIcon == null) {
                rolloverIcon = abstractButton.getSelectedIcon();
            }
        } else if (model.isSelected()) {
            if (abstractButton.isRolloverEnabled() && model.isRollover()) {
                rolloverIcon = abstractButton.getRolloverSelectedIcon();
                if (rolloverIcon == null) {
                    rolloverIcon = abstractButton.getSelectedIcon();
                }
            } else {
                rolloverIcon = abstractButton.getSelectedIcon();
            }
        } else if (abstractButton.isRolloverEnabled() && model.isRollover()) {
            rolloverIcon = abstractButton.getRolloverIcon();
        }
        if (rolloverIcon == null) {
            rolloverIcon = abstractButton.getIcon();
        }
        rolloverIcon.paintIcon(abstractButton, graphics, rectangle.f12372x, rectangle.f12373y);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected int getTextShiftOffset() {
        return 0;
    }
}
