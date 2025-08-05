package jdk.internal.org.objectweb.asm.util;

import java.io.PrintWriter;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/TraceClassVisitor.class */
public final class TraceClassVisitor extends ClassVisitor {
    private final PrintWriter pw;

    /* renamed from: p, reason: collision with root package name */
    public final Printer f12864p;

    public TraceClassVisitor(PrintWriter printWriter) {
        this(null, printWriter);
    }

    public TraceClassVisitor(ClassVisitor classVisitor, PrintWriter printWriter) {
        this(classVisitor, new Textifier(), printWriter);
    }

    public TraceClassVisitor(ClassVisitor classVisitor, Printer printer, PrintWriter printWriter) {
        super(Opcodes.ASM5, classVisitor);
        this.pw = printWriter;
        this.f12864p = printer;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.f12864p.visit(i2, i3, str, str2, str3, strArr);
        super.visit(i2, i3, str, str2, str3, strArr);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitSource(String str, String str2) {
        this.f12864p.visitSource(str, str2);
        super.visitSource(str, str2);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String str, String str2, String str3) {
        this.f12864p.visitOuterClass(str, str2, str3);
        super.visitOuterClass(str, str2, str3);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        return new TraceAnnotationVisitor(this.cv == null ? null : this.cv.visitAnnotation(str, z2), this.f12864p.visitClassAnnotation(str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return new TraceAnnotationVisitor(this.cv == null ? null : this.cv.visitTypeAnnotation(i2, typePath, str, z2), this.f12864p.visitClassTypeAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        this.f12864p.visitClassAttribute(attribute);
        super.visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        this.f12864p.visitInnerClass(str, str2, str3, i2);
        super.visitInnerClass(str, str2, str3, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        return new TraceFieldVisitor(this.cv == null ? null : this.cv.visitField(i2, str, str2, str3, obj), this.f12864p.visitField(i2, str, str2, str3, obj));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        return new TraceMethodVisitor(this.cv == null ? null : this.cv.visitMethod(i2, str, str2, str3, strArr), this.f12864p.visitMethod(i2, str, str2, str3, strArr));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        this.f12864p.visitClassEnd();
        if (this.pw != null) {
            this.f12864p.print(this.pw);
            this.pw.flush();
        }
        super.visitEnd();
    }
}
