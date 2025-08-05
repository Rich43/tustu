package jdk.internal.org.objectweb.asm.signature;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/signature/SignatureVisitor.class */
public abstract class SignatureVisitor {
    public static final char EXTENDS = '+';
    public static final char SUPER = '-';
    public static final char INSTANCEOF = '=';
    protected final int api;

    public SignatureVisitor(int i2) {
        if (i2 != 262144 && i2 != 327680) {
            throw new IllegalArgumentException();
        }
        this.api = i2;
    }

    public void visitFormalTypeParameter(String str) {
    }

    public SignatureVisitor visitClassBound() {
        return this;
    }

    public SignatureVisitor visitInterfaceBound() {
        return this;
    }

    public SignatureVisitor visitSuperclass() {
        return this;
    }

    public SignatureVisitor visitInterface() {
        return this;
    }

    public SignatureVisitor visitParameterType() {
        return this;
    }

    public SignatureVisitor visitReturnType() {
        return this;
    }

    public SignatureVisitor visitExceptionType() {
        return this;
    }

    public void visitBaseType(char c2) {
    }

    public void visitTypeVariable(String str) {
    }

    public SignatureVisitor visitArrayType() {
        return this;
    }

    public void visitClassType(String str) {
    }

    public void visitInnerClassType(String str) {
    }

    public void visitTypeArgument() {
    }

    public SignatureVisitor visitTypeArgument(char c2) {
        return this;
    }

    public void visitEnd() {
    }
}
