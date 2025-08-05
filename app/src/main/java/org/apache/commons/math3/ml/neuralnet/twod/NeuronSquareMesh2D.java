package org.apache.commons.math3.ml.neuralnet.twod;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.ml.neuralnet.FeatureInitializer;
import org.apache.commons.math3.ml.neuralnet.Network;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.SquareNeighbourhood;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/NeuronSquareMesh2D.class */
public class NeuronSquareMesh2D implements Iterable<Neuron>, Serializable {
    private static final long serialVersionUID = 1;
    private final Network network;
    private final int numberOfRows;
    private final int numberOfColumns;
    private final boolean wrapRows;
    private final boolean wrapColumns;
    private final SquareNeighbourhood neighbourhood;
    private final long[][] identifiers;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/NeuronSquareMesh2D$HorizontalDirection.class */
    public enum HorizontalDirection {
        RIGHT,
        CENTER,
        LEFT
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/NeuronSquareMesh2D$VerticalDirection.class */
    public enum VerticalDirection {
        UP,
        CENTER,
        DOWN
    }

    NeuronSquareMesh2D(boolean wrapRowDim, boolean wrapColDim, SquareNeighbourhood neighbourhoodType, double[][][] featuresList) {
        this.numberOfRows = featuresList.length;
        this.numberOfColumns = featuresList[0].length;
        if (this.numberOfRows < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(this.numberOfRows), 2, true);
        }
        if (this.numberOfColumns < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(this.numberOfColumns), 2, true);
        }
        this.wrapRows = wrapRowDim;
        this.wrapColumns = wrapColDim;
        this.neighbourhood = neighbourhoodType;
        int fLen = featuresList[0][0].length;
        this.network = new Network(0L, fLen);
        this.identifiers = new long[this.numberOfRows][this.numberOfColumns];
        for (int i2 = 0; i2 < this.numberOfRows; i2++) {
            for (int j2 = 0; j2 < this.numberOfColumns; j2++) {
                this.identifiers[i2][j2] = this.network.createNeuron(featuresList[i2][j2]);
            }
        }
        createLinks();
    }

    public NeuronSquareMesh2D(int numRows, boolean wrapRowDim, int numCols, boolean wrapColDim, SquareNeighbourhood neighbourhoodType, FeatureInitializer[] featureInit) {
        if (numRows < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(numRows), 2, true);
        }
        if (numCols < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(numCols), 2, true);
        }
        this.numberOfRows = numRows;
        this.wrapRows = wrapRowDim;
        this.numberOfColumns = numCols;
        this.wrapColumns = wrapColDim;
        this.neighbourhood = neighbourhoodType;
        this.identifiers = new long[this.numberOfRows][this.numberOfColumns];
        int fLen = featureInit.length;
        this.network = new Network(0L, fLen);
        for (int i2 = 0; i2 < numRows; i2++) {
            for (int j2 = 0; j2 < numCols; j2++) {
                double[] features = new double[fLen];
                for (int fIndex = 0; fIndex < fLen; fIndex++) {
                    features[fIndex] = featureInit[fIndex].value();
                }
                this.identifiers[i2][j2] = this.network.createNeuron(features);
            }
        }
        createLinks();
    }

    private NeuronSquareMesh2D(boolean wrapRowDim, boolean wrapColDim, SquareNeighbourhood neighbourhoodType, Network net2, long[][] idGrid) {
        this.numberOfRows = idGrid.length;
        this.numberOfColumns = idGrid[0].length;
        this.wrapRows = wrapRowDim;
        this.wrapColumns = wrapColDim;
        this.neighbourhood = neighbourhoodType;
        this.network = net2;
        this.identifiers = idGrid;
    }

    public synchronized NeuronSquareMesh2D copy() {
        long[][] idGrid = new long[this.numberOfRows][this.numberOfColumns];
        for (int r2 = 0; r2 < this.numberOfRows; r2++) {
            for (int c2 = 0; c2 < this.numberOfColumns; c2++) {
                idGrid[r2][c2] = this.identifiers[r2][c2];
            }
        }
        return new NeuronSquareMesh2D(this.wrapRows, this.wrapColumns, this.neighbourhood, this.network.copy(), idGrid);
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Neuron> iterator() {
        return this.network.iterator();
    }

    public Network getNetwork() {
        return this.network;
    }

    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    public Neuron getNeuron(int i2, int j2) {
        if (i2 < 0 || i2 >= this.numberOfRows) {
            throw new OutOfRangeException(Integer.valueOf(i2), 0, Integer.valueOf(this.numberOfRows - 1));
        }
        if (j2 < 0 || j2 >= this.numberOfColumns) {
            throw new OutOfRangeException(Integer.valueOf(j2), 0, Integer.valueOf(this.numberOfColumns - 1));
        }
        return this.network.getNeuron(this.identifiers[i2][j2]);
    }

    public Neuron getNeuron(int row, int col, HorizontalDirection alongRowDir, VerticalDirection alongColDir) {
        int[] location = getLocation(row, col, alongRowDir, alongColDir);
        if (location == null) {
            return null;
        }
        return getNeuron(location[0], location[1]);
    }

    private int[] getLocation(int row, int col, HorizontalDirection alongRowDir, VerticalDirection alongColDir) {
        int colOffset;
        int rowOffset;
        switch (alongRowDir) {
            case LEFT:
                colOffset = -1;
                break;
            case RIGHT:
                colOffset = 1;
                break;
            case CENTER:
                colOffset = 0;
                break;
            default:
                throw new MathInternalError();
        }
        int colIndex = col + colOffset;
        if (this.wrapColumns) {
            if (colIndex < 0) {
                colIndex += this.numberOfColumns;
            } else {
                colIndex %= this.numberOfColumns;
            }
        }
        switch (alongColDir) {
            case UP:
                rowOffset = -1;
                break;
            case DOWN:
                rowOffset = 1;
                break;
            case CENTER:
                rowOffset = 0;
                break;
            default:
                throw new MathInternalError();
        }
        int rowIndex = row + rowOffset;
        if (this.wrapRows) {
            if (rowIndex < 0) {
                rowIndex += this.numberOfRows;
            } else {
                rowIndex %= this.numberOfRows;
            }
        }
        if (rowIndex < 0 || rowIndex >= this.numberOfRows || colIndex < 0 || colIndex >= this.numberOfColumns) {
            return null;
        }
        return new int[]{rowIndex, colIndex};
    }

    private void createLinks() {
        List<Long> linkEnd = new ArrayList<>();
        int iLast = this.numberOfRows - 1;
        int jLast = this.numberOfColumns - 1;
        for (int i2 = 0; i2 < this.numberOfRows; i2++) {
            for (int j2 = 0; j2 < this.numberOfColumns; j2++) {
                linkEnd.clear();
                switch (this.neighbourhood) {
                    case MOORE:
                        if (i2 > 0) {
                            if (j2 > 0) {
                                linkEnd.add(Long.valueOf(this.identifiers[i2 - 1][j2 - 1]));
                            }
                            if (j2 < jLast) {
                                linkEnd.add(Long.valueOf(this.identifiers[i2 - 1][j2 + 1]));
                            }
                        }
                        if (i2 < iLast) {
                            if (j2 > 0) {
                                linkEnd.add(Long.valueOf(this.identifiers[i2 + 1][j2 - 1]));
                            }
                            if (j2 < jLast) {
                                linkEnd.add(Long.valueOf(this.identifiers[i2 + 1][j2 + 1]));
                            }
                        }
                        if (this.wrapRows) {
                            if (i2 == 0) {
                                if (j2 > 0) {
                                    linkEnd.add(Long.valueOf(this.identifiers[iLast][j2 - 1]));
                                }
                                if (j2 < jLast) {
                                    linkEnd.add(Long.valueOf(this.identifiers[iLast][j2 + 1]));
                                }
                            } else if (i2 == iLast) {
                                if (j2 > 0) {
                                    linkEnd.add(Long.valueOf(this.identifiers[0][j2 - 1]));
                                }
                                if (j2 < jLast) {
                                    linkEnd.add(Long.valueOf(this.identifiers[0][j2 + 1]));
                                }
                            }
                        }
                        if (this.wrapColumns) {
                            if (j2 == 0) {
                                if (i2 > 0) {
                                    linkEnd.add(Long.valueOf(this.identifiers[i2 - 1][jLast]));
                                }
                                if (i2 < iLast) {
                                    linkEnd.add(Long.valueOf(this.identifiers[i2 + 1][jLast]));
                                }
                            } else if (j2 == jLast) {
                                if (i2 > 0) {
                                    linkEnd.add(Long.valueOf(this.identifiers[i2 - 1][0]));
                                }
                                if (i2 < iLast) {
                                    linkEnd.add(Long.valueOf(this.identifiers[i2 + 1][0]));
                                }
                            }
                        }
                        if (this.wrapRows && this.wrapColumns) {
                            if (i2 == 0 && j2 == 0) {
                                linkEnd.add(Long.valueOf(this.identifiers[iLast][jLast]));
                            } else if (i2 == 0 && j2 == jLast) {
                                linkEnd.add(Long.valueOf(this.identifiers[iLast][0]));
                            } else if (i2 == iLast && j2 == 0) {
                                linkEnd.add(Long.valueOf(this.identifiers[0][jLast]));
                            } else if (i2 == iLast && j2 == jLast) {
                                linkEnd.add(Long.valueOf(this.identifiers[0][0]));
                            }
                        }
                        break;
                    case VON_NEUMANN:
                        if (i2 > 0) {
                            linkEnd.add(Long.valueOf(this.identifiers[i2 - 1][j2]));
                        }
                        if (i2 < iLast) {
                            linkEnd.add(Long.valueOf(this.identifiers[i2 + 1][j2]));
                        }
                        if (this.wrapRows) {
                            if (i2 == 0) {
                                linkEnd.add(Long.valueOf(this.identifiers[iLast][j2]));
                            } else if (i2 == iLast) {
                                linkEnd.add(Long.valueOf(this.identifiers[0][j2]));
                            }
                        }
                        if (j2 > 0) {
                            linkEnd.add(Long.valueOf(this.identifiers[i2][j2 - 1]));
                        }
                        if (j2 < jLast) {
                            linkEnd.add(Long.valueOf(this.identifiers[i2][j2 + 1]));
                        }
                        if (this.wrapColumns) {
                            if (j2 == 0) {
                                linkEnd.add(Long.valueOf(this.identifiers[i2][jLast]));
                            } else if (j2 == jLast) {
                                linkEnd.add(Long.valueOf(this.identifiers[i2][0]));
                            }
                        }
                        Neuron aNeuron = this.network.getNeuron(this.identifiers[i2][j2]);
                        Iterator i$ = linkEnd.iterator();
                        while (i$.hasNext()) {
                            long b2 = i$.next().longValue();
                            Neuron bNeuron = this.network.getNeuron(b2);
                            this.network.addLink(aNeuron, bNeuron);
                        }
                    default:
                        throw new MathInternalError();
                }
            }
        }
    }

    private void readObject(ObjectInputStream in) {
        throw new IllegalStateException();
    }

    private Object writeReplace() {
        double[][][] featuresList = new double[this.numberOfRows][this.numberOfColumns][];
        for (int i2 = 0; i2 < this.numberOfRows; i2++) {
            for (int j2 = 0; j2 < this.numberOfColumns; j2++) {
                featuresList[i2][j2] = getNeuron(i2, j2).getFeatures();
            }
        }
        return new SerializationProxy(this.wrapRows, this.wrapColumns, this.neighbourhood, featuresList);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/NeuronSquareMesh2D$SerializationProxy.class */
    private static class SerializationProxy implements Serializable {
        private static final long serialVersionUID = 20130226;
        private final boolean wrapRows;
        private final boolean wrapColumns;
        private final SquareNeighbourhood neighbourhood;
        private final double[][][] featuresList;

        SerializationProxy(boolean wrapRows, boolean wrapColumns, SquareNeighbourhood neighbourhood, double[][][] featuresList) {
            this.wrapRows = wrapRows;
            this.wrapColumns = wrapColumns;
            this.neighbourhood = neighbourhood;
            this.featuresList = featuresList;
        }

        private Object readResolve() {
            return new NeuronSquareMesh2D(this.wrapRows, this.wrapColumns, this.neighbourhood, this.featuresList);
        }
    }
}
