package com.sun.org.apache.bcel.internal.classfile;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Signature.class */
public final class Signature extends Attribute {
    private int signature_index;

    public Signature(Signature c2) {
        this(c2.getNameIndex(), c2.getLength(), c2.getSignatureIndex(), c2.getConstantPool());
    }

    Signature(int name_index, int length, DataInputStream file, ConstantPool constant_pool) throws IOException {
        this(name_index, length, file.readUnsignedShort(), constant_pool);
    }

    public Signature(int name_index, int length, int signature_index, ConstantPool constant_pool) {
        super((byte) 10, name_index, length, constant_pool);
        this.signature_index = signature_index;
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute, com.sun.org.apache.bcel.internal.classfile.Node
    public void accept(Visitor v2) {
        System.err.println("Visiting non-standard Signature object");
        v2.visitSignature(this);
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final void dump(DataOutputStream file) throws IOException {
        super.dump(file);
        file.writeShort(this.signature_index);
    }

    public final int getSignatureIndex() {
        return this.signature_index;
    }

    public final void setSignatureIndex(int signature_index) {
        this.signature_index = signature_index;
    }

    public final String getSignature() {
        ConstantUtf8 c2 = (ConstantUtf8) this.constant_pool.getConstant(this.signature_index, (byte) 1);
        return c2.getBytes();
    }

    /* loaded from: rt.jar:com/sun/org/apache/bcel/internal/classfile/Signature$MyByteArrayInputStream.class */
    private static final class MyByteArrayInputStream extends ByteArrayInputStream {
        MyByteArrayInputStream(String data) {
            super(data.getBytes());
        }

        final int mark() {
            return this.pos;
        }

        final String getData() {
            return new String(this.buf);
        }

        final void reset(int p2) {
            this.pos = p2;
        }

        final void unread() {
            if (this.pos > 0) {
                this.pos--;
            }
        }
    }

    private static boolean identStart(int ch) {
        return ch == 84 || ch == 76;
    }

    private static boolean identPart(int ch) {
        return ch == 47 || ch == 59;
    }

    private static final void matchIdent(MyByteArrayInputStream in, StringBuffer buf) {
        int i2 = in.read();
        int ch = i2;
        if (i2 == -1) {
            throw new RuntimeException("Illegal signature: " + in.getData() + " no ident, reaching EOF");
        }
        if (!identStart(ch)) {
            StringBuffer buf2 = new StringBuffer();
            int count = 1;
            while (Character.isJavaIdentifierPart((char) ch)) {
                buf2.append((char) ch);
                count++;
                ch = in.read();
            }
            if (ch == 58) {
                in.skip("Ljava/lang/Object".length());
                buf.append(buf2);
                in.read();
                in.unread();
                return;
            }
            for (int i3 = 0; i3 < count; i3++) {
                in.unread();
            }
            return;
        }
        StringBuffer buf22 = new StringBuffer();
        int ch2 = in.read();
        while (true) {
            buf22.append((char) ch2);
            ch2 = in.read();
            if (ch2 == -1 || (!Character.isJavaIdentifierPart((char) ch2) && ch2 != 47)) {
                break;
            }
        }
        buf.append(buf22.toString().replace('/', '.'));
        if (ch2 != -1) {
            in.unread();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x006e, code lost:
    
        r6.append((char) r0);
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static final void matchGJIdent(com.sun.org.apache.bcel.internal.classfile.Signature.MyByteArrayInputStream r5, java.lang.StringBuffer r6) {
        /*
            Method dump skipped, instructions count: 206
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.org.apache.bcel.internal.classfile.Signature.matchGJIdent(com.sun.org.apache.bcel.internal.classfile.Signature$MyByteArrayInputStream, java.lang.StringBuffer):void");
    }

    public static String translate(String s2) {
        StringBuffer buf = new StringBuffer();
        matchGJIdent(new MyByteArrayInputStream(s2), buf);
        return buf.toString();
    }

    public static final boolean isFormalParameterList(String s2) {
        return s2.startsWith("<") && s2.indexOf(58) > 0;
    }

    public static final boolean isActualParameterList(String s2) {
        return s2.startsWith("L") && s2.endsWith(">;");
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public final String toString() {
        String s2 = getSignature();
        return "Signature(" + s2 + ")";
    }

    @Override // com.sun.org.apache.bcel.internal.classfile.Attribute
    public Attribute copy(ConstantPool constant_pool) {
        return (Signature) clone();
    }
}
