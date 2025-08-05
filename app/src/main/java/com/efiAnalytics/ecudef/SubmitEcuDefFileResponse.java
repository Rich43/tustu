package com.efianalytics.ecudef;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitEcuDefFileResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/SubmitEcuDefFileResponse.class */
public class SubmitEcuDefFileResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected EcuDefSubmissionResponse _return;

    public EcuDefSubmissionResponse getReturn() {
        return this._return;
    }

    public void setReturn(EcuDefSubmissionResponse value) {
        this._return = value;
    }
}
