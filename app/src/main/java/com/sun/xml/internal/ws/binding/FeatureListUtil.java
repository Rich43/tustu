package com.sun.xml.internal.ws.binding;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/binding/FeatureListUtil.class */
public class FeatureListUtil {
    @NotNull
    public static WebServiceFeatureList mergeList(WebServiceFeatureList... lists) {
        WebServiceFeatureList result = new WebServiceFeatureList();
        for (WebServiceFeatureList list : lists) {
            result.addAll(list);
        }
        return result;
    }

    @Nullable
    public static <F extends WebServiceFeature> F mergeFeature(@NotNull Class<F> cls, @Nullable WebServiceFeatureList webServiceFeatureList, @Nullable WebServiceFeatureList webServiceFeatureList2) throws WebServiceException {
        F f2 = (F) (webServiceFeatureList != null ? webServiceFeatureList.get((Class) cls) : null);
        F f3 = (F) (webServiceFeatureList2 != null ? webServiceFeatureList2.get((Class) cls) : null);
        if (f2 == null) {
            return f3;
        }
        if (f3 == null) {
            return f2;
        }
        if (f2.equals(f3)) {
            return f2;
        }
        throw new WebServiceException(((Object) f2) + ", " + ((Object) f3));
    }

    public static boolean isFeatureEnabled(@NotNull Class<? extends WebServiceFeature> featureType, @Nullable WebServiceFeatureList list1, @Nullable WebServiceFeatureList list2) throws WebServiceException {
        WebServiceFeature mergedFeature = mergeFeature(featureType, list1, list2);
        return mergedFeature != null && mergedFeature.isEnabled();
    }
}
