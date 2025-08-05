package com.sun.xml.internal.bind.v2.runtime.unmarshaller;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor;
import com.sun.xml.internal.ws.encoding.MtomCodec;
import javax.activation.DataHandler;
import javax.xml.bind.attachment.AttachmentUnmarshaller;
import javax.xml.namespace.NamespaceContext;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/unmarshaller/MTOMDecorator.class */
final class MTOMDecorator implements XmlVisitor {
    private final XmlVisitor next;

    /* renamed from: au, reason: collision with root package name */
    private final AttachmentUnmarshaller f12073au;
    private UnmarshallerImpl parent;
    private final Base64Data base64data = new Base64Data();
    private boolean inXopInclude;
    private boolean followXop;

    public MTOMDecorator(UnmarshallerImpl parent, XmlVisitor next, AttachmentUnmarshaller au2) {
        this.parent = parent;
        this.next = next;
        this.f12073au = au2;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startDocument(LocatorEx loc, NamespaceContext nsContext) throws SAXException {
        this.next.startDocument(loc, nsContext);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endDocument() throws SAXException {
        this.next.endDocument();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startElement(TagName tagName) throws SAXException {
        if (tagName.local.equals(MtomCodec.XOP_LOCALNAME) && tagName.uri.equals("http://www.w3.org/2004/08/xop/include")) {
            String href = tagName.atts.getValue(Constants.ATTRNAME_HREF);
            DataHandler attachment = this.f12073au.getAttachmentAsDataHandler(href);
            if (attachment == null) {
                this.parent.getEventHandler().handleEvent(null);
            }
            this.base64data.set(attachment);
            this.next.text(this.base64data);
            this.inXopInclude = true;
            this.followXop = true;
            return;
        }
        this.next.startElement(tagName);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endElement(TagName tagName) throws SAXException {
        if (this.inXopInclude) {
            this.inXopInclude = false;
            this.followXop = true;
        } else {
            this.next.endElement(tagName);
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void startPrefixMapping(String prefix, String nsUri) throws SAXException {
        this.next.startPrefixMapping(prefix, nsUri);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void endPrefixMapping(String prefix) throws SAXException {
        this.next.endPrefixMapping(prefix);
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public void text(CharSequence pcdata) throws SAXException {
        if (!this.followXop) {
            this.next.text(pcdata);
        } else {
            this.followXop = false;
        }
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public UnmarshallingContext getContext() {
        return this.next.getContext();
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.XmlVisitor
    public XmlVisitor.TextPredictor getPredictor() {
        return this.next.getPredictor();
    }
}
