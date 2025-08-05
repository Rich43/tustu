package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.glassfish.external.statistics.BoundedRangeStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/BoundedRangeStatisticImpl.class */
public final class BoundedRangeStatisticImpl extends StatisticImpl implements BoundedRangeStatistic, InvocationHandler {
    private long lowerBound;
    private long upperBound;
    private long currentVal;
    private long highWaterMark;
    private long lowWaterMark;
    private final long initLowerBound;
    private final long initUpperBound;
    private final long initCurrentVal;
    private final long initHighWaterMark;
    private final long initLowWaterMark;

    /* renamed from: bs, reason: collision with root package name */
    private final BoundedRangeStatistic f12015bs;

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized String toString() {
        return super.toString() + NEWLINE + "Current: " + getCurrent() + NEWLINE + "LowWaterMark: " + getLowWaterMark() + NEWLINE + "HighWaterMark: " + getHighWaterMark() + NEWLINE + "LowerBound: " + getLowerBound() + NEWLINE + "UpperBound: " + getUpperBound();
    }

    public BoundedRangeStatisticImpl(long curVal, long highMark, long lowMark, long upper, long lower, String name, String unit, String desc, long startTime, long sampleTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.lowerBound = 0L;
        this.upperBound = 0L;
        this.currentVal = 0L;
        this.highWaterMark = Long.MIN_VALUE;
        this.lowWaterMark = Long.MAX_VALUE;
        this.f12015bs = (BoundedRangeStatistic) Proxy.newProxyInstance(BoundedRangeStatistic.class.getClassLoader(), new Class[]{BoundedRangeStatistic.class}, this);
        this.currentVal = curVal;
        this.initCurrentVal = curVal;
        this.highWaterMark = highMark;
        this.initHighWaterMark = highMark;
        this.lowWaterMark = lowMark;
        this.initLowWaterMark = lowMark;
        this.upperBound = upper;
        this.initUpperBound = upper;
        this.lowerBound = lower;
        this.initLowerBound = lower;
    }

    public synchronized BoundedRangeStatistic getStatistic() {
        return this.f12015bs;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        m2.put(Keywords.FUNC_CURRENT_STRING, Long.valueOf(getCurrent()));
        m2.put("lowerbound", Long.valueOf(getLowerBound()));
        m2.put("upperbound", Long.valueOf(getUpperBound()));
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

    @Override // com.sun.org.glassfish.external.statistics.BoundaryStatistic
    public synchronized long getLowerBound() {
        return this.lowerBound;
    }

    @Override // com.sun.org.glassfish.external.statistics.BoundaryStatistic
    public synchronized long getUpperBound() {
        return this.upperBound;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized void reset() {
        super.reset();
        this.lowerBound = this.initLowerBound;
        this.upperBound = this.initUpperBound;
        this.currentVal = this.initCurrentVal;
        this.highWaterMark = this.initHighWaterMark;
        this.lowWaterMark = this.initLowWaterMark;
        this.sampleTime = -1L;
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
