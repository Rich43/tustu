package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.dom.AttrImpl;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
import com.sun.org.apache.xerces.internal.dom.DocumentTypeImpl;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.dom.EntityImpl;
import com.sun.org.apache.xerces.internal.dom.NotationImpl;
import com.sun.org.apache.xerces.internal.dom.PSVIAttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.PSVIDocumentImpl;
import com.sun.org.apache.xerces.internal.dom.PSVIElementNSImpl;
import com.sun.org.apache.xerces.internal.impl.Constants;
import com.sun.org.apache.xerces.internal.impl.dv.XSSimpleType;
import com.sun.org.apache.xerces.internal.xni.Augmentations;
import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
import com.sun.org.apache.xerces.internal.xni.QName;
import com.sun.org.apache.xerces.internal.xni.XMLAttributes;
import com.sun.org.apache.xerces.internal.xni.XMLLocator;
import com.sun.org.apache.xerces.internal.xni.XMLResourceIdentifier;
import com.sun.org.apache.xerces.internal.xni.XMLString;
import com.sun.org.apache.xerces.internal.xni.XNIException;
import com.sun.org.apache.xerces.internal.xni.parser.XMLDocumentSource;
import com.sun.org.apache.xerces.internal.xs.AttributePSVI;
import com.sun.org.apache.xerces.internal.xs.ElementPSVI;
import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
import java.util.ArrayList;
import java.util.MissingResourceException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Notation;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/DOMResultBuilder.class */
final class DOMResultBuilder implements DOMDocumentHandler {
    private static final int[] kidOK = new int[13];
    private Document fDocument;
    private CoreDocumentImpl fDocumentImpl;
    private boolean fStorePSVI;
    private Node fTarget;
    private Node fNextSibling;
    private Node fCurrentNode;
    private Node fFragmentRoot;
    private boolean fIgnoreChars;
    private final ArrayList fTargetChildren = new ArrayList();
    private final QName fAttributeQName = new QName();

    static {
        kidOK[9] = 1410;
        int[] iArr = kidOK;
        int[] iArr2 = kidOK;
        int[] iArr3 = kidOK;
        kidOK[1] = 442;
        iArr3[5] = 442;
        iArr2[6] = 442;
        iArr[11] = 442;
        kidOK[2] = 40;
        kidOK[10] = 0;
        kidOK[7] = 0;
        kidOK[8] = 0;
        kidOK[3] = 0;
        kidOK[4] = 0;
        kidOK[12] = 0;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void setDOMResult(DOMResult result) {
        this.fCurrentNode = null;
        this.fFragmentRoot = null;
        this.fIgnoreChars = false;
        this.fTargetChildren.clear();
        if (result != null) {
            this.fTarget = result.getNode();
            this.fNextSibling = result.getNextSibling();
            this.fDocument = this.fTarget.getNodeType() == 9 ? (Document) this.fTarget : this.fTarget.getOwnerDocument();
            this.fDocumentImpl = this.fDocument instanceof CoreDocumentImpl ? (CoreDocumentImpl) this.fDocument : null;
            this.fStorePSVI = this.fDocument instanceof PSVIDocumentImpl;
            return;
        }
        this.fTarget = null;
        this.fNextSibling = null;
        this.fDocument = null;
        this.fDocumentImpl = null;
        this.fStorePSVI = false;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void doctypeDecl(DocumentType node) throws DOMException, MissingResourceException, XNIException {
        if (this.fDocumentImpl != null) {
            DocumentType docType = this.fDocumentImpl.createDocumentType(node.getName(), node.getPublicId(), node.getSystemId());
            String internalSubset = node.getInternalSubset();
            if (internalSubset != null) {
                ((DocumentTypeImpl) docType).setInternalSubset(internalSubset);
            }
            NamedNodeMap oldMap = node.getEntities();
            NamedNodeMap newMap = docType.getEntities();
            int length = oldMap.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                Entity oldEntity = (Entity) oldMap.item(i2);
                EntityImpl newEntity = (EntityImpl) this.fDocumentImpl.createEntity(oldEntity.getNodeName());
                newEntity.setPublicId(oldEntity.getPublicId());
                newEntity.setSystemId(oldEntity.getSystemId());
                newEntity.setNotationName(oldEntity.getNotationName());
                newMap.setNamedItem(newEntity);
            }
            NamedNodeMap oldMap2 = node.getNotations();
            NamedNodeMap newMap2 = docType.getNotations();
            int length2 = oldMap2.getLength();
            for (int i3 = 0; i3 < length2; i3++) {
                Notation oldNotation = (Notation) oldMap2.item(i3);
                NotationImpl newNotation = (NotationImpl) this.fDocumentImpl.createNotation(oldNotation.getNodeName());
                newNotation.setPublicId(oldNotation.getPublicId());
                newNotation.setSystemId(oldNotation.getSystemId());
                newMap2.setNamedItem(newNotation);
            }
            append(docType);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void characters(Text node) throws DOMException, MissingResourceException, XNIException {
        append(this.fDocument.createTextNode(node.getNodeValue()));
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void cdata(CDATASection node) throws DOMException, MissingResourceException, XNIException {
        append(this.fDocument.createCDATASection(node.getNodeValue()));
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void comment(Comment node) throws DOMException, MissingResourceException, XNIException {
        append(this.fDocument.createComment(node.getNodeValue()));
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void processingInstruction(ProcessingInstruction node) throws DOMException, MissingResourceException, XNIException {
        append(this.fDocument.createProcessingInstruction(node.getTarget(), node.getData()));
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void setIgnoringCharacters(boolean ignore) {
        this.fIgnoreChars = ignore;
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startDocument(XMLLocator locator, String encoding, NamespaceContext namespaceContext, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void xmlDecl(String version, String encoding, String standalone, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void doctypeDecl(String rootElement, String publicId, String systemId, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void comment(XMLString text, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void processingInstruction(String target, XMLString data, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startElement(QName element, XMLAttributes attributes, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        Element elem;
        int attrCount = attributes.getLength();
        if (this.fDocumentImpl == null) {
            elem = this.fDocument.createElementNS(element.uri, element.rawname);
            for (int i2 = 0; i2 < attrCount; i2++) {
                attributes.getName(i2, this.fAttributeQName);
                elem.setAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, attributes.getValue(i2));
            }
        } else {
            elem = this.fDocumentImpl.createElementNS(element.uri, element.rawname, element.localpart);
            for (int i3 = 0; i3 < attrCount; i3++) {
                attributes.getName(i3, this.fAttributeQName);
                AttrImpl attr = (AttrImpl) this.fDocumentImpl.createAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, this.fAttributeQName.localpart);
                attr.setValue(attributes.getValue(i3));
                AttributePSVI attrPSVI = (AttributePSVI) attributes.getAugmentations(i3).getItem(Constants.ATTRIBUTE_PSVI);
                if (attrPSVI != null) {
                    if (this.fStorePSVI) {
                        ((PSVIAttrNSImpl) attr).setPSVI(attrPSVI);
                    }
                    Object type = attrPSVI.getMemberTypeDefinition();
                    if (type == null) {
                        Object type2 = attrPSVI.getTypeDefinition();
                        if (type2 != null) {
                            attr.setType(type2);
                            if (((XSSimpleType) type2).isIDType()) {
                                ((ElementImpl) elem).setIdAttributeNode(attr, true);
                            }
                        }
                    } else {
                        attr.setType(type);
                        if (((XSSimpleType) type).isIDType()) {
                            ((ElementImpl) elem).setIdAttributeNode(attr, true);
                        }
                    }
                }
                attr.setSpecified(attributes.isSpecified(i3));
                elem.setAttributeNode(attr);
            }
        }
        append(elem);
        this.fCurrentNode = elem;
        if (this.fFragmentRoot == null) {
            this.fFragmentRoot = elem;
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void emptyElement(QName element, XMLAttributes attributes, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        startElement(element, attributes, augs);
        endElement(element, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startGeneralEntity(String name, XMLResourceIdentifier identifier, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void textDecl(String version, String encoding, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endGeneralEntity(String name, Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void characters(XMLString text, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        if (!this.fIgnoreChars) {
            append(this.fDocument.createTextNode(text.toString()));
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws DOMException, MissingResourceException, XNIException {
        characters(text, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        ElementPSVI elementPSVI;
        if (augs != null && this.fDocumentImpl != null && (elementPSVI = (ElementPSVI) augs.getItem(Constants.ELEMENT_PSVI)) != null) {
            if (this.fStorePSVI) {
                ((PSVIElementNSImpl) this.fCurrentNode).setPSVI(elementPSVI);
            }
            XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
            if (type == null) {
                type = elementPSVI.getTypeDefinition();
            }
            ((ElementNSImpl) this.fCurrentNode).setType(type);
        }
        if (this.fCurrentNode == this.fFragmentRoot) {
            this.fCurrentNode = null;
            this.fFragmentRoot = null;
        } else {
            this.fCurrentNode = this.fCurrentNode.getParentNode();
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws DOMException, XNIException {
        int length = this.fTargetChildren.size();
        if (this.fNextSibling == null) {
            for (int i2 = 0; i2 < length; i2++) {
                this.fTarget.appendChild((Node) this.fTargetChildren.get(i2));
            }
            return;
        }
        for (int i3 = 0; i3 < length; i3++) {
            this.fTarget.insertBefore((Node) this.fTargetChildren.get(i3), this.fNextSibling);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return null;
    }

    private void append(Node node) throws DOMException, MissingResourceException, XNIException {
        if (this.fCurrentNode != null) {
            this.fCurrentNode.appendChild(node);
        } else {
            if ((kidOK[this.fTarget.getNodeType()] & (1 << node.getNodeType())) == 0) {
                String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.DOM_DOMAIN, "HIERARCHY_REQUEST_ERR", null);
                throw new XNIException(msg);
            }
            this.fTargetChildren.add(node);
        }
    }
}
