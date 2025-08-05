package java.io;

/* loaded from: rt.jar:java/io/NotSerializableException.class */
public class NotSerializableException extends ObjectStreamException {
    private static final long serialVersionUID = 2906642554793891381L;

    public NotSerializableException(String str) {
        super(str);
    }

    public NotSerializableException() {
    }
}
