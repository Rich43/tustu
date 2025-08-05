package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.Envelope;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
import com.sun.xml.internal.messaging.saaj.util.transform.EfficientStreamingTransformer;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/EnvelopeImpl.class */
public abstract class EnvelopeImpl extends ElementImpl implements Envelope {
    protected HeaderImpl header;
    protected BodyImpl body;
    String omitXmlDecl;
    String charset;
    String xmlDecl;

    protected abstract NameImpl getHeaderName(String str);

    protected abstract NameImpl getBodyName(String str);

    protected EnvelopeImpl(SOAPDocumentImpl ownerDoc, Name name) {
        super(ownerDoc, name);
        this.omitXmlDecl = "yes";
        this.charset = "utf-8";
        this.xmlDecl = null;
    }

    protected EnvelopeImpl(SOAPDocumentImpl ownerDoc, QName name) {
        super(ownerDoc, name);
        this.omitXmlDecl = "yes";
        this.charset = "utf-8";
        this.xmlDecl = null;
    }

    protected EnvelopeImpl(SOAPDocumentImpl ownerDoc, NameImpl name, boolean createHeader, boolean createBody) throws SOAPException {
        this(ownerDoc, name);
        ensureNamespaceIsDeclared(getElementQName().getPrefix(), getElementQName().getNamespaceURI());
        if (createHeader) {
            addHeader();
        }
        if (createBody) {
            addBody();
        }
    }

    @Override // javax.xml.soap.SOAPEnvelope
    public SOAPHeader addHeader() throws SOAPException {
        return addHeader(null);
    }

    public SOAPHeader addHeader(String prefix) throws SOAPException {
        if (prefix == null || prefix.equals("")) {
            prefix = getPrefix();
        }
        NameImpl headerName = getHeaderName(prefix);
        NameImpl bodyName = getBodyName(prefix);
        SOAPElement firstChild = null;
        Iterator eachChild = getChildElementNodes();
        if (eachChild.hasNext()) {
            firstChild = (SOAPElement) eachChild.next();
            if (firstChild.getElementName().equals(headerName)) {
                log.severe("SAAJ0120.impl.header.already.exists");
                throw new SOAPExceptionImpl("Can't add a header when one is already present.");
            }
            if (!firstChild.getElementName().equals(bodyName)) {
                log.severe("SAAJ0121.impl.invalid.first.child.of.envelope");
                throw new SOAPExceptionImpl("First child of Envelope must be either a Header or Body");
            }
        }
        HeaderImpl header = (HeaderImpl) createElement(headerName);
        insertBefore(header, firstChild);
        header.ensureNamespaceIsDeclared(headerName.getPrefix(), headerName.getURI());
        return header;
    }

    protected void lookForHeader() throws SOAPException {
        NameImpl headerName = getHeaderName(null);
        HeaderImpl hdr = (HeaderImpl) findChild(headerName);
        this.header = hdr;
    }

    @Override // javax.xml.soap.SOAPEnvelope
    public SOAPHeader getHeader() throws SOAPException {
        lookForHeader();
        return this.header;
    }

    protected void lookForBody() throws SOAPException {
        NameImpl bodyName = getBodyName(null);
        BodyImpl bodyChildElement = (BodyImpl) findChild(bodyName);
        this.body = bodyChildElement;
    }

    @Override // javax.xml.soap.SOAPEnvelope
    public SOAPBody addBody() throws SOAPException {
        return addBody(null);
    }

    public SOAPBody addBody(String prefix) throws SOAPException {
        lookForBody();
        if (prefix == null || prefix.equals("")) {
            prefix = getPrefix();
        }
        if (this.body == null) {
            NameImpl bodyName = getBodyName(prefix);
            this.body = (BodyImpl) createElement(bodyName);
            insertBefore(this.body, null);
            this.body.ensureNamespaceIsDeclared(bodyName.getPrefix(), bodyName.getURI());
            return this.body;
        }
        log.severe("SAAJ0122.impl.body.already.exists");
        throw new SOAPExceptionImpl("Can't add a body when one is already present.");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(Name name) throws SOAPException {
        if (getBodyName(null).equals(name)) {
            return addBody(name.getPrefix());
        }
        if (getHeaderName(null).equals(name)) {
            return addHeader(name.getPrefix());
        }
        return super.addElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(QName name) throws SOAPException {
        if (getBodyName(null).equals(NameImpl.convertToName(name))) {
            return addBody(name.getPrefix());
        }
        if (getHeaderName(null).equals(NameImpl.convertToName(name))) {
            return addHeader(name.getPrefix());
        }
        return super.addElement(name);
    }

    @Override // javax.xml.soap.SOAPEnvelope
    public SOAPBody getBody() throws SOAPException {
        lookForBody();
        return this.body;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.Envelope
    public Source getContent() {
        return new DOMSource(getOwnerDocument());
    }

    @Override // javax.xml.soap.SOAPEnvelope
    public Name createName(String localName, String prefix, String uri) throws SOAPException {
        if ("xmlns".equals(prefix)) {
            log.severe("SAAJ0123.impl.no.reserved.xmlns");
            throw new SOAPExceptionImpl("Cannot declare reserved xmlns prefix");
        }
        if (prefix == null && "xmlns".equals(localName)) {
            log.severe("SAAJ0124.impl.qualified.name.cannot.be.xmlns");
            throw new SOAPExceptionImpl("Qualified name cannot be xmlns");
        }
        return NameImpl.create(localName, prefix, uri);
    }

    public Name createName(String localName, String prefix) throws SOAPException {
        String namespace = getNamespaceURI(prefix);
        if (namespace == null) {
            log.log(Level.SEVERE, "SAAJ0126.impl.cannot.locate.ns", (Object[]) new String[]{prefix});
            throw new SOAPExceptionImpl("Unable to locate namespace for prefix " + prefix);
        }
        return NameImpl.create(localName, prefix, namespace);
    }

    @Override // javax.xml.soap.SOAPEnvelope
    public Name createName(String localName) throws SOAPException {
        return NameImpl.createFromUnqualifiedName(localName);
    }

    public void setOmitXmlDecl(String value) {
        this.omitXmlDecl = value;
    }

    public void setXmlDecl(String value) {
        this.xmlDecl = value;
    }

    private String getOmitXmlDecl() {
        return this.omitXmlDecl;
    }

    public void setCharsetEncoding(String value) {
        this.charset = value;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.Envelope
    public void output(OutputStream out) throws IOException {
        try {
            Transformer transformer = EfficientStreamingTransformer.newTransformer();
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.setOutputProperty("encoding", this.charset);
            if (this.omitXmlDecl.equals("no") && this.xmlDecl == null) {
                this.xmlDecl = "<?xml version=\"" + getOwnerDocument().getXmlVersion() + "\" encoding=\"" + this.charset + "\" ?>";
            }
            StreamResult result = new StreamResult(out);
            if (this.xmlDecl != null) {
                OutputStreamWriter writer = new OutputStreamWriter(out, this.charset);
                writer.write(this.xmlDecl);
                writer.flush();
                result = new StreamResult(writer);
            }
            if (log.isLoggable(Level.FINE)) {
                log.log(Level.FINE, "SAAJ0190.impl.set.xml.declaration", (Object[]) new String[]{this.omitXmlDecl});
                log.log(Level.FINE, "SAAJ0191.impl.set.encoding", (Object[]) new String[]{this.charset});
            }
            transformer.transform(getContent(), result);
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.Envelope
    public void output(OutputStream out, boolean isFastInfoset) throws IOException {
        if (!isFastInfoset) {
            output(out);
            return;
        }
        try {
            getContent();
            Transformer transformer = EfficientStreamingTransformer.newTransformer();
            transformer.transform(getContent(), FastInfosetReflection.FastInfosetResult_new(out));
        } catch (Exception ex) {
            throw new IOException(ex.getMessage());
        }
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[]{this.elementQName.getLocalPart(), newName.getLocalPart()});
        throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
    }
}
