package javax.xml.crypto.dsig.dom;

import java.security.Key;
import javax.xml.crypto.KeySelector;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.XMLValidateContext;
import org.w3c.dom.Node;

/* loaded from: rt.jar:javax/xml/crypto/dsig/dom/DOMValidateContext.class */
public class DOMValidateContext extends DOMCryptoContext implements XMLValidateContext {
    private Node node;

    public DOMValidateContext(KeySelector keySelector, Node node) {
        if (keySelector == null) {
            throw new NullPointerException("key selector is null");
        }
        init(node, keySelector);
    }

    public DOMValidateContext(Key key, Node node) {
        if (key == null) {
            throw new NullPointerException("validatingKey is null");
        }
        init(node, KeySelector.singletonKeySelector(key));
    }

    private void init(Node node, KeySelector keySelector) {
        if (node == null) {
            throw new NullPointerException("node is null");
        }
        this.node = node;
        super.setKeySelector(keySelector);
        if (System.getSecurityManager() != null) {
            super.setProperty("org.jcp.xml.dsig.secureValidation", Boolean.TRUE);
        }
    }

    public void setNode(Node node) {
        if (node == null) {
            throw new NullPointerException();
        }
        this.node = node;
    }

    public Node getNode() {
        return this.node;
    }
}
