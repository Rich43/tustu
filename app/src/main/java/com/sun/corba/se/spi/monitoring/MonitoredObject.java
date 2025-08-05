package com.sun.corba.se.spi.monitoring;

import java.util.Collection;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/MonitoredObject.class */
public interface MonitoredObject {
    String getName();

    String getDescription();

    void addChild(MonitoredObject monitoredObject);

    void removeChild(String str);

    MonitoredObject getChild(String str);

    Collection getChildren();

    void setParent(MonitoredObject monitoredObject);

    MonitoredObject getParent();

    void addAttribute(MonitoredAttribute monitoredAttribute);

    void removeAttribute(String str);

    MonitoredAttribute getAttribute(String str);

    Collection getAttributes();

    void clearState();
}
