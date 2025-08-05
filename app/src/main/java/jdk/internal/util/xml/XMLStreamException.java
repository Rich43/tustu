package jdk.internal.util.xml;

/* loaded from: rt.jar:jdk/internal/util/xml/XMLStreamException.class */
public class XMLStreamException extends Exception {
    private static final long serialVersionUID = 1;
    protected Throwable nested;

    public XMLStreamException() {
    }

    public XMLStreamException(String str) {
        super(str);
    }

    public XMLStreamException(Throwable th) {
        super(th);
        this.nested = th;
    }

    public XMLStreamException(String str, Throwable th) {
        super(str, th);
        this.nested = th;
    }

    public Throwable getNestedException() {
        return this.nested;
    }
}
