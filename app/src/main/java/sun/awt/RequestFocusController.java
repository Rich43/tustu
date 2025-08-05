package sun.awt;

import java.awt.Component;
import sun.awt.CausedFocusEvent;

/* loaded from: rt.jar:sun/awt/RequestFocusController.class */
public interface RequestFocusController {
    boolean acceptRequestFocus(Component component, Component component2, boolean z2, boolean z3, CausedFocusEvent.Cause cause);
}
