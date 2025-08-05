package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;
import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/BodyImpl.class */
public abstract class BodyImpl extends ElementImpl implements SOAPBody {
    private SOAPFault fault;

    protected abstract NameImpl getFaultName(String str);

    protected abstract boolean isFault(SOAPElement sOAPElement);

    protected abstract SOAPBodyElement createBodyElement(Name name);

    protected abstract SOAPBodyElement createBodyElement(QName qName);

    protected abstract SOAPFault createFaultElement();

    protected abstract QName getDefaultFaultCode();

    protected BodyImpl(SOAPDocumentImpl ownerDoc, NameImpl bodyName) {
        super(ownerDoc, bodyName);
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPFault addFault() throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0110.impl.fault.already.exists");
            throw new SOAPExceptionImpl("Error: Fault already exists");
        }
        this.fault = createFaultElement();
        addNode(this.fault);
        this.fault.setFaultCode(getDefaultFaultCode());
        this.fault.setFaultString("Fault string, and possibly fault code, not set");
        return this.fault;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPFault addFault(Name faultCode, String faultString, Locale locale) throws SOAPException {
        SOAPFault fault = addFault();
        fault.setFaultCode(faultCode);
        fault.setFaultString(faultString, locale);
        return fault;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPFault addFault(QName faultCode, String faultString, Locale locale) throws SOAPException {
        SOAPFault fault = addFault();
        fault.setFaultCode(faultCode);
        fault.setFaultString(faultString, locale);
        return fault;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPFault addFault(Name faultCode, String faultString) throws SOAPException {
        SOAPFault fault = addFault();
        fault.setFaultCode(faultCode);
        fault.setFaultString(faultString);
        return fault;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPFault addFault(QName faultCode, String faultString) throws SOAPException {
        SOAPFault fault = addFault();
        fault.setFaultCode(faultCode);
        fault.setFaultString(faultString);
        return fault;
    }

    void initializeFault() {
        FaultImpl flt = (FaultImpl) findFault();
        this.fault = flt;
    }

    protected SOAPElement findFault() {
        Iterator eachChild = getChildElementNodes();
        while (eachChild.hasNext()) {
            SOAPElement child = (SOAPElement) eachChild.next();
            if (isFault(child)) {
                return child;
            }
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPBody
    public boolean hasFault() {
        initializeFault();
        return this.fault != null;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPFault getFault() {
        if (hasFault()) {
            return this.fault;
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPBodyElement addBodyElement(Name name) throws SOAPException {
        SOAPBodyElement newBodyElement = (SOAPBodyElement) ElementFactory.createNamedElement(((SOAPDocument) getOwnerDocument()).getDocument(), name.getLocalName(), name.getPrefix(), name.getURI());
        if (newBodyElement == null) {
            newBodyElement = createBodyElement(name);
        }
        addNode(newBodyElement);
        return newBodyElement;
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPBodyElement addBodyElement(QName qname) throws SOAPException {
        SOAPBodyElement newBodyElement = (SOAPBodyElement) ElementFactory.createNamedElement(((SOAPDocument) getOwnerDocument()).getDocument(), qname.getLocalPart(), qname.getPrefix(), qname.getNamespaceURI());
        if (newBodyElement == null) {
            newBodyElement = createBodyElement(qname);
        }
        addNode(newBodyElement);
        return newBodyElement;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.Node
    public void setParentElement(SOAPElement element) throws SOAPException, DOMException {
        if (!(element instanceof SOAPEnvelope)) {
            log.severe("SAAJ0111.impl.body.parent.must.be.envelope");
            throw new SOAPException("Parent of SOAPBody has to be a SOAPEnvelope");
        }
        super.setParentElement(element);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(Name name) throws SOAPException {
        return addBodyElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(QName name) throws SOAPException {
        return addBodyElement(name);
    }

    @Override // javax.xml.soap.SOAPBody
    public SOAPBodyElement addDocument(Document document) throws DOMException, SOAPException {
        SOAPBodyElement newBodyElement = null;
        DocumentFragment docFrag = document.createDocumentFragment();
        Element rootElement = document.getDocumentElement();
        if (rootElement != null) {
            docFrag.appendChild(rootElement);
            Document ownerDoc = getOwnerDocument();
            Node replacingNode = ownerDoc.importNode(docFrag, true);
            addNode(replacingNode);
            Iterator i2 = getChildElements(NameImpl.copyElementName(rootElement));
            while (i2.hasNext()) {
                newBodyElement = (SOAPBodyElement) i2.next();
            }
        }
        return newBodyElement;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement convertToSoapElement(Element element) {
        if ((element instanceof SOAPBodyElement) && !element.getClass().equals(ElementImpl.class)) {
            return (SOAPElement) element;
        }
        return replaceElementWithSOAPElement(element, (ElementImpl) createBodyElement(NameImpl.copyElementName(element)));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[]{this.elementQName.getLocalPart(), newName.getLocalPart()});
        throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
    }

    @Override // javax.xml.soap.SOAPBody
    public Document extractContentAsDocument() throws SOAPException {
        javax.xml.soap.Node firstBodyElement;
        Iterator eachChild = getChildElements();
        javax.xml.soap.Node node = null;
        while (true) {
            firstBodyElement = node;
            if (!eachChild.hasNext() || (firstBodyElement instanceof SOAPElement)) {
                break;
            }
            node = (javax.xml.soap.Node) eachChild.next();
        }
        boolean exactlyOneChildElement = true;
        if (firstBodyElement == null) {
            exactlyOneChildElement = false;
        } else {
            Node nextSibling = firstBodyElement.getNextSibling();
            while (true) {
                Node node2 = nextSibling;
                if (node2 == null) {
                    break;
                }
                if (!(node2 instanceof Element)) {
                    nextSibling = node2.getNextSibling();
                } else {
                    exactlyOneChildElement = false;
                    break;
                }
            }
        }
        if (!exactlyOneChildElement) {
            log.log(Level.SEVERE, "SAAJ0250.impl.body.should.have.exactly.one.child");
            throw new SOAPException("Cannot extract Document from body");
        }
        try {
            DocumentBuilderFactory factory = new DocumentBuilderFactoryImpl();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            Element rootElement = (Element) document.importNode(firstBodyElement, true);
            document.appendChild(rootElement);
            firstBodyElement.detachNode();
            return document;
        } catch (Exception e2) {
            log.log(Level.SEVERE, "SAAJ0251.impl.cannot.extract.document.from.body");
            throw new SOAPExceptionImpl("Unable to extract Document from body", e2);
        }
    }
}
