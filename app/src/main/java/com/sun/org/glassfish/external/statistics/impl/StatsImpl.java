package com.sun.org.glassfish.external.statistics.impl;

import com.sun.org.glassfish.external.statistics.Statistic;
import com.sun.org.glassfish.external.statistics.Stats;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/glassfish/external/statistics/impl/StatsImpl.class */
public final class StatsImpl implements Stats {
    private final StatisticImpl[] statArray;

    protected StatsImpl(StatisticImpl[] statisticArray) {
        this.statArray = statisticArray;
    }

    @Override // com.sun.org.glassfish.external.statistics.Stats
    public synchronized Statistic getStatistic(String statisticName) {
        Statistic stat = null;
        Statistic[] statisticArr = this.statArray;
        int length = statisticArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Statistic s2 = statisticArr[i2];
            if (!s2.getName().equals(statisticName)) {
                i2++;
            } else {
                stat = s2;
                break;
            }
        }
        return stat;
    }

    @Override // com.sun.org.glassfish.external.statistics.Stats
    public synchronized String[] getStatisticNames() {
        ArrayList list = new ArrayList();
        for (Statistic s2 : this.statArray) {
            list.add(s2.getName());
        }
        String[] strArray = new String[list.size()];
        return (String[]) list.toArray(strArray);
    }

    @Override // com.sun.org.glassfish.external.statistics.Stats
    public synchronized Statistic[] getStatistics() {
        return this.statArray;
    }

    public synchronized void reset() {
        for (StatisticImpl s2 : this.statArray) {
            s2.reset();
        }
    }
}
