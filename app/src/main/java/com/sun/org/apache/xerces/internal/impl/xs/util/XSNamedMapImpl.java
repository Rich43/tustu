package com.sun.org.apache.xerces.internal.impl.xs.util;

import com.sun.org.apache.xerces.internal.util.SymbolHash;
import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
import com.sun.org.apache.xerces.internal.xs.XSObject;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSNamedMapImpl.class */
public class XSNamedMapImpl extends AbstractMap implements XSNamedMap {
    public static final XSNamedMapImpl EMPTY_MAP = new XSNamedMapImpl(new XSObject[0], 0);
    final String[] fNamespaces;
    final int fNSNum;
    final SymbolHash[] fMaps;
    XSObject[] fArray;
    int fLength;
    private Set fEntrySet;

    public XSNamedMapImpl(String namespace, SymbolHash map) {
        this.fArray = null;
        this.fLength = -1;
        this.fEntrySet = null;
        this.fNamespaces = new String[]{namespace};
        this.fMaps = new SymbolHash[]{map};
        this.fNSNum = 1;
    }

    public XSNamedMapImpl(String[] namespaces, SymbolHash[] maps, int num) {
        this.fArray = null;
        this.fLength = -1;
        this.fEntrySet = null;
        this.fNamespaces = namespaces;
        this.fMaps = maps;
        this.fNSNum = num;
    }

    public XSNamedMapImpl(XSObject[] array, int length) {
        this.fArray = null;
        this.fLength = -1;
        this.fEntrySet = null;
        if (length == 0) {
            this.fNamespaces = null;
            this.fMaps = null;
            this.fNSNum = 0;
            this.fArray = array;
            this.fLength = 0;
            return;
        }
        this.fNamespaces = new String[]{array[0].getNamespace()};
        this.fMaps = null;
        this.fNSNum = 1;
        this.fArray = array;
        this.fLength = length;
    }

    public synchronized int getLength() {
        if (this.fLength == -1) {
            this.fLength = 0;
            for (int i2 = 0; i2 < this.fNSNum; i2++) {
                this.fLength += this.fMaps[i2].getLength();
            }
        }
        return this.fLength;
    }

    public XSObject itemByName(String namespace, String localName) {
        for (int i2 = 0; i2 < this.fNSNum; i2++) {
            if (isEqual(namespace, this.fNamespaces[i2])) {
                if (this.fMaps != null) {
                    return (XSObject) this.fMaps[i2].get(localName);
                }
                for (int j2 = 0; j2 < this.fLength; j2++) {
                    XSObject ret = this.fArray[j2];
                    if (ret.getName().equals(localName)) {
                        return ret;
                    }
                }
                return null;
            }
        }
        return null;
    }

    public synchronized XSObject item(int index) {
        if (this.fArray == null) {
            getLength();
            this.fArray = new XSObject[this.fLength];
            int pos = 0;
            for (int i2 = 0; i2 < this.fNSNum; i2++) {
                pos += this.fMaps[i2].getValues(this.fArray, pos);
            }
        }
        if (index < 0 || index >= this.fLength) {
            return null;
        }
        return this.fArray[index];
    }

    static boolean isEqual(String one, String two) {
        return one != null ? one.equals(two) : two == null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        if (key instanceof QName) {
            QName name = (QName) key;
            String namespaceURI = name.getNamespaceURI();
            if ("".equals(namespaceURI)) {
                namespaceURI = null;
            }
            String localPart = name.getLocalPart();
            return itemByName(namespaceURI, localPart);
        }
        return null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        return getLength();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public synchronized Set entrySet() {
        if (this.fEntrySet == null) {
            final int length = getLength();
            final XSNamedMapEntry[] entries = new XSNamedMapEntry[length];
            for (int i2 = 0; i2 < length; i2++) {
                XSObject xso = item(i2);
                entries[i2] = new XSNamedMapEntry(new QName(xso.getNamespace(), xso.getName()), xso);
            }
            this.fEntrySet = new AbstractSet() { // from class: com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl.1
                @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
                public Iterator iterator() {
                    return new Iterator() { // from class: com.sun.org.apache.xerces.internal.impl.xs.util.XSNamedMapImpl.1.1
                        private int index = 0;

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return this.index < length;
                        }

                        @Override // java.util.Iterator
                        public Object next() {
                            if (this.index < length) {
                                XSNamedMapEntry[] xSNamedMapEntryArr = entries;
                                int i3 = this.index;
                                this.index = i3 + 1;
                                return xSNamedMapEntryArr[i3];
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
                    return length;
                }
            };
        }
        return this.fEntrySet;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/xs/util/XSNamedMapImpl$XSNamedMapEntry.class */
    private static final class XSNamedMapEntry implements Map.Entry {
        private final QName key;
        private final XSObject value;

        public XSNamedMapEntry(QName key, XSObject value) {
            this.key = key;
            this.value = value;
        }

        @Override // java.util.Map.Entry
        public Object getKey() {
            return this.key;
        }

        @Override // java.util.Map.Entry
        public Object getValue() {
            return this.value;
        }

        @Override // java.util.Map.Entry
        public Object setValue(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.Map.Entry
        public boolean equals(Object o2) {
            if (o2 instanceof Map.Entry) {
                Map.Entry e2 = (Map.Entry) o2;
                Object otherKey = e2.getKey();
                Object otherValue = e2.getValue();
                if (this.key != null ? this.key.equals(otherKey) : otherKey == null) {
                    if (this.value != null ? this.value.equals(otherValue) : otherValue == null) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        }

        @Override // java.util.Map.Entry
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            StringBuffer buffer = new StringBuffer();
            buffer.append(String.valueOf(this.key));
            buffer.append('=');
            buffer.append(String.valueOf(this.value));
            return buffer.toString();
        }
    }
}
