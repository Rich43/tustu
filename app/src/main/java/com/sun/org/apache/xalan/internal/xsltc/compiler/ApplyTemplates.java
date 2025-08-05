package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
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
import com.sun.org.apache.xml.internal.utils.XML11Char;
import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/ApplyTemplates.class */
final class ApplyTemplates extends Instruction {
    private Expression _select;
    private Type _type = null;
    private QName _modeName;
    private String _functionName;

    ApplyTemplates() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        Util.println("ApplyTemplates");
        indent(indent + 4);
        Util.println("select " + this._select.toString());
        if (this._modeName != null) {
            indent(indent + 4);
            Util.println("mode " + ((Object) this._modeName));
        }
    }

    public boolean hasWithParams() {
        return hasContents();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String select = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT);
        String mode = getAttribute(com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MODE);
        if (select.length() > 0) {
            this._select = parser.parseExpression(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_SELECT, null);
        }
        if (mode.length() > 0) {
            if (!XML11Char.isXML11ValidQName(mode)) {
                ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) mode, (SyntaxTreeNode) this);
                parser.reportError(3, err);
            }
            this._modeName = parser.getQNameIgnoreDefaultNs(mode);
        }
        this._functionName = parser.getTopLevelStylesheet().getMode(this._modeName).functionName();
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        if (this._select != null) {
            this._type = this._select.typeCheck(stable);
            if ((this._type instanceof NodeType) || (this._type instanceof ReferenceType)) {
                this._select = new CastExpr(this._select, Type.NodeSet);
                this._type = Type.NodeSet;
            }
            if ((this._type instanceof NodeSetType) || (this._type instanceof ResultTreeType)) {
                typeCheckContents(stable);
                return Type.Void;
            }
            throw new TypeCheckError(this);
        }
        typeCheckContents(stable);
        return Type.Void;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        boolean setStartNodeCalled = false;
        Stylesheet stylesheet = classGen.getStylesheet();
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int current = methodGen.getLocalIndex(Keywords.FUNC_CURRENT_STRING);
        Vector<Sort> sortObjects = new Vector<>();
        for (SyntaxTreeNode child : getContents()) {
            if (child instanceof Sort) {
                sortObjects.addElement((Sort) child);
            }
        }
        if (stylesheet.hasLocalParams() || hasContents()) {
            il.append(classGen.loadTranslet());
            int pushFrame = cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.PUSH_PARAM_FRAME, "()V");
            il.append(new INVOKEVIRTUAL(pushFrame));
            translateContents(classGen, methodGen);
        }
        il.append(classGen.loadTranslet());
        if (this._type != null && (this._type instanceof ResultTreeType)) {
            if (sortObjects.size() > 0) {
                ErrorMsg err = new ErrorMsg(ErrorMsg.RESULT_TREE_SORT_ERR, (SyntaxTreeNode) this);
                getParser().reportError(4, err);
            }
            this._select.translate(classGen, methodGen);
            this._type.translateTo(classGen, methodGen, Type.NodeSet);
        } else {
            il.append(methodGen.loadDOM());
            if (sortObjects.size() > 0) {
                Sort.translateSortIterator(classGen, methodGen, this._select, sortObjects);
                int setStartNode = cpg.addInterfaceMethodref(Constants.NODE_ITERATOR, Constants.SET_START_NODE, "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
                il.append(methodGen.loadCurrentNode());
                il.append(new INVOKEINTERFACE(setStartNode, 2));
                setStartNodeCalled = true;
            } else if (this._select == null) {
                Mode.compileGetChildren(classGen, methodGen, current);
            } else {
                this._select.translate(classGen, methodGen);
            }
        }
        if (this._select != null && !setStartNodeCalled) {
            this._select.startIterator(classGen, methodGen);
        }
        String className = classGen.getStylesheet().getClassName();
        il.append(methodGen.loadHandler());
        String applyTemplatesSig = classGen.getApplyTemplatesSig();
        int applyTemplates = cpg.addMethodref(className, this._functionName, applyTemplatesSig);
        il.append(new INVOKEVIRTUAL(applyTemplates));
        for (SyntaxTreeNode child2 : getContents()) {
            if (child2 instanceof WithParam) {
                ((WithParam) child2).releaseResultTree(classGen, methodGen);
            }
        }
        if (stylesheet.hasLocalParams() || hasContents()) {
            il.append(classGen.loadTranslet());
            int popFrame = cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.POP_PARAM_FRAME, "()V");
            il.append(new INVOKEVIRTUAL(popFrame));
        }
    }
}
