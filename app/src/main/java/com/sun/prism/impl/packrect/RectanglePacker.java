package com.sun.prism.impl.packrect;

import com.sun.javafx.geom.Rectangle;
import com.sun.prism.Texture;
import java.util.ArrayList;
import java.util.List;

/* loaded from: jfxrt.jar:com/sun/prism/impl/packrect/RectanglePacker.class */
public class RectanglePacker {
    private Texture backingStore;
    private List<Level> levels;
    private static final int MIN_SIZE = 8;
    private static final int ROUND_UP = 4;
    private int recentUsedLevelIndex;
    private int length;
    private int size;
    private int sizeOffset;

    /* renamed from: x, reason: collision with root package name */
    private int f12020x;

    /* renamed from: y, reason: collision with root package name */
    private int f12021y;
    private boolean vertical;

    public RectanglePacker(Texture backingStore, int x2, int y2, int width, int height, boolean vertical) {
        this.levels = new ArrayList(150);
        this.recentUsedLevelIndex = 0;
        this.backingStore = backingStore;
        if (vertical) {
            this.length = height;
            this.size = width;
        } else {
            this.length = width;
            this.size = height;
        }
        this.f12020x = x2;
        this.f12021y = y2;
        this.vertical = vertical;
    }

    public RectanglePacker(Texture backingStore, int width, int height) {
        this(backingStore, 0, 0, width, height, false);
    }

    public final Texture getBackingStore() {
        return this.backingStore;
    }

    public final boolean add(Rectangle rect) {
        int newIndex;
        int requestedLength = this.vertical ? rect.height : rect.width;
        int requestedSize = this.vertical ? rect.width : rect.height;
        if (requestedLength > this.length || requestedSize > this.size) {
            return false;
        }
        int newSize = 8 > requestedSize ? 8 : requestedSize;
        int newSize2 = ((newSize + 4) - 1) - ((newSize - 1) % 4);
        if (this.recentUsedLevelIndex < this.levels.size() && this.levels.get(this.recentUsedLevelIndex).size != newSize2) {
            newIndex = binarySearch(this.levels, newSize2);
        } else {
            newIndex = this.recentUsedLevelIndex;
        }
        boolean newLevelFlag = this.sizeOffset + newSize2 <= this.size;
        int max = this.levels.size();
        for (int i2 = newIndex; i2 < max; i2++) {
            Level level = this.levels.get(i2);
            if (level.size > newSize2 + 8 && newLevelFlag) {
                break;
            }
            if (level.add(rect, this.f12020x, this.f12021y, requestedLength, requestedSize, this.vertical)) {
                this.recentUsedLevelIndex = i2;
                return true;
            }
        }
        if (!newLevelFlag) {
            return false;
        }
        Level newLevel = new Level(this.length, newSize2, this.sizeOffset);
        this.sizeOffset += newSize2;
        if (newIndex < this.levels.size() && this.levels.get(newIndex).size <= newSize2) {
            this.levels.add(newIndex + 1, newLevel);
            this.recentUsedLevelIndex = newIndex + 1;
        } else {
            this.levels.add(newIndex, newLevel);
            this.recentUsedLevelIndex = newIndex;
        }
        return newLevel.add(rect, this.f12020x, this.f12021y, requestedLength, requestedSize, this.vertical);
    }

    public void clear() {
        this.levels.clear();
        this.sizeOffset = 0;
        this.recentUsedLevelIndex = 0;
    }

    public void dispose() {
        if (this.backingStore != null) {
            this.backingStore.dispose();
        }
        this.backingStore = null;
        this.levels = null;
    }

    private static int binarySearch(List<Level> levels, int k2) {
        int key = k2 + 1;
        int from = 0;
        int to = levels.size() - 1;
        int mid = 0;
        int midSize = 0;
        if (to < 0) {
            return 0;
        }
        while (from <= to) {
            mid = (from + to) / 2;
            midSize = levels.get(mid).size;
            if (key < midSize) {
                to = mid - 1;
            } else {
                from = mid + 1;
            }
        }
        if (midSize < k2) {
            return mid + 1;
        }
        if (midSize > k2) {
            if (mid > 0) {
                return mid - 1;
            }
            return 0;
        }
        return mid;
    }
}
