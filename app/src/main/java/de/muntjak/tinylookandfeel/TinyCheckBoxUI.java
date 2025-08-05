package de.muntjak.tinylookandfeel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalCheckBoxUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyCheckBoxUI.class */
public class TinyCheckBoxUI extends MetalCheckBoxUI {
    private static final TinyCheckBoxUI SHARED_INSTANCE = new TinyCheckBoxUI();
    private static final TinyCheckBoxIcon checkIcon = new TinyCheckBoxIcon();
    private static final BasicStroke focusStroke = new BasicStroke(1.0f, 0, 2, 1.0f, new float[]{1.0f}, 1.0f);

    public static ComponentUI createUI(JComponent jComponent) {
        return SHARED_INSTANCE;
    }

    @Override // javax.swing.plaf.metal.MetalCheckBoxUI, javax.swing.plaf.metal.MetalRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI, javax.swing.plaf.basic.BasicButtonUI
    public void installDefaults(AbstractButton abstractButton) {
        InputMap inputMap;
        super.installDefaults(abstractButton);
        this.icon = checkIcon;
        abstractButton.setRolloverEnabled(true);
        if (Theme.buttonEnter.getValue() && abstractButton.isFocusable() && (inputMap = (InputMap) UIManager.get(new StringBuffer().append(getPropertyPrefix()).append("focusInputMap").toString())) != null) {
            inputMap.put(KeyStroke.getKeyStroke(10, 0, false), "pressed");
            inputMap.put(KeyStroke.getKeyStroke(10, 0, true), "released");
        }
    }

    @Override // javax.swing.plaf.metal.MetalRadioButtonUI, javax.swing.plaf.basic.BasicRadioButtonUI
    protected void paintFocus(Graphics graphics, Rectangle rectangle, Dimension dimension) {
        if (Theme.buttonFocus.getValue()) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            graphics2D.setColor(Color.black);
            graphics2D.setStroke(focusStroke);
            int i2 = rectangle.f12372x - 1;
            int i3 = rectangle.f12373y - 1;
            int i4 = i2 + rectangle.width + 1;
            int i5 = i3 + rectangle.height + 1;
            graphics2D.drawLine(i2, i3, i4, i3);
            graphics2D.drawLine(i2, i3, i2, i5);
            graphics2D.drawLine(i2, i5, i4, i5);
            graphics2D.drawLine(i4, i3, i4, i5);
        }
    }
}
