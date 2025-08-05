package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.org.apache.xalan.internal.templates.Constants;
import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/HeaderImpl.class */
public abstract class HeaderImpl extends ElementImpl implements SOAPHeader {
    protected static final boolean MUST_UNDERSTAND_ONLY = false;

    protected abstract SOAPHeaderElement createHeaderElement(Name name) throws SOAPException;

    protected abstract SOAPHeaderElement createHeaderElement(QName qName) throws SOAPException;

    protected abstract NameImpl getNotUnderstoodName();

    protected abstract NameImpl getUpgradeName();

    protected abstract NameImpl getSupportedEnvelopeName();

    protected HeaderImpl(SOAPDocumentImpl ownerDoc, NameImpl name) {
        super(ownerDoc, name);
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addHeaderElement(Name name) throws SOAPException {
        SOAPElement newHeaderElement = ElementFactory.createNamedElement(((SOAPDocument) getOwnerDocument()).getDocument(), name.getLocalName(), name.getPrefix(), name.getURI());
        if (newHeaderElement == null || !(newHeaderElement instanceof SOAPHeaderElement)) {
            newHeaderElement = createHeaderElement(name);
        }
        String uri = newHeaderElement.getElementQName().getNamespaceURI();
        if (uri == null || "".equals(uri)) {
            log.severe("SAAJ0131.impl.header.elems.ns.qualified");
            throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
        }
        addNode(newHeaderElement);
        return (SOAPHeaderElement) newHeaderElement;
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addHeaderElement(QName name) throws SOAPException {
        SOAPElement newHeaderElement = ElementFactory.createNamedElement(((SOAPDocument) getOwnerDocument()).getDocument(), name.getLocalPart(), name.getPrefix(), name.getNamespaceURI());
        if (newHeaderElement == null || !(newHeaderElement instanceof SOAPHeaderElement)) {
            newHeaderElement = createHeaderElement(name);
        }
        String uri = newHeaderElement.getElementQName().getNamespaceURI();
        if (uri == null || "".equals(uri)) {
            log.severe("SAAJ0131.impl.header.elems.ns.qualified");
            throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
        }
        addNode(newHeaderElement);
        return (SOAPHeaderElement) newHeaderElement;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(Name name) throws SOAPException {
        return addHeaderElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(QName name) throws SOAPException {
        return addHeaderElement(name);
    }

    @Override // javax.xml.soap.SOAPHeader
    public Iterator examineHeaderElements(String actor) {
        return getHeaderElementsForActor(actor, false, false);
    }

    @Override // javax.xml.soap.SOAPHeader
    public Iterator extractHeaderElements(String actor) {
        return getHeaderElementsForActor(actor, true, false);
    }

    protected Iterator getHeaderElementsForActor(String actor, boolean detach, boolean mustUnderstand) {
        if (actor == null || actor.equals("")) {
            log.severe("SAAJ0132.impl.invalid.value.for.actor.or.role");
            throw new IllegalArgumentException("Invalid value for actor or role");
        }
        return getHeaderElements(actor, detach, mustUnderstand);
    }

    protected Iterator getHeaderElements(String actor, boolean detach, boolean mustUnderstand) {
        List elementList = new ArrayList();
        Iterator eachChild = getChildElements();
        Object currentChild = iterate(eachChild);
        while (currentChild != null) {
            if (!(currentChild instanceof SOAPHeaderElement)) {
                currentChild = iterate(eachChild);
            } else {
                HeaderElementImpl currentElement = (HeaderElementImpl) currentChild;
                currentChild = iterate(eachChild);
                boolean isMustUnderstandMatching = !mustUnderstand || currentElement.getMustUnderstand();
                boolean doAdd = false;
                if (actor == null && isMustUnderstandMatching) {
                    doAdd = true;
                } else {
                    String currentActor = currentElement.getActorOrRole();
                    if (currentActor == null) {
                        currentActor = "";
                    }
                    if (currentActor.equalsIgnoreCase(actor) && isMustUnderstandMatching) {
                        doAdd = true;
                    }
                }
                if (doAdd) {
                    elementList.add(currentElement);
                    if (detach) {
                        currentElement.detachNode();
                    }
                }
            }
        }
        return elementList.listIterator();
    }

    private Object iterate(Iterator each) {
        if (each.hasNext()) {
            return each.next();
        }
        return null;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.Node
    public void setParentElement(SOAPElement element) throws SOAPException, DOMException {
        if (!(element instanceof SOAPEnvelope)) {
            log.severe("SAAJ0133.impl.header.parent.mustbe.envelope");
            throw new SOAPException("Parent of SOAPHeader has to be a SOAPEnvelope");
        }
        super.setParentElement(element);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(String localName) throws DOMException, SOAPException {
        SOAPElement element = super.addChildElement(localName);
        String uri = element.getElementName().getURI();
        if (uri == null || "".equals(uri)) {
            log.severe("SAAJ0134.impl.header.elems.ns.qualified");
            throw new SOAPExceptionImpl("HeaderElements must be namespace qualified");
        }
        return element;
    }

    @Override // javax.xml.soap.SOAPHeader
    public Iterator examineAllHeaderElements() {
        return getHeaderElements(null, false, false);
    }

    @Override // javax.xml.soap.SOAPHeader
    public Iterator examineMustUnderstandHeaderElements(String actor) {
        return getHeaderElements(actor, false, true);
    }

    @Override // javax.xml.soap.SOAPHeader
    public Iterator extractAllHeaderElements() {
        return getHeaderElements(null, true, false);
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addUpgradeHeaderElement(Iterator supportedSoapUris) throws SOAPException {
        if (supportedSoapUris == null) {
            log.severe("SAAJ0411.ver1_2.no.null.supportedURIs");
            throw new SOAPException("Argument cannot be null; iterator of supportedURIs cannot be null");
        }
        if (!supportedSoapUris.hasNext()) {
            log.severe("SAAJ0412.ver1_2.no.empty.list.of.supportedURIs");
            throw new SOAPException("List of supported URIs cannot be empty");
        }
        Name upgradeName = getUpgradeName();
        SOAPHeaderElement upgradeHeaderElement = (SOAPHeaderElement) addChildElement(upgradeName);
        Name supportedEnvelopeName = getSupportedEnvelopeName();
        int i2 = 0;
        while (supportedSoapUris.hasNext()) {
            SOAPElement subElement = upgradeHeaderElement.addChildElement(supportedEnvelopeName);
            String ns = Constants.ATTRNAME_NS + Integer.toString(i2);
            subElement.addAttribute(NameImpl.createFromUnqualifiedName(SOAP12NamespaceConstants.ATTR_NOT_UNDERSTOOD_QNAME), ns + ":Envelope");
            subElement.addNamespaceDeclaration(ns, (String) supportedSoapUris.next());
            i2++;
        }
        return upgradeHeaderElement;
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addUpgradeHeaderElement(String supportedSoapUri) throws SOAPException {
        return addUpgradeHeaderElement(new String[]{supportedSoapUri});
    }

    @Override // javax.xml.soap.SOAPHeader
    public SOAPHeaderElement addUpgradeHeaderElement(String[] supportedSoapUris) throws SOAPException {
        if (supportedSoapUris == null) {
            log.severe("SAAJ0411.ver1_2.no.null.supportedURIs");
            throw new SOAPException("Argument cannot be null; array of supportedURIs cannot be null");
        }
        if (supportedSoapUris.length == 0) {
            log.severe("SAAJ0412.ver1_2.no.empty.list.of.supportedURIs");
            throw new SOAPException("List of supported URIs cannot be empty");
        }
        Name upgradeName = getUpgradeName();
        SOAPHeaderElement upgradeHeaderElement = (SOAPHeaderElement) addChildElement(upgradeName);
        Name supportedEnvelopeName = getSupportedEnvelopeName();
        for (int i2 = 0; i2 < supportedSoapUris.length; i2++) {
            SOAPElement subElement = upgradeHeaderElement.addChildElement(supportedEnvelopeName);
            String ns = Constants.ATTRNAME_NS + Integer.toString(i2);
            subElement.addAttribute(NameImpl.createFromUnqualifiedName(SOAP12NamespaceConstants.ATTR_NOT_UNDERSTOOD_QNAME), ns + ":Envelope");
            subElement.addNamespaceDeclaration(ns, supportedSoapUris[i2]);
        }
        return upgradeHeaderElement;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement convertToSoapElement(Element element) {
        if (element instanceof SOAPHeaderElement) {
            return (SOAPElement) element;
        }
        try {
            return replaceElementWithSOAPElement(element, (ElementImpl) createHeaderElement(NameImpl.copyElementName(element)));
        } catch (SOAPException e2) {
            throw new ClassCastException("Could not convert Element to SOAPHeaderElement: " + e2.getMessage());
        }
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[]{this.elementQName.getLocalPart(), newName.getLocalPart()});
        throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
    }
}
