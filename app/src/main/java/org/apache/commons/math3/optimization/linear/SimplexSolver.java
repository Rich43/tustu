package org.apache.commons.math3.optimization.linear;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.util.Precision;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/linear/SimplexSolver.class */
public class SimplexSolver extends AbstractLinearOptimizer {
    private static final double DEFAULT_EPSILON = 1.0E-6d;
    private static final int DEFAULT_ULPS = 10;
    private final double epsilon;
    private final int maxUlps;

    public SimplexSolver() {
        this(1.0E-6d, 10);
    }

    public SimplexSolver(double epsilon, int maxUlps) {
        this.epsilon = epsilon;
        this.maxUlps = maxUlps;
    }

    private Integer getPivotColumn(SimplexTableau tableau) {
        double minValue = 0.0d;
        Integer minPos = null;
        for (int i2 = tableau.getNumObjectiveFunctions(); i2 < tableau.getWidth() - 1; i2++) {
            double entry = tableau.getEntry(0, i2);
            if (entry < minValue) {
                minValue = entry;
                minPos = Integer.valueOf(i2);
            }
        }
        return minPos;
    }

    private Integer getPivotRow(SimplexTableau tableau, int col) {
        List<Integer> minRatioPositions = new ArrayList<>();
        double minRatio = Double.MAX_VALUE;
        for (int i2 = tableau.getNumObjectiveFunctions(); i2 < tableau.getHeight(); i2++) {
            double rhs = tableau.getEntry(i2, tableau.getWidth() - 1);
            double entry = tableau.getEntry(i2, col);
            if (Precision.compareTo(entry, 0.0d, this.maxUlps) > 0) {
                double ratio = rhs / entry;
                int cmp = Double.compare(ratio, minRatio);
                if (cmp == 0) {
                    minRatioPositions.add(Integer.valueOf(i2));
                } else if (cmp < 0) {
                    minRatio = ratio;
                    minRatioPositions = new ArrayList<>();
                    minRatioPositions.add(Integer.valueOf(i2));
                }
            }
        }
        if (minRatioPositions.size() == 0) {
            return null;
        }
        if (minRatioPositions.size() > 1) {
            if (tableau.getNumArtificialVariables() > 0) {
                for (Integer row : minRatioPositions) {
                    for (int i3 = 0; i3 < tableau.getNumArtificialVariables(); i3++) {
                        int column = i3 + tableau.getArtificialVariableOffset();
                        if (Precision.equals(tableau.getEntry(row.intValue(), column), 1.0d, this.maxUlps) && row.equals(tableau.getBasicRow(column))) {
                            return row;
                        }
                    }
                }
            }
            if (getIterations() < getMaxIterations() / 2) {
                Integer minRow = null;
                int minIndex = tableau.getWidth();
                int varStart = tableau.getNumObjectiveFunctions();
                int varEnd = tableau.getWidth() - 1;
                for (Integer row2 : minRatioPositions) {
                    for (int i4 = varStart; i4 < varEnd && !row2.equals(minRow); i4++) {
                        Integer basicRow = tableau.getBasicRow(i4);
                        if (basicRow != null && basicRow.equals(row2) && i4 < minIndex) {
                            minIndex = i4;
                            minRow = row2;
                        }
                    }
                }
                return minRow;
            }
        }
        return minRatioPositions.get(0);
    }

    protected void doIteration(SimplexTableau tableau) throws OutOfRangeException, UnboundedSolutionException, MaxCountExceededException {
        incrementIterationsCounter();
        Integer pivotCol = getPivotColumn(tableau);
        Integer pivotRow = getPivotRow(tableau, pivotCol.intValue());
        if (pivotRow == null) {
            throw new UnboundedSolutionException();
        }
        double pivotVal = tableau.getEntry(pivotRow.intValue(), pivotCol.intValue());
        tableau.divideRow(pivotRow.intValue(), pivotVal);
        for (int i2 = 0; i2 < tableau.getHeight(); i2++) {
            if (i2 != pivotRow.intValue()) {
                double multiplier = tableau.getEntry(i2, pivotCol.intValue());
                tableau.subtractRow(i2, pivotRow.intValue(), multiplier);
            }
        }
    }

    protected void solvePhase1(SimplexTableau tableau) throws OutOfRangeException, UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        if (tableau.getNumArtificialVariables() == 0) {
            return;
        }
        while (!tableau.isOptimal()) {
            doIteration(tableau);
        }
        if (!Precision.equals(tableau.getEntry(0, tableau.getRhsOffset()), 0.0d, this.epsilon)) {
            throw new NoFeasibleSolutionException();
        }
    }

    @Override // org.apache.commons.math3.optimization.linear.AbstractLinearOptimizer
    public PointValuePair doOptimize() throws OutOfRangeException, UnboundedSolutionException, MaxCountExceededException, NoFeasibleSolutionException {
        SimplexTableau tableau = new SimplexTableau(getFunction(), getConstraints(), getGoalType(), restrictToNonNegative(), this.epsilon, this.maxUlps);
        solvePhase1(tableau);
        tableau.dropPhase1Objective();
        while (!tableau.isOptimal()) {
            doIteration(tableau);
        }
        return tableau.getSolution();
    }
}
