package org.apache.commons.math3.ml.neuralnet.twod.util;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.MapUtils;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.ml.neuralnet.twod.util.LocationFinder;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/SmoothedDataHistogram.class */
public class SmoothedDataHistogram implements MapDataVisualization {
    private final int smoothingBins;
    private final DistanceMeasure distance;
    private final double membershipNormalization;

    public SmoothedDataHistogram(int smoothingBins, DistanceMeasure distance) {
        this.smoothingBins = smoothingBins;
        this.distance = distance;
        double sum = 0.0d;
        for (int i2 = 0; i2 < smoothingBins; i2++) {
            sum += smoothingBins - i2;
        }
        this.membershipNormalization = 1.0d / sum;
    }

    @Override // org.apache.commons.math3.ml.neuralnet.twod.util.MapDataVisualization
    public double[][] computeImage(NeuronSquareMesh2D map, Iterable<double[]> data) throws DimensionMismatchException {
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        int mapSize = nR * nC;
        if (mapSize < this.smoothingBins) {
            throw new NumberIsTooSmallException(Integer.valueOf(mapSize), Integer.valueOf(this.smoothingBins), true);
        }
        LocationFinder finder = new LocationFinder(map);
        double[][] histo = new double[nR][nC];
        for (double[] sample : data) {
            Neuron[] sorted = MapUtils.sort(sample, map.getNetwork(), this.distance);
            for (int i2 = 0; i2 < this.smoothingBins; i2++) {
                LocationFinder.Location loc = finder.getLocation(sorted[i2]);
                int row = loc.getRow();
                int col = loc.getColumn();
                double[] dArr = histo[row];
                dArr[col] = dArr[col] + ((this.smoothingBins - i2) * this.membershipNormalization);
            }
        }
        return histo;
    }
}
