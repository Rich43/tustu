package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ConcatCall.class */
final class ConcatCall extends FunctionCall {
    public ConcatCall(QName fname, Vector arguments) {
        super(fname, arguments);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.FunctionCall, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        for (int i2 = 0; i2 < argumentCount(); i2++) {
            Expression exp = argument(i2);
            if (!exp.typeCheck(stable).identicalTo(Type.String)) {
                setArgument(i2, new CastExpr(exp, Type.String));
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
        int nArgs = argumentCount();
        switch (nArgs) {
            case 0:
                il.append(new PUSH(cpg, ""));
                break;
            case 1:
                argument().translate(classGen, methodGen);
                break;
            default:
                int initBuffer = cpg.addMethodref(Constants.STRING_BUFFER_CLASS, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V");
                com.sun.org.apache.bcel.internal.generic.Instruction append = new INVOKEVIRTUAL(cpg.addMethodref(Constants.STRING_BUFFER_CLASS, "append", "(Ljava/lang/String;)Ljava/lang/StringBuffer;"));
                int toString = cpg.addMethodref(Constants.STRING_BUFFER_CLASS, "toString", "()Ljava/lang/String;");
                il.append(new NEW(cpg.addClass(Constants.STRING_BUFFER_CLASS)));
                il.append(DUP);
                il.append(new INVOKESPECIAL(initBuffer));
                for (int i2 = 0; i2 < nArgs; i2++) {
                    argument(i2).translate(classGen, methodGen);
                    il.append(append);
                }
                il.append(new INVOKEVIRTUAL(toString));
                break;
        }
    }
}
