package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESTATIC;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.RealType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/FormatNumberCall.class */
final class FormatNumberCall extends FunctionCall {
    private Expression _value;
    private Expression _format;
    private Expression _name;
    private QName _resolvedQName;

    public FormatNumberCall(QName fname, Vector arguments) {
        super(fname, arguments);
        this._resolvedQName = null;
        this._value = argument(0);
        this._format = argument(1);
        this._name = argumentCount() == 3 ? argument(2) : null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        getStylesheet().numberFormattingUsed();
        Type tvalue = this._value.typeCheck(stable);
        if (!(tvalue instanceof RealType)) {
            this._value = new CastExpr(this._value, Type.Real);
        }
        Type tformat = this._format.typeCheck(stable);
        if (!(tformat instanceof StringType)) {
            this._format = new CastExpr(this._format, Type.String);
        }
        if (argumentCount() == 3) {
            Type tname = this._name.typeCheck(stable);
            if (this._name instanceof LiteralExpr) {
                LiteralExpr literal = (LiteralExpr) this._name;
                this._resolvedQName = getParser().getQNameIgnoreDefaultNs(literal.getValue());
            } else if (!(tname instanceof StringType)) {
                this._name = new CastExpr(this._name, Type.String);
            }
        }
        Type type = Type.String;
        this._type = type;
        return type;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        this._value.translate(classGen, methodGen);
        this._format.translate(classGen, methodGen);
        int fn3arg = cpg.addMethodref(Constants.BASIS_LIBRARY_CLASS, "formatNumber", "(DLjava/lang/String;Ljava/text/DecimalFormat;)Ljava/lang/String;");
        int get = cpg.addMethodref(Constants.TRANSLET_CLASS, "getDecimalFormat", "(Ljava/lang/String;)Ljava/text/DecimalFormat;");
        il.append(classGen.loadTranslet());
        if (this._name == null) {
            il.append(new PUSH(cpg, ""));
        } else if (this._resolvedQName != null) {
            il.append(new PUSH(cpg, this._resolvedQName.toString()));
        } else {
            this._name.translate(classGen, methodGen);
        }
        il.append(new INVOKEVIRTUAL(get));
        il.append(new INVOKESTATIC(fn3arg));
    }
}
