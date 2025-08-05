package org.apache.commons.math3.ml.neuralnet.twod.util;

import java.util.Collection;
import org.apache.commons.math3.ml.distance.DistanceMeasure;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/UnifiedDistanceMatrix.class */
public class UnifiedDistanceMatrix implements MapVisualization {
    private final boolean individualDistances;
    private final DistanceMeasure distance;

    public UnifiedDistanceMatrix(boolean individualDistances, DistanceMeasure distance) {
        this.individualDistances = individualDistances;
        this.distance = distance;
    }

    @Override // org.apache.commons.math3.ml.neuralnet.twod.util.MapVisualization
    public double[][] computeImage(NeuronSquareMesh2D map) {
        if (this.individualDistances) {
            return individualDistances(map);
        }
        return averageDistances(map);
    }

    private double[][] individualDistances(NeuronSquareMesh2D map) {
        int numRows = map.getNumberOfRows();
        int numCols = map.getNumberOfColumns();
        double[][] uMatrix = new double[(numRows * 2) + 1][(numCols * 2) + 1];
        for (int i2 = 0; i2 < numRows; i2++) {
            int iR = (2 * i2) + 1;
            for (int j2 = 0; j2 < numCols; j2++) {
                int jR = (2 * j2) + 1;
                double[] current = map.getNeuron(i2, j2).getFeatures();
                Neuron neighbour = map.getNeuron(i2, j2, NeuronSquareMesh2D.HorizontalDirection.RIGHT, NeuronSquareMesh2D.VerticalDirection.CENTER);
                if (neighbour != null) {
                    uMatrix[iR][jR + 1] = this.distance.compute(current, neighbour.getFeatures());
                }
                Neuron neighbour2 = map.getNeuron(i2, j2, NeuronSquareMesh2D.HorizontalDirection.CENTER, NeuronSquareMesh2D.VerticalDirection.DOWN);
                if (neighbour2 != null) {
                    uMatrix[iR + 1][jR] = this.distance.compute(current, neighbour2.getFeatures());
                }
            }
        }
        for (int i3 = 0; i3 < numRows; i3++) {
            int iR2 = (2 * i3) + 1;
            for (int j3 = 0; j3 < numCols; j3++) {
                int jR2 = (2 * j3) + 1;
                Neuron current2 = map.getNeuron(i3, j3);
                Neuron right = map.getNeuron(i3, j3, NeuronSquareMesh2D.HorizontalDirection.RIGHT, NeuronSquareMesh2D.VerticalDirection.CENTER);
                Neuron bottom = map.getNeuron(i3, j3, NeuronSquareMesh2D.HorizontalDirection.CENTER, NeuronSquareMesh2D.VerticalDirection.DOWN);
                Neuron bottomRight = map.getNeuron(i3, j3, NeuronSquareMesh2D.HorizontalDirection.RIGHT, NeuronSquareMesh2D.VerticalDirection.DOWN);
                double current2BottomRight = bottomRight == null ? 0.0d : this.distance.compute(current2.getFeatures(), bottomRight.getFeatures());
                double right2Bottom = (right == null || bottom == null) ? 0.0d : this.distance.compute(right.getFeatures(), bottom.getFeatures());
                uMatrix[iR2 + 1][jR2 + 1] = 0.5d * (current2BottomRight + right2Bottom);
            }
        }
        int lastRow = uMatrix.length - 1;
        uMatrix[0] = uMatrix[lastRow];
        int lastCol = uMatrix[0].length - 1;
        for (int r2 = 0; r2 < lastRow; r2++) {
            uMatrix[r2][0] = uMatrix[r2][lastCol];
        }
        return uMatrix;
    }

    private double[][] averageDistances(NeuronSquareMesh2D map) {
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
                    d2 += this.distance.compute(features, n2.getFeatures());
                }
                uMatrix[i2][j2] = d2 / count;
            }
        }
        return uMatrix;
    }
}
