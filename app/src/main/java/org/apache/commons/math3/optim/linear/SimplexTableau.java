package org.apache.commons.math3.optim.linear;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.icepdf.core.util.PdfOps;
import sun.util.locale.LanguageTag;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/linear/SimplexTableau.class */
class SimplexTableau implements Serializable {
    private static final String NEGATIVE_VAR_COLUMN_LABEL = "x-";
    private static final long serialVersionUID = -1369660067587938365L;

    /* renamed from: f, reason: collision with root package name */
    private final LinearObjectiveFunction f13070f;
    private final List<LinearConstraint> constraints;
    private final boolean restrictToNonNegative;
    private final List<String> columnLabels;
    private transient Array2DRowRealMatrix tableau;
    private final int numDecisionVariables;
    private final int numSlackVariables;
    private int numArtificialVariables;
    private final double epsilon;
    private final int maxUlps;
    private int[] basicVariables;
    private int[] basicRows;

    SimplexTableau(LinearObjectiveFunction f2, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative, double epsilon) {
        this(f2, constraints, goalType, restrictToNonNegative, epsilon, 10);
    }

    SimplexTableau(LinearObjectiveFunction f2, Collection<LinearConstraint> constraints, GoalType goalType, boolean restrictToNonNegative, double epsilon, int maxUlps) {
        this.columnLabels = new ArrayList();
        this.f13070f = f2;
        this.constraints = normalizeConstraints(constraints);
        this.restrictToNonNegative = restrictToNonNegative;
        this.epsilon = epsilon;
        this.maxUlps = maxUlps;
        this.numDecisionVariables = f2.getCoefficients().getDimension() + (restrictToNonNegative ? 0 : 1);
        this.numSlackVariables = getConstraintTypeCounts(Relationship.LEQ) + getConstraintTypeCounts(Relationship.GEQ);
        this.numArtificialVariables = getConstraintTypeCounts(Relationship.EQ) + getConstraintTypeCounts(Relationship.GEQ);
        this.tableau = createTableau(goalType == GoalType.MAXIMIZE);
        initializeBasicVariables(getSlackVariableOffset());
        initializeColumnLabels();
    }

    protected void initializeColumnLabels() {
        if (getNumObjectiveFunctions() == 2) {
            this.columnLabels.add(PdfOps.W_TOKEN);
        }
        this.columnLabels.add(Constants.HASIDCALL_INDEX_SIG);
        for (int i2 = 0; i2 < getOriginalNumDecisionVariables(); i2++) {
            this.columnLabels.add(LanguageTag.PRIVATEUSE + i2);
        }
        if (!this.restrictToNonNegative) {
            this.columnLabels.add(NEGATIVE_VAR_COLUMN_LABEL);
        }
        for (int i3 = 0; i3 < getNumSlackVariables(); i3++) {
            this.columnLabels.add(PdfOps.s_TOKEN + i3);
        }
        for (int i4 = 0; i4 < getNumArtificialVariables(); i4++) {
            this.columnLabels.add("a" + i4);
        }
        this.columnLabels.add("RHS");
    }

    protected Array2DRowRealMatrix createTableau(boolean maximize) throws OutOfRangeException {
        int width = this.numDecisionVariables + this.numSlackVariables + this.numArtificialVariables + getNumObjectiveFunctions() + 1;
        int height = this.constraints.size() + getNumObjectiveFunctions();
        Array2DRowRealMatrix matrix = new Array2DRowRealMatrix(height, width);
        if (getNumObjectiveFunctions() == 2) {
            matrix.setEntry(0, 0, -1.0d);
        }
        int zIndex = getNumObjectiveFunctions() == 1 ? 0 : 1;
        matrix.setEntry(zIndex, zIndex, maximize ? 1.0d : -1.0d);
        RealVector objectiveCoefficients = maximize ? this.f13070f.getCoefficients().mapMultiply(-1.0d) : this.f13070f.getCoefficients();
        copyArray(objectiveCoefficients.toArray(), matrix.getDataRef()[zIndex]);
        matrix.setEntry(zIndex, width - 1, maximize ? this.f13070f.getConstantTerm() : (-1.0d) * this.f13070f.getConstantTerm());
        if (!this.restrictToNonNegative) {
            matrix.setEntry(zIndex, getSlackVariableOffset() - 1, getInvertedCoefficientSum(objectiveCoefficients));
        }
        int slackVar = 0;
        int artificialVar = 0;
        for (int i2 = 0; i2 < this.constraints.size(); i2++) {
            LinearConstraint constraint = this.constraints.get(i2);
            int row = getNumObjectiveFunctions() + i2;
            copyArray(constraint.getCoefficients().toArray(), matrix.getDataRef()[row]);
            if (!this.restrictToNonNegative) {
                matrix.setEntry(row, getSlackVariableOffset() - 1, getInvertedCoefficientSum(constraint.getCoefficients()));
            }
            matrix.setEntry(row, width - 1, constraint.getValue());
            if (constraint.getRelationship() == Relationship.LEQ) {
                int i3 = slackVar;
                slackVar++;
                matrix.setEntry(row, getSlackVariableOffset() + i3, 1.0d);
            } else if (constraint.getRelationship() == Relationship.GEQ) {
                int i4 = slackVar;
                slackVar++;
                matrix.setEntry(row, getSlackVariableOffset() + i4, -1.0d);
            }
            if (constraint.getRelationship() == Relationship.EQ || constraint.getRelationship() == Relationship.GEQ) {
                matrix.setEntry(0, getArtificialVariableOffset() + artificialVar, 1.0d);
                int i5 = artificialVar;
                artificialVar++;
                matrix.setEntry(row, getArtificialVariableOffset() + i5, 1.0d);
                matrix.setRowVector(0, matrix.getRowVector(0).subtract(matrix.getRowVector(row)));
            }
        }
        return matrix;
    }

    public List<LinearConstraint> normalizeConstraints(Collection<LinearConstraint> originalConstraints) {
        List<LinearConstraint> normalized = new ArrayList<>(originalConstraints.size());
        for (LinearConstraint constraint : originalConstraints) {
            normalized.add(normalize(constraint));
        }
        return normalized;
    }

    private LinearConstraint normalize(LinearConstraint constraint) {
        if (constraint.getValue() < 0.0d) {
            return new LinearConstraint(constraint.getCoefficients().mapMultiply(-1.0d), constraint.getRelationship().oppositeRelationship(), (-1.0d) * constraint.getValue());
        }
        return new LinearConstraint(constraint.getCoefficients(), constraint.getRelationship(), constraint.getValue());
    }

    protected final int getNumObjectiveFunctions() {
        return this.numArtificialVariables > 0 ? 2 : 1;
    }

    private int getConstraintTypeCounts(Relationship relationship) {
        int count = 0;
        for (LinearConstraint constraint : this.constraints) {
            if (constraint.getRelationship() == relationship) {
                count++;
            }
        }
        return count;
    }

    protected static double getInvertedCoefficientSum(RealVector coefficients) {
        double sum = 0.0d;
        double[] arr$ = coefficients.toArray();
        for (double coefficient : arr$) {
            sum -= coefficient;
        }
        return sum;
    }

    protected Integer getBasicRow(int col) {
        int row = this.basicVariables[col];
        if (row == -1) {
            return null;
        }
        return Integer.valueOf(row);
    }

    protected int getBasicVariable(int row) {
        return this.basicRows[row];
    }

    private void initializeBasicVariables(int startColumn) {
        this.basicVariables = new int[getWidth() - 1];
        this.basicRows = new int[getHeight()];
        Arrays.fill(this.basicVariables, -1);
        for (int i2 = startColumn; i2 < getWidth() - 1; i2++) {
            Integer row = findBasicRow(i2);
            if (row != null) {
                this.basicVariables[i2] = row.intValue();
                this.basicRows[row.intValue()] = i2;
            }
        }
    }

    private Integer findBasicRow(int col) {
        Integer row = null;
        for (int i2 = 0; i2 < getHeight(); i2++) {
            double entry = getEntry(i2, col);
            if (Precision.equals(entry, 1.0d, this.maxUlps) && row == null) {
                row = Integer.valueOf(i2);
            } else if (!Precision.equals(entry, 0.0d, this.maxUlps)) {
                return null;
            }
        }
        return row;
    }

    protected void dropPhase1Objective() {
        if (getNumObjectiveFunctions() == 1) {
            return;
        }
        Set<Integer> columnsToDrop = new TreeSet<>();
        columnsToDrop.add(0);
        for (int i2 = getNumObjectiveFunctions(); i2 < getArtificialVariableOffset(); i2++) {
            double entry = getEntry(0, i2);
            if (Precision.compareTo(entry, 0.0d, this.epsilon) > 0) {
                columnsToDrop.add(Integer.valueOf(i2));
            }
        }
        for (int i3 = 0; i3 < getNumArtificialVariables(); i3++) {
            int col = i3 + getArtificialVariableOffset();
            if (getBasicRow(col) == null) {
                columnsToDrop.add(Integer.valueOf(col));
            }
        }
        double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
        for (int i4 = 1; i4 < getHeight(); i4++) {
            int col2 = 0;
            for (int j2 = 0; j2 < getWidth(); j2++) {
                if (!columnsToDrop.contains(Integer.valueOf(j2))) {
                    int i5 = col2;
                    col2++;
                    matrix[i4 - 1][i5] = getEntry(i4, j2);
                }
            }
        }
        Integer[] drop = (Integer[]) columnsToDrop.toArray(new Integer[columnsToDrop.size()]);
        for (int i6 = drop.length - 1; i6 >= 0; i6--) {
            this.columnLabels.remove(drop[i6].intValue());
        }
        this.tableau = new Array2DRowRealMatrix(matrix);
        this.numArtificialVariables = 0;
        initializeBasicVariables(getNumObjectiveFunctions());
    }

    private void copyArray(double[] src, double[] dest) {
        System.arraycopy(src, 0, dest, getNumObjectiveFunctions(), src.length);
    }

    boolean isOptimal() {
        double[] objectiveFunctionRow = getRow(0);
        int end = getRhsOffset();
        for (int i2 = getNumObjectiveFunctions(); i2 < end; i2++) {
            double entry = objectiveFunctionRow[i2];
            if (Precision.compareTo(entry, 0.0d, this.epsilon) < 0) {
                return false;
            }
        }
        return true;
    }

    protected PointValuePair getSolution() {
        int negativeVarColumn = this.columnLabels.indexOf(NEGATIVE_VAR_COLUMN_LABEL);
        Integer negativeVarBasicRow = negativeVarColumn > 0 ? getBasicRow(negativeVarColumn) : null;
        double mostNegative = negativeVarBasicRow == null ? 0.0d : getEntry(negativeVarBasicRow.intValue(), getRhsOffset());
        Set<Integer> usedBasicRows = new HashSet<>();
        double[] coefficients = new double[getOriginalNumDecisionVariables()];
        for (int i2 = 0; i2 < coefficients.length; i2++) {
            int colIndex = this.columnLabels.indexOf(LanguageTag.PRIVATEUSE + i2);
            if (colIndex < 0) {
                coefficients[i2] = 0.0d;
            } else {
                Integer basicRow = getBasicRow(colIndex);
                if (basicRow != null && basicRow.intValue() == 0) {
                    coefficients[i2] = 0.0d;
                } else if (usedBasicRows.contains(basicRow)) {
                    coefficients[i2] = 0.0d - (this.restrictToNonNegative ? 0.0d : mostNegative);
                } else {
                    usedBasicRows.add(basicRow);
                    coefficients[i2] = (basicRow == null ? 0.0d : getEntry(basicRow.intValue(), getRhsOffset())) - (this.restrictToNonNegative ? 0.0d : mostNegative);
                }
            }
        }
        return new PointValuePair(coefficients, this.f13070f.value(coefficients));
    }

    protected void performRowOperations(int pivotCol, int pivotRow) {
        double pivotVal = getEntry(pivotRow, pivotCol);
        divideRow(pivotRow, pivotVal);
        for (int i2 = 0; i2 < getHeight(); i2++) {
            if (i2 != pivotRow) {
                double multiplier = getEntry(i2, pivotCol);
                if (multiplier != 0.0d) {
                    subtractRow(i2, pivotRow, multiplier);
                }
            }
        }
        int previousBasicVariable = getBasicVariable(pivotRow);
        this.basicVariables[previousBasicVariable] = -1;
        this.basicVariables[pivotCol] = pivotRow;
        this.basicRows[pivotRow] = pivotCol;
    }

    protected void divideRow(int dividendRowIndex, double divisor) {
        double[] dividendRow = getRow(dividendRowIndex);
        for (int j2 = 0; j2 < getWidth(); j2++) {
            int i2 = j2;
            dividendRow[i2] = dividendRow[i2] / divisor;
        }
    }

    protected void subtractRow(int minuendRowIndex, int subtrahendRowIndex, double multiplier) {
        double[] minuendRow = getRow(minuendRowIndex);
        double[] subtrahendRow = getRow(subtrahendRowIndex);
        for (int i2 = 0; i2 < getWidth(); i2++) {
            int i3 = i2;
            minuendRow[i3] = minuendRow[i3] - (subtrahendRow[i2] * multiplier);
        }
    }

    protected final int getWidth() {
        return this.tableau.getColumnDimension();
    }

    protected final int getHeight() {
        return this.tableau.getRowDimension();
    }

    protected final double getEntry(int row, int column) {
        return this.tableau.getEntry(row, column);
    }

    protected final void setEntry(int row, int column, double value) throws OutOfRangeException {
        this.tableau.setEntry(row, column, value);
    }

    protected final int getSlackVariableOffset() {
        return getNumObjectiveFunctions() + this.numDecisionVariables;
    }

    protected final int getArtificialVariableOffset() {
        return getNumObjectiveFunctions() + this.numDecisionVariables + this.numSlackVariables;
    }

    protected final int getRhsOffset() {
        return getWidth() - 1;
    }

    protected final int getNumDecisionVariables() {
        return this.numDecisionVariables;
    }

    protected final int getOriginalNumDecisionVariables() {
        return this.f13070f.getCoefficients().getDimension();
    }

    protected final int getNumSlackVariables() {
        return this.numSlackVariables;
    }

    protected final int getNumArtificialVariables() {
        return this.numArtificialVariables;
    }

    protected final double[] getRow(int row) {
        return this.tableau.getDataRef()[row];
    }

    protected final double[][] getData() {
        return this.tableau.getData();
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof SimplexTableau) {
            SimplexTableau rhs = (SimplexTableau) other;
            return this.restrictToNonNegative == rhs.restrictToNonNegative && this.numDecisionVariables == rhs.numDecisionVariables && this.numSlackVariables == rhs.numSlackVariables && this.numArtificialVariables == rhs.numArtificialVariables && this.epsilon == rhs.epsilon && this.maxUlps == rhs.maxUlps && this.f13070f.equals(rhs.f13070f) && this.constraints.equals(rhs.constraints) && this.tableau.equals(rhs.tableau);
        }
        return false;
    }

    public int hashCode() {
        return (((((((Boolean.valueOf(this.restrictToNonNegative).hashCode() ^ this.numDecisionVariables) ^ this.numSlackVariables) ^ this.numArtificialVariables) ^ Double.valueOf(this.epsilon).hashCode()) ^ this.maxUlps) ^ this.f13070f.hashCode()) ^ this.constraints.hashCode()) ^ this.tableau.hashCode();
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        MatrixUtils.serializeRealMatrix(this.tableau, oos);
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException, SecurityException, IllegalArgumentException {
        ois.defaultReadObject();
        MatrixUtils.deserializeRealMatrix(this, "tableau", ois);
    }
}
