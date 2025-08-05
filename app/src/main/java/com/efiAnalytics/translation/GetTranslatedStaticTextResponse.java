package com.efianalytics.translation;

import com.sun.xml.internal.ws.model.RuntimeModeler;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTranslatedStaticTextResponse", propOrder = {"_return"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetTranslatedStaticTextResponse.class */
public class GetTranslatedStaticTextResponse {

    @XmlElement(name = RuntimeModeler.RETURN)
    protected String _return;

    public String getReturn() {
        return this._return;
    }

    public void setReturn(String value) {
        this._return = value;
    }
}
