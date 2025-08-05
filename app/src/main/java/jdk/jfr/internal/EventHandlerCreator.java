package jdk.jfr.internal;

import com.sun.org.apache.bcel.internal.Constants;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.commons.Method;
import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.SettingControl;
import jdk.jfr.ValueDescriptor;
import jdk.jfr.internal.EventInstrumentation;
import jdk.jfr.internal.handlers.EventHandler;
import sun.util.locale.LanguageTag;

/* loaded from: jfr.jar:jdk/jfr/internal/EventHandlerCreator.class */
final class EventHandlerCreator {
    private static final int CLASS_VERSION = 52;
    private static final String FIELD_EVENT_TYPE = "platformEventType";
    private static final String FIELD_PREFIX_STRING_POOL = "stringPool";
    private final ClassWriter classWriter;
    private final String className;
    private final String internalClassName;
    private final List<EventInstrumentation.SettingInfo> settingInfos;
    private final List<EventInstrumentation.FieldInfo> fields;
    private static final String SUFFIX = "_" + System.currentTimeMillis() + LanguageTag.SEP + JVM.getJVM().getPid();
    private static final Class<? extends EventHandler> eventHandlerProxy = EventHandlerProxyCreator.proxyClass;
    private static final jdk.internal.org.objectweb.asm.Type TYPE_STRING_POOL = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) StringPool.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_WRITER = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventWriter.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_PLATFORM_EVENT_TYPE = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) PlatformEventType.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_HANDLER = jdk.internal.org.objectweb.asm.Type.getType(eventHandlerProxy);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_SETTING_CONTROL = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) SettingControl.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_TYPE = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventType.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_CONTROL = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventControl.class);
    private static final String DESCRIPTOR_EVENT_HANDLER = "(" + jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE.getDescriptor() + TYPE_EVENT_TYPE.getDescriptor() + TYPE_EVENT_CONTROL.getDescriptor() + ")V";
    private static final Method METHOD_GET_EVENT_WRITER = new Method("getEventWriter", "()" + TYPE_EVENT_WRITER.getDescriptor());
    private static final Method METHOD_EVENT_HANDLER_CONSTRUCTOR = new Method(Constants.CONSTRUCTOR_NAME, DESCRIPTOR_EVENT_HANDLER);
    private static final Method METHOD_RESET = new Method(com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.RESET, "()V");

    public EventHandlerCreator(long j2, List<EventInstrumentation.SettingInfo> list, List<EventInstrumentation.FieldInfo> list2) {
        this.classWriter = new ClassWriter(3);
        this.className = makeEventHandlerName(j2);
        this.internalClassName = ASMToolkit.getInternalName(this.className);
        this.settingInfos = list;
        this.fields = list2;
    }

    public static String makeEventHandlerName(long j2) {
        return eventHandlerProxy.getName() + j2 + SUFFIX;
    }

    public EventHandlerCreator(long j2, List<EventInstrumentation.SettingInfo> list, EventType eventType, Class<? extends Event> cls) {
        this(j2, list, createFieldInfos(cls, eventType));
    }

    private static List<EventInstrumentation.FieldInfo> createFieldInfos(Class<? extends Event> cls, EventType eventType) throws Error {
        Field declaredField;
        ArrayList arrayList = new ArrayList();
        for (ValueDescriptor valueDescriptor : eventType.getFields()) {
            if (valueDescriptor != TypeLibrary.STACK_TRACE_FIELD && valueDescriptor != TypeLibrary.THREAD_FIELD) {
                String fieldName = PrivateAccess.getInstance().getFieldName(valueDescriptor);
                String descriptor = ASMToolkit.getDescriptor(valueDescriptor.getTypeName());
                String internalName = null;
                for (Class<? extends Event> superclass = cls; superclass != Event.class; superclass = superclass.getSuperclass()) {
                    try {
                        declaredField = superclass.getDeclaredField(fieldName);
                    } catch (NoSuchFieldException | SecurityException e2) {
                    }
                    if (superclass == cls || !Modifier.isPrivate(declaredField.getModifiers())) {
                        internalName = ASMToolkit.getInternalName(superclass.getName());
                        break;
                    }
                }
                if (internalName != null) {
                    arrayList.add(new EventInstrumentation.FieldInfo(fieldName, descriptor, internalName));
                } else {
                    throw new InternalError("Could not locate field " + fieldName + " for event type" + eventType.getName());
                }
            }
        }
        return arrayList;
    }

    public Class<? extends EventHandler> makeEventHandlerClass() {
        buildClassInfo();
        buildConstructor();
        buildWriteMethod();
        byte[] byteArray = this.classWriter.toByteArray();
        ASMToolkit.logASM(this.className, byteArray);
        return SecuritySupport.defineClass(this.className, byteArray, Event.class.getClassLoader()).asSubclass(EventHandler.class);
    }

    public static EventHandler instantiateEventHandler(Class<? extends EventHandler> cls, boolean z2, EventType eventType, EventControl eventControl) throws Error {
        try {
            Constructor<?> constructor = cls.getDeclaredConstructors()[0];
            SecuritySupport.setAccessible(constructor);
            try {
                List<EventInstrumentation.SettingInfo> settingInfos = eventControl.getSettingInfos();
                Object[] objArr = new Object[3 + settingInfos.size()];
                objArr[0] = Boolean.valueOf(z2);
                objArr[1] = eventType;
                objArr[2] = eventControl;
                for (EventInstrumentation.SettingInfo settingInfo : settingInfos) {
                    objArr[settingInfo.index + 3] = settingInfo.settingControl;
                }
                return (EventHandler) constructor.newInstance(objArr);
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e2) {
                throw ((Error) new InternalError("Could not instantiate event handler for " + eventType.getName() + ". " + e2.getMessage()).initCause(e2));
            }
        } catch (Exception e3) {
            throw ((Error) new InternalError("Could not get handler constructor for " + eventType.getName()).initCause(e3));
        }
    }

    private void buildConstructor() {
        MethodVisitor methodVisitorVisitMethod = this.classWriter.visitMethod(2, METHOD_EVENT_HANDLER_CONSTRUCTOR.getName(), makeConstructorDescriptor(this.settingInfos), null, null);
        methodVisitorVisitMethod.visitVarInsn(25, 0);
        methodVisitorVisitMethod.visitVarInsn(21, 1);
        methodVisitorVisitMethod.visitVarInsn(25, 2);
        methodVisitorVisitMethod.visitVarInsn(25, 3);
        methodVisitorVisitMethod.visitMethodInsn(183, jdk.internal.org.objectweb.asm.Type.getInternalName(eventHandlerProxy), METHOD_EVENT_HANDLER_CONSTRUCTOR.getName(), METHOD_EVENT_HANDLER_CONSTRUCTOR.getDescriptor(), false);
        for (EventInstrumentation.SettingInfo settingInfo : this.settingInfos) {
            methodVisitorVisitMethod.visitVarInsn(25, 0);
            methodVisitorVisitMethod.visitVarInsn(25, settingInfo.index + 4);
            methodVisitorVisitMethod.visitFieldInsn(181, this.internalClassName, settingInfo.fieldName, TYPE_SETTING_CONTROL.getDescriptor());
        }
        int i2 = 0;
        Iterator<EventInstrumentation.FieldInfo> it = this.fields.iterator();
        while (it.hasNext()) {
            if (it.next().isString()) {
                methodVisitorVisitMethod.visitVarInsn(25, 0);
                methodVisitorVisitMethod.visitVarInsn(25, 0);
                methodVisitorVisitMethod.visitMethodInsn(182, jdk.internal.org.objectweb.asm.Type.getInternalName(eventHandlerProxy), "createStringFieldWriter", "()" + TYPE_STRING_POOL.getDescriptor(), false);
                methodVisitorVisitMethod.visitFieldInsn(181, this.internalClassName, FIELD_PREFIX_STRING_POOL + i2, TYPE_STRING_POOL.getDescriptor());
            }
            i2++;
        }
        methodVisitorVisitMethod.visitInsn(177);
        methodVisitorVisitMethod.visitMaxs(0, 0);
        methodVisitorVisitMethod.visitEnd();
    }

    private void buildClassInfo() {
        this.classWriter.visit(52, 49, this.internalClassName, null, ASMToolkit.getInternalName(eventHandlerProxy.getName()), null);
        Iterator<EventInstrumentation.SettingInfo> it = this.settingInfos.iterator();
        while (it.hasNext()) {
            this.classWriter.visitField(17, it.next().fieldName, TYPE_SETTING_CONTROL.getDescriptor(), null, null);
        }
        int i2 = 0;
        Iterator<EventInstrumentation.FieldInfo> it2 = this.fields.iterator();
        while (it2.hasNext()) {
            if (it2.next().isString()) {
                this.classWriter.visitField(18, FIELD_PREFIX_STRING_POOL + i2, TYPE_STRING_POOL.getDescriptor(), null, null);
            }
            i2++;
        }
    }

    private void visitMethod(MethodVisitor methodVisitor, int i2, jdk.internal.org.objectweb.asm.Type type, Method method) {
        methodVisitor.visitMethodInsn(i2, type.getInternalName(), method.getName(), method.getDescriptor(), false);
    }

    private void buildWriteMethod() {
        Method methodMakeWriteMethod = ASMToolkit.makeWriteMethod(this.fields);
        jdk.internal.org.objectweb.asm.Type[] argumentTypes = jdk.internal.org.objectweb.asm.Type.getArgumentTypes(methodMakeWriteMethod.getDescriptor());
        MethodVisitor methodVisitorVisitMethod = this.classWriter.visitMethod(1, methodMakeWriteMethod.getName(), methodMakeWriteMethod.getDescriptor(), null, null);
        methodVisitorVisitMethod.visitCode();
        Label label = new Label();
        Label label2 = new Label();
        Label label3 = new Label();
        methodVisitorVisitMethod.visitTryCatchBlock(label, label2, label3, "java/lang/Throwable");
        methodVisitorVisitMethod.visitLabel(label);
        visitMethod(methodVisitorVisitMethod, 184, TYPE_EVENT_WRITER, METHOD_GET_EVENT_WRITER);
        methodVisitorVisitMethod.visitInsn(89);
        methodVisitorVisitMethod.visitVarInsn(25, 0);
        methodVisitorVisitMethod.visitFieldInsn(180, TYPE_EVENT_HANDLER.getInternalName(), FIELD_EVENT_TYPE, TYPE_PLATFORM_EVENT_TYPE.getDescriptor());
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.BEGIN_EVENT.asASM());
        Label label4 = new Label();
        methodVisitorVisitMethod.visitJumpInsn(153, label4);
        methodVisitorVisitMethod.visitInsn(89);
        methodVisitorVisitMethod.visitVarInsn(argumentTypes[0].getOpcode(21), 1);
        int i2 = 0 + 1;
        int size = 1 + argumentTypes[0].getSize();
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.PUT_LONG.asASM());
        methodVisitorVisitMethod.visitInsn(89);
        methodVisitorVisitMethod.visitVarInsn(argumentTypes[i2].getOpcode(21), size);
        int i3 = i2 + 1;
        int size2 = size + argumentTypes[i2].getSize();
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.PUT_LONG.asASM());
        methodVisitorVisitMethod.visitInsn(89);
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.PUT_EVENT_THREAD.asASM());
        methodVisitorVisitMethod.visitInsn(89);
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.PUT_STACK_TRACE.asASM());
        for (int i4 = 0 + 1 + 1; i4 < this.fields.size(); i4++) {
            methodVisitorVisitMethod.visitInsn(89);
            methodVisitorVisitMethod.visitVarInsn(argumentTypes[i3].getOpcode(21), size2);
            int i5 = i3;
            i3++;
            size2 += argumentTypes[i5].getSize();
            EventInstrumentation.FieldInfo fieldInfo = this.fields.get(i4);
            if (fieldInfo.isString()) {
                methodVisitorVisitMethod.visitVarInsn(25, 0);
                methodVisitorVisitMethod.visitFieldInsn(180, this.internalClassName, FIELD_PREFIX_STRING_POOL + i4, TYPE_STRING_POOL.getDescriptor());
            }
            visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.lookupMethod(fieldInfo).asASM());
        }
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, EventWriterMethod.END_EVENT.asASM());
        methodVisitorVisitMethod.visitJumpInsn(153, label);
        methodVisitorVisitMethod.visitLabel(label2);
        Label label5 = new Label();
        methodVisitorVisitMethod.visitJumpInsn(167, label5);
        methodVisitorVisitMethod.visitLabel(label3);
        methodVisitorVisitMethod.visitFrame(4, 0, null, 1, new Object[]{"java/lang/Throwable"});
        visitMethod(methodVisitorVisitMethod, 184, TYPE_EVENT_WRITER, METHOD_GET_EVENT_WRITER);
        methodVisitorVisitMethod.visitInsn(89);
        Label label6 = new Label();
        methodVisitorVisitMethod.visitJumpInsn(198, label6);
        methodVisitorVisitMethod.visitInsn(89);
        visitMethod(methodVisitorVisitMethod, 182, TYPE_EVENT_WRITER, METHOD_RESET);
        methodVisitorVisitMethod.visitLabel(label6);
        methodVisitorVisitMethod.visitFrame(3, 0, null, 2, new Object[]{"java/lang/Throwable", TYPE_EVENT_WRITER.getInternalName()});
        methodVisitorVisitMethod.visitInsn(87);
        methodVisitorVisitMethod.visitInsn(191);
        methodVisitorVisitMethod.visitLabel(label4);
        methodVisitorVisitMethod.visitFrame(3, 0, null, 1, new Object[]{TYPE_EVENT_WRITER.getInternalName()});
        methodVisitorVisitMethod.visitInsn(87);
        methodVisitorVisitMethod.visitLabel(label5);
        methodVisitorVisitMethod.visitFrame(3, 0, null, 0, null);
        methodVisitorVisitMethod.visitInsn(177);
        methodVisitorVisitMethod.visitMaxs(0, 0);
        methodVisitorVisitMethod.visitEnd();
    }

    private static String makeConstructorDescriptor(List<EventInstrumentation.SettingInfo> list) {
        StringJoiner stringJoiner = new StringJoiner("", "(", ")V");
        stringJoiner.add(jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE.getDescriptor());
        stringJoiner.add(jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventType.class).getDescriptor());
        stringJoiner.add(jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventControl.class).getDescriptor());
        for (int i2 = 0; i2 < list.size(); i2++) {
            stringJoiner.add(TYPE_SETTING_CONTROL.getDescriptor());
        }
        return stringJoiner.toString();
    }
}
