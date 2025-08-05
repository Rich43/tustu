package javax.swing.plaf.synth;

import java.awt.Graphics;
import javax.swing.JComponent;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthUI.class */
public interface SynthUI extends SynthConstants {
    SynthContext getContext(JComponent jComponent);

    void paintBorder(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5);
}
