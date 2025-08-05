package jdk.internal.org.objectweb.asm.commons;

import com.sun.org.apache.bcel.internal.Constants;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/StaticInitMerger.class */
public class StaticInitMerger extends ClassVisitor {
    private String name;
    private MethodVisitor clinit;
    private final String prefix;
    private int counter;

    public StaticInitMerger(String str, ClassVisitor classVisitor) {
        this(Opcodes.ASM5, str, classVisitor);
    }

    protected StaticInitMerger(int i2, String str, ClassVisitor classVisitor) {
        super(i2, classVisitor);
        this.prefix = str;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.cv.visit(i2, i3, str, str2, str3, strArr);
        this.name = str;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        MethodVisitor methodVisitorVisitMethod;
        if (Constants.STATIC_INITIALIZER_NAME.equals(str)) {
            StringBuilder sbAppend = new StringBuilder().append(this.prefix);
            int i3 = this.counter;
            this.counter = i3 + 1;
            String string = sbAppend.append(i3).toString();
            methodVisitorVisitMethod = this.cv.visitMethod(10, string, str2, str3, strArr);
            if (this.clinit == null) {
                this.clinit = this.cv.visitMethod(10, str, str2, null, null);
            }
            this.clinit.visitMethodInsn(184, this.name, string, str2, false);
        } else {
            methodVisitorVisitMethod = this.cv.visitMethod(i2, str, str2, str3, strArr);
        }
        return methodVisitorVisitMethod;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        if (this.clinit != null) {
            this.clinit.visitInsn(177);
            this.clinit.visitMaxs(0, 0);
        }
        this.cv.visitEnd();
    }
}
