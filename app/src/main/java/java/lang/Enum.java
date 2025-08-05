package java.lang;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.Enum;

/* loaded from: rt.jar:java/lang/Enum.class */
public abstract class Enum<E extends Enum<E>> implements Comparable<E>, Serializable {
    private final String name;
    private final int ordinal;

    public final String name() {
        return this.name;
    }

    public final int ordinal() {
        return this.ordinal;
    }

    protected Enum(String str, int i2) {
        this.name = str;
        this.ordinal = i2;
    }

    public String toString() {
        return this.name;
    }

    public final boolean equals(Object obj) {
        return this == obj;
    }

    public final int hashCode() {
        return super.hashCode();
    }

    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    @Override // java.lang.Comparable
    public final int compareTo(E e2) {
        if (getClass() == e2.getClass() || getDeclaringClass() == e2.getDeclaringClass()) {
            return this.ordinal - e2.ordinal;
        }
        throw new ClassCastException();
    }

    public final Class<E> getDeclaringClass() {
        Class<E> cls = (Class<E>) getClass();
        Class superclass = cls.getSuperclass();
        return superclass == Enum.class ? cls : superclass;
    }

    public static <T extends Enum<T>> T valueOf(Class<T> cls, String str) {
        T t2 = cls.enumConstantDirectory().get(str);
        if (t2 != null) {
            return t2;
        }
        if (str == null) {
            throw new NullPointerException("Name is null");
        }
        throw new IllegalArgumentException("No enum constant " + cls.getCanonicalName() + "." + str);
    }

    protected final void finalize() {
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        throw new InvalidObjectException("can't deserialize enum");
    }

    private void readObjectNoData() throws ObjectStreamException {
        throw new InvalidObjectException("can't deserialize enum");
    }
}
