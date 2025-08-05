package com.sun.xml.internal.bind.v2.model.annotation;

import com.sun.xml.internal.bind.v2.runtime.Location;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/model/annotation/Locatable.class */
public interface Locatable {
    Locatable getUpstream();

    Location getLocation();
}
