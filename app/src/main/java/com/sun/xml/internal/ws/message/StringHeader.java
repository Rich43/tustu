package com.sun.xml.internal.ws.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.stream.buffer.MutableXMLStreamBuffer;
import com.sun.xml.internal.ws.api.SOAPVersion;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.icepdf.core.util.PdfOps;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/* loaded from: rt.jar:com/sun/xml/internal/ws/message/StringHeader.class */
public class StringHeader extends AbstractHeaderImpl {
    protected final QName name;
    protected final String value;
    protected boolean mustUnderstand;
    protected SOAPVersion soapVersion;
    protected static final String MUST_UNDERSTAND = "mustUnderstand";
    protected static final String S12_MUST_UNDERSTAND_TRUE = "true";
    protected static final String S11_MUST_UNDERSTAND_TRUE = "1";
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StringHeader.class.desiredAssertionStatus();
    }

    public StringHeader(@NotNull QName name, @NotNull String value) {
        this.mustUnderstand = false;
        if (!$assertionsDisabled && name == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && value == null) {
            throw new AssertionError();
        }
        this.name = name;
        this.value = value;
    }

    public StringHeader(@NotNull QName name, @NotNull String value, @NotNull SOAPVersion soapVersion, boolean mustUnderstand) {
        this.mustUnderstand = false;
        this.name = name;
        this.value = value;
        this.soapVersion = soapVersion;
        this.mustUnderstand = mustUnderstand;
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getNamespaceURI() {
        return this.name.getNamespaceURI();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @NotNull
    public String getLocalPart() {
        return this.name.getLocalPart();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    @Nullable
    public String getAttribute(@NotNull String nsUri, @NotNull String localName) {
        if (this.mustUnderstand && this.soapVersion.nsUri.equals(nsUri) && "mustUnderstand".equals(localName)) {
            return getMustUnderstandLiteral(this.soapVersion);
        }
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
        w2.writeStartElement("", this.name.getLocalPart(), this.name.getNamespaceURI());
        w2.writeDefaultNamespace(this.name.getNamespaceURI());
        if (this.mustUnderstand) {
            w2.writeNamespace(PdfOps.S_TOKEN, this.soapVersion.nsUri);
            w2.writeAttribute(PdfOps.S_TOKEN, this.soapVersion.nsUri, "mustUnderstand", getMustUnderstandLiteral(this.soapVersion));
        }
        w2.writeCharacters(this.value);
        w2.writeEndElement();
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(SOAPMessage saaj) throws SOAPException {
        SOAPHeader header = saaj.getSOAPHeader();
        if (header == null) {
            header = saaj.getSOAPPart().getEnvelope().addHeader();
        }
        SOAPHeaderElement she = header.addHeaderElement(this.name);
        if (this.mustUnderstand) {
            she.setMustUnderstand(true);
        }
        she.addTextNode(this.value);
    }

    @Override // com.sun.xml.internal.ws.api.message.Header
    public void writeTo(ContentHandler h2, ErrorHandler errorHandler) throws SAXException {
        String nsUri = this.name.getNamespaceURI();
        String ln = this.name.getLocalPart();
        h2.startPrefixMapping("", nsUri);
        if (this.mustUnderstand) {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(this.soapVersion.nsUri, "mustUnderstand", "S:mustUnderstand", "CDATA", getMustUnderstandLiteral(this.soapVersion));
            h2.startElement(nsUri, ln, ln, attributes);
        } else {
            h2.startElement(nsUri, ln, ln, EMPTY_ATTS);
        }
        h2.characters(this.value.toCharArray(), 0, this.value.length());
        h2.endElement(nsUri, ln, ln);
    }

    private static String getMustUnderstandLiteral(SOAPVersion sv) {
        if (sv == SOAPVersion.SOAP_12) {
            return "true";
        }
        return "1";
    }
}
