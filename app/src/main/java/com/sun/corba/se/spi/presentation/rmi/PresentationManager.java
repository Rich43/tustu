package com.sun.corba.se.spi.presentation.rmi;

import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
import java.lang.reflect.Method;
import java.util.Map;
import javax.rmi.CORBA.Tie;
import org.omg.CORBA.Object;

/* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/PresentationManager.class */
public interface PresentationManager {

    /* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/PresentationManager$ClassData.class */
    public interface ClassData {
        Class getMyClass();

        IDLNameTranslator getIDLNameTranslator();

        String[] getTypeIds();

        InvocationHandlerFactory getInvocationHandlerFactory();

        Map getDictionary();
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/PresentationManager$StubFactory.class */
    public interface StubFactory {
        Object makeStub();

        String[] getTypeIds();
    }

    /* loaded from: rt.jar:com/sun/corba/se/spi/presentation/rmi/PresentationManager$StubFactoryFactory.class */
    public interface StubFactoryFactory {
        String getStubName(String str);

        StubFactory createStubFactory(String str, boolean z2, String str2, Class cls, ClassLoader classLoader);

        Tie getTie(Class cls);

        boolean createsDynamicStubs();
    }

    ClassData getClassData(Class cls);

    DynamicMethodMarshaller getDynamicMethodMarshaller(Method method);

    StubFactoryFactory getStubFactoryFactory(boolean z2);

    void setStubFactoryFactory(boolean z2, StubFactoryFactory stubFactoryFactory);

    Tie getTie();

    boolean useDynamicStubs();
}
