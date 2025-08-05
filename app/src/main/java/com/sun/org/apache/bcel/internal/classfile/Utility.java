package com.sun.org.apache.bcel.internal.classfile;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.org.apache.bcel.internal.util.ByteSequence;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayReader;
import java.io.CharArrayWriter;
import java.io.FilterReader;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javafx.fxml.FXMLLoader;
import jdk.internal.dynalink.CallSiteDescriptor;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Utility.class */
public abstract class Utility {
    private static int consumed_chars;
    private static final int FREE_CHARS = 48;
    private static final char ESCAPE_CHAR = '$';
    private static boolean wide = false;
    private static int[] CHAR_MAP = new int[48];
    private static int[] MAP_CHAR = new int[256];

    static {
        int j2 = 0;
        for (int i2 = 65; i2 <= 90; i2++) {
            CHAR_MAP[j2] = i2;
            MAP_CHAR[i2] = j2;
            j2++;
        }
        for (int i3 = 103; i3 <= 122; i3++) {
            CHAR_MAP[j2] = i3;
            MAP_CHAR[i3] = j2;
            j2++;
        }
        CHAR_MAP[j2] = 36;
        MAP_CHAR[36] = j2;
        int j3 = j2 + 1;
        CHAR_MAP[j3] = 95;
        MAP_CHAR[95] = j3;
    }

    public static final String accessToString(int access_flags) {
        return accessToString(access_flags, false);
    }

    public static final String accessToString(int access_flags, boolean for_class) {
        StringBuffer buf = new StringBuffer();
        int p2 = 0;
        int i2 = 0;
        while (p2 < 2048) {
            p2 = pow2(i2);
            if ((access_flags & p2) != 0 && (!for_class || (p2 != 32 && p2 != 512))) {
                buf.append(Constants.ACCESS_NAMES[i2] + " ");
            }
            i2++;
        }
        return buf.toString().trim();
    }

    public static final String classOrInterface(int access_flags) {
        return (access_flags & 512) != 0 ? "interface" : com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_CLASS;
    }

    public static final String codeToString(byte[] code, ConstantPool constant_pool, int index, int length, boolean verbose) {
        StringBuffer buf = new StringBuffer(code.length * 20);
        ByteSequence stream = new ByteSequence(code);
        for (int i2 = 0; i2 < index; i2++) {
            try {
                codeToString(stream, constant_pool, verbose);
            } catch (IOException e2) {
                System.out.println(buf.toString());
                e2.printStackTrace();
                throw new ClassFormatException("Byte code error: " + ((Object) e2));
            }
        }
        int i3 = 0;
        while (stream.available() > 0) {
            if (length < 0 || i3 < length) {
                String indices = fillup(stream.getIndex() + CallSiteDescriptor.TOKEN_DELIMITER, 6, true, ' ');
                buf.append(indices + codeToString(stream, constant_pool, verbose) + '\n');
            }
            i3++;
        }
        return buf.toString();
    }

    public static final String codeToString(byte[] code, ConstantPool constant_pool, int index, int length) {
        return codeToString(code, constant_pool, index, length, true);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:53:0x060f  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x062b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final java.lang.String codeToString(com.sun.org.apache.bcel.internal.util.ByteSequence r7, com.sun.org.apache.bcel.internal.classfile.ConstantPool r8, boolean r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 2333
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.bcel.internal.classfile.Utility.codeToString(com.sun.org.apache.bcel.internal.util.ByteSequence, com.sun.org.apache.bcel.internal.classfile.ConstantPool, boolean):java.lang.String");
    }

    public static final String codeToString(ByteSequence bytes, ConstantPool constant_pool) throws IOException {
        return codeToString(bytes, constant_pool, true);
    }

    public static final String compactClassName(String str) {
        return compactClassName(str, true);
    }

    public static final String compactClassName(String str, String prefix, boolean chopit) {
        int len = prefix.length();
        String str2 = str.replace('/', '.');
        if (chopit && str2.startsWith(prefix) && str2.substring(len).indexOf(46) == -1) {
            str2 = str2.substring(len);
        }
        return str2;
    }

    public static final String compactClassName(String str, boolean chopit) {
        return compactClassName(str, "java.lang.", chopit);
    }

    private static final boolean is_digit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    private static final boolean is_space(char ch) {
        return ch == ' ' || ch == '\t' || ch == '\r' || ch == '\n';
    }

    public static final int setBit(int flag, int i2) {
        return flag | pow2(i2);
    }

    public static final int clearBit(int flag, int i2) {
        int bit = pow2(i2);
        return (flag & bit) == 0 ? flag : flag ^ bit;
    }

    public static final boolean isSet(int flag, int i2) {
        return (flag & pow2(i2)) != 0;
    }

    public static final String methodTypeToSignature(String ret, String[] argv) throws ClassFormatException {
        StringBuffer buf = new StringBuffer("(");
        if (argv != null) {
            for (int i2 = 0; i2 < argv.length; i2++) {
                String str = getSignature(argv[i2]);
                if (str.endsWith("V")) {
                    throw new ClassFormatException("Invalid type: " + argv[i2]);
                }
                buf.append(str);
            }
        }
        buf.append(")" + getSignature(ret));
        return buf.toString();
    }

    public static final String[] methodSignatureArgumentTypes(String signature) throws ClassFormatException {
        return methodSignatureArgumentTypes(signature, true);
    }

    public static final String[] methodSignatureArgumentTypes(String signature, boolean chopit) throws ClassFormatException {
        ArrayList vec = new ArrayList();
        try {
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            for (int index = 1; signature.charAt(index) != ')'; index += consumed_chars) {
                vec.add(signatureToString(signature.substring(index), chopit));
            }
            String[] types = new String[vec.size()];
            vec.toArray(types);
            return types;
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
    }

    public static final String methodSignatureReturnType(String signature) throws ClassFormatException {
        return methodSignatureReturnType(signature, true);
    }

    public static final String methodSignatureReturnType(String signature, boolean chopit) throws ClassFormatException {
        try {
            int index = signature.lastIndexOf(41) + 1;
            String type = signatureToString(signature.substring(index), chopit);
            return type;
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
    }

    public static final String methodSignatureToString(String signature, String name, String access) {
        return methodSignatureToString(signature, name, access, true);
    }

    public static final String methodSignatureToString(String signature, String name, String access, boolean chopit) {
        return methodSignatureToString(signature, name, access, chopit, null);
    }

    public static final String methodSignatureToString(String signature, String name, String access, boolean chopit, LocalVariableTable vars) throws ClassFormatException {
        StringBuffer buf = new StringBuffer("(");
        int var_index = access.indexOf("static") >= 0 ? 0 : 1;
        try {
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            int index = 1;
            while (signature.charAt(index) != ')') {
                String param_type = signatureToString(signature.substring(index), chopit);
                buf.append(param_type);
                if (vars != null) {
                    LocalVariable l2 = vars.getLocalVariable(var_index);
                    if (l2 != null) {
                        buf.append(" " + l2.getName());
                    }
                } else {
                    buf.append(" arg" + var_index);
                }
                if (SchemaSymbols.ATTVAL_DOUBLE.equals(param_type) || SchemaSymbols.ATTVAL_LONG.equals(param_type)) {
                    var_index += 2;
                } else {
                    var_index++;
                }
                buf.append(", ");
                index += consumed_chars;
            }
            String type = signatureToString(signature.substring(index + 1), chopit);
            if (buf.length() > 1) {
                buf.setLength(buf.length() - 2);
            }
            buf.append(")");
            return access + (access.length() > 0 ? " " : "") + type + " " + name + buf.toString();
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
    }

    private static final int pow2(int n2) {
        return 1 << n2;
    }

    public static final String replace(String str, String old, String new_) {
        StringBuffer buf = new StringBuffer();
        try {
            if (str.indexOf(old) != -1) {
                int old_index = 0;
                while (true) {
                    int index = str.indexOf(old, old_index);
                    if (index == -1) {
                        break;
                    }
                    buf.append(str.substring(old_index, index));
                    buf.append(new_);
                    old_index = index + old.length();
                }
                buf.append(str.substring(old_index));
                str = buf.toString();
            }
        } catch (StringIndexOutOfBoundsException e2) {
            System.err.println(e2);
        }
        return str;
    }

    public static final String signatureToString(String signature) {
        return signatureToString(signature, true);
    }

    public static final String signatureToString(String signature, boolean chopit) {
        consumed_chars = 1;
        try {
            switch (signature.charAt(0)) {
                case 'B':
                    return SchemaSymbols.ATTVAL_BYTE;
                case 'C':
                    return "char";
                case 'D':
                    return SchemaSymbols.ATTVAL_DOUBLE;
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
                    throw new ClassFormatException("Invalid signature: `" + signature + PdfOps.SINGLE_QUOTE_TOKEN);
                case 'F':
                    return SchemaSymbols.ATTVAL_FLOAT;
                case 'I':
                    return "int";
                case 'J':
                    return SchemaSymbols.ATTVAL_LONG;
                case 'L':
                    int index = signature.indexOf(59);
                    if (index < 0) {
                        throw new ClassFormatException("Invalid signature: " + signature);
                    }
                    consumed_chars = index + 1;
                    return compactClassName(signature.substring(1, index), chopit);
                case 'S':
                    return SchemaSymbols.ATTVAL_SHORT;
                case 'V':
                    return "void";
                case 'Z':
                    return "boolean";
                case '[':
                    StringBuffer brackets = new StringBuffer();
                    int n2 = 0;
                    while (signature.charAt(n2) == '[') {
                        brackets.append("[]");
                        n2++;
                    }
                    int consumed_chars2 = n2;
                    String type = signatureToString(signature.substring(n2), chopit);
                    consumed_chars += consumed_chars2;
                    return type + brackets.toString();
            }
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid signature: " + ((Object) e2) + CallSiteDescriptor.TOKEN_DELIMITER + signature);
        }
    }

    public static String getSignature(String type) {
        StringBuffer buf = new StringBuffer();
        char[] chars = type.toCharArray();
        boolean char_found = false;
        boolean delim = false;
        int index = -1;
        int i2 = 0;
        while (true) {
            if (i2 < chars.length) {
                switch (chars[i2]) {
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                        if (char_found) {
                            delim = true;
                        } else {
                            continue;
                        }
                        i2++;
                    case '[':
                        if (!char_found) {
                            throw new RuntimeException("Illegal type: " + type);
                        }
                        index = i2;
                        break;
                    default:
                        char_found = true;
                        if (!delim) {
                            buf.append(chars[i2]);
                            continue;
                        }
                        i2++;
                }
            }
        }
        int brackets = 0;
        if (index > 0) {
            brackets = countBrackets(type.substring(index));
        }
        String type2 = buf.toString();
        buf.setLength(0);
        for (int i3 = 0; i3 < brackets; i3++) {
            buf.append('[');
        }
        boolean found = false;
        for (int i4 = 4; i4 <= 12 && !found; i4++) {
            if (Constants.TYPE_NAMES[i4].equals(type2)) {
                found = true;
                buf.append(Constants.SHORT_TYPE_NAMES[i4]);
            }
        }
        if (!found) {
            buf.append('L' + type2.replace('.', '/') + ';');
        }
        return buf.toString();
    }

    private static int countBrackets(String brackets) {
        char[] chars = brackets.toCharArray();
        int count = 0;
        boolean open = false;
        for (char c2 : chars) {
            switch (c2) {
                case '[':
                    if (open) {
                        throw new RuntimeException("Illegally nested brackets:" + brackets);
                    }
                    open = true;
                    break;
                case ']':
                    if (!open) {
                        throw new RuntimeException("Illegally nested brackets:" + brackets);
                    }
                    open = false;
                    count++;
                    break;
            }
        }
        if (open) {
            throw new RuntimeException("Illegally nested brackets:" + brackets);
        }
        return count;
    }

    public static final byte typeOfMethodSignature(String signature) throws ClassFormatException {
        try {
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            int index = signature.lastIndexOf(41) + 1;
            return typeOfSignature(signature.substring(index));
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
    }

    public static final byte typeOfSignature(String signature) throws ClassFormatException {
        try {
            switch (signature.charAt(0)) {
                case 'B':
                    return (byte) 8;
                case 'C':
                    return (byte) 5;
                case 'D':
                    return (byte) 7;
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
                    throw new ClassFormatException("Invalid method signature: " + signature);
                case 'F':
                    return (byte) 6;
                case 'I':
                    return (byte) 10;
                case 'J':
                    return (byte) 11;
                case 'L':
                    return (byte) 14;
                case 'S':
                    return (byte) 9;
                case 'V':
                    return (byte) 12;
                case 'Z':
                    return (byte) 4;
                case '[':
                    return (byte) 13;
            }
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
        throw new ClassFormatException("Invalid method signature: " + signature);
    }

    public static short searchOpcode(String name) {
        String name2 = name.toLowerCase();
        short s2 = 0;
        while (true) {
            short i2 = s2;
            if (i2 < Constants.OPCODE_NAMES.length) {
                if (!Constants.OPCODE_NAMES[i2].equals(name2)) {
                    s2 = (short) (i2 + 1);
                } else {
                    return i2;
                }
            } else {
                return (short) -1;
            }
        }
    }

    private static final short byteToShort(byte b2) {
        return b2 < 0 ? (short) (256 + b2) : b2;
    }

    public static final String toHexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < bytes.length; i2++) {
            short b2 = byteToShort(bytes[i2]);
            String hex = Integer.toString(b2, 16);
            if (b2 < 16) {
                buf.append('0');
            }
            buf.append(hex);
            if (i2 < bytes.length - 1) {
                buf.append(' ');
            }
        }
        return buf.toString();
    }

    public static final String format(int i2, int length, boolean left_justify, char fill) {
        return fillup(Integer.toString(i2), length, left_justify, fill);
    }

    public static final String fillup(String str, int length, boolean left_justify, char fill) {
        int len = length - str.length();
        char[] buf = new char[len < 0 ? 0 : len];
        for (int j2 = 0; j2 < buf.length; j2++) {
            buf[j2] = fill;
        }
        if (left_justify) {
            return str + new String(buf);
        }
        return new String(buf) + str;
    }

    static final boolean equals(byte[] a2, byte[] b2) {
        int size = a2.length;
        if (size != b2.length) {
            return false;
        }
        for (int i2 = 0; i2 < size; i2++) {
            if (a2[i2] != b2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static final void printArray(PrintStream out, Object[] obj) {
        out.println(printArray(obj, true));
    }

    public static final void printArray(PrintWriter out, Object[] obj) {
        out.println(printArray(obj, true));
    }

    public static final String printArray(Object[] obj) {
        return printArray(obj, true);
    }

    public static final String printArray(Object[] obj, boolean braces) {
        return printArray(obj, braces, false);
    }

    public static final String printArray(Object[] obj, boolean braces, boolean quote) {
        if (obj == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer();
        if (braces) {
            buf.append('{');
        }
        for (int i2 = 0; i2 < obj.length; i2++) {
            if (obj[i2] != null) {
                buf.append((quote ? PdfOps.DOUBLE_QUOTE__TOKEN : "") + obj[i2].toString() + (quote ? PdfOps.DOUBLE_QUOTE__TOKEN : ""));
            } else {
                buf.append(FXMLLoader.NULL_KEYWORD);
            }
            if (i2 < obj.length - 1) {
                buf.append(", ");
            }
        }
        if (braces) {
            buf.append('}');
        }
        return buf.toString();
    }

    public static boolean isJavaIdentifierPart(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ((ch >= '0' && ch <= '9') || ch == '_');
    }

    public static String encode(byte[] bytes, boolean compress) throws IOException {
        if (compress) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            GZIPOutputStream gos = new GZIPOutputStream(baos);
            gos.write(bytes, 0, bytes.length);
            gos.close();
            baos.close();
            bytes = baos.toByteArray();
        }
        CharArrayWriter caw = new CharArrayWriter();
        JavaWriter jw = new JavaWriter(caw);
        for (byte b2 : bytes) {
            int in = b2 & 255;
            jw.write(in);
        }
        return caw.toString();
    }

    public static byte[] decode(String s2, boolean uncompress) throws IOException, NumberFormatException {
        char[] chars = s2.toCharArray();
        CharArrayReader car = new CharArrayReader(chars);
        JavaReader jr = new JavaReader(car);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while (true) {
            int ch = jr.read();
            if (ch < 0) {
                break;
            }
            bos.write(ch);
        }
        bos.close();
        car.close();
        jr.close();
        byte[] bytes = bos.toByteArray();
        if (uncompress) {
            GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(bytes));
            byte[] tmp = new byte[bytes.length * 3];
            int count = 0;
            while (true) {
                int b2 = gis.read();
                if (b2 < 0) {
                    break;
                }
                int i2 = count;
                count++;
                tmp[i2] = (byte) b2;
            }
            bytes = new byte[count];
            System.arraycopy(tmp, 0, bytes, 0, count);
        }
        return bytes;
    }

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Utility$JavaReader.class */
    private static class JavaReader extends FilterReader {
        public JavaReader(Reader in) {
            super(in);
        }

        @Override // java.io.FilterReader, java.io.Reader
        public int read() throws IOException, NumberFormatException {
            int b2 = this.in.read();
            if (b2 != 36) {
                return b2;
            }
            int i2 = this.in.read();
            if (i2 < 0) {
                return -1;
            }
            if ((i2 < 48 || i2 > 57) && (i2 < 97 || i2 > 102)) {
                return Utility.MAP_CHAR[i2];
            }
            int j2 = this.in.read();
            if (j2 < 0) {
                return -1;
            }
            char[] tmp = {(char) i2, (char) j2};
            int s2 = Integer.parseInt(new String(tmp), 16);
            return s2;
        }

        @Override // java.io.FilterReader, java.io.Reader
        public int read(char[] cbuf, int off, int len) throws IOException {
            for (int i2 = 0; i2 < len; i2++) {
                cbuf[off + i2] = (char) read();
            }
            return len;
        }
    }

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Utility$JavaWriter.class */
    private static class JavaWriter extends FilterWriter {
        public JavaWriter(Writer out) {
            super(out);
        }

        @Override // java.io.FilterWriter, java.io.Writer
        public void write(int b2) throws IOException {
            if (Utility.isJavaIdentifierPart((char) b2) && b2 != 36) {
                this.out.write(b2);
                return;
            }
            this.out.write(36);
            if (b2 >= 0 && b2 < 48) {
                this.out.write(Utility.CHAR_MAP[b2]);
                return;
            }
            char[] tmp = Integer.toHexString(b2).toCharArray();
            if (tmp.length == 1) {
                this.out.write(48);
                this.out.write(tmp[0]);
            } else {
                this.out.write(tmp[0]);
                this.out.write(tmp[1]);
            }
        }

        @Override // java.io.FilterWriter, java.io.Writer
        public void write(char[] cbuf, int off, int len) throws IOException {
            for (int i2 = 0; i2 < len; i2++) {
                write(cbuf[off + i2]);
            }
        }

        @Override // java.io.FilterWriter, java.io.Writer
        public void write(String str, int off, int len) throws IOException {
            write(str.toCharArray(), off, len);
        }
    }

    public static final String convertString(String label) {
        char[] ch = label.toCharArray();
        StringBuffer buf = new StringBuffer();
        for (int i2 = 0; i2 < ch.length; i2++) {
            switch (ch[i2]) {
                case '\n':
                    buf.append("\\n");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '\"':
                    buf.append("\\\"");
                    break;
                case '\'':
                    buf.append("\\'");
                    break;
                case '\\':
                    buf.append("\\\\");
                    break;
                default:
                    buf.append(ch[i2]);
                    break;
            }
        }
        return buf.toString();
    }
}
