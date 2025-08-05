package com.sun.corba.se.impl.util;

import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.UtilSystemException;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import java.rmi.Remote;
import java.rmi.RemoteException;
import javax.rmi.CORBA.Tie;
import javax.rmi.CORBA.Util;
import javax.rmi.PortableRemoteObject;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.portable.BoxedValueHelper;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.ValueFactory;
import org.omg.CORBA_2_3.portable.Delegate;
import org.omg.PortableServer.Servant;
import org.omg.stub.java.rmi._Remote_Stub;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/Utility.class */
public final class Utility {
    public static final String STUB_PREFIX = "_";
    public static final String RMI_STUB_SUFFIX = "_Stub";
    public static final String DYNAMIC_STUB_SUFFIX = "_DynamicStub";
    public static final String IDL_STUB_SUFFIX = "Stub";
    public static final String TIE_SUFIX = "_Tie";
    private static IdentityHashtable tieCache = new IdentityHashtable();
    private static IdentityHashtable tieToStubCache = new IdentityHashtable();
    private static IdentityHashtable stubToTieCache = new IdentityHashtable();
    private static Object CACHE_MISS = new Object();
    private static UtilSystemException wrapper = UtilSystemException.get(CORBALogDomains.UTIL);
    private static OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.UTIL);

    public static Object autoConnect(Object obj, ORB orb, boolean z2) {
        if (obj == null) {
            return obj;
        }
        if (StubAdapter.isStub(obj)) {
            try {
                StubAdapter.getDelegate(obj);
            } catch (BAD_OPERATION e2) {
                try {
                    StubAdapter.connect(obj, orb);
                } catch (RemoteException e3) {
                    throw wrapper.objectNotConnected(e3, obj.getClass().getName());
                }
            }
            return obj;
        }
        if (obj instanceof Remote) {
            Tie tie = Util.getTie((Remote) obj);
            if (tie != null) {
                try {
                    tie.orb();
                } catch (SystemException e4) {
                    tie.orb(orb);
                }
                if (z2) {
                    Remote remoteLoadStub = loadStub(tie, null, null, true);
                    if (remoteLoadStub != null) {
                        return remoteLoadStub;
                    }
                    throw wrapper.couldNotLoadStub(obj.getClass().getName());
                }
                return StubAdapter.activateTie(tie);
            }
            throw wrapper.objectNotExported(obj.getClass().getName());
        }
        return obj;
    }

    public static Tie loadTie(Remote remote) {
        Tie tieLoadTie = null;
        Class<?> cls = remote.getClass();
        synchronized (tieCache) {
            Object obj = tieCache.get(remote);
            if (obj == null) {
                try {
                    tieLoadTie = loadTie(cls);
                    while (tieLoadTie == null) {
                        Class<? super Object> superclass = cls.getSuperclass();
                        cls = superclass;
                        if (superclass == null || cls == PortableRemoteObject.class || cls == Object.class) {
                            break;
                        }
                        tieLoadTie = loadTie(cls);
                    }
                } catch (Exception e2) {
                    wrapper.loadTieFailed(e2, cls.getName());
                }
                if (tieLoadTie == null) {
                    tieCache.put(remote, CACHE_MISS);
                } else {
                    tieCache.put(remote, tieLoadTie);
                }
            } else if (obj != CACHE_MISS) {
                try {
                    tieLoadTie = (Tie) obj.getClass().newInstance();
                } catch (Exception e3) {
                }
            }
        }
        return tieLoadTie;
    }

    private static Tie loadTie(Class cls) {
        return com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory().getTie(cls);
    }

    public static void clearCaches() {
        synchronized (tieToStubCache) {
            tieToStubCache.clear();
        }
        synchronized (tieCache) {
            tieCache.clear();
        }
        synchronized (stubToTieCache) {
            stubToTieCache.clear();
        }
    }

    static Class loadClassOfType(String str, String str2, ClassLoader classLoader, Class cls, ClassLoader classLoader2) throws ClassNotFoundException {
        Class<?> clsLoadClass = null;
        try {
            try {
                if (!PackagePrefixChecker.hasOffendingPrefix(PackagePrefixChecker.withoutPackagePrefix(str))) {
                    clsLoadClass = Util.loadClass(PackagePrefixChecker.withoutPackagePrefix(str), str2, classLoader);
                } else {
                    clsLoadClass = Util.loadClass(str, str2, classLoader);
                }
            } catch (ClassNotFoundException e2) {
                clsLoadClass = Util.loadClass(str, str2, classLoader);
            }
            if (cls == null) {
                return clsLoadClass;
            }
        } catch (ClassNotFoundException e3) {
            if (cls == null) {
                throw e3;
            }
        }
        if (clsLoadClass == null || !cls.isAssignableFrom(clsLoadClass)) {
            if (cls.getClassLoader() != classLoader2) {
                throw new IllegalArgumentException("expectedTypeClassLoader not class loader of expected Type.");
            }
            if (classLoader2 != null) {
                clsLoadClass = classLoader2.loadClass(str);
            } else {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                if (contextClassLoader == null) {
                    contextClassLoader = ClassLoader.getSystemClassLoader();
                }
                clsLoadClass = contextClassLoader.loadClass(str);
            }
        }
        return clsLoadClass;
    }

    public static Class loadClassForClass(String str, String str2, ClassLoader classLoader, Class cls, ClassLoader classLoader2) throws ClassNotFoundException {
        if (cls == null) {
            return Util.loadClass(str, str2, classLoader);
        }
        Class<?> clsLoadClass = null;
        try {
            clsLoadClass = Util.loadClass(str, str2, classLoader);
        } catch (ClassNotFoundException e2) {
            if (cls.getClassLoader() == null) {
                throw e2;
            }
        }
        if (clsLoadClass == null || (clsLoadClass.getClassLoader() != null && clsLoadClass.getClassLoader().loadClass(cls.getName()) != cls)) {
            if (cls.getClassLoader() != classLoader2) {
                throw new IllegalArgumentException("relatedTypeClassLoader not class loader of relatedType.");
            }
            if (classLoader2 != null) {
                clsLoadClass = classLoader2.loadClass(str);
            }
        }
        return clsLoadClass;
    }

    public static BoxedValueHelper getHelper(Class cls, String str, String str2) {
        ClassLoader classLoader;
        String className = null;
        if (cls != null) {
            className = cls.getName();
            if (str == null) {
                str = Util.getCodebase(cls);
            }
        } else {
            if (str2 != null) {
                className = RepositoryId.cache.getId(str2).getClassName();
            }
            if (className == null) {
                throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE);
            }
        }
        if (cls == null) {
            classLoader = null;
        } else {
            try {
                classLoader = cls.getClassLoader();
            } catch (ClassCastException e2) {
                throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, e2);
            } catch (ClassNotFoundException e3) {
                throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, e3);
            } catch (IllegalAccessException e4) {
                throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, e4);
            } catch (InstantiationException e5) {
                throw wrapper.unableLocateValueHelper(CompletionStatus.COMPLETED_MAYBE, e5);
            }
        }
        ClassLoader classLoader2 = classLoader;
        return (BoxedValueHelper) loadClassForClass(className + "Helper", str, classLoader2, cls, classLoader2).newInstance();
    }

    public static ValueFactory getFactory(Class cls, String str, ORB orb, String str2) {
        ClassLoader classLoader;
        ValueFactory valueFactoryLookup_value_factory = null;
        if (orb != null && str2 != null) {
            try {
                valueFactoryLookup_value_factory = ((org.omg.CORBA_2_3.ORB) orb).lookup_value_factory(str2);
            } catch (BAD_PARAM e2) {
            }
        }
        String className = null;
        if (cls != null) {
            className = cls.getName();
            if (str == null) {
                str = Util.getCodebase(cls);
            }
        } else {
            if (str2 != null) {
                className = RepositoryId.cache.getId(str2).getClassName();
            }
            if (className == null) {
                throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE);
            }
        }
        if (valueFactoryLookup_value_factory != null && (!valueFactoryLookup_value_factory.getClass().getName().equals(className + "DefaultFactory") || (cls == null && str == null))) {
            return valueFactoryLookup_value_factory;
        }
        if (cls == null) {
            classLoader = null;
        } else {
            try {
                classLoader = cls.getClassLoader();
            } catch (ClassCastException e3) {
                throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, e3);
            } catch (ClassNotFoundException e4) {
                throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, e4);
            } catch (IllegalAccessException e5) {
                throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, e5);
            } catch (InstantiationException e6) {
                throw omgWrapper.unableLocateValueFactory(CompletionStatus.COMPLETED_MAYBE, e6);
            }
        }
        ClassLoader classLoader2 = classLoader;
        return (ValueFactory) loadClassForClass(className + "DefaultFactory", str, classLoader2, cls, classLoader2).newInstance();
    }

    public static Remote loadStub(Tie tie, PresentationManager.StubFactory stubFactory, String str, boolean z2) {
        StubEntry stubEntryLoadStubAndUpdateCache = null;
        synchronized (tieToStubCache) {
            Object obj = tieToStubCache.get(tie);
            if (obj == null) {
                stubEntryLoadStubAndUpdateCache = loadStubAndUpdateCache(tie, stubFactory, str, z2);
            } else if (obj != CACHE_MISS) {
                stubEntryLoadStubAndUpdateCache = (StubEntry) obj;
                if (!stubEntryLoadStubAndUpdateCache.mostDerived && z2) {
                    stubEntryLoadStubAndUpdateCache = loadStubAndUpdateCache(tie, null, str, true);
                } else if (stubFactory != null && !StubAdapter.getTypeIds(stubEntryLoadStubAndUpdateCache.stub)[0].equals(stubFactory.getTypeIds()[0])) {
                    stubEntryLoadStubAndUpdateCache = loadStubAndUpdateCache(tie, null, str, true);
                    if (stubEntryLoadStubAndUpdateCache == null) {
                        stubEntryLoadStubAndUpdateCache = loadStubAndUpdateCache(tie, stubFactory, str, z2);
                    }
                } else {
                    try {
                        StubAdapter.getDelegate(stubEntryLoadStubAndUpdateCache.stub);
                    } catch (Exception e2) {
                        try {
                            StubAdapter.setDelegate(stubEntryLoadStubAndUpdateCache.stub, StubAdapter.getDelegate(tie));
                        } catch (Exception e3) {
                        }
                    }
                }
            }
        }
        if (stubEntryLoadStubAndUpdateCache != null) {
            return (Remote) stubEntryLoadStubAndUpdateCache.stub;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static StubEntry loadStubAndUpdateCache(Tie tie, PresentationManager.StubFactory stubFactory, String str, boolean z2) {
        boolean z3;
        ThreadDeath threadDeath;
        String[] strArr_all_interfaces;
        Object objectMakeStub = null;
        StubEntry stubEntry = null;
        boolean zIsStub = StubAdapter.isStub(tie);
        if (stubFactory != null) {
            try {
                objectMakeStub = stubFactory.makeStub();
            } finally {
                if (z3) {
                }
            }
        } else {
            if (zIsStub) {
                strArr_all_interfaces = StubAdapter.getTypeIds(tie);
            } else {
                strArr_all_interfaces = ((Servant) tie)._all_interfaces(null, null);
            }
            if (str == null) {
                str = Util.getCodebase(tie.getClass());
            }
            if (strArr_all_interfaces.length == 0) {
                objectMakeStub = new _Remote_Stub();
            } else {
                int i2 = 0;
                while (true) {
                    if (i2 >= strArr_all_interfaces.length) {
                        break;
                    }
                    if (strArr_all_interfaces[i2].length() == 0) {
                        objectMakeStub = new _Remote_Stub();
                        break;
                    }
                    try {
                        PresentationManager.StubFactoryFactory stubFactoryFactory = com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory();
                        RepositoryId id = RepositoryId.cache.getId(strArr_all_interfaces[i2]);
                        objectMakeStub = stubFactoryFactory.createStubFactory(id.getClassName(), id.isIDLType(), str, null, tie.getClass().getClassLoader()).makeStub();
                        break;
                    } catch (Exception e2) {
                        wrapper.errorInMakeStubFromRepositoryId(e2);
                        if (z2) {
                            break;
                        }
                        i2++;
                    }
                }
            }
        }
        if (objectMakeStub == null) {
            tieToStubCache.put(tie, CACHE_MISS);
        } else {
            if (zIsStub) {
                try {
                    StubAdapter.setDelegate(objectMakeStub, StubAdapter.getDelegate(tie));
                } catch (Exception e3) {
                    synchronized (stubToTieCache) {
                        stubToTieCache.put(objectMakeStub, tie);
                    }
                }
            } else {
                try {
                    StubAdapter.setDelegate(objectMakeStub, StubAdapter.getDelegate(tie));
                } catch (BAD_INV_ORDER e4) {
                    synchronized (stubToTieCache) {
                        stubToTieCache.put(objectMakeStub, tie);
                    }
                } catch (Exception e5) {
                    throw wrapper.noPoa(e5);
                }
            }
            stubEntry = new StubEntry(objectMakeStub, z2);
            tieToStubCache.put(tie, stubEntry);
        }
        return stubEntry;
    }

    public static Tie getAndForgetTie(Object object) {
        Tie tie;
        synchronized (stubToTieCache) {
            tie = (Tie) stubToTieCache.remove(object);
        }
        return tie;
    }

    public static void purgeStubForTie(Tie tie) {
        StubEntry stubEntry;
        synchronized (tieToStubCache) {
            stubEntry = (StubEntry) tieToStubCache.remove(tie);
        }
        if (stubEntry != null) {
            synchronized (stubToTieCache) {
                stubToTieCache.remove(stubEntry.stub);
            }
        }
    }

    public static void purgeTieAndServant(Tie tie) {
        synchronized (tieCache) {
            Remote target = tie.getTarget();
            if (target != null) {
                tieCache.remove(target);
            }
        }
    }

    public static String stubNameFromRepID(String str) {
        String strStubName;
        RepositoryId id = RepositoryId.cache.getId(str);
        String className = id.getClassName();
        if (id.isIDLType()) {
            strStubName = idlStubName(className);
        } else {
            strStubName = stubName(className);
        }
        return strStubName;
    }

    public static Remote loadStub(Object object, Class cls) {
        Remote remote = null;
        String str = null;
        try {
            try {
                str = ((Delegate) StubAdapter.getDelegate(object)).get_codebase(object);
            } catch (ClassCastException e2) {
                wrapper.classCastExceptionInLoadStub(e2);
            }
            remote = (Remote) com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory().createStubFactory(cls.getName(), false, str, cls, cls.getClassLoader()).makeStub();
            StubAdapter.setDelegate(remote, StubAdapter.getDelegate(object));
        } catch (Exception e3) {
            wrapper.exceptionInLoadStub(e3);
        }
        return remote;
    }

    public static Class loadStubClass(String str, String str2, Class cls) throws ClassNotFoundException {
        if (str.length() == 0) {
            throw new ClassNotFoundException();
        }
        String strStubNameFromRepID = stubNameFromRepID(str);
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        try {
            return loadClassOfType(strStubNameFromRepID, str2, classLoader, cls, classLoader);
        } catch (ClassNotFoundException e2) {
            return loadClassOfType(PackagePrefixChecker.packagePrefix() + strStubNameFromRepID, str2, classLoader, cls, classLoader);
        }
    }

    public static String stubName(String str) {
        return stubName(str, false);
    }

    public static String dynamicStubName(String str) {
        return stubName(str, true);
    }

    private static String stubName(String str, boolean z2) {
        String strStubNameForCompiler = stubNameForCompiler(str, z2);
        if (PackagePrefixChecker.hasOffendingPrefix(strStubNameForCompiler)) {
            strStubNameForCompiler = PackagePrefixChecker.packagePrefix() + strStubNameForCompiler;
        }
        return strStubNameForCompiler;
    }

    public static String stubNameForCompiler(String str) {
        return stubNameForCompiler(str, false);
    }

    private static String stubNameForCompiler(String str, boolean z2) {
        int iIndexOf = str.indexOf(36);
        if (iIndexOf < 0) {
            iIndexOf = str.lastIndexOf(46);
        }
        String str2 = z2 ? DYNAMIC_STUB_SUFFIX : RMI_STUB_SUFFIX;
        if (iIndexOf > 0) {
            return str.substring(0, iIndexOf + 1) + "_" + str.substring(iIndexOf + 1) + str2;
        }
        return "_" + str + str2;
    }

    public static String tieName(String str) {
        if (PackagePrefixChecker.hasOffendingPrefix(tieNameForCompiler(str))) {
            return PackagePrefixChecker.packagePrefix() + tieNameForCompiler(str);
        }
        return tieNameForCompiler(str);
    }

    public static String tieNameForCompiler(String str) {
        int iIndexOf = str.indexOf(36);
        if (iIndexOf < 0) {
            iIndexOf = str.lastIndexOf(46);
        }
        if (iIndexOf > 0) {
            return str.substring(0, iIndexOf + 1) + "_" + str.substring(iIndexOf + 1) + TIE_SUFIX;
        }
        return "_" + str + TIE_SUFIX;
    }

    public static void throwNotSerializableForCorba(String str) {
        throw omgWrapper.notSerializable(CompletionStatus.COMPLETED_MAYBE, str);
    }

    public static String idlStubName(String str) {
        String str2;
        int iLastIndexOf = str.lastIndexOf(46);
        if (iLastIndexOf > 0) {
            str2 = str.substring(0, iLastIndexOf + 1) + "_" + str.substring(iLastIndexOf + 1) + IDL_STUB_SUFFIX;
        } else {
            str2 = "_" + str + IDL_STUB_SUFFIX;
        }
        return str2;
    }

    public static void printStackTrace() {
        Throwable th = new Throwable("Printing stack trace:");
        th.fillInStackTrace();
        th.printStackTrace();
    }

    public static Object readObjectAndNarrow(InputStream inputStream, Class cls) throws ClassCastException {
        Object object = inputStream.read_Object();
        if (object != null) {
            return PortableRemoteObject.narrow(object, cls);
        }
        return null;
    }

    public static Object readAbstractAndNarrow(org.omg.CORBA_2_3.portable.InputStream inputStream, Class cls) throws ClassCastException {
        Object obj = inputStream.read_abstract_interface();
        if (obj != null) {
            return PortableRemoteObject.narrow(obj, cls);
        }
        return null;
    }

    static int hexOf(char c2) {
        int i2 = c2 - '0';
        if (i2 >= 0 && i2 <= 9) {
            return i2;
        }
        int i3 = (c2 - 'a') + 10;
        if (i3 >= 10 && i3 <= 15) {
            return i3;
        }
        int i4 = (c2 - 'A') + 10;
        if (i4 >= 10 && i4 <= 15) {
            return i4;
        }
        throw wrapper.badHexDigit();
    }
}
