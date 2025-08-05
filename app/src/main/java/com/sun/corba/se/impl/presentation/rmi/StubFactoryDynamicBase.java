package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.io.SerializablePermission;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryDynamicBase.class */
public abstract class StubFactoryDynamicBase extends StubFactoryBase {
    protected final ClassLoader loader;

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory
    public abstract Object makeStub();

    private static Void checkPermission() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new SerializablePermission("enableSubclassImplementation"));
            return null;
        }
        return null;
    }

    private StubFactoryDynamicBase(Void r4, PresentationManager.ClassData classData, ClassLoader classLoader) {
        super(classData);
        if (classLoader == null) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            this.loader = contextClassLoader == null ? ClassLoader.getSystemClassLoader() : contextClassLoader;
        } else {
            this.loader = classLoader;
        }
    }

    public StubFactoryDynamicBase(PresentationManager.ClassData classData, ClassLoader classLoader) {
        this(checkPermission(), classData, classLoader);
    }
}
