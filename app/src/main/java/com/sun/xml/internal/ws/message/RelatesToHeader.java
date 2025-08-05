package com.sun.xml.internal.ws.message;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/RelatesToHeader.class */
public final class RelatesToHeader extends StringHeader {
    protected String type;
    private final QName typeAttributeName;

    public RelatesToHeader(QName name, String messageId, String type) {
        super(name, messageId);
        this.type = type;
        this.typeAttributeName = new QName(name.getNamespaceURI(), "type");
    }

    public RelatesToHeader(QName name, String mid) {
        super(name, mid);
        this.typeAttributeName = new QName(name.getNamespaceURI(), "type");
    }

    public String getType() {
        return this.type;
    }

    @Override // com.sun.xml.internal.ws.message.StringHeader, com.sun.xml.internal.ws.api.message.Header
    public void writeTo(XMLStreamWriter w2) throws XMLStreamException {
        w2.writeStartElement("", this.name.getLocalPart(), this.name.getNamespaceURI());
        w2.writeDefaultNamespace(this.name.getNamespaceURI());
        if (this.type != null) {
            w2.writeAttribute("type", this.type);
        }
        w2.writeCharacters(this.value);
        w2.writeEndElement();
    }

    @Override // com.sun.xml.internal.ws.message.StringHeader, com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        SOAPHeader header = saaj.getSOAPHeader();
        if (header == null) {
            header = saaj.getSOAPPart().getEnvelope().addHeader();
        }
        SOAPHeaderElement she = header.addHeaderElement(this.name);
        if (this.type != null) {
            she.addAttribute(this.typeAttributeName, this.type);
        }
        she.addTextNode(this.value);
    }
}
