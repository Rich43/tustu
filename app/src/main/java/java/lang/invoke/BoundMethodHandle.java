package java.lang.invoke;

import com.sun.org.apache.bcel.internal.Constants;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import sun.invoke.util.ValueConversions;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/BoundMethodHandle.class */
abstract class BoundMethodHandle extends MethodHandle {
    private static final int FIELD_COUNT_THRESHOLD = 12;
    private static final int FORM_EXPRESSION_THRESHOLD = 24;
    private static final MethodHandles.Lookup LOOKUP;
    static final SpeciesData SPECIES_DATA;
    private static final SpeciesData[] SPECIES_DATA_CACHE;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract SpeciesData speciesData();

    abstract int fieldCount();

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.lang.invoke.MethodHandle
    public abstract BoundMethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm);

    abstract BoundMethodHandle copyWithExtendL(MethodType methodType, LambdaForm lambdaForm, Object obj);

    abstract BoundMethodHandle copyWithExtendI(MethodType methodType, LambdaForm lambdaForm, int i2);

    abstract BoundMethodHandle copyWithExtendJ(MethodType methodType, LambdaForm lambdaForm, long j2);

    abstract BoundMethodHandle copyWithExtendF(MethodType methodType, LambdaForm lambdaForm, float f2);

    abstract BoundMethodHandle copyWithExtendD(MethodType methodType, LambdaForm lambdaForm, double d2);

    static {
        $assertionsDisabled = !BoundMethodHandle.class.desiredAssertionStatus();
        LOOKUP = MethodHandles.Lookup.IMPL_LOOKUP;
        SPECIES_DATA = SpeciesData.EMPTY;
        SPECIES_DATA_CACHE = new SpeciesData[5];
    }

    BoundMethodHandle(MethodType methodType, LambdaForm lambdaForm) {
        super(methodType, lambdaForm);
        if (!$assertionsDisabled && speciesData() != speciesData(lambdaForm)) {
            throw new AssertionError();
        }
    }

    static BoundMethodHandle bindSingle(MethodType methodType, LambdaForm lambdaForm, LambdaForm.BasicType basicType, Object obj) {
        try {
            switch (basicType) {
                case L_TYPE:
                    return bindSingle(methodType, lambdaForm, obj);
                case I_TYPE:
                    return SpeciesData.EMPTY.extendWith(LambdaForm.BasicType.I_TYPE).constructor().invokeBasic(methodType, lambdaForm, ValueConversions.widenSubword(obj));
                case J_TYPE:
                    return SpeciesData.EMPTY.extendWith(LambdaForm.BasicType.J_TYPE).constructor().invokeBasic(methodType, lambdaForm, ((Long) obj).longValue());
                case F_TYPE:
                    return SpeciesData.EMPTY.extendWith(LambdaForm.BasicType.F_TYPE).constructor().invokeBasic(methodType, lambdaForm, ((Float) obj).floatValue());
                case D_TYPE:
                    return SpeciesData.EMPTY.extendWith(LambdaForm.BasicType.D_TYPE).constructor().invokeBasic(methodType, lambdaForm, ((Double) obj).doubleValue());
                default:
                    throw MethodHandleStatics.newInternalError("unexpected xtype: " + ((Object) basicType));
            }
        } catch (Throwable th) {
            throw MethodHandleStatics.newInternalError(th);
        }
    }

    LambdaFormEditor editor() {
        return this.form.editor();
    }

    static BoundMethodHandle bindSingle(MethodType methodType, LambdaForm lambdaForm, Object obj) {
        return Species_L.make(methodType, lambdaForm, obj);
    }

    @Override // java.lang.invoke.MethodHandle
    BoundMethodHandle bindArgumentL(int i2, Object obj) {
        return editor().bindArgumentL(this, i2, obj);
    }

    BoundMethodHandle bindArgumentI(int i2, int i3) {
        return editor().bindArgumentI(this, i2, i3);
    }

    BoundMethodHandle bindArgumentJ(int i2, long j2) {
        return editor().bindArgumentJ(this, i2, j2);
    }

    BoundMethodHandle bindArgumentF(int i2, float f2) {
        return editor().bindArgumentF(this, i2, f2);
    }

    BoundMethodHandle bindArgumentD(int i2, double d2) {
        return editor().bindArgumentD(this, i2, d2);
    }

    @Override // java.lang.invoke.MethodHandle
    BoundMethodHandle rebind() {
        if (!tooComplex()) {
            return this;
        }
        return makeReinvoker(this);
    }

    private boolean tooComplex() {
        return fieldCount() > 12 || this.form.expressionCount() > 24;
    }

    static BoundMethodHandle makeReinvoker(MethodHandle methodHandle) {
        return Species_L.make(methodHandle.type(), DelegatingMethodHandle.makeReinvokerForm(methodHandle, 7, Species_L.SPECIES_DATA, Species_L.SPECIES_DATA.getterFunction(0)), methodHandle);
    }

    static SpeciesData speciesData(LambdaForm lambdaForm) {
        Object obj = lambdaForm.names[0].constraint;
        if (obj instanceof SpeciesData) {
            return (SpeciesData) obj;
        }
        return SpeciesData.EMPTY;
    }

    @Override // java.lang.invoke.MethodHandle
    Object internalProperties() {
        return "\n& BMH=" + internalValues();
    }

    @Override // java.lang.invoke.MethodHandle
    final Object internalValues() {
        Object[] objArr = new Object[speciesData().fieldCount()];
        for (int i2 = 0; i2 < objArr.length; i2++) {
            objArr[i2] = arg(i2);
        }
        return Arrays.asList(objArr);
    }

    final Object arg(int i2) {
        try {
            switch (speciesData().fieldType(i2)) {
                case L_TYPE:
                    return speciesData().getters[i2].invokeBasic(this);
                case I_TYPE:
                    return Integer.valueOf(speciesData().getters[i2].invokeBasic(this));
                case J_TYPE:
                    return Long.valueOf(speciesData().getters[i2].invokeBasic(this));
                case F_TYPE:
                    return Float.valueOf(speciesData().getters[i2].invokeBasic(this));
                case D_TYPE:
                    return Double.valueOf(speciesData().getters[i2].invokeBasic(this));
                default:
                    throw new InternalError("unexpected type: " + speciesData().typeChars + "." + i2);
            }
        } catch (Throwable th) {
            throw MethodHandleStatics.newInternalError(th);
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/BoundMethodHandle$Species_L.class */
    private static final class Species_L extends BoundMethodHandle {
        final Object argL0;
        static final SpeciesData SPECIES_DATA = new SpeciesData("L", Species_L.class);

        private Species_L(MethodType methodType, LambdaForm lambdaForm, Object obj) {
            super(methodType, lambdaForm);
            this.argL0 = obj;
        }

        @Override // java.lang.invoke.BoundMethodHandle
        SpeciesData speciesData() {
            return SPECIES_DATA;
        }

        @Override // java.lang.invoke.BoundMethodHandle
        int fieldCount() {
            return 1;
        }

        static BoundMethodHandle make(MethodType methodType, LambdaForm lambdaForm, Object obj) {
            return new Species_L(methodType, lambdaForm, obj);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // java.lang.invoke.BoundMethodHandle, java.lang.invoke.MethodHandle
        public final BoundMethodHandle copyWith(MethodType methodType, LambdaForm lambdaForm) {
            return new Species_L(methodType, lambdaForm, this.argL0);
        }

        @Override // java.lang.invoke.BoundMethodHandle
        final BoundMethodHandle copyWithExtendL(MethodType methodType, LambdaForm lambdaForm, Object obj) {
            try {
                return SPECIES_DATA.extendWith(LambdaForm.BasicType.L_TYPE).constructor().invokeBasic(methodType, lambdaForm, this.argL0, obj);
            } catch (Throwable th) {
                throw MethodHandleStatics.uncaughtException(th);
            }
        }

        @Override // java.lang.invoke.BoundMethodHandle
        final BoundMethodHandle copyWithExtendI(MethodType methodType, LambdaForm lambdaForm, int i2) {
            try {
                return SPECIES_DATA.extendWith(LambdaForm.BasicType.I_TYPE).constructor().invokeBasic(methodType, lambdaForm, this.argL0, i2);
            } catch (Throwable th) {
                throw MethodHandleStatics.uncaughtException(th);
            }
        }

        @Override // java.lang.invoke.BoundMethodHandle
        final BoundMethodHandle copyWithExtendJ(MethodType methodType, LambdaForm lambdaForm, long j2) {
            try {
                return SPECIES_DATA.extendWith(LambdaForm.BasicType.J_TYPE).constructor().invokeBasic(methodType, lambdaForm, this.argL0, j2);
            } catch (Throwable th) {
                throw MethodHandleStatics.uncaughtException(th);
            }
        }

        @Override // java.lang.invoke.BoundMethodHandle
        final BoundMethodHandle copyWithExtendF(MethodType methodType, LambdaForm lambdaForm, float f2) {
            try {
                return SPECIES_DATA.extendWith(LambdaForm.BasicType.F_TYPE).constructor().invokeBasic(methodType, lambdaForm, this.argL0, f2);
            } catch (Throwable th) {
                throw MethodHandleStatics.uncaughtException(th);
            }
        }

        @Override // java.lang.invoke.BoundMethodHandle
        final BoundMethodHandle copyWithExtendD(MethodType methodType, LambdaForm lambdaForm, double d2) {
            try {
                return SPECIES_DATA.extendWith(LambdaForm.BasicType.D_TYPE).constructor().invokeBasic(methodType, lambdaForm, this.argL0, d2);
            } catch (Throwable th) {
                throw MethodHandleStatics.uncaughtException(th);
            }
        }
    }

    /* loaded from: rt.jar:java/lang/invoke/BoundMethodHandle$SpeciesData.class */
    static class SpeciesData {
        private final String typeChars;
        private final LambdaForm.BasicType[] typeCodes;
        private final Class<? extends BoundMethodHandle> clazz;

        @Stable
        private final MethodHandle[] constructor;

        @Stable
        private final MethodHandle[] getters;

        @Stable
        private final LambdaForm.NamedFunction[] nominalGetters;

        @Stable
        private final SpeciesData[] extensions;
        static final SpeciesData EMPTY;
        private static final ConcurrentMap<String, SpeciesData> CACHE;
        private static final boolean INIT_DONE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !BoundMethodHandle.class.desiredAssertionStatus();
            EMPTY = new SpeciesData("", BoundMethodHandle.class);
            CACHE = new ConcurrentHashMap();
            EMPTY.initForBootstrap();
            Species_L.SPECIES_DATA.initForBootstrap();
            if (!$assertionsDisabled && !speciesDataCachePopulated()) {
                throw new AssertionError();
            }
            INIT_DONE = Boolean.TRUE.booleanValue();
        }

        int fieldCount() {
            return this.typeCodes.length;
        }

        LambdaForm.BasicType fieldType(int i2) {
            return this.typeCodes[i2];
        }

        char fieldTypeChar(int i2) {
            return this.typeChars.charAt(i2);
        }

        Object fieldSignature() {
            return this.typeChars;
        }

        public Class<? extends BoundMethodHandle> fieldHolder() {
            return this.clazz;
        }

        public String toString() {
            return "SpeciesData<" + fieldSignature() + ">";
        }

        LambdaForm.NamedFunction getterFunction(int i2) {
            LambdaForm.NamedFunction namedFunction = this.nominalGetters[i2];
            if (!$assertionsDisabled && namedFunction.memberDeclaringClassOrNull() != fieldHolder()) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || namedFunction.returnType() == fieldType(i2)) {
                return namedFunction;
            }
            throw new AssertionError();
        }

        LambdaForm.NamedFunction[] getterFunctions() {
            return this.nominalGetters;
        }

        MethodHandle[] getterHandles() {
            return this.getters;
        }

        MethodHandle constructor() {
            return this.constructor[0];
        }

        SpeciesData(String str, Class<? extends BoundMethodHandle> cls) {
            this.typeChars = str;
            this.typeCodes = LambdaForm.BasicType.basicTypes(str);
            this.clazz = cls;
            if (!INIT_DONE) {
                this.constructor = new MethodHandle[1];
                this.getters = new MethodHandle[str.length()];
                this.nominalGetters = new LambdaForm.NamedFunction[str.length()];
            } else {
                this.constructor = Factory.makeCtors(cls, str, null);
                this.getters = Factory.makeGetters(cls, str, null);
                this.nominalGetters = Factory.makeNominalGetters(str, null, this.getters);
            }
            this.extensions = new SpeciesData[LambdaForm.BasicType.ARG_TYPE_LIMIT];
        }

        private void initForBootstrap() {
            if (!$assertionsDisabled && INIT_DONE) {
                throw new AssertionError();
            }
            if (constructor() == null) {
                String str = this.typeChars;
                CACHE.put(str, this);
                Factory.makeCtors(this.clazz, str, this.constructor);
                Factory.makeGetters(this.clazz, str, this.getters);
                Factory.makeNominalGetters(str, this.nominalGetters, this.getters);
            }
        }

        SpeciesData extendWith(byte b2) {
            return extendWith(LambdaForm.BasicType.basicType(b2));
        }

        SpeciesData extendWith(LambdaForm.BasicType basicType) {
            int iOrdinal = basicType.ordinal();
            SpeciesData speciesData = this.extensions[iOrdinal];
            if (speciesData != null) {
                return speciesData;
            }
            SpeciesData[] speciesDataArr = this.extensions;
            SpeciesData speciesData2 = get(this.typeChars + basicType.basicTypeChar());
            speciesDataArr[iOrdinal] = speciesData2;
            return speciesData2;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static SpeciesData get(String str) {
            return CACHE.computeIfAbsent(str, new Function<String, SpeciesData>() { // from class: java.lang.invoke.BoundMethodHandle.SpeciesData.1
                @Override // java.util.function.Function
                public SpeciesData apply(String str2) throws SecurityException, IllegalArgumentException {
                    Class<? extends BoundMethodHandle> concreteBMHClass = Factory.getConcreteBMHClass(str2);
                    SpeciesData speciesData = new SpeciesData(str2, concreteBMHClass);
                    Factory.setSpeciesDataToConcreteBMHClass(concreteBMHClass, speciesData);
                    return speciesData;
                }
            });
        }

        static boolean speciesDataCachePopulated() {
            try {
                for (Class<?> cls : BoundMethodHandle.class.getDeclaredClasses()) {
                    if (BoundMethodHandle.class.isAssignableFrom(cls)) {
                        Class<? extends U> clsAsSubclass = cls.asSubclass(BoundMethodHandle.class);
                        SpeciesData speciesDataFromConcreteBMHClass = Factory.getSpeciesDataFromConcreteBMHClass(clsAsSubclass);
                        if (!$assertionsDisabled && speciesDataFromConcreteBMHClass == null) {
                            throw new AssertionError((Object) clsAsSubclass.getName());
                        }
                        if (!$assertionsDisabled && speciesDataFromConcreteBMHClass.clazz != clsAsSubclass) {
                            throw new AssertionError();
                        }
                        if (!$assertionsDisabled && CACHE.get(speciesDataFromConcreteBMHClass.typeChars) != speciesDataFromConcreteBMHClass) {
                            throw new AssertionError();
                        }
                    }
                }
                return true;
            } catch (Throwable th) {
                throw MethodHandleStatics.newInternalError(th);
            }
        }
    }

    static SpeciesData getSpeciesData(String str) {
        return SpeciesData.get(str);
    }

    /* loaded from: rt.jar:java/lang/invoke/BoundMethodHandle$Factory.class */
    static class Factory {
        static final String JLO_SIG = "Ljava/lang/Object;";
        static final String JLS_SIG = "Ljava/lang/String;";
        static final String JLC_SIG = "Ljava/lang/Class;";
        static final String MH = "java/lang/invoke/MethodHandle";
        static final String MH_SIG = "Ljava/lang/invoke/MethodHandle;";
        static final String BMH = "java/lang/invoke/BoundMethodHandle";
        static final String BMH_SIG = "Ljava/lang/invoke/BoundMethodHandle;";
        static final String SPECIES_DATA = "java/lang/invoke/BoundMethodHandle$SpeciesData";
        static final String SPECIES_DATA_SIG = "Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        static final String STABLE_SIG = "Ljava/lang/invoke/Stable;";
        static final String SPECIES_PREFIX_NAME = "Species_";
        static final String SPECIES_PREFIX_PATH = "java/lang/invoke/BoundMethodHandle$Species_";
        static final String BMHSPECIES_DATA_EWI_SIG = "(B)Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        static final String BMHSPECIES_DATA_GFC_SIG = "(Ljava/lang/String;Ljava/lang/Class;)Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        static final String MYSPECIES_DATA_SIG = "()Ljava/lang/invoke/BoundMethodHandle$SpeciesData;";
        static final String VOID_SIG = "()V";
        static final String INT_SIG = "()I";
        static final String SIG_INCIPIT = "(Ljava/lang/invoke/MethodType;Ljava/lang/invoke/LambdaForm;";
        static final String[] E_THROWABLE;
        static final ConcurrentMap<String, Class<? extends BoundMethodHandle>> CLASS_CACHE;
        static final /* synthetic */ boolean $assertionsDisabled;

        Factory() {
        }

        static {
            $assertionsDisabled = !BoundMethodHandle.class.desiredAssertionStatus();
            E_THROWABLE = new String[]{"java/lang/Throwable"};
            CLASS_CACHE = new ConcurrentHashMap();
        }

        static Class<? extends BoundMethodHandle> getConcreteBMHClass(String str) {
            return CLASS_CACHE.computeIfAbsent(str, new Function<String, Class<? extends BoundMethodHandle>>() { // from class: java.lang.invoke.BoundMethodHandle.Factory.1
                @Override // java.util.function.Function
                public Class<? extends BoundMethodHandle> apply(String str2) {
                    return Factory.generateConcreteBMHClass(str2);
                }
            });
        }

        static Class<? extends BoundMethodHandle> generateConcreteBMHClass(String str) {
            ClassWriter classWriter = new ClassWriter(3);
            String strShortenSignature = LambdaForm.shortenSignature(str);
            String str2 = SPECIES_PREFIX_PATH + strShortenSignature;
            String str3 = SPECIES_PREFIX_NAME + strShortenSignature;
            classWriter.visit(50, 48, str2, null, BMH, null);
            classWriter.visitSource(str3, null);
            FieldVisitor fieldVisitorVisitField = classWriter.visitField(8, "SPECIES_DATA", SPECIES_DATA_SIG, null, null);
            fieldVisitorVisitField.visitAnnotation(STABLE_SIG, true);
            fieldVisitorVisitField.visitEnd();
            for (int i2 = 0; i2 < str.length(); i2++) {
                char cCharAt = str.charAt(i2);
                classWriter.visitField(16, makeFieldName(str, i2), cCharAt == 'L' ? "Ljava/lang/Object;" : String.valueOf(cCharAt), null, null).visitEnd();
            }
            MethodVisitor methodVisitorVisitMethod = classWriter.visitMethod(2, Constants.CONSTRUCTOR_NAME, makeSignature(str, true), null, null);
            methodVisitorVisitMethod.visitCode();
            methodVisitorVisitMethod.visitVarInsn(25, 0);
            methodVisitorVisitMethod.visitVarInsn(25, 1);
            methodVisitorVisitMethod.visitVarInsn(25, 2);
            methodVisitorVisitMethod.visitMethodInsn(183, BMH, Constants.CONSTRUCTOR_NAME, makeSignature("", true), false);
            int i3 = 0;
            int i4 = 0;
            while (i3 < str.length()) {
                char cCharAt2 = str.charAt(i3);
                methodVisitorVisitMethod.visitVarInsn(25, 0);
                methodVisitorVisitMethod.visitVarInsn(typeLoadOp(cCharAt2), i4 + 3);
                methodVisitorVisitMethod.visitFieldInsn(181, str2, makeFieldName(str, i3), typeSig(cCharAt2));
                if (cCharAt2 == 'J' || cCharAt2 == 'D') {
                    i4++;
                }
                i3++;
                i4++;
            }
            methodVisitorVisitMethod.visitInsn(177);
            methodVisitorVisitMethod.visitMaxs(0, 0);
            methodVisitorVisitMethod.visitEnd();
            MethodVisitor methodVisitorVisitMethod2 = classWriter.visitMethod(16, "speciesData", MYSPECIES_DATA_SIG, null, null);
            methodVisitorVisitMethod2.visitCode();
            methodVisitorVisitMethod2.visitFieldInsn(178, str2, "SPECIES_DATA", SPECIES_DATA_SIG);
            methodVisitorVisitMethod2.visitInsn(176);
            methodVisitorVisitMethod2.visitMaxs(0, 0);
            methodVisitorVisitMethod2.visitEnd();
            MethodVisitor methodVisitorVisitMethod3 = classWriter.visitMethod(16, "fieldCount", "()I", null, null);
            methodVisitorVisitMethod3.visitCode();
            int length = str.length();
            if (length <= 5) {
                methodVisitorVisitMethod3.visitInsn(3 + length);
            } else {
                methodVisitorVisitMethod3.visitIntInsn(17, length);
            }
            methodVisitorVisitMethod3.visitInsn(172);
            methodVisitorVisitMethod3.visitMaxs(0, 0);
            methodVisitorVisitMethod3.visitEnd();
            MethodVisitor methodVisitorVisitMethod4 = classWriter.visitMethod(8, "make", makeSignature(str, false), null, null);
            methodVisitorVisitMethod4.visitCode();
            methodVisitorVisitMethod4.visitTypeInsn(187, str2);
            methodVisitorVisitMethod4.visitInsn(89);
            methodVisitorVisitMethod4.visitVarInsn(25, 0);
            methodVisitorVisitMethod4.visitVarInsn(25, 1);
            int i5 = 0;
            int i6 = 0;
            while (i5 < str.length()) {
                char cCharAt3 = str.charAt(i5);
                methodVisitorVisitMethod4.visitVarInsn(typeLoadOp(cCharAt3), i6 + 2);
                if (cCharAt3 == 'J' || cCharAt3 == 'D') {
                    i6++;
                }
                i5++;
                i6++;
            }
            methodVisitorVisitMethod4.visitMethodInsn(183, str2, Constants.CONSTRUCTOR_NAME, makeSignature(str, true), false);
            methodVisitorVisitMethod4.visitInsn(176);
            methodVisitorVisitMethod4.visitMaxs(0, 0);
            methodVisitorVisitMethod4.visitEnd();
            MethodVisitor methodVisitorVisitMethod5 = classWriter.visitMethod(16, "copyWith", makeSignature("", false), null, null);
            methodVisitorVisitMethod5.visitCode();
            methodVisitorVisitMethod5.visitTypeInsn(187, str2);
            methodVisitorVisitMethod5.visitInsn(89);
            methodVisitorVisitMethod5.visitVarInsn(25, 1);
            methodVisitorVisitMethod5.visitVarInsn(25, 2);
            emitPushFields(str, str2, methodVisitorVisitMethod5);
            methodVisitorVisitMethod5.visitMethodInsn(183, str2, Constants.CONSTRUCTOR_NAME, makeSignature(str, true), false);
            methodVisitorVisitMethod5.visitInsn(176);
            methodVisitorVisitMethod5.visitMaxs(0, 0);
            methodVisitorVisitMethod5.visitEnd();
            for (LambdaForm.BasicType basicType : LambdaForm.BasicType.ARG_TYPES) {
                int iOrdinal = basicType.ordinal();
                char cBasicTypeChar = basicType.basicTypeChar();
                MethodVisitor methodVisitorVisitMethod6 = classWriter.visitMethod(16, "copyWithExtend" + cBasicTypeChar, makeSignature(String.valueOf(cBasicTypeChar), false), null, E_THROWABLE);
                methodVisitorVisitMethod6.visitCode();
                methodVisitorVisitMethod6.visitFieldInsn(178, str2, "SPECIES_DATA", SPECIES_DATA_SIG);
                int i7 = 3 + iOrdinal;
                if (!$assertionsDisabled && i7 > 8) {
                    throw new AssertionError();
                }
                methodVisitorVisitMethod6.visitInsn(i7);
                methodVisitorVisitMethod6.visitMethodInsn(182, SPECIES_DATA, "extendWith", BMHSPECIES_DATA_EWI_SIG, false);
                methodVisitorVisitMethod6.visitMethodInsn(182, SPECIES_DATA, "constructor", "()Ljava/lang/invoke/MethodHandle;", false);
                methodVisitorVisitMethod6.visitVarInsn(25, 1);
                methodVisitorVisitMethod6.visitVarInsn(25, 2);
                emitPushFields(str, str2, methodVisitorVisitMethod6);
                methodVisitorVisitMethod6.visitVarInsn(typeLoadOp(cBasicTypeChar), 3);
                methodVisitorVisitMethod6.visitMethodInsn(182, MH, "invokeBasic", makeSignature(str + cBasicTypeChar, false), false);
                methodVisitorVisitMethod6.visitInsn(176);
                methodVisitorVisitMethod6.visitMaxs(0, 0);
                methodVisitorVisitMethod6.visitEnd();
            }
            classWriter.visitEnd();
            byte[] byteArray = classWriter.toByteArray();
            InvokerBytecodeGenerator.maybeDump(str2, byteArray);
            return MethodHandleStatics.UNSAFE.defineClass(str2, byteArray, 0, byteArray.length, BoundMethodHandle.class.getClassLoader(), null).asSubclass(BoundMethodHandle.class);
        }

        private static int typeLoadOp(char c2) {
            switch (c2) {
                case 'D':
                    return 24;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                default:
                    throw MethodHandleStatics.newInternalError("unrecognized type " + c2);
                case 'F':
                    return 23;
                case 'I':
                    return 21;
                case 'J':
                    return 22;
                case 'L':
                    return 25;
            }
        }

        private static void emitPushFields(String str, String str2, MethodVisitor methodVisitor) {
            for (int i2 = 0; i2 < str.length(); i2++) {
                char cCharAt = str.charAt(i2);
                methodVisitor.visitVarInsn(25, 0);
                methodVisitor.visitFieldInsn(180, str2, makeFieldName(str, i2), typeSig(cCharAt));
            }
        }

        static String typeSig(char c2) {
            return c2 == 'L' ? "Ljava/lang/Object;" : String.valueOf(c2);
        }

        private static MethodHandle makeGetter(Class<?> cls, String str, int i2) {
            try {
                return BoundMethodHandle.LOOKUP.findGetter(cls, makeFieldName(str, i2), Wrapper.forBasicType(str.charAt(i2)).primitiveType());
            } catch (IllegalAccessException | NoSuchFieldException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }

        static MethodHandle[] makeGetters(Class<?> cls, String str, MethodHandle[] methodHandleArr) {
            if (methodHandleArr == null) {
                methodHandleArr = new MethodHandle[str.length()];
            }
            for (int i2 = 0; i2 < methodHandleArr.length; i2++) {
                methodHandleArr[i2] = makeGetter(cls, str, i2);
                if (!$assertionsDisabled && methodHandleArr[i2].internalMemberName().getDeclaringClass() != cls) {
                    throw new AssertionError();
                }
            }
            return methodHandleArr;
        }

        static MethodHandle[] makeCtors(Class<? extends BoundMethodHandle> cls, String str, MethodHandle[] methodHandleArr) {
            if (methodHandleArr == null) {
                methodHandleArr = new MethodHandle[1];
            }
            if (str.equals("")) {
                return methodHandleArr;
            }
            methodHandleArr[0] = makeCbmhCtor(cls, str);
            return methodHandleArr;
        }

        static LambdaForm.NamedFunction[] makeNominalGetters(String str, LambdaForm.NamedFunction[] namedFunctionArr, MethodHandle[] methodHandleArr) {
            if (namedFunctionArr == null) {
                namedFunctionArr = new LambdaForm.NamedFunction[str.length()];
            }
            for (int i2 = 0; i2 < namedFunctionArr.length; i2++) {
                namedFunctionArr[i2] = new LambdaForm.NamedFunction(methodHandleArr[i2]);
            }
            return namedFunctionArr;
        }

        static SpeciesData getSpeciesDataFromConcreteBMHClass(Class<? extends BoundMethodHandle> cls) {
            try {
                return (SpeciesData) cls.getDeclaredField("SPECIES_DATA").get(null);
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }

        static void setSpeciesDataToConcreteBMHClass(Class<? extends BoundMethodHandle> cls, SpeciesData speciesData) throws SecurityException, IllegalArgumentException {
            try {
                Field declaredField = cls.getDeclaredField("SPECIES_DATA");
                if (!$assertionsDisabled && declaredField.getDeclaredAnnotation(Stable.class) == null) {
                    throw new AssertionError();
                }
                declaredField.set(null, speciesData);
            } catch (ReflectiveOperationException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }

        private static String makeFieldName(String str, int i2) {
            if ($assertionsDisabled || (i2 >= 0 && i2 < str.length())) {
                return com.sun.org.apache.xalan.internal.templates.Constants.ELEMNAME_ARG_STRING + str.charAt(i2) + i2;
            }
            throw new AssertionError();
        }

        private static String makeSignature(String str, boolean z2) {
            StringBuilder sb = new StringBuilder(SIG_INCIPIT);
            for (char c2 : str.toCharArray()) {
                sb.append(typeSig(c2));
            }
            return sb.append(')').append(z2 ? "V" : BMH_SIG).toString();
        }

        static MethodHandle makeCbmhCtor(Class<? extends BoundMethodHandle> cls, String str) {
            try {
                return BoundMethodHandle.LOOKUP.findStatic(cls, "make", MethodType.fromMethodDescriptorString(makeSignature(str, false), null));
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchMethodException | TypeNotPresentException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }
    }

    private static SpeciesData checkCache(int i2, String str) {
        int i3 = i2 - 1;
        SpeciesData speciesData = SPECIES_DATA_CACHE[i3];
        if (speciesData != null) {
            return speciesData;
        }
        SpeciesData[] speciesDataArr = SPECIES_DATA_CACHE;
        SpeciesData speciesData2 = getSpeciesData(str);
        speciesDataArr[i3] = speciesData2;
        return speciesData2;
    }

    static SpeciesData speciesData_L() {
        return checkCache(1, "L");
    }

    static SpeciesData speciesData_LL() {
        return checkCache(2, "LL");
    }

    static SpeciesData speciesData_LLL() {
        return checkCache(3, "LLL");
    }

    static SpeciesData speciesData_LLLL() {
        return checkCache(4, "LLLL");
    }

    static SpeciesData speciesData_LLLLL() {
        return checkCache(5, "LLLLL");
    }
}
