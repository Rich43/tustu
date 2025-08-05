package com.sun.corba.se.impl.interceptors;

import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import java.util.ArrayList;
import java.util.List;

/* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/SlotTableStack.class */
public class SlotTableStack {
    private ORB orb;
    private InterceptorsSystemException wrapper;
    private int currentIndex = 0;
    private List tableContainer = new ArrayList();
    private SlotTablePool tablePool = new SlotTablePool();

    /* loaded from: rt.jar:com/sun/corba/se/impl/interceptors/SlotTableStack$SlotTablePool.class */
    private class SlotTablePool {
        private final int HIGH_WATER_MARK = 5;
        private SlotTable[] pool = new SlotTable[5];
        private int currentIndex = 0;

        SlotTablePool() {
        }

        void putSlotTable(SlotTable slotTable) {
            if (this.currentIndex >= 5) {
                return;
            }
            this.pool[this.currentIndex] = slotTable;
            this.currentIndex++;
        }

        SlotTable getSlotTable() {
            if (this.currentIndex == 0) {
                return null;
            }
            this.currentIndex--;
            return this.pool[this.currentIndex];
        }
    }

    SlotTableStack(ORB orb, SlotTable slotTable) {
        this.orb = orb;
        this.wrapper = InterceptorsSystemException.get(orb, CORBALogDomains.RPC_PROTOCOL);
        this.tableContainer.add(this.currentIndex, slotTable);
        this.currentIndex++;
    }

    void pushSlotTable() {
        SlotTable slotTable = this.tablePool.getSlotTable();
        if (slotTable == null) {
            slotTable = new SlotTable(this.orb, peekSlotTable().getSize());
        }
        if (this.currentIndex == this.tableContainer.size()) {
            this.tableContainer.add(this.currentIndex, slotTable);
        } else {
            if (this.currentIndex > this.tableContainer.size()) {
                throw this.wrapper.slotTableInvariant(new Integer(this.currentIndex), new Integer(this.tableContainer.size()));
            }
            this.tableContainer.set(this.currentIndex, slotTable);
        }
        this.currentIndex++;
    }

    void popSlotTable() {
        if (this.currentIndex <= 1) {
            throw this.wrapper.cantPopOnlyPicurrent();
        }
        this.currentIndex--;
        SlotTable slotTable = (SlotTable) this.tableContainer.get(this.currentIndex);
        this.tableContainer.set(this.currentIndex, null);
        slotTable.resetSlots();
        this.tablePool.putSlotTable(slotTable);
    }

    SlotTable peekSlotTable() {
        return (SlotTable) this.tableContainer.get(this.currentIndex - 1);
    }
}
