package com.sun.org.glassfish.external.statistics;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/Statistic.class */
public interface Statistic {
    String getName();

    String getUnit();

    String getDescription();

    long getStartTime();

    long getLastSampleTime();
}
