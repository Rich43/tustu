package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/RemappingAnnotationAdapter.class */
public class RemappingAnnotationAdapter extends AnnotationVisitor {
    protected final Remapper remapper;

    public RemappingAnnotationAdapter(AnnotationVisitor annotationVisitor, Remapper remapper) {
        this(Opcodes.ASM5, annotationVisitor, remapper);
    }

    protected RemappingAnnotationAdapter(int i2, AnnotationVisitor annotationVisitor, Remapper remapper) {
        super(i2, annotationVisitor);
        this.remapper = remapper;
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        this.f12857av.visit(str, this.remapper.mapValue(obj));
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        this.f12857av.visitEnum(str, this.remapper.mapDesc(str2), str3);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        AnnotationVisitor annotationVisitorVisitAnnotation = this.f12857av.visitAnnotation(str, this.remapper.mapDesc(str2));
        if (annotationVisitorVisitAnnotation == null) {
            return null;
        }
        return annotationVisitorVisitAnnotation == this.f12857av ? this : new RemappingAnnotationAdapter(annotationVisitorVisitAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        AnnotationVisitor annotationVisitorVisitArray = this.f12857av.visitArray(str);
        if (annotationVisitorVisitArray == null) {
            return null;
        }
        return annotationVisitorVisitArray == this.f12857av ? this : new RemappingAnnotationAdapter(annotationVisitorVisitArray, this.remapper);
    }
}
