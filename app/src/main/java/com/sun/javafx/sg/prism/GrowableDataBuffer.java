package com.sun.javafx.sg.prism;

import java.lang.ref.WeakReference;
import java.nio.BufferOverflowException;
import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/GrowableDataBuffer.class */
public class GrowableDataBuffer {
    static final int VAL_GROW_QUANTUM = 1024;
    static final int MAX_VAL_GROW = 1048576;
    static final int MIN_OBJ_GROW = 32;
    static WeakLink buflist = new WeakLink();
    byte[] vals;
    int writevalpos;
    int readvalpos;
    int savevalpos;
    Object[] objs;
    int writeobjpos;
    int readobjpos;
    int saveobjpos;

    /* loaded from: jfxrt.jar:com/sun/javafx/sg/prism/GrowableDataBuffer$WeakLink.class */
    static class WeakLink {
        WeakReference<GrowableDataBuffer> bufref;
        WeakLink next;

        WeakLink() {
        }
    }

    public static GrowableDataBuffer getBuffer(int minsize) {
        return getBuffer(minsize, 32);
    }

    public static synchronized GrowableDataBuffer getBuffer(int minvals, int minobjs) {
        WeakLink prev = buflist;
        WeakLink cur = buflist.next;
        while (cur != null) {
            GrowableDataBuffer curgdb = cur.bufref.get();
            WeakLink next = cur.next;
            if (curgdb == null) {
                cur = next;
                prev.next = next;
            } else {
                if (curgdb.valueCapacity() >= minvals && curgdb.objectCapacity() >= minobjs) {
                    prev.next = next;
                    return curgdb;
                }
                prev = cur;
                cur = next;
            }
        }
        return new GrowableDataBuffer(minvals, minobjs);
    }

    public static synchronized void returnBuffer(GrowableDataBuffer retgdb) {
        int retvlen = retgdb.valueCapacity();
        int retolen = retgdb.objectCapacity();
        retgdb.reset();
        WeakLink prev = buflist;
        WeakLink cur = buflist.next;
        while (cur != null) {
            GrowableDataBuffer curgdb = cur.bufref.get();
            WeakLink next = cur.next;
            if (curgdb == null) {
                cur = next;
                prev.next = next;
            } else {
                int curvlen = curgdb.valueCapacity();
                int curolen = curgdb.objectCapacity();
                if (curvlen > retvlen || (curvlen == retvlen && curolen >= retolen)) {
                    break;
                }
                prev = cur;
                cur = next;
            }
        }
        WeakLink retlink = new WeakLink();
        retlink.bufref = new WeakReference<>(retgdb);
        prev.next = retlink;
        retlink.next = cur;
    }

    private GrowableDataBuffer(int initvalsize, int initobjsize) {
        this.vals = new byte[initvalsize];
        this.objs = new Object[initobjsize];
    }

    public int readValuePosition() {
        return this.readvalpos;
    }

    public int writeValuePosition() {
        return this.writevalpos;
    }

    public int readObjectPosition() {
        return this.readobjpos;
    }

    public int writeObjectPosition() {
        return this.writeobjpos;
    }

    public int valueCapacity() {
        return this.vals.length;
    }

    public int objectCapacity() {
        return this.objs.length;
    }

    public void save() {
        this.savevalpos = this.readvalpos;
        this.saveobjpos = this.readobjpos;
    }

    public void restore() {
        this.readvalpos = this.savevalpos;
        this.readobjpos = this.saveobjpos;
    }

    public boolean hasValues() {
        return this.readvalpos < this.writevalpos;
    }

    public boolean hasObjects() {
        return this.readobjpos < this.writeobjpos;
    }

    public boolean isEmpty() {
        return this.writevalpos == 0;
    }

    public void reset() {
        this.writevalpos = 0;
        this.savevalpos = 0;
        this.readvalpos = 0;
        this.saveobjpos = 0;
        this.readobjpos = 0;
        if (this.writeobjpos > 0) {
            Arrays.fill(this.objs, 0, this.writeobjpos, (Object) null);
            this.writeobjpos = 0;
        }
    }

    public void append(GrowableDataBuffer gdb) {
        ensureWriteCapacity(gdb.writevalpos);
        System.arraycopy(gdb.vals, 0, this.vals, this.writevalpos, gdb.writevalpos);
        this.writevalpos += gdb.writevalpos;
        if (this.writeobjpos + gdb.writeobjpos > this.objs.length) {
            this.objs = Arrays.copyOf(this.objs, this.writeobjpos + gdb.writeobjpos);
        }
        System.arraycopy(gdb.objs, 0, this.objs, this.writeobjpos, gdb.writeobjpos);
        this.writeobjpos += gdb.writeobjpos;
    }

    private void ensureWriteCapacity(int newbytes) {
        if (newbytes > this.vals.length - this.writevalpos) {
            int newbytes2 = (this.writevalpos + newbytes) - this.vals.length;
            int growbytes = Math.min(this.vals.length, 1048576);
            if (growbytes < newbytes2) {
                growbytes = newbytes2;
            }
            int newsize = this.vals.length + growbytes;
            this.vals = Arrays.copyOf(this.vals, (newsize + 1023) & (-1024));
        }
    }

    private void ensureReadCapacity(int bytesneeded) {
        if (this.readvalpos + bytesneeded > this.writevalpos) {
            throw new BufferOverflowException();
        }
    }

    public void putBoolean(boolean b2) {
        putByte(b2 ? (byte) 1 : (byte) 0);
    }

    public void putByte(byte b2) {
        ensureWriteCapacity(1);
        byte[] bArr = this.vals;
        int i2 = this.writevalpos;
        this.writevalpos = i2 + 1;
        bArr[i2] = b2;
    }

    public void putChar(char c2) {
        ensureWriteCapacity(2);
        byte[] bArr = this.vals;
        int i2 = this.writevalpos;
        this.writevalpos = i2 + 1;
        bArr[i2] = (byte) (c2 >> '\b');
        byte[] bArr2 = this.vals;
        int i3 = this.writevalpos;
        this.writevalpos = i3 + 1;
        bArr2[i3] = (byte) c2;
    }

    public void putShort(short s2) {
        ensureWriteCapacity(2);
        byte[] bArr = this.vals;
        int i2 = this.writevalpos;
        this.writevalpos = i2 + 1;
        bArr[i2] = (byte) (s2 >> 8);
        byte[] bArr2 = this.vals;
        int i3 = this.writevalpos;
        this.writevalpos = i3 + 1;
        bArr2[i3] = (byte) s2;
    }

    public void putInt(int i2) {
        ensureWriteCapacity(4);
        byte[] bArr = this.vals;
        int i3 = this.writevalpos;
        this.writevalpos = i3 + 1;
        bArr[i3] = (byte) (i2 >> 24);
        byte[] bArr2 = this.vals;
        int i4 = this.writevalpos;
        this.writevalpos = i4 + 1;
        bArr2[i4] = (byte) (i2 >> 16);
        byte[] bArr3 = this.vals;
        int i5 = this.writevalpos;
        this.writevalpos = i5 + 1;
        bArr3[i5] = (byte) (i2 >> 8);
        byte[] bArr4 = this.vals;
        int i6 = this.writevalpos;
        this.writevalpos = i6 + 1;
        bArr4[i6] = (byte) i2;
    }

    public void putLong(long l2) {
        ensureWriteCapacity(8);
        byte[] bArr = this.vals;
        int i2 = this.writevalpos;
        this.writevalpos = i2 + 1;
        bArr[i2] = (byte) (l2 >> 56);
        byte[] bArr2 = this.vals;
        int i3 = this.writevalpos;
        this.writevalpos = i3 + 1;
        bArr2[i3] = (byte) (l2 >> 48);
        byte[] bArr3 = this.vals;
        int i4 = this.writevalpos;
        this.writevalpos = i4 + 1;
        bArr3[i4] = (byte) (l2 >> 40);
        byte[] bArr4 = this.vals;
        int i5 = this.writevalpos;
        this.writevalpos = i5 + 1;
        bArr4[i5] = (byte) (l2 >> 32);
        byte[] bArr5 = this.vals;
        int i6 = this.writevalpos;
        this.writevalpos = i6 + 1;
        bArr5[i6] = (byte) (l2 >> 24);
        byte[] bArr6 = this.vals;
        int i7 = this.writevalpos;
        this.writevalpos = i7 + 1;
        bArr6[i7] = (byte) (l2 >> 16);
        byte[] bArr7 = this.vals;
        int i8 = this.writevalpos;
        this.writevalpos = i8 + 1;
        bArr7[i8] = (byte) (l2 >> 8);
        byte[] bArr8 = this.vals;
        int i9 = this.writevalpos;
        this.writevalpos = i9 + 1;
        bArr8[i9] = (byte) l2;
    }

    public void putFloat(float f2) {
        putInt(Float.floatToIntBits(f2));
    }

    public void putDouble(double d2) {
        putLong(Double.doubleToLongBits(d2));
    }

    public void putObject(Object o2) {
        if (this.writeobjpos >= this.objs.length) {
            this.objs = Arrays.copyOf(this.objs, this.writeobjpos + 32);
        }
        Object[] objArr = this.objs;
        int i2 = this.writeobjpos;
        this.writeobjpos = i2 + 1;
        objArr[i2] = o2;
    }

    public byte peekByte(int i2) {
        if (i2 >= this.writevalpos) {
            throw new BufferOverflowException();
        }
        return this.vals[i2];
    }

    public Object peekObject(int i2) {
        if (i2 >= this.writeobjpos) {
            throw new BufferOverflowException();
        }
        return this.objs[i2];
    }

    public boolean getBoolean() {
        ensureReadCapacity(1);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        return bArr[i2] != 0;
    }

    public byte getByte() {
        ensureReadCapacity(1);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        return bArr[i2];
    }

    public int getUByte() {
        ensureReadCapacity(1);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        return bArr[i2] & 255;
    }

    public char getChar() {
        ensureReadCapacity(2);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        int i3 = bArr[i2] << 8;
        byte[] bArr2 = this.vals;
        int i4 = this.readvalpos;
        this.readvalpos = i4 + 1;
        int c2 = i3 | (bArr2[i4] & 255);
        return (char) c2;
    }

    public short getShort() {
        ensureReadCapacity(2);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        int i3 = bArr[i2] << 8;
        byte[] bArr2 = this.vals;
        int i4 = this.readvalpos;
        this.readvalpos = i4 + 1;
        int s2 = i3 | (bArr2[i4] & 255);
        return (short) s2;
    }

    public int getInt() {
        ensureReadCapacity(4);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        int i3 = bArr[i2] << 8;
        byte[] bArr2 = this.vals;
        int i4 = this.readvalpos;
        this.readvalpos = i4 + 1;
        int i5 = i3 | (bArr2[i4] & 255);
        byte[] bArr3 = this.vals;
        int i6 = this.readvalpos;
        this.readvalpos = i6 + 1;
        int i7 = (i5 << 8) | (bArr3[i6] & 255);
        byte[] bArr4 = this.vals;
        int i8 = this.readvalpos;
        this.readvalpos = i8 + 1;
        return (i7 << 8) | (bArr4[i8] & 255);
    }

    public long getLong() {
        ensureReadCapacity(8);
        byte[] bArr = this.vals;
        int i2 = this.readvalpos;
        this.readvalpos = i2 + 1;
        long l2 = bArr[i2];
        byte[] bArr2 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        long l3 = (l2 << 8) | (bArr2[r3] & 255);
        byte[] bArr3 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        long l4 = (l3 << 8) | (bArr3[r3] & 255);
        byte[] bArr4 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        long l5 = (l4 << 8) | (bArr4[r3] & 255);
        byte[] bArr5 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        long l6 = (l5 << 8) | (bArr5[r3] & 255);
        byte[] bArr6 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        long l7 = (l6 << 8) | (bArr6[r3] & 255);
        byte[] bArr7 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        long l8 = (l7 << 8) | (bArr7[r3] & 255);
        byte[] bArr8 = this.vals;
        this.readvalpos = this.readvalpos + 1;
        return (l8 << 8) | (bArr8[r3] & 255);
    }

    public float getFloat() {
        return Float.intBitsToFloat(getInt());
    }

    public double getDouble() {
        return Double.longBitsToDouble(getLong());
    }

    public Object getObject() {
        if (this.readobjpos >= this.objs.length) {
            throw new BufferOverflowException();
        }
        Object[] objArr = this.objs;
        int i2 = this.readobjpos;
        this.readobjpos = i2 + 1;
        return objArr[i2];
    }
}
