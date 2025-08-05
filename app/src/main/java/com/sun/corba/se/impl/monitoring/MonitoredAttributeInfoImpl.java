package com.sun.corba.se.impl.monitoring;

import com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo;

/* loaded from: rt.jar:com/sun/corba/se/impl/monitoring/MonitoredAttributeInfoImpl.class */
public class MonitoredAttributeInfoImpl implements MonitoredAttributeInfo {
    private final String description;
    private final Class type;
    private final boolean writableFlag;
    private final boolean statisticFlag;

    MonitoredAttributeInfoImpl(String str, Class cls, boolean z2, boolean z3) {
        this.description = str;
        this.type = cls;
        this.writableFlag = z2;
        this.statisticFlag = z3;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo
    public String getDescription() {
        return this.description;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo
    public Class type() {
        return this.type;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo
    public boolean isWritable() {
        return this.writableFlag;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeInfo
    public boolean isStatistic() {
        return this.statisticFlag;
    }
}
