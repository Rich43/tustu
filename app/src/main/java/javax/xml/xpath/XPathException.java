package javax.xml.xpath;

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamField;
import java.io.PrintStream;
import java.io.PrintWriter;

/* loaded from: rt.jar:javax/xml/xpath/XPathException.class */
public class XPathException extends Exception {
    private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("cause", Throwable.class)};
    private static final long serialVersionUID = -1837080260374986980L;

    public XPathException(String message) {
        super(message);
        if (message == null) {
            throw new NullPointerException("message can't be null");
        }
    }

    public XPathException(Throwable cause) {
        super(cause);
        if (cause == null) {
            throw new NullPointerException("cause can't be null");
        }
    }

    @Override // java.lang.Throwable
    public Throwable getCause() {
        return super.getCause();
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        ObjectOutputStream.PutField fields = out.putFields();
        fields.put("cause", super.getCause());
        out.writeFields();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField fields = in.readFields();
        Throwable scause = (Throwable) fields.get("cause", (Object) null);
        if (super.getCause() == null && scause != null) {
            try {
                super.initCause(scause);
            } catch (IllegalStateException e2) {
                throw new InvalidClassException("Inconsistent state: two causes");
            }
        }
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintStream s2) {
        if (getCause() != null) {
            getCause().printStackTrace(s2);
            s2.println("--------------- linked to ------------------");
        }
        super.printStackTrace(s2);
    }

    @Override // java.lang.Throwable
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    @Override // java.lang.Throwable
    public void printStackTrace(PrintWriter s2) {
        if (getCause() != null) {
            getCause().printStackTrace(s2);
            s2.println("--------------- linked to ------------------");
        }
        super.printStackTrace(s2);
    }
}
