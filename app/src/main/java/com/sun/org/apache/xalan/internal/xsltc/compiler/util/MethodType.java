package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/MethodType.class */
public final class MethodType extends Type {
    private final Type _resultType;
    private final Vector _argsType;

    public MethodType(Type resultType) {
        this._argsType = null;
        this._resultType = resultType;
    }

    public MethodType(Type resultType, Type arg1) {
        if (arg1 != Type.Void) {
            this._argsType = new Vector();
            this._argsType.addElement(arg1);
        } else {
            this._argsType = null;
        }
        this._resultType = resultType;
    }

    public MethodType(Type resultType, Type arg1, Type arg2) {
        this._argsType = new Vector(2);
        this._argsType.addElement(arg1);
        this._argsType.addElement(arg2);
        this._resultType = resultType;
    }

    public MethodType(Type resultType, Type arg1, Type arg2, Type arg3) {
        this._argsType = new Vector(3);
        this._argsType.addElement(arg1);
        this._argsType.addElement(arg2);
        this._argsType.addElement(arg3);
        this._resultType = resultType;
    }

    public MethodType(Type resultType, Vector argsType) {
        this._resultType = resultType;
        this._argsType = argsType.size() > 0 ? argsType : null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toString() {
        StringBuffer result = new StringBuffer("method{");
        if (this._argsType != null) {
            int count = this._argsType.size();
            for (int i2 = 0; i2 < count; i2++) {
                result.append(this._argsType.elementAt(i2));
                if (i2 != count - 1) {
                    result.append(',');
                }
            }
        } else {
            result.append("void");
        }
        result.append('}');
        return result.toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public String toSignature() {
        return toSignature("");
    }

    public String toSignature(String lastArgSig) {
        StringBuffer buffer = new StringBuffer();
        buffer.append('(');
        if (this._argsType != null) {
            int n2 = this._argsType.size();
            for (int i2 = 0; i2 < n2; i2++) {
                buffer.append(((Type) this._argsType.elementAt(i2)).toSignature());
            }
        }
        return buffer.append(lastArgSig).append(')').append(this._resultType.toSignature()).toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public com.sun.org.apache.bcel.internal.generic.Type toJCType() {
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public boolean identicalTo(Type other) {
        boolean result = false;
        if (other instanceof MethodType) {
            MethodType temp = (MethodType) other;
            if (this._resultType.identicalTo(temp._resultType)) {
                int len = argsCount();
                result = len == temp.argsCount();
                for (int i2 = 0; i2 < len && result; i2++) {
                    Type arg1 = (Type) this._argsType.elementAt(i2);
                    Type arg2 = (Type) temp._argsType.elementAt(i2);
                    result = arg1.identicalTo(arg2);
                }
            }
        }
        return result;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type
    public int distanceTo(Type other) {
        int result = Integer.MAX_VALUE;
        if (other instanceof MethodType) {
            MethodType mtype = (MethodType) other;
            if (this._argsType != null) {
                int len = this._argsType.size();
                if (len == mtype._argsType.size()) {
                    result = 0;
                    int i2 = 0;
                    while (true) {
                        if (i2 >= len) {
                            break;
                        }
                        Type arg1 = (Type) this._argsType.elementAt(i2);
                        Type arg2 = (Type) mtype._argsType.elementAt(i2);
                        int temp = arg1.distanceTo(arg2);
                        if (temp == Integer.MAX_VALUE) {
                            result = temp;
                            break;
                        }
                        result += arg1.distanceTo(arg2);
                        i2++;
                    }
                }
            } else if (mtype._argsType == null) {
                result = 0;
            }
        }
        return result;
    }

    public Type resultType() {
        return this._resultType;
    }

    public Vector argsType() {
        return this._argsType;
    }

    public int argsCount() {
        if (this._argsType == null) {
            return 0;
        }
        return this._argsType.size();
    }
}
