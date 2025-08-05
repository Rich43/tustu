package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryFactoryBase.class */
public abstract class StubFactoryFactoryBase implements PresentationManager.StubFactoryFactory {
    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public String getStubName(String str) {
        return Utility.stubName(str);
    }
}
