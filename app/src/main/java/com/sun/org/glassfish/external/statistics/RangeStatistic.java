package com.sun.org.glassfish.external.statistics;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/RangeStatistic.class */
public interface RangeStatistic extends Statistic {
    long getHighWaterMark();

    long getLowWaterMark();

    long getCurrent();
}
