package jdk.internal.org.objectweb.asm.util;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.TypePath;
import jdk.internal.org.objectweb.asm.TypeReference;
import jdk.internal.org.objectweb.asm.signature.SignatureReader;
import org.icepdf.core.util.PdfOps;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/Textifier.class */
public class Textifier extends Printer {
    public static final int INTERNAL_NAME = 0;
    public static final int FIELD_DESCRIPTOR = 1;
    public static final int FIELD_SIGNATURE = 2;
    public static final int METHOD_DESCRIPTOR = 3;
    public static final int METHOD_SIGNATURE = 4;
    public static final int CLASS_SIGNATURE = 5;
    public static final int TYPE_DECLARATION = 6;
    public static final int CLASS_DECLARATION = 7;
    public static final int PARAMETERS_DECLARATION = 8;
    public static final int HANDLE_DESCRIPTOR = 9;
    protected String tab;
    protected String tab2;
    protected String tab3;
    protected String ltab;
    protected Map<Label, String> labelNames;
    private int access;
    private int valueNumber;

    public Textifier() {
        this(Opcodes.ASM5);
        if (getClass() != Textifier.class) {
            throw new IllegalStateException();
        }
    }

    protected Textifier(int i2) {
        super(i2);
        this.tab = Constants.INDENT;
        this.tab2 = "    ";
        this.tab3 = GoToActionDialog.EMPTY_DESTINATION;
        this.ltab = "   ";
        this.valueNumber = 0;
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
            System.err.println("Prints a disassembled view of the given class.");
            System.err.println("Usage: Textifier [-debug] <fully qualified class name or class file name>");
            return;
        }
        if (strArr[z5 ? 1 : 0].endsWith(".class") || strArr[z5 ? 1 : 0].indexOf(92) > -1 || strArr[z5 ? 1 : 0].indexOf(47) > -1) {
            classReader = new ClassReader(new FileInputStream(strArr[z5 ? 1 : 0]));
        } else {
            classReader = new ClassReader(strArr[z5 ? 1 : 0]);
        }
        classReader.accept(new TraceClassVisitor(new PrintWriter(System.out)), i2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        this.access = i3;
        this.buf.setLength(0);
        this.buf.append("// class version ").append(i2 & 65535).append('.').append(i2 >>> 16).append(" (").append(i2).append(")\n");
        if ((i3 & 131072) != 0) {
            this.buf.append("// DEPRECATED\n");
        }
        this.buf.append("// access flags 0x").append(Integer.toHexString(i3).toUpperCase()).append('\n');
        appendDescriptor(5, str2);
        if (str2 != null) {
            TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(i3);
            new SignatureReader(str2).accept(traceSignatureVisitor);
            this.buf.append("// declaration: ").append(str).append(traceSignatureVisitor.getDeclaration()).append('\n');
        }
        appendAccess(i3 & (-33));
        if ((i3 & 8192) != 0) {
            this.buf.append("@interface ");
        } else if ((i3 & 512) != 0) {
            this.buf.append("interface ");
        } else if ((i3 & 16384) == 0) {
            this.buf.append("class ");
        }
        appendDescriptor(0, str);
        if (str3 != null && !"java/lang/Object".equals(str3)) {
            this.buf.append(" extends ");
            appendDescriptor(0, str3);
            this.buf.append(' ');
        }
        if (strArr != null && strArr.length > 0) {
            this.buf.append(" implements ");
            for (String str4 : strArr) {
                appendDescriptor(0, str4);
                this.buf.append(' ');
            }
        }
        this.buf.append(" {\n\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitSource(String str, String str2) {
        this.buf.setLength(0);
        if (str != null) {
            this.buf.append(this.tab).append("// compiled from: ").append(str).append('\n');
        }
        if (str2 != null) {
            this.buf.append(this.tab).append("// debug info: ").append(str2).append('\n');
        }
        if (this.buf.length() > 0) {
            this.text.add(this.buf.toString());
        }
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitOuterClass(String str, String str2, String str3) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("OUTERCLASS ");
        appendDescriptor(0, str);
        this.buf.append(' ');
        if (str2 != null) {
            this.buf.append(str2).append(' ');
        }
        appendDescriptor(3, str3);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitClassAnnotation(String str, boolean z2) {
        this.text.add("\n");
        return visitAnnotation(str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitClassTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        this.text.add("\n");
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitClassAttribute(Attribute attribute) {
        this.text.add("\n");
        visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("// access flags 0x");
        this.buf.append(Integer.toHexString(i2 & (-33)).toUpperCase()).append('\n');
        this.buf.append(this.tab);
        appendAccess(i2);
        this.buf.append("INNERCLASS ");
        appendDescriptor(0, str);
        this.buf.append(' ');
        appendDescriptor(0, str2);
        this.buf.append(' ');
        appendDescriptor(0, str3);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitField(int i2, String str, String str2, String str3, Object obj) {
        this.buf.setLength(0);
        this.buf.append('\n');
        if ((i2 & 131072) != 0) {
            this.buf.append(this.tab).append("// DEPRECATED\n");
        }
        this.buf.append(this.tab).append("// access flags 0x").append(Integer.toHexString(i2).toUpperCase()).append('\n');
        if (str3 != null) {
            this.buf.append(this.tab);
            appendDescriptor(2, str3);
            TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(0);
            new SignatureReader(str3).acceptType(traceSignatureVisitor);
            this.buf.append(this.tab).append("// declaration: ").append(traceSignatureVisitor.getDeclaration()).append('\n');
        }
        this.buf.append(this.tab);
        appendAccess(i2);
        appendDescriptor(1, str2);
        this.buf.append(' ').append(str);
        if (obj != null) {
            this.buf.append(" = ");
            if (obj instanceof String) {
                this.buf.append('\"').append(obj).append('\"');
            } else {
                this.buf.append(obj);
            }
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        this.buf.setLength(0);
        this.buf.append('\n');
        if ((i2 & 131072) != 0) {
            this.buf.append(this.tab).append("// DEPRECATED\n");
        }
        this.buf.append(this.tab).append("// access flags 0x").append(Integer.toHexString(i2).toUpperCase()).append('\n');
        if (str3 != null) {
            this.buf.append(this.tab);
            appendDescriptor(4, str3);
            TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(0);
            new SignatureReader(str3).accept(traceSignatureVisitor);
            String declaration = traceSignatureVisitor.getDeclaration();
            String returnType = traceSignatureVisitor.getReturnType();
            String exceptions = traceSignatureVisitor.getExceptions();
            this.buf.append(this.tab).append("// declaration: ").append(returnType).append(' ').append(str).append(declaration);
            if (exceptions != null) {
                this.buf.append(" throws ").append(exceptions);
            }
            this.buf.append('\n');
        }
        this.buf.append(this.tab);
        appendAccess(i2 & (-65));
        if ((i2 & 256) != 0) {
            this.buf.append("native ");
        }
        if ((i2 & 128) != 0) {
            this.buf.append("varargs ");
        }
        if ((i2 & 64) != 0) {
            this.buf.append("bridge ");
        }
        if ((this.access & 512) != 0 && (i2 & 1024) == 0 && (i2 & 8) == 0) {
            this.buf.append("default ");
        }
        this.buf.append(str);
        appendDescriptor(3, str2);
        if (strArr != null && strArr.length > 0) {
            this.buf.append(" throws ");
            for (String str4 : strArr) {
                appendDescriptor(0, str4);
                this.buf.append(' ');
            }
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitClassEnd() {
        this.text.add("}\n");
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visit(String str, Object obj) {
        this.buf.setLength(0);
        int i2 = this.valueNumber;
        this.valueNumber = i2 + 1;
        appendComa(i2);
        if (str != null) {
            this.buf.append(str).append('=');
        }
        if (obj instanceof String) {
            visitString((String) obj);
        } else if (obj instanceof Type) {
            visitType((Type) obj);
        } else if (obj instanceof Byte) {
            visitByte(((Byte) obj).byteValue());
        } else if (obj instanceof Boolean) {
            visitBoolean(((Boolean) obj).booleanValue());
        } else if (obj instanceof Short) {
            visitShort(((Short) obj).shortValue());
        } else if (obj instanceof Character) {
            visitChar(((Character) obj).charValue());
        } else if (obj instanceof Integer) {
            visitInt(((Integer) obj).intValue());
        } else if (obj instanceof Float) {
            visitFloat(((Float) obj).floatValue());
        } else if (obj instanceof Long) {
            visitLong(((Long) obj).longValue());
        } else if (obj instanceof Double) {
            visitDouble(((Double) obj).doubleValue());
        } else if (obj.getClass().isArray()) {
            this.buf.append('{');
            if (obj instanceof byte[]) {
                byte[] bArr = (byte[]) obj;
                for (int i3 = 0; i3 < bArr.length; i3++) {
                    appendComa(i3);
                    visitByte(bArr[i3]);
                }
            } else if (obj instanceof boolean[]) {
                boolean[] zArr = (boolean[]) obj;
                for (int i4 = 0; i4 < zArr.length; i4++) {
                    appendComa(i4);
                    visitBoolean(zArr[i4]);
                }
            } else if (obj instanceof short[]) {
                short[] sArr = (short[]) obj;
                for (int i5 = 0; i5 < sArr.length; i5++) {
                    appendComa(i5);
                    visitShort(sArr[i5]);
                }
            } else if (obj instanceof char[]) {
                char[] cArr = (char[]) obj;
                for (int i6 = 0; i6 < cArr.length; i6++) {
                    appendComa(i6);
                    visitChar(cArr[i6]);
                }
            } else if (obj instanceof int[]) {
                int[] iArr = (int[]) obj;
                for (int i7 = 0; i7 < iArr.length; i7++) {
                    appendComa(i7);
                    visitInt(iArr[i7]);
                }
            } else if (obj instanceof long[]) {
                long[] jArr = (long[]) obj;
                for (int i8 = 0; i8 < jArr.length; i8++) {
                    appendComa(i8);
                    visitLong(jArr[i8]);
                }
            } else if (obj instanceof float[]) {
                float[] fArr = (float[]) obj;
                for (int i9 = 0; i9 < fArr.length; i9++) {
                    appendComa(i9);
                    visitFloat(fArr[i9]);
                }
            } else if (obj instanceof double[]) {
                double[] dArr = (double[]) obj;
                for (int i10 = 0; i10 < dArr.length; i10++) {
                    appendComa(i10);
                    visitDouble(dArr[i10]);
                }
            }
            this.buf.append('}');
        }
        this.text.add(this.buf.toString());
    }

    private void visitInt(int i2) {
        this.buf.append(i2);
    }

    private void visitLong(long j2) {
        this.buf.append(j2).append('L');
    }

    private void visitFloat(float f2) {
        this.buf.append(f2).append('F');
    }

    private void visitDouble(double d2) {
        this.buf.append(d2).append('D');
    }

    private void visitChar(char c2) {
        this.buf.append("(char)").append((int) c2);
    }

    private void visitShort(short s2) {
        this.buf.append("(short)").append((int) s2);
    }

    private void visitByte(byte b2) {
        this.buf.append("(byte)").append((int) b2);
    }

    private void visitBoolean(boolean z2) {
        this.buf.append(z2);
    }

    private void visitString(String str) {
        appendString(this.buf, str);
    }

    private void visitType(Type type) {
        this.buf.append(type.getClassName()).append(".class");
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitEnum(String str, String str2, String str3) {
        this.buf.setLength(0);
        int i2 = this.valueNumber;
        this.valueNumber = i2 + 1;
        appendComa(i2);
        if (str != null) {
            this.buf.append(str).append('=');
        }
        appendDescriptor(1, str2);
        this.buf.append('.').append(str3);
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitAnnotation(String str, String str2) {
        this.buf.setLength(0);
        int i2 = this.valueNumber;
        this.valueNumber = i2 + 1;
        appendComa(i2);
        if (str != null) {
            this.buf.append(str).append('=');
        }
        this.buf.append('@');
        appendDescriptor(1, str2);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.text.add(")");
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitArray(String str) {
        this.buf.setLength(0);
        int i2 = this.valueNumber;
        this.valueNumber = i2 + 1;
        appendComa(i2);
        if (str != null) {
            this.buf.append(str).append('=');
        }
        this.buf.append('{');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.text.add("}");
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitAnnotationEnd() {
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitFieldAnnotation(String str, boolean z2) {
        return visitAnnotation(str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitFieldTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFieldAttribute(Attribute attribute) {
        visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFieldEnd() {
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitParameter(String str, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("// parameter ");
        appendAccess(i2);
        this.buf.append(' ').append(str == null ? "<no name>" : str).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitAnnotationDefault() {
        this.text.add(this.tab2 + "default=");
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.text.add("\n");
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitMethodAnnotation(String str, boolean z2) {
        return visitAnnotation(str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitMethodTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Textifier visitParameterAnnotation(int i2, String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append('@');
        appendDescriptor(1, str);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.text.add(z2 ? ") // parameter " : ") // invisible, parameter ");
        this.text.add(Integer.valueOf(i2));
        this.text.add("\n");
        return textifierCreateTextifier;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMethodAttribute(Attribute attribute) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("ATTRIBUTE ");
        appendDescriptor(-1, attribute.type);
        if (attribute instanceof Textifiable) {
            ((Textifiable) attribute).textify(this.buf, this.labelNames);
        } else {
            this.buf.append(" : unknown\n");
        }
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitCode() {
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        this.buf.setLength(0);
        this.buf.append(this.ltab);
        this.buf.append("FRAME ");
        switch (i2) {
            case -1:
            case 0:
                this.buf.append("FULL [");
                appendFrameTypes(i3, objArr);
                this.buf.append("] [");
                appendFrameTypes(i4, objArr2);
                this.buf.append(']');
                break;
            case 1:
                this.buf.append("APPEND [");
                appendFrameTypes(i3, objArr);
                this.buf.append(']');
                break;
            case 2:
                this.buf.append("CHOP ").append(i3);
                break;
            case 3:
                this.buf.append("SAME");
                break;
            case 4:
                this.buf.append("SAME1 ");
                appendFrameTypes(1, objArr2);
                break;
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitInsn(int i2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[i2]).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitIntInsn(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[i2]).append(' ').append(i2 == 188 ? TYPES[i3] : Integer.toString(i3)).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitVarInsn(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[i2]).append(' ').append(i3).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitTypeInsn(int i2, String str) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[i2]).append(' ');
        appendDescriptor(0, str);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[i2]).append(' ');
        appendDescriptor(0, str);
        this.buf.append('.').append(str2).append(" : ");
        appendDescriptor(1, str3);
        this.buf.append('\n');
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
        this.buf.append(this.tab2).append(OPCODES[i2]).append(' ');
        appendDescriptor(0, str);
        this.buf.append('.').append(str2).append(' ');
        appendDescriptor(3, str3);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("INVOKEDYNAMIC").append(' ');
        this.buf.append(str);
        appendDescriptor(3, str2);
        this.buf.append(" [");
        this.buf.append('\n');
        this.buf.append(this.tab3);
        appendHandle(handle);
        this.buf.append('\n');
        this.buf.append(this.tab3).append("// arguments:");
        if (objArr.length == 0) {
            this.buf.append(" none");
        } else {
            this.buf.append('\n');
            for (Object obj : objArr) {
                this.buf.append(this.tab3);
                if (obj instanceof String) {
                    Printer.appendString(this.buf, (String) obj);
                } else if (obj instanceof Type) {
                    Type type = (Type) obj;
                    if (type.getSort() == 11) {
                        appendDescriptor(3, type.getDescriptor());
                    } else {
                        this.buf.append(type.getDescriptor()).append(".class");
                    }
                } else if (obj instanceof Handle) {
                    appendHandle((Handle) obj);
                } else {
                    this.buf.append(obj);
                }
                this.buf.append(", \n");
            }
            this.buf.setLength(this.buf.length() - 3);
        }
        this.buf.append('\n');
        this.buf.append(this.tab2).append("]\n");
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitJumpInsn(int i2, Label label) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append(OPCODES[i2]).append(' ');
        appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLabel(Label label) {
        this.buf.setLength(0);
        this.buf.append(this.ltab);
        appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLdcInsn(Object obj) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LDC ");
        if (obj instanceof String) {
            Printer.appendString(this.buf, (String) obj);
        } else if (obj instanceof Type) {
            this.buf.append(((Type) obj).getDescriptor()).append(".class");
        } else {
            this.buf.append(obj);
        }
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitIincInsn(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("IINC ").append(i2).append(' ').append(i3).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("TABLESWITCH\n");
        for (int i4 = 0; i4 < labelArr.length; i4++) {
            this.buf.append(this.tab3).append(i2 + i4).append(": ");
            appendLabel(labelArr[i4]);
            this.buf.append('\n');
        }
        this.buf.append(this.tab3).append("default: ");
        appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LOOKUPSWITCH\n");
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            this.buf.append(this.tab3).append(iArr[i2]).append(": ");
            appendLabel(labelArr[i2]);
            this.buf.append('\n');
        }
        this.buf.append(this.tab3).append("default: ");
        appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMultiANewArrayInsn(String str, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("MULTIANEWARRAY ");
        appendDescriptor(1, str);
        this.buf.append(' ').append(i2).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        return visitTypeAnnotation(i2, typePath, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("TRYCATCHBLOCK ");
        appendLabel(label);
        this.buf.append(' ');
        appendLabel(label2);
        this.buf.append(' ');
        appendLabel(label3);
        this.buf.append(' ');
        appendDescriptor(0, str);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("TRYCATCHBLOCK @");
        appendDescriptor(1, str);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.buf.setLength(0);
        this.buf.append(") : ");
        appendTypeReference(i2);
        this.buf.append(", ").append((Object) typePath);
        this.buf.append(z2 ? "\n" : " // invisible\n");
        this.text.add(this.buf.toString());
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LOCALVARIABLE ").append(str).append(' ');
        appendDescriptor(1, str2);
        this.buf.append(' ');
        appendLabel(label);
        this.buf.append(' ');
        appendLabel(label2);
        this.buf.append(' ').append(i2).append('\n');
        if (str3 != null) {
            this.buf.append(this.tab2);
            appendDescriptor(2, str3);
            TraceSignatureVisitor traceSignatureVisitor = new TraceSignatureVisitor(0);
            new SignatureReader(str3).acceptType(traceSignatureVisitor);
            this.buf.append(this.tab2).append("// declaration: ").append(traceSignatureVisitor.getDeclaration()).append('\n');
        }
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public Printer visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LOCALVARIABLE @");
        appendDescriptor(1, str);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.buf.setLength(0);
        this.buf.append(") : ");
        appendTypeReference(i2);
        this.buf.append(", ").append((Object) typePath);
        for (int i3 = 0; i3 < labelArr.length; i3++) {
            this.buf.append(" [ ");
            appendLabel(labelArr[i3]);
            this.buf.append(" - ");
            appendLabel(labelArr2[i3]);
            this.buf.append(" - ").append(iArr[i3]).append(" ]");
        }
        this.buf.append(z2 ? "\n" : " // invisible\n");
        this.text.add(this.buf.toString());
        return textifierCreateTextifier;
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitLineNumber(int i2, Label label) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("LINENUMBER ").append(i2).append(' ');
        appendLabel(label);
        this.buf.append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMaxs(int i2, int i3) {
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("MAXSTACK = ").append(i2).append('\n');
        this.text.add(this.buf.toString());
        this.buf.setLength(0);
        this.buf.append(this.tab2).append("MAXLOCALS = ").append(i3).append('\n');
        this.text.add(this.buf.toString());
    }

    @Override // jdk.internal.org.objectweb.asm.util.Printer
    public void visitMethodEnd() {
    }

    public Textifier visitAnnotation(String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append('@');
        appendDescriptor(1, str);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.text.add(z2 ? ")\n" : ") // invisible\n");
        return textifierCreateTextifier;
    }

    public Textifier visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append('@');
        appendDescriptor(1, str);
        this.buf.append('(');
        this.text.add(this.buf.toString());
        Textifier textifierCreateTextifier = createTextifier();
        this.text.add(textifierCreateTextifier.getText());
        this.buf.setLength(0);
        this.buf.append(") : ");
        appendTypeReference(i2);
        this.buf.append(", ").append((Object) typePath);
        this.buf.append(z2 ? "\n" : " // invisible\n");
        this.text.add(this.buf.toString());
        return textifierCreateTextifier;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void visitAttribute(Attribute attribute) {
        this.buf.setLength(0);
        this.buf.append(this.tab).append("ATTRIBUTE ");
        appendDescriptor(-1, attribute.type);
        if (attribute instanceof Textifiable) {
            ((Textifiable) attribute).textify(this.buf, null);
        } else {
            this.buf.append(" : unknown\n");
        }
        this.text.add(this.buf.toString());
    }

    protected Textifier createTextifier() {
        return new Textifier();
    }

    protected void appendDescriptor(int i2, String str) {
        if (i2 == 5 || i2 == 2 || i2 == 4) {
            if (str != null) {
                this.buf.append("// signature ").append(str).append('\n');
                return;
            }
            return;
        }
        this.buf.append(str);
    }

    protected void appendLabel(Label label) {
        if (this.labelNames == null) {
            this.labelNames = new HashMap();
        }
        String str = this.labelNames.get(label);
        if (str == null) {
            str = "L" + this.labelNames.size();
            this.labelNames.put(label, str);
        }
        this.buf.append(str);
    }

    protected void appendHandle(Handle handle) {
        int tag = handle.getTag();
        this.buf.append("// handle kind 0x").append(Integer.toHexString(tag)).append(" : ");
        boolean z2 = false;
        switch (tag) {
            case 1:
                this.buf.append("GETFIELD");
                break;
            case 2:
                this.buf.append("GETSTATIC");
                break;
            case 3:
                this.buf.append("PUTFIELD");
                break;
            case 4:
                this.buf.append("PUTSTATIC");
                break;
            case 5:
                this.buf.append("INVOKEVIRTUAL");
                z2 = true;
                break;
            case 6:
                this.buf.append("INVOKESTATIC");
                z2 = true;
                break;
            case 7:
                this.buf.append("INVOKESPECIAL");
                z2 = true;
                break;
            case 8:
                this.buf.append("NEWINVOKESPECIAL");
                z2 = true;
                break;
            case 9:
                this.buf.append("INVOKEINTERFACE");
                z2 = true;
                break;
        }
        this.buf.append('\n');
        this.buf.append(this.tab3);
        appendDescriptor(0, handle.getOwner());
        this.buf.append('.');
        this.buf.append(handle.getName());
        if (!z2) {
            this.buf.append('(');
        }
        appendDescriptor(9, handle.getDesc());
        if (!z2) {
            this.buf.append(')');
        }
    }

    private void appendAccess(int i2) {
        if ((i2 & 1) != 0) {
            this.buf.append("public ");
        }
        if ((i2 & 2) != 0) {
            this.buf.append("private ");
        }
        if ((i2 & 4) != 0) {
            this.buf.append("protected ");
        }
        if ((i2 & 16) != 0) {
            this.buf.append("final ");
        }
        if ((i2 & 8) != 0) {
            this.buf.append("static ");
        }
        if ((i2 & 32) != 0) {
            this.buf.append("synchronized ");
        }
        if ((i2 & 64) != 0) {
            this.buf.append("volatile ");
        }
        if ((i2 & 128) != 0) {
            this.buf.append("transient ");
        }
        if ((i2 & 1024) != 0) {
            this.buf.append("abstract ");
        }
        if ((i2 & 2048) != 0) {
            this.buf.append("strictfp ");
        }
        if ((i2 & 4096) != 0) {
            this.buf.append("synthetic ");
        }
        if ((i2 & 32768) != 0) {
            this.buf.append("mandated ");
        }
        if ((i2 & 16384) != 0) {
            this.buf.append("enum ");
        }
    }

    private void appendComa(int i2) {
        if (i2 != 0) {
            this.buf.append(", ");
        }
    }

    private void appendTypeReference(int i2) {
        TypeReference typeReference = new TypeReference(i2);
        switch (typeReference.getSort()) {
            case 0:
                this.buf.append("CLASS_TYPE_PARAMETER ").append(typeReference.getTypeParameterIndex());
                break;
            case 1:
                this.buf.append("METHOD_TYPE_PARAMETER ").append(typeReference.getTypeParameterIndex());
                break;
            case 16:
                this.buf.append("CLASS_EXTENDS ").append(typeReference.getSuperTypeIndex());
                break;
            case 17:
                this.buf.append("CLASS_TYPE_PARAMETER_BOUND ").append(typeReference.getTypeParameterIndex()).append(", ").append(typeReference.getTypeParameterBoundIndex());
                break;
            case 18:
                this.buf.append("METHOD_TYPE_PARAMETER_BOUND ").append(typeReference.getTypeParameterIndex()).append(", ").append(typeReference.getTypeParameterBoundIndex());
                break;
            case 19:
                this.buf.append("FIELD");
                break;
            case 20:
                this.buf.append("METHOD_RETURN");
                break;
            case 21:
                this.buf.append("METHOD_RECEIVER");
                break;
            case 22:
                this.buf.append("METHOD_FORMAL_PARAMETER ").append(typeReference.getFormalParameterIndex());
                break;
            case 23:
                this.buf.append("THROWS ").append(typeReference.getExceptionIndex());
                break;
            case 64:
                this.buf.append("LOCAL_VARIABLE");
                break;
            case 65:
                this.buf.append("RESOURCE_VARIABLE");
                break;
            case 66:
                this.buf.append("EXCEPTION_PARAMETER ").append(typeReference.getTryCatchBlockIndex());
                break;
            case 67:
                this.buf.append("INSTANCEOF");
                break;
            case 68:
                this.buf.append("NEW");
                break;
            case 69:
                this.buf.append("CONSTRUCTOR_REFERENCE");
                break;
            case 70:
                this.buf.append("METHOD_REFERENCE");
                break;
            case 71:
                this.buf.append("CAST ").append(typeReference.getTypeArgumentIndex());
                break;
            case 72:
                this.buf.append("CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                break;
            case 73:
                this.buf.append("METHOD_INVOCATION_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                break;
            case 74:
                this.buf.append("CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                break;
            case 75:
                this.buf.append("METHOD_REFERENCE_TYPE_ARGUMENT ").append(typeReference.getTypeArgumentIndex());
                break;
        }
    }

    private void appendFrameTypes(int i2, Object[] objArr) {
        for (int i3 = 0; i3 < i2; i3++) {
            if (i3 > 0) {
                this.buf.append(' ');
            }
            if (objArr[i3] instanceof String) {
                String str = (String) objArr[i3];
                if (str.startsWith("[")) {
                    appendDescriptor(1, str);
                } else {
                    appendDescriptor(0, str);
                }
            } else if (objArr[i3] instanceof Integer) {
                switch (((Integer) objArr[i3]).intValue()) {
                    case 0:
                        appendDescriptor(1, "T");
                        break;
                    case 1:
                        appendDescriptor(1, "I");
                        break;
                    case 2:
                        appendDescriptor(1, PdfOps.F_TOKEN);
                        break;
                    case 3:
                        appendDescriptor(1, PdfOps.D_TOKEN);
                        break;
                    case 4:
                        appendDescriptor(1, "J");
                        break;
                    case 5:
                        appendDescriptor(1, "N");
                        break;
                    case 6:
                        appendDescriptor(1, "U");
                        break;
                }
            } else {
                appendLabel((Label) objArr[i3]);
            }
        }
    }
}
