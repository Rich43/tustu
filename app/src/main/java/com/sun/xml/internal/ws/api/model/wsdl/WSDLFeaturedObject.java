package com.sun.xml.internal.ws.api.model.wsdl;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.WSFeatureList;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/model/wsdl/WSDLFeaturedObject.class */
public interface WSDLFeaturedObject extends WSDLObject {
    @Nullable
    <F extends WebServiceFeature> F getFeature(@NotNull Class<F> cls);

    @NotNull
    WSFeatureList getFeatures();

    void addFeature(@NotNull WebServiceFeature webServiceFeature);
}
