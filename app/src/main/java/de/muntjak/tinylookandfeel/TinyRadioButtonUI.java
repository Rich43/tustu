package de.muntjak.tinylookandfeel;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.metal.MetalRadioButtonUI;

/* loaded from: tinylaf.jar:de/muntjak/tinylookandfeel/TinyRadioButtonUI.class */
public class TinyRadioButtonUI extends MetalRadioButtonUI {
    private static final TinyRadioButtonUI radioButtonUI = new TinyRadioButtonUI();
    private static BasicStroke focusStroke = new BasicStroke(1.0f, 0, 2, 1.0f, new float[]{1.0f, 1.0f}, 0.0f);
    private static TinyRadioButtonIcon radioButton;

    public static ComponentUI createUI(JComponent jComponent) {
        if (jComponent instanceof JRadioButton) {
            ((JRadioButton) jComponent).setRolloverEnabled(true);
        }
        return radioButtonUI;
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI, javax.swing.plaf.ComponentUI
    public void installUI(JComponent jComponent) {
        InputMap inputMap;
        super.installUI(jComponent);
        this.icon = getRadioButton();
        if (Theme.buttonEnter.getValue() && jComponent.isFocusable() && (inputMap = (InputMap) UIManager.get(new StringBuffer().append(getPropertyPrefix()).append("focusInputMap").toString())) != null) {
            inputMap.put(KeyStroke.getKeyStroke(10, 0, false), "pressed");
            inputMap.put(KeyStroke.getKeyStroke(10, 0, true), "released");
        }
    }

    protected TinyRadioButtonIcon getRadioButton() {
        if (radioButton == null) {
            radioButton = new TinyRadioButtonIcon();
        }
        return radioButton;
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
