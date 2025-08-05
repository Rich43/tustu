package com.sun.java.util.jar.pack;

import com.sun.java.util.jar.pack.ConstantPool;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

/* loaded from: rt.jar:com/sun/java/util/jar/pack/Fixups.class */
final class Fixups extends AbstractCollection<Fixup> {
    byte[] bytes;
    int head;
    int tail;
    int size;
    ConstantPool.Entry[] entries;
    int[] bigDescs;
    private static final int MINBIGSIZE = 1;
    private static final int[] noBigDescs;
    private static final int LOC_SHIFT = 1;
    private static final int FMT_MASK = 1;
    private static final byte UNUSED_BYTE = 0;
    private static final byte OVERFLOW_BYTE = -1;
    private static final int BIGSIZE = 0;
    private static final int U2_FORMAT = 0;
    private static final int U1_FORMAT = 1;
    private static final int SPECIAL_LOC = 0;
    private static final int SPECIAL_FMT = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Fixups.class.desiredAssertionStatus();
        noBigDescs = new int[]{1};
    }

    Fixups(byte[] bArr) {
        this.bytes = bArr;
        this.entries = new ConstantPool.Entry[3];
        this.bigDescs = noBigDescs;
    }

    Fixups() {
        this((byte[]) null);
    }

    Fixups(byte[] bArr, Collection<Fixup> collection) {
        this(bArr);
        addAll(collection);
    }

    Fixups(Collection<Fixup> collection) {
        this((byte[]) null);
        addAll(collection);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
    public int size() {
        return this.size;
    }

    public void trimToSize() {
        if (this.size != this.entries.length) {
            ConstantPool.Entry[] entryArr = this.entries;
            this.entries = new ConstantPool.Entry[this.size];
            System.arraycopy(entryArr, 0, this.entries, 0, this.size);
        }
        int i2 = this.bigDescs[0];
        if (i2 == 1) {
            this.bigDescs = noBigDescs;
        } else if (i2 != this.bigDescs.length) {
            int[] iArr = this.bigDescs;
            this.bigDescs = new int[i2];
            System.arraycopy(iArr, 0, this.bigDescs, 0, i2);
        }
    }

    public void visitRefs(Collection<ConstantPool.Entry> collection) {
        for (int i2 = 0; i2 < this.size; i2++) {
            collection.add(this.entries[i2]);
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public void clear() {
        if (this.bytes != null) {
            Iterator<Fixup> it = iterator();
            while (it.hasNext()) {
                Fixup next = it.next();
                storeIndex(next.location(), next.format(), 0);
            }
        }
        this.size = 0;
        if (this.bigDescs != noBigDescs) {
            this.bigDescs[0] = 1;
        }
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bArr) {
        if (this.bytes == bArr) {
            return;
        }
        ArrayList arrayList = null;
        if (!$assertionsDisabled) {
            ArrayList arrayList2 = new ArrayList(this);
            arrayList = arrayList2;
            if (arrayList2 == null) {
                throw new AssertionError();
            }
        }
        if (this.bytes == null || bArr == null) {
            ArrayList arrayList3 = new ArrayList(this);
            clear();
            this.bytes = bArr;
            addAll(arrayList3);
        } else {
            this.bytes = bArr;
        }
        if (!$assertionsDisabled && !arrayList.equals(new ArrayList(this))) {
            throw new AssertionError();
        }
    }

    static int fmtLen(int i2) {
        return 1 + ((i2 - 1) / (-1));
    }

    static int descLoc(int i2) {
        return i2 >>> 1;
    }

    static int descFmt(int i2) {
        return i2 & 1;
    }

    static int descEnd(int i2) {
        return descLoc(i2) + fmtLen(descFmt(i2));
    }

    static int makeDesc(int i2, int i3) {
        int i4 = (i2 << 1) | i3;
        if (!$assertionsDisabled && descLoc(i4) != i2) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || descFmt(i4) == i3) {
            return i4;
        }
        throw new AssertionError();
    }

    int fetchDesc(int i2, int i3) {
        int i4;
        byte b2 = this.bytes[i2];
        if (!$assertionsDisabled && b2 == -1) {
            throw new AssertionError();
        }
        if (i3 == 0) {
            i4 = ((b2 & 255) << 8) + (this.bytes[i2 + 1] & 255);
        } else {
            i4 = b2 & 255;
        }
        return i4 + (i2 << 1);
    }

    boolean storeDesc(int i2, int i3, int i4) {
        if (this.bytes == null) {
            return false;
        }
        int i5 = i4 - (i2 << 1);
        switch (i3) {
            case 0:
                if (!$assertionsDisabled && this.bytes[i2 + 0] != 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && this.bytes[i2 + 1] != 0) {
                    throw new AssertionError();
                }
                byte b2 = (byte) (i5 >> 8);
                byte b3 = (byte) (i5 >> 0);
                if (i5 == (i5 & 65535) && b2 != -1) {
                    this.bytes[i2 + 0] = b2;
                    this.bytes[i2 + 1] = b3;
                    if ($assertionsDisabled || fetchDesc(i2, i3) == i4) {
                        return true;
                    }
                    throw new AssertionError();
                }
                break;
            case 1:
                if (!$assertionsDisabled && this.bytes[i2] != 0) {
                    throw new AssertionError();
                }
                byte b4 = (byte) i5;
                if (i5 == (i5 & 255) && b4 != -1) {
                    this.bytes[i2] = b4;
                    if ($assertionsDisabled || fetchDesc(i2, i3) == i4) {
                        return true;
                    }
                    throw new AssertionError();
                }
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                break;
        }
        this.bytes[i2] = -1;
        if ($assertionsDisabled || i3 == 1) {
            return false;
        }
        byte b5 = (byte) this.bigDescs[0];
        this.bytes[i2 + 1] = b5;
        if (b5 == 999) {
            throw new AssertionError();
        }
        return false;
    }

    void storeIndex(int i2, int i3, int i4) {
        storeIndex(this.bytes, i2, i3, i4);
    }

    static void storeIndex(byte[] bArr, int i2, int i3, int i4) {
        switch (i3) {
            case 0:
                if (!$assertionsDisabled && i4 != (i4 & 65535)) {
                    throw new AssertionError(i4);
                }
                bArr[i2 + 0] = (byte) (i4 >> 8);
                bArr[i2 + 1] = (byte) (i4 >> 0);
                return;
            case 1:
                if (!$assertionsDisabled && i4 != (i4 & 255)) {
                    throw new AssertionError(i4);
                }
                bArr[i2] = (byte) i4;
                return;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
    }

    void addU1(int i2, ConstantPool.Entry entry) {
        add(i2, 1, entry);
    }

    void addU2(int i2, ConstantPool.Entry entry) {
        add(i2, 0, entry);
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Fixups$Fixup.class */
    public static class Fixup implements Comparable<Fixup> {
        int desc;
        ConstantPool.Entry entry;

        Fixup(int i2, ConstantPool.Entry entry) {
            this.desc = i2;
            this.entry = entry;
        }

        public Fixup(int i2, int i3, ConstantPool.Entry entry) {
            this.desc = Fixups.makeDesc(i2, i3);
            this.entry = entry;
        }

        public int location() {
            return Fixups.descLoc(this.desc);
        }

        public int format() {
            return Fixups.descFmt(this.desc);
        }

        public ConstantPool.Entry entry() {
            return this.entry;
        }

        @Override // java.lang.Comparable
        public int compareTo(Fixup fixup) {
            return location() - fixup.location();
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof Fixup)) {
                return false;
            }
            Fixup fixup = (Fixup) obj;
            return this.desc == fixup.desc && this.entry == fixup.entry;
        }

        public int hashCode() {
            return (59 * ((59 * 7) + this.desc)) + Objects.hashCode(this.entry);
        }

        public String toString() {
            return "@" + location() + (format() == 1 ? ".1" : "") + "=" + ((Object) this.entry);
        }
    }

    /* loaded from: rt.jar:com/sun/java/util/jar/pack/Fixups$Itr.class */
    private class Itr implements Iterator<Fixup> {
        int index;
        int bigIndex;
        int next;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Fixups.class.desiredAssertionStatus();
        }

        private Itr() {
            this.index = 0;
            this.bigIndex = 1;
            this.next = Fixups.this.head;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.index < Fixups.this.size;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Fixup next() {
            return new Fixup(nextDesc(), Fixups.this.entries[this.index]);
        }

        int nextDesc() {
            this.index++;
            int i2 = this.next;
            if (this.index < Fixups.this.size) {
                int iDescLoc = Fixups.descLoc(i2);
                int iDescFmt = Fixups.descFmt(i2);
                if (Fixups.this.bytes != null && Fixups.this.bytes[iDescLoc] != -1) {
                    this.next = Fixups.this.fetchDesc(iDescLoc, iDescFmt);
                } else {
                    if (!$assertionsDisabled && iDescFmt != 1 && Fixups.this.bytes != null && Fixups.this.bytes[iDescLoc + 1] != ((byte) this.bigIndex)) {
                        throw new AssertionError();
                    }
                    int[] iArr = Fixups.this.bigDescs;
                    int i3 = this.bigIndex;
                    this.bigIndex = i3 + 1;
                    this.next = iArr[i3];
                }
            }
            return i2;
        }
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.lang.Iterable, java.util.List
    public Iterator<Fixup> iterator() {
        return new Itr();
    }

    public void add(int i2, int i3, ConstantPool.Entry entry) {
        addDesc(makeDesc(i2, i3), entry);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public boolean add(Fixup fixup) {
        addDesc(fixup.desc, fixup.entry);
        return true;
    }

    @Override // java.util.AbstractCollection, java.util.Collection
    public boolean addAll(Collection<? extends Fixup> collection) {
        if (collection instanceof Fixups) {
            Fixups fixups = (Fixups) collection;
            if (fixups.size == 0) {
                return false;
            }
            if (this.size == 0 && this.entries.length < fixups.size) {
                growEntries(fixups.size);
            }
            ConstantPool.Entry[] entryArr = fixups.entries;
            fixups.getClass();
            Itr itr = new Itr();
            while (itr.hasNext()) {
                addDesc(itr.nextDesc(), entryArr[itr.index]);
            }
            return true;
        }
        return super.addAll(collection);
    }

    private void addDesc(int i2, ConstantPool.Entry entry) {
        if (this.entries.length == this.size) {
            growEntries(this.size * 2);
        }
        this.entries[this.size] = entry;
        if (this.size == 0) {
            this.tail = i2;
            this.head = i2;
        } else {
            int i3 = this.tail;
            int iDescLoc = descLoc(i3);
            int iDescFmt = descFmt(i3);
            int iFmtLen = fmtLen(iDescFmt);
            int iDescLoc2 = descLoc(i2);
            if (iDescLoc2 < iDescLoc + iFmtLen) {
                badOverlap(iDescLoc2);
            }
            this.tail = i2;
            if (!storeDesc(iDescLoc, iDescFmt, i2)) {
                int i4 = this.bigDescs[0];
                if (this.bigDescs.length == i4) {
                    growBigDescs();
                }
                this.bigDescs[i4] = i2;
                this.bigDescs[0] = i4 + 1;
            }
        }
        this.size++;
    }

    private void badOverlap(int i2) {
        throw new IllegalArgumentException("locs must be ascending and must not overlap:  " + i2 + " >> " + ((Object) this));
    }

    private void growEntries(int i2) {
        ConstantPool.Entry[] entryArr = this.entries;
        this.entries = new ConstantPool.Entry[Math.max(3, i2)];
        System.arraycopy(entryArr, 0, this.entries, 0, entryArr.length);
    }

    private void growBigDescs() {
        int[] iArr = this.bigDescs;
        this.bigDescs = new int[iArr.length * 2];
        System.arraycopy(iArr, 0, this.bigDescs, 0, iArr.length);
    }

    static Object addRefWithBytes(Object obj, byte[] bArr, ConstantPool.Entry entry) {
        return add(obj, bArr, 0, 0, entry);
    }

    static Object addRefWithLoc(Object obj, int i2, ConstantPool.Entry entry) {
        return add(obj, null, i2, 0, entry);
    }

    private static Object add(Object obj, byte[] bArr, int i2, int i3, ConstantPool.Entry entry) {
        Fixups fixups;
        if (obj == null) {
            if (i2 == 0 && i3 == 0) {
                return entry;
            }
            fixups = new Fixups(bArr);
        } else if (!(obj instanceof Fixups)) {
            fixups = new Fixups(bArr);
            fixups.add(0, 0, (ConstantPool.Entry) obj);
        } else {
            fixups = (Fixups) obj;
            if (!$assertionsDisabled && fixups.bytes != bArr) {
                throw new AssertionError();
            }
        }
        fixups.add(i2, i3, entry);
        return fixups;
    }

    public static void setBytes(Object obj, byte[] bArr) {
        if (obj instanceof Fixups) {
            ((Fixups) obj).setBytes(bArr);
        }
    }

    public static Object trimToSize(Object obj) {
        if (obj instanceof Fixups) {
            Fixups fixups = (Fixups) obj;
            fixups.trimToSize();
            if (fixups.size() == 0) {
                obj = null;
            }
        }
        return obj;
    }

    public static void visitRefs(Object obj, Collection<ConstantPool.Entry> collection) {
        if (obj != null) {
            if (!(obj instanceof Fixups)) {
                collection.add((ConstantPool.Entry) obj);
            } else {
                ((Fixups) obj).visitRefs(collection);
            }
        }
    }

    public static void finishRefs(Object obj, byte[] bArr, ConstantPool.Index index) {
        if (obj == null) {
            return;
        }
        if (!(obj instanceof Fixups)) {
            storeIndex(bArr, 0, 0, index.indexOf((ConstantPool.Entry) obj));
            return;
        }
        Fixups fixups = (Fixups) obj;
        if (!$assertionsDisabled && fixups.bytes != bArr) {
            throw new AssertionError();
        }
        fixups.finishRefs(index);
    }

    void finishRefs(ConstantPool.Index index) {
        if (isEmpty()) {
            return;
        }
        Iterator<Fixup> it = iterator();
        while (it.hasNext()) {
            Fixup next = it.next();
            storeIndex(next.location(), next.format(), index.indexOf(next.entry));
        }
        this.bytes = null;
        clear();
    }
}
