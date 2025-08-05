package com.sun.xml.internal.messaging.saaj.soap.ver1_2;

import com.sun.xml.internal.messaging.saaj.SOAPExceptionImpl;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocument;
import com.sun.xml.internal.messaging.saaj.soap.SOAPDocumentImpl;
import com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl;
import com.sun.xml.internal.messaging.saaj.soap.name.NameImpl;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/soap/ver1_2/Detail1_2Impl.class */
public class Detail1_2Impl extends DetailImpl {
    protected static final Logger log = Logger.getLogger(Detail1_2Impl.class.getName(), "com.sun.xml.internal.messaging.saaj.soap.ver1_2.LocalStrings");

    public Detail1_2Impl(SOAPDocumentImpl ownerDocument, String prefix) {
        super(ownerDocument, NameImpl.createSOAP12Name("Detail", prefix));
    }

    public Detail1_2Impl(SOAPDocumentImpl ownerDocument) {
        super(ownerDocument, NameImpl.createSOAP12Name("Detail"));
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl
    protected DetailEntry createDetailEntry(Name name) {
        return new DetailEntry1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.DetailImpl
    protected DetailEntry createDetailEntry(QName name) {
        return new DetailEntry1_2Impl(((SOAPDocument) getOwnerDocument()).getDocument(), name);
    }

    @Override // com.sun.xml.internal.messaging.saaj.soap.impl.ElementImpl, javax.xml.soap.SOAPElement
    public void setEncodingStyle(String encodingStyle) throws SOAPException {
        log.severe("SAAJ0403.ver1_2.no.encodingStyle.in.detail");
        throw new SOAPExceptionImpl("EncodingStyle attribute cannot appear in Detail");
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
}
