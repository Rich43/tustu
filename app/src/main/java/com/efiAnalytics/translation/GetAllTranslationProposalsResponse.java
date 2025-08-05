package com.efianalytics.translation;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllTranslationProposalsResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetAllTranslationProposalsResponse.class */
public class GetAllTranslationProposalsResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected List<TranslationStaticProposed> _return;

    public List<TranslationStaticProposed> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList();
        }
        return this._return;
    }
}
