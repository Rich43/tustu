package javax.swing.event;

import java.util.EventListener;

/* loaded from: rt.jar:javax/swing/event/DocumentListener.class */
public interface DocumentListener extends EventListener {
    void insertUpdate(DocumentEvent documentEvent);

    void removeUpdate(DocumentEvent documentEvent);

    void changedUpdate(DocumentEvent documentEvent);
}
