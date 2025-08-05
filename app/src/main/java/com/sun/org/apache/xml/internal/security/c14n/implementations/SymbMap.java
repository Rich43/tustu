package com.sun.org.apache.xml.internal.security.c14n.implementations;

import java.util.ArrayList;
import java.util.List;

/* compiled from: NameSpaceSymbTable.java */
/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/SymbMap.class */
class SymbMap implements Cloneable {
    int free = 23;
    NameSpaceSymbEntry[] entries = new NameSpaceSymbEntry[this.free];
    String[] keys = new String[this.free];

    SymbMap() {
    }

    void put(String str, NameSpaceSymbEntry nameSpaceSymbEntry) {
        int iIndex = index(str);
        String str2 = this.keys[iIndex];
        this.keys[iIndex] = str;
        this.entries[iIndex] = nameSpaceSymbEntry;
        if (str2 == null || !str2.equals(str)) {
            int i2 = this.free - 1;
            this.free = i2;
            if (i2 == 0) {
                this.free = this.entries.length;
                rehash(this.free << 2);
            }
        }
    }

    List<NameSpaceSymbEntry> entrySet() {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < this.entries.length; i2++) {
            if (this.entries[i2] != null && !"".equals(this.entries[i2].uri)) {
                arrayList.add(this.entries[i2]);
            }
        }
        return arrayList;
    }

    protected int index(Object obj) {
        String str;
        String[] strArr = this.keys;
        int length = strArr.length;
        int iHashCode = (obj.hashCode() & Integer.MAX_VALUE) % length;
        String str2 = strArr[iHashCode];
        if (str2 == null || str2.equals(obj)) {
            return iHashCode;
        }
        int i2 = length - 1;
        do {
            iHashCode = iHashCode == i2 ? 0 : iHashCode + 1;
            str = strArr[iHashCode];
            if (str == null) {
                break;
            }
        } while (!str.equals(obj));
        return iHashCode;
    }

    protected void rehash(int i2) {
        int length = this.keys.length;
        String[] strArr = this.keys;
        NameSpaceSymbEntry[] nameSpaceSymbEntryArr = this.entries;
        this.keys = new String[i2];
        this.entries = new NameSpaceSymbEntry[i2];
        int i3 = length;
        while (true) {
            int i4 = i3;
            i3--;
            if (i4 > 0) {
                if (strArr[i3] != null) {
                    String str = strArr[i3];
                    int iIndex = index(str);
                    this.keys[iIndex] = str;
                    this.entries[iIndex] = nameSpaceSymbEntryArr[i3];
                }
            } else {
                return;
            }
        }
    }

    NameSpaceSymbEntry get(String str) {
        return this.entries[index(str)];
    }

    protected Object clone() {
        try {
            SymbMap symbMap = (SymbMap) super.clone();
            symbMap.entries = new NameSpaceSymbEntry[this.entries.length];
            System.arraycopy(this.entries, 0, symbMap.entries, 0, this.entries.length);
            symbMap.keys = new String[this.keys.length];
            System.arraycopy(this.keys, 0, symbMap.keys, 0, this.keys.length);
            return symbMap;
        } catch (CloneNotSupportedException e2) {
            e2.printStackTrace();
            return null;
        }
    }
}
