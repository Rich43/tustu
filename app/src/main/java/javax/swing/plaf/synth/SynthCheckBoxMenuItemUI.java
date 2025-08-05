package javax.swing.plaf.synth;

import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthCheckBoxMenuItemUI.class */
public class SynthCheckBoxMenuItemUI extends SynthMenuItemUI {
    public static ComponentUI createUI(JComponent jComponent) {
        return new SynthCheckBoxMenuItemUI();
    }

    @Override // javax.swing.plaf.basic.BasicMenuItemUI
    protected String getPropertyPrefix() {
        return "CheckBoxMenuItem";
    }

    @Override // javax.swing.plaf.synth.SynthMenuItemUI
    void paintBackground(SynthContext synthContext, Graphics graphics, JComponent jComponent) {
        synthContext.getPainter().paintCheckBoxMenuItemBackground(synthContext, graphics, 0, 0, jComponent.getWidth(), jComponent.getHeight());
    }

    @Override // javax.swing.plaf.synth.SynthMenuItemUI, javax.swing.plaf.synth.SynthUI
    public void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        synthContext.getPainter().paintCheckBoxMenuItemBorder(synthContext, graphics, i2, i3, i4, i5);
    }
}
