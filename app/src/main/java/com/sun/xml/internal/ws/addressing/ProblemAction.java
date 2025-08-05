package com.sun.xml.internal.ws.addressing;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ProblemAction", namespace = W3CAddressingConstants.WSA_NAMESPACE_NAME)
/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/ProblemAction.class */
public class ProblemAction {

    @XmlElement(name = "Action", namespace = W3CAddressingConstants.WSA_NAMESPACE_NAME)
    private String action;

    @XmlElement(name = "SoapAction", namespace = W3CAddressingConstants.WSA_NAMESPACE_NAME)
    private String soapAction;

    public ProblemAction() {
    }

    public ProblemAction(String action) {
        this.action = action;
    }

    public ProblemAction(String action, String soapAction) {
        this.action = action;
        this.soapAction = soapAction;
    }

    public String getAction() {
        return this.action;
    }

    public String getSoapAction() {
        return this.soapAction;
    }
}
