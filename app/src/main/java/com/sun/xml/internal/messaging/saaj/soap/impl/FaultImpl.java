package com.sun.xml.internal.messaging.saaj.soap.impl;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import java.util.Locale;
import java.util.logging.Level;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPFaultElement;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import org.w3c.dom.Element;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/impl/FaultImpl.class */
public abstract class FaultImpl extends ElementImpl implements SOAPFault {
    protected SOAPFaultElement faultStringElement;
    protected SOAPFaultElement faultActorElement;
    protected SOAPFaultElement faultCodeElement;
    protected Detail detail;

    protected abstract NameImpl getDetailName();

    protected abstract NameImpl getFaultCodeName();

    protected abstract NameImpl getFaultStringName();

    protected abstract NameImpl getFaultActorName();

    protected abstract DetailImpl createDetail();

    protected abstract FaultElementImpl createSOAPFaultElement(String str);

    protected abstract FaultElementImpl createSOAPFaultElement(QName qName);

    protected abstract FaultElementImpl createSOAPFaultElement(Name name);

    protected abstract void checkIfStandardFaultCode(String str, String str2) throws SOAPException;

    protected abstract void finallySetFaultCode(String str) throws SOAPException;

    protected abstract boolean isStandardFaultElement(String str);

    protected abstract QName getDefaultFaultCode();

    @Override // javax.xml.soap.SOAPFault
    public abstract void setFaultActor(String str) throws SOAPException;

    protected FaultImpl(SOAPDocumentImpl ownerDoc, NameImpl name) {
        super(ownerDoc, name);
    }

    protected void findFaultCodeElement() {
        this.faultCodeElement = (SOAPFaultElement) findChild(getFaultCodeName());
    }

    protected void findFaultActorElement() {
        this.faultActorElement = (SOAPFaultElement) findChild(getFaultActorName());
    }

    protected void findFaultStringElement() {
        this.faultStringElement = (SOAPFaultElement) findChild(getFaultStringName());
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultCode(String faultCode) throws SOAPException {
        setFaultCode(NameImpl.getLocalNameFromTagName(faultCode), NameImpl.getPrefixFromTagName(faultCode), null);
    }

    public void setFaultCode(String faultCode, String prefix, String uri) throws SOAPException {
        if ((prefix == null || "".equals(prefix)) && uri != null && !"".equals(uri)) {
            prefix = getNamespacePrefix(uri);
            if (prefix == null || "".equals(prefix)) {
                prefix = "ns0";
            }
        }
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        if (this.faultCodeElement == null) {
            this.faultCodeElement = addFaultCodeElement();
        } else {
            this.faultCodeElement.removeContents();
        }
        if (uri == null || "".equals(uri)) {
            uri = this.faultCodeElement.getNamespaceURI(prefix);
        }
        if (uri == null || "".equals(uri)) {
            if (prefix != null && !"".equals(prefix)) {
                log.log(Level.SEVERE, "SAAJ0140.impl.no.ns.URI", new Object[]{prefix + CallSiteDescriptor.TOKEN_DELIMITER + faultCode});
                throw new SOAPExceptionImpl("Empty/Null NamespaceURI specified for faultCode \"" + prefix + CallSiteDescriptor.TOKEN_DELIMITER + faultCode + PdfOps.DOUBLE_QUOTE__TOKEN);
            }
            uri = "";
        }
        checkIfStandardFaultCode(faultCode, uri);
        ((FaultElementImpl) this.faultCodeElement).ensureNamespaceIsDeclared(prefix, uri);
        if (prefix == null || "".equals(prefix)) {
            finallySetFaultCode(faultCode);
        } else {
            finallySetFaultCode(prefix + CallSiteDescriptor.TOKEN_DELIMITER + faultCode);
        }
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultCode(Name faultCodeQName) throws SOAPException {
        setFaultCode(faultCodeQName.getLocalName(), faultCodeQName.getPrefix(), faultCodeQName.getURI());
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultCode(QName faultCodeQName) throws SOAPException {
        setFaultCode(faultCodeQName.getLocalPart(), faultCodeQName.getPrefix(), faultCodeQName.getNamespaceURI());
    }

    protected static QName convertCodeToQName(String code, SOAPElement codeContainingElement) {
        int prefixIndex = code.indexOf(58);
        if (prefixIndex == -1) {
            return new QName(code);
        }
        String prefix = code.substring(0, prefixIndex);
        String nsName = ((ElementImpl) codeContainingElement).lookupNamespaceURI(prefix);
        return new QName(nsName, getLocalPart(code), prefix);
    }

    protected void initializeDetail() {
        NameImpl detailName = getDetailName();
        this.detail = (Detail) findChild(detailName);
    }

    @Override // javax.xml.soap.SOAPFault
    public Detail getDetail() {
        if (this.detail == null) {
            initializeDetail();
        }
        if (this.detail != null && this.detail.getParentNode() == null) {
            this.detail = null;
        }
        return this.detail;
    }

    @Override // javax.xml.soap.SOAPFault
    public Detail addDetail() throws SOAPException {
        if (this.detail == null) {
            initializeDetail();
        }
        if (this.detail == null) {
            this.detail = createDetail();
            addNode(this.detail);
            return this.detail;
        }
        throw new SOAPExceptionImpl("Error: Detail already exists");
    }

    @Override // javax.xml.soap.SOAPFault
    public boolean hasDetail() {
        return getDetail() != null;
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultActor() {
        if (this.faultActorElement == null) {
            findFaultActorElement();
        }
        if (this.faultActorElement != null) {
            return this.faultActorElement.getValue();
        }
        return null;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement setElementQName(QName newName) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0146.impl.invalid.name.change.requested", new Object[]{this.elementQName.getLocalPart(), newName.getLocalPart()});
        throw new SOAPException("Cannot change name for " + this.elementQName.getLocalPart() + " to " + newName.getLocalPart());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement convertToSoapElement(Element element) {
        ElementImpl newElement;
        if (element instanceof SOAPFaultElement) {
            return (SOAPElement) element;
        }
        if (element instanceof SOAPElement) {
            SOAPElement soapElement = (SOAPElement) element;
            if (getDetailName().equals(soapElement.getElementName())) {
                return replaceElementWithSOAPElement(element, createDetail());
            }
            String localName = soapElement.getElementName().getLocalName();
            if (isStandardFaultElement(localName)) {
                return replaceElementWithSOAPElement(element, createSOAPFaultElement(soapElement.getElementQName()));
            }
            return soapElement;
        }
        Name elementName = NameImpl.copyElementName(element);
        if (getDetailName().equals(elementName)) {
            newElement = createDetail();
        } else {
            String localName2 = elementName.getLocalName();
            if (isStandardFaultElement(localName2)) {
                newElement = createSOAPFaultElement(elementName);
            } else {
                newElement = (ElementImpl) createElement(elementName);
            }
        }
        return replaceElementWithSOAPElement(element, newElement);
    }

    protected SOAPFaultElement addFaultCodeElement() throws SOAPException {
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        if (this.faultCodeElement == null) {
            this.faultCodeElement = addSOAPFaultElement(getFaultCodeName().getLocalName());
            return this.faultCodeElement;
        }
        throw new SOAPExceptionImpl("Error: Faultcode already exists");
    }

    private SOAPFaultElement addFaultStringElement() throws SOAPException {
        if (this.faultStringElement == null) {
            findFaultStringElement();
        }
        if (this.faultStringElement == null) {
            this.faultStringElement = addSOAPFaultElement(getFaultStringName().getLocalName());
            return this.faultStringElement;
        }
        throw new SOAPExceptionImpl("Error: Faultstring already exists");
    }

    private SOAPFaultElement addFaultActorElement() throws SOAPException {
        if (this.faultActorElement == null) {
            findFaultActorElement();
        }
        if (this.faultActorElement == null) {
            this.faultActorElement = addSOAPFaultElement(getFaultActorName().getLocalName());
            return this.faultActorElement;
        }
        throw new SOAPExceptionImpl("Error: Faultactor already exists");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(Name name) throws SOAPException {
        if (getDetailName().equals(name)) {
            return addDetail();
        }
        if (getFaultCodeName().equals(name)) {
            return addFaultCodeElement();
        }
        if (getFaultStringName().equals(name)) {
            return addFaultStringElement();
        }
        if (getFaultActorName().equals(name)) {
            return addFaultActorElement();
        }
        return super.addElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(QName name) throws SOAPException {
        return addElement(NameImpl.convertToName(name));
    }

    protected FaultElementImpl addSOAPFaultElement(String localName) throws SOAPException {
        FaultElementImpl faultElem = createSOAPFaultElement(localName);
        addNode(faultElem);
        return faultElem;
    }

    protected static Locale xmlLangToLocale(String xmlLang) {
        if (xmlLang == null) {
            return null;
        }
        int index = xmlLang.indexOf(LanguageTag.SEP);
        if (index == -1) {
            index = xmlLang.indexOf("_");
        }
        if (index == -1) {
            return new Locale(xmlLang, "");
        }
        String language = xmlLang.substring(0, index);
        String country = xmlLang.substring(index + 1);
        return new Locale(language, country);
    }

    protected static String localeToXmlLang(Locale locale) {
        String xmlLang = locale.getLanguage();
        String country = locale.getCountry();
        if (!"".equals(country)) {
            xmlLang = xmlLang + LanguageTag.SEP + country;
        }
        return xmlLang;
    }
}
