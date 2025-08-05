package jdk.internal.org.objectweb.asm.util;

import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.signature.SignatureVisitor;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/util/CheckSignatureAdapter.class */
public class CheckSignatureAdapter extends SignatureVisitor {
    public static final int CLASS_SIGNATURE = 0;
    public static final int METHOD_SIGNATURE = 1;
    public static final int TYPE_SIGNATURE = 2;
    private static final int EMPTY = 1;
    private static final int FORMAL = 2;
    private static final int BOUND = 4;
    private static final int SUPER = 8;
    private static final int PARAM = 16;
    private static final int RETURN = 32;
    private static final int SIMPLE_TYPE = 64;
    private static final int CLASS_TYPE = 128;
    private static final int END = 256;
    private final int type;
    private int state;
    private boolean canBeVoid;
    private final SignatureVisitor sv;

    public CheckSignatureAdapter(int i2, SignatureVisitor signatureVisitor) {
        this(Opcodes.ASM5, i2, signatureVisitor);
    }

    protected CheckSignatureAdapter(int i2, int i3, SignatureVisitor signatureVisitor) {
        super(i2);
        this.type = i3;
        this.state = 1;
        this.sv = signatureVisitor;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitFormalTypeParameter(String str) {
        if (this.type == 2 || (this.state != 1 && this.state != 2 && this.state != 4)) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(str, "formal type parameter");
        this.state = 2;
        if (this.sv != null) {
            this.sv.visitFormalTypeParameter(str);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitClassBound() {
        if (this.state != 2) {
            throw new IllegalStateException();
        }
        this.state = 4;
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitClassBound());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterfaceBound() {
        if (this.state != 2 && this.state != 4) {
            throw new IllegalArgumentException();
        }
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitInterfaceBound());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitSuperclass() {
        if (this.type != 0 || (this.state & 7) == 0) {
            throw new IllegalArgumentException();
        }
        this.state = 8;
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitSuperclass());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitInterface() {
        if (this.state != 8) {
            throw new IllegalStateException();
        }
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitInterface());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitParameterType() {
        if (this.type != 1 || (this.state & 23) == 0) {
            throw new IllegalArgumentException();
        }
        this.state = 16;
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitParameterType());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitReturnType() {
        if (this.type != 1 || (this.state & 23) == 0) {
            throw new IllegalArgumentException();
        }
        this.state = 32;
        CheckSignatureAdapter checkSignatureAdapter = new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitReturnType());
        checkSignatureAdapter.canBeVoid = true;
        return checkSignatureAdapter;
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitExceptionType() {
        if (this.state != 32) {
            throw new IllegalStateException();
        }
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitExceptionType());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitBaseType(char c2) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        if (c2 == 'V') {
            if (!this.canBeVoid) {
                throw new IllegalArgumentException();
            }
        } else if ("ZCBSIFJD".indexOf(c2) == -1) {
            throw new IllegalArgumentException();
        }
        this.state = 64;
        if (this.sv != null) {
            this.sv.visitBaseType(c2);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeVariable(String str) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(str, "type variable");
        this.state = 64;
        if (this.sv != null) {
            this.sv.visitTypeVariable(str);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitArrayType() {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        this.state = 64;
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitArrayType());
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitClassType(String str) {
        if (this.type != 2 || this.state != 1) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkInternalName(str, "class name");
        this.state = 128;
        if (this.sv != null) {
            this.sv.visitClassType(str);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitInnerClassType(String str) {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        CheckMethodAdapter.checkIdentifier(str, "inner class name");
        if (this.sv != null) {
            this.sv.visitInnerClassType(str);
        }
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitTypeArgument() {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        if (this.sv != null) {
            this.sv.visitTypeArgument();
        }
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public SignatureVisitor visitTypeArgument(char c2) {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        if ("+-=".indexOf(c2) == -1) {
            throw new IllegalArgumentException();
        }
        return new CheckSignatureAdapter(2, this.sv == null ? null : this.sv.visitTypeArgument(c2));
    }

    @Override // jdk.internal.org.objectweb.asm.signature.SignatureVisitor
    public void visitEnd() {
        if (this.state != 128) {
            throw new IllegalStateException();
        }
        this.state = 256;
        if (this.sv != null) {
            this.sv.visitEnd();
        }
    }
}
