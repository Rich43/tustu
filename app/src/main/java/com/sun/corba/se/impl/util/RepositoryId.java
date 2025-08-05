package com.sun.corba.se.impl.util;

import com.sun.corba.se.impl.io.ObjectStreamClass;
import com.sun.corba.se.impl.io.TypeMismatchException;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.rmi.Remote;
import java.util.Hashtable;
import javax.rmi.CORBA.ClassDesc;
import javax.rmi.CORBA.Util;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.ValueBase;

/* loaded from: rt.jar:com/sun/corba/se/impl/util/RepositoryId.class */
public class RepositoryId {
    private static final byte[] IDL_IDENTIFIER_CHARS = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1};
    private static final long serialVersionUID = 123456789;
    private static String defaultServerURL;
    private static boolean useCodebaseOnly;
    private static IdentityHashtable classToRepStr;
    private static IdentityHashtable classIDLToRepStr;
    private static IdentityHashtable classSeqToRepStr;
    private static final IdentityHashtable repStrToByteArray;
    private static Hashtable repStrToClass;
    private String repId = null;
    private boolean isSupportedFormat = true;
    private String typeString = null;
    private String versionString = null;
    private boolean isSequence = false;
    private boolean isRMIValueType = false;
    private boolean isIDLType = false;
    private String completeClassName = null;
    private String unqualifiedName = null;
    private String definedInId = null;
    private Class clazz = null;
    private String suid = null;
    private String actualSuid = null;
    private long suidLong = -1;
    private long actualSuidLong = -1;
    private static final String kSequenceKeyword = "seq";
    private static final String kValuePrefix = "RMI:";
    private static final String kIDLPrefix = "IDL:";
    private static final String kIDLNamePrefix = "omg.org/";
    private static final String kIDLClassnamePrefix = "org.omg.";
    private static final String kSequencePrefix = "[";
    private static final String kCORBAPrefix = "CORBA/";
    private static final String kArrayPrefix = "RMI:[CORBA/";
    private static final int kValuePrefixLength;
    private static final int kIDLPrefixLength;
    private static final int kSequencePrefixLength;
    private static final String kInterfaceHashCode = ":0000000000000000";
    private static final String kInterfaceOnlyHashStr = "0000000000000000";
    private static final String kExternalizableHashStr = "0000000000000001";
    public static final int kInitialValueTag = 2147483392;
    public static final int kNoTypeInfo = 0;
    public static final int kSingleRepTypeInfo = 2;
    public static final int kPartialListTypeInfo = 6;
    public static final int kChunkedMask = 8;
    public static final int kPreComputed_StandardRMIUnchunked;
    public static final int kPreComputed_CodeBaseRMIUnchunked;
    public static final int kPreComputed_StandardRMIChunked;
    public static final int kPreComputed_CodeBaseRMIChunked;
    public static final int kPreComputed_StandardRMIUnchunked_NoRep;
    public static final int kPreComputed_CodeBaseRMIUnchunked_NoRep;
    public static final int kPreComputed_StandardRMIChunked_NoRep;
    public static final int kPreComputed_CodeBaseRMIChunked_NoRep;
    public static final String kWStringValueVersion = "1.0";
    public static final String kWStringValueHash = ":1.0";
    public static final String kWStringStubValue = "WStringValue";
    public static final String kWStringTypeStr = "omg.org/CORBA/WStringValue";
    public static final String kWStringValueRepID = "IDL:omg.org/CORBA/WStringValue:1.0";
    public static final String kAnyRepID = "IDL:omg.org/CORBA/Any";
    public static final String kClassDescValueHash;
    public static final String kClassDescStubValue = "ClassDesc";
    public static final String kClassDescTypeStr = "javax.rmi.CORBA.ClassDesc";
    public static final String kClassDescValueRepID;
    public static final String kObjectValueHash = ":1.0";
    public static final String kObjectStubValue = "Object";
    public static final String kSequenceValueHash = ":1.0";
    public static final String kPrimitiveSequenceValueHash = ":0000000000000000";
    public static final String kSerializableValueHash = ":1.0";
    public static final String kSerializableStubValue = "Serializable";
    public static final String kExternalizableValueHash = ":1.0";
    public static final String kExternalizableStubValue = "Externalizable";
    public static final String kRemoteValueHash = "";
    public static final String kRemoteStubValue = "";
    public static final String kRemoteTypeStr = "";
    public static final String kRemoteValueRepID = "";
    private static final Hashtable kSpecialArrayTypeStrings;
    private static final Hashtable kSpecialCasesRepIDs;
    private static final Hashtable kSpecialCasesStubValues;
    private static final Hashtable kSpecialCasesVersions;
    private static final Hashtable kSpecialCasesClasses;
    private static final Hashtable kSpecialCasesArrayPrefix;
    private static final Hashtable kSpecialPrimitives;
    private static final byte[] ASCII_HEX;
    public static final RepositoryIdCache cache;
    public static final String kjava_rmi_Remote;
    public static final String korg_omg_CORBA_Object;
    public static final Class[] kNoParamTypes;
    public static final Object[] kNoArgs;

    static {
        defaultServerURL = null;
        useCodebaseOnly = false;
        if (defaultServerURL == null) {
            defaultServerURL = JDKBridge.getLocalCodebase();
        }
        useCodebaseOnly = JDKBridge.useCodebaseOnly();
        classToRepStr = new IdentityHashtable();
        classIDLToRepStr = new IdentityHashtable();
        classSeqToRepStr = new IdentityHashtable();
        repStrToByteArray = new IdentityHashtable();
        repStrToClass = new Hashtable();
        kValuePrefixLength = kValuePrefix.length();
        kIDLPrefixLength = kIDLPrefix.length();
        kSequencePrefixLength = kSequencePrefix.length();
        kPreComputed_StandardRMIUnchunked = computeValueTag(false, 2, false);
        kPreComputed_CodeBaseRMIUnchunked = computeValueTag(true, 2, false);
        kPreComputed_StandardRMIChunked = computeValueTag(false, 2, true);
        kPreComputed_CodeBaseRMIChunked = computeValueTag(true, 2, true);
        kPreComputed_StandardRMIUnchunked_NoRep = computeValueTag(false, 0, false);
        kPreComputed_CodeBaseRMIUnchunked_NoRep = computeValueTag(true, 0, false);
        kPreComputed_StandardRMIChunked_NoRep = computeValueTag(false, 0, true);
        kPreComputed_CodeBaseRMIChunked_NoRep = computeValueTag(true, 0, true);
        kClassDescValueHash = CallSiteDescriptor.TOKEN_DELIMITER + Long.toHexString(ObjectStreamClass.getActualSerialVersionUID(ClassDesc.class)).toUpperCase() + CallSiteDescriptor.TOKEN_DELIMITER + Long.toHexString(ObjectStreamClass.getSerialVersionUID(ClassDesc.class)).toUpperCase();
        kClassDescValueRepID = "RMI:javax.rmi.CORBA.ClassDesc" + kClassDescValueHash;
        kSpecialArrayTypeStrings = new Hashtable();
        kSpecialArrayTypeStrings.put("CORBA.WStringValue", new StringBuffer(String.class.getName()));
        kSpecialArrayTypeStrings.put(kClassDescTypeStr, new StringBuffer(Class.class.getName()));
        kSpecialArrayTypeStrings.put("CORBA.Object", new StringBuffer(Remote.class.getName()));
        kSpecialCasesRepIDs = new Hashtable();
        kSpecialCasesRepIDs.put(String.class, kWStringValueRepID);
        kSpecialCasesRepIDs.put(Class.class, kClassDescValueRepID);
        kSpecialCasesRepIDs.put(Remote.class, "");
        kSpecialCasesStubValues = new Hashtable();
        kSpecialCasesStubValues.put(String.class, kWStringStubValue);
        kSpecialCasesStubValues.put(Class.class, kClassDescStubValue);
        kSpecialCasesStubValues.put(Object.class, "Object");
        kSpecialCasesStubValues.put(Serializable.class, kSerializableStubValue);
        kSpecialCasesStubValues.put(Externalizable.class, kExternalizableStubValue);
        kSpecialCasesStubValues.put(Remote.class, "");
        kSpecialCasesVersions = new Hashtable();
        kSpecialCasesVersions.put(String.class, ":1.0");
        kSpecialCasesVersions.put(Class.class, kClassDescValueHash);
        kSpecialCasesVersions.put(Object.class, ":1.0");
        kSpecialCasesVersions.put(Serializable.class, ":1.0");
        kSpecialCasesVersions.put(Externalizable.class, ":1.0");
        kSpecialCasesVersions.put(Remote.class, "");
        kSpecialCasesClasses = new Hashtable();
        kSpecialCasesClasses.put(kWStringTypeStr, String.class);
        kSpecialCasesClasses.put(kClassDescTypeStr, Class.class);
        kSpecialCasesClasses.put("", Remote.class);
        kSpecialCasesClasses.put("org.omg.CORBA.WStringValue", String.class);
        kSpecialCasesClasses.put(kClassDescTypeStr, Class.class);
        kSpecialCasesArrayPrefix = new Hashtable();
        kSpecialCasesArrayPrefix.put(String.class, kArrayPrefix);
        kSpecialCasesArrayPrefix.put(Class.class, "RMI:[javax/rmi/CORBA/");
        kSpecialCasesArrayPrefix.put(Object.class, "RMI:[java/lang/");
        kSpecialCasesArrayPrefix.put(Serializable.class, "RMI:[java/io/");
        kSpecialCasesArrayPrefix.put(Externalizable.class, "RMI:[java/io/");
        kSpecialCasesArrayPrefix.put(Remote.class, kArrayPrefix);
        kSpecialPrimitives = new Hashtable();
        kSpecialPrimitives.put("int", SchemaSymbols.ATTVAL_LONG);
        kSpecialPrimitives.put(SchemaSymbols.ATTVAL_LONG, "longlong");
        kSpecialPrimitives.put(SchemaSymbols.ATTVAL_BYTE, "octet");
        ASCII_HEX = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
        cache = new RepositoryIdCache();
        kjava_rmi_Remote = createForAnyType(Remote.class);
        korg_omg_CORBA_Object = createForAnyType(Object.class);
        kNoParamTypes = new Class[0];
        kNoArgs = new Object[0];
    }

    RepositoryId() {
    }

    RepositoryId(String str) {
        init(str);
    }

    RepositoryId init(String str) {
        this.repId = str;
        if (str.length() == 0) {
            this.clazz = Remote.class;
            this.typeString = "";
            this.isRMIValueType = true;
            this.suid = kInterfaceOnlyHashStr;
            return this;
        }
        if (str.equals(kWStringValueRepID)) {
            this.clazz = String.class;
            this.typeString = kWStringTypeStr;
            this.isIDLType = true;
            this.completeClassName = "java.lang.String";
            this.versionString = "1.0";
            return this;
        }
        String strConvertFromISOLatin1 = convertFromISOLatin1(str);
        int iIndexOf = strConvertFromISOLatin1.indexOf(58);
        if (iIndexOf == -1) {
            throw new IllegalArgumentException("RepsitoryId must have the form <type>:<body>");
        }
        int iIndexOf2 = strConvertFromISOLatin1.indexOf(58, iIndexOf + 1);
        if (iIndexOf2 == -1) {
            this.versionString = "";
        } else {
            this.versionString = strConvertFromISOLatin1.substring(iIndexOf2);
        }
        if (strConvertFromISOLatin1.startsWith(kIDLPrefix)) {
            this.typeString = strConvertFromISOLatin1.substring(kIDLPrefixLength, strConvertFromISOLatin1.indexOf(58, kIDLPrefixLength));
            this.isIDLType = true;
            if (this.typeString.startsWith(kIDLNamePrefix)) {
                this.completeClassName = "org.omg." + this.typeString.substring(kIDLNamePrefix.length()).replace('/', '.');
            } else {
                this.completeClassName = this.typeString.replace('/', '.');
            }
        } else if (strConvertFromISOLatin1.startsWith(kValuePrefix)) {
            this.typeString = strConvertFromISOLatin1.substring(kValuePrefixLength, strConvertFromISOLatin1.indexOf(58, kValuePrefixLength));
            this.isRMIValueType = true;
            if (this.versionString.indexOf(46) == -1) {
                this.actualSuid = this.versionString.substring(1);
                this.suid = this.actualSuid;
                if (this.actualSuid.indexOf(58) != -1) {
                    int iIndexOf3 = this.actualSuid.indexOf(58) + 1;
                    this.suid = this.actualSuid.substring(iIndexOf3);
                    this.actualSuid = this.actualSuid.substring(0, iIndexOf3 - 1);
                }
            }
        } else {
            this.isSupportedFormat = false;
            this.typeString = "";
        }
        if (this.typeString.startsWith(kSequencePrefix)) {
            this.isSequence = true;
        }
        return this;
    }

    public final String getUnqualifiedName() {
        if (this.unqualifiedName == null) {
            String className = getClassName();
            int iLastIndexOf = className.lastIndexOf(46);
            if (iLastIndexOf == -1) {
                this.unqualifiedName = className;
                this.definedInId = "IDL::1.0";
            } else {
                this.unqualifiedName = className.substring(iLastIndexOf);
                this.definedInId = kIDLPrefix + className.substring(0, iLastIndexOf).replace('.', '/') + ":1.0";
            }
        }
        return this.unqualifiedName;
    }

    public final String getDefinedInId() {
        if (this.definedInId == null) {
            getUnqualifiedName();
        }
        return this.definedInId;
    }

    public final String getTypeString() {
        return this.typeString;
    }

    public final String getVersionString() {
        return this.versionString;
    }

    public final String getSerialVersionUID() {
        return this.suid;
    }

    public final String getActualSerialVersionUID() {
        return this.actualSuid;
    }

    public final long getSerialVersionUIDAsLong() {
        return this.suidLong;
    }

    public final long getActualSerialVersionUIDAsLong() {
        return this.actualSuidLong;
    }

    public final boolean isRMIValueType() {
        return this.isRMIValueType;
    }

    public final boolean isIDLType() {
        return this.isIDLType;
    }

    public final String getRepositoryId() {
        return this.repId;
    }

    public static byte[] getByteArray(String str) {
        byte[] bArr;
        synchronized (repStrToByteArray) {
            bArr = (byte[]) repStrToByteArray.get(str);
        }
        return bArr;
    }

    public static void setByteArray(String str, byte[] bArr) {
        synchronized (repStrToByteArray) {
            repStrToByteArray.put(str, bArr);
        }
    }

    public final boolean isSequence() {
        return this.isSequence;
    }

    public final boolean isSupportedFormat() {
        return this.isSupportedFormat;
    }

    public final String getClassName() {
        if (this.isRMIValueType) {
            return this.typeString;
        }
        if (this.isIDLType) {
            return this.completeClassName;
        }
        return null;
    }

    public final Class getAnyClassFromType() throws ClassNotFoundException {
        try {
            return getClassFromType();
        } catch (ClassNotFoundException e2) {
            Class cls = (Class) repStrToClass.get(this.repId);
            if (cls != null) {
                return cls;
            }
            throw e2;
        }
    }

    public final Class getClassFromType() throws ClassNotFoundException {
        if (this.clazz != null) {
            return this.clazz;
        }
        Class cls = (Class) kSpecialCasesClasses.get(getClassName());
        if (cls != null) {
            this.clazz = cls;
            return cls;
        }
        try {
            return Util.loadClass(getClassName(), null, null);
        } catch (ClassNotFoundException e2) {
            if (defaultServerURL != null) {
                try {
                    return getClassFromType(defaultServerURL);
                } catch (MalformedURLException e3) {
                    throw e2;
                }
            }
            throw e2;
        }
    }

    public final Class getClassFromType(Class cls, String str) throws ClassNotFoundException {
        if (this.clazz != null) {
            return this.clazz;
        }
        Class cls2 = (Class) kSpecialCasesClasses.get(getClassName());
        if (cls2 != null) {
            this.clazz = cls2;
            return cls2;
        }
        ClassLoader classLoader = cls == null ? null : cls.getClassLoader();
        return Utility.loadClassOfType(getClassName(), str, classLoader, cls, classLoader);
    }

    public final Class getClassFromType(String str) throws MalformedURLException, ClassNotFoundException {
        return Util.loadClass(getClassName(), str, null);
    }

    public final String toString() {
        return this.repId;
    }

    public static boolean useFullValueDescription(Class cls, String str) throws IOException {
        RepositoryId id;
        RepositoryId id2;
        String strCreateForAnyType = createForAnyType(cls);
        if (strCreateForAnyType.equals(str)) {
            return false;
        }
        synchronized (cache) {
            id = cache.getId(str);
            id2 = cache.getId(strCreateForAnyType);
        }
        if (id.isRMIValueType() && id2.isRMIValueType()) {
            if (!id.getSerialVersionUID().equals(id2.getSerialVersionUID())) {
                throw new IOException("Mismatched serialization UIDs : Source (Rep. ID" + ((Object) id2) + ") = " + id2.getSerialVersionUID() + " whereas Target (Rep. ID " + str + ") = " + id.getSerialVersionUID());
            }
            return true;
        }
        throw new IOException("The repository ID is not of an RMI value type (Expected ID = " + strCreateForAnyType + "; Received ID = " + str + ")");
    }

    private static String createHashString(Serializable serializable) {
        return createHashString((Class) serializable.getClass());
    }

    private static String createHashString(Class cls) {
        String upperCase;
        String str;
        String upperCase2;
        if (cls.isInterface() || !Serializable.class.isAssignableFrom(cls)) {
            return ":0000000000000000";
        }
        long actualSerialVersionUID = ObjectStreamClass.getActualSerialVersionUID(cls);
        if (actualSerialVersionUID == 0) {
            upperCase = kInterfaceOnlyHashStr;
        } else if (actualSerialVersionUID == 1) {
            upperCase = kExternalizableHashStr;
        } else {
            upperCase = Long.toHexString(actualSerialVersionUID).toUpperCase();
        }
        while (true) {
            str = upperCase;
            if (str.length() >= 16) {
                break;
            }
            upperCase = "0" + str;
        }
        long serialVersionUID2 = ObjectStreamClass.getSerialVersionUID(cls);
        if (serialVersionUID2 == 0) {
            upperCase2 = kInterfaceOnlyHashStr;
        } else if (serialVersionUID2 == 1) {
            upperCase2 = kExternalizableHashStr;
        } else {
            upperCase2 = Long.toHexString(serialVersionUID2).toUpperCase();
        }
        while (true) {
            String str2 = upperCase2;
            if (str2.length() < 16) {
                upperCase2 = "0" + str2;
            } else {
                return CallSiteDescriptor.TOKEN_DELIMITER + (str + CallSiteDescriptor.TOKEN_DELIMITER + str2);
            }
        }
    }

    public static String createSequenceRepID(Object obj) {
        return createSequenceRepID((Class) obj.getClass());
    }

    public static String createSequenceRepID(Class cls) {
        String string;
        synchronized (classSeqToRepStr) {
            String str = (String) classSeqToRepStr.get(cls);
            if (str != null) {
                return str;
            }
            int i2 = 0;
            while (true) {
                Class<?> componentType = cls.getComponentType();
                if (componentType == null) {
                    break;
                }
                i2++;
                cls = componentType;
            }
            if (cls.isPrimitive()) {
                string = kValuePrefix + cls.getName() + ":0000000000000000";
            } else {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append(kValuePrefix);
                while (true) {
                    int i3 = i2;
                    i2--;
                    if (i3 <= 0) {
                        break;
                    }
                    stringBuffer.append(kSequencePrefix);
                }
                stringBuffer.append("L");
                stringBuffer.append(convertToISOLatin1(cls.getName()));
                stringBuffer.append(";");
                stringBuffer.append(createHashString(cls));
                string = stringBuffer.toString();
            }
            classSeqToRepStr.put(cls, string);
            return string;
        }
    }

    public static String createForSpecialCase(Class cls) {
        if (cls.isArray()) {
            return createSequenceRepID(cls);
        }
        return (String) kSpecialCasesRepIDs.get(cls);
    }

    public static String createForSpecialCase(Serializable serializable) {
        Class<?> cls = serializable.getClass();
        if (cls.isArray()) {
            return createSequenceRepID(serializable);
        }
        return createForSpecialCase((Class) cls);
    }

    public static String createForJavaType(Serializable serializable) throws TypeMismatchException {
        synchronized (classToRepStr) {
            String strCreateForSpecialCase = createForSpecialCase(serializable);
            if (strCreateForSpecialCase != null) {
                return strCreateForSpecialCase;
            }
            Class<?> cls = serializable.getClass();
            String str = (String) classToRepStr.get(cls);
            if (str != null) {
                return str;
            }
            String str2 = kValuePrefix + convertToISOLatin1(cls.getName()) + createHashString((Class) cls);
            classToRepStr.put(cls, str2);
            repStrToClass.put(str2, cls);
            return str2;
        }
    }

    public static String createForJavaType(Class cls) throws TypeMismatchException {
        synchronized (classToRepStr) {
            String strCreateForSpecialCase = createForSpecialCase(cls);
            if (strCreateForSpecialCase != null) {
                return strCreateForSpecialCase;
            }
            String str = (String) classToRepStr.get(cls);
            if (str != null) {
                return str;
            }
            String str2 = kValuePrefix + convertToISOLatin1(cls.getName()) + createHashString(cls);
            classToRepStr.put(cls, str2);
            repStrToClass.put(str2, cls);
            return str2;
        }
    }

    public static String createForIDLType(Class cls, int i2, int i3) throws TypeMismatchException {
        synchronized (classIDLToRepStr) {
            String str = (String) classIDLToRepStr.get(cls);
            if (str != null) {
                return str;
            }
            String str2 = kIDLPrefix + convertToISOLatin1(cls.getName()).replace('.', '/') + CallSiteDescriptor.TOKEN_DELIMITER + i2 + "." + i3;
            classIDLToRepStr.put(cls, str2);
            return str2;
        }
    }

    private static String getIdFromHelper(Class cls) {
        try {
            return (String) Utility.loadClassForClass(cls.getName() + "Helper", null, cls.getClassLoader(), cls, cls.getClassLoader()).getDeclaredMethod("id", kNoParamTypes).invoke(null, kNoArgs);
        } catch (ClassNotFoundException e2) {
            throw new MARSHAL(e2.toString());
        } catch (IllegalAccessException e3) {
            throw new MARSHAL(e3.toString());
        } catch (NoSuchMethodException e4) {
            throw new MARSHAL(e4.toString());
        } catch (InvocationTargetException e5) {
            throw new MARSHAL(e5.toString());
        }
    }

    public static String createForAnyType(Class cls) {
        try {
            if (cls.isArray()) {
                return createSequenceRepID(cls);
            }
            if (IDLEntity.class.isAssignableFrom(cls)) {
                try {
                    return getIdFromHelper(cls);
                } catch (Throwable th) {
                    return createForIDLType(cls, 1, 0);
                }
            }
            return createForJavaType(cls);
        } catch (TypeMismatchException e2) {
            return null;
        }
    }

    public static boolean isAbstractBase(Class cls) {
        return cls.isInterface() && IDLEntity.class.isAssignableFrom(cls) && !ValueBase.class.isAssignableFrom(cls) && !Object.class.isAssignableFrom(cls);
    }

    public static boolean isAnyRequired(Class cls) {
        return cls == Object.class || cls == Serializable.class || cls == Externalizable.class;
    }

    public static long fromHex(String str) {
        if (str.startsWith("0x")) {
            return Long.valueOf(str.substring(2), 16).longValue();
        }
        return Long.valueOf(str, 16).longValue();
    }

    public static String convertToISOLatin1(String str) {
        int length = str.length();
        if (length == 0) {
            return str;
        }
        StringBuffer stringBuffer = null;
        for (int i2 = 0; i2 < length; i2++) {
            char cCharAt = str.charAt(i2);
            if (cCharAt > 255 || IDL_IDENTIFIER_CHARS[cCharAt] == 0) {
                if (stringBuffer == null) {
                    stringBuffer = new StringBuffer(str.substring(0, i2));
                }
                stringBuffer.append("\\U" + ((char) ASCII_HEX[(cCharAt & 61440) >>> 12]) + ((char) ASCII_HEX[(cCharAt & 3840) >>> 8]) + ((char) ASCII_HEX[(cCharAt & 240) >>> 4]) + ((char) ASCII_HEX[cCharAt & 15]));
            } else if (stringBuffer != null) {
                stringBuffer.append(cCharAt);
            }
        }
        if (stringBuffer != null) {
            str = stringBuffer.toString();
        }
        return str;
    }

    private static String convertFromISOLatin1(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        while (true) {
            int iIndexOf = stringBuffer.toString().indexOf("\\U");
            if (iIndexOf != -1) {
                String str2 = "0000" + stringBuffer.toString().substring(iIndexOf + 2, iIndexOf + 6);
                byte[] bArr = new byte[(str2.length() - 4) / 2];
                int i2 = 4;
                int i3 = 0;
                while (i2 < str2.length()) {
                    bArr[i3] = (byte) ((Utility.hexOf(str2.charAt(i2)) << 4) & 240);
                    int i4 = i3;
                    bArr[i4] = (byte) (bArr[i4] | ((byte) ((Utility.hexOf(str2.charAt(i2 + 1)) << 0) & 15)));
                    i2 += 2;
                    i3++;
                }
                stringBuffer = new StringBuffer(delete(stringBuffer.toString(), iIndexOf, iIndexOf + 6));
                stringBuffer.insert(iIndexOf, (char) bArr[1]);
            } else {
                return stringBuffer.toString();
            }
        }
    }

    private static String delete(String str, int i2, int i3) {
        return str.substring(0, i2) + str.substring(i3, str.length());
    }

    private static String replace(String str, String str2, String str3) {
        int iIndexOf = str.indexOf(str2);
        while (true) {
            int i2 = iIndexOf;
            if (i2 != -1) {
                str = new String(str.substring(0, i2) + str3 + str.substring(i2 + str2.length()));
                iIndexOf = str.indexOf(str2);
            } else {
                return str;
            }
        }
    }

    public static int computeValueTag(boolean z2, int i2, boolean z3) {
        int i3 = 2147483392;
        if (z2) {
            i3 = 2147483392 | 1;
        }
        int i4 = i3 | i2;
        if (z3) {
            i4 |= 8;
        }
        return i4;
    }

    public static boolean isCodeBasePresent(int i2) {
        return (i2 & 1) == 1;
    }

    public static int getTypeInfo(int i2) {
        return i2 & 6;
    }

    public static boolean isChunkedEncoding(int i2) {
        return (i2 & 8) != 0;
    }

    public static String getServerURL() {
        return defaultServerURL;
    }
}
