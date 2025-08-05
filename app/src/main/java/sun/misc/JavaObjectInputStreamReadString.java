package sun.misc;

import java.io.IOException;
import java.io.ObjectInputStream;

@FunctionalInterface
/* loaded from: rt.jar:sun/misc/JavaObjectInputStreamReadString.class */
public interface JavaObjectInputStreamReadString {
    String readString(ObjectInputStream objectInputStream) throws IOException;
}
