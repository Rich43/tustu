package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/SlotAllocator.class */
final class SlotAllocator {
    private int _firstAvailableSlot;
    private int _size = 8;
    private int _free = 0;
    private int[] _slotsTaken = new int[this._size];

    SlotAllocator() {
    }

    public void initialize(LocalVariableGen[] vars) {
        int length = vars.length;
        int slot = 0;
        for (int i2 = 0; i2 < length; i2++) {
            int size = vars[i2].getType().getSize();
            int index = vars[i2].getIndex();
            slot = Math.max(slot, index + size);
        }
        this._firstAvailableSlot = slot;
    }

    public int allocateSlot(com.sun.org.apache.bcel.internal.generic.Type type) {
        int size = type.getSize();
        int limit = this._free;
        int slot = this._firstAvailableSlot;
        int where = 0;
        if (this._free + size > this._size) {
            int i2 = this._size * 2;
            this._size = i2;
            int[] array = new int[i2];
            for (int j2 = 0; j2 < limit; j2++) {
                array[j2] = this._slotsTaken[j2];
            }
            this._slotsTaken = array;
        }
        while (true) {
            if (where >= limit) {
                break;
            }
            if (slot + size <= this._slotsTaken[where]) {
                for (int j3 = limit - 1; j3 >= where; j3--) {
                    this._slotsTaken[j3 + size] = this._slotsTaken[j3];
                }
            } else {
                int i3 = where;
                where++;
                slot = this._slotsTaken[i3] + 1;
            }
        }
        for (int j4 = 0; j4 < size; j4++) {
            this._slotsTaken[where + j4] = slot + j4;
        }
        this._free += size;
        return slot;
    }

    public void releaseSlot(LocalVariableGen lvg) {
        int size = lvg.getType().getSize();
        int slot = lvg.getIndex();
        int limit = this._free;
        int i2 = 0;
        while (i2 < limit) {
            if (this._slotsTaken[i2] != slot) {
                i2++;
            } else {
                int j2 = i2 + size;
                while (j2 < limit) {
                    int i3 = i2;
                    i2++;
                    int i4 = j2;
                    j2++;
                    this._slotsTaken[i3] = this._slotsTaken[i4];
                }
                this._free -= size;
                return;
            }
        }
        String state = "Variable slot allocation error(size=" + size + ", slot=" + slot + ", limit=" + limit + ")";
        ErrorMsg err = new ErrorMsg(ErrorMsg.INTERNAL_ERR, state);
        throw new Error(err.toString());
    }
}
