package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import java.rmi.Remote;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryFactoryDynamicBase.class */
public abstract class StubFactoryFactoryDynamicBase extends StubFactoryFactoryBase {
    protected final ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);

    public abstract PresentationManager.StubFactory makeDynamicStubFactory(PresentationManager presentationManager, PresentationManager.ClassData classData, ClassLoader classLoader);

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public PresentationManager.StubFactory createStubFactory(String str, boolean z2, String str2, Class cls, ClassLoader classLoader) {
        try {
            Class clsLoadClass = Util.loadClass(str, str2, classLoader);
            PresentationManager presentationManager = ORB.getPresentationManager();
            if (IDLEntity.class.isAssignableFrom(clsLoadClass) && !Remote.class.isAssignableFrom(clsLoadClass)) {
                return presentationManager.getStubFactoryFactory(false).createStubFactory(str, true, str2, cls, classLoader);
            }
            return makeDynamicStubFactory(presentationManager, presentationManager.getClassData(clsLoadClass), classLoader);
        } catch (ClassNotFoundException e2) {
            throw this.wrapper.classNotFound3(CompletionStatus.COMPLETED_MAYBE, e2, str);
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public Tie getTie(Class cls) {
        return new ReflectiveTie(ORB.getPresentationManager(), this.wrapper);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public boolean createsDynamicStubs() {
        return true;
    }
}
