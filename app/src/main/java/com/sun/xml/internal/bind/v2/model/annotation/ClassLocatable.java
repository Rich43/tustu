package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.Location;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/ClassLocatable.class */
public class ClassLocatable<C> implements Locatable {
    private final Locatable upstream;
    private final C clazz;
    private final Navigator<?, C, ?, ?> nav;

    public ClassLocatable(Locatable upstream, C clazz, Navigator<?, C, ?, ?> nav) {
        this.upstream = upstream;
        this.clazz = clazz;
        this.nav = nav;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.nav.getClassLocation(this.clazz);
    }
}
