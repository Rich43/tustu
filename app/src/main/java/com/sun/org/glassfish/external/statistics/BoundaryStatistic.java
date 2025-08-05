package com.sun.org.glassfish.external.statistics;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/BoundaryStatistic.class */
public interface BoundaryStatistic extends Statistic {
    long getUpperBound();

    long getLowerBound();
}
