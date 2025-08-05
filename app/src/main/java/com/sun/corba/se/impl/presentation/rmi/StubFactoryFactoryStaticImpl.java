package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.impl.util.PackagePrefixChecker;
import com.sun.corba.se.impl.util.Utility;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import org.omg.CORBA.CompletionStatus;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/StubFactoryFactoryStaticImpl.class */
public class StubFactoryFactoryStaticImpl extends StubFactoryFactoryBase {
    private ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.RPC_PRESENTATION);

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public PresentationManager.StubFactory createStubFactory(String str, boolean z2, String str2, Class cls, ClassLoader classLoader) {
        String strStubNameForCompiler;
        Class<?> clsLoadClass;
        if (z2) {
            strStubNameForCompiler = Utility.idlStubName(str);
        } else {
            strStubNameForCompiler = Utility.stubNameForCompiler(str);
        }
        ClassLoader classLoader2 = cls == null ? classLoader : cls.getClassLoader();
        String str3 = strStubNameForCompiler;
        String str4 = strStubNameForCompiler;
        if (PackagePrefixChecker.hasOffendingPrefix(strStubNameForCompiler)) {
            str3 = PackagePrefixChecker.packagePrefix() + strStubNameForCompiler;
        } else {
            str4 = PackagePrefixChecker.packagePrefix() + strStubNameForCompiler;
        }
        try {
            clsLoadClass = Util.loadClass(str3, str2, classLoader2);
        } catch (ClassNotFoundException e2) {
            this.wrapper.classNotFound1(CompletionStatus.COMPLETED_MAYBE, e2, str3);
            try {
                clsLoadClass = Util.loadClass(str4, str2, classLoader2);
            } catch (ClassNotFoundException e3) {
                throw this.wrapper.classNotFound2(CompletionStatus.COMPLETED_MAYBE, e3, str4);
            }
        }
        if (clsLoadClass == null || (cls != null && !cls.isAssignableFrom(clsLoadClass))) {
            try {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader == null) {
                    contextClassLoader = ClassLoader.getSystemClassLoader();
                }
                clsLoadClass = contextClassLoader.loadClass(str);
            } catch (Exception e4) {
                IllegalStateException illegalStateException = new IllegalStateException("Could not load class " + strStubNameForCompiler);
                illegalStateException.initCause(e4);
                throw illegalStateException;
            }
        }
        return new StubFactoryStaticImpl(clsLoadClass);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public Tie getTie(Class cls) {
        String strTieName = Utility.tieName(cls.getName());
        try {
            try {
                return (Tie) Utility.loadClassForClass(strTieName, Util.getCodebase(cls), null, cls, cls.getClassLoader()).newInstance();
            } catch (Exception e2) {
                return (Tie) Utility.loadClassForClass(PackagePrefixChecker.packagePrefix() + strTieName, Util.getCodebase(cls), null, cls, cls.getClassLoader()).newInstance();
            }
        } catch (Exception e3) {
            return null;
        }
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory
    public boolean createsDynamicStubs() {
        return false;
    }
}
