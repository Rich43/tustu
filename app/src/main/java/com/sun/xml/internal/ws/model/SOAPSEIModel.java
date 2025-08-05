package com.sun.xml.internal.ws.model;

import com.sun.xml.internal.ws.api.model.ParameterBinding;
import com.sun.xml.internal.ws.binding.WebServiceFeatureList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javax.jws.WebParam;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/model/SOAPSEIModel.class */
public class SOAPSEIModel extends AbstractSEIModelImpl {
    public SOAPSEIModel(WebServiceFeatureList features) {
        super(features);
    }

    @Override // com.sun.xml.internal.ws.model.AbstractSEIModelImpl
    protected void populateMaps() {
        int emptyBodyCount = 0;
        for (JavaMethodImpl jm : getJavaMethods()) {
            put(jm.getMethod(), jm);
            boolean bodyFound = false;
            for (ParameterImpl p2 : jm.getRequestParameters()) {
                ParameterBinding binding = p2.getBinding();
                if (binding.isBody()) {
                    put(p2.getName(), jm);
                    bodyFound = true;
                }
            }
            if (!bodyFound) {
                put(this.emptyBodyName, jm);
                emptyBodyCount++;
            }
        }
        if (emptyBodyCount > 1) {
        }
    }

    public Set<QName> getKnownHeaders() {
        Set<QName> headers = new HashSet<>();
        for (JavaMethodImpl method : getJavaMethods()) {
            Iterator<ParameterImpl> params = method.getRequestParameters().iterator();
            fillHeaders(params, headers, WebParam.Mode.IN);
            Iterator<ParameterImpl> params2 = method.getResponseParameters().iterator();
            fillHeaders(params2, headers, WebParam.Mode.OUT);
        }
        return headers;
    }

    private void fillHeaders(Iterator<ParameterImpl> params, Set<QName> headers, WebParam.Mode mode) {
        while (params.hasNext()) {
            ParameterImpl param = params.next();
            ParameterBinding binding = mode == WebParam.Mode.IN ? param.getInBinding() : param.getOutBinding();
            QName name = param.getName();
            if (binding.isHeader() && !headers.contains(name)) {
                headers.add(name);
            }
        }
    }
}
