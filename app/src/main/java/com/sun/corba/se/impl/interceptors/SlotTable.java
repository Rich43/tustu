package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.corba.AnyImpl;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.PortableInterceptor.InvalidSlot;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/SlotTable.class */
public class SlotTable {
    private Any[] theSlotData;
    private ORB orb;
    private boolean dirtyFlag = false;

    SlotTable(ORB orb, int i2) {
        this.orb = orb;
        this.theSlotData = new Any[i2];
    }

    public void set_slot(int i2, Any any) throws InvalidSlot {
        if (i2 >= this.theSlotData.length) {
            throw new InvalidSlot();
        }
        this.dirtyFlag = true;
        this.theSlotData[i2] = any;
    }

    public Any get_slot(int i2) throws InvalidSlot {
        if (i2 >= this.theSlotData.length) {
            throw new InvalidSlot();
        }
        if (this.theSlotData[i2] == null) {
            this.theSlotData[i2] = new AnyImpl(this.orb);
        }
        return this.theSlotData[i2];
    }

    void resetSlots() {
        if (this.dirtyFlag) {
            for (int i2 = 0; i2 < this.theSlotData.length; i2++) {
                this.theSlotData[i2] = null;
            }
        }
    }

    int getSize() {
        return this.theSlotData.length;
    }
}
