package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.spi.orbutil.proxy.CompositeInvocationHandlerImpl;
import com.sun.corba.se.spi.orbutil.proxy.DelegateInvocationHandlerImpl;
import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
import com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler;
import com.sun.corba.se.spi.presentation.rmi.DynamicStub;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/InvocationHandlerFactoryImpl.class */
public class InvocationHandlerFactoryImpl implements InvocationHandlerFactory {
    private final PresentationManager.ClassData classData;
    private final PresentationManager pm;
    private Class[] proxyInterfaces;

    public InvocationHandlerFactoryImpl(PresentationManager presentationManager, PresentationManager.ClassData classData) {
        this.classData = classData;
        this.pm = presentationManager;
        Class[] interfaces = classData.getIDLNameTranslator().getInterfaces();
        this.proxyInterfaces = new Class[interfaces.length + 1];
        for (int i2 = 0; i2 < interfaces.length; i2++) {
            this.proxyInterfaces[i2] = interfaces[i2];
        }
        this.proxyInterfaces[interfaces.length] = DynamicStub.class;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/InvocationHandlerFactoryImpl$CustomCompositeInvocationHandlerImpl.class */
    private class CustomCompositeInvocationHandlerImpl extends CompositeInvocationHandlerImpl implements LinkedInvocationHandler, Serializable {
        private transient DynamicStub stub;

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler
        public void setProxy(Proxy proxy) {
            ((DynamicStubImpl) this.stub).setSelf((DynamicStub) proxy);
        }

        @Override // com.sun.corba.se.spi.orbutil.proxy.LinkedInvocationHandler
        public Proxy getProxy() {
            return (Proxy) ((DynamicStubImpl) this.stub).getSelf();
        }

        public CustomCompositeInvocationHandlerImpl(DynamicStub dynamicStub) {
            this.stub = dynamicStub;
        }

        public Object writeReplace() throws ObjectStreamException {
            return this.stub;
        }
    }

    @Override // com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory
    public InvocationHandler getInvocationHandler() {
        return getInvocationHandler(new DynamicStubImpl(this.classData.getTypeIds()));
    }

    InvocationHandler getInvocationHandler(DynamicStub dynamicStub) {
        final InvocationHandler invocationHandlerCreate = DelegateInvocationHandlerImpl.create(dynamicStub);
        StubInvocationHandlerImpl stubInvocationHandlerImpl = new StubInvocationHandlerImpl(this.pm, this.classData, dynamicStub);
        final CustomCompositeInvocationHandlerImpl customCompositeInvocationHandlerImpl = new CustomCompositeInvocationHandlerImpl(dynamicStub);
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: com.sun.corba.se.impl.presentation.rmi.InvocationHandlerFactoryImpl.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                customCompositeInvocationHandlerImpl.addInvocationHandler(DynamicStub.class, invocationHandlerCreate);
                customCompositeInvocationHandlerImpl.addInvocationHandler(Object.class, invocationHandlerCreate);
                customCompositeInvocationHandlerImpl.addInvocationHandler(Object.class, invocationHandlerCreate);
                return null;
            }
        });
        customCompositeInvocationHandlerImpl.setDefaultHandler(stubInvocationHandlerImpl);
        return customCompositeInvocationHandlerImpl;
    }

    @Override // com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory
    public Class[] getProxyInterfaces() {
        return this.proxyInterfaces;
    }
}
