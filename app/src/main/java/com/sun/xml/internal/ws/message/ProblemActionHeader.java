package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/ProblemActionHeader.class */
public class ProblemActionHeader extends AbstractHeaderImpl {

    @NotNull
    protected String action;
    protected String soapAction;

    /* renamed from: av, reason: collision with root package name */
    @NotNull
    protected AddressingVersion f12087av;
    private static final String actionLocalName = "Action";
    private static final String soapActionLocalName = "SoapAction";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ProblemActionHeader.class.desiredAssertionStatus();
    }

    public ProblemActionHeader(@NotNull String action, @NotNull AddressingVersion av2) {
        this(action, null, av2);
    }

    public ProblemActionHeader(@NotNull String action, String soapAction, @NotNull AddressingVersion av2) {
        if (!$assertionsDisabled && action == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && av2 == null) {
            throw new AssertionError();
        }
        this.action = action;
        this.soapAction = soapAction;
        this.f12087av = av2;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getNamespaceURI() {
        return this.f12087av.nsUri;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getLocalPart() {
        return "ProblemAction";
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @Nullable
    public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public XMLStreamReader readHeader() throws XMLStreamException {
        MutableXMLStreamBuffer buf = new MutableXMLStreamBuffer();
        XMLStreamWriter w2 = buf.createFromXMLStreamWriter();
        writeTo(w2);
        return buf.readAsXMLStreamReader();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        w2.writeStartElement("", getLocalPart(), getNamespaceURI());
        w2.writeDefaultNamespace(getNamespaceURI());
        w2.writeStartElement(actionLocalName);
        w2.writeCharacters(this.action);
        w2.writeEndElement();
        if (this.soapAction != null) {
            w2.writeStartElement(soapActionLocalName);
            w2.writeCharacters(this.soapAction);
            w2.writeEndElement();
        }
        w2.writeEndElement();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        SOAPHeader header = saaj.getSOAPHeader();
        if (header == null) {
            header = saaj.getSOAPPart().getEnvelope().addHeader();
        }
        SOAPHeaderElement she = header.addHeaderElement(new QName(getNamespaceURI(), getLocalPart()));
        she.addChildElement(actionLocalName);
        she.addTextNode(this.action);
        if (this.soapAction != null) {
            she.addChildElement(soapActionLocalName);
            she.addTextNode(this.soapAction);
        }
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler h2, ErrorHandler errorHandler) throws SAXException {
        String nsUri = getNamespaceURI();
        String ln = getLocalPart();
        h2.startPrefixMapping("", nsUri);
        h2.startElement(nsUri, ln, ln, EMPTY_ATTS);
        h2.startElement(nsUri, actionLocalName, actionLocalName, EMPTY_ATTS);
        h2.characters(this.action.toCharArray(), 0, this.action.length());
        h2.endElement(nsUri, actionLocalName, actionLocalName);
        if (this.soapAction != null) {
            h2.startElement(nsUri, soapActionLocalName, soapActionLocalName, EMPTY_ATTS);
            h2.characters(this.soapAction.toCharArray(), 0, this.soapAction.length());
            h2.endElement(nsUri, soapActionLocalName, soapActionLocalName);
        }
        h2.endElement(nsUri, ln, ln);
    }
}
