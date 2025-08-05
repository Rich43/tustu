package org.apache.commons.math3.ml.neuralnet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;
import org.apache.commons.math3.util.Pair;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/MapUtils.class */
public class MapUtils {
    private MapUtils() {
    }

    public static Neuron findBest(double[] features, Iterable<Neuron> neurons, DistanceMeasure distance) throws DimensionMismatchException {
        Neuron best = null;
        double min = Double.POSITIVE_INFINITY;
        for (Neuron n2 : neurons) {
            double d2 = distance.compute(n2.getFeatures(), features);
            if (d2 < min) {
                min = d2;
                best = n2;
            }
        }
        return best;
    }

    public static Pair<Neuron, Neuron> findBestAndSecondBest(double[] features, Iterable<Neuron> neurons, DistanceMeasure distance) throws DimensionMismatchException {
        Neuron[] best = {null, null};
        double[] min = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        for (Neuron n2 : neurons) {
            double d2 = distance.compute(n2.getFeatures(), features);
            if (d2 < min[0]) {
                min[1] = min[0];
                best[1] = best[0];
                min[0] = d2;
                best[0] = n2;
            } else if (d2 < min[1]) {
                min[1] = d2;
                best[1] = n2;
            }
        }
        return new Pair<>(best[0], best[1]);
    }

    public static Neuron[] sort(double[] features, Iterable<Neuron> neurons, DistanceMeasure distance) throws DimensionMismatchException {
        List<PairNeuronDouble> list = new ArrayList<>();
        for (Neuron n2 : neurons) {
            double d2 = distance.compute(n2.getFeatures(), features);
            list.add(new PairNeuronDouble(n2, d2));
        }
        Collections.sort(list, PairNeuronDouble.COMPARATOR);
        int len = list.size();
        Neuron[] sorted = new Neuron[len];
        for (int i2 = 0; i2 < len; i2++) {
            sorted[i2] = list.get(i2).getNeuron();
        }
        return sorted;
    }

    public static double[][] computeU(NeuronSquareMesh2D map, DistanceMeasure distance) {
        int numRows = map.getNumberOfRows();
        int numCols = map.getNumberOfColumns();
        double[][] uMatrix = new double[numRows][numCols];
        Network net2 = map.getNetwork();
        for (int i2 = 0; i2 < numRows; i2++) {
            for (int j2 = 0; j2 < numCols; j2++) {
                Neuron neuron = map.getNeuron(i2, j2);
                Collection<Neuron> neighbours = net2.getNeighbours(neuron);
                double[] features = neuron.getFeatures();
                double d2 = 0.0d;
                int count = 0;
                for (Neuron n2 : neighbours) {
                    count++;
                    d2 += distance.compute(features, n2.getFeatures());
                }
                uMatrix[i2][j2] = d2 / count;
            }
        }
        return uMatrix;
    }

    public static int[][] computeHitHistogram(Iterable<double[]> data, NeuronSquareMesh2D map, DistanceMeasure distance) throws DimensionMismatchException {
        HashMap<Neuron, Integer> hit = new HashMap<>();
        Network net2 = map.getNetwork();
        for (double[] f2 : data) {
            Neuron best = findBest(f2, net2, distance);
            Integer count = hit.get(best);
            if (count == null) {
                hit.put(best, 1);
            } else {
                hit.put(best, Integer.valueOf(count.intValue() + 1));
            }
        }
        int numRows = map.getNumberOfRows();
        int numCols = map.getNumberOfColumns();
        int[][] histo = new int[numRows][numCols];
        for (int i2 = 0; i2 < numRows; i2++) {
            for (int j2 = 0; j2 < numCols; j2++) {
                Neuron neuron = map.getNeuron(i2, j2);
                Integer count2 = hit.get(neuron);
                if (count2 == null) {
                    histo[i2][j2] = 0;
                } else {
                    histo[i2][j2] = count2.intValue();
                }
            }
        }
        return histo;
    }

    public static double computeQuantizationError(Iterable<double[]> data, Iterable<Neuron> neurons, DistanceMeasure distance) {
        double d2 = 0.0d;
        int count = 0;
        for (double[] f2 : data) {
            count++;
            d2 += distance.compute(f2, findBest(f2, neurons, distance).getFeatures());
        }
        if (count == 0) {
            throw new NoDataException();
        }
        return d2 / count;
    }

    public static double computeTopographicError(Iterable<double[]> data, Network net2, DistanceMeasure distance) throws DimensionMismatchException {
        int notAdjacentCount = 0;
        int count = 0;
        for (double[] f2 : data) {
            count++;
            Pair<Neuron, Neuron> p2 = findBestAndSecondBest(f2, net2, distance);
            if (!net2.getNeighbours(p2.getFirst()).contains(p2.getSecond())) {
                notAdjacentCount++;
            }
        }
        if (count == 0) {
            throw new NoDataException();
        }
        return notAdjacentCount / count;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/MapUtils$PairNeuronDouble.class */
    private static class PairNeuronDouble {
        static final Comparator<PairNeuronDouble> COMPARATOR = new Comparator<PairNeuronDouble>() { // from class: org.apache.commons.math3.ml.neuralnet.MapUtils.PairNeuronDouble.1
            @Override // java.util.Comparator
            public int compare(PairNeuronDouble o1, PairNeuronDouble o2) {
                return Double.compare(o1.value, o2.value);
            }
        };
        private final Neuron neuron;
        private final double value;

        PairNeuronDouble(Neuron neuron, double value) {
            this.neuron = neuron;
            this.value = value;
        }

        public Neuron getNeuron() {
            return this.neuron;
        }
    }
}
