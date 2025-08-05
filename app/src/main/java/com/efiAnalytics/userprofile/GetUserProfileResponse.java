package com.efianalytics.userprofile;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserProfileResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/GetUserProfileResponse.class */
public class GetUserProfileResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected UserProfile _return;

    public UserProfile getReturn() {
        return this._return;
    }

    public void setReturn(UserProfile value) {
        this._return = value;
    }
}
