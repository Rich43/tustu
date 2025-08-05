package org.apache.commons.math3.stat.interval;

import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/interval/AgrestiCoullInterval.class */
public class AgrestiCoullInterval implements BinomialConfidenceInterval {
    @Override // org.apache.commons.math3.stat.interval.BinomialConfidenceInterval
    public ConfidenceInterval createInterval(int numberOfTrials, int numberOfSuccesses, double confidenceLevel) throws OutOfRangeException {
        IntervalUtils.checkParameters(numberOfTrials, numberOfSuccesses, confidenceLevel);
        double alpha = (1.0d - confidenceLevel) / 2.0d;
        NormalDistribution normalDistribution = new NormalDistribution();
        double z2 = normalDistribution.inverseCumulativeProbability(1.0d - alpha);
        double zSquared = FastMath.pow(z2, 2);
        double modifiedNumberOfTrials = numberOfTrials + zSquared;
        double modifiedSuccessesRatio = (1.0d / modifiedNumberOfTrials) * (numberOfSuccesses + (0.5d * zSquared));
        double difference = z2 * FastMath.sqrt((1.0d / modifiedNumberOfTrials) * modifiedSuccessesRatio * (1.0d - modifiedSuccessesRatio));
        return new ConfidenceInterval(modifiedSuccessesRatio - difference, modifiedSuccessesRatio + difference, confidenceLevel);
    }
}
