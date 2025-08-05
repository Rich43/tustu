package jdk.jfr.internal.instrument;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.commons.RemappingMethodAdapter;
import jdk.internal.org.objectweb.asm.commons.SimpleRemapper;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.jfr.internal.LogLevel;
import jdk.jfr.internal.LogTag;
import jdk.jfr.internal.Logger;

@Deprecated
/* loaded from: jfr.jar:jdk/jfr/internal/instrument/JIMethodMergeAdapter.class */
final class JIMethodMergeAdapter extends ClassVisitor {
    private final ClassNode cn;
    private final List<Method> methodFilter;
    private final Map<String, String> typeMap;

    public JIMethodMergeAdapter(ClassVisitor classVisitor, ClassNode classNode, List<Method> list, JITypeMapping[] jITypeMappingArr) {
        super(Opcodes.ASM5, classVisitor);
        this.cn = classNode;
        this.methodFilter = list;
        this.typeMap = new HashMap();
        for (JITypeMapping jITypeMapping : jITypeMappingArr) {
            this.typeMap.put(jITypeMapping.from().replace('.', '/'), jITypeMapping.to().replace('.', '/'));
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        super.visit(i2, i3, str, str2, str3, strArr);
        this.typeMap.put(this.cn.name, str);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        if (methodInFilter(str, str2)) {
            Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.DEBUG, "Deleting " + str + str2);
            return null;
        }
        return super.visitMethod(i2, str, str2, str3, strArr);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        SimpleRemapper simpleRemapper = new SimpleRemapper(this.typeMap);
        for (MethodNode methodNode : this.cn.methods) {
            if (methodInFilter(methodNode.name, methodNode.desc)) {
                Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.DEBUG, "Copying method: " + methodNode.name + methodNode.desc);
                Logger.log(LogTag.JFR_SYSTEM_BYTECODE, LogLevel.DEBUG, "   with mapper: " + ((Object) this.typeMap));
                String[] strArr = new String[methodNode.exceptions.size()];
                methodNode.exceptions.toArray(strArr);
                MethodVisitor methodVisitorVisitMethod = this.cv.visitMethod(methodNode.access, methodNode.name, methodNode.desc, methodNode.signature, strArr);
                methodNode.instructions.resetLabels();
                methodNode.accept(new RemappingMethodAdapter(methodNode.access, methodNode.desc, methodVisitorVisitMethod, simpleRemapper));
            }
        }
        super.visitEnd();
    }

    private boolean methodInFilter(String str, String str2) {
        for (Method method : this.methodFilter) {
            if (method.getName().equals(str) && Type.getMethodDescriptor(method).equals(str2)) {
                return true;
            }
        }
        return false;
    }
}
