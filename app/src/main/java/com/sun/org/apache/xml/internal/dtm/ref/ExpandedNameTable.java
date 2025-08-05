package com.sun.org.apache.xml.internal.dtm.ref;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/ExpandedNameTable.class */
public class ExpandedNameTable {
    private ExtendedType[] m_extendedTypes;
    private int m_nextType;
    public static final int ELEMENT = 1;
    public static final int ATTRIBUTE = 2;
    public static final int TEXT = 3;
    public static final int CDATA_SECTION = 4;
    public static final int ENTITY_REFERENCE = 5;
    public static final int ENTITY = 6;
    public static final int PROCESSING_INSTRUCTION = 7;
    public static final int COMMENT = 8;
    public static final int DOCUMENT = 9;
    public static final int DOCUMENT_TYPE = 10;
    public static final int DOCUMENT_FRAGMENT = 11;
    public static final int NOTATION = 12;
    public static final int NAMESPACE = 13;
    ExtendedType hashET = new ExtendedType(-1, "", "");
    private int m_capacity = m_initialCapacity;
    private int m_threshold = (int) (this.m_capacity * m_loadFactor);
    private HashEntry[] m_table = new HashEntry[this.m_capacity];
    private static int m_initialSize = 128;
    private static float m_loadFactor = 0.75f;
    private static int m_initialCapacity = 203;
    private static ExtendedType[] m_defaultExtendedTypes = new ExtendedType[14];

    static {
        for (int i2 = 0; i2 < 14; i2++) {
            m_defaultExtendedTypes[i2] = new ExtendedType(i2, "", "");
        }
    }

    public ExpandedNameTable() {
        initExtendedTypes();
    }

    private void initExtendedTypes() {
        this.m_extendedTypes = new ExtendedType[m_initialSize];
        for (int i2 = 0; i2 < 14; i2++) {
            this.m_extendedTypes[i2] = m_defaultExtendedTypes[i2];
            this.m_table[i2] = new HashEntry(m_defaultExtendedTypes[i2], i2, i2, null);
        }
        this.m_nextType = 14;
    }

    public int getExpandedTypeID(String namespace, String localName, int type) {
        return getExpandedTypeID(namespace, localName, type, false);
    }

    public int getExpandedTypeID(String namespace, String localName, int type, boolean searchOnly) {
        if (null == namespace) {
            namespace = "";
        }
        if (null == localName) {
            localName = "";
        }
        int hash = type + namespace.hashCode() + localName.hashCode();
        this.hashET.redefine(type, namespace, localName, hash);
        int index = hash % this.m_capacity;
        if (index < 0) {
            index = -index;
        }
        HashEntry hashEntry = this.m_table[index];
        while (true) {
            HashEntry e2 = hashEntry;
            if (e2 != null) {
                if (e2.hash != hash || !e2.key.equals(this.hashET)) {
                    hashEntry = e2.next;
                } else {
                    return e2.value;
                }
            } else {
                if (searchOnly) {
                    return -1;
                }
                if (this.m_nextType > this.m_threshold) {
                    rehash();
                    index = hash % this.m_capacity;
                    if (index < 0) {
                        index = -index;
                    }
                }
                ExtendedType newET = new ExtendedType(type, namespace, localName, hash);
                if (this.m_extendedTypes.length == this.m_nextType) {
                    ExtendedType[] newArray = new ExtendedType[this.m_extendedTypes.length * 2];
                    System.arraycopy(this.m_extendedTypes, 0, newArray, 0, this.m_extendedTypes.length);
                    this.m_extendedTypes = newArray;
                }
                this.m_extendedTypes[this.m_nextType] = newET;
                HashEntry entry = new HashEntry(newET, this.m_nextType, hash, this.m_table[index]);
                this.m_table[index] = entry;
                int i2 = this.m_nextType;
                this.m_nextType = i2 + 1;
                return i2;
            }
        }
    }

    private void rehash() {
        int oldCapacity = this.m_capacity;
        HashEntry[] oldTable = this.m_table;
        int newCapacity = (2 * oldCapacity) + 1;
        this.m_capacity = newCapacity;
        this.m_threshold = (int) (newCapacity * m_loadFactor);
        this.m_table = new HashEntry[newCapacity];
        for (int i2 = oldCapacity - 1; i2 >= 0; i2--) {
            HashEntry old = oldTable[i2];
            while (old != null) {
                HashEntry e2 = old;
                old = old.next;
                int newIndex = e2.hash % newCapacity;
                if (newIndex < 0) {
                    newIndex = -newIndex;
                }
                e2.next = this.m_table[newIndex];
                this.m_table[newIndex] = e2;
            }
        }
    }

    public int getExpandedTypeID(int type) {
        return type;
    }

    public String getLocalName(int ExpandedNameID) {
        return this.m_extendedTypes[ExpandedNameID].getLocalName();
    }

    public final int getLocalNameID(int ExpandedNameID) {
        if (this.m_extendedTypes[ExpandedNameID].getLocalName().equals("")) {
            return 0;
        }
        return ExpandedNameID;
    }

    public String getNamespace(int ExpandedNameID) {
        String namespace = this.m_extendedTypes[ExpandedNameID].getNamespace();
        if (namespace.equals("")) {
            return null;
        }
        return namespace;
    }

    public final int getNamespaceID(int ExpandedNameID) {
        if (this.m_extendedTypes[ExpandedNameID].getNamespace().equals("")) {
            return 0;
        }
        return ExpandedNameID;
    }

    public final short getType(int ExpandedNameID) {
        return (short) this.m_extendedTypes[ExpandedNameID].getNodeType();
    }

    public int getSize() {
        return this.m_nextType;
    }

    public ExtendedType[] getExtendedTypes() {
        return this.m_extendedTypes;
    }

    /* loaded from: rt.jar:com/sun/org/apache/xml/internal/dtm/ref/ExpandedNameTable$HashEntry.class */
    private static final class HashEntry {
        ExtendedType key;
        int value;
        int hash;
        HashEntry next;

        protected HashEntry(ExtendedType key, int value, int hash, HashEntry next) {
            this.key = key;
            this.value = value;
            this.hash = hash;
            this.next = next;
        }
    }
}
