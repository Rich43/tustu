package com.sun.org.apache.xalan.internal.xsltc.compiler;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.INVOKEVIRTUAL;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ClassGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.ErrorMsg;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodGenerator;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Type;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TypeCheckError;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.util.Vector;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/CallTemplate.class */
final class CallTemplate extends Instruction {
    private QName _name;
    private SyntaxTreeNode[] _parameters = null;
    private Template _calleeTemplate = null;

    CallTemplate() {
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void display(int indent) {
        indent(indent);
        System.out.print("CallTemplate");
        Util.println(" name " + ((Object) this._name));
        displayContents(indent + 4);
    }

    public boolean hasWithParams() {
        return elementCount() > 0;
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void parseContents(Parser parser) {
        String name = getAttribute("name");
        if (name.length() > 0) {
            if (!XML11Char.isXML11ValidQName(name)) {
                ErrorMsg err = new ErrorMsg("INVALID_QNAME_ERR", (Object) name, (SyntaxTreeNode) this);
                parser.reportError(3, err);
            }
            this._name = parser.getQNameIgnoreDefaultNs(name);
        } else {
            reportError(this, parser, ErrorMsg.REQUIRED_ATTR_ERR, "name");
        }
        parseChildren(parser);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
        Template template = stable.lookupTemplate(this._name);
        if (template != null) {
            typeCheckContents(stable);
            return Type.Void;
        }
        ErrorMsg err = new ErrorMsg(ErrorMsg.TEMPLATE_UNDEF_ERR, (Object) this._name, (SyntaxTreeNode) this);
        throw new TypeCheckError(err);
    }

    @Override // com.sun.org.apache.xalan.internal.xsltc.compiler.Instruction, com.sun.org.apache.xalan.internal.xsltc.compiler.SyntaxTreeNode
    public void translate(ClassGenerator classGen, MethodGenerator methodGen) {
        Stylesheet stylesheet = classGen.getStylesheet();
        ConstantPoolGen cpg = classGen.getConstantPool();
        InstructionList il = methodGen.getInstructionList();
        if (stylesheet.hasLocalParams() || hasContents()) {
            this._calleeTemplate = getCalleeTemplate();
            if (this._calleeTemplate != null) {
                buildParameterList();
            } else {
                int push = cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.PUSH_PARAM_FRAME, "()V");
                il.append(classGen.loadTranslet());
                il.append(new INVOKEVIRTUAL(push));
                translateContents(classGen, methodGen);
            }
        }
        String className = stylesheet.getClassName();
        String methodName = Util.escape(this._name.toString());
        il.append(classGen.loadTranslet());
        il.append(methodGen.loadDOM());
        il.append(methodGen.loadIterator());
        il.append(methodGen.loadHandler());
        il.append(methodGen.loadCurrentNode());
        StringBuffer methodSig = new StringBuffer("(Lcom/sun/org/apache/xalan/internal/xsltc/DOM;Lcom/sun/org/apache/xml/internal/dtm/DTMAxisIterator;Lcom/sun/org/apache/xml/internal/serializer/SerializationHandler;I");
        if (this._calleeTemplate != null) {
            int numParams = this._parameters.length;
            for (int i2 = 0; i2 < numParams; i2++) {
                SyntaxTreeNode node = this._parameters[i2];
                methodSig.append(Constants.OBJECT_SIG);
                if (node instanceof Param) {
                    il.append(ACONST_NULL);
                } else {
                    node.translate(classGen, methodGen);
                }
            }
        }
        methodSig.append(")V");
        il.append(new INVOKEVIRTUAL(cpg.addMethodref(className, methodName, methodSig.toString())));
        if (this._parameters != null) {
            for (int i3 = 0; i3 < this._parameters.length; i3++) {
                if (this._parameters[i3] instanceof WithParam) {
                    ((WithParam) this._parameters[i3]).releaseResultTree(classGen, methodGen);
                }
            }
        }
        if (this._calleeTemplate == null) {
            if (stylesheet.hasLocalParams() || hasContents()) {
                int pop = cpg.addMethodref(Constants.TRANSLET_CLASS, Constants.POP_PARAM_FRAME, "()V");
                il.append(classGen.loadTranslet());
                il.append(new INVOKEVIRTUAL(pop));
            }
        }
    }

    public Template getCalleeTemplate() {
        Template foundTemplate = getXSLTC().getParser().getSymbolTable().lookupTemplate(this._name);
        if (foundTemplate.isSimpleNamedTemplate()) {
            return foundTemplate;
        }
        return null;
    }

    private void buildParameterList() {
        Vector<Param> defaultParams = this._calleeTemplate.getParameters();
        int numParams = defaultParams.size();
        this._parameters = new SyntaxTreeNode[numParams];
        for (int i2 = 0; i2 < numParams; i2++) {
            this._parameters[i2] = defaultParams.elementAt(i2);
        }
        int count = elementCount();
        for (int i3 = 0; i3 < count; i3++) {
            Object node = elementAt(i3);
            if (node instanceof WithParam) {
                WithParam withParam = (WithParam) node;
                QName name = withParam.getName();
                int k2 = 0;
                while (true) {
                    if (k2 < numParams) {
                        SyntaxTreeNode parm = this._parameters[k2];
                        if ((parm instanceof Param) && ((Param) parm).getName().equals(name)) {
                            withParam.setDoParameterOptimization(true);
                            this._parameters[k2] = withParam;
                            break;
                        } else if (!(parm instanceof WithParam) || !((WithParam) parm).getName().equals(name)) {
                            k2++;
                        } else {
                            withParam.setDoParameterOptimization(true);
                            this._parameters[k2] = withParam;
                            break;
                        }
                    }
                }
            }
        }
    }
}
