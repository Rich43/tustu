package java.lang.invoke;

import java.io.FilePermission;
import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.LinkedHashSet;
import java.util.PropertyPermission;
import java.util.concurrent.atomic.AtomicInteger;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Type;
import sun.invoke.util.BytecodeDescriptor;
import sun.misc.Unsafe;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:java/lang/invoke/InnerClassLambdaMetafactory.class */
final class InnerClassLambdaMetafactory extends AbstractValidatingLambdaMetafactory {
    private static final int CLASSFILE_VERSION = 52;
    private static final String JAVA_LANG_OBJECT = "java/lang/Object";
    private static final String NAME_CTOR = "<init>";
    private static final String NAME_FACTORY = "get$Lambda";
    private static final String NAME_SERIALIZED_LAMBDA = "java/lang/invoke/SerializedLambda";
    private static final String DESCR_METHOD_WRITE_REPLACE = "()Ljava/lang/Object;";
    private static final String DESCR_METHOD_WRITE_OBJECT = "(Ljava/io/ObjectOutputStream;)V";
    private static final String DESCR_METHOD_READ_OBJECT = "(Ljava/io/ObjectInputStream;)V";
    private static final String NAME_METHOD_WRITE_REPLACE = "writeReplace";
    private static final String NAME_METHOD_READ_OBJECT = "readObject";
    private static final String NAME_METHOD_WRITE_OBJECT = "writeObject";
    private static final ProxyClassesDumper dumper;
    private final String implMethodClassName;
    private final String implMethodName;
    private final String implMethodDesc;
    private final Class<?> implMethodReturnClass;
    private final MethodType constructorType;
    private final ClassWriter cw;
    private final String[] argNames;
    private final String[] argDescs;
    private final String lambdaClassName;
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    private static final String METHOD_DESCRIPTOR_VOID = Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]);
    private static final String DESCR_CTOR_SERIALIZED_LAMBDA = MethodType.methodType(Void.TYPE, Class.class, String.class, String.class, String.class, Integer.TYPE, String.class, String.class, String.class, String.class, Object[].class).toMethodDescriptorString();
    private static final String DESCR_CTOR_NOT_SERIALIZABLE_EXCEPTION = MethodType.methodType(Void.TYPE, (Class<?>) String.class).toMethodDescriptorString();
    private static final String NAME_NOT_SERIALIZABLE_EXCEPTION = "java/io/NotSerializableException";
    private static final String[] SER_HOSTILE_EXCEPTIONS = {NAME_NOT_SERIALIZABLE_EXCEPTION};
    private static final String[] EMPTY_STRING_ARRAY = new String[0];
    private static final AtomicInteger counter = new AtomicInteger(0);

    static {
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("jdk.internal.lambda.dumpProxyClasses"), (AccessControlContext) null, new PropertyPermission("jdk.internal.lambda.dumpProxyClasses", "read"));
        dumper = null == str ? null : ProxyClassesDumper.getInstance(str);
    }

    public InnerClassLambdaMetafactory(MethodHandles.Lookup lookup, MethodType methodType, String str, MethodType methodType2, MethodHandle methodHandle, MethodType methodType3, boolean z2, Class<?>[] clsArr, MethodType[] methodTypeArr) throws LambdaConversionException {
        super(lookup, methodType, str, methodType2, methodHandle, methodType3, z2, clsArr, methodTypeArr);
        this.implMethodClassName = this.implDefiningClass.getName().replace('.', '/');
        this.implMethodName = this.implInfo.getName();
        this.implMethodDesc = this.implMethodType.toMethodDescriptorString();
        this.implMethodReturnClass = this.implKind == 8 ? this.implDefiningClass : this.implMethodType.returnType();
        this.constructorType = methodType.changeReturnType(Void.TYPE);
        this.lambdaClassName = this.targetClass.getName().replace('.', '/') + "$$Lambda$" + counter.incrementAndGet();
        this.cw = new ClassWriter(1);
        int iParameterCount = methodType.parameterCount();
        if (iParameterCount > 0) {
            this.argNames = new String[iParameterCount];
            this.argDescs = new String[iParameterCount];
            for (int i2 = 0; i2 < iParameterCount; i2++) {
                this.argNames[i2] = "arg$" + (i2 + 1);
                this.argDescs[i2] = BytecodeDescriptor.unparse(methodType.parameterType(i2));
            }
            return;
        }
        String[] strArr = EMPTY_STRING_ARRAY;
        this.argDescs = strArr;
        this.argNames = strArr;
    }

    @Override // java.lang.invoke.AbstractValidatingLambdaMetafactory
    CallSite buildCallSite() throws LambdaConversionException, IllegalArgumentException {
        final Class<?> clsSpinInnerClass = spinInnerClass();
        if (this.invokedType.parameterCount() == 0) {
            Constructor[] constructorArr = (Constructor[]) AccessController.doPrivileged(new PrivilegedAction<Constructor<?>[]>() { // from class: java.lang.invoke.InnerClassLambdaMetafactory.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Constructor<?>[] run2() throws SecurityException {
                    Constructor<?>[] declaredConstructors = clsSpinInnerClass.getDeclaredConstructors();
                    if (declaredConstructors.length == 1) {
                        declaredConstructors[0].setAccessible(true);
                    }
                    return declaredConstructors;
                }
            });
            if (constructorArr.length != 1) {
                throw new LambdaConversionException("Expected one lambda constructor for " + clsSpinInnerClass.getCanonicalName() + ", got " + constructorArr.length);
            }
            try {
                return new ConstantCallSite(MethodHandles.constant(this.samBase, constructorArr[0].newInstance(new Object[0])));
            } catch (ReflectiveOperationException e2) {
                throw new LambdaConversionException("Exception instantiating lambda object", e2);
            }
        }
        try {
            UNSAFE.ensureClassInitialized(clsSpinInnerClass);
            return new ConstantCallSite(MethodHandles.Lookup.IMPL_LOOKUP.findStatic(clsSpinInnerClass, NAME_FACTORY, this.invokedType));
        } catch (ReflectiveOperationException e3) {
            throw new LambdaConversionException("Exception finding constructor", e3);
        }
    }

    private Class<?> spinInnerClass() throws LambdaConversionException {
        String[] strArr;
        String strReplace = this.samBase.getName().replace('.', '/');
        boolean z2 = !this.isSerializable && Serializable.class.isAssignableFrom(this.samBase);
        if (this.markerInterfaces.length == 0) {
            strArr = new String[]{strReplace};
        } else {
            LinkedHashSet linkedHashSet = new LinkedHashSet(this.markerInterfaces.length + 1);
            linkedHashSet.add(strReplace);
            for (Class<?> cls : this.markerInterfaces) {
                linkedHashSet.add(cls.getName().replace('.', '/'));
                z2 |= !this.isSerializable && Serializable.class.isAssignableFrom(cls);
            }
            strArr = (String[]) linkedHashSet.toArray(new String[linkedHashSet.size()]);
        }
        this.cw.visit(52, 4144, this.lambdaClassName, null, JAVA_LANG_OBJECT, strArr);
        for (int i2 = 0; i2 < this.argDescs.length; i2++) {
            this.cw.visitField(18, this.argNames[i2], this.argDescs[i2], null, null).visitEnd();
        }
        generateConstructor();
        if (this.invokedType.parameterCount() != 0) {
            generateFactory();
        }
        MethodVisitor methodVisitorVisitMethod = this.cw.visitMethod(1, this.samMethodName, this.samMethodType.toMethodDescriptorString(), null, null);
        methodVisitorVisitMethod.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
        new ForwardingMethodGenerator(methodVisitorVisitMethod).generate(this.samMethodType);
        if (this.additionalBridges != null) {
            for (MethodType methodType : this.additionalBridges) {
                MethodVisitor methodVisitorVisitMethod2 = this.cw.visitMethod(65, this.samMethodName, methodType.toMethodDescriptorString(), null, null);
                methodVisitorVisitMethod2.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
                new ForwardingMethodGenerator(methodVisitorVisitMethod2).generate(methodType);
            }
        }
        if (this.isSerializable) {
            generateSerializationFriendlyMethods();
        } else if (z2) {
            generateSerializationHostileMethods();
        }
        this.cw.visitEnd();
        final byte[] byteArray = this.cw.toByteArray();
        if (dumper != null) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.invoke.InnerClassLambdaMetafactory.2
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    InnerClassLambdaMetafactory.dumper.dumpClass(InnerClassLambdaMetafactory.this.lambdaClassName, byteArray);
                    return null;
                }
            }, (AccessControlContext) null, new FilePermission("<<ALL FILES>>", "read, write"), new PropertyPermission("user.dir", "read"));
        }
        return UNSAFE.defineAnonymousClass(this.targetClass, byteArray, null);
    }

    private void generateFactory() {
        MethodVisitor methodVisitorVisitMethod = this.cw.visitMethod(10, NAME_FACTORY, this.invokedType.toMethodDescriptorString(), null, null);
        methodVisitorVisitMethod.visitCode();
        methodVisitorVisitMethod.visitTypeInsn(187, this.lambdaClassName);
        methodVisitorVisitMethod.visitInsn(89);
        int iParameterCount = this.invokedType.parameterCount();
        int parameterSize = 0;
        for (int i2 = 0; i2 < iParameterCount; i2++) {
            Class<?> clsParameterType = this.invokedType.parameterType(i2);
            methodVisitorVisitMethod.visitVarInsn(getLoadOpcode(clsParameterType), parameterSize);
            parameterSize += getParameterSize(clsParameterType);
        }
        methodVisitorVisitMethod.visitMethodInsn(183, this.lambdaClassName, "<init>", this.constructorType.toMethodDescriptorString(), false);
        methodVisitorVisitMethod.visitInsn(176);
        methodVisitorVisitMethod.visitMaxs(-1, -1);
        methodVisitorVisitMethod.visitEnd();
    }

    private void generateConstructor() {
        MethodVisitor methodVisitorVisitMethod = this.cw.visitMethod(2, "<init>", this.constructorType.toMethodDescriptorString(), null, null);
        methodVisitorVisitMethod.visitCode();
        methodVisitorVisitMethod.visitVarInsn(25, 0);
        methodVisitorVisitMethod.visitMethodInsn(183, JAVA_LANG_OBJECT, "<init>", METHOD_DESCRIPTOR_VOID, false);
        int iParameterCount = this.invokedType.parameterCount();
        int parameterSize = 0;
        for (int i2 = 0; i2 < iParameterCount; i2++) {
            methodVisitorVisitMethod.visitVarInsn(25, 0);
            Class<?> clsParameterType = this.invokedType.parameterType(i2);
            methodVisitorVisitMethod.visitVarInsn(getLoadOpcode(clsParameterType), parameterSize + 1);
            parameterSize += getParameterSize(clsParameterType);
            methodVisitorVisitMethod.visitFieldInsn(181, this.lambdaClassName, this.argNames[i2], this.argDescs[i2]);
        }
        methodVisitorVisitMethod.visitInsn(177);
        methodVisitorVisitMethod.visitMaxs(-1, -1);
        methodVisitorVisitMethod.visitEnd();
    }

    private void generateSerializationFriendlyMethods() {
        TypeConvertingMethodAdapter typeConvertingMethodAdapter = new TypeConvertingMethodAdapter(this.cw.visitMethod(18, NAME_METHOD_WRITE_REPLACE, DESCR_METHOD_WRITE_REPLACE, null, null));
        typeConvertingMethodAdapter.visitCode();
        typeConvertingMethodAdapter.visitTypeInsn(187, NAME_SERIALIZED_LAMBDA);
        typeConvertingMethodAdapter.visitInsn(89);
        typeConvertingMethodAdapter.visitLdcInsn(Type.getType(this.targetClass));
        typeConvertingMethodAdapter.visitLdcInsn(this.invokedType.returnType().getName().replace('.', '/'));
        typeConvertingMethodAdapter.visitLdcInsn(this.samMethodName);
        typeConvertingMethodAdapter.visitLdcInsn(this.samMethodType.toMethodDescriptorString());
        typeConvertingMethodAdapter.visitLdcInsn(Integer.valueOf(this.implInfo.getReferenceKind()));
        typeConvertingMethodAdapter.visitLdcInsn(this.implInfo.getDeclaringClass().getName().replace('.', '/'));
        typeConvertingMethodAdapter.visitLdcInsn(this.implInfo.getName());
        typeConvertingMethodAdapter.visitLdcInsn(this.implInfo.getMethodType().toMethodDescriptorString());
        typeConvertingMethodAdapter.visitLdcInsn(this.instantiatedMethodType.toMethodDescriptorString());
        typeConvertingMethodAdapter.iconst(this.argDescs.length);
        typeConvertingMethodAdapter.visitTypeInsn(189, JAVA_LANG_OBJECT);
        for (int i2 = 0; i2 < this.argDescs.length; i2++) {
            typeConvertingMethodAdapter.visitInsn(89);
            typeConvertingMethodAdapter.iconst(i2);
            typeConvertingMethodAdapter.visitVarInsn(25, 0);
            typeConvertingMethodAdapter.visitFieldInsn(180, this.lambdaClassName, this.argNames[i2], this.argDescs[i2]);
            typeConvertingMethodAdapter.boxIfTypePrimitive(Type.getType(this.argDescs[i2]));
            typeConvertingMethodAdapter.visitInsn(83);
        }
        typeConvertingMethodAdapter.visitMethodInsn(183, NAME_SERIALIZED_LAMBDA, "<init>", DESCR_CTOR_SERIALIZED_LAMBDA, false);
        typeConvertingMethodAdapter.visitInsn(176);
        typeConvertingMethodAdapter.visitMaxs(-1, -1);
        typeConvertingMethodAdapter.visitEnd();
    }

    private void generateSerializationHostileMethods() {
        MethodVisitor methodVisitorVisitMethod = this.cw.visitMethod(18, NAME_METHOD_WRITE_OBJECT, DESCR_METHOD_WRITE_OBJECT, null, SER_HOSTILE_EXCEPTIONS);
        methodVisitorVisitMethod.visitCode();
        methodVisitorVisitMethod.visitTypeInsn(187, NAME_NOT_SERIALIZABLE_EXCEPTION);
        methodVisitorVisitMethod.visitInsn(89);
        methodVisitorVisitMethod.visitLdcInsn("Non-serializable lambda");
        methodVisitorVisitMethod.visitMethodInsn(183, NAME_NOT_SERIALIZABLE_EXCEPTION, "<init>", DESCR_CTOR_NOT_SERIALIZABLE_EXCEPTION, false);
        methodVisitorVisitMethod.visitInsn(191);
        methodVisitorVisitMethod.visitMaxs(-1, -1);
        methodVisitorVisitMethod.visitEnd();
        MethodVisitor methodVisitorVisitMethod2 = this.cw.visitMethod(18, NAME_METHOD_READ_OBJECT, DESCR_METHOD_READ_OBJECT, null, SER_HOSTILE_EXCEPTIONS);
        methodVisitorVisitMethod2.visitCode();
        methodVisitorVisitMethod2.visitTypeInsn(187, NAME_NOT_SERIALIZABLE_EXCEPTION);
        methodVisitorVisitMethod2.visitInsn(89);
        methodVisitorVisitMethod2.visitLdcInsn("Non-serializable lambda");
        methodVisitorVisitMethod2.visitMethodInsn(183, NAME_NOT_SERIALIZABLE_EXCEPTION, "<init>", DESCR_CTOR_NOT_SERIALIZABLE_EXCEPTION, false);
        methodVisitorVisitMethod2.visitInsn(191);
        methodVisitorVisitMethod2.visitMaxs(-1, -1);
        methodVisitorVisitMethod2.visitEnd();
    }

    /* loaded from: rt.jar:java/lang/invoke/InnerClassLambdaMetafactory$ForwardingMethodGenerator.class */
    private class ForwardingMethodGenerator extends TypeConvertingMethodAdapter {
        ForwardingMethodGenerator(MethodVisitor methodVisitor) {
            super(methodVisitor);
        }

        void generate(MethodType methodType) {
            visitCode();
            if (InnerClassLambdaMetafactory.this.implKind == 8) {
                visitTypeInsn(187, InnerClassLambdaMetafactory.this.implMethodClassName);
                visitInsn(89);
            }
            for (int i2 = 0; i2 < InnerClassLambdaMetafactory.this.argNames.length; i2++) {
                visitVarInsn(25, 0);
                visitFieldInsn(180, InnerClassLambdaMetafactory.this.lambdaClassName, InnerClassLambdaMetafactory.this.argNames[i2], InnerClassLambdaMetafactory.this.argDescs[i2]);
            }
            convertArgumentTypes(methodType);
            visitMethodInsn(invocationOpcode(), InnerClassLambdaMetafactory.this.implMethodClassName, InnerClassLambdaMetafactory.this.implMethodName, InnerClassLambdaMetafactory.this.implMethodDesc, InnerClassLambdaMetafactory.this.implDefiningClass.isInterface());
            Class<?> clsReturnType = methodType.returnType();
            convertType(InnerClassLambdaMetafactory.this.implMethodReturnClass, clsReturnType, clsReturnType);
            visitInsn(InnerClassLambdaMetafactory.getReturnOpcode(clsReturnType));
            visitMaxs(-1, -1);
            visitEnd();
        }

        private void convertArgumentTypes(MethodType methodType) {
            int parameterSize = 0;
            boolean z2 = InnerClassLambdaMetafactory.this.implIsInstanceMethod && InnerClassLambdaMetafactory.this.invokedType.parameterCount() == 0;
            int i2 = z2 ? 1 : 0;
            if (z2) {
                Class<?> clsParameterType = methodType.parameterType(0);
                visitVarInsn(InnerClassLambdaMetafactory.getLoadOpcode(clsParameterType), 0 + 1);
                parameterSize = 0 + InnerClassLambdaMetafactory.getParameterSize(clsParameterType);
                convertType(clsParameterType, InnerClassLambdaMetafactory.this.implDefiningClass, InnerClassLambdaMetafactory.this.instantiatedMethodType.parameterType(0));
            }
            int iParameterCount = methodType.parameterCount();
            int iParameterCount2 = InnerClassLambdaMetafactory.this.implMethodType.parameterCount() - iParameterCount;
            for (int i3 = i2; i3 < iParameterCount; i3++) {
                Class<?> clsParameterType2 = methodType.parameterType(i3);
                visitVarInsn(InnerClassLambdaMetafactory.getLoadOpcode(clsParameterType2), parameterSize + 1);
                parameterSize += InnerClassLambdaMetafactory.getParameterSize(clsParameterType2);
                convertType(clsParameterType2, InnerClassLambdaMetafactory.this.implMethodType.parameterType(iParameterCount2 + i3), InnerClassLambdaMetafactory.this.instantiatedMethodType.parameterType(i3));
            }
        }

        private int invocationOpcode() throws InternalError {
            switch (InnerClassLambdaMetafactory.this.implKind) {
                case 5:
                    return 182;
                case 6:
                    return 184;
                case 7:
                    return 183;
                case 8:
                    return 183;
                case 9:
                    return 185;
                default:
                    throw new InternalError("Unexpected invocation kind: " + InnerClassLambdaMetafactory.this.implKind);
            }
        }
    }

    static int getParameterSize(Class<?> cls) {
        if (cls == Void.TYPE) {
            return 0;
        }
        if (cls == Long.TYPE || cls == Double.TYPE) {
            return 2;
        }
        return 1;
    }

    static int getLoadOpcode(Class<?> cls) {
        if (cls == Void.TYPE) {
            throw new InternalError("Unexpected void type of load opcode");
        }
        return 21 + getOpcodeOffset(cls);
    }

    static int getReturnOpcode(Class<?> cls) {
        if (cls == Void.TYPE) {
            return 177;
        }
        return 172 + getOpcodeOffset(cls);
    }

    private static int getOpcodeOffset(Class<?> cls) {
        if (cls.isPrimitive()) {
            if (cls == Long.TYPE) {
                return 1;
            }
            if (cls == Float.TYPE) {
                return 2;
            }
            if (cls == Double.TYPE) {
                return 3;
            }
            return 0;
        }
        return 4;
    }
}
