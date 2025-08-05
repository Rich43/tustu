package com.sun.corba.se.impl.util;

import java.util.Dictionary;
import java.util.Enumeration;
import org.apache.commons.math3.geometry.VectorFormat;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/IdentityHashtable.class */
public final class IdentityHashtable extends Dictionary {
    private transient IdentityHashtableEntry[] table;
    private transient int count;
    private int threshold;
    private float loadFactor;

    public IdentityHashtable(int i2, float f2) {
        if (i2 <= 0 || f2 <= 0.0d) {
            throw new IllegalArgumentException();
        }
        this.loadFactor = f2;
        this.table = new IdentityHashtableEntry[i2];
        this.threshold = (int) (i2 * f2);
    }

    public IdentityHashtable(int i2) {
        this(i2, 0.75f);
    }

    public IdentityHashtable() {
        this(101, 0.75f);
    }

    @Override // java.util.Dictionary
    public int size() {
        return this.count;
    }

    @Override // java.util.Dictionary
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override // java.util.Dictionary
    public Enumeration keys() {
        return new IdentityHashtableEnumerator(this.table, true);
    }

    @Override // java.util.Dictionary
    public Enumeration elements() {
        return new IdentityHashtableEnumerator(this.table, false);
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            throw new NullPointerException();
        }
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int length = identityHashtableEntryArr.length;
        while (true) {
            int i2 = length;
            length--;
            if (i2 > 0) {
                IdentityHashtableEntry identityHashtableEntry = identityHashtableEntryArr[length];
                while (true) {
                    IdentityHashtableEntry identityHashtableEntry2 = identityHashtableEntry;
                    if (identityHashtableEntry2 != null) {
                        if (identityHashtableEntry2.value != obj) {
                            identityHashtableEntry = identityHashtableEntry2.next;
                        } else {
                            return true;
                        }
                    }
                }
            } else {
                return false;
            }
        }
    }

    public boolean containsKey(Object obj) {
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int iIdentityHashCode = System.identityHashCode(obj);
        IdentityHashtableEntry identityHashtableEntry = identityHashtableEntryArr[(iIdentityHashCode & Integer.MAX_VALUE) % identityHashtableEntryArr.length];
        while (true) {
            IdentityHashtableEntry identityHashtableEntry2 = identityHashtableEntry;
            if (identityHashtableEntry2 != null) {
                if (identityHashtableEntry2.hash != iIdentityHashCode || identityHashtableEntry2.key != obj) {
                    identityHashtableEntry = identityHashtableEntry2.next;
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // java.util.Dictionary
    public Object get(Object obj) {
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int iIdentityHashCode = System.identityHashCode(obj);
        IdentityHashtableEntry identityHashtableEntry = identityHashtableEntryArr[(iIdentityHashCode & Integer.MAX_VALUE) % identityHashtableEntryArr.length];
        while (true) {
            IdentityHashtableEntry identityHashtableEntry2 = identityHashtableEntry;
            if (identityHashtableEntry2 != null) {
                if (identityHashtableEntry2.hash != iIdentityHashCode || identityHashtableEntry2.key != obj) {
                    identityHashtableEntry = identityHashtableEntry2.next;
                } else {
                    return identityHashtableEntry2.value;
                }
            } else {
                return null;
            }
        }
    }

    protected void rehash() {
        int length = this.table.length;
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int i2 = (length * 2) + 1;
        IdentityHashtableEntry[] identityHashtableEntryArr2 = new IdentityHashtableEntry[i2];
        this.threshold = (int) (i2 * this.loadFactor);
        this.table = identityHashtableEntryArr2;
        int i3 = length;
        while (true) {
            int i4 = i3;
            i3--;
            if (i4 > 0) {
                IdentityHashtableEntry identityHashtableEntry = identityHashtableEntryArr[i3];
                while (identityHashtableEntry != null) {
                    IdentityHashtableEntry identityHashtableEntry2 = identityHashtableEntry;
                    identityHashtableEntry = identityHashtableEntry.next;
                    int i5 = (identityHashtableEntry2.hash & Integer.MAX_VALUE) % i2;
                    identityHashtableEntry2.next = identityHashtableEntryArr2[i5];
                    identityHashtableEntryArr2[i5] = identityHashtableEntry2;
                }
            } else {
                return;
            }
        }
    }

    @Override // java.util.Dictionary
    public Object put(Object obj, Object obj2) {
        if (obj2 == null) {
            throw new NullPointerException();
        }
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int iIdentityHashCode = System.identityHashCode(obj);
        int length = (iIdentityHashCode & Integer.MAX_VALUE) % identityHashtableEntryArr.length;
        IdentityHashtableEntry identityHashtableEntry = identityHashtableEntryArr[length];
        while (true) {
            IdentityHashtableEntry identityHashtableEntry2 = identityHashtableEntry;
            if (identityHashtableEntry2 != null) {
                if (identityHashtableEntry2.hash != iIdentityHashCode || identityHashtableEntry2.key != obj) {
                    identityHashtableEntry = identityHashtableEntry2.next;
                } else {
                    Object obj3 = identityHashtableEntry2.value;
                    identityHashtableEntry2.value = obj2;
                    return obj3;
                }
            } else {
                if (this.count >= this.threshold) {
                    rehash();
                    return put(obj, obj2);
                }
                IdentityHashtableEntry identityHashtableEntry3 = new IdentityHashtableEntry();
                identityHashtableEntry3.hash = iIdentityHashCode;
                identityHashtableEntry3.key = obj;
                identityHashtableEntry3.value = obj2;
                identityHashtableEntry3.next = identityHashtableEntryArr[length];
                identityHashtableEntryArr[length] = identityHashtableEntry3;
                this.count++;
                return null;
            }
        }
    }

    @Override // java.util.Dictionary
    public Object remove(Object obj) {
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int iIdentityHashCode = System.identityHashCode(obj);
        int length = (iIdentityHashCode & Integer.MAX_VALUE) % identityHashtableEntryArr.length;
        IdentityHashtableEntry identityHashtableEntry = null;
        for (IdentityHashtableEntry identityHashtableEntry2 = identityHashtableEntryArr[length]; identityHashtableEntry2 != null; identityHashtableEntry2 = identityHashtableEntry2.next) {
            if (identityHashtableEntry2.hash != iIdentityHashCode || identityHashtableEntry2.key != obj) {
                identityHashtableEntry = identityHashtableEntry2;
            } else {
                if (identityHashtableEntry != null) {
                    identityHashtableEntry.next = identityHashtableEntry2.next;
                } else {
                    identityHashtableEntryArr[length] = identityHashtableEntry2.next;
                }
                this.count--;
                return identityHashtableEntry2.value;
            }
        }
        return null;
    }

    public void clear() {
        IdentityHashtableEntry[] identityHashtableEntryArr = this.table;
        int length = identityHashtableEntryArr.length;
        while (true) {
            length--;
            if (length >= 0) {
                identityHashtableEntryArr[length] = null;
            } else {
                this.count = 0;
                return;
            }
        }
    }

    public String toString() {
        int size = size() - 1;
        StringBuffer stringBuffer = new StringBuffer();
        Enumeration enumerationKeys = keys();
        Enumeration enumerationElements = elements();
        stringBuffer.append(VectorFormat.DEFAULT_PREFIX);
        for (int i2 = 0; i2 <= size; i2++) {
            stringBuffer.append(enumerationKeys.nextElement2().toString() + "=" + enumerationElements.nextElement2().toString());
            if (i2 < size) {
                stringBuffer.append(", ");
            }
        }
        stringBuffer.append("}");
        return stringBuffer.toString();
    }
}
