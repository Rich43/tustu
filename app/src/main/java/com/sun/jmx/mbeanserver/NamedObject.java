package com.sun.jmx.mbeanserver;

import javax.management.DynamicMBean;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.RuntimeOperationsException;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/NamedObject.class */
public class NamedObject {
    private final ObjectName name;
    private final DynamicMBean object;

    public NamedObject(ObjectName objectName, DynamicMBean dynamicMBean) {
        if (objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid name->" + objectName.toString()));
        }
        this.name = objectName;
        this.object = dynamicMBean;
    }

    public NamedObject(String str, DynamicMBean dynamicMBean) throws MalformedObjectNameException {
        ObjectName objectName = new ObjectName(str);
        if (objectName.isPattern()) {
            throw new RuntimeOperationsException(new IllegalArgumentException("Invalid name->" + objectName.toString()));
        }
        this.name = objectName;
        this.object = dynamicMBean;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof NamedObject)) {
            return this.name.equals(((NamedObject) obj).getName());
        }
        return false;
    }

    public int hashCode() {
        return this.name.hashCode();
    }

    public ObjectName getName() {
        return this.name;
    }

    public DynamicMBean getObject() {
        return this.object;
    }
}
