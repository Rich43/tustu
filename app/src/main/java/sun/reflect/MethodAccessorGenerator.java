package sun.reflect;

import com.sun.org.apache.bcel.internal.Constants;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/reflect/MethodAccessorGenerator.class */
class MethodAccessorGenerator extends AccessorGenerator {
    private static final short NUM_BASE_CPOOL_ENTRIES = 12;
    private static final short NUM_METHODS = 2;
    private static final short NUM_SERIALIZATION_CPOOL_ENTRIES = 2;
    private static volatile int methodSymnum = 0;
    private static volatile int constructorSymnum = 0;
    private static volatile int serializationConstructorSymnum = 0;
    private Class<?> declaringClass;
    private Class<?>[] parameterTypes;
    private Class<?> returnType;
    private boolean isConstructor;
    private boolean forSerialization;
    private short targetMethodRef;
    private short invokeIdx;
    private short invokeDescriptorIdx;
    private short nonPrimitiveParametersBaseIdx;

    MethodAccessorGenerator() {
    }

    public MethodAccessor generateMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, Class<?>[] clsArr2, int i2) {
        return (MethodAccessor) generate(cls, str, clsArr, cls2, clsArr2, i2, false, false, null);
    }

    public ConstructorAccessor generateConstructor(Class<?> cls, Class<?>[] clsArr, Class<?>[] clsArr2, int i2) {
        return (ConstructorAccessor) generate(cls, Constants.CONSTRUCTOR_NAME, clsArr, Void.TYPE, clsArr2, i2, true, false, null);
    }

    public SerializationConstructorAccessorImpl generateSerializationConstructor(Class<?> cls, Class<?>[] clsArr, Class<?>[] clsArr2, int i2, Class<?> cls2) {
        return (SerializationConstructorAccessorImpl) generate(cls, Constants.CONSTRUCTOR_NAME, clsArr, Void.TYPE, clsArr2, i2, true, true, cls2);
    }

    private MagicAccessorImpl generate(final Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, Class<?>[] clsArr2, int i2, boolean z2, boolean z3, Class<?> cls3) {
        ByteVector byteVectorCreate = ByteVectorFactory.create();
        this.asm = new ClassFileAssembler(byteVectorCreate);
        this.declaringClass = cls;
        this.parameterTypes = clsArr;
        this.returnType = cls2;
        this.modifiers = i2;
        this.isConstructor = z2;
        this.forSerialization = z3;
        this.asm.emitMagicAndVersion();
        short s2 = 42;
        boolean zUsesPrimitiveTypes = usesPrimitiveTypes();
        if (zUsesPrimitiveTypes) {
            s2 = (short) (42 + 72);
        }
        if (z3) {
            s2 = (short) (s2 + 2);
        }
        short sNumNonPrimitiveParameterTypes = (short) (s2 + ((short) (2 * numNonPrimitiveParameterTypes())));
        this.asm.emitShort(add(sNumNonPrimitiveParameterTypes, (short) 1));
        final String strGenerateName = generateName(z2, z3);
        this.asm.emitConstantPoolUTF8(strGenerateName);
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.thisClass = this.asm.cpi();
        if (z2) {
            if (z3) {
                this.asm.emitConstantPoolUTF8("sun/reflect/SerializationConstructorAccessorImpl");
            } else {
                this.asm.emitConstantPoolUTF8("sun/reflect/ConstructorAccessorImpl");
            }
        } else {
            this.asm.emitConstantPoolUTF8("sun/reflect/MethodAccessorImpl");
        }
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.superClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8(getClassName(cls, false));
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.targetClass = this.asm.cpi();
        short sCpi = 0;
        if (z3) {
            this.asm.emitConstantPoolUTF8(getClassName(cls3, false));
            this.asm.emitConstantPoolClass(this.asm.cpi());
            sCpi = this.asm.cpi();
        }
        this.asm.emitConstantPoolUTF8(str);
        this.asm.emitConstantPoolUTF8(buildInternalSignature());
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        if (isInterface()) {
            this.asm.emitConstantPoolInterfaceMethodref(this.targetClass, this.asm.cpi());
        } else if (z3) {
            this.asm.emitConstantPoolMethodref(sCpi, this.asm.cpi());
        } else {
            this.asm.emitConstantPoolMethodref(this.targetClass, this.asm.cpi());
        }
        this.targetMethodRef = this.asm.cpi();
        if (z2) {
            this.asm.emitConstantPoolUTF8("newInstance");
        } else {
            this.asm.emitConstantPoolUTF8("invoke");
        }
        this.invokeIdx = this.asm.cpi();
        if (z2) {
            this.asm.emitConstantPoolUTF8("([Ljava/lang/Object;)Ljava/lang/Object;");
        } else {
            this.asm.emitConstantPoolUTF8("(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;");
        }
        this.invokeDescriptorIdx = this.asm.cpi();
        this.nonPrimitiveParametersBaseIdx = add(this.asm.cpi(), (short) 2);
        for (Class<?> cls4 : clsArr) {
            if (!isPrimitive(cls4)) {
                this.asm.emitConstantPoolUTF8(getClassName(cls4, false));
                this.asm.emitConstantPoolClass(this.asm.cpi());
            }
        }
        emitCommonConstantPoolEntries();
        if (zUsesPrimitiveTypes) {
            emitBoxingContantPoolEntries();
        }
        if (this.asm.cpi() != sNumNonPrimitiveParameterTypes) {
            throw new InternalError("Adjust this code (cpi = " + ((int) this.asm.cpi()) + ", numCPEntries = " + ((int) sNumNonPrimitiveParameterTypes) + ")");
        }
        this.asm.emitShort((short) 1);
        this.asm.emitShort(this.thisClass);
        this.asm.emitShort(this.superClass);
        this.asm.emitShort((short) 0);
        this.asm.emitShort((short) 0);
        this.asm.emitShort((short) 2);
        emitConstructor();
        emitInvoke();
        this.asm.emitShort((short) 0);
        byteVectorCreate.trim();
        final byte[] data = byteVectorCreate.getData();
        return (MagicAccessorImpl) AccessController.doPrivileged(new PrivilegedAction<MagicAccessorImpl>() { // from class: sun.reflect.MethodAccessorGenerator.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public MagicAccessorImpl run2() {
                try {
                    return (MagicAccessorImpl) ClassDefiner.defineClass(strGenerateName, data, 0, data.length, cls.getClassLoader()).newInstance();
                } catch (IllegalAccessException | InstantiationException e2) {
                    throw new InternalError(e2);
                }
            }
        });
    }

    private void emitInvoke() {
        if (this.parameterTypes.length > 65535) {
            throw new InternalError("Can't handle more than 65535 parameters");
        }
        ClassFileAssembler classFileAssembler = new ClassFileAssembler();
        if (this.isConstructor) {
            classFileAssembler.setMaxLocals(2);
        } else {
            classFileAssembler.setMaxLocals(3);
        }
        short length = 0;
        if (this.isConstructor) {
            classFileAssembler.opc_new(this.targetClass);
            classFileAssembler.opc_dup();
        } else {
            if (isPrimitive(this.returnType)) {
                classFileAssembler.opc_new(indexForPrimitiveType(this.returnType));
                classFileAssembler.opc_dup();
            }
            if (!isStatic()) {
                classFileAssembler.opc_aload_1();
                Label label = new Label();
                classFileAssembler.opc_ifnonnull(label);
                classFileAssembler.opc_new(this.nullPointerClass);
                classFileAssembler.opc_dup();
                classFileAssembler.opc_invokespecial(this.nullPointerCtorIdx, 0, 0);
                classFileAssembler.opc_athrow();
                label.bind();
                length = classFileAssembler.getLength();
                classFileAssembler.opc_aload_1();
                classFileAssembler.opc_checkcast(this.targetClass);
            }
        }
        Label label2 = new Label();
        if (this.parameterTypes.length == 0) {
            if (this.isConstructor) {
                classFileAssembler.opc_aload_1();
            } else {
                classFileAssembler.opc_aload_2();
            }
            classFileAssembler.opc_ifnull(label2);
        }
        if (this.isConstructor) {
            classFileAssembler.opc_aload_1();
        } else {
            classFileAssembler.opc_aload_2();
        }
        classFileAssembler.opc_arraylength();
        classFileAssembler.opc_sipush((short) this.parameterTypes.length);
        classFileAssembler.opc_if_icmpeq(label2);
        classFileAssembler.opc_new(this.illegalArgumentClass);
        classFileAssembler.opc_dup();
        classFileAssembler.opc_invokespecial(this.illegalArgumentCtorIdx, 0, 0);
        classFileAssembler.opc_athrow();
        label2.bind();
        short sAdd = this.nonPrimitiveParametersBaseIdx;
        Label label3 = null;
        byte bTypeSizeInStackSlots = 1;
        for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
            Class<?> cls = this.parameterTypes[i2];
            bTypeSizeInStackSlots = (byte) (bTypeSizeInStackSlots + ((byte) typeSizeInStackSlots(cls)));
            if (label3 != null) {
                label3.bind();
                label3 = null;
            }
            if (this.isConstructor) {
                classFileAssembler.opc_aload_1();
            } else {
                classFileAssembler.opc_aload_2();
            }
            classFileAssembler.opc_sipush((short) i2);
            classFileAssembler.opc_aaload();
            if (isPrimitive(cls)) {
                if (this.isConstructor) {
                    classFileAssembler.opc_astore_2();
                } else {
                    classFileAssembler.opc_astore_3();
                }
                Label label4 = null;
                label3 = new Label();
                for (int i3 = 0; i3 < primitiveTypes.length; i3++) {
                    Class<?> cls2 = primitiveTypes[i3];
                    if (canWidenTo(cls2, cls)) {
                        if (label4 != null) {
                            label4.bind();
                        }
                        if (this.isConstructor) {
                            classFileAssembler.opc_aload_2();
                        } else {
                            classFileAssembler.opc_aload_3();
                        }
                        classFileAssembler.opc_instanceof(indexForPrimitiveType(cls2));
                        label4 = new Label();
                        classFileAssembler.opc_ifeq(label4);
                        if (this.isConstructor) {
                            classFileAssembler.opc_aload_2();
                        } else {
                            classFileAssembler.opc_aload_3();
                        }
                        classFileAssembler.opc_checkcast(indexForPrimitiveType(cls2));
                        classFileAssembler.opc_invokevirtual(unboxingMethodForPrimitiveType(cls2), 0, typeSizeInStackSlots(cls2));
                        emitWideningBytecodeForPrimitiveConversion(classFileAssembler, cls2, cls);
                        classFileAssembler.opc_goto(label3);
                    }
                }
                if (label4 == null) {
                    throw new InternalError("Must have found at least identity conversion");
                }
                label4.bind();
                classFileAssembler.opc_new(this.illegalArgumentClass);
                classFileAssembler.opc_dup();
                classFileAssembler.opc_invokespecial(this.illegalArgumentCtorIdx, 0, 0);
                classFileAssembler.opc_athrow();
            } else {
                classFileAssembler.opc_checkcast(sAdd);
                sAdd = add(sAdd, (short) 2);
            }
        }
        if (label3 != null) {
            label3.bind();
        }
        short length2 = classFileAssembler.getLength();
        if (this.isConstructor) {
            classFileAssembler.opc_invokespecial(this.targetMethodRef, bTypeSizeInStackSlots, 0);
        } else if (isStatic()) {
            classFileAssembler.opc_invokestatic(this.targetMethodRef, bTypeSizeInStackSlots, typeSizeInStackSlots(this.returnType));
        } else if (isInterface()) {
            if (isPrivate()) {
                classFileAssembler.opc_invokespecial(this.targetMethodRef, bTypeSizeInStackSlots, 0);
            } else {
                classFileAssembler.opc_invokeinterface(this.targetMethodRef, bTypeSizeInStackSlots, bTypeSizeInStackSlots, typeSizeInStackSlots(this.returnType));
            }
        } else {
            classFileAssembler.opc_invokevirtual(this.targetMethodRef, bTypeSizeInStackSlots, typeSizeInStackSlots(this.returnType));
        }
        short length3 = classFileAssembler.getLength();
        if (!this.isConstructor) {
            if (isPrimitive(this.returnType)) {
                classFileAssembler.opc_invokespecial(ctorIndexForPrimitiveType(this.returnType), typeSizeInStackSlots(this.returnType), 0);
            } else if (this.returnType == Void.TYPE) {
                classFileAssembler.opc_aconst_null();
            }
        }
        classFileAssembler.opc_areturn();
        short length4 = classFileAssembler.getLength();
        classFileAssembler.setStack(1);
        classFileAssembler.opc_invokespecial(this.toStringIdx, 0, 1);
        classFileAssembler.opc_new(this.illegalArgumentClass);
        classFileAssembler.opc_dup_x1();
        classFileAssembler.opc_swap();
        classFileAssembler.opc_invokespecial(this.illegalArgumentStringCtorIdx, 1, 0);
        classFileAssembler.opc_athrow();
        short length5 = classFileAssembler.getLength();
        classFileAssembler.setStack(1);
        classFileAssembler.opc_new(this.invocationTargetClass);
        classFileAssembler.opc_dup_x1();
        classFileAssembler.opc_swap();
        classFileAssembler.opc_invokespecial(this.invocationTargetCtorIdx, 1, 0);
        classFileAssembler.opc_athrow();
        ClassFileAssembler classFileAssembler2 = new ClassFileAssembler();
        classFileAssembler2.emitShort(length);
        classFileAssembler2.emitShort(length2);
        classFileAssembler2.emitShort(length4);
        classFileAssembler2.emitShort(this.classCastClass);
        classFileAssembler2.emitShort(length);
        classFileAssembler2.emitShort(length2);
        classFileAssembler2.emitShort(length4);
        classFileAssembler2.emitShort(this.nullPointerClass);
        classFileAssembler2.emitShort(length2);
        classFileAssembler2.emitShort(length3);
        classFileAssembler2.emitShort(length5);
        classFileAssembler2.emitShort(this.throwableClass);
        emitMethod(this.invokeIdx, classFileAssembler.getMaxLocals(), classFileAssembler, classFileAssembler2, new short[]{this.invocationTargetClass});
    }

    private boolean usesPrimitiveTypes() {
        if (this.returnType.isPrimitive()) {
            return true;
        }
        for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
            if (this.parameterTypes[i2].isPrimitive()) {
                return true;
            }
        }
        return false;
    }

    private int numNonPrimitiveParameterTypes() {
        int i2 = 0;
        for (int i3 = 0; i3 < this.parameterTypes.length; i3++) {
            if (!this.parameterTypes[i3].isPrimitive()) {
                i2++;
            }
        }
        return i2;
    }

    private boolean isInterface() {
        return this.declaringClass.isInterface();
    }

    private String buildInternalSignature() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (int i2 = 0; i2 < this.parameterTypes.length; i2++) {
            stringBuffer.append(getClassName(this.parameterTypes[i2], true));
        }
        stringBuffer.append(")");
        stringBuffer.append(getClassName(this.returnType, true));
        return stringBuffer.toString();
    }

    private static synchronized String generateName(boolean z2, boolean z3) {
        if (z2) {
            if (z3) {
                int i2 = serializationConstructorSymnum + 1;
                serializationConstructorSymnum = i2;
                return "sun/reflect/GeneratedSerializationConstructorAccessor" + i2;
            }
            int i3 = constructorSymnum + 1;
            constructorSymnum = i3;
            return "sun/reflect/GeneratedConstructorAccessor" + i3;
        }
        int i4 = methodSymnum + 1;
        methodSymnum = i4;
        return "sun/reflect/GeneratedMethodAccessor" + i4;
    }
}
