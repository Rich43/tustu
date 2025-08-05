package java.awt.event;

import java.util.EventListener;

/* loaded from: rt.jar:java/awt/event/InputMethodListener.class */
public interface InputMethodListener extends EventListener {
    void inputMethodTextChanged(InputMethodEvent inputMethodEvent);

    void caretPositionChanged(InputMethodEvent inputMethodEvent);
}
