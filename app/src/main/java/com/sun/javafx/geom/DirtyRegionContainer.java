package com.sun.javafx.geom;

import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/DirtyRegionContainer.class */
public final class DirtyRegionContainer {
    public static final int DTR_OK = 1;
    public static final int DTR_CONTAINS_CLIP = 0;
    private RectBounds[] dirtyRegions;
    private int emptyIndex;
    private int[][] heap;
    private int heapSize;
    private long invalidMask;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !DirtyRegionContainer.class.desiredAssertionStatus();
    }

    public DirtyRegionContainer(int count) {
        initDirtyRegions(count);
    }

    public boolean equals(Object obj) {
        if (obj instanceof DirtyRegionContainer) {
            DirtyRegionContainer drc = (DirtyRegionContainer) obj;
            if (size() != drc.size()) {
                return false;
            }
            for (int i2 = 0; i2 < this.emptyIndex; i2++) {
                if (!getDirtyRegion(i2).equals(drc.getDirtyRegion(i2))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public int hashCode() {
        int hash = (97 * 5) + Arrays.deepHashCode(this.dirtyRegions);
        return (97 * hash) + this.emptyIndex;
    }

    public DirtyRegionContainer deriveWithNewRegion(RectBounds region) {
        if (region == null) {
            return this;
        }
        this.dirtyRegions[0].deriveWithNewBounds(region);
        this.emptyIndex = 1;
        return this;
    }

    public DirtyRegionContainer deriveWithNewRegions(RectBounds[] regions) {
        if (regions == null || regions.length == 0) {
            return this;
        }
        if (regions.length > maxSpace()) {
            initDirtyRegions(regions.length);
        }
        regioncopy(regions, 0, this.dirtyRegions, 0, regions.length);
        this.emptyIndex = regions.length;
        return this;
    }

    public DirtyRegionContainer deriveWithNewContainer(DirtyRegionContainer other) {
        if (other == null || other.maxSpace() == 0) {
            return this;
        }
        if (other.maxSpace() > maxSpace()) {
            initDirtyRegions(other.maxSpace());
        }
        regioncopy(other.dirtyRegions, 0, this.dirtyRegions, 0, other.emptyIndex);
        this.emptyIndex = other.emptyIndex;
        return this;
    }

    private void initDirtyRegions(int count) {
        this.dirtyRegions = new RectBounds[count];
        for (int i2 = 0; i2 < count; i2++) {
            this.dirtyRegions[i2] = new RectBounds();
        }
        this.emptyIndex = 0;
    }

    public DirtyRegionContainer copy() {
        DirtyRegionContainer drc = new DirtyRegionContainer(maxSpace());
        regioncopy(this.dirtyRegions, 0, drc.dirtyRegions, 0, this.emptyIndex);
        drc.emptyIndex = this.emptyIndex;
        return drc;
    }

    public int maxSpace() {
        return this.dirtyRegions.length;
    }

    public RectBounds getDirtyRegion(int index) {
        return this.dirtyRegions[index];
    }

    public void setDirtyRegion(int index, RectBounds region) {
        this.dirtyRegions[index] = region;
    }

    public void addDirtyRegion(RectBounds region) {
        if (region.isEmpty()) {
            return;
        }
        int tempIndex = 0;
        int regionCount = this.emptyIndex;
        for (int i2 = 0; i2 < regionCount; i2++) {
            RectBounds dr = this.dirtyRegions[tempIndex];
            if (region.intersects(dr)) {
                region.unionWith(dr);
                RectBounds tmp = this.dirtyRegions[tempIndex];
                this.dirtyRegions[tempIndex] = this.dirtyRegions[this.emptyIndex - 1];
                this.dirtyRegions[this.emptyIndex - 1] = tmp;
                this.emptyIndex--;
            } else {
                tempIndex++;
            }
        }
        if (hasSpace()) {
            this.dirtyRegions[this.emptyIndex].deriveWithNewBounds(region);
            this.emptyIndex++;
        } else if (this.dirtyRegions.length == 1) {
            this.dirtyRegions[0].deriveWithUnion(region);
        } else {
            compress(region);
        }
    }

    public void merge(DirtyRegionContainer other) {
        int otherSize = other.size();
        for (int i2 = 0; i2 < otherSize; i2++) {
            addDirtyRegion(other.getDirtyRegion(i2));
        }
    }

    public int size() {
        return this.emptyIndex;
    }

    public void reset() {
        this.emptyIndex = 0;
    }

    private RectBounds compress(RectBounds region) {
        compress_heap();
        addDirtyRegion(region);
        return region;
    }

    private boolean hasSpace() {
        return this.emptyIndex < this.dirtyRegions.length;
    }

    private void regioncopy(RectBounds[] src, int from, RectBounds[] dest, int to, int length) {
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = from;
            from++;
            RectBounds rb = src[i3];
            if (rb == null) {
                int i4 = to;
                to++;
                dest[i4].makeEmpty();
            } else {
                int i5 = to;
                to++;
                dest[i5].deriveWithNewBounds(rb);
            }
        }
    }

    public boolean checkAndClearRegion(int index) {
        boolean removed = false;
        if (this.dirtyRegions[index].isEmpty()) {
            System.arraycopy(this.dirtyRegions, index + 1, this.dirtyRegions, index, (this.emptyIndex - index) - 1);
            this.emptyIndex--;
            removed = true;
        }
        return removed;
    }

    public void grow(int horizontal, int vertical) {
        if (horizontal != 0 || vertical != 0) {
            for (int i2 = 0; i2 < this.emptyIndex; i2++) {
                getDirtyRegion(i2).grow(horizontal, vertical);
            }
        }
    }

    public void roundOut() {
        for (int i2 = 0; i2 < this.emptyIndex; i2++) {
            this.dirtyRegions[i2].roundOut();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i2 = 0; i2 < this.emptyIndex; i2++) {
            sb.append((Object) this.dirtyRegions[i2]);
            sb.append('\n');
        }
        return sb.toString();
    }

    private void heapCompress() {
        this.invalidMask = 0L;
        int[] map = new int[this.dirtyRegions.length];
        for (int i2 = 0; i2 < map.length; i2++) {
            map[i2] = i2;
        }
        for (int i3 = 0; i3 < this.dirtyRegions.length / 2; i3++) {
            int[] min = takeMinWithMap(map);
            int idx0 = resolveMap(map, min[1]);
            int idx1 = resolveMap(map, min[2]);
            if (idx0 != idx1) {
                this.dirtyRegions[idx0].deriveWithUnion(this.dirtyRegions[idx1]);
                map[idx1] = idx0;
                this.invalidMask |= 1 << idx0;
                this.invalidMask |= 1 << idx1;
            }
        }
        for (int i4 = 0; i4 < this.emptyIndex; i4++) {
            if (map[i4] != i4) {
                while (map[this.emptyIndex - 1] != this.emptyIndex - 1) {
                    this.emptyIndex--;
                }
                if (i4 < this.emptyIndex - 1) {
                    RectBounds tmp = this.dirtyRegions[this.emptyIndex - 1];
                    this.dirtyRegions[this.emptyIndex - 1] = this.dirtyRegions[i4];
                    this.dirtyRegions[i4] = tmp;
                    map[i4] = i4;
                    this.emptyIndex--;
                }
            }
        }
    }

    private void heapify() {
        for (int i2 = this.heapSize / 2; i2 >= 0; i2--) {
            siftDown(i2);
        }
    }

    private void siftDown(int i2) {
        int end = this.heapSize >> 1;
        while (i2 < end) {
            int child = (i2 << 1) + 1;
            int[] left = this.heap[child];
            if (child + 1 < this.heapSize && this.heap[child + 1][0] < left[0]) {
                child++;
            }
            if (this.heap[child][0] < this.heap[i2][0]) {
                int[] temp = this.heap[child];
                this.heap[child] = this.heap[i2];
                this.heap[i2] = temp;
                i2 = child;
            } else {
                return;
            }
        }
    }

    private int[] takeMinWithMap(int[] map) {
        int[] temp;
        int[] iArr = this.heap[0];
        while (true) {
            temp = iArr;
            if ((((1 << temp[1]) | (1 << temp[2])) & this.invalidMask) <= 0) {
                break;
            }
            temp[0] = unifiedRegionArea(resolveMap(map, temp[1]), resolveMap(map, temp[2]));
            siftDown(0);
            if (this.heap[0] == temp) {
                break;
            }
            iArr = this.heap[0];
        }
        this.heap[this.heapSize - 1] = temp;
        siftDown(0);
        this.heapSize--;
        return temp;
    }

    private int[] takeMin() {
        int[] temp = this.heap[0];
        this.heap[0] = this.heap[this.heapSize - 1];
        this.heap[this.heapSize - 1] = temp;
        siftDown(0);
        this.heapSize--;
        return temp;
    }

    private int resolveMap(int[] map, int idx) {
        while (map[idx] != idx) {
            idx = map[idx];
        }
        return idx;
    }

    private int unifiedRegionArea(int i0, int i1) {
        RectBounds r0 = this.dirtyRegions[i0];
        RectBounds r1 = this.dirtyRegions[i1];
        float minX = r0.getMinX() < r1.getMinX() ? r0.getMinX() : r1.getMinX();
        float minY = r0.getMinY() < r1.getMinY() ? r0.getMinY() : r1.getMinY();
        float maxX = r0.getMaxX() > r1.getMaxX() ? r0.getMaxX() : r1.getMaxX();
        float maxY = r0.getMaxY() > r1.getMaxY() ? r0.getMaxY() : r1.getMaxY();
        return (int) ((maxX - minX) * (maxY - minY));
    }

    private void compress_heap() {
        if (!$assertionsDisabled && this.dirtyRegions.length != this.emptyIndex) {
            throw new AssertionError();
        }
        if (this.heap == null) {
            int n2 = this.dirtyRegions.length;
            this.heap = new int[(n2 * (n2 - 1)) / 2][3];
        }
        this.heapSize = this.heap.length;
        int k2 = 0;
        for (int i2 = 0; i2 < this.dirtyRegions.length - 1; i2++) {
            for (int j2 = i2 + 1; j2 < this.dirtyRegions.length; j2++) {
                this.heap[k2][0] = unifiedRegionArea(i2, j2);
                this.heap[k2][1] = i2;
                int i3 = k2;
                k2++;
                this.heap[i3][2] = j2;
            }
        }
        heapify();
        heapCompress();
    }
}
