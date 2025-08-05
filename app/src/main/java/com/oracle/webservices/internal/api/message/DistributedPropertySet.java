package com.oracle.webservices.internal.api.message;

import com.sun.istack.internal.Nullable;
import java.util.Map;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/DistributedPropertySet.class */
public interface DistributedPropertySet extends PropertySet {
    @Nullable
    <T extends PropertySet> T getSatellite(Class<T> cls);

    Map<Class<? extends PropertySet>, PropertySet> getSatellites();

    void addSatellite(PropertySet propertySet);

    void addSatellite(Class<? extends PropertySet> cls, PropertySet propertySet);

    void removeSatellite(PropertySet propertySet);

    void copySatelliteInto(MessageContext messageContext);
}
