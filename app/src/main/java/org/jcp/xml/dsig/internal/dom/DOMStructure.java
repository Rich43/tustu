package org.jcp.xml.dsig.internal.dom;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMStructure.class */
public abstract class DOMStructure implements XMLStructure {
    public abstract void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException;

    @Override // javax.xml.crypto.XMLStructure
    public final boolean isFeatureSupported(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        return false;
    }
}
