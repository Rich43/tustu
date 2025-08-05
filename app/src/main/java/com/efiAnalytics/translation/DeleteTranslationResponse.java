package com.efianalytics.translation;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteTranslationResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/DeleteTranslationResponse.class */
public class DeleteTranslationResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected MutationResult _return;

    public MutationResult getReturn() {
        return this._return;
    }

    public void setReturn(MutationResult value) {
        this._return = value;
    }
}
