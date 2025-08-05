package com.sun.corba.se.impl.presentation.rmi;

import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryStaticImpl.class */
public class StubFactoryStaticImpl extends StubFactoryBase {
    private Class stubClass;

    public StubFactoryStaticImpl(Class cls) {
        super(null);
        this.stubClass = cls;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory
    public Object makeStub() {
        try {
            return (Object) this.stubClass.newInstance();
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2);
        } catch (InstantiationException e3) {
            throw new RuntimeException(e3);
        }
    }
}
