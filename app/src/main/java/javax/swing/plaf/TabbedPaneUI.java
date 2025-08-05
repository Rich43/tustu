package javax.swing.plaf;

import java.awt.Rectangle;
import javax.swing.JTabbedPane;

/* loaded from: rt.jar:javax/swing/plaf/TabbedPaneUI.class */
public abstract class TabbedPaneUI extends ComponentUI {
    public abstract int tabForCoordinate(JTabbedPane jTabbedPane, int i2, int i3);

    public abstract Rectangle getTabBounds(JTabbedPane jTabbedPane, int i2);

    public abstract int getTabRunCount(JTabbedPane jTabbedPane);
}
