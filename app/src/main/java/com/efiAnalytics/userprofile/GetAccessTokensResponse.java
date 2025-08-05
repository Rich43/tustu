package com.efianalytics.userprofile;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAccessTokensResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/GetAccessTokensResponse.class */
public class GetAccessTokensResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected List<RoleToken> _return;

    public List<RoleToken> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList();
        }
        return this._return;
    }
}
