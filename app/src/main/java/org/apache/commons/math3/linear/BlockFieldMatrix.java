package org.apache.commons.math3.linear;

import java.io.Serializable;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/BlockFieldMatrix.class */
public class BlockFieldMatrix<T extends FieldElement<T>> extends AbstractFieldMatrix<T> implements Serializable {
    public static final int BLOCK_SIZE = 36;
    private static final long serialVersionUID = -4602336630143123183L;
    private final T[][] blocks;
    private final int rows;
    private final int columns;
    private final int blockRows;
    private final int blockColumns;

    public BlockFieldMatrix(Field<T> field, int i2, int i3) throws NotStrictlyPositiveException {
        super(field, i2, i3);
        this.rows = i2;
        this.columns = i3;
        this.blockRows = ((i2 + 36) - 1) / 36;
        this.blockColumns = ((i3 + 36) - 1) / 36;
        this.blocks = (T[][]) createBlocksLayout(field, i2, i3);
    }

    public BlockFieldMatrix(T[][] rawData) throws DimensionMismatchException {
        this(rawData.length, rawData[0].length, toBlocksLayout(rawData), false);
    }

    public BlockFieldMatrix(int i2, int i3, T[][] tArr, boolean z2) throws NotStrictlyPositiveException, DimensionMismatchException {
        super(extractField(tArr), i2, i3);
        this.rows = i2;
        this.columns = i3;
        this.blockRows = ((i2 + 36) - 1) / 36;
        this.blockColumns = ((i3 + 36) - 1) / 36;
        if (z2) {
            this.blocks = (T[][]) ((FieldElement[][]) MathArrays.buildArray(getField(), this.blockRows * this.blockColumns, -1));
        } else {
            this.blocks = tArr;
        }
        int i4 = 0;
        for (int i5 = 0; i5 < this.blockRows; i5++) {
            int iBlockHeight = blockHeight(i5);
            int i6 = 0;
            while (i6 < this.blockColumns) {
                if (tArr[i4].length != iBlockHeight * blockWidth(i6)) {
                    throw new DimensionMismatchException(tArr[i4].length, iBlockHeight * blockWidth(i6));
                }
                if (z2) {
                    ((T[][]) this.blocks)[i4] = (FieldElement[]) tArr[i4].clone();
                }
                i6++;
                i4++;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends FieldElement<T>> T[][] toBlocksLayout(T[][] tArr) throws DimensionMismatchException {
        int length = tArr.length;
        int length2 = tArr[0].length;
        int i2 = ((length + 36) - 1) / 36;
        int i3 = ((length2 + 36) - 1) / 36;
        for (T[] tArr2 : tArr) {
            int length3 = tArr2.length;
            if (length3 != length2) {
                throw new DimensionMismatchException(length2, length3);
            }
        }
        Field fieldExtractField = extractField(tArr);
        T[][] tArr3 = (T[][]) ((FieldElement[][]) MathArrays.buildArray(fieldExtractField, i2 * i3, -1));
        int i4 = 0;
        for (int i5 = 0; i5 < i2; i5++) {
            int i6 = i5 * 36;
            int iMin = FastMath.min(i6 + 36, length);
            int i7 = iMin - i6;
            for (int i8 = 0; i8 < i3; i8++) {
                int i9 = i8 * 36;
                int iMin2 = FastMath.min(i9 + 36, length2) - i9;
                FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(fieldExtractField, i7 * iMin2);
                tArr3[i4] = fieldElementArr;
                int i10 = 0;
                for (int i11 = i6; i11 < iMin; i11++) {
                    System.arraycopy(tArr[i11], i9, fieldElementArr, i10, iMin2);
                    i10 += iMin2;
                }
                i4++;
            }
        }
        return tArr3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static <T extends FieldElement<T>> T[][] createBlocksLayout(Field<T> field, int i2, int i3) {
        int i4 = ((i2 + 36) - 1) / 36;
        int i5 = ((i3 + 36) - 1) / 36;
        T[][] tArr = (T[][]) ((FieldElement[][]) MathArrays.buildArray(field, i4 * i5, -1));
        int i6 = 0;
        for (int i7 = 0; i7 < i4; i7++) {
            int i8 = i7 * 36;
            int iMin = FastMath.min(i8 + 36, i2) - i8;
            for (int i9 = 0; i9 < i5; i9++) {
                int i10 = i9 * 36;
                tArr[i6] = (FieldElement[]) MathArrays.buildArray(field, iMin * (FastMath.min(i10 + 36, i3) - i10));
                i6++;
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> createMatrix(int rowDimension, int columnDimension) throws NotStrictlyPositiveException {
        return new BlockFieldMatrix(getField(), rowDimension, columnDimension);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> copy() {
        BlockFieldMatrix<T> copied = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i2 = 0; i2 < this.blocks.length; i2++) {
            System.arraycopy(this.blocks[i2], 0, copied.blocks[i2], 0, this.blocks[i2].length);
        }
        return copied;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> add(FieldMatrix<T> fieldMatrix) throws MatrixDimensionMismatchException {
        try {
            return add((BlockFieldMatrix) fieldMatrix);
        } catch (ClassCastException e2) {
            checkAdditionCompatible(fieldMatrix);
            BlockFieldMatrix blockFieldMatrix = new BlockFieldMatrix(getField(), this.rows, this.columns);
            int i2 = 0;
            for (int i3 = 0; i3 < blockFieldMatrix.blockRows; i3++) {
                for (int i4 = 0; i4 < blockFieldMatrix.blockColumns; i4++) {
                    FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix.blocks)[i2];
                    T[] tArr = this.blocks[i2];
                    int i5 = i3 * 36;
                    int iMin = FastMath.min(i5 + 36, this.rows);
                    int i6 = i4 * 36;
                    int iMin2 = FastMath.min(i6 + 36, this.columns);
                    int i7 = 0;
                    for (int i8 = i5; i8 < iMin; i8++) {
                        for (int i9 = i6; i9 < iMin2; i9++) {
                            fieldElementArr[i7] = (FieldElement) tArr[i7].add(fieldMatrix.getEntry(i8, i9));
                            i7++;
                        }
                    }
                    i2++;
                }
            }
            return blockFieldMatrix;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BlockFieldMatrix<T> add(BlockFieldMatrix<T> blockFieldMatrix) throws MatrixDimensionMismatchException {
        checkAdditionCompatible(blockFieldMatrix);
        BlockFieldMatrix<T> blockFieldMatrix2 = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i2 = 0; i2 < blockFieldMatrix2.blocks.length; i2++) {
            FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix2.blocks)[i2];
            T[] tArr = this.blocks[i2];
            T[] tArr2 = blockFieldMatrix.blocks[i2];
            for (int i3 = 0; i3 < fieldElementArr.length; i3++) {
                fieldElementArr[i3] = (FieldElement) tArr[i3].add(tArr2[i3]);
            }
        }
        return blockFieldMatrix2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> subtract(FieldMatrix<T> fieldMatrix) throws MatrixDimensionMismatchException {
        try {
            return subtract((BlockFieldMatrix) fieldMatrix);
        } catch (ClassCastException e2) {
            checkSubtractionCompatible(fieldMatrix);
            BlockFieldMatrix blockFieldMatrix = new BlockFieldMatrix(getField(), this.rows, this.columns);
            int i2 = 0;
            for (int i3 = 0; i3 < blockFieldMatrix.blockRows; i3++) {
                for (int i4 = 0; i4 < blockFieldMatrix.blockColumns; i4++) {
                    FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix.blocks)[i2];
                    T[] tArr = this.blocks[i2];
                    int i5 = i3 * 36;
                    int iMin = FastMath.min(i5 + 36, this.rows);
                    int i6 = i4 * 36;
                    int iMin2 = FastMath.min(i6 + 36, this.columns);
                    int i7 = 0;
                    for (int i8 = i5; i8 < iMin; i8++) {
                        for (int i9 = i6; i9 < iMin2; i9++) {
                            fieldElementArr[i7] = (FieldElement) tArr[i7].subtract(fieldMatrix.getEntry(i8, i9));
                            i7++;
                        }
                    }
                    i2++;
                }
            }
            return blockFieldMatrix;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public BlockFieldMatrix<T> subtract(BlockFieldMatrix<T> blockFieldMatrix) throws MatrixDimensionMismatchException {
        checkSubtractionCompatible(blockFieldMatrix);
        BlockFieldMatrix<T> blockFieldMatrix2 = new BlockFieldMatrix<>(getField(), this.rows, this.columns);
        for (int i2 = 0; i2 < blockFieldMatrix2.blocks.length; i2++) {
            FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix2.blocks)[i2];
            T[] tArr = this.blocks[i2];
            T[] tArr2 = blockFieldMatrix.blocks[i2];
            for (int i3 = 0; i3 < fieldElementArr.length; i3++) {
                fieldElementArr[i3] = (FieldElement) tArr[i3].subtract(tArr2[i3]);
            }
        }
        return blockFieldMatrix2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> scalarAdd(T t2) {
        BlockFieldMatrix blockFieldMatrix = new BlockFieldMatrix(getField(), this.rows, this.columns);
        for (int i2 = 0; i2 < blockFieldMatrix.blocks.length; i2++) {
            FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix.blocks)[i2];
            T[] tArr = this.blocks[i2];
            for (int i3 = 0; i3 < fieldElementArr.length; i3++) {
                fieldElementArr[i3] = (FieldElement) tArr[i3].add(t2);
            }
        }
        return blockFieldMatrix;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> scalarMultiply(T t2) {
        BlockFieldMatrix blockFieldMatrix = new BlockFieldMatrix(getField(), this.rows, this.columns);
        for (int i2 = 0; i2 < blockFieldMatrix.blocks.length; i2++) {
            FieldElement[] fieldElementArr = ((T[][]) blockFieldMatrix.blocks)[i2];
            T[] tArr = this.blocks[i2];
            for (int i3 = 0; i3 < fieldElementArr.length; i3++) {
                fieldElementArr[i3] = (FieldElement) tArr[i3].multiply(t2);
            }
        }
        return blockFieldMatrix;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v51, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> multiply(FieldMatrix<T> m2) throws DimensionMismatchException {
        try {
            return multiply((BlockFieldMatrix) m2);
        } catch (ClassCastException e2) {
            checkMultiplicationCompatible(m2);
            BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, m2.getColumnDimension());
            T zero = getField().getZero();
            int blockIndex = 0;
            for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
                int pStart = iBlock * 36;
                int pEnd = FastMath.min(pStart + 36, this.rows);
                for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                    int qStart = jBlock * 36;
                    int qEnd = FastMath.min(qStart + 36, m2.getColumnDimension());
                    FieldElement[] fieldElementArr = out.blocks[blockIndex];
                    for (int kBlock = 0; kBlock < this.blockColumns; kBlock++) {
                        int kWidth = blockWidth(kBlock);
                        T[] tBlock = this.blocks[(iBlock * this.blockColumns) + kBlock];
                        int rStart = kBlock * 36;
                        int k2 = 0;
                        for (int p2 = pStart; p2 < pEnd; p2++) {
                            int lStart = (p2 - pStart) * kWidth;
                            int lEnd = lStart + kWidth;
                            for (int q2 = qStart; q2 < qEnd; q2++) {
                                T sum = zero;
                                int r2 = rStart;
                                for (int l2 = lStart; l2 < lEnd; l2++) {
                                    sum = (FieldElement) sum.add(tBlock[l2].multiply(m2.getEntry(r2, q2)));
                                    r2++;
                                }
                                fieldElementArr[k2] = (FieldElement) fieldElementArr[k2].add(sum);
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

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v57, types: [org.apache.commons.math3.FieldElement] */
    /* JADX WARN: Type inference failed for: r0v68, types: [org.apache.commons.math3.FieldElement] */
    public BlockFieldMatrix<T> multiply(BlockFieldMatrix<T> m2) throws DimensionMismatchException {
        int n2;
        checkMultiplicationCompatible(m2);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, m2.columns);
        T zero = getField().getZero();
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
            int pStart = iBlock * 36;
            int pEnd = FastMath.min(pStart + 36, this.rows);
            for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                int jWidth = out.blockWidth(jBlock);
                int jWidth2 = jWidth + jWidth;
                int jWidth3 = jWidth2 + jWidth;
                int jWidth4 = jWidth3 + jWidth;
                FieldElement[] fieldElementArr = out.blocks[blockIndex];
                for (int kBlock = 0; kBlock < this.blockColumns; kBlock++) {
                    int kWidth = blockWidth(kBlock);
                    T[] tBlock = this.blocks[(iBlock * this.blockColumns) + kBlock];
                    T[] mBlock = m2.blocks[(kBlock * m2.blockColumns) + jBlock];
                    int k2 = 0;
                    for (int p2 = pStart; p2 < pEnd; p2++) {
                        int lStart = (p2 - pStart) * kWidth;
                        int lEnd = lStart + kWidth;
                        for (int nStart = 0; nStart < jWidth; nStart++) {
                            T sum = zero;
                            int l2 = lStart;
                            int i2 = nStart;
                            while (true) {
                                n2 = i2;
                                if (l2 >= lEnd - 3) {
                                    break;
                                }
                                sum = (FieldElement) ((FieldElement) ((FieldElement) ((FieldElement) sum.add(tBlock[l2].multiply(mBlock[n2]))).add(tBlock[l2 + 1].multiply(mBlock[n2 + jWidth]))).add(tBlock[l2 + 2].multiply(mBlock[n2 + jWidth2]))).add(tBlock[l2 + 3].multiply(mBlock[n2 + jWidth3]));
                                l2 += 4;
                                i2 = n2 + jWidth4;
                            }
                            while (l2 < lEnd) {
                                int i3 = l2;
                                l2++;
                                sum = (FieldElement) sum.add(tBlock[i3].multiply(mBlock[n2]));
                                n2 += jWidth;
                            }
                            fieldElementArr[k2] = (FieldElement) fieldElementArr[k2].add(sum);
                            k2++;
                        }
                    }
                }
                blockIndex++;
            }
        }
        return out;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[][] getData() {
        T[][] tArr = (T[][]) ((FieldElement[][]) MathArrays.buildArray(getField(), getRowDimension(), getColumnDimension()));
        int i2 = this.columns - ((this.blockColumns - 1) * 36);
        for (int i3 = 0; i3 < this.blockRows; i3++) {
            int i4 = i3 * 36;
            int iMin = FastMath.min(i4 + 36, this.rows);
            int i5 = 0;
            int i6 = 0;
            for (int i7 = i4; i7 < iMin; i7++) {
                T[] tArr2 = tArr[i7];
                int i8 = i3 * this.blockColumns;
                int i9 = 0;
                for (int i10 = 0; i10 < this.blockColumns - 1; i10++) {
                    int i11 = i8;
                    i8++;
                    System.arraycopy(this.blocks[i11], i5, tArr2, i9, 36);
                    i9 += 36;
                }
                System.arraycopy(this.blocks[i8], i6, tArr2, i9, i2);
                i5 += 36;
                i6 += i2;
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getSubMatrix(int startRow, int endRow, int startColumn, int endColumn) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(startRow, endRow, startColumn, endColumn);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), (endRow - startRow) + 1, (endColumn - startColumn) + 1);
        int blockStartRow = startRow / 36;
        int rowsShift = startRow % 36;
        int blockStartColumn = startColumn / 36;
        int columnsShift = startColumn % 36;
        int pBlock = blockStartRow;
        for (int iBlock = 0; iBlock < out.blockRows; iBlock++) {
            int iHeight = out.blockHeight(iBlock);
            int qBlock = blockStartColumn;
            for (int jBlock = 0; jBlock < out.blockColumns; jBlock++) {
                int jWidth = out.blockWidth(jBlock);
                int outIndex = (iBlock * out.blockColumns) + jBlock;
                T[] outBlock = out.blocks[outIndex];
                int index = (pBlock * this.blockColumns) + qBlock;
                int width = blockWidth(qBlock);
                int heightExcess = (iHeight + rowsShift) - 36;
                int widthExcess = (jWidth + columnsShift) - 36;
                if (heightExcess > 0) {
                    if (widthExcess > 0) {
                        int width2 = blockWidth(qBlock + 1);
                        copyBlockPart(this.blocks[index], width, rowsShift, 36, columnsShift, 36, outBlock, jWidth, 0, 0);
                        copyBlockPart(this.blocks[index + 1], width2, rowsShift, 36, 0, widthExcess, outBlock, jWidth, 0, jWidth - widthExcess);
                        copyBlockPart(this.blocks[index + this.blockColumns], width, 0, heightExcess, columnsShift, 36, outBlock, jWidth, iHeight - heightExcess, 0);
                        copyBlockPart(this.blocks[index + this.blockColumns + 1], width2, 0, heightExcess, 0, widthExcess, outBlock, jWidth, iHeight - heightExcess, jWidth - widthExcess);
                    } else {
                        copyBlockPart(this.blocks[index], width, rowsShift, 36, columnsShift, jWidth + columnsShift, outBlock, jWidth, 0, 0);
                        copyBlockPart(this.blocks[index + this.blockColumns], width, 0, heightExcess, columnsShift, jWidth + columnsShift, outBlock, jWidth, iHeight - heightExcess, 0);
                    }
                } else if (widthExcess > 0) {
                    int width22 = blockWidth(qBlock + 1);
                    copyBlockPart(this.blocks[index], width, rowsShift, iHeight + rowsShift, columnsShift, 36, outBlock, jWidth, 0, 0);
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

    private void copyBlockPart(T[] srcBlock, int srcWidth, int srcStartRow, int srcEndRow, int srcStartColumn, int srcEndColumn, T[] dstBlock, int dstWidth, int dstStartRow, int dstStartColumn) {
        int length = srcEndColumn - srcStartColumn;
        int srcPos = (srcStartRow * srcWidth) + srcStartColumn;
        int dstPos = (dstStartRow * dstWidth) + dstStartColumn;
        for (int srcRow = srcStartRow; srcRow < srcEndRow; srcRow++) {
            System.arraycopy(srcBlock, srcPos, dstBlock, dstPos, length);
            srcPos += srcWidth;
            dstPos += dstWidth;
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setSubMatrix(T[][] subMatrix, int row, int column) throws OutOfRangeException, NullArgumentException, NoDataException, DimensionMismatchException {
        MathUtils.checkNotNull(subMatrix);
        int refLength = subMatrix[0].length;
        if (refLength == 0) {
            throw new NoDataException(LocalizedFormats.AT_LEAST_ONE_COLUMN);
        }
        int endRow = (row + subMatrix.length) - 1;
        int endColumn = (column + refLength) - 1;
        checkSubMatrixIndex(row, endRow, column, endColumn);
        for (T[] subRow : subMatrix) {
            if (subRow.length != refLength) {
                throw new DimensionMismatchException(refLength, subRow.length);
            }
        }
        int blockStartRow = row / 36;
        int blockEndRow = (endRow + 36) / 36;
        int blockStartColumn = column / 36;
        int blockEndColumn = (endColumn + 36) / 36;
        for (int iBlock = blockStartRow; iBlock < blockEndRow; iBlock++) {
            int iHeight = blockHeight(iBlock);
            int firstRow = iBlock * 36;
            int iStart = FastMath.max(row, firstRow);
            int iEnd = FastMath.min(endRow + 1, firstRow + iHeight);
            for (int jBlock = blockStartColumn; jBlock < blockEndColumn; jBlock++) {
                int jWidth = blockWidth(jBlock);
                int firstColumn = jBlock * 36;
                int jStart = FastMath.max(column, firstColumn);
                int jEnd = FastMath.min(endColumn + 1, firstColumn + jWidth);
                int jLength = jEnd - jStart;
                T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
                for (int i2 = iStart; i2 < iEnd; i2++) {
                    System.arraycopy(subMatrix[i2 - row], jStart - column, block, ((i2 - firstRow) * jWidth) + (jStart - firstColumn), jLength);
                }
            }
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getRowMatrix(int row) throws OutOfRangeException {
        int i2;
        checkRowIndex(row);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), 1, this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outBlockIndex = 0;
        int outIndex = 0;
        T[] outBlock = out.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
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

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setRowMatrix(int row, FieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setRowMatrix(row, (BlockFieldMatrix) matrix);
        } catch (ClassCastException e2) {
            super.setRowMatrix(row, matrix);
        }
    }

    public void setRowMatrix(int row, BlockFieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        int i2;
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (matrix.getRowDimension() != 1 || matrix.getColumnDimension() != nCols) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), 1, nCols);
        }
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int mBlockIndex = 0;
        int mIndex = 0;
        T[] mBlock = matrix.blocks[0];
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
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

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> getColumnMatrix(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), this.rows, 1);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outBlockIndex = 0;
        int outIndex = 0;
        T[] outBlock = out.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
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

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setColumnMatrix(int column, FieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumnMatrix(column, (BlockFieldMatrix) matrix);
        } catch (ClassCastException e2) {
            super.setColumnMatrix(column, matrix);
        }
    }

    void setColumnMatrix(int column, BlockFieldMatrix<T> matrix) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (matrix.getRowDimension() != nRows || matrix.getColumnDimension() != 1) {
            throw new MatrixDimensionMismatchException(matrix.getRowDimension(), matrix.getColumnDimension(), nRows, 1);
        }
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int mBlockIndex = 0;
        int mIndex = 0;
        T[] mBlock = matrix.blocks[0];
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
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

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldVector<T> getRowVector(int row) throws OutOfRangeException {
        checkRowIndex(row);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(getField(), this.columns);
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            System.arraycopy(block, iRow * jWidth, fieldElementArr, outIndex, jWidth);
            outIndex += jWidth;
        }
        return new ArrayFieldVector((Field) getField(), fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setRowVector(int row, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setRow(row, ((ArrayFieldVector) vector).getDataRef());
        } catch (ClassCastException e2) {
            super.setRowVector(row, vector);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldVector<T> getColumnVector(int column) throws OutOfRangeException {
        checkColumnIndex(column);
        FieldElement[] fieldElementArr = (FieldElement[]) MathArrays.buildArray(getField(), this.rows);
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                int i3 = outIndex;
                outIndex++;
                fieldElementArr[i3] = block[(i2 * jWidth) + jColumn];
            }
        }
        return new ArrayFieldVector((Field) getField(), fieldElementArr, false);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setColumnVector(int column, FieldVector<T> vector) throws OutOfRangeException, MatrixDimensionMismatchException {
        try {
            setColumn(column, ((ArrayFieldVector) vector).getDataRef());
        } catch (ClassCastException e2) {
            super.setColumnVector(column, vector);
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[] getRow(int i2) throws OutOfRangeException {
        checkRowIndex(i2);
        T[] tArr = (T[]) ((FieldElement[]) MathArrays.buildArray(getField(), this.columns));
        int i3 = i2 / 36;
        int i4 = i2 - (i3 * 36);
        int i5 = 0;
        for (int i6 = 0; i6 < this.blockColumns; i6++) {
            int iBlockWidth = blockWidth(i6);
            System.arraycopy(this.blocks[(i3 * this.blockColumns) + i6], i4 * iBlockWidth, tArr, i5, iBlockWidth);
            i5 += iBlockWidth;
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setRow(int row, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkRowIndex(row);
        int nCols = getColumnDimension();
        if (array.length != nCols) {
            throw new MatrixDimensionMismatchException(1, array.length, 1, nCols);
        }
        int iBlock = row / 36;
        int iRow = row - (iBlock * 36);
        int outIndex = 0;
        for (int jBlock = 0; jBlock < this.blockColumns; jBlock++) {
            int jWidth = blockWidth(jBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            System.arraycopy(array, outIndex, block, iRow * jWidth, jWidth);
            outIndex += jWidth;
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[] getColumn(int i2) throws OutOfRangeException {
        checkColumnIndex(i2);
        T[] tArr = (T[]) ((FieldElement[]) MathArrays.buildArray(getField(), this.rows));
        int i3 = i2 / 36;
        int i4 = i2 - (i3 * 36);
        int iBlockWidth = blockWidth(i3);
        int i5 = 0;
        for (int i6 = 0; i6 < this.blockRows; i6++) {
            int iBlockHeight = blockHeight(i6);
            T[] tArr2 = this.blocks[(i6 * this.blockColumns) + i3];
            for (int i7 = 0; i7 < iBlockHeight; i7++) {
                int i8 = i5;
                i5++;
                tArr[i8] = tArr2[(i7 * iBlockWidth) + i4];
            }
        }
        return tArr;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setColumn(int column, T[] array) throws OutOfRangeException, MatrixDimensionMismatchException {
        checkColumnIndex(column);
        int nRows = getRowDimension();
        if (array.length != nRows) {
            throw new MatrixDimensionMismatchException(array.length, 1, nRows, 1);
        }
        int jBlock = column / 36;
        int jColumn = column - (jBlock * 36);
        int jWidth = blockWidth(jBlock);
        int outIndex = 0;
        for (int iBlock = 0; iBlock < this.blockRows; iBlock++) {
            int iHeight = blockHeight(iBlock);
            T[] block = this.blocks[(iBlock * this.blockColumns) + jBlock];
            for (int i2 = 0; i2 < iHeight; i2++) {
                int i3 = outIndex;
                outIndex++;
                block[(i2 * jWidth) + jColumn] = array[i3];
            }
        }
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T getEntry(int row, int column) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        int k2 = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
        return this.blocks[(iBlock * this.blockColumns) + jBlock][k2];
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void setEntry(int row, int column, T value) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        int k2 = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
        this.blocks[(iBlock * this.blockColumns) + jBlock][k2] = value;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void addToEntry(int row, int column, T increment) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        int k2 = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
        FieldElement[] fieldElementArr = this.blocks[(iBlock * this.blockColumns) + jBlock];
        fieldElementArr[k2] = (FieldElement) fieldElementArr[k2].add(increment);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public void multiplyEntry(int row, int column, T factor) throws OutOfRangeException {
        checkRowIndex(row);
        checkColumnIndex(column);
        int iBlock = row / 36;
        int jBlock = column / 36;
        int k2 = ((row - (iBlock * 36)) * blockWidth(jBlock)) + (column - (jBlock * 36));
        FieldElement[] fieldElementArr = this.blocks[(iBlock * this.blockColumns) + jBlock];
        fieldElementArr[k2] = (FieldElement) fieldElementArr[k2].multiply(factor);
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public FieldMatrix<T> transpose() {
        int nRows = getRowDimension();
        int nCols = getColumnDimension();
        BlockFieldMatrix<T> out = new BlockFieldMatrix<>(getField(), nCols, nRows);
        int blockIndex = 0;
        for (int iBlock = 0; iBlock < this.blockColumns; iBlock++) {
            for (int jBlock = 0; jBlock < this.blockRows; jBlock++) {
                T[] outBlock = out.blocks[blockIndex];
                T[] tBlock = this.blocks[(jBlock * this.blockColumns) + iBlock];
                int pStart = iBlock * 36;
                int pEnd = FastMath.min(pStart + 36, this.columns);
                int qStart = jBlock * 36;
                int qEnd = FastMath.min(qStart + 36, this.rows);
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

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.AnyMatrix
    public int getRowDimension() {
        return this.rows;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.AnyMatrix
    public int getColumnDimension() {
        return this.columns;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v38, types: [org.apache.commons.math3.FieldElement] */
    /* JADX WARN: Type inference failed for: r0v47, types: [org.apache.commons.math3.FieldElement] */
    /* JADX WARN: Type inference failed for: r0v5, types: [T extends org.apache.commons.math3.FieldElement<T>[], org.apache.commons.math3.FieldElement[]] */
    /* JADX WARN: Type inference failed for: r2v5, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[] operate(T[] tArr) throws DimensionMismatchException {
        if (tArr.length != this.columns) {
            throw new DimensionMismatchException(tArr.length, this.columns);
        }
        ?? r0 = (T[]) ((FieldElement[]) MathArrays.buildArray(getField(), this.rows));
        T zero = getField().getZero();
        for (int i2 = 0; i2 < this.blockRows; i2++) {
            int i3 = i2 * 36;
            int iMin = FastMath.min(i3 + 36, this.rows);
            for (int i4 = 0; i4 < this.blockColumns; i4++) {
                T[] tArr2 = this.blocks[(i2 * this.blockColumns) + i4];
                int i5 = i4 * 36;
                int iMin2 = FastMath.min(i5 + 36, this.columns);
                int i6 = 0;
                for (int i7 = i3; i7 < iMin; i7++) {
                    T t2 = zero;
                    int i8 = i5;
                    while (i8 < iMin2 - 3) {
                        t2 = (FieldElement) ((FieldElement) ((FieldElement) ((FieldElement) t2.add(tArr2[i6].multiply(tArr[i8]))).add(tArr2[i6 + 1].multiply(tArr[i8 + 1]))).add(tArr2[i6 + 2].multiply(tArr[i8 + 2]))).add(tArr2[i6 + 3].multiply(tArr[i8 + 3]));
                        i6 += 4;
                        i8 += 4;
                    }
                    while (i8 < iMin2) {
                        int i9 = i6;
                        i6++;
                        int i10 = i8;
                        i8++;
                        t2 = (FieldElement) t2.add(tArr2[i9].multiply(tArr[i10]));
                    }
                    r0[i7] = (FieldElement) r0[i7].add(t2);
                }
            }
        }
        return r0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v47, types: [org.apache.commons.math3.FieldElement] */
    /* JADX WARN: Type inference failed for: r0v5, types: [T extends org.apache.commons.math3.FieldElement<T>[], org.apache.commons.math3.FieldElement[]] */
    /* JADX WARN: Type inference failed for: r0v58, types: [org.apache.commons.math3.FieldElement] */
    /* JADX WARN: Type inference failed for: r2v5, types: [org.apache.commons.math3.FieldElement] */
    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T[] preMultiply(T[] tArr) throws DimensionMismatchException {
        if (tArr.length != this.rows) {
            throw new DimensionMismatchException(tArr.length, this.rows);
        }
        ?? r0 = (T[]) ((FieldElement[]) MathArrays.buildArray(getField(), this.columns));
        T zero = getField().getZero();
        for (int i2 = 0; i2 < this.blockColumns; i2++) {
            int iBlockWidth = blockWidth(i2);
            int i3 = iBlockWidth + iBlockWidth;
            int i4 = i3 + iBlockWidth;
            int i5 = i4 + iBlockWidth;
            int i6 = i2 * 36;
            int iMin = FastMath.min(i6 + 36, this.columns);
            for (int i7 = 0; i7 < this.blockRows; i7++) {
                T[] tArr2 = this.blocks[(i7 * this.blockColumns) + i2];
                int i8 = i7 * 36;
                int iMin2 = FastMath.min(i8 + 36, this.rows);
                for (int i9 = i6; i9 < iMin; i9++) {
                    int i10 = i9 - i6;
                    T t2 = zero;
                    int i11 = i8;
                    while (i11 < iMin2 - 3) {
                        t2 = (FieldElement) ((FieldElement) ((FieldElement) ((FieldElement) t2.add(tArr2[i10].multiply(tArr[i11]))).add(tArr2[i10 + iBlockWidth].multiply(tArr[i11 + 1]))).add(tArr2[i10 + i3].multiply(tArr[i11 + 2]))).add(tArr2[i10 + i4].multiply(tArr[i11 + 3]));
                        i10 += i5;
                        i11 += 4;
                    }
                    while (i11 < iMin2) {
                        int i12 = i11;
                        i11++;
                        t2 = (FieldElement) t2.add(tArr2[i10].multiply(tArr[i12]));
                        i10 += iBlockWidth;
                    }
                    r0[i9] = (FieldElement) r0[i9].add(t2);
                }
            }
        }
        return r0;
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) {
        fieldMatrixChangingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int i2 = 0; i2 < this.blockRows; i2++) {
            int i3 = i2 * 36;
            int iMin = FastMath.min(i3 + 36, this.rows);
            for (int i4 = i3; i4 < iMin; i4++) {
                for (int i5 = 0; i5 < this.blockColumns; i5++) {
                    int iBlockWidth = blockWidth(i5);
                    int i6 = i5 * 36;
                    int iMin2 = FastMath.min(i6 + 36, this.columns);
                    Object[] objArr = this.blocks[(i2 * this.blockColumns) + i5];
                    int i7 = (i4 - i3) * iBlockWidth;
                    for (int i8 = i6; i8 < iMin2; i8++) {
                        objArr[i7] = fieldMatrixChangingVisitor.visit(i4, i8, objArr[i7]);
                        i7++;
                    }
                }
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        fieldMatrixPreservingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        for (int i2 = 0; i2 < this.blockRows; i2++) {
            int i3 = i2 * 36;
            int iMin = FastMath.min(i3 + 36, this.rows);
            for (int i4 = i3; i4 < iMin; i4++) {
                for (int i5 = 0; i5 < this.blockColumns; i5++) {
                    int iBlockWidth = blockWidth(i5);
                    int i6 = i5 * 36;
                    int iMin2 = FastMath.min(i6 + 36, this.columns);
                    T[] tArr = this.blocks[(i2 * this.blockColumns) + i5];
                    int i7 = (i4 - i3) * iBlockWidth;
                    for (int i8 = i6; i8 < iMin2; i8++) {
                        fieldMatrixPreservingVisitor.visit(i4, i8, tArr[i7]);
                        i7++;
                    }
                }
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixChangingVisitor.start(this.rows, this.columns, i2, i3, i4, i5);
        for (int i6 = i2 / 36; i6 < 1 + (i3 / 36); i6++) {
            int i7 = i6 * 36;
            int iMax = FastMath.max(i2, i7);
            int iMin = FastMath.min((i6 + 1) * 36, 1 + i3);
            for (int i8 = iMax; i8 < iMin; i8++) {
                for (int i9 = i4 / 36; i9 < 1 + (i5 / 36); i9++) {
                    int iBlockWidth = blockWidth(i9);
                    int i10 = i9 * 36;
                    int iMax2 = FastMath.max(i4, i10);
                    int iMin2 = FastMath.min((i9 + 1) * 36, 1 + i5);
                    Object[] objArr = this.blocks[(i6 * this.blockColumns) + i9];
                    int i11 = (((i8 - i7) * iBlockWidth) + iMax2) - i10;
                    for (int i12 = iMax2; i12 < iMin2; i12++) {
                        objArr[i11] = fieldMatrixChangingVisitor.visit(i8, i12, objArr[i11]);
                        i11++;
                    }
                }
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInRowOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixPreservingVisitor.start(this.rows, this.columns, i2, i3, i4, i5);
        for (int i6 = i2 / 36; i6 < 1 + (i3 / 36); i6++) {
            int i7 = i6 * 36;
            int iMax = FastMath.max(i2, i7);
            int iMin = FastMath.min((i6 + 1) * 36, 1 + i3);
            for (int i8 = iMax; i8 < iMin; i8++) {
                for (int i9 = i4 / 36; i9 < 1 + (i5 / 36); i9++) {
                    int iBlockWidth = blockWidth(i9);
                    int i10 = i9 * 36;
                    int iMax2 = FastMath.max(i4, i10);
                    int iMin2 = FastMath.min((i9 + 1) * 36, 1 + i5);
                    T[] tArr = this.blocks[(i6 * this.blockColumns) + i9];
                    int i11 = (((i8 - i7) * iBlockWidth) + iMax2) - i10;
                    for (int i12 = iMax2; i12 < iMin2; i12++) {
                        fieldMatrixPreservingVisitor.visit(i8, i12, tArr[i11]);
                        i11++;
                    }
                }
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor) {
        fieldMatrixChangingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int i2 = 0;
        for (int i3 = 0; i3 < this.blockRows; i3++) {
            int i4 = i3 * 36;
            int iMin = FastMath.min(i4 + 36, this.rows);
            for (int i5 = 0; i5 < this.blockColumns; i5++) {
                int i6 = i5 * 36;
                int iMin2 = FastMath.min(i6 + 36, this.columns);
                Object[] objArr = this.blocks[i2];
                int i7 = 0;
                for (int i8 = i4; i8 < iMin; i8++) {
                    for (int i9 = i6; i9 < iMin2; i9++) {
                        objArr[i7] = fieldMatrixChangingVisitor.visit(i8, i9, objArr[i7]);
                        i7++;
                    }
                }
                i2++;
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor) {
        fieldMatrixPreservingVisitor.start(this.rows, this.columns, 0, this.rows - 1, 0, this.columns - 1);
        int i2 = 0;
        for (int i3 = 0; i3 < this.blockRows; i3++) {
            int i4 = i3 * 36;
            int iMin = FastMath.min(i4 + 36, this.rows);
            for (int i5 = 0; i5 < this.blockColumns; i5++) {
                int i6 = i5 * 36;
                int iMin2 = FastMath.min(i6 + 36, this.columns);
                T[] tArr = this.blocks[i2];
                int i7 = 0;
                for (int i8 = i4; i8 < iMin; i8++) {
                    for (int i9 = i6; i9 < iMin2; i9++) {
                        fieldMatrixPreservingVisitor.visit(i8, i9, tArr[i7]);
                        i7++;
                    }
                }
                i2++;
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixChangingVisitor<T> fieldMatrixChangingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixChangingVisitor.start(this.rows, this.columns, i2, i3, i4, i5);
        for (int i6 = i2 / 36; i6 < 1 + (i3 / 36); i6++) {
            int i7 = i6 * 36;
            int iMax = FastMath.max(i2, i7);
            int iMin = FastMath.min((i6 + 1) * 36, 1 + i3);
            for (int i8 = i4 / 36; i8 < 1 + (i5 / 36); i8++) {
                int iBlockWidth = blockWidth(i8);
                int i9 = i8 * 36;
                int iMax2 = FastMath.max(i4, i9);
                int iMin2 = FastMath.min((i8 + 1) * 36, 1 + i5);
                Object[] objArr = this.blocks[(i6 * this.blockColumns) + i8];
                for (int i10 = iMax; i10 < iMin; i10++) {
                    int i11 = (((i10 - i7) * iBlockWidth) + iMax2) - i9;
                    for (int i12 = iMax2; i12 < iMin2; i12++) {
                        objArr[i11] = fieldMatrixChangingVisitor.visit(i10, i12, objArr[i11]);
                        i11++;
                    }
                }
            }
        }
        return (T) fieldMatrixChangingVisitor.end();
    }

    @Override // org.apache.commons.math3.linear.AbstractFieldMatrix, org.apache.commons.math3.linear.FieldMatrix
    public T walkInOptimizedOrder(FieldMatrixPreservingVisitor<T> fieldMatrixPreservingVisitor, int i2, int i3, int i4, int i5) throws NumberIsTooSmallException, OutOfRangeException {
        checkSubMatrixIndex(i2, i3, i4, i5);
        fieldMatrixPreservingVisitor.start(this.rows, this.columns, i2, i3, i4, i5);
        for (int i6 = i2 / 36; i6 < 1 + (i3 / 36); i6++) {
            int i7 = i6 * 36;
            int iMax = FastMath.max(i2, i7);
            int iMin = FastMath.min((i6 + 1) * 36, 1 + i3);
            for (int i8 = i4 / 36; i8 < 1 + (i5 / 36); i8++) {
                int iBlockWidth = blockWidth(i8);
                int i9 = i8 * 36;
                int iMax2 = FastMath.max(i4, i9);
                int iMin2 = FastMath.min((i8 + 1) * 36, 1 + i5);
                T[] tArr = this.blocks[(i6 * this.blockColumns) + i8];
                for (int i10 = iMax; i10 < iMin; i10++) {
                    int i11 = (((i10 - i7) * iBlockWidth) + iMax2) - i9;
                    for (int i12 = iMax2; i12 < iMin2; i12++) {
                        fieldMatrixPreservingVisitor.visit(i10, i12, tArr[i11]);
                        i11++;
                    }
                }
            }
        }
        return (T) fieldMatrixPreservingVisitor.end();
    }

    private int blockHeight(int blockRow) {
        if (blockRow == this.blockRows - 1) {
            return this.rows - (blockRow * 36);
        }
        return 36;
    }

    private int blockWidth(int blockColumn) {
        if (blockColumn == this.blockColumns - 1) {
            return this.columns - (blockColumn * 36);
        }
        return 36;
    }
}
