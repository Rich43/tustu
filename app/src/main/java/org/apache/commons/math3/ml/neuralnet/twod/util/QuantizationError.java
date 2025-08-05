package org.apache.commons.math3.ml.neuralnet.twod.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.MapUtils;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.ml.neuralnet.twod.util.LocationFinder;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/QuantizationError.class */
public class QuantizationError implements MapDataVisualization {
    private final DistanceMeasure distance;

    public QuantizationError(DistanceMeasure distance) {
        this.distance = distance;
    }

    @Override // org.apache.commons.math3.ml.neuralnet.twod.util.MapDataVisualization
    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) throws DimensionMismatchException {
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        LocationFinder finder = new LocationFinder(map);
        int[][] hit = new int[nR][nC];
        double[][] error = new double[nR][nC];
        for (double[] sample : data) {
            Neuron best = MapUtils.findBest(sample, map, this.distance);
            LocationFinder.Location loc = finder.getLocation(best);
            int row = loc.getRow();
            int col = loc.getColumn();
            int[] iArr = hit[row];
            iArr[col] = iArr[col] + 1;
            double[] dArr = error[row];
            dArr[col] = dArr[col] + this.distance.compute(sample, best.getFeatures());
        }
        for (int r2 = 0; r2 < nR; r2++) {
            for (int c2 = 0; c2 < nC; c2++) {
                int count = hit[r2][c2];
                if (count != 0) {
                    double[] dArr2 = error[r2];
                    int i2 = c2;
                    dArr2[i2] = dArr2[i2] / count;
                }
            }
        }
        return error;
    }
}
