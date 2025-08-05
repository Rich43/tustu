package com.sun.xml.internal.ws.api;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/WSFeatureList.class */
public interface WSFeatureList extends Iterable<WebServiceFeature> {
    boolean isEnabled(@NotNull Class<? extends WebServiceFeature> cls);

    @Nullable
    <F extends WebServiceFeature> F get(@NotNull Class<F> cls);

    @NotNull
    WebServiceFeature[] toArray();

    void mergeFeatures(@NotNull WebServiceFeature[] webServiceFeatureArr, boolean z2);

    void mergeFeatures(@NotNull Iterable<WebServiceFeature> iterable, boolean z2);
}
