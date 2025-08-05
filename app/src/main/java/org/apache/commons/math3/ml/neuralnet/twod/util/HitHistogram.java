package org.apache.commons.math3.ml.neuralnet.twod.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.MapUtils;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.ml.neuralnet.twod.util.LocationFinder;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/HitHistogram.class */
public class HitHistogram implements MapDataVisualization {
    private final DistanceMeasure distance;
    private final boolean normalizeCount;

    public HitHistogram(boolean normalizeCount, DistanceMeasure distance) {
        this.normalizeCount = normalizeCount;
        this.distance = distance;
    }

    @Override // org.apache.commons.math3.ml.neuralnet.twod.util.MapDataVisualization
    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) throws DimensionMismatchException {
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        LocationFinder finder = new LocationFinder(map);
        int numSamples = 0;
        double[][] hit = new double[nR][nC];
        for (double[] sample : data) {
            Neuron best = MapUtils.findBest(sample, map, this.distance);
            LocationFinder.Location loc = finder.getLocation(best);
            int row = loc.getRow();
            int col = loc.getColumn();
            double[] dArr = hit[row];
            dArr[col] = dArr[col] + 1.0d;
            numSamples++;
        }
        if (this.normalizeCount) {
            for (int r2 = 0; r2 < nR; r2++) {
                for (int c2 = 0; c2 < nC; c2++) {
                    double[] dArr2 = hit[r2];
                    int i2 = c2;
                    dArr2[i2] = dArr2[i2] / numSamples;
                }
            }
        }
        return hit;
    }
}
