package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.glassfish.external.statistics.TimeStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/TimeStatisticImpl.class */
public final class TimeStatisticImpl extends StatisticImpl implements TimeStatistic, InvocationHandler {
    private long count;
    private long maxTime;
    private long minTime;
    private long totTime;
    private final long initCount;
    private final long initMaxTime;
    private final long initMinTime;
    private final long initTotTime;
    private final TimeStatistic ts;

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public final synchronized String toString() {
        return super.toString() + NEWLINE + "Count: " + getCount() + NEWLINE + "MinTime: " + getMinTime() + NEWLINE + "MaxTime: " + getMaxTime() + NEWLINE + "TotalTime: " + getTotalTime();
    }

    public TimeStatisticImpl(long counter, long maximumTime, long minimumTime, long totalTime, String name, String unit, String desc, long startTime, long sampleTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.count = 0L;
        this.maxTime = 0L;
        this.minTime = 0L;
        this.totTime = 0L;
        this.ts = (TimeStatistic) Proxy.newProxyInstance(TimeStatistic.class.getClassLoader(), new Class[]{TimeStatistic.class}, this);
        this.count = counter;
        this.initCount = counter;
        this.maxTime = maximumTime;
        this.initMaxTime = maximumTime;
        this.minTime = minimumTime;
        this.initMinTime = minimumTime;
        this.totTime = totalTime;
        this.initTotTime = totalTime;
    }

    public synchronized TimeStatistic getStatistic() {
        return this.ts;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        m2.put("count", Long.valueOf(getCount()));
        m2.put("maxtime", Long.valueOf(getMaxTime()));
        m2.put("mintime", Long.valueOf(getMinTime()));
        m2.put("totaltime", Long.valueOf(getTotalTime()));
        return m2;
    }

    public synchronized void incrementCount(long current) {
        if (this.count == 0) {
            this.totTime = current;
            this.maxTime = current;
            this.minTime = current;
        } else {
            this.totTime += current;
            this.maxTime = current >= this.maxTime ? current : this.maxTime;
            this.minTime = current >= this.minTime ? this.minTime : current;
        }
        this.count++;
        this.sampleTime = System.currentTimeMillis();
    }

    @Override // com.sun.org.glassfish.external.statistics.TimeStatistic
    public synchronized long getCount() {
        return this.count;
    }

    @Override // com.sun.org.glassfish.external.statistics.TimeStatistic
    public synchronized long getMaxTime() {
        return this.maxTime;
    }

    @Override // com.sun.org.glassfish.external.statistics.TimeStatistic
    public synchronized long getMinTime() {
        return this.minTime;
    }

    @Override // com.sun.org.glassfish.external.statistics.TimeStatistic
    public synchronized long getTotalTime() {
        return this.totTime;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized void reset() {
        super.reset();
        this.count = this.initCount;
        this.maxTime = this.initMaxTime;
        this.minTime = this.initMinTime;
        this.totTime = this.initTotTime;
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
