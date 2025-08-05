package jdk.internal.org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXMLLoader;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.TypePath;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/ASMifier.class */
public class ASMifier extends Printer {
    protected final String name;
    protected final int id;
    protected Map<Label, String> labelNames;
    private static final int ACCESS_CLASS = 262144;
    private static final int ACCESS_FIELD = 524288;
    private static final int ACCESS_INNER = 1048576;

    public ASMifier() {
        this(Opcodes.ASM5, "cw", 0);
        if (getClass() != ASMifier.class) {
            throw new IllegalStateException();
        }
    }

    protected ASMifier(int i2, String str, int i3) {
        super(i2);
        this.name = str;
        this.id = i3;
    }

    public static void main(String[] strArr) throws Exception {
        ClassReader classReader;
        boolean z2 = false;
        int i2 = 2;
        boolean z3 = true;
        if (strArr.length < 1 || strArr.length > 2) {
            z3 = false;
        }
        boolean z4 = z3;
        boolean z5 = z2;
        boolean z6 = z3;
        if (z4) {
            z5 = z2;
            z6 = z3;
            if ("-debug".equals(strArr[0])) {
                boolean z7 = true;
                i2 = 0;
                z5 = z7;
                z6 = z3;
                if (strArr.length != 2) {
                    z6 = false;
                    z5 = z7;
                }
            }
        }
        if (!z6) {
            System.err.println("Prints the ASM code to generate the given class.");
            System.err.println("Usage: ASMifier [-debug] <fully qualified class name or class file name>");
            return;
        }
        if (strArr[z5 ? 1 : 0].endsWith(".class") || strArr[z5 ? 1 : 0].indexOf(92) > -1 || strArr[z5 ? 1 : 0].indexOf(47) > -1) {
            classReader = new ClassReader(new FileInputStream(strArr[z5 ? 1 : 0]));
        } else {
            classReader = new ClassReader(strArr[z5 ? 1 : 0]);
        }
        classReader.accept(new TraceClassVisitor(null, new ASMifier(), new PrintWriter(System.out)), i2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        String strSubstring;
        int iLastIndexOf = str.lastIndexOf(47);
        if (iLastIndexOf == -1) {
            strSubstring = str;
        } else {
            this.text.add("package asm." + str.substring(0, iLastIndexOf).replace('/', '.') + ";\n");
            strSubstring = str.substring(iLastIndexOf + 1);
        }
        this.text.add("import java.util.*;\n");
        this.text.add("import jdk.internal.org.objectweb.asm.*;\n");
        this.text.add("public class " + strSubstring + "Dump implements Opcodes {\n\n");
        this.text.add("public static byte[] dump () throws Exception {\n\n");
        this.text.add("ClassWriter cw = new ClassWriter(0);\n");
        this.text.add("FieldVisitor fv;\n");
        this.text.add("MethodVisitor mv;\n");
        this.text.add("AnnotationVisitor av0;\n\n");
        this.buf.setLength(0);
        this.buf.append("cw.visit(");
        switch (i2) {
            case 46:
                this.buf.append("V1_2");
                break;
            case 47:
                this.buf.append("V1_3");
                break;
            case 48:
                this.buf.append("V1_4");
                break;
            case 49:
                this.buf.append("V1_5");
                break;
            case 50:
                this.buf.append("V1_6");
                break;
            case 51:
                this.buf.append("V1_7");
                break;
            case 196653:
                this.buf.append("V1_1");
                break;
            default:
                this.buf.append(i2);
                break;
        }
        this.buf.append(", ");
        appendAccess(i3 | 262144);
        this.buf.append(", ");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(", ");
        if (strArr != null && strArr.length > 0) {
            this.buf.append("new String[] {");
            int i4 = 0;
            while (i4 < strArr.length) {
                this.buf.append(i4 == 0 ? " " : ", ");
                appendConstant(strArr[i4]);
                i4++;
            }
            this.buf.append(" }");
        } else {
            this.buf.append(FXMLLoader.NULL_KEYWORD);
        }
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitSource(String str, String str2) {
        this.buf.setLength(0);
        this.buf.append("cw.visitSource(");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitOuterClass(String str, String str2, String str3) {
        this.buf.setLength(0);
        this.buf.append("cw.visitOuterClass(");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitClassAnnotation(String str, boolean z2) {
        return visitAnnotation(str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitClassTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitClassAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        this.buf.setLength(0);
        this.buf.append("cw.visitInnerClass(");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(", ");
        appendAccess(i2 | 1048576);
        this.buf.append(");\n\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitField(int i2, String str, String str2, String str3, Object obj) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("fv = cw.visitField(");
        appendAccess(i2 | 524288);
        this.buf.append(", ");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(", ");
        appendConstant(obj);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("fv", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("mv = cw.visitMethod(");
        appendAccess(i2);
        this.buf.append(", ");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(", ");
        if (strArr != null && strArr.length > 0) {
            this.buf.append("new String[] {");
            int i3 = 0;
            while (i3 < strArr.length) {
                this.buf.append(i3 == 0 ? " " : ", ");
                appendConstant(strArr[i3]);
                i3++;
            }
            this.buf.append(" }");
        } else {
            this.buf.append(FXMLLoader.NULL_KEYWORD);
        }
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("mv", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitClassEnd() {
        this.text.add("cw.visitEnd();\n\n");
        this.text.add("return cw.toByteArray();\n");
        this.text.add("}\n");
        this.text.add("}\n");
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visit(String str, Object obj) {
        this.buf.setLength(0);
        this.buf.append("av").append(this.id).append(".visit(");
        appendConstant(this.buf, str);
        this.buf.append(", ");
        appendConstant(this.buf, obj);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitEnum(String str, String str2, String str3) {
        this.buf.setLength(0);
        this.buf.append("av").append(this.id).append(".visitEnum(");
        appendConstant(this.buf, str);
        this.buf.append(", ");
        appendConstant(this.buf, str2);
        this.buf.append(", ");
        appendConstant(this.buf, str3);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitAnnotation(String str, String str2) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
        this.buf.append(this.id).append(".visitAnnotation(");
        appendConstant(this.buf, str);
        this.buf.append(", ");
        appendConstant(this.buf, str2);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", this.id + 1);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitArray(String str) {
        this.buf.setLength(0);
        this.buf.append("{\n");
        this.buf.append("AnnotationVisitor av").append(this.id + 1).append(" = av");
        this.buf.append(this.id).append(".visitArray(");
        appendConstant(this.buf, str);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", this.id + 1);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitAnnotationEnd() {
        this.buf.setLength(0);
        this.buf.append("av").append(this.id).append(".visitEnd();\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitFieldAnnotation(String str, boolean z2) {
        return visitAnnotation(str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitFieldTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFieldAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFieldEnd() {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitEnd();\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitParameter(String str, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitParameter(");
        appendString(this.buf, str);
        this.buf.append(", ");
        appendAccess(i2);
        this.text.add(this.buf.append(");\n").toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitAnnotationDefault() {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitAnnotationDefault();\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitMethodAnnotation(String str, boolean z2) {
        return visitAnnotation(str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitMethodTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitParameterAnnotation(int i2, String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitParameterAnnotation(").append(i2).append(", ");
        appendConstant(str);
        this.buf.append(", ").append(z2).append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMethodAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitCode() {
        this.text.add(this.name + ".visitCode();\n");
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        this.buf.setLength(0);
        switch (i2) {
            case -1:
            case 0:
                declareFrameTypes(i3, objArr);
                declareFrameTypes(i4, objArr2);
                if (i2 == -1) {
                    this.buf.append(this.name).append(".visitFrame(Opcodes.F_NEW, ");
                } else {
                    this.buf.append(this.name).append(".visitFrame(Opcodes.F_FULL, ");
                }
                this.buf.append(i3).append(", new Object[] {");
                appendFrameTypes(i3, objArr);
                this.buf.append("}, ").append(i4).append(", new Object[] {");
                appendFrameTypes(i4, objArr2);
                this.buf.append('}');
                break;
            case 1:
                declareFrameTypes(i3, objArr);
                this.buf.append(this.name).append(".visitFrame(Opcodes.F_APPEND,").append(i3).append(", new Object[] {");
                appendFrameTypes(i3, objArr);
                this.buf.append("}, 0, null");
                break;
            case 2:
                this.buf.append(this.name).append(".visitFrame(Opcodes.F_CHOP,").append(i3).append(", null, 0, null");
                break;
            case 3:
                this.buf.append(this.name).append(".visitFrame(Opcodes.F_SAME, 0, null, 0, null");
                break;
            case 4:
                declareFrameTypes(1, objArr2);
                this.buf.append(this.name).append(".visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {");
                appendFrameTypes(1, objArr2);
                this.buf.append('}');
                break;
        }
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitInsn(int i2) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitInsn(").append(OPCODES[i2]).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitIntInsn(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitIntInsn(").append(OPCODES[i2]).append(", ").append(i2 == 188 ? TYPES[i3] : Integer.toString(i3)).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitVarInsn(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitVarInsn(").append(OPCODES[i2]).append(", ").append(i3).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitTypeInsn(int i2, String str) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitTypeInsn(").append(OPCODES[i2]).append(", ");
        appendConstant(str);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitFieldInsn(").append(OPCODES[i2]).append(", ");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(i2, str, str2, str3);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, i2 == 185);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            super.visitMethodInsn(i2, str, str2, str3, z2);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, z2);
        }
    }

    private void doVisitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitMethodInsn(").append(OPCODES[i2]).append(", ");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(", ");
        this.buf.append(z2 ? "true" : "false");
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitInvokeDynamicInsn(");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(handle);
        this.buf.append(", new Object[]{");
        for (int i2 = 0; i2 < objArr.length; i2++) {
            appendConstant(objArr[i2]);
            if (i2 != objArr.length - 1) {
                this.buf.append(", ");
            }
        }
        this.buf.append("});\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitJumpInsn(int i2, Label label) {
        this.buf.setLength(0);
        declareLabel(label);
        this.buf.append(this.name).append(".visitJumpInsn(").append(OPCODES[i2]).append(", ");
        appendLabel(label);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLabel(Label label) {
        this.buf.setLength(0);
        declareLabel(label);
        this.buf.append(this.name).append(".visitLabel(");
        appendLabel(label);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLdcInsn(Object obj) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitLdcInsn(");
        appendConstant(obj);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitIincInsn(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitIincInsn(").append(i2).append(", ").append(i3).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        this.buf.setLength(0);
        for (Label label2 : labelArr) {
            declareLabel(label2);
        }
        declareLabel(label);
        this.buf.append(this.name).append(".visitTableSwitchInsn(").append(i2).append(", ").append(i3).append(", ");
        appendLabel(label);
        this.buf.append(", new Label[] {");
        int i4 = 0;
        while (i4 < labelArr.length) {
            this.buf.append(i4 == 0 ? " " : ", ");
            appendLabel(labelArr[i4]);
            i4++;
        }
        this.buf.append(" });\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.buf.setLength(0);
        for (Label label2 : labelArr) {
            declareLabel(label2);
        }
        declareLabel(label);
        this.buf.append(this.name).append(".visitLookupSwitchInsn(");
        appendLabel(label);
        this.buf.append(", new int[] {");
        int i2 = 0;
        while (i2 < iArr.length) {
            this.buf.append(i2 == 0 ? " " : ", ").append(iArr[i2]);
            i2++;
        }
        this.buf.append(" }, new Label[] {");
        int i3 = 0;
        while (i3 < labelArr.length) {
            this.buf.append(i3 == 0 ? " " : ", ");
            appendLabel(labelArr[i3]);
            i3++;
        }
        this.buf.append(" });\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMultiANewArrayInsn(String str, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitMultiANewArrayInsn(");
        appendConstant(str);
        this.buf.append(", ").append(i2).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation("visitInsnAnnotation", i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.buf.setLength(0);
        declareLabel(label);
        declareLabel(label2);
        declareLabel(label3);
        this.buf.append(this.name).append(".visitTryCatchBlock(");
        appendLabel(label);
        this.buf.append(", ");
        appendLabel(label2);
        this.buf.append(", ");
        appendLabel(label3);
        this.buf.append(", ");
        appendConstant(str);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public ASMifier visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation("visitTryCatchAnnotation", i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitLocalVariable(");
        appendConstant(str);
        this.buf.append(", ");
        appendConstant(str2);
        this.buf.append(", ");
        appendConstant(str3);
        this.buf.append(", ");
        appendLabel(label);
        this.buf.append(", ");
        appendLabel(label2);
        this.buf.append(", ").append(i2).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitLocalVariableAnnotation(");
        this.buf.append(i2);
        this.buf.append(", TypePath.fromString(\"").append((Object) typePath).append("\"), ");
        this.buf.append("new Label[] {");
        int i3 = 0;
        while (i3 < labelArr.length) {
            this.buf.append(i3 == 0 ? " " : ", ");
            appendLabel(labelArr[i3]);
            i3++;
        }
        this.buf.append(" }, new Label[] {");
        int i4 = 0;
        while (i4 < labelArr2.length) {
            this.buf.append(i4 == 0 ? " " : ", ");
            appendLabel(labelArr2[i4]);
            i4++;
        }
        this.buf.append(" }, new int[] {");
        int i5 = 0;
        while (i5 < iArr.length) {
            this.buf.append(i5 == 0 ? " " : ", ").append(iArr[i5]);
            i5++;
        }
        this.buf.append(" }, ");
        appendConstant(str);
        this.buf.append(", ").append(z2).append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLineNumber(int i2, Label label) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitLineNumber(").append(i2).append(", ");
        appendLabel(label);
        this.buf.append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMaxs(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitMaxs(").append(i2).append(", ").append(i3).append(");\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMethodEnd() {
        this.buf.setLength(0);
        this.buf.append(this.name).append(".visitEnd();\n");
        this.text.add(this.buf.toString());
    }

    public ASMifier visitAnnotation(String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = ").append(this.name).append(".visitAnnotation(");
        appendConstant(str);
        this.buf.append(", ").append(z2).append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    public ASMifier visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation("visitTypeAnnotation", i2, typePath, str, z2);
    }

    public ASMifier visitTypeAnnotation(String str, int i2, TypePath typePath, String str2, boolean z2) {
        this.buf.setLength(0);
        this.buf.append("{\n").append("av0 = ").append(this.name).append(".").append(str).append("(");
        this.buf.append(i2);
        this.buf.append(", TypePath.fromString(\"").append((Object) typePath).append("\"), ");
        appendConstant(str2);
        this.buf.append(", ").append(z2).append(");\n");
        this.text.add(this.buf.toString());
        ASMifier aSMifierCreateASMifier = createASMifier("av", 0);
        this.text.add(aSMifierCreateASMifier.getText());
        this.text.add("}\n");
        return aSMifierCreateASMifier;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void visitAttribute(Attribute attribute) {
        this.buf.setLength(0);
        this.buf.append("// ATTRIBUTE ").append(attribute.type).append('\n');
        if (attribute instanceof ASMifiable) {
            if (this.labelNames == null) {
                this.labelNames = new HashMap();
            }
            this.buf.append("{\n");
            ((ASMifiable) attribute).asmify(this.buf, "attr", this.labelNames);
            this.buf.append(this.name).append(".visitAttribute(attr);\n");
            this.buf.append("}\n");
        }
        this.text.add(this.buf.toString());
    }

    protected ASMifier createASMifier(String str, int i2) {
        return new ASMifier(Opcodes.ASM5, str, i2);
    }

    void appendAccess(int i2) {
        boolean z2 = true;
        if ((i2 & 1) != 0) {
            this.buf.append("ACC_PUBLIC");
            z2 = false;
        }
        if ((i2 & 2) != 0) {
            this.buf.append("ACC_PRIVATE");
            z2 = false;
        }
        if ((i2 & 4) != 0) {
            this.buf.append("ACC_PROTECTED");
            z2 = false;
        }
        if ((i2 & 16) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_FINAL");
            z2 = false;
        }
        if ((i2 & 8) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_STATIC");
            z2 = false;
        }
        if ((i2 & 32) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            if ((i2 & 262144) == 0) {
                this.buf.append("ACC_SYNCHRONIZED");
            } else {
                this.buf.append("ACC_SUPER");
            }
            z2 = false;
        }
        if ((i2 & 64) != 0 && (i2 & 524288) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_VOLATILE");
            z2 = false;
        }
        if ((i2 & 64) != 0 && (i2 & 262144) == 0 && (i2 & 524288) == 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_BRIDGE");
            z2 = false;
        }
        if ((i2 & 128) != 0 && (i2 & 262144) == 0 && (i2 & 524288) == 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_VARARGS");
            z2 = false;
        }
        if ((i2 & 128) != 0 && (i2 & 524288) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_TRANSIENT");
            z2 = false;
        }
        if ((i2 & 256) != 0 && (i2 & 262144) == 0 && (i2 & 524288) == 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_NATIVE");
            z2 = false;
        }
        if ((i2 & 16384) != 0 && ((i2 & 262144) != 0 || (i2 & 524288) != 0 || (i2 & 1048576) != 0)) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_ENUM");
            z2 = false;
        }
        if ((i2 & 8192) != 0 && ((i2 & 262144) != 0 || (i2 & 1048576) != 0)) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_ANNOTATION");
            z2 = false;
        }
        if ((i2 & 1024) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_ABSTRACT");
            z2 = false;
        }
        if ((i2 & 512) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_INTERFACE");
            z2 = false;
        }
        if ((i2 & 2048) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_STRICT");
            z2 = false;
        }
        if ((i2 & 4096) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_SYNTHETIC");
            z2 = false;
        }
        if ((i2 & 131072) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_DEPRECATED");
            z2 = false;
        }
        if ((i2 & 32768) != 0) {
            if (!z2) {
                this.buf.append(" + ");
            }
            this.buf.append("ACC_MANDATED");
            z2 = false;
        }
        if (z2) {
            this.buf.append('0');
        }
    }

    protected void appendConstant(Object obj) {
        appendConstant(this.buf, obj);
    }

    static void appendConstant(StringBuffer stringBuffer, Object obj) {
        if (obj == null) {
            stringBuffer.append(FXMLLoader.NULL_KEYWORD);
            return;
        }
        if (obj instanceof String) {
            appendString(stringBuffer, (String) obj);
            return;
        }
        if (obj instanceof Type) {
            stringBuffer.append("Type.getType(\"");
            stringBuffer.append(((Type) obj).getDescriptor());
            stringBuffer.append("\")");
            return;
        }
        if (obj instanceof Handle) {
            stringBuffer.append("new Handle(");
            Handle handle = (Handle) obj;
            stringBuffer.append("Opcodes.").append(HANDLE_TAG[handle.getTag()]).append(", \"");
            stringBuffer.append(handle.getOwner()).append("\", \"");
            stringBuffer.append(handle.getName()).append("\", \"");
            stringBuffer.append(handle.getDesc()).append("\")");
            return;
        }
        if (obj instanceof Byte) {
            stringBuffer.append("new Byte((byte)").append(obj).append(')');
            return;
        }
        if (obj instanceof Boolean) {
            stringBuffer.append(((Boolean) obj).booleanValue() ? "Boolean.TRUE" : "Boolean.FALSE");
            return;
        }
        if (obj instanceof Short) {
            stringBuffer.append("new Short((short)").append(obj).append(')');
            return;
        }
        if (obj instanceof Character) {
            stringBuffer.append("new Character((char)").append((int) ((Character) obj).charValue()).append(')');
            return;
        }
        if (obj instanceof Integer) {
            stringBuffer.append("new Integer(").append(obj).append(')');
            return;
        }
        if (obj instanceof Float) {
            stringBuffer.append("new Float(\"").append(obj).append("\")");
            return;
        }
        if (obj instanceof Long) {
            stringBuffer.append("new Long(").append(obj).append("L)");
            return;
        }
        if (obj instanceof Double) {
            stringBuffer.append("new Double(\"").append(obj).append("\")");
            return;
        }
        if (obj instanceof byte[]) {
            byte[] bArr = (byte[]) obj;
            stringBuffer.append("new byte[] {");
            int i2 = 0;
            while (i2 < bArr.length) {
                stringBuffer.append(i2 == 0 ? "" : ",").append((int) bArr[i2]);
                i2++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof boolean[]) {
            boolean[] zArr = (boolean[]) obj;
            stringBuffer.append("new boolean[] {");
            int i3 = 0;
            while (i3 < zArr.length) {
                stringBuffer.append(i3 == 0 ? "" : ",").append(zArr[i3]);
                i3++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof short[]) {
            short[] sArr = (short[]) obj;
            stringBuffer.append("new short[] {");
            int i4 = 0;
            while (i4 < sArr.length) {
                stringBuffer.append(i4 == 0 ? "" : ",").append("(short)").append((int) sArr[i4]);
                i4++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof char[]) {
            char[] cArr = (char[]) obj;
            stringBuffer.append("new char[] {");
            int i5 = 0;
            while (i5 < cArr.length) {
                stringBuffer.append(i5 == 0 ? "" : ",").append("(char)").append((int) cArr[i5]);
                i5++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof int[]) {
            int[] iArr = (int[]) obj;
            stringBuffer.append("new int[] {");
            int i6 = 0;
            while (i6 < iArr.length) {
                stringBuffer.append(i6 == 0 ? "" : ",").append(iArr[i6]);
                i6++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            stringBuffer.append("new long[] {");
            int i7 = 0;
            while (i7 < jArr.length) {
                stringBuffer.append(i7 == 0 ? "" : ",").append(jArr[i7]).append('L');
                i7++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof float[]) {
            float[] fArr = (float[]) obj;
            stringBuffer.append("new float[] {");
            int i8 = 0;
            while (i8 < fArr.length) {
                stringBuffer.append(i8 == 0 ? "" : ",").append(fArr[i8]).append('f');
                i8++;
            }
            stringBuffer.append('}');
            return;
        }
        if (obj instanceof double[]) {
            double[] dArr = (double[]) obj;
            stringBuffer.append("new double[] {");
            int i9 = 0;
            while (i9 < dArr.length) {
                stringBuffer.append(i9 == 0 ? "" : ",").append(dArr[i9]).append('d');
                i9++;
            }
            stringBuffer.append('}');
        }
    }

    private void declareFrameTypes(int i2, Object[] objArr) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (objArr[i3] instanceof Label) {
                declareLabel((Label) objArr[i3]);
            }
        }
    }

    private void appendFrameTypes(int i2, Object[] objArr) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (i3 > 0) {
                this.buf.append(", ");
            }
            if (objArr[i3] instanceof String) {
                appendConstant(objArr[i3]);
            } else if (objArr[i3] instanceof Integer) {
                switch (((Integer) objArr[i3]).intValue()) {
                    case 0:
                        this.buf.append("Opcodes.TOP");
                        break;
                    case 1:
                        this.buf.append("Opcodes.INTEGER");
                        break;
                    case 2:
                        this.buf.append("Opcodes.FLOAT");
                        break;
                    case 3:
                        this.buf.append("Opcodes.DOUBLE");
                        break;
                    case 4:
                        this.buf.append("Opcodes.LONG");
                        break;
                    case 5:
                        this.buf.append("Opcodes.NULL");
                        break;
                    case 6:
                        this.buf.append("Opcodes.UNINITIALIZED_THIS");
                        break;
                }
            } else {
                appendLabel((Label) objArr[i3]);
            }
        }
    }

    protected void declareLabel(Label label) {
        if (this.labelNames == null) {
            this.labelNames = new HashMap();
        }
        if (this.labelNames.get(label) == null) {
            String str = PdfOps.l_TOKEN + this.labelNames.size();
            this.labelNames.put(label, str);
            this.buf.append("Label ").append(str).append(" = new Label();\n");
        }
    }

    protected void appendLabel(Label label) {
        this.buf.append(this.labelNames.get(label));
    }
}
