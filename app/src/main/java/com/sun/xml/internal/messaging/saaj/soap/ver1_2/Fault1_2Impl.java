package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.w3c.dom.DOMException;
import sun.security.x509.CRLReasonCodeExtension;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/Fault1_2Impl.class */
public class Fault1_2Impl extends FaultImpl {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_2_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");
    private static final QName textName = new QName("http://www.w3.org/2003/05/soap-envelope", "Text");
    private final QName valueName;
    private final QName subcodeName;
    private SOAPElement innermostSubCodeElement;

    public Fault1_2Impl(SOAPDocumentImpl ownerDoc, String name, String prefix) {
        super(ownerDoc, NameImpl.createFault1_2Name(name, prefix));
        this.valueName = new QName("http://www.w3.org/2003/05/soap-envelope", "Value", getPrefix());
        this.subcodeName = new QName("http://www.w3.org/2003/05/soap-envelope", "Subcode", getPrefix());
        this.innermostSubCodeElement = null;
    }

    public Fault1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createFault1_2Name(null, prefix));
        this.valueName = new QName("http://www.w3.org/2003/05/soap-envelope", "Value", getPrefix());
        this.subcodeName = new QName("http://www.w3.org/2003/05/soap-envelope", "Subcode", getPrefix());
        this.innermostSubCodeElement = null;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getDetailName() {
        return NameImpl.createSOAP12Name("Detail", getPrefix());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getFaultCodeName() {
        return NameImpl.createSOAP12Name("Code", getPrefix());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getFaultStringName() {
        return getFaultReasonName();
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getFaultActorName() {
        return getFaultRoleName();
    }

    private NameImpl getFaultRoleName() {
        return NameImpl.createSOAP12Name("Role", getPrefix());
    }

    private NameImpl getFaultReasonName() {
        return NameImpl.createSOAP12Name("Reason", getPrefix());
    }

    private NameImpl getFaultReasonTextName() {
        return NameImpl.createSOAP12Name("Text", getPrefix());
    }

    private NameImpl getFaultNodeName() {
        return NameImpl.createSOAP12Name("Node", getPrefix());
    }

    private static NameImpl getXmlLangName() {
        return NameImpl.createXmlName("lang");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected DetailImpl createDetail() {
        return new Detail1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected FaultElementImpl createSOAPFaultElement(String localName) {
        return new FaultElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), localName);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected void checkIfStandardFaultCode(String faultCode, String uri) throws SOAPException {
        QName qname = new QName(uri, faultCode);
        if (SOAPConstants.SOAP_DATAENCODINGUNKNOWN_FAULT.equals(qname) || SOAPConstants.SOAP_MUSTUNDERSTAND_FAULT.equals(qname) || SOAPConstants.SOAP_RECEIVER_FAULT.equals(qname) || SOAPConstants.SOAP_SENDER_FAULT.equals(qname) || SOAPConstants.SOAP_VERSIONMISMATCH_FAULT.equals(qname)) {
            return;
        }
        log.log(Level.SEVERE, "SAAJ0435.ver1_2.code.not.standard", qname);
        throw new SOAPExceptionImpl(((Object) qname) + " is not a standard Code value");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected void finallySetFaultCode(String faultcode) throws SOAPException {
        SOAPElement value = this.faultCodeElement.addChildElement(this.valueName);
        value.addTextNode(faultcode);
    }

    private void findReasonElement() {
        findFaultStringElement();
    }

    @Override // javax.xml.soap.SOAPFault
    public Iterator getFaultReasonTexts() throws SOAPException {
        if (this.faultStringElement == null) {
            findReasonElement();
        }
        Iterator eachTextElement = this.faultStringElement.getChildElements(textName);
        List texts = new ArrayList();
        while (eachTextElement.hasNext()) {
            SOAPElement textElement = (SOAPElement) eachTextElement.next();
            Locale thisLocale = getLocale(textElement);
            if (thisLocale == null) {
                log.severe("SAAJ0431.ver1_2.xml.lang.missing");
                throw new SOAPExceptionImpl("\"xml:lang\" attribute is not present on the Text element");
            }
            texts.add(textElement.getValue());
        }
        if (texts.isEmpty()) {
            log.severe("SAAJ0434.ver1_2.text.element.not.present");
            throw new SOAPExceptionImpl("env:Text must be present inside env:Reason");
        }
        return texts.iterator();
    }

    @Override // javax.xml.soap.SOAPFault
    public void addFaultReasonText(String text, Locale locale) throws SOAPException {
        SOAPElement reasonText;
        if (locale == null) {
            log.severe("SAAJ0430.ver1_2.locale.required");
            throw new SOAPException("locale is required and must not be null");
        }
        if (this.faultStringElement == null) {
            findReasonElement();
        }
        if (this.faultStringElement == null) {
            this.faultStringElement = addSOAPFaultElement("Reason");
            reasonText = this.faultStringElement.addChildElement(getFaultReasonTextName());
        } else {
            removeDefaultFaultString();
            reasonText = getFaultReasonTextElement(locale);
            if (reasonText != null) {
                reasonText.removeContents();
            } else {
                reasonText = this.faultStringElement.addChildElement(getFaultReasonTextName());
            }
        }
        String xmlLang = localeToXmlLang(locale);
        reasonText.addAttribute(getXmlLangName(), xmlLang);
        reasonText.addTextNode(text);
    }

    private void removeDefaultFaultString() throws SOAPException {
        SOAPElement reasonText = getFaultReasonTextElement(Locale.getDefault());
        if (reasonText != null && "Fault string, and possibly fault code, not set".equals(reasonText.getValue())) {
            reasonText.detachNode();
        }
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultReasonText(Locale locale) throws SOAPException {
        SOAPElement textElement;
        if (locale == null) {
            return null;
        }
        if (this.faultStringElement == null) {
            findReasonElement();
        }
        if (this.faultStringElement != null && (textElement = getFaultReasonTextElement(locale)) != null) {
            textElement.normalize();
            return textElement.getFirstChild().getNodeValue();
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPFault
    public Iterator getFaultReasonLocales() throws SOAPException {
        if (this.faultStringElement == null) {
            findReasonElement();
        }
        Iterator eachTextElement = this.faultStringElement.getChildElements(textName);
        List localeSet = new ArrayList();
        while (eachTextElement.hasNext()) {
            SOAPElement textElement = (SOAPElement) eachTextElement.next();
            Locale thisLocale = getLocale(textElement);
            if (thisLocale == null) {
                log.severe("SAAJ0431.ver1_2.xml.lang.missing");
                throw new SOAPExceptionImpl("\"xml:lang\" attribute is not present on the Text element");
            }
            localeSet.add(thisLocale);
        }
        if (localeSet.isEmpty()) {
            log.severe("SAAJ0434.ver1_2.text.element.not.present");
            throw new SOAPExceptionImpl("env:Text elements with mandatory xml:lang attributes must be present inside env:Reason");
        }
        return localeSet.iterator();
    }

    @Override // javax.xml.soap.SOAPFault
    public Locale getFaultStringLocale() {
        Locale locale = null;
        try {
            locale = (Locale) getFaultReasonLocales().next();
        } catch (SOAPException e2) {
        }
        return locale;
    }

    private SOAPElement getFaultReasonTextElement(Locale locale) throws SOAPException {
        Iterator eachTextElement = this.faultStringElement.getChildElements(textName);
        while (eachTextElement.hasNext()) {
            SOAPElement textElement = (SOAPElement) eachTextElement.next();
            Locale thisLocale = getLocale(textElement);
            if (thisLocale == null) {
                log.severe("SAAJ0431.ver1_2.xml.lang.missing");
                throw new SOAPExceptionImpl("\"xml:lang\" attribute is not present on the Text element");
            }
            if (thisLocale.equals(locale)) {
                return textElement;
            }
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultNode() {
        SOAPElement faultNode = findChild(getFaultNodeName());
        if (faultNode == null) {
            return null;
        }
        return faultNode.getValue();
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultNode(String uri) throws SOAPException {
        SOAPElement faultNode = findChild(getFaultNodeName());
        if (faultNode != null) {
            faultNode.detachNode();
        }
        SOAPElement faultNode2 = createSOAPFaultElement(getFaultNodeName()).addTextNode(uri);
        if (getFaultRole() != null) {
            insertBefore(faultNode2, this.faultActorElement);
        } else if (hasDetail()) {
            insertBefore(faultNode2, this.detail);
        } else {
            addNode(faultNode2);
        }
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultRole() {
        return getFaultActor();
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultRole(String uri) throws SOAPException {
        if (this.faultActorElement == null) {
            findFaultActorElement();
        }
        if (this.faultActorElement != null) {
            this.faultActorElement.detachNode();
        }
        this.faultActorElement = createSOAPFaultElement(getFaultActorName());
        this.faultActorElement.addTextNode(uri);
        if (hasDetail()) {
            insertBefore(this.faultActorElement, this.detail);
        } else {
            addNode(this.faultActorElement);
        }
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultCode() {
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        Iterator codeValues = this.faultCodeElement.getChildElements(this.valueName);
        return ((SOAPElement) codeValues.next()).getValue();
    }

    @Override // javax.xml.soap.SOAPFault
    public QName getFaultCodeAsQName() {
        String faultcode = getFaultCode();
        if (faultcode == null) {
            return null;
        }
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        Iterator valueElements = this.faultCodeElement.getChildElements(this.valueName);
        return convertCodeToQName(faultcode, (SOAPElement) valueElements.next());
    }

    @Override // javax.xml.soap.SOAPFault
    public Name getFaultCodeAsName() {
        String faultcode = getFaultCode();
        if (faultcode == null) {
            return null;
        }
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        Iterator valueElements = this.faultCodeElement.getChildElements(this.valueName);
        return NameImpl.convertToName(convertCodeToQName(faultcode, (SOAPElement) valueElements.next()));
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultString() {
        String reason = null;
        try {
            reason = (String) getFaultReasonTexts().next();
        } catch (SOAPException e2) {
        }
        return reason;
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultString(String faultString) throws SOAPException {
        addFaultReasonText(faultString, Locale.getDefault());
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultString(String faultString, Locale locale) throws SOAPException {
        addFaultReasonText(faultString, locale);
    }

    @Override // javax.xml.soap.SOAPFault
    public void appendFaultSubcode(QName subcode) throws SOAPException, DOMException {
        String prefix;
        if (subcode == null) {
            return;
        }
        if (subcode.getNamespaceURI() == null || "".equals(subcode.getNamespaceURI())) {
            log.severe("SAAJ0432.ver1_2.subcode.not.ns.qualified");
            throw new SOAPExceptionImpl("A Subcode must be namespace-qualified");
        }
        if (this.innermostSubCodeElement == null) {
            if (this.faultCodeElement == null) {
                findFaultCodeElement();
            }
            this.innermostSubCodeElement = this.faultCodeElement;
        }
        if (subcode.getPrefix() == null || "".equals(subcode.getPrefix())) {
            prefix = ((ElementImpl) this.innermostSubCodeElement).getNamespacePrefix(subcode.getNamespaceURI());
        } else {
            prefix = subcode.getPrefix();
        }
        if (prefix == null || "".equals(prefix)) {
            prefix = "ns1";
        }
        this.innermostSubCodeElement = this.innermostSubCodeElement.addChildElement(this.subcodeName);
        SOAPElement subcodeValueElement = this.innermostSubCodeElement.addChildElement(this.valueName);
        ((ElementImpl) subcodeValueElement).ensureNamespaceIsDeclared(prefix, subcode.getNamespaceURI());
        subcodeValueElement.addTextNode(prefix + CallSiteDescriptor.TOKEN_DELIMITER + subcode.getLocalPart());
    }

    @Override // javax.xml.soap.SOAPFault
    public void removeAllFaultSubcodes() {
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        Iterator subcodeElements = this.faultCodeElement.getChildElements(this.subcodeName);
        if (subcodeElements.hasNext()) {
            SOAPElement subcode = (SOAPElement) subcodeElements.next();
            subcode.detachNode();
        }
    }

    @Override // javax.xml.soap.SOAPFault
    public Iterator getFaultSubcodes() {
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        final List subcodeList = new ArrayList();
        Iterator childElements = this.faultCodeElement.getChildElements(this.subcodeName);
        while (true) {
            Iterator subcodeElements = childElements;
            if (subcodeElements.hasNext()) {
                SOAPElement currentCodeElement = (ElementImpl) subcodeElements.next();
                Iterator valueElements = currentCodeElement.getChildElements(this.valueName);
                SOAPElement valueElement = (SOAPElement) valueElements.next();
                String code = valueElement.getValue();
                subcodeList.add(convertCodeToQName(code, valueElement));
                childElements = currentCodeElement.getChildElements(this.subcodeName);
            } else {
                return new Iterator() { // from class: com.sun.xml.internal.messaging.saaj.soap.ver1_2.Fault1_2Impl.1
                    Iterator subCodeIter;

                    {
                        this.subCodeIter = subcodeList.iterator();
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        return this.subCodeIter.hasNext();
                    }

                    @Override // java.util.Iterator
                    public Object next() {
                        return this.subCodeIter.next();
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException("Method remove() not supported on SubCodes Iterator");
                    }
                };
            }
        }
    }

    private static Locale getLocale(SOAPElement reasonText) {
        return xmlLangToLocale(reasonText.getAttributeValue(getXmlLangName()));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0407.ver1_2.no.encodingStyle.in.fault");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Fault");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addAttribute(Name name, String value) throws SOAPException {
        if (name.getLocalName().equals("encodingStyle") && name.getURI().equals("http://www.w3.org/2003/05/soap-envelope")) {
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addAttribute(QName name, String value) throws SOAPException {
        if (name.getLocalPart().equals("encodingStyle") && name.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope")) {
            setEncodingStyle(value);
        }
        return super.addAttribute(name, value);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addTextNode(String text) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0416.ver1_2.adding.text.not.legal", getElementQName());
        throw new SOAPExceptionImpl("Adding text to SOAP 1.2 Fault is not legal");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(SOAPElement element) throws SOAPException {
        String localName = element.getLocalName();
        if ("Detail".equalsIgnoreCase(localName)) {
            if (hasDetail()) {
                log.severe("SAAJ0436.ver1_2.detail.exists.error");
                throw new SOAPExceptionImpl("Cannot add Detail, Detail already exists");
            }
            String uri = element.getElementQName().getNamespaceURI();
            if (!uri.equals("http://www.w3.org/2003/05/soap-envelope")) {
                log.severe("SAAJ0437.ver1_2.version.mismatch.error");
                throw new SOAPExceptionImpl("Cannot add Detail, Incorrect SOAP version specified for Detail element");
            }
        }
        if (element instanceof Detail1_2Impl) {
            ElementImpl importedElement = (ElementImpl) importElement(element);
            addNode(importedElement);
            return convertToSoapElement(importedElement);
        }
        return super.addChildElement(element);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected boolean isStandardFaultElement(String localName) {
        if (localName.equalsIgnoreCase("code") || localName.equalsIgnoreCase(CRLReasonCodeExtension.REASON) || localName.equalsIgnoreCase("node") || localName.equalsIgnoreCase(SOAP12NamespaceConstants.ATTR_ACTOR) || localName.equalsIgnoreCase("detail")) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected QName getDefaultFaultCode() {
        return SOAPConstants.SOAP_SENDER_FAULT;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected FaultElementImpl createSOAPFaultElement(QName qname) {
        return new FaultElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected FaultElementImpl createSOAPFaultElement(Name qname) {
        return new FaultElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), (NameImpl) qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl, javax.xml.soap.SOAPFault
    public void setFaultActor(String faultActor) throws SOAPException {
        setFaultRole(faultActor);
    }
}
