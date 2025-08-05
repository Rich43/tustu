package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.FDistribution;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/interval/ClopperPearsonInterval.class */
public class ClopperPearsonInterval implements BinomialConfidenceInterval {
    @Override // org.apache.commons.math3.stat.interval.BinomialConfidenceInterval
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) {
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double lowerBound = 0.0d;
        double upperBound = 0.0d;
        double alpha = (1.0d - confidenceLevel) / 2.0d;
        FDistribution distributionLowerBound = new FDistribution(2 * ((numberOfTrials - numberOfSuccesses) + 1), 2 * numberOfSuccesses);
        double fValueLowerBound = distributionLowerBound.inverseCumulativeProbability(1.0d - alpha);
        if (numberOfSuccesses > 0) {
            lowerBound = numberOfSuccesses / (numberOfSuccesses + (((numberOfTrials - numberOfSuccesses) + 1) * fValueLowerBound));
        }
        FDistribution distributionUpperBound = new FDistribution(2 * (numberOfSuccesses + 1), 2 * (numberOfTrials - numberOfSuccesses));
        double fValueUpperBound = distributionUpperBound.inverseCumulativeProbability(1.0d - alpha);
        if (numberOfSuccesses > 0) {
            upperBound = ((numberOfSuccesses + 1) * fValueUpperBound) / ((numberOfTrials - numberOfSuccesses) + ((numberOfSuccesses + 1) * fValueUpperBound));
        }
        return new ConfidenceInterval(lowerBound, upperBound, confidenceLevel);
    }
}
