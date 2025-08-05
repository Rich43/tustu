package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/MonitoredAttributeBase.class */
public abstract class MonitoredAttributeBase implements MonitoredAttribute {
    String name;
    MonitoredAttributeInfo attributeInfo;

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public abstract Object getValue();

    public MonitoredAttributeBase(String str, MonitoredAttributeInfo monitoredAttributeInfo) {
        this.name = str;
        this.attributeInfo = monitoredAttributeInfo;
    }

    MonitoredAttributeBase(String str) {
        this.name = str;
    }

    void setMonitoredAttributeInfo(MonitoredAttributeInfo monitoredAttributeInfo) {
        this.attributeInfo = monitoredAttributeInfo;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public void clearState() {
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public void setValue(Object obj) {
        if (!this.attributeInfo.isWritable()) {
            throw new IllegalStateException("The Attribute " + this.name + " is not Writable...");
        }
        throw new IllegalStateException("The method implementation is not provided for the attribute " + this.name);
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public MonitoredAttributeInfo getAttributeInfo() {
        return this.attributeInfo;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public String getName() {
        return this.name;
    }
}
