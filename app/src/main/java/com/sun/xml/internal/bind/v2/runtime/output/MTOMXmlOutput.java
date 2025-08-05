package com.sun.xml.internal.bind.v2.runtime.output;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.bind.v2.runtime.Name;
import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/output/MTOMXmlOutput.class */
public final class MTOMXmlOutput extends XmlOutputAbstractImpl {
    private final XmlOutput next;
    private String nsUri;
    private String localName;

    public MTOMXmlOutput(XmlOutput next) {
        this.next = next;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws SAXException, XMLStreamException, IOException {
        super.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
        this.next.startDocument(serializer, fragment, nsUriIndex2prefixIndex, nsContext);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws SAXException, XMLStreamException, IOException {
        this.next.endDocument(fragment);
        super.endDocument(fragment);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws XMLStreamException, IOException {
        this.next.beginStartTag(name);
        this.nsUri = name.nsUri;
        this.localName = name.localName;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(int prefix, String localName) throws XMLStreamException, IOException {
        this.next.beginStartTag(prefix, localName);
        this.nsUri = this.nsContext.getNamespaceURI(prefix);
        this.localName = localName;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws XMLStreamException, IOException {
        this.next.attribute(name, value);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void attribute(int prefix, String localName, String value) throws XMLStreamException, IOException {
        this.next.attribute(prefix, localName, value);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endStartTag() throws SAXException, IOException {
        this.next.endStartTag();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws SAXException, XMLStreamException, IOException {
        this.next.endTag(name);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutputAbstractImpl, com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void endTag(int prefix, String localName) throws SAXException, XMLStreamException, IOException {
        this.next.endTag(prefix, localName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(String value, boolean needsSeparatingWhitespace) throws SAXException, XMLStreamException, IOException {
        this.next.text(value, needsSeparatingWhitespace);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.output.XmlOutput
    public void text(Pcdata value, boolean needsSeparatingWhitespace) throws XMLStreamException, SAXException, IOException {
        String cid;
        if ((value instanceof Base64Data) && !this.serializer.getInlineBinaryFlag()) {
            Base64Data b64d = (Base64Data) value;
            if (b64d.hasData()) {
                cid = this.serializer.attachmentMarshaller.addMtomAttachment(b64d.get(), 0, b64d.getDataLen(), b64d.getMimeType(), this.nsUri, this.localName);
            } else {
                cid = this.serializer.attachmentMarshaller.addMtomAttachment(b64d.getDataHandler(), this.nsUri, this.localName);
            }
            if (cid != null) {
                this.nsContext.getCurrent().push();
                int prefix = this.nsContext.declareNsUri("http://www.w3.org/2004/08/xop/include", "xop", false);
                beginStartTag(prefix, MtomCodec.XOP_LOCALNAME);
                attribute(-1, Constants.ATTRNAME_HREF, cid);
                endStartTag();
                endTag(prefix, MtomCodec.XOP_LOCALNAME);
                this.nsContext.getCurrent().pop();
                return;
            }
        }
        this.next.text(value, needsSeparatingWhitespace);
    }
}
