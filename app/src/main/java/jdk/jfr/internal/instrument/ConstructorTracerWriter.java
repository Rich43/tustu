package jdk.jfr.internal.instrument;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;

/* loaded from: jfr.jar:jdk/jfr/internal/instrument/ConstructorTracerWriter.class */
final class ConstructorTracerWriter extends ClassVisitor {
    private ConstructorWriter useInputParameter;
    private ConstructorWriter noUseInputParameter;

    static byte[] generateBytes(Class<?> cls, byte[] bArr) throws IOException {
        ClassReader classReader = new ClassReader(new ByteArrayInputStream(bArr));
        ClassWriter classWriter = new ClassWriter(1);
        classReader.accept(new ConstructorTracerWriter(classWriter, cls), 0);
        return classWriter.toByteArray();
    }

    private ConstructorTracerWriter(ClassVisitor classVisitor, Class<?> cls) {
        super(Opcodes.ASM5, classVisitor);
        this.useInputParameter = new ConstructorWriter(cls, true);
        this.noUseInputParameter = new ConstructorWriter(cls, false);
    }

    private boolean isConstructor(String str) {
        return str.equals(Constants.CONSTRUCTOR_NAME);
    }

    private boolean takesStringParameter(String str) {
        Type[] argumentTypes = Type.getArgumentTypes(str);
        if (argumentTypes.length > 0 && argumentTypes[0].getClassName().equals(String.class.getName())) {
            return true;
        }
        return false;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        MethodVisitor methodVisitorVisitMethod = super.visitMethod(i2, str, str2, str3, strArr);
        if (isConstructor(str)) {
            if (takesStringParameter(str2)) {
                this.useInputParameter.setMethodVisitor(methodVisitorVisitMethod);
                return this.useInputParameter;
            }
            this.noUseInputParameter.setMethodVisitor(methodVisitorVisitMethod);
            return this.noUseInputParameter;
        }
        return methodVisitorVisitMethod;
    }
}
