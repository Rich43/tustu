package com.sun.xml.internal.ws.api;

import javax.xml.ws.WebServiceFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/ComponentFeature.class */
public class ComponentFeature extends WebServiceFeature implements ServiceSharedFeatureMarker {
    private final Component component;
    private final Target target;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/ComponentFeature$Target.class */
    public enum Target {
        CONTAINER,
        ENDPOINT,
        SERVICE,
        STUB
    }

    public ComponentFeature(Component component) {
        this(component, Target.CONTAINER);
    }

    public ComponentFeature(Component component, Target target) {
        this.enabled = true;
        this.component = component;
        this.target = target;
    }

    @Override // javax.xml.ws.WebServiceFeature
    public String getID() {
        return ComponentFeature.class.getName();
    }

    public Component getComponent() {
        return this.component;
    }

    public Target getTarget() {
        return this.target;
    }
}
