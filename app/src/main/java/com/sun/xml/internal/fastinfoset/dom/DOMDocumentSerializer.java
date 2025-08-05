package com.sun.xml.internal.fastinfoset.dom;

import com.sun.xml.internal.fastinfoset.Encoder;
import com.sun.xml.internal.fastinfoset.QualifiedName;
import com.sun.xml.internal.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.internal.fastinfoset.util.NamespaceContextImplementation;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import java.io.IOException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/dom/DOMDocumentSerializer.class */
public class DOMDocumentSerializer extends Encoder {
    protected NamespaceContextImplementation _namespaceScopeContext = new NamespaceContextImplementation();
    protected Node[] _attributes = new Node[32];

    public final void serialize(Node n2) throws DOMException, IOException {
        switch (n2.getNodeType()) {
            case 1:
                serializeElementAsDocument(n2);
                break;
            case 7:
                serializeProcessingInstruction(n2);
                break;
            case 8:
                serializeComment(n2);
                break;
            case 9:
                serialize((Document) n2);
                break;
        }
    }

    public final void serialize(Document d2) throws DOMException, IOException {
        reset();
        encodeHeader(false);
        encodeInitialVocabulary();
        NodeList nl = d2.getChildNodes();
        for (int i2 = 0; i2 < nl.getLength(); i2++) {
            Node n2 = nl.item(i2);
            switch (n2.getNodeType()) {
                case 1:
                    serializeElement(n2);
                    break;
                case 7:
                    serializeProcessingInstruction(n2);
                    break;
                case 8:
                    serializeComment(n2);
                    break;
            }
        }
        encodeDocumentTermination();
    }

    protected final void serializeElementAsDocument(Node e2) throws DOMException, IOException {
        reset();
        encodeHeader(false);
        encodeInitialVocabulary();
        serializeElement(e2);
        encodeDocumentTermination();
    }

    protected final void serializeElement(Node e2) throws DOMException, IOException {
        encodeTermination();
        int attributesSize = 0;
        this._namespaceScopeContext.pushContext();
        if (e2.hasAttributes()) {
            NamedNodeMap nnm = e2.getAttributes();
            for (int i2 = 0; i2 < nnm.getLength(); i2++) {
                Node a2 = nnm.item(i2);
                String namespaceURI = a2.getNamespaceURI();
                if (namespaceURI != null && namespaceURI.equals("http://www.w3.org/2000/xmlns/")) {
                    String attrPrefix = a2.getLocalName();
                    String attrNamespace = a2.getNodeValue();
                    if (attrPrefix == "xmlns" || attrPrefix.equals("xmlns")) {
                        attrPrefix = "";
                    }
                    this._namespaceScopeContext.declarePrefix(attrPrefix, attrNamespace);
                } else {
                    if (attributesSize == this._attributes.length) {
                        Node[] attributes = new Node[((attributesSize * 3) / 2) + 1];
                        System.arraycopy(this._attributes, 0, attributes, 0, attributesSize);
                        this._attributes = attributes;
                    }
                    int i3 = attributesSize;
                    attributesSize++;
                    this._attributes[i3] = a2;
                    String attrNamespaceURI = a2.getNamespaceURI();
                    String attrPrefix2 = a2.getPrefix();
                    if (attrPrefix2 != null && !this._namespaceScopeContext.getNamespaceURI(attrPrefix2).equals(attrNamespaceURI)) {
                        this._namespaceScopeContext.declarePrefix(attrPrefix2, attrNamespaceURI);
                    }
                }
            }
        }
        String elementNamespaceURI = e2.getNamespaceURI();
        String elementPrefix = e2.getPrefix();
        if (elementPrefix == null) {
            elementPrefix = "";
        }
        if (elementNamespaceURI != null && !this._namespaceScopeContext.getNamespaceURI(elementPrefix).equals(elementNamespaceURI)) {
            this._namespaceScopeContext.declarePrefix(elementPrefix, elementNamespaceURI);
        }
        if (!this._namespaceScopeContext.isCurrentContextEmpty()) {
            if (attributesSize > 0) {
                write(120);
            } else {
                write(56);
            }
            for (int i4 = this._namespaceScopeContext.getCurrentContextStartIndex(); i4 < this._namespaceScopeContext.getCurrentContextEndIndex(); i4++) {
                String prefix = this._namespaceScopeContext.getPrefix(i4);
                String uri = this._namespaceScopeContext.getNamespaceURI(i4);
                encodeNamespaceAttribute(prefix, uri);
            }
            write(240);
            this._b = 0;
        } else {
            this._b = attributesSize > 0 ? 64 : 0;
        }
        encodeElement(elementNamespaceURI == null ? "" : elementNamespaceURI, e2.getNodeName(), e2.getLocalName());
        if (attributesSize > 0) {
            for (int i5 = 0; i5 < attributesSize; i5++) {
                Node a3 = this._attributes[i5];
                this._attributes[i5] = null;
                String namespaceURI2 = a3.getNamespaceURI();
                encodeAttribute(namespaceURI2 == null ? "" : namespaceURI2, a3.getNodeName(), a3.getLocalName());
                String value = a3.getNodeValue();
                boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
            }
            this._b = 240;
            this._terminate = true;
        }
        if (e2.hasChildNodes()) {
            NodeList nl = e2.getChildNodes();
            for (int i6 = 0; i6 < nl.getLength(); i6++) {
                Node n2 = nl.item(i6);
                switch (n2.getNodeType()) {
                    case 1:
                        serializeElement(n2);
                        break;
                    case 3:
                        serializeText(n2);
                        break;
                    case 4:
                        serializeCDATA(n2);
                        break;
                    case 7:
                        serializeProcessingInstruction(n2);
                        break;
                    case 8:
                        serializeComment(n2);
                        break;
                }
            }
        }
        encodeElementTermination();
        this._namespaceScopeContext.popContext();
    }

    protected final void serializeText(Node t2) throws DOMException, IOException {
        String text = t2.getNodeValue();
        int length = text != null ? text.length() : 0;
        if (length == 0) {
            return;
        }
        if (length < this._charBuffer.length) {
            text.getChars(0, length, this._charBuffer, 0);
            if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(this._charBuffer, 0, length)) {
                return;
            }
            encodeTermination();
            encodeCharacters(this._charBuffer, 0, length);
            return;
        }
        char[] ch = text.toCharArray();
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, 0, length)) {
            return;
        }
        encodeTermination();
        encodeCharactersNoClone(ch, 0, length);
    }

    protected final void serializeCDATA(Node t2) throws DOMException, IOException {
        String text = t2.getNodeValue();
        int length = text != null ? text.length() : 0;
        if (length == 0) {
            return;
        }
        char[] ch = text.toCharArray();
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, 0, length)) {
            return;
        }
        encodeTermination();
        try {
            encodeCIIBuiltInAlgorithmDataAsCDATA(ch, 0, length);
        } catch (FastInfosetException e2) {
            throw new IOException("");
        }
    }

    protected final void serializeComment(Node c2) throws DOMException, IOException {
        if (getIgnoreComments()) {
            return;
        }
        encodeTermination();
        String comment = c2.getNodeValue();
        int length = comment != null ? comment.length() : 0;
        if (length == 0) {
            encodeComment(this._charBuffer, 0, 0);
        } else if (length < this._charBuffer.length) {
            comment.getChars(0, length, this._charBuffer, 0);
            encodeComment(this._charBuffer, 0, length);
        } else {
            char[] ch = comment.toCharArray();
            encodeCommentNoClone(ch, 0, length);
        }
    }

    protected final void serializeProcessingInstruction(Node pi) throws DOMException, IOException {
        if (getIgnoreProcesingInstructions()) {
            return;
        }
        encodeTermination();
        String target = pi.getNodeName();
        String data = pi.getNodeValue();
        encodeProcessingInstruction(target, data);
    }

    protected final void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                if (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName)) {
                    encodeNonZeroIntegerOnThirdBit(names[i2].index);
                    return;
                }
            }
        }
        if (localName != null) {
            encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
        } else {
            encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, "", qName, entry);
        }
    }

    protected final void encodeAttribute(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i2 = 0; i2 < entry._valueIndex; i2++) {
                if (namespaceURI == names[i2].namespaceName || namespaceURI.equals(names[i2].namespaceName)) {
                    encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i2].index);
                    return;
                }
            }
        }
        if (localName != null) {
            encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
        } else {
            encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, "", qName, entry);
        }
    }
}
