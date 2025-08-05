package com.sun.xml.internal.ws.fault;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import com.sun.xml.internal.ws.util.DOMUtil;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = SOAPNamespaceConstants.TAG_FAULT, namespace = "http://schemas.xmlsoap.org/soap/envelope/")
@XmlType(name = "", propOrder = {"faultcode", "faultstring", "faultactor", "detail"})
/* loaded from: rt.jar:com/sun/xml/internal/ws/fault/SOAP11Fault.class */
class SOAP11Fault extends SOAPFaultBuilder {

    @XmlElement(namespace = "")
    private QName faultcode;

    @XmlElement(namespace = "")
    private String faultstring;

    @XmlElement(namespace = "")
    private String faultactor;

    @XmlElement(namespace = "")
    private DetailType detail;

    SOAP11Fault() {
    }

    SOAP11Fault(QName code, String reason, String actor, Element detailObject) {
        this.faultcode = code;
        this.faultstring = reason;
        this.faultactor = actor;
        if (detailObject != null) {
            if ((detailObject.getNamespaceURI() == null || "".equals(detailObject.getNamespaceURI())) && "detail".equals(detailObject.getLocalName())) {
                this.detail = new DetailType();
                for (Element detailEntry : DOMUtil.getChildElements(detailObject)) {
                    this.detail.getDetails().add(detailEntry);
                }
                return;
            }
            this.detail = new DetailType(detailObject);
        }
    }

    SOAP11Fault(SOAPFault fault) {
        this.faultcode = fault.getFaultCodeAsQName();
        this.faultstring = fault.getFaultString();
        this.faultactor = fault.getFaultActor();
        if (fault.getDetail() != null) {
            this.detail = new DetailType();
            Iterator iter = fault.getDetail().getDetailEntries();
            while (iter.hasNext()) {
                Element fd = (Element) iter.next();
                this.detail.getDetails().add(fd);
            }
        }
    }

    QName getFaultcode() {
        return this.faultcode;
    }

    void setFaultcode(QName faultcode) {
        this.faultcode = faultcode;
    }

    @Override // com.sun.xml.internal.ws.fault.SOAPFaultBuilder
    String getFaultString() {
        return this.faultstring;
    }

    void setFaultstring(String faultstring) {
        this.faultstring = faultstring;
    }

    String getFaultactor() {
        return this.faultactor;
    }

    void setFaultactor(String faultactor) {
        this.faultactor = faultactor;
    }

    @Override // com.sun.xml.internal.ws.fault.SOAPFaultBuilder
    DetailType getDetail() {
        return this.detail;
    }

    @Override // com.sun.xml.internal.ws.fault.SOAPFaultBuilder
    void setDetail(DetailType detail) {
        this.detail = detail;
    }

    @Override // com.sun.xml.internal.ws.fault.SOAPFaultBuilder
    protected Throwable getProtocolException() throws DOMException {
        try {
            SOAPFault fault = SOAPVersion.SOAP_11.getSOAPFactory().createFault(this.faultstring, this.faultcode);
            fault.setFaultActor(this.faultactor);
            if (this.detail != null) {
                Detail d2 = fault.addDetail();
                for (Element det : this.detail.getDetails()) {
                    Node n2 = fault.getOwnerDocument().importNode(det, true);
                    d2.appendChild(n2);
                }
            }
            return new ServerSOAPFaultException(fault);
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }
}
