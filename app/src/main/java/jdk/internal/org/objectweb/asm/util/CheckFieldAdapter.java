package jdk.internal.org.objectweb.asm.util;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/CheckFieldAdapter.class */
public class CheckFieldAdapter extends FieldVisitor {
    private boolean end;

    public CheckFieldAdapter(FieldVisitor fieldVisitor) {
        this(Opcodes.ASM5, fieldVisitor);
        if (getClass() != CheckFieldAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected CheckFieldAdapter(int i2, FieldVisitor fieldVisitor) {
        super(i2, fieldVisitor);
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        checkEnd();
        CheckMethodAdapter.checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        checkEnd();
        int i3 = i2 >>> 24;
        if (i3 != 19) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i3));
        }
        CheckClassAdapter.checkTypeRefAndPath(i2, typePath);
        CheckMethodAdapter.checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        checkEnd();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitEnd() {
        checkEnd();
        this.end = true;
        super.visitEnd();
    }

    private void checkEnd() {
        if (this.end) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
}
