package com.sun.xml.internal.ws.api;

import com.oracle.webservices.internal.api.message.BaseDistributedPropertySet;
import com.sun.istack.internal.NotNull;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/DistributedPropertySet.class */
public abstract class DistributedPropertySet extends BaseDistributedPropertySet {
    public void addSatellite(@NotNull PropertySet satellite) {
        super.addSatellite((com.oracle.webservices.internal.api.message.PropertySet) satellite);
    }

    public void addSatellite(@NotNull Class keyClass, @NotNull PropertySet satellite) {
        super.addSatellite((Class<? extends com.oracle.webservices.internal.api.message.PropertySet>) keyClass, (com.oracle.webservices.internal.api.message.PropertySet) satellite);
    }

    public void copySatelliteInto(@NotNull DistributedPropertySet r2) {
        super.copySatelliteInto((com.oracle.webservices.internal.api.message.DistributedPropertySet) r2);
    }

    public void removeSatellite(PropertySet satellite) {
        super.removeSatellite((com.oracle.webservices.internal.api.message.PropertySet) satellite);
    }
}
