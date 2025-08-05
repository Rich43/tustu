package com.sun.corba.se.spi.monitoring;

/* loaded from: rt.jar:com/sun/corba/se/spi/monitoring/StatisticMonitoredAttribute.class */
public class StatisticMonitoredAttribute extends MonitoredAttributeBase {
    private StatisticsAccumulator statisticsAccumulator;
    private Object mutex;

    public StatisticMonitoredAttribute(String str, String str2, StatisticsAccumulator statisticsAccumulator, Object obj) {
        super(str);
        setMonitoredAttributeInfo(MonitoringFactories.getMonitoredAttributeInfoFactory().createMonitoredAttributeInfo(str2, String.class, false, true));
        this.statisticsAccumulator = statisticsAccumulator;
        this.mutex = obj;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public Object getValue() {
        String value;
        synchronized (this.mutex) {
            value = this.statisticsAccumulator.getValue();
        }
        return value;
    }

    @Override // com.sun.corba.se.spi.monitoring.MonitoredAttributeBase, com.sun.corba.se.spi.monitoring.MonitoredAttribute
    public void clearState() {
        synchronized (this.mutex) {
            this.statisticsAccumulator.clearState();
        }
    }

    public StatisticsAccumulator getStatisticsAccumulator() {
        return this.statisticsAccumulator;
    }
}
