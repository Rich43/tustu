package java.util.prefs;

import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EventObject;

/* loaded from: rt.jar:java/util/prefs/NodeChangeEvent.class */
public class NodeChangeEvent extends EventObject {
    private Preferences child;
    private static final long serialVersionUID = 8068949086596572957L;

    public NodeChangeEvent(Preferences preferences, Preferences preferences2) {
        super(preferences);
        this.child = preferences2;
    }

    public Preferences getParent() {
        return (Preferences) getSource();
    }

    public Preferences getChild() {
        return this.child;
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws NotSerializableException {
        throw new NotSerializableException("Not serializable.");
    }

    private void readObject(ObjectInputStream objectInputStream) throws NotSerializableException {
        throw new NotSerializableException("Not serializable.");
    }
}
