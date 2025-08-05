package com.sun.corba.se.impl.orbutil;

import com.sun.corba.se.impl.corba.CORBAObjectImpl;
import com.sun.corba.se.impl.ior.iiop.JavaSerializationComponent;
import com.sun.corba.se.impl.logging.OMGSystemException;
import com.sun.corba.se.impl.logging.ORBUtilSystemException;
import com.sun.corba.se.pept.transport.ContactInfoList;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.orb.ORBVersionFactory;
import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
import com.sun.corba.se.spi.transport.CorbaContactInfoList;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PermissionCollection;
import java.security.Policy;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.security.ProtectionDomain;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import javax.rmi.CORBA.Util;
import javax.rmi.CORBA.ValueHandler;
import javax.rmi.CORBA.ValueHandlerMultiFormat;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.StructMember;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import sun.corba.SharedSecrets;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ORBUtility.class */
public final class ORBUtility {
    private static ORBUtilSystemException wrapper = ORBUtilSystemException.get(CORBALogDomains.UTIL);
    private static OMGSystemException omgWrapper = OMGSystemException.get(CORBALogDomains.UTIL);
    private static StructMember[] members = null;
    private static final Hashtable exceptionClassNames = new Hashtable();
    private static final Hashtable exceptionRepositoryIds = new Hashtable();

    private ORBUtility() {
    }

    static {
        exceptionClassNames.put("IDL:omg.org/CORBA/BAD_CONTEXT:1.0", "org.omg.CORBA.BAD_CONTEXT");
        exceptionClassNames.put("IDL:omg.org/CORBA/BAD_INV_ORDER:1.0", "org.omg.CORBA.BAD_INV_ORDER");
        exceptionClassNames.put("IDL:omg.org/CORBA/BAD_OPERATION:1.0", "org.omg.CORBA.BAD_OPERATION");
        exceptionClassNames.put("IDL:omg.org/CORBA/BAD_PARAM:1.0", "org.omg.CORBA.BAD_PARAM");
        exceptionClassNames.put("IDL:omg.org/CORBA/BAD_TYPECODE:1.0", "org.omg.CORBA.BAD_TYPECODE");
        exceptionClassNames.put("IDL:omg.org/CORBA/COMM_FAILURE:1.0", "org.omg.CORBA.COMM_FAILURE");
        exceptionClassNames.put("IDL:omg.org/CORBA/DATA_CONVERSION:1.0", "org.omg.CORBA.DATA_CONVERSION");
        exceptionClassNames.put("IDL:omg.org/CORBA/IMP_LIMIT:1.0", "org.omg.CORBA.IMP_LIMIT");
        exceptionClassNames.put("IDL:omg.org/CORBA/INTF_REPOS:1.0", "org.omg.CORBA.INTF_REPOS");
        exceptionClassNames.put("IDL:omg.org/CORBA/INTERNAL:1.0", "org.omg.CORBA.INTERNAL");
        exceptionClassNames.put("IDL:omg.org/CORBA/INV_FLAG:1.0", "org.omg.CORBA.INV_FLAG");
        exceptionClassNames.put("IDL:omg.org/CORBA/INV_IDENT:1.0", "org.omg.CORBA.INV_IDENT");
        exceptionClassNames.put("IDL:omg.org/CORBA/INV_OBJREF:1.0", "org.omg.CORBA.INV_OBJREF");
        exceptionClassNames.put("IDL:omg.org/CORBA/MARSHAL:1.0", "org.omg.CORBA.MARSHAL");
        exceptionClassNames.put("IDL:omg.org/CORBA/NO_MEMORY:1.0", "org.omg.CORBA.NO_MEMORY");
        exceptionClassNames.put("IDL:omg.org/CORBA/FREE_MEM:1.0", "org.omg.CORBA.FREE_MEM");
        exceptionClassNames.put("IDL:omg.org/CORBA/NO_IMPLEMENT:1.0", "org.omg.CORBA.NO_IMPLEMENT");
        exceptionClassNames.put("IDL:omg.org/CORBA/NO_PERMISSION:1.0", "org.omg.CORBA.NO_PERMISSION");
        exceptionClassNames.put("IDL:omg.org/CORBA/NO_RESOURCES:1.0", "org.omg.CORBA.NO_RESOURCES");
        exceptionClassNames.put("IDL:omg.org/CORBA/NO_RESPONSE:1.0", "org.omg.CORBA.NO_RESPONSE");
        exceptionClassNames.put("IDL:omg.org/CORBA/OBJ_ADAPTER:1.0", "org.omg.CORBA.OBJ_ADAPTER");
        exceptionClassNames.put("IDL:omg.org/CORBA/INITIALIZE:1.0", "org.omg.CORBA.INITIALIZE");
        exceptionClassNames.put("IDL:omg.org/CORBA/PERSIST_STORE:1.0", "org.omg.CORBA.PERSIST_STORE");
        exceptionClassNames.put("IDL:omg.org/CORBA/TRANSIENT:1.0", "org.omg.CORBA.TRANSIENT");
        exceptionClassNames.put("IDL:omg.org/CORBA/UNKNOWN:1.0", "org.omg.CORBA.UNKNOWN");
        exceptionClassNames.put("IDL:omg.org/CORBA/OBJECT_NOT_EXIST:1.0", "org.omg.CORBA.OBJECT_NOT_EXIST");
        exceptionClassNames.put("IDL:omg.org/CORBA/INVALID_TRANSACTION:1.0", "org.omg.CORBA.INVALID_TRANSACTION");
        exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_REQUIRED:1.0", "org.omg.CORBA.TRANSACTION_REQUIRED");
        exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0", "org.omg.CORBA.TRANSACTION_ROLLEDBACK");
        exceptionClassNames.put("IDL:omg.org/CORBA/INV_POLICY:1.0", "org.omg.CORBA.INV_POLICY");
        exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_UNAVAILABLE:1.0", "org.omg.CORBA.TRANSACTION_UNAVAILABLE");
        exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_MODE:1.0", "org.omg.CORBA.TRANSACTION_MODE");
        exceptionClassNames.put("IDL:omg.org/CORBA/CODESET_INCOMPATIBLE:1.0", "org.omg.CORBA.CODESET_INCOMPATIBLE");
        exceptionClassNames.put("IDL:omg.org/CORBA/REBIND:1.0", "org.omg.CORBA.REBIND");
        exceptionClassNames.put("IDL:omg.org/CORBA/TIMEOUT:1.0", "org.omg.CORBA.TIMEOUT");
        exceptionClassNames.put("IDL:omg.org/CORBA/BAD_QOS:1.0", "org.omg.CORBA.BAD_QOS");
        exceptionClassNames.put("IDL:omg.org/CORBA/INVALID_ACTIVITY:1.0", "org.omg.CORBA.INVALID_ACTIVITY");
        exceptionClassNames.put("IDL:omg.org/CORBA/ACTIVITY_COMPLETED:1.0", "org.omg.CORBA.ACTIVITY_COMPLETED");
        exceptionClassNames.put("IDL:omg.org/CORBA/ACTIVITY_REQUIRED:1.0", "org.omg.CORBA.ACTIVITY_REQUIRED");
        Enumeration enumerationKeys = exceptionClassNames.keys();
        while (enumerationKeys.hasMoreElements()) {
            try {
                String str = (String) enumerationKeys.nextElement2();
                exceptionRepositoryIds.put((String) exceptionClassNames.get(str), str);
            } catch (NoSuchElementException e2) {
                return;
            }
        }
    }

    private static StructMember[] systemExceptionMembers(ORB orb) {
        if (members == null) {
            members = new StructMember[3];
            members[0] = new StructMember("id", orb.create_string_tc(0), null);
            members[1] = new StructMember("minor", orb.get_primitive_tc(TCKind.tk_long), null);
            members[2] = new StructMember("completed", orb.get_primitive_tc(TCKind.tk_long), null);
        }
        return members;
    }

    private static TypeCode getSystemExceptionTypeCode(ORB orb, String str, String str2) {
        TypeCode typeCodeCreate_exception_tc;
        synchronized (TypeCode.class) {
            typeCodeCreate_exception_tc = orb.create_exception_tc(str, str2, systemExceptionMembers(orb));
        }
        return typeCodeCreate_exception_tc;
    }

    private static boolean isSystemExceptionTypeCode(TypeCode typeCode, ORB orb) {
        StructMember[] structMemberArrSystemExceptionMembers = systemExceptionMembers(orb);
        try {
            if (typeCode.kind().value() == 22 && typeCode.member_count() == 3 && typeCode.member_type(0).equal(structMemberArrSystemExceptionMembers[0].type) && typeCode.member_type(1).equal(structMemberArrSystemExceptionMembers[1].type)) {
                if (typeCode.member_type(2).equal(structMemberArrSystemExceptionMembers[2].type)) {
                    return true;
                }
            }
            return false;
        } catch (BadKind e2) {
            return false;
        } catch (Bounds e3) {
            return false;
        }
    }

    public static void insertSystemException(SystemException systemException, Any any) throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = any.create_output_stream();
        ORB orb = (ORB) outputStreamCreate_output_stream.orb();
        String name = systemException.getClass().getName();
        String strRepositoryIdOf = repositoryIdOf(name);
        outputStreamCreate_output_stream.write_string(strRepositoryIdOf);
        outputStreamCreate_output_stream.write_long(systemException.minor);
        outputStreamCreate_output_stream.write_long(systemException.completed.value());
        any.read_value(outputStreamCreate_output_stream.create_input_stream(), getSystemExceptionTypeCode(orb, strRepositoryIdOf, name));
    }

    public static SystemException extractSystemException(Any any) {
        InputStream inputStreamCreate_input_stream = any.create_input_stream();
        if (!isSystemExceptionTypeCode(any.type(), (ORB) inputStreamCreate_input_stream.orb())) {
            throw wrapper.unknownDsiSysex(CompletionStatus.COMPLETED_MAYBE);
        }
        return readSystemException(inputStreamCreate_input_stream);
    }

    public static ValueHandler createValueHandler() {
        try {
            return (ValueHandler) AccessController.doPrivileged(new PrivilegedExceptionAction<ValueHandler>() { // from class: com.sun.corba.se.impl.orbutil.ORBUtility.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public ValueHandler run() throws Exception {
                    return Util.createValueHandler();
                }
            });
        } catch (PrivilegedActionException e2) {
            throw new InternalError(e2.getMessage());
        }
    }

    public static boolean isForeignORB(ORB orb) {
        if (orb == null) {
            return false;
        }
        try {
            return orb.getORBVersion().equals(ORBVersionFactory.getFOREIGN());
        } catch (SecurityException e2) {
            return false;
        }
    }

    public static int bytesToInt(byte[] bArr, int i2) {
        int i3 = i2 + 1;
        int i4 = (bArr[i2] << 24) & (-16777216);
        int i5 = i3 + 1;
        int i6 = (bArr[i3] << 16) & 16711680;
        int i7 = i5 + 1;
        int i8 = (bArr[i5] << 8) & NormalizerImpl.CC_MASK;
        int i9 = i7 + 1;
        return i4 | i6 | i8 | ((bArr[i7] << 0) & 255);
    }

    public static void intToBytes(int i2, byte[] bArr, int i3) {
        int i4 = i3 + 1;
        bArr[i3] = (byte) ((i2 >>> 24) & 255);
        int i5 = i4 + 1;
        bArr[i4] = (byte) ((i2 >>> 16) & 255);
        int i6 = i5 + 1;
        bArr[i5] = (byte) ((i2 >>> 8) & 255);
        int i7 = i6 + 1;
        bArr[i6] = (byte) ((i2 >>> 0) & 255);
    }

    public static int hexOf(char c2) {
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

    public static void writeSystemException(SystemException systemException, OutputStream outputStream) {
        outputStream.write_string(repositoryIdOf(systemException.getClass().getName()));
        outputStream.write_long(systemException.minor);
        outputStream.write_long(systemException.completed.value());
    }

    public static SystemException readSystemException(InputStream inputStream) {
        try {
            SystemException systemException = (SystemException) SharedSecrets.getJavaCorbaAccess().loadClass(classNameOf(inputStream.read_string())).newInstance();
            systemException.minor = inputStream.read_long();
            systemException.completed = CompletionStatus.from_int(inputStream.read_long());
            return systemException;
        } catch (Exception e2) {
            throw wrapper.unknownSysex(CompletionStatus.COMPLETED_MAYBE, e2);
        }
    }

    public static String classNameOf(String str) {
        String str2 = (String) exceptionClassNames.get(str);
        if (str2 == null) {
            str2 = "org.omg.CORBA.UNKNOWN";
        }
        return str2;
    }

    public static boolean isSystemException(String str) {
        if (((String) exceptionClassNames.get(str)) == null) {
            return false;
        }
        return true;
    }

    public static byte getEncodingVersion(ORB orb, IOR ior) {
        if (orb.getORBData().isJavaSerializationEnabled()) {
            Iterator itIteratorById = ((IIOPProfileTemplate) ior.getProfile().getTaggedProfileTemplate()).iteratorById(ORBConstants.TAG_JAVA_SERIALIZATION_ID);
            if (itIteratorById.hasNext()) {
                JavaSerializationComponent javaSerializationComponent = (JavaSerializationComponent) itIteratorById.next();
                byte bJavaSerializationVersion = javaSerializationComponent.javaSerializationVersion();
                if (bJavaSerializationVersion >= 1) {
                    return (byte) 1;
                }
                if (bJavaSerializationVersion > 0) {
                    return javaSerializationComponent.javaSerializationVersion();
                }
                return (byte) 0;
            }
            return (byte) 0;
        }
        return (byte) 0;
    }

    public static String repositoryIdOf(String str) {
        String str2 = (String) exceptionRepositoryIds.get(str);
        if (str2 == null) {
            str2 = "IDL:omg.org/CORBA/UNKNOWN:1.0";
        }
        return str2;
    }

    public static int[] parseVersion(String str) {
        if (str == null) {
            return new int[0];
        }
        char[] charArray = str.toCharArray();
        int i2 = 0;
        while (i2 < charArray.length && (charArray[i2] < '0' || charArray[i2] > '9')) {
            if (i2 != charArray.length) {
                i2++;
            } else {
                return new int[0];
            }
        }
        int i3 = i2 + 1;
        int i4 = 1;
        while (i3 < charArray.length) {
            if (charArray[i3] == '.') {
                i4++;
            } else if (charArray[i3] < '0' || charArray[i3] > '9') {
                break;
            }
            i3++;
        }
        int[] iArr = new int[i4];
        for (int i5 = 0; i5 < i4; i5++) {
            int iIndexOf = str.indexOf(46, i2);
            if (iIndexOf == -1 || iIndexOf > i3) {
                iIndexOf = i3;
            }
            if (i2 >= iIndexOf) {
                iArr[i5] = 0;
            } else {
                iArr[i5] = Integer.parseInt(str.substring(i2, iIndexOf));
            }
            i2 = iIndexOf + 1;
        }
        return iArr;
    }

    public static int compareVersion(int[] iArr, int[] iArr2) {
        if (iArr == null) {
            iArr = new int[0];
        }
        if (iArr2 == null) {
            iArr2 = new int[0];
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (i2 >= iArr2.length || iArr[i2] > iArr2[i2]) {
                return 1;
            }
            if (iArr[i2] < iArr2[i2]) {
                return -1;
            }
        }
        return iArr.length == iArr2.length ? 0 : -1;
    }

    public static synchronized int compareVersion(String str, String str2) {
        return compareVersion(parseVersion(str), parseVersion(str2));
    }

    private static String compressClassName(String str) {
        if (str.startsWith(ORBConstants.SUN_LC_VERSION_PREFIX)) {
            return "(ORB)." + str.substring(ORBConstants.SUN_LC_VERSION_PREFIX.length());
        }
        return str;
    }

    public static String getThreadName(Thread thread) {
        if (thread == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        String name = thread.getName();
        StringTokenizer stringTokenizer = new StringTokenizer(name);
        int iCountTokens = stringTokenizer.countTokens();
        if (iCountTokens != 5) {
            return name;
        }
        String[] strArr = new String[iCountTokens];
        for (int i2 = 0; i2 < iCountTokens; i2++) {
            strArr[i2] = stringTokenizer.nextToken();
        }
        if (!strArr[0].equals("SelectReaderThread")) {
            return name;
        }
        return "SelectReaderThread[" + strArr[2] + CallSiteDescriptor.TOKEN_DELIMITER + strArr[3] + "]";
    }

    private static String formatStackTraceElement(StackTraceElement stackTraceElement) {
        String str;
        StringBuilder sbAppend = new StringBuilder().append(compressClassName(stackTraceElement.getClassName())).append(".").append(stackTraceElement.getMethodName());
        if (stackTraceElement.isNativeMethod()) {
            str = "(Native Method)";
        } else if (stackTraceElement.getFileName() != null && stackTraceElement.getLineNumber() >= 0) {
            str = "(" + stackTraceElement.getFileName() + CallSiteDescriptor.TOKEN_DELIMITER + stackTraceElement.getLineNumber() + ")";
        } else {
            str = stackTraceElement.getFileName() != null ? "(" + stackTraceElement.getFileName() + ")" : "(Unknown Source)";
        }
        return sbAppend.append(str).toString();
    }

    private static void printStackTrace(StackTraceElement[] stackTraceElementArr) {
        System.out.println("    Stack Trace:");
        for (int i2 = 1; i2 < stackTraceElementArr.length; i2++) {
            System.out.print("        >");
            System.out.println(formatStackTraceElement(stackTraceElementArr[i2]));
        }
    }

    public static synchronized void dprint(Object obj, String str) {
        System.out.println(compressClassName(obj.getClass().getName()) + "(" + getThreadName(Thread.currentThread()) + "): " + str);
    }

    public static synchronized void dprint(String str, String str2) {
        System.out.println(compressClassName(str) + "(" + getThreadName(Thread.currentThread()) + "): " + str2);
    }

    public synchronized void dprint(String str) {
        dprint(this, str);
    }

    public static synchronized void dprintTrace(Object obj, String str) {
        dprint(obj, str);
        printStackTrace(new Throwable().getStackTrace());
    }

    public static synchronized void dprint(Object obj, String str, Throwable th) {
        System.out.println(compressClassName(obj.getClass().getName()) + '(' + ((Object) Thread.currentThread()) + "): " + str);
        if (th != null) {
            printStackTrace(th.getStackTrace());
        }
    }

    public static String[] concatenateStringArrays(String[] strArr, String[] strArr2) {
        String[] strArr3 = new String[strArr.length + strArr2.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr3[i2] = strArr[i2];
        }
        for (int i3 = 0; i3 < strArr2.length; i3++) {
            strArr3[i3 + strArr.length] = strArr2[i3];
        }
        return strArr3;
    }

    public static void throwNotSerializableForCorba(String str) {
        throw omgWrapper.notSerializable(CompletionStatus.COMPLETED_MAYBE, str);
    }

    public static byte getMaxStreamFormatVersion() {
        try {
            ValueHandler valueHandler = (ValueHandler) AccessController.doPrivileged(new PrivilegedExceptionAction<ValueHandler>() { // from class: com.sun.corba.se.impl.orbutil.ORBUtility.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public ValueHandler run() throws Exception {
                    return Util.createValueHandler();
                }
            });
            if (!(valueHandler instanceof ValueHandlerMultiFormat)) {
                return (byte) 1;
            }
            return ((ValueHandlerMultiFormat) valueHandler).getMaximumStreamFormatVersion();
        } catch (PrivilegedActionException e2) {
            throw new InternalError(e2.getMessage());
        }
    }

    public static CorbaClientDelegate makeClientDelegate(IOR ior) {
        ORB orb = ior.getORB();
        return orb.getClientDelegateFactory().create(orb.getCorbaContactInfoListFactory().create(ior));
    }

    public static Object makeObjectReference(IOR ior) {
        CorbaClientDelegate corbaClientDelegateMakeClientDelegate = makeClientDelegate(ior);
        CORBAObjectImpl cORBAObjectImpl = new CORBAObjectImpl();
        StubAdapter.setDelegate(cORBAObjectImpl, corbaClientDelegateMakeClientDelegate);
        return cORBAObjectImpl;
    }

    public static IOR getIOR(Object object) {
        if (object == null) {
            throw wrapper.nullObjectReference();
        }
        if (StubAdapter.isStub(object)) {
            Delegate delegate = StubAdapter.getDelegate(object);
            if (delegate instanceof CorbaClientDelegate) {
                ContactInfoList contactInfoList = ((CorbaClientDelegate) delegate).getContactInfoList();
                if (contactInfoList instanceof CorbaContactInfoList) {
                    IOR targetIOR = ((CorbaContactInfoList) contactInfoList).getTargetIOR();
                    if (targetIOR == null) {
                        throw wrapper.nullIor();
                    }
                    return targetIOR;
                }
                throw new INTERNAL();
            }
            throw wrapper.objrefFromForeignOrb();
        }
        throw wrapper.localObjectNotAllowed();
    }

    public static IOR connectAndGetIOR(ORB orb, Object object) {
        IOR ior;
        try {
            ior = getIOR(object);
        } catch (BAD_OPERATION e2) {
            if (StubAdapter.isStub(object)) {
                try {
                    StubAdapter.connect(object, orb);
                } catch (RemoteException e3) {
                    throw wrapper.connectingServant(e3);
                }
            } else {
                orb.connect(object);
            }
            ior = getIOR(object);
        }
        return ior;
    }

    public static String operationNameAndRequestId(CorbaMessageMediator corbaMessageMediator) {
        return "op/" + corbaMessageMediator.getOperationName() + " id/" + corbaMessageMediator.getRequestId();
    }

    public static boolean isPrintable(char c2) {
        if (!Character.isJavaIdentifierStart(c2) && !Character.isDigit(c2)) {
            switch (Character.getType(c2)) {
            }
            return true;
        }
        return true;
    }

    public static String getClassSecurityInfo(final Class cls) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ORBUtility.3
            @Override // java.security.PrivilegedAction
            public Object run() {
                StringBuffer stringBuffer = new StringBuffer(500);
                ProtectionDomain protectionDomain = cls.getProtectionDomain();
                PermissionCollection permissions = Policy.getPolicy().getPermissions(protectionDomain);
                stringBuffer.append("\nPermissionCollection ");
                stringBuffer.append(permissions.toString());
                stringBuffer.append(protectionDomain.toString());
                return stringBuffer.toString();
            }
        });
    }
}
