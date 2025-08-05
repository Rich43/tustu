package com.sun.xml.internal.ws.fault;

import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAP12NamespaceConstants;
import com.sun.xml.internal.ws.encoding.soap.streaming.SOAPNamespaceConstants;
import com.sun.xml.internal.ws.util.DOMUtil;
import java.util.Iterator;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import javax.xml.soap.Detail;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.ws.WebServiceException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import sun.security.x509.CRLReasonCodeExtension;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = SOAPNamespaceConstants.TAG_FAULT, namespace = "http://www.w3.org/2003/05/soap-envelope")
@XmlType(name = "", propOrder = {"code", CRLReasonCodeExtension.REASON, "node", SOAP12NamespaceConstants.ATTR_ACTOR, "detail"})
/* loaded from: rt.jar:com/sun/xml/internal/ws/fault/SOAP12Fault.class */
class SOAP12Fault extends SOAPFaultBuilder {

    @XmlTransient
    private static final String ns = "http://www.w3.org/2003/05/soap-envelope";

    @XmlElement(namespace = "http://www.w3.org/2003/05/soap-envelope", name = "Code")
    private CodeType code;

    @XmlElement(namespace = "http://www.w3.org/2003/05/soap-envelope", name = "Reason")
    private ReasonType reason;

    @XmlElement(namespace = "http://www.w3.org/2003/05/soap-envelope", name = "Node")
    private String node;

    @XmlElement(namespace = "http://www.w3.org/2003/05/soap-envelope", name = "Role")
    private String role;

    @XmlElement(namespace = "http://www.w3.org/2003/05/soap-envelope", name = "Detail")
    private DetailType detail;

    SOAP12Fault() {
    }

    SOAP12Fault(CodeType code, ReasonType reason, String node, String role, DetailType detail) {
        this.code = code;
        this.reason = reason;
        this.node = node;
        this.role = role;
        this.detail = detail;
    }

    SOAP12Fault(CodeType code, ReasonType reason, String node, String role, Element detailObject) {
        this.code = code;
        this.reason = reason;
        this.node = node;
        this.role = role;
        if (detailObject != null) {
            if (detailObject.getNamespaceURI().equals("http://www.w3.org/2003/05/soap-envelope") && detailObject.getLocalName().equals("Detail")) {
                this.detail = new DetailType();
                for (Element detailEntry : DOMUtil.getChildElements(detailObject)) {
                    this.detail.getDetails().add(detailEntry);
                }
                return;
            }
            this.detail = new DetailType(detailObject);
        }
    }

    SOAP12Fault(SOAPFault fault) {
        this.code = new CodeType(fault.getFaultCodeAsQName());
        try {
            fillFaultSubCodes(fault);
            this.reason = new ReasonType(fault.getFaultString());
            this.role = fault.getFaultRole();
            this.node = fault.getFaultNode();
            if (fault.getDetail() != null) {
                this.detail = new DetailType();
                Iterator iter = fault.getDetail().getDetailEntries();
                while (iter.hasNext()) {
                    Element fd = (Element) iter.next();
                    this.detail.getDetails().add(fd);
                }
            }
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    SOAP12Fault(QName code, String reason, Element detailObject) {
        this(new CodeType(code), new ReasonType(reason), (String) null, (String) null, detailObject);
    }

    CodeType getCode() {
        return this.code;
    }

    ReasonType getReason() {
        return this.reason;
    }

    String getNode() {
        return this.node;
    }

    String getRole() {
        return this.role;
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
    String getFaultString() {
        return this.reason.texts().get(0).getText();
    }

    @Override // com.sun.xml.internal.ws.fault.SOAPFaultBuilder
    protected Throwable getProtocolException() throws DOMException {
        try {
            SOAPFault fault = SOAPVersion.SOAP_12.getSOAPFactory().createFault();
            if (this.reason != null) {
                for (TextType tt : this.reason.texts()) {
                    fault.setFaultString(tt.getText());
                }
            }
            if (this.code != null) {
                fault.setFaultCode(this.code.getValue());
                fillFaultSubCodes(fault, this.code.getSubcode());
            }
            if (this.detail != null && this.detail.getDetail(0) != null) {
                Detail detail = fault.addDetail();
                for (Node obj : this.detail.getDetails()) {
                    Node n2 = fault.getOwnerDocument().importNode(obj, true);
                    detail.appendChild(n2);
                }
            }
            if (this.node != null) {
                fault.setFaultNode(this.node);
            }
            return new ServerSOAPFaultException(fault);
        } catch (SOAPException e2) {
            throw new WebServiceException(e2);
        }
    }

    private void fillFaultSubCodes(SOAPFault fault, SubcodeType subcode) throws SOAPException {
        if (subcode != null) {
            fault.appendFaultSubcode(subcode.getValue());
            fillFaultSubCodes(fault, subcode.getSubcode());
        }
    }

    private void fillFaultSubCodes(SOAPFault fault) throws SOAPException {
        Iterator subcodes = fault.getFaultSubcodes();
        SubcodeType firstSct = null;
        while (subcodes.hasNext()) {
            QName subcode = (QName) subcodes.next();
            if (firstSct == null) {
                firstSct = new SubcodeType(subcode);
                this.code.setSubcode(firstSct);
            } else {
                SubcodeType nextSct = new SubcodeType(subcode);
                firstSct.setSubcode(nextSct);
                firstSct = nextSct;
            }
        }
    }
}
