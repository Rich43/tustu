package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.generic.ASTORE;
import com.sun.org.apache.bcel.internal.generic.CHECKCAST;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GETFIELD;
import com.sun.org.apache.bcel.internal.generic.INVOKESPECIAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.bcel.internal.generic.PUTFIELD;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.BooleanType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.FilterGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.IntType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NumberType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Predicate.class */
final class Predicate extends Expression implements Closure {
    private Expression _exp;
    private boolean _canOptimize = true;
    private boolean _nthPositionFilter = false;
    private boolean _nthDescendant = false;
    int _ptype = -1;
    private String _className = null;
    private ArrayList _closureVars = null;
    private Closure _parentClosure = null;
    private Expression _value = null;
    private Step _step = null;

    public Predicate(Expression exp) {
        this._exp = null;
        this._exp = exp;
        this._exp.setParent(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void setParser(Parser parser) {
        super.setParser(parser);
        this._exp.setParser(parser);
    }

    public boolean isNthPositionFilter() {
        return this._nthPositionFilter;
    }

    public boolean isNthDescendant() {
        return this._nthDescendant;
    }

    public void dontOptimize() {
        this._canOptimize = false;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasPositionCall() {
        return this._exp.hasPositionCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public boolean hasLastCall() {
        return this._exp.hasLastCall();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public boolean inInnerClass() {
        return this._className != null;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public Closure getParentClosure() {
        if (this._parentClosure == null) {
            SyntaxTreeNode node = getParent();
            while (true) {
                if (node instanceof Closure) {
                    this._parentClosure = (Closure) node;
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
        }
        return this._parentClosure;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public String getInnerClassName() {
        return this._className;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Closure
    public void addVariable(VariableRefBase variableRef) {
        if (this._closureVars == null) {
            this._closureVars = new ArrayList();
        }
        if (!this._closureVars.contains(variableRef)) {
            this._closureVars.add(variableRef);
            Closure parentClosure = getParentClosure();
            if (parentClosure != null) {
                parentClosure.addVariable(variableRef);
            }
        }
    }

    public int getPosType() {
        if (this._ptype == -1) {
            SyntaxTreeNode parent = getParent();
            if (parent instanceof StepPattern) {
                this._ptype = ((StepPattern) parent).getNodeType();
            } else if (parent instanceof AbsoluteLocationPath) {
                AbsoluteLocationPath path = (AbsoluteLocationPath) parent;
                Expression exp = path.getPath();
                if (exp instanceof Step) {
                    this._ptype = ((Step) exp).getNodeType();
                }
            } else if (parent instanceof VariableRefBase) {
                VariableRefBase ref = (VariableRefBase) parent;
                VariableBase var = ref.getVariable();
                Expression exp2 = var.getExpression();
                if (exp2 instanceof Step) {
                    this._ptype = ((Step) exp2).getNodeType();
                }
            } else if (parent instanceof Step) {
                this._ptype = ((Step) parent).getNodeType();
            }
        }
        return this._ptype;
    }

    public boolean parentIsPattern() {
        return getParent() instanceof Pattern;
    }

    public Expression getExpr() {
        return this._exp;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression
    public String toString() {
        return "pred(" + ((Object) this._exp) + ')';
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Type texp = this._exp.typeCheck(stable);
        if (texp instanceof ReferenceType) {
            Expression expression = this._exp;
            Type type = Type.Real;
            texp = type;
            this._exp = new CastExpr(expression, type);
        }
        if (texp instanceof ResultTreeType) {
            this._exp = new CastExpr(this._exp, Type.Boolean);
            this._exp = new CastExpr(this._exp, Type.Real);
            texp = this._exp.typeCheck(stable);
        }
        if (texp instanceof NumberType) {
            if (!(texp instanceof IntType)) {
                this._exp = new CastExpr(this._exp, Type.Int);
            }
            if (this._canOptimize) {
                this._nthPositionFilter = (this._exp.hasLastCall() || this._exp.hasPositionCall()) ? false : true;
                if (this._nthPositionFilter) {
                    SyntaxTreeNode parent = getParent();
                    this._nthDescendant = (parent instanceof Step) && (parent.getParent() instanceof AbsoluteLocationPath);
                    Type type2 = Type.NodeSet;
                    this._type = type2;
                    return type2;
                }
            }
            this._nthDescendant = false;
            this._nthPositionFilter = false;
            QName position = getParser().getQNameIgnoreDefaultNs(Keywords.FUNC_POSITION_STRING);
            PositionCall positionCall = new PositionCall(position);
            positionCall.setParser(getParser());
            positionCall.setParent(this);
            this._exp = new EqualityExpr(0, positionCall, this._exp);
            if (this._exp.typeCheck(stable) != Type.Boolean) {
                this._exp = new CastExpr(this._exp, Type.Boolean);
            }
            Type type3 = Type.Boolean;
            this._type = type3;
            return type3;
        }
        if (!(texp instanceof BooleanType)) {
            this._exp = new CastExpr(this._exp, Type.Boolean);
        }
        Type type4 = Type.Boolean;
        this._type = type4;
        return type4;
    }

    private void compileFilter(ClassGenerator classGen, MethodGenerator methodGen) {
        this._className = getXSLTC().getHelperClassName();
        FilterGenerator filterGen = new FilterGenerator(this._className, Constants.OBJECT_CLASS, toString(), 33, new String[]{Constants.CURRENT_NODE_LIST_FILTER}, classGen.getStylesheet());
        ConstantPoolGen cpg = filterGen.getConstantPool();
        int length = this._closureVars == null ? 0 : this._closureVars.size();
        for (int i2 = 0; i2 < length; i2++) {
            VariableBase var = ((VariableRefBase) this._closureVars.get(i2)).getVariable();
            filterGen.addField(new Field(1, cpg.addUtf8(var.getEscapedName()), cpg.addUtf8(var.getType().toSignature()), null, cpg.getConstantPool()));
        }
        InstructionList il = new InstructionList();
        TestGenerator testGen = new TestGenerator(17, com.sun.org.apache.bcel.internal.generic.Type.BOOLEAN, new com.sun.org.apache.bcel.internal.generic.Type[]{com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, com.sun.org.apache.bcel.internal.generic.Type.INT, Util.getJCRefType("Lcom/sun/org/apache/xalan/internal/xsltc/runtime/AbstractTranslet;"), Util.getJCRefType("Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;")}, new String[]{"node", Keywords.FUNC_POSITION_STRING, Keywords.FUNC_LAST_STRING, Keywords.FUNC_CURRENT_STRING, "translet", Constants.ITERATOR_PNAME}, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_TEST, this._className, il, cpg);
        LocalVariableGen local = testGen.addLocalVariable(Constants.DOCUMENT_PNAME, Util.getJCRefType(Constants.DOM_INTF_SIG), null, null);
        String className = classGen.getClassName();
        il.append(filterGen.loadTranslet());
        il.append(new CHECKCAST(cpg.addClass(className)));
        il.append(new GETFIELD(cpg.addFieldref(className, Constants.DOM_FIELD, Constants.DOM_INTF_SIG)));
        local.setStart(il.append(new ASTORE(local.getIndex())));
        testGen.setDomIndex(local.getIndex());
        this._exp.translate(filterGen, testGen);
        il.append(IRETURN);
        filterGen.addEmptyConstructor(1);
        filterGen.addMethod(testGen);
        getXSLTC().dumpClass(filterGen.getJavaClass());
    }

    public boolean isBooleanTest() {
        return this._exp instanceof BooleanExpr;
    }

    public boolean isNodeValueTest() {
        return (!this._canOptimize || getStep() == null || getCompareValue() == null) ? false : true;
    }

    public Step getStep() {
        if (this._step != null) {
            return this._step;
        }
        if (this._exp == null) {
            return null;
        }
        if (this._exp instanceof EqualityExpr) {
            EqualityExpr exp = (EqualityExpr) this._exp;
            Expression left = exp.getLeft();
            Expression right = exp.getRight();
            if (left instanceof CastExpr) {
                left = ((CastExpr) left).getExpr();
            }
            if (left instanceof Step) {
                this._step = (Step) left;
            }
            if (right instanceof CastExpr) {
                right = ((CastExpr) right).getExpr();
            }
            if (right instanceof Step) {
                this._step = (Step) right;
            }
        }
        return this._step;
    }

    public Expression getCompareValue() {
        if (this._value != null) {
            return this._value;
        }
        if (this._exp != null && (this._exp instanceof EqualityExpr)) {
            EqualityExpr exp = (EqualityExpr) this._exp;
            Expression left = exp.getLeft();
            Expression right = exp.getRight();
            if (left instanceof LiteralExpr) {
                this._value = left;
                return this._value;
            }
            if ((left instanceof VariableRefBase) && left.getType() == Type.String) {
                this._value = left;
                return this._value;
            }
            if (right instanceof LiteralExpr) {
                this._value = right;
                return this._value;
            }
            if ((right instanceof VariableRefBase) && right.getType() == Type.String) {
                this._value = right;
                return this._value;
            }
            return null;
        }
        return null;
    }

    public void translateFilter(ClassGenerator classGen, MethodGenerator methodGen) {
        Closure variableClosure;
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        compileFilter(classGen, methodGen);
        il.append(new NEW(cpg.addClass(this._className)));
        il.append(DUP);
        il.append(new INVOKESPECIAL(cpg.addMethodref(this._className, com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, "()V")));
        int length = this._closureVars == null ? 0 : this._closureVars.size();
        for (int i2 = 0; i2 < length; i2++) {
            VariableRefBase varRef = (VariableRefBase) this._closureVars.get(i2);
            VariableBase var = varRef.getVariable();
            Type varType = var.getType();
            il.append(DUP);
            Closure parentClosure = this._parentClosure;
            while (true) {
                variableClosure = parentClosure;
                if (variableClosure == null || variableClosure.inInnerClass()) {
                    break;
                } else {
                    parentClosure = variableClosure.getParentClosure();
                }
            }
            if (variableClosure != null) {
                il.append(ALOAD_0);
                il.append(new GETFIELD(cpg.addFieldref(variableClosure.getInnerClassName(), var.getEscapedName(), varType.toSignature())));
            } else {
                il.append(var.loadInstruction());
            }
            il.append(new PUTFIELD(cpg.addFieldref(this._className, var.getEscapedName(), varType.toSignature())));
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Expression, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (this._nthPositionFilter || this._nthDescendant) {
            this._exp.translate(classGen, methodGen);
            return;
        }
        if (isNodeValueTest() && (getParent() instanceof Step)) {
            this._value.translate(classGen, methodGen);
            il.append(new CHECKCAST(cpg.addClass("java.lang.String")));
            il.append(new PUSH(cpg, ((EqualityExpr) this._exp).getOp()));
            return;
        }
        translateFilter(classGen, methodGen);
    }
}
