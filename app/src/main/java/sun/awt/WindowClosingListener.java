package sun.awt;

import java.awt.event.WindowEvent;

/* loaded from: rt.jar:sun/awt/WindowClosingListener.class */
public interface WindowClosingListener {
    RuntimeException windowClosingNotify(WindowEvent windowEvent);

    RuntimeException windowClosingDelivered(WindowEvent windowEvent);
}
