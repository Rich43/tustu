package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.glassfish.external.statistics.AverageRangeStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/AverageRangeStatisticImpl.class */
public final class AverageRangeStatisticImpl extends StatisticImpl implements AverageRangeStatistic, InvocationHandler {
    private long currentVal;
    private long highWaterMark;
    private long lowWaterMark;
    private long numberOfSamples;
    private long runningTotal;
    private final long initCurrentVal;
    private final long initHighWaterMark;
    private final long initLowWaterMark;
    private final long initNumberOfSamples;
    private final long initRunningTotal;

    /* renamed from: as, reason: collision with root package name */
    private final AverageRangeStatistic f12013as;

    public AverageRangeStatisticImpl(long curVal, long highMark, long lowMark, String name, String unit, String desc, long startTime, long sampleTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.currentVal = 0L;
        this.highWaterMark = Long.MIN_VALUE;
        this.lowWaterMark = Long.MAX_VALUE;
        this.numberOfSamples = 0L;
        this.runningTotal = 0L;
        this.f12013as = (AverageRangeStatistic) Proxy.newProxyInstance(AverageRangeStatistic.class.getClassLoader(), new Class[]{AverageRangeStatistic.class}, this);
        this.currentVal = curVal;
        this.initCurrentVal = curVal;
        this.highWaterMark = highMark;
        this.initHighWaterMark = highMark;
        this.lowWaterMark = lowMark;
        this.initLowWaterMark = lowMark;
        this.numberOfSamples = 0L;
        this.initNumberOfSamples = this.numberOfSamples;
        this.runningTotal = 0L;
        this.initRunningTotal = this.runningTotal;
    }

    public synchronized AverageRangeStatistic getStatistic() {
        return this.f12013as;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized String toString() {
        return super.toString() + NEWLINE + "Current: " + getCurrent() + NEWLINE + "LowWaterMark: " + getLowWaterMark() + NEWLINE + "HighWaterMark: " + getHighWaterMark() + NEWLINE + "Average:" + getAverage();
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        m2.put(Keywords.FUNC_CURRENT_STRING, Long.valueOf(getCurrent()));
        m2.put("lowwatermark", Long.valueOf(getLowWaterMark()));
        m2.put("highwatermark", Long.valueOf(getHighWaterMark()));
        m2.put("average", Long.valueOf(getAverage()));
        return m2;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized void reset() {
        super.reset();
        this.currentVal = this.initCurrentVal;
        this.highWaterMark = this.initHighWaterMark;
        this.lowWaterMark = this.initLowWaterMark;
        this.numberOfSamples = this.initNumberOfSamples;
        this.runningTotal = this.initRunningTotal;
        this.sampleTime = -1L;
    }

    @Override // com.sun.org.glassfish.external.statistics.AverageRangeStatistic
    public synchronized long getAverage() {
        if (this.numberOfSamples == 0) {
            return -1L;
        }
        return this.runningTotal / this.numberOfSamples;
    }

    @Override // com.sun.org.glassfish.external.statistics.RangeStatistic
    public synchronized long getCurrent() {
        return this.currentVal;
    }

    public synchronized void setCurrent(long curVal) {
        this.currentVal = curVal;
        this.lowWaterMark = curVal >= this.lowWaterMark ? this.lowWaterMark : curVal;
        this.highWaterMark = curVal >= this.highWaterMark ? curVal : this.highWaterMark;
        this.numberOfSamples++;
        this.runningTotal += curVal;
        this.sampleTime = System.currentTimeMillis();
    }

    @Override // com.sun.org.glassfish.external.statistics.RangeStatistic
    public synchronized long getHighWaterMark() {
        return this.highWaterMark;
    }

    @Override // com.sun.org.glassfish.external.statistics.RangeStatistic
    public synchronized long getLowWaterMark() {
        return this.lowWaterMark;
    }

    @Override // java.lang.reflect.InvocationHandler
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        checkMethod(method);
        try {
            Object result = method.invoke(this, args);
            return result;
        } catch (InvocationTargetException e2) {
            throw e2.getTargetException();
        } catch (Exception e3) {
            throw new RuntimeException("unexpected invocation exception: " + e3.getMessage());
        }
    }
}
