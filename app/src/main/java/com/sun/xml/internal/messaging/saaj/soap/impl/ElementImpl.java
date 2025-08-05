package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xml.internal.security.utils.Constants;
import com.sun.org.apache.xml.internal.security.utils.EncryptionConstants;
import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.messaging.saaj.util.NamespaceContextIterator;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/ElementImpl.class */
public class ElementImpl extends ElementNSImpl implements SOAPElement, SOAPBodyElement {
    private AttributeManager encodingStyleAttribute;
    protected QName elementQName;
    public static final String DSIG_NS = "http://www.w3.org/2000/09/xmldsig#".intern();
    public static final String XENC_NS = EncryptionConstants.EncryptionSpecNS.intern();
    public static final String WSU_NS = PolicyConstants.WSU_NAMESPACE_URI.intern();
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_IMPL_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.impl.LocalStrings");
    public static final String XMLNS_URI = "http://www.w3.org/2000/xmlns/".intern();
    public static final String XML_URI = "http://www.w3.org/XML/1998/namespace".intern();

    public ElementImpl(SOAPDocumentImpl ownerDoc, Name name) {
        super(ownerDoc, name.getURI(), name.getQualifiedName(), name.getLocalName());
        this.encodingStyleAttribute = new AttributeManager();
        this.elementQName = NameImpl.convertToQName(name);
    }

    public ElementImpl(SOAPDocumentImpl ownerDoc, QName name) {
        super(ownerDoc, name.getNamespaceURI(), getQualifiedName(name), name.getLocalPart());
        this.encodingStyleAttribute = new AttributeManager();
        this.elementQName = name;
    }

    public ElementImpl(SOAPDocumentImpl ownerDoc, String uri, String qualifiedName) {
        super(ownerDoc, uri, qualifiedName);
        this.encodingStyleAttribute = new AttributeManager();
        this.elementQName = new QName(uri, getLocalPart(qualifiedName), getPrefix(qualifiedName));
    }

    public void ensureNamespaceIsDeclared(String prefix, String uri) throws DOMException {
        String alreadyDeclaredUri = getNamespaceURI(prefix);
        if (alreadyDeclaredUri == null || !alreadyDeclaredUri.equals(uri)) {
            try {
                addNamespaceDeclaration(prefix, uri);
            } catch (SOAPException e2) {
            }
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ParentNode, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public Document getOwnerDocument() {
        Document doc = super.getOwnerDocument();
        if (doc instanceof SOAPDocument) {
            return ((SOAPDocument) doc).getDocument();
        }
        return doc;
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(Name name) throws SOAPException {
        return addElement(name);
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(QName qname) throws SOAPException {
        return addElement(qname);
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(String localName) throws DOMException, SOAPException {
        Name nameCreateFromUnqualifiedName;
        String nsUri = getNamespaceURI("");
        if (nsUri == null || nsUri.isEmpty()) {
            nameCreateFromUnqualifiedName = NameImpl.createFromUnqualifiedName(localName);
        } else {
            nameCreateFromUnqualifiedName = NameImpl.createFromQualifiedName(localName, nsUri);
        }
        Name name = nameCreateFromUnqualifiedName;
        return addChildElement(name);
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(String localName, String prefix) throws DOMException, SOAPException {
        String uri = getNamespaceURI(prefix);
        if (uri == null) {
            log.log(Level.SEVERE, "SAAJ0101.impl.parent.of.body.elem.mustbe.body", (Object[]) new String[]{prefix});
            throw new SOAPExceptionImpl("Unable to locate namespace for prefix " + prefix);
        }
        return addChildElement(localName, prefix, uri);
    }

    @Override // javax.xml.soap.SOAPElement
    public String getNamespaceURI(String prefix) throws DOMException {
        if ("xmlns".equals(prefix)) {
            return XMLNS_URI;
        }
        if ("xml".equals(prefix)) {
            return XML_URI;
        }
        if ("".equals(prefix)) {
            Node parentNode = this;
            while (true) {
                Node currentAncestor = parentNode;
                if (currentAncestor != null && !(currentAncestor instanceof Document)) {
                    if (currentAncestor instanceof ElementImpl) {
                        ((ElementImpl) currentAncestor).getElementQName();
                        if (((Element) currentAncestor).hasAttributeNS(XMLNS_URI, "xmlns")) {
                            String uri = ((Element) currentAncestor).getAttributeNS(XMLNS_URI, "xmlns");
                            if ("".equals(uri)) {
                                return null;
                            }
                            return uri;
                        }
                    }
                    parentNode = currentAncestor.getParentNode();
                } else {
                    return null;
                }
            }
        } else if (prefix != null) {
            Node parentNode2 = this;
            while (true) {
                Node currentAncestor2 = parentNode2;
                if (currentAncestor2 != null && !(currentAncestor2 instanceof Document)) {
                    if (((Element) currentAncestor2).hasAttributeNS(XMLNS_URI, prefix)) {
                        return ((Element) currentAncestor2).getAttributeNS(XMLNS_URI, prefix);
                    }
                    parentNode2 = currentAncestor2.getParentNode();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }

    public SOAPElement setElementQName(QName newName) throws SOAPException {
        ElementImpl copy = new ElementImpl((SOAPDocumentImpl) getOwnerDocument(), newName);
        return replaceElementWithSOAPElement(this, copy);
    }

    @Override // javax.xml.soap.SOAPElement
    public QName createQName(String localName, String prefix) throws DOMException, SOAPException {
        String uri = getNamespaceURI(prefix);
        if (uri == null) {
            log.log(Level.SEVERE, "SAAJ0102.impl.cannot.locate.ns", new Object[]{prefix});
            throw new SOAPException("Unable to locate namespace for prefix " + prefix);
        }
        return new QName(uri, localName, prefix);
    }

    public String getNamespacePrefix(String uri) {
        NamespaceContextIterator eachNamespace = getNamespaceContextNodes();
        while (eachNamespace.hasNext()) {
            Attr namespaceDecl = eachNamespace.nextNamespaceAttr();
            if (namespaceDecl.getNodeValue().equals(uri)) {
                String candidatePrefix = namespaceDecl.getLocalName();
                if ("xmlns".equals(candidatePrefix)) {
                    return "";
                }
                return candidatePrefix;
            }
        }
        Node parentNode = this;
        while (true) {
            Node currentAncestor = parentNode;
            if (currentAncestor != null && !(currentAncestor instanceof Document)) {
                if (uri.equals(currentAncestor.getNamespaceURI())) {
                    return currentAncestor.getPrefix();
                }
                parentNode = currentAncestor.getParentNode();
            } else {
                return null;
            }
        }
    }

    protected Attr getNamespaceAttr(String prefix) {
        NamespaceContextIterator eachNamespace = getNamespaceContextNodes();
        if (!"".equals(prefix)) {
            prefix = CallSiteDescriptor.TOKEN_DELIMITER + prefix;
        }
        while (eachNamespace.hasNext()) {
            Attr namespaceDecl = eachNamespace.nextNamespaceAttr();
            if (!"".equals(prefix)) {
                if (namespaceDecl.getNodeName().endsWith(prefix)) {
                    return namespaceDecl;
                }
            } else if (namespaceDecl.getNodeName().equals("xmlns")) {
                return namespaceDecl;
            }
        }
        return null;
    }

    public NamespaceContextIterator getNamespaceContextNodes() {
        return getNamespaceContextNodes(true);
    }

    public NamespaceContextIterator getNamespaceContextNodes(boolean traverseStack) {
        return new NamespaceContextIterator(this, traverseStack);
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(String localName, String prefix, String uri) throws SOAPException {
        SOAPElement newElement = createElement(NameImpl.create(localName, prefix, uri));
        addNode(newElement);
        return convertToSoapElement(newElement);
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(SOAPElement element) throws SOAPException, DOMException {
        String elementURI = element.getElementName().getURI();
        String localName = element.getLocalName();
        if ("http://schemas.xmlsoap.org/soap/envelope/".equals(elementURI) || "http://www.w3.org/2003/05/soap-envelope".equals(elementURI)) {
            if ("Envelope".equalsIgnoreCase(localName) || "Header".equalsIgnoreCase(localName) || "Body".equalsIgnoreCase(localName)) {
                log.severe("SAAJ0103.impl.cannot.add.fragements");
                throw new SOAPExceptionImpl("Cannot add fragments which contain elements which are in the SOAP namespace");
            }
            if (SOAPNamespaceConstants.TAG_FAULT.equalsIgnoreCase(localName) && !"Body".equalsIgnoreCase(getLocalName())) {
                log.severe("SAAJ0154.impl.adding.fault.to.nonbody");
                throw new SOAPExceptionImpl("Cannot add a SOAPFault as a child of " + getLocalName());
            }
            if ("Detail".equalsIgnoreCase(localName) && !SOAPNamespaceConstants.TAG_FAULT.equalsIgnoreCase(getLocalName())) {
                log.severe("SAAJ0155.impl.adding.detail.nonfault");
                throw new SOAPExceptionImpl("Cannot add a Detail as a child of " + getLocalName());
            }
            if (SOAPNamespaceConstants.TAG_FAULT.equalsIgnoreCase(localName)) {
                if (!elementURI.equals(getElementName().getURI())) {
                    log.severe("SAAJ0158.impl.version.mismatch.fault");
                    throw new SOAPExceptionImpl("SOAP Version mismatch encountered when trying to add SOAPFault to SOAPBody");
                }
                Iterator it = getChildElements();
                if (it.hasNext()) {
                    log.severe("SAAJ0156.impl.adding.fault.error");
                    throw new SOAPExceptionImpl("Cannot add SOAPFault as a child of a non-Empty SOAPBody");
                }
            }
        }
        String encodingStyle = element.getEncodingStyle();
        ElementImpl importedElement = (ElementImpl) importElement(element);
        addNode(importedElement);
        if (encodingStyle != null) {
            importedElement.setEncodingStyle(encodingStyle);
        }
        return convertToSoapElement(importedElement);
    }

    protected Element importElement(Element element) {
        Document document = getOwnerDocument();
        Document oldDocument = element.getOwnerDocument();
        if (!oldDocument.equals(document)) {
            return (Element) document.importNode(element, true);
        }
        return element;
    }

    protected SOAPElement addElement(Name name) throws SOAPException {
        SOAPElement newElement = createElement(name);
        addNode(newElement);
        return newElement;
    }

    protected SOAPElement addElement(QName name) throws SOAPException {
        SOAPElement newElement = createElement(name);
        addNode(newElement);
        return newElement;
    }

    protected SOAPElement createElement(Name name) {
        if (isNamespaceQualified(name)) {
            return (SOAPElement) getOwnerDocument().createElementNS(name.getURI(), name.getQualifiedName());
        }
        return (SOAPElement) getOwnerDocument().createElement(name.getQualifiedName());
    }

    protected SOAPElement createElement(QName name) {
        if (isNamespaceQualified(name)) {
            return (SOAPElement) getOwnerDocument().createElementNS(name.getNamespaceURI(), getQualifiedName(name));
        }
        return (SOAPElement) getOwnerDocument().createElement(getQualifiedName(name));
    }

    protected void addNode(Node newElement) throws SOAPException {
        insertBefore(newElement, null);
        if (!(getOwnerDocument() instanceof DocumentFragment) && (newElement instanceof ElementImpl)) {
            ElementImpl element = (ElementImpl) newElement;
            QName elementName = element.getElementQName();
            if (!"".equals(elementName.getNamespaceURI())) {
                element.ensureNamespaceIsDeclared(elementName.getPrefix(), elementName.getNamespaceURI());
            }
        }
    }

    protected SOAPElement findChild(NameImpl name) {
        Iterator eachChild = getChildElementNodes();
        while (eachChild.hasNext()) {
            SOAPElement child = (SOAPElement) eachChild.next();
            if (child.getElementName().equals(name)) {
                return child;
            }
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addTextNode(String text) throws SOAPException {
        if (text.startsWith("<![CDATA[") || text.startsWith("<![cdata[")) {
            return addCDATA(text.substring("<![CDATA[".length(), text.length() - 3));
        }
        return addText(text);
    }

    protected SOAPElement addCDATA(String text) throws DOMException, SOAPException {
        Text cdata = getOwnerDocument().createCDATASection(text);
        addNode(cdata);
        return this;
    }

    protected SOAPElement addText(String text) throws SOAPException {
        Text textNode = getOwnerDocument().createTextNode(text);
        addNode(textNode);
        return this;
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addAttribute(Name name, String value) throws DOMException, SOAPException {
        addAttributeBare(name, value);
        if (!"".equals(name.getURI())) {
            ensureNamespaceIsDeclared(name.getPrefix(), name.getURI());
        }
        return this;
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addAttribute(QName qname, String value) throws DOMException, SOAPException {
        addAttributeBare(qname, value);
        if (!"".equals(qname.getNamespaceURI())) {
            ensureNamespaceIsDeclared(qname.getPrefix(), qname.getNamespaceURI());
        }
        return this;
    }

    private void addAttributeBare(Name name, String value) {
        addAttributeBare(name.getURI(), name.getPrefix(), name.getQualifiedName(), value);
    }

    private void addAttributeBare(QName name, String value) {
        addAttributeBare(name.getNamespaceURI(), name.getPrefix(), getQualifiedName(name), value);
    }

    private void addAttributeBare(String uri, String prefix, String qualifiedName, String value) {
        String uri2 = uri.length() == 0 ? null : uri;
        if (qualifiedName.equals("xmlns")) {
            uri2 = XMLNS_URI;
        }
        if (uri2 == null) {
            setAttribute(qualifiedName, value);
        } else {
            setAttributeNS(uri2, qualifiedName, value);
        }
    }

    @Override // javax.xml.soap.SOAPElement
    public SOAPElement addNamespaceDeclaration(String prefix, String uri) throws SOAPException {
        if (prefix.length() > 0) {
            setAttributeNS(XMLNS_URI, "xmlns:" + prefix, uri);
        } else {
            setAttributeNS(XMLNS_URI, "xmlns", uri);
        }
        return this;
    }

    @Override // javax.xml.soap.SOAPElement
    public String getAttributeValue(Name name) {
        return getAttributeValueFrom(this, name);
    }

    @Override // javax.xml.soap.SOAPElement
    public String getAttributeValue(QName qname) {
        return getAttributeValueFrom(this, qname.getNamespaceURI(), qname.getLocalPart(), qname.getPrefix(), getQualifiedName(qname));
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getAllAttributes() {
        Iterator i2 = getAllAttributesFrom(this);
        ArrayList list = new ArrayList();
        while (i2.hasNext()) {
            Name name = (Name) i2.next();
            if (!"xmlns".equalsIgnoreCase(name.getPrefix())) {
                list.add(name);
            }
        }
        return list.iterator();
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getAllAttributesAsQNames() {
        Iterator i2 = getAllAttributesFrom(this);
        ArrayList list = new ArrayList();
        while (i2.hasNext()) {
            Name name = (Name) i2.next();
            if (!"xmlns".equalsIgnoreCase(name.getPrefix())) {
                list.add(NameImpl.convertToQName(name));
            }
        }
        return list.iterator();
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getNamespacePrefixes() {
        return doGetNamespacePrefixes(false);
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getVisibleNamespacePrefixes() {
        return doGetNamespacePrefixes(true);
    }

    protected Iterator doGetNamespacePrefixes(final boolean deep) {
        return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl.1
            String next = null;
            String last = null;
            NamespaceContextIterator eachNamespace;

            {
                this.eachNamespace = ElementImpl.this.getNamespaceContextNodes(deep);
            }

            void findNext() {
                while (this.next == null && this.eachNamespace.hasNext()) {
                    String attributeKey = this.eachNamespace.nextNamespaceAttr().getNodeName();
                    if (attributeKey.startsWith("xmlns:")) {
                        this.next = attributeKey.substring("xmlns:".length());
                    }
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                findNext();
                return this.next != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                findNext();
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                this.last = this.next;
                this.next = null;
                return this.last;
            }

            @Override // java.util.Iterator
            public void remove() throws DOMException {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                this.eachNamespace.remove();
                this.next = null;
                this.last = null;
            }
        };
    }

    @Override // javax.xml.soap.SOAPElement
    public Name getElementName() {
        return NameImpl.convertToName(this.elementQName);
    }

    @Override // javax.xml.soap.SOAPElement
    public QName getElementQName() {
        return this.elementQName;
    }

    @Override // javax.xml.soap.SOAPElement
    public boolean removeAttribute(Name name) {
        return removeAttribute(name.getURI(), name.getLocalName());
    }

    @Override // javax.xml.soap.SOAPElement
    public boolean removeAttribute(QName name) {
        return removeAttribute(name.getNamespaceURI(), name.getLocalPart());
    }

    private boolean removeAttribute(String uri, String localName) {
        String nonzeroLengthUri = (uri == null || uri.length() == 0) ? null : uri;
        Attr attribute = getAttributeNodeNS(nonzeroLengthUri, localName);
        if (attribute == null) {
            return false;
        }
        removeAttributeNode(attribute);
        return true;
    }

    @Override // javax.xml.soap.SOAPElement
    public boolean removeNamespaceDeclaration(String prefix) {
        Attr declaration = getNamespaceAttr(prefix);
        if (declaration == null) {
            return false;
        }
        try {
            removeAttributeNode(declaration);
            return true;
        } catch (DOMException e2) {
            return true;
        }
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getChildElements() {
        return getChildElementsFrom(this);
    }

    protected SOAPElement convertToSoapElement(Element element) {
        if (element instanceof SOAPElement) {
            return (SOAPElement) element;
        }
        return replaceElementWithSOAPElement(element, (ElementImpl) createElement(NameImpl.copyElementName(element)));
    }

    protected static SOAPElement replaceElementWithSOAPElement(Element element, ElementImpl copy) throws DOMException {
        Iterator eachAttribute = getAllAttributesFrom(element);
        while (eachAttribute.hasNext()) {
            Name name = (Name) eachAttribute.next();
            copy.addAttributeBare(name, getAttributeValueFrom(element, name));
        }
        Iterator eachChild = getChildElementsFrom(element);
        while (eachChild.hasNext()) {
            Node nextChild = (Node) eachChild.next();
            copy.insertBefore(nextChild, null);
        }
        Node parent = element.getParentNode();
        if (parent != null) {
            parent.replaceChild(copy, element);
        }
        return copy;
    }

    protected Iterator getChildElementNodes() {
        return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl.2
            Iterator eachNode;
            Node next = null;
            Node last = null;

            {
                this.eachNode = ElementImpl.this.getChildElements();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.next == null) {
                    while (true) {
                        if (!this.eachNode.hasNext()) {
                            break;
                        }
                        Node node = (Node) this.eachNode.next();
                        if (node instanceof SOAPElement) {
                            this.next = node;
                            break;
                        }
                    }
                }
                return this.next != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (hasNext()) {
                    this.last = this.next;
                    this.next = null;
                    return this.last;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                Node target = this.last;
                this.last = null;
                ElementImpl.this.removeChild(target);
            }
        };
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getChildElements(Name name) {
        return getChildElements(name.getURI(), name.getLocalName());
    }

    @Override // javax.xml.soap.SOAPElement
    public Iterator getChildElements(QName qname) {
        return getChildElements(qname.getNamespaceURI(), qname.getLocalPart());
    }

    private Iterator getChildElements(final String nameUri, final String nameLocal) {
        return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl.3
            Iterator eachElement;
            Node next = null;
            Node last = null;

            {
                this.eachElement = ElementImpl.this.getChildElementNodes();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.next == null) {
                    while (true) {
                        if (!this.eachElement.hasNext()) {
                            break;
                        }
                        Node element = (Node) this.eachElement.next();
                        String elementUri = element.getNamespaceURI();
                        String elementUri2 = elementUri == null ? "" : elementUri;
                        String elementName = element.getLocalName();
                        if (elementUri2.equals(nameUri) && elementName.equals(nameLocal)) {
                            this.next = element;
                            break;
                        }
                    }
                }
                return this.next != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                this.last = this.next;
                this.next = null;
                return this.last;
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                Node target = this.last;
                this.last = null;
                ElementImpl.this.removeChild(target);
            }
        };
    }

    @Override // javax.xml.soap.SOAPElement
    public void removeContents() throws DOMException {
        Node firstChild = getFirstChild();
        while (true) {
            Node currentChild = firstChild;
            if (currentChild != null) {
                Node temp = currentChild.getNextSibling();
                if (currentChild instanceof javax.xml.soap.Node) {
                    ((javax.xml.soap.Node) currentChild).detachNode();
                } else {
                    Node parent = currentChild.getParentNode();
                    if (parent != null) {
                        parent.removeChild(currentChild);
                    }
                }
                firstChild = temp;
            } else {
                return;
            }
        }
    }

    @Override // javax.xml.soap.SOAPElement
    public void setEncodingStyle(String encodingStyle) throws DOMException, SOAPException {
        if (!"".equals(encodingStyle)) {
            try {
                new URI(encodingStyle);
            } catch (URISyntaxException e2) {
                log.log(Level.SEVERE, "SAAJ0105.impl.encoding.style.mustbe.valid.URI", (Object[]) new String[]{encodingStyle});
                throw new IllegalArgumentException("Encoding style (" + encodingStyle + ") should be a valid URI");
            }
        }
        this.encodingStyleAttribute.setValue(encodingStyle);
        tryToFindEncodingStyleAttributeName();
    }

    @Override // javax.xml.soap.SOAPElement
    public String getEncodingStyle() throws DOMException {
        Attr attr;
        String encodingStyle = this.encodingStyleAttribute.getValue();
        if (encodingStyle != null) {
            return encodingStyle;
        }
        String soapNamespace = getSOAPNamespace();
        if (soapNamespace != null && (attr = getAttributeNodeNS(soapNamespace, "encodingStyle")) != null) {
            String encodingStyle2 = attr.getValue();
            try {
                setEncodingStyle(encodingStyle2);
            } catch (SOAPException e2) {
            }
            return encodingStyle2;
        }
        return null;
    }

    @Override // javax.xml.soap.Node
    public String getValue() {
        javax.xml.soap.Node valueNode = getValueNode();
        if (valueNode == null) {
            return null;
        }
        return valueNode.getValue();
    }

    @Override // javax.xml.soap.Node
    public void setValue(String value) throws DOMException {
        Node valueNode = getValueNodeStrict();
        if (valueNode != null) {
            valueNode.setNodeValue(value);
            return;
        }
        try {
            addTextNode(value);
        } catch (SOAPException e2) {
            throw new RuntimeException(e2.getMessage());
        }
    }

    protected Node getValueNodeStrict() {
        Node node = getFirstChild();
        if (node != null) {
            if (node.getNextSibling() == null && node.getNodeType() == 3) {
                return node;
            }
            log.severe("SAAJ0107.impl.elem.child.not.single.text");
            throw new IllegalStateException();
        }
        return null;
    }

    protected javax.xml.soap.Node getValueNode() {
        Iterator i2 = getChildElements();
        while (i2.hasNext()) {
            javax.xml.soap.Node n2 = (javax.xml.soap.Node) i2.next();
            if (n2.getNodeType() == 3 || n2.getNodeType() == 4) {
                normalize();
                return n2;
            }
        }
        return null;
    }

    public void setParentElement(SOAPElement element) throws SOAPException, DOMException {
        if (element == null) {
            log.severe("SAAJ0106.impl.no.null.to.parent.elem");
            throw new SOAPException("Cannot pass NULL to setParentElement");
        }
        element.addChildElement(this);
        findEncodingStyleAttributeName();
    }

    protected void findEncodingStyleAttributeName() throws DOMException, SOAPException {
        String soapNamespacePrefix;
        String soapNamespace = getSOAPNamespace();
        if (soapNamespace != null && (soapNamespacePrefix = getNamespacePrefix(soapNamespace)) != null) {
            setEncodingStyleNamespace(soapNamespace, soapNamespacePrefix);
        }
    }

    protected void setEncodingStyleNamespace(String soapNamespace, String soapNamespacePrefix) throws DOMException, SOAPException {
        Name encodingStyleAttributeName = NameImpl.create("encodingStyle", soapNamespacePrefix, soapNamespace);
        this.encodingStyleAttribute.setName(encodingStyleAttributeName);
    }

    @Override // javax.xml.soap.Node
    public SOAPElement getParentElement() {
        Node parentNode = getParentNode();
        if (parentNode instanceof SOAPDocument) {
            return null;
        }
        return (SOAPElement) parentNode;
    }

    protected String getSOAPNamespace() {
        String antecedentNamespace;
        String soapNamespace = null;
        SOAPElement parentElement = this;
        while (true) {
            SOAPElement antecedent = parentElement;
            if (antecedent == null) {
                break;
            }
            Name antecedentName = antecedent.getElementName();
            antecedentNamespace = antecedentName.getURI();
            if ("http://schemas.xmlsoap.org/soap/envelope/".equals(antecedentNamespace) || "http://www.w3.org/2003/05/soap-envelope".equals(antecedentNamespace)) {
                break;
            }
            parentElement = antecedent.getParentElement();
        }
        soapNamespace = antecedentNamespace;
        return soapNamespace;
    }

    @Override // javax.xml.soap.Node
    public void detachNode() throws DOMException {
        Node parent = getParentNode();
        if (parent != null) {
            parent.removeChild(this);
        }
        this.encodingStyleAttribute.clearNameAndValue();
    }

    public void tryToFindEncodingStyleAttributeName() throws DOMException {
        try {
            findEncodingStyleAttributeName();
        } catch (SOAPException e2) {
        }
    }

    @Override // javax.xml.soap.Node
    public void recycleNode() throws DOMException {
        detachNode();
    }

    /* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/ElementImpl$AttributeManager.class */
    class AttributeManager {
        Name attributeName = null;
        String attributeValue = null;

        AttributeManager() {
        }

        public void setName(Name newName) throws DOMException, SOAPException {
            clearAttribute();
            this.attributeName = newName;
            reconcileAttribute();
        }

        public void clearName() {
            clearAttribute();
            this.attributeName = null;
        }

        public void setValue(String value) throws DOMException, SOAPException {
            this.attributeValue = value;
            reconcileAttribute();
        }

        public Name getName() {
            return this.attributeName;
        }

        public String getValue() {
            return this.attributeValue;
        }

        public void clearNameAndValue() {
            this.attributeName = null;
            this.attributeValue = null;
        }

        private void reconcileAttribute() throws DOMException, SOAPException {
            if (this.attributeName != null) {
                ElementImpl.this.removeAttribute(this.attributeName);
                if (this.attributeValue != null) {
                    ElementImpl.this.addAttribute(this.attributeName, this.attributeValue);
                }
            }
        }

        private void clearAttribute() {
            if (this.attributeName != null) {
                ElementImpl.this.removeAttribute(this.attributeName);
            }
        }
    }

    protected static Attr getNamespaceAttrFrom(Element element, String prefix) {
        NamespaceContextIterator eachNamespace = new NamespaceContextIterator(element);
        while (eachNamespace.hasNext()) {
            Attr namespaceDecl = eachNamespace.nextNamespaceAttr();
            String declaredPrefix = NameImpl.getLocalNameFromTagName(namespaceDecl.getNodeName());
            if (declaredPrefix.equals(prefix)) {
                return namespaceDecl;
            }
        }
        return null;
    }

    protected static Iterator getAllAttributesFrom(Element element) {
        final NamedNodeMap attributes = element.getAttributes();
        return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl.4
            int attributesLength;
            int attributeIndex = 0;
            String currentName;

            {
                this.attributesLength = attributes.getLength();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.attributeIndex < this.attributesLength;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                NamedNodeMap namedNodeMap = attributes;
                int i2 = this.attributeIndex;
                this.attributeIndex = i2 + 1;
                Node current = namedNodeMap.item(i2);
                this.currentName = current.getNodeName();
                String prefix = NameImpl.getPrefixFromTagName(this.currentName);
                if (prefix.length() == 0) {
                    return NameImpl.createFromUnqualifiedName(this.currentName);
                }
                Name attributeName = NameImpl.createFromQualifiedName(this.currentName, current.getNamespaceURI());
                return attributeName;
            }

            @Override // java.util.Iterator
            public void remove() throws DOMException {
                if (this.currentName == null) {
                    throw new IllegalStateException();
                }
                attributes.removeNamedItem(this.currentName);
            }
        };
    }

    protected static String getAttributeValueFrom(Element element, Name name) {
        return getAttributeValueFrom(element, name.getURI(), name.getLocalName(), name.getPrefix(), name.getQualifiedName());
    }

    private static String getAttributeValueFrom(Element element, String uri, String localName, String prefix, String qualifiedName) throws DOMException {
        String nonzeroLengthUri = (uri == null || uri.length() == 0) ? null : uri;
        boolean mustUseGetAttributeNodeNS = nonzeroLengthUri != null;
        if (mustUseGetAttributeNodeNS) {
            if (!element.hasAttributeNS(uri, localName)) {
                return null;
            }
            String attrValue = element.getAttributeNS(nonzeroLengthUri, localName);
            return attrValue;
        }
        Attr attribute = element.getAttributeNode(qualifiedName);
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    protected static Iterator getChildElementsFrom(final Element element) {
        return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl.5
            Node next;
            Node nextNext = null;
            Node last = null;

            {
                this.next = element.getFirstChild();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.next != null) {
                    return true;
                }
                if (this.next == null && this.nextNext != null) {
                    this.next = this.nextNext;
                }
                return this.next != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (hasNext()) {
                    this.last = this.next;
                    this.next = null;
                    if ((element instanceof ElementImpl) && (this.last instanceof Element)) {
                        this.last = ((ElementImpl) element).convertToSoapElement((Element) this.last);
                    }
                    this.nextNext = this.last.getNextSibling();
                    return this.last;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                if (this.last == null) {
                    throw new IllegalStateException();
                }
                Node target = this.last;
                this.last = null;
                element.removeChild(target);
            }
        };
    }

    public static String getQualifiedName(QName name) {
        String qualifiedName;
        String prefix = name.getPrefix();
        String localName = name.getLocalPart();
        if (prefix != null && prefix.length() > 0) {
            qualifiedName = prefix + CallSiteDescriptor.TOKEN_DELIMITER + localName;
        } else {
            qualifiedName = localName;
        }
        return qualifiedName;
    }

    public static String getLocalPart(String qualifiedName) {
        if (qualifiedName == null) {
            throw new IllegalArgumentException("Cannot get local name for a \"null\" qualified name");
        }
        int index = qualifiedName.indexOf(58);
        if (index < 0) {
            return qualifiedName;
        }
        return qualifiedName.substring(index + 1);
    }

    public static String getPrefix(String qualifiedName) {
        if (qualifiedName == null) {
            throw new IllegalArgumentException("Cannot get prefix for a  \"null\" qualified name");
        }
        int index = qualifiedName.indexOf(58);
        if (index < 0) {
            return "";
        }
        return qualifiedName.substring(0, index);
    }

    protected boolean isNamespaceQualified(Name name) {
        return !"".equals(name.getURI());
    }

    protected boolean isNamespaceQualified(QName name) {
        return !"".equals(name.getNamespaceURI());
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ElementImpl, org.w3c.dom.Element
    public void setAttributeNS(String namespaceURI, String qualifiedName, String value) {
        String localName;
        int index = qualifiedName.indexOf(58);
        if (index < 0) {
            localName = qualifiedName;
        } else {
            localName = qualifiedName.substring(index + 1);
        }
        super.setAttributeNS(namespaceURI, qualifiedName, value);
        String tmpURI = getNamespaceURI();
        boolean isIDNS = false;
        if (tmpURI != null && (tmpURI.equals(DSIG_NS) || tmpURI.equals(XENC_NS))) {
            isIDNS = true;
        }
        if (localName.equals(Constants._ATT_ID)) {
            if (namespaceURI == null || namespaceURI.equals("")) {
                setIdAttribute(localName, true);
            } else if (isIDNS || WSU_NS.equals(namespaceURI)) {
                setIdAttributeNS(namespaceURI, localName, true);
            }
        }
    }
}
