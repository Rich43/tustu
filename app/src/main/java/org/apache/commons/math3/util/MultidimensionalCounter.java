package org.apache.commons.math3.util;

import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MultidimensionalCounter.class */
public class MultidimensionalCounter implements Iterable<Integer> {
    private final int dimension;
    private final int[] uniCounterOffset;
    private final int[] size;
    private final int totalSize;
    private final int last;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MultidimensionalCounter$Iterator.class */
    public class Iterator implements java.util.Iterator<Integer> {
        private final int[] counter;
        private int count = -1;
        private final int maxCount;

        Iterator() {
            this.counter = new int[MultidimensionalCounter.this.dimension];
            this.maxCount = MultidimensionalCounter.this.totalSize - 1;
            this.counter[MultidimensionalCounter.this.last] = -1;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.count < this.maxCount;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Integer next() {
            if (hasNext()) {
                int i2 = MultidimensionalCounter.this.last;
                while (true) {
                    if (i2 >= 0) {
                        if (this.counter[i2] == MultidimensionalCounter.this.size[i2] - 1) {
                            this.counter[i2] = 0;
                            i2--;
                        } else {
                            int[] iArr = this.counter;
                            int i3 = i2;
                            iArr[i3] = iArr[i3] + 1;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                int i4 = this.count + 1;
                this.count = i4;
                return Integer.valueOf(i4);
            }
            throw new NoSuchElementException();
        }

        public int getCount() {
            return this.count;
        }

        public int[] getCounts() {
            return MathArrays.copyOf(this.counter);
        }

        public int getCount(int dim) {
            return this.counter[dim];
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public MultidimensionalCounter(int... size) throws NotStrictlyPositiveException {
        this.dimension = size.length;
        this.size = MathArrays.copyOf(size);
        this.uniCounterOffset = new int[this.dimension];
        this.last = this.dimension - 1;
        int tS = size[this.last];
        for (int i2 = 0; i2 < this.last; i2++) {
            int count = 1;
            for (int j2 = i2 + 1; j2 < this.dimension; j2++) {
                count *= size[j2];
            }
            this.uniCounterOffset[i2] = count;
            tS *= size[i2];
        }
        this.uniCounterOffset[this.last] = 0;
        if (tS <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(tS));
        }
        this.totalSize = tS;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator iterator() {
        return new Iterator();
    }

    public int getDimension() {
        return this.dimension;
    }

    public int[] getCounts(int index) throws OutOfRangeException {
        if (index < 0 || index >= this.totalSize) {
            throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(this.totalSize));
        }
        int[] indices = new int[this.dimension];
        int count = 0;
        for (int i2 = 0; i2 < this.last; i2++) {
            int idx = 0;
            int offset = this.uniCounterOffset[i2];
            while (count <= index) {
                count += offset;
                idx++;
            }
            count -= offset;
            indices[i2] = idx - 1;
        }
        indices[this.last] = index - count;
        return indices;
    }

    public int getCount(int... c2) throws OutOfRangeException, DimensionMismatchException {
        if (c2.length != this.dimension) {
            throw new DimensionMismatchException(c2.length, this.dimension);
        }
        int count = 0;
        for (int i2 = 0; i2 < this.dimension; i2++) {
            int index = c2[i2];
            if (index < 0 || index >= this.size[i2]) {
                throw new OutOfRangeException(Integer.valueOf(index), 0, Integer.valueOf(this.size[i2] - 1));
            }
            count += this.uniCounterOffset[i2] * c2[i2];
        }
        return count + c2[this.last];
    }

    public int getSize() {
        return this.totalSize;
    }

    public int[] getSizes() {
        return MathArrays.copyOf(this.size);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < this.dimension; i2++) {
            sb.append("[").append(getCount(i2)).append("]");
        }
        return sb.toString();
    }
}
