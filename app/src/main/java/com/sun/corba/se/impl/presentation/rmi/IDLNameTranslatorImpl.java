package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.orbutil.ObjectUtility;
import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/IDLNameTranslatorImpl.class */
public class IDLNameTranslatorImpl implements IDLNameTranslator {
    private static final String UNDERSCORE = "_";
    private static final String INNER_CLASS_SEPARATOR = "__";
    private static final String BASE_IDL_ARRAY_ELEMENT_TYPE = "seq";
    private static final String LEADING_UNDERSCORE_CHAR = "J";
    private static final String ID_CONTAINER_CLASH_CHAR = "_";
    private static final String OVERLOADED_TYPE_SEPARATOR = "__";
    private static final String ATTRIBUTE_METHOD_CLASH_MANGLE_CHARS = "__";
    private static final String GET_ATTRIBUTE_PREFIX = "_get_";
    private static final String SET_ATTRIBUTE_PREFIX = "_set_";
    private static final String IS_ATTRIBUTE_PREFIX = "_get_";
    private Class[] interf_;
    private Map methodToIDLNameMap_;
    private Map IDLNameToMethodMap_;
    private Method[] methods_;
    private static String[] IDL_KEYWORDS = {"abstract", "any", "attribute", "boolean", "case", "char", "const", "context", "custom", "default", SchemaSymbols.ATTVAL_DOUBLE, "enum", "exception", FXMLLoader.FX_FACTORY_ATTRIBUTE, "FALSE", "fixed", SchemaSymbols.ATTVAL_FLOAT, "in", "inout", "interface", SchemaSymbols.ATTVAL_LONG, "module", "native", "Object", "octet", "oneway", "out", PolicyConstants.VISIBILITY_VALUE_PRIVATE, "public", "raises", "readonly", "sequence", SchemaSymbols.ATTVAL_SHORT, "string", "struct", "supports", "switch", "TRUE", "truncatable", "typedef", "unsigned", "union", "ValueBase", "valuetype", "void", "wchar", "wstring"};
    private static char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final String[] BASE_IDL_ARRAY_MODULE_TYPE = {"org", "omg", "boxedRMI"};
    private static Set idlKeywords_ = new HashSet();

    static {
        for (int i2 = 0; i2 < IDL_KEYWORDS.length; i2++) {
            idlKeywords_.add(IDL_KEYWORDS[i2].toUpperCase());
        }
    }

    public static IDLNameTranslator get(Class cls) {
        return new IDLNameTranslatorImpl(new Class[]{cls});
    }

    public static IDLNameTranslator get(Class[] clsArr) {
        return new IDLNameTranslatorImpl(clsArr);
    }

    public static String getExceptionId(Class cls) {
        return classToIDLType(cls).getExceptionName();
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator
    public Class[] getInterfaces() {
        return this.interf_;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator
    public Method[] getMethods() {
        return this.methods_;
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator
    public Method getMethod(String str) {
        return (Method) this.IDLNameToMethodMap_.get(str);
    }

    @Override // com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator
    public String getIDLName(Method method) {
        return (String) this.methodToIDLNameMap_.get(method);
    }

    private IDLNameTranslatorImpl(Class[] clsArr) throws SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(new DynamicAccessPermission("access"));
        }
        try {
            IDLTypesUtil iDLTypesUtil = new IDLTypesUtil();
            for (Class cls : clsArr) {
                iDLTypesUtil.validateRemoteInterface(cls);
            }
            this.interf_ = clsArr;
            buildNameTranslation();
        } catch (IDLTypeException e2) {
            IllegalStateException illegalStateException = new IllegalStateException(e2.getMessage());
            illegalStateException.initCause(e2);
            throw illegalStateException;
        }
    }

    private void buildNameTranslation() throws SecurityException {
        String str;
        HashMap map = new HashMap();
        for (int i2 = 0; i2 < this.interf_.length; i2++) {
            Class cls = this.interf_[i2];
            IDLTypesUtil iDLTypesUtil = new IDLTypesUtil();
            final Method[] methods = cls.getMethods();
            AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.presentation.rmi.IDLNameTranslatorImpl.1
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    Method.setAccessible(methods, true);
                    return null;
                }
            });
            for (Method method : methods) {
                IDLMethodInfo iDLMethodInfo = new IDLMethodInfo();
                iDLMethodInfo.method = method;
                if (iDLTypesUtil.isPropertyAccessorMethod(method, cls)) {
                    iDLMethodInfo.isProperty = true;
                    String attributeNameForProperty = iDLTypesUtil.getAttributeNameForProperty(method.getName());
                    iDLMethodInfo.originalName = attributeNameForProperty;
                    iDLMethodInfo.mangledName = attributeNameForProperty;
                } else {
                    iDLMethodInfo.isProperty = false;
                    iDLMethodInfo.originalName = method.getName();
                    iDLMethodInfo.mangledName = method.getName();
                }
                map.put(method, iDLMethodInfo);
            }
        }
        for (V v2 : map.values()) {
            Iterator it = map.values().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                IDLMethodInfo iDLMethodInfo2 = (IDLMethodInfo) it.next();
                if (v2 != iDLMethodInfo2 && !v2.originalName.equals(iDLMethodInfo2.originalName) && v2.originalName.equalsIgnoreCase(iDLMethodInfo2.originalName)) {
                    v2.mangledName = mangleCaseSensitiveCollision(v2.originalName);
                    break;
                }
            }
        }
        for (V v3 : map.values()) {
            v3.mangledName = mangleIdentifier(v3.mangledName, v3.isProperty);
        }
        for (V v4 : map.values()) {
            if (!v4.isProperty) {
                Iterator it2 = map.values().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    IDLMethodInfo iDLMethodInfo3 = (IDLMethodInfo) it2.next();
                    if (v4 != iDLMethodInfo3 && !iDLMethodInfo3.isProperty && v4.originalName.equals(iDLMethodInfo3.originalName)) {
                        v4.mangledName = mangleOverloadedMethod(v4.mangledName, v4.method);
                        break;
                    }
                }
            }
        }
        for (V v5 : map.values()) {
            if (v5.isProperty) {
                Iterator it3 = map.values().iterator();
                while (true) {
                    if (!it3.hasNext()) {
                        break;
                    }
                    IDLMethodInfo iDLMethodInfo4 = (IDLMethodInfo) it3.next();
                    if (v5 != iDLMethodInfo4 && !iDLMethodInfo4.isProperty && v5.mangledName.equals(iDLMethodInfo4.mangledName)) {
                        v5.mangledName += "__";
                        break;
                    }
                }
            }
        }
        for (int i3 = 0; i3 < this.interf_.length; i3++) {
            String mappedContainerName = getMappedContainerName(this.interf_[i3]);
            for (V v6 : map.values()) {
                if (!v6.isProperty && identifierClashesWithContainer(mappedContainerName, v6.mangledName)) {
                    v6.mangledName = mangleContainerClash(v6.mangledName);
                }
            }
        }
        this.methodToIDLNameMap_ = new HashMap();
        this.IDLNameToMethodMap_ = new HashMap();
        this.methods_ = (Method[]) map.keySet().toArray(new Method[0]);
        for (V v7 : map.values()) {
            String str2 = v7.mangledName;
            if (v7.isProperty) {
                String name = v7.method.getName();
                if (!name.startsWith("get") && name.startsWith("set")) {
                    str = SET_ATTRIBUTE_PREFIX;
                } else {
                    str = "_get_";
                }
                str2 = str + v7.mangledName;
            }
            this.methodToIDLNameMap_.put(v7.method, str2);
            if (this.IDLNameToMethodMap_.containsKey(str2)) {
                throw new IllegalStateException("Error : methods " + this.IDLNameToMethodMap_.get(str2) + " and " + ((Object) v7.method) + " both result in IDL name '" + str2 + PdfOps.SINGLE_QUOTE_TOKEN);
            }
            this.IDLNameToMethodMap_.put(str2, v7.method);
        }
    }

    private static String mangleIdentifier(String str) {
        return mangleIdentifier(str, false);
    }

    private static String mangleIdentifier(String str, boolean z2) {
        String strMangleUnicodeChars = str;
        if (hasLeadingUnderscore(strMangleUnicodeChars)) {
            strMangleUnicodeChars = mangleLeadingUnderscore(strMangleUnicodeChars);
        }
        if (!z2 && isIDLKeyword(strMangleUnicodeChars)) {
            strMangleUnicodeChars = mangleIDLKeywordClash(strMangleUnicodeChars);
        }
        if (!isIDLIdentifier(strMangleUnicodeChars)) {
            strMangleUnicodeChars = mangleUnicodeChars(strMangleUnicodeChars);
        }
        return strMangleUnicodeChars;
    }

    static boolean isIDLKeyword(String str) {
        return idlKeywords_.contains(str.toUpperCase());
    }

    static String mangleIDLKeywordClash(String str) {
        return "_" + str;
    }

    private static String mangleLeadingUnderscore(String str) {
        return "J" + str;
    }

    private static boolean hasLeadingUnderscore(String str) {
        return str.startsWith("_");
    }

    static String mangleUnicodeChars(String str) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (isIDLIdentifierChar(cCharAt)) {
                stringBuffer.append(cCharAt);
            } else {
                stringBuffer.append(charToUnicodeRepresentation(cCharAt));
            }
        }
        return stringBuffer.toString();
    }

    String mangleCaseSensitiveCollision(String str) {
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.append("_");
        boolean z2 = false;
        for (int i2 = 0; i2 < str.length(); i2++) {
            if (Character.isUpperCase(str.charAt(i2))) {
                if (z2) {
                    stringBuffer.append("_");
                }
                stringBuffer.append(i2);
                z2 = true;
            }
        }
        return stringBuffer.toString();
    }

    private static String mangleContainerClash(String str) {
        return str + "_";
    }

    private static boolean identifierClashesWithContainer(String str, String str2) {
        return str2.equalsIgnoreCase(str);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [int] */
    public static String charToUnicodeRepresentation(char c2) {
        StringBuffer stringBuffer = new StringBuffer();
        char c3 = c2;
        while (true) {
            char c4 = c3;
            if (c4 <= 0) {
                break;
            }
            stringBuffer.insert(0, HEX_DIGITS[c4 % 16]);
            c3 = c4 / 16;
        }
        int length = 4 - stringBuffer.length();
        for (int i2 = 0; i2 < length; i2++) {
            stringBuffer.insert(0, "0");
        }
        stringBuffer.insert(0, "U");
        return stringBuffer.toString();
    }

    private static boolean isIDLIdentifier(String str) {
        boolean zIsIDLIdentifierChar;
        boolean z2 = true;
        for (int i2 = 0; i2 < str.length(); i2++) {
            char cCharAt = str.charAt(i2);
            if (i2 == 0) {
                zIsIDLIdentifierChar = isIDLAlphabeticChar(cCharAt);
            } else {
                zIsIDLIdentifierChar = isIDLIdentifierChar(cCharAt);
            }
            z2 = zIsIDLIdentifierChar;
            if (!z2) {
                break;
            }
        }
        return z2;
    }

    private static boolean isIDLIdentifierChar(char c2) {
        return isIDLAlphabeticChar(c2) || isIDLDecimalDigit(c2) || isUnderscore(c2);
    }

    private static boolean isIDLAlphabeticChar(char c2) {
        return (c2 >= 'A' && c2 <= 'Z') || (c2 >= 'a' && c2 <= 'z') || (c2 >= 192 && c2 <= 255 && c2 != 215 && c2 != 247);
    }

    private static boolean isIDLDecimalDigit(char c2) {
        return c2 >= '0' && c2 <= '9';
    }

    private static boolean isUnderscore(char c2) {
        return c2 == '_';
    }

    private static String mangleOverloadedMethod(String str, Method method) {
        IDLTypesUtil iDLTypesUtil = new IDLTypesUtil();
        String str2 = str + "__";
        Class<?>[] parameterTypes = method.getParameterTypes();
        for (int i2 = 0; i2 < parameterTypes.length; i2++) {
            Class<?> cls = parameterTypes[i2];
            if (i2 > 0) {
                str2 = str2 + "__";
            }
            IDLType iDLTypeClassToIDLType = classToIDLType(cls);
            String moduleName = iDLTypeClassToIDLType.getModuleName();
            String memberName = iDLTypeClassToIDLType.getMemberName();
            String strMangleIDLKeywordClash = moduleName.length() > 0 ? moduleName + "_" + memberName : memberName;
            if (!iDLTypesUtil.isPrimitive(cls) && iDLTypesUtil.getSpecialCaseIDLTypeMapping(cls) == null && isIDLKeyword(strMangleIDLKeywordClash)) {
                strMangleIDLKeywordClash = mangleIDLKeywordClash(strMangleIDLKeywordClash);
            }
            str2 = str2 + mangleUnicodeChars(strMangleIDLKeywordClash);
        }
        return str2;
    }

    private static IDLType classToIDLType(Class cls) {
        IDLType specialCaseIDLTypeMapping;
        IDLTypesUtil iDLTypesUtil = new IDLTypesUtil();
        if (iDLTypesUtil.isPrimitive(cls)) {
            specialCaseIDLTypeMapping = iDLTypesUtil.getPrimitiveIDLTypeMapping(cls);
        } else if (cls.isArray()) {
            Class<?> componentType = cls.getComponentType();
            int i2 = 1;
            while (componentType.isArray()) {
                componentType = componentType.getComponentType();
                i2++;
            }
            IDLType iDLTypeClassToIDLType = classToIDLType(componentType);
            String[] strArr = BASE_IDL_ARRAY_MODULE_TYPE;
            if (iDLTypeClassToIDLType.hasModule()) {
                strArr = (String[]) ObjectUtility.concatenateArrays(strArr, iDLTypeClassToIDLType.getModules());
            }
            specialCaseIDLTypeMapping = new IDLType(cls, strArr, BASE_IDL_ARRAY_ELEMENT_TYPE + i2 + "_" + iDLTypeClassToIDLType.getMemberName());
        } else {
            specialCaseIDLTypeMapping = iDLTypesUtil.getSpecialCaseIDLTypeMapping(cls);
            if (specialCaseIDLTypeMapping == null) {
                String strReplaceAll = getUnmappedContainerName(cls).replaceAll("\\$", "__");
                if (hasLeadingUnderscore(strReplaceAll)) {
                    strReplaceAll = mangleLeadingUnderscore(strReplaceAll);
                }
                String packageName = getPackageName(cls);
                if (packageName == null) {
                    specialCaseIDLTypeMapping = new IDLType(cls, strReplaceAll);
                } else {
                    if (iDLTypesUtil.isEntity(cls)) {
                        packageName = "org.omg.boxedIDL." + packageName;
                    }
                    StringTokenizer stringTokenizer = new StringTokenizer(packageName, ".");
                    String[] strArr2 = new String[stringTokenizer.countTokens()];
                    int i3 = 0;
                    while (stringTokenizer.hasMoreElements()) {
                        String strNextToken = stringTokenizer.nextToken();
                        int i4 = i3;
                        i3++;
                        strArr2[i4] = hasLeadingUnderscore(strNextToken) ? mangleLeadingUnderscore(strNextToken) : strNextToken;
                    }
                    specialCaseIDLTypeMapping = new IDLType(cls, strArr2, strReplaceAll);
                }
            }
        }
        return specialCaseIDLTypeMapping;
    }

    private static String getPackageName(Class cls) {
        String strSubstring;
        Package r0 = cls.getPackage();
        if (r0 != null) {
            strSubstring = r0.getName();
        } else {
            String name = cls.getName();
            int iIndexOf = name.indexOf(46);
            strSubstring = iIndexOf == -1 ? null : name.substring(0, iIndexOf);
        }
        return strSubstring;
    }

    private static String getMappedContainerName(Class cls) {
        return mangleIdentifier(getUnmappedContainerName(cls));
    }

    private static String getUnmappedContainerName(Class cls) {
        String strSubstring;
        String packageName = getPackageName(cls);
        String name = cls.getName();
        if (packageName != null) {
            strSubstring = name.substring(packageName.length() + 1);
        } else {
            strSubstring = name;
        }
        return strSubstring;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/IDLNameTranslatorImpl$IDLMethodInfo.class */
    private static class IDLMethodInfo {
        public Method method;
        public boolean isProperty;
        public String originalName;
        public String mangledName;

        private IDLMethodInfo() {
        }
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("IDLNameTranslator[");
        for (int i2 = 0; i2 < this.interf_.length; i2++) {
            if (i2 != 0) {
                stringBuffer.append(" ");
            }
            stringBuffer.append(this.interf_[i2].getName());
        }
        stringBuffer.append("]\n");
        for (Method method : this.methodToIDLNameMap_.keySet()) {
            stringBuffer.append(((String) this.methodToIDLNameMap_.get(method)) + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) method) + "\n");
        }
        return stringBuffer.toString();
    }
}
