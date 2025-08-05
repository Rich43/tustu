package com.sun.org.glassfish.external.statistics;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/Stats.class */
public interface Stats {
    Statistic getStatistic(String str);

    String[] getStatisticNames();

    Statistic[] getStatistics();
}
