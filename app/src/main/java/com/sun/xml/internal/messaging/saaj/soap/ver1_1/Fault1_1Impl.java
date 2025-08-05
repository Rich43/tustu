package com.sun.xml.internal.messaging.saaj.soap.ver1_1;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.FaultElementImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.messaging.saaj.util.LogDomainConstants;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_1/Fault1_1Impl.class */
public class Fault1_1Impl extends FaultImpl {
    protected static final Logger log = Logger.getLogger(LogDomainConstants.SOAP_VER1_1_DOMAIN, "com.sun.xml.internal.messaging.saaj.soap.ver1_1.LocalStrings");

    public Fault1_1Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createFault1_1Name(prefix));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getDetailName() {
        return NameImpl.createDetail1_1Name();
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getFaultCodeName() {
        return NameImpl.createFromUnqualifiedName("faultcode");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getFaultStringName() {
        return NameImpl.createFromUnqualifiedName("faultstring");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected NameImpl getFaultActorName() {
        return NameImpl.createFromUnqualifiedName("faultactor");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected DetailImpl createDetail() {
        return new Detail1_1Impl(((SOAPDocument) getOwnerDocument()).getDocument());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected FaultElementImpl createSOAPFaultElement(String localName) {
        return new FaultElement1_1Impl(((SOAPDocument) getOwnerDocument()).getDocument(), localName);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected void checkIfStandardFaultCode(String faultCode, String uri) throws SOAPException {
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected void finallySetFaultCode(String faultcode) throws SOAPException {
        this.faultCodeElement.addTextNode(faultcode);
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultCode() {
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        return this.faultCodeElement.getValue();
    }

    @Override // javax.xml.soap.SOAPFault
    public Name getFaultCodeAsName() {
        String faultcodeString = getFaultCode();
        if (faultcodeString == null) {
            return null;
        }
        int prefixIndex = faultcodeString.indexOf(58);
        if (prefixIndex == -1) {
            return NameImpl.createFromUnqualifiedName(faultcodeString);
        }
        String prefix = faultcodeString.substring(0, prefixIndex);
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        String nsName = this.faultCodeElement.getNamespaceURI(prefix);
        return NameImpl.createFromQualifiedName(faultcodeString, nsName);
    }

    @Override // javax.xml.soap.SOAPFault
    public QName getFaultCodeAsQName() {
        String faultcodeString = getFaultCode();
        if (faultcodeString == null) {
            return null;
        }
        if (this.faultCodeElement == null) {
            findFaultCodeElement();
        }
        return convertCodeToQName(faultcodeString, this.faultCodeElement);
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultString(String faultString) throws SOAPException {
        if (this.faultStringElement == null) {
            findFaultStringElement();
        }
        if (this.faultStringElement == null) {
            this.faultStringElement = addSOAPFaultElement("faultstring");
        } else {
            this.faultStringElement.removeContents();
            this.faultStringElement.removeAttribute("xml:lang");
        }
        this.faultStringElement.addTextNode(faultString);
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultString() {
        if (this.faultStringElement == null) {
            findFaultStringElement();
        }
        return this.faultStringElement.getValue();
    }

    @Override // javax.xml.soap.SOAPFault
    public Locale getFaultStringLocale() {
        String xmlLangAttr;
        if (this.faultStringElement == null) {
            findFaultStringElement();
        }
        if (this.faultStringElement != null && (xmlLangAttr = this.faultStringElement.getAttributeValue(NameImpl.createFromUnqualifiedName("xml:lang"))) != null) {
            return xmlLangToLocale(xmlLangAttr);
        }
        return null;
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultString(String faultString, Locale locale) throws SOAPException {
        setFaultString(faultString);
        this.faultStringElement.addAttribute(NameImpl.createFromTagName("xml:lang"), localeToXmlLang(locale));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected boolean isStandardFaultElement(String localName) {
        if (localName.equalsIgnoreCase("detail") || localName.equalsIgnoreCase("faultcode") || localName.equalsIgnoreCase("faultstring") || localName.equalsIgnoreCase("faultactor")) {
            return true;
        }
        return false;
    }

    @Override // javax.xml.soap.SOAPFault
    public void appendFaultSubcode(QName subcode) {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "appendFaultSubcode");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public void removeAllFaultSubcodes() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "removeAllFaultSubcodes");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public Iterator getFaultSubcodes() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultSubcodes");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultReasonText(Locale locale) {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultReasonText");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public Iterator getFaultReasonTexts() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultReasonTexts");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public Iterator getFaultReasonLocales() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultReasonLocales");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public void addFaultReasonText(String text, Locale locale) throws SOAPException {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "addFaultReasonText");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultRole() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultRole");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultRole(String uri) {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "setFaultRole");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public String getFaultNode() {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "getFaultNode");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // javax.xml.soap.SOAPFault
    public void setFaultNode(String uri) {
        log.log(Level.SEVERE, "SAAJ0303.ver1_1.msg.op.unsupported.in.SOAP1.1", "setFaultNode");
        throw new UnsupportedOperationException("Not supported in SOAP 1.1");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected QName getDefaultFaultCode() {
        return new QName("http://schemas.xmlsoap.org/soap/envelope/", "Server");
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(SOAPElement element) throws SOAPException {
        String localName = element.getLocalName();
        if ("Detail".equalsIgnoreCase(localName) && hasDetail()) {
            log.severe("SAAJ0305.ver1_2.detail.exists.error");
            throw new SOAPExceptionImpl("Cannot add Detail, Detail already exists");
        }
        return super.addChildElement(element);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected FaultElementImpl createSOAPFaultElement(QName qname) {
        return new FaultElement1_1Impl(((SOAPDocument) getOwnerDocument()).getDocument(), qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
    protected FaultElementImpl createSOAPFaultElement(Name qname) {
        return new FaultElement1_1Impl(((SOAPDocument) getOwnerDocument()).getDocument(), (NameImpl) qname);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl
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
        if ((uri == null || "".equals(uri)) && prefix != null && !"".equals("prefix")) {
            uri = this.faultCodeElement.getNamespaceURI(prefix);
        }
        if (uri == null || "".equals(uri)) {
            if (prefix != null && !"".equals(prefix)) {
                log.log(Level.SEVERE, "SAAJ0307.impl.no.ns.URI", new Object[]{prefix + CallSiteDescriptor.TOKEN_DELIMITER + faultCode});
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

    private boolean standardFaultCode(String faultCode) {
        if (faultCode.equals("VersionMismatch") || faultCode.equals("MustUnderstand") || faultCode.equals("Client") || faultCode.equals("Server") || faultCode.startsWith("VersionMismatch.") || faultCode.startsWith("MustUnderstand.") || faultCode.startsWith("Client.") || faultCode.startsWith("Server.")) {
            return true;
        }
        return false;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.FaultImpl, javax.xml.soap.SOAPFault
    public void setFaultActor(String faultActor) throws SOAPException {
        if (this.faultActorElement == null) {
            findFaultActorElement();
        }
        if (this.faultActorElement != null) {
            this.faultActorElement.detachNode();
        }
        if (faultActor == null) {
            return;
        }
        this.faultActorElement = createSOAPFaultElement(getFaultActorName());
        this.faultActorElement.addTextNode(faultActor);
        if (hasDetail()) {
            insertBefore(this.faultActorElement, this.detail);
        } else {
            addNode(this.faultActorElement);
        }
    }
}
