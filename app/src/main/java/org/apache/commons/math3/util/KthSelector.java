package org.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.NullArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/KthSelector.class */
public class KthSelector implements Serializable {
    private static final long serialVersionUID = 20140713;
    private static final int MIN_SELECT_SIZE = 15;
    private final PivotingStrategyInterface pivotingStrategy;

    public KthSelector() {
        this.pivotingStrategy = new MedianOf3PivotingStrategy();
    }

    public KthSelector(PivotingStrategyInterface pivotingStrategy) throws NullArgumentException {
        MathUtils.checkNotNull(pivotingStrategy);
        this.pivotingStrategy = pivotingStrategy;
    }

    public PivotingStrategyInterface getPivotingStrategy() {
        return this.pivotingStrategy;
    }

    public double select(double[] work, int[] pivotsHeap, int k2) {
        int pivot;
        int begin = 0;
        int end = work.length;
        int node = 0;
        boolean usePivotsHeap = pivotsHeap != null;
        while (end - begin > 15) {
            if (usePivotsHeap && node < pivotsHeap.length && pivotsHeap[node] >= 0) {
                pivot = pivotsHeap[node];
            } else {
                pivot = partition(work, begin, end, this.pivotingStrategy.pivotIndex(work, begin, end));
                if (usePivotsHeap && node < pivotsHeap.length) {
                    pivotsHeap[node] = pivot;
                }
            }
            if (k2 == pivot) {
                return work[k2];
            }
            if (k2 < pivot) {
                end = pivot;
                node = FastMath.min((2 * node) + 1, usePivotsHeap ? pivotsHeap.length : end);
            } else {
                begin = pivot + 1;
                node = FastMath.min((2 * node) + 2, usePivotsHeap ? pivotsHeap.length : end);
            }
        }
        Arrays.sort(work, begin, end);
        return work[k2];
    }

    private int partition(double[] work, int begin, int end, int pivot) {
        double value = work[pivot];
        work[pivot] = work[begin];
        int i2 = begin + 1;
        int j2 = end - 1;
        while (i2 < j2) {
            while (i2 < j2 && work[j2] > value) {
                j2--;
            }
            while (i2 < j2 && work[i2] < value) {
                i2++;
            }
            if (i2 < j2) {
                double tmp = work[i2];
                int i3 = i2;
                i2++;
                work[i3] = work[j2];
                int i4 = j2;
                j2--;
                work[i4] = tmp;
            }
        }
        if (i2 >= end || work[i2] > value) {
            i2--;
        }
        work[begin] = work[i2];
        work[i2] = value;
        return i2;
    }
}
