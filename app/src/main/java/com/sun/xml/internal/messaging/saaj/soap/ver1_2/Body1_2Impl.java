package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBodyElement;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import org.w3c.dom.Node;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/Body1_2Impl.class */
public class Body1_2Impl extends BodyImpl {
    protected static final Logger log = Logger.getLogger(Body1_2Impl.class.getName(), "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");

    public Body1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createBody1_2Name(prefix));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
    protected NameImpl getFaultName(String name) {
        return NameImpl.createFault1_2Name(name, null);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
    protected SOAPBodyElement createBodyElement(Name name) {
        return new BodyElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
    protected SOAPBodyElement createBodyElement(QName name) {
        return new BodyElement1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
    protected QName getDefaultFaultCode() {
        return SOAPConstants.SOAP_RECEIVER_FAULT;
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl, javax.xml.soap.SOAPBody
    public SOAPFault addFault() throws SOAPException {
        if (hasAnyChildElement()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addFault();
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0401.ver1_2.no.encodingstyle.in.body");
        throw new SOAPExceptionImpl("encodingStyle attribute cannot appear on Body");
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

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
    protected boolean isFault(SOAPElement child) {
        return child.getElementName().getURI().equals("http://www.w3.org/2003/05/soap-envelope") && child.getElementName().getLocalName().equals(SOAPNamespaceConstants.TAG_FAULT);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl
    protected SOAPFault createFaultElement() {
        return new Fault1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), getPrefix());
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl, javax.xml.soap.SOAPBody
    public SOAPBodyElement addBodyElement(Name name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addBodyElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl, javax.xml.soap.SOAPBody
    public SOAPBodyElement addBodyElement(QName name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addBodyElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl, com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(Name name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.BodyImpl, com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl
    protected SOAPElement addElement(QName name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(Name name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addChildElement(name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public SOAPElement addChildElement(QName name) throws SOAPException {
        if (hasFault()) {
            log.severe("SAAJ0402.ver1_2.only.fault.allowed.in.body");
            throw new SOAPExceptionImpl("No other element except Fault allowed in SOAPBody");
        }
        return super.addChildElement(name);
    }

    private boolean hasAnyChildElement() {
        Node firstChild = getFirstChild();
        while (true) {
            Node currentNode = firstChild;
            if (currentNode != null) {
                if (currentNode.getNodeType() == 1) {
                    return true;
                }
                firstChild = currentNode.getNextSibling();
            } else {
                return false;
            }
        }
    }
}
