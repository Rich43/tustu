package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
import com.sun.corba.se.spi.presentation.rmi.DynamicStub;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.lang.reflect.Proxy;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryProxyImpl.class */
public class StubFactoryProxyImpl extends StubFactoryDynamicBase {
    public StubFactoryProxyImpl(PresentationManager.ClassData classData, ClassLoader classLoader) {
        super(classData, classLoader);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.sun.corba.se.impl.presentation.rmi.StubFactoryDynamicBase, com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory
    public Object makeStub() {
        InvocationHandlerFactory invocationHandlerFactory = this.classData.getInvocationHandlerFactory();
        LinkedInvocationHandler linkedInvocationHandler = (LinkedInvocationHandler) invocationHandlerFactory.getInvocationHandler();
        DynamicStub dynamicStub = (DynamicStub) Proxy.newProxyInstance(this.loader, invocationHandlerFactory.getProxyInterfaces(), linkedInvocationHandler);
        linkedInvocationHandler.setProxy((Proxy) dynamicStub);
        return dynamicStub;
    }
}
