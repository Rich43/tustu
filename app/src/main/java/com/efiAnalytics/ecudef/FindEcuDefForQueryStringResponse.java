package com.efianalytics.ecudef;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findEcuDefForQueryStringResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/FindEcuDefForQueryStringResponse.class */
public class FindEcuDefForQueryStringResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected EcuDefQueryResponse _return;

    public EcuDefQueryResponse getReturn() {
        return this._return;
    }

    public void setReturn(EcuDefQueryResponse value) {
        this._return = value;
    }
}
