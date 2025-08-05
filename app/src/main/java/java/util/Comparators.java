package java.util;

import java.io.Serializable;

/* loaded from: rt.jar:java/util/Comparators.class */
class Comparators {
    private Comparators() {
        throw new AssertionError((Object) "no instances");
    }

    /* loaded from: rt.jar:java/util/Comparators$NaturalOrderComparator.class */
    enum NaturalOrderComparator implements Comparator<Comparable<Object>> {
        INSTANCE;

        @Override // java.util.Comparator
        public int compare(Comparable<Object> comparable, Comparable<Object> comparable2) {
            return comparable.compareTo(comparable2);
        }

        @Override // java.util.Comparator
        public Comparator<Comparable<Object>> reversed() {
            return Comparator.reverseOrder();
        }
    }

    /* loaded from: rt.jar:java/util/Comparators$NullComparator.class */
    static final class NullComparator<T> implements Comparator<T>, Serializable {
        private static final long serialVersionUID = -7569533591570686392L;
        private final boolean nullFirst;
        private final Comparator<T> real;

        /* JADX WARN: Multi-variable type inference failed */
        NullComparator(boolean z2, Comparator<? super T> comparator) {
            this.nullFirst = z2;
            this.real = comparator;
        }

        @Override // java.util.Comparator
        public int compare(T t2, T t3) {
            if (t2 == null) {
                if (t3 == null) {
                    return 0;
                }
                return this.nullFirst ? -1 : 1;
            }
            if (t3 == null) {
                return this.nullFirst ? 1 : -1;
            }
            if (this.real == null) {
                return 0;
            }
            return this.real.compare(t2, t3);
        }

        @Override // java.util.Comparator
        public Comparator<T> thenComparing(Comparator<? super T> comparator) {
            Objects.requireNonNull(comparator);
            return new NullComparator(this.nullFirst, this.real == null ? comparator : this.real.thenComparing(comparator));
        }

        @Override // java.util.Comparator
        public Comparator<T> reversed() {
            return new NullComparator(!this.nullFirst, this.real == null ? null : this.real.reversed());
        }
    }
}
