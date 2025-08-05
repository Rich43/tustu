package javax.swing.plaf.synth;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthRadioButtonMenuItemUI.class */
public class SynthRadioButtonMenuItemUI extends SynthMenuItemUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthRadioButtonMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected String getPropertyPrefix() {
        return "RadioButtonMenuItem";
    }

    @Override // javax.swing.plaf.synth.SynthMenuItemUI
    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintRadioButtonMenuItemBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.synth.SynthMenuItemUI, javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintRadioButtonMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
    }
}
