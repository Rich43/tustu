package org.omg.CORBA;

import com.sun.corba.se.impl.orb.ORBImpl;
import com.sun.corba.se.impl.orb.ORBSingleton;
import java.applet.Applet;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;
import org.omg.CORBA.ORBPackage.InconsistentTypeCode;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CORBA.portable.OutputStream;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:org/omg/CORBA/ORB.class */
public abstract class ORB {
    private static final String ORBClassKey = "org.omg.CORBA.ORBClass";
    private static final String ORBSingletonClassKey = "org.omg.CORBA.ORBSingletonClass";
    private static ORB singleton;

    protected abstract void set_parameters(String[] strArr, Properties properties);

    protected abstract void set_parameters(Applet applet, Properties properties);

    public abstract String[] list_initial_services();

    public abstract Object resolve_initial_references(String str) throws InvalidName;

    public abstract String object_to_string(Object object);

    public abstract Object string_to_object(String str);

    public abstract NVList create_list(int i2);

    public abstract NamedValue create_named_value(String str, Any any, int i2);

    public abstract ExceptionList create_exception_list();

    public abstract ContextList create_context_list();

    public abstract Context get_default_context();

    public abstract Environment create_environment();

    public abstract OutputStream create_output_stream();

    public abstract void send_multiple_requests_oneway(Request[] requestArr);

    public abstract void send_multiple_requests_deferred(Request[] requestArr);

    public abstract boolean poll_next_response();

    public abstract Request get_next_response() throws WrongTransaction;

    public abstract TypeCode get_primitive_tc(TCKind tCKind);

    public abstract TypeCode create_struct_tc(String str, String str2, StructMember[] structMemberArr);

    public abstract TypeCode create_union_tc(String str, String str2, TypeCode typeCode, UnionMember[] unionMemberArr);

    public abstract TypeCode create_enum_tc(String str, String str2, String[] strArr);

    public abstract TypeCode create_alias_tc(String str, String str2, TypeCode typeCode);

    public abstract TypeCode create_exception_tc(String str, String str2, StructMember[] structMemberArr);

    public abstract TypeCode create_interface_tc(String str, String str2);

    public abstract TypeCode create_string_tc(int i2);

    public abstract TypeCode create_wstring_tc(int i2);

    public abstract TypeCode create_sequence_tc(int i2, TypeCode typeCode);

    @Deprecated
    public abstract TypeCode create_recursive_sequence_tc(int i2, int i3);

    public abstract TypeCode create_array_tc(int i2, TypeCode typeCode);

    public abstract Any create_any();

    private static String getSystemProperty(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.omg.CORBA.ORB.1
            @Override // java.security.PrivilegedAction
            public java.lang.Object run() {
                return System.getProperty(str);
            }
        });
    }

    private static String getPropertyFromFile(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: org.omg.CORBA.ORB.2
            private Properties getFileProperties(String str2) {
                try {
                    File file = new File(str2);
                    if (!file.exists()) {
                        return null;
                    }
                    Properties properties = new Properties();
                    FileInputStream fileInputStream = new FileInputStream(file);
                    try {
                        properties.load(fileInputStream);
                        fileInputStream.close();
                        return properties;
                    } catch (Throwable th) {
                        fileInputStream.close();
                        throw th;
                    }
                } catch (Exception e2) {
                    return null;
                }
            }

            @Override // java.security.PrivilegedAction
            public java.lang.Object run() {
                String property;
                Properties fileProperties = getFileProperties(System.getProperty("user.home") + File.separator + "orb.properties");
                if (fileProperties != null && (property = fileProperties.getProperty(str)) != null) {
                    return property;
                }
                Properties fileProperties2 = getFileProperties(System.getProperty("java.home") + File.separator + "lib" + File.separator + "orb.properties");
                if (fileProperties2 == null) {
                    return null;
                }
                return fileProperties2.getProperty(str);
            }
        });
    }

    public static synchronized ORB init() {
        if (singleton == null) {
            String systemProperty = getSystemProperty(ORBSingletonClassKey);
            if (systemProperty == null) {
                systemProperty = getPropertyFromFile(ORBSingletonClassKey);
            }
            if (systemProperty == null || systemProperty.equals("com.sun.corba.se.impl.orb.ORBSingleton")) {
                singleton = new ORBSingleton();
            } else {
                singleton = create_impl(systemProperty);
            }
        }
        return singleton;
    }

    private static ORB create_impl(String str) {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        if (contextClassLoader == null) {
            contextClassLoader = ClassLoader.getSystemClassLoader();
        }
        try {
            ReflectUtil.checkPackageAccess(str);
            return (ORB) Class.forName(str, true, contextClassLoader).asSubclass(ORB.class).newInstance();
        } catch (Throwable th) {
            INITIALIZE initialize = new INITIALIZE("can't instantiate default ORB implementation " + str);
            initialize.initCause(th);
            throw initialize;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [org.omg.CORBA.ORB] */
    public static ORB init(String[] strArr, Properties properties) {
        ORBImpl oRBImpl;
        String propertyFromFile = null;
        if (properties != null) {
            propertyFromFile = properties.getProperty(ORBClassKey);
        }
        if (propertyFromFile == null) {
            propertyFromFile = getSystemProperty(ORBClassKey);
        }
        if (propertyFromFile == null) {
            propertyFromFile = getPropertyFromFile(ORBClassKey);
        }
        if (propertyFromFile == null || propertyFromFile.equals("com.sun.corba.se.impl.orb.ORBImpl")) {
            oRBImpl = new ORBImpl();
        } else {
            oRBImpl = create_impl(propertyFromFile);
        }
        oRBImpl.set_parameters(strArr, properties);
        return oRBImpl;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v12, types: [org.omg.CORBA.ORB] */
    public static ORB init(Applet applet, Properties properties) {
        ORBImpl oRBImpl;
        String parameter = applet.getParameter(ORBClassKey);
        if (parameter == null && properties != null) {
            parameter = properties.getProperty(ORBClassKey);
        }
        if (parameter == null) {
            parameter = getSystemProperty(ORBClassKey);
        }
        if (parameter == null) {
            parameter = getPropertyFromFile(ORBClassKey);
        }
        if (parameter == null || parameter.equals("com.sun.corba.se.impl.orb.ORBImpl")) {
            oRBImpl = new ORBImpl();
        } else {
            oRBImpl = create_impl(parameter);
        }
        oRBImpl.set_parameters(applet, properties);
        return oRBImpl;
    }

    public void connect(Object object) {
        throw new NO_IMPLEMENT();
    }

    public void destroy() {
        throw new NO_IMPLEMENT();
    }

    public void disconnect(Object object) {
        throw new NO_IMPLEMENT();
    }

    public NVList create_operation_list(Object object) {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader == null) {
                contextClassLoader = ClassLoader.getSystemClassLoader();
            }
            return (NVList) getClass().getMethod("create_operation_list", Class.forName("org.omg.CORBA.OperationDef", true, contextClassLoader)).invoke(this, object);
        } catch (RuntimeException e2) {
            throw e2;
        } catch (InvocationTargetException e3) {
            Throwable targetException = e3.getTargetException();
            if (targetException instanceof Error) {
                throw ((Error) targetException);
            }
            if (targetException instanceof RuntimeException) {
                throw ((RuntimeException) targetException);
            }
            throw new NO_IMPLEMENT();
        } catch (Exception e4) {
            throw new NO_IMPLEMENT();
        }
    }

    public TypeCode create_native_tc(String str, String str2) {
        throw new NO_IMPLEMENT();
    }

    public TypeCode create_abstract_interface_tc(String str, String str2) {
        throw new NO_IMPLEMENT();
    }

    public TypeCode create_fixed_tc(short s2, short s3) {
        throw new NO_IMPLEMENT();
    }

    public TypeCode create_value_tc(String str, String str2, short s2, TypeCode typeCode, ValueMember[] valueMemberArr) {
        throw new NO_IMPLEMENT();
    }

    public TypeCode create_recursive_tc(String str) {
        throw new NO_IMPLEMENT();
    }

    public TypeCode create_value_box_tc(String str, String str2, TypeCode typeCode) {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public Current get_current() {
        throw new NO_IMPLEMENT();
    }

    public void run() {
        throw new NO_IMPLEMENT();
    }

    public void shutdown(boolean z2) {
        throw new NO_IMPLEMENT();
    }

    public boolean work_pending() {
        throw new NO_IMPLEMENT();
    }

    public void perform_work() {
        throw new NO_IMPLEMENT();
    }

    public boolean get_service_information(short s2, ServiceInformationHolder serviceInformationHolder) {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynAny create_dyn_any(Any any) {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynAny create_basic_dyn_any(TypeCode typeCode) throws InconsistentTypeCode {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynStruct create_dyn_struct(TypeCode typeCode) throws InconsistentTypeCode {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynSequence create_dyn_sequence(TypeCode typeCode) throws InconsistentTypeCode {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynArray create_dyn_array(TypeCode typeCode) throws InconsistentTypeCode {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynUnion create_dyn_union(TypeCode typeCode) throws InconsistentTypeCode {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public DynEnum create_dyn_enum(TypeCode typeCode) throws InconsistentTypeCode {
        throw new NO_IMPLEMENT();
    }

    public Policy create_policy(int i2, Any any) throws PolicyError {
        throw new NO_IMPLEMENT();
    }
}
