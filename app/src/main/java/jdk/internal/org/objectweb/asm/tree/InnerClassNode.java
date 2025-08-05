package jdk.internal.org.objectweb.asm.tree;

import jdk.internal.org.objectweb.asm.ClassVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/tree/InnerClassNode.class */
public class InnerClassNode {
    public String name;
    public String outerName;
    public String innerName;
    public int access;

    public InnerClassNode(String str, String str2, String str3, int i2) {
        this.name = str;
        this.outerName = str2;
        this.innerName = str3;
        this.access = i2;
    }

    public void accept(ClassVisitor classVisitor) {
        classVisitor.visitInnerClass(this.name, this.outerName, this.innerName, this.access);
    }
}
