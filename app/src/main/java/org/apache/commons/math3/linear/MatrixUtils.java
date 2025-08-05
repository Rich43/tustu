package org.apache.commons.math3.linear;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.fraction.BigFraction;
import org.apache.commons.math3.fraction.Fraction;
import org.apache.commons.math3.geometry.VectorFormat;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/MatrixUtils.class */
public class MatrixUtils {
    public static final RealMatrixFormat DEFAULT_FORMAT = RealMatrixFormat.getInstance();
    public static final RealMatrixFormat OCTAVE_FORMAT = new RealMatrixFormat("[", "]", "", "", VectorFormat.DEFAULT_SEPARATOR, ", ");

    private MatrixUtils() {
    }

    public static RealMatrix createRealMatrix(int rows, int columns) {
        return rows * columns <= 4096 ? new Array2DRowRealMatrix(rows, columns) : new BlockRealMatrix(rows, columns);
    }

    public static <T extends FieldElement<T>> FieldMatrix<T> createFieldMatrix(Field<T> field, int rows, int columns) {
        return rows * columns <= 4096 ? new Array2DRowFieldMatrix(field, rows, columns) : new BlockFieldMatrix(field, rows, columns);
    }

    public static RealMatrix createRealMatrix(double[][] data) throws NullArgumentException, NoDataException, DimensionMismatchException {
        if (data == null || data[0] == null) {
            throw new NullArgumentException();
        }
        return data.length * data[0].length <= 4096 ? new Array2DRowRealMatrix(data) : new BlockRealMatrix(data);
    }

    public static <T extends FieldElement<T>> FieldMatrix<T> createFieldMatrix(T[][] data) throws NullArgumentException, NoDataException, DimensionMismatchException {
        if (data == null || data[0] == null) {
            throw new NullArgumentException();
        }
        return data.length * data[0].length <= 4096 ? new Array2DRowFieldMatrix(data) : new BlockFieldMatrix(data);
    }

    public static RealMatrix createRealIdentityMatrix(int dimension) throws OutOfRangeException {
        RealMatrix m2 = createRealMatrix(dimension, dimension);
        for (int i2 = 0; i2 < dimension; i2++) {
            m2.setEntry(i2, i2, 1.0d);
        }
        return m2;
    }

    public static <T extends FieldElement<T>> FieldMatrix<T> createFieldIdentityMatrix(Field<T> field, int dimension) {
        T zero = field.getZero();
        T one = field.getOne();
        FieldElement[][] fieldElementArr = (FieldElement[][]) MathArrays.buildArray(field, dimension, dimension);
        for (int row = 0; row < dimension; row++) {
            FieldElement[] fieldElementArr2 = fieldElementArr[row];
            Arrays.fill(fieldElementArr2, zero);
            fieldElementArr2[row] = one;
        }
        return new Array2DRowFieldMatrix((Field) field, fieldElementArr, false);
    }

    public static RealMatrix createRealDiagonalMatrix(double[] diagonal) throws OutOfRangeException {
        RealMatrix m2 = createRealMatrix(diagonal.length, diagonal.length);
        for (int i2 = 0; i2 < diagonal.length; i2++) {
            m2.setEntry(i2, i2, diagonal[i2]);
        }
        return m2;
    }

    public static <T extends FieldElement<T>> FieldMatrix<T> createFieldDiagonalMatrix(T[] diagonal) throws OutOfRangeException {
        FieldMatrix<T> m2 = createFieldMatrix(diagonal[0].getField2(), diagonal.length, diagonal.length);
        for (int i2 = 0; i2 < diagonal.length; i2++) {
            m2.setEntry(i2, i2, diagonal[i2]);
        }
        return m2;
    }

    public static RealVector createRealVector(double[] data) throws NullArgumentException, NoDataException {
        if (data == null) {
            throw new NullArgumentException();
        }
        return new ArrayRealVector(data, true);
    }

    public static <T extends FieldElement<T>> FieldVector<T> createFieldVector(T[] data) throws NullArgumentException, ZeroException, NoDataException {
        if (data == null) {
            throw new NullArgumentException();
        }
        if (data.length == 0) {
            throw new ZeroException(LocalizedFormats.VECTOR_MUST_HAVE_AT_LEAST_ONE_ELEMENT, new Object[0]);
        }
        return new ArrayFieldVector(data[0].getField2(), (FieldElement[]) data, true);
    }

    public static RealMatrix createRowRealMatrix(double[] rowData) throws OutOfRangeException, NullArgumentException, NoDataException {
        if (rowData == null) {
            throw new NullArgumentException();
        }
        int nCols = rowData.length;
        RealMatrix m2 = createRealMatrix(1, nCols);
        for (int i2 = 0; i2 < nCols; i2++) {
            m2.setEntry(0, i2, rowData[i2]);
        }
        return m2;
    }

    public static <T extends FieldElement<T>> FieldMatrix<T> createRowFieldMatrix(T[] rowData) throws OutOfRangeException, NullArgumentException, NoDataException {
        if (rowData == null) {
            throw new NullArgumentException();
        }
        int nCols = rowData.length;
        if (nCols == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        FieldMatrix<T> m2 = createFieldMatrix(rowData[0].getField2(), 1, nCols);
        for (int i2 = 0; i2 < nCols; i2++) {
            m2.setEntry(0, i2, rowData[i2]);
        }
        return m2;
    }

    public static RealMatrix createColumnRealMatrix(double[] columnData) throws OutOfRangeException, NullArgumentException, NoDataException {
        if (columnData == null) {
            throw new NullArgumentException();
        }
        int nRows = columnData.length;
        RealMatrix m2 = createRealMatrix(nRows, 1);
        for (int i2 = 0; i2 < nRows; i2++) {
            m2.setEntry(i2, 0, columnData[i2]);
        }
        return m2;
    }

    public static <T extends FieldElement<T>> FieldMatrix<T> createColumnFieldMatrix(T[] columnData) throws OutOfRangeException, NullArgumentException, NoDataException {
        if (columnData == null) {
            throw new NullArgumentException();
        }
        int nRows = columnData.length;
        if (nRows == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_ROW);
        }
        FieldMatrix<T> m2 = createFieldMatrix(columnData[0].getField2(), nRows, 1);
        for (int i2 = 0; i2 < nRows; i2++) {
            m2.setEntry(i2, 0, columnData[i2]);
        }
        return m2;
    }

    private static boolean isSymmetricInternal(RealMatrix matrix, double relativeTolerance, boolean raiseException) throws OutOfRangeException {
        int rows = matrix.getRowDimension();
        if (rows != matrix.getColumnDimension()) {
            if (raiseException) {
                throw new NonSquareMatrixException(rows, matrix.getColumnDimension());
            }
            return false;
        }
        for (int i2 = 0; i2 < rows; i2++) {
            for (int j2 = i2 + 1; j2 < rows; j2++) {
                double mij = matrix.getEntry(i2, j2);
                double mji = matrix.getEntry(j2, i2);
                if (FastMath.abs(mij - mji) > FastMath.max(FastMath.abs(mij), FastMath.abs(mji)) * relativeTolerance) {
                    if (raiseException) {
                        throw new NonSymmetricMatrixException(i2, j2, relativeTolerance);
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public static void checkSymmetric(RealMatrix matrix, double eps) throws OutOfRangeException {
        isSymmetricInternal(matrix, eps, true);
    }

    public static boolean isSymmetric(RealMatrix matrix, double eps) {
        return isSymmetricInternal(matrix, eps, false);
    }

    public static void checkMatrixIndex(AnyMatrix m2, int row, int column) throws OutOfRangeException {
        checkRowIndex(m2, row);
        checkColumnIndex(m2, column);
    }

    public static void checkRowIndex(AnyMatrix m2, int row) throws OutOfRangeException {
        if (row < 0 || row >= m2.getRowDimension()) {
            throw new OutOfRangeException(LocalizedFormats.ROW_INDEX, Integer.valueOf(row), 0, Integer.valueOf(m2.getRowDimension() - 1));
        }
    }

    public static void checkColumnIndex(AnyMatrix m2, int column) throws OutOfRangeException {
        if (column < 0 || column >= m2.getColumnDimension()) {
            throw new OutOfRangeException(LocalizedFormats.COLUMN_INDEX, Integer.valueOf(column), 0, Integer.valueOf(m2.getColumnDimension() - 1));
        }
    }

    public static void checkSubMatrixIndex(AnyMatrix m2, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkRowIndex(m2, startRow);
        checkRowIndex(m2, endRow);
        if (endRow < startRow) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_ROW_AFTER_FINAL_ROW, Integer.valueOf(endRow), Integer.valueOf(startRow), false);
        }
        checkColumnIndex(m2, startColumn);
        checkColumnIndex(m2, endColumn);
        if (endColumn < startColumn) {
            throw new NumberIsTooSmallException(LocalizedFormats.INITIAL_COLUMN_AFTER_FINAL_COLUMN, Integer.valueOf(endColumn), Integer.valueOf(startColumn), false);
        }
    }

    public static void checkSubMatrixIndex(AnyMatrix m2, int[] selectedRows, int[] selectedColumns) throws OutOfRangeException, NullArgumentException, NoDataException {
        if (selectedRows == null) {
            throw new NullArgumentException();
        }
        if (selectedColumns == null) {
            throw new NullArgumentException();
        }
        if (selectedRows.length == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_SELECTED_ROW_INDEX_ARRAY);
        }
        if (selectedColumns.length == 0) {
            throw new NoDataException(LocalizedFormats.EMPTY_SELECTED_COLUMN_INDEX_ARRAY);
        }
        for (int row : selectedRows) {
            checkRowIndex(m2, row);
        }
        for (int column : selectedColumns) {
            checkColumnIndex(m2, column);
        }
    }

    public static void checkAdditionCompatible(AnyMatrix left, AnyMatrix right) throws MatrixDimensionMismatchException {
        if (left.getRowDimension() != right.getRowDimension() || left.getColumnDimension() != right.getColumnDimension()) {
            throw new MatrixDimensionMismatchException(left.getRowDimension(), left.getColumnDimension(), right.getRowDimension(), right.getColumnDimension());
        }
    }

    public static void checkSubtractionCompatible(AnyMatrix left, AnyMatrix right) throws MatrixDimensionMismatchException {
        if (left.getRowDimension() != right.getRowDimension() || left.getColumnDimension() != right.getColumnDimension()) {
            throw new MatrixDimensionMismatchException(left.getRowDimension(), left.getColumnDimension(), right.getRowDimension(), right.getColumnDimension());
        }
    }

    public static void checkMultiplicationCompatible(AnyMatrix left, AnyMatrix right) throws DimensionMismatchException {
        if (left.getColumnDimension() != right.getRowDimension()) {
            throw new DimensionMismatchException(left.getColumnDimension(), right.getRowDimension());
        }
    }

    public static Array2DRowRealMatrix fractionMatrixToRealMatrix(FieldMatrix<Fraction> m2) {
        FractionMatrixConverter converter = new FractionMatrixConverter();
        m2.walkInOptimizedOrder(converter);
        return converter.getConvertedMatrix();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/MatrixUtils$FractionMatrixConverter.class */
    private static class FractionMatrixConverter extends DefaultFieldMatrixPreservingVisitor<Fraction> {
        private double[][] data;

        FractionMatrixConverter() {
            super(Fraction.ZERO);
        }

        @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            this.data = new double[rows][columns];
        }

        @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public void visit(int row, int column, Fraction value) {
            this.data[row][column] = value.doubleValue();
        }

        Array2DRowRealMatrix getConvertedMatrix() {
            return new Array2DRowRealMatrix(this.data, false);
        }
    }

    public static Array2DRowRealMatrix bigFractionMatrixToRealMatrix(FieldMatrix<BigFraction> m2) {
        BigFractionMatrixConverter converter = new BigFractionMatrixConverter();
        m2.walkInOptimizedOrder(converter);
        return converter.getConvertedMatrix();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/MatrixUtils$BigFractionMatrixConverter.class */
    private static class BigFractionMatrixConverter extends DefaultFieldMatrixPreservingVisitor<BigFraction> {
        private double[][] data;

        BigFractionMatrixConverter() {
            super(BigFraction.ZERO);
        }

        @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            this.data = new double[rows][columns];
        }

        @Override // org.apache.commons.math3.linear.DefaultFieldMatrixPreservingVisitor, org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public void visit(int row, int column, BigFraction value) {
            this.data[row][column] = value.doubleValue();
        }

        Array2DRowRealMatrix getConvertedMatrix() {
            return new Array2DRowRealMatrix(this.data, false);
        }
    }

    public static void serializeRealVector(RealVector vector, ObjectOutputStream oos) throws IOException {
        int n2 = vector.getDimension();
        oos.writeInt(n2);
        for (int i2 = 0; i2 < n2; i2++) {
            oos.writeDouble(vector.getEntry(i2));
        }
    }

    public static void deserializeRealVector(Object instance, String fieldName, ObjectInputStream ois) throws IOException, SecurityException, ClassNotFoundException, IllegalArgumentException {
        try {
            int n2 = ois.readInt();
            double[] data = new double[n2];
            for (int i2 = 0; i2 < n2; i2++) {
                data[i2] = ois.readDouble();
            }
            RealVector vector = new ArrayRealVector(data, false);
            java.lang.reflect.Field f2 = instance.getClass().getDeclaredField(fieldName);
            f2.setAccessible(true);
            f2.set(instance, vector);
        } catch (IllegalAccessException iae) {
            IOException ioe = new IOException();
            ioe.initCause(iae);
            throw ioe;
        } catch (NoSuchFieldException nsfe) {
            IOException ioe2 = new IOException();
            ioe2.initCause(nsfe);
            throw ioe2;
        }
    }

    public static void serializeRealMatrix(RealMatrix matrix, ObjectOutputStream oos) throws IOException {
        int n2 = matrix.getRowDimension();
        int m2 = matrix.getColumnDimension();
        oos.writeInt(n2);
        oos.writeInt(m2);
        for (int i2 = 0; i2 < n2; i2++) {
            for (int j2 = 0; j2 < m2; j2++) {
                oos.writeDouble(matrix.getEntry(i2, j2));
            }
        }
    }

    public static void deserializeRealMatrix(Object instance, String fieldName, ObjectInputStream ois) throws IOException, SecurityException, ClassNotFoundException, IllegalArgumentException {
        try {
            int n2 = ois.readInt();
            int m2 = ois.readInt();
            double[][] data = new double[n2][m2];
            for (int i2 = 0; i2 < n2; i2++) {
                double[] dataI = data[i2];
                for (int j2 = 0; j2 < m2; j2++) {
                    dataI[j2] = ois.readDouble();
                }
            }
            RealMatrix matrix = new Array2DRowRealMatrix(data, false);
            java.lang.reflect.Field f2 = instance.getClass().getDeclaredField(fieldName);
            f2.setAccessible(true);
            f2.set(instance, matrix);
        } catch (IllegalAccessException iae) {
            IOException ioe = new IOException();
            ioe.initCause(iae);
            throw ioe;
        } catch (NoSuchFieldException nsfe) {
            IOException ioe2 = new IOException();
            ioe2.initCause(nsfe);
            throw ioe2;
        }
    }

    public static void solveLowerTriangularSystem(RealMatrix rm, RealVector b2) throws OutOfRangeException, DimensionMismatchException, MathArithmeticException {
        if (rm == null || b2 == null || rm.getRowDimension() != b2.getDimension()) {
            throw new DimensionMismatchException(rm == null ? 0 : rm.getRowDimension(), b2 == null ? 0 : b2.getDimension());
        }
        if (rm.getColumnDimension() != rm.getRowDimension()) {
            throw new NonSquareMatrixException(rm.getRowDimension(), rm.getColumnDimension());
        }
        int rows = rm.getRowDimension();
        for (int i2 = 0; i2 < rows; i2++) {
            double diag = rm.getEntry(i2, i2);
            if (FastMath.abs(diag) < Precision.SAFE_MIN) {
                throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
            }
            double bi2 = b2.getEntry(i2) / diag;
            b2.setEntry(i2, bi2);
            for (int j2 = i2 + 1; j2 < rows; j2++) {
                b2.setEntry(j2, b2.getEntry(j2) - (bi2 * rm.getEntry(j2, i2)));
            }
        }
    }

    public static void solveUpperTriangularSystem(RealMatrix rm, RealVector b2) throws OutOfRangeException, DimensionMismatchException, MathArithmeticException {
        if (rm == null || b2 == null || rm.getRowDimension() != b2.getDimension()) {
            throw new DimensionMismatchException(rm == null ? 0 : rm.getRowDimension(), b2 == null ? 0 : b2.getDimension());
        }
        if (rm.getColumnDimension() != rm.getRowDimension()) {
            throw new NonSquareMatrixException(rm.getRowDimension(), rm.getColumnDimension());
        }
        int rows = rm.getRowDimension();
        for (int i2 = rows - 1; i2 > -1; i2--) {
            double diag = rm.getEntry(i2, i2);
            if (FastMath.abs(diag) < Precision.SAFE_MIN) {
                throw new MathArithmeticException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
            }
            double bi2 = b2.getEntry(i2) / diag;
            b2.setEntry(i2, bi2);
            for (int j2 = i2 - 1; j2 > -1; j2--) {
                b2.setEntry(j2, b2.getEntry(j2) - (bi2 * rm.getEntry(j2, i2)));
            }
        }
    }

    public static RealMatrix blockInverse(RealMatrix m2, int splitIndex) throws NumberIsTooSmallException, OutOfRangeException, MatrixDimensionMismatchException, NullArgumentException, NoDataException, SingularMatrixException, DimensionMismatchException {
        int n2 = m2.getRowDimension();
        if (m2.getColumnDimension() != n2) {
            throw new NonSquareMatrixException(m2.getRowDimension(), m2.getColumnDimension());
        }
        int splitIndex1 = splitIndex + 1;
        RealMatrix a2 = m2.getSubMatrix(0, splitIndex, 0, splitIndex);
        RealMatrix b2 = m2.getSubMatrix(0, splitIndex, splitIndex1, n2 - 1);
        RealMatrix c2 = m2.getSubMatrix(splitIndex1, n2 - 1, 0, splitIndex);
        RealMatrix d2 = m2.getSubMatrix(splitIndex1, n2 - 1, splitIndex1, n2 - 1);
        SingularValueDecomposition aDec = new SingularValueDecomposition(a2);
        DecompositionSolver aSolver = aDec.getSolver();
        if (!aSolver.isNonSingular()) {
            throw new SingularMatrixException();
        }
        RealMatrix aInv = aSolver.getInverse();
        SingularValueDecomposition dDec = new SingularValueDecomposition(d2);
        DecompositionSolver dSolver = dDec.getSolver();
        if (!dSolver.isNonSingular()) {
            throw new SingularMatrixException();
        }
        RealMatrix dInv = dSolver.getInverse();
        RealMatrix tmp1 = a2.subtract(b2.multiply(dInv).multiply(c2));
        SingularValueDecomposition tmp1Dec = new SingularValueDecomposition(tmp1);
        DecompositionSolver tmp1Solver = tmp1Dec.getSolver();
        if (!tmp1Solver.isNonSingular()) {
            throw new SingularMatrixException();
        }
        RealMatrix result00 = tmp1Solver.getInverse();
        RealMatrix tmp2 = d2.subtract(c2.multiply(aInv).multiply(b2));
        SingularValueDecomposition tmp2Dec = new SingularValueDecomposition(tmp2);
        DecompositionSolver tmp2Solver = tmp2Dec.getSolver();
        if (!tmp2Solver.isNonSingular()) {
            throw new SingularMatrixException();
        }
        RealMatrix result11 = tmp2Solver.getInverse();
        RealMatrix result01 = aInv.multiply(b2).multiply(result11).scalarMultiply(-1.0d);
        RealMatrix result10 = dInv.multiply(c2).multiply(result00).scalarMultiply(-1.0d);
        RealMatrix result = new Array2DRowRealMatrix(n2, n2);
        result.setSubMatrix(result00.getData(), 0, 0);
        result.setSubMatrix(result01.getData(), 0, splitIndex1);
        result.setSubMatrix(result10.getData(), splitIndex1, 0);
        result.setSubMatrix(result11.getData(), splitIndex1, splitIndex1);
        return result;
    }

    public static RealMatrix inverse(RealMatrix matrix) throws NullArgumentException, NonSquareMatrixException, SingularMatrixException {
        return inverse(matrix, 0.0d);
    }

    public static RealMatrix inverse(RealMatrix matrix, double threshold) throws NullArgumentException, NonSquareMatrixException, SingularMatrixException {
        MathUtils.checkNotNull(matrix);
        if (!matrix.isSquare()) {
            throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
        }
        if (matrix instanceof DiagonalMatrix) {
            return ((DiagonalMatrix) matrix).inverse(threshold);
        }
        QRDecomposition decomposition = new QRDecomposition(matrix, threshold);
        return decomposition.getSolver().getInverse();
    }
}
