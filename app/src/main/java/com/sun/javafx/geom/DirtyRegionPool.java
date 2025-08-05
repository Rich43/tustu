package com.sun.javafx.geom;

import java.util.Deque;
import java.util.LinkedList;

/* loaded from: jfxrt.jar:com/sun/javafx/geom/DirtyRegionPool.class */
public final class DirtyRegionPool {
    private static final int POOL_SIZE_MIN = 4;
    private static final int EXPIRATION_TIME = 3000;
    private static final int COUNT_BETWEEN_EXPIRATION_CHECK = 90;
    private final int containerSize;
    private int clearCounter = 90;
    private final Deque<DirtyRegionContainer> fixed = new LinkedList();
    private final Deque<PoolItem> unlocked = new LinkedList();
    private final Deque<PoolItem> locked = new LinkedList();

    /* loaded from: jfxrt.jar:com/sun/javafx/geom/DirtyRegionPool$PoolItem.class */
    private class PoolItem {
        DirtyRegionContainer container;
        long timeStamp;

        public PoolItem(DirtyRegionContainer container, long timeStamp) {
            this.container = container;
            this.timeStamp = timeStamp;
        }
    }

    public DirtyRegionPool(int containerSize) {
        this.containerSize = containerSize;
        for (int i2 = 0; i2 < 4; i2++) {
            this.fixed.add(new DirtyRegionContainer(containerSize));
        }
    }

    public DirtyRegionContainer checkOut() {
        clearExpired();
        if (!this.fixed.isEmpty()) {
            return this.fixed.pop();
        }
        if (!this.unlocked.isEmpty()) {
            PoolItem item = this.unlocked.pop();
            this.locked.push(item);
            return item.container;
        }
        DirtyRegionContainer c2 = new DirtyRegionContainer(this.containerSize);
        this.locked.push(new PoolItem(null, -1L));
        return c2;
    }

    public void checkIn(DirtyRegionContainer drc) {
        drc.reset();
        if (this.locked.isEmpty()) {
            this.fixed.push(drc);
            return;
        }
        PoolItem item = this.locked.pop();
        item.container = drc;
        item.timeStamp = System.currentTimeMillis();
        this.unlocked.push(item);
    }

    private void clearExpired() {
        if (this.unlocked.isEmpty()) {
            return;
        }
        int i2 = this.clearCounter;
        this.clearCounter = i2 - 1;
        if (i2 == 0) {
            this.clearCounter = 90;
            PoolItem i3 = this.unlocked.peekLast();
            long now = System.currentTimeMillis();
            while (i3 != null && i3.timeStamp + 3000 < now) {
                this.unlocked.removeLast();
                i3 = this.unlocked.peekLast();
            }
        }
    }
}
