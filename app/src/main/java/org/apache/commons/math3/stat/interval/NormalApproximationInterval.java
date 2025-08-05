package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/interval/NormalApproximationInterval.class */
public class NormalApproximationInterval implements BinomialConfidenceInterval {
    @Override // org.apache.commons.math3.stat.interval.BinomialConfidenceInterval
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) {
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double mean = numberOfSuccesses / numberOfTrials;
        double alpha = (1.0d - confidenceLevel) / 2.0d;
        NormalDistribution normalDistribution = new NormalDistribution();
        double difference = normalDistribution.inverseCumulativeProbability(1.0d - alpha) * FastMath.sqrt((1.0d / numberOfTrials) * mean * (1.0d - mean));
        return new ConfidenceInterval(mean - difference, mean + difference, confidenceLevel);
    }
}
