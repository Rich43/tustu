package com.sun.org.apache.xerces.internal.dom;

import com.sun.org.apache.xerces.internal.impl.dv.xs.XSSimpleTypeDecl;
import com.sun.org.apache.xerces.internal.impl.xs.XSComplexTypeDecl;
import com.sun.org.apache.xerces.internal.util.URI;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.util.MissingResourceException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/dom/ElementNSImpl.class */
public class ElementNSImpl extends ElementImpl {
    static final long serialVersionUID = -9142310625494392642L;
    static final String xmlURI = "http://www.w3.org/XML/1998/namespace";
    protected String namespaceURI;
    protected String localName;
    transient XSTypeDefinition type;

    protected ElementNSImpl() {
    }

    protected ElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName) throws DOMException, MissingResourceException {
        super(ownerDocument, qualifiedName);
        setName(namespaceURI, qualifiedName);
    }

    private void setName(String namespaceURI, String qname) throws MissingResourceException {
        this.namespaceURI = namespaceURI;
        if (namespaceURI != null) {
            this.namespaceURI = namespaceURI.length() == 0 ? null : namespaceURI;
        }
        if (qname == null) {
            String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
            throw new DOMException((short) 14, msg);
        }
        int colon1 = qname.indexOf(58);
        int colon2 = qname.lastIndexOf(58);
        this.ownerDocument.checkNamespaceWF(qname, colon1, colon2);
        if (colon1 < 0) {
            this.localName = qname;
            if (this.ownerDocument.errorChecking) {
                this.ownerDocument.checkQName(null, this.localName);
                if ((qname.equals("xmlns") && (namespaceURI == null || !namespaceURI.equals(NamespaceContext.XMLNS_URI))) || (namespaceURI != null && namespaceURI.equals(NamespaceContext.XMLNS_URI) && !qname.equals("xmlns"))) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                    throw new DOMException((short) 14, msg2);
                }
                return;
            }
            return;
        }
        String prefix = qname.substring(0, colon1);
        this.localName = qname.substring(colon2 + 1);
        if (this.ownerDocument.errorChecking) {
            if (namespaceURI == null || (prefix.equals("xml") && !namespaceURI.equals(NamespaceContext.XML_URI))) {
                String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                throw new DOMException((short) 14, msg3);
            }
            this.ownerDocument.checkQName(prefix, this.localName);
            this.ownerDocument.checkDOMNSErr(prefix, namespaceURI);
        }
    }

    protected ElementNSImpl(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) throws DOMException {
        super(ownerDocument, qualifiedName);
        this.localName = localName;
        this.namespaceURI = namespaceURI;
    }

    protected ElementNSImpl(CoreDocumentImpl ownerDocument, String value) {
        super(ownerDocument, value);
    }

    void rename(String namespaceURI, String qualifiedName) {
        if (needsSyncData()) {
            synchronizeData();
        }
        this.name = qualifiedName;
        setName(namespaceURI, qualifiedName);
        reconcileDefaultAttributes();
    }

    protected void setValues(CoreDocumentImpl ownerDocument, String namespaceURI, String qualifiedName, String localName) {
        this.firstChild = null;
        this.previousSibling = null;
        this.nextSibling = null;
        this.fNodeListCache = null;
        this.attributes = null;
        this.flags = (short) 0;
        setOwnerDocument(ownerDocument);
        needsSyncData(true);
        this.name = qualifiedName;
        this.localName = localName;
        this.namespaceURI = namespaceURI;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getNamespaceURI() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.namespaceURI;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getPrefix() {
        if (needsSyncData()) {
            synchronizeData();
        }
        int index = this.name.indexOf(58);
        if (index < 0) {
            return null;
        }
        return this.name.substring(0, index);
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public void setPrefix(String prefix) throws DOMException, MissingResourceException {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.ownerDocument.errorChecking) {
            if (isReadOnly()) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NO_MODIFICATION_ALLOWED_ERR", null);
                throw new DOMException((short) 7, msg);
            }
            if (prefix != null && prefix.length() != 0) {
                if (!CoreDocumentImpl.isXMLName(prefix, this.ownerDocument.isXML11Version())) {
                    String msg2 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "INVALID_CHARACTER_ERR", null);
                    throw new DOMException((short) 5, msg2);
                }
                if (this.namespaceURI == null || prefix.indexOf(58) >= 0) {
                    String msg3 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                    throw new DOMException((short) 14, msg3);
                }
                if (prefix.equals("xml") && !this.namespaceURI.equals("http://www.w3.org/XML/1998/namespace")) {
                    String msg4 = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "NAMESPACE_ERR", null);
                    throw new DOMException((short) 14, msg4);
                }
            }
        }
        if (prefix != null && prefix.length() != 0) {
            this.name = prefix + CallSiteDescriptor.TOKEN_DELIMITER + this.localName;
        } else {
            this.name = this.localName;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getLocalName() {
        if (needsSyncData()) {
            synchronizeData();
        }
        return this.localName;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ElementImpl, com.sun.org.apache.xerces.internal.dom.NodeImpl, org.w3c.dom.Node
    public String getBaseURI() {
        Attr attrNode;
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.attributes != null && (attrNode = (Attr) this.attributes.getNamedItemNS("http://www.w3.org/XML/1998/namespace", "base")) != null) {
            String uri = attrNode.getNodeValue();
            if (uri.length() != 0) {
                try {
                    uri = new URI(uri).toString();
                    return uri;
                } catch (URI.MalformedURIException e2) {
                    NodeImpl parentOrOwner = parentNode() != null ? parentNode() : this.ownerNode;
                    String parentBaseURI = parentOrOwner != null ? parentOrOwner.getBaseURI() : null;
                    if (parentBaseURI != null) {
                        try {
                            return new URI(new URI(parentBaseURI), uri).toString();
                        } catch (URI.MalformedURIException e3) {
                            return null;
                        }
                    }
                    return null;
                }
            }
        }
        String parentElementBaseURI = parentNode() != null ? parentNode().getBaseURI() : null;
        if (parentElementBaseURI != null) {
            try {
                return new URI(parentElementBaseURI).toString();
            } catch (URI.MalformedURIException e4) {
                return null;
            }
        }
        String baseURI = this.ownerNode != null ? this.ownerNode.getBaseURI() : null;
        if (baseURI != null) {
            try {
                return new URI(baseURI).toString();
            } catch (URI.MalformedURIException e5) {
                return null;
            }
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ElementImpl, org.w3c.dom.TypeInfo
    public String getTypeName() {
        if (this.type != null) {
            if (this.type instanceof XSSimpleTypeDecl) {
                return ((XSSimpleTypeDecl) this.type).getTypeName();
            }
            if (this.type instanceof XSComplexTypeDecl) {
                return ((XSComplexTypeDecl) this.type).getTypeName();
            }
            return null;
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ElementImpl, org.w3c.dom.TypeInfo
    public String getTypeNamespace() {
        if (this.type != null) {
            return this.type.getNamespace();
        }
        return null;
    }

    @Override // com.sun.org.apache.xerces.internal.dom.ElementImpl, org.w3c.dom.TypeInfo
    public boolean isDerivedFrom(String typeNamespaceArg, String typeNameArg, int derivationMethod) {
        if (needsSyncData()) {
            synchronizeData();
        }
        if (this.type != null) {
            if (this.type instanceof XSSimpleTypeDecl) {
                return ((XSSimpleTypeDecl) this.type).isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
            }
            if (this.type instanceof XSComplexTypeDecl) {
                return ((XSComplexTypeDecl) this.type).isDOMDerivedFrom(typeNamespaceArg, typeNameArg, derivationMethod);
            }
            return false;
        }
        return false;
    }

    public void setType(XSTypeDefinition type) {
        this.type = type;
    }
}
