package jdk.internal.org.objectweb.asm.util;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.Handle;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.TypePath;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.analysis.Analyzer;
import jdk.internal.org.objectweb.asm.tree.analysis.BasicVerifier;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/CheckMethodAdapter.class */
public class CheckMethodAdapter extends MethodVisitor {
    public int version;
    private int access;
    private boolean startCode;
    private boolean endCode;
    private boolean endMethod;
    private int insnCount;
    private final Map<Label, Integer> labels;
    private Set<Label> usedLabels;
    private int expandedFrames;
    private int compressedFrames;
    private int lastFrame;
    private List<Label> handlers;
    private static final int[] TYPE = new int["BBBBBBBBBBBBBBBBCCIAADDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBDDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBJBBBBBBBBBBBBBBBBBBBBHHHHHHHHHHHHHHHHDKLBBBBBBFFFFGGGGAECEBBEEBBAMHHAA".length()];
    private static Field labelStatusField;

    static {
        for (int i2 = 0; i2 < TYPE.length; i2++) {
            TYPE[i2] = ("BBBBBBBBBBBBBBBBCCIAADDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBDDDDDAAAAAAAAAAAAAAAAAAAABBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBJBBBBBBBBBBBBBBBBBBBBHHHHHHHHHHHHHHHHDKLBBBBBBFFFFGGGGAECEBBEEBBAMHHAA".charAt(i2) - 'A') - 1;
        }
    }

    public CheckMethodAdapter(MethodVisitor methodVisitor) {
        this(methodVisitor, new HashMap());
    }

    public CheckMethodAdapter(MethodVisitor methodVisitor, Map<Label, Integer> map) {
        this(Opcodes.ASM5, methodVisitor, map);
        if (getClass() != CheckMethodAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected CheckMethodAdapter(int i2, MethodVisitor methodVisitor, Map<Label, Integer> map) {
        super(i2, methodVisitor);
        this.lastFrame = -1;
        this.labels = map;
        this.usedLabels = new HashSet();
        this.handlers = new ArrayList();
    }

    public CheckMethodAdapter(int i2, String str, String str2, final MethodVisitor methodVisitor, Map<Label, Integer> map) {
        this(new MethodNode(Opcodes.ASM5, i2, str, str2, null, null) { // from class: jdk.internal.org.objectweb.asm.util.CheckMethodAdapter.1
            @Override // jdk.internal.org.objectweb.asm.tree.MethodNode, jdk.internal.org.objectweb.asm.MethodVisitor
            public void visitEnd() {
                Analyzer analyzer = new Analyzer(new BasicVerifier());
                try {
                    analyzer.analyze("dummy", this);
                    accept(methodVisitor);
                } catch (Exception e2) {
                    if ((e2 instanceof IndexOutOfBoundsException) && this.maxLocals == 0 && this.maxStack == 0) {
                        throw new RuntimeException("Data flow checking option requires valid, non zero maxLocals and maxStack values.");
                    }
                    e2.printStackTrace();
                    StringWriter stringWriter = new StringWriter();
                    PrintWriter printWriter = new PrintWriter((Writer) stringWriter, true);
                    CheckClassAdapter.printAnalyzerResult(this, analyzer, printWriter);
                    printWriter.close();
                    throw new RuntimeException(e2.getMessage() + ' ' + stringWriter.toString());
                }
            }
        }, map);
        this.access = i2;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitParameter(String str, int i2) {
        if (str != null) {
            checkUnqualifiedName(this.version, str, "name");
        }
        CheckClassAdapter.checkAccess(i2, 36880);
        super.visitParameter(str, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        checkEndMethod();
        checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        checkEndMethod();
        int i3 = i2 >>> 24;
        if (i3 != 1 && i3 != 18 && i3 != 20 && i3 != 21 && i3 != 22 && i3 != 23) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i3));
        }
        CheckClassAdapter.checkTypeRefAndPath(i2, typePath);
        checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        checkEndMethod();
        return new CheckAnnotationAdapter(super.visitAnnotationDefault(), false);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int i2, String str, boolean z2) {
        checkEndMethod();
        checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitParameterAnnotation(i2, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        checkEndMethod();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitCode() {
        if ((this.access & 1024) != 0) {
            throw new RuntimeException("Abstract methods cannot have code");
        }
        this.startCode = true;
        super.visitCode();
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFrame(int i2, int i3, Object[] objArr, int i4, Object[] objArr2) {
        int i5;
        int i6;
        if (this.insnCount == this.lastFrame) {
            throw new IllegalStateException("At most one frame can be visited at a given code location.");
        }
        this.lastFrame = this.insnCount;
        switch (i2) {
            case -1:
            case 0:
                i5 = Integer.MAX_VALUE;
                i6 = Integer.MAX_VALUE;
                break;
            case 1:
            case 2:
                i5 = 3;
                i6 = 0;
                break;
            case 3:
                i5 = 0;
                i6 = 0;
                break;
            case 4:
                i5 = 0;
                i6 = 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid frame type " + i2);
        }
        if (i3 > i5) {
            throw new IllegalArgumentException("Invalid nLocal=" + i3 + " for frame type " + i2);
        }
        if (i4 > i6) {
            throw new IllegalArgumentException("Invalid nStack=" + i4 + " for frame type " + i2);
        }
        if (i2 != 2) {
            if (i3 > 0 && (objArr == null || objArr.length < i3)) {
                throw new IllegalArgumentException("Array local[] is shorter than nLocal");
            }
            for (int i7 = 0; i7 < i3; i7++) {
                checkFrameValue(objArr[i7]);
            }
        }
        if (i4 > 0 && (objArr2 == null || objArr2.length < i4)) {
            throw new IllegalArgumentException("Array stack[] is shorter than nStack");
        }
        for (int i8 = 0; i8 < i4; i8++) {
            checkFrameValue(objArr2[i8]);
        }
        if (i2 == -1) {
            this.expandedFrames++;
        } else {
            this.compressedFrames++;
        }
        if (this.expandedFrames > 0 && this.compressedFrames > 0) {
            throw new RuntimeException("Expanded and compressed frames must not be mixed.");
        }
        super.visitFrame(i2, i3, objArr, i4, objArr2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInsn(int i2) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 0);
        super.visitInsn(i2);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int i2, int i3) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 1);
        switch (i2) {
            case 16:
                checkSignedByte(i3, "Invalid operand");
                break;
            case 17:
                checkSignedShort(i3, "Invalid operand");
                break;
            default:
                if (i3 < 4 || i3 > 11) {
                    throw new IllegalArgumentException("Invalid operand (must be an array type code T_...): " + i3);
                }
                break;
        }
        super.visitIntInsn(i2, i3);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int i2, int i3) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 2);
        checkUnsignedShort(i3, "Invalid variable index");
        super.visitVarInsn(i2, i3);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int i2, String str) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 3);
        checkInternalName(str, "type");
        if (i2 == 187 && str.charAt(0) == '[') {
            throw new IllegalArgumentException("NEW cannot be used to create arrays: " + str);
        }
        super.visitTypeInsn(i2, str);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int i2, String str, String str2, String str3) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 4);
        checkInternalName(str, "owner");
        checkUnqualifiedName(this.version, str2, "name");
        checkDesc(str3, false);
        super.visitFieldInsn(i2, str, str2, str3);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    @Deprecated
    public void visitMethodInsn(int i2, String str, String str2, String str3) {
        if (this.api >= 327680) {
            super.visitMethodInsn(i2, str, str2, str3);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, i2 == 185);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        if (this.api < 327680) {
            super.visitMethodInsn(i2, str, str2, str3, z2);
        } else {
            doVisitMethodInsn(i2, str, str2, str3, z2);
        }
    }

    private void doVisitMethodInsn(int i2, String str, String str2, String str3, boolean z2) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 5);
        if (i2 != 183 || !Constants.CONSTRUCTOR_NAME.equals(str2)) {
            checkMethodIdentifier(this.version, str2, "name");
        }
        checkInternalName(str, "owner");
        checkMethodDesc(str3);
        if (i2 == 182 && z2) {
            throw new IllegalArgumentException("INVOKEVIRTUAL can't be used with interfaces");
        }
        if (i2 == 185 && !z2) {
            throw new IllegalArgumentException("INVOKEINTERFACE can't be used with classes");
        }
        if (this.mv != null) {
            this.mv.visitMethodInsn(i2, str, str2, str3, z2);
        }
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String str, String str2, Handle handle, Object... objArr) {
        checkStartCode();
        checkEndCode();
        checkMethodIdentifier(this.version, str, "name");
        checkMethodDesc(str2);
        if (handle.getTag() != 6 && handle.getTag() != 8) {
            throw new IllegalArgumentException("invalid handle tag " + handle.getTag());
        }
        for (Object obj : objArr) {
            checkLDCConstant(obj);
        }
        super.visitInvokeDynamicInsn(str, str2, handle, objArr);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int i2, Label label) {
        checkStartCode();
        checkEndCode();
        checkOpcode(i2, 6);
        checkLabel(label, false, "label");
        checkNonDebugLabel(label);
        super.visitJumpInsn(i2, label);
        this.usedLabels.add(label);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        checkStartCode();
        checkEndCode();
        checkLabel(label, false, "label");
        if (this.labels.get(label) != null) {
            throw new IllegalArgumentException("Already visited label");
        }
        this.labels.put(label, Integer.valueOf(this.insnCount));
        super.visitLabel(label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object obj) {
        checkStartCode();
        checkEndCode();
        checkLDCConstant(obj);
        super.visitLdcInsn(obj);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int i2, int i3) {
        checkStartCode();
        checkEndCode();
        checkUnsignedShort(i2, "Invalid variable index");
        checkSignedShort(i3, "Invalid increment");
        super.visitIincInsn(i2, i3);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int i2, int i3, Label label, Label... labelArr) {
        checkStartCode();
        checkEndCode();
        if (i3 < i2) {
            throw new IllegalArgumentException("Max = " + i3 + " must be greater than or equal to min = " + i2);
        }
        checkLabel(label, false, "default label");
        checkNonDebugLabel(label);
        if (labelArr == null || labelArr.length != (i3 - i2) + 1) {
            throw new IllegalArgumentException("There must be max - min + 1 labels");
        }
        for (int i4 = 0; i4 < labelArr.length; i4++) {
            checkLabel(labelArr[i4], false, "label at index " + i4);
            checkNonDebugLabel(labelArr[i4]);
        }
        super.visitTableSwitchInsn(i2, i3, label, labelArr);
        for (Label label2 : labelArr) {
            this.usedLabels.add(label2);
        }
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label label, int[] iArr, Label[] labelArr) {
        checkEndCode();
        checkStartCode();
        checkLabel(label, false, "default label");
        checkNonDebugLabel(label);
        if (iArr == null || labelArr == null || iArr.length != labelArr.length) {
            throw new IllegalArgumentException("There must be the same number of keys and labels");
        }
        for (int i2 = 0; i2 < labelArr.length; i2++) {
            checkLabel(labelArr[i2], false, "label at index " + i2);
            checkNonDebugLabel(labelArr[i2]);
        }
        super.visitLookupSwitchInsn(label, iArr, labelArr);
        this.usedLabels.add(label);
        for (Label label2 : labelArr) {
            this.usedLabels.add(label2);
        }
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String str, int i2) {
        checkStartCode();
        checkEndCode();
        checkDesc(str, false);
        if (str.charAt(0) != '[') {
            throw new IllegalArgumentException("Invalid descriptor (must be an array type descriptor): " + str);
        }
        if (i2 < 1) {
            throw new IllegalArgumentException("Invalid dimensions (must be greater than 0): " + i2);
        }
        if (i2 > str.lastIndexOf(91) + 1) {
            throw new IllegalArgumentException("Invalid dimensions (must not be greater than dims(desc)): " + i2);
        }
        super.visitMultiANewArrayInsn(str, i2);
        this.insnCount++;
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        checkStartCode();
        checkEndCode();
        int i3 = i2 >>> 24;
        if (i3 != 67 && i3 != 68 && i3 != 69 && i3 != 70 && i3 != 71 && i3 != 72 && i3 != 73 && i3 != 74 && i3 != 75) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i3));
        }
        CheckClassAdapter.checkTypeRefAndPath(i2, typePath);
        checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitInsnAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label label, Label label2, Label label3, String str) {
        checkStartCode();
        checkEndCode();
        checkLabel(label, false, "start label");
        checkLabel(label2, false, "end label");
        checkLabel(label3, false, "handler label");
        checkNonDebugLabel(label);
        checkNonDebugLabel(label2);
        checkNonDebugLabel(label3);
        if (this.labels.get(label) != null || this.labels.get(label2) != null || this.labels.get(label3) != null) {
            throw new IllegalStateException("Try catch blocks must be visited before their labels");
        }
        if (str != null) {
            checkInternalName(str, "type");
        }
        super.visitTryCatchBlock(label, label2, label3, str);
        this.handlers.add(label);
        this.handlers.add(label2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        checkStartCode();
        checkEndCode();
        int i3 = i2 >>> 24;
        if (i3 != 66) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i3));
        }
        CheckClassAdapter.checkTypeRefAndPath(i2, typePath);
        checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitTryCatchAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String str, String str2, String str3, Label label, Label label2, int i2) {
        checkStartCode();
        checkEndCode();
        checkUnqualifiedName(this.version, str, "name");
        checkDesc(str2, false);
        checkLabel(label, true, "start label");
        checkLabel(label2, true, "end label");
        checkUnsignedShort(i2, "Invalid variable index");
        if (this.labels.get(label2).intValue() < this.labels.get(label).intValue()) {
            throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
        }
        super.visitLocalVariable(str, str2, str3, label, label2, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int i2, TypePath typePath, Label[] labelArr, Label[] labelArr2, int[] iArr, String str, boolean z2) {
        checkStartCode();
        checkEndCode();
        int i3 = i2 >>> 24;
        if (i3 != 64 && i3 != 65) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i3));
        }
        CheckClassAdapter.checkTypeRefAndPath(i2, typePath);
        checkDesc(str, false);
        if (labelArr == null || labelArr2 == null || iArr == null || labelArr2.length != labelArr.length || iArr.length != labelArr.length) {
            throw new IllegalArgumentException("Invalid start, end and index arrays (must be non null and of identical length");
        }
        for (int i4 = 0; i4 < labelArr.length; i4++) {
            checkLabel(labelArr[i4], true, "start label");
            checkLabel(labelArr2[i4], true, "end label");
            checkUnsignedShort(iArr[i4], "Invalid variable index");
            if (this.labels.get(labelArr2[i4]).intValue() < this.labels.get(labelArr[i4]).intValue()) {
                throw new IllegalArgumentException("Invalid start and end labels (end must be greater than start)");
            }
        }
        return super.visitLocalVariableAnnotation(i2, typePath, labelArr, labelArr2, iArr, str, z2);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int i2, Label label) {
        checkStartCode();
        checkEndCode();
        checkUnsignedShort(i2, "Invalid line number");
        checkLabel(label, true, "start label");
        super.visitLineNumber(i2, label);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitMaxs(int i2, int i3) {
        checkStartCode();
        checkEndCode();
        this.endCode = true;
        Iterator<Label> it = this.usedLabels.iterator();
        while (it.hasNext()) {
            if (this.labels.get(it.next()) == null) {
                throw new IllegalStateException("Undefined label used");
            }
        }
        int i4 = 0;
        while (i4 < this.handlers.size()) {
            int i5 = i4;
            int i6 = i4 + 1;
            Integer num = this.labels.get(this.handlers.get(i5));
            i4 = i6 + 1;
            Integer num2 = this.labels.get(this.handlers.get(i6));
            if (num == null || num2 == null) {
                throw new IllegalStateException("Undefined try catch block labels");
            }
            if (num2.intValue() <= num.intValue()) {
                throw new IllegalStateException("Emty try catch block handler range");
            }
        }
        checkUnsignedShort(i2, "Invalid max stack");
        checkUnsignedShort(i3, "Invalid max locals");
        super.visitMaxs(i2, i3);
    }

    @Override // jdk.internal.org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        checkEndMethod();
        this.endMethod = true;
        super.visitEnd();
    }

    void checkStartCode() {
        if (!this.startCode) {
            throw new IllegalStateException("Cannot visit instructions before visitCode has been called.");
        }
    }

    void checkEndCode() {
        if (this.endCode) {
            throw new IllegalStateException("Cannot visit instructions after visitMaxs has been called.");
        }
    }

    void checkEndMethod() {
        if (this.endMethod) {
            throw new IllegalStateException("Cannot visit elements after visitEnd has been called.");
        }
    }

    void checkFrameValue(Object obj) {
        if (obj == Opcodes.TOP || obj == Opcodes.INTEGER || obj == Opcodes.FLOAT || obj == Opcodes.LONG || obj == Opcodes.DOUBLE || obj == Opcodes.NULL || obj == Opcodes.UNINITIALIZED_THIS) {
            return;
        }
        if (obj instanceof String) {
            checkInternalName((String) obj, "Invalid stack frame value");
        } else {
            if (!(obj instanceof Label)) {
                throw new IllegalArgumentException("Invalid stack frame value: " + obj);
            }
            this.usedLabels.add((Label) obj);
        }
    }

    static void checkOpcode(int i2, int i3) {
        if (i2 < 0 || i2 > 199 || TYPE[i2] != i3) {
            throw new IllegalArgumentException("Invalid opcode: " + i2);
        }
    }

    static void checkSignedByte(int i2, String str) {
        if (i2 < -128 || i2 > 127) {
            throw new IllegalArgumentException(str + " (must be a signed byte): " + i2);
        }
    }

    static void checkSignedShort(int i2, String str) {
        if (i2 < -32768 || i2 > 32767) {
            throw new IllegalArgumentException(str + " (must be a signed short): " + i2);
        }
    }

    static void checkUnsignedShort(int i2, String str) {
        if (i2 < 0 || i2 > 65535) {
            throw new IllegalArgumentException(str + " (must be an unsigned short): " + i2);
        }
    }

    static void checkConstant(Object obj) {
        if (!(obj instanceof Integer) && !(obj instanceof Float) && !(obj instanceof Long) && !(obj instanceof Double) && !(obj instanceof String)) {
            throw new IllegalArgumentException("Invalid constant: " + obj);
        }
    }

    void checkLDCConstant(Object obj) {
        if (!(obj instanceof Type)) {
            if (obj instanceof Handle) {
                if ((this.version & 65535) < 51) {
                    throw new IllegalArgumentException("ldc of a handle requires at least version 1.7");
                }
                int tag = ((Handle) obj).getTag();
                if (tag < 1 || tag > 9) {
                    throw new IllegalArgumentException("invalid handle tag " + tag);
                }
                return;
            }
            checkConstant(obj);
            return;
        }
        int sort = ((Type) obj).getSort();
        if (sort != 10 && sort != 9 && sort != 11) {
            throw new IllegalArgumentException("Illegal LDC constant value");
        }
        if (sort != 11 && (this.version & 65535) < 49) {
            throw new IllegalArgumentException("ldc of a constant class requires at least version 1.5");
        }
        if (sort == 11 && (this.version & 65535) < 51) {
            throw new IllegalArgumentException("ldc of a method type requires at least version 1.7");
        }
    }

    static void checkUnqualifiedName(int i2, String str, String str2) {
        if ((i2 & 65535) < 49) {
            checkIdentifier(str, str2);
            return;
        }
        for (int i3 = 0; i3 < str.length(); i3++) {
            if (".;[/".indexOf(str.charAt(i3)) != -1) {
                throw new IllegalArgumentException("Invalid " + str2 + " (must be a valid unqualified name): " + str);
            }
        }
    }

    static void checkIdentifier(String str, String str2) {
        checkIdentifier(str, 0, -1, str2);
    }

    static void checkIdentifier(String str, int i2, int i3, String str2) {
        if (str == null || (i3 != -1 ? i3 <= i2 : str.length() <= i2)) {
            throw new IllegalArgumentException("Invalid " + str2 + " (must not be null or empty)");
        }
        if (!Character.isJavaIdentifierStart(str.charAt(i2))) {
            throw new IllegalArgumentException("Invalid " + str2 + " (must be a valid Java identifier): " + str);
        }
        int length = i3 == -1 ? str.length() : i3;
        for (int i4 = i2 + 1; i4 < length; i4++) {
            if (!Character.isJavaIdentifierPart(str.charAt(i4))) {
                throw new IllegalArgumentException("Invalid " + str2 + " (must be a valid Java identifier): " + str);
            }
        }
    }

    static void checkMethodIdentifier(int i2, String str, String str2) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid " + str2 + " (must not be null or empty)");
        }
        if ((i2 & 65535) >= 49) {
            for (int i3 = 0; i3 < str.length(); i3++) {
                if (".;[/<>".indexOf(str.charAt(i3)) != -1) {
                    throw new IllegalArgumentException("Invalid " + str2 + " (must be a valid unqualified name): " + str);
                }
            }
            return;
        }
        if (!Character.isJavaIdentifierStart(str.charAt(0))) {
            throw new IllegalArgumentException("Invalid " + str2 + " (must be a '<init>', '<clinit>' or a valid Java identifier): " + str);
        }
        for (int i4 = 1; i4 < str.length(); i4++) {
            if (!Character.isJavaIdentifierPart(str.charAt(i4))) {
                throw new IllegalArgumentException("Invalid " + str2 + " (must be '<init>' or '<clinit>' or a valid Java identifier): " + str);
            }
        }
    }

    static void checkInternalName(String str, String str2) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid " + str2 + " (must not be null or empty)");
        }
        if (str.charAt(0) == '[') {
            checkDesc(str, false);
        } else {
            checkInternalName(str, 0, -1, str2);
        }
    }

    static void checkInternalName(String str, int i2, int i3, String str2) {
        int iIndexOf;
        int length = i3 == -1 ? str.length() : i3;
        int i4 = i2;
        do {
            try {
                iIndexOf = str.indexOf(47, i4 + 1);
                if (iIndexOf == -1 || iIndexOf > length) {
                    iIndexOf = length;
                }
                checkIdentifier(str, i4, iIndexOf, null);
                i4 = iIndexOf + 1;
            } catch (IllegalArgumentException e2) {
                throw new IllegalArgumentException("Invalid " + str2 + " (must be a fully qualified class name in internal form): " + str);
            }
        } while (iIndexOf != length);
    }

    static void checkDesc(String str, boolean z2) {
        if (checkDesc(str, 0, z2) != str.length()) {
            throw new IllegalArgumentException("Invalid descriptor: " + str);
        }
    }

    static int checkDesc(String str, int i2, boolean z2) {
        if (str == null || i2 >= str.length()) {
            throw new IllegalArgumentException("Invalid type descriptor (must not be null or empty)");
        }
        switch (str.charAt(i2)) {
            case 'B':
            case 'C':
            case 'D':
            case 'F':
            case 'I':
            case 'J':
            case 'S':
            case 'Z':
                return i2 + 1;
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new IllegalArgumentException("Invalid descriptor: " + str);
            case 'L':
                int iIndexOf = str.indexOf(59, i2);
                if (iIndexOf == -1 || iIndexOf - i2 < 2) {
                    throw new IllegalArgumentException("Invalid descriptor: " + str);
                }
                try {
                    checkInternalName(str, i2 + 1, iIndexOf, null);
                    return iIndexOf + 1;
                } catch (IllegalArgumentException e2) {
                    throw new IllegalArgumentException("Invalid descriptor: " + str);
                }
            case 'V':
                if (z2) {
                    return i2 + 1;
                }
                throw new IllegalArgumentException("Invalid descriptor: " + str);
            case '[':
                int i3 = i2 + 1;
                while (i3 < str.length() && str.charAt(i3) == '[') {
                    i3++;
                }
                if (i3 < str.length()) {
                    return checkDesc(str, i3, false);
                }
                throw new IllegalArgumentException("Invalid descriptor: " + str);
        }
    }

    static void checkMethodDesc(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException("Invalid method descriptor (must not be null or empty)");
        }
        if (str.charAt(0) != '(' || str.length() < 3) {
            throw new IllegalArgumentException("Invalid descriptor: " + str);
        }
        int iCheckDesc = 1;
        if (str.charAt(1) != ')') {
            while (str.charAt(iCheckDesc) != 'V') {
                iCheckDesc = checkDesc(str, iCheckDesc, false);
                if (iCheckDesc >= str.length() || str.charAt(iCheckDesc) == ')') {
                }
            }
            throw new IllegalArgumentException("Invalid descriptor: " + str);
        }
        if (checkDesc(str, iCheckDesc + 1, true) != str.length()) {
            throw new IllegalArgumentException("Invalid descriptor: " + str);
        }
    }

    void checkLabel(Label label, boolean z2, String str) {
        if (label == null) {
            throw new IllegalArgumentException("Invalid " + str + " (must not be null)");
        }
        if (z2 && this.labels.get(label) == null) {
            throw new IllegalArgumentException("Invalid " + str + " (must be visited first)");
        }
    }

    private static void checkNonDebugLabel(Label label) {
        int iIntValue;
        Field labelStatusField2 = getLabelStatusField();
        if (labelStatusField2 == null) {
            iIntValue = 0;
        } else {
            try {
                iIntValue = ((Integer) labelStatusField2.get(label)).intValue();
            } catch (IllegalAccessException e2) {
                throw new Error("Internal error");
            }
        }
        if ((iIntValue & 1) != 0) {
            throw new IllegalArgumentException("Labels used for debug info cannot be reused for control flow");
        }
    }

    private static Field getLabelStatusField() {
        if (labelStatusField == null) {
            labelStatusField = getLabelField("a");
            if (labelStatusField == null) {
                labelStatusField = getLabelField("status");
            }
        }
        return labelStatusField;
    }

    private static Field getLabelField(String str) throws SecurityException {
        try {
            Field declaredField = Label.class.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (NoSuchFieldException e2) {
            return null;
        }
    }
}
