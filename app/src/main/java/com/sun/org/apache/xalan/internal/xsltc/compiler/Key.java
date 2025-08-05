package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.BranchHandle;
import com.sun.org.apache.bcel.internal.generic.BranchInstruction;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.GOTO;
import com.sun.org.apache.bcel.internal.generic.IFEQ;
import com.sun.org.apache.bcel.internal.generic.IFGE;
import com.sun.org.apache.bcel.internal.generic.IFGT;
import com.sun.org.apache.bcel.internal.generic.ILOAD;
import com.sun.org.apache.bcel.internal.generic.INVOKEINTERFACE;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.ISTORE;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.LocalVariableGen;
import com.sun.org.apache.bcel.internal.generic.PUSH;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.NodeSetType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.StringType;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import com.sun.org.apache.xpath.internal.compiler.Keywords;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/Key.class */
final class Key extends TopLevelElement {
    private QName _name;
    private Pattern _match;
    private Expression _use;
    private Type _useType;

    Key() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        if (!XML11Char.isXML11ValidQName(name)) {
            ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) name, (SyntaxTreeNode) this);
            parser.reportError(3, err);
        }
        this._name = parser.getQNameIgnoreDefaultNs(name);
        getSymbolTable().addKey(this._name, this);
        this._match = parser.parsePattern(this, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MATCH, null);
        this._use = parser.parseExpression(this, "use", null);
        if (this._name == null) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");
        } else if (this._match.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, com.sun.org.apache.xalan.internal.templates.Constants.ATTRNAME_MATCH);
        } else if (this._use.isDummy()) {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "use");
        }
    }

    public String getName() {
        return this._name.toString();
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        this._match.typeCheck(stable);
        this._useType = this._use.typeCheck(stable);
        if (!(this._useType instanceof StringType) && !(this._useType instanceof NodeSetType)) {
            this._use = new CastExpr(this._use, Type.String);
        }
        return Type.Void;
    }

    public void traverseNodeSet(ClassGenerator classGen, MethodGenerator methodGen, int buildKeyIndex) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        int getNodeValue = cpg.addInterfaceMethodref(Constants.DOM_INTF, Constants.GET_NODE_VALUE, "(I)Ljava/lang/String;");
        cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNodeIdent", Constants.GET_PARENT_SIG);
        int keyDom = cpg.addMethodref(Constants.TRANSLET_CLASS, "setKeyIndexDom", "(Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
        LocalVariableGen parentNode = methodGen.addLocalVariable("parentNode", Util.getJCRefType("I"), null, null);
        parentNode.setStart(il.append(new ISTORE(parentNode.getIndex())));
        il.append(methodGen.loadCurrentNode());
        il.append(methodGen.loadIterator());
        this._use.translate(classGen, methodGen);
        this._use.startIterator(classGen, methodGen);
        il.append(methodGen.storeIterator());
        BranchHandle nextNode = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle loop = il.append(NOP);
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, this._name.toString()));
        parentNode.setEnd(il.append(new ILOAD(parentNode.getIndex())));
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadCurrentNode());
        il.append(new INVOKEINTERFACE(getNodeValue, 2));
        il.append(new INVOKEVIRTUAL(buildKeyIndex));
        il.append(classGen.loadTranslet());
        il.append(new PUSH(cpg, getName()));
        il.append(methodGen.loadDOM());
        il.append(new INVOKEVIRTUAL(keyDom));
        nextNode.setTarget(il.append(methodGen.loadIterator()));
        il.append(methodGen.nextNode());
        il.append(DUP);
        il.append(methodGen.storeCurrentNode());
        il.append((BranchInstruction) new IFGE(loop));
        il.append(methodGen.storeIterator());
        il.append(methodGen.storeCurrentNode());
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.TopLevelElement, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        methodGen.getLocalIndex(Keywords.FUNC_CURRENT_STRING);
        int key = cpg.addMethodref(Constants.TRANSLET_CLASS, "buildKeyIndex", "(Ljava/lang/String;ILjava/lang/String;)V");
        int keyDom = cpg.addMethodref(Constants.TRANSLET_CLASS, "setKeyIndexDom", "(Ljava/lang/String;Lcom/sun/org/apache/xalan/internal/xsltc/DOM;)V");
        cpg.addInterfaceMethodref(Constants.DOM_INTF, "getNodeIdent", Constants.GET_PARENT_SIG);
        int git = cpg.addInterfaceMethodref(Constants.DOM_INTF, "getAxisIterator", "(I)Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;");
        il.append(methodGen.loadCurrentNode());
        il.append(methodGen.loadIterator());
        il.append(methodGen.loadDOM());
        il.append(new PUSH(cpg, 4));
        il.append(new INVOKEINTERFACE(git, 2));
        il.append(methodGen.loadCurrentNode());
        il.append(methodGen.setStartNode());
        il.append(methodGen.storeIterator());
        BranchHandle nextNode = il.append((BranchInstruction) new GOTO(null));
        InstructionHandle loop = il.append(NOP);
        il.append(methodGen.loadCurrentNode());
        this._match.translate(classGen, methodGen);
        this._match.synthesize(classGen, methodGen);
        BranchHandle skipNode = il.append((BranchInstruction) new IFEQ(null));
        if (this._useType instanceof NodeSetType) {
            il.append(methodGen.loadCurrentNode());
            traverseNodeSet(classGen, methodGen, key);
        } else {
            il.append(classGen.loadTranslet());
            il.append(DUP);
            il.append(new PUSH(cpg, this._name.toString()));
            il.append(DUP_X1);
            il.append(methodGen.loadCurrentNode());
            this._use.translate(classGen, methodGen);
            il.append(new INVOKEVIRTUAL(key));
            il.append(methodGen.loadDOM());
            il.append(new INVOKEVIRTUAL(keyDom));
        }
        InstructionHandle skip = il.append(NOP);
        il.append(methodGen.loadIterator());
        il.append(methodGen.nextNode());
        il.append(DUP);
        il.append(methodGen.storeCurrentNode());
        il.append((BranchInstruction) new IFGT(loop));
        il.append(methodGen.storeIterator());
        il.append(methodGen.storeCurrentNode());
        nextNode.setTarget(skip);
        skipNode.setTarget(skip);
    }
}
