package sun.misc;

/* loaded from: rt.jar:sun/misc/LRUCache.class */
public abstract class LRUCache<N, V> {
    private V[] oa = null;
    private final int size;

    protected abstract V create(N n2);

    protected abstract boolean hasName(V v2, N n2);

    public LRUCache(int i2) {
        this.size = i2;
    }

    public static void moveToFront(Object[] objArr, int i2) {
        Object obj = objArr[i2];
        for (int i3 = i2; i3 > 0; i3--) {
            objArr[i3] = objArr[i3 - 1];
        }
        objArr[0] = obj;
    }

    public V forName(N n2) {
        if (this.oa == null) {
            this.oa = (V[]) new Object[this.size];
        } else {
            for (int i2 = 0; i2 < this.oa.length; i2++) {
                V v2 = this.oa[i2];
                if (v2 != null && hasName(v2, n2)) {
                    if (i2 > 0) {
                        moveToFront(this.oa, i2);
                    }
                    return v2;
                }
            }
        }
        V vCreate = create(n2);
        this.oa[this.oa.length - 1] = vCreate;
        moveToFront(this.oa, this.oa.length - 1);
        return vCreate;
    }
}
