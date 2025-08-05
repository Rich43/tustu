package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/LocationPathPattern.class */
public abstract class LocationPathPattern extends Pattern {
    private Template _template;
    private int _importPrecedence;
    private double _priority = Double.NaN;
    private int _position = 0;

    public abstract StepPattern getKernelPattern();

    public abstract void reduceKernelPattern();

    public abstract boolean isWildcard();

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern, com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
    }

    public void setTemplate(Template template) {
        this._template = template;
        this._priority = template.getPriority();
        this._importPrecedence = template.getImportPrecedence();
        this._position = template.getPosition();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Template getTemplate() {
        return this._template;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Pattern
    public final double getPriority() {
        return Double.isNaN(this._priority) ? getDefaultPriority() : this._priority;
    }

    public double getDefaultPriority() {
        return 0.5d;
    }

    public boolean noSmallerThan(LocationPathPattern other) {
        if (this._importPrecedence > other._importPrecedence) {
            return true;
        }
        if (this._importPrecedence != other._importPrecedence) {
            return false;
        }
        if (this._priority > other._priority) {
            return true;
        }
        if (this._priority == other._priority && this._position > other._position) {
            return true;
        }
        return false;
    }

    public int getAxis() {
        StepPattern sp = getKernelPattern();
        if (sp != null) {
            return sp.getAxis();
        }
        return 3;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "root()";
    }
}
