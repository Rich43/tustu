package java.lang.invoke;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.invoke.BoundMethodHandle;
import java.lang.invoke.MemberName;
import java.lang.invoke.MethodHandleImpl;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;
import sun.invoke.util.Wrapper;

/* loaded from: rt.jar:java/lang/invoke/LambdaForm.class */
class LambdaForm {
    final int arity;
    final int result;
    final boolean forceInline;
    final MethodHandle customized;

    @Stable
    final Name[] names;
    final String debugName;
    MemberName vmentry;
    private boolean isCompiled;
    volatile Object transformCache;
    public static final int VOID_RESULT = -1;
    public static final int LAST_RESULT = -2;
    private static final boolean USE_PREDEFINED_INTERPRET_METHODS = true;
    private static final int COMPILE_THRESHOLD;
    private int invocationCounter;
    static final int INTERNED_ARGUMENT_LIMIT = 10;
    private static final Name[][] INTERNED_ARGUMENTS;
    private static final MemberName.Factory IMPL_NAMES;
    private static final LambdaForm[] LF_identityForm;
    private static final LambdaForm[] LF_zeroForm;
    private static final NamedFunction[] NF_identity;
    private static final NamedFunction[] NF_zero;
    private static final HashMap<String, Integer> DEBUG_NAME_COUNTERS;
    private static final boolean TRACE_INTERPRETER;
    static final /* synthetic */ boolean $assertionsDisabled;

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: rt.jar:java/lang/invoke/LambdaForm$Compiled.class */
    @interface Compiled {
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    /* loaded from: rt.jar:java/lang/invoke/LambdaForm$Hidden.class */
    @interface Hidden {
    }

    static {
        $assertionsDisabled = !LambdaForm.class.desiredAssertionStatus();
        COMPILE_THRESHOLD = Math.max(-1, MethodHandleStatics.COMPILE_THRESHOLD);
        INTERNED_ARGUMENTS = new Name[BasicType.ARG_TYPE_LIMIT][10];
        for (BasicType basicType : BasicType.ARG_TYPES) {
            int iOrdinal = basicType.ordinal();
            for (int i2 = 0; i2 < INTERNED_ARGUMENTS[iOrdinal].length; i2++) {
                INTERNED_ARGUMENTS[iOrdinal][i2] = new Name(i2, basicType);
            }
        }
        IMPL_NAMES = MemberName.getFactory();
        LF_identityForm = new LambdaForm[BasicType.TYPE_LIMIT];
        LF_zeroForm = new LambdaForm[BasicType.TYPE_LIMIT];
        NF_identity = new NamedFunction[BasicType.TYPE_LIMIT];
        NF_zero = new NamedFunction[BasicType.TYPE_LIMIT];
        if (MethodHandleStatics.debugEnabled()) {
            DEBUG_NAME_COUNTERS = new HashMap<>();
        } else {
            DEBUG_NAME_COUNTERS = null;
        }
        createIdentityForms();
        computeInitialPreparedForms();
        NamedFunction.initializeInvokers();
        TRACE_INTERPRETER = MethodHandleStatics.TRACE_INTERPRETER;
    }

    /* loaded from: rt.jar:java/lang/invoke/LambdaForm$BasicType.class */
    enum BasicType {
        L_TYPE('L', Object.class, Wrapper.OBJECT),
        I_TYPE('I', Integer.TYPE, Wrapper.INT),
        J_TYPE('J', Long.TYPE, Wrapper.LONG),
        F_TYPE('F', Float.TYPE, Wrapper.FLOAT),
        D_TYPE('D', Double.TYPE, Wrapper.DOUBLE),
        V_TYPE('V', Void.TYPE, Wrapper.VOID);

        static final BasicType[] ALL_TYPES;
        static final BasicType[] ARG_TYPES;
        static final int ARG_TYPE_LIMIT;
        static final int TYPE_LIMIT;
        private final char btChar;
        private final Class<?> btClass;
        private final Wrapper btWrapper;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LambdaForm.class.desiredAssertionStatus();
            ALL_TYPES = values();
            ARG_TYPES = (BasicType[]) Arrays.copyOf(ALL_TYPES, ALL_TYPES.length - 1);
            ARG_TYPE_LIMIT = ARG_TYPES.length;
            TYPE_LIMIT = ALL_TYPES.length;
            if (!$assertionsDisabled && !checkBasicType()) {
                throw new AssertionError();
            }
        }

        BasicType(char c2, Class cls, Wrapper wrapper) {
            this.btChar = c2;
            this.btClass = cls;
            this.btWrapper = wrapper;
        }

        char basicTypeChar() {
            return this.btChar;
        }

        Class<?> basicTypeClass() {
            return this.btClass;
        }

        Wrapper basicTypeWrapper() {
            return this.btWrapper;
        }

        int basicTypeSlots() {
            return this.btWrapper.stackSlots();
        }

        static BasicType basicType(byte b2) {
            return ALL_TYPES[b2];
        }

        static BasicType basicType(char c2) {
            switch (c2) {
                case 'B':
                case 'C':
                case 'S':
                case 'Z':
                    return I_TYPE;
                case 'D':
                    return D_TYPE;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    throw MethodHandleStatics.newInternalError("Unknown type char: '" + c2 + PdfOps.SINGLE_QUOTE_TOKEN);
                case 'F':
                    return F_TYPE;
                case 'I':
                    return I_TYPE;
                case 'J':
                    return J_TYPE;
                case 'L':
                    return L_TYPE;
                case 'V':
                    return V_TYPE;
            }
        }

        static BasicType basicType(Wrapper wrapper) {
            return basicType(wrapper.basicTypeChar());
        }

        static BasicType basicType(Class<?> cls) {
            return !cls.isPrimitive() ? L_TYPE : basicType(Wrapper.forPrimitiveType(cls));
        }

        static char basicTypeChar(Class<?> cls) {
            return basicType(cls).btChar;
        }

        static BasicType[] basicTypes(List<Class<?>> list) {
            BasicType[] basicTypeArr = new BasicType[list.size()];
            for (int i2 = 0; i2 < basicTypeArr.length; i2++) {
                basicTypeArr[i2] = basicType(list.get(i2));
            }
            return basicTypeArr;
        }

        static BasicType[] basicTypes(String str) {
            BasicType[] basicTypeArr = new BasicType[str.length()];
            for (int i2 = 0; i2 < basicTypeArr.length; i2++) {
                basicTypeArr[i2] = basicType(str.charAt(i2));
            }
            return basicTypeArr;
        }

        static byte[] basicTypesOrd(BasicType[] basicTypeArr) {
            byte[] bArr = new byte[basicTypeArr.length];
            for (int i2 = 0; i2 < basicTypeArr.length; i2++) {
                bArr[i2] = (byte) basicTypeArr[i2].ordinal();
            }
            return bArr;
        }

        static boolean isBasicTypeChar(char c2) {
            return "LIJFDV".indexOf(c2) >= 0;
        }

        static boolean isArgBasicTypeChar(char c2) {
            return "LIJFD".indexOf(c2) >= 0;
        }

        private static boolean checkBasicType() {
            for (int i2 = 0; i2 < ARG_TYPE_LIMIT; i2++) {
                if (!$assertionsDisabled && ARG_TYPES[i2].ordinal() != i2) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && ARG_TYPES[i2] != ALL_TYPES[i2]) {
                    throw new AssertionError();
                }
            }
            for (int i3 = 0; i3 < TYPE_LIMIT; i3++) {
                if (!$assertionsDisabled && ALL_TYPES[i3].ordinal() != i3) {
                    throw new AssertionError();
                }
            }
            if (!$assertionsDisabled && ALL_TYPES[TYPE_LIMIT - 1] != V_TYPE) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || !Arrays.asList(ARG_TYPES).contains(V_TYPE)) {
                return true;
            }
            throw new AssertionError();
        }
    }

    LambdaForm(String str, int i2, Name[] nameArr, int i3) {
        this(str, i2, nameArr, i3, true, null);
    }

    LambdaForm(String str, int i2, Name[] nameArr, int i3, boolean z2, MethodHandle methodHandle) {
        this.invocationCounter = 0;
        if (!$assertionsDisabled && !namesOK(i2, nameArr)) {
            throw new AssertionError();
        }
        this.arity = i2;
        this.result = fixResult(i3, nameArr);
        this.names = (Name[]) nameArr.clone();
        this.debugName = fixDebugName(str);
        this.forceInline = z2;
        this.customized = methodHandle;
        int iNormalize = normalize();
        if (iNormalize > 253) {
            if (!$assertionsDisabled && iNormalize > 255) {
                throw new AssertionError();
            }
            compileToBytecode();
        }
    }

    LambdaForm(String str, int i2, Name[] nameArr) {
        this(str, i2, nameArr, -2, true, null);
    }

    LambdaForm(String str, int i2, Name[] nameArr, boolean z2) {
        this(str, i2, nameArr, -2, z2, null);
    }

    LambdaForm(String str, Name[] nameArr, Name[] nameArr2, Name name) {
        this(str, nameArr.length, buildNames(nameArr, nameArr2, name), -2, true, null);
    }

    LambdaForm(String str, Name[] nameArr, Name[] nameArr2, Name name, boolean z2) {
        this(str, nameArr.length, buildNames(nameArr, nameArr2, name), -2, z2, null);
    }

    private static Name[] buildNames(Name[] nameArr, Name[] nameArr2, Name name) {
        int length = nameArr.length;
        int length2 = length + nameArr2.length + (name == null ? 0 : 1);
        Name[] nameArr3 = (Name[]) Arrays.copyOf(nameArr, length2);
        System.arraycopy(nameArr2, 0, nameArr3, length, nameArr2.length);
        if (name != null) {
            nameArr3[length2 - 1] = name;
        }
        return nameArr3;
    }

    private LambdaForm(String str) {
        this.invocationCounter = 0;
        if (!$assertionsDisabled && !isValidSignature(str)) {
            throw new AssertionError();
        }
        this.arity = signatureArity(str);
        this.result = signatureReturn(str) == BasicType.V_TYPE ? -1 : this.arity;
        this.names = buildEmptyNames(this.arity, str);
        this.debugName = "LF.zero";
        this.forceInline = true;
        this.customized = null;
        if (!$assertionsDisabled && !nameRefsAreLegal()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !isEmpty()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !str.equals(basicTypeSignature())) {
            throw new AssertionError((Object) (str + " != " + basicTypeSignature()));
        }
    }

    private static Name[] buildEmptyNames(int i2, String str) {
        if (!$assertionsDisabled && !isValidSignature(str)) {
            throw new AssertionError();
        }
        int i3 = i2 + 1;
        if (i2 < 0 || str.length() != i3 + 1) {
            throw new IllegalArgumentException("bad arity for " + str);
        }
        int i4 = BasicType.basicType(str.charAt(i3)) == BasicType.V_TYPE ? 0 : 1;
        Name[] nameArrArguments = arguments(i4, str.substring(0, i2));
        for (int i5 = 0; i5 < i4; i5++) {
            nameArrArguments[i2 + i5] = new Name(constantZero(BasicType.basicType(str.charAt(i3 + i5))), new Object[0]).newIndex(i2 + i5);
        }
        return nameArrArguments;
    }

    private static int fixResult(int i2, Name[] nameArr) {
        if (i2 == -2) {
            i2 = nameArr.length - 1;
        }
        if (i2 >= 0 && nameArr[i2].type == BasicType.V_TYPE) {
            i2 = -1;
        }
        return i2;
    }

    private static String fixDebugName(String str) {
        Integer num;
        if (DEBUG_NAME_COUNTERS != null) {
            int iIndexOf = str.indexOf(95);
            int length = str.length();
            if (iIndexOf < 0) {
                iIndexOf = length;
            }
            String strSubstring = str.substring(0, iIndexOf);
            synchronized (DEBUG_NAME_COUNTERS) {
                num = DEBUG_NAME_COUNTERS.get(strSubstring);
                if (num == null) {
                    num = 0;
                }
                DEBUG_NAME_COUNTERS.put(strSubstring, Integer.valueOf(num.intValue() + 1));
            }
            StringBuilder sb = new StringBuilder(strSubstring);
            sb.append('_');
            int length2 = sb.length();
            sb.append(num.intValue());
            for (int length3 = sb.length() - length2; length3 < 3; length3++) {
                sb.insert(length2, '0');
            }
            if (iIndexOf < length) {
                do {
                    iIndexOf++;
                    if (iIndexOf >= length) {
                        break;
                    }
                } while (Character.isDigit(str.charAt(iIndexOf)));
                if (iIndexOf < length && str.charAt(iIndexOf) == '_') {
                    iIndexOf++;
                }
                if (iIndexOf < length) {
                    sb.append('_').append((CharSequence) str, iIndexOf, length);
                }
            }
            return sb.toString();
        }
        return str;
    }

    private static boolean namesOK(int i2, Name[] nameArr) {
        for (int i3 = 0; i3 < nameArr.length; i3++) {
            Name name = nameArr[i3];
            if (!$assertionsDisabled && name == null) {
                throw new AssertionError((Object) "n is null");
            }
            if (i3 < i2) {
                if (!$assertionsDisabled && !name.isParam()) {
                    throw new AssertionError((Object) (((Object) name) + " is not param at " + i3));
                }
            } else if (!$assertionsDisabled && name.isParam()) {
                throw new AssertionError((Object) (((Object) name) + " is param at " + i3));
            }
        }
        return true;
    }

    LambdaForm customize(MethodHandle methodHandle) {
        LambdaForm lambdaForm = new LambdaForm(this.debugName, this.arity, this.names, this.result, this.forceInline, methodHandle);
        if (COMPILE_THRESHOLD > 0 && this.isCompiled) {
            lambdaForm.compileToBytecode();
        }
        lambdaForm.transformCache = this;
        return lambdaForm;
    }

    LambdaForm uncustomize() {
        if (this.customized == null) {
            return this;
        }
        if (!$assertionsDisabled && this.transformCache == null) {
            throw new AssertionError();
        }
        LambdaForm lambdaForm = (LambdaForm) this.transformCache;
        if (COMPILE_THRESHOLD > 0 && this.isCompiled) {
            lambdaForm.compileToBytecode();
        }
        return lambdaForm;
    }

    private int normalize() {
        Name[] nameArr = null;
        int length = 0;
        int i2 = 0;
        for (int i3 = 0; i3 < this.names.length; i3++) {
            Name name = this.names[i3];
            if (!name.initIndex(i3)) {
                if (nameArr == null) {
                    nameArr = (Name[]) this.names.clone();
                    i2 = i3;
                }
                this.names[i3] = name.cloneWithIndex(i3);
            }
            if (name.arguments != null && length < name.arguments.length) {
                length = name.arguments.length;
            }
        }
        if (nameArr != null) {
            int i4 = this.arity;
            if (i4 <= i2) {
                i4 = i2 + 1;
            }
            for (int i5 = i4; i5 < this.names.length; i5++) {
                this.names[i5] = this.names[i5].replaceNames(nameArr, this.names, i2, i5).newIndex(i5);
            }
        }
        if (!$assertionsDisabled && !nameRefsAreLegal()) {
            throw new AssertionError();
        }
        int iMin = Math.min(this.arity, 10);
        boolean z2 = false;
        for (int i6 = 0; i6 < iMin; i6++) {
            Name name2 = this.names[i6];
            Name nameInternArgument = internArgument(name2);
            if (name2 != nameInternArgument) {
                this.names[i6] = nameInternArgument;
                z2 = true;
            }
        }
        if (z2) {
            for (int i7 = this.arity; i7 < this.names.length; i7++) {
                this.names[i7].internArguments();
            }
        }
        if ($assertionsDisabled || nameRefsAreLegal()) {
            return length;
        }
        throw new AssertionError();
    }

    boolean nameRefsAreLegal() {
        if (!$assertionsDisabled && (this.arity < 0 || this.arity > this.names.length)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (this.result < -1 || this.result >= this.names.length)) {
            throw new AssertionError();
        }
        for (int i2 = 0; i2 < this.arity; i2++) {
            Name name = this.names[i2];
            if (!$assertionsDisabled && name.index() != i2) {
                throw new AssertionError(Arrays.asList(Integer.valueOf(name.index()), Integer.valueOf(i2)));
            }
            if (!$assertionsDisabled && !name.isParam()) {
                throw new AssertionError();
            }
        }
        for (int i3 = this.arity; i3 < this.names.length; i3++) {
            Name name2 = this.names[i3];
            if (!$assertionsDisabled && name2.index() != i3) {
                throw new AssertionError();
            }
            for (Object obj : name2.arguments) {
                if (obj instanceof Name) {
                    Name name3 = (Name) obj;
                    short s2 = name3.index;
                    if (!$assertionsDisabled && (0 > s2 || s2 >= this.names.length)) {
                        throw new AssertionError((Object) (name2.debugString() + ": 0 <= i2 && i2 < names.length: 0 <= " + ((int) s2) + " < " + this.names.length));
                    }
                    if (!$assertionsDisabled && this.names[s2] != name3) {
                        throw new AssertionError(Arrays.asList("-1-", Integer.valueOf(i3), "-2-", name2.debugString(), "-3-", Integer.valueOf(s2), "-4-", name3.debugString(), "-5-", this.names[s2].debugString(), "-6-", this));
                    }
                    if (!$assertionsDisabled && s2 >= i3) {
                        throw new AssertionError();
                    }
                }
            }
        }
        return true;
    }

    BasicType returnType() {
        return this.result < 0 ? BasicType.V_TYPE : this.names[this.result].type;
    }

    BasicType parameterType(int i2) {
        return parameter(i2).type;
    }

    Name parameter(int i2) {
        if (!$assertionsDisabled && i2 >= this.arity) {
            throw new AssertionError();
        }
        Name name = this.names[i2];
        if ($assertionsDisabled || name.isParam()) {
            return name;
        }
        throw new AssertionError();
    }

    Object parameterConstraint(int i2) {
        return parameter(i2).constraint;
    }

    int arity() {
        return this.arity;
    }

    int expressionCount() {
        return this.names.length - this.arity;
    }

    MethodType methodType() {
        return signatureType(basicTypeSignature());
    }

    final String basicTypeSignature() {
        StringBuilder sb = new StringBuilder(arity() + 3);
        int iArity = arity();
        for (int i2 = 0; i2 < iArity; i2++) {
            sb.append(parameterType(i2).basicTypeChar());
        }
        return sb.append('_').append(returnType().basicTypeChar()).toString();
    }

    static int signatureArity(String str) {
        if ($assertionsDisabled || isValidSignature(str)) {
            return str.indexOf(95);
        }
        throw new AssertionError();
    }

    static BasicType signatureReturn(String str) {
        return BasicType.basicType(str.charAt(signatureArity(str) + 1));
    }

    static boolean isValidSignature(String str) {
        int length;
        int iIndexOf = str.indexOf(95);
        if (iIndexOf < 0 || (length = str.length()) != iIndexOf + 2) {
            return false;
        }
        int i2 = 0;
        while (i2 < length) {
            if (i2 != iIndexOf) {
                char cCharAt = str.charAt(i2);
                if (cCharAt == 'V') {
                    return i2 == length - 1 && iIndexOf == length - 2;
                }
                if (!BasicType.isArgBasicTypeChar(cCharAt)) {
                    return false;
                }
            }
            i2++;
        }
        return true;
    }

    static MethodType signatureType(String str) {
        Class[] clsArr = new Class[signatureArity(str)];
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            clsArr[i2] = BasicType.basicType(str.charAt(i2)).btClass;
        }
        return MethodType.methodType((Class<?>) signatureReturn(str).btClass, (Class<?>[]) clsArr);
    }

    public void prepare() {
        if (COMPILE_THRESHOLD == 0 && !this.isCompiled) {
            compileToBytecode();
        }
        if (this.vmentry != null) {
            return;
        }
        this.vmentry = getPreparedForm(basicTypeSignature()).vmentry;
    }

    MemberName compileToBytecode() {
        if (this.vmentry != null && this.isCompiled) {
            return this.vmentry;
        }
        MethodType methodType = methodType();
        if (!$assertionsDisabled && this.vmentry != null && !this.vmentry.getMethodType().basicType().equals((Object) methodType)) {
            throw new AssertionError();
        }
        try {
            this.vmentry = InvokerBytecodeGenerator.generateCustomizedCode(this, methodType);
            if (TRACE_INTERPRETER) {
                traceInterpreter("compileToBytecode", this);
            }
            this.isCompiled = true;
            return this.vmentry;
        } catch (Error | Exception e2) {
            throw MethodHandleStatics.newInternalError(toString(), e2);
        }
    }

    private static void computeInitialPreparedForms() {
        for (MemberName memberName : MemberName.getFactory().getMethods(LambdaForm.class, false, null, null, null)) {
            if (memberName.isStatic() && memberName.isPackage()) {
                MethodType methodType = memberName.getMethodType();
                if (methodType.parameterCount() > 0 && methodType.parameterType(0) == MethodHandle.class && memberName.getName().startsWith("interpret_")) {
                    String strBasicTypeSignature = basicTypeSignature(methodType);
                    if (!$assertionsDisabled && !memberName.getName().equals("interpret" + strBasicTypeSignature.substring(strBasicTypeSignature.indexOf(95)))) {
                        throw new AssertionError();
                    }
                    LambdaForm lambdaForm = new LambdaForm(strBasicTypeSignature);
                    lambdaForm.vmentry = memberName;
                    methodType.form().setCachedLambdaForm(6, lambdaForm);
                }
            }
        }
    }

    static Object interpret_L(MethodHandle methodHandle) throws Throwable {
        Object[] objArr = {methodHandle};
        String str = null;
        if (!$assertionsDisabled) {
            str = "L_L";
            if (!argumentTypesMatch("L_L", objArr)) {
                throw new AssertionError();
            }
        }
        Object objInterpretWithArguments = methodHandle.form.interpretWithArguments(objArr);
        if ($assertionsDisabled || returnTypesMatch(str, objArr, objInterpretWithArguments)) {
            return objInterpretWithArguments;
        }
        throw new AssertionError();
    }

    static Object interpret_L(MethodHandle methodHandle, Object obj) throws Throwable {
        Object[] objArr = {methodHandle, obj};
        String str = null;
        if (!$assertionsDisabled) {
            str = "LL_L";
            if (!argumentTypesMatch("LL_L", objArr)) {
                throw new AssertionError();
            }
        }
        Object objInterpretWithArguments = methodHandle.form.interpretWithArguments(objArr);
        if ($assertionsDisabled || returnTypesMatch(str, objArr, objInterpretWithArguments)) {
            return objInterpretWithArguments;
        }
        throw new AssertionError();
    }

    static Object interpret_L(MethodHandle methodHandle, Object obj, Object obj2) throws Throwable {
        Object[] objArr = {methodHandle, obj, obj2};
        String str = null;
        if (!$assertionsDisabled) {
            str = "LLL_L";
            if (!argumentTypesMatch("LLL_L", objArr)) {
                throw new AssertionError();
            }
        }
        Object objInterpretWithArguments = methodHandle.form.interpretWithArguments(objArr);
        if ($assertionsDisabled || returnTypesMatch(str, objArr, objInterpretWithArguments)) {
            return objInterpretWithArguments;
        }
        throw new AssertionError();
    }

    private static LambdaForm getPreparedForm(String str) {
        MethodType methodTypeSignatureType = signatureType(str);
        LambdaForm lambdaFormCachedLambdaForm = methodTypeSignatureType.form().cachedLambdaForm(6);
        if (lambdaFormCachedLambdaForm != null) {
            return lambdaFormCachedLambdaForm;
        }
        if (!$assertionsDisabled && !isValidSignature(str)) {
            throw new AssertionError();
        }
        LambdaForm lambdaForm = new LambdaForm(str);
        lambdaForm.vmentry = InvokerBytecodeGenerator.generateLambdaFormInterpreterEntryPoint(str);
        return methodTypeSignatureType.form().setCachedLambdaForm(6, lambdaForm);
    }

    private static boolean argumentTypesMatch(String str, Object[] objArr) {
        int iSignatureArity = signatureArity(str);
        if (!$assertionsDisabled && objArr.length != iSignatureArity) {
            throw new AssertionError((Object) ("av.length == arity: av.length=" + objArr.length + ", arity=" + iSignatureArity));
        }
        if (!$assertionsDisabled && !(objArr[0] instanceof MethodHandle)) {
            throw new AssertionError((Object) ("av[0] not instace of MethodHandle: " + objArr[0]));
        }
        MethodType methodTypeType = ((MethodHandle) objArr[0]).type();
        if (!$assertionsDisabled && methodTypeType.parameterCount() != iSignatureArity - 1) {
            throw new AssertionError();
        }
        int i2 = 0;
        while (i2 < objArr.length) {
            Class<?> clsParameterType = i2 == 0 ? MethodHandle.class : methodTypeType.parameterType(i2 - 1);
            if (!$assertionsDisabled && !valueMatches(BasicType.basicType(str.charAt(i2)), clsParameterType, objArr[i2])) {
                throw new AssertionError();
            }
            i2++;
        }
        return true;
    }

    private static boolean valueMatches(BasicType basicType, Class<?> cls, Object obj) {
        if (cls == Void.TYPE) {
            basicType = BasicType.V_TYPE;
        }
        if (!$assertionsDisabled && basicType != BasicType.basicType(cls)) {
            throw new AssertionError((Object) (((Object) basicType) + " == basicType(" + ((Object) cls) + ")=" + ((Object) BasicType.basicType(cls))));
        }
        switch (basicType) {
            case I_TYPE:
                if ($assertionsDisabled || checkInt(cls, obj)) {
                    return true;
                }
                throw new AssertionError((Object) ("checkInt(" + ((Object) cls) + "," + obj + ")"));
            case J_TYPE:
                if ($assertionsDisabled || (obj instanceof Long)) {
                    return true;
                }
                throw new AssertionError((Object) ("instanceof Long: " + obj));
            case F_TYPE:
                if ($assertionsDisabled || (obj instanceof Float)) {
                    return true;
                }
                throw new AssertionError((Object) ("instanceof Float: " + obj));
            case D_TYPE:
                if ($assertionsDisabled || (obj instanceof Double)) {
                    return true;
                }
                throw new AssertionError((Object) ("instanceof Double: " + obj));
            case L_TYPE:
                if ($assertionsDisabled || checkRef(cls, obj)) {
                    return true;
                }
                throw new AssertionError((Object) ("checkRef(" + ((Object) cls) + "," + obj + ")"));
            case V_TYPE:
                return true;
            default:
                if ($assertionsDisabled) {
                    return true;
                }
                throw new AssertionError();
        }
    }

    private static boolean returnTypesMatch(String str, Object[] objArr, Object obj) {
        return valueMatches(signatureReturn(str), ((MethodHandle) objArr[0]).type().returnType(), obj);
    }

    private static boolean checkInt(Class<?> cls, Object obj) {
        if (!$assertionsDisabled && !(obj instanceof Integer)) {
            throw new AssertionError();
        }
        if (cls == Integer.TYPE) {
            return true;
        }
        Wrapper wrapperForBasicType = Wrapper.forBasicType(cls);
        if ($assertionsDisabled || wrapperForBasicType.isSubwordOrInt()) {
            return obj.equals(Wrapper.INT.wrap(wrapperForBasicType.wrap(obj)));
        }
        throw new AssertionError();
    }

    private static boolean checkRef(Class<?> cls, Object obj) {
        if (!$assertionsDisabled && cls.isPrimitive()) {
            throw new AssertionError();
        }
        if (obj == null || cls.isInterface()) {
            return true;
        }
        return cls.isInstance(obj);
    }

    @Hidden
    @DontInline
    Object interpretWithArguments(Object... objArr) throws Throwable {
        if (TRACE_INTERPRETER) {
            return interpretWithArgumentsTracing(objArr);
        }
        checkInvocationCounter();
        if (!$assertionsDisabled && !arityCheck(objArr)) {
            throw new AssertionError();
        }
        Object[] objArrCopyOf = Arrays.copyOf(objArr, this.names.length);
        for (int length = objArr.length; length < objArrCopyOf.length; length++) {
            objArrCopyOf[length] = interpretName(this.names[length], objArrCopyOf);
        }
        Object obj = this.result < 0 ? null : objArrCopyOf[this.result];
        if ($assertionsDisabled || resultCheck(objArr, obj)) {
            return obj;
        }
        throw new AssertionError();
    }

    @Hidden
    @DontInline
    Object interpretName(Name name, Object[] objArr) throws Throwable {
        if (TRACE_INTERPRETER) {
            traceInterpreter("| interpretName", name.debugString(), (Object[]) null);
        }
        Object[] objArrCopyOf = Arrays.copyOf(name.arguments, name.arguments.length, Object[].class);
        for (int i2 = 0; i2 < objArrCopyOf.length; i2++) {
            Object obj = objArrCopyOf[i2];
            if (obj instanceof Name) {
                int iIndex = ((Name) obj).index();
                if (!$assertionsDisabled && this.names[iIndex] != obj) {
                    throw new AssertionError();
                }
                objArrCopyOf[i2] = objArr[iIndex];
            }
        }
        return name.function.invokeWithArguments(objArrCopyOf);
    }

    private void checkInvocationCounter() {
        if (COMPILE_THRESHOLD != 0 && this.invocationCounter < COMPILE_THRESHOLD) {
            this.invocationCounter++;
            if (this.invocationCounter >= COMPILE_THRESHOLD) {
                compileToBytecode();
            }
        }
    }

    Object interpretWithArgumentsTracing(Object... objArr) throws Throwable {
        traceInterpreter("[ interpretWithArguments", this, objArr);
        if (this.invocationCounter < COMPILE_THRESHOLD) {
            int i2 = this.invocationCounter;
            this.invocationCounter = i2 + 1;
            traceInterpreter("| invocationCounter", Integer.valueOf(i2));
            if (this.invocationCounter >= COMPILE_THRESHOLD) {
                compileToBytecode();
            }
        }
        try {
            if (!$assertionsDisabled && !arityCheck(objArr)) {
                throw new AssertionError();
            }
            Object[] objArrCopyOf = Arrays.copyOf(objArr, this.names.length);
            for (int length = objArr.length; length < objArrCopyOf.length; length++) {
                objArrCopyOf[length] = interpretName(this.names[length], objArrCopyOf);
            }
            Object obj = this.result < 0 ? null : objArrCopyOf[this.result];
            traceInterpreter("] return =>", obj);
            return obj;
        } catch (Throwable th) {
            traceInterpreter("] throw =>", th);
            throw th;
        }
    }

    static void traceInterpreter(String str, Object obj, Object... objArr) {
        if (TRACE_INTERPRETER) {
            System.out.println("LFI: " + str + " " + (obj != null ? obj : "") + ((objArr == null || objArr.length == 0) ? "" : Arrays.asList(objArr)));
        }
    }

    static void traceInterpreter(String str, Object obj) {
        traceInterpreter(str, obj, (Object[]) null);
    }

    private boolean arityCheck(Object[] objArr) {
        if (!$assertionsDisabled && objArr.length != this.arity) {
            throw new AssertionError((Object) (this.arity + "!=" + ((Object) Arrays.asList(objArr)) + ".length"));
        }
        if (!$assertionsDisabled && !(objArr[0] instanceof MethodHandle)) {
            throw new AssertionError((Object) ("not MH: " + objArr[0]));
        }
        MethodHandle methodHandle = (MethodHandle) objArr[0];
        if (!$assertionsDisabled && methodHandle.internalForm() != this) {
            throw new AssertionError();
        }
        argumentTypesMatch(basicTypeSignature(), objArr);
        return true;
    }

    private boolean resultCheck(Object[] objArr, Object obj) {
        MethodType methodTypeType = ((MethodHandle) objArr[0]).type();
        if ($assertionsDisabled || valueMatches(returnType(), methodTypeType.returnType(), obj)) {
            return true;
        }
        throw new AssertionError();
    }

    private boolean isEmpty() {
        if (this.result < 0) {
            return this.names.length == this.arity;
        }
        if (this.result == this.arity && this.names.length == this.arity + 1) {
            return this.names[this.arity].isConstantZero();
        }
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(this.debugName + "=Lambda(");
        for (int i2 = 0; i2 < this.names.length; i2++) {
            if (i2 == this.arity) {
                sb.append(")=>{");
            }
            Name name = this.names[i2];
            if (i2 >= this.arity) {
                sb.append("\n    ");
            }
            sb.append(name.paramString());
            if (i2 < this.arity) {
                if (i2 + 1 < this.arity) {
                    sb.append(",");
                }
            } else {
                sb.append("=").append(name.exprString());
                sb.append(";");
            }
        }
        if (this.arity == this.names.length) {
            sb.append(")=>{");
        }
        sb.append(this.result < 0 ? "void" : this.names[this.result]).append("}");
        if (TRACE_INTERPRETER) {
            sb.append(CallSiteDescriptor.TOKEN_DELIMITER).append(basicTypeSignature());
            sb.append("/").append((Object) this.vmentry);
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        return (obj instanceof LambdaForm) && equals((LambdaForm) obj);
    }

    public boolean equals(LambdaForm lambdaForm) {
        if (this.result != lambdaForm.result) {
            return false;
        }
        return Arrays.equals(this.names, lambdaForm.names);
    }

    public int hashCode() {
        return this.result + (31 * Arrays.hashCode(this.names));
    }

    LambdaFormEditor editor() {
        return LambdaFormEditor.lambdaFormEditor(this);
    }

    boolean contains(Name name) {
        int iIndex = name.index();
        if (iIndex >= 0) {
            return iIndex < this.names.length && name.equals(this.names[iIndex]);
        }
        for (int i2 = this.arity; i2 < this.names.length; i2++) {
            if (name.equals(this.names[i2])) {
                return true;
            }
        }
        return false;
    }

    LambdaForm addArguments(int i2, BasicType... basicTypeArr) {
        int i3 = i2 + 1;
        if (!$assertionsDisabled && i3 > this.arity) {
            throw new AssertionError();
        }
        int length = this.names.length;
        int length2 = basicTypeArr.length;
        Name[] nameArr = (Name[]) Arrays.copyOf(this.names, length + length2);
        int i4 = this.arity + length2;
        int i5 = this.result;
        if (i5 >= i3) {
            i5 += length2;
        }
        System.arraycopy(this.names, i3, nameArr, i3 + length2, length - i3);
        for (int i6 = 0; i6 < length2; i6++) {
            nameArr[i3 + i6] = new Name(basicTypeArr[i6]);
        }
        return new LambdaForm(this.debugName, i4, nameArr, i5);
    }

    LambdaForm addArguments(int i2, List<Class<?>> list) {
        return addArguments(i2, BasicType.basicTypes(list));
    }

    LambdaForm permuteArguments(int i2, int[] iArr, BasicType[] basicTypeArr) {
        int length = this.names.length;
        int length2 = basicTypeArr.length;
        int length3 = iArr.length;
        if (!$assertionsDisabled && i2 + length3 != this.arity) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !permutedTypesMatch(iArr, basicTypeArr, this.names, i2)) {
            throw new AssertionError();
        }
        int i3 = 0;
        while (i3 < length3 && iArr[i3] == i3) {
            i3++;
        }
        Name[] nameArr = new Name[(length - length3) + length2];
        System.arraycopy(this.names, 0, nameArr, 0, i2 + i3);
        int i4 = length - this.arity;
        System.arraycopy(this.names, i2 + length3, nameArr, i2 + length2, i4);
        int length4 = nameArr.length - i4;
        int i5 = this.result;
        if (i5 >= 0) {
            if (i5 < i2 + length3) {
                i5 = iArr[i5 - i2];
            } else {
                i5 = (i5 - length3) + length2;
            }
        }
        for (int i6 = i3; i6 < length3; i6++) {
            Name name = this.names[i2 + i6];
            int i7 = iArr[i6];
            Name name2 = nameArr[i2 + i7];
            if (name2 == null) {
                Name name3 = new Name(basicTypeArr[i7]);
                name2 = name3;
                nameArr[i2 + i7] = name3;
            } else if (!$assertionsDisabled && name2.type != basicTypeArr[i7]) {
                throw new AssertionError();
            }
            for (int i8 = length4; i8 < nameArr.length; i8++) {
                nameArr[i8] = nameArr[i8].replaceName(name, name2);
            }
        }
        for (int i9 = i2 + i3; i9 < length4; i9++) {
            if (nameArr[i9] == null) {
                nameArr[i9] = argument(i9, basicTypeArr[i9 - i2]);
            }
        }
        for (int i10 = this.arity; i10 < this.names.length; i10++) {
            int i11 = (i10 - this.arity) + length4;
            Name name4 = this.names[i10];
            Name name5 = nameArr[i11];
            if (name4 != name5) {
                for (int i12 = i11 + 1; i12 < nameArr.length; i12++) {
                    nameArr[i12] = nameArr[i12].replaceName(name4, name5);
                }
            }
        }
        return new LambdaForm(this.debugName, length4, nameArr, i5);
    }

    static boolean permutedTypesMatch(int[] iArr, BasicType[] basicTypeArr, Name[] nameArr, int i2) {
        int length = basicTypeArr.length;
        int length2 = iArr.length;
        for (int i3 = 0; i3 < length2; i3++) {
            if (!$assertionsDisabled && !nameArr[i2 + i3].isParam()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && nameArr[i2 + i3].type != basicTypeArr[iArr[i3]]) {
                throw new AssertionError();
            }
        }
        return true;
    }

    /* loaded from: rt.jar:java/lang/invoke/LambdaForm$NamedFunction.class */
    static class NamedFunction {
        final MemberName member;

        @Stable
        MethodHandle resolvedHandle;

        @Stable
        MethodHandle invoker;
        static final MethodType INVOKER_METHOD_TYPE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LambdaForm.class.desiredAssertionStatus();
            INVOKER_METHOD_TYPE = MethodType.methodType(Object.class, MethodHandle.class, Object[].class);
        }

        NamedFunction(MethodHandle methodHandle) {
            this(methodHandle.internalMemberName(), methodHandle);
        }

        NamedFunction(MemberName memberName, MethodHandle methodHandle) {
            this.member = memberName;
            this.resolvedHandle = methodHandle;
        }

        NamedFunction(MethodType methodType) {
            if (!$assertionsDisabled && methodType != methodType.basicType()) {
                throw new AssertionError(methodType);
            }
            if (methodType.parameterSlotCount() < 253) {
                this.resolvedHandle = methodType.invokers().basicInvoker();
                this.member = this.resolvedHandle.internalMemberName();
            } else {
                this.member = Invokers.invokeBasicMethod(methodType);
            }
            if (!$assertionsDisabled && !isInvokeBasic(this.member)) {
                throw new AssertionError();
            }
        }

        private static boolean isInvokeBasic(MemberName memberName) {
            return memberName != null && memberName.getDeclaringClass() == MethodHandle.class && "invokeBasic".equals(memberName.getName());
        }

        NamedFunction(Method method) {
            this(new MemberName(method));
        }

        NamedFunction(Field field) {
            this(new MemberName(field));
        }

        NamedFunction(MemberName memberName) {
            this.member = memberName;
            this.resolvedHandle = null;
        }

        MethodHandle resolvedHandle() {
            if (this.resolvedHandle == null) {
                resolve();
            }
            return this.resolvedHandle;
        }

        void resolve() {
            this.resolvedHandle = DirectMethodHandle.make(this.member);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && (obj instanceof NamedFunction)) {
                return this.member != null && this.member.equals(((NamedFunction) obj).member);
            }
            return false;
        }

        public int hashCode() {
            if (this.member != null) {
                return this.member.hashCode();
            }
            return super.hashCode();
        }

        static void initializeInvokers() {
            for (MemberName memberName : MemberName.getFactory().getMethods(NamedFunction.class, false, null, null, null)) {
                if (memberName.isStatic() && memberName.isPackage() && memberName.getMethodType().equals((Object) INVOKER_METHOD_TYPE) && memberName.getName().startsWith("invoke_")) {
                    String strSubstring = memberName.getName().substring("invoke_".length());
                    MethodType methodTypeGenericMethodType = MethodType.genericMethodType(LambdaForm.signatureArity(strSubstring));
                    if (LambdaForm.signatureReturn(strSubstring) == BasicType.V_TYPE) {
                        methodTypeGenericMethodType = methodTypeGenericMethodType.changeReturnType(Void.TYPE);
                    }
                    methodTypeGenericMethodType.form().setCachedMethodHandle(1, DirectMethodHandle.make(memberName));
                }
            }
        }

        @Hidden
        static Object invoke__V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if (!$assertionsDisabled && !arityCheck(0, Void.TYPE, methodHandle, objArr)) {
                throw new AssertionError();
            }
            methodHandle.invokeBasic();
            return null;
        }

        @Hidden
        static Object invoke_L_V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if (!$assertionsDisabled && !arityCheck(1, Void.TYPE, methodHandle, objArr)) {
                throw new AssertionError();
            }
            methodHandle.invokeBasic(objArr[0]);
            return null;
        }

        @Hidden
        static Object invoke_LL_V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if (!$assertionsDisabled && !arityCheck(2, Void.TYPE, methodHandle, objArr)) {
                throw new AssertionError();
            }
            methodHandle.invokeBasic(objArr[0], objArr[1]);
            return null;
        }

        @Hidden
        static Object invoke_LLL_V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if (!$assertionsDisabled && !arityCheck(3, Void.TYPE, methodHandle, objArr)) {
                throw new AssertionError();
            }
            methodHandle.invokeBasic(objArr[0], objArr[1], objArr[2]);
            return null;
        }

        @Hidden
        static Object invoke_LLLL_V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if (!$assertionsDisabled && !arityCheck(4, Void.TYPE, methodHandle, objArr)) {
                throw new AssertionError();
            }
            methodHandle.invokeBasic(objArr[0], objArr[1], objArr[2], objArr[3]);
            return null;
        }

        @Hidden
        static Object invoke_LLLLL_V(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if (!$assertionsDisabled && !arityCheck(5, Void.TYPE, methodHandle, objArr)) {
                throw new AssertionError();
            }
            methodHandle.invokeBasic(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
            return null;
        }

        @Hidden
        static Object invoke__L(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if ($assertionsDisabled || arityCheck(0, methodHandle, objArr)) {
                return methodHandle.invokeBasic();
            }
            throw new AssertionError();
        }

        @Hidden
        static Object invoke_L_L(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if ($assertionsDisabled || arityCheck(1, methodHandle, objArr)) {
                return methodHandle.invokeBasic(objArr[0]);
            }
            throw new AssertionError();
        }

        @Hidden
        static Object invoke_LL_L(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if ($assertionsDisabled || arityCheck(2, methodHandle, objArr)) {
                return methodHandle.invokeBasic(objArr[0], objArr[1]);
            }
            throw new AssertionError();
        }

        @Hidden
        static Object invoke_LLL_L(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if ($assertionsDisabled || arityCheck(3, methodHandle, objArr)) {
                return methodHandle.invokeBasic(objArr[0], objArr[1], objArr[2]);
            }
            throw new AssertionError();
        }

        @Hidden
        static Object invoke_LLLL_L(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if ($assertionsDisabled || arityCheck(4, methodHandle, objArr)) {
                return methodHandle.invokeBasic(objArr[0], objArr[1], objArr[2], objArr[3]);
            }
            throw new AssertionError();
        }

        @Hidden
        static Object invoke_LLLLL_L(MethodHandle methodHandle, Object[] objArr) throws Throwable {
            if ($assertionsDisabled || arityCheck(5, methodHandle, objArr)) {
                return methodHandle.invokeBasic(objArr[0], objArr[1], objArr[2], objArr[3], objArr[4]);
            }
            throw new AssertionError();
        }

        private static boolean arityCheck(int i2, MethodHandle methodHandle, Object[] objArr) {
            return arityCheck(i2, Object.class, methodHandle, objArr);
        }

        private static boolean arityCheck(int i2, Class<?> cls, MethodHandle methodHandle, Object[] objArr) {
            if (!$assertionsDisabled && objArr.length != i2) {
                throw new AssertionError(Arrays.asList(Integer.valueOf(objArr.length), Integer.valueOf(i2)));
            }
            if (!$assertionsDisabled && methodHandle.type().basicType() != MethodType.genericMethodType(i2).changeReturnType(cls)) {
                throw new AssertionError(Arrays.asList(methodHandle, cls, Integer.valueOf(i2)));
            }
            MemberName memberNameInternalMemberName = methodHandle.internalMemberName();
            if (isInvokeBasic(memberNameInternalMemberName)) {
                if (!$assertionsDisabled && i2 <= 0) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && !(objArr[0] instanceof MethodHandle)) {
                    throw new AssertionError();
                }
                MethodHandle methodHandle2 = (MethodHandle) objArr[0];
                if ($assertionsDisabled || methodHandle2.type().basicType() == MethodType.genericMethodType(i2 - 1).changeReturnType(cls)) {
                    return true;
                }
                throw new AssertionError(Arrays.asList(memberNameInternalMemberName, methodHandle2, cls, Integer.valueOf(i2)));
            }
            return true;
        }

        private static MethodHandle computeInvoker(MethodTypeForm methodTypeForm) {
            MethodTypeForm methodTypeFormForm = methodTypeForm.basicType().form();
            MethodHandle methodHandleCachedMethodHandle = methodTypeFormForm.cachedMethodHandle(1);
            if (methodHandleCachedMethodHandle != null) {
                return methodHandleCachedMethodHandle;
            }
            DirectMethodHandle directMethodHandleMake = DirectMethodHandle.make(InvokerBytecodeGenerator.generateNamedFunctionInvoker(methodTypeFormForm));
            MethodHandle methodHandleCachedMethodHandle2 = methodTypeFormForm.cachedMethodHandle(1);
            if (methodHandleCachedMethodHandle2 != null) {
                return methodHandleCachedMethodHandle2;
            }
            if (!directMethodHandleMake.type().equals((Object) INVOKER_METHOD_TYPE)) {
                throw MethodHandleStatics.newInternalError(directMethodHandleMake.debugString());
            }
            return methodTypeFormForm.setCachedMethodHandle(1, directMethodHandleMake);
        }

        @Hidden
        Object invokeWithArguments(Object... objArr) throws Throwable {
            if (LambdaForm.TRACE_INTERPRETER) {
                return invokeWithArgumentsTracing(objArr);
            }
            if ($assertionsDisabled || checkArgumentTypes(objArr, methodType())) {
                return invoker().invokeBasic(resolvedHandle(), objArr);
            }
            throw new AssertionError();
        }

        @Hidden
        Object invokeWithArgumentsTracing(Object[] objArr) throws Throwable {
            try {
                LambdaForm.traceInterpreter("[ call", this, objArr);
                if (this.invoker == null) {
                    LambdaForm.traceInterpreter("| getInvoker", this);
                    invoker();
                }
                if (this.resolvedHandle == null) {
                    LambdaForm.traceInterpreter("| resolve", this);
                    resolvedHandle();
                }
                if (!$assertionsDisabled && !checkArgumentTypes(objArr, methodType())) {
                    throw new AssertionError();
                }
                Object objInvokeBasic = invoker().invokeBasic(resolvedHandle(), objArr);
                LambdaForm.traceInterpreter("] return =>", objInvokeBasic);
                return objInvokeBasic;
            } catch (Throwable th) {
                LambdaForm.traceInterpreter("] throw =>", th);
                throw th;
            }
        }

        private MethodHandle invoker() {
            if (this.invoker != null) {
                return this.invoker;
            }
            MethodHandle methodHandleComputeInvoker = computeInvoker(methodType().form());
            this.invoker = methodHandleComputeInvoker;
            return methodHandleComputeInvoker;
        }

        private static boolean checkArgumentTypes(Object[] objArr, MethodType methodType) {
            return true;
        }

        MethodType methodType() {
            if (this.resolvedHandle != null) {
                return this.resolvedHandle.type();
            }
            return this.member.getInvocationType();
        }

        MemberName member() {
            if ($assertionsDisabled || assertMemberIsConsistent()) {
                return this.member;
            }
            throw new AssertionError();
        }

        private boolean assertMemberIsConsistent() {
            if (this.resolvedHandle instanceof DirectMethodHandle) {
                MemberName memberNameInternalMemberName = this.resolvedHandle.internalMemberName();
                if ($assertionsDisabled || memberNameInternalMemberName.equals(this.member)) {
                    return true;
                }
                throw new AssertionError();
            }
            return true;
        }

        Class<?> memberDeclaringClassOrNull() {
            if (this.member == null) {
                return null;
            }
            return this.member.getDeclaringClass();
        }

        BasicType returnType() {
            return BasicType.basicType(methodType().returnType());
        }

        BasicType parameterType(int i2) {
            return BasicType.basicType(methodType().parameterType(i2));
        }

        int arity() {
            return methodType().parameterCount();
        }

        public String toString() {
            return this.member == null ? String.valueOf(this.resolvedHandle) : this.member.getDeclaringClass().getSimpleName() + "." + this.member.getName();
        }

        public boolean isIdentity() {
            return equals(LambdaForm.identity(returnType()));
        }

        public boolean isConstantZero() {
            return equals(LambdaForm.constantZero(returnType()));
        }

        public MethodHandleImpl.Intrinsic intrinsicName() {
            return this.resolvedHandle == null ? MethodHandleImpl.Intrinsic.NONE : this.resolvedHandle.intrinsicName();
        }
    }

    public static String basicTypeSignature(MethodType methodType) {
        char[] cArr = new char[methodType.parameterCount() + 2];
        int i2 = 0;
        Iterator<Class<?>> it = methodType.parameterList().iterator();
        while (it.hasNext()) {
            int i3 = i2;
            i2++;
            cArr[i3] = BasicType.basicTypeChar(it.next());
        }
        int i4 = i2;
        int i5 = i2 + 1;
        cArr[i4] = '_';
        int i6 = i5 + 1;
        cArr[i5] = BasicType.basicTypeChar(methodType.returnType());
        if ($assertionsDisabled || i6 == cArr.length) {
            return String.valueOf(cArr);
        }
        throw new AssertionError();
    }

    public static String shortenSignature(String str) {
        char cCharAt = 65535;
        int i2 = 0;
        StringBuilder sbAppend = null;
        int length = str.length();
        if (length < 3) {
            return str;
        }
        int i3 = 0;
        while (i3 <= length) {
            char c2 = cCharAt;
            cCharAt = i3 == length ? (char) 65535 : str.charAt(i3);
            if (cCharAt == c2) {
                i2++;
            } else {
                int i4 = i2;
                i2 = 1;
                if (i4 < 3) {
                    if (sbAppend != null) {
                        while (true) {
                            i4--;
                            if (i4 >= 0) {
                                sbAppend.append(c2);
                            }
                        }
                    }
                } else {
                    if (sbAppend == null) {
                        sbAppend = new StringBuilder().append((CharSequence) str, 0, i3 - i4);
                    }
                    sbAppend.append(c2).append(i4);
                }
            }
            i3++;
        }
        return sbAppend == null ? str : sbAppend.toString();
    }

    /* loaded from: rt.jar:java/lang/invoke/LambdaForm$Name.class */
    static final class Name {
        final BasicType type;
        private short index;
        final NamedFunction function;
        final Object constraint;

        @Stable
        final Object[] arguments;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !LambdaForm.class.desiredAssertionStatus();
        }

        private Name(int i2, BasicType basicType, NamedFunction namedFunction, Object[] objArr) {
            this.index = (short) i2;
            this.type = basicType;
            this.function = namedFunction;
            this.arguments = objArr;
            this.constraint = null;
            if (!$assertionsDisabled && this.index != i2) {
                throw new AssertionError();
            }
        }

        private Name(Name name, Object obj) {
            this.index = name.index;
            this.type = name.type;
            this.function = name.function;
            this.arguments = name.arguments;
            this.constraint = obj;
            if (!$assertionsDisabled && obj != null && !isParam()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && obj != null && !(obj instanceof BoundMethodHandle.SpeciesData) && !(obj instanceof Class)) {
                throw new AssertionError();
            }
        }

        Name(MethodHandle methodHandle, Object... objArr) {
            this(new NamedFunction(methodHandle), objArr);
        }

        Name(MethodType methodType, Object... objArr) {
            this(new NamedFunction(methodType), objArr);
            if ($assertionsDisabled) {
                return;
            }
            if (!(objArr[0] instanceof Name) || ((Name) objArr[0]).type != BasicType.L_TYPE) {
                throw new AssertionError();
            }
        }

        Name(MemberName memberName, Object... objArr) {
            this(new NamedFunction(memberName), objArr);
        }

        /* JADX WARN: Illegal instructions before constructor call */
        Name(NamedFunction namedFunction, Object... objArr) {
            BasicType basicTypeReturnType = namedFunction.returnType();
            Object[] objArrCopyOf = Arrays.copyOf(objArr, objArr.length, Object[].class);
            this(-1, basicTypeReturnType, namedFunction, objArrCopyOf);
            if (!$assertionsDisabled && objArrCopyOf.length != namedFunction.arity()) {
                throw new AssertionError((Object) ("arity mismatch: arguments.length=" + objArrCopyOf.length + " == function.arity()=" + namedFunction.arity() + " in " + debugString()));
            }
            for (int i2 = 0; i2 < objArrCopyOf.length; i2++) {
                if (!$assertionsDisabled && !typesMatch(namedFunction.parameterType(i2), objArrCopyOf[i2])) {
                    throw new AssertionError((Object) ("types don't match: function.parameterType(" + i2 + ")=" + ((Object) namedFunction.parameterType(i2)) + ", arguments[" + i2 + "]=" + objArrCopyOf[i2] + " in " + debugString()));
                }
            }
        }

        Name(int i2, BasicType basicType) {
            this(i2, basicType, null, null);
        }

        Name(BasicType basicType) {
            this(-1, basicType);
        }

        BasicType type() {
            return this.type;
        }

        int index() {
            return this.index;
        }

        boolean initIndex(int i2) {
            if (this.index != i2) {
                if (this.index != -1) {
                    return false;
                }
                this.index = (short) i2;
                return true;
            }
            return true;
        }

        char typeChar() {
            return this.type.btChar;
        }

        void resolve() {
            if (this.function != null) {
                this.function.resolve();
            }
        }

        Name newIndex(int i2) {
            return initIndex(i2) ? this : cloneWithIndex(i2);
        }

        Name cloneWithIndex(int i2) {
            return new Name(i2, this.type, this.function, this.arguments == null ? null : (Object[]) this.arguments.clone()).withConstraint(this.constraint);
        }

        Name withConstraint(Object obj) {
            return obj == this.constraint ? this : new Name(this, obj);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v17, types: [java.lang.Object[]] */
        /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object[]] */
        Name replaceName(Name name, Name name2) {
            if (name == name2) {
                return this;
            }
            Name[] nameArr = this.arguments;
            if (nameArr == null) {
                return this;
            }
            boolean z2 = false;
            for (int i2 = 0; i2 < nameArr.length; i2++) {
                if (nameArr[i2] == name) {
                    if (!z2) {
                        z2 = true;
                        nameArr = (Object[]) nameArr.clone();
                    }
                    nameArr[i2] = name2;
                }
            }
            return !z2 ? this : new Name(this.function, nameArr);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r0v2, types: [java.lang.Object[]] */
        /* JADX WARN: Type inference failed for: r0v27, types: [java.lang.Object[]] */
        Name replaceNames(Name[] nameArr, Name[] nameArr2, int i2, int i3) {
            Name name;
            short s2;
            if (i2 >= i3) {
                return this;
            }
            Name[] nameArr3 = this.arguments;
            boolean z2 = false;
            for (int i4 = 0; i4 < nameArr3.length; i4++) {
                if ((nameArr3[i4] instanceof Name) && ((s2 = (name = nameArr3[i4]).index) < 0 || s2 >= nameArr2.length || name != nameArr2[s2])) {
                    int i5 = i2;
                    while (true) {
                        if (i5 >= i3) {
                            break;
                        }
                        if (name != nameArr[i5]) {
                            i5++;
                        } else if (name != nameArr2[i5]) {
                            if (!z2) {
                                z2 = true;
                                nameArr3 = (Object[]) nameArr3.clone();
                            }
                            nameArr3[i4] = nameArr2[i5];
                        }
                    }
                }
            }
            return !z2 ? this : new Name(this.function, nameArr3);
        }

        void internArguments() {
            Object[] objArr = this.arguments;
            for (int i2 = 0; i2 < objArr.length; i2++) {
                if (objArr[i2] instanceof Name) {
                    Name name = (Name) objArr[i2];
                    if (name.isParam() && name.index < 10) {
                        objArr[i2] = LambdaForm.internArgument(name);
                    }
                }
            }
        }

        boolean isParam() {
            return this.function == null;
        }

        boolean isConstantZero() {
            return !isParam() && this.arguments.length == 0 && this.function.isConstantZero();
        }

        public String toString() {
            return (isParam() ? "a" : "t") + (this.index >= 0 ? this.index : System.identityHashCode(this)) + CallSiteDescriptor.TOKEN_DELIMITER + typeChar();
        }

        public String debugString() throws SecurityException {
            String strParamString = paramString();
            return this.function == null ? strParamString : strParamString + "=" + exprString();
        }

        public String paramString() throws SecurityException {
            String string = toString();
            Object simpleName = this.constraint;
            if (simpleName == null) {
                return string;
            }
            if (simpleName instanceof Class) {
                simpleName = ((Class) simpleName).getSimpleName();
            }
            return string + "/" + simpleName;
        }

        public String exprString() {
            if (this.function == null) {
                return toString();
            }
            StringBuilder sb = new StringBuilder(this.function.toString());
            sb.append("(");
            String str = "";
            for (Object obj : this.arguments) {
                sb.append(str);
                str = ",";
                if ((obj instanceof Name) || (obj instanceof Integer)) {
                    sb.append(obj);
                } else {
                    sb.append("(").append(obj).append(")");
                }
            }
            sb.append(")");
            return sb.toString();
        }

        static boolean typesMatch(BasicType basicType, Object obj) {
            if (obj instanceof Name) {
                return ((Name) obj).type == basicType;
            }
            switch (basicType) {
                case I_TYPE:
                    return obj instanceof Integer;
                case J_TYPE:
                    return obj instanceof Long;
                case F_TYPE:
                    return obj instanceof Float;
                case D_TYPE:
                    return obj instanceof Double;
                default:
                    if ($assertionsDisabled || basicType == BasicType.L_TYPE) {
                        return true;
                    }
                    throw new AssertionError();
            }
        }

        int lastUseIndex(Name name) {
            if (this.arguments == null) {
                return -1;
            }
            int length = this.arguments.length;
            do {
                length--;
                if (length < 0) {
                    return -1;
                }
            } while (this.arguments[length] != name);
            return length;
        }

        int useCount(Name name) {
            if (this.arguments == null) {
                return 0;
            }
            int i2 = 0;
            int length = this.arguments.length;
            while (true) {
                length--;
                if (length >= 0) {
                    if (this.arguments[length] == name) {
                        i2++;
                    }
                } else {
                    return i2;
                }
            }
        }

        boolean contains(Name name) {
            return this == name || lastUseIndex(name) >= 0;
        }

        public boolean equals(Name name) {
            if (this == name) {
                return true;
            }
            return !isParam() && this.type == name.type && this.function.equals(name.function) && Arrays.equals(this.arguments, name.arguments);
        }

        public boolean equals(Object obj) {
            return (obj instanceof Name) && equals((Name) obj);
        }

        public int hashCode() {
            if (isParam()) {
                return this.index | (this.type.ordinal() << 8);
            }
            return this.function.hashCode() ^ Arrays.hashCode(this.arguments);
        }
    }

    int lastUseIndex(Name name) {
        short s2 = name.index;
        int length = this.names.length;
        if (!$assertionsDisabled && this.names[s2] != name) {
            throw new AssertionError();
        }
        if (this.result == s2) {
            return length;
        }
        int i2 = length;
        do {
            i2--;
            if (i2 <= s2) {
                return -1;
            }
        } while (this.names[i2].lastUseIndex(name) < 0);
        return i2;
    }

    int useCount(Name name) {
        short s2 = name.index;
        int length = this.names.length;
        int iLastUseIndex = lastUseIndex(name);
        if (iLastUseIndex < 0) {
            return 0;
        }
        int iUseCount = 0;
        if (iLastUseIndex == length) {
            iUseCount = 0 + 1;
            iLastUseIndex--;
        }
        int iIndex = name.index() + 1;
        if (iIndex < this.arity) {
            iIndex = this.arity;
        }
        for (int i2 = iIndex; i2 <= iLastUseIndex; i2++) {
            iUseCount += this.names[i2].useCount(name);
        }
        return iUseCount;
    }

    static Name argument(int i2, char c2) {
        return argument(i2, BasicType.basicType(c2));
    }

    static Name argument(int i2, BasicType basicType) {
        if (i2 >= 10) {
            return new Name(i2, basicType);
        }
        return INTERNED_ARGUMENTS[basicType.ordinal()][i2];
    }

    static Name internArgument(Name name) {
        if (!$assertionsDisabled && !name.isParam()) {
            throw new AssertionError((Object) ("not param: " + ((Object) name)));
        }
        if ($assertionsDisabled || name.index < 10) {
            return name.constraint != null ? name : argument(name.index, name.type);
        }
        throw new AssertionError();
    }

    static Name[] arguments(int i2, String str) {
        int length = str.length();
        Name[] nameArr = new Name[length + i2];
        for (int i3 = 0; i3 < length; i3++) {
            nameArr[i3] = argument(i3, str.charAt(i3));
        }
        return nameArr;
    }

    static Name[] arguments(int i2, char... cArr) {
        int length = cArr.length;
        Name[] nameArr = new Name[length + i2];
        for (int i3 = 0; i3 < length; i3++) {
            nameArr[i3] = argument(i3, cArr[i3]);
        }
        return nameArr;
    }

    static Name[] arguments(int i2, List<Class<?>> list) {
        int size = list.size();
        Name[] nameArr = new Name[size + i2];
        for (int i3 = 0; i3 < size; i3++) {
            nameArr[i3] = argument(i3, BasicType.basicType(list.get(i3)));
        }
        return nameArr;
    }

    static Name[] arguments(int i2, Class<?>... clsArr) {
        int length = clsArr.length;
        Name[] nameArr = new Name[length + i2];
        for (int i3 = 0; i3 < length; i3++) {
            nameArr[i3] = argument(i3, BasicType.basicType(clsArr[i3]));
        }
        return nameArr;
    }

    static Name[] arguments(int i2, MethodType methodType) {
        int iParameterCount = methodType.parameterCount();
        Name[] nameArr = new Name[iParameterCount + i2];
        for (int i3 = 0; i3 < iParameterCount; i3++) {
            nameArr[i3] = argument(i3, BasicType.basicType(methodType.parameterType(i3)));
        }
        return nameArr;
    }

    static LambdaForm identityForm(BasicType basicType) {
        return LF_identityForm[basicType.ordinal()];
    }

    static LambdaForm zeroForm(BasicType basicType) {
        return LF_zeroForm[basicType.ordinal()];
    }

    static NamedFunction identity(BasicType basicType) {
        return NF_identity[basicType.ordinal()];
    }

    static NamedFunction constantZero(BasicType basicType) {
        return NF_zero[basicType.ordinal()];
    }

    private static void createIdentityForms() {
        LambdaForm lambdaForm;
        LambdaForm lambdaForm2;
        BasicType[] basicTypeArr = BasicType.ALL_TYPES;
        int length = basicTypeArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            BasicType basicType = basicTypeArr[i2];
            int iOrdinal = basicType.ordinal();
            char cBasicTypeChar = basicType.basicTypeChar();
            boolean z2 = basicType == BasicType.V_TYPE;
            Class<?> cls = basicType.btClass;
            MethodType methodType = MethodType.methodType(cls);
            MemberName memberName = new MemberName((Class<?>) LambdaForm.class, "identity_" + cBasicTypeChar, z2 ? methodType : methodType.appendParameterTypes(cls), (byte) 6);
            try {
                MemberName memberNameResolveOrFail = IMPL_NAMES.resolveOrFail((byte) 6, new MemberName((Class<?>) LambdaForm.class, "zero_" + cBasicTypeChar, methodType, (byte) 6), null, NoSuchMethodException.class);
                MemberName memberNameResolveOrFail2 = IMPL_NAMES.resolveOrFail((byte) 6, memberName, null, NoSuchMethodException.class);
                NamedFunction namedFunction = new NamedFunction(memberNameResolveOrFail2);
                if (z2) {
                    lambdaForm = new LambdaForm(memberNameResolveOrFail2.getName(), 1, new Name[]{argument(0, BasicType.L_TYPE)}, -1);
                } else {
                    lambdaForm = new LambdaForm(memberNameResolveOrFail2.getName(), 2, new Name[]{argument(0, BasicType.L_TYPE), argument(1, basicType)}, 1);
                }
                LF_identityForm[iOrdinal] = lambdaForm;
                NF_identity[iOrdinal] = namedFunction;
                NamedFunction namedFunction2 = new NamedFunction(memberNameResolveOrFail);
                if (z2) {
                    lambdaForm2 = lambdaForm;
                } else {
                    lambdaForm2 = new LambdaForm(memberNameResolveOrFail.getName(), 1, new Name[]{argument(0, BasicType.L_TYPE), new Name(namedFunction, Wrapper.forBasicType(cBasicTypeChar).zero())}, 1);
                }
                LF_zeroForm[iOrdinal] = lambdaForm2;
                NF_zero[iOrdinal] = namedFunction2;
                if (!$assertionsDisabled && !namedFunction.isIdentity()) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && !namedFunction2.isConstantZero()) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && !new Name(namedFunction2, new Object[0]).isConstantZero()) {
                    throw new AssertionError();
                }
            } catch (IllegalAccessException | NoSuchMethodException e2) {
                throw MethodHandleStatics.newInternalError(e2);
            }
        }
        for (BasicType basicType2 : BasicType.ALL_TYPES) {
            int iOrdinal2 = basicType2.ordinal();
            NamedFunction namedFunction3 = NF_identity[iOrdinal2];
            namedFunction3.resolvedHandle = SimpleMethodHandle.make(namedFunction3.member.getInvocationType(), LF_identityForm[iOrdinal2]);
            NamedFunction namedFunction4 = NF_zero[iOrdinal2];
            namedFunction4.resolvedHandle = SimpleMethodHandle.make(namedFunction4.member.getInvocationType(), LF_zeroForm[iOrdinal2]);
            if (!$assertionsDisabled && !namedFunction3.isIdentity()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !namedFunction4.isConstantZero()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !new Name(namedFunction4, new Object[0]).isConstantZero()) {
                throw new AssertionError();
            }
        }
    }

    private static int identity_I(int i2) {
        return i2;
    }

    private static long identity_J(long j2) {
        return j2;
    }

    private static float identity_F(float f2) {
        return f2;
    }

    private static double identity_D(double d2) {
        return d2;
    }

    private static Object identity_L(Object obj) {
        return obj;
    }

    private static void identity_V() {
    }

    private static int zero_I() {
        return 0;
    }

    private static long zero_J() {
        return 0L;
    }

    private static float zero_F() {
        return 0.0f;
    }

    private static double zero_D() {
        return 0.0d;
    }

    private static Object zero_L() {
        return null;
    }

    private static void zero_V() {
    }
}
