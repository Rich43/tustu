package java.lang.invoke;

import com.sun.javafx.fxml.BeanAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MemberName;
import java.lang.invoke.MethodHandleImpl;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import sun.invoke.util.VerifyAccess;
import sun.invoke.util.VerifyType;
import sun.invoke.util.Wrapper;
import sun.misc.Unsafe;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/lang/invoke/InvokerBytecodeGenerator.class */
class InvokerBytecodeGenerator {
    private static final String MH = "java/lang/invoke/MethodHandle";
    private static final String MHI = "java/lang/invoke/MethodHandleImpl";
    private static final String LF = "java/lang/invoke/LambdaForm";
    private static final String LFN = "java/lang/invoke/LambdaForm$Name";
    private static final String CLS = "java/lang/Class";
    private static final String OBJ = "java/lang/Object";
    private static final String OBJARY = "[Ljava/lang/Object;";
    private static final String MH_SIG = "Ljava/lang/invoke/MethodHandle;";
    private static final String LF_SIG = "Ljava/lang/invoke/LambdaForm;";
    private static final String LFN_SIG = "Ljava/lang/invoke/LambdaForm$Name;";
    private static final String LL_SIG = "(Ljava/lang/Object;)Ljava/lang/Object;";
    private static final String LLV_SIG = "(Ljava/lang/Object;Ljava/lang/Object;)V";
    private static final String CLL_SIG = "(Ljava/lang/Class;Ljava/lang/Object;)Ljava/lang/Object;";
    private static final String superName = "java/lang/Object";
    private final String className;
    private final String sourceFile;
    private final LambdaForm lambdaForm;
    private final String invokerName;
    private final MethodType invokerType;
    private final int[] localsMap;
    private final LambdaForm.BasicType[] localTypes;
    private final Class<?>[] localClasses;
    private ClassWriter cw;
    private MethodVisitor mv;
    private static final MemberName.Factory MEMBERNAME_FACTORY;
    private static final Class<?> HOST_CLASS;
    private static final HashMap<String, Integer> DUMP_CLASS_FILES_COUNTERS;
    private static final File DUMP_CLASS_FILES_DIR;
    Map<Object, CpPatch> cpPatches;
    int cph;
    private static Class<?>[] STATICALLY_INVOCABLE_PACKAGES;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InvokerBytecodeGenerator.class.desiredAssertionStatus();
        MEMBERNAME_FACTORY = MemberName.getFactory();
        HOST_CLASS = LambdaForm.class;
        if (MethodHandleStatics.DUMP_CLASS_FILES) {
            DUMP_CLASS_FILES_COUNTERS = new HashMap<>();
            try {
                File file = new File("DUMP_CLASS_FILES");
                if (!file.exists()) {
                    file.mkdirs();
                }
                DUMP_CLASS_FILES_DIR = file;
                System.out.println("Dumping class files to " + ((Object) DUMP_CLASS_FILES_DIR) + "/...");
            } catch (Exception e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        } else {
            DUMP_CLASS_FILES_COUNTERS = null;
            DUMP_CLASS_FILES_DIR = null;
        }
        STATICALLY_INVOCABLE_PACKAGES = new Class[]{Object.class, Arrays.class, Unsafe.class};
    }

    private InvokerBytecodeGenerator(LambdaForm lambdaForm, int i2, String str, String str2, MethodType methodType) {
        this.cpPatches = new HashMap();
        this.cph = 0;
        if (str2.contains(".")) {
            int iIndexOf = str2.indexOf(".");
            str = str2.substring(0, iIndexOf);
            str2 = str2.substring(iIndexOf + 1);
        }
        str = MethodHandleStatics.DUMP_CLASS_FILES ? makeDumpableClassName(str) : str;
        this.className = "java/lang/invoke/LambdaForm$" + str;
        this.sourceFile = "LambdaForm$" + str;
        this.lambdaForm = lambdaForm;
        this.invokerName = str2;
        this.invokerType = methodType;
        this.localsMap = new int[i2 + 1];
        this.localTypes = new LambdaForm.BasicType[i2 + 1];
        this.localClasses = new Class[i2 + 1];
    }

    private InvokerBytecodeGenerator(String str, String str2, MethodType methodType) {
        this(null, methodType.parameterCount(), str, str2, methodType);
        this.localTypes[this.localTypes.length - 1] = LambdaForm.BasicType.V_TYPE;
        for (int i2 = 0; i2 < this.localsMap.length; i2++) {
            this.localsMap[i2] = methodType.parameterSlotCount() - methodType.parameterSlotDepth(i2);
            if (i2 < methodType.parameterCount()) {
                this.localTypes[i2] = LambdaForm.BasicType.basicType(methodType.parameterType(i2));
            }
        }
    }

    private InvokerBytecodeGenerator(String str, LambdaForm lambdaForm, MethodType methodType) {
        this(lambdaForm, lambdaForm.names.length, str, lambdaForm.debugName, methodType);
        LambdaForm.Name[] nameArr = lambdaForm.names;
        int iBasicTypeSlots = 0;
        for (int i2 = 0; i2 < this.localsMap.length; i2++) {
            this.localsMap[i2] = iBasicTypeSlots;
            if (i2 < nameArr.length) {
                LambdaForm.BasicType basicTypeType = nameArr[i2].type();
                iBasicTypeSlots += basicTypeType.basicTypeSlots();
                this.localTypes[i2] = basicTypeType;
            }
        }
    }

    static void maybeDump(final String str, final byte[] bArr) {
        if (MethodHandleStatics.DUMP_CLASS_FILES) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.invoke.InvokerBytecodeGenerator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    try {
                        File file = new File(InvokerBytecodeGenerator.DUMP_CLASS_FILES_DIR, str + ".class");
                        System.out.println("dump: " + ((Object) file));
                        file.getParentFile().mkdirs();
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        fileOutputStream.write(bArr);
                        fileOutputStream.close();
                        return null;
                    } catch (IOException e2) {
                        throw MethodHandleStatics.newInternalError(e2);
                    }
                }
            });
        }
    }

    private static String makeDumpableClassName(String str) {
        Integer num;
        synchronized (DUMP_CLASS_FILES_COUNTERS) {
            num = DUMP_CLASS_FILES_COUNTERS.get(str);
            if (num == null) {
                num = 0;
            }
            DUMP_CLASS_FILES_COUNTERS.put(str, Integer.valueOf(num.intValue() + 1));
        }
        String string = num.toString();
        while (true) {
            String str2 = string;
            if (str2.length() < 3) {
                string = "0" + str2;
            } else {
                return str + str2;
            }
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/InvokerBytecodeGenerator$CpPatch.class */
    class CpPatch {
        final int index;
        final String placeholder;
        final Object value;

        CpPatch(int i2, String str, Object obj) {
            this.index = i2;
            this.placeholder = str;
            this.value = obj;
        }

        public String toString() {
            return "CpPatch/index=" + this.index + ",placeholder=" + this.placeholder + ",value=" + this.value;
        }
    }

    String constantPlaceholder(Object obj) {
        StringBuilder sbAppend = new StringBuilder().append("CONSTANT_PLACEHOLDER_");
        int i2 = this.cph;
        this.cph = i2 + 1;
        String string = sbAppend.append(i2).toString();
        if (MethodHandleStatics.DUMP_CLASS_FILES) {
            string = string + " <<" + debugString(obj) + ">>";
        }
        if (this.cpPatches.containsKey(string)) {
            throw new InternalError("observed CP placeholder twice: " + string);
        }
        this.cpPatches.put(string, new CpPatch(this.cw.newConst(string), string, obj));
        return string;
    }

    Object[] cpPatches(byte[] bArr) {
        int constantPoolSize = getConstantPoolSize(bArr);
        Object[] objArr = new Object[constantPoolSize];
        for (CpPatch cpPatch : this.cpPatches.values()) {
            if (cpPatch.index >= constantPoolSize) {
                throw new InternalError("in cpool[" + constantPoolSize + "]: " + ((Object) cpPatch) + "\n" + Arrays.toString(Arrays.copyOf(bArr, 20)));
            }
            objArr[cpPatch.index] = cpPatch.value;
        }
        return objArr;
    }

    private static String debugString(Object obj) {
        if (obj instanceof MethodHandle) {
            MethodHandle methodHandle = (MethodHandle) obj;
            MemberName memberNameInternalMemberName = methodHandle.internalMemberName();
            if (memberNameInternalMemberName != null) {
                return memberNameInternalMemberName.toString();
            }
            return methodHandle.debugString();
        }
        return obj.toString();
    }

    private static int getConstantPoolSize(byte[] bArr) {
        return ((bArr[8] & 255) << 8) | (bArr[9] & 255);
    }

    private MemberName loadMethod(byte[] bArr) {
        return resolveInvokerMember(loadAndInitializeInvokerClass(bArr, cpPatches(bArr)), this.invokerName, this.invokerType);
    }

    private static Class<?> loadAndInitializeInvokerClass(byte[] bArr, Object[] objArr) {
        Class<?> clsDefineAnonymousClass = MethodHandleStatics.UNSAFE.defineAnonymousClass(HOST_CLASS, bArr, objArr);
        MethodHandleStatics.UNSAFE.ensureClassInitialized(clsDefineAnonymousClass);
        return clsDefineAnonymousClass;
    }

    private static MemberName resolveInvokerMember(Class<?> cls, String str, MethodType methodType) {
        try {
            return MEMBERNAME_FACTORY.resolveOrFail((byte) 6, new MemberName(cls, str, methodType, (byte) 6), HOST_CLASS, ReflectiveOperationException.class);
        } catch (ReflectiveOperationException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    private void classFilePrologue() {
        this.cw = new ClassWriter(3);
        this.cw.visit(52, 48, this.className, null, "java/lang/Object", null);
        this.cw.visitSource(this.sourceFile, null);
        this.mv = this.cw.visitMethod(8, this.invokerName, this.invokerType.toMethodDescriptorString(), null, null);
    }

    private void classFileEpilogue() {
        this.mv.visitMaxs(0, 0);
        this.mv.visitEnd();
    }

    private void emitConst(Object obj) {
        if (obj == null) {
            this.mv.visitInsn(1);
            return;
        }
        if (obj instanceof Integer) {
            emitIconstInsn(((Integer) obj).intValue());
            return;
        }
        if (obj instanceof Long) {
            long jLongValue = ((Long) obj).longValue();
            if (jLongValue == ((short) jLongValue)) {
                emitIconstInsn((int) jLongValue);
                this.mv.visitInsn(133);
                return;
            }
        }
        if (obj instanceof Float) {
            float fFloatValue = ((Float) obj).floatValue();
            if (fFloatValue == ((short) fFloatValue)) {
                emitIconstInsn((int) fFloatValue);
                this.mv.visitInsn(134);
                return;
            }
        }
        if (obj instanceof Double) {
            double dDoubleValue = ((Double) obj).doubleValue();
            if (dDoubleValue == ((short) dDoubleValue)) {
                emitIconstInsn((int) dDoubleValue);
                this.mv.visitInsn(135);
                return;
            }
        }
        if (obj instanceof Boolean) {
            emitIconstInsn(((Boolean) obj).booleanValue() ? 1 : 0);
        } else {
            this.mv.visitLdcInsn(obj);
        }
    }

    private void emitIconstInsn(int i2) {
        int i3;
        switch (i2) {
            case 0:
                i3 = 3;
                break;
            case 1:
                i3 = 4;
                break;
            case 2:
                i3 = 5;
                break;
            case 3:
                i3 = 6;
                break;
            case 4:
                i3 = 7;
                break;
            case 5:
                i3 = 8;
                break;
            default:
                if (i2 == ((byte) i2)) {
                    this.mv.visitIntInsn(16, i2 & 255);
                    return;
                } else if (i2 == ((short) i2)) {
                    this.mv.visitIntInsn(17, (char) i2);
                    return;
                } else {
                    this.mv.visitLdcInsn(Integer.valueOf(i2));
                    return;
                }
        }
        this.mv.visitInsn(i3);
    }

    private void emitLoadInsn(LambdaForm.BasicType basicType, int i2) throws InternalError {
        this.mv.visitVarInsn(loadInsnOpcode(basicType), this.localsMap[i2]);
    }

    private int loadInsnOpcode(LambdaForm.BasicType basicType) throws InternalError {
        switch (basicType) {
            case I_TYPE:
                return 21;
            case J_TYPE:
                return 22;
            case F_TYPE:
                return 23;
            case D_TYPE:
                return 24;
            case L_TYPE:
                return 25;
            default:
                throw new InternalError("unknown type: " + ((Object) basicType));
        }
    }

    private void emitAloadInsn(int i2) throws InternalError {
        emitLoadInsn(LambdaForm.BasicType.L_TYPE, i2);
    }

    private void emitStoreInsn(LambdaForm.BasicType basicType, int i2) throws InternalError {
        this.mv.visitVarInsn(storeInsnOpcode(basicType), this.localsMap[i2]);
    }

    private int storeInsnOpcode(LambdaForm.BasicType basicType) throws InternalError {
        switch (basicType) {
            case I_TYPE:
                return 54;
            case J_TYPE:
                return 55;
            case F_TYPE:
                return 56;
            case D_TYPE:
                return 57;
            case L_TYPE:
                return 58;
            default:
                throw new InternalError("unknown type: " + ((Object) basicType));
        }
    }

    private void emitAstoreInsn(int i2) throws InternalError {
        emitStoreInsn(LambdaForm.BasicType.L_TYPE, i2);
    }

    private byte arrayTypeCode(Wrapper wrapper) {
        switch (wrapper) {
            case BOOLEAN:
                return (byte) 4;
            case BYTE:
                return (byte) 8;
            case CHAR:
                return (byte) 5;
            case SHORT:
                return (byte) 9;
            case INT:
                return (byte) 10;
            case LONG:
                return (byte) 11;
            case FLOAT:
                return (byte) 6;
            case DOUBLE:
                return (byte) 7;
            case OBJECT:
                return (byte) 0;
            default:
                throw new InternalError();
        }
    }

    private int arrayInsnOpcode(byte b2, int i2) throws InternalError {
        int i3;
        if (!$assertionsDisabled && i2 != 83 && i2 != 50) {
            throw new AssertionError();
        }
        switch (b2) {
            case 0:
                i3 = 83;
                break;
            case 1:
            case 2:
            case 3:
            default:
                throw new InternalError();
            case 4:
                i3 = 84;
                break;
            case 5:
                i3 = 85;
                break;
            case 6:
                i3 = 81;
                break;
            case 7:
                i3 = 82;
                break;
            case 8:
                i3 = 84;
                break;
            case 9:
                i3 = 86;
                break;
            case 10:
                i3 = 79;
                break;
            case 11:
                i3 = 80;
                break;
        }
        return (i3 - 83) + i2;
    }

    private void freeFrameLocal(int i2) {
        int iIndexForFrameLocal = indexForFrameLocal(i2);
        if (iIndexForFrameLocal < 0) {
            return;
        }
        LambdaForm.BasicType basicType = this.localTypes[iIndexForFrameLocal];
        int iMakeLocalTemp = makeLocalTemp(basicType);
        this.mv.visitVarInsn(loadInsnOpcode(basicType), i2);
        this.mv.visitVarInsn(storeInsnOpcode(basicType), iMakeLocalTemp);
        if (!$assertionsDisabled && this.localsMap[iIndexForFrameLocal] != i2) {
            throw new AssertionError();
        }
        this.localsMap[iIndexForFrameLocal] = iMakeLocalTemp;
        if (!$assertionsDisabled && indexForFrameLocal(i2) >= 0) {
            throw new AssertionError();
        }
    }

    private int indexForFrameLocal(int i2) {
        for (int i3 = 0; i3 < this.localsMap.length; i3++) {
            if (this.localsMap[i3] == i2 && this.localTypes[i3] != LambdaForm.BasicType.V_TYPE) {
                return i3;
            }
        }
        return -1;
    }

    private int makeLocalTemp(LambdaForm.BasicType basicType) {
        int i2 = this.localsMap[this.localsMap.length - 1];
        this.localsMap[this.localsMap.length - 1] = i2 + basicType.basicTypeSlots();
        return i2;
    }

    private void emitBoxing(Wrapper wrapper) {
        String str = "java/lang/" + wrapper.wrapperType().getSimpleName();
        this.mv.visitMethodInsn(184, str, BeanAdapter.VALUE_OF_METHOD_NAME, "(" + wrapper.basicTypeChar() + ")L" + str + ";", false);
    }

    private void emitUnboxing(Wrapper wrapper) throws InternalError {
        String str = "java/lang/" + wrapper.wrapperType().getSimpleName();
        String str2 = wrapper.primitiveSimpleName() + "Value";
        String str3 = "()" + wrapper.basicTypeChar();
        emitReferenceCast(wrapper.wrapperType(), null);
        this.mv.visitMethodInsn(182, str, str2, str3, false);
    }

    private void emitImplicitConversion(LambdaForm.BasicType basicType, Class<?> cls, Object obj) throws InternalError {
        if (!$assertionsDisabled && LambdaForm.BasicType.basicType(cls) != basicType) {
            throw new AssertionError();
        }
        if (cls == basicType.basicTypeClass() && basicType != LambdaForm.BasicType.L_TYPE) {
            return;
        }
        switch (basicType) {
            case I_TYPE:
                if (!VerifyType.isNullConversion((Class<?>) Integer.TYPE, cls, false)) {
                    emitPrimCast(basicType.basicTypeWrapper(), Wrapper.forPrimitiveType(cls));
                    return;
                }
                return;
            case L_TYPE:
                if (VerifyType.isNullConversion((Class<?>) Object.class, cls, false)) {
                    if (MethodHandleStatics.PROFILE_LEVEL > 0) {
                        emitReferenceCast(Object.class, obj);
                        return;
                    }
                    return;
                }
                emitReferenceCast(cls, obj);
                return;
            default:
                throw MethodHandleStatics.newInternalError("bad implicit conversion: tc=" + ((Object) basicType) + ": " + ((Object) cls));
        }
    }

    private boolean assertStaticType(Class<?> cls, LambdaForm.Name name) {
        int iIndex = name.index();
        Class<?> cls2 = this.localClasses[iIndex];
        if (cls2 != null && (cls2 == cls || cls.isAssignableFrom(cls2))) {
            return true;
        }
        if (cls2 == null || cls2.isAssignableFrom(cls)) {
            this.localClasses[iIndex] = cls;
            return false;
        }
        return false;
    }

    private void emitReferenceCast(Class<?> cls, Object obj) throws InternalError {
        LambdaForm.Name name = null;
        if (obj instanceof LambdaForm.Name) {
            LambdaForm.Name name2 = (LambdaForm.Name) obj;
            if (assertStaticType(cls, name2)) {
                return;
            }
            if (this.lambdaForm.useCount(name2) > 1) {
                name = name2;
            }
        }
        if (isStaticallyNameable(cls)) {
            this.mv.visitTypeInsn(192, getInternalName(cls));
        } else {
            this.mv.visitLdcInsn(constantPlaceholder(cls));
            this.mv.visitTypeInsn(192, CLS);
            this.mv.visitInsn(95);
            this.mv.visitMethodInsn(184, MHI, "castReference", CLL_SIG, false);
            if (Object[].class.isAssignableFrom(cls)) {
                this.mv.visitTypeInsn(192, OBJARY);
            } else if (MethodHandleStatics.PROFILE_LEVEL > 0) {
                this.mv.visitTypeInsn(192, "java/lang/Object");
            }
        }
        if (name != null) {
            this.mv.visitInsn(89);
            emitAstoreInsn(name.index());
        }
    }

    private void emitReturnInsn(LambdaForm.BasicType basicType) {
        int i2;
        switch (basicType) {
            case I_TYPE:
                i2 = 172;
                break;
            case J_TYPE:
                i2 = 173;
                break;
            case F_TYPE:
                i2 = 174;
                break;
            case D_TYPE:
                i2 = 175;
                break;
            case L_TYPE:
                i2 = 176;
                break;
            case V_TYPE:
                i2 = 177;
                break;
            default:
                throw new InternalError("unknown return type: " + ((Object) basicType));
        }
        this.mv.visitInsn(i2);
    }

    private static String getInternalName(Class<?> cls) {
        if (cls == Object.class) {
            return "java/lang/Object";
        }
        if (cls == Object[].class) {
            return OBJARY;
        }
        if (cls == Class.class) {
            return CLS;
        }
        if (cls == MethodHandle.class) {
            return MH;
        }
        if ($assertionsDisabled || VerifyAccess.isTypeVisible(cls, (Class<?>) Object.class)) {
            return cls.getName().replace('.', '/');
        }
        throw new AssertionError((Object) cls.getName());
    }

    static MemberName generateCustomizedCode(LambdaForm lambdaForm, MethodType methodType) {
        InvokerBytecodeGenerator invokerBytecodeGenerator = new InvokerBytecodeGenerator("MH", lambdaForm, methodType);
        return invokerBytecodeGenerator.loadMethod(invokerBytecodeGenerator.generateCustomizedCodeBytes());
    }

    private boolean checkActualReceiver() {
        this.mv.visitInsn(89);
        this.mv.visitVarInsn(25, this.localsMap[0]);
        this.mv.visitMethodInsn(184, MHI, "assertSame", LLV_SIG, false);
        return true;
    }

    private byte[] generateCustomizedCodeBytes() throws InternalError {
        classFilePrologue();
        this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
        this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Compiled;", true);
        if (this.lambdaForm.forceInline) {
            this.mv.visitAnnotation("Ljava/lang/invoke/ForceInline;", true);
        } else {
            this.mv.visitAnnotation("Ljava/lang/invoke/DontInline;", true);
        }
        if (this.lambdaForm.customized != null) {
            this.mv.visitLdcInsn(constantPlaceholder(this.lambdaForm.customized));
            this.mv.visitTypeInsn(192, MH);
            if (!$assertionsDisabled && !checkActualReceiver()) {
                throw new AssertionError();
            }
            this.mv.visitVarInsn(58, this.localsMap[0]);
        }
        LambdaForm.Name nameEmitGuardWithCatch = null;
        int i2 = this.lambdaForm.arity;
        while (i2 < this.lambdaForm.names.length) {
            LambdaForm.Name name = this.lambdaForm.names[i2];
            emitStoreResult(nameEmitGuardWithCatch);
            nameEmitGuardWithCatch = name;
            MethodHandleImpl.Intrinsic intrinsicIntrinsicName = name.function.intrinsicName();
            switch (intrinsicIntrinsicName) {
                case SELECT_ALTERNATIVE:
                    if (!$assertionsDisabled && !isSelectAlternative(i2)) {
                        throw new AssertionError();
                    }
                    if (MethodHandleStatics.PROFILE_GWT) {
                        if (!$assertionsDisabled && (!(name.arguments[0] instanceof LambdaForm.Name) || !nameRefersTo((LambdaForm.Name) name.arguments[0], MethodHandleImpl.class, "profileBoolean"))) {
                            throw new AssertionError();
                        }
                        this.mv.visitAnnotation("Ljava/lang/invoke/InjectedProfile;", true);
                    }
                    nameEmitGuardWithCatch = emitSelectAlternative(name, this.lambdaForm.names[i2 + 1]);
                    i2++;
                    continue;
                    i2++;
                    break;
                case GUARD_WITH_CATCH:
                    if (!$assertionsDisabled && !isGuardWithCatch(i2)) {
                        throw new AssertionError();
                    }
                    nameEmitGuardWithCatch = emitGuardWithCatch(i2);
                    i2 += 2;
                    continue;
                    i2++;
                    break;
                case NEW_ARRAY:
                    if (isStaticallyNameable(name.function.methodType().returnType())) {
                        emitNewArray(name);
                    }
                    i2++;
                    break;
                case ARRAY_LOAD:
                    emitArrayLoad(name);
                    continue;
                    i2++;
                case ARRAY_STORE:
                    emitArrayStore(name);
                    continue;
                    i2++;
                case IDENTITY:
                    if (!$assertionsDisabled && name.arguments.length != 1) {
                        throw new AssertionError();
                    }
                    emitPushArguments(name);
                    continue;
                    i2++;
                    break;
                case ZERO:
                    if (!$assertionsDisabled && name.arguments.length != 0) {
                        throw new AssertionError();
                    }
                    emitConst(name.type.basicTypeWrapper().zero());
                    continue;
                    i2++;
                    break;
                case NONE:
                    break;
                default:
                    throw MethodHandleStatics.newInternalError("Unknown intrinsic: " + ((Object) intrinsicIntrinsicName));
            }
            MemberName memberNameMember = name.function.member();
            if (isStaticallyInvocable(memberNameMember)) {
                emitStaticInvoke(memberNameMember, name);
            } else {
                emitInvoke(name);
            }
            i2++;
        }
        emitReturn(nameEmitGuardWithCatch);
        classFileEpilogue();
        bogusMethod(this.lambdaForm);
        byte[] byteArray = this.cw.toByteArray();
        maybeDump(this.className, byteArray);
        return byteArray;
    }

    void emitArrayLoad(LambdaForm.Name name) throws InternalError {
        emitArrayOp(name, 50);
    }

    void emitArrayStore(LambdaForm.Name name) throws InternalError {
        emitArrayOp(name, 83);
    }

    void emitArrayOp(LambdaForm.Name name, int i2) throws InternalError {
        if (!$assertionsDisabled && i2 != 50 && i2 != 83) {
            throw new AssertionError();
        }
        Class<?> componentType = name.function.methodType().parameterType(0).getComponentType();
        if (!$assertionsDisabled && componentType == null) {
            throw new AssertionError();
        }
        emitPushArguments(name);
        if (componentType.isPrimitive()) {
            i2 = arrayInsnOpcode(arrayTypeCode(Wrapper.forPrimitiveType(componentType)), i2);
        }
        this.mv.visitInsn(i2);
    }

    void emitInvoke(LambdaForm.Name name) throws InternalError {
        if (!$assertionsDisabled && isLinkerMethodInvoke(name)) {
            throw new AssertionError();
        }
        MethodHandle methodHandle = name.function.resolvedHandle;
        if (!$assertionsDisabled && methodHandle == null) {
            throw new AssertionError((Object) name.exprString());
        }
        this.mv.visitLdcInsn(constantPlaceholder(methodHandle));
        emitReferenceCast(MethodHandle.class, methodHandle);
        emitPushArguments(name);
        this.mv.visitMethodInsn(182, MH, "invokeBasic", name.function.methodType().basicType().toMethodDescriptorString(), false);
    }

    static boolean isStaticallyInvocable(LambdaForm.Name name) {
        return isStaticallyInvocable(name.function.member());
    }

    static boolean isStaticallyInvocable(MemberName memberName) {
        if (memberName == null || memberName.isConstructor()) {
            return false;
        }
        Class<?> declaringClass = memberName.getDeclaringClass();
        if (declaringClass.isArray() || declaringClass.isPrimitive() || declaringClass.isAnonymousClass() || declaringClass.isLocalClass() || declaringClass.getClassLoader() != MethodHandle.class.getClassLoader() || ReflectUtil.isVMAnonymousClass(declaringClass)) {
            return false;
        }
        MethodType methodOrFieldType = memberName.getMethodOrFieldType();
        if (!isStaticallyNameable(methodOrFieldType.returnType())) {
            return false;
        }
        for (Class<?> cls : methodOrFieldType.parameterArray()) {
            if (!isStaticallyNameable(cls)) {
                return false;
            }
        }
        if (!memberName.isPrivate() && VerifyAccess.isSamePackage(MethodHandle.class, declaringClass)) {
            return true;
        }
        if (memberName.isPublic() && isStaticallyNameable(declaringClass)) {
            return true;
        }
        return false;
    }

    static boolean isStaticallyNameable(Class<?> cls) {
        if (cls == Object.class) {
            return true;
        }
        while (cls.isArray()) {
            cls = cls.getComponentType();
        }
        if (cls.isPrimitive()) {
            return true;
        }
        if (ReflectUtil.isVMAnonymousClass(cls) || cls.getClassLoader() != Object.class.getClassLoader()) {
            return false;
        }
        if (VerifyAccess.isSamePackage(MethodHandle.class, cls)) {
            return true;
        }
        if (!Modifier.isPublic(cls.getModifiers())) {
            return false;
        }
        for (Class<?> cls2 : STATICALLY_INVOCABLE_PACKAGES) {
            if (VerifyAccess.isSamePackage(cls2, cls)) {
                return true;
            }
        }
        return false;
    }

    void emitStaticInvoke(LambdaForm.Name name) throws InternalError {
        emitStaticInvoke(name.function.member(), name);
    }

    void emitStaticInvoke(MemberName memberName, LambdaForm.Name name) throws InternalError {
        if (!$assertionsDisabled && !memberName.equals(name.function.member())) {
            throw new AssertionError();
        }
        String internalName = getInternalName(memberName.getDeclaringClass());
        String name2 = memberName.getName();
        byte referenceKind = memberName.getReferenceKind();
        if (referenceKind == 7) {
            if (!$assertionsDisabled && !memberName.canBeStaticallyBound()) {
                throw new AssertionError(memberName);
            }
            referenceKind = 5;
        }
        if (memberName.getDeclaringClass().isInterface() && referenceKind == 5) {
            referenceKind = 9;
        }
        emitPushArguments(name);
        if (memberName.isMethod()) {
            this.mv.visitMethodInsn(refKindOpcode(referenceKind), internalName, name2, memberName.getMethodType().toMethodDescriptorString(), memberName.getDeclaringClass().isInterface());
        } else {
            this.mv.visitFieldInsn(refKindOpcode(referenceKind), internalName, name2, MethodType.toFieldDescriptorString(memberName.getFieldType()));
        }
        if (name.type == LambdaForm.BasicType.L_TYPE) {
            Class<?> clsReturnType = memberName.getInvocationType().returnType();
            if (!$assertionsDisabled && clsReturnType.isPrimitive()) {
                throw new AssertionError();
            }
            if (clsReturnType != Object.class && !clsReturnType.isInterface()) {
                assertStaticType(clsReturnType, name);
            }
        }
    }

    void emitNewArray(LambdaForm.Name name) throws InternalError {
        Class<?> clsReturnType = name.function.methodType().returnType();
        if (name.arguments.length == 0) {
            try {
                Object objInvoke = (Object) name.function.resolvedHandle.invoke();
                if (!$assertionsDisabled && Array.getLength(objInvoke) != 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && objInvoke.getClass() != clsReturnType) {
                    throw new AssertionError();
                }
                this.mv.visitLdcInsn(constantPlaceholder(objInvoke));
                emitReferenceCast(clsReturnType, objInvoke);
                return;
            } catch (Throwable th) {
                throw MethodHandleStatics.newInternalError(th);
            }
        }
        Class<?> componentType = clsReturnType.getComponentType();
        if (!$assertionsDisabled && componentType == null) {
            throw new AssertionError();
        }
        emitIconstInsn(name.arguments.length);
        int iArrayInsnOpcode = 83;
        if (!componentType.isPrimitive()) {
            this.mv.visitTypeInsn(189, getInternalName(componentType));
        } else {
            byte bArrayTypeCode = arrayTypeCode(Wrapper.forPrimitiveType(componentType));
            iArrayInsnOpcode = arrayInsnOpcode(bArrayTypeCode, 83);
            this.mv.visitIntInsn(188, bArrayTypeCode);
        }
        for (int i2 = 0; i2 < name.arguments.length; i2++) {
            this.mv.visitInsn(89);
            emitIconstInsn(i2);
            emitPushArgument(name, i2);
            this.mv.visitInsn(iArrayInsnOpcode);
        }
        assertStaticType(clsReturnType, name);
    }

    int refKindOpcode(byte b2) {
        switch (b2) {
            case 1:
                return 180;
            case 2:
                return 178;
            case 3:
                return 181;
            case 4:
                return 179;
            case 5:
                return 182;
            case 6:
                return 184;
            case 7:
                return 183;
            case 8:
            default:
                throw new InternalError("refKind=" + ((int) b2));
            case 9:
                return 185;
        }
    }

    private boolean memberRefersTo(MemberName memberName, Class<?> cls, String str) {
        return memberName != null && memberName.getDeclaringClass() == cls && memberName.getName().equals(str);
    }

    private boolean nameRefersTo(LambdaForm.Name name, Class<?> cls, String str) {
        return name.function != null && memberRefersTo(name.function.member(), cls, str);
    }

    private boolean isInvokeBasic(LambdaForm.Name name) {
        if (name.function == null || name.arguments.length < 1) {
            return false;
        }
        MemberName memberNameMember = name.function.member();
        return (!memberRefersTo(memberNameMember, MethodHandle.class, "invokeBasic") || memberNameMember.isPublic() || memberNameMember.isStatic()) ? false : true;
    }

    private boolean isLinkerMethodInvoke(LambdaForm.Name name) {
        MemberName memberNameMember;
        return name.function != null && name.arguments.length >= 1 && (memberNameMember = name.function.member()) != null && memberNameMember.getDeclaringClass() == MethodHandle.class && !memberNameMember.isPublic() && memberNameMember.isStatic() && memberNameMember.getName().startsWith("linkTo");
    }

    private boolean isSelectAlternative(int i2) {
        if (i2 + 1 >= this.lambdaForm.names.length) {
            return false;
        }
        LambdaForm.Name name = this.lambdaForm.names[i2];
        LambdaForm.Name name2 = this.lambdaForm.names[i2 + 1];
        return nameRefersTo(name, MethodHandleImpl.class, "selectAlternative") && isInvokeBasic(name2) && name2.lastUseIndex(name) == 0 && this.lambdaForm.lastUseIndex(name) == i2 + 1;
    }

    private boolean isGuardWithCatch(int i2) {
        if (i2 + 2 >= this.lambdaForm.names.length) {
            return false;
        }
        LambdaForm.Name name = this.lambdaForm.names[i2];
        LambdaForm.Name name2 = this.lambdaForm.names[i2 + 1];
        LambdaForm.Name name3 = this.lambdaForm.names[i2 + 2];
        return nameRefersTo(name2, MethodHandleImpl.class, "guardWithCatch") && isInvokeBasic(name) && isInvokeBasic(name3) && name2.lastUseIndex(name) == 3 && this.lambdaForm.lastUseIndex(name) == i2 + 1 && name3.lastUseIndex(name2) == 1 && this.lambdaForm.lastUseIndex(name2) == i2 + 2;
    }

    private LambdaForm.Name emitSelectAlternative(LambdaForm.Name name, LambdaForm.Name name2) throws InternalError {
        if (!$assertionsDisabled && !isStaticallyInvocable(name2)) {
            throw new AssertionError();
        }
        LambdaForm.Name name3 = (LambdaForm.Name) name2.arguments[0];
        Label label = new Label();
        Label label2 = new Label();
        emitPushArgument(name, 0);
        this.mv.visitJumpInsn(153, label);
        Class[] clsArr = (Class[]) this.localClasses.clone();
        emitPushArgument(name, 1);
        emitAstoreInsn(name3.index());
        emitStaticInvoke(name2);
        this.mv.visitJumpInsn(167, label2);
        this.mv.visitLabel(label);
        System.arraycopy(clsArr, 0, this.localClasses, 0, clsArr.length);
        emitPushArgument(name, 2);
        emitAstoreInsn(name3.index());
        emitStaticInvoke(name2);
        this.mv.visitLabel(label2);
        System.arraycopy(clsArr, 0, this.localClasses, 0, clsArr.length);
        return name2;
    }

    private LambdaForm.Name emitGuardWithCatch(int i2) throws InternalError {
        LambdaForm.Name name = this.lambdaForm.names[i2];
        LambdaForm.Name name2 = this.lambdaForm.names[i2 + 1];
        LambdaForm.Name name3 = this.lambdaForm.names[i2 + 2];
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        Label label4 = new Label();
        MethodType methodTypeChangeReturnType = name.function.resolvedHandle.type().dropParameterTypes(0, 1).changeReturnType(name3.function.resolvedHandle.type().returnType());
        this.mv.visitTryCatchBlock(label, label2, label3, "java/lang/Throwable");
        this.mv.visitLabel(label);
        emitPushArgument(name2, 0);
        emitPushArguments(name, 1);
        this.mv.visitMethodInsn(182, MH, "invokeBasic", methodTypeChangeReturnType.basicType().toMethodDescriptorString(), false);
        this.mv.visitLabel(label2);
        this.mv.visitJumpInsn(167, label4);
        this.mv.visitLabel(label3);
        this.mv.visitInsn(89);
        emitPushArgument(name2, 1);
        this.mv.visitInsn(95);
        this.mv.visitMethodInsn(182, CLS, "isInstance", "(Ljava/lang/Object;)Z", false);
        Label label5 = new Label();
        this.mv.visitJumpInsn(153, label5);
        emitPushArgument(name2, 2);
        this.mv.visitInsn(95);
        emitPushArguments(name, 1);
        this.mv.visitMethodInsn(182, MH, "invokeBasic", methodTypeChangeReturnType.insertParameterTypes(0, Throwable.class).basicType().toMethodDescriptorString(), false);
        this.mv.visitJumpInsn(167, label4);
        this.mv.visitLabel(label5);
        this.mv.visitInsn(191);
        this.mv.visitLabel(label4);
        return name3;
    }

    private void emitPushArguments(LambdaForm.Name name) throws InternalError {
        emitPushArguments(name, 0);
    }

    private void emitPushArguments(LambdaForm.Name name, int i2) throws InternalError {
        for (int i3 = i2; i3 < name.arguments.length; i3++) {
            emitPushArgument(name, i3);
        }
    }

    private void emitPushArgument(LambdaForm.Name name, int i2) throws InternalError {
        emitPushArgument(name.function.methodType().parameterType(i2), name.arguments[i2]);
    }

    private void emitPushArgument(Class<?> cls, Object obj) throws InternalError {
        LambdaForm.BasicType basicType = LambdaForm.BasicType.basicType(cls);
        if (obj instanceof LambdaForm.Name) {
            LambdaForm.Name name = (LambdaForm.Name) obj;
            emitLoadInsn(name.type, name.index());
            emitImplicitConversion(name.type, cls, name);
        } else if ((obj == null || (obj instanceof String)) && basicType == LambdaForm.BasicType.L_TYPE) {
            emitConst(obj);
        } else if (Wrapper.isWrapperType(obj.getClass()) && basicType != LambdaForm.BasicType.L_TYPE) {
            emitConst(obj);
        } else {
            this.mv.visitLdcInsn(constantPlaceholder(obj));
            emitImplicitConversion(LambdaForm.BasicType.L_TYPE, cls, obj);
        }
    }

    private void emitStoreResult(LambdaForm.Name name) throws InternalError {
        if (name != null && name.type != LambdaForm.BasicType.V_TYPE) {
            emitStoreInsn(name.type, name.index());
        }
    }

    private void emitReturn(LambdaForm.Name name) throws InternalError {
        Class<?> clsReturnType = this.invokerType.returnType();
        LambdaForm.BasicType basicTypeReturnType = this.lambdaForm.returnType();
        if (!$assertionsDisabled && basicTypeReturnType != LambdaForm.BasicType.basicType(clsReturnType)) {
            throw new AssertionError();
        }
        if (basicTypeReturnType == LambdaForm.BasicType.V_TYPE) {
            this.mv.visitInsn(177);
            return;
        }
        LambdaForm.Name name2 = this.lambdaForm.names[this.lambdaForm.result];
        if (name2 != name) {
            emitLoadInsn(basicTypeReturnType, this.lambdaForm.result);
        }
        emitImplicitConversion(basicTypeReturnType, clsReturnType, name2);
        emitReturnInsn(basicTypeReturnType);
    }

    private void emitPrimCast(Wrapper wrapper, Wrapper wrapper2) {
        if (wrapper == wrapper2) {
            return;
        }
        if (wrapper.isSubwordOrInt()) {
            emitI2X(wrapper2);
            return;
        }
        if (wrapper2.isSubwordOrInt()) {
            emitX2I(wrapper);
            if (wrapper2.bitWidth() < 32) {
                emitI2X(wrapper2);
                return;
            }
            return;
        }
        boolean z2 = false;
        switch (wrapper) {
            case LONG:
                switch (wrapper2) {
                    case FLOAT:
                        this.mv.visitInsn(137);
                        break;
                    case DOUBLE:
                        this.mv.visitInsn(138);
                        break;
                    default:
                        z2 = true;
                        break;
                }
            case FLOAT:
                switch (wrapper2) {
                    case LONG:
                        this.mv.visitInsn(140);
                        break;
                    case DOUBLE:
                        this.mv.visitInsn(141);
                        break;
                    default:
                        z2 = true;
                        break;
                }
            case DOUBLE:
                switch (wrapper2) {
                    case LONG:
                        this.mv.visitInsn(143);
                        break;
                    case FLOAT:
                        this.mv.visitInsn(144);
                        break;
                    default:
                        z2 = true;
                        break;
                }
            default:
                z2 = true;
                break;
        }
        if (z2) {
            throw new IllegalStateException("unhandled prim cast: " + ((Object) wrapper) + "2" + ((Object) wrapper2));
        }
    }

    private void emitI2X(Wrapper wrapper) {
        switch (wrapper) {
            case BOOLEAN:
                this.mv.visitInsn(4);
                this.mv.visitInsn(126);
                return;
            case BYTE:
                this.mv.visitInsn(145);
                return;
            case CHAR:
                this.mv.visitInsn(146);
                return;
            case SHORT:
                this.mv.visitInsn(147);
                return;
            case INT:
                return;
            case LONG:
                this.mv.visitInsn(133);
                return;
            case FLOAT:
                this.mv.visitInsn(134);
                return;
            case DOUBLE:
                this.mv.visitInsn(135);
                return;
            default:
                throw new InternalError("unknown type: " + ((Object) wrapper));
        }
    }

    private void emitX2I(Wrapper wrapper) {
        switch (wrapper) {
            case LONG:
                this.mv.visitInsn(136);
                return;
            case FLOAT:
                this.mv.visitInsn(139);
                return;
            case DOUBLE:
                this.mv.visitInsn(142);
                return;
            default:
                throw new InternalError("unknown type: " + ((Object) wrapper));
        }
    }

    static MemberName generateLambdaFormInterpreterEntryPoint(String str) {
        if (!$assertionsDisabled && !LambdaForm.isValidSignature(str)) {
            throw new AssertionError();
        }
        InvokerBytecodeGenerator invokerBytecodeGenerator = new InvokerBytecodeGenerator("LFI", "interpret_" + LambdaForm.signatureReturn(str).basicTypeChar(), LambdaForm.signatureType(str).changeParameterType(0, MethodHandle.class));
        return invokerBytecodeGenerator.loadMethod(invokerBytecodeGenerator.generateLambdaFormInterpreterEntryPointBytes());
    }

    private byte[] generateLambdaFormInterpreterEntryPointBytes() throws InternalError {
        classFilePrologue();
        this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
        this.mv.visitAnnotation("Ljava/lang/invoke/DontInline;", true);
        emitIconstInsn(this.invokerType.parameterCount());
        this.mv.visitTypeInsn(189, "java/lang/Object");
        for (int i2 = 0; i2 < this.invokerType.parameterCount(); i2++) {
            Class<?> clsParameterType = this.invokerType.parameterType(i2);
            this.mv.visitInsn(89);
            emitIconstInsn(i2);
            emitLoadInsn(LambdaForm.BasicType.basicType(clsParameterType), i2);
            if (clsParameterType.isPrimitive()) {
                emitBoxing(Wrapper.forPrimitiveType(clsParameterType));
            }
            this.mv.visitInsn(83);
        }
        emitAloadInsn(0);
        this.mv.visitFieldInsn(180, MH, "form", LF_SIG);
        this.mv.visitInsn(95);
        this.mv.visitMethodInsn(182, LF, "interpretWithArguments", "([Ljava/lang/Object;)Ljava/lang/Object;", false);
        Class<?> clsReturnType = this.invokerType.returnType();
        if (clsReturnType.isPrimitive() && clsReturnType != Void.TYPE) {
            emitUnboxing(Wrapper.forPrimitiveType(clsReturnType));
        }
        emitReturnInsn(LambdaForm.BasicType.basicType(clsReturnType));
        classFileEpilogue();
        bogusMethod(this.invokerType);
        byte[] byteArray = this.cw.toByteArray();
        maybeDump(this.className, byteArray);
        return byteArray;
    }

    static MemberName generateNamedFunctionInvoker(MethodTypeForm methodTypeForm) {
        InvokerBytecodeGenerator invokerBytecodeGenerator = new InvokerBytecodeGenerator("NFI", "invoke_" + LambdaForm.shortenSignature(LambdaForm.basicTypeSignature(methodTypeForm.erasedType())), LambdaForm.NamedFunction.INVOKER_METHOD_TYPE);
        return invokerBytecodeGenerator.loadMethod(invokerBytecodeGenerator.generateNamedFunctionInvokerImpl(methodTypeForm));
    }

    private byte[] generateNamedFunctionInvokerImpl(MethodTypeForm methodTypeForm) throws InternalError {
        MethodType methodTypeErasedType = methodTypeForm.erasedType();
        classFilePrologue();
        this.mv.visitAnnotation("Ljava/lang/invoke/LambdaForm$Hidden;", true);
        this.mv.visitAnnotation("Ljava/lang/invoke/ForceInline;", true);
        emitAloadInsn(0);
        for (int i2 = 0; i2 < methodTypeErasedType.parameterCount(); i2++) {
            emitAloadInsn(1);
            emitIconstInsn(i2);
            this.mv.visitInsn(50);
            Class<?> clsParameterType = methodTypeErasedType.parameterType(i2);
            if (clsParameterType.isPrimitive()) {
                methodTypeErasedType.basicType().wrap().parameterType(i2);
                Wrapper wrapperForBasicType = Wrapper.forBasicType(clsParameterType);
                Wrapper wrapper = wrapperForBasicType.isSubwordOrInt() ? Wrapper.INT : wrapperForBasicType;
                emitUnboxing(wrapper);
                emitPrimCast(wrapper, wrapperForBasicType);
            }
        }
        this.mv.visitMethodInsn(182, MH, "invokeBasic", methodTypeErasedType.basicType().toMethodDescriptorString(), false);
        Class<?> clsReturnType = methodTypeErasedType.returnType();
        if (clsReturnType != Void.TYPE && clsReturnType.isPrimitive()) {
            Wrapper wrapperForBasicType2 = Wrapper.forBasicType(clsReturnType);
            Wrapper wrapper2 = wrapperForBasicType2.isSubwordOrInt() ? Wrapper.INT : wrapperForBasicType2;
            emitPrimCast(wrapperForBasicType2, wrapper2);
            emitBoxing(wrapper2);
        }
        if (clsReturnType == Void.TYPE) {
            this.mv.visitInsn(1);
        }
        emitReturnInsn(LambdaForm.BasicType.L_TYPE);
        classFileEpilogue();
        bogusMethod(methodTypeErasedType);
        byte[] byteArray = this.cw.toByteArray();
        maybeDump(this.className, byteArray);
        return byteArray;
    }

    private void bogusMethod(Object... objArr) {
        if (MethodHandleStatics.DUMP_CLASS_FILES) {
            this.mv = this.cw.visitMethod(8, "dummy", "()V", null, null);
            for (Object obj : objArr) {
                this.mv.visitLdcInsn(obj.toString());
                this.mv.visitInsn(87);
            }
            this.mv.visitInsn(177);
            this.mv.visitMaxs(0, 0);
            this.mv.visitEnd();
        }
    }
}
