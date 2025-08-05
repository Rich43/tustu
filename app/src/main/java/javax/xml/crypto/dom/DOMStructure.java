package javax.xml.crypto.dom;

import javax.xml.crypto.XMLStructure;
import org.w3c.dom.Node;

/* loaded from: rt.jar:javax/xml/crypto/dom/DOMStructure.class */
public class DOMStructure implements XMLStructure {
    private final Node node;

    public DOMStructure(Node node) {
        if (node == null) {
            throw new NullPointerException("node cannot be null");
        }
        this.node = node;
    }

    public Node getNode() {
        return this.node;
    }

    @Override // javax.xml.crypto.XMLStructure
    public boolean isFeatureSupported(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        return false;
    }
}
