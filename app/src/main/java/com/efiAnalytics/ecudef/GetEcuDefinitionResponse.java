package com.efianalytics.ecudef;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEcuDefinitionResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/GetEcuDefinitionResponse.class */
public class GetEcuDefinitionResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected ClientEcuDefinitionFile _return;

    public ClientEcuDefinitionFile getReturn() {
        return this._return;
    }

    public void setReturn(ClientEcuDefinitionFile value) {
        this._return = value;
    }
}
