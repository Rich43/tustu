package org.apache.commons.math3.ml.neuralnet.twod.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.MapUtils;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.ml.neuralnet.twod.util.LocationFinder;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/TopographicErrorHistogram.class */
public class TopographicErrorHistogram implements MapDataVisualization {
    private final DistanceMeasure distance;
    private final boolean relativeCount;

    public TopographicErrorHistogram(boolean relativeCount, DistanceMeasure distance) {
        this.relativeCount = relativeCount;
        this.distance = distance;
    }

    @Override // org.apache.commons.math3.ml.neuralnet.twod.util.MapDataVisualization
    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) throws DimensionMismatchException {
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        Network net2 = map.getNetwork();
        LocationFinder finder = new LocationFinder(map);
        int[][] hit = new int[nR][nC];
        double[][] error = new double[nR][nC];
        for (double[] sample : data) {
            Pair<Neuron, Neuron> p2 = MapUtils.findBestAndSecondBest(sample, map, this.distance);
            Neuron best = p2.getFirst();
            LocationFinder.Location loc = finder.getLocation(best);
            int row = loc.getRow();
            int col = loc.getColumn();
            int[] iArr = hit[row];
            iArr[col] = iArr[col] + 1;
            if (!net2.getNeighbours(best).contains(p2.getSecond())) {
                double[] dArr = error[row];
                dArr[col] = dArr[col] + 1.0d;
            }
        }
        if (this.relativeCount) {
            for (int r2 = 0; r2 < nR; r2++) {
                for (int c2 = 0; c2 < nC; c2++) {
                    double[] dArr2 = error[r2];
                    int i2 = c2;
                    dArr2[i2] = dArr2[i2] / hit[r2][c2];
                }
            }
        }
        return error;
    }
}
