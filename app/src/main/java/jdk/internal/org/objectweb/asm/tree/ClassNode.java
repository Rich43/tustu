package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.TypePath;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/ClassNode.class */
public class ClassNode extends ClassVisitor {
    public int version;
    public int access;
    public String name;
    public String signature;
    public String superName;
    public List<String> interfaces;
    public String sourceFile;
    public String sourceDebug;
    public String outerClass;
    public String outerMethod;
    public String outerMethodDesc;
    public List<AnnotationNode> visibleAnnotations;
    public List<AnnotationNode> invisibleAnnotations;
    public List<TypeAnnotationNode> visibleTypeAnnotations;
    public List<TypeAnnotationNode> invisibleTypeAnnotations;
    public List<Attribute> attrs;
    public List<InnerClassNode> innerClasses;
    public List<FieldNode> fields;
    public List<MethodNode> methods;

    public ClassNode() {
        this(Opcodes.ASM5);
        if (getClass() != ClassNode.class) {
            throw new IllegalStateException();
        }
    }

    public ClassNode(int i2) {
        super(i2);
        this.interfaces = new ArrayList();
        this.innerClasses = new ArrayList();
        this.fields = new ArrayList();
        this.methods = new ArrayList();
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.version = i2;
        this.access = i3;
        this.name = str;
        this.signature = str2;
        this.superName = str3;
        if (strArr != null) {
            this.interfaces.addAll(Arrays.asList(strArr));
        }
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitSource(String str, String str2) {
        this.sourceFile = str;
        this.sourceDebug = str2;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String str, String str2, String str3) {
        this.outerClass = str;
        this.outerMethod = str2;
        this.outerMethodDesc = str3;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
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

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
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

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        if (this.attrs == null) {
            this.attrs = new ArrayList(1);
        }
        this.attrs.add(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        this.innerClasses.add(new InnerClassNode(str, str2, str3, i2));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        FieldNode fieldNode = new FieldNode(i2, str, str2, str3, obj);
        this.fields.add(fieldNode);
        return fieldNode;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        MethodNode methodNode = new MethodNode(i2, str, str2, str3, strArr);
        this.methods.add(methodNode);
        return methodNode;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
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
            Iterator<FieldNode> it = this.fields.iterator();
            while (it.hasNext()) {
                it.next().check(i2);
            }
            Iterator<MethodNode> it2 = this.methods.iterator();
            while (it2.hasNext()) {
                it2.next().check(i2);
            }
        }
    }

    public void accept(ClassVisitor classVisitor) {
        String[] strArr = new String[this.interfaces.size()];
        this.interfaces.toArray(strArr);
        classVisitor.visit(this.version, this.access, this.name, this.signature, this.superName, strArr);
        if (this.sourceFile != null || this.sourceDebug != null) {
            classVisitor.visitSource(this.sourceFile, this.sourceDebug);
        }
        if (this.outerClass != null) {
            classVisitor.visitOuterClass(this.outerClass, this.outerMethod, this.outerMethodDesc);
        }
        int size = this.visibleAnnotations == null ? 0 : this.visibleAnnotations.size();
        for (int i2 = 0; i2 < size; i2++) {
            AnnotationNode annotationNode = this.visibleAnnotations.get(i2);
            annotationNode.accept(classVisitor.visitAnnotation(annotationNode.desc, true));
        }
        int size2 = this.invisibleAnnotations == null ? 0 : this.invisibleAnnotations.size();
        for (int i3 = 0; i3 < size2; i3++) {
            AnnotationNode annotationNode2 = this.invisibleAnnotations.get(i3);
            annotationNode2.accept(classVisitor.visitAnnotation(annotationNode2.desc, false));
        }
        int size3 = this.visibleTypeAnnotations == null ? 0 : this.visibleTypeAnnotations.size();
        for (int i4 = 0; i4 < size3; i4++) {
            TypeAnnotationNode typeAnnotationNode = this.visibleTypeAnnotations.get(i4);
            typeAnnotationNode.accept(classVisitor.visitTypeAnnotation(typeAnnotationNode.typeRef, typeAnnotationNode.typePath, typeAnnotationNode.desc, true));
        }
        int size4 = this.invisibleTypeAnnotations == null ? 0 : this.invisibleTypeAnnotations.size();
        for (int i5 = 0; i5 < size4; i5++) {
            TypeAnnotationNode typeAnnotationNode2 = this.invisibleTypeAnnotations.get(i5);
            typeAnnotationNode2.accept(classVisitor.visitTypeAnnotation(typeAnnotationNode2.typeRef, typeAnnotationNode2.typePath, typeAnnotationNode2.desc, false));
        }
        int size5 = this.attrs == null ? 0 : this.attrs.size();
        for (int i6 = 0; i6 < size5; i6++) {
            classVisitor.visitAttribute(this.attrs.get(i6));
        }
        for (int i7 = 0; i7 < this.innerClasses.size(); i7++) {
            this.innerClasses.get(i7).accept(classVisitor);
        }
        for (int i8 = 0; i8 < this.fields.size(); i8++) {
            this.fields.get(i8).accept(classVisitor);
        }
        for (int i9 = 0; i9 < this.methods.size(); i9++) {
            this.methods.get(i9).accept(classVisitor);
        }
        classVisitor.visitEnd();
    }
}
