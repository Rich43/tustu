package java.io;

/* loaded from: rt.jar:java/io/Externalizable.class */
public interface Externalizable extends Serializable {
    void writeExternal(ObjectOutput objectOutput) throws IOException;

    void readExternal(ObjectInput objectInput) throws IOException, ClassNotFoundException;
}
