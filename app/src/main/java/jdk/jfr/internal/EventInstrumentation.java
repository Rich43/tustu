package jdk.jfr.internal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.commons.Method;
import jdk.internal.org.objectweb.asm.tree.AnnotationNode;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.FieldNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.jfr.Enabled;
import jdk.jfr.Event;
import jdk.jfr.Name;
import jdk.jfr.Registered;
import jdk.jfr.SettingControl;
import jdk.jfr.SettingDefinition;
import jdk.jfr.internal.handlers.EventHandler;
import jdk.nashorn.internal.runtime.regexp.joni.constants.AsmConstants;

/* loaded from: jfr.jar:jdk/jfr/internal/EventInstrumentation.class */
public final class EventInstrumentation {
    public static final String FIELD_EVENT_THREAD = "eventThread";
    public static final String FIELD_STACK_TRACE = "stackTrace";
    public static final String FIELD_DURATION = "duration";
    static final String FIELD_EVENT_HANDLER = "eventHandler";
    static final String FIELD_START_TIME = "startTime";
    private static final Class<? extends EventHandler> eventHandlerProxy = EventHandlerProxyCreator.proxyClass;
    private static final jdk.internal.org.objectweb.asm.Type ANNOTATION_TYPE_NAME = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) Name.class);
    private static final jdk.internal.org.objectweb.asm.Type ANNOTATION_TYPE_REGISTERED = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) Registered.class);
    private static final jdk.internal.org.objectweb.asm.Type ANNOTATION_TYPE_ENABLED = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) Enabled.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_HANDLER = jdk.internal.org.objectweb.asm.Type.getType(eventHandlerProxy);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_SETTING_CONTROL = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) SettingControl.class);
    private static final Method METHOD_COMMIT = new Method("commit", jdk.internal.org.objectweb.asm.Type.VOID_TYPE, new jdk.internal.org.objectweb.asm.Type[0]);
    private static final Method METHOD_BEGIN = new Method("begin", jdk.internal.org.objectweb.asm.Type.VOID_TYPE, new jdk.internal.org.objectweb.asm.Type[0]);
    private static final Method METHOD_END = new Method(AsmConstants.END, jdk.internal.org.objectweb.asm.Type.VOID_TYPE, new jdk.internal.org.objectweb.asm.Type[0]);
    private static final Method METHOD_IS_ENABLED = new Method("isEnabled", jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE, new jdk.internal.org.objectweb.asm.Type[0]);
    private static final Method METHOD_TIME_STAMP = new Method("timestamp", jdk.internal.org.objectweb.asm.Type.LONG_TYPE, new jdk.internal.org.objectweb.asm.Type[0]);
    private static final Method METHOD_EVENT_SHOULD_COMMIT = new Method("shouldCommit", jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE, new jdk.internal.org.objectweb.asm.Type[0]);
    private static final Method METHOD_EVENT_HANDLER_SHOULD_COMMIT = new Method("shouldCommit", jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE, new jdk.internal.org.objectweb.asm.Type[]{jdk.internal.org.objectweb.asm.Type.LONG_TYPE});
    private static final Method METHOD_DURATION = new Method("duration", jdk.internal.org.objectweb.asm.Type.LONG_TYPE, new jdk.internal.org.objectweb.asm.Type[]{jdk.internal.org.objectweb.asm.Type.LONG_TYPE});
    private final ClassNode classNode;
    private final List<SettingInfo> settingInfos;
    private final List<FieldInfo> fieldInfos;
    private final Method writeMethod;
    private final String eventHandlerXInternalName;
    private final String eventName;
    private boolean guardHandlerReference;
    private Class<?> superClass;

    /* loaded from: jfr.jar:jdk/jfr/internal/EventInstrumentation$SettingInfo.class */
    static final class SettingInfo {
        private String methodName;
        private String internalSettingName;
        private String settingDescriptor;
        final String fieldName;
        final int index;
        SettingControl settingControl;

        public SettingInfo(String str, int i2) {
            this.fieldName = str;
            this.index = i2;
        }
    }

    /* loaded from: jfr.jar:jdk/jfr/internal/EventInstrumentation$FieldInfo.class */
    static final class FieldInfo {
        private static final jdk.internal.org.objectweb.asm.Type STRING = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) String.class);
        final String fieldName;
        final String fieldDescriptor;
        final String internalClassName;

        public FieldInfo(String str, String str2, String str3) {
            this.fieldName = str;
            this.fieldDescriptor = str2;
            this.internalClassName = str3;
        }

        public boolean isString() {
            return STRING.getDescriptor().equals(this.fieldDescriptor);
        }
    }

    EventInstrumentation(Class<?> cls, byte[] bArr, long j2) {
        this.superClass = cls;
        this.classNode = createClassNode(bArr);
        this.settingInfos = buildSettingInfos(cls, this.classNode);
        this.fieldInfos = buildFieldInfos(cls, this.classNode);
        this.writeMethod = makeWriteMethod(this.fieldInfos);
        this.eventHandlerXInternalName = ASMToolkit.getInternalName(EventHandlerCreator.makeEventHandlerName(j2));
        String str = (String) annotationValue(this.classNode, ANNOTATION_TYPE_NAME.getDescriptor(), String.class);
        this.eventName = str == null ? this.classNode.name.replace("/", ".") : str;
    }

    public String getClassName() {
        return this.classNode.name.replace("/", ".");
    }

    private ClassNode createClassNode(byte[] bArr) {
        ClassNode classNode = new ClassNode();
        new ClassReader(bArr).accept(classNode, 0);
        return classNode;
    }

    boolean isRegistered() {
        Registered registered;
        Boolean bool = (Boolean) annotationValue(this.classNode, ANNOTATION_TYPE_REGISTERED.getDescriptor(), Boolean.class);
        if (bool != null) {
            return bool.booleanValue();
        }
        if (this.superClass != null && (registered = (Registered) this.superClass.getAnnotation(Registered.class)) != null) {
            return registered.value();
        }
        return true;
    }

    boolean isEnabled() {
        Enabled enabled;
        Boolean bool = (Boolean) annotationValue(this.classNode, ANNOTATION_TYPE_ENABLED.getDescriptor(), Boolean.class);
        if (bool != null) {
            return bool.booleanValue();
        }
        if (this.superClass != null && (enabled = (Enabled) this.superClass.getAnnotation(Enabled.class)) != null) {
            return enabled.value();
        }
        return true;
    }

    private static <T> T annotationValue(ClassNode classNode, String str, Class<?> cls) {
        List<Object> list;
        if (classNode.visibleAnnotations != null) {
            for (AnnotationNode annotationNode : classNode.visibleAnnotations) {
                if (str.equals(annotationNode.desc) && (list = annotationNode.values) != null && list.size() == 2) {
                    Object obj = list.get(0);
                    T t2 = (T) list.get(1);
                    if ((obj instanceof String) && t2 != null && cls == t2.getClass() && "value".equals((String) obj)) {
                        return t2;
                    }
                }
            }
            return null;
        }
        return null;
    }

    private static List<SettingInfo> buildSettingInfos(Class<?> cls, ClassNode classNode) throws SecurityException {
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        String descriptor = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) SettingDefinition.class).getDescriptor();
        for (MethodNode methodNode : classNode.methods) {
            if (methodNode.visibleAnnotations != null) {
                Iterator<AnnotationNode> it = methodNode.visibleAnnotations.iterator();
                while (it.hasNext()) {
                    if (descriptor.equals(it.next().desc) && jdk.internal.org.objectweb.asm.Type.getReturnType(methodNode.desc).equals(jdk.internal.org.objectweb.asm.Type.getType(Boolean.TYPE))) {
                        jdk.internal.org.objectweb.asm.Type[] argumentTypes = jdk.internal.org.objectweb.asm.Type.getArgumentTypes(methodNode.desc);
                        if (argumentTypes.length == 1) {
                            jdk.internal.org.objectweb.asm.Type type = argumentTypes[0];
                            SettingInfo settingInfo = new SettingInfo("setting" + arrayList.size(), arrayList.size());
                            settingInfo.methodName = methodNode.name;
                            settingInfo.settingDescriptor = type.getDescriptor();
                            settingInfo.internalSettingName = type.getInternalName();
                            hashSet.add(methodNode.name);
                            arrayList.add(settingInfo);
                        }
                    }
                }
            }
        }
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls2 = superclass;
            if (cls2 != Event.class) {
                for (java.lang.reflect.Method method : cls2.getDeclaredMethods()) {
                    if (!hashSet.contains(method.getName()) && !Modifier.isPrivate(method.getModifiers()) && method.getReturnType().equals(Boolean.TYPE) && method.getParameterCount() == 1) {
                        jdk.internal.org.objectweb.asm.Type type2 = jdk.internal.org.objectweb.asm.Type.getType(method.getParameters()[0].getType());
                        SettingInfo settingInfo2 = new SettingInfo("setting" + arrayList.size(), arrayList.size());
                        settingInfo2.methodName = method.getName();
                        settingInfo2.settingDescriptor = type2.getDescriptor();
                        settingInfo2.internalSettingName = type2.getInternalName();
                        hashSet.add(method.getName());
                        arrayList.add(settingInfo2);
                    }
                }
                superclass = cls2.getSuperclass();
            } else {
                return arrayList;
            }
        }
    }

    private static List<FieldInfo> buildFieldInfos(Class<?> cls, ClassNode classNode) throws SecurityException {
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList(classNode.fields.size());
        arrayList.add(new FieldInfo(FIELD_START_TIME, jdk.internal.org.objectweb.asm.Type.LONG_TYPE.getDescriptor(), classNode.name));
        arrayList.add(new FieldInfo("duration", jdk.internal.org.objectweb.asm.Type.LONG_TYPE.getDescriptor(), classNode.name));
        for (FieldNode fieldNode : classNode.fields) {
            String className = jdk.internal.org.objectweb.asm.Type.getType(fieldNode.desc).getClassName();
            if (!hashSet.contains(fieldNode.name) && isValidField(fieldNode.access, className)) {
                arrayList.add(new FieldInfo(fieldNode.name, fieldNode.desc, classNode.name));
                hashSet.add(fieldNode.name);
            }
        }
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls2 = superclass;
            if (cls2 != Event.class) {
                for (Field field : cls2.getDeclaredFields()) {
                    if (!Modifier.isPrivate(field.getModifiers()) && isValidField(field.getModifiers(), field.getType().getName())) {
                        String name = field.getName();
                        if (!hashSet.contains(name)) {
                            arrayList.add(new FieldInfo(name, jdk.internal.org.objectweb.asm.Type.getType(field.getType()).getDescriptor(), ASMToolkit.getInternalName(cls2.getName())));
                            hashSet.add(name);
                        }
                    }
                }
                superclass = cls2.getSuperclass();
            } else {
                return arrayList;
            }
        }
    }

    public static boolean isValidField(int i2, String str) {
        if (Modifier.isTransient(i2) || Modifier.isStatic(i2)) {
            return false;
        }
        return Type.isValidJavaFieldType(str);
    }

    public byte[] buildInstrumented() {
        makeInstrumented();
        return toByteArray();
    }

    private byte[] toByteArray() {
        ClassWriter classWriter = new ClassWriter(2);
        this.classNode.accept(classWriter);
        classWriter.visitEnd();
        byte[] byteArray = classWriter.toByteArray();
        Utils.writeGeneratedASM(this.classNode.name, byteArray);
        return byteArray;
    }

    public byte[] builUninstrumented() {
        makeUninstrumented();
        return toByteArray();
    }

    private void makeInstrumented() {
        updateMethod(METHOD_IS_ENABLED, methodVisitor -> {
            Label label = new Label();
            if (this.guardHandlerReference) {
                methodVisitor.visitFieldInsn(178, getInternalClassName(), FIELD_EVENT_HANDLER, TYPE_EVENT_HANDLER.getDescriptor());
                methodVisitor.visitJumpInsn(198, label);
            }
            methodVisitor.visitFieldInsn(178, getInternalClassName(), FIELD_EVENT_HANDLER, TYPE_EVENT_HANDLER.getDescriptor());
            ASMToolkit.invokeVirtual(methodVisitor, TYPE_EVENT_HANDLER.getInternalName(), METHOD_IS_ENABLED);
            methodVisitor.visitInsn(172);
            if (this.guardHandlerReference) {
                methodVisitor.visitLabel(label);
                methodVisitor.visitFrame(3, 0, null, 0, null);
                methodVisitor.visitInsn(3);
                methodVisitor.visitInsn(172);
            }
        });
        updateMethod(METHOD_BEGIN, methodVisitor2 -> {
            methodVisitor2.visitIntInsn(25, 0);
            ASMToolkit.invokeStatic(methodVisitor2, TYPE_EVENT_HANDLER.getInternalName(), METHOD_TIME_STAMP);
            methodVisitor2.visitFieldInsn(181, getInternalClassName(), FIELD_START_TIME, "J");
            methodVisitor2.visitInsn(177);
        });
        updateMethod(METHOD_END, methodVisitor3 -> {
            methodVisitor3.visitIntInsn(25, 0);
            methodVisitor3.visitIntInsn(25, 0);
            methodVisitor3.visitFieldInsn(180, getInternalClassName(), FIELD_START_TIME, "J");
            ASMToolkit.invokeStatic(methodVisitor3, TYPE_EVENT_HANDLER.getInternalName(), METHOD_DURATION);
            methodVisitor3.visitFieldInsn(181, getInternalClassName(), "duration", "J");
            methodVisitor3.visitInsn(177);
            methodVisitor3.visitMaxs(0, 0);
        });
        updateMethod(METHOD_COMMIT, methodVisitor4 -> {
            methodVisitor4.visitCode();
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitMethodInsn(182, getInternalClassName(), METHOD_IS_ENABLED.getName(), METHOD_IS_ENABLED.getDescriptor(), false);
            Label label = new Label();
            methodVisitor4.visitJumpInsn(154, label);
            methodVisitor4.visitInsn(177);
            methodVisitor4.visitLabel(label);
            methodVisitor4.visitFrame(3, 0, null, 0, null);
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitFieldInsn(180, getInternalClassName(), FIELD_START_TIME, "J");
            methodVisitor4.visitInsn(9);
            methodVisitor4.visitInsn(148);
            Label label2 = new Label();
            methodVisitor4.visitJumpInsn(154, label2);
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitMethodInsn(184, TYPE_EVENT_HANDLER.getInternalName(), METHOD_TIME_STAMP.getName(), METHOD_TIME_STAMP.getDescriptor(), false);
            methodVisitor4.visitFieldInsn(181, getInternalClassName(), FIELD_START_TIME, "J");
            Label label3 = new Label();
            methodVisitor4.visitJumpInsn(167, label3);
            methodVisitor4.visitLabel(label2);
            methodVisitor4.visitFrame(3, 0, null, 0, null);
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitFieldInsn(180, getInternalClassName(), "duration", "J");
            methodVisitor4.visitInsn(9);
            methodVisitor4.visitInsn(148);
            methodVisitor4.visitJumpInsn(154, label3);
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitMethodInsn(184, TYPE_EVENT_HANDLER.getInternalName(), METHOD_TIME_STAMP.getName(), METHOD_TIME_STAMP.getDescriptor(), false);
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitFieldInsn(180, getInternalClassName(), FIELD_START_TIME, "J");
            methodVisitor4.visitInsn(101);
            methodVisitor4.visitFieldInsn(181, getInternalClassName(), "duration", "J");
            methodVisitor4.visitLabel(label3);
            methodVisitor4.visitFrame(3, 0, null, 0, null);
            methodVisitor4.visitVarInsn(25, 0);
            methodVisitor4.visitMethodInsn(182, getInternalClassName(), METHOD_EVENT_SHOULD_COMMIT.getName(), METHOD_EVENT_SHOULD_COMMIT.getDescriptor(), false);
            Label label4 = new Label();
            methodVisitor4.visitJumpInsn(153, label4);
            methodVisitor4.visitFieldInsn(178, getInternalClassName(), FIELD_EVENT_HANDLER, jdk.internal.org.objectweb.asm.Type.getDescriptor(eventHandlerProxy));
            methodVisitor4.visitTypeInsn(192, this.eventHandlerXInternalName);
            for (FieldInfo fieldInfo : this.fieldInfos) {
                methodVisitor4.visitVarInsn(25, 0);
                methodVisitor4.visitFieldInsn(180, fieldInfo.internalClassName, fieldInfo.fieldName, fieldInfo.fieldDescriptor);
            }
            methodVisitor4.visitMethodInsn(182, this.eventHandlerXInternalName, this.writeMethod.getName(), this.writeMethod.getDescriptor(), false);
            methodVisitor4.visitLabel(label4);
            methodVisitor4.visitFrame(3, 0, null, 0, null);
            methodVisitor4.visitInsn(177);
            methodVisitor4.visitEnd();
        });
        updateMethod(METHOD_EVENT_SHOULD_COMMIT, methodVisitor5 -> {
            Label label = new Label();
            methodVisitor5.visitFieldInsn(178, getInternalClassName(), FIELD_EVENT_HANDLER, jdk.internal.org.objectweb.asm.Type.getDescriptor(eventHandlerProxy));
            methodVisitor5.visitVarInsn(25, 0);
            methodVisitor5.visitFieldInsn(180, getInternalClassName(), "duration", "J");
            ASMToolkit.invokeVirtual(methodVisitor5, TYPE_EVENT_HANDLER.getInternalName(), METHOD_EVENT_HANDLER_SHOULD_COMMIT);
            methodVisitor5.visitJumpInsn(153, label);
            for (SettingInfo settingInfo : this.settingInfos) {
                methodVisitor5.visitIntInsn(25, 0);
                methodVisitor5.visitFieldInsn(178, getInternalClassName(), FIELD_EVENT_HANDLER, jdk.internal.org.objectweb.asm.Type.getDescriptor(eventHandlerProxy));
                methodVisitor5.visitTypeInsn(192, this.eventHandlerXInternalName);
                methodVisitor5.visitFieldInsn(180, this.eventHandlerXInternalName, settingInfo.fieldName, TYPE_SETTING_CONTROL.getDescriptor());
                methodVisitor5.visitTypeInsn(192, settingInfo.internalSettingName);
                methodVisitor5.visitMethodInsn(182, getInternalClassName(), settingInfo.methodName, "(" + settingInfo.settingDescriptor + ")Z", false);
                methodVisitor5.visitJumpInsn(153, label);
            }
            methodVisitor5.visitInsn(4);
            methodVisitor5.visitInsn(172);
            methodVisitor5.visitLabel(label);
            methodVisitor5.visitInsn(3);
            methodVisitor5.visitInsn(172);
        });
    }

    private void makeUninstrumented() {
        updateExistingWithReturnFalse(METHOD_EVENT_SHOULD_COMMIT);
        updateExistingWithReturnFalse(METHOD_IS_ENABLED);
        updateExistingWithEmptyVoidMethod(METHOD_COMMIT);
        updateExistingWithEmptyVoidMethod(METHOD_BEGIN);
        updateExistingWithEmptyVoidMethod(METHOD_END);
    }

    private final void updateExistingWithEmptyVoidMethod(Method method) {
        updateMethod(method, methodVisitor -> {
            methodVisitor.visitInsn(177);
        });
    }

    private final void updateExistingWithReturnFalse(Method method) {
        updateMethod(method, methodVisitor -> {
            methodVisitor.visitInsn(3);
            methodVisitor.visitInsn(172);
        });
    }

    private MethodNode getMethodNode(Method method) {
        for (MethodNode methodNode : this.classNode.methods) {
            if (methodNode.name.equals(method.getName()) && methodNode.desc.equals(method.getDescriptor())) {
                return methodNode;
            }
        }
        return null;
    }

    private final void updateMethod(Method method, Consumer<MethodVisitor> consumer) {
        MethodNode methodNode = getMethodNode(method);
        int iIndexOf = this.classNode.methods.indexOf(methodNode);
        this.classNode.methods.remove(methodNode);
        MethodVisitor methodVisitorVisitMethod = this.classNode.visitMethod(methodNode.access, methodNode.name, methodNode.desc, null, null);
        methodVisitorVisitMethod.visitCode();
        consumer.accept(methodVisitorVisitMethod);
        methodVisitorVisitMethod.visitMaxs(0, 0);
        MethodNode methodNode2 = getMethodNode(method);
        this.classNode.methods.remove(methodNode2);
        this.classNode.methods.add(iIndexOf, methodNode2);
    }

    public static Method makeWriteMethod(List<FieldInfo> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        Iterator<FieldInfo> it = list.iterator();
        while (it.hasNext()) {
            sb.append(it.next().fieldDescriptor);
        }
        sb.append(")V");
        return new Method("write", sb.toString());
    }

    private String getInternalClassName() {
        return this.classNode.name;
    }

    public List<SettingInfo> getSettingInfos() {
        return this.settingInfos;
    }

    public List<FieldInfo> getFieldInfos() {
        return this.fieldInfos;
    }

    public String getEventName() {
        return this.eventName;
    }

    public void setGuardHandler(boolean z2) {
        this.guardHandlerReference = z2;
    }
}
