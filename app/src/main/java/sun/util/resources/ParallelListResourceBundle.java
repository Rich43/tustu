package sun.util.resources;

import java.util.AbstractSet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicMarkableReference;

/* loaded from: rt.jar:sun/util/resources/ParallelListResourceBundle.class */
public abstract class ParallelListResourceBundle extends ResourceBundle {
    private volatile ConcurrentMap<String, Object> lookup;
    private volatile Set<String> keyset;
    private final AtomicMarkableReference<Object[][]> parallelContents = new AtomicMarkableReference<>(null, false);

    protected abstract Object[][] getContents();

    protected ParallelListResourceBundle() {
    }

    ResourceBundle getParent() {
        return this.parent;
    }

    public void setParallelContents(OpenListResourceBundle openListResourceBundle) {
        if (openListResourceBundle == null) {
            this.parallelContents.compareAndSet(null, null, false, true);
        } else {
            this.parallelContents.compareAndSet(null, openListResourceBundle.getContents(), false, false);
        }
    }

    boolean areParallelContentsComplete() {
        if (this.parallelContents.isMarked()) {
            return true;
        }
        boolean[] zArr = new boolean[1];
        return this.parallelContents.get(zArr) != null || zArr[0];
    }

    @Override // java.util.ResourceBundle
    protected Object handleGetObject(String str) {
        if (str == null) {
            throw new NullPointerException();
        }
        loadLookupTablesIfNecessary();
        return this.lookup.get(str);
    }

    @Override // java.util.ResourceBundle
    public Enumeration<String> getKeys() {
        return Collections.enumeration(keySet());
    }

    @Override // java.util.ResourceBundle
    public boolean containsKey(String str) {
        return keySet().contains(str);
    }

    @Override // java.util.ResourceBundle
    protected Set<String> handleKeySet() {
        loadLookupTablesIfNecessary();
        return this.lookup.keySet();
    }

    @Override // java.util.ResourceBundle
    public Set<String> keySet() {
        while (true) {
            Set<String> set = this.keyset;
            if (set == null) {
                KeySet keySet = new KeySet(handleKeySet(), this.parent);
                synchronized (this) {
                    if (this.keyset == null) {
                        this.keyset = keySet;
                    }
                }
            } else {
                return set;
            }
        }
    }

    synchronized void resetKeySet() {
        this.keyset = null;
    }

    void loadLookupTablesIfNecessary() {
        ConcurrentMap<String, Object> concurrentHashMap = this.lookup;
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap();
            for (Object[] objArr : getContents()) {
                concurrentHashMap.put((String) objArr[0], objArr[1]);
            }
        }
        Object[][] reference = this.parallelContents.getReference();
        if (reference != null) {
            for (Object[] objArr2 : reference) {
                concurrentHashMap.putIfAbsent((String) objArr2[0], objArr2[1]);
            }
            this.parallelContents.set(null, true);
        }
        if (this.lookup == null) {
            synchronized (this) {
                if (this.lookup == null) {
                    this.lookup = concurrentHashMap;
                }
            }
        }
    }

    /* loaded from: rt.jar:sun/util/resources/ParallelListResourceBundle$KeySet.class */
    private static class KeySet extends AbstractSet<String> {
        private final Set<String> set;
        private final ResourceBundle parent;

        private KeySet(Set<String> set, ResourceBundle resourceBundle) {
            this.set = set;
            this.parent = resourceBundle;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            if (this.set.contains(obj)) {
                return true;
            }
            if (this.parent != null) {
                return this.parent.containsKey((String) obj);
            }
            return false;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
        public Iterator<String> iterator() {
            if (this.parent == null) {
                return this.set.iterator();
            }
            return new Iterator<String>() { // from class: sun.util.resources.ParallelListResourceBundle.KeySet.1
                private Iterator<String> itr;
                private boolean usingParent;

                {
                    this.itr = KeySet.this.set.iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    if (this.itr.hasNext()) {
                        return true;
                    }
                    if (!this.usingParent) {
                        HashSet hashSet = new HashSet(KeySet.this.parent.keySet());
                        hashSet.removeAll(KeySet.this.set);
                        this.itr = hashSet.iterator();
                        this.usingParent = true;
                    }
                    return this.itr.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public String next() {
                    if (hasNext()) {
                        return this.itr.next();
                    }
                    throw new NoSuchElementException();
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw new UnsupportedOperationException();
                }
            };
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            if (this.parent == null) {
                return this.set.size();
            }
            HashSet hashSet = new HashSet(this.set);
            hashSet.addAll(this.parent.keySet());
            return hashSet.size();
        }
    }
}
