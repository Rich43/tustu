package com.efianalytics.translation;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTranslationProposalResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetTranslationProposalResponse.class */
public class GetTranslationProposalResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected TranslationStaticProposed _return;

    public TranslationStaticProposed getReturn() {
        return this._return;
    }

    public void setReturn(TranslationStaticProposed value) {
        this._return = value;
    }
}
