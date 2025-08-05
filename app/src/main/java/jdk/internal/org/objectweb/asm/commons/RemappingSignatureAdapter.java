package jdk.internal.org.objectweb.asm.commons;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.signature.SignatureVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/RemappingSignatureAdapter.class */
public class RemappingSignatureAdapter extends SignatureVisitor {

    /* renamed from: v, reason: collision with root package name */
    private final SignatureVisitor f12861v;
    private final Remapper remapper;
    private String className;

    public RemappingSignatureAdapter(SignatureVisitor signatureVisitor, Remapper remapper) {
        this(Opcodes.ASM5, signatureVisitor, remapper);
    }

    protected RemappingSignatureAdapter(int i2, SignatureVisitor signatureVisitor, Remapper remapper) {
        super(i2);
        this.f12861v = signatureVisitor;
        this.remapper = remapper;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitClassType(String str) {
        this.className = str;
        this.f12861v.visitClassType(this.remapper.mapType(str));
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitInnerClassType(String str) {
        String str2 = this.remapper.mapType(this.className) + '$';
        this.className += '$' + str;
        String strMapType = this.remapper.mapType(this.className);
        this.f12861v.visitInnerClassType(strMapType.substring(strMapType.startsWith(str2) ? str2.length() : strMapType.lastIndexOf(36) + 1));
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String str) {
        this.f12861v.visitFormalTypeParameter(str);
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeVariable(String str) {
        this.f12861v.visitTypeVariable(str);
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        this.f12861v.visitArrayType();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitBaseType(char c2) {
        this.f12861v.visitBaseType(c2);
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        this.f12861v.visitClassBound();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        this.f12861v.visitExceptionType();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        this.f12861v.visitInterface();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        this.f12861v.visitInterfaceBound();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        this.f12861v.visitParameterType();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        this.f12861v.visitReturnType();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        this.f12861v.visitSuperclass();
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        this.f12861v.visitTypeArgument();
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char c2) {
        this.f12861v.visitTypeArgument(c2);
        return this;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitEnd() {
        this.f12861v.visitEnd();
    }
}
