package org.jcp.xml.dsig.internal.dom;

import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.XMLUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMCryptoContext;
import javax.xml.crypto.dsig.keyinfo.PGPData;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:org/jcp/xml/dsig/internal/dom/DOMPGPData.class */
public final class DOMPGPData extends DOMStructure implements PGPData {
    private final byte[] keyId;
    private final byte[] keyPacket;
    private final List<XMLStructure> externalElements;

    public DOMPGPData(byte[] bArr, List<? extends XMLStructure> list) {
        if (bArr == null) {
            throw new NullPointerException("keyPacket cannot be null");
        }
        if (list == null || list.isEmpty()) {
            this.externalElements = Collections.emptyList();
        } else {
            this.externalElements = Collections.unmodifiableList(new ArrayList(list));
            int size = this.externalElements.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (!(this.externalElements.get(i2) instanceof XMLStructure)) {
                    throw new ClassCastException("other[" + i2 + "] is not a valid PGPData type");
                }
            }
        }
        this.keyPacket = (byte[]) bArr.clone();
        checkKeyPacket(bArr);
        this.keyId = null;
    }

    public DOMPGPData(byte[] bArr, byte[] bArr2, List<? extends XMLStructure> list) {
        if (bArr == null) {
            throw new NullPointerException("keyId cannot be null");
        }
        if (bArr.length != 8) {
            throw new IllegalArgumentException("keyId must be 8 bytes long");
        }
        if (list == null || list.isEmpty()) {
            this.externalElements = Collections.emptyList();
        } else {
            this.externalElements = Collections.unmodifiableList(new ArrayList(list));
            int size = this.externalElements.size();
            for (int i2 = 0; i2 < size; i2++) {
                if (!(this.externalElements.get(i2) instanceof XMLStructure)) {
                    throw new ClassCastException("other[" + i2 + "] is not a valid PGPData type");
                }
            }
        }
        this.keyId = (byte[]) bArr.clone();
        this.keyPacket = bArr2 == null ? null : (byte[]) bArr2.clone();
        if (bArr2 != null) {
            checkKeyPacket(bArr2);
        }
    }

    public DOMPGPData(Element element) throws MarshalException {
        byte[] bArrDecode = null;
        byte[] bArrDecode2 = null;
        ArrayList arrayList = new ArrayList();
        Node firstChild = element.getFirstChild();
        while (true) {
            Node node = firstChild;
            if (node != null) {
                if (node.getNodeType() == 1) {
                    Element element2 = (Element) node;
                    String localName = element2.getLocalName();
                    String namespaceURI = element2.getNamespaceURI();
                    if (Constants._TAG_PGPKEYID.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        bArrDecode = XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(element2));
                    } else if (Constants._TAG_PGPKEYPACKET.equals(localName) && "http://www.w3.org/2000/09/xmldsig#".equals(namespaceURI)) {
                        bArrDecode2 = XMLUtils.decode(XMLUtils.getFullTextChildrenFromNode(element2));
                    } else {
                        arrayList.add(new javax.xml.crypto.dom.DOMStructure(element2));
                    }
                }
                firstChild = node.getNextSibling();
            } else {
                this.keyId = bArrDecode;
                this.keyPacket = bArrDecode2;
                this.externalElements = Collections.unmodifiableList(arrayList);
                return;
            }
        }
    }

    @Override // javax.xml.crypto.dsig.keyinfo.PGPData
    public byte[] getKeyId() {
        if (this.keyId == null) {
            return null;
        }
        return (byte[]) this.keyId.clone();
    }

    @Override // javax.xml.crypto.dsig.keyinfo.PGPData
    public byte[] getKeyPacket() {
        if (this.keyPacket == null) {
            return null;
        }
        return (byte[]) this.keyPacket.clone();
    }

    @Override // javax.xml.crypto.dsig.keyinfo.PGPData
    public List<XMLStructure> getExternalElements() {
        return this.externalElements;
    }

    @Override // org.jcp.xml.dsig.internal.dom.DOMStructure
    public void marshal(Node node, String str, DOMCryptoContext dOMCryptoContext) throws MarshalException, DOMException {
        Document ownerDocument = DOMUtils.getOwnerDocument(node);
        Element elementCreateElement = DOMUtils.createElement(ownerDocument, Constants._TAG_PGPDATA, "http://www.w3.org/2000/09/xmldsig#", str);
        if (this.keyId != null) {
            Element elementCreateElement2 = DOMUtils.createElement(ownerDocument, Constants._TAG_PGPKEYID, "http://www.w3.org/2000/09/xmldsig#", str);
            elementCreateElement2.appendChild(ownerDocument.createTextNode(XMLUtils.encodeToString(this.keyId)));
            elementCreateElement.appendChild(elementCreateElement2);
        }
        if (this.keyPacket != null) {
            Element elementCreateElement3 = DOMUtils.createElement(ownerDocument, Constants._TAG_PGPKEYPACKET, "http://www.w3.org/2000/09/xmldsig#", str);
            elementCreateElement3.appendChild(ownerDocument.createTextNode(XMLUtils.encodeToString(this.keyPacket)));
            elementCreateElement.appendChild(elementCreateElement3);
        }
        Iterator<XMLStructure> it = this.externalElements.iterator();
        while (it.hasNext()) {
            DOMUtils.appendChild(elementCreateElement, ((javax.xml.crypto.dom.DOMStructure) it.next()).getNode());
        }
        node.appendChild(elementCreateElement);
    }

    private void checkKeyPacket(byte[] bArr) {
        if (bArr.length < 3) {
            throw new IllegalArgumentException("keypacket must be at least 3 bytes long");
        }
        byte b2 = bArr[0];
        if ((b2 & 128) != 128) {
            throw new IllegalArgumentException("keypacket tag is invalid: bit 7 is not set");
        }
        if ((b2 & 64) != 64) {
            throw new IllegalArgumentException("old keypacket tag format is unsupported");
        }
        if ((b2 & 6) != 6 && (b2 & 14) != 14 && (b2 & 5) != 5 && (b2 & 7) != 7) {
            throw new IllegalArgumentException("keypacket tag is invalid: must be 6, 14, 5, or 7");
        }
    }
}
