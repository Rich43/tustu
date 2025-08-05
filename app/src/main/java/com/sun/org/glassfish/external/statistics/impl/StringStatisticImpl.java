package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import com.sun.org.glassfish.external.statistics.StringStatistic;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/StringStatisticImpl.class */
public final class StringStatisticImpl extends StatisticImpl implements StringStatistic, InvocationHandler {
    private volatile String str;
    private final String initStr;
    private final StringStatistic ss;

    public StringStatisticImpl(String str, String name, String unit, String desc, long sampleTime, long startTime) {
        super(name, unit, desc, startTime, sampleTime);
        this.str = null;
        this.ss = (StringStatistic) Proxy.newProxyInstance(StringStatistic.class.getClassLoader(), new Class[]{StringStatistic.class}, this);
        this.str = str;
        this.initStr = str;
    }

    public StringStatisticImpl(String name, String unit, String desc) {
        this("", name, unit, desc, System.currentTimeMillis(), System.currentTimeMillis());
    }

    public synchronized StringStatistic getStatistic() {
        return this.ss;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized Map getStaticAsMap() {
        Map m2 = super.getStaticAsMap();
        if (getCurrent() != null) {
            m2.put(Keywords.FUNC_CURRENT_STRING, getCurrent());
        }
        return m2;
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized String toString() {
        return super.toString() + NEWLINE + "Current-value: " + getCurrent();
    }

    @Override // com.sun.org.glassfish.external.statistics.StringStatistic
    public String getCurrent() {
        return this.str;
    }

    public void setCurrent(String str) {
        this.str = str;
        this.sampleTime = System.currentTimeMillis();
    }

    @Override // com.sun.org.glassfish.external.statistics.impl.StatisticImpl
    public synchronized void reset() {
        super.reset();
        this.str = this.initStr;
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
