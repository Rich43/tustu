package com.sun.corba.se.impl.monitoring;

import com.sun.corba.se.spi.monitoring.MonitoredAttribute;
import com.sun.corba.se.spi.monitoring.MonitoredObject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: rt.jar:com/sun/corba/se/impl/monitoring/MonitoredObjectImpl.class */
public class MonitoredObjectImpl implements MonitoredObject {
    private final String name;
    private final String description;
    private Map children = new HashMap();
    private Map monitoredAttributes = new HashMap();
    private MonitoredObject parent = null;

    MonitoredObjectImpl(String str, String str2) {
        this.name = str;
        this.description = str2;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public MonitoredObject getChild(String str) {
        MonitoredObject monitoredObject;
        synchronized (this) {
            monitoredObject = (MonitoredObject) this.children.get(str);
        }
        return monitoredObject;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public Collection getChildren() {
        Collection collectionValues;
        synchronized (this) {
            collectionValues = this.children.values();
        }
        return collectionValues;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public void addChild(MonitoredObject monitoredObject) {
        if (monitoredObject != null) {
            synchronized (this) {
                this.children.put(monitoredObject.getName(), monitoredObject);
                monitoredObject.setParent(this);
            }
        }
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public void removeChild(String str) {
        if (str != null) {
            synchronized (this) {
                this.children.remove(str);
            }
        }
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public synchronized MonitoredObject getParent() {
        return this.parent;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public synchronized void setParent(MonitoredObject monitoredObject) {
        this.parent = monitoredObject;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public MonitoredAttribute getAttribute(String str) {
        MonitoredAttribute monitoredAttribute;
        synchronized (this) {
            monitoredAttribute = (MonitoredAttribute) this.monitoredAttributes.get(str);
        }
        return monitoredAttribute;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public Collection getAttributes() {
        Collection collectionValues;
        synchronized (this) {
            collectionValues = this.monitoredAttributes.values();
        }
        return collectionValues;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public void addAttribute(MonitoredAttribute monitoredAttribute) {
        if (monitoredAttribute != null) {
            synchronized (this) {
                this.monitoredAttributes.put(monitoredAttribute.getName(), monitoredAttribute);
            }
        }
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public void removeAttribute(String str) {
        if (str != null) {
            synchronized (this) {
                this.monitoredAttributes.remove(str);
            }
        }
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public void clearState() {
        synchronized (this) {
            Iterator it = this.monitoredAttributes.values().iterator();
            while (it.hasNext()) {
                ((MonitoredAttribute) it.next()).clearState();
            }
            Iterator it2 = this.children.values().iterator();
            while (it2.hasNext()) {
                ((MonitoredObject) it2.next()).clearState();
            }
        }
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public String getName() {
        return this.name;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredObject
    public String getDescription() {
        return this.description;
    }
}
