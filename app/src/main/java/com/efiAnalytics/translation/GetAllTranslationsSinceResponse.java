package com.efianalytics.translation;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllTranslationsSinceResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetAllTranslationsSinceResponse.class */
public class GetAllTranslationsSinceResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected List<TranslationsStatic> _return;

    public List<TranslationsStatic> getReturn() {
        if (this._return == null) {
            this._return = new ArrayList();
        }
        return this._return;
    }
}
