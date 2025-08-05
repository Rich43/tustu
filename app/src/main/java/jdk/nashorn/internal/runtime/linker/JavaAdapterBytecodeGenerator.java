package jdk.nashorn.internal.runtime.linker;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.InstructionAdapter;
import jdk.nashorn.api.scripting.ScriptUtils;
import jdk.nashorn.internal.lookup.Lookup;
import jdk.nashorn.internal.runtime.JSType;
import jdk.nashorn.internal.runtime.ScriptFunction;
import jdk.nashorn.internal.runtime.ScriptObject;
import jdk.nashorn.internal.runtime.linker.AdaptationResult;
import sun.reflect.CallerSensitive;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/JavaAdapterBytecodeGenerator.class */
final class JavaAdapterBytecodeGenerator {
    private static final Type SCRIPTUTILS_TYPE;
    private static final Type OBJECT_TYPE;
    private static final Type CLASS_TYPE;
    static final String OBJECT_TYPE_NAME;
    static final String SCRIPTUTILS_TYPE_NAME;
    static final String INIT = "<init>";
    static final String GLOBAL_FIELD_NAME = "global";
    static final String GLOBAL_TYPE_DESCRIPTOR;
    static final String SET_GLOBAL_METHOD_DESCRIPTOR;
    static final String VOID_NOARG_METHOD_DESCRIPTOR;
    private static final Type SCRIPT_OBJECT_TYPE;
    private static final Type SCRIPT_FUNCTION_TYPE;
    private static final Type STRING_TYPE;
    private static final Type METHOD_TYPE_TYPE;
    private static final Type METHOD_HANDLE_TYPE;
    private static final String GET_HANDLE_OBJECT_DESCRIPTOR;
    private static final String GET_HANDLE_FUNCTION_DESCRIPTOR;
    private static final String GET_CLASS_INITIALIZER_DESCRIPTOR;
    private static final Type RUNTIME_EXCEPTION_TYPE;
    private static final Type THROWABLE_TYPE;
    private static final Type UNSUPPORTED_OPERATION_TYPE;
    private static final String SERVICES_CLASS_TYPE_NAME;
    private static final String RUNTIME_EXCEPTION_TYPE_NAME;
    private static final String ERROR_TYPE_NAME;
    private static final String THROWABLE_TYPE_NAME;
    private static final String UNSUPPORTED_OPERATION_TYPE_NAME;
    private static final String METHOD_HANDLE_TYPE_DESCRIPTOR;
    private static final String GET_GLOBAL_METHOD_DESCRIPTOR;
    private static final String GET_CLASS_METHOD_DESCRIPTOR;
    private static final String EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR;
    private static final String UNWRAP_METHOD_DESCRIPTOR;
    private static final String GET_CONVERTER_METHOD_DESCRIPTOR;
    private static final String TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR;
    private static final String TO_STRING_METHOD_DESCRIPTOR;
    private static final String ADAPTER_PACKAGE_PREFIX = "jdk/nashorn/javaadapters/";
    private static final String ADAPTER_CLASS_NAME_SUFFIX = "$$NashornJavaAdapter";
    private static final String JAVA_PACKAGE_PREFIX = "java/";
    private static final int MAX_GENERATED_TYPE_NAME_LENGTH = 255;
    private static final String CLASS_INIT = "<clinit>";
    static final String SUPER_PREFIX = "super$";
    private static final Collection<MethodInfo> EXCLUDED;
    private final Class<?> superClass;
    private final List<Class<?>> interfaces;
    private final ClassLoader commonLoader;
    private final boolean classOverride;
    private final String superClassName;
    private final String generatedClassName;
    private final String samName;
    private final ClassWriter cw;
    private static final AccessControlContext GET_DECLARED_MEMBERS_ACC_CTXT;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final Set<String> usedFieldNames = new HashSet();
    private final Set<String> abstractMethodNames = new HashSet();
    private final Set<MethodInfo> finalMethods = new HashSet(EXCLUDED);
    private final Set<MethodInfo> methodInfos = new HashSet();
    private boolean autoConvertibleFromFunction = false;
    private boolean hasExplicitFinalizer = false;
    private final Map<Class<?>, String> converterFields = new LinkedHashMap();
    private final Set<Class<?>> samReturnTypes = new HashSet();

    static {
        $assertionsDisabled = !JavaAdapterBytecodeGenerator.class.desiredAssertionStatus();
        SCRIPTUTILS_TYPE = Type.getType((Class<?>) ScriptUtils.class);
        OBJECT_TYPE = Type.getType((Class<?>) Object.class);
        CLASS_TYPE = Type.getType((Class<?>) Class.class);
        OBJECT_TYPE_NAME = OBJECT_TYPE.getInternalName();
        SCRIPTUTILS_TYPE_NAME = SCRIPTUTILS_TYPE.getInternalName();
        GLOBAL_TYPE_DESCRIPTOR = OBJECT_TYPE.getDescriptor();
        SET_GLOBAL_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE, OBJECT_TYPE);
        VOID_NOARG_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]);
        SCRIPT_OBJECT_TYPE = Type.getType((Class<?>) ScriptObject.class);
        SCRIPT_FUNCTION_TYPE = Type.getType((Class<?>) ScriptFunction.class);
        STRING_TYPE = Type.getType((Class<?>) String.class);
        METHOD_TYPE_TYPE = Type.getType((Class<?>) MethodType.class);
        METHOD_HANDLE_TYPE = Type.getType((Class<?>) MethodHandle.class);
        GET_HANDLE_OBJECT_DESCRIPTOR = Type.getMethodDescriptor(METHOD_HANDLE_TYPE, OBJECT_TYPE, STRING_TYPE, METHOD_TYPE_TYPE);
        GET_HANDLE_FUNCTION_DESCRIPTOR = Type.getMethodDescriptor(METHOD_HANDLE_TYPE, SCRIPT_FUNCTION_TYPE, METHOD_TYPE_TYPE);
        GET_CLASS_INITIALIZER_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, new Type[0]);
        RUNTIME_EXCEPTION_TYPE = Type.getType((Class<?>) RuntimeException.class);
        THROWABLE_TYPE = Type.getType((Class<?>) Throwable.class);
        UNSUPPORTED_OPERATION_TYPE = Type.getType((Class<?>) UnsupportedOperationException.class);
        SERVICES_CLASS_TYPE_NAME = Type.getInternalName(JavaAdapterServices.class);
        RUNTIME_EXCEPTION_TYPE_NAME = RUNTIME_EXCEPTION_TYPE.getInternalName();
        ERROR_TYPE_NAME = Type.getInternalName(Error.class);
        THROWABLE_TYPE_NAME = THROWABLE_TYPE.getInternalName();
        UNSUPPORTED_OPERATION_TYPE_NAME = UNSUPPORTED_OPERATION_TYPE.getInternalName();
        METHOD_HANDLE_TYPE_DESCRIPTOR = METHOD_HANDLE_TYPE.getDescriptor();
        GET_GLOBAL_METHOD_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, new Type[0]);
        GET_CLASS_METHOD_DESCRIPTOR = Type.getMethodDescriptor(CLASS_TYPE, new Type[0]);
        EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE);
        UNWRAP_METHOD_DESCRIPTOR = Type.getMethodDescriptor(OBJECT_TYPE, OBJECT_TYPE);
        GET_CONVERTER_METHOD_DESCRIPTOR = Type.getMethodDescriptor(METHOD_HANDLE_TYPE, CLASS_TYPE);
        TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR = Type.getMethodDescriptor(Type.CHAR_TYPE, OBJECT_TYPE);
        TO_STRING_METHOD_DESCRIPTOR = Type.getMethodDescriptor(STRING_TYPE, OBJECT_TYPE);
        EXCLUDED = getExcludedMethods();
        GET_DECLARED_MEMBERS_ACC_CTXT = ClassAndLoader.createPermAccCtxt("accessDeclaredMembers");
    }

    JavaAdapterBytecodeGenerator(Class<?> superClass, List<Class<?>> interfaces, ClassLoader commonLoader, boolean classOverride) throws SecurityException, AdaptationException {
        if (!$assertionsDisabled && (superClass == null || superClass.isInterface())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && interfaces == null) {
            throw new AssertionError();
        }
        this.superClass = superClass;
        this.interfaces = interfaces;
        this.classOverride = classOverride;
        this.commonLoader = commonLoader;
        this.cw = new ClassWriter(3) { // from class: jdk.nashorn.internal.runtime.linker.JavaAdapterBytecodeGenerator.1
            @Override // jdk.internal.org.objectweb.asm.ClassWriter
            protected String getCommonSuperClass(String type1, String type2) {
                return JavaAdapterBytecodeGenerator.this.getCommonSuperClass(type1, type2);
            }
        };
        this.superClassName = Type.getInternalName(superClass);
        this.generatedClassName = getGeneratedClassName(superClass, interfaces);
        this.cw.visit(51, 33, this.generatedClassName, null, this.superClassName, getInternalTypeNames(interfaces));
        generateGlobalFields();
        gatherMethods(superClass);
        gatherMethods(interfaces);
        this.samName = this.abstractMethodNames.size() == 1 ? this.abstractMethodNames.iterator().next() : null;
        generateHandleFields();
        generateConverterFields();
        if (classOverride) {
            generateClassInit();
        }
        generateConstructors();
        generateMethods();
        generateSuperMethods();
        if (this.hasExplicitFinalizer) {
            generateFinalizerMethods();
        }
        this.cw.visitEnd();
    }

    private void generateGlobalFields() {
        this.cw.visitField(18 | (this.classOverride ? 8 : 0), "global", GLOBAL_TYPE_DESCRIPTOR, null, null).visitEnd();
        this.usedFieldNames.add("global");
    }

    JavaAdapterClassLoader createAdapterClassLoader() {
        return new JavaAdapterClassLoader(this.generatedClassName, this.cw.toByteArray());
    }

    boolean isAutoConvertibleFromFunction() {
        return this.autoConvertibleFromFunction;
    }

    private static String getGeneratedClassName(Class<?> superType, List<Class<?>> interfaces) {
        Class<?> namingType = superType == Object.class ? interfaces.isEmpty() ? Object.class : interfaces.get(0) : superType;
        Package pkg = namingType.getPackage();
        String namingTypeName = Type.getInternalName(namingType);
        StringBuilder buf = new StringBuilder();
        if (namingTypeName.startsWith(JAVA_PACKAGE_PREFIX) || pkg == null || pkg.isSealed()) {
            buf.append(ADAPTER_PACKAGE_PREFIX).append(namingTypeName);
        } else {
            buf.append(namingTypeName).append(ADAPTER_CLASS_NAME_SUFFIX);
        }
        Iterator<Class<?>> it = interfaces.iterator();
        if (superType == Object.class && it.hasNext()) {
            it.next();
        }
        while (it.hasNext()) {
            buf.append("$$").append(it.next().getSimpleName());
        }
        return buf.toString().substring(0, Math.min(255, buf.length()));
    }

    private static String[] getInternalTypeNames(List<Class<?>> classes) {
        int interfaceCount = classes.size();
        String[] interfaceNames = new String[interfaceCount];
        for (int i2 = 0; i2 < interfaceCount; i2++) {
            interfaceNames[i2] = Type.getInternalName(classes.get(i2));
        }
        return interfaceNames;
    }

    private void generateHandleFields() {
        int flags = 18 | (this.classOverride ? 8 : 0);
        for (MethodInfo mi : this.methodInfos) {
            this.cw.visitField(flags, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR, null, null).visitEnd();
        }
    }

    private void generateConverterFields() {
        int flags = 18 | (this.classOverride ? 8 : 0);
        for (MethodInfo mi : this.methodInfos) {
            Class<?> returnType = mi.type.returnType();
            if (!returnType.isPrimitive() && returnType != Object.class && returnType != String.class && !this.converterFields.containsKey(returnType)) {
                String name = nextName("convert");
                this.converterFields.put(returnType, name);
                if (mi.getName().equals(this.samName)) {
                    this.samReturnTypes.add(returnType);
                }
                this.cw.visitField(flags, name, METHOD_HANDLE_TYPE_DESCRIPTOR, null, null).visitEnd();
            }
        }
    }

    private void generateClassInit() {
        Label initGlobal;
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(8, "<clinit>", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), null, null));
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getClassOverrides", GET_CLASS_INITIALIZER_DESCRIPTOR, false);
        if (this.samName != null) {
            Label notAFunction = new Label();
            mv.dup();
            mv.instanceOf(SCRIPT_FUNCTION_TYPE);
            mv.ifeq(notAFunction);
            mv.checkcast(SCRIPT_FUNCTION_TYPE);
            for (MethodInfo mi : this.methodInfos) {
                if (mi.getName().equals(this.samName)) {
                    mv.dup();
                    loadMethodTypeAndGetHandle(mv, mi, GET_HANDLE_FUNCTION_DESCRIPTOR);
                } else {
                    mv.visitInsn(1);
                }
                mv.putstatic(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
            }
            initGlobal = new Label();
            mv.goTo(initGlobal);
            mv.visitLabel(notAFunction);
        } else {
            initGlobal = null;
        }
        for (MethodInfo mi2 : this.methodInfos) {
            mv.dup();
            mv.aconst(mi2.getName());
            loadMethodTypeAndGetHandle(mv, mi2, GET_HANDLE_OBJECT_DESCRIPTOR);
            mv.putstatic(this.generatedClassName, mi2.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        if (initGlobal != null) {
            mv.visitLabel(initGlobal);
        }
        invokeGetGlobalWithNullCheck(mv);
        mv.putstatic(this.generatedClassName, "global", GLOBAL_TYPE_DESCRIPTOR);
        generateConverterInit(mv, false);
        endInitMethod(mv);
    }

    private void generateConverterInit(InstructionAdapter mv, boolean samOnly) {
        if (!$assertionsDisabled && samOnly && this.classOverride) {
            throw new AssertionError();
        }
        for (Map.Entry<Class<?>, String> converterField : this.converterFields.entrySet()) {
            Class<?> returnType = converterField.getKey();
            if (!this.classOverride) {
                mv.visitVarInsn(25, 0);
            }
            if (samOnly && !this.samReturnTypes.contains(returnType)) {
                mv.visitInsn(1);
            } else {
                mv.aconst(Type.getType(converterField.getKey()));
                mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getObjectConverter", GET_CONVERTER_METHOD_DESCRIPTOR, false);
            }
            if (this.classOverride) {
                mv.putstatic(this.generatedClassName, converterField.getValue(), METHOD_HANDLE_TYPE_DESCRIPTOR);
            } else {
                mv.putfield(this.generatedClassName, converterField.getValue(), METHOD_HANDLE_TYPE_DESCRIPTOR);
            }
        }
    }

    private static void loadMethodTypeAndGetHandle(InstructionAdapter mv, MethodInfo mi, String getHandleDescriptor) {
        mv.aconst(Type.getMethodType(mi.type.generic().toMethodDescriptorString()));
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getHandle", getHandleDescriptor, false);
    }

    private static void invokeGetGlobalWithNullCheck(InstructionAdapter mv) {
        invokeGetGlobal(mv);
        mv.dup();
        mv.invokevirtual(OBJECT_TYPE_NAME, "getClass", GET_CLASS_METHOD_DESCRIPTOR, false);
        mv.pop();
    }

    private void generateConstructors() throws SecurityException, AdaptationException {
        boolean gotCtor = false;
        for (Constructor<?> ctor : this.superClass.getDeclaredConstructors()) {
            int modifier = ctor.getModifiers();
            if ((modifier & 5) != 0 && !isCallerSensitive(ctor)) {
                generateConstructors(ctor);
                gotCtor = true;
            }
        }
        if (!gotCtor) {
            throw new AdaptationException(AdaptationResult.Outcome.ERROR_NO_ACCESSIBLE_CONSTRUCTOR, this.superClass.getCanonicalName());
        }
    }

    private void generateConstructors(Constructor<?> ctor) {
        if (this.classOverride) {
            generateDelegatingConstructor(ctor);
            return;
        }
        generateOverridingConstructor(ctor, false);
        if (this.samName != null) {
            if (!this.autoConvertibleFromFunction && ctor.getParameterTypes().length == 0) {
                this.autoConvertibleFromFunction = true;
            }
            generateOverridingConstructor(ctor, true);
        }
    }

    private void generateDelegatingConstructor(Constructor<?> ctor) {
        Type originalCtorType = Type.getType(ctor);
        Type[] argTypes = originalCtorType.getArgumentTypes();
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1 | (ctor.isVarArgs() ? 128 : 0), "<init>", Type.getMethodDescriptor(originalCtorType.getReturnType(), argTypes), null, null));
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        int offset = 1;
        for (Type argType : argTypes) {
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, "<init>", originalCtorType.getDescriptor(), false);
        endInitMethod(mv);
    }

    private void generateOverridingConstructor(Constructor<?> ctor, boolean fromFunction) {
        Type originalCtorType = Type.getType(ctor);
        Type[] originalArgTypes = originalCtorType.getArgumentTypes();
        int argLen = originalArgTypes.length;
        Type[] newArgTypes = new Type[argLen + 1];
        Type extraArgumentType = fromFunction ? SCRIPT_FUNCTION_TYPE : SCRIPT_OBJECT_TYPE;
        newArgTypes[argLen] = extraArgumentType;
        System.arraycopy(originalArgTypes, 0, newArgTypes, 0, argLen);
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1, "<init>", Type.getMethodDescriptor(originalCtorType.getReturnType(), newArgTypes), null, null));
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        Class<?>[] argTypes = ctor.getParameterTypes();
        int offset = 1;
        for (int i2 = 0; i2 < argLen; i2++) {
            Type argType = Type.getType(argTypes[i2]);
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, "<init>", originalCtorType.getDescriptor(), false);
        String getHandleDescriptor = fromFunction ? GET_HANDLE_FUNCTION_DESCRIPTOR : GET_HANDLE_OBJECT_DESCRIPTOR;
        for (MethodInfo mi : this.methodInfos) {
            mv.visitVarInsn(25, 0);
            if (fromFunction && !mi.getName().equals(this.samName)) {
                mv.visitInsn(1);
            } else {
                mv.visitVarInsn(25, offset);
                if (!fromFunction) {
                    mv.aconst(mi.getName());
                }
                loadMethodTypeAndGetHandle(mv, mi, getHandleDescriptor);
            }
            mv.putfield(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        mv.visitVarInsn(25, 0);
        invokeGetGlobalWithNullCheck(mv);
        mv.putfield(this.generatedClassName, "global", GLOBAL_TYPE_DESCRIPTOR);
        generateConverterInit(mv, fromFunction);
        endInitMethod(mv);
        if (!fromFunction) {
            newArgTypes[argLen] = OBJECT_TYPE;
            InstructionAdapter mv2 = new InstructionAdapter(this.cw.visitMethod(1, "<init>", Type.getMethodDescriptor(originalCtorType.getReturnType(), newArgTypes), null, null));
            generateOverridingConstructorWithObjectParam(mv2, ctor, originalCtorType.getDescriptor());
        }
    }

    private void generateOverridingConstructorWithObjectParam(InstructionAdapter mv, Constructor<?> ctor, String ctorDescriptor) {
        mv.visitCode();
        mv.visitVarInsn(25, 0);
        Class<?>[] argTypes = ctor.getParameterTypes();
        int offset = 1;
        for (Class<?> cls : argTypes) {
            Type argType = Type.getType(cls);
            mv.load(offset, argType);
            offset += argType.getSize();
        }
        mv.invokespecial(this.superClassName, "<init>", ctorDescriptor, false);
        mv.visitVarInsn(25, offset);
        mv.visitInsn(1);
        mv.visitInsn(1);
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getHandle", GET_HANDLE_OBJECT_DESCRIPTOR, false);
        endInitMethod(mv);
    }

    private static void endInitMethod(InstructionAdapter mv) {
        mv.visitInsn(177);
        endMethod(mv);
    }

    private static void endMethod(InstructionAdapter mv) {
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private static void invokeGetGlobal(InstructionAdapter mv) {
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "getGlobal", GET_GLOBAL_METHOD_DESCRIPTOR, false);
    }

    private static void invokeSetGlobal(InstructionAdapter mv) {
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "setGlobal", SET_GLOBAL_METHOD_DESCRIPTOR, false);
    }

    /* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/linker/JavaAdapterBytecodeGenerator$MethodInfo.class */
    private static class MethodInfo {
        private final Method method;
        private final MethodType type;
        private String methodHandleFieldName;

        private MethodInfo(Class<?> clazz, String name, Class<?>... argTypes) throws NoSuchMethodException {
            this(clazz.getDeclaredMethod(name, argTypes));
        }

        private MethodInfo(Method method) {
            this.method = method;
            this.type = Lookup.MH.type(method.getReturnType(), method.getParameterTypes());
        }

        public boolean equals(Object obj) {
            return (obj instanceof MethodInfo) && equals((MethodInfo) obj);
        }

        private boolean equals(MethodInfo other) {
            return getName().equals(other.getName()) && this.type.equals((Object) other.type);
        }

        String getName() {
            return this.method.getName();
        }

        public int hashCode() {
            return getName().hashCode() ^ this.type.hashCode();
        }

        void setIsCanonical(JavaAdapterBytecodeGenerator self) {
            this.methodHandleFieldName = self.nextName(getName());
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String nextName(String name) {
        int i2 = 0;
        String strConcat = name;
        while (true) {
            String nextName = strConcat;
            if (!this.usedFieldNames.add(nextName)) {
                int i3 = i2;
                i2++;
                String ordinal = String.valueOf(i3);
                int maxNameLen = 255 - ordinal.length();
                strConcat = (name.length() <= maxNameLen ? name : name.substring(0, maxNameLen)).concat(ordinal);
            } else {
                return nextName;
            }
        }
    }

    private void generateMethods() {
        for (MethodInfo mi : this.methodInfos) {
            generateMethod(mi);
        }
    }

    private void generateMethod(MethodInfo mi) {
        Label throwableHandler;
        Method method = mi.method;
        Class<?>[] exceptions = method.getExceptionTypes();
        String[] exceptionNames = getExceptionNames(exceptions);
        MethodType type = mi.type;
        String methodDesc = type.toMethodDescriptorString();
        String name = mi.getName();
        Type asmType = Type.getMethodType(methodDesc);
        Type[] asmArgTypes = asmType.getArgumentTypes();
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(getAccessModifiers(method), name, methodDesc, null, exceptionNames));
        mv.visitCode();
        Label handleDefined = new Label();
        Class<?> returnType = type.returnType();
        Type asmReturnType = Type.getType(returnType);
        if (this.classOverride) {
            mv.getstatic(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        } else {
            mv.visitVarInsn(25, 0);
            mv.getfield(this.generatedClassName, mi.methodHandleFieldName, METHOD_HANDLE_TYPE_DESCRIPTOR);
        }
        mv.visitInsn(89);
        mv.visitJumpInsn(199, handleDefined);
        if (Modifier.isAbstract(method.getModifiers())) {
            mv.anew(UNSUPPORTED_OPERATION_TYPE);
            mv.dup();
            mv.invokespecial(UNSUPPORTED_OPERATION_TYPE_NAME, "<init>", VOID_NOARG_METHOD_DESCRIPTOR, false);
            mv.athrow();
        } else {
            mv.visitInsn(87);
            emitSuperCall(mv, method.getDeclaringClass(), name, methodDesc);
        }
        mv.visitLabel(handleDefined);
        if (this.classOverride) {
            mv.getstatic(this.generatedClassName, "global", GLOBAL_TYPE_DESCRIPTOR);
        } else {
            mv.visitVarInsn(25, 0);
            mv.getfield(this.generatedClassName, "global", GLOBAL_TYPE_DESCRIPTOR);
        }
        Label setupGlobal = new Label();
        mv.visitLabel(setupGlobal);
        int nextLocalVar = 1;
        for (Type type2 : asmArgTypes) {
            nextLocalVar += type2.getSize();
        }
        int currentGlobalVar = nextLocalVar;
        int nextLocalVar2 = nextLocalVar + 1;
        int i2 = nextLocalVar2 + 1;
        mv.dup();
        invokeGetGlobal(mv);
        mv.dup();
        mv.visitVarInsn(58, currentGlobalVar);
        Label globalsDiffer = new Label();
        mv.ifacmpne(globalsDiffer);
        mv.pop();
        mv.iconst(0);
        Label invokeHandle = new Label();
        mv.goTo(invokeHandle);
        mv.visitLabel(globalsDiffer);
        invokeSetGlobal(mv);
        mv.iconst(1);
        mv.visitLabel(invokeHandle);
        mv.visitVarInsn(54, nextLocalVar2);
        int varOffset = 1;
        for (Type t2 : asmArgTypes) {
            mv.load(varOffset, t2);
            boxStackTop(mv, t2);
            varOffset += t2.getSize();
        }
        Label tryBlockStart = new Label();
        mv.visitLabel(tryBlockStart);
        emitInvokeExact(mv, type.generic());
        convertReturnValue(mv, returnType, asmReturnType);
        Label tryBlockEnd = new Label();
        mv.visitLabel(tryBlockEnd);
        emitFinally(mv, currentGlobalVar, nextLocalVar2);
        mv.areturn(asmReturnType);
        boolean throwableDeclared = isThrowableDeclared(exceptions);
        if (!throwableDeclared) {
            throwableHandler = new Label();
            mv.visitLabel(throwableHandler);
            mv.anew(RUNTIME_EXCEPTION_TYPE);
            mv.dupX1();
            mv.swap();
            mv.invokespecial(RUNTIME_EXCEPTION_TYPE_NAME, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, THROWABLE_TYPE), false);
        } else {
            throwableHandler = null;
        }
        Label rethrowHandler = new Label();
        mv.visitLabel(rethrowHandler);
        emitFinally(mv, currentGlobalVar, nextLocalVar2);
        mv.athrow();
        Label methodEnd = new Label();
        mv.visitLabel(methodEnd);
        mv.visitLocalVariable("currentGlobal", GLOBAL_TYPE_DESCRIPTOR, null, setupGlobal, methodEnd, currentGlobalVar);
        mv.visitLocalVariable("globalsDiffer", Type.BOOLEAN_TYPE.getDescriptor(), null, setupGlobal, methodEnd, nextLocalVar2);
        if (throwableDeclared) {
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, THROWABLE_TYPE_NAME);
            if (!$assertionsDisabled && throwableHandler != null) {
                throw new AssertionError();
            }
        } else {
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, RUNTIME_EXCEPTION_TYPE_NAME);
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, ERROR_TYPE_NAME);
            for (String excName : exceptionNames) {
                mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, rethrowHandler, excName);
            }
            mv.visitTryCatchBlock(tryBlockStart, tryBlockEnd, throwableHandler, THROWABLE_TYPE_NAME);
        }
        endMethod(mv);
    }

    private void convertReturnValue(InstructionAdapter mv, Class<?> returnType, Type asmReturnType) {
        switch (asmReturnType.getSort()) {
            case 0:
                mv.pop();
                break;
            case 1:
                JSType.TO_BOOLEAN.invoke(mv);
                break;
            case 2:
                mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "toCharPrimitive", TO_CHAR_PRIMITIVE_METHOD_DESCRIPTOR, false);
                break;
            case 3:
                JSType.TO_INT32.invoke(mv);
                mv.visitInsn(145);
                break;
            case 4:
                JSType.TO_INT32.invoke(mv);
                mv.visitInsn(147);
                break;
            case 5:
                JSType.TO_INT32.invoke(mv);
                break;
            case 6:
                JSType.TO_NUMBER.invoke(mv);
                mv.visitInsn(144);
                break;
            case 7:
                JSType.TO_LONG.invoke(mv);
                break;
            case 8:
                JSType.TO_NUMBER.invoke(mv);
                break;
            default:
                if (asmReturnType.equals(OBJECT_TYPE)) {
                    mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "exportReturnValue", EXPORT_RETURN_VALUE_METHOD_DESCRIPTOR, false);
                    break;
                } else if (asmReturnType.equals(STRING_TYPE)) {
                    mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "toString", TO_STRING_METHOD_DESCRIPTOR, false);
                    break;
                } else {
                    if (this.classOverride) {
                        mv.getstatic(this.generatedClassName, this.converterFields.get(returnType), METHOD_HANDLE_TYPE_DESCRIPTOR);
                    } else {
                        mv.visitVarInsn(25, 0);
                        mv.getfield(this.generatedClassName, this.converterFields.get(returnType), METHOD_HANDLE_TYPE_DESCRIPTOR);
                    }
                    mv.swap();
                    emitInvokeExact(mv, MethodType.methodType(returnType, (Class<?>) Object.class));
                    break;
                }
        }
    }

    private static void emitInvokeExact(InstructionAdapter mv, MethodType type) {
        mv.invokevirtual(METHOD_HANDLE_TYPE.getInternalName(), "invokeExact", type.toMethodDescriptorString(), false);
    }

    private static void boxStackTop(InstructionAdapter mv, Type t2) {
        switch (t2.getSort()) {
            case 1:
                invokeValueOf(mv, "Boolean", 'Z');
                return;
            case 2:
                invokeValueOf(mv, "Character", 'C');
                return;
            case 3:
            case 4:
            case 5:
                invokeValueOf(mv, "Integer", 'I');
                return;
            case 6:
                mv.visitInsn(141);
                invokeValueOf(mv, "Double", 'D');
                return;
            case 7:
                invokeValueOf(mv, "Long", 'J');
                return;
            case 8:
                invokeValueOf(mv, "Double", 'D');
                return;
            case 9:
            case 11:
                return;
            case 10:
                if (t2.equals(OBJECT_TYPE)) {
                    mv.invokestatic(SCRIPTUTILS_TYPE_NAME, "unwrap", UNWRAP_METHOD_DESCRIPTOR, false);
                    return;
                }
                return;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError();
                }
                return;
        }
    }

    private static void invokeValueOf(InstructionAdapter mv, String boxedType, char unboxedType) {
        mv.invokestatic("java/lang/" + boxedType, BeanAdapter.VALUE_OF_METHOD_NAME, "(" + unboxedType + ")Ljava/lang/" + boxedType + ";", false);
    }

    private static void emitFinally(InstructionAdapter mv, int currentGlobalVar, int globalsDifferVar) {
        mv.visitVarInsn(21, globalsDifferVar);
        Label skip = new Label();
        mv.ifeq(skip);
        mv.visitVarInsn(25, currentGlobalVar);
        invokeSetGlobal(mv);
        mv.visitLabel(skip);
    }

    private static boolean isThrowableDeclared(Class<?>[] exceptions) {
        for (Class<?> exception : exceptions) {
            if (exception == Throwable.class) {
                return true;
            }
        }
        return false;
    }

    private void generateSuperMethods() {
        for (MethodInfo mi : this.methodInfos) {
            if (!Modifier.isAbstract(mi.method.getModifiers())) {
                generateSuperMethod(mi);
            }
        }
    }

    private void generateSuperMethod(MethodInfo mi) {
        Method method = mi.method;
        String methodDesc = mi.type.toMethodDescriptorString();
        String name = mi.getName();
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(getAccessModifiers(method), SUPER_PREFIX + name, methodDesc, null, getExceptionNames(method.getExceptionTypes())));
        mv.visitCode();
        emitSuperCall(mv, method.getDeclaringClass(), name, methodDesc);
        endMethod(mv);
    }

    private Class<?> findInvokespecialOwnerFor(Class<?> cl) {
        if (!$assertionsDisabled && !Modifier.isInterface(cl.getModifiers())) {
            throw new AssertionError((Object) (((Object) cl) + " is not an interface"));
        }
        if (cl.isAssignableFrom(this.superClass)) {
            return this.superClass;
        }
        for (Class<?> iface : this.interfaces) {
            if (cl.isAssignableFrom(iface)) {
                return iface;
            }
        }
        throw new AssertionError((Object) ("can't find the class/interface that extends " + ((Object) cl)));
    }

    private void emitSuperCall(InstructionAdapter mv, Class<?> owner, String name, String methodDesc) {
        mv.visitVarInsn(25, 0);
        int nextParam = 1;
        Type methodType = Type.getMethodType(methodDesc);
        for (Type t2 : methodType.getArgumentTypes()) {
            mv.load(nextParam, t2);
            nextParam += t2.getSize();
        }
        if (Modifier.isInterface(owner.getModifiers())) {
            mv.invokespecial(Type.getInternalName(findInvokespecialOwnerFor(owner)), name, methodDesc, false);
        } else {
            mv.invokespecial(this.superClassName, name, methodDesc, false);
        }
        mv.areturn(methodType.getReturnType());
    }

    private void generateFinalizerMethods() {
        String finalizerDelegateName = nextName("access$");
        generateFinalizerDelegate(finalizerDelegateName);
        generateFinalizerOverride(finalizerDelegateName);
    }

    private void generateFinalizerDelegate(String finalizerDelegateName) {
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(10, finalizerDelegateName, Type.getMethodDescriptor(Type.VOID_TYPE, OBJECT_TYPE), null, null));
        mv.visitVarInsn(25, 0);
        mv.checkcast(Type.getType(this.generatedClassName));
        mv.invokespecial(this.superClassName, "finalize", Type.getMethodDescriptor(Type.VOID_TYPE, new Type[0]), false);
        mv.visitInsn(177);
        endMethod(mv);
    }

    private void generateFinalizerOverride(String finalizerDelegateName) {
        InstructionAdapter mv = new InstructionAdapter(this.cw.visitMethod(1, "finalize", VOID_NOARG_METHOD_DESCRIPTOR, null, null));
        mv.aconst(new Handle(6, this.generatedClassName, finalizerDelegateName, Type.getMethodDescriptor(Type.VOID_TYPE, OBJECT_TYPE)));
        mv.visitVarInsn(25, 0);
        mv.invokestatic(SERVICES_CLASS_TYPE_NAME, "invokeNoPermissions", Type.getMethodDescriptor(METHOD_HANDLE_TYPE, OBJECT_TYPE), false);
        mv.visitInsn(177);
        endMethod(mv);
    }

    private static String[] getExceptionNames(Class<?>[] exceptions) {
        String[] exceptionNames = new String[exceptions.length];
        for (int i2 = 0; i2 < exceptions.length; i2++) {
            exceptionNames[i2] = Type.getInternalName(exceptions[i2]);
        }
        return exceptionNames;
    }

    private static int getAccessModifiers(Method method) {
        return 1 | (method.isVarArgs() ? 128 : 0);
    }

    private void gatherMethods(Class<?> type) throws AdaptationException {
        if (Modifier.isPublic(type.getModifiers())) {
            Method[] typeMethods = type.isInterface() ? type.getMethods() : type.getDeclaredMethods();
            for (Method typeMethod : typeMethods) {
                String name = typeMethod.getName();
                if (!name.startsWith(SUPER_PREFIX)) {
                    int m2 = typeMethod.getModifiers();
                    if (!Modifier.isStatic(m2) && (Modifier.isPublic(m2) || Modifier.isProtected(m2))) {
                        if (name.equals("finalize") && typeMethod.getParameterCount() == 0) {
                            if (type != Object.class) {
                                this.hasExplicitFinalizer = true;
                                if (Modifier.isFinal(m2)) {
                                    throw new AdaptationException(AdaptationResult.Outcome.ERROR_FINAL_FINALIZER, type.getCanonicalName());
                                }
                            } else {
                                continue;
                            }
                        } else {
                            MethodInfo mi = new MethodInfo(typeMethod);
                            if (Modifier.isFinal(m2) || isCallerSensitive(typeMethod)) {
                                this.finalMethods.add(mi);
                            } else if (!this.finalMethods.contains(mi) && this.methodInfos.add(mi)) {
                                if (Modifier.isAbstract(m2)) {
                                    this.abstractMethodNames.add(mi.getName());
                                }
                                mi.setIsCanonical(this);
                            }
                        }
                    }
                }
            }
        }
        if (!type.isInterface()) {
            Class<?> superType = type.getSuperclass();
            if (superType != null) {
                gatherMethods(superType);
            }
            for (Class<?> itf : type.getInterfaces()) {
                gatherMethods(itf);
            }
        }
    }

    private void gatherMethods(List<Class<?>> classes) throws AdaptationException {
        for (Class<?> c2 : classes) {
            gatherMethods(c2);
        }
    }

    private static Collection<MethodInfo> getExcludedMethods() {
        return (Collection) AccessController.doPrivileged(new PrivilegedAction<Collection<MethodInfo>>() { // from class: jdk.nashorn.internal.runtime.linker.JavaAdapterBytecodeGenerator.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Collection<MethodInfo> run2() {
                try {
                    return Arrays.asList(new MethodInfo(Object.class, "finalize", new Class[0]), new MethodInfo(Object.class, "clone", new Class[0]));
                } catch (NoSuchMethodException e2) {
                    throw new AssertionError(e2);
                }
            }
        }, GET_DECLARED_MEMBERS_ACC_CTXT);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getCommonSuperClass(String type1, String type2) {
        try {
            Class<?> c1 = Class.forName(type1.replace('/', '.'), false, this.commonLoader);
            Class<?> c2 = Class.forName(type2.replace('/', '.'), false, this.commonLoader);
            if (c1.isAssignableFrom(c2)) {
                return type1;
            }
            if (c2.isAssignableFrom(c1)) {
                return type2;
            }
            if (c1.isInterface() || c2.isInterface()) {
                return OBJECT_TYPE_NAME;
            }
            return assignableSuperClass(c1, c2).getName().replace('.', '/');
        } catch (ClassNotFoundException e2) {
            throw new RuntimeException(e2);
        }
    }

    private static Class<?> assignableSuperClass(Class<?> c1, Class<?> c2) {
        Class<?> superClass = c1.getSuperclass();
        return superClass.isAssignableFrom(c2) ? superClass : assignableSuperClass(superClass, c2);
    }

    private static boolean isCallerSensitive(AccessibleObject e2) {
        return e2.isAnnotationPresent(CallerSensitive.class);
    }
}
