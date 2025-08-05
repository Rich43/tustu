package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.glassfish.external.statistics.RangeStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/RangeStatisticImpl.class */
public final class RangeStatisticImpl extends StatisticImpl implements RangeStatistic, InvocationHandler {
    private long currentVal;
    private long highWaterMark;
    private long lowWaterMark;
    private final long initCurrentVal;
    private final long initHighWaterMark;
    private final long initLowWaterMark;
    private final RangeStatistic rs;

    public RangeStatisticImpl(long curVal, long highMark, long lowMark, String name, String unit, String desc, long startTime, long sampleTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.currentVal = 0L;
        this.highWaterMark = Long.MIN_VALUE;
        this.lowWaterMark = Long.MAX_VALUE;
        this.rs = (RangeStatistic) Proxy.newProxyInstance(RangeStatistic.class.getClassLoader(), new Class[]{RangeStatistic.class}, this);
        this.currentVal = curVal;
        this.initCurrentVal = curVal;
        this.highWaterMark = highMark;
        this.initHighWaterMark = highMark;
        this.lowWaterMark = lowMark;
        this.initLowWaterMark = lowMark;
    }

    public synchronized RangeStatistic getStatistic() {
        return this.rs;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        m2.put(Keywords.FUNC_CURRENT_STRING, Long.valueOf(getCurrent()));
        m2.put("lowwatermark", Long.valueOf(getLowWaterMark()));
        m2.put("highwatermark", Long.valueOf(getHighWaterMark()));
        return m2;
    }

    @Override // com.sun.org.glassfish.external.statistics.RangeStatistic
    public synchronized long getCurrent() {
        return this.currentVal;
    }

    public synchronized void setCurrent(long curVal) {
        this.currentVal = curVal;
        this.lowWaterMark = curVal >= this.lowWaterMark ? this.lowWaterMark : curVal;
        this.highWaterMark = curVal >= this.highWaterMark ? curVal : this.highWaterMark;
        this.sampleTime = System.currentTimeMillis();
    }

    @Override // com.sun.org.glassfish.external.statistics.RangeStatistic
    public synchronized long getHighWaterMark() {
        return this.highWaterMark;
    }

    public synchronized void setHighWaterMark(long hwm) {
        this.highWaterMark = hwm;
    }

    @Override // com.sun.org.glassfish.external.statistics.RangeStatistic
    public synchronized long getLowWaterMark() {
        return this.lowWaterMark;
    }

    public synchronized void setLowWaterMark(long lwm) {
        this.lowWaterMark = lwm;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized void reset() {
        super.reset();
        this.currentVal = this.initCurrentVal;
        this.highWaterMark = this.initHighWaterMark;
        this.lowWaterMark = this.initLowWaterMark;
        this.sampleTime = -1L;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized String toString() {
        return super.toString() + NEWLINE + "Current: " + getCurrent() + NEWLINE + "LowWaterMark: " + getLowWaterMark() + NEWLINE + "HighWaterMark: " + getHighWaterMark();
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method m2, Object[] args) throws Throwable {
        checkMethod(m2);
        try {
            Object result = m2.invoke(this, args);
            return result;
        } catch (InvocationTargetException e2) {
            throw e2.getTargetException();
        } catch (Exception e3) {
            throw new RuntimeException("unexpected invocation exception: " + e3.getMessage());
        }
    }
}
