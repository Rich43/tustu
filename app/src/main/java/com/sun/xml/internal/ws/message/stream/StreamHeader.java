package com.sun.xml.internal.ws.message.stream;

import com.sun.istack.internal.FinalArrayList;
import com.sun.istack.internal.NotNull;
import com.sun.xml.internal.stream.buffer.XMLStreamBuffer;
import com.sun.xml.internal.stream.buffer.XMLStreamBufferSource;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import com.sun.xml.internal.ws.message.AbstractHeaderImpl;
import com.sun.xml.internal.ws.util.xml.XmlUtil;
import java.util.Set;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.Node;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/StreamHeader.class */
public abstract class StreamHeader extends AbstractHeaderImpl {
    protected final XMLStreamBuffer _mark;
    protected boolean _isMustUnderstand;

    @NotNull
    protected String _role;
    protected boolean _isRelay;
    protected String _localName;
    protected String _namespaceURI;
    private final FinalArrayList<Attribute> attributes;
    static final /* synthetic */ boolean $assertionsDisabled;

    protected abstract FinalArrayList<Attribute> processHeaderAttributes(XMLStreamReader xMLStreamReader);

    static {
        $assertionsDisabled = !StreamHeader.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/message/stream/StreamHeader$Attribute.class */
    protected static final class Attribute {
        final String nsUri;
        final String localName;
        final String value;

        public Attribute(String nsUri, String localName, String value) {
            this.nsUri = StreamHeader.fixNull(nsUri);
            this.localName = localName;
            this.value = value;
        }
    }

    protected StreamHeader(XMLStreamReader reader, XMLStreamBuffer mark) {
        if (!$assertionsDisabled && (reader == null || mark == null)) {
            throw new AssertionError();
        }
        this._mark = mark;
        this._localName = reader.getLocalName();
        this._namespaceURI = reader.getNamespaceURI();
        this.attributes = processHeaderAttributes(reader);
    }

    protected StreamHeader(XMLStreamReader reader) throws XMLStreamException {
        this._localName = reader.getLocalName();
        this._namespaceURI = reader.getNamespaceURI();
        this.attributes = processHeaderAttributes(reader);
        this._mark = XMLStreamBuffer.createNewBufferFromXMLStreamReader(reader);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public final boolean isIgnorable(@NotNull SOAPVersion soapVersion, @NotNull Set<String> roles) {
        return (this._isMustUnderstand && roles != null && roles.contains(this._role)) ? false : true;
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getRole(@NotNull SOAPVersion soapVersion) {
        if ($assertionsDisabled || this._role != null) {
            return this._role;
        }
        throw new AssertionError();
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    public boolean isRelay() {
        return this._isRelay;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getNamespaceURI() {
        return this._namespaceURI;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getLocalPart() {
        return this._localName;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public String getAttribute(String nsUri, String localName) {
        if (this.attributes != null) {
            for (int i2 = this.attributes.size() - 1; i2 >= 0; i2--) {
                Attribute a2 = this.attributes.get(i2);
                if (a2.localName.equals(localName) && a2.nsUri.equals(nsUri)) {
                    return a2.value;
                }
            }
            return null;
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        return this._mark.readAsXMLStreamReader();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        if (this._mark.getInscopeNamespaces().size() > 0) {
            this._mark.writeToXMLStreamWriter(w2, true);
        } else {
            this._mark.writeToXMLStreamWriter(w2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        try {
            TransformerFactory tf = XmlUtil.newTransformerFactory();
            Transformer t2 = tf.newTransformer();
            XMLStreamBufferSource source = new XMLStreamBufferSource(this._mark);
            DOMResult result = new DOMResult();
            t2.transform(source, result);
            Node d2 = result.getNode();
            if (d2.getNodeType() == 9) {
                d2 = d2.getFirstChild();
            }
            SOAPHeader header = saaj.getSOAPHeader();
            if (header == null) {
                header = saaj.getSOAPPart().getEnvelope().addHeader();
            }
            Node node = header.getOwnerDocument().importNode(d2, true);
            header.appendChild(node);
        } catch (Exception e2) {
            throw new SOAPException(e2);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler contentHandler, ErrorHandler errorHandler) throws SAXException {
        this._mark.writeTo(contentHandler);
    }

    @Override // com.sun.xml.internal.ws.message.AbstractHeaderImpl, com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public WSEndpointReference readAsEPR(AddressingVersion expected) throws XMLStreamException {
        return new WSEndpointReference(this._mark, expected);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String fixNull(String s2) {
        return s2 == null ? "" : s2;
    }
}
