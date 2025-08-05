package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.runtime.Location;
import java.lang.annotation.Annotation;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/Quick.class */
public abstract class Quick implements Annotation, Locatable, Location {
    private final Locatable upstream;

    protected abstract Annotation getAnnotation();

    protected abstract Quick newInstance(Locatable locatable, Annotation annotation);

    protected Quick(Locatable upstream) {
        this.upstream = upstream;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public final Location getLocation() {
        return this;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public final Locatable getUpstream() {
        return this.upstream;
    }

    @Override // java.lang.annotation.Annotation, com.sun.xml.internal.bind.v2.runtime.Location
    public final String toString() {
        return getAnnotation().toString();
    }
}
