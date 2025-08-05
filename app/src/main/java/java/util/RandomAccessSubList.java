package java.util;

/* compiled from: AbstractList.java */
/* loaded from: rt.jar:java/util/RandomAccessSubList.class */
class RandomAccessSubList<E> extends SubList<E> implements RandomAccess {
    RandomAccessSubList(AbstractList<E> abstractList, int i2, int i3) {
        super(abstractList, i2, i3);
    }

    @Override // java.util.SubList, java.util.AbstractList, java.util.List
    public List<E> subList(int i2, int i3) {
        return new RandomAccessSubList(this, i2, i3);
    }
}
