package com.sun.org.glassfish.external.statistics;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/TimeStatistic.class */
public interface TimeStatistic extends Statistic {
    long getCount();

    long getMaxTime();

    long getMinTime();

    long getTotalTime();
}
