package javafx.scene.input;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventTarget;
import javafx.event.EventType;

/* loaded from: jfxrt.jar:javafx/scene/input/InputMethodEvent.class */
public final class InputMethodEvent extends InputEvent {
    private static final long serialVersionUID = 20121107;
    public static final EventType<InputMethodEvent> INPUT_METHOD_TEXT_CHANGED = new EventType<>(InputEvent.ANY, "INPUT_METHOD_TEXT_CHANGED");
    public static final EventType<InputMethodEvent> ANY = INPUT_METHOD_TEXT_CHANGED;
    private transient ObservableList<InputMethodTextRun> composed;
    private final String committed;
    private final int caretPosition;

    public InputMethodEvent(@NamedArg("source") Object source, @NamedArg("target") EventTarget target, @NamedArg("eventType") EventType<InputMethodEvent> eventType, @NamedArg("composed") List<InputMethodTextRun> composed, @NamedArg("committed") String committed, @NamedArg("caretPosition") int caretPosition) {
        super(source, target, eventType);
        this.composed = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(composed));
        this.committed = committed;
        this.caretPosition = caretPosition;
    }

    public InputMethodEvent(@NamedArg("eventType") EventType<InputMethodEvent> eventType, @NamedArg("composed") List<InputMethodTextRun> composed, @NamedArg("committed") String committed, @NamedArg("caretPosition") int caretPosition) {
        this(null, null, eventType, composed, committed, caretPosition);
    }

    public final ObservableList<InputMethodTextRun> getComposed() {
        return this.composed;
    }

    public final String getCommitted() {
        return this.committed;
    }

    public final int getCaretPosition() {
        return this.caretPosition;
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder("InputMethodEvent [");
        sb.append("source = ").append(getSource());
        sb.append(", target = ").append((Object) getTarget());
        sb.append(", eventType = ").append((Object) getEventType());
        sb.append(", consumed = ").append(isConsumed());
        sb.append(", composed = ").append((Object) getComposed());
        sb.append(", committed = ").append(getCommitted());
        sb.append(", caretPosition = ").append(getCaretPosition());
        return sb.append("]").toString();
    }

    @Override // javafx.event.Event
    public InputMethodEvent copyFor(Object newSource, EventTarget newTarget) {
        return (InputMethodEvent) super.copyFor(newSource, newTarget);
    }

    @Override // javafx.scene.input.InputEvent, javafx.event.Event
    public EventType<InputMethodEvent> getEventType() {
        return super.getEventType();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(new ArrayList(this.composed));
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        ArrayList<InputMethodTextRun> o2 = (ArrayList) ois.readObject();
        this.composed = FXCollections.unmodifiableObservableList(FXCollections.observableArrayList(o2));
    }
}
