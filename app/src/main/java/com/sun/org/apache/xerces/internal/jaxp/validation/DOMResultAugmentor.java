package com.sun.org.apache.xerces.internal.jaxp.validation;

import com.sun.org.apache.xerces.internal.dom.AttrImpl;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xerces.internal.dom.ElementImpl;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
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
import java.util.MissingResourceException;
import javax.xml.transform.dom.DOMResult;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/jaxp/validation/DOMResultAugmentor.class */
final class DOMResultAugmentor implements DOMDocumentHandler {
    private DOMValidatorHelper fDOMValidatorHelper;
    private Document fDocument;
    private CoreDocumentImpl fDocumentImpl;
    private boolean fStorePSVI;
    private boolean fIgnoreChars;
    private final QName fAttributeQName = new QName();

    public DOMResultAugmentor(DOMValidatorHelper helper) {
        this.fDOMValidatorHelper = helper;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void setDOMResult(DOMResult result) {
        this.fIgnoreChars = false;
        if (result != null) {
            Node target = result.getNode();
            this.fDocument = target.getNodeType() == 9 ? (Document) target : target.getOwnerDocument();
            this.fDocumentImpl = this.fDocument instanceof CoreDocumentImpl ? (CoreDocumentImpl) this.fDocument : null;
            this.fStorePSVI = this.fDocument instanceof PSVIDocumentImpl;
            return;
        }
        this.fDocument = null;
        this.fDocumentImpl = null;
        this.fStorePSVI = false;
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void doctypeDecl(DocumentType node) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void characters(Text node) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void cdata(CDATASection node) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void comment(Comment node) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.jaxp.validation.DOMDocumentHandler
    public void processingInstruction(ProcessingInstruction node) throws XNIException {
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
        Element currentElement = (Element) this.fDOMValidatorHelper.getCurrentElement();
        NamedNodeMap attrMap = currentElement.getAttributes();
        int oldLength = attrMap.getLength();
        if (this.fDocumentImpl != null) {
            for (int i2 = 0; i2 < oldLength; i2++) {
                AttrImpl attr = (AttrImpl) attrMap.item(i2);
                AttributePSVI attrPSVI = (AttributePSVI) attributes.getAugmentations(i2).getItem(Constants.ATTRIBUTE_PSVI);
                if (attrPSVI != null && processAttributePSVI(attr, attrPSVI)) {
                    ((ElementImpl) currentElement).setIdAttributeNode(attr, true);
                }
            }
        }
        int newLength = attributes.getLength();
        if (newLength > oldLength) {
            if (this.fDocumentImpl == null) {
                for (int i3 = oldLength; i3 < newLength; i3++) {
                    attributes.getName(i3, this.fAttributeQName);
                    currentElement.setAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, attributes.getValue(i3));
                }
                return;
            }
            for (int i4 = oldLength; i4 < newLength; i4++) {
                attributes.getName(i4, this.fAttributeQName);
                AttrImpl attr2 = (AttrImpl) this.fDocumentImpl.createAttributeNS(this.fAttributeQName.uri, this.fAttributeQName.rawname, this.fAttributeQName.localpart);
                attr2.setValue(attributes.getValue(i4));
                AttributePSVI attrPSVI2 = (AttributePSVI) attributes.getAugmentations(i4).getItem(Constants.ATTRIBUTE_PSVI);
                if (attrPSVI2 != null && processAttributePSVI(attr2, attrPSVI2)) {
                    ((ElementImpl) currentElement).setIdAttributeNode(attr2, true);
                }
                attr2.setSpecified(false);
                currentElement.setAttributeNode(attr2);
            }
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
    public void characters(XMLString text, Augmentations augs) throws XNIException {
        if (!this.fIgnoreChars) {
            Element currentElement = (Element) this.fDOMValidatorHelper.getCurrentElement();
            currentElement.appendChild(this.fDocument.createTextNode(text.toString()));
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void ignorableWhitespace(XMLString text, Augmentations augs) throws XNIException {
        characters(text, augs);
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endElement(QName element, Augmentations augs) throws XNIException {
        ElementPSVI elementPSVI;
        Node currentElement = this.fDOMValidatorHelper.getCurrentElement();
        if (augs != null && this.fDocumentImpl != null && (elementPSVI = (ElementPSVI) augs.getItem(Constants.ELEMENT_PSVI)) != null) {
            if (this.fStorePSVI) {
                ((PSVIElementNSImpl) currentElement).setPSVI(elementPSVI);
            }
            XSTypeDefinition type = elementPSVI.getMemberTypeDefinition();
            if (type == null) {
                type = elementPSVI.getTypeDefinition();
            }
            ((ElementNSImpl) currentElement).setType(type);
        }
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void startCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endCDATA(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void endDocument(Augmentations augs) throws XNIException {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public void setDocumentSource(XMLDocumentSource source) {
    }

    @Override // com.sun.org.apache.xerces.internal.xni.XMLDocumentHandler
    public XMLDocumentSource getDocumentSource() {
        return null;
    }

    private boolean processAttributePSVI(AttrImpl attr, AttributePSVI attrPSVI) {
        if (this.fStorePSVI) {
            ((PSVIAttrNSImpl) attr).setPSVI(attrPSVI);
        }
        Object type = attrPSVI.getMemberTypeDefinition();
        if (type == null) {
            Object type2 = attrPSVI.getTypeDefinition();
            if (type2 != null) {
                attr.setType(type2);
                return ((XSSimpleType) type2).isIDType();
            }
            return false;
        }
        attr.setType(type);
        return ((XSSimpleType) type).isIDType();
    }
}
