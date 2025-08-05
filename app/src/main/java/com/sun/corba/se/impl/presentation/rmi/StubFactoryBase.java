package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryBase.class */
public abstract class StubFactoryBase implements PresentationManager.StubFactory {
    private String[] typeIds = null;
    protected final PresentationManager.ClassData classData;

    protected StubFactoryBase(PresentationManager.ClassData classData) {
        this.classData = classData;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory
    public synchronized String[] getTypeIds() {
        if (this.typeIds == null) {
            if (this.classData == null) {
                this.typeIds = StubAdapter.getTypeIds(makeStub());
            } else {
                this.typeIds = this.classData.getTypeIds();
            }
        }
        return this.typeIds;
    }
}
