package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.glassfish.external.statistics.CountStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/CountStatisticImpl.class */
public final class CountStatisticImpl extends StatisticImpl implements CountStatistic, InvocationHandler {
    private long count;
    private final long initCount;
    private final CountStatistic cs;

    public CountStatisticImpl(long countVal, String name, String unit, String desc, long sampleTime, long startTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.count = 0L;
        this.cs = (CountStatistic) Proxy.newProxyInstance(CountStatistic.class.getClassLoader(), new Class[]{CountStatistic.class}, this);
        this.count = countVal;
        this.initCount = countVal;
    }

    public CountStatisticImpl(String name, String unit, String desc) {
        this(0L, name, unit, desc, -1L, System.currentTimeMillis());
    }

    public synchronized CountStatistic getStatistic() {
        return this.cs;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        m2.put("count", Long.valueOf(getCount()));
        return m2;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized String toString() {
        return super.toString() + NEWLINE + "Count: " + getCount();
    }

    @Override // com.sun.org.glassfish.external.statistics.CountStatistic
    public synchronized long getCount() {
        return this.count;
    }

    public synchronized void setCount(long countVal) {
        this.count = countVal;
        this.sampleTime = System.currentTimeMillis();
    }

    public synchronized void increment() {
        this.count++;
        this.sampleTime = System.currentTimeMillis();
    }

    public synchronized void increment(long delta) {
        this.count += delta;
        this.sampleTime = System.currentTimeMillis();
    }

    public synchronized void decrement() {
        this.count--;
        this.sampleTime = System.currentTimeMillis();
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized void reset() {
        super.reset();
        this.count = this.initCount;
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
