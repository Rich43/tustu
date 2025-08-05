package jdk.internal.org.objectweb.asm.util;

import com.sun.org.apache.bcel.internal.Constants;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import jdk.internal.org.objectweb.asm.AnnotationVisitor;
import jdk.internal.org.objectweb.asm.Attribute;
import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassVisitor;
import jdk.internal.org.objectweb.asm.FieldVisitor;
import jdk.internal.org.objectweb.asm.Label;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.TypePath;
import jdk.internal.org.objectweb.asm.tree.ClassNode;
import jdk.internal.org.objectweb.asm.tree.MethodNode;
import jdk.internal.org.objectweb.asm.tree.analysis.Analyzer;
import jdk.internal.org.objectweb.asm.tree.analysis.BasicValue;
import jdk.internal.org.objectweb.asm.tree.analysis.Frame;
import jdk.internal.org.objectweb.asm.tree.analysis.SimpleVerifier;
import jdk.nashorn.internal.runtime.regexp.joni.Config;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/CheckClassAdapter.class */
public class CheckClassAdapter extends ClassVisitor {
    private int version;
    private boolean start;
    private boolean source;
    private boolean outer;
    private boolean end;
    private Map<Label, Integer> labels;
    private boolean checkDataFlow;

    public static void main(String[] strArr) throws Exception {
        ClassReader classReader;
        if (strArr.length != 1) {
            System.err.println("Verifies the given class.");
            System.err.println("Usage: CheckClassAdapter <fully qualified class name or class file name>");
        } else {
            if (strArr[0].endsWith(".class")) {
                classReader = new ClassReader(new FileInputStream(strArr[0]));
            } else {
                classReader = new ClassReader(strArr[0]);
            }
            verify(classReader, false, new PrintWriter(System.err));
        }
    }

    public static void verify(ClassReader classReader, ClassLoader classLoader, boolean z2, PrintWriter printWriter) {
        ClassNode classNode = new ClassNode();
        classReader.accept(new CheckClassAdapter(classNode, false), 2);
        Type objectType = classNode.superName == null ? null : Type.getObjectType(classNode.superName);
        List<MethodNode> list = classNode.methods;
        ArrayList arrayList = new ArrayList();
        Iterator<String> it = classNode.interfaces.iterator();
        while (it.hasNext()) {
            arrayList.add(Type.getObjectType(it.next()));
        }
        for (int i2 = 0; i2 < list.size(); i2++) {
            MethodNode methodNode = list.get(i2);
            SimpleVerifier simpleVerifier = new SimpleVerifier(Type.getObjectType(classNode.name), objectType, arrayList, (classNode.access & 512) != 0);
            Analyzer analyzer = new Analyzer(simpleVerifier);
            if (classLoader != null) {
                simpleVerifier.setClassLoader(classLoader);
            }
            try {
                analyzer.analyze(classNode.name, methodNode);
            } catch (Exception e2) {
                e2.printStackTrace(printWriter);
            }
            if (z2) {
                printAnalyzerResult(methodNode, analyzer, printWriter);
            }
        }
        printWriter.flush();
    }

    public static void verify(ClassReader classReader, boolean z2, PrintWriter printWriter) {
        verify(classReader, null, z2, printWriter);
    }

    static void printAnalyzerResult(MethodNode methodNode, Analyzer<BasicValue> analyzer, PrintWriter printWriter) {
        Frame[] frames = analyzer.getFrames();
        Textifier textifier = new Textifier();
        TraceMethodVisitor traceMethodVisitor = new TraceMethodVisitor(textifier);
        printWriter.println(methodNode.name + methodNode.desc);
        for (int i2 = 0; i2 < methodNode.instructions.size(); i2++) {
            methodNode.instructions.get(i2).accept(traceMethodVisitor);
            StringBuilder sb = new StringBuilder();
            Frame frame = frames[i2];
            if (frame == null) {
                sb.append('?');
            } else {
                for (int i3 = 0; i3 < frame.getLocals(); i3++) {
                    sb.append(getShortName(((BasicValue) frame.getLocal(i3)).toString())).append(' ');
                }
                sb.append(" : ");
                for (int i4 = 0; i4 < frame.getStackSize(); i4++) {
                    sb.append(getShortName(((BasicValue) frame.getStack(i4)).toString())).append(' ');
                }
            }
            while (sb.length() < methodNode.maxStack + methodNode.maxLocals + 1) {
                sb.append(' ');
            }
            printWriter.print(Integer.toString(i2 + Config.MAX_REPEAT_NUM).substring(1));
            printWriter.print(" " + ((Object) sb) + " : " + textifier.text.get(textifier.text.size() - 1));
        }
        for (int i5 = 0; i5 < methodNode.tryCatchBlocks.size(); i5++) {
            methodNode.tryCatchBlocks.get(i5).accept(traceMethodVisitor);
            printWriter.print(" " + textifier.text.get(textifier.text.size() - 1));
        }
        printWriter.println();
    }

    private static String getShortName(String str) {
        int iLastIndexOf = str.lastIndexOf(47);
        int length = str.length();
        if (str.charAt(length - 1) == ';') {
            length--;
        }
        return iLastIndexOf == -1 ? str : str.substring(iLastIndexOf + 1, length);
    }

    public CheckClassAdapter(ClassVisitor classVisitor) {
        this(classVisitor, true);
    }

    public CheckClassAdapter(ClassVisitor classVisitor, boolean z2) {
        this(Opcodes.ASM5, classVisitor, z2);
        if (getClass() != CheckClassAdapter.class) {
            throw new IllegalStateException();
        }
    }

    protected CheckClassAdapter(int i2, ClassVisitor classVisitor, boolean z2) {
        super(i2, classVisitor);
        this.labels = new HashMap();
        this.checkDataFlow = z2;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visit(int i2, int i3, String str, String str2, String str3, String[] strArr) {
        if (this.start) {
            throw new IllegalStateException("visit must be called only once");
        }
        this.start = true;
        checkState();
        checkAccess(i3, 423473);
        if (str == null || !str.endsWith("package-info")) {
            CheckMethodAdapter.checkInternalName(str, "class name");
        }
        if ("java/lang/Object".equals(str)) {
            if (str3 != null) {
                throw new IllegalArgumentException("The super class name of the Object class must be 'null'");
            }
        } else {
            CheckMethodAdapter.checkInternalName(str3, "super class name");
        }
        if (str2 != null) {
            checkClassSignature(str2);
        }
        if ((i3 & 512) != 0 && !"java/lang/Object".equals(str3)) {
            throw new IllegalArgumentException("The super class name of interfaces must be 'java/lang/Object'");
        }
        if (strArr != null) {
            for (int i4 = 0; i4 < strArr.length; i4++) {
                CheckMethodAdapter.checkInternalName(strArr[i4], "interface name at index " + i4);
            }
        }
        this.version = i2;
        super.visit(i2, i3, str, str2, str3, strArr);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitSource(String str, String str2) {
        checkState();
        if (this.source) {
            throw new IllegalStateException("visitSource can be called only once.");
        }
        this.source = true;
        super.visitSource(str, str2);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String str, String str2, String str3) {
        checkState();
        if (this.outer) {
            throw new IllegalStateException("visitOuterClass can be called only once.");
        }
        this.outer = true;
        if (str == null) {
            throw new IllegalArgumentException("Illegal outer class owner");
        }
        if (str3 != null) {
            CheckMethodAdapter.checkMethodDesc(str3);
        }
        super.visitOuterClass(str, str2, str3);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String str, String str2, String str3, int i2) {
        checkState();
        CheckMethodAdapter.checkInternalName(str, "class name");
        if (str2 != null) {
            CheckMethodAdapter.checkInternalName(str2, "outer class name");
        }
        if (str3 != null) {
            int i3 = 0;
            while (i3 < str3.length() && Character.isDigit(str3.charAt(i3))) {
                i3++;
            }
            if (i3 == 0 || i3 < str3.length()) {
                CheckMethodAdapter.checkIdentifier(str3, i3, -1, "inner class name");
            }
        }
        checkAccess(i2, 30239);
        super.visitInnerClass(str, str2, str3, i2);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int i2, String str, String str2, String str3, Object obj) {
        checkState();
        checkAccess(i2, 413919);
        CheckMethodAdapter.checkUnqualifiedName(this.version, str, "field name");
        CheckMethodAdapter.checkDesc(str2, false);
        if (str3 != null) {
            checkFieldSignature(str3);
        }
        if (obj != null) {
            CheckMethodAdapter.checkConstant(obj);
        }
        return new CheckFieldAdapter(super.visitField(i2, str, str2, str3, obj));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int i2, String str, String str2, String str3, String[] strArr) {
        CheckMethodAdapter checkMethodAdapter;
        checkState();
        checkAccess(i2, 400895);
        if (!Constants.CONSTRUCTOR_NAME.equals(str) && !Constants.STATIC_INITIALIZER_NAME.equals(str)) {
            CheckMethodAdapter.checkMethodIdentifier(this.version, str, "method name");
        }
        CheckMethodAdapter.checkMethodDesc(str2);
        if (str3 != null) {
            checkMethodSignature(str3);
        }
        if (strArr != null) {
            for (int i3 = 0; i3 < strArr.length; i3++) {
                CheckMethodAdapter.checkInternalName(strArr[i3], "exception name at index " + i3);
            }
        }
        if (this.checkDataFlow) {
            checkMethodAdapter = new CheckMethodAdapter(i2, str, str2, super.visitMethod(i2, str, str2, str3, strArr), this.labels);
        } else {
            checkMethodAdapter = new CheckMethodAdapter(super.visitMethod(i2, str, str2, str3, strArr), this.labels);
        }
        checkMethodAdapter.version = this.version;
        return checkMethodAdapter;
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String str, boolean z2) {
        checkState();
        CheckMethodAdapter.checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitTypeAnnotation(int i2, TypePath typePath, String str, boolean z2) {
        checkState();
        int i3 = i2 >>> 24;
        if (i3 != 0 && i3 != 17 && i3 != 16) {
            throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i3));
        }
        checkTypeRefAndPath(i2, typePath);
        CheckMethodAdapter.checkDesc(str, false);
        return new CheckAnnotationAdapter(super.visitTypeAnnotation(i2, typePath, str, z2));
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        checkState();
        if (attribute == null) {
            throw new IllegalArgumentException("Invalid attribute (must not be null)");
        }
        super.visitAttribute(attribute);
    }

    @Override // jdk.internal.org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        checkState();
        this.end = true;
        super.visitEnd();
    }

    private void checkState() {
        if (!this.start) {
            throw new IllegalStateException("Cannot visit member before visit has been called.");
        }
        if (this.end) {
            throw new IllegalStateException("Cannot visit member after visitEnd has been called.");
        }
    }

    static void checkAccess(int i2, int i3) {
        if ((i2 & (i3 ^ (-1))) != 0) {
            throw new IllegalArgumentException("Invalid access flags: " + i2);
        }
        if (((i2 & 1) == 0 ? 0 : 1) + ((i2 & 2) == 0 ? 0 : 1) + ((i2 & 4) == 0 ? 0 : 1) > 1) {
            throw new IllegalArgumentException("public private and protected are mutually exclusive: " + i2);
        }
        if (((i2 & 16) == 0 ? 0 : 1) + ((i2 & 1024) == 0 ? 0 : 1) > 1) {
            throw new IllegalArgumentException("final and abstract are mutually exclusive: " + i2);
        }
    }

    public static void checkClassSignature(String str) {
        int i2;
        int iCheckFormalTypeParameters = 0;
        if (getChar(str, 0) == '<') {
            iCheckFormalTypeParameters = checkFormalTypeParameters(str, 0);
        }
        int iCheckClassTypeSignature = checkClassTypeSignature(str, iCheckFormalTypeParameters);
        while (true) {
            i2 = iCheckClassTypeSignature;
            if (getChar(str, i2) != 'L') {
                break;
            } else {
                iCheckClassTypeSignature = checkClassTypeSignature(str, i2);
            }
        }
        if (i2 != str.length()) {
            throw new IllegalArgumentException(str + ": error at index " + i2);
        }
    }

    public static void checkMethodSignature(String str) {
        int i2;
        int iCheckTypeSignature;
        int iCheckFormalTypeParameters = 0;
        if (getChar(str, 0) == '<') {
            iCheckFormalTypeParameters = checkFormalTypeParameters(str, 0);
        }
        int iCheckChar = checkChar('(', str, iCheckFormalTypeParameters);
        while (true) {
            i2 = iCheckChar;
            if ("ZCBSIFJDL[T".indexOf(getChar(str, i2)) == -1) {
                break;
            } else {
                iCheckChar = checkTypeSignature(str, i2);
            }
        }
        int iCheckChar2 = checkChar(')', str, i2);
        if (getChar(str, iCheckChar2) == 'V') {
            iCheckTypeSignature = iCheckChar2 + 1;
        } else {
            iCheckTypeSignature = checkTypeSignature(str, iCheckChar2);
        }
        while (getChar(str, iCheckTypeSignature) == '^') {
            int i3 = iCheckTypeSignature + 1;
            if (getChar(str, i3) == 'L') {
                iCheckTypeSignature = checkClassTypeSignature(str, i3);
            } else {
                iCheckTypeSignature = checkTypeVariableSignature(str, i3);
            }
        }
        if (iCheckTypeSignature != str.length()) {
            throw new IllegalArgumentException(str + ": error at index " + iCheckTypeSignature);
        }
    }

    public static void checkFieldSignature(String str) {
        int iCheckFieldTypeSignature = checkFieldTypeSignature(str, 0);
        if (iCheckFieldTypeSignature != str.length()) {
            throw new IllegalArgumentException(str + ": error at index " + iCheckFieldTypeSignature);
        }
    }

    static void checkTypeRefAndPath(int i2, TypePath typePath) {
        int i3;
        switch (i2 >>> 24) {
            case 0:
            case 1:
            case 22:
                i3 = -65536;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
            case 50:
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            default:
                throw new IllegalArgumentException("Invalid type reference sort 0x" + Integer.toHexString(i2 >>> 24));
            case 16:
            case 17:
            case 18:
            case 23:
            case 66:
                i3 = -256;
                break;
            case 19:
            case 20:
            case 21:
            case 64:
            case 65:
            case 67:
            case 68:
            case 69:
            case 70:
                i3 = -16777216;
                break;
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
                i3 = -16776961;
                break;
        }
        if ((i2 & (i3 ^ (-1))) != 0) {
            throw new IllegalArgumentException("Invalid type reference 0x" + Integer.toHexString(i2));
        }
        if (typePath != null) {
            for (int i4 = 0; i4 < typePath.getLength(); i4++) {
                int step = typePath.getStep(i4);
                if (step != 0 && step != 1 && step != 3 && step != 2) {
                    throw new IllegalArgumentException("Invalid type path step " + i4 + " in " + ((Object) typePath));
                }
                if (step != 3 && typePath.getStepArgument(i4) != 0) {
                    throw new IllegalArgumentException("Invalid type path step argument for step " + i4 + " in " + ((Object) typePath));
                }
            }
        }
    }

    private static int checkFormalTypeParameters(String str, int i2) {
        int iCheckFormalTypeParameter = checkFormalTypeParameter(str, checkChar('<', str, i2));
        while (true) {
            int i3 = iCheckFormalTypeParameter;
            if (getChar(str, i3) != '>') {
                iCheckFormalTypeParameter = checkFormalTypeParameter(str, i3);
            } else {
                return i3 + 1;
            }
        }
    }

    private static int checkFormalTypeParameter(String str, int i2) {
        int iCheckChar = checkChar(':', str, checkIdentifier(str, i2));
        if ("L[T".indexOf(getChar(str, iCheckChar)) != -1) {
            iCheckChar = checkFieldTypeSignature(str, iCheckChar);
        }
        while (getChar(str, iCheckChar) == ':') {
            iCheckChar = checkFieldTypeSignature(str, iCheckChar + 1);
        }
        return iCheckChar;
    }

    private static int checkFieldTypeSignature(String str, int i2) {
        switch (getChar(str, i2)) {
            case 'L':
                return checkClassTypeSignature(str, i2);
            case '[':
                return checkTypeSignature(str, i2 + 1);
            default:
                return checkTypeVariableSignature(str, i2);
        }
    }

    private static int checkClassTypeSignature(String str, int i2) {
        int iCheckIdentifier;
        int iCheckIdentifier2 = checkIdentifier(str, checkChar('L', str, i2));
        while (true) {
            iCheckIdentifier = iCheckIdentifier2;
            if (getChar(str, iCheckIdentifier) != '/') {
                break;
            }
            iCheckIdentifier2 = checkIdentifier(str, iCheckIdentifier + 1);
        }
        if (getChar(str, iCheckIdentifier) == '<') {
            iCheckIdentifier = checkTypeArguments(str, iCheckIdentifier);
        }
        while (getChar(str, iCheckIdentifier) == '.') {
            iCheckIdentifier = checkIdentifier(str, iCheckIdentifier + 1);
            if (getChar(str, iCheckIdentifier) == '<') {
                iCheckIdentifier = checkTypeArguments(str, iCheckIdentifier);
            }
        }
        return checkChar(';', str, iCheckIdentifier);
    }

    private static int checkTypeArguments(String str, int i2) {
        int iCheckTypeArgument = checkTypeArgument(str, checkChar('<', str, i2));
        while (true) {
            int i3 = iCheckTypeArgument;
            if (getChar(str, i3) != '>') {
                iCheckTypeArgument = checkTypeArgument(str, i3);
            } else {
                return i3 + 1;
            }
        }
    }

    private static int checkTypeArgument(String str, int i2) {
        char c2 = getChar(str, i2);
        if (c2 == '*') {
            return i2 + 1;
        }
        if (c2 == '+' || c2 == '-') {
            i2++;
        }
        return checkFieldTypeSignature(str, i2);
    }

    private static int checkTypeVariableSignature(String str, int i2) {
        return checkChar(';', str, checkIdentifier(str, checkChar('T', str, i2)));
    }

    private static int checkTypeSignature(String str, int i2) {
        switch (getChar(str, i2)) {
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
            case 'L':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'V':
            case 'W':
            case 'X':
            case 'Y':
            default:
                return checkFieldTypeSignature(str, i2);
        }
    }

    private static int checkIdentifier(String str, int i2) {
        if (!Character.isJavaIdentifierStart(getChar(str, i2))) {
            throw new IllegalArgumentException(str + ": identifier expected at index " + i2);
        }
        do {
            i2++;
        } while (Character.isJavaIdentifierPart(getChar(str, i2)));
        return i2;
    }

    private static int checkChar(char c2, String str, int i2) {
        if (getChar(str, i2) == c2) {
            return i2 + 1;
        }
        throw new IllegalArgumentException(str + ": '" + c2 + "' expected at index " + i2);
    }

    private static char getChar(String str, int i2) {
        if (i2 < str.length()) {
            return str.charAt(i2);
        }
        return (char) 0;
    }
}
