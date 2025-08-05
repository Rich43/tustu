package com.sun.corba.se.impl.ior;

import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.spi.activation.Activator;
import com.sun.corba.se.spi.activation._ActivatorStub;
import com.sun.corba.se.spi.activation._InitialNameServiceStub;
import com.sun.corba.se.spi.activation._LocatorStub;
import com.sun.corba.se.spi.activation._RepositoryStub;
import com.sun.corba.se.spi.activation._ServerManagerStub;
import com.sun.corba.se.spi.activation._ServerStub;
import com.sun.corba.se.spi.ior.IORTypeCheckRegistry;
import com.sun.corba.se.spi.orb.ORB;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.omg.CosNaming.BindingIterator;
import org.omg.CosNaming.NamingContext;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming._BindingIteratorStub;
import org.omg.CosNaming._NamingContextExtStub;
import org.omg.CosNaming._NamingContextStub;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactory;
import org.omg.DynamicAny.DynArray;
import org.omg.DynamicAny.DynEnum;
import org.omg.DynamicAny.DynFixed;
import org.omg.DynamicAny.DynSequence;
import org.omg.DynamicAny.DynStruct;
import org.omg.DynamicAny.DynUnion;
import org.omg.DynamicAny.DynValue;
import org.omg.DynamicAny._DynAnyFactoryStub;
import org.omg.DynamicAny._DynAnyStub;
import org.omg.DynamicAny._DynArrayStub;
import org.omg.DynamicAny._DynEnumStub;
import org.omg.DynamicAny._DynFixedStub;
import org.omg.DynamicAny._DynSequenceStub;
import org.omg.DynamicAny._DynStructStub;
import org.omg.DynamicAny._DynUnionStub;
import org.omg.DynamicAny._DynValueStub;
import org.omg.PortableServer.ServantActivator;
import org.omg.PortableServer.ServantLocator;
import org.omg.PortableServer._ServantActivatorStub;
import org.omg.PortableServer._ServantLocatorStub;

/* loaded from: rt.jar:com/sun/corba/se/impl/ior/IORTypeCheckRegistryImpl.class */
public class IORTypeCheckRegistryImpl implements IORTypeCheckRegistry {
    private final Set<String> iorTypeNames;
    private static final Set<String> builtinIorTypeNames = initBuiltinIorTypeNames();
    private ORB theOrb;

    public IORTypeCheckRegistryImpl(String str, ORB orb) {
        this.theOrb = orb;
        this.iorTypeNames = parseIorClassNameList(str);
    }

    @Override // com.sun.corba.se.spi.ior.IORTypeCheckRegistry
    public boolean isValidIORType(String str) {
        dprintTransport(".isValidIORType : iorClassName == " + str);
        return validateIorTypeByName(str);
    }

    private boolean validateIorTypeByName(String str) {
        dprintTransport(".validateIorTypeByName : iorClassName == " + str);
        boolean zCheckIorTypeNames = checkIorTypeNames(str);
        if (!zCheckIorTypeNames) {
            zCheckIorTypeNames = checkBuiltinClassNames(str);
        }
        dprintTransport(".validateIorTypeByName : isValidType == " + zCheckIorTypeNames);
        return zCheckIorTypeNames;
    }

    private boolean checkIorTypeNames(String str) {
        return this.iorTypeNames != null && this.iorTypeNames.contains(str);
    }

    private boolean checkBuiltinClassNames(String str) {
        return builtinIorTypeNames.contains(str);
    }

    private Set<String> parseIorClassNameList(String str) {
        Set<String> setUnmodifiableSet = null;
        if (str != null) {
            setUnmodifiableSet = Collections.unmodifiableSet(new HashSet(Arrays.asList(str.split(";"))));
            if (this.theOrb.orbInitDebugFlag) {
                dprintConfiguredIorTypeNames();
            }
        }
        return setUnmodifiableSet;
    }

    private static Set<String> initBuiltinIorTypeNames() {
        Set<Class<?>> setInitBuiltInCorbaStubTypes = initBuiltInCorbaStubTypes();
        String[] strArr = new String[setInitBuiltInCorbaStubTypes.size()];
        int i2 = 0;
        Iterator<Class<?>> it = setInitBuiltInCorbaStubTypes.iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            strArr[i3] = it.next().getName();
        }
        return Collections.unmodifiableSet(new HashSet(Arrays.asList(strArr)));
    }

    private static Set<Class<?>> initBuiltInCorbaStubTypes() {
        return new HashSet(Arrays.asList(Activator.class, _ActivatorStub.class, _InitialNameServiceStub.class, _LocatorStub.class, _RepositoryStub.class, _ServerManagerStub.class, _ServerStub.class, BindingIterator.class, _BindingIteratorStub.class, NamingContextExt.class, _NamingContextExtStub.class, NamingContext.class, _NamingContextStub.class, DynAnyFactory.class, _DynAnyFactoryStub.class, DynAny.class, _DynAnyStub.class, DynArray.class, _DynArrayStub.class, DynEnum.class, _DynEnumStub.class, DynFixed.class, _DynFixedStub.class, DynSequence.class, _DynSequenceStub.class, DynStruct.class, _DynStructStub.class, DynUnion.class, _DynUnionStub.class, _DynValueStub.class, DynValue.class, ServantActivator.class, _ServantActivatorStub.class, ServantLocator.class, _ServantLocatorStub.class));
    }

    private void dprintConfiguredIorTypeNames() {
        if (this.iorTypeNames != null) {
            Iterator<String> it = this.iorTypeNames.iterator();
            while (it.hasNext()) {
                ORBUtility.dprint(this, ".dprintConfiguredIorTypeNames: " + it.next());
            }
        }
    }

    private void dprintTransport(String str) {
        if (this.theOrb.transportDebugFlag) {
            ORBUtility.dprint(this, str);
        }
    }
}
