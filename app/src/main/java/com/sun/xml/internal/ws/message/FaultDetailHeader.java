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

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/FaultDetailHeader.class */
public class FaultDetailHeader extends AbstractHeaderImpl {

    /* renamed from: av, reason: collision with root package name */
    private AddressingVersion f12086av;
    private String wrapper;
    private String problemValue;

    public FaultDetailHeader(AddressingVersion av2, String wrapper, QName problemHeader) {
        this.problemValue = null;
        this.f12086av = av2;
        this.wrapper = wrapper;
        this.problemValue = problemHeader.toString();
    }

    public FaultDetailHeader(AddressingVersion av2, String wrapper, String problemValue) {
        this.problemValue = null;
        this.f12086av = av2;
        this.wrapper = wrapper;
        this.problemValue = problemValue;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getNamespaceURI() {
        return this.f12086av.nsUri;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getLocalPart() {
        return this.f12086av.faultDetailTag.getLocalPart();
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
        w2.writeStartElement("", this.f12086av.faultDetailTag.getLocalPart(), this.f12086av.faultDetailTag.getNamespaceURI());
        w2.writeDefaultNamespace(this.f12086av.nsUri);
        w2.writeStartElement("", this.wrapper, this.f12086av.nsUri);
        w2.writeCharacters(this.problemValue);
        w2.writeEndElement();
        w2.writeEndElement();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        SOAPHeader header = saaj.getSOAPHeader();
        if (header == null) {
            header = saaj.getSOAPPart().getEnvelope().addHeader();
        }
        header.addHeaderElement(this.f12086av.faultDetailTag);
        SOAPHeaderElement she = header.addHeaderElement(new QName(this.f12086av.nsUri, this.wrapper));
        she.addTextNode(this.problemValue);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler h2, ErrorHandler errorHandler) throws SAXException {
        String nsUri = this.f12086av.nsUri;
        String ln = this.f12086av.faultDetailTag.getLocalPart();
        h2.startPrefixMapping("", nsUri);
        h2.startElement(nsUri, ln, ln, EMPTY_ATTS);
        h2.startElement(nsUri, this.wrapper, this.wrapper, EMPTY_ATTS);
        h2.characters(this.problemValue.toCharArray(), 0, this.problemValue.length());
        h2.endElement(nsUri, this.wrapper, this.wrapper);
        h2.endElement(nsUri, ln, ln);
    }
}
