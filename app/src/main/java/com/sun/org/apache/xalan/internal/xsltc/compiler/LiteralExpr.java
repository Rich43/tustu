package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LiteralExpr.class */
final class LiteralExpr extends Expression {
    private final String _value;
    private final String _namespace;

    public LiteralExpr(String value) {
        this._value = value;
        this._namespace = null;
    }

    public LiteralExpr(String value, String namespace) {
        this._value = value;
        this._namespace = namespace.equals("") ? null : namespace;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type type = Type.String;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "literal-expr(" + this._value + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    protected boolean contextDependent() {
        return false;
    }

    protected String getValue() {
        return this._value;
    }

    protected String getNamespace() {
        return this._namespace;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(new PUSH(cpg, this._value));
    }
}
