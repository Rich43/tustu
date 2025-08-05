package javax.swing.plaf.metal;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;
import sun.awt.AppContext;
import sun.swing.SwingUtilities2;

/* loaded from: rt.jar:javax/swing/plaf/metal/MetalButtonUI.class */
public class MetalButtonUI extends BasicButtonUI {
    protected Color focusColor;
    protected Color selectColor;
    protected Color disabledTextColor;
    private static final Object METAL_BUTTON_UI_KEY = new Object();

    public static ComponentUI createUI(JComponent jComponent) {
        AppContext appContext = AppContext.getAppContext();
        MetalButtonUI metalButtonUI = (MetalButtonUI) appContext.get(METAL_BUTTON_UI_KEY);
        if (metalButtonUI == null) {
            metalButtonUI = new MetalButtonUI();
            appContext.put(METAL_BUTTON_UI_KEY, metalButtonUI);
        }
        return metalButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        super.installDefaults(abstractButton);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    public void uninstallDefaults(AbstractButton abstractButton) {
        super.uninstallDefaults(abstractButton);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected BasicButtonListener createButtonListener(AbstractButton abstractButton) {
        return super.createButtonListener(abstractButton);
    }

    protected Color getSelectColor() {
        this.selectColor = UIManager.getColor(getPropertyPrefix() + Constants.ATTRNAME_SELECT);
        return this.selectColor;
    }

    protected Color getDisabledTextColor() {
        this.disabledTextColor = UIManager.getColor(getPropertyPrefix() + "disabledText");
        return this.disabledTextColor;
    }

    protected Color getFocusColor() {
        this.focusColor = UIManager.getColor(getPropertyPrefix() + "focus");
        return this.focusColor;
    }

    @Override // javax.swing.plaf.ComponentUI
    public void update(Graphics graphics, JComponent jComponent) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        if ((jComponent.getBackground() instanceof UIResource) && abstractButton.isContentAreaFilled() && jComponent.isEnabled()) {
            ButtonModel model = abstractButton.getModel();
            if (!MetalUtils.isToolBarButton(jComponent)) {
                if (!model.isArmed() && !model.isPressed() && MetalUtils.drawGradient(jComponent, graphics, "Button.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight(), true)) {
                    paint(graphics, jComponent);
                    return;
                }
            } else if (model.isRollover() && MetalUtils.drawGradient(jComponent, graphics, "Button.gradient", 0, 0, jComponent.getWidth(), jComponent.getHeight(), true)) {
                paint(graphics, jComponent);
                return;
            }
        }
        super.update(graphics, jComponent);
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintButtonPressed(Graphics graphics, AbstractButton abstractButton) {
        if (abstractButton.isContentAreaFilled()) {
            Dimension size = abstractButton.getSize();
            graphics.setColor(getSelectColor());
            graphics.fillRect(0, 0, size.width, size.height);
        }
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

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected void paintText(Graphics graphics, JComponent jComponent, Rectangle rectangle, String str) {
        AbstractButton abstractButton = (AbstractButton) jComponent;
        ButtonModel model = abstractButton.getModel();
        FontMetrics fontMetrics = SwingUtilities2.getFontMetrics(jComponent, graphics);
        int displayedMnemonicIndex = abstractButton.getDisplayedMnemonicIndex();
        if (model.isEnabled()) {
            graphics.setColor(abstractButton.getForeground());
        } else {
            graphics.setColor(getDisabledTextColor());
        }
        SwingUtilities2.drawStringUnderlineCharAt(jComponent, graphics, str, displayedMnemonicIndex, rectangle.f12372x, rectangle.f12373y + fontMetrics.getAscent());
    }
}
