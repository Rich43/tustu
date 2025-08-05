package com.sun.corba.se.impl.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/IdentityHashtableEnumerator.class */
class IdentityHashtableEnumerator implements Enumeration {
    boolean keys;
    int index;
    IdentityHashtableEntry[] table;
    IdentityHashtableEntry entry;

    IdentityHashtableEnumerator(IdentityHashtableEntry[] identityHashtableEntryArr, boolean z2) {
        this.table = identityHashtableEntryArr;
        this.keys = z2;
        this.index = identityHashtableEntryArr.length;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        IdentityHashtableEntry identityHashtableEntry;
        if (this.entry != null) {
            return true;
        }
        do {
            int i2 = this.index;
            this.index = i2 - 1;
            if (i2 > 0) {
                identityHashtableEntry = this.table[this.index];
                this.entry = identityHashtableEntry;
            } else {
                return false;
            }
        } while (identityHashtableEntry == null);
        return true;
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        IdentityHashtableEntry identityHashtableEntry;
        if (this.entry == null) {
            do {
                int i2 = this.index;
                this.index = i2 - 1;
                if (i2 <= 0) {
                    break;
                }
                identityHashtableEntry = this.table[this.index];
                this.entry = identityHashtableEntry;
            } while (identityHashtableEntry == null);
        }
        if (this.entry != null) {
            IdentityHashtableEntry identityHashtableEntry2 = this.entry;
            this.entry = identityHashtableEntry2.next;
            return this.keys ? identityHashtableEntry2.key : identityHashtableEntry2.value;
        }
        throw new NoSuchElementException("IdentityHashtableEnumerator");
    }
}
