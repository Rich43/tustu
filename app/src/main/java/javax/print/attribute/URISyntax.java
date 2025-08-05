package javax.print.attribute;

import java.io.Serializable;
import java.net.URI;

/* loaded from: rt.jar:javax/print/attribute/URISyntax.class */
public abstract class URISyntax implements Serializable, Cloneable {
    private static final long serialVersionUID = -7842661210486401678L;
    private URI uri;

    protected URISyntax(URI uri) {
        this.uri = verify(uri);
    }

    private static URI verify(URI uri) {
        if (uri == null) {
            throw new NullPointerException(" uri is null");
        }
        return uri;
    }

    public URI getURI() {
        return this.uri;
    }

    public int hashCode() {
        return this.uri.hashCode();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof URISyntax) && this.uri.equals(((URISyntax) obj).uri);
    }

    public String toString() {
        return this.uri.toString();
    }
}
