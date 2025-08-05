package sun.rmi.server;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.WeakHashMap;

/* loaded from: rt.jar:sun/rmi/server/WeakClassHashMap.class */
public abstract class WeakClassHashMap<V> {
    private Map<Class<?>, ValueCell<V>> internalMap = new WeakHashMap();

    protected abstract V computeValue(Class<?> cls);

    protected WeakClassHashMap() {
    }

    public V get(Class<?> cls) {
        ValueCell<V> valueCell;
        V v2;
        synchronized (this.internalMap) {
            valueCell = this.internalMap.get(cls);
            if (valueCell == null) {
                valueCell = new ValueCell<>();
                this.internalMap.put(cls, valueCell);
            }
        }
        synchronized (valueCell) {
            V vComputeValue = null;
            if (valueCell.ref != null) {
                vComputeValue = valueCell.ref.get();
            }
            if (vComputeValue == null) {
                vComputeValue = computeValue(cls);
                valueCell.ref = new SoftReference(vComputeValue);
            }
            v2 = vComputeValue;
        }
        return v2;
    }

    /* loaded from: rt.jar:sun/rmi/server/WeakClassHashMap$ValueCell.class */
    private static class ValueCell<T> {
        Reference<T> ref = null;

        ValueCell() {
        }
    }
}
