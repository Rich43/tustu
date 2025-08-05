package jdk.internal.org.objectweb.asm.tree;

import java.util.ArrayList;
import java.util.List;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/AnnotationNode.class */
public class AnnotationNode extends AnnotationVisitor {
    public String desc;
    public List<Object> values;

    public AnnotationNode(String str) {
        this(Opcodes.ASM5, str);
        if (getClass() != AnnotationNode.class) {
            throw new IllegalStateException();
        }
    }

    public AnnotationNode(int i2, String str) {
        super(i2);
        this.desc = str;
    }

    AnnotationNode(List<Object> list) {
        super(Opcodes.ASM5);
        this.values = list;
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visit(String str, Object obj) {
        if (this.values == null) {
            this.values = new ArrayList(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(str);
        }
        this.values.add(obj);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String str, String str2, String str3) {
        if (this.values == null) {
            this.values = new ArrayList(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(str);
        }
        this.values.add(new String[]{str2, str3});
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String str, String str2) {
        if (this.values == null) {
            this.values = new ArrayList(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(str);
        }
        AnnotationNode annotationNode = new AnnotationNode(str2);
        this.values.add(annotationNode);
        return annotationNode;
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String str) {
        if (this.values == null) {
            this.values = new ArrayList(this.desc != null ? 2 : 1);
        }
        if (this.desc != null) {
            this.values.add(str);
        }
        ArrayList arrayList = new ArrayList();
        this.values.add(arrayList);
        return new AnnotationNode(arrayList);
    }

    @Override // jdk.internal.org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
    }

    public void check(int i2) {
    }

    public void accept(AnnotationVisitor annotationVisitor) {
        if (annotationVisitor != null) {
            if (this.values != null) {
                for (int i2 = 0; i2 < this.values.size(); i2 += 2) {
                    accept(annotationVisitor, (String) this.values.get(i2), this.values.get(i2 + 1));
                }
            }
            annotationVisitor.visitEnd();
        }
    }

    static void accept(AnnotationVisitor annotationVisitor, String str, Object obj) {
        if (annotationVisitor != null) {
            if (obj instanceof String[]) {
                String[] strArr = (String[]) obj;
                annotationVisitor.visitEnum(str, strArr[0], strArr[1]);
                return;
            }
            if (obj instanceof AnnotationNode) {
                AnnotationNode annotationNode = (AnnotationNode) obj;
                annotationNode.accept(annotationVisitor.visitAnnotation(str, annotationNode.desc));
                return;
            }
            if (obj instanceof List) {
                AnnotationVisitor annotationVisitorVisitArray = annotationVisitor.visitArray(str);
                if (annotationVisitorVisitArray != null) {
                    List list = (List) obj;
                    for (int i2 = 0; i2 < list.size(); i2++) {
                        accept(annotationVisitorVisitArray, null, list.get(i2));
                    }
                    annotationVisitorVisitArray.visitEnd();
                    return;
                }
                return;
            }
            annotationVisitor.visit(str, obj);
        }
    }
}
