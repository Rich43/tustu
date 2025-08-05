package com.sun.xml.internal.ws.api;

import java.util.List;
import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/ComponentsFeature.class */
public class ComponentsFeature extends WebServiceFeature implements ServiceSharedFeatureMarker {
    private final List<ComponentFeature> componentFeatures;

    public ComponentsFeature(List<ComponentFeature> componentFeatures) {
        this.enabled = true;
        this.componentFeatures = componentFeatures;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ComponentsFeature.class.getName();
    }

    public List<ComponentFeature> getComponentFeatures() {
        return this.componentFeatures;
    }
}
