package sun.reflect;

import com.sun.org.apache.bcel.internal.Constants;
import java.lang.reflect.Modifier;
import org.icepdf.core.util.PdfOps;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/reflect/AccessorGenerator.class */
class AccessorGenerator implements ClassFileConstants {
    protected static final short S0 = 0;
    protected static final short S1 = 1;
    protected static final short S2 = 2;
    protected static final short S3 = 3;
    protected static final short S4 = 4;
    protected static final short S5 = 5;
    protected static final short S6 = 6;
    protected ClassFileAssembler asm;
    protected int modifiers;
    protected short thisClass;
    protected short superClass;
    protected short targetClass;
    protected short throwableClass;
    protected short classCastClass;
    protected short nullPointerClass;
    protected short illegalArgumentClass;
    protected short invocationTargetClass;
    protected short initIdx;
    protected short initNameAndTypeIdx;
    protected short initStringNameAndTypeIdx;
    protected short nullPointerCtorIdx;
    protected short illegalArgumentCtorIdx;
    protected short illegalArgumentStringCtorIdx;
    protected short invocationTargetCtorIdx;
    protected short superCtorIdx;
    protected short objectClass;
    protected short toStringIdx;
    protected short codeIdx;
    protected short exceptionsIdx;
    protected short booleanIdx;
    protected short booleanCtorIdx;
    protected short booleanUnboxIdx;
    protected short byteIdx;
    protected short byteCtorIdx;
    protected short byteUnboxIdx;
    protected short characterIdx;
    protected short characterCtorIdx;
    protected short characterUnboxIdx;
    protected short doubleIdx;
    protected short doubleCtorIdx;
    protected short doubleUnboxIdx;
    protected short floatIdx;
    protected short floatCtorIdx;
    protected short floatUnboxIdx;
    protected short integerIdx;
    protected short integerCtorIdx;
    protected short integerUnboxIdx;
    protected short longIdx;
    protected short longCtorIdx;
    protected short longUnboxIdx;
    protected short shortIdx;
    protected short shortCtorIdx;
    protected short shortUnboxIdx;
    protected final short NUM_COMMON_CPOOL_ENTRIES = 30;
    protected final short NUM_BOXING_CPOOL_ENTRIES = 72;
    private ClassFileAssembler illegalArgumentCodeBuffer;
    static final Unsafe unsafe = Unsafe.getUnsafe();
    protected static final Class<?>[] primitiveTypes = {Boolean.TYPE, Byte.TYPE, Character.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE};

    AccessorGenerator() {
    }

    protected void emitCommonConstantPoolEntries() {
        this.asm.emitConstantPoolUTF8("java/lang/Throwable");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.throwableClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/ClassCastException");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.classCastClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/NullPointerException");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.nullPointerClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/IllegalArgumentException");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.illegalArgumentClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/reflect/InvocationTargetException");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.invocationTargetClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8(Constants.CONSTRUCTOR_NAME);
        this.initIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("()V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.initNameAndTypeIdx = this.asm.cpi();
        this.asm.emitConstantPoolMethodref(this.nullPointerClass, this.initNameAndTypeIdx);
        this.nullPointerCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolMethodref(this.illegalArgumentClass, this.initNameAndTypeIdx);
        this.illegalArgumentCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(Ljava/lang/String;)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.initStringNameAndTypeIdx = this.asm.cpi();
        this.asm.emitConstantPoolMethodref(this.illegalArgumentClass, this.initStringNameAndTypeIdx);
        this.illegalArgumentStringCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(Ljava/lang/Throwable;)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(this.invocationTargetClass, this.asm.cpi());
        this.invocationTargetCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolMethodref(this.superClass, this.initNameAndTypeIdx);
        this.superCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Object");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.objectClass = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("toString");
        this.asm.emitConstantPoolUTF8("()Ljava/lang/String;");
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(this.objectClass, this.asm.cpi());
        this.toStringIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("Code");
        this.codeIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("Exceptions");
        this.exceptionsIdx = this.asm.cpi();
    }

    protected void emitBoxingContantPoolEntries() {
        this.asm.emitConstantPoolUTF8("java/lang/Boolean");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.booleanIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(Z)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.booleanCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.BOOLEAN_VALUE);
        this.asm.emitConstantPoolUTF8(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.BOOLEAN_VALUE_SIG);
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.booleanUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Byte");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.byteIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(B)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.byteCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("byteValue");
        this.asm.emitConstantPoolUTF8("()B");
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.byteUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Character");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.characterIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(C)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.characterCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("charValue");
        this.asm.emitConstantPoolUTF8("()C");
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.characterUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Double");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.doubleIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(D)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.doubleCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOUBLE_VALUE);
        this.asm.emitConstantPoolUTF8(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.DOUBLE_VALUE_SIG);
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.doubleUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Float");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.floatIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(F)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.floatCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("floatValue");
        this.asm.emitConstantPoolUTF8("()F");
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.floatUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Integer");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.integerIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(I)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.integerCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.INT_VALUE);
        this.asm.emitConstantPoolUTF8("()I");
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.integerUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Long");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.longIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(J)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.longCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("longValue");
        this.asm.emitConstantPoolUTF8("()J");
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.longUnboxIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("java/lang/Short");
        this.asm.emitConstantPoolClass(this.asm.cpi());
        this.shortIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("(S)V");
        this.asm.emitConstantPoolNameAndType(this.initIdx, this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 2), this.asm.cpi());
        this.shortCtorIdx = this.asm.cpi();
        this.asm.emitConstantPoolUTF8("shortValue");
        this.asm.emitConstantPoolUTF8(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.GET_NODE_TYPE_SIG);
        this.asm.emitConstantPoolNameAndType(sub(this.asm.cpi(), (short) 1), this.asm.cpi());
        this.asm.emitConstantPoolMethodref(sub(this.asm.cpi(), (short) 6), this.asm.cpi());
        this.shortUnboxIdx = this.asm.cpi();
    }

    protected static short add(short s2, short s3) {
        return (short) (s2 + s3);
    }

    protected static short sub(short s2, short s3) {
        return (short) (s2 - s3);
    }

    protected boolean isStatic() {
        return Modifier.isStatic(this.modifiers);
    }

    protected boolean isPrivate() {
        return Modifier.isPrivate(this.modifiers);
    }

    protected static String getClassName(Class<?> cls, boolean z2) {
        if (cls.isPrimitive()) {
            if (cls == Boolean.TYPE) {
                return com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG;
            }
            if (cls == Byte.TYPE) {
                return PdfOps.B_TOKEN;
            }
            if (cls == Character.TYPE) {
                return "C";
            }
            if (cls == Double.TYPE) {
                return PdfOps.D_TOKEN;
            }
            if (cls == Float.TYPE) {
                return PdfOps.F_TOKEN;
            }
            if (cls == Integer.TYPE) {
                return "I";
            }
            if (cls == Long.TYPE) {
                return "J";
            }
            if (cls == Short.TYPE) {
                return PdfOps.S_TOKEN;
            }
            if (cls == Void.TYPE) {
                return "V";
            }
            throw new InternalError("Should have found primitive type");
        }
        if (cls.isArray()) {
            return "[" + getClassName(cls.getComponentType(), true);
        }
        if (z2) {
            return internalize("L" + cls.getName() + ";");
        }
        return internalize(cls.getName());
    }

    private static String internalize(String str) {
        return str.replace('.', '/');
    }

    protected void emitConstructor() {
        ClassFileAssembler classFileAssembler = new ClassFileAssembler();
        classFileAssembler.setMaxLocals(1);
        classFileAssembler.opc_aload_0();
        classFileAssembler.opc_invokespecial(this.superCtorIdx, 0, 0);
        classFileAssembler.opc_return();
        emitMethod(this.initIdx, classFileAssembler.getMaxLocals(), classFileAssembler, null, null);
    }

    protected void emitMethod(short s2, int i2, ClassFileAssembler classFileAssembler, ClassFileAssembler classFileAssembler2, short[] sArr) {
        short length = classFileAssembler.getLength();
        short length2 = 0;
        if (classFileAssembler2 != null) {
            length2 = classFileAssembler2.getLength();
            if (length2 % 8 != 0) {
                throw new IllegalArgumentException("Illegal exception table");
            }
        }
        int i3 = 12 + length + length2;
        int i4 = length2 / 8;
        this.asm.emitShort((short) 1);
        this.asm.emitShort(s2);
        this.asm.emitShort(add(s2, (short) 1));
        if (sArr == null) {
            this.asm.emitShort((short) 1);
        } else {
            this.asm.emitShort((short) 2);
        }
        this.asm.emitShort(this.codeIdx);
        this.asm.emitInt(i3);
        this.asm.emitShort(classFileAssembler.getMaxStack());
        this.asm.emitShort((short) Math.max(i2, (int) classFileAssembler.getMaxLocals()));
        this.asm.emitInt(length);
        this.asm.append(classFileAssembler);
        this.asm.emitShort((short) i4);
        if (classFileAssembler2 != null) {
            this.asm.append(classFileAssembler2);
        }
        this.asm.emitShort((short) 0);
        if (sArr != null) {
            this.asm.emitShort(this.exceptionsIdx);
            this.asm.emitInt(2 + (2 * sArr.length));
            this.asm.emitShort((short) sArr.length);
            for (short s3 : sArr) {
                this.asm.emitShort(s3);
            }
        }
    }

    protected short indexForPrimitiveType(Class<?> cls) {
        if (cls == Boolean.TYPE) {
            return this.booleanIdx;
        }
        if (cls == Byte.TYPE) {
            return this.byteIdx;
        }
        if (cls == Character.TYPE) {
            return this.characterIdx;
        }
        if (cls == Double.TYPE) {
            return this.doubleIdx;
        }
        if (cls == Float.TYPE) {
            return this.floatIdx;
        }
        if (cls == Integer.TYPE) {
            return this.integerIdx;
        }
        if (cls == Long.TYPE) {
            return this.longIdx;
        }
        if (cls == Short.TYPE) {
            return this.shortIdx;
        }
        throw new InternalError("Should have found primitive type");
    }

    protected short ctorIndexForPrimitiveType(Class<?> cls) {
        if (cls == Boolean.TYPE) {
            return this.booleanCtorIdx;
        }
        if (cls == Byte.TYPE) {
            return this.byteCtorIdx;
        }
        if (cls == Character.TYPE) {
            return this.characterCtorIdx;
        }
        if (cls == Double.TYPE) {
            return this.doubleCtorIdx;
        }
        if (cls == Float.TYPE) {
            return this.floatCtorIdx;
        }
        if (cls == Integer.TYPE) {
            return this.integerCtorIdx;
        }
        if (cls == Long.TYPE) {
            return this.longCtorIdx;
        }
        if (cls == Short.TYPE) {
            return this.shortCtorIdx;
        }
        throw new InternalError("Should have found primitive type");
    }

    protected static boolean canWidenTo(Class<?> cls, Class<?> cls2) {
        if (!cls.isPrimitive()) {
            return false;
        }
        if (cls == Boolean.TYPE) {
            if (cls2 == Boolean.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Byte.TYPE) {
            if (cls2 == Byte.TYPE || cls2 == Short.TYPE || cls2 == Integer.TYPE || cls2 == Long.TYPE || cls2 == Float.TYPE || cls2 == Double.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Short.TYPE) {
            if (cls2 == Short.TYPE || cls2 == Integer.TYPE || cls2 == Long.TYPE || cls2 == Float.TYPE || cls2 == Double.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Character.TYPE) {
            if (cls2 == Character.TYPE || cls2 == Integer.TYPE || cls2 == Long.TYPE || cls2 == Float.TYPE || cls2 == Double.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Integer.TYPE) {
            if (cls2 == Integer.TYPE || cls2 == Long.TYPE || cls2 == Float.TYPE || cls2 == Double.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Long.TYPE) {
            if (cls2 == Long.TYPE || cls2 == Float.TYPE || cls2 == Double.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Float.TYPE) {
            if (cls2 == Float.TYPE || cls2 == Double.TYPE) {
                return true;
            }
            return false;
        }
        if (cls == Double.TYPE && cls2 == Double.TYPE) {
            return true;
        }
        return false;
    }

    protected static void emitWideningBytecodeForPrimitiveConversion(ClassFileAssembler classFileAssembler, Class<?> cls, Class<?> cls2) {
        if (cls == Byte.TYPE || cls == Short.TYPE || cls == Character.TYPE || cls == Integer.TYPE) {
            if (cls2 == Long.TYPE) {
                classFileAssembler.opc_i2l();
                return;
            } else if (cls2 == Float.TYPE) {
                classFileAssembler.opc_i2f();
                return;
            } else {
                if (cls2 == Double.TYPE) {
                    classFileAssembler.opc_i2d();
                    return;
                }
                return;
            }
        }
        if (cls == Long.TYPE) {
            if (cls2 == Float.TYPE) {
                classFileAssembler.opc_l2f();
                return;
            } else {
                if (cls2 == Double.TYPE) {
                    classFileAssembler.opc_l2d();
                    return;
                }
                return;
            }
        }
        if (cls == Float.TYPE && cls2 == Double.TYPE) {
            classFileAssembler.opc_f2d();
        }
    }

    protected short unboxingMethodForPrimitiveType(Class<?> cls) {
        if (cls == Boolean.TYPE) {
            return this.booleanUnboxIdx;
        }
        if (cls == Byte.TYPE) {
            return this.byteUnboxIdx;
        }
        if (cls == Character.TYPE) {
            return this.characterUnboxIdx;
        }
        if (cls == Short.TYPE) {
            return this.shortUnboxIdx;
        }
        if (cls == Integer.TYPE) {
            return this.integerUnboxIdx;
        }
        if (cls == Long.TYPE) {
            return this.longUnboxIdx;
        }
        if (cls == Float.TYPE) {
            return this.floatUnboxIdx;
        }
        if (cls == Double.TYPE) {
            return this.doubleUnboxIdx;
        }
        throw new InternalError("Illegal primitive type " + cls.getName());
    }

    protected static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() && cls != Void.TYPE;
    }

    protected int typeSizeInStackSlots(Class<?> cls) {
        if (cls == Void.TYPE) {
            return 0;
        }
        if (cls == Long.TYPE || cls == Double.TYPE) {
            return 2;
        }
        return 1;
    }

    protected ClassFileAssembler illegalArgumentCodeBuffer() {
        if (this.illegalArgumentCodeBuffer == null) {
            this.illegalArgumentCodeBuffer = new ClassFileAssembler();
            this.illegalArgumentCodeBuffer.opc_new(this.illegalArgumentClass);
            this.illegalArgumentCodeBuffer.opc_dup();
            this.illegalArgumentCodeBuffer.opc_invokespecial(this.illegalArgumentCtorIdx, 0, 0);
            this.illegalArgumentCodeBuffer.opc_athrow();
        }
        return this.illegalArgumentCodeBuffer;
    }
}
