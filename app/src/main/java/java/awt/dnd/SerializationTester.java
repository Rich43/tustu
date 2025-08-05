package java.awt.dnd;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

/* loaded from: rt.jar:java/awt/dnd/SerializationTester.class */
final class SerializationTester {
    private static ObjectOutputStream stream;

    static {
        try {
            stream = new ObjectOutputStream(new OutputStream() { // from class: java.awt.dnd.SerializationTester.1
                @Override // java.io.OutputStream
                public void write(int i2) {
                }
            });
        } catch (IOException e2) {
        }
    }

    static boolean test(Object obj) {
        try {
            if (!(obj instanceof Serializable)) {
                return false;
            }
            try {
                stream.writeObject(obj);
                try {
                    stream.reset();
                    return true;
                } catch (IOException e2) {
                    return true;
                }
            } catch (IOException e3) {
                return false;
            }
        } finally {
            try {
                stream.reset();
            } catch (IOException e4) {
            }
        }
    }

    private SerializationTester() {
    }
}
