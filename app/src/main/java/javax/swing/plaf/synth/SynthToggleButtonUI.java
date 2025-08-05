package javax.swing.plaf.synth;

import java.awt.Graphics;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthToggleButtonUI.class */
public class SynthToggleButtonUI extends SynthButtonUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthToggleButtonUI();
    }

    @Override // javax.swing.plaf.basic.BasicButtonUI
    protected String getPropertyPrefix() {
        return "ToggleButton.";
    }

    @Override // javax.swing.plaf.synth.SynthButtonUI
    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        if (((AbstractButton) jComponent).isContentAreaFilled()) {
            synthContext.getPainter().paintToggleButtonBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
        }
    }

    @Override // javax.swing.plaf.synth.SynthButtonUI, javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintToggleButtonBorder(synthContext, graphics, i2, i3, i4, i5);
    }
}
