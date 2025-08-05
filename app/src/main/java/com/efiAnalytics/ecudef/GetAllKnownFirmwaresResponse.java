package com.efianalytics.ecudef;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllKnownFirmwaresResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/GetAllKnownFirmwaresResponse.class */
public class GetAllKnownFirmwaresResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected List<FirmwareIdentifier> _return;

    public List<FirmwareIdentifier> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList();
        }
        return this._return;
    }
}
