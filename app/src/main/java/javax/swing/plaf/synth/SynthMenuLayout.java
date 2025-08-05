package javax.swing.plaf.synth;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JPopupMenu;
import javax.swing.plaf.basic.DefaultMenuLayout;

/* loaded from: rt.jar:javax/swing/plaf/synth/SynthMenuLayout.class */
class SynthMenuLayout extends DefaultMenuLayout {
    public SynthMenuLayout(Container container, int i2) {
        super(container, i2);
    }

    @Override // javax.swing.plaf.basic.DefaultMenuLayout, javax.swing.BoxLayout, java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        if (container instanceof JPopupMenu) {
            ((JPopupMenu) container).putClientProperty(SynthMenuItemLayoutHelper.MAX_ACC_OR_ARROW_WIDTH, null);
        }
        return super.preferredLayoutSize(container);
    }
}
