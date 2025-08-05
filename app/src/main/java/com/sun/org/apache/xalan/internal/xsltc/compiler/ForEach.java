package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFGT;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ReferenceType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ResultTreeType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import java.util.Iterator;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ForEach.class */
final class ForEach extends Instruction {
    private Expression _select;
    private Type _type;

    ForEach() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("ForEach");
        indent(indent + 4);
        Util.println("select " + this._select.toString());
        displayContents(indent + 4);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        this._select = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, null);
        parseChildren(parser);
        if (this._select.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
        }
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this._type = this._select.typeCheck(stable);
        if ((this._type instanceof ReferenceType) || (this._type instanceof NodeType)) {
            this._select = new CastExpr(this._select, Type.NodeSet);
            typeCheckContents(stable);
            return Type.Void;
        }
        if ((this._type instanceof NodeSetType) || (this._type instanceof ResultTreeType)) {
            typeCheckContents(stable);
            return Type.Void;
        }
        throw new TypeCheckError(this);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        il.append(methodGen.loadCurrentNode());
        il.append(methodGen.loadIterator());
        Vector sortObjects = new Vector();
        Iterator<SyntaxTreeNode> children = elements();
        while (children.hasNext()) {
            SyntaxTreeNode child = children.next();
            if (child instanceof Sort) {
                sortObjects.addElement(child);
            }
        }
        if (this._type != null && (this._type instanceof ResultTreeType)) {
            il.append(methodGen.loadDOM());
            if (sortObjects.size() > 0) {
                ErrorMsg msg = new ErrorMsg(ErrorMsg.RESULT_TREE_SORT_ERR, (SyntaxTreeNode) this);
                getParser().reportError(4, msg);
            }
            this._select.translate(classGen, methodGen);
            this._type.translateTo(classGen, methodGen, Type.NodeSet);
            il.append(SWAP);
            il.append(methodGen.storeDOM());
        } else {
            if (sortObjects.size() > 0) {
                Sort.translateSortIterator(classGen, methodGen, this._select, sortObjects);
            } else {
                this._select.translate(classGen, methodGen);
            }
            if (!(this._type instanceof ReferenceType)) {
                il.append(methodGen.loadContextNode());
                il.append(methodGen.setStartNode());
            }
        }
        il.append(methodGen.storeIterator());
        initializeVariables(classGen, methodGen);
        BranchHandle nextNode = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle loop = il.append(NOP);
        translateContents(classGen, methodGen);
        nextNode.setTarget(il.append(methodGen.loadIterator()));
        il.append(methodGen.nextNode());
        il.append(DUP);
        il.append(methodGen.storeCurrentNode());
        il.append((BranchInstruction) new IFGT(loop));
        if (this._type != null && (this._type instanceof ResultTreeType)) {
            il.append(methodGen.storeDOM());
        }
        il.append(methodGen.storeIterator());
        il.append(methodGen.storeCurrentNode());
    }

    public void initializeVariables(ClassGenerator classGen, MethodGenerator methodGen) {
        int n2 = elementCount();
        for (int i2 = 0; i2 < n2; i2++) {
            SyntaxTreeNode child = getContents().get(i2);
            if (child instanceof Variable) {
                Variable var = (Variable) child;
                var.initialize(classGen, methodGen);
            }
        }
    }
}
