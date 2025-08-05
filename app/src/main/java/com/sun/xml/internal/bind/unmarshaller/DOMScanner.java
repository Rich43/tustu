package com.sun.xml.internal.bind.unmarshaller;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
import java.util.Enumeration;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;

/* loaded from: rt.jar:com/sun/xml/internal/bind/unmarshaller/DOMScanner.class */
public class DOMScanner implements LocatorEx, InfosetScanner {
    private Node currentNode = null;
    private final AttributesImpl atts = new AttributesImpl();
    private ContentHandler receiver = null;
    private Locator locator = this;

    public void setLocator(Locator loc) {
        this.locator = loc;
    }

    @Override // com.sun.xml.internal.bind.unmarshaller.InfosetScanner
    public void scan(Object node) throws SAXException {
        if (node instanceof Document) {
            scan((Document) node);
        } else {
            scan((Element) node);
        }
    }

    public void scan(Document doc) throws SAXException {
        scan(doc.getDocumentElement());
    }

    public void scan(Element e2) throws SAXException {
        setCurrentLocation(e2);
        this.receiver.setDocumentLocator(this.locator);
        this.receiver.startDocument();
        NamespaceSupport nss = new NamespaceSupport();
        buildNamespaceSupport(nss, e2.getParentNode());
        Enumeration en = nss.getPrefixes();
        while (en.hasMoreElements()) {
            String prefix = (String) en.nextElement2();
            this.receiver.startPrefixMapping(prefix, nss.getURI(prefix));
        }
        visit(e2);
        Enumeration en2 = nss.getPrefixes();
        while (en2.hasMoreElements()) {
            this.receiver.endPrefixMapping((String) en2.nextElement2());
        }
        setCurrentLocation(e2);
        this.receiver.endDocument();
    }

    public void parse(Element e2, ContentHandler handler) throws SAXException, DOMException {
        this.receiver = handler;
        setCurrentLocation(e2);
        this.receiver.startDocument();
        this.receiver.setDocumentLocator(this.locator);
        visit(e2);
        setCurrentLocation(e2);
        this.receiver.endDocument();
    }

    public void parseWithContext(Element e2, ContentHandler handler) throws SAXException {
        setContentHandler(handler);
        scan(e2);
    }

    private void buildNamespaceSupport(NamespaceSupport nss, Node node) {
        if (node == null || node.getNodeType() != 1) {
            return;
        }
        buildNamespaceSupport(nss, node.getParentNode());
        nss.pushContext();
        NamedNodeMap atts = node.getAttributes();
        for (int i2 = 0; i2 < atts.getLength(); i2++) {
            Attr a2 = (Attr) atts.item(i2);
            if ("xmlns".equals(a2.getPrefix())) {
                nss.declarePrefix(a2.getLocalName(), a2.getValue());
            } else if ("xmlns".equals(a2.getName())) {
                nss.declarePrefix("", a2.getValue());
            }
        }
    }

    public void visit(Element e2) throws SAXException, DOMException {
        setCurrentLocation(e2);
        NamedNodeMap attributes = e2.getAttributes();
        this.atts.clear();
        int len = attributes == null ? 0 : attributes.getLength();
        for (int i2 = len - 1; i2 >= 0; i2--) {
            Attr a2 = (Attr) attributes.item(i2);
            String name = a2.getName();
            if (name.startsWith("xmlns")) {
                if (name.length() == 5) {
                    this.receiver.startPrefixMapping("", a2.getValue());
                } else {
                    String localName = a2.getLocalName();
                    if (localName == null) {
                        localName = name.substring(6);
                    }
                    this.receiver.startPrefixMapping(localName, a2.getValue());
                }
            } else {
                String uri = a2.getNamespaceURI();
                if (uri == null) {
                    uri = "";
                }
                String local = a2.getLocalName();
                if (local == null) {
                    local = a2.getName();
                }
                this.atts.addAttribute(uri, local, a2.getName(), "CDATA", a2.getValue());
            }
        }
        String uri2 = e2.getNamespaceURI();
        if (uri2 == null) {
            uri2 = "";
        }
        String local2 = e2.getLocalName();
        String qname = e2.getTagName();
        if (local2 == null) {
            local2 = qname;
        }
        this.receiver.startElement(uri2, local2, qname, this.atts);
        NodeList children = e2.getChildNodes();
        int clen = children.getLength();
        for (int i3 = 0; i3 < clen; i3++) {
            visit(children.item(i3));
        }
        setCurrentLocation(e2);
        this.receiver.endElement(uri2, local2, qname);
        for (int i4 = len - 1; i4 >= 0; i4--) {
            Attr a3 = (Attr) attributes.item(i4);
            String name2 = a3.getName();
            if (name2.startsWith("xmlns")) {
                if (name2.length() == 5) {
                    this.receiver.endPrefixMapping("");
                } else {
                    this.receiver.endPrefixMapping(a3.getLocalName());
                }
            }
        }
    }

    private void visit(Node n2) throws DOMException, SAXException {
        setCurrentLocation(n2);
        switch (n2.getNodeType()) {
            case 1:
                visit((Element) n2);
                break;
            case 3:
            case 4:
                String value = n2.getNodeValue();
                this.receiver.characters(value.toCharArray(), 0, value.length());
                break;
            case 5:
                this.receiver.skippedEntity(n2.getNodeName());
                break;
            case 7:
                ProcessingInstruction pi = (ProcessingInstruction) n2;
                this.receiver.processingInstruction(pi.getTarget(), pi.getData());
                break;
        }
    }

    private void setCurrentLocation(Node currNode) {
        this.currentNode = currNode;
    }

    public Node getCurrentLocation() {
        return this.currentNode;
    }

    @Override // com.sun.xml.internal.bind.unmarshaller.InfosetScanner
    public Object getCurrentElement() {
        return this.currentNode;
    }

    @Override // com.sun.xml.internal.bind.unmarshaller.InfosetScanner
    public LocatorEx getLocator() {
        return this;
    }

    @Override // com.sun.xml.internal.bind.unmarshaller.InfosetScanner
    public void setContentHandler(ContentHandler handler) {
        this.receiver = handler;
    }

    @Override // com.sun.xml.internal.bind.unmarshaller.InfosetScanner
    public ContentHandler getContentHandler() {
        return this.receiver;
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return -1;
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return -1;
    }

    @Override // com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx
    public ValidationEventLocator getLocation() {
        return new ValidationEventLocatorImpl(getCurrentLocation());
    }
}
