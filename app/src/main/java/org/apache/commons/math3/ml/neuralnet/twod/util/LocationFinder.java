package org.apache.commons.math3.ml.neuralnet.twod.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.ml.neuralnet.Neuron;
import org.apache.commons.math3.ml.neuralnet.twod.NeuronSquareMesh2D;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/LocationFinder.class */
public class LocationFinder {
    private final Map<Long, Location> locations = new HashMap();

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ml/neuralnet/twod/util/LocationFinder$Location.class */
    public static class Location {
        private final int row;
        private final int column;

        public Location(int row, int column) {
            this.row = row;
            this.column = column;
        }

        public int getRow() {
            return this.row;
        }

        public int getColumn() {
            return this.column;
        }
    }

    public LocationFinder(NeuronSquareMesh2D map) {
        int nR = map.getNumberOfRows();
        int nC = map.getNumberOfColumns();
        for (int r2 = 0; r2 < nR; r2++) {
            for (int c2 = 0; c2 < nC; c2++) {
                Long id = Long.valueOf(map.getNeuron(r2, c2).getIdentifier());
                if (this.locations.get(id) != null) {
                    throw new MathIllegalStateException();
                }
                this.locations.put(id, new Location(r2, c2));
            }
        }
    }

    public Location getLocation(Neuron n2) {
        return this.locations.get(Long.valueOf(n2.getIdentifier()));
    }
}
