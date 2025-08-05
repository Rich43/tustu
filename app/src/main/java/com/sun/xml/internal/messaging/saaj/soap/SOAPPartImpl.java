package com.sun.xml.internal.messaging.saaj.soap;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeBodyPart;
import com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.EnvelopeImpl;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import com.sun.xml.internal.messaging.saaj.util.FastInfosetReflection;
import com.sun.xml.internal.messaging.saaj.util.JAXMStreamSource;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.messaging.saaj.util.MimeHeadersUtil;
import com.sun.xml.internal.messaging.saaj.util.SAAJUtil;
import com.sun.xml.internal.messaging.saaj.util.XMLDeclarationParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.w3c.dom.UserDataHandler;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/SOAPPartImpl.class */
public abstract class SOAPPartImpl extends SOAPPart implements SOAPDocument {
    protected MimeHeaders headers;
    protected Envelope envelope;
    protected Source source;
    protected SOAPDocumentImpl document;
    private boolean sourceWasSet;
    protected boolean omitXmlDecl;
    protected String sourceCharsetEncoding;
    protected MessageImpl message;
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.LocalStrings");
    static final boolean lazyContentLength = SAAJUtil.getSystemBoolean("saaj.lazy.contentlength");

    protected abstract String getContentType();

    protected abstract Envelope createEnvelopeFromSource() throws SOAPException;

    protected abstract Envelope createEmptyEnvelope(String str) throws SOAPException;

    protected abstract SOAPPartImpl duplicateType();

    protected SOAPPartImpl() {
        this(null);
    }

    protected SOAPPartImpl(MessageImpl message) {
        this.sourceWasSet = false;
        this.omitXmlDecl = true;
        this.sourceCharsetEncoding = null;
        this.document = new SOAPDocumentImpl(this);
        this.headers = new MimeHeaders();
        this.message = message;
        this.headers.setHeader("Content-Type", getContentType());
    }

    protected String getContentTypeString() {
        return getContentType();
    }

    public boolean isFastInfoset() {
        if (this.message != null) {
            return this.message.isFastInfoset();
        }
        return false;
    }

    @Override // javax.xml.soap.SOAPPart
    public SOAPEnvelope getEnvelope() throws SOAPException {
        if (this.sourceWasSet) {
            this.sourceWasSet = false;
        }
        lookForEnvelope();
        if (this.envelope != null) {
            if (this.source != null) {
                this.document.removeChild(this.envelope);
                this.envelope = createEnvelopeFromSource();
            }
        } else if (this.source != null) {
            this.envelope = createEnvelopeFromSource();
        } else {
            this.envelope = createEmptyEnvelope(null);
            this.document.insertBefore(this.envelope, null);
        }
        return this.envelope;
    }

    protected void lookForEnvelope() throws SOAPException {
        Element envelopeChildElement = this.document.doGetDocumentElement();
        if (envelopeChildElement == null || (envelopeChildElement instanceof Envelope)) {
            this.envelope = (EnvelopeImpl) envelopeChildElement;
            return;
        }
        if (!(envelopeChildElement instanceof ElementImpl)) {
            log.severe("SAAJ0512.soap.incorrect.factory.used");
            throw new SOAPExceptionImpl("Unable to create envelope: incorrect factory used during tree construction");
        }
        ElementImpl soapElement = (ElementImpl) envelopeChildElement;
        if (soapElement.getLocalName().equalsIgnoreCase("Envelope")) {
            String prefix = soapElement.getPrefix();
            String uri = prefix == null ? soapElement.getNamespaceURI() : soapElement.getNamespaceURI(prefix);
            if (!uri.equals("http://schemas.xmlsoap.org/soap/envelope/") && !uri.equals("http://www.w3.org/2003/05/soap-envelope")) {
                log.severe("SAAJ0513.soap.unknown.ns");
                throw new SOAPVersionMismatchException("Unable to create envelope from given source because the namespace was not recognized");
            }
            return;
        }
        log.severe("SAAJ0514.soap.root.elem.not.named.envelope");
        throw new SOAPExceptionImpl("Unable to create envelope from given source because the root element is not named \"Envelope\"");
    }

    @Override // javax.xml.soap.SOAPPart
    public void removeAllMimeHeaders() {
        this.headers.removeAllHeaders();
    }

    @Override // javax.xml.soap.SOAPPart
    public void removeMimeHeader(String header) {
        this.headers.removeHeader(header);
    }

    @Override // javax.xml.soap.SOAPPart
    public String[] getMimeHeader(String name) {
        return this.headers.getHeader(name);
    }

    @Override // javax.xml.soap.SOAPPart
    public void setMimeHeader(String name, String value) {
        this.headers.setHeader(name, value);
    }

    @Override // javax.xml.soap.SOAPPart
    public void addMimeHeader(String name, String value) {
        this.headers.addHeader(name, value);
    }

    @Override // javax.xml.soap.SOAPPart
    public Iterator getAllMimeHeaders() {
        return this.headers.getAllHeaders();
    }

    @Override // javax.xml.soap.SOAPPart
    public Iterator getMatchingMimeHeaders(String[] names) {
        return this.headers.getMatchingHeaders(names);
    }

    @Override // javax.xml.soap.SOAPPart
    public Iterator getNonMatchingMimeHeaders(String[] names) {
        return this.headers.getNonMatchingHeaders(names);
    }

    @Override // javax.xml.soap.SOAPPart
    public Source getContent() throws SOAPException {
        if (this.source != null) {
            InputStream bis = null;
            if (this.source instanceof JAXMStreamSource) {
                StreamSource streamSource = (StreamSource) this.source;
                bis = streamSource.getInputStream();
            } else if (FastInfosetReflection.isFastInfosetSource(this.source)) {
                SAXSource saxSource = (SAXSource) this.source;
                bis = saxSource.getInputSource().getByteStream();
            }
            if (bis != null) {
                try {
                    bis.reset();
                } catch (IOException e2) {
                }
            }
            return this.source;
        }
        return ((Envelope) getEnvelope()).getContent();
    }

    @Override // javax.xml.soap.SOAPPart
    public void setContent(Source source) throws SOAPException {
        try {
            if (source instanceof StreamSource) {
                InputStream is = ((StreamSource) source).getInputStream();
                Reader rdr = ((StreamSource) source).getReader();
                if (is != null) {
                    this.source = new JAXMStreamSource(is);
                } else if (rdr != null) {
                    this.source = new JAXMStreamSource(rdr);
                } else {
                    log.severe("SAAJ0544.soap.no.valid.reader.for.src");
                    throw new SOAPExceptionImpl("Source does not have a valid Reader or InputStream");
                }
            } else if (FastInfosetReflection.isFastInfosetSource(source)) {
                InputStream is2 = FastInfosetReflection.FastInfosetSource_getInputStream(source);
                if (!(is2 instanceof ByteInputStream)) {
                    ByteOutputStream bout = new ByteOutputStream();
                    bout.write(is2);
                    FastInfosetReflection.FastInfosetSource_setInputStream(source, bout.newInputStream());
                }
                this.source = source;
            } else {
                this.source = source;
            }
            this.sourceWasSet = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.severe("SAAJ0545.soap.cannot.set.src.for.part");
            throw new SOAPExceptionImpl("Error setting the source for SOAPPart: " + ex.getMessage());
        }
    }

    public InputStream getContentAsStream() throws IOException {
        if (this.source != null) {
            InputStream is = null;
            if ((this.source instanceof StreamSource) && !isFastInfoset()) {
                is = ((StreamSource) this.source).getInputStream();
            } else if (FastInfosetReflection.isFastInfosetSource(this.source) && isFastInfoset()) {
                try {
                    is = FastInfosetReflection.FastInfosetSource_getInputStream(this.source);
                } catch (Exception e2) {
                    throw new IOException(e2.toString());
                }
            }
            if (is != null) {
                if (lazyContentLength) {
                    return is;
                }
                if (!(is instanceof ByteInputStream)) {
                    log.severe("SAAJ0546.soap.stream.incorrect.type");
                    throw new IOException("Internal error: stream not of the right type");
                }
                return (ByteInputStream) is;
            }
        }
        ByteOutputStream b2 = new ByteOutputStream();
        try {
            Envelope env = (Envelope) getEnvelope();
            env.output(b2, isFastInfoset());
            return b2.newInputStream();
        } catch (SOAPException soapException) {
            log.severe("SAAJ0547.soap.cannot.externalize");
            throw new SOAPIOException("SOAP exception while trying to externalize: ", soapException);
        }
    }

    MimeBodyPart getMimePart() throws SOAPException {
        try {
            MimeBodyPart headerEnvelope = new MimeBodyPart();
            headerEnvelope.setDataHandler(getDataHandler());
            AttachmentPartImpl.copyMimeHeaders(this.headers, headerEnvelope);
            return headerEnvelope;
        } catch (SOAPException ex) {
            throw ex;
        } catch (Exception ex2) {
            log.severe("SAAJ0548.soap.cannot.externalize.hdr");
            throw new SOAPExceptionImpl("Unable to externalize header", ex2);
        }
    }

    MimeHeaders getMimeHeaders() {
        return this.headers;
    }

    DataHandler getDataHandler() {
        DataSource ds = new DataSource() { // from class: com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl.1
            @Override // javax.activation.DataSource
            public OutputStream getOutputStream() throws IOException {
                throw new IOException("Illegal Operation");
            }

            @Override // javax.activation.DataSource
            public String getContentType() {
                return SOAPPartImpl.this.getContentTypeString();
            }

            @Override // javax.activation.DataSource
            public String getName() {
                return SOAPPartImpl.this.getContentId();
            }

            @Override // javax.activation.DataSource
            public InputStream getInputStream() throws IOException {
                return SOAPPartImpl.this.getContentAsStream();
            }
        };
        return new DataHandler(ds);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPDocument
    public SOAPDocumentImpl getDocument() {
        handleNewSource();
        return this.document;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.SOAPDocument
    public SOAPPartImpl getSOAPPart() {
        return this;
    }

    @Override // org.w3c.dom.Document
    public DocumentType getDoctype() {
        return this.document.getDoctype();
    }

    @Override // org.w3c.dom.Document
    public DOMImplementation getImplementation() {
        return this.document.getImplementation();
    }

    @Override // org.w3c.dom.Document
    public Element getDocumentElement() {
        try {
            getEnvelope();
        } catch (SOAPException e2) {
        }
        return this.document.getDocumentElement();
    }

    protected void doGetDocumentElement() {
        handleNewSource();
        try {
            lookForEnvelope();
        } catch (SOAPException e2) {
        }
    }

    @Override // org.w3c.dom.Document
    public Element createElement(String tagName) throws DOMException {
        return this.document.createElement(tagName);
    }

    @Override // org.w3c.dom.Document
    public DocumentFragment createDocumentFragment() {
        return this.document.createDocumentFragment();
    }

    @Override // org.w3c.dom.Document
    public Text createTextNode(String data) {
        return this.document.createTextNode(data);
    }

    @Override // org.w3c.dom.Document
    public Comment createComment(String data) {
        return this.document.createComment(data);
    }

    @Override // org.w3c.dom.Document
    public CDATASection createCDATASection(String data) throws DOMException {
        return this.document.createCDATASection(data);
    }

    @Override // org.w3c.dom.Document
    public ProcessingInstruction createProcessingInstruction(String target, String data) throws DOMException {
        return this.document.createProcessingInstruction(target, data);
    }

    @Override // org.w3c.dom.Document
    public Attr createAttribute(String name) throws DOMException {
        return this.document.createAttribute(name);
    }

    @Override // org.w3c.dom.Document
    public EntityReference createEntityReference(String name) throws DOMException {
        return this.document.createEntityReference(name);
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagName(String tagname) {
        handleNewSource();
        return this.document.getElementsByTagName(tagname);
    }

    @Override // org.w3c.dom.Document
    public Node importNode(Node importedNode, boolean deep) throws DOMException {
        handleNewSource();
        return this.document.importNode(importedNode, deep);
    }

    @Override // org.w3c.dom.Document
    public Element createElementNS(String namespaceURI, String qualifiedName) throws DOMException {
        return this.document.createElementNS(namespaceURI, qualifiedName);
    }

    @Override // org.w3c.dom.Document
    public Attr createAttributeNS(String namespaceURI, String qualifiedName) throws DOMException {
        return this.document.createAttributeNS(namespaceURI, qualifiedName);
    }

    @Override // org.w3c.dom.Document
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        handleNewSource();
        return this.document.getElementsByTagNameNS(namespaceURI, localName);
    }

    @Override // org.w3c.dom.Document
    public Element getElementById(String elementId) {
        handleNewSource();
        return this.document.getElementById(elementId);
    }

    @Override // org.w3c.dom.Node
    public Node appendChild(Node newChild) throws DOMException {
        handleNewSource();
        return this.document.appendChild(newChild);
    }

    @Override // org.w3c.dom.Node
    public Node cloneNode(boolean deep) {
        handleNewSource();
        return this.document.cloneNode(deep);
    }

    protected SOAPPartImpl doCloneNode() {
        handleNewSource();
        SOAPPartImpl newSoapPart = duplicateType();
        newSoapPart.headers = MimeHeadersUtil.copy(this.headers);
        newSoapPart.source = this.source;
        return newSoapPart;
    }

    @Override // org.w3c.dom.Node
    public NamedNodeMap getAttributes() {
        return this.document.getAttributes();
    }

    @Override // org.w3c.dom.Node
    public NodeList getChildNodes() {
        handleNewSource();
        return this.document.getChildNodes();
    }

    @Override // org.w3c.dom.Node
    public Node getFirstChild() {
        handleNewSource();
        return this.document.getFirstChild();
    }

    @Override // org.w3c.dom.Node
    public Node getLastChild() {
        handleNewSource();
        return this.document.getLastChild();
    }

    @Override // org.w3c.dom.Node
    public String getLocalName() {
        return this.document.getLocalName();
    }

    @Override // org.w3c.dom.Node
    public String getNamespaceURI() {
        return this.document.getNamespaceURI();
    }

    @Override // org.w3c.dom.Node
    public Node getNextSibling() {
        handleNewSource();
        return this.document.getNextSibling();
    }

    @Override // org.w3c.dom.Node
    public String getNodeName() {
        return this.document.getNodeName();
    }

    @Override // org.w3c.dom.Node
    public short getNodeType() {
        return this.document.getNodeType();
    }

    @Override // org.w3c.dom.Node
    public String getNodeValue() throws DOMException {
        return this.document.getNodeValue();
    }

    @Override // org.w3c.dom.Node
    public Document getOwnerDocument() {
        return this.document.getOwnerDocument();
    }

    @Override // org.w3c.dom.Node
    public Node getParentNode() {
        return this.document.getParentNode();
    }

    @Override // org.w3c.dom.Node
    public String getPrefix() {
        return this.document.getPrefix();
    }

    @Override // org.w3c.dom.Node
    public Node getPreviousSibling() {
        return this.document.getPreviousSibling();
    }

    @Override // org.w3c.dom.Node
    public boolean hasAttributes() {
        return this.document.hasAttributes();
    }

    @Override // org.w3c.dom.Node
    public boolean hasChildNodes() {
        handleNewSource();
        return this.document.hasChildNodes();
    }

    @Override // org.w3c.dom.Node
    public Node insertBefore(Node arg0, Node arg1) throws DOMException {
        handleNewSource();
        return this.document.insertBefore(arg0, arg1);
    }

    @Override // org.w3c.dom.Node
    public boolean isSupported(String arg0, String arg1) {
        return this.document.isSupported(arg0, arg1);
    }

    @Override // org.w3c.dom.Node
    public void normalize() {
        handleNewSource();
        this.document.normalize();
    }

    @Override // org.w3c.dom.Node
    public Node removeChild(Node arg0) throws DOMException {
        handleNewSource();
        return this.document.removeChild(arg0);
    }

    @Override // org.w3c.dom.Node
    public Node replaceChild(Node arg0, Node arg1) throws DOMException {
        handleNewSource();
        return this.document.replaceChild(arg0, arg1);
    }

    @Override // org.w3c.dom.Node
    public void setNodeValue(String arg0) throws DOMException {
        this.document.setNodeValue(arg0);
    }

    @Override // org.w3c.dom.Node
    public void setPrefix(String arg0) throws DOMException {
        this.document.setPrefix(arg0);
    }

    private void handleNewSource() {
        if (this.sourceWasSet) {
            try {
                getEnvelope();
            } catch (SOAPException e2) {
            }
        }
    }

    protected XMLDeclarationParser lookForXmlDecl() throws SOAPException {
        Reader reader;
        if (this.source != null && (this.source instanceof StreamSource)) {
            InputStream inputStream = ((StreamSource) this.source).getInputStream();
            if (inputStream != null) {
                if (getSourceCharsetEncoding() == null) {
                    reader = new InputStreamReader(inputStream);
                } else {
                    try {
                        reader = new InputStreamReader(inputStream, getSourceCharsetEncoding());
                    } catch (UnsupportedEncodingException uee) {
                        log.log(Level.SEVERE, "SAAJ0551.soap.unsupported.encoding", new Object[]{getSourceCharsetEncoding()});
                        throw new SOAPExceptionImpl("Unsupported encoding " + getSourceCharsetEncoding(), uee);
                    }
                }
            } else {
                reader = ((StreamSource) this.source).getReader();
            }
            if (reader != null) {
                PushbackReader pushbackReader = new PushbackReader(reader, 4096);
                XMLDeclarationParser ev = new XMLDeclarationParser(pushbackReader);
                try {
                    ev.parse();
                    String xmlDecl = ev.getXmlDeclaration();
                    if (xmlDecl != null && xmlDecl.length() > 0) {
                        this.omitXmlDecl = false;
                    }
                    if (lazyContentLength) {
                        this.source = new StreamSource(pushbackReader);
                    }
                    return ev;
                } catch (Exception e2) {
                    log.log(Level.SEVERE, "SAAJ0552.soap.xml.decl.parsing.failed");
                    throw new SOAPExceptionImpl("XML declaration parsing failed", e2);
                }
            }
            return null;
        }
        if (this.source == null || !(this.source instanceof DOMSource)) {
        }
        return null;
    }

    public void setSourceCharsetEncoding(String charset) {
        this.sourceCharsetEncoding = charset;
    }

    @Override // org.w3c.dom.Document
    public Node renameNode(Node n2, String namespaceURI, String qualifiedName) throws DOMException {
        handleNewSource();
        return this.document.renameNode(n2, namespaceURI, qualifiedName);
    }

    @Override // org.w3c.dom.Document
    public void normalizeDocument() {
        this.document.normalizeDocument();
    }

    @Override // org.w3c.dom.Document
    public DOMConfiguration getDomConfig() {
        return this.document.getDomConfig();
    }

    @Override // org.w3c.dom.Document
    public Node adoptNode(Node source) throws DOMException {
        handleNewSource();
        return this.document.adoptNode(source);
    }

    @Override // org.w3c.dom.Document
    public void setDocumentURI(String documentURI) {
        this.document.setDocumentURI(documentURI);
    }

    @Override // org.w3c.dom.Document
    public String getDocumentURI() {
        return this.document.getDocumentURI();
    }

    @Override // org.w3c.dom.Document
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        this.document.setStrictErrorChecking(strictErrorChecking);
    }

    @Override // org.w3c.dom.Document
    public String getInputEncoding() {
        return this.document.getInputEncoding();
    }

    @Override // org.w3c.dom.Document
    public String getXmlEncoding() {
        return this.document.getXmlEncoding();
    }

    @Override // org.w3c.dom.Document
    public boolean getXmlStandalone() {
        return this.document.getXmlStandalone();
    }

    @Override // org.w3c.dom.Document
    public void setXmlStandalone(boolean xmlStandalone) throws DOMException {
        this.document.setXmlStandalone(xmlStandalone);
    }

    @Override // org.w3c.dom.Document
    public String getXmlVersion() {
        return this.document.getXmlVersion();
    }

    @Override // org.w3c.dom.Document
    public void setXmlVersion(String xmlVersion) throws DOMException {
        this.document.setXmlVersion(xmlVersion);
    }

    @Override // org.w3c.dom.Document
    public boolean getStrictErrorChecking() {
        return this.document.getStrictErrorChecking();
    }

    @Override // org.w3c.dom.Node
    public String getBaseURI() {
        return this.document.getBaseURI();
    }

    @Override // org.w3c.dom.Node
    public short compareDocumentPosition(Node other) throws DOMException {
        return this.document.compareDocumentPosition(other);
    }

    @Override // org.w3c.dom.Node
    public String getTextContent() throws DOMException {
        return this.document.getTextContent();
    }

    @Override // org.w3c.dom.Node
    public void setTextContent(String textContent) throws DOMException {
        this.document.setTextContent(textContent);
    }

    @Override // org.w3c.dom.Node
    public boolean isSameNode(Node other) {
        return this.document.isSameNode(other);
    }

    @Override // org.w3c.dom.Node
    public String lookupPrefix(String namespaceURI) {
        return this.document.lookupPrefix(namespaceURI);
    }

    @Override // org.w3c.dom.Node
    public boolean isDefaultNamespace(String namespaceURI) {
        return this.document.isDefaultNamespace(namespaceURI);
    }

    @Override // org.w3c.dom.Node
    public String lookupNamespaceURI(String prefix) {
        return this.document.lookupNamespaceURI(prefix);
    }

    @Override // org.w3c.dom.Node
    public boolean isEqualNode(Node arg) {
        return this.document.isEqualNode(arg);
    }

    @Override // org.w3c.dom.Node
    public Object getFeature(String feature, String version) {
        return this.document.getFeature(feature, version);
    }

    @Override // org.w3c.dom.Node
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        return this.document.setUserData(key, data, handler);
    }

    @Override // org.w3c.dom.Node
    public Object getUserData(String key) {
        return this.document.getUserData(key);
    }

    @Override // javax.xml.soap.Node
    public void recycleNode() {
    }

    @Override // javax.xml.soap.Node
    public String getValue() {
        return null;
    }

    @Override // javax.xml.soap.Node
    public void setValue(String value) {
        log.severe("SAAJ0571.soappart.setValue.not.defined");
        throw new IllegalStateException("Setting value of a soap part is not defined");
    }

    @Override // javax.xml.soap.Node
    public void setParentElement(SOAPElement parent) throws SOAPException {
        log.severe("SAAJ0570.soappart.parent.element.not.defined");
        throw new SOAPExceptionImpl("The parent element of a soap part is not defined");
    }

    @Override // javax.xml.soap.Node
    public SOAPElement getParentElement() {
        return null;
    }

    @Override // javax.xml.soap.Node
    public void detachNode() {
    }

    public String getSourceCharsetEncoding() {
        return this.sourceCharsetEncoding;
    }
}
