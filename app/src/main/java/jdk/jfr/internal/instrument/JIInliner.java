package jdk.jfr.internal.instrument;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;

@Deprecated
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIInliner.class */
final class JIInliner extends ClassVisitor {
    private final String targetClassName;
    private final String instrumentationClassName;
    private final ClassNode targetClassNode;
    private final List<Method> instrumentationMethods;

    JIInliner(int i2, ClassVisitor classVisitor, String str, String str2, ClassReader classReader, List<Method> list) {
        super(i2, classVisitor);
        this.targetClassName = str;
        this.instrumentationClassName = str2;
        this.instrumentationMethods = list;
        ClassNode classNode = new ClassNode(Opcodes.ASM5);
        classReader.accept(classNode, 8);
        this.targetClassNode = classNode;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        MethodVisitor methodVisitorVisitMethod = super.visitMethod(i2, str, str2, str3, strArr);
        if (isInstrumentationMethod(str, str2)) {
            MethodNode methodNodeFindTargetMethodNode = findTargetMethodNode(str, str2);
            if (methodNodeFindTargetMethodNode == null) {
                throw new IllegalArgumentException("Could not find the method to instrument in the target class");
            }
            if (Modifier.isNative(methodNodeFindTargetMethodNode.access)) {
                throw new IllegalArgumentException("Cannot instrument native methods: " + this.targetClassNode.name + "." + methodNodeFindTargetMethodNode.name + methodNodeFindTargetMethodNode.desc);
            }
            Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.DEBUG, "Inliner processing method " + str + str2);
            return new JIMethodCallInliner(i2, str2, methodVisitorVisitMethod, methodNodeFindTargetMethodNode, this.targetClassName, this.instrumentationClassName);
        }
        return methodVisitorVisitMethod;
    }

    private boolean isInstrumentationMethod(String str, String str2) {
        for (Method method : this.instrumentationMethods) {
            if (method.getName().equals(str) && Type.getMethodDescriptor(method).equals(str2)) {
                return true;
            }
        }
        return false;
    }

    private MethodNode findTargetMethodNode(String str, String str2) {
        for (MethodNode methodNode : this.targetClassNode.methods) {
            if (methodNode.desc.equals(str2) && methodNode.name.equals(str)) {
                return methodNode;
            }
        }
        throw new IllegalArgumentException("could not find MethodNode for " + str + str2);
    }
}
