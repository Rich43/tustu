package java.lang.invoke;

import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import sun.invoke.util.BytecodeDescriptor;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/TypeConvertingMethodAdapter.class */
class TypeConvertingMethodAdapter extends MethodVisitor {
    private static final int NUM_WRAPPERS;
    private static final String NAME_OBJECT = "java/lang/Object";
    private static final String WRAPPER_PREFIX = "Ljava/lang/";
    private static final String NAME_BOX_METHOD = "valueOf";
    private static final int[][] wideningOpcodes;
    private static final Wrapper[] FROM_WRAPPER_NAME;
    private static final Wrapper[] FROM_TYPE_SORT;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !TypeConvertingMethodAdapter.class.desiredAssertionStatus();
        NUM_WRAPPERS = Wrapper.values().length;
        wideningOpcodes = new int[NUM_WRAPPERS][NUM_WRAPPERS];
        FROM_WRAPPER_NAME = new Wrapper[16];
        FROM_TYPE_SORT = new Wrapper[16];
        for (Wrapper wrapper : Wrapper.values()) {
            if (wrapper.basicTypeChar() != 'L') {
                int iHashWrapperName = hashWrapperName(wrapper.wrapperSimpleName());
                if (!$assertionsDisabled && FROM_WRAPPER_NAME[iHashWrapperName] != null) {
                    throw new AssertionError();
                }
                FROM_WRAPPER_NAME[iHashWrapperName] = wrapper;
            }
        }
        for (int i2 = 0; i2 < NUM_WRAPPERS; i2++) {
            for (int i3 = 0; i3 < NUM_WRAPPERS; i3++) {
                wideningOpcodes[i2][i3] = 0;
            }
        }
        initWidening(Wrapper.LONG, 133, Wrapper.BYTE, Wrapper.SHORT, Wrapper.INT, Wrapper.CHAR);
        initWidening(Wrapper.LONG, 140, Wrapper.FLOAT);
        initWidening(Wrapper.FLOAT, 134, Wrapper.BYTE, Wrapper.SHORT, Wrapper.INT, Wrapper.CHAR);
        initWidening(Wrapper.FLOAT, 137, Wrapper.LONG);
        initWidening(Wrapper.DOUBLE, 135, Wrapper.BYTE, Wrapper.SHORT, Wrapper.INT, Wrapper.CHAR);
        initWidening(Wrapper.DOUBLE, 141, Wrapper.FLOAT);
        initWidening(Wrapper.DOUBLE, 138, Wrapper.LONG);
        FROM_TYPE_SORT[3] = Wrapper.BYTE;
        FROM_TYPE_SORT[4] = Wrapper.SHORT;
        FROM_TYPE_SORT[5] = Wrapper.INT;
        FROM_TYPE_SORT[7] = Wrapper.LONG;
        FROM_TYPE_SORT[2] = Wrapper.CHAR;
        FROM_TYPE_SORT[6] = Wrapper.FLOAT;
        FROM_TYPE_SORT[8] = Wrapper.DOUBLE;
        FROM_TYPE_SORT[1] = Wrapper.BOOLEAN;
    }

    TypeConvertingMethodAdapter(MethodVisitor methodVisitor) {
        super(Opcodes.ASM5, methodVisitor);
    }

    private static void initWidening(Wrapper wrapper, int i2, Wrapper... wrapperArr) {
        for (Wrapper wrapper2 : wrapperArr) {
            wideningOpcodes[wrapper2.ordinal()][wrapper.ordinal()] = i2;
        }
    }

    private static int hashWrapperName(String str) {
        if (str.length() < 3) {
            return 0;
        }
        return ((3 * str.charAt(1)) + str.charAt(2)) % 16;
    }

    private Wrapper wrapperOrNullFromDescriptor(String str) {
        if (!str.startsWith(WRAPPER_PREFIX)) {
            return null;
        }
        String strSubstring = str.substring(WRAPPER_PREFIX.length(), str.length() - 1);
        Wrapper wrapper = FROM_WRAPPER_NAME[hashWrapperName(strSubstring)];
        if (wrapper == null || wrapper.wrapperSimpleName().equals(strSubstring)) {
            return wrapper;
        }
        return null;
    }

    private static String wrapperName(Wrapper wrapper) {
        return "java/lang/" + wrapper.wrapperSimpleName();
    }

    private static String unboxMethod(Wrapper wrapper) {
        return wrapper.primitiveSimpleName() + "Value";
    }

    private static String boxingDescriptor(Wrapper wrapper) {
        return String.format("(%s)L%s;", Character.valueOf(wrapper.basicTypeChar()), wrapperName(wrapper));
    }

    private static String unboxingDescriptor(Wrapper wrapper) {
        return "()" + wrapper.basicTypeChar();
    }

    void boxIfTypePrimitive(Type type) {
        Wrapper wrapper = FROM_TYPE_SORT[type.getSort()];
        if (wrapper != null) {
            box(wrapper);
        }
    }

    void widen(Wrapper wrapper, Wrapper wrapper2) {
        int i2;
        if (wrapper != wrapper2 && (i2 = wideningOpcodes[wrapper.ordinal()][wrapper2.ordinal()]) != 0) {
            visitInsn(i2);
        }
    }

    void box(Wrapper wrapper) {
        visitMethodInsn(184, wrapperName(wrapper), "valueOf", boxingDescriptor(wrapper), false);
    }

    void unbox(String str, Wrapper wrapper) {
        visitMethodInsn(182, str, unboxMethod(wrapper), unboxingDescriptor(wrapper), false);
    }

    private String descriptorToName(String str) {
        int length = str.length() - 1;
        if (str.charAt(0) == 'L' && str.charAt(length) == ';') {
            return str.substring(1, length);
        }
        return str;
    }

    void cast(String str, String str2) {
        String strDescriptorToName = descriptorToName(str);
        String strDescriptorToName2 = descriptorToName(str2);
        if (!strDescriptorToName2.equals(strDescriptorToName) && !strDescriptorToName2.equals(NAME_OBJECT)) {
            visitTypeInsn(192, strDescriptorToName2);
        }
    }

    private boolean isPrimitive(Wrapper wrapper) {
        return wrapper != Wrapper.OBJECT;
    }

    private Wrapper toWrapper(String str) {
        char cCharAt = str.charAt(0);
        if (cCharAt == '[' || cCharAt == '(') {
            cCharAt = 'L';
        }
        return Wrapper.forBasicType(cCharAt);
    }

    void convertType(Class<?> cls, Class<?> cls2, Class<?> cls3) {
        String strUnparse;
        String strWrapperName;
        if ((cls.equals(cls2) && cls.equals(cls3)) || cls == Void.TYPE || cls2 == Void.TYPE) {
            return;
        }
        if (cls.isPrimitive()) {
            Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
            if (cls2.isPrimitive()) {
                widen(wrapperForPrimitiveType, Wrapper.forPrimitiveType(cls2));
                return;
            }
            String strUnparse2 = BytecodeDescriptor.unparse(cls2);
            Wrapper wrapperWrapperOrNullFromDescriptor = wrapperOrNullFromDescriptor(strUnparse2);
            if (wrapperWrapperOrNullFromDescriptor != null) {
                widen(wrapperForPrimitiveType, wrapperWrapperOrNullFromDescriptor);
                box(wrapperWrapperOrNullFromDescriptor);
                return;
            } else {
                box(wrapperForPrimitiveType);
                cast(wrapperName(wrapperForPrimitiveType), strUnparse2);
                return;
            }
        }
        String strUnparse3 = BytecodeDescriptor.unparse(cls);
        if (cls3.isPrimitive()) {
            strUnparse = strUnparse3;
        } else {
            strUnparse = BytecodeDescriptor.unparse(cls3);
            cast(strUnparse3, strUnparse);
        }
        String strUnparse4 = BytecodeDescriptor.unparse(cls2);
        if (cls2.isPrimitive()) {
            Wrapper wrapper = toWrapper(strUnparse4);
            Wrapper wrapperWrapperOrNullFromDescriptor2 = wrapperOrNullFromDescriptor(strUnparse);
            if (wrapperWrapperOrNullFromDescriptor2 != null) {
                if (wrapperWrapperOrNullFromDescriptor2.isSigned() || wrapperWrapperOrNullFromDescriptor2.isFloating()) {
                    unbox(wrapperName(wrapperWrapperOrNullFromDescriptor2), wrapper);
                    return;
                } else {
                    unbox(wrapperName(wrapperWrapperOrNullFromDescriptor2), wrapperWrapperOrNullFromDescriptor2);
                    widen(wrapperWrapperOrNullFromDescriptor2, wrapper);
                    return;
                }
            }
            if (wrapper.isSigned() || wrapper.isFloating()) {
                strWrapperName = "java/lang/Number";
            } else {
                strWrapperName = wrapperName(wrapper);
            }
            cast(strUnparse, strWrapperName);
            unbox(strWrapperName, wrapper);
            return;
        }
        cast(strUnparse, strUnparse4);
    }

    void iconst(int i2) {
        if (i2 >= -1 && i2 <= 5) {
            this.mv.visitInsn(3 + i2);
            return;
        }
        if (i2 >= -128 && i2 <= 127) {
            this.mv.visitIntInsn(16, i2);
        } else if (i2 >= -32768 && i2 <= 32767) {
            this.mv.visitIntInsn(17, i2);
        } else {
            this.mv.visitLdcInsn(Integer.valueOf(i2));
        }
    }
}
