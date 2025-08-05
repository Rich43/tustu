package sun.misc;

import com.sun.java.util.jar.pack.Constants;
import com.sun.javafx.fxml.BeanAdapter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import org.icepdf.core.util.PdfOps;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:sun/misc/ProxyGenerator.class */
public class ProxyGenerator {
    private static final int CLASSFILE_MAJOR_VERSION = 49;
    private static final int CLASSFILE_MINOR_VERSION = 0;
    private static final int CONSTANT_UTF8 = 1;
    private static final int CONSTANT_UNICODE = 2;
    private static final int CONSTANT_INTEGER = 3;
    private static final int CONSTANT_FLOAT = 4;
    private static final int CONSTANT_LONG = 5;
    private static final int CONSTANT_DOUBLE = 6;
    private static final int CONSTANT_CLASS = 7;
    private static final int CONSTANT_STRING = 8;
    private static final int CONSTANT_FIELD = 9;
    private static final int CONSTANT_METHOD = 10;
    private static final int CONSTANT_INTERFACEMETHOD = 11;
    private static final int CONSTANT_NAMEANDTYPE = 12;
    private static final int ACC_PUBLIC = 1;
    private static final int ACC_PRIVATE = 2;
    private static final int ACC_STATIC = 8;
    private static final int ACC_FINAL = 16;
    private static final int ACC_SUPER = 32;
    private static final int opc_aconst_null = 1;
    private static final int opc_iconst_0 = 3;
    private static final int opc_bipush = 16;
    private static final int opc_sipush = 17;
    private static final int opc_ldc = 18;
    private static final int opc_ldc_w = 19;
    private static final int opc_iload = 21;
    private static final int opc_lload = 22;
    private static final int opc_fload = 23;
    private static final int opc_dload = 24;
    private static final int opc_aload = 25;
    private static final int opc_iload_0 = 26;
    private static final int opc_lload_0 = 30;
    private static final int opc_fload_0 = 34;
    private static final int opc_dload_0 = 38;
    private static final int opc_aload_0 = 42;
    private static final int opc_astore = 58;
    private static final int opc_astore_0 = 75;
    private static final int opc_aastore = 83;
    private static final int opc_pop = 87;
    private static final int opc_dup = 89;
    private static final int opc_ireturn = 172;
    private static final int opc_lreturn = 173;
    private static final int opc_freturn = 174;
    private static final int opc_dreturn = 175;
    private static final int opc_areturn = 176;
    private static final int opc_return = 177;
    private static final int opc_getstatic = 178;
    private static final int opc_putstatic = 179;
    private static final int opc_getfield = 180;
    private static final int opc_invokevirtual = 182;
    private static final int opc_invokespecial = 183;
    private static final int opc_invokestatic = 184;
    private static final int opc_invokeinterface = 185;
    private static final int opc_new = 187;
    private static final int opc_anewarray = 189;
    private static final int opc_athrow = 191;
    private static final int opc_checkcast = 192;
    private static final int opc_wide = 196;
    private static final String superclassName = "java/lang/reflect/Proxy";
    private static final String handlerFieldName = "h";
    private static final boolean saveGeneratedFiles;
    private static Method hashCodeMethod;
    private static Method equalsMethod;
    private static Method toStringMethod;
    private String className;
    private Class<?>[] interfaces;
    private int accessFlags;
    private ConstantPool cp = new ConstantPool();
    private List<FieldInfo> fields = new ArrayList();
    private List<MethodInfo> methods = new ArrayList();
    private Map<String, List<ProxyMethod>> proxyMethods = new HashMap();
    private int proxyMethodCount = 0;
    static final /* synthetic */ boolean $assertionsDisabled;

    static /* synthetic */ int access$408(ProxyGenerator proxyGenerator) {
        int i2 = proxyGenerator.proxyMethodCount;
        proxyGenerator.proxyMethodCount = i2 + 1;
        return i2;
    }

    static {
        $assertionsDisabled = !ProxyGenerator.class.desiredAssertionStatus();
        saveGeneratedFiles = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("sun.misc.ProxyGenerator.saveGeneratedFiles"))).booleanValue();
        try {
            hashCodeMethod = Object.class.getMethod("hashCode", new Class[0]);
            equalsMethod = Object.class.getMethod("equals", Object.class);
            toStringMethod = Object.class.getMethod("toString", new Class[0]);
        } catch (NoSuchMethodException e2) {
            throw new NoSuchMethodError(e2.getMessage());
        }
    }

    public static byte[] generateProxyClass(String str, Class<?>[] clsArr) {
        return generateProxyClass(str, clsArr, 49);
    }

    public static byte[] generateProxyClass(final String str, Class<?>[] clsArr, int i2) throws SecurityException {
        final byte[] bArrGenerateClassFile = new ProxyGenerator(str, clsArr, i2).generateClassFile();
        if (saveGeneratedFiles) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.misc.ProxyGenerator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    Path pathResolve;
                    try {
                        int iLastIndexOf = str.lastIndexOf(46);
                        if (iLastIndexOf > 0) {
                            Path path = Paths.get(str.substring(0, iLastIndexOf).replace('.', File.separatorChar), new String[0]);
                            Files.createDirectories(path, new FileAttribute[0]);
                            pathResolve = path.resolve(str.substring(iLastIndexOf + 1, str.length()) + ".class");
                        } else {
                            pathResolve = Paths.get(str + ".class", new String[0]);
                        }
                        Files.write(pathResolve, bArrGenerateClassFile, new OpenOption[0]);
                        return null;
                    } catch (IOException e2) {
                        throw new InternalError("I/O exception saving generated file: " + ((Object) e2));
                    }
                }
            });
        }
        return bArrGenerateClassFile;
    }

    private ProxyGenerator(String str, Class<?>[] clsArr, int i2) {
        this.className = str;
        this.interfaces = clsArr;
        this.accessFlags = i2;
    }

    private byte[] generateClassFile() throws SecurityException {
        addProxyMethod(hashCodeMethod, Object.class);
        addProxyMethod(equalsMethod, Object.class);
        addProxyMethod(toStringMethod, Object.class);
        for (Class<?> cls : this.interfaces) {
            for (Method method : cls.getMethods()) {
                addProxyMethod(method, cls);
            }
        }
        Iterator<List<ProxyMethod>> it = this.proxyMethods.values().iterator();
        while (it.hasNext()) {
            checkReturnTypes(it.next());
        }
        try {
            this.methods.add(generateConstructor());
            Iterator<List<ProxyMethod>> it2 = this.proxyMethods.values().iterator();
            while (it2.hasNext()) {
                for (ProxyMethod proxyMethod : it2.next()) {
                    this.fields.add(new FieldInfo(proxyMethod.methodFieldName, "Ljava/lang/reflect/Method;", 10));
                    this.methods.add(proxyMethod.generateMethod());
                }
            }
            this.methods.add(generateStaticInitializer());
            if (this.methods.size() > 65535) {
                throw new IllegalArgumentException("method limit exceeded");
            }
            if (this.fields.size() > 65535) {
                throw new IllegalArgumentException("field limit exceeded");
            }
            this.cp.getClass(dotToSlash(this.className));
            this.cp.getClass(superclassName);
            for (Class<?> cls2 : this.interfaces) {
                this.cp.getClass(dotToSlash(cls2.getName()));
            }
            this.cp.setReadOnly();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            try {
                dataOutputStream.writeInt(Constants.JAVA_MAGIC);
                dataOutputStream.writeShort(0);
                dataOutputStream.writeShort(49);
                this.cp.write(dataOutputStream);
                dataOutputStream.writeShort(this.accessFlags);
                dataOutputStream.writeShort(this.cp.getClass(dotToSlash(this.className)));
                dataOutputStream.writeShort(this.cp.getClass(superclassName));
                dataOutputStream.writeShort(this.interfaces.length);
                for (Class<?> cls3 : this.interfaces) {
                    dataOutputStream.writeShort(this.cp.getClass(dotToSlash(cls3.getName())));
                }
                dataOutputStream.writeShort(this.fields.size());
                Iterator<FieldInfo> it3 = this.fields.iterator();
                while (it3.hasNext()) {
                    it3.next().write(dataOutputStream);
                }
                dataOutputStream.writeShort(this.methods.size());
                Iterator<MethodInfo> it4 = this.methods.iterator();
                while (it4.hasNext()) {
                    it4.next().write(dataOutputStream);
                }
                dataOutputStream.writeShort(0);
                return byteArrayOutputStream.toByteArray();
            } catch (IOException e2) {
                throw new InternalError("unexpected I/O Exception", e2);
            }
        } catch (IOException e3) {
            throw new InternalError("unexpected I/O Exception", e3);
        }
    }

    private void addProxyMethod(Method method, Class<?> cls) {
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        Class<?> returnType = method.getReturnType();
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        String str = name + getParameterDescriptors(parameterTypes);
        List<ProxyMethod> arrayList = this.proxyMethods.get(str);
        if (arrayList != null) {
            for (ProxyMethod proxyMethod : arrayList) {
                if (returnType == proxyMethod.returnType) {
                    ArrayList arrayList2 = new ArrayList();
                    collectCompatibleTypes(exceptionTypes, proxyMethod.exceptionTypes, arrayList2);
                    collectCompatibleTypes(proxyMethod.exceptionTypes, exceptionTypes, arrayList2);
                    proxyMethod.exceptionTypes = new Class[arrayList2.size()];
                    proxyMethod.exceptionTypes = (Class[]) arrayList2.toArray(proxyMethod.exceptionTypes);
                    return;
                }
            }
        } else {
            arrayList = new ArrayList(3);
            this.proxyMethods.put(str, arrayList);
        }
        arrayList.add(new ProxyMethod(name, parameterTypes, returnType, exceptionTypes, cls));
    }

    private static void checkReturnTypes(List<ProxyMethod> list) {
        if (list.size() < 2) {
            return;
        }
        LinkedList linkedList = new LinkedList();
        for (ProxyMethod proxyMethod : list) {
            Class<?> cls = proxyMethod.returnType;
            if (cls.isPrimitive()) {
                throw new IllegalArgumentException("methods with same signature " + getFriendlyMethodSignature(proxyMethod.methodName, proxyMethod.parameterTypes) + " but incompatible return types: " + cls.getName() + " and others");
            }
            boolean z2 = false;
            ListIterator<E> listIterator = linkedList.listIterator();
            while (true) {
                if (listIterator.hasNext()) {
                    Class<?> cls2 = (Class) listIterator.next();
                    if (cls.isAssignableFrom(cls2)) {
                        if (!$assertionsDisabled && z2) {
                            throw new AssertionError();
                        }
                    } else if (cls2.isAssignableFrom(cls)) {
                        if (!z2) {
                            listIterator.set(cls);
                            z2 = true;
                        } else {
                            listIterator.remove();
                        }
                    }
                } else if (!z2) {
                    linkedList.add(cls);
                }
            }
        }
        if (linkedList.size() > 1) {
            ProxyMethod proxyMethod2 = list.get(0);
            throw new IllegalArgumentException("methods with same signature " + getFriendlyMethodSignature(proxyMethod2.methodName, proxyMethod2.parameterTypes) + " but incompatible return types: " + ((Object) linkedList));
        }
    }

    /* loaded from: rt.jar:sun/misc/ProxyGenerator$FieldInfo.class */
    private class FieldInfo {
        public int accessFlags;
        public String name;
        public String descriptor;

        public FieldInfo(String str, String str2, int i2) {
            this.name = str;
            this.descriptor = str2;
            this.accessFlags = i2;
            ProxyGenerator.this.cp.getUtf8(str);
            ProxyGenerator.this.cp.getUtf8(str2);
        }

        public void write(DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(this.accessFlags);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.name));
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.descriptor));
            dataOutputStream.writeShort(0);
        }
    }

    /* loaded from: rt.jar:sun/misc/ProxyGenerator$ExceptionTableEntry.class */
    private static class ExceptionTableEntry {
        public short startPc;
        public short endPc;
        public short handlerPc;
        public short catchType;

        public ExceptionTableEntry(short s2, short s3, short s4, short s5) {
            this.startPc = s2;
            this.endPc = s3;
            this.handlerPc = s4;
            this.catchType = s5;
        }
    }

    /* loaded from: rt.jar:sun/misc/ProxyGenerator$MethodInfo.class */
    private class MethodInfo {
        public int accessFlags;
        public String name;
        public String descriptor;
        public short maxStack;
        public short maxLocals;
        public ByteArrayOutputStream code = new ByteArrayOutputStream();
        public List<ExceptionTableEntry> exceptionTable = new ArrayList();
        public short[] declaredExceptions;

        public MethodInfo(String str, String str2, int i2) {
            this.name = str;
            this.descriptor = str2;
            this.accessFlags = i2;
            ProxyGenerator.this.cp.getUtf8(str);
            ProxyGenerator.this.cp.getUtf8(str2);
            ProxyGenerator.this.cp.getUtf8("Code");
            ProxyGenerator.this.cp.getUtf8("Exceptions");
        }

        public void write(DataOutputStream dataOutputStream) throws IOException {
            dataOutputStream.writeShort(this.accessFlags);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.name));
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.descriptor));
            dataOutputStream.writeShort(2);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8("Code"));
            dataOutputStream.writeInt(12 + this.code.size() + (8 * this.exceptionTable.size()));
            dataOutputStream.writeShort(this.maxStack);
            dataOutputStream.writeShort(this.maxLocals);
            dataOutputStream.writeInt(this.code.size());
            this.code.writeTo(dataOutputStream);
            dataOutputStream.writeShort(this.exceptionTable.size());
            for (ExceptionTableEntry exceptionTableEntry : this.exceptionTable) {
                dataOutputStream.writeShort(exceptionTableEntry.startPc);
                dataOutputStream.writeShort(exceptionTableEntry.endPc);
                dataOutputStream.writeShort(exceptionTableEntry.handlerPc);
                dataOutputStream.writeShort(exceptionTableEntry.catchType);
            }
            dataOutputStream.writeShort(0);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8("Exceptions"));
            dataOutputStream.writeInt(2 + (2 * this.declaredExceptions.length));
            dataOutputStream.writeShort(this.declaredExceptions.length);
            for (short s2 : this.declaredExceptions) {
                dataOutputStream.writeShort(s2);
            }
        }
    }

    /* loaded from: rt.jar:sun/misc/ProxyGenerator$ProxyMethod.class */
    private class ProxyMethod {
        public String methodName;
        public Class<?>[] parameterTypes;
        public Class<?> returnType;
        public Class<?>[] exceptionTypes;
        public Class<?> fromClass;
        public String methodFieldName;

        private ProxyMethod(String str, Class<?>[] clsArr, Class<?> cls, Class<?>[] clsArr2, Class<?> cls2) {
            this.methodName = str;
            this.parameterTypes = clsArr;
            this.returnType = cls;
            this.exceptionTypes = clsArr2;
            this.fromClass = cls2;
            this.methodFieldName = PdfOps.m_TOKEN + ProxyGenerator.access$408(ProxyGenerator.this);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public MethodInfo generateMethod() throws IOException {
            MethodInfo methodInfo = ProxyGenerator.this.new MethodInfo(this.methodName, ProxyGenerator.getMethodDescriptor(this.parameterTypes, this.returnType), 17);
            int[] iArr = new int[this.parameterTypes.length];
            int wordsPerType = 1;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                iArr[i2] = wordsPerType;
                wordsPerType += ProxyGenerator.getWordsPerType(this.parameterTypes[i2]);
            }
            int i3 = wordsPerType;
            DataOutputStream dataOutputStream = new DataOutputStream(methodInfo.code);
            ProxyGenerator.this.code_aload(0, dataOutputStream);
            dataOutputStream.writeByte(180);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(ProxyGenerator.superclassName, "h", "Ljava/lang/reflect/InvocationHandler;"));
            ProxyGenerator.this.code_aload(0, dataOutputStream);
            dataOutputStream.writeByte(178);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(ProxyGenerator.dotToSlash(ProxyGenerator.this.className), this.methodFieldName, "Ljava/lang/reflect/Method;"));
            if (this.parameterTypes.length > 0) {
                ProxyGenerator.this.code_ipush(this.parameterTypes.length, dataOutputStream);
                dataOutputStream.writeByte(189);
                dataOutputStream.writeShort(ProxyGenerator.this.cp.getClass("java/lang/Object"));
                for (int i4 = 0; i4 < this.parameterTypes.length; i4++) {
                    dataOutputStream.writeByte(89);
                    ProxyGenerator.this.code_ipush(i4, dataOutputStream);
                    codeWrapArgument(this.parameterTypes[i4], iArr[i4], dataOutputStream);
                    dataOutputStream.writeByte(83);
                }
            } else {
                dataOutputStream.writeByte(1);
            }
            dataOutputStream.writeByte(185);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getInterfaceMethodRef("java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;"));
            dataOutputStream.writeByte(4);
            dataOutputStream.writeByte(0);
            if (this.returnType == Void.TYPE) {
                dataOutputStream.writeByte(87);
                dataOutputStream.writeByte(177);
            } else {
                codeUnwrapReturnValue(this.returnType, dataOutputStream);
            }
            short size = (short) methodInfo.code.size();
            List listComputeUniqueCatchList = ProxyGenerator.computeUniqueCatchList(this.exceptionTypes);
            if (listComputeUniqueCatchList.size() > 0) {
                Iterator it = listComputeUniqueCatchList.iterator();
                while (it.hasNext()) {
                    methodInfo.exceptionTable.add(new ExceptionTableEntry((short) 0, size, size, ProxyGenerator.this.cp.getClass(ProxyGenerator.dotToSlash(((Class) it.next()).getName()))));
                }
                dataOutputStream.writeByte(191);
                methodInfo.exceptionTable.add(new ExceptionTableEntry((short) 0, size, (short) methodInfo.code.size(), ProxyGenerator.this.cp.getClass("java/lang/Throwable")));
                ProxyGenerator.this.code_astore(i3, dataOutputStream);
                dataOutputStream.writeByte(187);
                dataOutputStream.writeShort(ProxyGenerator.this.cp.getClass("java/lang/reflect/UndeclaredThrowableException"));
                dataOutputStream.writeByte(89);
                ProxyGenerator.this.code_aload(i3, dataOutputStream);
                dataOutputStream.writeByte(183);
                dataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef("java/lang/reflect/UndeclaredThrowableException", com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/lang/Throwable;)V"));
                dataOutputStream.writeByte(191);
            }
            if (methodInfo.code.size() > 65535) {
                throw new IllegalArgumentException("code size limit exceeded");
            }
            methodInfo.maxStack = (short) 10;
            methodInfo.maxLocals = (short) (i3 + 1);
            methodInfo.declaredExceptions = new short[this.exceptionTypes.length];
            for (int i5 = 0; i5 < this.exceptionTypes.length; i5++) {
                methodInfo.declaredExceptions[i5] = ProxyGenerator.this.cp.getClass(ProxyGenerator.dotToSlash(this.exceptionTypes[i5].getName()));
            }
            return methodInfo;
        }

        private void codeWrapArgument(Class<?> cls, int i2, DataOutputStream dataOutputStream) throws IOException {
            if (!cls.isPrimitive()) {
                ProxyGenerator.this.code_aload(i2, dataOutputStream);
                return;
            }
            PrimitiveTypeInfo primitiveTypeInfo = PrimitiveTypeInfo.get(cls);
            if (cls == Integer.TYPE || cls == Boolean.TYPE || cls == Byte.TYPE || cls == Character.TYPE || cls == Short.TYPE) {
                ProxyGenerator.this.code_iload(i2, dataOutputStream);
            } else if (cls == Long.TYPE) {
                ProxyGenerator.this.code_lload(i2, dataOutputStream);
            } else if (cls == Float.TYPE) {
                ProxyGenerator.this.code_fload(i2, dataOutputStream);
            } else if (cls == Double.TYPE) {
                ProxyGenerator.this.code_dload(i2, dataOutputStream);
            } else {
                throw new AssertionError();
            }
            dataOutputStream.writeByte(184);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef(primitiveTypeInfo.wrapperClassName, BeanAdapter.VALUE_OF_METHOD_NAME, primitiveTypeInfo.wrapperValueOfDesc));
        }

        private void codeUnwrapReturnValue(Class<?> cls, DataOutputStream dataOutputStream) throws IOException {
            if (cls.isPrimitive()) {
                PrimitiveTypeInfo primitiveTypeInfo = PrimitiveTypeInfo.get(cls);
                dataOutputStream.writeByte(192);
                dataOutputStream.writeShort(ProxyGenerator.this.cp.getClass(primitiveTypeInfo.wrapperClassName));
                dataOutputStream.writeByte(182);
                dataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef(primitiveTypeInfo.wrapperClassName, primitiveTypeInfo.unwrapMethodName, primitiveTypeInfo.unwrapMethodDesc));
                if (cls == Integer.TYPE || cls == Boolean.TYPE || cls == Byte.TYPE || cls == Character.TYPE || cls == Short.TYPE) {
                    dataOutputStream.writeByte(172);
                    return;
                }
                if (cls == Long.TYPE) {
                    dataOutputStream.writeByte(173);
                    return;
                } else if (cls == Float.TYPE) {
                    dataOutputStream.writeByte(174);
                    return;
                } else {
                    if (cls == Double.TYPE) {
                        dataOutputStream.writeByte(175);
                        return;
                    }
                    throw new AssertionError();
                }
            }
            dataOutputStream.writeByte(192);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getClass(ProxyGenerator.dotToSlash(cls.getName())));
            dataOutputStream.writeByte(176);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void codeFieldInitialization(DataOutputStream dataOutputStream) throws IOException {
            ProxyGenerator.this.codeClassForName(this.fromClass, dataOutputStream);
            ProxyGenerator.this.code_ldc(ProxyGenerator.this.cp.getString(this.methodName), dataOutputStream);
            ProxyGenerator.this.code_ipush(this.parameterTypes.length, dataOutputStream);
            dataOutputStream.writeByte(189);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getClass("java/lang/Class"));
            for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
                dataOutputStream.writeByte(89);
                ProxyGenerator.this.code_ipush(i2, dataOutputStream);
                if (!this.parameterTypes[i2].isPrimitive()) {
                    ProxyGenerator.this.codeClassForName(this.parameterTypes[i2], dataOutputStream);
                } else {
                    PrimitiveTypeInfo primitiveTypeInfo = PrimitiveTypeInfo.get(this.parameterTypes[i2]);
                    dataOutputStream.writeByte(178);
                    dataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(primitiveTypeInfo.wrapperClassName, "TYPE", "Ljava/lang/Class;"));
                }
                dataOutputStream.writeByte(83);
            }
            dataOutputStream.writeByte(182);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"));
            dataOutputStream.writeByte(179);
            dataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(ProxyGenerator.dotToSlash(ProxyGenerator.this.className), this.methodFieldName, "Ljava/lang/reflect/Method;"));
        }
    }

    private MethodInfo generateConstructor() throws IOException {
        MethodInfo methodInfo = new MethodInfo(com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/lang/reflect/InvocationHandler;)V", 1);
        DataOutputStream dataOutputStream = new DataOutputStream(methodInfo.code);
        code_aload(0, dataOutputStream);
        code_aload(1, dataOutputStream);
        dataOutputStream.writeByte(183);
        dataOutputStream.writeShort(this.cp.getMethodRef(superclassName, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/lang/reflect/InvocationHandler;)V"));
        dataOutputStream.writeByte(177);
        methodInfo.maxStack = (short) 10;
        methodInfo.maxLocals = (short) 2;
        methodInfo.declaredExceptions = new short[0];
        return methodInfo;
    }

    private MethodInfo generateStaticInitializer() throws IOException {
        MethodInfo methodInfo = new MethodInfo(com.sun.org.apache.bcel.internal.Constants.STATIC_INITIALIZER_NAME, "()V", 8);
        DataOutputStream dataOutputStream = new DataOutputStream(methodInfo.code);
        Iterator<List<ProxyMethod>> it = this.proxyMethods.values().iterator();
        while (it.hasNext()) {
            Iterator<ProxyMethod> it2 = it.next().iterator();
            while (it2.hasNext()) {
                it2.next().codeFieldInitialization(dataOutputStream);
            }
        }
        dataOutputStream.writeByte(177);
        short size = (short) methodInfo.code.size();
        methodInfo.exceptionTable.add(new ExceptionTableEntry((short) 0, size, size, this.cp.getClass("java/lang/NoSuchMethodException")));
        code_astore(1, dataOutputStream);
        dataOutputStream.writeByte(187);
        dataOutputStream.writeShort(this.cp.getClass("java/lang/NoSuchMethodError"));
        dataOutputStream.writeByte(89);
        code_aload(1, dataOutputStream);
        dataOutputStream.writeByte(182);
        dataOutputStream.writeShort(this.cp.getMethodRef("java/lang/Throwable", "getMessage", "()Ljava/lang/String;"));
        dataOutputStream.writeByte(183);
        dataOutputStream.writeShort(this.cp.getMethodRef("java/lang/NoSuchMethodError", com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/lang/String;)V"));
        dataOutputStream.writeByte(191);
        methodInfo.exceptionTable.add(new ExceptionTableEntry((short) 0, size, (short) methodInfo.code.size(), this.cp.getClass("java/lang/ClassNotFoundException")));
        code_astore(1, dataOutputStream);
        dataOutputStream.writeByte(187);
        dataOutputStream.writeShort(this.cp.getClass("java/lang/NoClassDefFoundError"));
        dataOutputStream.writeByte(89);
        code_aload(1, dataOutputStream);
        dataOutputStream.writeByte(182);
        dataOutputStream.writeShort(this.cp.getMethodRef("java/lang/Throwable", "getMessage", "()Ljava/lang/String;"));
        dataOutputStream.writeByte(183);
        dataOutputStream.writeShort(this.cp.getMethodRef("java/lang/NoClassDefFoundError", com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "(Ljava/lang/String;)V"));
        dataOutputStream.writeByte(191);
        if (methodInfo.code.size() > 65535) {
            throw new IllegalArgumentException("code size limit exceeded");
        }
        methodInfo.maxStack = (short) 10;
        methodInfo.maxLocals = (short) (1 + 1);
        methodInfo.declaredExceptions = new short[0];
        return methodInfo;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_iload(int i2, DataOutputStream dataOutputStream) throws IOException {
        codeLocalLoadStore(i2, 21, 26, dataOutputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_lload(int i2, DataOutputStream dataOutputStream) throws IOException {
        codeLocalLoadStore(i2, 22, 30, dataOutputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_fload(int i2, DataOutputStream dataOutputStream) throws IOException {
        codeLocalLoadStore(i2, 23, 34, dataOutputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_dload(int i2, DataOutputStream dataOutputStream) throws IOException {
        codeLocalLoadStore(i2, 24, 38, dataOutputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_aload(int i2, DataOutputStream dataOutputStream) throws IOException {
        codeLocalLoadStore(i2, 25, 42, dataOutputStream);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_astore(int i2, DataOutputStream dataOutputStream) throws IOException {
        codeLocalLoadStore(i2, 58, 75, dataOutputStream);
    }

    private void codeLocalLoadStore(int i2, int i3, int i4, DataOutputStream dataOutputStream) throws IOException {
        if (!$assertionsDisabled && (i2 < 0 || i2 > 65535)) {
            throw new AssertionError();
        }
        if (i2 <= 3) {
            dataOutputStream.writeByte(i4 + i2);
            return;
        }
        if (i2 <= 255) {
            dataOutputStream.writeByte(i3);
            dataOutputStream.writeByte(i2 & 255);
        } else {
            dataOutputStream.writeByte(196);
            dataOutputStream.writeByte(i3);
            dataOutputStream.writeShort(i2 & 65535);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_ldc(int i2, DataOutputStream dataOutputStream) throws IOException {
        if (!$assertionsDisabled && (i2 < 0 || i2 > 65535)) {
            throw new AssertionError();
        }
        if (i2 <= 255) {
            dataOutputStream.writeByte(18);
            dataOutputStream.writeByte(i2 & 255);
        } else {
            dataOutputStream.writeByte(19);
            dataOutputStream.writeShort(i2 & 65535);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void code_ipush(int i2, DataOutputStream dataOutputStream) throws IOException {
        if (i2 >= -1 && i2 <= 5) {
            dataOutputStream.writeByte(3 + i2);
            return;
        }
        if (i2 >= -128 && i2 <= 127) {
            dataOutputStream.writeByte(16);
            dataOutputStream.writeByte(i2 & 255);
        } else {
            if (i2 >= -32768 && i2 <= 32767) {
                dataOutputStream.writeByte(17);
                dataOutputStream.writeShort(i2 & 65535);
                return;
            }
            throw new AssertionError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void codeClassForName(Class<?> cls, DataOutputStream dataOutputStream) throws IOException {
        code_ldc(this.cp.getString(cls.getName()), dataOutputStream);
        dataOutputStream.writeByte(184);
        dataOutputStream.writeShort(this.cp.getMethodRef("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;"));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String dotToSlash(String str) {
        return str.replace('.', '/');
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getMethodDescriptor(Class<?>[] clsArr, Class<?> cls) {
        return getParameterDescriptors(clsArr) + (cls == Void.TYPE ? "V" : getFieldType(cls));
    }

    private static String getParameterDescriptors(Class<?>[] clsArr) {
        StringBuilder sb = new StringBuilder("(");
        for (Class<?> cls : clsArr) {
            sb.append(getFieldType(cls));
        }
        sb.append(')');
        return sb.toString();
    }

    private static String getFieldType(Class<?> cls) {
        if (cls.isPrimitive()) {
            return PrimitiveTypeInfo.get(cls).baseTypeString;
        }
        if (cls.isArray()) {
            return cls.getName().replace('.', '/');
        }
        return "L" + dotToSlash(cls.getName()) + ";";
    }

    private static String getFriendlyMethodSignature(String str, Class<?>[] clsArr) {
        StringBuilder sb = new StringBuilder(str);
        sb.append('(');
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            if (i2 > 0) {
                sb.append(',');
            }
            Class<?> componentType = clsArr[i2];
            int i3 = 0;
            while (componentType.isArray()) {
                componentType = componentType.getComponentType();
                i3++;
            }
            sb.append(componentType.getName());
            while (true) {
                int i4 = i3;
                i3--;
                if (i4 > 0) {
                    sb.append("[]");
                }
            }
        }
        sb.append(')');
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static int getWordsPerType(Class<?> cls) {
        if (cls == Long.TYPE || cls == Double.TYPE) {
            return 2;
        }
        return 1;
    }

    private static void collectCompatibleTypes(Class<?>[] clsArr, Class<?>[] clsArr2, List<Class<?>> list) {
        for (Class<?> cls : clsArr) {
            if (!list.contains(cls)) {
                int length = clsArr2.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    if (!clsArr2[i2].isAssignableFrom(cls)) {
                        i2++;
                    } else {
                        list.add(cls);
                        break;
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public static List<Class<?>> computeUniqueCatchList(Class<?>[] clsArr) {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Error.class);
        arrayList.add(RuntimeException.class);
        int length = clsArr.length;
        int i2 = 0;
        while (true) {
            if (i2 >= length) {
                break;
            }
            Class<?> cls = clsArr[i2];
            if (cls.isAssignableFrom(Throwable.class)) {
                arrayList.clear();
                break;
            }
            if (Throwable.class.isAssignableFrom(cls)) {
                int i3 = 0;
                while (true) {
                    if (i3 < arrayList.size()) {
                        Class<?> cls2 = (Class) arrayList.get(i3);
                        if (cls2.isAssignableFrom(cls)) {
                            break;
                        }
                        if (cls.isAssignableFrom(cls2)) {
                            arrayList.remove(i3);
                        } else {
                            i3++;
                        }
                    } else {
                        arrayList.add(cls);
                        break;
                    }
                }
            }
            i2++;
        }
        return arrayList;
    }

    /* loaded from: rt.jar:sun/misc/ProxyGenerator$PrimitiveTypeInfo.class */
    private static class PrimitiveTypeInfo {
        public String baseTypeString;
        public String wrapperClassName;
        public String wrapperValueOfDesc;
        public String unwrapMethodName;
        public String unwrapMethodDesc;
        private static Map<Class<?>, PrimitiveTypeInfo> table;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ProxyGenerator.class.desiredAssertionStatus();
            table = new HashMap();
            add(Byte.TYPE, Byte.class);
            add(Character.TYPE, Character.class);
            add(Double.TYPE, Double.class);
            add(Float.TYPE, Float.class);
            add(Integer.TYPE, Integer.class);
            add(Long.TYPE, Long.class);
            add(Short.TYPE, Short.class);
            add(Boolean.TYPE, Boolean.class);
        }

        private static void add(Class<?> cls, Class<?> cls2) {
            table.put(cls, new PrimitiveTypeInfo(cls, cls2));
        }

        private PrimitiveTypeInfo(Class<?> cls, Class<?> cls2) {
            if (!$assertionsDisabled && !cls.isPrimitive()) {
                throw new AssertionError();
            }
            this.baseTypeString = Array.newInstance(cls, 0).getClass().getName().substring(1);
            this.wrapperClassName = ProxyGenerator.dotToSlash(cls2.getName());
            this.wrapperValueOfDesc = "(" + this.baseTypeString + ")L" + this.wrapperClassName + ";";
            this.unwrapMethodName = cls.getName() + "Value";
            this.unwrapMethodDesc = "()" + this.baseTypeString;
        }

        public static PrimitiveTypeInfo get(Class<?> cls) {
            return table.get(cls);
        }
    }

    /* loaded from: rt.jar:sun/misc/ProxyGenerator$ConstantPool.class */
    private static class ConstantPool {
        private List<Entry> pool;
        private Map<Object, Short> map;
        private boolean readOnly;

        private ConstantPool() {
            this.pool = new ArrayList(32);
            this.map = new HashMap(16);
            this.readOnly = false;
        }

        public short getUtf8(String str) {
            if (str == null) {
                throw new NullPointerException();
            }
            return getValue(str);
        }

        public short getInteger(int i2) {
            return getValue(new Integer(i2));
        }

        public short getFloat(float f2) {
            return getValue(new Float(f2));
        }

        public short getClass(String str) {
            return getIndirect(new IndirectEntry(7, getUtf8(str)));
        }

        public short getString(String str) {
            return getIndirect(new IndirectEntry(8, getUtf8(str)));
        }

        public short getFieldRef(String str, String str2, String str3) {
            return getIndirect(new IndirectEntry(9, getClass(str), getNameAndType(str2, str3)));
        }

        public short getMethodRef(String str, String str2, String str3) {
            return getIndirect(new IndirectEntry(10, getClass(str), getNameAndType(str2, str3)));
        }

        public short getInterfaceMethodRef(String str, String str2, String str3) {
            return getIndirect(new IndirectEntry(11, getClass(str), getNameAndType(str2, str3)));
        }

        public short getNameAndType(String str, String str2) {
            return getIndirect(new IndirectEntry(12, getUtf8(str), getUtf8(str2)));
        }

        public void setReadOnly() {
            this.readOnly = true;
        }

        public void write(OutputStream outputStream) throws IOException {
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeShort(this.pool.size() + 1);
            Iterator<Entry> it = this.pool.iterator();
            while (it.hasNext()) {
                it.next().write(dataOutputStream);
            }
        }

        private short addEntry(Entry entry) {
            this.pool.add(entry);
            if (this.pool.size() >= 65535) {
                throw new IllegalArgumentException("constant pool size limit exceeded");
            }
            return (short) this.pool.size();
        }

        private short getValue(Object obj) {
            Short sh = this.map.get(obj);
            if (sh != null) {
                return sh.shortValue();
            }
            if (this.readOnly) {
                throw new InternalError("late constant pool addition: " + obj);
            }
            short sAddEntry = addEntry(new ValueEntry(obj));
            this.map.put(obj, new Short(sAddEntry));
            return sAddEntry;
        }

        private short getIndirect(IndirectEntry indirectEntry) {
            Short sh = this.map.get(indirectEntry);
            if (sh != null) {
                return sh.shortValue();
            }
            if (this.readOnly) {
                throw new InternalError("late constant pool addition");
            }
            short sAddEntry = addEntry(indirectEntry);
            this.map.put(indirectEntry, new Short(sAddEntry));
            return sAddEntry;
        }

        /* loaded from: rt.jar:sun/misc/ProxyGenerator$ConstantPool$Entry.class */
        private static abstract class Entry {
            public abstract void write(DataOutputStream dataOutputStream) throws IOException;

            private Entry() {
            }
        }

        /* loaded from: rt.jar:sun/misc/ProxyGenerator$ConstantPool$ValueEntry.class */
        private static class ValueEntry extends Entry {
            private Object value;

            public ValueEntry(Object obj) {
                super();
                this.value = obj;
            }

            @Override // sun.misc.ProxyGenerator.ConstantPool.Entry
            public void write(DataOutputStream dataOutputStream) throws IOException {
                if (this.value instanceof String) {
                    dataOutputStream.writeByte(1);
                    dataOutputStream.writeUTF((String) this.value);
                    return;
                }
                if (this.value instanceof Integer) {
                    dataOutputStream.writeByte(3);
                    dataOutputStream.writeInt(((Integer) this.value).intValue());
                    return;
                }
                if (this.value instanceof Float) {
                    dataOutputStream.writeByte(4);
                    dataOutputStream.writeFloat(((Float) this.value).floatValue());
                } else if (this.value instanceof Long) {
                    dataOutputStream.writeByte(5);
                    dataOutputStream.writeLong(((Long) this.value).longValue());
                } else {
                    if (this.value instanceof Double) {
                        dataOutputStream.writeDouble(6.0d);
                        dataOutputStream.writeDouble(((Double) this.value).doubleValue());
                        return;
                    }
                    throw new InternalError("bogus value entry: " + this.value);
                }
            }
        }

        /* loaded from: rt.jar:sun/misc/ProxyGenerator$ConstantPool$IndirectEntry.class */
        private static class IndirectEntry extends Entry {
            private int tag;
            private short index0;
            private short index1;

            public IndirectEntry(int i2, short s2) {
                super();
                this.tag = i2;
                this.index0 = s2;
                this.index1 = (short) 0;
            }

            public IndirectEntry(int i2, short s2, short s3) {
                super();
                this.tag = i2;
                this.index0 = s2;
                this.index1 = s3;
            }

            @Override // sun.misc.ProxyGenerator.ConstantPool.Entry
            public void write(DataOutputStream dataOutputStream) throws IOException {
                dataOutputStream.writeByte(this.tag);
                dataOutputStream.writeShort(this.index0);
                if (this.tag == 9 || this.tag == 10 || this.tag == 11 || this.tag == 12) {
                    dataOutputStream.writeShort(this.index1);
                }
            }

            public int hashCode() {
                return this.tag + this.index0 + this.index1;
            }

            public boolean equals(Object obj) {
                if (obj instanceof IndirectEntry) {
                    IndirectEntry indirectEntry = (IndirectEntry) obj;
                    if (this.tag == indirectEntry.tag && this.index0 == indirectEntry.index0 && this.index1 == indirectEntry.index1) {
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }
    }
}
