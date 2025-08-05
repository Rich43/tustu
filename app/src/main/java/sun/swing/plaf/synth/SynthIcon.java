package sun.swing.plaf.synth;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.plaf.synth.SynthContext;

/* loaded from: rt.jar:sun/swing/plaf/synth/SynthIcon.class */
public abstract class SynthIcon implements Icon {
    public abstract void paintIcon(SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5);

    public abstract int getIconWidth(SynthContext synthContext);

    public abstract int getIconHeight(SynthContext synthContext);

    public static int getIconWidth(Icon icon, SynthContext synthContext) {
        if (icon == null) {
            return 0;
        }
        if (icon instanceof SynthIcon) {
            return ((SynthIcon) icon).getIconWidth(synthContext);
        }
        return icon.getIconWidth();
    }

    public static int getIconHeight(Icon icon, SynthContext synthContext) {
        if (icon == null) {
            return 0;
        }
        if (icon instanceof SynthIcon) {
            return ((SynthIcon) icon).getIconHeight(synthContext);
        }
        return icon.getIconHeight();
    }

    public static void paintIcon(Icon icon, SynthContext synthContext, Graphics graphics, int i2, int i3, int i4, int i5) {
        if (icon instanceof SynthIcon) {
            ((SynthIcon) icon).paintIcon(synthContext, graphics, i2, i3, i4, i5);
        } else if (icon != null) {
            icon.paintIcon(synthContext.getComponent(), graphics, i2, i3);
        }
    }

    @Override // javax.swing.Icon
    public void paintIcon(Component component, Graphics graphics, int i2, int i3) {
        paintIcon(null, graphics, i2, i3, 0, 0);
    }

    @Override // javax.swing.Icon
    public int getIconWidth() {
        return getIconWidth(null);
    }

    @Override // javax.swing.Icon
    public int getIconHeight() {
        return getIconHeight(null);
    }
}
