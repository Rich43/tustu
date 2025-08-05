package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryFactoryProxyImpl.class */
public class StubFactoryFactoryProxyImpl extends StubFactoryFactoryDynamicBase {
    @Override // com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryDynamicBase
    public PresentationManager.StubFactory makeDynamicStubFactory(PresentationManager presentationManager, final PresentationManager.ClassData classData, final ClassLoader classLoader) {
        return (PresentationManager.StubFactory) AccessController.doPrivileged(new PrivilegedAction<StubFactoryProxyImpl>() { // from class: com.sun.corba.se.impl.presentation.rmi.StubFactoryFactoryProxyImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public StubFactoryProxyImpl run() {
                return new StubFactoryProxyImpl(classData, classLoader);
            }
        });
    }
}
