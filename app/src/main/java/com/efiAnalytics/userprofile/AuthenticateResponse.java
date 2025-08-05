package com.efianalytics.userprofile;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "authenticateResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/AuthenticateResponse.class */
public class AuthenticateResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected AuthenticationResponse _return;

    public AuthenticationResponse getReturn() {
        return this._return;
    }

    public void setReturn(AuthenticationResponse value) {
        this._return = value;
    }
}
