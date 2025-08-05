package com.sun.xml.internal.ws.util.xml;

import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
import com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx;
import com.sun.xml.internal.ws.encoding.TagInfoset;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XMLReaderComposite.class */
public class XMLReaderComposite implements XMLStreamReaderEx {
    protected State state = State.StartTag;
    protected ElemInfo elemInfo;
    protected TagInfoset tagInfo;
    protected XMLStreamReader[] children;
    protected int payloadIndex;
    protected XMLStreamReader payloadReader;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XMLReaderComposite$State.class */
    public enum State {
        StartTag,
        Payload,
        EndTag
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/util/xml/XMLReaderComposite$ElemInfo.class */
    public static class ElemInfo implements NamespaceContext {
        ElemInfo ancestor;
        TagInfoset tagInfo;

        public ElemInfo(TagInfoset tag, ElemInfo parent) {
            this.tagInfo = tag;
            this.ancestor = parent;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getNamespaceURI(String prefix) {
            String n2 = this.tagInfo.getNamespaceURI(prefix);
            if (n2 != null) {
                return n2;
            }
            if (this.ancestor != null) {
                return this.ancestor.getNamespaceURI(prefix);
            }
            return null;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public String getPrefix(String uri) {
            String p2 = this.tagInfo.getPrefix(uri);
            if (p2 != null) {
                return p2;
            }
            if (this.ancestor != null) {
                return this.ancestor.getPrefix(uri);
            }
            return null;
        }

        public List<String> allPrefixes(String namespaceURI) {
            List<String> l2 = this.tagInfo.allPrefixes(namespaceURI);
            if (this.ancestor != null) {
                List<String> p2 = this.ancestor.allPrefixes(namespaceURI);
                p2.addAll(l2);
                return p2;
            }
            return l2;
        }

        @Override // javax.xml.namespace.NamespaceContext
        public Iterator<String> getPrefixes(String namespaceURI) {
            return allPrefixes(namespaceURI).iterator();
        }
    }

    public XMLReaderComposite(ElemInfo elem, XMLStreamReader[] wrapees) {
        this.payloadIndex = -1;
        this.elemInfo = elem;
        this.tagInfo = elem.tagInfo;
        this.children = wrapees;
        if (this.children != null && this.children.length > 0) {
            this.payloadIndex = 0;
            this.payloadReader = this.children[this.payloadIndex];
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int next() throws XMLStreamException {
        switch (this.state) {
            case StartTag:
                if (this.payloadReader != null) {
                    this.state = State.Payload;
                    return this.payloadReader.getEventType();
                }
                this.state = State.EndTag;
                return 2;
            case EndTag:
                return 8;
            case Payload:
            default:
                int next = 8;
                if (this.payloadReader != null && this.payloadReader.hasNext()) {
                    next = this.payloadReader.next();
                }
                if (next != 8) {
                    return next;
                }
                if (this.payloadIndex + 1 < this.children.length) {
                    this.payloadIndex++;
                    this.payloadReader = this.children[this.payloadIndex];
                    return this.payloadReader.getEventType();
                }
                this.state = State.EndTag;
                return 2;
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasNext() throws XMLStreamException {
        switch (this.state) {
            case StartTag:
            case Payload:
            default:
                return true;
            case EndTag:
                return false;
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getElementText() throws XMLStreamException {
        switch (this.state) {
            case StartTag:
                return this.payloadReader.isCharacters() ? this.payloadReader.getText() : "";
            case Payload:
            default:
                return this.payloadReader.getElementText();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int nextTag() throws XMLStreamException {
        int e2 = next();
        if (e2 == 8) {
            return e2;
        }
        while (e2 != 8) {
            if (e2 != 1 && e2 != 2) {
                e2 = next();
            }
            return e2;
        }
        return e2;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Object getProperty(String name) throws IllegalArgumentException {
        if (this.payloadReader != null) {
            return this.payloadReader.getProperty(name);
        }
        return null;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (this.payloadReader != null) {
            this.payloadReader.require(type, namespaceURI, localName);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public void close() throws XMLStreamException {
        if (this.payloadReader != null) {
            this.payloadReader.close();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(String prefix) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.elemInfo.getNamespaceURI(prefix);
            case Payload:
            default:
                return this.payloadReader.getNamespaceURI(prefix);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStartElement() {
        switch (this.state) {
            case StartTag:
                return true;
            case EndTag:
                return false;
            case Payload:
            default:
                return this.payloadReader.isStartElement();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isEndElement() {
        switch (this.state) {
            case StartTag:
                return false;
            case EndTag:
                return true;
            case Payload:
            default:
                return this.payloadReader.isEndElement();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isCharacters() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return false;
            case Payload:
            default:
                return this.payloadReader.isCharacters();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isWhiteSpace() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return false;
            case Payload:
            default:
                return this.payloadReader.isWhiteSpace();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(String uri, String localName) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.atts.getValue(uri, localName);
            case Payload:
            default:
                return this.payloadReader.getAttributeValue(uri, localName);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getAttributeCount() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.atts.getLength();
            case Payload:
            default:
                return this.payloadReader.getAttributeCount();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getAttributeName(int i2) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return new QName(this.tagInfo.atts.getURI(i2), this.tagInfo.atts.getLocalName(i2), getPrfix(this.tagInfo.atts.getQName(i2)));
            case Payload:
            default:
                return this.payloadReader.getAttributeName(i2);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeNamespace(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.atts.getURI(index);
            case Payload:
            default:
                return this.payloadReader.getAttributeNamespace(index);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeLocalName(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.atts.getLocalName(index);
            case Payload:
            default:
                return this.payloadReader.getAttributeLocalName(index);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributePrefix(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return getPrfix(this.tagInfo.atts.getQName(index));
            case Payload:
            default:
                return this.payloadReader.getAttributePrefix(index);
        }
    }

    private static String getPrfix(String qName) {
        if (qName == null) {
            return null;
        }
        int i2 = qName.indexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        return i2 > 0 ? qName.substring(0, i2) : "";
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeType(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.atts.getType(index);
            case Payload:
            default:
                return this.payloadReader.getAttributeType(index);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getAttributeValue(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.atts.getValue(index);
            case Payload:
            default:
                return this.payloadReader.getAttributeValue(index);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isAttributeSpecified(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return index < this.tagInfo.atts.getLength() && this.tagInfo.atts.getLocalName(index) != null;
            case Payload:
            default:
                return this.payloadReader.isAttributeSpecified(index);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getNamespaceCount() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.ns.length / 2;
            case Payload:
            default:
                return this.payloadReader.getNamespaceCount();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespacePrefix(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.ns[2 * index];
            case Payload:
            default:
                return this.payloadReader.getNamespacePrefix(index);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI(int index) {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.ns[(2 * index) + 1];
            case Payload:
            default:
                return this.payloadReader.getNamespaceURI(index);
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx, javax.xml.stream.XMLStreamReader
    public NamespaceContextEx getNamespaceContext() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return new NamespaceContextExAdaper(this.elemInfo);
            case Payload:
            default:
                if (isPayloadReaderEx()) {
                    return payloadReaderEx().getNamespaceContext();
                }
                return new NamespaceContextExAdaper(this.payloadReader.getNamespaceContext());
        }
    }

    private boolean isPayloadReaderEx() {
        return this.payloadReader instanceof XMLStreamReaderEx;
    }

    private XMLStreamReaderEx payloadReaderEx() {
        return (XMLStreamReaderEx) this.payloadReader;
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getEventType() {
        switch (this.state) {
            case StartTag:
                return 1;
            case EndTag:
                return 2;
            case Payload:
            default:
                return this.payloadReader.getEventType();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getText() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getText();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public char[] getTextCharacters() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getTextCharacters();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int length) throws XMLStreamException {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return -1;
            case Payload:
            default:
                return this.payloadReader.getTextCharacters(sourceStart, target, targetStart, length);
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextStart() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return 0;
            case Payload:
            default:
                return this.payloadReader.getTextStart();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public int getTextLength() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return 0;
            case Payload:
            default:
                return this.payloadReader.getTextLength();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getEncoding() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getEncoding();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasText() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return false;
            case Payload:
            default:
                return this.payloadReader.hasText();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public Location getLocation() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return new Location() { // from class: com.sun.xml.internal.ws.util.xml.XMLReaderComposite.1
                    @Override // javax.xml.stream.Location
                    public int getLineNumber() {
                        return 0;
                    }

                    @Override // javax.xml.stream.Location
                    public int getColumnNumber() {
                        return 0;
                    }

                    @Override // javax.xml.stream.Location
                    public int getCharacterOffset() {
                        return 0;
                    }

                    @Override // javax.xml.stream.Location
                    public String getPublicId() {
                        return null;
                    }

                    @Override // javax.xml.stream.Location
                    public String getSystemId() {
                        return null;
                    }
                };
            case Payload:
            default:
                return this.payloadReader.getLocation();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public QName getName() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return new QName(this.tagInfo.nsUri, this.tagInfo.localName, this.tagInfo.prefix);
            case Payload:
            default:
                return this.payloadReader.getName();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getLocalName() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.localName;
            case Payload:
            default:
                return this.payloadReader.getLocalName();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean hasName() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return true;
            case Payload:
            default:
                return this.payloadReader.hasName();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getNamespaceURI() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.nsUri;
            case Payload:
            default:
                return this.payloadReader.getNamespaceURI();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPrefix() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return this.tagInfo.prefix;
            case Payload:
            default:
                return this.payloadReader.getPrefix();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getVersion() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getVersion();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean isStandalone() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return true;
            case Payload:
            default:
                return this.payloadReader.isStandalone();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public boolean standaloneSet() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return true;
            case Payload:
            default:
                return this.payloadReader.standaloneSet();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getCharacterEncodingScheme() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getCharacterEncodingScheme();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPITarget() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getPITarget();
        }
    }

    @Override // javax.xml.stream.XMLStreamReader
    public String getPIData() {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return this.payloadReader.getPIData();
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
    public String getElementTextTrim() throws XMLStreamException {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return isPayloadReaderEx() ? payloadReaderEx().getElementTextTrim() : this.payloadReader.getElementText().trim();
        }
    }

    @Override // com.sun.xml.internal.org.jvnet.staxex.XMLStreamReaderEx
    public CharSequence getPCDATA() throws XMLStreamException {
        switch (this.state) {
            case StartTag:
            case EndTag:
                return null;
            case Payload:
            default:
                return isPayloadReaderEx() ? payloadReaderEx().getPCDATA() : this.payloadReader.getElementText();
        }
    }
}
