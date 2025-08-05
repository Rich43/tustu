package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/interval/WilsonScoreInterval.class */
public class WilsonScoreInterval implements BinomialConfidenceInterval {
    @Override // org.apache.commons.math3.stat.interval.BinomialConfidenceInterval
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) throws OutOfRangeException {
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double alpha = (1.0d - confidenceLevel) / 2.0d;
        NormalDistribution normalDistribution = new NormalDistribution();
        double z2 = normalDistribution.inverseCumulativeProbability(1.0d - alpha);
        double zSquared = FastMath.pow(z2, 2);
        double mean = numberOfSuccesses / numberOfTrials;
        double factor = 1.0d / (1.0d + ((1.0d / numberOfTrials) * zSquared));
        double modifiedSuccessRatio = mean + ((1.0d / (2 * numberOfTrials)) * zSquared);
        double difference = z2 * FastMath.sqrt(((1.0d / numberOfTrials) * mean * (1.0d - mean)) + ((1.0d / (4.0d * FastMath.pow(numberOfTrials, 2))) * zSquared));
        double lowerBound = factor * (modifiedSuccessRatio - difference);
        double upperBound = factor * (modifiedSuccessRatio + difference);
        return new ConfidenceInterval(lowerBound, upperBound, confidenceLevel);
    }
}
