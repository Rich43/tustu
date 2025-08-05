package com.efianalytics.translation;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addUpdateProposedStaticTranslationResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/AddUpdateProposedStaticTranslationResponse.class */
public class AddUpdateProposedStaticTranslationResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected MutationResult _return;

    public MutationResult getReturn() {
        return this._return;
    }

    public void setReturn(MutationResult value) {
        this._return = value;
    }
}
