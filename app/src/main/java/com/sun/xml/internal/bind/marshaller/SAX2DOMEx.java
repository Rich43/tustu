package com.sun.xml.internal.bind.marshaller;

import com.sun.istack.internal.FinalArrayList;
import com.sun.xml.internal.bind.util.Which;
import com.sun.xml.internal.bind.v2.util.XmlFactory;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/SAX2DOMEx.class */
public class SAX2DOMEx implements ContentHandler {
    private Node node;
    private boolean isConsolidate;
    protected final Stack<Node> nodeStack;
    private final FinalArrayList<String> unprocessedNamespaces;
    protected final Document document;

    public SAX2DOMEx(Node node) {
        this(node, false);
    }

    public SAX2DOMEx(Node node, boolean isConsolidate) {
        this.node = null;
        this.nodeStack = new Stack<>();
        this.unprocessedNamespaces = new FinalArrayList<>();
        this.node = node;
        this.isConsolidate = isConsolidate;
        this.nodeStack.push(this.node);
        if (node instanceof Document) {
            this.document = (Document) node;
        } else {
            this.document = node.getOwnerDocument();
        }
    }

    public SAX2DOMEx(DocumentBuilderFactory f2) throws ParserConfigurationException {
        this.node = null;
        this.nodeStack = new Stack<>();
        this.unprocessedNamespaces = new FinalArrayList<>();
        f2.setValidating(false);
        this.document = f2.newDocumentBuilder().newDocument();
        this.node = this.document;
        this.nodeStack.push(this.document);
    }

    public SAX2DOMEx() throws IllegalStateException, ParserConfigurationException {
        this.node = null;
        this.nodeStack = new Stack<>();
        this.unprocessedNamespaces = new FinalArrayList<>();
        DocumentBuilderFactory factory = XmlFactory.createDocumentBuilderFactory(false);
        factory.setValidating(false);
        this.document = factory.newDocumentBuilder().newDocument();
        this.node = this.document;
        this.nodeStack.push(this.document);
    }

    public final Element getCurrentElement() {
        return (Element) this.nodeStack.peek();
    }

    public Node getDOM() {
        return this.node;
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() {
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() {
    }

    protected void namespace(Element element, String prefix, String uri) throws DOMException {
        String qname;
        if ("".equals(prefix) || prefix == null) {
            qname = "xmlns";
        } else {
            qname = "xmlns:" + prefix;
        }
        if (element.hasAttributeNS("http://www.w3.org/2000/xmlns/", qname)) {
            element.removeAttributeNS("http://www.w3.org/2000/xmlns/", qname);
        }
        element.setAttributeNS("http://www.w3.org/2000/xmlns/", qname, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespace, String localName, String qName, Attributes attrs) throws DOMException {
        Node parent = this.nodeStack.peek();
        Element element = this.document.createElementNS(namespace, qName);
        if (element == null) {
            throw new AssertionError((Object) Messages.format(Messages.DOM_IMPL_DOESNT_SUPPORT_CREATELEMENTNS, this.document.getClass().getName(), Which.which(this.document.getClass())));
        }
        for (int i2 = 0; i2 < this.unprocessedNamespaces.size(); i2 += 2) {
            String prefix = this.unprocessedNamespaces.get(i2);
            String uri = this.unprocessedNamespaces.get(i2 + 1);
            namespace(element, prefix, uri);
        }
        this.unprocessedNamespaces.clear();
        if (attrs != null) {
            int length = attrs.getLength();
            for (int i3 = 0; i3 < length; i3++) {
                String namespaceuri = attrs.getURI(i3);
                String value = attrs.getValue(i3);
                String qname = attrs.getQName(i3);
                element.setAttributeNS(namespaceuri, qname, value);
            }
        }
        parent.appendChild(element);
        this.nodeStack.push(element);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespace, String localName, String qName) {
        this.nodeStack.pop();
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws DOMException {
        characters(new String(ch, start, length));
    }

    protected Text characters(String s2) throws DOMException {
        Text text;
        Node parent = this.nodeStack.peek();
        Node lastChild = parent.getLastChild();
        if (this.isConsolidate && lastChild != null && lastChild.getNodeType() == 3) {
            text = (Text) lastChild;
            text.appendData(s2);
        } else {
            text = this.document.createTextNode(s2);
            parent.appendChild(text);
        }
        return text;
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws DOMException, SAXException {
        Node parent = this.nodeStack.peek();
        Node n2 = this.document.createProcessingInstruction(target, data);
        parent.appendChild(n2);
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        this.unprocessedNamespaces.add(prefix);
        this.unprocessedNamespaces.add(uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }
}
