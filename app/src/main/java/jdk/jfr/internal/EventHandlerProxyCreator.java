package jdk.jfr.internal;

import com.sun.org.apache.bcel.internal.Constants;
import java.util.StringJoiner;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.commons.Method;
import jdk.jfr.Event;
import jdk.jfr.EventType;
import jdk.jfr.internal.handlers.EventHandler;

/* loaded from: jfr.jar:jdk/jfr/internal/EventHandlerProxyCreator.class */
final class EventHandlerProxyCreator {
    private static final int CLASS_VERSION = 52;
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_TYPE = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventType.class);
    private static final jdk.internal.org.objectweb.asm.Type TYPE_EVENT_CONTROL = jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventControl.class);
    private static final String DESCRIPTOR_EVENT_HANDLER = "(" + jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE.getDescriptor() + TYPE_EVENT_TYPE.getDescriptor() + TYPE_EVENT_CONTROL.getDescriptor() + ")V";
    private static final Method METHOD_EVENT_HANDLER_CONSTRUCTOR = new Method(Constants.CONSTRUCTOR_NAME, DESCRIPTOR_EVENT_HANDLER);
    private static final String DESCRIPTOR_TIME_STAMP = "()" + jdk.internal.org.objectweb.asm.Type.LONG_TYPE.getDescriptor();
    private static final Method METHOD_TIME_STAMP = new Method("timestamp", DESCRIPTOR_TIME_STAMP);
    private static final String DESCRIPTOR_DURATION = "(" + jdk.internal.org.objectweb.asm.Type.LONG_TYPE.getDescriptor() + ")" + jdk.internal.org.objectweb.asm.Type.LONG_TYPE.getDescriptor();
    private static final Method METHOD_DURATION = new Method("duration", DESCRIPTOR_DURATION);
    private static final ClassWriter classWriter = new ClassWriter(3);
    private static final String className = "jdk.jfr.proxy.internal.EventHandlerProxy";
    private static final String internalClassName = ASMToolkit.getInternalName(className);
    static final Class<? extends EventHandler> proxyClass = makeEventHandlerProxyClass();

    EventHandlerProxyCreator() {
    }

    static void ensureInitialized() {
    }

    public static Class<? extends EventHandler> makeEventHandlerProxyClass() {
        buildClassInfo();
        buildConstructor();
        buildTimestampMethod();
        buildDurationMethod();
        byte[] byteArray = classWriter.toByteArray();
        ASMToolkit.logASM(className, byteArray);
        return SecuritySupport.defineClass(className, byteArray, Event.class.getClassLoader()).asSubclass(EventHandler.class);
    }

    private static void buildConstructor() {
        MethodVisitor methodVisitorVisitMethod = classWriter.visitMethod(0, METHOD_EVENT_HANDLER_CONSTRUCTOR.getName(), makeConstructorDescriptor(), null, null);
        methodVisitorVisitMethod.visitVarInsn(25, 0);
        methodVisitorVisitMethod.visitVarInsn(21, 1);
        methodVisitorVisitMethod.visitVarInsn(25, 2);
        methodVisitorVisitMethod.visitVarInsn(25, 3);
        methodVisitorVisitMethod.visitMethodInsn(183, jdk.internal.org.objectweb.asm.Type.getInternalName(EventHandler.class), METHOD_EVENT_HANDLER_CONSTRUCTOR.getName(), METHOD_EVENT_HANDLER_CONSTRUCTOR.getDescriptor(), false);
        methodVisitorVisitMethod.visitInsn(177);
        methodVisitorVisitMethod.visitMaxs(0, 0);
        methodVisitorVisitMethod.visitEnd();
    }

    private static void buildClassInfo() {
        classWriter.visit(52, 1057, internalClassName, null, ASMToolkit.getInternalName(EventHandler.class.getName()), null);
    }

    private static void buildTimestampMethod() {
        MethodVisitor methodVisitorVisitMethod = classWriter.visitMethod(9, METHOD_TIME_STAMP.getName(), METHOD_TIME_STAMP.getDescriptor(), null, null);
        methodVisitorVisitMethod.visitCode();
        methodVisitorVisitMethod.visitMethodInsn(184, jdk.internal.org.objectweb.asm.Type.getInternalName(EventHandler.class), METHOD_TIME_STAMP.getName(), METHOD_TIME_STAMP.getDescriptor(), false);
        methodVisitorVisitMethod.visitInsn(173);
        methodVisitorVisitMethod.visitMaxs(0, 0);
        methodVisitorVisitMethod.visitEnd();
    }

    private static void buildDurationMethod() {
        MethodVisitor methodVisitorVisitMethod = classWriter.visitMethod(9, METHOD_DURATION.getName(), METHOD_DURATION.getDescriptor(), null, null);
        methodVisitorVisitMethod.visitCode();
        methodVisitorVisitMethod.visitVarInsn(22, 0);
        methodVisitorVisitMethod.visitMethodInsn(184, jdk.internal.org.objectweb.asm.Type.getInternalName(EventHandler.class), METHOD_DURATION.getName(), METHOD_DURATION.getDescriptor(), false);
        methodVisitorVisitMethod.visitInsn(173);
        methodVisitorVisitMethod.visitMaxs(0, 0);
        methodVisitorVisitMethod.visitEnd();
    }

    private static String makeConstructorDescriptor() {
        StringJoiner stringJoiner = new StringJoiner("", "(", ")V");
        stringJoiner.add(jdk.internal.org.objectweb.asm.Type.BOOLEAN_TYPE.getDescriptor());
        stringJoiner.add(jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventType.class).getDescriptor());
        stringJoiner.add(jdk.internal.org.objectweb.asm.Type.getType((Class<?>) EventControl.class).getDescriptor());
        return stringJoiner.toString();
    }
}
