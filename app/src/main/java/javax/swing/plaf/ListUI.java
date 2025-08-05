package javax.swing.plaf;

import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JList;

/* loaded from: rt.jar:javax/swing/plaf/ListUI.class */
public abstract class ListUI extends ComponentUI {
    public abstract int locationToIndex(JList jList, Point point);

    public abstract Point indexToLocation(JList jList, int i2);

    public abstract Rectangle getCellBounds(JList jList, int i2, int i3);
}
