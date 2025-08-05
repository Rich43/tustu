package sun.misc;

import java.io.InvalidClassException;
import java.io.ObjectInputStream;

/* loaded from: rt.jar:sun/misc/JavaOISAccess.class */
public interface JavaOISAccess {
    void setObjectInputFilter(ObjectInputStream objectInputStream, ObjectInputFilter objectInputFilter);

    ObjectInputFilter getObjectInputFilter(ObjectInputStream objectInputStream);

    void checkArray(ObjectInputStream objectInputStream, Class<?> cls, int i2) throws InvalidClassException;
}
