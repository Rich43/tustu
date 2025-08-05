package sun.util;

import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/* loaded from: rt.jar:sun/util/PreHashedMap.class */
public abstract class PreHashedMap<V> extends AbstractMap<String, V> {
    private final int rows;
    private final int size;
    private final int shift;
    private final int mask;
    private final Object[] ht;

    protected abstract void init(Object[] objArr);

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.AbstractMap, java.util.Map
    public /* bridge */ /* synthetic */ Object put(Object obj, Object obj2) {
        return put((String) obj, (String) obj2);
    }

    protected PreHashedMap(int i2, int i3, int i4, int i5) {
        this.rows = i2;
        this.size = i3;
        this.shift = i4;
        this.mask = i5;
        this.ht = new Object[i2];
        init(this.ht);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private V toV(Object obj) {
        return obj;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public V get(Object obj) {
        Object[] objArr = (Object[]) this.ht[(obj.hashCode() >> this.shift) & this.mask];
        if (objArr == null) {
            return null;
        }
        while (!objArr[0].equals(obj)) {
            if (objArr.length < 3) {
                return null;
            }
            objArr = objArr[2];
        }
        return toV(objArr[1]);
    }

    public V put(String str, V v2) {
        Object[] objArr = (Object[]) this.ht[(str.hashCode() >> this.shift) & this.mask];
        if (objArr == null) {
            throw new UnsupportedOperationException(str);
        }
        while (!objArr[0].equals(str)) {
            if (objArr.length < 3) {
                throw new UnsupportedOperationException(str);
            }
            objArr = objArr[2];
        }
        V v3 = toV(objArr[1]);
        objArr[1] = v2;
        return v3;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<String> keySet() {
        return new AbstractSet<String>() { // from class: sun.util.PreHashedMap.1
            @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
            public int size() {
                return PreHashedMap.this.size;
            }

            @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
            public Iterator<String> iterator() {
                return new Iterator<String>() { // from class: sun.util.PreHashedMap.1.1

                    /* renamed from: i, reason: collision with root package name */
                    private int f13682i = -1;

                    /* renamed from: a, reason: collision with root package name */
                    Object[] f13683a = null;
                    String cur = null;

                    private boolean findNext() {
                        if (this.f13683a != null) {
                            if (this.f13683a.length == 3) {
                                this.f13683a = (Object[]) this.f13683a[2];
                                this.cur = (String) this.f13683a[0];
                                return true;
                            }
                            this.f13682i++;
                            this.f13683a = null;
                        }
                        this.cur = null;
                        if (this.f13682i >= PreHashedMap.this.rows) {
                            return false;
                        }
                        if (this.f13682i < 0 || PreHashedMap.this.ht[this.f13682i] == null) {
                            do {
                                int i2 = this.f13682i + 1;
                                this.f13682i = i2;
                                if (i2 >= PreHashedMap.this.rows) {
                                    return false;
                                }
                            } while (PreHashedMap.this.ht[this.f13682i] == null);
                        }
                        this.f13683a = (Object[]) PreHashedMap.this.ht[this.f13682i];
                        this.cur = (String) this.f13683a[0];
                        return true;
                    }

                    @Override // java.util.Iterator
                    public boolean hasNext() {
                        if (this.cur != null) {
                            return true;
                        }
                        return findNext();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Iterator
                    public String next() {
                        if (this.cur == null && !findNext()) {
                            throw new NoSuchElementException();
                        }
                        String str = this.cur;
                        this.cur = null;
                        return str;
                    }

                    @Override // java.util.Iterator
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    /* renamed from: sun.util.PreHashedMap$2, reason: invalid class name */
    /* loaded from: rt.jar:sun/util/PreHashedMap$2.class */
    class AnonymousClass2 extends AbstractSet<Map.Entry<String, V>> {
        AnonymousClass2() {
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return PreHashedMap.this.size;
        }

        /* renamed from: sun.util.PreHashedMap$2$1, reason: invalid class name */
        /* loaded from: rt.jar:sun/util/PreHashedMap$2$1.class */
        class AnonymousClass1 implements Iterator<Map.Entry<String, V>> {

            /* renamed from: i, reason: collision with root package name */
            final Iterator<String> f13684i;

            AnonymousClass1() {
                this.f13684i = PreHashedMap.this.keySet().iterator();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.f13684i.hasNext();
            }

            @Override // java.util.Iterator
            public Map.Entry<String, V> next() {
                return new Map.Entry<String, V>() { // from class: sun.util.PreHashedMap.2.1.1

                    /* renamed from: k, reason: collision with root package name */
                    String f13685k;

                    {
                        this.f13685k = AnonymousClass1.this.f13684i.next();
                    }

                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.util.Map.Entry
                    public String getKey() {
                        return this.f13685k;
                    }

                    @Override // java.util.Map.Entry
                    public V getValue() {
                        return (V) PreHashedMap.this.get(this.f13685k);
                    }

                    @Override // java.util.Map.Entry
                    public int hashCode() {
                        Object obj = PreHashedMap.this.get(this.f13685k);
                        return this.f13685k.hashCode() + (obj == null ? 0 : obj.hashCode());
                    }

                    @Override // java.util.Map.Entry
                    public boolean equals(Object obj) {
                        if (obj == this) {
                            return true;
                        }
                        if (!(obj instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry entry = (Map.Entry) obj;
                        if (getKey() != null ? getKey().equals(entry.getKey()) : entry.getKey() == null) {
                            if (getValue() != null ? getValue().equals(entry.getValue()) : entry.getValue() == null) {
                                return true;
                            }
                        }
                        return false;
                    }

                    @Override // java.util.Map.Entry
                    public V setValue(V v2) {
                        throw new UnsupportedOperationException();
                    }
                };
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<Map.Entry<String, V>> iterator() {
            return new AnonymousClass1();
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, V>> entrySet() {
        return new AnonymousClass2();
    }
}
