package sun.awt;

import java.awt.SecondaryLoop;

/* loaded from: rt.jar:sun/awt/FwDispatcher.class */
public interface FwDispatcher {
    boolean isDispatchThread();

    void scheduleDispatch(Runnable runnable);

    SecondaryLoop createSecondaryLoop();
}
