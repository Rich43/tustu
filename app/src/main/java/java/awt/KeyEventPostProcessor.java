package java.awt;

import java.awt.event.KeyEvent;

@FunctionalInterface
/* loaded from: rt.jar:java/awt/KeyEventPostProcessor.class */
public interface KeyEventPostProcessor {
    boolean postProcessKeyEvent(KeyEvent keyEvent);
}
