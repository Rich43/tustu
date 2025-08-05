package com.sun.javafx.css;

import com.sun.javafx.css.StyleCacheEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/* loaded from: jfxrt.jar:com/sun/javafx/css/StyleCache.class */
public final class StyleCache {
    private Map<StyleCacheEntry.Key, StyleCacheEntry> entries;

    public void clear() {
        if (this.entries == null) {
            return;
        }
        Thread.dumpStack();
        this.entries.clear();
    }

    public StyleCacheEntry getStyleCacheEntry(StyleCacheEntry.Key key) {
        StyleCacheEntry entry = null;
        if (this.entries != null) {
            entry = this.entries.get(key);
        }
        return entry;
    }

    public void addStyleCacheEntry(StyleCacheEntry.Key key, StyleCacheEntry entry) {
        if (this.entries == null) {
            this.entries = new HashMap(5);
        }
        this.entries.put(key, entry);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/StyleCache$Key.class */
    public static final class Key {
        final int[] styleMapIds;
        private int hash;

        public Key(int[] styleMapIds, int count) {
            this.hash = Integer.MIN_VALUE;
            this.styleMapIds = new int[count];
            System.arraycopy(styleMapIds, 0, this.styleMapIds, 0, count);
        }

        public Key(Key other) {
            this(other.styleMapIds, other.styleMapIds.length);
        }

        public int[] getStyleMapIds() {
            return this.styleMapIds;
        }

        public String toString() {
            return Arrays.toString(this.styleMapIds);
        }

        public int hashCode() {
            if (this.hash == Integer.MIN_VALUE) {
                this.hash = 3;
                if (this.styleMapIds != null) {
                    for (int i2 = 0; i2 < this.styleMapIds.length; i2++) {
                        int id = this.styleMapIds[i2];
                        this.hash = 17 * (this.hash + id);
                    }
                }
            }
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj == null || obj.getClass() != getClass()) {
                return false;
            }
            Key other = (Key) obj;
            if (this.hash != other.hash) {
                return false;
            }
            if ((this.styleMapIds == null) ^ (other.styleMapIds == null)) {
                return false;
            }
            if (this.styleMapIds == null) {
                return true;
            }
            if (this.styleMapIds.length != other.styleMapIds.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.styleMapIds.length; i2++) {
                if (this.styleMapIds[i2] != other.styleMapIds[i2]) {
                    return false;
                }
            }
            return true;
        }
    }
}
