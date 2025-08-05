package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import org.omg.CORBA.Any;
import org.omg.CORBA.LocalObject;
import org.omg.PortableInterceptor.Current;
import org.omg.PortableInterceptor.InvalidSlot;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/PICurrent.class */
public class PICurrent extends LocalObject implements Current {
    private ORB myORB;
    private OMGSystemException wrapper;
    private ThreadLocal threadLocalSlotTable = new ThreadLocal() { // from class: com.sun.corba.se.impl.interceptors.PICurrent.1
        @Override // java.lang.ThreadLocal
        protected Object initialValue() {
            return new SlotTableStack(PICurrent.this.myORB, new SlotTable(PICurrent.this.myORB, PICurrent.this.slotCounter));
        }
    };
    private boolean orbInitializing = true;
    private int slotCounter = 0;

    PICurrent(ORB orb) {
        this.myORB = orb;
        this.wrapper = OMGSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
    }

    int allocateSlotId() {
        int i2 = this.slotCounter;
        this.slotCounter++;
        return i2;
    }

    SlotTable getSlotTable() {
        return ((SlotTableStack) this.threadLocalSlotTable.get()).peekSlotTable();
    }

    void pushSlotTable() {
        ((SlotTableStack) this.threadLocalSlotTable.get()).pushSlotTable();
    }

    void popSlotTable() {
        ((SlotTableStack) this.threadLocalSlotTable.get()).popSlotTable();
    }

    @Override // org.omg.PortableInterceptor.CurrentOperations
    public void set_slot(int i2, Any any) throws InvalidSlot {
        if (this.orbInitializing) {
            throw this.wrapper.invalidPiCall3();
        }
        getSlotTable().set_slot(i2, any);
    }

    @Override // org.omg.PortableInterceptor.CurrentOperations
    public Any get_slot(int i2) throws InvalidSlot {
        if (this.orbInitializing) {
            throw this.wrapper.invalidPiCall4();
        }
        return getSlotTable().get_slot(i2);
    }

    void resetSlotTable() {
        getSlotTable().resetSlots();
    }

    void setORBInitializing(boolean z2) {
        this.orbInitializing = z2;
    }
}
