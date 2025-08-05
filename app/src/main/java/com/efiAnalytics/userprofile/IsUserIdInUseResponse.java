package com.efianalytics.userprofile;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "isUserIdInUseResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/IsUserIdInUseResponse.class */
public class IsUserIdInUseResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected Boolean _return;

    public Boolean isReturn() {
        return this._return;
    }

    public void setReturn(Boolean value) {
        this._return = value;
    }
}
