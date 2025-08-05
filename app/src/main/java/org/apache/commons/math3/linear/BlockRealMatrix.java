package org.apache.commons.math3.linear;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/BlockRealMatrix.class */
public class BlockRealMatrix extends AbstractRealMatrix implements Serializable {
    public static final int BLOCK_SIZE = 52;
    private static final long serialVersionUID = 4991895511313664478L;
    private final double[][] blocks;
    private final int rows;
    private final int columns;
    private final int blockRows;
    private final int blockColumns;

    public BlockRealMatrix(int rows, int columns) throws NotStrictlyPositiveException {
        super(rows, columns);
        this.rows = rows;
        this.columns = columns;
        this.blockRows = ((rows + 52) - 1) / 52;
        this.blockColumns = ((columns + 52) - 1) / 52;
        this.blocks = createBlocksLayout(rows, columns);
    }

    public BlockRealMatrix(double[][] rawData) throws NotStrictlyPositiveException, DimensionMismatchException {
        this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
    }

    /* JADX WARN: Type inference failed for: r1v25, types: [double[], double[][]] */
    public BlockRealMatrix(int rows, int columns, double[][] blockData, boolean copyArray) throws NotStrictlyPositiveException, DimensionMismatchException {
        super(rows, columns);
        this.rows = rows;
        this.columns = columns;
        this.blockRows = ((rows + 52) - 1) / 52;
        this.blockColumns = ((columns + 52) - 1) / 52;
        if (copyArray) {
            this.blocks = new double[this.blockRows * this.blockColumns];
        } else {
            this.blocks = blockData;
        }
        int index = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            int jBlock = 0;
            while (jBlock < this.blockColumns) {
                if (blockData[index].length != iHeight * blockWidth(jBlock)) {
                    throw new DimensionMismatchException(blockData[index].length, iHeight * blockWidth(jBlock));
                }
                if (copyArray) {
                    this.blocks[index] = (double[]) blockData[index].clone();
                }
                jBlock++;
                index++;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v17, types: [double[], double[][]] */
    public static double[][] toBlocksLayout(double[][] rawData) throws DimensionMismatchException {
        int rows = rawData.length;
        int columns = rawData[0].length;
        int blockRows = ((rows + 52) - 1) / 52;
        int blockColumns = ((columns + 52) - 1) / 52;
        for (double[] dArr : rawData) {
            int length = dArr.length;
            if (length != columns) {
                throw new DimensionMismatchException(columns, length);
            }
        }
        ?? r0 = new double[blockRows * blockColumns];
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, rows);
            int iHeight = pEnd - pStart;
            for (int jBlock = 0; jBlock < blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, columns);
                int jWidth = qEnd - qStart;
                double[] block = new double[iHeight * jWidth];
                r0[blockIndex] = block;
                int index = 0;
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    System.arraycopy(rawData[p2], qStart, block, index, jWidth);
                    index += jWidth;
                }
                blockIndex++;
            }
        }
        return r0;
    }

    /* JADX WARN: Type inference failed for: r0v10, types: [double[], double[][]] */
    public static double[][] createBlocksLayout(int rows, int columns) {
        int blockRows = ((rows + 52) - 1) / 52;
        int blockColumns = ((columns + 52) - 1) / 52;
        ?? r0 = new double[blockRows * blockColumns];
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, rows);
            int iHeight = pEnd - pStart;
            for (int jBlock = 0; jBlock < blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, columns);
                int jWidth = qEnd - qStart;
                r0[blockIndex] = new double[iHeight * jWidth];
                blockIndex++;
            }
        }
        return r0;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new BlockRealMatrix(rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix copy() {
        BlockRealMatrix copied = new BlockRealMatrix(this.rows, this.columns);
        for (int i2 = 0; i2 < this.blocks.length; i2++) {
            System.arraycopy(this.blocks[i2], 0, copied.blocks[i2], 0, this.blocks[i2].length);
        }
        return copied;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix add(RealMatrix m2) throws MatrixDimensionMismatchException {
        try {
            return add((BlockRealMatrix) m2);
        } catch (ClassCastException e2) {
            MatrixUtils.checkAdditionCompatible(this, m2);
            BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
            int blockIndex = 0;
            for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    double[] outBlock = out.blocks[blockIndex];
                    double[] tBlock = this.blocks[blockIndex];
                    int pStart = iBlock * 52;
                    int pEnd = FastMath.min(pStart + 52, this.rows);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    int k2 = 0;
                    for (int p2 = pStart; p2 < pEnd; p2++) {
                        for (int q2 = qStart; q2 < qEnd; q2++) {
                            outBlock[k2] = tBlock[k2] + m2.getEntry(p2, q2);
                            k2++;
                        }
                    }
                    blockIndex++;
                }
            }
            return out;
        }
    }

    public BlockRealMatrix add(BlockRealMatrix m2) throws MatrixDimensionMismatchException {
        MatrixUtils.checkAdditionCompatible(this, m2);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            double[] mBlock = m2.blocks[blockIndex];
            for (int k2 = 0; k2 < outBlock.length; k2++) {
                outBlock[k2] = tBlock[k2] + mBlock[k2];
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix subtract(RealMatrix m2) throws MatrixDimensionMismatchException {
        try {
            return subtract((BlockRealMatrix) m2);
        } catch (ClassCastException e2) {
            MatrixUtils.checkSubtractionCompatible(this, m2);
            BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
            int blockIndex = 0;
            for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    double[] outBlock = out.blocks[blockIndex];
                    double[] tBlock = this.blocks[blockIndex];
                    int pStart = iBlock * 52;
                    int pEnd = FastMath.min(pStart + 52, this.rows);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    int k2 = 0;
                    for (int p2 = pStart; p2 < pEnd; p2++) {
                        for (int q2 = qStart; q2 < qEnd; q2++) {
                            outBlock[k2] = tBlock[k2] - m2.getEntry(p2, q2);
                            k2++;
                        }
                    }
                    blockIndex++;
                }
            }
            return out;
        }
    }

    public BlockRealMatrix subtract(BlockRealMatrix m2) throws MatrixDimensionMismatchException {
        MatrixUtils.checkSubtractionCompatible(this, m2);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            double[] mBlock = m2.blocks[blockIndex];
            for (int k2 = 0; k2 < outBlock.length; k2++) {
                outBlock[k2] = tBlock[k2] - mBlock[k2];
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix scalarAdd(double d2) {
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            for (int k2 = 0; k2 < outBlock.length; k2++) {
                outBlock[k2] = tBlock[k2] + d2;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealMatrix scalarMultiply(double d2) {
        BlockRealMatrix out = new BlockRealMatrix(this.rows, this.columns);
        for (int blockIndex = 0; blockIndex < out.blocks.length; blockIndex++) {
            double[] outBlock = out.blocks[blockIndex];
            double[] tBlock = this.blocks[blockIndex];
            for (int k2 = 0; k2 < outBlock.length; k2++) {
                outBlock[k2] = tBlock[k2] * d2;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix multiply(RealMatrix m2) throws DimensionMismatchException {
        try {
            return multiply((BlockRealMatrix) m2);
        } catch (ClassCastException e2) {
            MatrixUtils.checkMultiplicationCompatible(this, m2);
            BlockRealMatrix out = new BlockRealMatrix(this.rows, m2.getColumnDimension());
            int blockIndex = 0;
            for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
                int pStart = iBlock * 52;
                int pEnd = FastMath.min(pStart + 52, this.rows);
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, m2.getColumnDimension());
                    double[] outBlock = out.blocks[blockIndex];
                    for (int kBlock = 0; kBlock < this.blockColumns; kBlock++) {
                        int kWidth = blockWidth(kBlock);
                        double[] tBlock = this.blocks[(iBlock * this.blockColumns) + kBlock];
                        int rStart = kBlock * 52;
                        int k2 = 0;
                        for (int p2 = pStart; p2 < pEnd; p2++) {
                            int lStart = (p2 - pStart) * kWidth;
                            int lEnd = lStart + kWidth;
                            for (int q2 = qStart; q2 < qEnd; q2++) {
                                double sum = 0.0d;
                                int r2 = rStart;
                                for (int l2 = lStart; l2 < lEnd; l2++) {
                                    sum += tBlock[l2] * m2.getEntry(r2, q2);
                                    r2++;
                                }
                                int i2 = k2;
                                outBlock[i2] = outBlock[i2] + sum;
                                k2++;
                            }
                        }
                    }
                    blockIndex++;
                }
            }
            return out;
        }
    }

    public BlockRealMatrix multiply(BlockRealMatrix m2) throws DimensionMismatchException {
        int n2;
        MatrixUtils.checkMultiplicationCompatible(this, m2);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, m2.columns);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                int jWidth = out.blockWidth(jBlock);
                int jWidth2 = jWidth + jWidth;
                int jWidth3 = jWidth2 + jWidth;
                int jWidth4 = jWidth3 + jWidth;
                double[] outBlock = out.blocks[blockIndex];
                for (int kBlock = 0; kBlock < this.blockColumns; kBlock++) {
                    int kWidth = blockWidth(kBlock);
                    double[] tBlock = this.blocks[(iBlock * this.blockColumns) + kBlock];
                    double[] mBlock = m2.blocks[(kBlock * m2.blockColumns) + jBlock];
                    int k2 = 0;
                    for (int p2 = pStart; p2 < pEnd; p2++) {
                        int lStart = (p2 - pStart) * kWidth;
                        int lEnd = lStart + kWidth;
                        for (int nStart = 0; nStart < jWidth; nStart++) {
                            double sum = 0.0d;
                            int l2 = lStart;
                            int i2 = nStart;
                            while (true) {
                                n2 = i2;
                                if (l2 >= lEnd - 3) {
                                    break;
                                }
                                sum += (tBlock[l2] * mBlock[n2]) + (tBlock[l2 + 1] * mBlock[n2 + jWidth]) + (tBlock[l2 + 2] * mBlock[n2 + jWidth2]) + (tBlock[l2 + 3] * mBlock[n2 + jWidth3]);
                                l2 += 4;
                                i2 = n2 + jWidth4;
                            }
                            while (l2 < lEnd) {
                                int i3 = l2;
                                l2++;
                                sum += tBlock[i3] * mBlock[n2];
                                n2 += jWidth;
                            }
                            int i4 = k2;
                            outBlock[i4] = outBlock[i4] + sum;
                            k2++;
                        }
                    }
                }
                blockIndex++;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[][] getData() {
        double[][] data = new double[getRowDimension()][getColumnDimension()];
        int lastColumns = this.columns - ((this.blockColumns - 1) * 52);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            int regularPos = 0;
            int lastPos = 0;
            for (int p2 = pStart; p2 < pEnd; p2++) {
                double[] dataP = data[p2];
                int blockIndex = iBlock * this.blockColumns;
                int dataPos = 0;
                for (int jBlock = 0; jBlock < this.blockColumns - 1; jBlock++) {
                    int i2 = blockIndex;
                    blockIndex++;
                    System.arraycopy(this.blocks[i2], regularPos, dataP, dataPos, 52);
                    dataPos += 52;
                }
                System.arraycopy(this.blocks[blockIndex], lastPos, dataP, dataPos, lastColumns);
                regularPos += 52;
                lastPos += lastColumns;
            }
        }
        return data;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double getNorm() {
        double[] colSums = new double[52];
        double maxColSum = 0.0d;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            Arrays.fill(colSums, 0, jWidth, 0.0d);
            for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
                int iHeight = blockHeight(iBlock);
                double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                for (int j2 = 0; j2 < jWidth; j2++) {
                    double sum = 0.0d;
                    for (int i2 = 0; i2 < iHeight; i2++) {
                        sum += FastMath.abs(block[(i2 * jWidth) + j2]);
                    }
                    int i3 = j2;
                    colSums[i3] = colSums[i3] + sum;
                }
            }
            for (int j3 = 0; j3 < jWidth; j3++) {
                maxColSum = FastMath.max(maxColSum, colSums[j3]);
            }
        }
        return maxColSum;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double getFrobeniusNorm() {
        double sum2 = 0.0d;
        for (int blockIndex = 0; blockIndex < this.blocks.length; blockIndex++) {
            double[] arr$ = this.blocks[blockIndex];
            for (double entry : arr$) {
                sum2 += entry * entry;
            }
        }
        return FastMath.sqrt(sum2);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        BlockRealMatrix out = new BlockRealMatrix((endRow - startRow) + 1, (endColumn - startColumn) + 1);
        int blockStartRow = startRow / 52;
        int rowsShift = startRow % 52;
        int blockStartColumn = startColumn / 52;
        int columnsShift = startColumn % 52;
        int pBlock = blockStartRow;
        for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
            int iHeight = out.blockHeight(iBlock);
            int qBlock = blockStartColumn;
            for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                int jWidth = out.blockWidth(jBlock);
                int outIndex = (iBlock * out.blockColumns) + jBlock;
                double[] outBlock = out.blocks[outIndex];
                int index = (pBlock * this.blockColumns) + qBlock;
                int width = blockWidth(qBlock);
                int heightExcess = (iHeight + rowsShift) - 52;
                int widthExcess = (jWidth + columnsShift) - 52;
                if (heightExcess > 0) {
                    if (widthExcess > 0) {
                        int width2 = blockWidth(qBlock + 1);
                        copyBlockPart(this.blocks[index], width, rowsShift, 52, columnsShift, 52, outBlock, jWidth, 0, 0);
                        copyBlockPart(this.blocks[index + 1], width2, rowsShift, 52, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
                        copyBlockPart(this.blocks[index + this.blockColumns], width, 0, heightExcess, columnsShift, 52, outBlock, jWidth, iHeight - heightExcess, 0);
                        copyBlockPart(this.blocks[index + this.blockColumns + 1], width2, 0, heightExcess, 0, widthExcess, outBlock, jWidth, iHeight - heightExcess, jWidth - widthExcess);
                    } else {
                        copyBlockPart(this.blocks[index], width, rowsShift, 52, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
                        copyBlockPart(this.blocks[index + this.blockColumns], width, 0, heightExcess, columnsShift, jWidth + columnsShift, outBlock, jWidth, iHeight - heightExcess, 0);
                    }
                } else if (widthExcess > 0) {
                    int width22 = blockWidth(qBlock + 1);
                    copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, 52, outBlock, jWidth, 0, 0);
                    copyBlockPart(this.blocks[index + 1], width22, rowsShift, iHeight + rowsShift, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
                } else {
                    copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
                }
                qBlock++;
            }
            pBlock++;
        }
        return out;
    }

    private void copyBlockPart(double[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, double[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn) {
        int length = srcEndColumn - srcStartColumn;
        int srcPos = (srcStartRow * srcWidth) + srcStartColumn;
        int dstPos = (dstStartRow * dstWidth) + dstStartColumn;
        for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++) {
            System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
            srcPos += srcWidth;
            dstPos += dstWidth;
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setSubMatrix(double[][] subMatrix, int row, int column) throws NumberIsTooSmallException, OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        MathUtils.checkNotNull(subMatrix);
        int refLength = subMatrix[0].length;
        if (refLength == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        int endRow = (row + subMatrix.length) - 1;
        int endColumn = (column + refLength) - 1;
        MatrixUtils.checkSubMatrixIndex(this, row, endRow, column, endColumn);
        for (double[] subRow : subMatrix) {
            if (subRow.length != refLength) {
                throw new DimensionMismatchException(refLength, subRow.length);
            }
        }
        int blockStartRow = row / 52;
        int blockEndRow = (endRow + 52) / 52;
        int blockStartColumn = column / 52;
        int blockEndColumn = (endColumn + 52) / 52;
        for (int iBlock = blockStartRow; iBlock < blockEndRow; iBlock++) {
            int iHeight = blockHeight(iBlock);
            int firstRow = iBlock * 52;
            int iStart = FastMath.max(row, firstRow);
            int iEnd = FastMath.min(endRow + 1, firstRow + iHeight);
            for (int jBlock = blockStartColumn; jBlock < blockEndColumn; jBlock++) {
                int jWidth = blockWidth(jBlock);
                int firstColumn = jBlock * 52;
                int jStart = FastMath.max(column, firstColumn);
                int jEnd = FastMath.min(endColumn + 1, firstColumn + jWidth);
                int jLength = jEnd - jStart;
                double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                for (int i2 = iStart; i2 < iEnd; i2++) {
                    System.arraycopy(subMatrix[i2 - row], jStart - column, block, ((i2 - firstRow) * jWidth) + (jStart - firstColumn), jLength);
                }
            }
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix getRowMatrix(int row) throws OutOfRangeException {
        int i2;
        MatrixUtils.checkRowIndex(this, row);
        BlockRealMatrix out = new BlockRealMatrix(1, this.columns);
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outBlockIndex = 0;
        int outIndex = 0;
        double[] outBlock = out.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            int available = outBlock.length - outIndex;
            if (jWidth > available) {
                System.arraycopy(block, iRow * jWidth, outBlock, outIndex, available);
                outBlockIndex++;
                outBlock = out.blocks[outBlockIndex];
                System.arraycopy(block, iRow * jWidth, outBlock, 0, jWidth - available);
                i2 = jWidth - available;
            } else {
                System.arraycopy(block, iRow * jWidth, outBlock, outIndex, jWidth);
                i2 = outIndex + jWidth;
            }
            outIndex = i2;
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setRowMatrix(int row, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setRowMatrix(row, (BlockRealMatrix) matrix);
        } catch (ClassCastException e2) {
            super.setRowMatrix(row, matrix);
        }
    }

    public void setRowMatrix(int row, BlockRealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        int i2;
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
        }
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int mBlockIndex = 0;
        int mIndex = 0;
        double[] mBlock = matrix.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            int available = mBlock.length - mIndex;
            if (jWidth > available) {
                System.arraycopy(mBlock, mIndex, block, iRow * jWidth, available);
                mBlockIndex++;
                mBlock = matrix.blocks[mBlockIndex];
                System.arraycopy(mBlock, 0, block, iRow * jWidth, jWidth - available);
                i2 = jWidth - available;
            } else {
                System.arraycopy(mBlock, mIndex, block, iRow * jWidth, jWidth);
                i2 = mIndex + jWidth;
            }
            mIndex = i2;
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix getColumnMatrix(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        BlockRealMatrix out = new BlockRealMatrix(this.rows, 1);
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outBlockIndex = 0;
        int outIndex = 0;
        double[] outBlock = out.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                if (outIndex >= outBlock.length) {
                    outBlockIndex++;
                    outBlock = out.blocks[outBlockIndex];
                    outIndex = 0;
                }
                int i3 = outIndex;
                outIndex++;
                outBlock[i3] = block[(i2 * jWidth) + jColumn];
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setColumnMatrix(int column, RealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumnMatrix(column, (BlockRealMatrix) matrix);
        } catch (ClassCastException e2) {
            super.setColumnMatrix(column, matrix);
        }
    }

    void setColumnMatrix(int column, BlockRealMatrix matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
        }
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int mBlockIndex = 0;
        int mIndex = 0;
        double[] mBlock = matrix.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                if (mIndex >= mBlock.length) {
                    mBlockIndex++;
                    mBlock = matrix.blocks[mBlockIndex];
                    mIndex = 0;
                }
                int i3 = mIndex;
                mIndex++;
                block[(i2 * jWidth) + jColumn] = mBlock[i3];
            }
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealVector getRowVector(int row) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        double[] outData = new double[this.columns];
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            System.arraycopy(block, iRow * jWidth, outData, outIndex, jWidth);
            outIndex += jWidth;
        }
        return new ArrayRealVector(outData, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setRowVector(int row, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setRow(row, ((ArrayRealVector) vector).getDataRef());
        } catch (ClassCastException e2) {
            super.setRowVector(row, vector);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public RealVector getColumnVector(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        double[] outData = new double[this.rows];
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                int i3 = outIndex;
                outIndex++;
                outData[i3] = block[(i2 * jWidth) + jColumn];
            }
        }
        return new ArrayRealVector(outData, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setColumnVector(int column, RealVector vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumn(column, ((ArrayRealVector) vector).getDataRef());
        } catch (ClassCastException e2) {
            super.setColumnVector(column, vector);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] getRow(int row) throws OutOfRangeException {
        MatrixUtils.checkRowIndex(this, row);
        double[] out = new double[this.columns];
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            System.arraycopy(block, iRow * jWidth, out, outIndex, jWidth);
            outIndex += jWidth;
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setRow(int row, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkRowIndex(this, row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        int iBlock = row / 52;
        int iRow = row - (iBlock * 52);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            System.arraycopy(array, outIndex, block, iRow * jWidth, jWidth);
            outIndex += jWidth;
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] getColumn(int column) throws OutOfRangeException {
        MatrixUtils.checkColumnIndex(this, column);
        double[] out = new double[this.rows];
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                int i3 = outIndex;
                outIndex++;
                out[i3] = block[(i2 * jWidth) + jColumn];
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setColumn(int column, double[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        MatrixUtils.checkColumnIndex(this, column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        }
        int jBlock = column / 52;
        int jColumn = column - (jBlock * 52);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                int i3 = outIndex;
                outIndex++;
                block[(i2 * jWidth) + jColumn] = array[i3];
            }
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double getEntry(int row, int column) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        int k2 = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
        return this.blocks[(iBlock * this.blockColumns) + jBlock][k2];
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void setEntry(int row, int column, double value) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        int k2 = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
        this.blocks[(iBlock * this.blockColumns) + jBlock][k2] = value;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void addToEntry(int row, int column, double increment) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        int k2 = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
        double[] dArr = this.blocks[(iBlock * this.blockColumns) + jBlock];
        dArr[k2] = dArr[k2] + increment;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public void multiplyEntry(int row, int column, double factor) throws OutOfRangeException {
        MatrixUtils.checkMatrixIndex(this, row, column);
        int iBlock = row / 52;
        int jBlock = column / 52;
        int k2 = ((row - (iBlock * 52)) * blockWidth(jBlock)) + (column - (jBlock * 52));
        double[] dArr = this.blocks[(iBlock * this.blockColumns) + jBlock];
        dArr[k2] = dArr[k2] * factor;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public BlockRealMatrix transpose() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        BlockRealMatrix out = new BlockRealMatrix(nCols, nRows);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < this.blockColumns; iBlock++) {
            for (int jBlock = 0; jBlock < this.blockRows; jBlock++) {
                double[] outBlock = out.blocks[blockIndex];
                double[] tBlock = this.blocks[(jBlock * this.blockColumns) + iBlock];
                int pStart = iBlock * 52;
                int pEnd = FastMath.min(pStart + 52, this.columns);
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.rows);
                int k2 = 0;
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    int lInc = pEnd - pStart;
                    int l2 = p2 - pStart;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        outBlock[k2] = tBlock[l2];
                        k2++;
                        l2 += lInc;
                    }
                }
                blockIndex++;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealLinearOperator, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] operate(double[] v2) throws DimensionMismatchException {
        if (v2.length != this.columns) {
            throw new DimensionMismatchException(v2.length, this.columns);
        }
        double[] out = new double[this.rows];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.columns);
                int k2 = 0;
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    double sum = 0.0d;
                    int q2 = qStart;
                    while (q2 < qEnd - 3) {
                        sum += (block[k2] * v2[q2]) + (block[k2 + 1] * v2[q2 + 1]) + (block[k2 + 2] * v2[q2 + 2]) + (block[k2 + 3] * v2[q2 + 3]);
                        k2 += 4;
                        q2 += 4;
                    }
                    while (q2 < qEnd) {
                        int i2 = k2;
                        k2++;
                        int i3 = q2;
                        q2++;
                        sum += block[i2] * v2[i3];
                    }
                    int i4 = p2;
                    out[i4] = out[i4] + sum;
                }
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double[] preMultiply(double[] v2) throws DimensionMismatchException {
        if (v2.length != this.rows) {
            throw new DimensionMismatchException(v2.length, this.rows);
        }
        double[] out = new double[this.columns];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            int jWidth2 = jWidth + jWidth;
            int jWidth3 = jWidth2 + jWidth;
            int jWidth4 = jWidth3 + jWidth;
            int qStart = jBlock * 52;
            int qEnd = FastMath.min(qStart + 52, this.columns);
            for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
                double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                int pStart = iBlock * 52;
                int pEnd = FastMath.min(pStart + 52, this.rows);
                for (int q2 = qStart; q2 < qEnd; q2++) {
                    int k2 = q2 - qStart;
                    double sum = 0.0d;
                    int p2 = pStart;
                    while (p2 < pEnd - 3) {
                        sum += (block[k2] * v2[p2]) + (block[k2 + jWidth] * v2[p2 + 1]) + (block[k2 + jWidth2] * v2[p2 + 2]) + (block[k2 + jWidth3] * v2[p2 + 3]);
                        k2 += jWidth4;
                        p2 += 4;
                    }
                    while (p2 < pEnd) {
                        int i2 = p2;
                        p2++;
                        sum += block[k2] * v2[i2];
                        k2 += jWidth;
                    }
                    int i3 = q2;
                    out[i3] = out[i3] + sum;
                }
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixChangingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int p2 = pStart; p2 < pEnd; p2++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                    int k2 = (p2 - pStart) * jWidth;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        block[k2] = visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int p2 = pStart; p2 < pEnd; p2++) {
                for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int qStart = jBlock * 52;
                    int qEnd = FastMath.min(qStart + 52, this.columns);
                    double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                    int k2 = (p2 - pStart) * jWidth;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
        for (int iBlock = startRow / 52; iBlock < 1 + (endRow / 52); iBlock++) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(startRow, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
            for (int p2 = pStart; p2 < pEnd; p2++) {
                for (int jBlock = startColumn / 52; jBlock < 1 + (endColumn / 52); jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int q0 = jBlock * 52;
                    int qStart = FastMath.max(startColumn, q0);
                    int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
                    double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                    int k2 = (((p2 - p0) * jWidth) + qStart) - q0;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        block[k2] = visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInRowOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
        for (int iBlock = startRow / 52; iBlock < 1 + (endRow / 52); iBlock++) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(startRow, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
            for (int p2 = pStart; p2 < pEnd; p2++) {
                for (int jBlock = startColumn / 52; jBlock < 1 + (endColumn / 52); jBlock++) {
                    int jWidth = blockWidth(jBlock);
                    int q0 = jBlock * 52;
                    int qStart = FastMath.max(startColumn, q0);
                    int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
                    double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                    int k2 = (((p2 - p0) * jWidth) + qStart) - q0;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.columns);
                double[] block = this.blocks[blockIndex];
                int k2 = 0;
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        block[k2] = visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
                blockIndex++;
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor) {
        visitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int pStart = iBlock * 52;
            int pEnd = FastMath.min(pStart + 52, this.rows);
            for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
                int qStart = jBlock * 52;
                int qEnd = FastMath.min(qStart + 52, this.columns);
                double[] block = this.blocks[blockIndex];
                int k2 = 0;
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
                blockIndex++;
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixChangingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
        for (int iBlock = startRow / 52; iBlock < 1 + (endRow / 52); iBlock++) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(startRow, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
            for (int jBlock = startColumn / 52; jBlock < 1 + (endColumn / 52); jBlock++) {
                int jWidth = blockWidth(jBlock);
                int q0 = jBlock * 52;
                int qStart = FastMath.max(startColumn, q0);
                int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
                double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    int k2 = (((p2 - p0) * jWidth) + qStart) - q0;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        block[k2] = visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
            }
        }
        return visitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractRealMatrix, org.apache.commons.math3.linear.RealMatrix
    public double walkInOptimizedOrder(RealMatrixPreservingVisitor visitor, int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        MatrixUtils.checkSubMatrixIndex(this, startRow, endRow, startColumn, endColumn);
        visitor.start(this.rows, this.columns, startRow, endRow, startColumn, endColumn);
        for (int iBlock = startRow / 52; iBlock < 1 + (endRow / 52); iBlock++) {
            int p0 = iBlock * 52;
            int pStart = FastMath.max(startRow, p0);
            int pEnd = FastMath.min((iBlock + 1) * 52, 1 + endRow);
            for (int jBlock = startColumn / 52; jBlock < 1 + (endColumn / 52); jBlock++) {
                int jWidth = blockWidth(jBlock);
                int q0 = jBlock * 52;
                int qStart = FastMath.max(startColumn, q0);
                int qEnd = FastMath.min((jBlock + 1) * 52, 1 + endColumn);
                double[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                for (int p2 = pStart; p2 < pEnd; p2++) {
                    int k2 = (((p2 - p0) * jWidth) + qStart) - q0;
                    for (int q2 = qStart; q2 < qEnd; q2++) {
                        visitor.visit(p2, q2, block[k2]);
                        k2++;
                    }
                }
            }
        }
        return visitor.end();
    }

    private int blockHeight(int blockRow) {
        if (blockRow == this.blockRows - 1) {
            return this.rows - (blockRow * 52);
        }
        return 52;
    }

    private int blockWidth(int blockColumn) {
        if (blockColumn == this.blockColumns - 1) {
            return this.columns - (blockColumn * 52);
        }
        return 52;
    }
}
