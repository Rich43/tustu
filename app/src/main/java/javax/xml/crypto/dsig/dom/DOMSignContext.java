package javax.xml.crypto.dsig.dom;

import java.security.Key;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.XMLSignContext;
import org.w3c.dom.Node;

/* loaded from: rt.jar:javax/xml/crypto/dsig/dom/DOMSignContext.class */
public class DOMSignContext extends DOMCryptoContext implements XMLSignContext {
    private Node parent;
    private Node nextSibling;

    public DOMSignContext(Key key, Node node) {
        if (key == null) {
            throw new NullPointerException("signingKey cannot be null");
        }
        if (node == null) {
            throw new NullPointerException("parent cannot be null");
        }
        setKeySelector(KeySelector.singletonKeySelector(key));
        this.parent = node;
    }

    public DOMSignContext(Key key, Node node, Node node2) {
        if (key == null) {
            throw new NullPointerException("signingKey cannot be null");
        }
        if (node == null) {
            throw new NullPointerException("parent cannot be null");
        }
        if (node2 == null) {
            throw new NullPointerException("nextSibling cannot be null");
        }
        setKeySelector(KeySelector.singletonKeySelector(key));
        this.parent = node;
        this.nextSibling = node2;
    }

    public DOMSignContext(KeySelector keySelector, Node node) {
        if (keySelector == null) {
            throw new NullPointerException("key selector cannot be null");
        }
        if (node == null) {
            throw new NullPointerException("parent cannot be null");
        }
        setKeySelector(keySelector);
        this.parent = node;
    }

    public DOMSignContext(KeySelector keySelector, Node node, Node node2) {
        if (keySelector == null) {
            throw new NullPointerException("key selector cannot be null");
        }
        if (node == null) {
            throw new NullPointerException("parent cannot be null");
        }
        if (node2 == null) {
            throw new NullPointerException("nextSibling cannot be null");
        }
        setKeySelector(keySelector);
        this.parent = node;
        this.nextSibling = node2;
    }

    public void setParent(Node node) {
        if (node == null) {
            throw new NullPointerException("parent is null");
        }
        this.parent = node;
    }

    public void setNextSibling(Node node) {
        this.nextSibling = node;
    }

    public Node getParent() {
        return this.parent;
    }

    public Node getNextSibling() {
        return this.nextSibling;
    }
}
