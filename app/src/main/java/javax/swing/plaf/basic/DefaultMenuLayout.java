package javax.swing.plaf.basic;

import java.awt.Container;
import java.awt.Dimension;
import javax.swing.BoxLayout;
import javax.swing.JPopupMenu;
import javax.swing.plaf.UIResource;
import sun.swing.MenuItemLayoutHelper;

/* loaded from: rt.jar:javax/swing/plaf/basic/DefaultMenuLayout.class */
public class DefaultMenuLayout extends BoxLayout implements UIResource {
    public DefaultMenuLayout(Container container, int i2) {
        super(container, i2);
    }

    @Override // javax.swing.BoxLayout, java.awt.LayoutManager
    public Dimension preferredLayoutSize(Container container) {
        if (container instanceof JPopupMenu) {
            JPopupMenu jPopupMenu = (JPopupMenu) container;
            MenuItemLayoutHelper.clearUsedClientProperties(jPopupMenu);
            if (jPopupMenu.getComponentCount() == 0) {
                return new Dimension(0, 0);
            }
        }
        super.invalidateLayout(container);
        return super.preferredLayoutSize(container);
    }
}
