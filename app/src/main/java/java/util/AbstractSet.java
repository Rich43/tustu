package java.util;

/* loaded from: rt.jar:java/util/AbstractSet.class */
public abstract class AbstractSet<E> extends AbstractCollection<E> implements Set<E> {
    protected AbstractSet() {
    }

    @Override // java.util.Collection, java.util.List
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Set)) {
            return false;
        }
        Collection<?> collection = (Collection) obj;
        if (collection.size() != size()) {
            return false;
        }
        try {
            return containsAll(collection);
        } catch (ClassCastException e2) {
            return false;
        } catch (NullPointerException e3) {
            return false;
        }
    }

    @Override // java.util.Collection, java.util.List
    public int hashCode() {
        int iHashCode = 0;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            E next = it.next();
            if (next != null) {
                iHashCode += next.hashCode();
            }
        }
        return iHashCode;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean removeAll(Collection<?> collection) {
        Objects.requireNonNull(collection);
        boolean zRemove = false;
        if (size() > collection.size()) {
            Iterator<?> it = collection.iterator();
            while (it.hasNext()) {
                zRemove |= remove(it.next());
            }
        } else {
            Iterator<E> it2 = iterator();
            while (it2.hasNext()) {
                if (collection.contains(it2.next())) {
                    it2.remove();
                    zRemove = true;
                }
            }
        }
        return zRemove;
    }
}
