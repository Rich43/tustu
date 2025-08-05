package sun.swing;

import javax.swing.Icon;
import javax.swing.JMenuItem;

/* loaded from: rt.jar:sun/swing/MenuItemCheckIconFactory.class */
public interface MenuItemCheckIconFactory {
    Icon getIcon(JMenuItem jMenuItem);

    boolean isCompatible(Object obj, String str);
}
