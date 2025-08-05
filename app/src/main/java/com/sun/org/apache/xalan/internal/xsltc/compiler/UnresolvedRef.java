package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/UnresolvedRef.class */
final class UnresolvedRef extends VariableRefBase {
    private QName _variableName;
    private VariableRefBase _ref = null;

    public UnresolvedRef(QName name) {
        this._variableName = null;
        this._variableName = name;
    }

    public QName getName() {
        return this._variableName;
    }

    private ErrorMsg reportError() {
        ErrorMsg err = new ErrorMsg(ErrorMsg.VARIABLE_UNDEF_ERR, (Object) this._variableName, (SyntaxTreeNode) this);
        getParser().reportError(3, err);
        return err;
    }

    private VariableRefBase resolve(Parser parser, SymbolTable stable) {
        VariableBase ref = parser.lookupVariable(this._variableName);
        if (ref == null) {
            ref = (VariableBase) stable.lookupName(this._variableName);
        }
        if (ref == null) {
            reportError();
            return null;
        }
        this._variable = ref;
        addParentDependency();
        if (ref instanceof Variable) {
            return new VariableRef((Variable) ref);
        }
        if (ref instanceof Param) {
            return new ParameterRef((Param) ref);
        }
        return null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.VariableRefBase, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._ref != null) {
            String name = this._variableName.toString();
            new ErrorMsg(ErrorMsg.CIRCULAR_VARIABLE_ERR, (Object) name, (SyntaxTreeNode) this);
        }
        VariableRefBase variableRefBaseResolve = resolve(getParser(), stable);
        this._ref = variableRefBaseResolve;
        if (variableRefBaseResolve != null) {
            Type typeTypeCheck = this._ref.typeCheck(stable);
            this._type = typeTypeCheck;
            return typeTypeCheck;
        }
        throw new TypeCheckError(reportError());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        if (this._ref != null) {
            this._ref.translate(classGen, methodGen);
        } else {
            reportError();
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.VariableRefBase, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "unresolved-ref()";
    }
}
