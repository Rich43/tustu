package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.math.BigInteger;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dom.DOMCryptoContext;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMCryptoBinary.class */
public final class DOMCryptoBinary extends DOMStructure {
    private final BigInteger bigNum;
    private final String value;

    public DOMCryptoBinary(BigInteger bigInteger) {
        if (bigInteger == null) {
            throw new NullPointerException("bigNum is null");
        }
        this.bigNum = bigInteger;
        this.value = XMLUtils.encodeToString(XMLUtils.getBytes(bigInteger, bigInteger.bitLength()));
    }

    public DOMCryptoBinary(Node node) throws MarshalException {
        this.value = node.getNodeValue();
        try {
            this.bigNum = new BigInteger(1, XMLUtils.decode(((Text) node).getData()));
        } catch (Exception e2) {
            throw new MarshalException(e2);
        }
    }

    public BigInteger getBigNum() {
        return this.bigNum;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        node.appendChild(DOMUtils.getOwnerDocument(node).createTextNode(this.value));
    }
}
