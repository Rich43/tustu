package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import java.util.Objects;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/VariableRefBase.class */
class VariableRefBase extends Expression {
    protected VariableBase _variable;
    protected Closure _closure;

    public VariableRefBase(VariableBase variable) {
        this._closure = null;
        this._variable = variable;
        variable.addReference(this);
    }

    public VariableRefBase() {
        this._closure = null;
        this._variable = null;
    }

    public VariableBase getVariable() {
        return this._variable;
    }

    public void addParentDependency() {
        SyntaxTreeNode node;
        SyntaxTreeNode parent = this;
        while (true) {
            node = parent;
            if (node == null || (node instanceof TopLevelElement)) {
                break;
            } else {
                parent = node.getParent();
            }
        }
        TopLevelElement parent2 = (TopLevelElement) node;
        if (parent2 != null) {
            VariableBase var = this._variable;
            if (this._variable._ignore) {
                if (this._variable instanceof Variable) {
                    var = parent2.getSymbolTable().lookupVariable(this._variable._name);
                } else if (this._variable instanceof Param) {
                    var = parent2.getSymbolTable().lookupParam(this._variable._name);
                }
            }
            parent2.addDependency(var);
        }
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof VariableRefBase) && this._variable == ((VariableRefBase) obj)._variable);
    }

    public int hashCode() {
        return Objects.hashCode(this._variable);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "variable-ref(" + ((Object) this._variable.getName()) + '/' + ((Object) this._variable.getType()) + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._type != null) {
            return this._type;
        }
        if (this._variable.isLocal()) {
            SyntaxTreeNode node = getParent();
            while (true) {
                if (node instanceof Closure) {
                    this._closure = (Closure) node;
                    break;
                }
                if (node instanceof TopLevelElement) {
                    break;
                }
                node = node.getParent();
                if (node == null) {
                    break;
                }
            }
            if (this._closure != null) {
                this._closure.addVariable(this);
            }
        }
        this._type = this._variable.getType();
        if (this._type == null) {
            this._variable.typeCheck(stable);
            this._type = this._variable.getType();
        }
        addParentDependency();
        return this._type;
    }
}
