package com.sun.corba.se.spi.presentation.rmi;

import com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryProxyImpl;
import com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryStaticImpl;
import com.sun.corba.se.impl.presentation.rmi.StubFactoryStaticImpl;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;

/* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/PresentationDefaults.class */
public abstract class PresentationDefaults {
    private static StubFactoryFactoryStaticImpl staticImpl = null;

    private PresentationDefaults() {
    }

    public static synchronized PresentationManager.StubFactoryFactory getStaticStubFactoryFactory() {
        if (staticImpl == null) {
            staticImpl = new StubFactoryFactoryStaticImpl();
        }
        return staticImpl;
    }

    public static PresentationManager.StubFactoryFactory getProxyStubFactoryFactory() {
        return new StubFactoryFactoryProxyImpl();
    }

    public static PresentationManager.StubFactory makeStaticStubFactory(Class cls) {
        return new StubFactoryStaticImpl(cls);
    }
}
