package jdk.internal.org.objectweb.asm.signature;

import jdk.internal.org.objectweb.asm.Opcodes;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/signature/SignatureWriter.class */
public class SignatureWriter extends SignatureVisitor {
    private final StringBuffer buf;
    private boolean hasFormals;
    private boolean hasParameters;
    private int argumentStack;

    public SignatureWriter() {
        super(Opcodes.ASM5);
        this.buf = new StringBuffer();
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String str) {
        if (!this.hasFormals) {
            this.hasFormals = true;
            this.buf.append('<');
        }
        this.buf.append(str);
        this.buf.append(':');
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        this.buf.append(':');
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        endFormals();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        endFormals();
        if (!this.hasParameters) {
            this.hasParameters = true;
            this.buf.append('(');
        }
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        endFormals();
        if (!this.hasParameters) {
            this.buf.append('(');
        }
        this.buf.append(')');
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        this.buf.append('^');
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitBaseType(char c2) {
        this.buf.append(c2);
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeVariable(String str) {
        this.buf.append('T');
        this.buf.append(str);
        this.buf.append(';');
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        this.buf.append('[');
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitClassType(String str) {
        this.buf.append('L');
        this.buf.append(str);
        this.argumentStack *= 2;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitInnerClassType(String str) {
        endArguments();
        this.buf.append('.');
        this.buf.append(str);
        this.argumentStack *= 2;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        if (this.argumentStack % 2 == 0) {
            this.argumentStack++;
            this.buf.append('<');
        }
        this.buf.append('*');
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char c2) {
        if (this.argumentStack % 2 == 0) {
            this.argumentStack++;
            this.buf.append('<');
        }
        if (c2 != '=') {
            this.buf.append(c2);
        }
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitEnd() {
        endArguments();
        this.buf.append(';');
    }

    public String toString() {
        return this.buf.toString();
    }

    private void endFormals() {
        if (this.hasFormals) {
            this.hasFormals = false;
            this.buf.append('>');
        }
    }

    private void endArguments() {
        if (this.argumentStack % 2 != 0) {
            this.buf.append('>');
        }
        this.argumentStack /= 2;
    }
}
