package javax.swing.plaf.metal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicToggleButtonUI;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalToggleButtonUI.class */
public class MetalToggleButtonUI extends BasicToggleButtonUI {
    private static final Object METAL_TOGGLE_BUTTON_UI_KEY = new Object();
    protected Color focusColor;
    protected Color selectColor;
    protected Color disabledTextColor;
    private boolean defaults_initialized = false;

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MetalToggleButtonUI metalToggleButtonUI = (MetalToggleButtonUI) appContext.get(METAL_TOGGLE_BUTTON_UI_KEY);
        if (metalToggleButtonUI == null) {
            metalToggleButtonUI = new MetalToggleButtonUI();
            appContext.put(METAL_TOGGLE_BUTTON_UI_KEY, metalToggleButtonUI);
        }
        return metalToggleButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
        if (!this.defaults_initialized) {
            this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
            this.selectColor = UIManager.getColor(getPropertyPrefix() + Constants.ATTRNAME_SELECT);
            this.disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");
            this.defaults_initialized = true;
        }
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
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

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        if ((jComponent.getBackground() instanceof UIResource) && abstractButton.isContentAreaFilled() && jComponent.isEnabled()) {
            ButtonModel model = abstractButton.getModel();
            if (!MetalUtils.isToolBarButton(jComponent)) {
                if (!model.isArmed() && !model.isPressed() && MetalUtils.drawGradient(jComponent, graphics, "ToggleButton.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight(), true)) {
                    paint(graphics, jComponent);
                    return;
                }
            } else if ((model.isRollover() || model.isSelected()) && MetalUtils.drawGradient(jComponent, graphics, "ToggleButton.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight(), true)) {
                paint(graphics, jComponent);
                return;
            }
        }
        super.update(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        if (abstractButton.isContentAreaFilled()) {
            graphics.setColor(getSelectColor());
            graphics.fillRect(0, 0, abstractButton.getWidth(), abstractButton.getHeight());
        }
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintText(Graphics graphics, JComponent jComponent, Rectangle rectangle, String str) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(abstractButton, graphics);
        int displayedMnemonicIndex = abstractButton.getDisplayedMnemonicIndex();
        if (model.isEnabled()) {
            graphics.setColor(abstractButton.getForeground());
        } else if (model.isSelected()) {
            graphics.setColor(jComponent.getBackground());
        } else {
            graphics.setColor(getDisabledTextColor());
        }
        SwingUtilities2.drawStringUnderlineCharAt(jComponent, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintFocus(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle, Rectangle rectangle2, Rectangle rectangle3) {
        Rectangle rectangle4 = new Rectangle();
        String text = abstractButton.getText();
        boolean z2 = abstractButton.getIcon() != null;
        if (text != null && !text.equals("")) {
            if (!z2) {
                rectangle4.setBounds(rectangle2);
            } else {
                rectangle4.setBounds(rectangle3.union(rectangle2));
            }
        } else if (z2) {
            rectangle4.setBounds(rectangle3);
        }
        graphics.setColor(getFocusColor());
        graphics.drawRect(rectangle4.f12372x - 1, rectangle4.f12373y - 1, rectangle4.width + 1, rectangle4.height + 1);
    }

    @Override // javax.swing.plaf.basic.BasicToggleButtonUI
    protected void paintIcon(Graphics graphics, AbstractButton abstractButton, Rectangle rectangle) {
        super.paintIcon(graphics, abstractButton, rectangle);
    }
}
