package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/RemappingFieldAdapter.class */
public class RemappingFieldAdapter extends FieldVisitor {
    private final Remapper remapper;

    public RemappingFieldAdapter(FieldVisitor fieldVisitor, Remapper remapper) {
        this(Opcodes.ASM5, fieldVisitor, remapper);
    }

    protected RemappingFieldAdapter(int i2, FieldVisitor fieldVisitor, Remapper remapper) {
        super(i2, fieldVisitor);
        this.remapper = remapper;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitAnnotation = this.fv.visitAnnotation(this.remapper.mapDesc(str), z2);
        if (annotationVisitorVisitAnnotation == null) {
            return null;
        }
        return new RemappingAnnotationAdapter(annotationVisitorVisitAnnotation, this.remapper);
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        AnnotationVisitor annotationVisitorVisitTypeAnnotation = super.visitTypeAnnotation(i2, typePath, this.remapper.mapDesc(str), z2);
        if (annotationVisitorVisitTypeAnnotation == null) {
            return null;
        }
        return new RemappingAnnotationAdapter(annotationVisitorVisitTypeAnnotation, this.remapper);
    }
}
