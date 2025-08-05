package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.model.nav.Navigator;
import com.sun.xml.internal.bind.v2.runtime.Location;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/FieldLocatable.class */
public class FieldLocatable<F> implements Locatable {
    private final Locatable upstream;
    private final F field;
    private final Navigator<?, ?, F, ?> nav;

    public FieldLocatable(Locatable upstream, F field, Navigator<?, ?, F, ?> nav) {
        this.upstream = upstream;
        this.field = field;
        this.nav = nav;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Locatable getUpstream() {
        return this.upstream;
    }

    @Override // com.sun.xml.internal.bind.v2.model.annotation.Locatable
    public Location getLocation() {
        return this.nav.getFieldLocation(this.field);
    }
}
