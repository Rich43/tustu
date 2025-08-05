package com.sun.java.swing.plaf.windows;

import com.sun.java.swing.plaf.windows.TMSchema;
import javax.swing.JMenuItem;

/* loaded from: rt.jar:com/sun/java/swing/plaf/windows/WindowsMenuItemUIAccessor.class */
interface WindowsMenuItemUIAccessor {
    JMenuItem getMenuItem();

    TMSchema.State getState(JMenuItem jMenuItem);

    TMSchema.Part getPart(JMenuItem jMenuItem);
}
