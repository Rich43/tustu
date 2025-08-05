package com.sun.xml.internal.fastinfoset.util;

import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
import com.sun.xml.internal.org.jvnet.fastinfoset.FastInfosetException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/PrefixArray.class */
public class PrefixArray extends ValueArray {
    public static final int PREFIX_MAP_SIZE = 64;
    private int _initialCapacity;
    public String[] _array;
    private PrefixArray _readOnlyArray;
    private PrefixEntry[] _prefixMap;
    private PrefixEntry _prefixPool;
    private NamespaceEntry _namespacePool;
    private NamespaceEntry[] _inScopeNamespaces;
    public int[] _currentInScope;
    public int _declarationId;

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/PrefixArray$PrefixEntry.class */
    private static class PrefixEntry {
        private PrefixEntry next;
        private int prefixId;

        private PrefixEntry() {
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/fastinfoset/util/PrefixArray$NamespaceEntry.class */
    private static class NamespaceEntry {
        private NamespaceEntry next;
        private int declarationId;
        private int namespaceIndex;
        private String prefix;
        private String namespaceName;
        private int prefixEntryIndex;

        private NamespaceEntry() {
        }
    }

    public PrefixArray(int initialCapacity, int maximumCapacity) {
        this._prefixMap = new PrefixEntry[64];
        this._initialCapacity = initialCapacity;
        this._maximumCapacity = maximumCapacity;
        this._array = new String[initialCapacity];
        this._inScopeNamespaces = new NamespaceEntry[initialCapacity + 2];
        this._currentInScope = new int[initialCapacity + 2];
        increaseNamespacePool(initialCapacity);
        increasePrefixPool(initialCapacity);
        initializeEntries();
    }

    public PrefixArray() {
        this(10, Integer.MAX_VALUE);
    }

    private final void initializeEntries() {
        this._inScopeNamespaces[0] = this._namespacePool;
        this._namespacePool = this._namespacePool.next;
        this._inScopeNamespaces[0].next = null;
        this._inScopeNamespaces[0].prefix = "";
        this._inScopeNamespaces[0].namespaceName = "";
        NamespaceEntry namespaceEntry = this._inScopeNamespaces[0];
        this._currentInScope[0] = 0;
        namespaceEntry.namespaceIndex = 0;
        int index = KeyIntMap.indexFor(KeyIntMap.hashHash(this._inScopeNamespaces[0].prefix.hashCode()), this._prefixMap.length);
        this._prefixMap[index] = this._prefixPool;
        this._prefixPool = this._prefixPool.next;
        this._prefixMap[index].next = null;
        this._prefixMap[index].prefixId = 0;
        this._inScopeNamespaces[1] = this._namespacePool;
        this._namespacePool = this._namespacePool.next;
        this._inScopeNamespaces[1].next = null;
        this._inScopeNamespaces[1].prefix = "xml";
        this._inScopeNamespaces[1].namespaceName = "http://www.w3.org/XML/1998/namespace";
        NamespaceEntry namespaceEntry2 = this._inScopeNamespaces[1];
        this._currentInScope[1] = 1;
        namespaceEntry2.namespaceIndex = 1;
        int index2 = KeyIntMap.indexFor(KeyIntMap.hashHash(this._inScopeNamespaces[1].prefix.hashCode()), this._prefixMap.length);
        if (this._prefixMap[index2] == null) {
            this._prefixMap[index2] = this._prefixPool;
            this._prefixPool = this._prefixPool.next;
            this._prefixMap[index2].next = null;
        } else {
            PrefixEntry e2 = this._prefixMap[index2];
            this._prefixMap[index2] = this._prefixPool;
            this._prefixPool = this._prefixPool.next;
            this._prefixMap[index2].next = e2;
        }
        this._prefixMap[index2].prefixId = 1;
    }

    private final void increaseNamespacePool(int capacity) {
        if (this._namespacePool == null) {
            this._namespacePool = new NamespaceEntry();
        }
        for (int i2 = 0; i2 < capacity; i2++) {
            NamespaceEntry ne = new NamespaceEntry();
            ne.next = this._namespacePool;
            this._namespacePool = ne;
        }
    }

    private final void increasePrefixPool(int capacity) {
        if (this._prefixPool == null) {
            this._prefixPool = new PrefixEntry();
        }
        for (int i2 = 0; i2 < capacity; i2++) {
            PrefixEntry pe = new PrefixEntry();
            pe.next = this._prefixPool;
            this._prefixPool = pe;
        }
    }

    public int countNamespacePool() {
        int i2 = 0;
        NamespaceEntry namespaceEntry = this._namespacePool;
        while (true) {
            NamespaceEntry e2 = namespaceEntry;
            if (e2 != null) {
                i2++;
                namespaceEntry = e2.next;
            } else {
                return i2;
            }
        }
    }

    public int countPrefixPool() {
        int i2 = 0;
        PrefixEntry prefixEntry = this._prefixPool;
        while (true) {
            PrefixEntry e2 = prefixEntry;
            if (e2 != null) {
                i2++;
                prefixEntry = e2.next;
            } else {
                return i2;
            }
        }
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void clear() {
        for (int i2 = this._readOnlyArraySize; i2 < this._size; i2++) {
            this._array[i2] = null;
        }
        this._size = this._readOnlyArraySize;
    }

    public final void clearCompletely() {
        this._prefixPool = null;
        this._namespacePool = null;
        for (int i2 = 0; i2 < this._size + 2; i2++) {
            this._currentInScope[i2] = 0;
            this._inScopeNamespaces[i2] = null;
        }
        for (int i3 = 0; i3 < this._prefixMap.length; i3++) {
            this._prefixMap[i3] = null;
        }
        increaseNamespacePool(this._initialCapacity);
        increasePrefixPool(this._initialCapacity);
        initializeEntries();
        this._declarationId = 0;
        clear();
    }

    public final String[] getArray() {
        if (this._array == null) {
            return null;
        }
        String[] clonedArray = new String[this._array.length];
        System.arraycopy(this._array, 0, clonedArray, 0, this._array.length);
        return clonedArray;
    }

    @Override // com.sun.xml.internal.fastinfoset.util.ValueArray
    public final void setReadOnlyArray(ValueArray readOnlyArray, boolean clear) {
        if (!(readOnlyArray instanceof PrefixArray)) {
            throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.illegalClass", new Object[]{readOnlyArray}));
        }
        setReadOnlyArray((PrefixArray) readOnlyArray, clear);
    }

    public final void setReadOnlyArray(PrefixArray readOnlyArray, boolean clear) {
        if (readOnlyArray != null) {
            this._readOnlyArray = readOnlyArray;
            this._readOnlyArraySize = readOnlyArray.getSize();
            clearCompletely();
            this._inScopeNamespaces = new NamespaceEntry[this._readOnlyArraySize + this._inScopeNamespaces.length];
            this._currentInScope = new int[this._readOnlyArraySize + this._currentInScope.length];
            initializeEntries();
            if (clear) {
                clear();
            }
            this._array = getCompleteArray();
            this._size = this._readOnlyArraySize;
        }
    }

    public final String[] getCompleteArray() {
        if (this._readOnlyArray == null) {
            return getArray();
        }
        String[] ra = this._readOnlyArray.getCompleteArray();
        String[] a2 = new String[this._readOnlyArraySize + this._array.length];
        System.arraycopy(ra, 0, a2, 0, this._readOnlyArraySize);
        return a2;
    }

    public final String get(int i2) {
        return this._array[i2];
    }

    public final int add(String s2) {
        if (this._size == this._array.length) {
            resize();
        }
        String[] strArr = this._array;
        int i2 = this._size;
        this._size = i2 + 1;
        strArr[i2] = s2;
        return this._size;
    }

    protected final void resize() {
        if (this._size == this._maximumCapacity) {
            throw new ValueArrayResourceException(CommonResourceBundle.getInstance().getString("message.arrayMaxCapacity"));
        }
        int newSize = ((this._size * 3) / 2) + 1;
        if (newSize > this._maximumCapacity) {
            newSize = this._maximumCapacity;
        }
        String[] newArray = new String[newSize];
        System.arraycopy(this._array, 0, newArray, 0, this._size);
        this._array = newArray;
        int newSize2 = newSize + 2;
        NamespaceEntry[] newInScopeNamespaces = new NamespaceEntry[newSize2];
        System.arraycopy(this._inScopeNamespaces, 0, newInScopeNamespaces, 0, this._inScopeNamespaces.length);
        this._inScopeNamespaces = newInScopeNamespaces;
        int[] newCurrentInScope = new int[newSize2];
        System.arraycopy(this._currentInScope, 0, newCurrentInScope, 0, this._currentInScope.length);
        this._currentInScope = newCurrentInScope;
    }

    public final void clearDeclarationIds() {
        for (int i2 = 0; i2 < this._size; i2++) {
            NamespaceEntry e2 = this._inScopeNamespaces[i2];
            if (e2 != null) {
                e2.declarationId = 0;
            }
        }
        this._declarationId = 1;
    }

    public final void pushScope(int prefixIndex, int namespaceIndex) throws FastInfosetException {
        if (this._namespacePool == null) {
            increaseNamespacePool(16);
        }
        NamespaceEntry e2 = this._namespacePool;
        this._namespacePool = e2.next;
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry current = this._inScopeNamespaces[prefixIndex2];
        if (current == null) {
            e2.declarationId = this._declarationId;
            int namespaceIndex2 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex2;
            e2.namespaceIndex = namespaceIndex2;
            e2.next = null;
            this._inScopeNamespaces[prefixIndex2] = e2;
            return;
        }
        if (current.declarationId < this._declarationId) {
            e2.declarationId = this._declarationId;
            int namespaceIndex3 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex3;
            e2.namespaceIndex = namespaceIndex3;
            e2.next = current;
            current.declarationId = 0;
            this._inScopeNamespaces[prefixIndex2] = e2;
            return;
        }
        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateNamespaceAttribute"));
    }

    public final void pushScopeWithPrefixEntry(String prefix, String namespaceName, int prefixIndex, int namespaceIndex) throws FastInfosetException {
        if (this._namespacePool == null) {
            increaseNamespacePool(16);
        }
        if (this._prefixPool == null) {
            increasePrefixPool(16);
        }
        NamespaceEntry e2 = this._namespacePool;
        this._namespacePool = e2.next;
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry current = this._inScopeNamespaces[prefixIndex2];
        if (current == null) {
            e2.declarationId = this._declarationId;
            int namespaceIndex2 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex2;
            e2.namespaceIndex = namespaceIndex2;
            e2.next = null;
            this._inScopeNamespaces[prefixIndex2] = e2;
        } else if (current.declarationId < this._declarationId) {
            e2.declarationId = this._declarationId;
            int namespaceIndex3 = namespaceIndex + 1;
            this._currentInScope[prefixIndex2] = namespaceIndex3;
            e2.namespaceIndex = namespaceIndex3;
            e2.next = current;
            current.declarationId = 0;
            this._inScopeNamespaces[prefixIndex2] = e2;
        } else {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.duplicateNamespaceAttribute"));
        }
        PrefixEntry p2 = this._prefixPool;
        this._prefixPool = this._prefixPool.next;
        p2.prefixId = prefixIndex2;
        e2.prefix = prefix;
        e2.namespaceName = namespaceName;
        e2.prefixEntryIndex = KeyIntMap.indexFor(KeyIntMap.hashHash(prefix.hashCode()), this._prefixMap.length);
        PrefixEntry pCurrent = this._prefixMap[e2.prefixEntryIndex];
        p2.next = pCurrent;
        this._prefixMap[e2.prefixEntryIndex] = p2;
    }

    public final void popScope(int prefixIndex) {
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry e2 = this._inScopeNamespaces[prefixIndex2];
        this._inScopeNamespaces[prefixIndex2] = e2.next;
        this._currentInScope[prefixIndex2] = e2.next != null ? e2.next.namespaceIndex : 0;
        e2.next = this._namespacePool;
        this._namespacePool = e2;
    }

    public final void popScopeWithPrefixEntry(int prefixIndex) {
        int prefixIndex2 = prefixIndex + 1;
        NamespaceEntry e2 = this._inScopeNamespaces[prefixIndex2];
        this._inScopeNamespaces[prefixIndex2] = e2.next;
        this._currentInScope[prefixIndex2] = e2.next != null ? e2.next.namespaceIndex : 0;
        e2.prefix = e2.namespaceName = null;
        e2.next = this._namespacePool;
        this._namespacePool = e2;
        PrefixEntry current = this._prefixMap[e2.prefixEntryIndex];
        if (current.prefixId == prefixIndex2) {
            this._prefixMap[e2.prefixEntryIndex] = current.next;
            current.next = this._prefixPool;
            this._prefixPool = current;
            return;
        }
        PrefixEntry prev = current;
        PrefixEntry prefixEntry = current.next;
        while (true) {
            PrefixEntry current2 = prefixEntry;
            if (current2 != null) {
                if (current2.prefixId == prefixIndex2) {
                    prev.next = current2.next;
                    current2.next = this._prefixPool;
                    this._prefixPool = current2;
                    return;
                }
                prev = current2;
                prefixEntry = current2.next;
            } else {
                return;
            }
        }
    }

    public final String getNamespaceFromPrefix(String prefix) {
        NamespaceEntry ne;
        int index = KeyIntMap.indexFor(KeyIntMap.hashHash(prefix.hashCode()), this._prefixMap.length);
        PrefixEntry prefixEntry = this._prefixMap[index];
        while (true) {
            PrefixEntry pe = prefixEntry;
            if (pe != null) {
                ne = this._inScopeNamespaces[pe.prefixId];
                if (prefix == ne.prefix || prefix.equals(ne.prefix)) {
                    break;
                }
                prefixEntry = pe.next;
            } else {
                return null;
            }
        }
        return ne.namespaceName;
    }

    public final String getPrefixFromNamespace(String namespaceName) {
        int position = 0;
        while (true) {
            position++;
            if (position < this._size + 2) {
                NamespaceEntry ne = this._inScopeNamespaces[position];
                if (ne != null && namespaceName.equals(ne.namespaceName)) {
                    return ne.prefix;
                }
            } else {
                return null;
            }
        }
    }

    public final Iterator getPrefixes() {
        return new Iterator() { // from class: com.sun.xml.internal.fastinfoset.util.PrefixArray.1
            int _position = 1;
            NamespaceEntry _ne;

            {
                this._ne = PrefixArray.this._inScopeNamespaces[this._position];
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this._ne != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (this._position != PrefixArray.this._size + 2) {
                    String prefix = this._ne.prefix;
                    moveToNext();
                    return prefix;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private final void moveToNext() {
                do {
                    int i2 = this._position + 1;
                    this._position = i2;
                    if (i2 < PrefixArray.this._size + 2) {
                        this._ne = PrefixArray.this._inScopeNamespaces[this._position];
                    } else {
                        this._ne = null;
                        return;
                    }
                } while (this._ne == null);
            }
        };
    }

    public final Iterator getPrefixesFromNamespace(final String namespaceName) {
        return new Iterator() { // from class: com.sun.xml.internal.fastinfoset.util.PrefixArray.2
            String _namespaceName;
            int _position = 0;
            NamespaceEntry _ne;

            {
                this._namespaceName = namespaceName;
                moveToNext();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this._ne != null;
            }

            @Override // java.util.Iterator
            public Object next() {
                if (this._position != PrefixArray.this._size + 2) {
                    String prefix = this._ne.prefix;
                    moveToNext();
                    return prefix;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException();
            }

            private final void moveToNext() {
                while (true) {
                    int i2 = this._position + 1;
                    this._position = i2;
                    if (i2 < PrefixArray.this._size + 2) {
                        this._ne = PrefixArray.this._inScopeNamespaces[this._position];
                        if (this._ne != null && this._namespaceName.equals(this._ne.namespaceName)) {
                            return;
                        }
                    } else {
                        this._ne = null;
                        return;
                    }
                }
            }
        };
    }
}
