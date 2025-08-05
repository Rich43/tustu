package sun.java2d.loops;

/* loaded from: rt.jar:sun/java2d/loops/RenderCache.class */
public final class RenderCache {
    private Entry[] entries;

    /* loaded from: rt.jar:sun/java2d/loops/RenderCache$Entry.class */
    final class Entry {
        private SurfaceType src;
        private CompositeType comp;
        private SurfaceType dst;
        private Object value;

        public Entry(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2, Object obj) {
            this.src = surfaceType;
            this.comp = compositeType;
            this.dst = surfaceType2;
            this.value = obj;
        }

        public boolean matches(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
            return this.src == surfaceType && this.comp == compositeType && this.dst == surfaceType2;
        }

        public Object getValue() {
            return this.value;
        }
    }

    public RenderCache(int i2) {
        this.entries = new Entry[i2];
    }

    public synchronized Object get(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2) {
        Entry entry;
        int length = this.entries.length - 1;
        for (int i2 = length; i2 >= 0 && (entry = this.entries[i2]) != null; i2--) {
            if (entry.matches(surfaceType, compositeType, surfaceType2)) {
                if (i2 < length - 4) {
                    System.arraycopy(this.entries, i2 + 1, this.entries, i2, length - i2);
                    this.entries[length] = entry;
                }
                return entry.getValue();
            }
        }
        return null;
    }

    public synchronized void put(SurfaceType surfaceType, CompositeType compositeType, SurfaceType surfaceType2, Object obj) {
        Entry entry = new Entry(surfaceType, compositeType, surfaceType2, obj);
        int length = this.entries.length;
        System.arraycopy(this.entries, 1, this.entries, 0, length - 1);
        this.entries[length - 1] = entry;
    }
}
