package com.efianalytics.userprofile;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "updateUserProfileResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UpdateUserProfileResponse.class */
public class UpdateUserProfileResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected UserChangeResult _return;

    public UserChangeResult getReturn() {
        return this._return;
    }

    public void setReturn(UserChangeResult value) {
        this._return = value;
    }
}
