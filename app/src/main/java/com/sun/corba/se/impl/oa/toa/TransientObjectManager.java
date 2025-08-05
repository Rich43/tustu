package com.sun.corba.se.impl.oa.toa;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.orb.ORB;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/toa/TransientObjectManager.class */
public final class TransientObjectManager {
    private ORB orb;
    private int maxSize = 128;
    private Element[] elementArray = new Element[this.maxSize];
    private Element freeList;

    void dprint(String str) {
        ORBUtility.dprint(this, str);
    }

    public TransientObjectManager(ORB orb) {
        this.orb = orb;
        this.elementArray[this.maxSize - 1] = new Element(this.maxSize - 1, null);
        for (int i2 = this.maxSize - 2; i2 >= 0; i2--) {
            this.elementArray[i2] = new Element(i2, this.elementArray[i2 + 1]);
        }
        this.freeList = this.elementArray[0];
    }

    public synchronized byte[] storeServant(Object obj, Object obj2) {
        if (this.freeList == null) {
            doubleSize();
        }
        Element element = this.freeList;
        this.freeList = (Element) this.freeList.servant;
        byte[] key = element.getKey(obj, obj2);
        if (this.orb.transientObjectManagerDebugFlag) {
            dprint("storeServant returns key for element " + ((Object) element));
        }
        return key;
    }

    public synchronized Object lookupServant(byte[] bArr) {
        int iBytesToInt = ORBUtility.bytesToInt(bArr, 0);
        int iBytesToInt2 = ORBUtility.bytesToInt(bArr, 4);
        if (this.orb.transientObjectManagerDebugFlag) {
            dprint("lookupServant called with index=" + iBytesToInt + ", counter=" + iBytesToInt2);
        }
        if (this.elementArray[iBytesToInt].counter == iBytesToInt2 && this.elementArray[iBytesToInt].valid) {
            if (this.orb.transientObjectManagerDebugFlag) {
                dprint("\tcounter is valid");
            }
            return this.elementArray[iBytesToInt].servant;
        }
        if (this.orb.transientObjectManagerDebugFlag) {
            dprint("\tcounter is invalid");
            return null;
        }
        return null;
    }

    public synchronized Object lookupServantData(byte[] bArr) {
        int iBytesToInt = ORBUtility.bytesToInt(bArr, 0);
        int iBytesToInt2 = ORBUtility.bytesToInt(bArr, 4);
        if (this.orb.transientObjectManagerDebugFlag) {
            dprint("lookupServantData called with index=" + iBytesToInt + ", counter=" + iBytesToInt2);
        }
        if (this.elementArray[iBytesToInt].counter == iBytesToInt2 && this.elementArray[iBytesToInt].valid) {
            if (this.orb.transientObjectManagerDebugFlag) {
                dprint("\tcounter is valid");
            }
            return this.elementArray[iBytesToInt].servantData;
        }
        if (this.orb.transientObjectManagerDebugFlag) {
            dprint("\tcounter is invalid");
            return null;
        }
        return null;
    }

    public synchronized void deleteServant(byte[] bArr) {
        int iBytesToInt = ORBUtility.bytesToInt(bArr, 0);
        if (this.orb.transientObjectManagerDebugFlag) {
            dprint("deleting servant at index=" + iBytesToInt);
        }
        this.elementArray[iBytesToInt].delete(this.freeList);
        this.freeList = this.elementArray[iBytesToInt];
    }

    public synchronized byte[] getKey(Object obj) {
        for (int i2 = 0; i2 < this.maxSize; i2++) {
            if (this.elementArray[i2].valid && this.elementArray[i2].servant == obj) {
                return this.elementArray[i2].toBytes();
            }
        }
        return null;
    }

    private void doubleSize() {
        Element[] elementArr = this.elementArray;
        int i2 = this.maxSize;
        this.maxSize *= 2;
        this.elementArray = new Element[this.maxSize];
        for (int i3 = 0; i3 < i2; i3++) {
            this.elementArray[i3] = elementArr[i3];
        }
        this.elementArray[this.maxSize - 1] = new Element(this.maxSize - 1, null);
        for (int i4 = this.maxSize - 2; i4 >= i2; i4--) {
            this.elementArray[i4] = new Element(i4, this.elementArray[i4 + 1]);
        }
        this.freeList = this.elementArray[i2];
    }
}
