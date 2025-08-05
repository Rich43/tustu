package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.List;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/FieldNode.class */
public class FieldNode extends FieldVisitor {
    public int access;
    public String name;
    public String desc;
    public String signature;
    public Object value;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;

    public FieldNode(int i2, String str, String str2, String str3, Object obj) {
        this(Opcodes.ASM5, i2, str, str2, str3, obj);
        if (getClass() != FieldNode.class) {
            throw new IllegalStateException();
        }
    }

    public FieldNode(int i2, int i3, String str, String str2, String str3, Object obj) {
        super(i2);
        this.access = i3;
        this.name = str;
        this.desc = str2;
        this.signature = str3;
        this.value = obj;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        AnnotationNode annotationNode = new AnnotationNode(str);
        if (z2) {
            if (this.visibleAnnotations == null) {
                this.visibleAnnotations = new ArrayList(1);
            }
            this.visibleAnnotations.add(annotationNode);
        } else {
            if (this.invisibleAnnotations == null) {
                this.invisibleAnnotations = new ArrayList(1);
            }
            this.invisibleAnnotations.add(annotationNode);
        }
        return annotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        TypeAnnotationNode typeAnnotationNode = new TypeAnnotationNode(i2, typePath, str);
        if (z2) {
            if (this.visibleTypeAnnotations == null) {
                this.visibleTypeAnnotations = new ArrayList(1);
            }
            this.visibleTypeAnnotations.add(typeAnnotationNode);
        } else {
            if (this.invisibleTypeAnnotations == null) {
                this.invisibleTypeAnnotations = new ArrayList(1);
            }
            this.invisibleTypeAnnotations.add(typeAnnotationNode);
        }
        return typeAnnotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = new ArrayList(1);
        }
        this.attrs.add(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.FieldVisitor
    public void visitEnd() {
    }

    public void check(int i2) {
        if (i2 == 262144) {
            if (this.visibleTypeAnnotations != null && this.visibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }
            if (this.invisibleTypeAnnotations != null && this.invisibleTypeAnnotations.size() > 0) {
                throw new RuntimeException();
            }
        }
    }

    public void accept(ClassVisitor classVisitor) {
        FieldVisitor fieldVisitorVisitField = classVisitor.visitField(this.access, this.name, this.desc, this.signature, this.value);
        if (fieldVisitorVisitField == null) {
            return;
        }
        int size = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
        for (int i2 = 0; i2 < size; i2++) {
            AnnotationNode annotationNode = this.visibleAnnotations.get(i2);
            annotationNode.accept(fieldVisitorVisitField.visitAnnotation(annotationNode.desc, true));
        }
        int size2 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
        for (int i3 = 0; i3 < size2; i3++) {
            AnnotationNode annotationNode2 = this.invisibleAnnotations.get(i3);
            annotationNode2.accept(fieldVisitorVisitField.visitAnnotation(annotationNode2.desc, false));
        }
        int size3 = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();
        for (int i4 = 0; i4 < size3; i4++) {
            TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i4);
            typeAnnotationNode.accept(fieldVisitorVisitField.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size4 = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();
        for (int i5 = 0; i5 < size4; i5++) {
            TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(i5);
            typeAnnotationNode2.accept(fieldVisitorVisitField.visitTypeAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
        int size5 = this.attrs == null ? 0 : this.attrs.size();
        for (int i6 = 0; i6 < size5; i6++) {
            fieldVisitorVisitField.visitAttribute(this.attrs.get(i6));
        }
        fieldVisitorVisitField.visitEnd();
    }
}
