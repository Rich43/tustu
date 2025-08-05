package javax.imageio.metadata;

import javax.imageio.IIOException;
import org.w3c.dom.Node;

/* loaded from: rt.jar:javax/imageio/metadata/IIOInvalidTreeException.class */
public class IIOInvalidTreeException extends IIOException {
    protected Node offendingNode;

    public IIOInvalidTreeException(String str, Node node) {
        super(str);
        this.offendingNode = null;
        this.offendingNode = node;
    }

    public IIOInvalidTreeException(String str, Throwable th, Node node) {
        super(str, th);
        this.offendingNode = null;
        this.offendingNode = node;
    }

    public Node getOffendingNode() {
        return this.offendingNode;
    }
}
