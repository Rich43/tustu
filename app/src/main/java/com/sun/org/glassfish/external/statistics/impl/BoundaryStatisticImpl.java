package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.glassfish.external.statistics.BoundaryStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/BoundaryStatisticImpl.class */
public final class BoundaryStatisticImpl extends StatisticImpl implements BoundaryStatistic, InvocationHandler {
    private final long lowerBound;
    private final long upperBound;

    /* renamed from: bs, reason: collision with root package name */
    private final BoundaryStatistic f12014bs;

    public BoundaryStatisticImpl(long lower, long upper, String name, String unit, String desc, long startTime, long sampleTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.f12014bs = (BoundaryStatistic) Proxy.newProxyInstance(BoundaryStatistic.class.getClassLoader(), new Class[]{BoundaryStatistic.class}, this);
        this.upperBound = upper;
        this.lowerBound = lower;
    }

    public synchronized BoundaryStatistic getStatistic() {
        return this.f12014bs;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        m2.put("lowerbound", Long.valueOf(getLowerBound()));
        m2.put("upperbound", Long.valueOf(getUpperBound()));
        return m2;
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
