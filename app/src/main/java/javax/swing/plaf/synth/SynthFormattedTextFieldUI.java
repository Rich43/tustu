package javax.swing.plaf.synth;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthFormattedTextFieldUI.class */
public class SynthFormattedTextFieldUI extends SynthTextFieldUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthFormattedTextFieldUI();
    }

    @Override // javax.swing.plaf.basic.BasicTextFieldUI, javax.swing.plaf.basic.BasicTextUI
    protected String getPropertyPrefix() {
        return "FormattedTextField";
    }

    @Override // javax.swing.plaf.synth.SynthTextFieldUI
    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintFormattedTextFieldBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.synth.SynthTextFieldUI, javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintFormattedTextFieldBorder(synthContext, graphics, i2, i3, i4, i5);
    }
}
